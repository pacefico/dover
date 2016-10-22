using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataObjects
{
    public class TodoItem
    {
        public TodoItem()
        {
            //Items = new List<Item>();
        }
        public string Text { get; set; }
        public string Id { get; set; }
        public bool Complete { get; set; }
        //public virtual ICollection<Item> Items { get; set; }
    }
}