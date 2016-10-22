using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataObjects
{
    public class Item
    {
        public string Id { get; set; }
        public string ItemName { get; set; }
        public string TodoItemId { get; set; }
        //public virtual TodoItem TodoItem { get; set; }
    }
}