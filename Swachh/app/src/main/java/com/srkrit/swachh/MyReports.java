package com.srkrit.swachh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.srkrit.swachh.adater.CustomListAdapter;
import com.srkrit.swachh.app.AppController;
import com.srkrit.swachh.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyReports extends AppCompatActivity {
    private static final String url = "http://www.srkrit.in/swachh/myreports";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;
    Map<String,String> params;
    Activity mActivity;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        mActivity=this;

        session=SessionManager.getInstance(mActivity);

        if ((session.get("username")=="")||(session.get("username")==null)){
            startActivity(new Intent(mActivity,Main2Activity.class));
            finish();
        }


        Transition exitTrans = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            exitTrans = new Explode();
            getWindow().setExitTransition(exitTrans);

            Transition reenterTrans = new Explode();
            getWindow().setReenterTransition(reenterTrans);
        }



        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(mActivity, movieList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading...");
        pDialog.show();

        params=new HashMap<String, String>();
        params.put("username",session.get("username"));

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        hidePDialog();
                        try {
                            JSONArray jsonarr = new JSONArray(response);


                        // Parsing json
                        for (int i = 0; i < jsonarr.length(); i++) {


                                JSONObject obj1=jsonarr.getJSONObject(i);

                                JSONObject obj=obj1.getJSONObject("Swachhissue");

                                Movie movie = new Movie();

                            movie.setId(obj.getString("id"));
                            movie.setTitle(obj.getString("title"));

                            movie.setThumbnailUrl(obj.getString("image"));

                            movie.setRating(obj.getString("user"));

                            movie.setYear(obj.getString("created"));

                            movie.setStatus(obj.getString("status"));
                            movie.setAddress(obj.getString("issueaddress"));
                            movie.setLatitude(obj.getString("latitude"));
                            movie.setLongitude(obj.getString("longitude"));

                                String genre = obj.getString("description");
                                movie.setGenre(genre);

                             movieList.add(movie);




                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

                        params.clear();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        params.clear();
                        hidePDialog();
                        Toast.makeText(mActivity, "No results. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.e("res1: ", String.valueOf(error));
//							Toast.makeText(getApplicationContext(), "ERROR "+error.toString(), Toast.LENGTH_SHORT).show();
                    }

                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };


        AppController.getInstance().addToRequestQueue(strRequest);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie thisItem=movieList.get(i);

                Toast.makeText(mActivity, "title:"+thisItem.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(mActivity,ReportDetails.class);
                intent.putExtra("id", thisItem.getId());
                intent.putExtra("title", thisItem.getTitle());
                intent.putExtra("image", thisItem.getThumbnailUrl());
                intent.putExtra("user", thisItem.getRating());
                intent.putExtra("status", thisItem.getStatus());
                intent.putExtra("address", thisItem.getAddress());
                intent.putExtra("latitude", thisItem.getLatitude());
                intent.putExtra("longitude", thisItem.getLongitude());
                intent.putExtra("description", thisItem.getGenre());
                intent.putExtra("created", thisItem.getYear());
                startActivity(intent);;
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
