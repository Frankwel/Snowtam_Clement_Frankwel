package fr.fyt.snowtam_clement_frankwel.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.fyt.snowtam_clement_frankwel.R;
import fr.fyt.snowtam_clement_frankwel.model.ListViewAdapter;
import fr.fyt.snowtam_clement_frankwel.model.Snowtam;

/**
 * usefull link: https://www.icao.int/safety/Pages/default.aspx
 */
public class MainActivity extends AppCompatActivity {

    ListView codeListView;
    EditText editTextCode;
    ImageButton ibValidate;
    Button btnSearch;

    ListViewAdapter adapter;
    ArrayList<String> codeList;

    ArrayList<Snowtam> allSnowtam;

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
        allSnowtam = new ArrayList<Snowtam>();

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
                if(internetAvailable()){
                    ArrayList<String> queryResponse = new ArrayList<>();
                    for(int i=0; i<codeList.size(); i++){
                        queryResponse.add(getSnowtam(codeList.get(i)));
                    }
                    allSnowtam = decodingSnowtam(queryResponse);

                    Intent intent = new Intent(MainActivity.this, DecodingActivity.class);

                    Gson gson = new Gson();
                    String jsonSnowtam = gson.toJson(allSnowtam);

                    //send the list of codes to DecodingActivity
                    intent.putExtra("allSnowtam", jsonSnowtam);

                    startActivity(intent);
                }
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


    private ArrayList<Snowtam> decodingSnowtam(List<String> snowtamList){
        ArrayList<Snowtam> listToReturn = new ArrayList<>();

        Snowtam snowtam;
        for(int i=0; i<snowtamList.size(); i++){
            snowtam = new Snowtam(codeList.get(i), snowtamList.get(i), "valeur décodée");
            listToReturn.add(snowtam);
        }
        return listToReturn;
    }

    /**
     * this function make query to find snowtam online
     * @param code
     * @return string of snowtam corresponding
     */
    private String getSnowtam(String code){
        String snowtam = "ERROR";

            //faire le code pour recupérer un snowtam en ligne à partir du code en paramètre

        snowtam = "code retrouvé";
        return snowtam;
    }

    /**
     * This function look for internet connexion to make query online after
     * @return a boolean
     */
    private boolean internetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
