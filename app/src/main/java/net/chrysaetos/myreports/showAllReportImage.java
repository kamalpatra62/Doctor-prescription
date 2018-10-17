package net.chrysaetos.myreports;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showAllReportImage extends AppCompatActivity {
    private  String key,docName,dateValue,illnessValue;
    private TextView tvDocName,tvDateValue,tvIllnessType;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private ArrayList<reportPhotos> arrayReportsPhoto;
    private allReportsPhotoViewAdapter allRpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_report_image);

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("reportKey");
        docName = bundle.getString("DocName");
        dateValue = bundle.getString("DateValue");
        illnessValue = bundle.getString("Illness");


        tvDocName = findViewById(R.id.tvDocNamePhoto);
        tvDocName.setText("Doctor Name: "+docName);
        tvDateValue = findViewById(R.id.tvDateValuePhoto);
        tvDateValue.setText(dateValue);
        tvIllnessType = findViewById(R.id.tvtypeIllnessPhoto);
        tvIllnessType.setText("Illness Type: "+illnessValue);

        findViewById(R.id.pbOkReportPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerReportPhoto);


        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayReportsPhoto = new ArrayList<reportPhotos>();
        allRpAdapter = new allReportsPhotoViewAdapter(getApplicationContext(), arrayReportsPhoto);
        recyclerView.setAdapter(allRpAdapter);


        //####################################################################################

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference( firebaseAuth.getUid());
//        ref.child("ReportImages").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                //Log.d(TAG, "SingleValue : ");
//
//                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                    //Log.d(TAG, "SvchildKey : " + noteDataSnapshot.getKey().toString());
//
//                    //Log.d("kamalTest", "SvChilddata : " + noteDataSnapshot.getValue());
//                    String imagePath = noteDataSnapshot.getValue().toString();
//                    //showAllReportImage.reportPhotos rep = noteDataSnapshot.getValue(showAllReportImage.reportPhotos.class);
//                    showAllReportImage.reportPhotos photo = new showAllReportImage.reportPhotos();
//                    photo.setReportImagePath(imagePath);
//
//                    arrayReportsPhoto.add(photo);
//                    allRpAdapter.notifyDataSetChanged();
//
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        ref.child("ReportImages").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.d(TAG, "SingleValue : ");

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    //Log.d(TAG, "SvchildKey : " + noteDataSnapshot.getKey().toString());

                    //Log.d("kamalTest", "SvChilddata : " + noteDataSnapshot.getValue());
                    String imagePath = noteDataSnapshot.getValue().toString();
                    //showAllReportImage.reportPhotos rep = noteDataSnapshot.getValue(showAllReportImage.reportPhotos.class);
                    showAllReportImage.reportPhotos photo = new showAllReportImage.reportPhotos();
                    photo.setReportImagePath(imagePath);

                    arrayReportsPhoto.add(photo);
                    allRpAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static class reportPhotos {

        private String reportImagePath;

        public String getReportImagePath() {
            return reportImagePath;
        }

        public void setReportImagePath(String reportImagePath) {
            this.reportImagePath = reportImagePath;
        }

        public reportPhotos(String reportImagePath) {
            this.reportImagePath = reportImagePath;
        }

        public reportPhotos() {
        }
    }


}
