package fr.fyt.snowtam_clement_frankwel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

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
    ArrayList<String> queryResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // elements present on layout
        editTextCode = (EditText)findViewById(R.id.editTextCode);
        ibValidate = (ImageButton)findViewById(R.id.ibValidate);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        codeListView = (ListView)findViewById(R.id.codeListView);

        codeList = new ArrayList<>();
        queryResponse = new ArrayList<>();

        btnSearch.setVisibility(View.INVISIBLE); //make btnSearch unusable while codeList is empty

        adapter = new ListViewAdapter(MainActivity.this, android.R.layout.simple_list_item_1, codeList);
        codeListView.setAdapter(adapter);


        ibValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                if(editTextCode.getText().toString().trim().length()!=4){
                    new MaterialDialog.Builder(MainActivity.this)
                            .title(getString(R.string.bad_code))
                            .content(getString(R.string.message_bad_code))
                            .positiveText(getString(R.string.ok))
                            .show();
                }
                else if(codeList.contains(editTextCode.getText().toString().trim().toUpperCase())){
                    new MaterialDialog.Builder(MainActivity.this)
                            .title(getString(R.string.existed_code))
                            .content(getString(R.string.message_existed_code))
                            .positiveText(getString(R.string.ok))
                            .show();
                }
                else{
                    codeList.add(editTextCode.getText().toString().trim().toUpperCase());
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
               Loading loading = new Loading();
               loading.execute();
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
     * Displays snowtam in decoded form
     */
    private Snowtam decodingSnowtam(String key, String oneSnowtam){
        Snowtam snowtam = new Snowtam();
        Converter converter = new Converter();

        snowtam.setKey(key);
        snowtam.setCode(oneSnowtam);
        snowtam.setResult("");

        String lines[] = oneSnowtam.split("\n");
        for (int j=0; j<lines.length; j++){
            String elements[] = lines[j].split("\\)");
            for(int k=0; k<elements.length-1; k++){
                //take the last char of elements[k] and look for
                String separator = elements[k].substring(elements[k].length() - 1);
                switch (separator) {
                    case "A":
                        //Test if the previous last character is an empty space
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.location) + elements[k+1].substring(0, elements[k+1].length()-1) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.location) + elements[k+1].substring(0, elements[k+1].length()-3) + "\n");
                        }
                        break;
                    case "B":
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.time) + converter.convertTime(elements[k+1].substring(0, elements[k+1].length()-1).trim()) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.time) + converter.convertTime(elements[k+1].substring(0, elements[k+1].length()).trim()) + "\n");
                        }
                        break;
                    case "C":
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.runway) + elements[k+1].substring(0, elements[k+1].length()-1) + " " + getString(R.string.runway_unit) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.runway) + elements[k+1].substring(0, elements[k+1].length()-3) + " " + getString(R.string.runway_unit) + "\n");
                        }
                        break;
                    case "D":
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.runway_lenght) + elements[k+1].substring(0, elements[k+1].length()-1) + " " + getString(R.string.runway_unit) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.runway_lenght) + elements[k+1].substring(0, elements[k+1].length()) + " " + getString(R.string.runway_unit) + "\n");
                        }
                        break;
                    case "E":
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.runway_width) + elements[k+1].substring(0, elements[k+1].length()-1) + " " + getString(R.string.runway_unit) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.runway_width) + elements[k+1].substring(0, elements[k+1].length()) + " " + getString(R.string.runway_unit) + "\n");
                        }
                        break;
                    case "F":
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.deposit_depth) + converter.convertCondition(elements[k+1].substring(0, elements[k+1].length()-1).trim()) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.deposit_depth) + converter.convertCondition(elements[k+1].substring(0, elements[k+1].length()).trim()) + "\n");
                        }
                        break;
                    case "G":
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.mean_depth) + elements[k+1].substring(0, elements[k+1].length()-1) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.mean_depth) + elements[k+1].substring(0, elements[k+1].length()) + "\n");
                        }
                        break;
                    case "H":
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.bracking_action) + converter.convertBreakingAction(elements[k+1].substring(0, elements[k+1].length()-1).trim()) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.bracking_action) + converter.convertBreakingAction(elements[k+1].substring(0, elements[k+1].length()-3).trim()) + "\n");
                        }
                        break;
                    case "N":
                        if(elements[k+1].substring(elements[k+1].length()-2, elements[k+1].length()-1).equals(" ")){
                            snowtam.setResult(snowtam.getResult() + getString(R.string.taxiway) + elements[k+1].substring(0, elements[k+1].length()-1) + "\n");
                        }
                        else {
                            snowtam.setResult(snowtam.getResult() + getString(R.string.taxiway) + elements[k+1].substring(0, elements[k+1].length()-3) + "\n");
                        }
                        break;
                    case "T":
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
        return snowtam;
    }

    /**
     * This function look for internet connexion
     * @return a boolean
     */
    private boolean internetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Displays a spinner while the system performs the recovery request for the SnowTam
     */
    public class Loading extends AsyncTask<Void, Integer, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Recherche des snowtams en cours ..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            //progressDialog.setCancelable(false);
            progressDialog.show(); // Display Progress Dialog
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            if(internetAvailable()) {
                final ArrayList<Snowtam> allSnowtam = new ArrayList<>();
                for (int i = 0; i < codeList.size(); i++) {
                    String url = "https://v4p4sz5ijk.execute-api.us-east-1.amazonaws.com/anbdata/states/notams/notams-list?api_key=bae907e0-ce02-11e7-81ce-1fabb66fbe08&format=json&type=&Qcode=&locations=" + codeList.get(i) + "&qstring=&states=&ICAOonly=";
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    final int finalI = i;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("SNOWTAM")) {
                                        String result = response.substring(response.indexOf("SNOWTAM"), response.indexOf(".)"));
                                        final Snowtam[] snowtam = {decodingSnowtam(codeList.get(finalI), result)};

                                        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+codeList.get(finalI)+"&key=AIzaSyBxcMtkBIT2IU6VydoJB4yfoT-f3nSzY3Y";

                                        final RequestQueue rqtRq = Volley.newRequestQueue(MainActivity.this);

                                        StringRequest stgRq = new StringRequest(Request.Method.GET, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {/*
                                                        String lat = response.substring(response.indexOf("lat")+7, response.indexOf("lng")-18);
                                                        String lng = response.substring(response.indexOf("lng")+7, response.indexOf("location_type")-29);
                                                        String airportName = response.substring(response.indexOf("short_name")+15,response.indexOf("types")-19);
                                                        snowtam[0].setLat(parseDouble(lat));
                                                        snowtam[0].setLng(parseDouble(lng));
                                                        snowtam[0].setAirportName(airportName);*/

                                                        try {
                                                            snowtam[0] = completeSnowtam(response, snowtam[0]);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        rqtRq.stop();
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                rqtRq.stop();
                                            }
                                        });
                                        rqtRq.add(stgRq);
                                        allSnowtam.add(snowtam[0]);

                                        if(finalI == codeList.size()-1){
                                            progressDialog.cancel();
                                            Intent intent = new Intent(MainActivity.this, DecodingActivity.class);

                                            Gson gson = new Gson();
                                            String jsonSnowtam = gson.toJson(allSnowtam);

                                            //send the list of codes to DecodingActivity
                                            intent.putExtra("allSnowtam", jsonSnowtam);

                                            startActivity(intent);
                                        }
                                    } else {
                                        allSnowtam.add(new Snowtam(codeList.get(finalI), "Pas de Snowtam", "L'aeroport " + codeList.get(finalI) + " ne possède pas de snowtam en cette période de l'année"));
                                        if(finalI == codeList.size()-1){
                                            progressDialog.cancel();
                                            Intent intent = new Intent(MainActivity.this, DecodingActivity.class);

                                            Gson gson = new Gson();
                                            String jsonSnowtam = gson.toJson(allSnowtam);

                                            //send the list of codes to DecodingActivity
                                            intent.putExtra("allSnowtam", jsonSnowtam);

                                            startActivity(intent);
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            allSnowtam.add(new Snowtam(codeList.get(finalI), "Pas de Snowtam", "L'aeroport " + codeList.get(finalI) + " ne possède pas de snowtam en cette période de l'année"));
                            if(finalI == codeList.size()-1){
                                progressDialog.cancel();
                                Intent intent = new Intent(MainActivity.this, DecodingActivity.class);

                                Gson gson = new Gson();
                                String jsonSnowtam = gson.toJson(allSnowtam);

                                //send the list of codes to DecodingActivity
                                intent.putExtra("allSnowtam", jsonSnowtam);

                                startActivity(intent);
                            }
                        }
                    });
                    queue.add(stringRequest);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }


    private Snowtam completeSnowtam(String req, Snowtam snowtam) throws JSONException {

        Snowtam snowtam1 = snowtam;
        JSONObject root = new JSONObject(req);
        JSONArray results = root.getJSONArray("results");
        JSONObject jsonObject;

        jsonObject = results.getJSONObject(0);
        double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

        String airportName = jsonObject.getJSONArray("address_components").getJSONObject(0).getString("long_name");

        snowtam1.setLat(lat);
        snowtam1.setLng(lng);
        snowtam1.setAirportName(airportName);

        return snowtam1;
    }

}
