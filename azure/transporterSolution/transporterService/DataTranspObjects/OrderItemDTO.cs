using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class OrderItemDTO : EntityData
    {
        public OrderItemDTO()
        {
            ProductDetailItems = new List<ProductDetailItemDTO>();
        }
        public virtual UserItemDTO UserItem { get; set; }
        public virtual RouteItemDTO RouteItem { get; set; }
        public string DateTime { get; set; }
        public string Comments { get; set; }

        public virtual ICollection<ProductDetailItemDTO> ProductDetailItems { get; set; }
        //List<ProductDetails>
        //List<ServiceType>
    }
}