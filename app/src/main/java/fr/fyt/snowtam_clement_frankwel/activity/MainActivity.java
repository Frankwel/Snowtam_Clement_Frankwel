package fr.fyt.snowtam_clement_frankwel.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import fr.fyt.snowtam_clement_frankwel.R;
import fr.fyt.snowtam_clement_frankwel.utility.ListViewAdapter;

public class MainActivity extends AppCompatActivity {

    ListView codeListView;
    EditText editTextCode;
    ImageButton ibValidate;
    Button btnSearch;

    ListViewAdapter adapter;
    ArrayList<String> codeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // elements present on layout
        editTextCode = (EditText)findViewById(R.id.editTextCode);
        ibValidate = (ImageButton)findViewById(R.id.ibValidate);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        codeListView = (ListView)findViewById(R.id.codeListView);

        codeList = new ArrayList<String>();

        btnSearch.setVisibility(View.INVISIBLE); //make btnSearch unusable while codeList is empty

        adapter = new ListViewAdapter(MainActivity.this, android.R.layout.simple_list_item_1, codeList);
        codeListView.setAdapter(adapter);

        ibValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextCode.getText().toString().length()!=4){
                    new MaterialDialog.Builder(MainActivity.this)
                            .title(getString(R.string.bad_code))
                            .content(getString(R.string.message_bad_code))
                            .positiveText(getString(R.string.ok))
                            .show();
                }
                else{
                    codeList.add(editTextCode.getText().toString());
                    editTextCode.setText(""); //set the edittextCode empty
                    adapter = new ListViewAdapter(MainActivity.this, android.R.layout.simple_list_item_1, codeList);
                    codeListView.setAdapter(adapter);

                    btnSearch.setVisibility(View.VISIBLE); //make btnSearch usable due to codeList contain element

                    if(codeList.size() == 5){
                        ibValidate.setVisibility(View.INVISIBLE);
                        editTextCode.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        //action for btnSearch
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DecodingActivity.class);
                startActivity(intent);
            }
        });

        codeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                final String item = (String)parent.getItemAtPosition(position);
                view.animate().setDuration(500).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                      // supress an element in the ListView
                        codeList.remove(item);
                       adapter.notifyDataSetChanged();
                       view.setAlpha(1);

                        ibValidate.setVisibility(View.VISIBLE);
                        editTextCode.setVisibility(View.VISIBLE);

                        if(codeList.isEmpty()){
                            btnSearch.setVisibility(View.INVISIBLE); //make btnSearch unusable because codeList is empty
                        }
                    }
                });
            }
        });

    }
}
