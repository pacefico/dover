namespace transporterService.DataObjects
{
    using Microsoft.WindowsAzure.Mobile.Service;
    using System.Collections.Generic;
    using DataTranspObjects;

    public partial class LatLnItem : BaseItem
    {
        public string Name { get; set; }
        public string Latitud { get; set; }
        public string Longitud { get; set; }
    }
}
