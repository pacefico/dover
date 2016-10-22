package com.example.fugliara.ccs002.server.controllers;

import android.util.Log;

import com.example.fugliara.ccs002.server.AzureRequest;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.dataobjects.LatLnItem;
import com.example.fugliara.ccs002.server.dataobjects.OrderItem;
import com.example.fugliara.ccs002.server.dataobjects.OrderRequestItem;
import com.example.fugliara.ccs002.server.dataobjects.OrderStatusItem;
import com.example.fugliara.ccs002.server.dataobjects.ProductDetailItem;
import com.example.fugliara.ccs002.server.dataobjects.RouteItem;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;

public class OrderController {
    private static final String TAG = "TAG_OrderController";

    private MobileServiceTable<OrderItem> serviceTableOrder = null;
    private MobileServiceTable<OrderRequestItem> serviceTableRequest = null;
    private MobileServiceTable<OrderStatusItem> serviceTableStatus = null;


    public OrderController(MobileServiceClient mClient) {
        serviceTableOrder = mClient.getTable(OrderItem.class);
        serviceTableRequest = mClient.getTable(OrderRequestItem.class);
        serviceTableStatus = mClient.getTable(OrderStatusItem.class);
    }

    public void createOrder(final AzureResponse<OrderItem> result, final RouteItem routeItem, final ProductDetailItem productDetail) {
        AzureRequest<OrderItem> request = new AzureRequest<OrderItem>() {
            @Override
            public MobileServiceList<OrderItem> send() {
                MobileServiceList<OrderItem> queryResult = null;
                try {
                    OrderItem order = new OrderItem();
                    order.route = routeItem;
                    order.productDetailItems = new ArrayList<ProductDetailItem>();
                    order.productDetailItems.add(productDetail);
                    order.user = ProviderService.getInstance().getSessionItemView().userItem;
                    order = serviceTableOrder.insert(order).get();
                    queryResult = new MobileServiceList<OrderItem>(new ArrayList<OrderItem>(), 0);
                    queryResult.add(order);
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "createOrder() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void getOrder(final AzureResponse<OrderItem> result) {
        AzureRequest<OrderItem> request = new AzureRequest<OrderItem>() {
            @Override
            public MobileServiceList<OrderItem> send() {
                MobileServiceList<OrderItem> queryResult = null;
                try {
                    queryResult = serviceTableOrder.parameter("userId", ProviderService.getInstance().getSessionItemView().userItem.id).execute().get();
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "getOrder() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void getOrderStatus(final AzureResponse<OrderStatusItem> result, final String orderId) {
        AzureRequest<OrderStatusItem> request = new AzureRequest<OrderStatusItem>() {
            @Override
            public MobileServiceList<OrderStatusItem> send() {
                MobileServiceList<OrderStatusItem> queryResult = null;
                try {
                    OrderStatusItem status;
                    status = serviceTableStatus.lookUp(orderId).get();
                    queryResult = new MobileServiceList<OrderStatusItem>(new ArrayList<OrderStatusItem>(), 0);
                    queryResult.add(status);
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "getOrderStatus() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void getOrderRequest(final AzureResponse<OrderRequestItem> result, final LatLnItem from, final LatLnItem to, final int maxDistance) {
        AzureRequest<OrderRequestItem> request = new AzureRequest<OrderRequestItem>() {
            @Override
            public MobileServiceList<OrderRequestItem> send() {
                MobileServiceList<OrderRequestItem> queryResult = null;
                try {
                    OrderRequestItem orderRequest = new OrderRequestItem();
                    orderRequest.from = from;
                    orderRequest.to = to;
                    orderRequest.maxDistance = maxDistance;
                    orderRequest.user = ProviderService.getInstance().getSessionItemView().userItem;
                    orderRequest = serviceTableRequest.insert(orderRequest).get();
                    queryResult = new MobileServiceList<OrderRequestItem>(new ArrayList<OrderRequestItem>(), 0);
                    queryResult.add(orderRequest);
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "getOrderRequest() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }

    public void updateOrderStatusItem(final AzureResponse<OrderStatusItem> result, final OrderStatusItem orderStatusItem) {
        AzureRequest<OrderStatusItem> request = new AzureRequest<OrderStatusItem>() {
            @Override
            public MobileServiceList<OrderStatusItem> send() {
                MobileServiceList<OrderStatusItem> queryResult = null;
                try {
                    OrderStatusItem orderStatusItemResult;
                    orderStatusItemResult = serviceTableStatus.update(orderStatusItem).get(); // patch
                    queryResult = new MobileServiceList<OrderStatusItem>(new ArrayList<OrderStatusItem>(), 0);
                    queryResult.add(orderStatusItemResult);
                } catch (Exception exception) {
                    result.exception = true;
                    Log.d(TAG, "updateOrderStatusItem() Error: " + exception.getMessage());
                }
                return queryResult;
            }
        };

        request.execute(result);
    }
}
