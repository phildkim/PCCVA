package com.example.philipkim.pcc;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionsActivity extends AppCompatActivity {
    TextView tv_qna;
    Button btn_qnaLogout, btn_facultyInfo, btn_faq, btn_question, btn_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Intent studentInfo = getIntent();
        final String studentInfoName = studentInfo.getStringExtra("studentName");
        final String studentInfoPhone = studentInfo.getStringExtra("studentPhone");
        final String studentInfoEmail = studentInfo.getStringExtra("studentEmail");
        tv_qna = findViewById(R.id.qnaTextView);
        btn_qnaLogout = findViewById(R.id.button7);
        btn_facultyInfo = findViewById(R.id.button6);
        btn_faq = findViewById(R.id.faqBtn);
        btn_question = findViewById(R.id.askQuestionBtn);
        btn_map = findViewById(R.id.mapBtn);
        tv_qna.setText(String.format("Hello, %s what would you like to do?", studentInfoName));

        // click map button
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, MapActivity.class);
                intent.putExtra("studentName", studentInfoName);
                intent.putExtra("studentPhone", studentInfoPhone);
                intent.putExtra("studentEmail", studentInfoEmail);
                startActivity(intent);
            }
        });

        // click faq button
        btn_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, FAQActivity.class);
                intent.putExtra("studentName", studentInfoName);
                intent.putExtra("studentPhone", studentInfoPhone);
                intent.putExtra("studentEmail", studentInfoEmail);
                startActivity(intent);
            }
        });

        // click on ask question
        btn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askQuestion(studentInfoEmail, studentInfoPhone);
            }
        });

        // click on faculty info
        btn_facultyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, FacultyActivity.class);
                intent.putExtra("studentName", studentInfoName);
                intent.putExtra("studentPhone", studentInfoPhone);
                intent.putExtra("studentEmail", studentInfoEmail);
                startActivity(intent);
            }
        });

        // click on logout
        btn_qnaLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, StudentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void askQuestion(final String email, final String phone)
    {
        final EditText editText = new EditText(QuestionsActivity.this);
        editText.setHint("ENTER QUESTION");
        final AlertDialog dialog = new AlertDialog.Builder(QuestionsActivity.this)
                .setTitle("Other questions:")
                .setIcon(R.drawable.faq)
                .setMessage("Type your question")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final AlertDialog.Builder statusBuilder = new AlertDialog.Builder(QuestionsActivity.this);
                        statusBuilder.setTitle("Reply to email or text");
                        statusBuilder.setIcon(R.drawable.status);
                        statusBuilder.setMessage("Your question: " + editText.getText().toString() + "\n\nEmail: " + email + "\nPhone: " + phone);
                        statusBuilder.setPositiveButton("email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(QuestionsActivity.this, "We will reply to " + email + " soon as possible.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                        statusBuilder.setNegativeButton("phone-sms", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(QuestionsActivity.this, "We will reply to " + phone + " soon as possible.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = statusBuilder.create();
                        alertDialog.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        dialog.show();
    }
}