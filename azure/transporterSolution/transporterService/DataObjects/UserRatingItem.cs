using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class UserRatingItem : BaseItem
    {
        public string Comments { get; set; }
        public int Value { get; set; }
        public virtual UserItem UserSource { get; set; }
        public virtual UserItem UserDestin { get; set; }
        public virtual OrderItem OrderItem { get; set; }
        public string DateTime { get; set; }
    }
}