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
using AutoMapper;
using System.Collections.Generic;
using transporterService.Utils;

namespace transporterService.Controllers
{
    public class InviteItemController : TableController<InviteItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<InviteItemDTO, InviteItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/InviteItem
        public IQueryable<InviteItemDTO> GetAllInviteItem()
        {
            return Query();
        }

        // GET tables/InviteItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<InviteItemDTO> GetInviteItem(string id)
        {
            InviteItem query = context.InviteItems.FirstOrDefault(a => a.Id == id);
            InviteItemDTO resp = new InviteItemDTO();
            resp = Mapper.Map<InviteItem, InviteItemDTO>(query, resp);

            List<InviteItemDTO> list = new List<InviteItemDTO> {
                resp
            };
            return SingleResult<InviteItemDTO>.Create(list.AsQueryable());
            //return Lookup(id);
        }

        // PATCH tables/InviteItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<InviteItemDTO> PatchInviteItem(string id, Delta<InviteItemDTO> patch)
        {
            return UpdateAsync(id, patch);
        }

        // POST tables/InviteItem
        public async Task<IHttpActionResult> PostInviteItem(InviteItemDTO item)
        {
            if (item.Id != null)
            {
                if (item.Id == "")
                {
                    item.Id = Guid.NewGuid().ToString();
                }
                string code = item.Id.Replace("-", "");
                code = code.Substring(0, 4);
                item.InviteCode = code;

                UserItem user = this.context.UserItems.Where(a => a.Id == item.HostUserId).FirstOrDefault();
                string userStr = "pela nossa equipe";
                if (user != null)
                {
                    userStr = "por " + user.Name + " email: " + user.Email;
                }

                string content = String.Format(SendGridUtils.inviteMessage, userStr, code);
                SendGridUtils.EmailUser eu = new SendGridUtils.EmailUser(item.GuestUserEmail, SendGridUtils.inviteSubject, content);
                SendGridUtils sgu = new SendGridUtils();
                sgu.send(eu);
            }
            InviteItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/InviteItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteInviteItem(string id)
        {
            return DeleteAsync(id);
        }

    }
}