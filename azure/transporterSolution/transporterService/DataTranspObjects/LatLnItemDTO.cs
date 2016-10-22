using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class LatLnItemDTO : EntityData
    {
        public string Latitud { get; set; }
        public string Longitud { get; set; }
        public string Name { get; set; }
        //public virtual ICollection<RouteItemDTO> RouteItems { get; set; }

        //public LatLnItemDTO()
        //{
        //    RouteItems = new List<RouteItemDTO>();
        //}
    }
}