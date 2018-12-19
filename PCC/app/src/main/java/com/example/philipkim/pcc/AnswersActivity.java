package com.example.philipkim.pcc;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnswersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        ActionBar actionBar = getSupportActionBar();
        TextView mDetailTv = findViewById(R.id.textView);
        TextView question = findViewById(R.id.questionText);

        Intent intent = getIntent();
        String mActionBarTitle = intent.getStringExtra("actionBarTitle");
        String questionContent = intent.getStringExtra("questionTv");
        String mContent = intent.getStringExtra("contentTv");

        //set actionbar title
        if (actionBar != null) {
            actionBar.setTitle("FAQ " + mActionBarTitle);
        }
        question.setText(String.format("Question: %s", questionContent));
        mDetailTv.setText(String.format("Answer: %s", mContent));
    }
}
