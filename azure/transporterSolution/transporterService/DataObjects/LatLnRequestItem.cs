using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class LatLnRequestItem : BaseItem
    {
        public string Name { get; set; }
        public string Latitud { get; set; }
        public string Longitud { get; set; }
    }
}