package net.chrysaetos.myreports;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class dashboardFragment extends Fragment {

    public FirebaseDatabase database;
    public DatabaseReference ref;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "kamal";
    private List lUsers;
    private List lUsersKey;
    private dataStore _dataStore;
    private String[] listUsersName;
    private String[] listUsersKey;

    public dashboardFragment() {
        // Required empty public constructor

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference(firebaseAuth.getUid());


        _dataStore = dataStore.getInstance();


        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                _dataStore.getlUsers().clear();
                _dataStore.getlUsersKey().clear();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "DCkey : " + noteDataSnapshot.getKey().toString());
                    Log.d(TAG, "DCData : " + noteDataSnapshot.getValue());
                    addProfile.users user = noteDataSnapshot.getValue(addProfile.users.class);
                    _dataStore.addTolUsersKey(noteDataSnapshot.getKey().toString());
                    _dataStore.addTolUsers(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataStore x = dataStore.getInstance();

        //Log.d(TAG, "singleTonData : " + x.s);
        Button pbAddRepots = (Button) view.findViewById(R.id.pbAddRepots);
        pbAddRepots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserlist();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Select Member");
                mBuilder.setSingleChoiceItems(listUsersName, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                       // Toast.makeText(getActivity(),listUsersKey[i]+" selected",Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();


                        Intent intent = new Intent(getActivity(), add_report.class);
                        intent.putExtra("UserKey",listUsersKey[i]);
                        startActivity(intent);
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }

        });

        Button pbShowReports = (Button) view.findViewById(R.id.pbShowReports);
        pbShowReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserlist();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Select Member");
                mBuilder.setSingleChoiceItems(listUsersName, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Toast.makeText(getActivity(),listUsersKey[i]+" selected",Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();


                        Intent intent = new Intent(getActivity(), showReports.class);
                        intent.putExtra("UserKey",listUsersKey[i]);
                        intent.putExtra("UserName",listUsersName[i]);
                        startActivity(intent);
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }


        });

        Button pbdeleteProfile = (Button) view.findViewById(R.id.pbEditProfile);
        pbdeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), profileDelete.class);
                        startActivity(intent);
                    }
                });


        Button pbAddprofile = (Button) view.findViewById(R.id.pbAddMember);
        pbAddprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), addUser.class);
                startActivity(intent);
            }
        });


    }

    public  void getUserlist() {
        listUsersName = new String[_dataStore.getlUsers().size()];
        listUsersKey = new String[_dataStore.getlUsersKey().size()];
        for (int i = 0; i < _dataStore.getlUsers().size(); i++) {
            listUsersName[i] = ((addProfile.users) _dataStore.getlUsers().get(i)).getName().toString();
            listUsersKey[i] = _dataStore.getlUsersKey().get(i).toString();
        }

    }

}
