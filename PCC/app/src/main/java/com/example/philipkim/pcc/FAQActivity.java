package com.example.philipkim.pcc;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;

public class FAQActivity extends AppCompatActivity {
    ListView listView;
    ListViewAdapter adapter;
    ArrayList<Model> arrayList = new ArrayList<>();
    String[] questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Frequently Asked Questions");
        questions = new String[]{"What is a unit?",
                "How many units do I have to take each semester?",
                "What is the maximum units I can take each semester?",
                "Do I have to declare a major?",
                "How do I declare a major?",
                "What does “Permission” or “Restricted” mean on the online schedule of classes?",
                "Who is staff in the schedule of classes?",
                "How do I view my Comprehensive Student Educational Plan (CSEP) in LancerPoint?"};
        listView = findViewById(R.id.listView);
        for(int i = 0; i < questions.length; i++)
        {
            Model model = new Model(questions[i]);
            arrayList.add(model);
        }
        adapter = new ListViewAdapter(this, arrayList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.faq_menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s))
                {
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else
                {
                    adapter.filter(s);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}