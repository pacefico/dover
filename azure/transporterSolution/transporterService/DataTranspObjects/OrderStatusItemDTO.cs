using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class OrderStatusItemDTO : EntityData
    {
        public OrderStatusItemDTO()
        {
            ImageItems = new List<ImageItemDTO>();
        }
        public bool IsActual { get; set; }
        public string DateTime { get; set; }
        public virtual OrderItemDTO OrderItem { get; set; }
        public virtual StatusDescriptionItemDTO StatusItem { get; set; }

        public virtual ICollection<ImageItemDTO> ImageItems { get; set; }
    }
}