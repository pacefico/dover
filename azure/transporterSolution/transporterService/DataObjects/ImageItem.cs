using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class ImageItem : BaseItem 
    {
        public string ImageUrl { get; set; }
        public string OrderItemID { get; set; }
        public string ProductDetailItemID { get; set; }

        [ForeignKey("OrderItemID")]
        public virtual OrderStatusItem OrderStatusItem { get; set; }

        [ForeignKey("ProductDetailItemID")]
        public virtual ProductDetailItem ProductDetailItem { get; set; }

        //default url
        //http://dover.blob.core.windows.net/imageproduct/defaultproduct.png
        //http://dover.blob.core.windows.net/imagestatus/defaultdelivered.png
        //http://dover.blob.core.windows.net/imagestatus/defaultsent.png
        //http://dover.blob.core.windows.net/imageuser/defaultuserfemale.png
        //http://dover.blob.core.windows.net/imageuser/defaultusermale.png
        //ex:
        // https://azure.microsoft.com/en-us/documentation/articles/mobile-services-android-upload-data-blob-storage/
    }

    
}