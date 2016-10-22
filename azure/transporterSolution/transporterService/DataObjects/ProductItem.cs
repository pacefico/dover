using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class ProductItem : BaseItem
    {
        public ProductItem()
        {
            //ImageItems = new List<ImageItem>();
        }
        public string ContentDescription { get; set; }
        public string Title { get; set; }

        public int Height { get; set; }
        public int Width { get; set; }
        public int Length { get; set; }
        public int WeightKg { get; set; }

        public virtual ProductTypeItem ProductType { get; set; }
        public virtual ServiceTypeItem ServiceType { get; set; }
        //public virtual OrderItem OrderItem { get; set; }

        //public virtual ICollection<ImageItem> ImageItems { get; set; }

    }
}