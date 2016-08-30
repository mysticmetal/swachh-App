package com.srkrit.swachh;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ReportIssue extends AppCompatActivity {
    Session session;
    Activity mActivity;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Report an Issue");
        setSupportActionBar(toolbar);

        mActivity=this;

        Transition exitTrans = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            exitTrans = new Explode();
            getWindow().setExitTransition(exitTrans);

            Transition reenterTrans = new Explode();
            getWindow().setReenterTransition(reenterTrans);
        }

        mRequestQueue = Volley.newRequestQueue(this);

        session=SessionManager.getInstance(mActivity);
        if ((session.get("id")=="")||(session.get("id")==null)){
            startActivity(new Intent(mActivity,Main2Activity.class));
            finish();
        }


    }
}
