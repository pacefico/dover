using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.WindowsAzure.Mobile.Service;
using transporterService.DataTranspObjects;
using transporterService.Models;
using transporterService.Extensions;
using transporterService.DataObjects;
using System;
using AutoMapper;
using System.Collections.Generic;

namespace transporterService.Controllers
{
    public class OrderRequestItemController : TableController<OrderRequestItemDTO>
    {
        private transporterContext context;
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            this.context = new transporterContext();
            DomainManager = new SimpleMappedEntityDomainManager<OrderRequestItemDTO, OrderRequestItem>
                                (context, Request, Services, routeItem => routeItem.Id);
        }

        // GET tables/OrderRequestItem
        //[QueryableExpand("From"), QueryableExpand("To")]
        //public IQueryable<OrderRequestItemDTO> GetAllOrderRequestItemDTO()
        //{
        //    return Query();
        //}

        // GET tables/OrderRequestItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //[QueryableExpand("From"), QueryableExpand("To")]
        //public SingleResult<OrderRequestItemDTO> GetOrderRequestItemDTO(string id)
        //{
        //    return Lookup(id);
        //}

        // PATCH tables/OrderRequestItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task<OrderRequestItemDTO> PatchOrderRequestItemDTO(string id, Delta<OrderRequestItemDTO> patch)
        //{
        //    return UpdateAsync(id, patch);
        //}

        // POST tables/OrderRequestItem
        public async Task<IHttpActionResult> PostOrderRequestItemDTO(OrderRequestItemDTO item)
        {
            OrderRequestItem current = new OrderRequestItem();
            current = Mapper.Map<OrderRequestItemDTO, OrderRequestItem>(item, current);

            List<RouteItem> queryTemp = this.context.RouteItems.Include("From").Include("To").Include("User").Where(a => a.User.Id != item.User.Id).ToList();
            //if (item.From != null && item.From.Name != "")
            //{
            //    query = query.Where(a => a.From.Name.Contains(item.From.Name));
            //}
            //if (item.To != null && item.To.Name != "")
            //{
            //    query = query.Where(a => a.To.Name.Contains(item.To.Name));
            //}
            List<RouteItem> query = new List<RouteItem>();
            foreach (var rota in queryTemp)
            {
                Double dFrom = distance(Double.Parse(rota.From.Latitud), Double.Parse(rota.From.Longitud), Double.Parse(current.From.Latitud), Double.Parse(current.From.Longitud), 'K');
                Double dTo = distance(Double.Parse(rota.To.Latitud), Double.Parse(rota.To.Longitud), Double.Parse(current.To.Latitud), Double.Parse(current.To.Longitud), 'K');


                //if (dFrom < 10 && dTo < 10)
                if (dFrom < item.MaxDistance && dTo < item.MaxDistance)
                {
                    query.Add(rota);
                }
            }


            if (query.Count() > 0)
            {
                current.Results = query.Count();
                current.RouteItems = query;
            } else {
                current.Results = 0;
                current.RouteItems = new List<RouteItem>();
            }

            current.Id = Guid.NewGuid().ToString();
            current.From.Id = Guid.NewGuid().ToString();
            current.To.Id = Guid.NewGuid().ToString();
            current.DateTime = DateTime.Now.ToString();

            if (current.User != null && current.User.Id != "")
            {
                current.User = this.context.UserItems.FirstOrDefault(a => a.Id == item.User.Id);
            }
            
            this.context.OrderRequestItems.Add(current);
            await this.context.SaveChangesAsync();
            
            //OrderRequestItemDTO current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        private double distance(double lat1, double lon1, double lat2, double lon2, char unit)
        {
            double theta = lon1 - lon2;
            double dist = Math.Sin(deg2rad(lat1)) * Math.Sin(deg2rad(lat2)) +
                          Math.Cos(deg2rad(lat1)) * Math.Cos(deg2rad(lat2)) *
                          Math.Cos(deg2rad(theta));
            dist = Math.Acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit == 'K')
            {
                dist = dist * 1.609344;
            }
            else if (unit == 'N')
            {
                dist = dist * 0.8684;
            }
            return (dist);
        }

        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //::  This function converts decimal degrees to radians             :::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        private double deg2rad(double deg)
        {
            return (deg * Math.PI / 180.0);
        }

        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //::  This function converts radians to decimal degrees             :::
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        private double rad2deg(double rad)
        {
            return (rad / Math.PI * 180.0);
        }

        // DELETE tables/OrderRequestItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        //public Task DeleteOrderRequestItemDTO(string id)
        //{
        //    return DeleteAsync(id);
        //}

    }
}