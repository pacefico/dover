using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class ItineraryItem : BaseItem
    {
        public string Date { get; set; }
        public string Time { get; set; }
        public virtual RouteItem RouteItem { get; set; }
    }
}