package com.example.philipkim.pcc;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class AppointmentListAdapter extends ArrayAdapter<AppointmentInformation> {
    private Context context;
    private int resource;
    private int lastPosition = -1;

    // Constructs ViewHolder for the ViewList
    static class ViewHolder {
        TextView _deanName;
        TextView _appDate;
        TextView _startTime;
        TextView _endTime;
    }

    // Constructs ListAdapter for ViewList
    public AppointmentListAdapter(Context context, int resource, ArrayList<AppointmentInformation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    // Sets all the information on the ListView
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String strID = getItem(position).getDeanID();
        String strName = getItem(position).getDeanName();
        String strDate = getItem(position).getAppDate();
        String strStartTime = getItem(position).getStartTime();
        String strEndTime = getItem(position).getEndTime();
        String strAllTimes = getItem(position).getAllTimes();
        AppointmentInformation appointments = new AppointmentInformation(strID, strName, strDate, strStartTime, strEndTime, strAllTimes);
        View result;
        ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(this.resource, parent, false);
            holder = new ViewHolder();
            holder._deanName = convertView.findViewById(R.id.deanNameTextView);
            holder._appDate = convertView.findViewById(R.id.dateTextView);
            holder._startTime = convertView.findViewById(R.id.startTimeTextView);
            holder._endTime = convertView.findViewById(R.id.endTimeTextView);
            result = convertView;
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;
        holder._deanName.setText(appointments.getDeanName());
        holder._appDate.setText(appointments.getAppDate());
        holder._startTime.setText(appointments.getStartTime());
        holder._endTime.setText(appointments.getEndTime());
        return  convertView;
    }
}
