using System;
using System.Collections.Generic;
using System.Web.Http;
using Microsoft.WindowsAzure.Mobile.Service;
using transporterService.DataObjects;
using transporterService.Models;
using transporterService.DataTranspObjects;
using AutoMapper;
using System.Data.Entity;

namespace transporterService
{
    public static class WebApiConfig
    {
        public static void Register()
        {
            // Use this class to set configuration options for your mobile service
            ConfigOptions options = new ConfigOptions();

            // Use this class to set WebAPI configuration options
            HttpConfiguration config = ServiceConfig.Initialize(new ConfigBuilder(options));

            // To display errors in the browser during development, uncomment the following
            // line. Comment it out again when you deploy your service for production use.
            // config.IncludeErrorDetailPolicy = IncludeErrorDetailPolicy.Always;
            
            // Set default and null value handling to "Include" for Json Serializer
            config.Formatters.JsonFormatter.SerializerSettings.DefaultValueHandling = Newtonsoft.Json.DefaultValueHandling.Include;
            config.Formatters.JsonFormatter.SerializerSettings.NullValueHandling = Newtonsoft.Json.NullValueHandling.Include;

            config.Formatters.JsonFormatter.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore;

            //Database.SetInitializer(new CreateDatabaseIfNotExists<transporterContext>());

            CreateMappers();
            Database.SetInitializer(new transporterInitializer());
        }

        public static void CreateMappers()
        {

            AutoMapper.Mapper.Initialize(cfg =>
            {
                cfg.CreateMap<StatusDescriptionItem, StatusDescriptionItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<StatusDescriptionItemDTO, StatusDescriptionItem>();

                cfg.CreateMap<UserRatingItem, UserRatingItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<UserRatingItemDTO, UserRatingItem>();

                cfg.CreateMap<ServiceTypeItem, ServiceTypeItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<ServiceTypeItemDTO, ServiceTypeItem>();

                cfg.CreateMap<ProductTypeItem, ProductTypeItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<ProductTypeItemDTO, ProductTypeItem>();

                cfg.CreateMap<ProductDetailItem, ProductDetailItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore())
                    .ForMember(itemDTO => itemDTO.OrderItem,
                                            map => map.MapFrom(item => item.OrderItem));
                cfg.CreateMap<ProductDetailItemDTO, ProductDetailItem>()
                    .ForMember(itemDTO => itemDTO.OrderItem,
                                            map => map.MapFrom(item => item.OrderItem));

                cfg.CreateMap<ProductAddressItem, ProductAddressItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<ProductAddressItemDTO, ProductAddressItem>();

                cfg.CreateMap<ImageItem, ImageItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<ImageItemDTO, ImageItem>();

                cfg.CreateMap<OrderRequestItem, OrderRequestItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<OrderRequestItemDTO, OrderRequestItem>();

                cfg.CreateMap<LatLnItem, LatLnItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<LatLnItemDTO, LatLnItem>();

                cfg.CreateMap<RouteItem, RouteItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<RouteItemDTO, RouteItem>();

                cfg.CreateMap<InviteUserItem, InviteUserItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<InviteUserItemDTO, InviteUserItem>();

                cfg.CreateMap<InviteItem, InviteItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<InviteItemDTO, InviteItem>();

                cfg.CreateMap<ItineraryItem, ItineraryItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<ItineraryItemDTO, ItineraryItem>();

                cfg.CreateMap<LatLnRequestItem, LatLnRequestItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                    
                cfg.CreateMap<LatLnRequestItemDTO, LatLnRequestItem>();

                cfg.CreateMap<OrderRequestItem, OrderRequestItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                cfg.CreateMap<OrderRequestItemDTO, OrderRequestItem>()
                    .ForMember(x => x.RouteItems, opt => opt.Ignore());

                cfg.CreateMap<UserFacebookItem, UserFacebookItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore());
                //.ForMember(itemDTO => itemDTO.UserItem,
                //                        map => map.MapFrom(item => item.UserItem));
                cfg.CreateMap<UserFacebookItemDTO, UserFacebookItem>();
                //.ForMember(itemDTO => itemDTO.UserItem,
                //                        map => map.MapFrom(item => item.UserItem));

                cfg.CreateMap<UserItem, UserItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore())
                    .ForMember(itemDTO => itemDTO.UserFacebookItem,
                                            map => map.MapFrom(item => item.UserFacebookItem))
                    .ForAllMembers(opt => opt.Condition(srs => !srs.IsSourceValueNull));
                //.IgnoreAllPropertiesWithAnInaccessibleSetter();
                cfg.CreateMap<UserItemDTO, UserItem>()
                    .ForMember(itemDTO => itemDTO.UserFacebookItem,
                                            map => map.MapFrom(item => item.UserFacebookItem))
                    .ForAllMembers(opt => opt.Condition(srs => !srs.IsSourceValueNull));
                                            //.IgnoreAllPropertiesWithAnInaccessibleSetter();

