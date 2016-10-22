package com.example.fugliara.ccs002.views.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fugliara.ccs002.R;

public class ProfileActivity extends AppCompatActivity {

    private String[] menuArray = {"Qualificação", "Convidar Novo Usuario"};

    private ListView listViewMenu;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        listViewMenu = (ListView) findViewById(R.id.listViewProfile);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, menuArray);
        listViewMenu.setAdapter(arrayAdapter);

        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(view.getContext(), PointsActivity.class);
                        break;
                    case 1:
                        intent = new Intent(view.getContext(), ProfileInviteActivity.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
