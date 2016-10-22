using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class UserFacebookItem : BaseItem
    {
        //public virtual UserItem UserItem { get; set; }

        public string DateTime { get; set; }
        public string Token { get; set; }
        public string Email { get; set; }
        public string Name { get; set; }
    }
}