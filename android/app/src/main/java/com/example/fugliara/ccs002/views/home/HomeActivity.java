package com.example.fugliara.ccs002.views.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.views.itinerary.RouteActivity;
import com.example.fugliara.ccs002.views.order.OrderActivity;
import com.example.fugliara.ccs002.views.profile.ProfileActivity;
import com.example.fugliara.ccs002.views.tracking.TrackingActivity;

public class HomeActivity extends AppCompatActivity {
    private String[] menuArray = {"Quero Transportar", "Quero Enviar", "Acompanhar Transporte", "Minha Conta"};

    private ListView listViewMenu;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listViewMenu = (ListView) findViewById(R.id.listViewMenu);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, menuArray);
        listViewMenu.setAdapter(arrayAdapter);

        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                            intent = new Intent(view.getContext(), RouteActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        } else {
                            Toast.makeText(getApplicationContext(), "Internet Offline", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        intent = new Intent(view.getContext(), OrderActivity.class);
                        break;
                    case 2:
                        intent = new Intent(view.getContext(), TrackingActivity.class);
                        break;
                    case 3:
                        intent = new Intent(view.getContext(), ProfileActivity.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
