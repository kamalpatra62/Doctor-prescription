package net.chrysaetos.myreports;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class showReports extends AppCompatActivity {
    private ArrayList<reports> arrayReports;
    private allReportsViewAdapter allRvAdapter;
    private String userKey,userName;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storRef;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> arryDeleteImagePath;
    private TextView reportView;
    private TextView mTextViewToolbarTitle;
    private int countFile,deleteRecordPos;
    private String  tempReportkey;
    private ProgressDialog dialog;

    ExpandableSearchAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> docList,illnessList,dateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My title");
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        userKey = bundle.getString("UserKey");
        userName = bundle.getString("UserName");

        toolbar.setTitle(userName + " Reports");

//        ActionBar ab = getActionBar();
//        ab.setTitle(userName + " Reports");

//        mTextViewToolbarTitle = getTextViewTitle(toolbar);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mTextViewTitle.setTransitionName(getString(R.string.your_transition_name));
//        }

        reportView = findViewById(R.id.reportsView);
        reportView.setVisibility(View.VISIBLE);


//###################################################################################################
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reportsRecyclerView);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayReports = new ArrayList<reports>();
        allRvAdapter = new allReportsViewAdapter(getApplicationContext(), arrayReports);
        recyclerView.setAdapter(allRvAdapter);

//####################################################################################################

        docList = new ArrayList<String>();
        illnessList = new ArrayList<String>();
        dateList = new ArrayList<String>();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

