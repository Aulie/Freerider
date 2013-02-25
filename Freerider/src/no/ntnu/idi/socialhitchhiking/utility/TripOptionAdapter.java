package no.ntnu.idi.socialhitchhiking.utility;

import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.journey.TripOption;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TripOptionAdapter extends ArrayAdapter<TripOption>{

    Context context; 
    int layoutResourceId;    
    TripOption data[] = null;
    
    public TripOptionAdapter(Context context, int layoutResourceId, TripOption[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TripOptionHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new TripOptionHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (TripOptionHolder)row.getTag();
        }
        
        TripOption option = data[position];
        holder.txtTitle.setText(option.title);
        holder.imgIcon.setImageResource(option.icon);
        
        return row;
    }
    
    static class TripOptionHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
