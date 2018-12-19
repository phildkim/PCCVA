package com.example.philipkim.pcc;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;
    String[] questions, answers;


    //constructor
    public ListViewAdapter(Context context, List<Model> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder {
        TextView mTitleTv;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int postition, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row, null);
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.mTitleTv.setText(modellist.get(postition).getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions = new String[]{"What is a unit?",
                        "How many units do I have to take each semester?",
                        "What is the maximum units I can take each semester?",
                        "Do I have to declare a major?",
                        "How do I declare a major?",
                        "What does “Permission” or “Restricted” mean on the online schedule of classes?",
                        "Who is staff in the schedule of classes?",
                        "How do I view my Comprehensive Student Educational Plan (CSEP) in LancerPoint?"};
                answers = new String[]{"A unit/hour is the amount of college credit you will receive for a course based on the number of hours the course meets weekly.",
                        "You only need to have one unit for each Fall and Spring semester to be a student at PCC.",
                        "You can take a maximum of 19.3 units in the Fall and Spring semesters.",
                        "You don't need to declare a major during your first semester at PCC.",
                        "You can change your major at the Career Center (L Building).",
                        "\"Permission\" and \"Restricted\" classes are reserved for special programs.",
                        "This means that an instructor has not yet been designated to teach the class.",
                        "For more information on viewing go to \"How to View Your Comprehensive Student Educational Plan in LancerPoint.\""};

                int counter = postition + 1;
                if (modellist.get(postition).getTitle().equals(questions[postition])){
                    Intent intent = new Intent(mContext, AnswersActivity.class);
                    intent.putExtra("actionBarTitle", "#" + counter);
                    intent.putExtra("questionTv", questions[postition]);
                    intent.putExtra("contentTv", answers[postition]);
                    mContext.startActivity(intent);
                }
            }
        });
        return view;
    }

    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length()==0){
            modellist.addAll(arrayList);
        }
        else {
            for (Model model : arrayList){
                if (model.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
