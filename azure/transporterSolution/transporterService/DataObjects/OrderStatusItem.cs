using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class OrderStatusItem : BaseItem
    {
        public OrderStatusItem()
        {
            ImageItems = new List<ImageItem>();
        }

        public bool IsActual { get; set; }
        public string DateTime { get; set; }
        public virtual OrderItem OrderItem { get; set; }
        public virtual StatusDescriptionItem StatusItem { get; set; }

        public virtual ICollection<ImageItem> ImageItems { get; set; }
        
    }
}