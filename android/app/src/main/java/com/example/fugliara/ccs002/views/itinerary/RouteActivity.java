package com.example.fugliara.ccs002.views.itinerary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.RouteItem;
import com.example.fugliara.ccs002.views.fragments.GMapV2Direction;
import com.example.fugliara.ccs002.views.fragments.GetDirectionsAsyncTask;
import com.example.fugliara.ccs002.views.fragments.MapInterface;
import com.example.fugliara.ccs002.views.fragments.MapUtils;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteActivity extends FragmentActivity implements MapInterface {

    private RouteActivity passThis = this;

    private GoogleMap map;
    private SupportMapFragment fragment;
    private LatLngBounds latlngBounds;
    private Polyline newPolyline;
    private int width, height;

    private ArrayList<RouteItem> routes = null;
    private ListView listViewRoute;
    private ArrayAdapter arrayAdapter;
    private int indexRoute = -1;

    private View viewLastCell = null;

    private Button buttonItinerary;
    private Button buttonDeleteRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        setUpMapIfNeeded();

        fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        map = fragment.getMap();

        configureSizes();

        Button buttonCreateRoute = (Button) findViewById(R.id.buttonCreateRoute);
        buttonCreateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateRouteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        buttonItinerary = (Button) findViewById(R.id.buttonItinerary);
        buttonItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexRoute != -1) {
                    Intent intent = new Intent(v.getContext(), ItineraryActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione uma rota", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonItinerary.setVisibility(View.INVISIBLE);

        buttonDeleteRoute = (Button) findViewById(R.id.buttonDeleteRoute);
        buttonDeleteRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexRoute != -1) {
                    if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                        final ProgressWait progress;
                        progress = new ProgressWait(passThis, "Deletando");
                        progress.show();

                        final View passView = v;

                        AzureService.getInstance().getRouteController().deleteRoute(new AzureResponse<RouteItem>() {
                            @Override
                            public void receive(MobileServiceList<RouteItem> result) {
                                progress.dismiss();
                                LatLng sorocaba = MapUtils.address2LatLng(passView.getContext(), "Mexico");
                                LatLng saoPaulo = MapUtils.address2LatLng(passView.getContext(), "Angola");
                                latlngBounds = createLatLngBoundsObject(sorocaba, saoPaulo);
                                indexRoute = -1;
                                cleanCell();
                                buttonDeleteRoute.setVisibility(View.INVISIBLE);
                                buttonItinerary.setVisibility(View.INVISIBLE);
                                map.clear();
                                listRoutesLoad();

                                map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
                                Toast.makeText(getApplicationContext(), "Deletado", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void fail() {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Falha ao deletar", Toast.LENGTH_SHORT).show();
                            }
                        }, routes.get(indexRoute));
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Offline", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione uma rota", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonDeleteRoute.setVisibility(View.INVISIBLE);
        listRoutesLoad();
    }

    private void listRoutesLoad() {
        final ProgressWait progress;
        progress = new ProgressWait(passThis, "Buscando Rotas");
        progress.show();

        AzureService.getInstance().getRouteController().getRoutes(new AzureResponse<RouteItem>() {
            @Override
            public void receive(MobileServiceList<RouteItem> result) {
                routes = new ArrayList<RouteItem>();
                routes.addAll(result);

                String[] menuArray = new String[routes.size()];
                int index = 0;
                for (RouteItem route : routes) {
                    menuArray[index] = route.from.name.toUpperCase() + " a " + route.to.name.toUpperCase();
                    index++;
                }

                listViewRoute = (ListView) findViewById(R.id.listViewRoutes);
                arrayAdapter = new ArrayAdapter(passThis, android.R.layout.simple_list_item_1, menuArray);
                listViewRoute.setAdapter(arrayAdapter);

                listViewRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                            cleanCell();
                            view.setBackgroundColor(Color.rgb(100, 149, 237));
                            viewLastCell = view;
                            indexRoute = position;
                            buttonDeleteRoute.setVisibility(View.VISIBLE);
                            buttonItinerary.setVisibility(View.VISIBLE);
                            RouteItem route = routes.get(indexRoute);
                            ProviderService.getInstance().getSessionItemView().routeItem = route;
                            if (route.from != null && route.to != null) {
                                findDirections(Double.valueOf(route.from.latitud), Double.valueOf(route.from.longitud), Double.valueOf(route.to.latitud), Double.valueOf(route.to.longitud), GMapV2Direction.MODE_DRIVING);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Internet Offline", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                progress.dismiss();
            }

            @Override
            public void fail() {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Falha ao buscar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        indexRoute = -1;
        cleanCell();
        buttonDeleteRoute.setVisibility(View.INVISIBLE);
        buttonItinerary.setVisibility(View.INVISIBLE);
        map.clear();

        LatLng mexico = MapUtils.address2LatLng(this, "Mexico");
        LatLng angola = MapUtils.address2LatLng(this, "Angola");
        if (mexico != null && angola != null) {
            latlngBounds = createLatLngBoundsObject(mexico, angola);

            map.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }
        if (newPolyline != null) {
            newPolyline.remove();
        }
        newPolyline = map.addPolyline(rectLine);
        RouteItem route = routes.get(indexRoute);
        LatLng from = new LatLng(Double.valueOf(route.from.latitud), Double.valueOf(route.from.longitud));
        LatLng to = new LatLng(Double.valueOf(route.to.latitud), Double.valueOf(route.to.longitud));
        latlngBounds = createLatLngBoundsObject(from, to);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
    }

    @Override
    public Context getActivity() {
        return passThis;
    }

    private void configureSizes() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = (int) ((double) size.y * 0.7);

        fragment.getView().getLayoutParams().width = width;
        fragment.getView().getLayoutParams().height = height;
    }

    private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation) {
        if (firstLocation != null && secondLocation != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(firstLocation).include(secondLocation);

            return builder.build();
        }
        return null;
    }

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);
    }

    public void cleanCell() {
        if (viewLastCell != null) {
            viewLastCell.setBackgroundColor(Color.TRANSPARENT);
            viewLastCell = null;
        }
    }
}
