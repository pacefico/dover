using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.WindowsAzure.Mobile.Service;
using transporterService.DataObjects;
using transporterService.Models;
using transporterService.Extensions;
using transporterService.DataTranspObjects;
using System.Collections.Generic;
using AutoMapper;
using System;

namespace transporterService.Controllers
{
    public class InviteUserItemController : TableController<InviteUserItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
           
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<InviteUserItemDTO, InviteUserItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/InviteUserItem
        public IQueryable<InviteUserItemDTO> GetAllInviteUserItem()
        {
            return Query();
        }

        // GET tables/InviteUserItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<InviteUserItemDTO> GetInviteUserItem(string id)
        {
            InviteUserItem query = context.InviteUserItems.FirstOrDefault(a => a.Id == id);
            InviteUserItemDTO resp = new InviteUserItemDTO();
            resp = Mapper.Map<InviteUserItem, InviteUserItemDTO>(query, resp);

            List<InviteUserItemDTO> list = new List<InviteUserItemDTO> {
                resp
            };
            return SingleResult<InviteUserItemDTO>.Create(list.AsQueryable());
        }

        // PATCH tables/InviteUserItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<InviteUserItemDTO> PatchInviteUserItem(string id, Delta<InviteUserItemDTO> patch)
        {
            return UpdateAsync(id, patch);
        }

        // POST tables/InviteUserItem
        public async Task<IHttpActionResult> PostInviteUserItem(InviteUserItemDTO item)
        {
            if (item.Id != null)
            {
                if (item.Id == "")
                {
                    item.Id = Guid.NewGuid().ToString();
                }
            }
            InviteUserItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/InviteUserItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteInviteUserItem(string id)
        {
            return DeleteAsync(id);
        }

    }
}