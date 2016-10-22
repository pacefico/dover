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

namespace transporterService.Controllers
{
    public class ServiceTypeItemController : TableController<ServiceTypeItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<ServiceTypeItemDTO, ServiceTypeItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/ServiceTypeItem
        public IQueryable<ServiceTypeItemDTO> GetAllServiceTypeItemDTO()
        {
            return Query(); 
        }

        // GET tables/ServiceTypeItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public SingleResult<ServiceTypeItemDTO> GetServiceTypeItemDTO(string id)
        //{
        //    return Lookup(id);
        //}

        // PATCH tables/ServiceTypeItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task<ServiceTypeItemDTO> PatchServiceTypeItemDTO(string id, Delta<ServiceTypeItemDTO> patch)
        //{
        //     return UpdateAsync(id, patch);
        //}

        // POST tables/ServiceTypeItem
        //public async Task<IHttpActionResult> PostServiceTypeItemDTO(ServiceTypeItemDTO item)
        //{
        //    ServiceTypeItemDTO current = await InsertAsync(item);
        //    return CreatedAtRoute("Tables", new { id = current.Id }, current);
        //}

        // DELETE tables/ServiceTypeItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task DeleteServiceTypeItemDTO(string id)
        //{
        //     return DeleteAsync(id);
        //}

    }
}