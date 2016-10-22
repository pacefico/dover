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
using System;
using System.Collections.Generic;

namespace transporterService.Controllers
{
    public class ImageItemController : TableController<ImageItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<ImageItemDTO, ImageItem>
                                (context, Request, Services, item => item.Id);
        }

        // GET tables/ImageItem
        //public IQueryable<ImageItemDTO> GetAllImageItemDTO()
        //{
        //    return Query(); 
        //}

        // GET tables/ImageItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<ImageItemDTO> GetImageItemDTO(string id, string type)
        {
            if (id == "0")
            {
                //http://dover.blob.core.windows.net/imageproduct/defaultproduct.png

                string newUrl = "http://dover.blob.core.windows.net/";

                ImageItemDTO newImage = new ImageItemDTO();
                newImage.Id = Guid.NewGuid().ToString();

                switch (type)
                {
                    case "status":
                        newUrl += "imagestatus/" + newImage.Id;
                        newImage.ImageUrl = newUrl;
                        break;
                    case "product":
                        newUrl += "imageproduct/" + newImage.Id;
                        newImage.ImageUrl = newUrl;
                        break;
                    case "user":
                        newUrl += "imageuser/" + newImage.Id;
                        newImage.ImageUrl = newUrl;
                        break;
                    default:
                        newUrl += "imagestatus/" + newImage.Id;
                        newImage.ImageUrl = newUrl;
                        break;
                }

                List<ImageItemDTO> list = new List<ImageItemDTO> {
                    newImage
                };
                return SingleResult<ImageItemDTO>.Create(list.AsQueryable());
            } else
            {
                return Lookup(id);
            }
            //return null;
        }

       

    }
}