package net.chrysaetos.myreports;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.View;
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

public class profileDelete extends AppCompatActivity {

    profilePIcAdapter profiledeleteAdapter;
    private ArrayList<addProfile.users> _arrayUsers;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storRef;
    private FirebaseAuth firebaseAuth;
    private  int deleteUsers;
    private String tempUserkey;
    private ArrayList<String > _userReportsKey;
    private int countUserRecorts,countFile;
    private ArrayList<String> arryDeleteImagePath;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_delete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerDeleteView);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        _arrayUsers = new ArrayList<addProfile.users>();
        _userReportsKey = new ArrayList<String>();
        countUserRecorts = 0;
        countFile = 0;
        arryDeleteImagePath = new ArrayList<String>();
        dialog = new ProgressDialog(this);

        profiledeleteAdapter = new profilePIcAdapter(getApplicationContext(),_arrayUsers);
        recyclerView.setAdapter(profiledeleteAdapter);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference(firebaseAuth.getUid());
        readUser();

    }
    public  void readUser()
    {
        ref.child("Users")
//        ref.orderByChild("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Log.d(TAG, "SingleValue : ");
                        _arrayUsers.clear();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            //Log.d(TAG, "SvchildKey : " + noteDataSnapshot.getKey().toString());

//                    Log.d("deleteProfile", "SvChilddata : " + noteDataSnapshot.getValue());
                            addProfile.users rep = noteDataSnapshot.getValue(addProfile.users.class);
                            rep.setUsrKey(noteDataSnapshot.getKey());
                            _arrayUsers.add(rep);
                            profiledeleteAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 121:
                tempUserkey = _arrayUsers.get(item.getGroupId()).getUsrKey().toString();
                deleteUsers = item.getGroupId();
                if (!tempUserkey.equalsIgnoreCase("Admin")) {

                    storRef = storage.getReferenceFromUrl(_arrayUsers.get(item.getGroupId()).getImageGo().toString());
                    storRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });
                    ref.child("Users").child(tempUserkey).child("Reports").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                String userReportkey = noteDataSnapshot.getKey().toString();
                                _userReportsKey.add(userReportkey);
                            }
                                deleteUsersRecords(countUserRecorts);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {

                    Toast.makeText(getApplicationContext(), "You can't delete Admin profile", Toast.LENGTH_SHORT).show();
                }
                return true;
            case 122:
                tempUserkey = _arrayUsers.get(item.getGroupId()).getUsrKey().toString();
                Intent i = new Intent(profileDelete.this, editProfileActivity.class);
                i.putExtra("UserKey",tempUserkey);
                startActivity(i);
                //Toast.makeText(getApplicationContext(), "You clicked cancel " , Toast.LENGTH_SHORT).show();

                return true;
            case 123:
                return true;
            default:
                return super.onContextItemSelected(item);


        }
    }

    private void deleteUsersRecords(int count)
    {
        if(count<_userReportsKey.size())
        {
            dialog.setTitle("Deleting Profile");
            dialog.show();
            dialog.setMessage("Deleting... ");
            ref.child("ReportImages").child(_userReportsKey.get(count)).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Log.d(TAG, "SingleValue : ");


                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        String imagePath = noteDataSnapshot.getValue().toString();
                        //Log.d("kamalDeleteUser",imagePath);
                        arryDeleteImagePath.add(imagePath);

                    }
                    countFile = 0;
//                    if(arryDeleteImagePath.size()>0)
                        deleteFiles(countFile);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
        else if(count == _userReportsKey.size())
        {
            ref.child("Users").child(tempUserkey).removeValue();
            profiledeleteAdapter.removeRecords(deleteUsers);
            dialog.dismiss();
        }
    }

    private  void deleteFiles(int count)
    {
        if(count < arryDeleteImagePath.size())
        {
            //Log.d("kamalDeleteUser","Delete Image Path"+arryDeleteImagePath.get(count).toString());

            storRef = storage.getReferenceFromUrl(arryDeleteImagePath.get(count).toString());
            storRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    //Toast.makeText(getApplicationContext(), "deleted Image file" , Toast.LENGTH_SHORT).show();
                    countFile = countFile + 1;

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
            ref.child("ReportImages").child(_userReportsKey.get(countUserRecorts).toString()).removeValue();

            countUserRecorts = countUserRecorts+1;
            deleteUsersRecords(countUserRecorts);

        }
    }

}
