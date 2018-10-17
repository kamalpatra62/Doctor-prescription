package net.chrysaetos.myreports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class pdataListAdapter extends ArrayAdapter<pdata> {
    private  Context mcontext;
    int mresource;

    public pdataListAdapter(Context context, int resource, ArrayList<pdata> obc) {
        super(context, resource, obc);

        mcontext = context;
        mresource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).getPersonalInfo();
        String value =  getItem(position).getPersonalValue();

        pdata profileData = new pdata(name,value);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mresource,parent,false);

        TextView tvInfo = convertView.findViewById(R.id.textinfo);

        TextView tvValue = convertView.findViewById(R.id.textvalue);

        tvInfo.setText(name);
        tvValue.setText(value);

        return convertView;

    }
}
