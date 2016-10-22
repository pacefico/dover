using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class InviteUserItemDTO : EntityData
    {
        public string HostUserId { get; set; }
        public int AvailableInvites { get; set; }
        public int Invitations { get; set; }
    }
}