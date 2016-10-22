package com.example.fugliara.ccs002.views.itinerary;

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
import com.example.fugliara.ccs002.server.dataobjects.ItineraryItem;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.example.fugliara.ccs002.views.provider.ProviderService;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.ArrayList;

public class ItineraryActivity extends AppCompatActivity {

    private ArrayList<ItineraryItem> itineraries = null;
    private ListView listViewIt;
    private ArrayAdapter arrayAdapter;
    private int indexItinerary = -1;
    private View viewLastCell = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ItineraryActivity passThis = this;
        itineraries = new ArrayList<ItineraryItem>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        Button buttonCreateIt = (Button) findViewById(R.id.buttonCreateIt);
        buttonCreateIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
                    EditText editTextDateIt = (EditText) findViewById(R.id.editTextDateIt);
                    EditText editTextTimeIt = (EditText) findViewById(R.id.editTextTimeIt);
                    String date = editTextDateIt.getText().toString();
                    String time = editTextTimeIt.getText().toString();
                    if ((!date.isEmpty()) && (!time.isEmpty())) {
                        final ProgressWait progress;
                        progress = new ProgressWait(passThis, "Criando");
                        progress.show();

                        AzureService.getInstance().getItineraryController().createItinerary(new AzureResponse<ItineraryItem>() {
                            @Override
                            public void receive(MobileServiceList<ItineraryItem> result) {
                                progress.dismiss();
                                indexItinerary = -1;
                                cleanCell();
                                listItineraryLoad();
                                Toast.makeText(getApplicationContext(), "Criado", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void fail() {
                                Toast.makeText(getApplicationContext(), "Falha ao criar", Toast.LENGTH_SHORT).show();
                            }
                        }, ProviderService.getInstance().getSessionItemView().routeItem, date, time);
                    } else {
                        Toast.makeText(getApplicationContext(), "Valores inválidos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonDeleteIt = (Button) findViewById(R.id.buttonDeleteIt);
        buttonDeleteIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexItinerary < 0) {
                    Toast.makeText(getApplicationContext(), "Selecione um Itinerário", Toast.LENGTH_SHORT).show();
                } else {
                    if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {

                        final ProgressWait progress;
                        progress = new ProgressWait(passThis, "Deletando");
                        progress.show();

                        AzureService.getInstance().getItineraryController().deleteItinerary(new AzureResponse<ItineraryItem>() {
                            @Override
                            public void receive(MobileServiceList<ItineraryItem> result) {
                                progress.dismiss();
                                indexItinerary = -1;
                                cleanCell();
                                listItineraryLoad();
                                Toast.makeText(getApplicationContext(), "Deletado", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void fail() {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Falha ao deletar", Toast.LENGTH_SHORT).show();
                            }
                        }, itineraries.get(indexItinerary));
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Offline", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        listItineraryLoad();
    }

    private void listItineraryLoad() {
        final ItineraryActivity passThis = this;

        final ProgressWait progress;
        progress = new ProgressWait(passThis, "Buscando");
        progress.show();

        AzureService.getInstance().getItineraryController().getItinerary(new AzureResponse<ItineraryItem>() {
            @Override
            public void receive(MobileServiceList<ItineraryItem> result) {
                progress.dismiss();
                itineraries.clear();
                itineraries.addAll(result);
                String[] menuArray = new String[itineraries.size()];
                int index = 0;
                for (ItineraryItem itinerary : itineraries) {
                    menuArray[index] = itinerary.time + " - " + itinerary.date;
                    index++;
                }

                listViewIt = (ListView) findViewById(R.id.listViewIt);
                arrayAdapter = new ArrayAdapter(passThis, android.R.layout.simple_list_item_1, menuArray);
                listViewIt.setAdapter(arrayAdapter);

                listViewIt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        indexItinerary = position;
                        cleanCell();
                        view.setBackgroundColor(Color.rgb(100, 149, 237));
                        viewLastCell = view;
                    }
                });
            }

            @Override
            public void fail() {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Falhas ao buscar", Toast.LENGTH_SHORT).show();
            }
        }, ProviderService.getInstance().getSessionItemView().routeItem);
    }

    public void cleanCell() {
        if (viewLastCell != null) {
            viewLastCell.setBackgroundColor(Color.TRANSPARENT);
            viewLastCell = null;
        }
    }
}
