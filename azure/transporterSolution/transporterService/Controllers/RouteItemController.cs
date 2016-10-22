using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.WindowsAzure.Mobile.Service;
using transporterService.DataObjects;
using transporterService.Models;
using transporterService.DataTranspObjects;
using transporterService.Extensions;
using System.Collections.Generic;
using AutoMapper;
using System;
using AutoMapper.QueryableExtensions;

namespace transporterService.Controllers
{
    public class RouteItemController : TableController<RouteItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<RouteItemDTO, RouteItem>
                                (context, Request, Services, routeItem => routeItem.Id);
        }

        // GET tables/RouteItem
        [QueryableExpand("From"), QueryableExpand("To"), QueryableExpand("User")]
        public IQueryable<RouteItemDTO> GetAllRouteItem(string UserId)
        {
            if (UserId != null && UserId != "" && UserId != "0")
            {
                var query = context.RouteItems.Include("User").Where(a => a.User.Id == UserId).Project().To<RouteItemDTO>();
                return query;
            } else
            {
                return null;
            }
        }

        // GET tables/RouteItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        [QueryableExpand("From"), QueryableExpand("To"), QueryableExpand("User")]
        public SingleResult<RouteItemDTO> GetRouteItem(string id)
        {
            RouteItem query = context.RouteItems.FirstOrDefault(a => a.Id == id);

            RouteItemDTO resp = new RouteItemDTO();
            if (query != null)
            {
                resp = Mapper.Map<RouteItem, RouteItemDTO>(query, resp);
            }
            else
            {
                return null;
            }
            List<RouteItemDTO> list = new List<RouteItemDTO> {
                resp
            };
            return SingleResult<RouteItemDTO>.Create(list.AsQueryable());
        }

        // PATCH tables/RouteItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public async Task<RouteItemDTO> PatchRouteItem(string id, Delta<RouteItemDTO> patch)
        {
            //Read TodoItem to update from database so that EntityFramework updates
            //existing entry
            RouteItem currentRouteItem = this.context.RouteItems.FirstOrDefault(j => (j.Id == id));
            RouteItemDTO toUpdateRouteItem = patch.GetEntity();

            bool containsFrom = patch.GetChangedPropertyNames().Contains("From");
            bool containsTo = patch.GetChangedPropertyNames().Contains("To");
            bool containsUser = patch.GetChangedPropertyNames().Contains("User");

            //UserItem userCurrent = this.context.UserItems.FirstOrDefault(a => a.Id == currentRouteItem.User.Id);

            LatLnItem toCurrent = null;
            if (containsTo) {
                toCurrent = Mapper.Map<LatLnItemDTO, LatLnItem>(toUpdateRouteItem.To, toCurrent);
            } else {
                toCurrent = this.context.LatLnItems.FirstOrDefault(a => a.Id == currentRouteItem.To.Id);
            }
            if (toCurrent != null) {
                toUpdateRouteItem.To = Mapper.Map<LatLnItem, LatLnItemDTO>(toCurrent, toUpdateRouteItem.To);
            }

            LatLnItem fromCurrent = null;
            if (containsFrom)            {
                fromCurrent = Mapper.Map<LatLnItemDTO, LatLnItem>(toUpdateRouteItem.From, fromCurrent);
            }else            {
                fromCurrent = this.context.LatLnItems.FirstOrDefault(a => a.Id == currentRouteItem.From.Id);
            }
            if (fromCurrent != null)
            {
                toUpdateRouteItem.From = Mapper.Map<LatLnItem, LatLnItemDTO>(fromCurrent, toUpdateRouteItem.From);
            }
            UserItem userCurrent = this.context.UserItems.FirstOrDefault(a => a.Id == currentRouteItem.User.Id);
            toUpdateRouteItem.User = Mapper.Map<UserItem, UserItemDTO>(userCurrent, toUpdateRouteItem.User);

            currentRouteItem = Mapper.Map<RouteItemDTO, RouteItem>(toUpdateRouteItem, currentRouteItem);

            await this.context.SaveChangesAsync();

            //Convert to client type before returning the result
            var result = Mapper.Map<RouteItem, RouteItemDTO>(currentRouteItem);
            return result;
            //return UpdateAsync(id, patch);
        }

        // POST tables/RouteItem
        public async Task<IHttpActionResult> PostRouteItem(RouteItemDTO item)
        {
            RouteItem routeToPost = new RouteItem();
            routeToPost = Mapper.Map<RouteItemDTO, RouteItem>(item, routeToPost);
            routeToPost.User = this.context.UserItems.FirstOrDefault(a => a.Id == item.User.Id);

            if (routeToPost.Id == "") {
                routeToPost.Id = Guid.NewGuid().ToString();
            } 
            if (routeToPost.From != null)
            {
                if (routeToPost.From.Id == "")
                {
                    routeToPost.From.Id = Guid.NewGuid().ToString();
                    this.context.LatLnItems.Add(routeToPost.From);
                }
            }
            if (routeToPost.To != null)
            {
                if (routeToPost.To.Id == "")
                {
                    routeToPost.To.Id = Guid.NewGuid().ToString();
                    this.context.LatLnItems.Add(routeToPost.To);
                }
            }

                this.context.RouteItems.Add(routeToPost);
                await this.context.SaveChangesAsync();

            //RouteItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = routeToPost.Id }, routeToPost);
        }

        // DELETE tables/RouteItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteRouteItem(string id)
        {
            RouteItem routeToDelete = this.context.RouteItems.FirstOrDefault(a => a.Id == id);

            OrderItem orderItem = this.context.OrderItems.FirstOrDefault(a => a.RouteItem.Id == id);

            if (orderItem == null) { 
                if (routeToDelete != null) {
                    LatLnItem fromToDelete = this.context.LatLnItems.FirstOrDefault(a => a.Id == routeToDelete.From.Id);
                    if (fromToDelete != null)
                    {
                        this.context.LatLnItems.Remove(fromToDelete);
                    }
                    LatLnItem toToDelete = this.context.LatLnItems.FirstOrDefault(a => a.Id == routeToDelete.To.Id);
                    if (fromToDelete != null)
                    {
                        this.context.LatLnItems.Remove(toToDelete);
                    }

                    var itineraries = context.ItineraryItems.Where(a => a.RouteItem.Id == routeToDelete.Id);
                    foreach( var it in itineraries)
                    {
                        this.context.ItineraryItems.Remove(it);
                    }
                }
                this.context.SaveChanges();
                return DeleteAsync(id);
            }
            return null;
        }

    }
}