using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.WindowsAzure.Mobile.Service;
using transporterService.DataTranspObjects;
using transporterService.Models;
using transporterService.Extensions;
using transporterService.DataObjects;
using AutoMapper;
using AutoMapper.QueryableExtensions;
using System.Collections.Generic;
using System;
using transporterService.Utils;

namespace transporterService.Controllers
{
    public class OrderStatusItemController : TableController<OrderStatusItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<OrderStatusItemDTO, OrderStatusItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/OrderStatusItem ImageItems
        [QueryableExpand("StatusItem"), QueryableExpand("OrderItem"), QueryableExpand("OrderItem/UserItem"), QueryableExpand("ImageItems")]
        public IQueryable<OrderStatusItemDTO> GetAllOrderStatusItemDTO(string OrderId)
        {
            if (OrderId != null && OrderId != "" && OrderId != "0")
            {
                var q = context.OrderStatusItems.Where(a => a.OrderItem.Id == OrderId).ToList();
                List<OrderStatusItemDTO> list = new List<OrderStatusItemDTO>();
                foreach (var item in q)
                {
                    OrderStatusItemDTO iDto = new OrderStatusItemDTO();
                    iDto = Mapper.Map<OrderStatusItem, OrderStatusItemDTO>(item);
                    list.Add(iDto);
                }
                return list.AsQueryable<OrderStatusItemDTO>();
            }
            else
            {
                return context.OrderStatusItems.Project().To<OrderStatusItemDTO>();
            }
            //return Query(); 
        }

        // GET tables/OrderStatusItem/48D68C86-6EA6-4C25-AA33-223FC9A27959     , QueryableExpand("OrderItem/RouteItem")
        [Queryable(MaxExpansionDepth = 4), QueryableExpand("StatusItem"), QueryableExpand("OrderItem/UserItem"), QueryableExpand("OrderItem/ProductDetailItems"), QueryableExpand("OrderItem/RouteItem/User"), QueryableExpand("ImageItems")]
        public SingleResult<OrderStatusItemDTO> GetOrderStatusItemDTO(string id)
        {
            if (id != null && id != "" && id != "0")
            {
                var query = context.OrderStatusItems.Where(a => a.OrderItem.Id == id && a.IsActual).FirstOrDefault();
                if (query != null)
                {
                    OrderStatusItemDTO item = new OrderStatusItemDTO();
                    item = Mapper.Map<OrderStatusItem, OrderStatusItemDTO>(query);

                    return SingleResult<OrderStatusItemDTO>.Create((new List<OrderStatusItemDTO>() { item }).AsQueryable());
                }
                return null;
            } else
            {
                return null;
            }
            //return Lookup(id);
        }

        // PATCH tables/OrderStatusItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public async Task<OrderStatusItemDTO> PatchOrderStatusItemDTO(string id, Delta<OrderStatusItemDTO> patch)
        {
            OrderStatusItemDTO actual = patch.GetEntity();
            OrderStatusItem current = context.OrderStatusItems.Where(a => a.Id == actual.Id).FirstOrDefault();
            OrderStatusItem newCurrent = new OrderStatusItem();
            if (current != null)
            {
                if (current.IsActual)
                {
                    current.IsActual = false;
                }
            }
            newCurrent = Mapper.Map<OrderStatusItemDTO, OrderStatusItem>(actual, newCurrent);

            newCurrent.Id = Guid.NewGuid().ToString();
            newCurrent.IsActual = true;
            newCurrent.OrderItem = context.OrderItems.Where(a => a.Id == actual.OrderItem.Id).FirstOrDefault();
            newCurrent.StatusItem = context.StatusDescriptionItems.Where(a => a.Type == actual.StatusItem.Type).FirstOrDefault();

            // Email with status of order
            if (newCurrent.StatusItem.UserType == 1) // 1 = transportador enviando status para quem envia
            {
                if (newCurrent.StatusItem.Type == 3 
                    || newCurrent.StatusItem.Type == 4
                    || newCurrent.StatusItem.Type == 5
                    || newCurrent.StatusItem.Type == 6
                    || newCurrent.StatusItem.Type == 7
                    || newCurrent.StatusItem.Type == 9)
                {
                    string subject = String.Format(SendGridUtils.updateStatusSubject, newCurrent.StatusItem.Name);

                    UserItem userTransporter = newCurrent.OrderItem.RouteItem.User;
                    string message = String.Format(SendGridUtils.updateStatusMessage, userTransporter.Name, newCurrent.StatusItem.Name, userTransporter.Email);

                    SendGridUtils.EmailUser eu = new SendGridUtils.EmailUser(newCurrent.OrderItem.UserItem.Email, subject, message);


                    if (newCurrent.ImageItems != null)
                    {
                        BlobUtils blob = new BlobUtils();
                        foreach (var item in newCurrent.ImageItems)
                        {
                            if (!(item.ImageUrl.EndsWith(".jpg", System.StringComparison.CurrentCultureIgnoreCase)))
                            {
                                item.ImageUrl += ".jpg";
                            }

                            var content = blob.getBlobStream(item.ImageUrl);

                            if (content != null)
                            {
                                var attach = new SendGridUtils.EmailUser.Attachment();
                                attach.StreamContent = content;
                                attach.Name = item.Id;
                                
                                eu.Attachments.Add(attach);
                                //blob.getBlobStream(item.ImageUrl);
                            }
                        }
                    }
                    SendGridUtils sgu = new SendGridUtils();
                    sgu.send(eu);
                }
            }

            context.OrderStatusItems.Add(newCurrent);
            await this.context.SaveChangesAsync();

            //Convert to client type before returning the result
            var result = Mapper.Map<OrderStatusItem, OrderStatusItemDTO>(newCurrent);
            return result;
            //return UpdateAsync(id, patch);
        }

        

            //Convert to client type before returning the result


        //// POST tables/OrderStatusItem
        //public async Task<IHttpActionResult> PostOrderStatusItemDTO(OrderStatusItemDTO item)
        //{
        //    OrderStatusItemDTO current = await InsertAsync(item);
        //    return CreatedAtRoute("Tables", new { id = current.Id }, current);
        //}

        //// DELETE tables/OrderStatusItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task DeleteOrderStatusItemDTO(string id)
        //{
        //     return DeleteAsync(id);
        //}

    }
}