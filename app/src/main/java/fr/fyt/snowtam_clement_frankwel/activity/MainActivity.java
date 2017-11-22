package fr.fyt.snowtam_clement_frankwel.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
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
import fr.fyt.snowtam_clement_frankwel.model.Converter;
import fr.fyt.snowtam_clement_frankwel.model.ListViewAdapter;
import fr.fyt.snowtam_clement_frankwel.model.Snowtam;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
                    codeList.add(editTextCode.getText().toString().toUpperCase());
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
                        //queryResponse.add(getSnowtam(codeList.get(i)));
                        queryResponse.add("SNOWTAM 0311\n" +
                                "A) ENSB\n" +
                                "B) 10130958 C) 10\n" +
                                "F) 7/7/7 G) XX/XX/XX H) 4/4/3\n" +
                                "N) ALL REPORTED TWYS/2\n" +
                                "R) ALL REPORTED APRONS/2\n" +
                                "T) CONTAMINATION/100/100/100/PERCENT");
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


    /**
     * this function is used to decode snowtam
     */
    private ArrayList<Snowtam> decodingSnowtam(List<String> snowtamList){
        ArrayList<Snowtam> listToReturn = new ArrayList<>();

        Snowtam snowtam = new Snowtam();
        Converter converter = new Converter();

        for(int i=0; i<snowtamList.size(); i++){
            //snowtam.setKey(codeList.get(i));
            snowtam.setCode(snowtamList.get(i));
            snowtam.setResult("");

            String lines[] = snowtamList.get(i).split("\n");
            for (int j=0; j<lines.length; j++){
                String elements[] = lines[j].split("\\)");
                for(int k=0; k<elements.length-1; k++){
                    //take the last char of elements[k] and look for
                    String separator = elements[k].substring(elements[k].length() - 1);
                    switch (separator)
                    {
                        case "A":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.location) + elements[k+1].substring(0, elements[k+1].length()-1) + "\n");
                                snowtam.setKey(elements[k+1].substring(0, elements[k+1].length()-1));
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.location) + elements[k+1].substring(0, elements[k+1].length()) + "\n");
                                snowtam.setKey(elements[k+1].substring(0, elements[k+1].length()));
                            }
                            break;
                        case "B":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.time) + converter.convertTime(elements[k+1].substring(0, elements[k+1].length()-1).trim()) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.time) + converter.convertTime(elements[k+1].substring(0, elements[k+1].length()).trim()) + "\n");
                            }
                            break;
                        case "C":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.runway) + elements[k+1].substring(0, elements[k+1].length()-1) + " " + getString(R.string.runway_unit) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.runway) + elements[k+1].substring(0, elements[k+1].length()) + " " + getString(R.string.runway_unit) + "\n");
                            }
                            break;
                        case "D":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.runway_lenght) + elements[k+1].substring(0, elements[k+1].length()-1) + " " + getString(R.string.runway_unit) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.runway_lenght) + elements[k+1].substring(0, elements[k+1].length()) + " " + getString(R.string.runway_unit) + "\n");
                            }
                            break;
                        case "E":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.runway_width) + elements[k+1].substring(0, elements[k+1].length()-1) + " " + getString(R.string.runway_unit) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.runway_width) + elements[k+1].substring(0, elements[k+1].length()) + " " + getString(R.string.runway_unit) + "\n");
                            }
                            break;
                        case "F":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.deposit_depth) + converter.convertCondition(elements[k+1].substring(0, elements[k+1].length()-1).trim()) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.deposit_depth) + converter.convertCondition(elements[k+1].substring(0, elements[k+1].length()).trim()) + "\n");
                            }
                            break;
                        case "G":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.mean_depth) + elements[k+1].substring(0, elements[k+1].length()-1) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.mean_depth) + elements[k+1].substring(0, elements[k+1].length()) + "\n");
                            }
                            break;
                        case "H":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.bracking_action) + converter.convertBreakingAction(elements[k+1].substring(0, elements[k+1].length()-1).trim()) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.bracking_action) + converter.convertBreakingAction(elements[k+1].substring(0, elements[k+1].length()).trim()) + "\n");
                            }
                            break;
                        case "N":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.taxiway) + elements[k+1].substring(0, elements[k+1].length()-1) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.taxiway) + elements[k+1].substring(0, elements[k+1].length()) + "\n");
                            }
                            break;
                        case "T":
                            //test if the before last char is a blank space
                            if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                                snowtam.setResult(snowtam.getResult() + getString(R.string.remark) + elements[k+1].substring(0, elements[k+1].length()-1) + "\n");
                            }
                            else {
                                snowtam.setResult(snowtam.getResult() + getString(R.string.remark) + elements[k+1].substring(0, elements[k+1].length()) + "\n");
                            }
                            break;
                    }
                }
            }
            listToReturn.add(snowtam);
        }
        return listToReturn;
    }

    /**
     * this function make query to find snowtam online
     * @param code
     * @return snowTam code
     */
    //TODO Mettre le string result dans le string snowTam
    private String getSnowtam(String code){
        String url = "https://v4p4sz5ijk.execute-api.us-east-1.amazonaws.com/anbdata/states/notams/notams-list?api_key=bae907e0-ce02-11e7-81ce-1fabb66fbe08&format=json&type=&Qcode=&locations="+code+"&qstring=&states=&ICAOonly=";
        final String snowTam = "";

        final RequestQueue rqtRq = Volley.newRequestQueue(MainActivity.this);

        StringRequest stgRq = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.substring(response.indexOf("SNOWTAM"), response.indexOf(".)"));
                        rqtRq.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String result = "Something went wrong...";
                error.printStackTrace();
                rqtRq.stop();
            }
        });
        rqtRq.add(stgRq);


        return snowTam;
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
