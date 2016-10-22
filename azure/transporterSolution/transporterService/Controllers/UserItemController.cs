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
using AutoMapper.QueryableExtensions;
using transporterService.Utils;

namespace transporterService.Controllers
{
    public class UserItemController : TableController<UserItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<UserItemDTO, UserItem>
                                (context, Request, Services, userItem => userItem.Id);
        }

        // GET tables/UserItem
        //public IQueryable<UserItemDTO> GetAllUserItem()
        //{
        //    return Query(); 
        //}

        [QueryableExpand("UserFacebookItem")]
        public IQueryable<UserItemDTO> GetAllUserItem(string email, string pass, string facebookToken)
        {
            var query = context.UserItems.ToList();

            if (facebookToken != "null") {
                var queryFacebook = context.UserFacebookItems.Where(a => a.Token == facebookToken).FirstOrDefault();

                if (queryFacebook != null) {
                    query = query.Where(a => a.UserFacebookItem != null ? a.UserFacebookItem.Id == queryFacebook.Id && a.UserFacebookItem.Email == email : false).ToList();
                } else
                {
                    return null;
                }
            } else if (email != "null" && pass != "null")
            {
                query = query.Where(a => a.Email == email && a.Password == pass).ToList();
            } else {
                return null;
            }
            List<UserItemDTO> list = new List<UserItemDTO>();
            if (query.Count()> 0)
            {
                foreach (var q in query)
                {
                    UserItemDTO item = new UserItemDTO();
                    item.Id = q.Id;
                    item.UserFacebookItem = new UserFacebookItemDTO();
                    item = Mapper.Map<UserItem, UserItemDTO>(q, item);
                    list.Add(item);
                }
            }
            return list.AsQueryable();

                //.Project().To<UserItemDTO>(); ;
                //return Query();
        }

        [QueryableExpand("UserFacebookItem")]
        public IQueryable<UserItemDTO> GetAllUserItem()
        {
            var query = context.UserItems.ToList();//.Project().To<UserItemDTO>();

            List<UserItemDTO> list = new List<UserItemDTO>();
            foreach (var q in query)
            {
                UserItemDTO item = new UserItemDTO();
                item = Mapper.Map<UserItem, UserItemDTO>(q, item);
                list.Add(item);
            }
            return list.AsQueryable();

            //return Query();
        }

        // GET tables/UserItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<UserItemDTO> GetUserItem(string id)
        {
            UserItem query = context.UserItems.FirstOrDefault(a => a.Id == id);
            UserItemDTO resp = new UserItemDTO();
            resp = Mapper.Map<UserItem, UserItemDTO>(query, resp);

            List<UserItemDTO> list = new List<UserItemDTO> {
                resp
            };
            return SingleResult<UserItemDTO>.Create(list.AsQueryable());
        }

        // PATCH tables/UserItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<UserItemDTO> PatchUserItem(string id, Delta<UserItemDTO> patch)
        {
             return UpdateAsync(id, patch);
        }

        // POST tables/UserItem
        public async Task<IHttpActionResult> PostUserItem(UserItemDTO item)
        {
            if (item.Id != null)
            {
                if (item.Id == "")
                {
                    item.Id = Guid.NewGuid().ToString();
                }
                
            }
            if (item.UserFacebookItem != null)
            {
                item.UserFacebookItem.Id = Guid.NewGuid().ToString();
                item.UserFacebookItem.DateTime = DateTime.Now.ToString();
            }
            UserItemDTO current = await InsertAsync(item);

            InviteUserItem iuItem = this.context.InviteUserItems.Where(a => a.HostUserId == item.Id).FirstOrDefault();
            if (iuItem == null)
            {
                UserItem newUser = this.context.UserItems.Where(a => a.Id == item.Id).FirstOrDefault();
                if (newUser != null)
                {
                    iuItem = new InviteUserItem();
                    iuItem.Id = Guid.NewGuid().ToString();
                    iuItem.AvailableInvites = 30;
                    iuItem.Invitations = 0;
                    iuItem.HostUserId = newUser.Id;
                    this.context.InviteUserItems.Add(iuItem);
                    this.context.SaveChanges();
                }
            }
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/UserItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteUserItem(string id)
        {
             return DeleteAsync(id);
        }

    }
}