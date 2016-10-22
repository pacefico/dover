package com.example.fugliara.ccs002.views.tracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.OrderItem;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.home.HomeActivity;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public class TrackingActivity extends AppCompatActivity {

    private static final String TAG = "TAG_TrackingActivity";
    private MobileServiceList<OrderItem> orders = null;
    private ListView listViewOrders;
    private ArrayAdapter arrayAdapter;
    private TrackingActivity passThis = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
            final ProgressWait progress;
            progress = new ProgressWait(passThis, "Buscando");
            progress.show();

            AzureService.getInstance().getOrderController().getOrder(new AzureResponse<OrderItem>() {
                @Override
                public void receive(MobileServiceList<OrderItem> result) {
                    if (result.size() > 0) {
                        try {
                            orders = result;

                            String[] menuArray = new String[orders.size()];
                            int index = 0;
                            for (OrderItem order : orders) {
                                menuArray[index] = order.route.from.name.toUpperCase() + " a " + order.route.to.name.toUpperCase() + " (" + order.route.user.name + ")";
                                index++;
                            }

                            listViewOrders = (ListView) findViewById(R.id.listViewAgendamentos);
                            arrayAdapter = new ArrayAdapter(passThis, android.R.layout.simple_list_item_1, menuArray);
                            listViewOrders.setAdapter(arrayAdapter);

                            listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(passThis, StatusActivity.class);
                                    intent.putExtra("orderId", orders.get(position).id);
                                    startActivity(intent);
                                }
                            });
                        } catch (Exception exception) {
                            Intent intent = new Intent(passThis, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Toast.makeText(passThis, "Falha ao exibir envio", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "buscar envio() Error: " + exception.getMessage());
                        }
                    } else {
                        Intent intent = new Intent(passThis, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(passThis, "Não possui envios", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }

                @Override
                public void fail() {
                    progress.dismiss();
                    Intent intent = new Intent(passThis, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(passThis, "Falha ao buscar", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent intent = new Intent(passThis, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(passThis, "Internet Offline", Toast.LENGTH_SHORT).show();
        }
    }
}
