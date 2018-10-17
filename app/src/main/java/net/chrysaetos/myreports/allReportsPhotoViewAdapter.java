package net.chrysaetos.myreports;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class allReportsPhotoViewAdapter extends RecyclerView.Adapter<allReportsPhotoViewAdapter.MyHolder> {

    private ArrayList<showAllReportImage.reportPhotos> report;
    private Context c;
    public allReportsPhotoViewAdapter(Context c, ArrayList<showAllReportImage.reportPhotos> reports) {
        this.report = reports;
        this.c = c;
    }


    @Override
    public void onBindViewHolder(allReportsPhotoViewAdapter.MyHolder holder, int position) {
        showAllReportImage.reportPhotos s = report.get(position);

        //Glide.with(c).load(s.getReportImagePath()).into(holder.getImageView());
        Picasso.get().load(s.getReportImagePath()).fit().centerCrop().into(holder.getImageView());


    }

    @Override
    public allReportsPhotoViewAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.adapter_report_photo_view,parent,false);
        return new allReportsPhotoViewAdapter.MyHolder(v);
    }


    @Override
    public int getItemCount() {
        return report.size();
    }

    /*
    VIEW HOLDER CLASS
     */
    class MyHolder extends RecyclerView.ViewHolder
    {


        private ImageView imageView;
        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public MyHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.reportPhotoView);
        }

    }
}