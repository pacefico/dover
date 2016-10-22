using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using transporterService.DataTranspObjects;

namespace transporterService.DataObjects
{
    public partial class StatusDescriptionItem : BaseItem
    {
        public string Name { get; set; }
        public int Type { get; set; }
        public int UserType { get; set; }

        //UserType: 0 = none, 1 = carrier, 2 = sender, 3 = both
    }
}