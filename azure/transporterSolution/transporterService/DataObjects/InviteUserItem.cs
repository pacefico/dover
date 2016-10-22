using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class InviteUserItem : BaseItem
    {
        public string HostUserId { get; set; }
        public int AvailableInvites { get; set; }
        public int Invitations { get; set; }

    }
}