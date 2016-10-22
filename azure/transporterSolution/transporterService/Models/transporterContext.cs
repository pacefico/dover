using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using Microsoft.WindowsAzure.Mobile.Service;
using Microsoft.WindowsAzure.Mobile.Service.Tables;
using transporterService.DataTranspObjects;
using transporterService.DataObjects;

namespace transporterService.Models
{
    public class transporterContext : DbContext
    {
        // You can add custom code to this file. Changes will not be overwritten.
        // 
        // If you want Entity Framework to alter your database
        // automatically whenever you change your model schema, please use data migrations.
        // For more information refer to the documentation:
        // http://msdn.microsoft.com/en-us/data/jj591621.aspx
        //
        // To enable Entity Framework migrations in the cloud, please ensure that the 
        // service name, set by the 'MS_MobileServiceName' AppSettings in the local 
        // Web.config, is the same as the service name when hosted in Azure.
        private const string connectionStringName = "Name=MS_TableConnectionString1";

        public transporterContext() : base(connectionStringName)
        {
        }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            string schema = ServiceSettingsDictionary.GetSchemaName();
            if (!string.IsNullOrEmpty(schema))
            {
                modelBuilder.HasDefaultSchema(schema);
            }

            modelBuilder.Conventions.Add(
                new AttributeToColumnAnnotationConvention<TableColumnAttribute, string>(
                    "ServiceTableColumn", (property, attributes) => attributes.Single().ColumnType.ToString()));


            // Configure UserItemId as PK for UserFacebookItem
            //modelBuilder.Entity<UserFacebookItem>()
            //    .HasKey(e => e.UserItemId);

            //// Configure StudentId as FK for StudentAddress
            //modelBuilder.Entity<UserItem>()
            //            .HasOptional(s => s.UserFacebookItem) // Mark UserFacebookItem is optional for UserItem
            //            .WithRequired(ad => ad.UserItem); // Create inverse relationship

            //modelBuilder.Entity<UserItem>()
            //    .HasOptional(s => s.UserFacebookItem)
            //    .WithRequired( f => f.UserItem);

            //modelBuilder.Entity<UserItemDTO>()
            //    .HasOptional(s => s.UserFacebookItem)
            //    .WithRequired(f => f.UserItem);
            //modelBuilder.Entity<Foo>()
            //.HasOptional(f => f.Boo)
            //.WithRequired(s => s.Foo);

            //ignoring creation
            //modelBuilder.Entity<OrderRequestItem>().Ignore(o => o.RouteItems);
            //base.OnModelCreating(modelBuilder);
        }

        //public System.Data.Entity.DbSet<transporterService.DataObjects.Item> Items { get; set; }

        //public System.Data.Entity.DbSet<transporterService.DataObjects.TodoItem> TodoItems { get; set; }

        public DbSet<DataObjects.LatLnItem> LatLnItems { get; set; }

        public DbSet<DataObjects.RouteItem> RouteItems { get; set; }

        public DbSet<DataObjects.UserItem> UserItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.InviteItem> InviteItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.InviteUserItem> InviteUserItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.ItineraryItem> ItineraryItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.OrderRequestItem> OrderRequestItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.LatLnRequestItem> LatLnRequestItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.OrderItem> OrderItems { get; set; } 

        public System.Data.Entity.DbSet<transporterService.DataObjects.StatusDescriptionItem> StatusDescriptionItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.UserRatingItem> UserRatingItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.OrderStatusItem> OrderStatusItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.UserFacebookItem> UserFacebookItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.ImageItem> ImageItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.ProductAddressItem> ProductAddressItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.ProductDetailItem> ProductDetailItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.ProductTypeItem> ProductTypeItems { get; set; }

        public System.Data.Entity.DbSet<transporterService.DataObjects.ServiceTypeItem> ServiceTypeItems { get; set; }
    }

}
