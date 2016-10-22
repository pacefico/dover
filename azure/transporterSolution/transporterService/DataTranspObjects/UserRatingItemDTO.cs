using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class UserRatingItemDTO : EntityData
    {
        public string Comments { get; set; }
        public int Value { get; set; }
        public virtual UserItemDTO UserSource { get; set; }
        public virtual UserItemDTO UserDestin { get; set;  }
        public virtual OrderItemDTO OrderItem { get; set; }
        public string DateTime { get; set; }
    }
}