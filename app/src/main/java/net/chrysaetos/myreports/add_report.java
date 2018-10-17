package net.chrysaetos.myreports;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class add_report extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 251;
    private  static int PICK_CAMERA = 65;
//    private ImageView imageView;
    private static final String TAG = "kamal";
    ArrayList<reportImage> images;
    reportViewAdapter rvAdapter;
    private String pictureFilePath;
    private String mCurrentPhotoPath;
    private String userKey;
    private FirebaseStorage storage;
    private StorageReference storRef,storageReference;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    reportImage obj;
    private EditText _etDocName,_etIllness;
    private String _tempDocName;
    Uri file;
    Uri photoUri;
    private boolean _tempFlag;
    private int pbClickCount;
    private int count;
    private String rImageKey;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report2);

        Bundle bundle = getIntent().getExtras();
        userKey = bundle.getString("UserKey");
        _tempFlag = true;

        dialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(firebaseAuth.getUid());

        database = FirebaseDatabase.getInstance();
        ref = database.getReference(firebaseAuth.getUid());


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//        imageView = (ImageView)findViewById(R.id.imageView2);
        _etDocName = findViewById(R.id.etDocName);
        _etIllness= findViewById(R.id.etIllness);

        pbClickCount = 0;
        count = 0;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewReport);
        // set a LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // set Horizontal Orientation
        recyclerView.setLayoutManager(linearLayoutManager);

        images = new ArrayList<reportImage>();
//        obj = new reportImage();
//        obj.setText("kamal");
//        images.add(obj);
        rvAdapter = new reportViewAdapter(getApplicationContext(), images);
        recyclerView.setAdapter(rvAdapter);
