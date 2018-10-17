package net.chrysaetos.myreports;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.angrybyte.numberpicker.view.ActualNumberPicker;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class editProfileActivity extends AppCompatActivity {

    private  static int PICK_IMAGE = 62;
    private static final int PERMISSION_REQUEST_CODE = 250;
    Uri profileImagePath;
    private String userKey,pblooddGroup;
    private addProfile.users userObj;
    private static final String TAG = "kamal";
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storRef;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private EditText editTextName,editTextPhoneNo,editTextEmail;
    private RadioButton radioButtonMale;
    private DatePicker simpleDatePicker;
    private ImageView imageView;
    private Button pbPosA,pbNegA,pbPosB,pbNegB,pbPosO,pbNegO,pbPosAB,pbNegAB;
    private ActualNumberPicker mPicker;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);

        Bundle bundle = getIntent().getExtras();
        userKey = bundle.getString("UserKey");

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNo = findViewById(R.id.editTextContact);
        editTextEmail = findViewById(R.id.editTextEmail);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        simpleDatePicker = findViewById(R.id.datePicker2);
        mPicker = (ActualNumberPicker) findViewById(R.id.actual_picker);

        pbPosA = findViewById(R.id.pbPosA);
        pbNegA = findViewById(R.id.pbNegA);
        pbPosB = findViewById(R.id.pbPosB);
        pbNegB = findViewById(R.id.pbNegB);
        pbPosO = findViewById(R.id.pbPosO);
        pbNegO = findViewById(R.id.pbNegO);
        pbPosAB = findViewById(R.id.pbPosAB);
        pbNegAB = findViewById(R.id.pbNegAB);


        imageView = findViewById(R.id.imageButton);

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(firebaseAuth.getUid());
        database = FirebaseDatabase.getInstance();
        ref = database.getReference(firebaseAuth.getUid());
        ref.child("Users").child(userKey)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userObj = dataSnapshot.getValue(addProfile.users.class);
                Picasso.get().load(userObj.getImageGo()).transform(new RoundedCornersTransform()).into(imageView);
                editTextName.setText(userObj.getName().toString());
                editTextPhoneNo.setText(userObj.getPhnumber().toString());
                editTextEmail.setText(userObj.getEmail().toString());
                if(userObj.getGender().toString().equalsIgnoreCase("Male"))
                    radioButtonMale.setChecked(true);
                else
                    radioButtonMale.setChecked(false);
                String date = userObj.getDob();
                if(date.length() > 0) {
                    List<String> dateList = Arrays.asList(date.split("-"));
                    simpleDatePicker.updateDate(Integer.valueOf(dateList.get(0)), Integer.valueOf(dateList.get(1)), Integer.valueOf(dateList.get(2)));
                }

                String bloodGr = userObj.getBooldGroup().toString();
                pblooddGroup = bloodGr;
                if(bloodGr.equalsIgnoreCase("A+"))
                {
                    pbPosA.setBackgroundColor(Color.parseColor("#0b1e87"));
                    pbPosA.setTextColor(Color.WHITE);

                }
                else if(bloodGr.equalsIgnoreCase("A-"))
                {
                    pbNegA.setBackgroundColor(Color.parseColor("#0b1e87"));
                    pbNegA.setTextColor(Color.WHITE);
                }
                else if(bloodGr.equalsIgnoreCase("B+"))
                {
                    pbPosB.setBackgroundColor(Color.parseColor("#0b1e87"));
                    pbPosB.setTextColor(Color.WHITE);
                }
                else if(bloodGr.equalsIgnoreCase("B-"))
                {
                    pbNegB.setBackgroundColor(Color.parseColor("#0b1e87"));
                    pbNegB.setTextColor(Color.WHITE);
                }
                else if(bloodGr.equalsIgnoreCase("O+"))
                {
                    pbPosO.setBackgroundColor(Color.parseColor("#0b1e87"));
                    pbPosO.setTextColor(Color.WHITE);
                }
                else if(bloodGr.equalsIgnoreCase("O-"))
                {
                    pbNegO.setBackgroundColor(Color.parseColor("#0b1e87"));
                    pbNegO.setTextColor(Color.WHITE);
                }
                else if(bloodGr.equalsIgnoreCase("AB+"))
                {
                    pbPosAB.setBackgroundColor(Color.parseColor("#0b1e87"));
                    pbPosAB.setTextColor(Color.WHITE);
                }
                else if(bloodGr.equalsIgnoreCase("AB-"))
                {
                    pbNegAB.setBackgroundColor(Color.parseColor("#0b1e87"));
                    pbNegAB.setTextColor(Color.WHITE);
                }

                mPicker.setValue(Integer.valueOf(userObj.getWeight()));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void posA(View v)
    {

        pbPosA.setBackgroundColor(Color.parseColor("#0b1e87"));
        pbPosB.setBackgroundResource(android.R.drawable.btn_default);
        pbPosO.setBackgroundResource(android.R.drawable.btn_default);
        pbPosAB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegA.setBackgroundResource(android.R.drawable.btn_default);
        pbNegB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegO.setBackgroundResource(android.R.drawable.btn_default);
        pbNegAB.setBackgroundResource(android.R.drawable.btn_default);

        pbPosA.setTextColor(Color.WHITE);
        pbPosB.setTextColor(Color.BLACK);
        pbPosO.setTextColor(Color.BLACK);
        pbPosAB.setTextColor(Color.BLACK);
        pbNegA.setTextColor(Color.BLACK);
        pbNegB.setTextColor(Color.BLACK);
        pbNegO.setTextColor(Color.BLACK);
        pbNegAB.setTextColor(Color.BLACK);

        pblooddGroup = "A+";
    }
    void negA(View v)
    {
        pbPosA.setBackgroundResource(android.R.drawable.btn_default);
        pbPosB.setBackgroundResource(android.R.drawable.btn_default);
        pbPosO.setBackgroundResource(android.R.drawable.btn_default);
        pbPosAB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegA.setBackgroundColor(Color.parseColor("#0b1e87"));
        pbNegB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegO.setBackgroundResource(android.R.drawable.btn_default);
        pbNegAB.setBackgroundResource(android.R.drawable.btn_default);

        pbPosA.setTextColor(Color.BLACK);
        pbPosB.setTextColor(Color.BLACK);
        pbPosO.setTextColor(Color.BLACK);
        pbPosAB.setTextColor(Color.BLACK);
        pbNegA.setTextColor(Color.WHITE);
        pbNegB.setTextColor(Color.BLACK);
        pbNegO.setTextColor(Color.BLACK);
        pbNegAB.setTextColor(Color.BLACK);
        pblooddGroup = "A-";
    }
    void posB(View v)
    {
        pbPosA.setBackgroundResource(android.R.drawable.btn_default);
        pbPosB.setBackgroundColor(Color.parseColor("#0b1e87"));
        pbPosO.setBackgroundResource(android.R.drawable.btn_default);
        pbPosAB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegA.setBackgroundResource(android.R.drawable.btn_default);
        pbNegB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegO.setBackgroundResource(android.R.drawable.btn_default);
        pbNegAB.setBackgroundResource(android.R.drawable.btn_default);

        pbPosA.setTextColor(Color.BLACK);
        pbPosB.setTextColor(Color.WHITE);
        pbPosO.setTextColor(Color.BLACK);
        pbPosAB.setTextColor(Color.BLACK);
        pbNegA.setTextColor(Color.BLACK);
        pbNegB.setTextColor(Color.BLACK);
        pbNegO.setTextColor(Color.BLACK);
        pbNegAB.setTextColor(Color.BLACK);
        pblooddGroup = "B+";

    }
    void negB(View v)
    {

        pbPosA.setBackgroundResource(android.R.drawable.btn_default);
        pbPosB.setBackgroundResource(android.R.drawable.btn_default);
        pbPosO.setBackgroundResource(android.R.drawable.btn_default);
        pbPosAB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegA.setBackgroundResource(android.R.drawable.btn_default);
        pbNegB.setBackgroundColor(Color.parseColor("#0b1e87"));
        pbNegO.setBackgroundResource(android.R.drawable.btn_default);
        pbNegAB.setBackgroundResource(android.R.drawable.btn_default);

        pbPosA.setTextColor(Color.BLACK);
        pbPosB.setTextColor(Color.BLACK);
        pbPosO.setTextColor(Color.BLACK);
        pbPosAB.setTextColor(Color.BLACK);
        pbNegA.setTextColor(Color.BLACK);
        pbNegB.setTextColor(Color.WHITE);
        pbNegO.setTextColor(Color.BLACK);
        pbNegAB.setTextColor(Color.BLACK);
        pblooddGroup = "B-";
    }
    void posO(View v)
    {

        pbPosA.setBackgroundResource(android.R.drawable.btn_default);
        pbPosB.setBackgroundResource(android.R.drawable.btn_default);
        pbPosO.setBackgroundColor(Color.parseColor("#0b1e87"));
        pbPosAB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegA.setBackgroundResource(android.R.drawable.btn_default);
        pbNegB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegO.setBackgroundResource(android.R.drawable.btn_default);
        pbNegAB.setBackgroundResource(android.R.drawable.btn_default);

        pbPosA.setTextColor(Color.BLACK);
        pbPosB.setTextColor(Color.BLACK);
        pbPosO.setTextColor(Color.WHITE);
        pbPosAB.setTextColor(Color.BLACK);
        pbNegA.setTextColor(Color.BLACK);
        pbNegB.setTextColor(Color.BLACK);
        pbNegO.setTextColor(Color.BLACK);
        pbNegAB.setTextColor(Color.BLACK);
        pblooddGroup = "O+";
    }
    void negO(View v)
    {
        pbPosA.setBackgroundResource(android.R.drawable.btn_default);
        pbPosB.setBackgroundResource(android.R.drawable.btn_default);
        pbPosO.setBackgroundResource(android.R.drawable.btn_default);
        pbPosAB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegA.setBackgroundResource(android.R.drawable.btn_default);
        pbNegB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegO.setBackgroundColor(Color.parseColor("#0b1e87"));
        pbNegAB.setBackgroundResource(android.R.drawable.btn_default);

        pbPosA.setTextColor(Color.BLACK);
        pbPosB.setTextColor(Color.BLACK);
        pbPosO.setTextColor(Color.BLACK);
        pbPosAB.setTextColor(Color.BLACK);
        pbNegA.setTextColor(Color.BLACK);
        pbNegB.setTextColor(Color.BLACK);
        pbNegO.setTextColor(Color.WHITE);
        pbNegAB.setTextColor(Color.BLACK);

        pblooddGroup = "O-";

    }
    void posAB(View v)
    {

        pbPosA.setBackgroundResource(android.R.drawable.btn_default);
        pbPosB.setBackgroundResource(android.R.drawable.btn_default);
        pbPosO.setBackgroundResource(android.R.drawable.btn_default);
        pbPosAB.setBackgroundColor(Color.parseColor("#0b1e87"));
        pbNegA.setBackgroundResource(android.R.drawable.btn_default);
        pbNegB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegO.setBackgroundResource(android.R.drawable.btn_default);
        pbNegAB.setBackgroundResource(android.R.drawable.btn_default);

        pbPosA.setTextColor(Color.BLACK);
        pbPosB.setTextColor(Color.BLACK);
        pbPosO.setTextColor(Color.BLACK);
        pbPosAB.setTextColor(Color.WHITE);
        pbNegA.setTextColor(Color.BLACK);
        pbNegB.setTextColor(Color.BLACK);
        pbNegO.setTextColor(Color.BLACK);
        pbNegAB.setTextColor(Color.BLACK);
        pblooddGroup = "AB+";
    }
    void negAB(View v)
    {

        pbPosA.setBackgroundResource(android.R.drawable.btn_default);
        pbPosB.setBackgroundResource(android.R.drawable.btn_default);
        pbPosO.setBackgroundResource(android.R.drawable.btn_default);
        pbPosAB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegA.setBackgroundResource(android.R.drawable.btn_default);
        pbNegB.setBackgroundResource(android.R.drawable.btn_default);
        pbNegO.setBackgroundResource(android.R.drawable.btn_default);
        pbNegAB.setBackgroundColor(Color.parseColor("#0b1e87"));

        pbPosA.setTextColor(Color.BLACK);
        pbPosB.setTextColor(Color.BLACK);
        pbPosO.setTextColor(Color.BLACK);
        pbPosAB.setTextColor(Color.BLACK);
        pbNegA.setTextColor(Color.BLACK);
        pbNegB.setTextColor(Color.BLACK);
        pbNegO.setTextColor(Color.BLACK);
        pbNegAB.setTextColor(Color.WHITE);

        pblooddGroup = "AB-";
    }

    public void takeProfilePic(View v) {

        view = v;
        requestPermission();
    }
    public  void pickImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted ) {
                        pickImage();
                    }
                    else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA,READ_EXTERNAL_STORAGE},