                cfg.CreateMap<OrderItem, OrderItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore())
                    .ForMember(itemDTO => itemDTO.ProductDetailItems,
                                            map => map.MapFrom(item => item.ProductDetailItems))
                    .ForMember(itemDTO => itemDTO.RouteItem,
                                          map => map.MapFrom(item => item.RouteItem))
                    .ForMember(itemDTO => itemDTO.UserItem,
                                          map => map.MapFrom(item => item.UserItem)); 
                cfg.CreateMap<OrderItemDTO, OrderItem>()
                    .ForMember(itemDTO => itemDTO.ProductDetailItems,
                                            map => map.MapFrom(item => item.ProductDetailItems))
                    .ForMember(itemDTO => itemDTO.RouteItem,
                                          map => map.MapFrom(item => item.RouteItem))
                    .ForMember(itemDTO => itemDTO.UserItem,
                                          map => map.MapFrom(item => item.UserItem));

                cfg.CreateMap<OrderStatusItem, OrderStatusItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore())
                    .ForMember(itemDTO => itemDTO.ImageItems,
                                            map => map.MapFrom(item => item.ImageItems));

                cfg.CreateMap<OrderStatusItemDTO, OrderStatusItem>()
                   .ForMember(itemDTO => itemDTO.ImageItems,
                                         map => map.MapFrom(item => item.ImageItems));

                //===== ItineraryItem
                cfg.CreateMap<ItineraryItem, ItineraryItemDTO>()
                    .ForMember(x => x.CreatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Deleted, opt => opt.Ignore())
                    .ForMember(x => x.UpdatedAt, opt => opt.Ignore())
                    .ForMember(x => x.Version, opt => opt.Ignore())
                    .ForMember(itemDTO => itemDTO.RouteItem,
                                          map => map.MapFrom(item => item.RouteItem))
                    .IgnoreAllPropertiesWithAnInaccessibleSetter();
                cfg.CreateMap<ItineraryItemDTO, ItineraryItem>()
                    .ForMember(item => item.RouteItem,
                                          map => map.MapFrom(itemDTO => itemDTO.RouteItem))
                    .IgnoreAllPropertiesWithAnInaccessibleSetter();

                Mapper.AssertConfigurationIsValid();

                //cfg.CreateMap<UserFacebookItem, UserFacebookItemDTO>()
                //                 .ForMember(itemDTO => itemDTO.UserItem,
                //                                       map => map.MapFrom(item => item.UserItem));
                //cfg.CreateMap<UserFacebookItemDTO, UserFacebookItem>()
                //    .ForMember(itemDTO => itemDTO.UserItem,
                //                          map => map.MapFrom(item => item.UserItem));
                //cfg.CreateMap<UserItem, UserItemDTO>()
                //                 .ForMember(itemDTO => itemDTO.UserFacebookItem,
                //                                       map => map.MapFrom(item => item.UserFacebookItem));
                //cfg.CreateMap<UserItemDTO, UserItem>()
                //    .ForMember(itemDTO => itemDTO.UserFacebookItem,
                //                          map => map.MapFrom(item => item.UserFacebookItem));
            });
        }
    }

    public class transporterInitializer : ClearDatabaseSchemaIfModelChanges<transporterContext>
    //public class transporterInitializer : DropCreateDatabaseAlways<transporterContext>
    {
        protected override void Seed(transporterContext context)
        {
            var plainTextBytes = System.Text.Encoding.UTF8.GetBytes("p");
            string passA = System.Convert.ToBase64String(plainTextBytes);

            var plainTextBytesb = System.Text.Encoding.UTF8.GetBytes("f");
            string passb = System.Convert.ToBase64String(plainTextBytesb);

            var plainTextBytesc = System.Text.Encoding.UTF8.GetBytes("k");
            string passc = System.Convert.ToBase64String(plainTextBytesc);


            //criando facabeook test items  ==============================================
            List<UserFacebookItem> facebookItems = new List<UserFacebookItem>
            {
                new UserFacebookItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToShortDateString(),
                    Token = "EstaEhUmaStringDeTeste",
                    Name = "Name de teste",
                    Email = "Email de teste",
                    //UserItem = context.UserItems.ToListAsync().Result.Find((a)=> a.Name == "Fellipe")
                },
                new UserFacebookItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToShortDateString(),
                    Token = "EstaEhUmaStringDeTeste",
                    Name = "Name de teste",
                    Email = "Email de teste",
                    //UserItem = context.UserItems.ToListAsync().Result.Find( (a)=> a.Name == "Paulo")
                },
            };
            foreach (UserFacebookItem item in facebookItems)
            {
                context.Set<UserFacebookItem>().Add(item);
            }

            List<UserItem> userItems = new List<UserItem>
            {
                new UserItem { Id = Guid.NewGuid().ToString(), Name = "Fellipe", Email = "f@f.com", Password = passb,
                UserFacebookItem = facebookItems[0] },
                new UserItem { Id = Guid.NewGuid().ToString(), Name = "Paulo", Email = "p@p.com", Password = passA,
                UserFacebookItem = facebookItems[1] },
                new UserItem { Id = Guid.NewGuid().ToString(), Name = "Katia", Email = "k@k.com", Password = passc,
                UserFacebookItem = null
                }
            };
            foreach (UserItem userItem in userItems)
            {
                context.Set<UserItem>().Add(userItem);
            }

             List<RouteItem> routeItems = new List<RouteItem> {
                new RouteItem {
                    Id = Guid.NewGuid().ToString(),
                    From  = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "52.37518",
                        Longitud = "4.895439", 
                        Name = "AMSTERDAM" },
                    To = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "48.856132",
                        Longitud = "2.352448",
                        Name = "PARIS"},
                    User = userItems.Find( (a)=> a.Name == "Fellipe"),
                },
                new RouteItem {
                    Id = Guid.NewGuid().ToString(),
                    From  = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "52.37518", Longitud = "4.895439", Name = "AMSTERDAM"},
                    To = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "50.111772", Longitud = "8.682632", Name = "FRANKFURT"},
                    User = userItems.Find( (a)=> a.Name == "Fellipe"),
                },

                new RouteItem {
                    Id = Guid.NewGuid().ToString(),
                    From  = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "52.37518", Longitud = "4.895439", Name = "AMSTERDAM"},
                    To = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "48.856132", Longitud = "2.352448", Name = "PARIS"},
                    User = userItems.Find( (a)=> a.Name == "Paulo" ),
                },
                new RouteItem {
                    Id = Guid.NewGuid().ToString(),
                    From  = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "52.37518", Longitud = "4.895439", Name = "AMSTERDAM"},
                    To = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "48.856132", Longitud = "2.352448", Name = "PARIS"},
                    User = userItems.Find( (a)=> a.Name == "Paulo"),
                },
                new RouteItem {
                    Id = Guid.NewGuid().ToString(),
                    From  = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "52.37518", Longitud = "4.895439", Name = "AMSTERDAM"},
                    To = new LatLnItem { Id = Guid.NewGuid().ToString(),
                        Latitud = "48.856132", Longitud = "2.352448", Name = "PARIS"},
                    User = userItems.Find( (a)=> a.Name == "Paulo"),
                },
            };
            foreach (RouteItem routeItem in routeItems)
            {
                context.Set<RouteItem>().Add(routeItem);
            }

            //criacação de itinerário ==============================================
            List<ItineraryItem> itineraryItems = new List<ItineraryItem>
            {
                new ItineraryItem { Id = Guid.NewGuid().ToString(), Date = DateTime.Now.ToShortDateString(), Time = DateTime.Now.ToShortTimeString(), RouteItem = routeItems[0] },
                new ItineraryItem { Id = Guid.NewGuid().ToString(), Date = DateTime.Now.ToShortDateString(), Time = DateTime.Now.ToShortTimeString(), RouteItem = routeItems[1] },
                new ItineraryItem { Id = Guid.NewGuid().ToString(), Date = DateTime.Now.ToShortDateString(), Time = DateTime.Now.ToShortTimeString(), RouteItem = routeItems[2] },
                new ItineraryItem { Id = Guid.NewGuid().ToString(), Date = DateTime.Now.ToShortDateString(), Time = DateTime.Now.ToShortTimeString(), RouteItem = routeItems[3] }
            };
            foreach (ItineraryItem itItem in itineraryItems)
            {
                context.Set<ItineraryItem>().Add(itItem);
            }

            //criação de invite user ==============================================
            List<InviteUserItem> inviteUserItems = new List<InviteUserItem>
            {
                new InviteUserItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Invitations = 0,
                    AvailableInvites = 50,
                    HostUserId = userItems.Find( (a)=> a.Name == "Fellipe").Id
                },
                new InviteUserItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Invitations = 0,
                    AvailableInvites = 50,
                    HostUserId = userItems.Find( (a)=> a.Name == "Paulo").Id
                }
            };
            foreach (InviteUserItem inviteUserItem in inviteUserItems)
            {
                context.Set<InviteUserItem>().Add(inviteUserItem);
            }

            //criação de guest user ==============================================
            List<InviteItem> inviteItems = new List<InviteItem>
            {
                new InviteItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    InviteCode = "a52b",
                    InviteDate = DateTime.Now,
                    HostUserId = userItems.Find( (a)=> a.Name == "Fellipe").Id,
                    GuestUserEmail = "a@a.com"
                },
                new InviteItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    InviteCode = "a52b",
                    InviteDate = DateTime.Now,
                    HostUserId = userItems.Find( (a)=> a.Name == "Paulo").Id,
                    GuestUserEmail = "b@b.com"
                }
            };
            foreach (InviteItem inviteItem in inviteItems)
            {
                context.Set<InviteItem>().Add(inviteItem);
            }

            //criação de orderrequest  ==============================================
            List<OrderRequestItem> orderRequestItems = new List<OrderRequestItem>
            {
                new OrderRequestItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToShortDateString(),
                    Results = 0,
                    User = userItems.Find( (a)=> a.Name == "Fellipe"),
                    From = new LatLnRequestItem
                    {
                        Id = Guid.NewGuid().ToString(),
                        Latitud = "52.37518",
                        Longitud = "4.895439",
                        Name = "AMSTERDAM"
                    },
                    To = new LatLnRequestItem
                    {
                        Id = Guid.NewGuid().ToString(),
                        Latitud = "48.856132",
                        Longitud = "2.352448",
                        Name = "PARIS"
                    },
                    MaxDistance = 5
                }
            };
            foreach (OrderRequestItem orderRequestItem in orderRequestItems)
            {
                context.Set<OrderRequestItem>().Add(orderRequestItem);
            }

            //criando ImageItem test items  ==============================================
            List<ImageItem> imageItems = new List<ImageItem>
            {
                new ImageItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    ImageUrl = "http://dover.blob.core.windows.net/imageproduct/defaultproduct.png",
                },
                new ImageItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    ImageUrl = "http://dover.blob.core.windows.net/imageproduct/defaultproduct.png",
                },
                new ImageItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    ImageUrl = "http://dover.blob.core.windows.net/imagestatus/defaultsent.png",
                },
                new ImageItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    ImageUrl = "http://dover.blob.core.windows.net/imagestatus/defaultdelivered.png",
                }
            };
            foreach (ImageItem item in imageItems)
            {
                context.Set<ImageItem>().Add(item);
            }

            //criando ServiceTypeItem test items  ==============================================
            //List<ServiceTypeItem> serviceTypeItems = new List<ServiceTypeItem>
            //{
            //    new ServiceTypeItem()
            //    {
            //        Id = Guid.NewGuid().ToString(),
            //        Description = "1 - Em Mãos"
            //    },
            //    new ServiceTypeItem()
            //    {
            //        Id = Guid.NewGuid().ToString(),
            //        Description = "2 - No correio mais próximo"
            //    },
            //};
            //foreach (ServiceTypeItem item in serviceTypeItems)
            //{
            //    context.Set<ServiceTypeItem>().Add(item);
            //}

            //criando ProductTypeItem test items  ==============================================
            List<ProductTypeItem> productTypeItems = new List<ProductTypeItem>
            {
                new ProductTypeItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Description = "Envelope"
                },
                new ProductTypeItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Description = "Caixa"
                },
            };
            foreach (ProductTypeItem item in productTypeItems)
            {
                context.Set<ProductTypeItem>().Add(item);
            }

            //criando produto para uma order (orderitem) ==============================================
            List<ProductDetailItem> productItems = new List<ProductDetailItem>
            {
                new ProductDetailItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Title = "Produtos de limpeza",
                    ContentDescription = "Rua Coronel",
                    Address = "Av Lagoa",
                    Height = 10, Length = 15, WeightKg = 1, Width = 5,
                    ProductType = productTypeItems[0],
                    Service = "Em Mãos",
                    //ServiceType = serviceTypeItems[0],
                    //OrderItem = orderItems[0],
                    ImageItems = new List<ImageItem>() { imageItems[0] }
                },
                 new ProductDetailItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Title = "Outros produtos",
                    ContentDescription = "Rua Coronel",
                    Address = "Av Lagoa",
                    Height = 12, Length = 17, WeightKg = 2, Width = 10,
                    ProductType = productTypeItems[1],
                    Service = "No correio mais próximo",
                    //ServiceType = serviceTypeItems[1],
                    //OrderItem = orderItems[1],
                    ImageItems = new List<ImageItem>() { imageItems [1] }
                }
            };
            //foreach (ProductDetailItem item in productItems)
            //{
            //    context.Set<ProductDetailItem>().Add(item);
            //}

            //criando associação de uma rota para um usuário (orderitem) ==============================================
            List<OrderItem> orderItems = new List<OrderItem>
            {
                new OrderItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToShortDateString(),
                    Comments = "Eu agendei esta rota para entregar pastéis",
                    RouteItem = routeItems[2],
                    UserItem = userItems.Find( (a)=> a.Name == "Fellipe"),
                    ProductDetailItems = new List<ProductDetailItem>() { productItems[0] }

                },
                 new OrderItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToShortDateString(),
                    Comments = "Eu agendei esta rota para entregar ostras",
                    RouteItem = routeItems[3],
                    UserItem = userItems.Find( (a)=> a.Name == "Fellipe"),
                    ProductDetailItems = new List<ProductDetailItem>() { productItems[1] }
                }
            };
            foreach (OrderItem orderItem in orderItems)
            {
                context.Set<OrderItem>().Add(orderItem);
            }

            //criando status description items  ==============================================
            List<StatusDescriptionItem> statusDescriptionItems = new List<StatusDescriptionItem>
            {
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Envio agendado",
                    Type = 1,
                    UserType = 2 //Sender
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Aguardando retirada do produto",
                    Type = 2,
                    UserType = 1
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Produto retirado",
                    Type = 3,
                    UserType = 1
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Transporte Iniciado",
                    Type = 4,
                    UserType = 1
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Chegou no destino",
                    Type = 5,
                    UserType = 1
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Produto entregue",
                    Type = 6,
                    UserType = 1
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Produto entregue",
                    Type = 7,
                    UserType = 1
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Aguardando qualificação do Transportador",
                    Type = 8,
                    UserType = 2
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Aguardando qualificação do Cliente",
                    Type = 9,
                    UserType = 1
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Finalizado",
                    Type = 98,
                    UserType = 3
                },
                new StatusDescriptionItem()
                {
                    Id = Guid.NewGuid().ToString(),
                    Name = "Ocorreu um problema",
                    Type = 99,
                    UserType = 1
                },
            };
            foreach (StatusDescriptionItem sdItem in statusDescriptionItems)
            {
                context.Set<StatusDescriptionItem>().Add(sdItem);
            }

            //criando rating items  ==============================================
            List<UserRatingItem> ratingItems = new List<UserRatingItem>
            {
                new UserRatingItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    OrderItem = orderItems[0],
                    UserSource = orderItems[0].RouteItem.User,
                    UserDestin = orderItems[0].UserItem,
                    Comments = "Solicitante muito atencioso e compreensivo, o produto foi entregue com sucesso",
                    Value = 5
                },
                new UserRatingItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    OrderItem = orderItems[0],
                    UserDestin = orderItems[0].RouteItem.User,
                    UserSource = orderItems[0].UserItem,
                    Comments = "O transportador entregou o produto no prazo combinado, recomendo!",
                    Value = 5
                },
                new UserRatingItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    OrderItem = orderItems[1],
                    UserSource = orderItems[1].RouteItem.User,
                    UserDestin = orderItems[1].UserItem,
                    Comments = "Mais uma vez o solicitante foi muito atencioso",
                    Value = 5
                },
                new UserRatingItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    OrderItem = orderItems[1],
                    UserDestin = orderItems[1].RouteItem.User,
                    UserSource = orderItems[1].UserItem,
                    Comments = "O transportador teve um problema no percurso mas tudo foi resolvido no devido tempo, recomendo!",
                    Value = 5
                },
            };
            foreach (UserRatingItem item in ratingItems)
            {
                context.Set<UserRatingItem>().Add(item);
            }

            //criando order status items  ==============================================
            List<OrderStatusItem> statusItems = new List<OrderStatusItem>
            {
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[0],
                    ImageItems = { imageItems[0] }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[1],
                    ImageItems = {  imageItems[0] }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[2],
                    ImageItems = { imageItems[2] }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[3],
                    ImageItems = { }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[4],
                    ImageItems = { }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[5],
                    ImageItems = { imageItems[3] }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[6],
                    ImageItems = { imageItems[3], imageItems[3] }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[8],
                    ImageItems = { imageItems[0] }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = true,
                    OrderItem = orderItems[0],
                    StatusItem = statusDescriptionItems[8],
                    ImageItems = { imageItems[0] }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = false,
                    OrderItem = orderItems[1],
                    StatusItem = statusDescriptionItems[0],
                    ImageItems = { imageItems[0] }
                },
                new OrderStatusItem
                {
                    Id = Guid.NewGuid().ToString(),
                    DateTime = DateTime.Now.ToString(),
                    IsActual = true,
                    OrderItem = orderItems[1],
                    StatusItem = statusDescriptionItems[1],
                    ImageItems = { imageItems[0] }
                },
            };
            foreach (OrderStatusItem item in statusItems)
            {
                context.Set<OrderStatusItem>().Add(item);
            }

            

            

            //criando produto para uma order (orderitem) ==============================================
            //List<ProductAddressItem> productAddressItems = new List<ProductAddressItem>
            //{
            //    new ProductAddressItem()
            //    {
            //        Id = Guid.NewGuid().ToString(),
            //        Address = "Rua numero 1",
            //        Number = "234",
            //        City = "Sorocaba",
            //        PostalCode = "12000-189",
            //        Location = "Parque Boa Esperaça",
            //        Reference = "Próximo ao parque",
            //        State = "São Paulo",
            //        ProductDetail = productItems[0]
            //    },
            //};
            //foreach (ProductAddressItem item in productAddressItems)
            //{
            //    context.Set<ProductAddressItem>().Add(item);
            //}

            

            //============================= TODOITEMS BEGIN ==============================

            //List<Item> items = new List<Item>
            //{
            //    new Item { Id = "1", ItemName = "Milk" },
            //    new Item { Id = "2", ItemName = "Eggs" }
            //};
            //foreach (Item item in items)
            //{
            //    context.Set<Item>().Add(item);
            //}

            //List<TodoItem> todoItems = new List<TodoItem>
            //{
            //    new TodoItem { Id = "1", Text = "Grocery", Complete = false, Items=items }
            //};
            //foreach (TodoItem todoitem in todoItems)
            //{
            //    context.Set<TodoItem>().Add(todoitem);
            //}
            //============================= TODOITEMS END ==============================



            base.Seed(context);
        }
    }
}

