package net.chrysaetos.myreports;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class profilePIcAdapter extends RecyclerView.Adapter<profilePIcAdapter.MyViewHolder>{

    private List<addProfile.users> profileList;
    private Context c;
    public profilePIcAdapter(Context c,List<addProfile.users> profileList) {
        this.profileList = profileList;
        this.c = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.adapter_delete_profile, parent, false);

            return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        addProfile.users profile = profileList.get(position);
        holder.name.setText(profile.getName());
        Picasso.get().load(profile.getImageGo()).transform(new CircleTransform()).fit().centerCrop().into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView name;
        public ImageView profileImage;
        public CardView profileCardview;


        public MyViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.textNameDelete);
            this.profileImage = (ImageView) view.findViewById(R.id.imageProfilDelete);
            this.profileCardview = (CardView) view.findViewById(R.id.deleteProfileCardView);
            profileCardview.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu Menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Menu.setHeaderTitle("Select an Option");
            Menu.add(this.getAdapterPosition(),121,0,"Delete");
            Menu.add(this.getAdapterPosition(),122,0,"Edit");
            Menu.add(this.getAdapterPosition(),123,0,"Cancel");

        }
    }
    public void removeRecords(int position)
    {
        profileList.remove(position);

        notifyDataSetChanged();
    }
}
