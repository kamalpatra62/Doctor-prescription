package net.chrysaetos.myreports;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class accountFragment extends Fragment {

    private RecyclerView recyclerView;
    private profilePIcAdapter profileAdapter;
    public FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public DatabaseReference ref;
    private FirebaseAuth firebaseAuth;

    private static final String TAG = "kamal";

    public accountFragment() {


        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ref = database.getReference(firebaseAuth.getUid()).child("Users");
        //ref = database.getReference("Users");

        ref.keepSynced(true);
        //.d(TAG, "mainactivity : "+ firebaseAuth.getCurrentUser());




    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.addusermenu,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_accont :
//                Intent i = new Intent(getActivity(), addProfile.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.putExtra("PhoneNo","");
//                i.putExtra("Email","");
//                i.putExtra("FirstTime","No");
//                startActivity(i);
                Intent i = new Intent(getActivity(), addUser.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar =  view.findViewById(R.id.toolbarAccount);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerPicView1);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(profileAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<addProfile.users> options =
                new FirebaseRecyclerOptions.Builder<addProfile.users>()
                        .setQuery(ref.orderByChild("Users"),addProfile.users.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecylerAdapter = new FirebaseRecyclerAdapter<addProfile.users, UserViewHolder>(options) {
            View mmview;
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                mmview = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_profile_view, parent, false);

                return new UserViewHolder(mmview);
            }
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull addProfile.users model) {
                holder.setName(model.getName());
                holder.setPhoneNo(model.getPhnumber());
                holder.setProfilePic(getActivity(),model.getImageGo());
            }


        };

        recyclerView.setAdapter(firebaseRecylerAdapter);
        firebaseRecylerAdapter.startListening();


    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
//        private CardView profileCardview;
//        private RelativeLayout profileLayout;
        public UserViewHolder(View itemView)
        {
            super(itemView);
            mView=itemView;
//            this.profileCardview = (CardView) mView.findViewById(R.id.profileCardView);
//            this.profileLayout = (RelativeLayout)mView.findViewById(R.id.profileRelative);



        }
        public void setName(String name)
        {
            TextView textNameView = (TextView)mView.findViewById(R.id.tvName);
            textNameView.setText(name);
        }
        public  void setPhoneNo(String ph)
        {
            TextView phnoTv = (TextView)mView.findViewById(R.id.tvPhoneNo);
            phnoTv.setText(ph);
        }

        public void setProfilePic(Context ctx, String image)
        {
            ImageView imgV = (ImageView)mView.findViewById(R.id.profillePic);
            Picasso.get().load(image).transform(new CircleTransform()).fit().centerCrop().into(imgV);
        }




    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return true;
    }
}
