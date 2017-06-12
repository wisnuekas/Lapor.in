package com.example.wisnuekas.laporin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;


public class LaporActivity extends AppCompatActivity {

    private Button buttonChoose;
    private Button buttonSend;
    private ImageView imageUpload;
    private EditText annotationEditText;
    private EditText coordinateEditText;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    String nameImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.button_choose);
        buttonSend = (Button) findViewById(R.id.button_send);
        imageUpload = (ImageView) findViewById(R.id.image_upload);
        coordinateEditText = (EditText) findViewById(R.id.geo_tag);
        annotationEditText = (EditText) findViewById(R.id.annotation);

        //Setting clicklistener
        buttonChoose.setOnClickListener(new ImageListener());
        buttonSend.setOnClickListener(new SendListener());
        coordinateEditText.setOnClickListener(new CoordinateListener());
    }

    public void uploadMultipart() {

        annotationEditText = (EditText) findViewById(R.id.annotation);
        String annotation = annotationEditText.getText().toString();

        //getting the actual path of the image
        String path = getPath(filePath);

        //Date time
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime()).toString();

        //Coordinate
        coordinateEditText = (EditText) findViewById(R.id.geo_tag);
        String coordinate = coordinateEditText.getText().toString();

        //getting name for the image
        String nameImg = date+"_"+coordinate;


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name_img", nameImg)
                    .addParameter("annotation", annotation) //Adding text parameter to the request
                    .addParameter("coordinate", coordinate)
                    .addParameter("date", date)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageUpload.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    protected class ImageListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            showFileChooser();
        }
    }

    protected class SendListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            uploadMultipart();

            Toast.makeText(getApplicationContext(), "Kirim", Toast.LENGTH_SHORT).show();


        }
    }

    protected class CoordinateListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Bitmap bitmap = ((BitmapDrawable)imageUpload.getDrawable()).getBitmap();
            String imageEncode = encodeToBase64(bitmap);

            annotationEditText = (EditText) findViewById(R.id.annotation);
            String annotation = annotationEditText.getText().toString();

            Intent i = new Intent(LaporActivity.this, MapsActivity.class);
            i.putExtra("IMAGE_ENCODE", imageEncode);
            i.putExtra("ANNOTATION", annotation);

            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String imgEncode = getIntent().getStringExtra("IMG_ENCODE");
        String annotation = getIntent().getStringExtra("ANNOTATION");
        String koordinat = getIntent().getStringExtra("KOORDINAT");

        imageUpload = (ImageView) findViewById(R.id.image_upload);
        if (imgEncode != null){
            Bitmap bitmapsaved = decodeToBase64(imgEncode);
            imageUpload.setImageBitmap(bitmapsaved);
        }


        coordinateEditText = (EditText) findViewById(R.id.geo_tag);
        coordinateEditText.setText(koordinat);

        annotationEditText = (EditText) findViewById(R.id.annotation);
        annotationEditText.setText(annotation);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//
//        String koordinat = getIntent().getStringExtra("KOORDINAT");
//        coordinateEditText = (EditText) findViewById(R.id.geo_tag);
//        coordinateEditText.setText(koordinat);
//
//        SharedPreferences activityPreferences = getPreferences(MODE_PRIVATE);
//        String imageStr = activityPreferences.getString("IMAGE", "");
//        String annotation = activityPreferences.getString("ANNOTATION", "");
//        nameImg = activityPreferences.getString("NAME_IMG", "");
//        Bitmap bitmapsaved = decodeToBase64(imageStr);
//        imageUpload = (ImageView) findViewById(R.id.image_upload);
//        //imageUpload.setImageBitmap(bitmapsaved);
//
//        annotationEditText = (EditText) findViewById(R.id.annotation);
//        annotationEditText.setText(annotation);
//
//
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        SharedPreferences activityPreferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = activityPreferences.edit();
//
//        editor.clear();
//        editor.commit();
//
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        imageUpload = (ImageView) findViewById(R.id.image_upload);
//        Bitmap bitmap = ((BitmapDrawable)imageUpload.getDrawable()).getBitmap();
//
//        annotationEditText = (EditText) findViewById(R.id.annotation);
//        String annotation = annotationEditText.getText().toString();
//
//        coordinateEditText = (EditText) findViewById(R.id.geo_tag);
//        String coordinate = coordinateEditText.getText().toString();
//
//        //Date time
//        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
//        String date = df.format(Calendar.getInstance().getTime());
//
//        String nameImg = date+"_"+coordinate;
//
//        SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = activityPreferences.edit();
//
//        editor.putString("IMAGE", encodeToBase64(bitmap));
//        editor.putString("ANNOTATION",  annotation);
//        editor.putString("COORDINATE", coordinate);
//        editor.putString("NAME_IMG", nameImg);
//
//        editor.commit();
//
//    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
