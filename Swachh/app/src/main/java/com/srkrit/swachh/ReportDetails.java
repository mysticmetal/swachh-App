package com.srkrit.swachh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.srkrit.swachh.app.AppController;
import com.srkrit.swachh.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportDetails extends AppCompatActivity {

    Activity mActivity;
    private ProgressDialog pDialog;
    Intent intent;
    Map<String,String> params;
    TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        mActivity=this;
        Transition exitTrans = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            exitTrans = new Explode();
            getWindow().setExitTransition(exitTrans);

            Transition reenterTrans = new Explode();
            getWindow().setReenterTransition(reenterTrans);
        }

        intent=getIntent();

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        Picasso.with(this)
                .load(intent.getStringExtra("image"))
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(imageView);

        TextView tv_title=(TextView)findViewById(R.id.textView);
        TextView tv_by=(TextView)findViewById(R.id.textView3);
        TextView tv_desc=(TextView)findViewById(R.id.textView4);
        tv_status=(TextView)findViewById(R.id.textView5);

        Toast.makeText(mActivity, "title set", Toast.LENGTH_SHORT).show();
        tv_title.setText(intent.getStringExtra("title"));
        tv_by.setText(intent.getStringExtra("user"));
        tv_desc.setText(intent.getStringExtra("description"));
        tv_status.setText("Status: "+intent.getStringExtra("status"));

    }

    public void openMap(View view){
        Intent openMapIntent=new Intent(mActivity,MapsActivity.class);
        openMapIntent.putExtra("latitude",intent.getStringExtra("latitude"));
        openMapIntent.putExtra("longitude",intent.getStringExtra("longitude"));
        openMapIntent.putExtra("address",intent.getStringExtra("address"));
        openMapIntent.putExtra("title",intent.getStringExtra("title"));
        startActivity(openMapIntent);
    }


    public void updateStatus(View view){

        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if ((editText.getText().toString()!="")&&(editText.getText()!=null))
                        pDialog = new ProgressDialog(mActivity);

                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        params=new HashMap<String, String>();
                        params.put("id",intent.getStringExtra("id"));
                        params.put("status",editText.getText().toString());

                        StringRequest strRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.services_url)+"updatestatus",
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        tv_status.setText("Status: " + editText.getText().toString());
                                        hidePDialog();

                                        params.clear();
                                        Toast.makeText(ReportDetails.this, "Successfully updated", Toast.LENGTH_SHORT).show();

                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        params.clear();
                                        hidePDialog();
                                        Toast.makeText(mActivity, "Unable to update. Please try again.", Toast.LENGTH_SHORT).show();
//                        Log.e("res1: ", String.valueOf(error));
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



                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();









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
