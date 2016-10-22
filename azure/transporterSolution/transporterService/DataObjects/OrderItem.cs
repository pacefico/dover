using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class OrderItem : BaseItem
    {
        public OrderItem()
        {
            ProductDetailItems = new List<ProductDetailItem>();
        }
        public virtual UserItem UserItem { get; set; }
        public virtual RouteItem RouteItem { get; set; }
        public string DateTime { get; set; }
        public string Comments { get; set; }

        public virtual ICollection<ProductDetailItem> ProductDetailItems { get; set; }
    }
}