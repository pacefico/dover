package com.example.fugliara.ccs002.views.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fugliara.ccs002.R;
import com.example.fugliara.ccs002.server.AzureResponse;
import com.example.fugliara.ccs002.server.AzureService;
import com.example.fugliara.ccs002.server.dataobjects.UserRatingItem;
import com.example.fugliara.ccs002.views.fragments.ProgressWait;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public class PointsActivity extends AppCompatActivity {

    private static final String TAG = "TAG_PointsActivity";
    private MobileServiceList<UserRatingItem> ratings = null;
    private ListView listViewRatings;
    private ArrayAdapter arrayAdapter;
    private PointsActivity passThis = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        if (AzureService.getInstance().isNetworkAvailable(getApplicationContext())) {
            final ProgressWait progress;
            progress = new ProgressWait(passThis, "Buscando");
            progress.show();

            AzureService.getInstance().getUserController().getUserRating(new AzureResponse<UserRatingItem>() {
                @Override
                public void receive(MobileServiceList<UserRatingItem> result) {
                    if (result.size() > 0) {
                        try {
                            ratings = result;

                            String[] menuArray = new String[ratings.size()];
                            int index = 0;
                            for (UserRatingItem rating : ratings) {
                                menuArray[index] = rating.userSource.name + " : " + rating.value;
                                index++;
                            }

                            listViewRatings = (ListView) findViewById(R.id.listViewPoints);
                            arrayAdapter = new ArrayAdapter(passThis, android.R.layout.simple_list_item_1, menuArray);
                            listViewRatings.setAdapter(arrayAdapter);
                        } catch (Exception exception) {
                            Intent intent = new Intent(passThis, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Toast.makeText(passThis, "Falha ao exibir pontuação", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pontuação envio() Error: " + exception.getMessage());
                        }
                    } else {
                        Intent intent = new Intent(passThis, ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(passThis, "Não possui pontuação", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }

                @Override
                public void fail() {
                    progress.dismiss();
                    Intent intent = new Intent(passThis, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(passThis, "Falha ao buscar", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent intent = new Intent(passThis, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(passThis, "Internet Offline", Toast.LENGTH_SHORT).show();
        }
    }
}
