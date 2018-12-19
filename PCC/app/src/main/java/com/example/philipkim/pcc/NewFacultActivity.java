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

public class NewFacultActivity extends AppCompatActivity {
    String URL_ADDFACULTY = "http://10.0.2.2:8080/pccphp/faculty_register.php";
    Button btn_addFaculty, btn_facultyLogout;
    private EditText et_facultyName, et_facultyTitle, et_facultyEmail;
    private MaskEditText et_facultyPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_facult);
        et_facultyName = findViewById(R.id.facultyEditText);
        et_facultyTitle = findViewById(R.id.facultyTitleEditText);
        et_facultyEmail = findViewById(R.id.facultyEmailEditText);
        et_facultyPhone = findViewById(R.id.facultyPhoneEditText);
        btn_addFaculty = findViewById(R.id.facultyAddBtn);
        btn_facultyLogout = findViewById(R.id.facultyLogoutBtn);

        btn_addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFacultyInformationToDatabase();
            }
        });

        btn_facultyLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewFacultActivity.this, AdminActivity.class));
            }
        });
    }

    // Add to faculty information database
    private void addFacultyInformationToDatabase()
    {
        String _facultyName = et_facultyName.getText().toString();
        String _facultyTitle = et_facultyTitle.getText().toString();
        String _facultyEmail = et_facultyEmail.getText().toString();
        String _facultyPhone = et_facultyPhone.getText().toString();
        if(!_facultyName.isEmpty() || !_facultyTitle.isEmpty() || !_facultyEmail.isEmpty() || !_facultyPhone.isEmpty())
        {
            if(_facultyPhone.length() != 15)
            {
                et_facultyPhone.setError("Please enter 10 digits");
            }
            else
            {
                RegisterFaculty();
            }
        }
        else
        {
            et_facultyName.setError("Please enter faculty name.");
            et_facultyTitle.setError("Please enter faculty title.");
            et_facultyPhone.setError("Please enter faculty phone.");
            et_facultyEmail.setError("Please enter faculty email.");
        }
    }

    // Set faculty information to database
    private void RegisterFaculty()
    {
        final String strFacultyName = et_facultyName.getText().toString();
        final String strFacultyTitle = et_facultyTitle.getText().toString();
        final String strFacultyEmail = et_facultyEmail.getText().toString();
        final String strFacultyPhone = et_facultyPhone.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDFACULTY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1"))
                            {
                                Toast.makeText(NewFacultActivity.this,et_facultyName.getText().toString() + " has been added successfully.", Toast.LENGTH_SHORT).show();
                                et_facultyName.setText(null);
                                et_facultyTitle.setText(null);
                                et_facultyPhone.setText(null);
                                et_facultyEmail.setText(null);
                            }
                            else
                            {
                                Toast.makeText(NewFacultActivity.this,"REGISTRATION ERROR!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewFacultActivity.this,"ERROR OCCURRED!!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewFacultActivity.this,"REGISTRATION ERROR OCCURRED!!!!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name", strFacultyName);
                params.put("title", strFacultyTitle);
                params.put("phone", strFacultyPhone);
                params.put("email", strFacultyEmail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}