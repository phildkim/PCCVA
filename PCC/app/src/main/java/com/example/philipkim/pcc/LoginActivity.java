package com.example.philipkim.pcc;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    static String URL_STUDENTLOGIN = "http://10.0.2.2:8080/pccphp/student_login.php";
    static String URL_ADMINLOGIN = "http://10.0.2.2:8080/pccphp/admin_login.php";
    SessionAdminManager sessionAdminManager;
    SessionStudentManager sessionStudentManager;
    RadioButton rb_admin, rb_student;
    EditText et_username, et_password;
    TextView tv_register;
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create session objects for admin and student
        sessionAdminManager = new SessionAdminManager(this);
        sessionStudentManager = new SessionStudentManager(this);

        // Cast components
        rb_admin = findViewById(R.id.adminRadioButton);
        rb_student = findViewById(R.id.studentRadioButton);
        et_username = findViewById(R.id.usernameEditText);
        et_password = findViewById(R.id.passwordEditText);
        btn_login = findViewById(R.id.loginButton);
        tv_register = findViewById(R.id.registerTextView);
        tv_register.setTextColor(Color.parseColor("#4245f4"));

        // Login for admin or student.
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAdminStudent();
            }
        });


        // Register new admin or student.
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterAdminStudent();
            }
        });
    }

    // Void method to login admin or student
    private void LoginAdminStudent()
    {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if(!username.isEmpty() || !password.isEmpty())
        {
            if(rb_admin.isChecked())
            {
                UserLogin(URL_ADMINLOGIN);
            }
            else if(rb_student.isChecked())
            {
                UserLogin(URL_STUDENTLOGIN);
            }
            else
            {
                Toast.makeText(LoginActivity.this, "Please select admin or student radio button", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            et_username.setError("Please enter username.");
            et_password.setError("Please enter password");
        }
    }

    // Void method to register new admin or student
    private void RegisterAdminStudent()
    {
        if(rb_admin.isChecked())
        {
            startActivity(new Intent(getApplicationContext(), NewAdminActivity.class));
        }
        else if(rb_student.isChecked())
        {
            startActivity(new Intent(getApplicationContext(), NewStudentActivity.class));
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Please select admin or student radio button", Toast.LENGTH_LONG).show();
        }
    }

    // Void method for admin and student to login.
    private void UserLogin(String url)
    {
        if(url.equals("http://10.0.2.2:8080/pccphp/admin_login.php"))
        {
            StringRequest request = new StringRequest(
                    Request.Method.POST, URL_ADMINLOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");
                        if(success.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); ++i) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id").trim();
                                String name = object.getString("name").trim();
                                String username = object.getString("username").trim();
                                sessionAdminManager.createSession(id, name, username);
                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                et_username.setText(null);
                                et_password.setText(null);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Incorrect username or password ", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"An error occurred!", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("username", et_username.getText().toString());
                    params.put("password", et_password.getText().toString());
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }
        else
        {
            StringRequest request = new StringRequest(
                    Request.Method.POST, URL_STUDENTLOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");
                        if(success.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); ++i) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id").trim();
                                String name = object.getString("name").trim();
                                String username = object.getString("username").trim();
                                String email = object.getString("email").trim();
                                String phone = object.getString("phone").trim();
                                String major = object.getString("major").trim();
                                sessionStudentManager.createSession(id, name, username, email, phone, major);
                                Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("phone", phone);
                                intent.putExtra("major", major);
                                startActivity(intent);
                                et_username.setText(null);
                                et_password.setText(null);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Incorrect username or password ", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"An error occurred!", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("username", et_username.getText().toString());
                    params.put("password", et_password.getText().toString());
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }
    }
}