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
using AutoMapper.QueryableExtensions;
using System;
using AutoMapper;
using System.Collections.Generic;

namespace transporterService.Controllers
{
    public class UserRatingItemController : TableController<UserRatingItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<UserRatingItemDTO, UserRatingItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/UserRatingItem
        [QueryableExpand("UserDestin"), QueryableExpand("UserSource")]
        public IQueryable<UserRatingItemDTO> GetAllUserRatingItemDTO(string UserId)
        { 
            if (UserId != null && UserId != "" && UserId != "0")
            {
                var query = context.UserRatingItems.Include("UserDestin").Include("UserSource").Where(a => a.UserDestin.Id == UserId).ToList();
                List<UserRatingItemDTO> list = new List<UserRatingItemDTO>();
                foreach (var q in query)
                {
                    UserRatingItemDTO item = new UserRatingItemDTO();
                    item.UserDestin = new UserItemDTO();
                    if (q.UserDestin != null)
                    {
                        item.UserDestin.Id = q.UserDestin.Id;
                        item.UserDestin.Name = q.UserDestin.Name;
                        item.UserDestin.Email = q.UserDestin.Email;
                    }
                    item.UserSource = new UserItemDTO();
                    if (q.UserSource != null)
                    {
                        item.UserSource.Id = q.UserSource.Id;
                        item.UserSource.Name = q.UserSource.Name;
                        item.UserSource.Email = q.UserSource.Email;
                    }
                    //item.OrderItem = new OrderItemDTO();
                    //item.OrderItem = Mapper.Map<OrderItem, OrderItemDTO>(q.OrderItem, item.OrderItem);

                    item.Id = q.Id;
                    item.Comments = q.Comments;
                    item.DateTime = q.DateTime;
                    item.Value = q.Value;

                    list.Add(item);
                }
                    //.Project().To<UserRatingItemDTO>();
                return list.AsQueryable();
            }
            else
            {
                return null;
            }
            //return Query(); 
        }

        // GET tables/UserRatingItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public SingleResult<UserRatingItemDTO> GetUserRatingItemDTO(string id)
        //{
        //    return Lookup(id);
        //}

        //// PATCH tables/UserRatingItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task<UserRatingItemDTO> PatchUserRatingItemDTO(string id, Delta<UserRatingItemDTO> patch)
        //{
        //     return UpdateAsync(id, patch);
        //}

        // POST tables/UserRatingItem
        public async Task<IHttpActionResult> PostUserRatingItemDTO(UserRatingItemDTO item)
        {
            UserRatingItem toAdd = new UserRatingItem();
            toAdd = Mapper.Map<UserRatingItemDTO, UserRatingItem>(item, toAdd);

            toAdd.Id = Guid.NewGuid().ToString();
                //item.UserDestin = context.UserItems.Where(a => a.Id == item.UserDestin.Id);
            
            toAdd.OrderItem = this.context.OrderItems.Include("RouteItem").Where(a => a.Id == item.OrderItem.Id).FirstOrDefault();
            toAdd.UserSource = this.context.UserItems.Where(a => a.Id == item.UserSource.Id).FirstOrDefault();
            toAdd.UserDestin = this.context.UserItems.Where(a => a.Id == item.UserDestin.Id).FirstOrDefault();

            this.context.UserRatingItems.Add(toAdd);
            await context.SaveChangesAsync();
            //UserRatingItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = toAdd.Id }, toAdd);
        }

        //// DELETE tables/UserRatingItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task DeleteUserRatingItemDTO(string id)
        //{
        //     return DeleteAsync(id);
        //}

    }
}