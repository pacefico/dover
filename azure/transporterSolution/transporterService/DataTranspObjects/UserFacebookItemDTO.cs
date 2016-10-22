using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public class UserFacebookItemDTO : EntityData
    {
        //public virtual UserItemDTO UserItem { get; set; }

        public string DateTime { get; set; }
        public string Token { get; set; }
        public string Email { get; set; }
        public string Name { get; set; }
    }
}