//        ######################################################################################
        countFile = 0;
        arryDeleteImagePath = new ArrayList<String>();
        dialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference( firebaseAuth.getUid());
        ref.child("Users").child(userKey).child("Reports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.d(TAG, "SingleValue : ");
                int count = 0;
                docList.add("All");
                illnessList.add("All");
                dateList.add("All");
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    //Log.d(TAG, "SvchildKey : " + noteDataSnapshot.getKey().toString());
                    count = count + 1;
                    //Log.d(TAG, "SvChilddata : " + noteDataSnapshot.getValue());
                    showReports.reports rep = noteDataSnapshot.getValue(showReports.reports.class);
                    rep.setReports("Report " + count);
                    rep.setKey(noteDataSnapshot.getKey());
                    arrayReports.add(rep);
                    allRvAdapter.notifyDataSetChanged();
                    reportView.setVisibility(View.GONE);

                   // docList.add(rep.getDoc());
                    //dateList.add(rep.getDate());

                    if(!docList.contains(rep.getDoc())){
                        docList.add(rep.getDoc());
                    }
                    if(!illnessList.contains(rep.getIllness())){
                        illnessList.add(rep.getIllness());
                    }
                    if(!dateList.contains(rep.getDate())){
                        dateList.add(rep.getDate());
                    }

                }

                listAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//###################################################################################################

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        listDataHeader.add("Doctor");
        listDataHeader.add("Illness");
        listDataHeader.add("Date");

        listDataChild.put(listDataHeader.get(0), docList); // Header, Child data
        listDataChild.put(listDataHeader.get(1), illnessList);
        listDataChild.put(listDataHeader.get(2), dateList);
        listAdapter = new ExpandableSearchAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                final String selected = (String) listAdapter.getChild(
                        groupPosition, childPosition);
                arrayReports.clear();
                if(groupPosition == 0)
                {
                    if(selected.equalsIgnoreCase("All")) {
                        showAllreports();
                    }
                    else {
                        ref.child("Users").child(userKey).child("Reports").orderByChild("Doc").equalTo(selected).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Log.d(TAG, "SingleValue : ");
                                int count = 0;
                                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                    //Log.d(TAG, "SvchildKey : " + noteDataSnapshot.getKey().toString());
                                    count = count + 1;
                                    //Log.d(TAG, "SvChilddata : " + noteDataSnapshot.getValue());
                                    showReports.reports rep = noteDataSnapshot.getValue(showReports.reports.class);
                                    rep.setReports("Report " + count);
                                    rep.setKey(noteDataSnapshot.getKey());
                                    arrayReports.add(rep);
                                    allRvAdapter.notifyDataSetChanged();
                                    reportView.setVisibility(View.GONE);

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else if(groupPosition == 1)
                {
                    if (selected.equalsIgnoreCase("All")) {
                        showAllreports();
                    } else {
                        ref.child("Users").child(userKey).child("Reports").orderByChild("Illness").equalTo(selected).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Log.d(TAG, "SingleValue : ");
                                int count = 0;
                                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                    //Log.d(TAG, "SvchildKey : " + noteDataSnapshot.getKey().toString());
                                    count = count + 1;
                                    //Log.d(TAG, "SvChilddata : " + noteDataSnapshot.getValue());
                                    showReports.reports rep = noteDataSnapshot.getValue(showReports.reports.class);
                                    rep.setReports("Report " + count);
                                    rep.setKey(noteDataSnapshot.getKey());
                                    arrayReports.add(rep);
                                    allRvAdapter.notifyDataSetChanged();
                                    reportView.setVisibility(View.GONE);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else if(groupPosition == 2) {
                    if (selected.equalsIgnoreCase("All")) {
                        showAllreports();
                    } else {
                        ref.child("Users").child(userKey).child("Reports").orderByChild("Date").equalTo(selected).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Log.d(TAG, "SingleValue : ");
                                int count = 0;
                                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                    //Log.d(TAG, "SvchildKey : " + noteDataSnapshot.getKey().toString());
                                    count = count + 1;
                                    //Log.d(TAG, "SvChilddata : " + noteDataSnapshot.getValue());
                                    showReports.reports rep = noteDataSnapshot.getValue(showReports.reports.class);
                                    rep.setReports("Report " + count);
                                    rep.setKey(noteDataSnapshot.getKey());
                                    arrayReports.add(rep);
                                    allRvAdapter.notifyDataSetChanged();
                                    reportView.setVisibility(View.GONE);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

               // Toast.makeText(showReports.this, "selected:" + selected + "groupPos" + groupPosition+"childPos"+childPosition, Toast.LENGTH_SHORT).show();


                return true;
            }
        });
//##################################################################################################
    }

    private void showAllreports()
    {
        ref.child("Users").child(userKey).child("Reports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.d(TAG, "SingleValue : ");
                int count = 0;
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    count = count + 1;
                    showReports.reports rep = noteDataSnapshot.getValue(showReports.reports.class);
                    rep.setReports("Report " + count);
                    rep.setKey(noteDataSnapshot.getKey());
                    arrayReports.add(rep);
                    allRvAdapter.notifyDataSetChanged();
                    reportView.setVisibility(View.GONE);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case 121:
                tempReportkey = arrayReports.get(item.getGroupId()).getKey().toString();
                deleteRecordPos = item.getGroupId();
                ref.child("ReportImages").child(tempReportkey).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Log.d(TAG, "SingleValue : ");


                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            String imagePath = noteDataSnapshot.getValue().toString();
                            arryDeleteImagePath.add(imagePath);

                        }

                        deleteFiles(countFile);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });



                //Toast.makeText(getApplicationContext(), "You clicked delete:key " + tempReportkey , Toast.LENGTH_SHORT).show();
                return true;
            case 122:
                //Toast.makeText(getApplicationContext(), "You clicked cancel " , Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);


        }


    }

    private  void deleteFiles(int count)
    {
        if(count < arryDeleteImagePath.size())
        {
            dialog.setTitle("Deleting Data");
            dialog.show();
            storRef = storage.getReferenceFromUrl(arryDeleteImagePath.get(count).toString());
            storRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    //Toast.makeText(getApplicationContext(), "deleted Image file" , Toast.LENGTH_SHORT).show();
                    countFile = countFile + 1;
                    dialog.setMessage("Deleting... ");
                    deleteFiles(countFile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    //deleteFiles(countFile+1);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "fail to deleted file" + exception, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(count == arryDeleteImagePath.size())
        {
            ref.child("ReportImages").child(tempReportkey).removeValue();
            ref.child("Users").child(userKey).child("Reports").child(tempReportkey).removeValue();
            allRvAdapter.removeRecords(deleteRecordPos);
            dialog.dismiss();
        }
    }

    public static class reports {

        private String report;
        private String key;
        private String Date;
        private String Doc;

        public String getIllness() {
            return Illness;
        }

        public void setIllness(String illness) {
            Illness = illness;
        }

        private String Illness;

        public reports(String date, String doc, String illness) {
            this.Date = date;
            this.Doc = doc;
            this.Illness = illness;
        }

        public reports() {

        }
        public String getReports() {
            return report;
        }

        public void setReports(String reports) {
            this.report = reports;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getDoc() {
            return Doc;
        }

        public void setDoc(String doc) {
            Doc = doc;
        }


        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }



}
