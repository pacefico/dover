using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.WindowsAzure.Mobile.Service;
using transporterService.DataObjects;
using transporterService.Models;
using transporterService.DataTranspObjects;

namespace transporterService.Controllers
{
    //public class ItemController : TableController<ItemDTO>
    //{
    //    protected override void Initialize(HttpControllerContext controllerContext)
    //    {
    //        base.Initialize(controllerContext);
    //        transporterContext context = new transporterContext();
    //        DomainManager = new EntityDomainManager<ItemDTO>(context, Request, Services);
    //    }

    //    // GET tables/Item
    //    public IQueryable<ItemDTO> GetAllItem()
    //    {
    //        return Query();
    //    }

    //    // GET tables/Item/48D68C86-6EA6-4C25-AA33-223FC9A27959
    //    public SingleResult<ItemDTO> GetItem(string id)
    //    {
    //        return Lookup(id);
    //    }

    //    // PATCH tables/Item/48D68C86-6EA6-4C25-AA33-223FC9A27959
    //    public Task<ItemDTO> PatchItem(string id, Delta<ItemDTO> patch)
    //    {
    //        return UpdateAsync(id, patch);
    //    }

    //    // POST tables/Item
    //    public async Task<IHttpActionResult> PostItem(ItemDTO item)
    //    {
    //        ItemDTO current = await InsertAsync(item);
    //        return CreatedAtRoute("Tables", new { id = current.Id }, current);
    //    }

    //    // DELETE tables/Item/48D68C86-6EA6-4C25-AA33-223FC9A27959
    //    public Task DeleteItem(string id)
    //    {
    //        return DeleteAsync(id);
    //    }

    //}
}