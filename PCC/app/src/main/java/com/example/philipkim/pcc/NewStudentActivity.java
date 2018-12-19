package com.example.philipkim.pcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.santalu.maskedittext.MaskEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewStudentActivity extends AppCompatActivity {
    static String URL_STUDENTREGISTER = "http://10.0.2.2:8080/pccphp/student_register.php";
    private EditText et_studentName, et_studentUsername, et_studentEmail, et_studentPassword, et_studentMajor;
    private MaskEditText et_studentPhone;
    Button btn_studentRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);

        // Cast components
        et_studentName = findViewById(R.id.studentNameEditText);
        et_studentUsername = findViewById(R.id.studentUsernameEditText);
        et_studentEmail = findViewById(R.id.studentEmailEditText);
        et_studentPhone = findViewById(R.id.studentPhoneEditText);
        et_studentPassword = findViewById(R.id.studentPasswordEditText);
        et_studentMajor = findViewById(R.id.studentMajorEditText);
        btn_studentRegister = findViewById(R.id.studentRegisterButton);


        // Add new student
        btn_studentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewStudentToMySQL();
            }
        });
    }

    /*
     * A void method to add new student to MySQL
     * */
    private void addNewStudentToMySQL() {
        String studentName = et_studentName.getText().toString().trim();
        String studentUsername = et_studentUsername.getText().toString().trim();
        String studentEmail = et_studentEmail.getText().toString().trim();
        String studentPhone = et_studentPhone.getText().toString().trim();
        String studentPassword = et_studentPassword.getText().toString().trim();

        if(!studentName.isEmpty() && !studentUsername.isEmpty() && !studentEmail.isEmpty() && !studentPhone.isEmpty() && !studentPassword.isEmpty())
        {
            if(isPhoneNumberValid(et_studentPhone) && isPasswordrValid(et_studentPassword))
            {
                if(isMajorValid(et_studentMajor))
                {
                    RegisterStudent();
                }
            }
        }
        else
        {
            et_studentName.setError("Please enter your full name.");
            et_studentUsername.setError("Please enter a username.");
            et_studentEmail.setError("Please enter your email.");
            et_studentPhone.setError("Please enter your phone number.");
            et_studentPassword.setError("Please enter a password.");
        }
    }

    /*
     * A void method to register new student
     * */
    private void RegisterStudent()
    {
        final String id = idGenerator();
        final String name = et_studentName.getText().toString().trim();
        final String username = et_studentUsername.getText().toString().trim();
        final String email = et_studentEmail.getText().toString().trim();
        final String phone = et_studentPhone.getText().toString().trim();
        final String password = et_studentPassword.getText().toString().trim();
        final String major = et_studentMajor.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_STUDENTREGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1"))
                            {
                                // Go to StudentActivity after successful login.
                                Intent intent = new Intent(NewStudentActivity.this, StudentActivity.class);
                                intent.putExtra("studentID", id);
                                intent.putExtra("studentName", name);
                                startActivity(intent);
                                // Set EditText to null after successful login.
                                et_studentName.setText(null);
                                et_studentUsername.setText(null);
                                et_studentEmail.setText(null);
                                et_studentPhone.setText(null);
                                et_studentPassword.setText(null);
                                et_studentMajor.setText(null);
                            }
                            else
                            {
                                Toast.makeText(NewStudentActivity.this,"REGISTRATION ERROR!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewStudentActivity.this,"ERROR OCCURRED!!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewStudentActivity.this,"REGISTRATION ERROR OCCURRED!!!!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("username", username);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                params.put("major", major);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*
     * A boolean method to check student major
     * */
    private boolean isMajorValid(EditText major)
    {
        if(major.getText().toString().trim().isEmpty())
        {
            major.setText(getString(R.string.NewStudentUndeclared));
            return false;
        }
        return true;
    }

    /*
     * A boolean method to check phone length
     * */
    private boolean isPhoneNumberValid(EditText phone)
    {
        if(phone.getText().toString().trim().length() != 15)
        {
            Toast.makeText(NewStudentActivity.this,"ENTER 10 DIGIT PHONE NUMBER", Toast.LENGTH_SHORT).show();
            et_studentPhone.setError("Must be 10 digits");
            return false;
        }
        return true;
    }

    /*
     * A boolean method to check password length
     * */
    private boolean isPasswordrValid(EditText password)
    {
        if(password.getText().toString().trim().length() < 4)
        {
            Toast.makeText(NewStudentActivity.this,"ENTER AT LEAST 4 CHARACTERS", Toast.LENGTH_SHORT).show();
            et_studentPassword.setError("Must be at 4 characters.");
            return false;
        }
        return true;
    }

    /*
     * A String method to generate random student ID number.
     * */
    private String idGenerator()
    {
        String newId;
        int random = (int)(Math.random() * 99999999 + 1);
        newId = Integer.toString(random);
        return newId;
    }
}
