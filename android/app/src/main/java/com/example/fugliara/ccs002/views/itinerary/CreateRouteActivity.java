package com.example.fugliara.ccs002.views.itinerary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.RouteItem;
import com.example.fugliara.ccs002.views.fragments.MapInterface;
import com.example.fugliara.ccs002.views.fragments.MapUtils;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.ArrayList;

public class CreateRouteActivity extends AppCompatActivity implements MapInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        Button buttonCreateByAddress = (Button) findViewById(R.id.buttonCreateByAddress);
        buttonCreateByAddress.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                    EditText editTextFrom = (EditText) findViewById(R.id.editTextFrom);
                    EditText editTextTo = (EditText) findViewById(R.id.editTextTo);

                    String from = editTextFrom.getText().toString();
                    String to = editTextTo.getText().toString();

                    callCreate(from, to);
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void callCreate(String from, String to) {
        final CreateRouteActivity passThis = this;
        if ((!from.isEmpty()) && (!to.isEmpty())) {
            LatLng fromCoord = MapUtils.address2LatLng(this, from);
            LatLng toCoord = MapUtils.address2LatLng(this, to);
            if (fromCoord != null && toCoord != null) {
                final ProgressWait progress;
                progress = new ProgressWait(this, "Criando");
                progress.show();

                AzureService.getInstance().getRouteController().createRoute(new AzureResponse<RouteItem>() {
                    @Override
                    public void receive(MobileServiceList<RouteItem> result) {
                        progress.dismiss();
                        Intent intent = new Intent(passThis, RouteActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Criada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail() {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Falha ao criar", Toast.LENGTH_SHORT).show();
                    }
                }, from, to, fromCoord, toCoord);
            } else {
                Toast.makeText(getApplicationContext(), "Origem ou Destino inválidos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Valores inválidos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {

    }

    @Override
    public Context getActivity() {
        return this;
    }
}
