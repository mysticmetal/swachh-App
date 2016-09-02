package com.srkrit.swachh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportIssue extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private static final int REQUEST_CAMERA =1 ;
    private static final int SELECT_FILE =2 ;
    Session session;
    Activity mActivity;
    RequestQueue mRequestQueue;
    int imageSelected=0;
    Location mLastLocation;
    private LocationManager locationManager;
    private String provider;

    EditText et_username,et_description,et_title;
    ImageView upload_camera,upload_preview;
    Button submit_report_btn;

    String username,user,title,description,loc_address;
    double latitude,longitude;
    String userChoosenTask;
    private String KEY_IMAGE = "image";
    private Bitmap bitmap;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);


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




        et_username=(EditText)findViewById(R.id.et_username);
        et_description=(EditText)findViewById(R.id.et_description);
        et_title=(EditText)findViewById(R.id.et_title);
        upload_camera=(ImageView)findViewById(R.id.upload_camera);
        upload_preview=(ImageView)findViewById(R.id.upload_preview);
        submit_report_btn=(Button)findViewById(R.id.submit_issue_btn);


        if ((mActivity.getIntent().getStringExtra("username")!="")&&(mActivity.getIntent().getStringExtra("username")!=null)){
            et_username.setText(mActivity.getIntent().getStringExtra("username"));
        }


        mRequestQueue = Volley.newRequestQueue(this);

        if ((session.get("username")=="")||(session.get("username")==null)){
            onBackPressed();
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }





        submit_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username=et_username.getText().toString();
                title=et_title.getText().toString();
                description=et_description.getText().toString();

                user=session.get("username");


                if((username!="")&&(imageSelected==1)&&(title!="")&&(user!="")&&(description!="")){
                    uploadImage();
                }

            }
        });






    }



        protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }




    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude=mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();

            GetCurrentAddress currentadd=new GetCurrentAddress();
            currentadd.execute();

//            Toast.makeText(mActivity, String.valueOf(mLastLocation.getLatitude())+"---"+String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
        }
        else{
            latitude=0;
            longitude=0;
            Toast.makeText(mActivity, "Location unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void selectImage(View view) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Gallery";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            imageSelected=1;
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }



    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                Toast.makeText(mActivity, data.toString(), Toast.LENGTH_SHORT).show();

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                bitmap=bm;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(ReportIssue.this, "Image not found. Try again.", Toast.LENGTH_SHORT).show();
        }

        upload_preview.setImageBitmap(bm);
        upload_preview.setVisibility(View.VISIBLE);
        upload_camera.setVisibility(View.GONE);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail=null;
        try {
            thumbnail = (Bitmap) data.getExtras().get("data");
            bitmap=thumbnail;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            if (thumbnail != null) {
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            }
            else{
                Toast.makeText(ReportIssue.this, "Thumbnail not supported", Toast.LENGTH_SHORT).show();
            }

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        upload_preview.setImageBitmap(thumbnail);
        upload_preview.setVisibility(View.VISIBLE);
        upload_camera.setVisibility(View.GONE);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+getResources().getString(R.string.upload_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();


                        //Showing toast message of the response
                        upload_camera.setVisibility(View.VISIBLE);
                        upload_preview.setVisibility(View.GONE);
                        et_title.setText("");
                        et_username.setText("");
                        et_description.setText("");
                        Toast.makeText(mActivity, "Successfully reported." , Toast.LENGTH_LONG).show();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Toast.makeText(mActivity, "Network error. Try again.", Toast.LENGTH_LONG).show();


                        //Showing toast
//                        Log.e("volley error",volleyError.getMessage().toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);


                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put("title", title);
                params.put("username", username);
                params.put("user", user);
                params.put("description", description);
                params.put("issueaddress", loc_address);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }





    public  String getAddress(Context ctx, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);


                String locality=address.getLocality();
                String country=address.getCountryName();
                String region_code=address.getCountryCode();
                String zipcode=address.getPostalCode();
                double lat =address.getLatitude();
                double lon= address.getLongitude();

                result.append(address.getAddressLine(0)+", "+locality+", ");
                result.append(country+" "+ region_code+" ");
                result.append(zipcode);

                loc_address= String.valueOf(result);

//                Log.e("addr", String.valueOf(result));

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class GetCurrentAddress extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... urls) {
            // this lat and log we can get from current location but here we given hard coded

            return getAddress(mActivity, latitude, longitude);
        }

        @Override
        protected void onPostExecute(String resultString) {

//            Toast.makeText(mActivity, "addr:"+resultString, Toast.LENGTH_SHORT).show();
        }
    }

}
