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
using System;
using System.Collections.Generic;
using AutoMapper;
using AutoMapper.QueryableExtensions;

namespace transporterService.Controllers
{
    public class ItineraryItemController : TableController<ItineraryItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<ItineraryItemDTO, ItineraryItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/ItineraryItem
        [QueryableExpand("RouteItem")]
        public IQueryable<ItineraryItemDTO> GetAllItineraryItem(string RouteId)
        {
            if (RouteId != null && RouteId != "" && RouteId != "0")
            {
                var query = context.ItineraryItems.Include("RouteItem").Where(a => a.RouteItem.Id == RouteId).Project().To<ItineraryItemDTO>();
                return query;
            }
            else
            {
                return null;
            }
            //return Query();
        }

        // GET tables/ItineraryItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        [QueryableExpand("FacebookItem")]
        public SingleResult<ItineraryItemDTO> GetItineraryItem(string id)
        {
            ItineraryItem query = context.ItineraryItems.FirstOrDefault(a => a.Id == id);
            ItineraryItemDTO resp = new ItineraryItemDTO();
            resp = Mapper.Map<ItineraryItem, ItineraryItemDTO>(query, resp);

            List<ItineraryItemDTO> list = new List<ItineraryItemDTO> {
                resp
            };
            return SingleResult<ItineraryItemDTO>.Create(list.AsQueryable());
            //return Lookup(id);
        }

        // PATCH tables/ItineraryItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<ItineraryItemDTO> PatchItineraryItem(string id, Delta<ItineraryItemDTO> patch)
        {
            return UpdateAsync(id, patch);
        }

        // POST tables/ItineraryItem
        public async Task<IHttpActionResult> PostItineraryItem(ItineraryItemDTO item)
        {
            if (item.Id != null)
            {
                if (item.Id == "")
                {
                    item.Id = Guid.NewGuid().ToString();
                }
            }

            ItineraryItem toPost = new ItineraryItem();
            toPost = Mapper.Map<ItineraryItemDTO, ItineraryItem>(item, toPost);
            toPost.RouteItem = this.context.RouteItems.FirstOrDefault(a => a.Id == item.RouteItem.Id);

            this.context.ItineraryItems.Add(toPost);
            await this.context.SaveChangesAsync();

            //ItineraryItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = toPost.Id }, toPost);
        }

        // DELETE tables/ItineraryItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteItineraryItem(string id)
        {
            return DeleteAsync(id);
        }

    }
}