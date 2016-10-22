package com.example.fugliara.ccs002.views.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.LatLnItem;
import com.example.fugliara.ccs002.server.dataobjects.OrderRequestItem;
import com.example.fugliara.ccs002.server.dataobjects.RouteItem;
import com.example.fugliara.ccs002.views.fragments.MapUtils;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private List<RouteItem> routes = null;
    private ListView listViewRoute;
    private ArrayAdapter arrayAdapter;
    private OrderActivity passThis = this;
    private View viewLastCell = null;

    private int indexRoute = -1;

    private Button buttonSendOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Button buttonFindRoute = (Button) findViewById(R.id.buttonFind);
        buttonFindRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                    EditText editTextFrom = (EditText) findViewById(R.id.editTextFromSend);
                    EditText editTextTo = (EditText) findViewById(R.id.editTextToSend);
                    EditText editTextRaio = (EditText) findViewById(R.id.editTextRaio);

                    indexRoute = -1;
                    cleanCell();
                    buttonSendOrder.setVisibility(View.GONE);

                    if (!editTextFrom.getText().toString().isEmpty() && !editTextTo.getText().toString().isEmpty() && !editTextRaio.getText().toString().isEmpty()) {
                        LatLng fromCoord = MapUtils.address2LatLng(passThis, editTextFrom.getText().toString());
                        LatLng toCoord = MapUtils.address2LatLng(passThis, editTextTo.getText().toString());
                        if (fromCoord != null && toCoord != null) {
                            LatLnItem fromItem = new LatLnItem(editTextFrom.getText().toString(), String.valueOf(fromCoord.latitude), String.valueOf(fromCoord.longitude));
                            LatLnItem toItem = new LatLnItem(editTextTo.getText().toString(), String.valueOf(toCoord.latitude), String.valueOf(toCoord.longitude));

                            final ProgressWait progress;
                            progress = new ProgressWait(passThis, "Buscando");
                            progress.show();

                            AzureService.getInstance().getOrderController().getOrderRequest(new AzureResponse<OrderRequestItem>() {
                                @Override
                                public void receive(MobileServiceList<OrderRequestItem> result) {
                                    if (result.size() > 0) {
                                        routes = result.get(0).routeItens;
                                        if (routes.size() > 0) {
                                            String[] menuArray = new String[routes.size()];
                                            int index = 0;
                                            for (RouteItem route : routes) {
                                                menuArray[index] = route.from.name.toUpperCase() + " a " + route.to.name.toUpperCase() + " (" + route.user.name + ")";
                                                index++;
                                            }

                                            listViewRoute = (ListView) findViewById(R.id.listViewFind);
                                            arrayAdapter = new ArrayAdapter(passThis, android.R.layout.simple_list_item_1, menuArray);
                                            listViewRoute.setAdapter(arrayAdapter);

                                            listViewRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    indexRoute = position;
                                                    cleanCell();
                                                    view.setBackgroundColor(Color.rgb(100, 149, 237));
                                                    viewLastCell = view;
                                                    buttonSendOrder.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        } else {
                                            Toast.makeText(passThis, "Rotas não encontradas", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(passThis, "Falha ao buscar rota", Toast.LENGTH_SHORT).show();
                                    }
                                    progress.dismiss();
                                }

                                @Override
                                public void fail() {
                                    progress.dismiss();
                                    Toast.makeText(passThis, "Falha ao buscar", Toast.LENGTH_SHORT).show();
                                }
                            }, fromItem, toItem, Integer.valueOf(editTextRaio.getText().toString()));
                        } else {
                            Toast.makeText(passThis, "Origem, Destino ou Distância inválidos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(passThis, "Origem, Destino ou Distância inválidos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(passThis, "Internet Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSendOrder = (Button) findViewById(R.id.buttonSendOrder);
        buttonSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexRoute != -1) {
                    ProviderService.getInstance().getSessionItemView().routeItem = routes.get(indexRoute);
                    Intent intent = new Intent(passThis, ProductActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(passThis, "Selecione uma rota", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonSendOrder.setVisibility(View.GONE);
    }

    public void cleanCell() {
        if (viewLastCell != null) {
            viewLastCell.setBackgroundColor(Color.TRANSPARENT);
            viewLastCell = null;
        }
    }
}
