package net.chrysaetos.myreports;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class allReportsViewAdapter extends RecyclerView.Adapter<allReportsViewAdapter.MyHolder> {

    private ArrayList<showReports.reports> report;
    private Context c;
    public allReportsViewAdapter(Context c, ArrayList<showReports.reports> reports) {
        this.report = reports;
        this.c = c;
    }


    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        showReports.reports s = report.get(position);
        holder.getDateValue().setText(s.getDate());
        holder.getDocName().setText("Doctor Name: "+s.getDoc());
        holder.getReportview().setText(s.getReports());
        holder.setKey(s.getKey());
        holder.setStrDocName(s.getDoc());
        holder.setStrDateValue(s.getDate());
        holder.setStrIllnessValue(s.getIllness());
    }

    @Override
    public allReportsViewAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.adapter_report_view,parent,false);
        return new allReportsViewAdapter.MyHolder(v);
    }


    @Override
    public int getItemCount() {
        return report.size();
    }

    /*
    VIEW HOLDER CLASS
     */
    class MyHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {

        private TextView docName;
        private TextView dateValue;
        private TextView reportview;
        private String key;
        private String strDocName;
        private String strDateValue;
        private String strIllnessValue;
        private CardView reportsCardview;

        public String getStrIllnessValue() {
            return strIllnessValue;
        }

        public void setStrIllnessValue(String strIllnessValue) {
            this.strIllnessValue = strIllnessValue;
        }




        public String getStrDocName() {
            return strDocName;
        }

        public void setStrDocName(String strDocName) {
            this.strDocName = strDocName;
        }

        public String getStrDateValue() {
            return strDateValue;
        }

        public void setStrDateValue(String strDateValue) {
            this.strDateValue = strDateValue;
        }



        public MyHolder(final View itemView) {
            super(itemView);
            this.docName = (TextView) itemView.findViewById(R.id.tvDocName);
            this.dateValue = (TextView) itemView.findViewById(R.id.tvDateValue);
            this.reportview = (TextView) itemView.findViewById(R.id.tvReports);
            this.reportsCardview = (CardView) itemView.findViewById(R.id.profileCardView);
            reportsCardview.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    //Toast.makeText(view.getContext(), "You clicked " + pos + getKey(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(c, showAllReportImage.class);
                    intent.putExtra("reportKey",getKey());
                    intent.putExtra("DocName",getStrDocName());
                    intent.putExtra("DateValue",getStrDateValue());
                    intent.putExtra("Illness",getStrIllnessValue());
                    c.startActivity(intent);
                }
            });

//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    int pos = getAdapterPosition();
//                    //Toast.makeText(view.getContext(), "You clicked long " + pos + getKey(), Toast.LENGTH_SHORT).show();
//
//
//
//                    return true;
//                }
//            });
//
//            itemView.setOnCreateContextMenuListener(this);

        }



        public TextView getReportview() {
            return reportview;
        }

        public void setReportview(TextView reportview) {
            this.reportview = reportview;
        }

        public TextView getDocName() {
            return docName;
        }

        public void setDocName(TextView docName) {
            this.docName = docName;
        }

        public TextView getDateValue() {
            return dateValue;
        }

        public void setDateValue(TextView dateValue) {
            this.dateValue = dateValue;
        }


        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public void onCreateContextMenu(ContextMenu Menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            Menu.setHeaderTitle("Select an Option");
            Menu.add(this.getAdapterPosition(),121,0,"Delete");
            Menu.add(this.getAdapterPosition(),122,0,"Cancel");

        }


    }
    public void removeRecords(int position)
    {
        report.remove(position);

        notifyDataSetChanged();
    }
}
