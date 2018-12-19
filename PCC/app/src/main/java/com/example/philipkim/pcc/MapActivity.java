package com.example.philipkim.pcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapActivity extends AppCompatActivity {
    static String studentName, studentPhone, studentEmail;
    Button btn_mapLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btn_mapLogout = findViewById(R.id.mapLogoutBtb);

        Intent studentInfo = getIntent();
        studentName = studentInfo.getStringExtra("studentName");
        studentPhone = studentInfo.getStringExtra("studentPhone");
        studentEmail = studentInfo.getStringExtra("studentEmail");


        btn_mapLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, QuestionsActivity.class);
                intent.putExtra("studentName", studentName);
                intent.putExtra("studentPhone", studentPhone);
                intent.putExtra("studentEmail", studentEmail);
                startActivity(intent);
            }
        });
    }
}
