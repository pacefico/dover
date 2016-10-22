using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations.Schema;

namespace transporterService.DataTranspObjects
{
    public partial class OrderRequestItemDTO : EntityData
    {
        public string DateTime { get; set; }
        public virtual LatLnRequestItemDTO From { get; set; }
        public virtual LatLnRequestItemDTO To { get; set; }
        public virtual UserItemDTO User { get; set; }
        public int MaxDistance { get; set; }
        public int Results { get; set; }
    }
}