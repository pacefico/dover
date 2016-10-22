using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class ImageItemDTO : EntityData
    {
        public string ImageUrl { get; set; }
        public string OrderItemID { get; set; }
        public string ProductDetailItemID { get; set; }

        [ForeignKey("OrderItemID")]
        public virtual OrderStatusItemDTO OrderStatusItem { get; set; }

        [ForeignKey("ProductDetailItemID")]
        public virtual ProductDetailItemDTO ProductDetailItem { get; set; }

    }
}