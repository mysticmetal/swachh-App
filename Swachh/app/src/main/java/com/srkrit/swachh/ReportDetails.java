package com.srkrit.swachh;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.srkrit.swachh.app.AppController;

public class ReportDetails extends AppCompatActivity {

    Activity mActivity;
    Intent intent;

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
        Toast.makeText(mActivity, "intent:"+intent.getStringExtra("address"), Toast.LENGTH_SHORT).show();

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        Picasso.with(this)
                .load(intent.getStringExtra("image"))
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(imageView);

        TextView tv_title=(TextView)findViewById(R.id.textView);
        TextView tv_by=(TextView)findViewById(R.id.textView3);
        TextView tv_desc=(TextView)findViewById(R.id.textView4);
        TextView tv_status=(TextView)findViewById(R.id.textView5);

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

        Toast.makeText(ReportDetails.this, "id:"+intent.getStringExtra("id"), Toast.LENGTH_SHORT).show();

    }
}
