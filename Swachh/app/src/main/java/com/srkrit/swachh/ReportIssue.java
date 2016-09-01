package com.srkrit.swachh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class ReportIssue extends AppCompatActivity {
    private static final int REQUEST_CAMERA =1 ;
    private static final int SELECT_FILE =2 ;
    Session session;
    Activity mActivity;
    RequestQueue mRequestQueue;
    int imageSelected=0;

    EditText et_username,et_description,et_title;
    ImageView upload_camera,upload_preview;
    Button submit_report_btn;

    String username,user,title,description,loc_address;
    double latitude,longitude;
    String userChoosenTask;
    private String KEY_IMAGE = "image";
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);


        mActivity=this;

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


        mRequestQueue = Volley.newRequestQueue(this);
        session=SessionManager.getInstance(mActivity);

        if ((session.get("username")=="")||(session.get("username")==null)){
            onBackPressed();
        }


        submit_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username=et_username.getText().toString();
                title=et_title.getText().toString();
                description=et_description.getText().toString();

                user=session.get("username");

                Toast.makeText(ReportIssue.this, username+" "+title+" "+description+" "+user, Toast.LENGTH_SHORT).show();

                if((username!="")&&(imageSelected==1)&&(title!="")&&(user!="")&&(description!="")){
                    Toast.makeText(ReportIssue.this, "OK", Toast.LENGTH_SHORT).show();

                    uploadImage();
                }

            }
        });






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
            Toast.makeText(ReportIssue.this, "empty gal data", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReportIssue.this, "unable to show thumbnail", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mActivity, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
//                        Toast.makeText(mActivity, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                params.put("issueaddress", "addr");
                params.put("latitude", "lat");
                params.put("longitude", "long");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
