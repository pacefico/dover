﻿using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class ProductDetailItemDTO : EntityData
    {
        public ProductDetailItemDTO()
        {
            ImageItems = new List<ImageItemDTO>();
        }
        public string OrderItemID { get; set; }
        public string ContentDescription { get; set; }
        public string Address { get; set; }
        public string Title { get; set; }
        public string Service { get; set; }

        public int Height { get; set; }
        public int Width { get; set; }
        public int Length { get; set; }
        public int WeightKg { get; set; }

        public virtual ProductTypeItemDTO ProductType { get; set; }
        public virtual ServiceTypeItemDTO ServiceType { get; set; }

        [ForeignKey("OrderItemID")]
        public virtual OrderItemDTO OrderItem { get; set; }

        public virtual ICollection<ImageItemDTO> ImageItems { get; set; }

    }
}