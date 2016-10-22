using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataObjects;

namespace transporterService.DataTranspObjects
{
    public partial class ItineraryItemDTO : EntityData
    {
        public string Date { get; set; }
        public string Time { get; set; }
        public virtual RouteItem RouteItem { get; set; }
    }
}