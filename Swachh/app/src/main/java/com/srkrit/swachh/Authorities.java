package com.srkrit.swachh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Authorities extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String url = "http://www.srkrit.in/swachh/all_users";
    private ProgressDialog pDialog;
    ListView lview;
    ListViewAdapter lviewAdapter;
    Activity mActivity;


    List<String> month,number,usernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorities);

        mActivity=this;


        Transition exitTrans = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            exitTrans = new Explode();
            getWindow().setExitTransition(exitTrans);

            Transition reenterTrans = new Explode();
            getWindow().setReenterTransition(reenterTrans);
        }

        lview = (ListView) findViewById(R.id.all_list);

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading...");
        pDialog.show();

        month=new ArrayList<String>();
        number=new ArrayList<String>();
        usernames=new ArrayList<String>();


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

                                JSONObject obj=obj1.getJSONObject("Swachhuser");

                                month.add(obj.getString("name")+" #"+obj.getString("username"));
                                usernames.add(obj.getString("username"));
                                number.add(obj.getString("description"));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        lviewAdapter.notifyDataSetChanged();


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        hidePDialog();
                        Toast.makeText(mActivity, "No results. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.e("res1: ", String.valueOf(error));
                    }

                });


        AppController.getInstance().addToRequestQueue(strRequest);

        lviewAdapter = new ListViewAdapter(this, month, number);

        lview.setAdapter(lviewAdapter);

        lview.setOnItemClickListener(this);




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }


    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

        Intent reportIntent=new Intent(mActivity,ReportIssue.class);
        reportIntent.putExtra("username",usernames.get(position));
        startActivity(reportIntent);
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
