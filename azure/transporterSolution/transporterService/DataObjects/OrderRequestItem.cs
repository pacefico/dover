using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class OrderRequestItem : BaseItem
    {
        public string DateTime { get; set; }
        public virtual LatLnRequestItem From { get; set; }
        public virtual LatLnRequestItem To { get; set; }
        public virtual UserItem User { get; set; }
        public int MaxDistance { get; set; }

        //response
        public int Results { get; set; }
        public virtual ICollection<RouteItem> RouteItems { get; set; }

        //public OrderRequestItem()
        //{
        //    RouteItems = new List<RouteItem>();
        //}

    }
}