//                                                            PERMISSION_REQUEST_CODE);
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
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
        new AlertDialog.Builder(editProfileActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            Uri uri = data.getData();
            profileImagePath = uri;
            Picasso.get().load(profileImagePath).transform(new RoundedCornersTransform()).fit().centerCrop().into(imageView);
        }
    }

    public void addUserDatabase(View v) {

        final String name = ((EditText)findViewById(R.id.editTextName)).getText().toString();
        final String phone = ((EditText)findViewById(R.id.editTextContact)).getText().toString();
        final String email = ((EditText)findViewById(R.id.editTextEmail)).getText().toString();
        final String genderM;
        Boolean gender = ((RadioButton) findViewById(R.id.radioButtonMale)).isChecked();

        if(gender)
            genderM = "Male";
        else
            genderM = "Female";


        DatePicker dobPicker = (DatePicker)findViewById(R.id.datePicker2);
        Integer dobYear = dobPicker.getYear();
        Integer dobMonth = dobPicker.getMonth();
        Integer dobDate = dobPicker.getDayOfMonth();
        StringBuilder sb= new StringBuilder();
        sb.append(dobYear.toString()).append("-").append(dobMonth.toString()).append("-").append(dobDate.toString());
        final String dobStr=sb.toString();

        mPicker = (ActualNumberPicker) findViewById(R.id.actual_picker);
        final int weight = mPicker.getValue();

        boolean checkEmail,checkPhno;
        checkEmail = false;
        checkPhno = false;
        if(email!=null)
        {
            if(email.length()>0 ) {
                Pattern p;
                Matcher m;
                String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                p = Pattern.compile(EMAIL_STRING);
                m = p.matcher(email);
                checkEmail = m.matches();
                if (!checkEmail) {
                    Toast.makeText(getApplicationContext(), "Not Valid Email", Toast.LENGTH_LONG).show();
                }
            }
        }
        if(phone!=null)
        {
            if(phone.length() >0 ) {

                if (!Pattern.matches("[a-zA-Z]+", phone)) {
                    if (phone.length() < 6 || phone.length() > 13) {
                        checkPhno = false;
                        Toast.makeText(getApplicationContext(), "Not Valid Phone Number", Toast.LENGTH_LONG).show();
                    } else {
                        checkPhno = true;
                    }
                } else {
                    checkPhno = false;
                }

            }
        }
        if(name!=null && phone!=null &&  email!=null && checkEmail && checkPhno ) {
            if(name.length()>0 && phone.length()>0 && email.length()>0) {

                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Uploading Data");
                dialog.show();

                String imageuri;
                final String newKey;

                storRef = storageReference.child("Users").child(userKey).child("profileImage");

                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
//
                UploadTask uploadTask = storRef.putBytes(data);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100* taskSnapshot.getBytesTransferred()) /taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded..."+(int)progress+"%");

                    }
                });

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storRef.getDownloadUrl();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {


                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Successfully uploaded", Toast.LENGTH_SHORT).show();
                            if (downloadUri != null) {

                                String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                                userObj = new addProfile.users(name, phone, email, genderM, dobStr, pblooddGroup, String.valueOf(weight),photoStringLink);
                                ref.child("Users").child(userKey).setValue(userObj);

//                                Intent i = new Intent(editProfileActivity.this, dashboardFragment.class);
//                                startActivity(i);
                                    finish();
                            }

                        } else {
                            // Handle failures
                            // ...
                        }
                    }

//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_LONG).show();
//
//
//                    Log.d(TAG, "afterStoreImgaeUri :" + taskSnapshot.getStorage().getDownloadUrl().toString());
//                    //pweight = taskSnapshot.getDownloadUrl().toString();
//                    objuser = new users(pname, pcontact, pemailid, pgender, pdob, pbloodGrop, pweight,taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
//                    //objuser = new users(pname, pcontact, pemailid, pgender, pdob, pbloodGrop, pweight);
//                    ref.child("Users").child("Admin").setValue(objuser);
//
//                    finish();
//
//                }
                });
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please enter your details",Toast.LENGTH_LONG).show();
        }

    }
}
