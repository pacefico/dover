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
using System.Collections.Generic;
using AutoMapper;
using System;
using AutoMapper.QueryableExtensions;

namespace transporterService.Controllers
{
    public class OrderItemController : TableController<OrderItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<OrderItemDTO, OrderItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/OrderItem
        [QueryableExpand("RouteItem"), QueryableExpand("ProductDetailItems"), QueryableExpand("UserItem"), QueryableExpand("RouteItem/From"), QueryableExpand("RouteItem/To"), QueryableExpand("RouteItem/User")]
        public IQueryable<OrderItemDTO> GetAllOrderItemDTO(string UserId)
        {
            if (UserId != null && UserId != "" && UserId != "0")
            {
                var query = context.OrderItems.Where(a => a.RouteItem.User.Id == UserId || a.UserItem.Id == UserId).ToList(); //.Project().To<OrderItemDTO>();
                List<OrderItemDTO> list = new List<OrderItemDTO>();
                foreach(var q in query)
                {
                    OrderItemDTO item = new OrderItemDTO();

                    item.RouteItem = new RouteItemDTO();
                    item.RouteItem = Mapper.Map<RouteItem, RouteItemDTO>(q.RouteItem, item.RouteItem);

                    item.UserItem = new UserItemDTO();
                    item.UserItem = Mapper.Map<UserItem, UserItemDTO>(q.UserItem, item.UserItem);

                    item.Comments = q.Comments;
                    item.Id = q.Id;
                    item.DateTime = q.DateTime;
                    item.ProductDetailItems = new List<ProductDetailItemDTO>();

                    foreach (var prod in q.ProductDetailItems)
                    {
                        ProductDetailItemDTO pdi = new ProductDetailItemDTO();
                        pdi = Mapper.Map<ProductDetailItem, ProductDetailItemDTO>(prod, pdi);
                        item.ProductDetailItems.Add(pdi);
                    }

                    //item = Mapper.Map<OrderItem, OrderItemDTO>(q, item);
                    list.Add(item);
                }

                return list.AsQueryable();
            }
            else
            {
                return null;
            }
            
        }

        //// GET tables/OrderItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        [QueryableExpand("RouteItem"), QueryableExpand("UserItem"), QueryableExpand("ProductDetailItems"), QueryableExpand("ProductDetailItems/ImageItems")]
        public SingleResult<OrderItemDTO> GetOrderItemDTO(string id)
        {
            OrderItem q = context.OrderItems.FirstOrDefault(a => a.Id == id);

            OrderItemDTO resp = new OrderItemDTO();
            if (q != null)
            {
                resp.RouteItem = new RouteItemDTO();
                resp.RouteItem = Mapper.Map<RouteItem, RouteItemDTO>(q.RouteItem, resp.RouteItem);

                resp.UserItem = new UserItemDTO();
                resp.UserItem = Mapper.Map<UserItem, UserItemDTO>(q.UserItem, resp.UserItem);

                resp.Comments = q.Comments;
                resp.Id = q.Id;
                resp.DateTime = q.DateTime;
                resp.ProductDetailItems = new List<ProductDetailItemDTO>();

                foreach (var prod in q.ProductDetailItems)
                {
                    ProductDetailItemDTO pdi = new ProductDetailItemDTO();
                    pdi = Mapper.Map<ProductDetailItem, ProductDetailItemDTO>(prod, pdi);
                    resp.ProductDetailItems.Add(pdi);
                }
            }
            else
            {
                return null;
            }
            List<OrderItemDTO> list = new List<OrderItemDTO> {
                resp
            };
            return SingleResult<OrderItemDTO>.Create(list.AsQueryable());
            //return Lookup(id);
        }

        //// PATCH tables/OrderItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task<OrderItemDTO> PatchOrderItemDTO(string id, Delta<OrderItemDTO> patch)
        //{
        //     return UpdateAsync(id, patch);
        //}

        // POST tables/OrderItem
        public async Task<IHttpActionResult> PostOrderItemDTO(OrderItemDTO item)
        {
            OrderItem current = new OrderItem();
            current = Mapper.Map<OrderItemDTO, OrderItem>(item, current);
            current.Id = Guid.NewGuid().ToString();
            current.DateTime = DateTime.Now.ToString();

            current.UserItem = this.context.UserItems.Where(a => a.Id == current.UserItem.Id).FirstOrDefault();
            current.RouteItem = this.context.RouteItems.Where(a => a.Id == current.RouteItem.Id).FirstOrDefault();
            foreach ( var prod in current.ProductDetailItems)
            {
                if (prod.Id.Trim() == "")
                {
                    prod.Id = Guid.NewGuid().ToString();
                }
            }

            this.context.OrderItems.Add(current);
            
            OrderStatusItem orderStatus = new OrderStatusItem();
            orderStatus.Id = Guid.NewGuid().ToString();
            orderStatus.IsActual = true;
            orderStatus.OrderItem = current;
            orderStatus.DateTime = DateTime.Now.ToString();
            orderStatus.StatusItem = this.context.StatusDescriptionItems.FirstOrDefault(a => a.Type == 1);
           

            this.context.OrderStatusItems.Add(orderStatus);
            await this.context.SaveChangesAsync();

            //OrderItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/OrderItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteOrderItemDTO(string id)
        {
            return DeleteAsync(id);
        }

    }
}