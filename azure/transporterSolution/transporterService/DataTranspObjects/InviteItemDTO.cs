using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class InviteItemDTO : EntityData
    {
        public string HostUserId { get; set; }
        public string InviteCode { get; set; }
        public DateTimeOffset InviteDate { get; set; }
        public string GuestUserEmail { get; set; }
    }
}