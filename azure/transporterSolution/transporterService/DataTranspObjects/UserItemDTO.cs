using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class UserItemDTO : EntityData
    {
        public string Name { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }

        //public string UserFacebookItemID { get; set; }
        //[ForeignKey("UserFacebookItemID")]
        public virtual UserFacebookItemDTO UserFacebookItem { get; set; }
    }
}