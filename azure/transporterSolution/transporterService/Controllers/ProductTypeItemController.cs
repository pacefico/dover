using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.WindowsAzure.Mobile.Service;
using transporterService.DataTranspObjects;
using transporterService.Models;
using transporterService.DataObjects;
using transporterService.Extensions;

namespace transporterService.Controllers
{
    public class ProductTypeItemController : TableController<ProductTypeItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<ProductTypeItemDTO, ProductTypeItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/ProductTypeItem
        public IQueryable<ProductTypeItemDTO> GetAllProductTypeItemDTO()
        {
            return Query(); 
        }

        // GET tables/ProductTypeItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public SingleResult<ProductTypeItemDTO> GetProductTypeItemDTO(string id)
        //{
        //    return Lookup(id);
        //}

        // PATCH tables/ProductTypeItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task<ProductTypeItemDTO> PatchProductTypeItemDTO(string id, Delta<ProductTypeItemDTO> patch)
        //{
        //     return UpdateAsync(id, patch);
        //}

        // POST tables/ProductTypeItem
        //public async Task<IHttpActionResult> PostProductTypeItemDTO(ProductTypeItemDTO item)
        //{
        //    ProductTypeItemDTO current = await InsertAsync(item);
        //    return CreatedAtRoute("Tables", new { id = current.Id }, current);
        //}

        // DELETE tables/ProductTypeItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task DeleteProductTypeItemDTO(string id)
        //{
        //     return DeleteAsync(id);
        //}

    }
}