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

public class FacultyListAdapter extends ArrayAdapter<FacultyInformation> {
    private int resource;
    private int lastPosition = -1;
    private Context context;

    // Constructs a ViewHolder for the facult listView
    static class ViewHolder {
        TextView _name;
        TextView _title;
        TextView _phone;
        TextView _email;
    }

    // Constructs a FacultyListAdapter for the facult listView
    public FacultyListAdapter(Context context, int resource, ArrayList<FacultyInformation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    // Get faculty information and display on ListView
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String title = getItem(position).getTitle();
        String phone = getItem(position).getPhone();
        String email = getItem(position).getEmail();
        FacultyInformation faculty = new FacultyInformation(name, title, phone, email);
        View result;
        ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(this.resource, parent, false);
            holder = new ViewHolder();
            holder._name = convertView.findViewById(R.id.deanNameTextView);
            holder._title = convertView.findViewById(R.id.dateTextView);
            holder._phone = convertView.findViewById(R.id.startTimeTextView);
            holder._email = convertView.findViewById(R.id.endTimeTextView);
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
        holder._name.setText(faculty.getName());
        holder._title.setText(faculty.getTitle());
        holder._phone.setText(faculty.getPhone());
        holder._email.setText(faculty.getEmail());
        return convertView;
    }
}