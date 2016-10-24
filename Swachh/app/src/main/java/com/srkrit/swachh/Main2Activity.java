package com.srkrit.swachh;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    Activity mActivity;
    RequestQueue mRequestQueue;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Swachh App");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        mActivity=this;


        Transition exitTrans = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            exitTrans = new Explode();
            getWindow().setExitTransition(exitTrans);

            Transition reenterTrans = new Explode();
            getWindow().setReenterTransition(reenterTrans);
        }

        session=SessionManager.getInstance(mActivity);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main2);
        TextView headerName=(TextView)headerView.findViewById(R.id.nav_name);
        TextView headerUsername=(TextView)headerView.findViewById(R.id.nav_username);

        if ((session.get("username")!="")&&((session.get("username")!=null))){
            headerName.setText("Name: "+session.get("name"));
            headerUsername.setText("UserName: "+session.get("username"));
        }

        navigationView.setNavigationItemSelectedListener(this);




        mRequestQueue = Volley.newRequestQueue(this);


        if ((session.get("username")=="")||(session.get("username")==null)){
            openSettings();
        }





    }




    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }



    public void openChildMod(View view){



        if (!((session.get("id")=="")||(session.get("id")==null))){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(new Intent(this,ReportIssue.class),options.toBundle());
            }
            else{
                startActivity(new Intent(this,ReportIssue.class));
            }
        }
        else{
            Toast.makeText(Main2Activity.this, "Please register.", Toast.LENGTH_SHORT).show();
        }


    }

    public void openGuardianMod(View view){

        if (!((session.get("id")=="")||(session.get("id")==null))) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(new Intent(this, MyReports.class), options.toBundle());
            } else {
                startActivity(new Intent(this, MyReports.class));
            }
        }
        else{
            Toast.makeText(Main2Activity.this, "Please register.", Toast.LENGTH_SHORT).show();
        }

    }


    public void openChildMod(){

        if (!((session.get("id")=="")||(session.get("id")==null))) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(new Intent(this, ReportIssue.class), options.toBundle());
            } else {
                startActivity(new Intent(this, ReportIssue.class));
            }
        }
        else{
            Toast.makeText(Main2Activity.this, "Please register.", Toast.LENGTH_SHORT).show();
        }

    }

    public void openGuardianMod(){

        if (!((session.get("id")=="")||(session.get("id")==null))) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(new Intent(this, MyReports.class), options.toBundle());
            } else {
                startActivity(new Intent(this, MyReports.class));
            }
        }
        else{
            Toast.makeText(Main2Activity.this, "Please register.", Toast.LENGTH_SHORT).show();
        }

    }

    public void openMyIssues(){

        if (!((session.get("id")=="")||(session.get("id")==null))) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(new Intent(this, MyIssues.class), options.toBundle());
            } else {
                startActivity(new Intent(this, MyIssues.class));
            }
        }
        else{
            Toast.makeText(Main2Activity.this, "Please register.", Toast.LENGTH_SHORT).show();
        }

    }

    public void openMyIssues(View view){

        if (!((session.get("id")=="")||(session.get("id")==null))) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(new Intent(this, MyIssues.class), options.toBundle());
            } else {
                startActivity(new Intent(this, MyIssues.class));
            }
        }
        else{
            Toast.makeText(Main2Activity.this, "Please register.", Toast.LENGTH_SHORT).show();
        }

    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }




    public void openSettings() {
//		Toast.makeText(MainActivity.this, "open settings", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alert=new AlertDialog.Builder(mActivity);


        LinearLayout formLayout=new LinearLayout(mActivity);


        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        formLayout.setPadding(25,5,25,5);

        formLayout.setOrientation(LinearLayout.VERTICAL);
        formLayout.setLayoutParams(LLParams);

        final EditText yourName=new EditText(mActivity);
        yourName.setHint("Unique Username");
        yourName.setSingleLine();
        formLayout.addView(yourName);

        final EditText yourNo=new EditText(mActivity);
        yourNo.setHint("Name");
        yourNo.setSingleLine();
        formLayout.addView(yourNo);

        final EditText guardianNo=new EditText(mActivity);
        guardianNo.setHint("Description");
        guardianNo.setInputType(InputType.TYPE_CLASS_TEXT);
        formLayout.addView(guardianNo);

        alert.setView(formLayout);

        alert.setTitle("Register")
                .setMessage("Please fill all details")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if ((yourName.getText().toString()!="")&&(yourNo.getText().toString()!="")&&(guardianNo.getText().toString()!=""))
                        {

//							Toast.makeText(MainActivity.this, "save", Toast.LENGTH_SHORT).show();

                            Map<String,String> params=new HashMap<String, String>();
                            params.put("username",yourName.getText().toString());
                            params.put("description",guardianNo.getText().toString());
                            params.put("name",yourNo.getText().toString());



                            saveToServer(params);

                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//						Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();

                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.register_icon))
                .show();
    }





    public void openSettings(View view) {

        AlertDialog.Builder alert=new AlertDialog.Builder(mActivity);


        LinearLayout formLayout=new LinearLayout(mActivity);


        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        formLayout.setPadding(25,5,25,5);

        formLayout.setOrientation(LinearLayout.VERTICAL);
        formLayout.setLayoutParams(LLParams);

        final EditText yourName=new EditText(mActivity);
        yourName.setHint("Unique Username");
        yourName.setSingleLine();
        formLayout.addView(yourName);

        final EditText yourNo=new EditText(mActivity);
        yourNo.setHint("Name");
        yourNo.setSingleLine();
        formLayout.addView(yourNo);

        final EditText guardianNo=new EditText(mActivity);
        guardianNo.setHint("Description");
        guardianNo.setInputType(InputType.TYPE_CLASS_TEXT);
        formLayout.addView(guardianNo);

        alert.setView(formLayout);

        alert.setTitle("Register")
                .setMessage("Please fill all details")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if ((yourName.getText().toString()!="")&&(yourNo.getText().toString()!="")&&(guardianNo.getText().toString()!=""))
                        {

//							Toast.makeText(MainActivity.this, "save", Toast.LENGTH_SHORT).show();

                            Map<String,String> params=new HashMap<String, String>();
                            params.put("username",yourName.getText().toString());
                            params.put("description",guardianNo.getText().toString());
                            params.put("name",yourNo.getText().toString());
                            saveToServer(params);

                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//						Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();

                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.register_icon))
                .show();
    }

    private void saveToServer(final Map<String, String> params) {

        Log.e("params", String.valueOf(params));


        String url =getResources().getString(R.string.services_url)+"register";



        if (isInternetAvailable()){


            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

							Log.e("response ", response);

                            Toast.makeText(Main2Activity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();


                            try {
                                JSONObject responseObj=new JSONObject(response);
                                session.set("id", responseObj.getString("id"));
                                session.set("username", params.get("username"));
                                session.set("name", params.get("name"));
                                session.set("description", params.get("description"));




//								Log.e("userno",session.get("userno"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            params.clear();

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            params.clear();
                            Toast.makeText(Main2Activity.this, "Unable to save. Please try again.", Toast.LENGTH_SHORT).show();
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

            mRequestQueue.add(strRequest);









        }
        else{

            new AlertDialog.Builder(mActivity)
                    .setTitle("Internet Needed")
                    .setMessage("Please enable internet")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                        }
                    })
//					.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							// do nothing
//						}
//					})
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();


        }

    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_child) {
            openChildMod();
        } else if (id == R.id.nav_guardian) {
            openGuardianMod();
        } else if (id == R.id.nav_open_map) {
            openMyIssues();
        } else if (id == R.id.nav_register) {
            openSettings();
        } else if (id == R.id.nav_about_us) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(new Intent(this,Aboutus.class),options.toBundle());
            }
            else{
                startActivity(new Intent(this,Aboutus.class));
            }

        }
        else if (id == R.id.nav_help) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(new Intent(this,Authorities.class),options.toBundle());
            }
            else{
                startActivity(new Intent(this,Authorities.class));
            }

        }
        else if (id == R.id.nav_share) {

            Intent i=new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT,"Install Swachh App");
            i.putExtra(Intent.EXTRA_TEXT, "Download Swachh App: https://play.google.com/store/apps/details?id=com.srkrit.swachh");
            startActivity(Intent.createChooser(i,"Share via"));


        } else if (id == R.id.nav_exit) {
            AlertDialog.Builder alert=new AlertDialog.Builder(mActivity);
            alert.setTitle("Exit")
                    .setMessage("Are you aure you want to exit?")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(getResources().getDrawable(R.drawable.exit_icon))
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
