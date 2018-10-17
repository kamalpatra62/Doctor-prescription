package net.chrysaetos.myreports;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class reportViewAdapter extends RecyclerView.Adapter<reportViewAdapter.MyHolderCamera> {

    private ArrayList<reportImage> reportImages;
    private Context c;

    public reportViewAdapter(Context c,ArrayList<reportImage> images) {
        this.reportImages = images;
        this.c = c;
    }

    /*
    INITIALIZE VIEWHOLDER
     */

    @Override
    public void onBindViewHolder(MyHolderCamera holder, int position) {
        reportImage s= reportImages.get(position);

        Glide.with(c).load(s.getImageReportUri()).into(holder.img);
        //holder.img.setImageBitmap(s.getBitmap());
        //Picasso.get().load(s.getBitmap()).fit().centerCrop().into(holder.img);
//        Picasso.get().load(new File(s.getBitmap())).fit().centerCrop().into(holder.img);
//        .load(new File(c.getPathPhoto())).into(holder.photo);
        //holder.tvt.setText(s.getText());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // display a toast with person name on item click
//                Toast.makeText(context, personNames.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    @Override
    public MyHolderCamera onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.camera_preview_layout,parent,false);
        return new MyHolderCamera(v);
    }

    /*
    BIND
     */


    /*
    TOTAL SPACECRAFTS NUM
     */
    @Override
    public int getItemCount() {
        return reportImages.size();
    }

    /*
    VIEW HOLDER CLASS
     */
    class MyHolderCamera extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView tvt;

        public MyHolderCamera(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.reportPic);
            //tvt = (TextView) itemView.findViewById(R.id.textvt);
        }
    }
}