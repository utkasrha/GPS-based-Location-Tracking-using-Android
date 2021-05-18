package com.college.employeetrackingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.college.util.AppController;
import com.college.util.Keys;
import com.college.util.SharedPreference;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskCompleteActivity extends AppCompatActivity {

    ImageView imageView;
    Button button_upload;
    File imageFile=null;
    ImageView imageView_task;
    private String selectedImagePath="",filename="",imageFilePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_complete);
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.NAMESPACE = "com.college.employeetrackingapplication";
        SharedPreference.initialize(getApplicationContext());
        AppController.initialize(getApplicationContext());
        String e_id=SharedPreference.get("e_id");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        String t_id=i.getStringExtra("t_id");
        button_upload=findViewById(R.id.btn_upload);
        imageView_task=findViewById(R.id.img_task);
        imageView_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_permissions()){
                    openCameraIntent();
                }
            }
        });
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.i("nik",selectedImagePath);
                    String image= UUID.randomUUID().toString();
                    UploadNotificationConfig config = new UploadNotificationConfig();
                    config.getCompleted().autoClear = true;
                    config.setTitleForAllStatuses("photo");
                    config.setIconForAllStatuses(R.mipmap.ic_launcher);
                    new MultipartUploadRequest(getApplicationContext(),image, Keys.URL.UPLOAD_TASK)
                            .addFileToUpload(selectedImagePath,"ta_photo")
                            .addParameter("t_id",t_id)
                            .addParameter("e_id",e_id)
                            .setMaxRetries(5)
                            .setNotificationConfig(config)
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(Context context, UploadInfo uploadInfo) {
                                    Log.i("nik","on progress upload");
                                }

                                @Override
                                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                                    Log.i("nik","on error upload");
                                }

                                @Override
                                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                                    Log.i("nik","on completed upload");
                                    Log.i("nik",serverResponse.getBodyAsString());
                                    try {
                                        JSONObject json=new JSONObject(serverResponse.getBodyAsString());
                                        if (json.getString("success").equals("1")){
                                            Toast.makeText(TaskCompleteActivity.this, "Task Completed Successfully", Toast.LENGTH_SHORT).show();
                                            imageView_task.setImageResource(0);
                                            imageView_task.setImageResource(R.drawable.ic_photo);

                                        }else {
                                            Toast.makeText(TaskCompleteActivity.this, "Unsuccessfully uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(Context context, UploadInfo uploadInfo) {
                                    Log.i("nik", "onCancelled");
                                }
                            })
                            .startUpload();


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, 2);
            }
        } else {

            return true;
        }

        return false;
    }
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(this.getPackageManager()) != null) {
            //Create a file to store the image
            // File photoFile = null;
            //try {
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + ".png";

            File filesDir = getApplicationContext().getFilesDir();
            imageFile = new File(filesDir, imageFileName);
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//
//            }
            if (imageFile != null) {
                SharedPreference.save("imagefilepath",imageFile.getAbsolutePath());
                Uri photoURI = FileProvider.getUriForFile(this.getApplicationContext(), this.getPackageName(),imageFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 2);
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==2) {
            if (grantResults.length > 0) {
                openCameraIntent();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                filename="";
                imageFilePath=SharedPreference.get("imagefilepath");
                Uri selectedImage = Uri.parse(imageFilePath);
                imageView_task.setImageURI(selectedImage);
                selectedImagePath = selectedImage.getPath();
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}