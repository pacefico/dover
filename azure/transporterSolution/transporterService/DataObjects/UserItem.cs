using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class UserItem : BaseItem
    {
        public string Name { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }

        //public string UserFacebookItemID { get; set; }
        //[ForeignKey("UserFacebookItemID")]
        public virtual UserFacebookItem UserFacebookItem {get;set;}
    }
}
