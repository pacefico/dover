using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataObjects;

namespace transporterService.DataTranspObjects
{
    public partial class RouteItemDTO : EntityData
    {
        public virtual LatLnItemDTO From { get; set; }
        public virtual LatLnItemDTO To { get; set; }
        public virtual UserItemDTO User { get; set; }

        //Type: 0 = none, 1 = transportar, 2 = enviar
    }
}