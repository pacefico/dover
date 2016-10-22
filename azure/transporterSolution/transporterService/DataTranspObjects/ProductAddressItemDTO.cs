using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class ProductAddressItemDTO : EntityData
    {
        public string Address { get; set; }
        public string Number { get; set; }
        public string Location { get; set; }
        public string City { get; set; }
        public string State { get; set; }
        public string PostalCode { get; set; }
        public string Reference { get; set; }

        public virtual ProductDetailItemDTO ProductDetail { get; set; }

    }
}