//        rvAdapter
//        obj = new reportImage();
//        obj.setText("kamal 2");
//        images.add(obj);
//        rvAdapter.notifyDataSetChanged();


    }

    public void takeCameraImage(View view)
    {
        requestPermission();
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && writeAccepted) {
                        pickCamera();
                    }
                    else {

//                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA,READ_EXTERNAL_STORAGE},
//                                                            PERMISSION_REQUEST_CODE);
                                                    requestPermissions(new String[]{CAMERA,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(add_report.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public  void pickCamera()
    {

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = Uri.fromFile(getOutputMediaFile());
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//        startActivityForResult(intent, PICK_CAMERA);

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile(); } catch (IOException ex) {
//                Log.e("DEBUG_TAG", "createFile", ex);
//            }
//            if (photoFile != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//            }
//        } else {
//            File file = null;
//            try {
//                file = new File(createImageFile().getPath());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//        }
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
//            startActivityForResult(intent, PICK_CAMERA);
//        }


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
                //Toast.makeText(getApplicationContext(),"uri"+ photoUri,Toast.LENGTH_LONG).show();
                //Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, PICK_CAMERA);
            }
        }




//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
//        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(cameraIntent, PICK_CAMERA);

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null; try {
//                photoFile = createImageFile(); } catch (IOException ex) {
//                Log.e("DEBUG_TAG", "createFile", ex);
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                startActivityForResult(takePictureIntent, PICK_CAMERA);
//            }


//            File pictureFile = null;
////            try {
////                //pictureFile = getPictureFile();
////            } catch (IOException ex) {
////                Toast.makeText(this,
////                        "Photo file can't be created, please try again",
////                        Toast.LENGTH_SHORT).show();
////                return;
////            }
//            if (pictureFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "net.chrysaetos.myreports",
//                        pictureFile);
//                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                //startActivityForResult(cameraIntent, PICK_CAMERA);
//            }
//
//            Log.d(TAG, "filename "+ pictureFilePath.toString());
//
//        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MyReport_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
//    private File createImageFile() throws IOException {
//// Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new
//                Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getAlbumDir();
//        File image = File.createTempFile(
//                imageFileName, /* prefix */ ".jpg", /* suffix */ storageDir /* directory */
//        );
//        // Save a file: path for use with ACTION_VIEW intents
//        pictureFilePath = image.getAbsolutePath();
//        return image; }

    private File getAlbumDir() { File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStorageDirectory() + "/dcim/"
                    + "MyRecipes");
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    Log.d("CameraSample", "failed to create directory");
                    return null; }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "myreports_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image;
        image = new File("kamal");
//        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
//        pictureFilePath = image.getAbsolutePath();
        return image;
    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        //Toast.makeText(getApplicationContext(),"insideImage"+ Activity.RESULT_OK,Toast.LENGTH_LONG).show();
        if(requestCode == PICK_CAMERA && resultCode == RESULT_OK)
        {

//            Uri uri = data.getExtras().get("data");

            File imgFile = new  File(mCurrentPhotoPath);
            if(imgFile.exists()){
                //Uri uri = data.getData();
//                Glide.with(this).load(photoUri).into(imageView);
//                imageView.setImageURI(Uri.fromFile(imgFile));
                //Glide.with(this).load(imgFile.getAbsolutePath()).into(imageView);
                obj = new reportImage();
//                obj.setText("kamal 2");
                obj.setImageReportUri(photoUri);
                //obj.setImageReportUri(imgFile.getAbsolutePath());
                images.add(obj);
                rvAdapter.notifyDataSetChanged();
            }
//            Toast.makeText(getApplicationContext(),"uri" + pictureFilePath,Toast.LENGTH_LONG).show();
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(photo);
//


        }
    }

    public void callme(){

         count = count + 1;
        Toast.makeText(getApplicationContext(), "Successfully uploaded.. "+ count , Toast.LENGTH_SHORT).show();
        if(count == images.size()) {
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            ref.child("Users").child(userKey).child("Reports").child(rImageKey).child("Doc").setValue(_etDocName.getText().toString());
            ref.child("Users").child(userKey).child("Reports").child(rImageKey).child("Illness").setValue(_etIllness.getText().toString());
            ref.child("Users").child(userKey).child("Reports").child(rImageKey).child("Date").setValue(date);

            dialog.dismiss();
            finish();
        }
    }

    public void updateReport(int i)
    {
        if(i<images.size()) {

            dialog.setTitle("Uploading Data");
            dialog.show();
            storRef = storageReference.child("Reports").child(userKey).child(images.get(i).getImageReportUri().getLastPathSegment());
//                ref.child("ReportImages").child(rImageKey).push();

            //Toast.makeText(getApplicationContext(), images.get(i).getImageReportUri(), Toast.LENGTH_LONG).show();
            UploadTask uploadTask = storRef.putFile(images.get(i).getImageReportUri());

            //arrayListUploadTask.add(uploadTask);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded ... " + (int) progress + "%");

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                    if (task.isSuccessful()) {


                        //Toast.makeText(getApplicationContext(), "Successfully uploaded.. "+ ii  , Toast.LENGTH_SHORT).show();
                        //dialog.show();
                        //dialog.setMessage("Uploaded " + ii+"/"+images.size());

                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downloadUrl = uri;
                                callme();
                                //  Log.d("kamalUrl",downloadUrl.toString());
                                String newKey = ref.child("ReportImages").child(rImageKey).push().getKey().toString();
                                ref.child("ReportImages").child(rImageKey).child(newKey).setValue(downloadUrl.toString());
                                updateReport(count);
                                //dialog.dismiss();
                                //finish();
                            }
                        });
                        //final Task<Uri> firebaseUri = task.getResult().getStorage().getDownloadUrl();
                        //firebaseUri.getResult().toString();
                        // Log.d("kamalUrl",storRef.getDownloadUrl().toString());
//                            String newKey = ref.child("ReportImages").child(rImageKey).push().getKey().toString();
//                            ref.child("ReportImages").child(rImageKey).child(newKey).setValue(storRef.getDownloadUrl().toString());
                        //Toast.makeText(getApplicationContext(), "Successfully uploaded.. "+ ii  , Toast.LENGTH_SHORT).show();


                        //task.getResult().getUploadSessionUri();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void uploadReport(View v) {

        if (_etDocName.getText().length() > 0 && _etDocName.getText() != null  && _etIllness.getText().length() > 0 && _etIllness.getText() != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading Data");
            dialog.show();

            rImageKey = ref.child("ReportImages").push().getKey().toString();

            if(images.size()>0)
                updateReport(0);

        }
        else
        {
            if(_etDocName.getText().length() <= 0 || _etDocName.getText() == null) {
                Toast.makeText(getApplicationContext(), "Please Enter Doctor Name", Toast.LENGTH_LONG).show();
            }else if (_etIllness.getText().length() <= 0 || _etIllness.getText() == null) {
                Toast.makeText(getApplicationContext(), "Please Enter Type of Illness", Toast.LENGTH_LONG).show();
            }

        }
    }

}
