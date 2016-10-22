namespace transporterService.DataObjects
{
    using Microsoft.WindowsAzure.Mobile.Service;
    using DataTranspObjects;

    public partial class RouteItem : BaseItem
    {
        public virtual LatLnItem From { get; set; }
        public virtual LatLnItem To { get; set; }
        public virtual UserItem User { get; set; }

        //Type: 0 = none, 1 = transportar, 2 = enviar
    }
}
