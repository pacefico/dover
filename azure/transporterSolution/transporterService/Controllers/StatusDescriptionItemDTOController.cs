using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.WindowsAzure.Mobile.Service;
using transporterService.DataTranspObjects;
using transporterService.Models;
using AutoMapper;
using transporterService.Extensions;
using transporterService.DataObjects;

namespace transporterService.Controllers
{
    public class StatusDescriptionItemController : TableController<StatusDescriptionItemDTO>
    {
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            transporterContext context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<StatusDescriptionItemDTO, StatusDescriptionItem>
                               (context, Request, Services, statusDescriptionItem => statusDescriptionItem.Id);
        }

        // GET tables/StatusDescriptionItemDTO
        public IQueryable<StatusDescriptionItemDTO> GetAllStatusDescriptionItemDTO()
        {
            return Query(); 
        }

        // GET tables/StatusDescriptionItemDTO/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<StatusDescriptionItemDTO> GetStatusDescriptionItemDTO(string id)
        {
            return Lookup(id);
        }

        // PATCH tables/StatusDescriptionItemDTO/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<StatusDescriptionItemDTO> PatchStatusDescriptionItemDTO(string id, Delta<StatusDescriptionItemDTO> patch)
        {
             return UpdateAsync(id, patch);
        }

        // POST tables/StatusDescriptionItemDTO
        public async Task<IHttpActionResult> PostStatusDescriptionItemDTO(StatusDescriptionItemDTO item)
        {
            StatusDescriptionItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/StatusDescriptionItemDTO/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteStatusDescriptionItemDTO(string id)
        {
             return DeleteAsync(id);
        }

    }
}