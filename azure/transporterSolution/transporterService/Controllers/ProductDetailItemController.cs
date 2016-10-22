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
    public class ProductDetailItemController : TableController<ProductDetailItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<ProductDetailItemDTO, ProductDetailItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/ProductDetailItem
        public IQueryable<ProductDetailItemDTO> GetAllProductDetailItemDTO()
        {
            return Query(); 
        }

        // GET tables/ProductDetailItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<ProductDetailItemDTO> GetProductDetailItemDTO(string id)
        {
            return Lookup(id);
        }

        // PATCH tables/ProductDetailItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<ProductDetailItemDTO> PatchProductDetailItemDTO(string id, Delta<ProductDetailItemDTO> patch)
        {
             return UpdateAsync(id, patch);
        }

        // POST tables/ProductDetailItem
        public async Task<IHttpActionResult> PostProductDetailItemDTO(ProductDetailItemDTO item)
        {
            ProductDetailItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/ProductDetailItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteProductDetailItemDTO(string id)
        {
             return DeleteAsync(id);
        }

    }
}