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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class NewAdminActivity extends AppCompatActivity {
    static String URL_ADMINREGISTER = "http://10.0.2.2:8080/pccphp/admin_register.php";
    private EditText et_adminName, et_adminUsername, et_adminPassword, et_adminCode;
    Button btn_adminRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_admin);

        // Cast components
        et_adminName = findViewById(R.id.adminNameEditText);
        et_adminUsername = findViewById(R.id.adminUsernameEditText);
        et_adminPassword = findViewById(R.id.adminPasswordEditText);
        et_adminCode = findViewById(R.id.adminCodeEditText);
        btn_adminRegister = findViewById(R.id.adminRegisterButton);

        // Register new admin
        btn_adminRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAdminToMySQL();
            }
        });
    }

    /*
     * A void method to add new admin to MySQL
     * */
    private void addNewAdminToMySQL()
    {
        String adminName = et_adminName.getText().toString().trim();
        String adminUsername = et_adminUsername.getText().toString().trim();
        String adminPassword = et_adminPassword.getText().toString().trim();
        String adminCode = et_adminCode.getText().toString().trim();

        if(!adminName.isEmpty() && !adminUsername.isEmpty() && !adminPassword.isEmpty() && !adminCode.isEmpty())
        {
            if(isValidAdminCode(et_adminCode) && isValidAdminPassword(et_adminPassword))
            {
                RegisterAdmin();
            }
        }
        else
        {
            et_adminName.setError("Please enter full name.");
            et_adminUsername.setError("Please enter user name.");
            et_adminPassword.setError("Please enter password.");
            et_adminCode.setError("Please enter admin code.");
        }
    }

    /*
     * A void method to register new students.
     * */
    private void RegisterAdmin()
    {
        // Set String to EditText
        final String id = idAdminGenerator();
        final String name = et_adminName.getText().toString().trim();
        final String username = et_adminUsername.getText().toString().trim();
        final String password = et_adminPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADMINREGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1"))
                            {
                                // Go to StudentActivity after successful login.
                                Intent intent = new Intent(NewAdminActivity.this, AdminActivity.class);
                                intent.putExtra("adminID", id);
                                intent.putExtra("adminName", name);
                                startActivity(intent);
                                // Set EditText to null after successful login.
                                et_adminName.setText(null);
                                et_adminUsername.setText(null);
                                et_adminPassword.setText(null);
                                et_adminCode.setText(null);
                            }
                            else
                            {
                                Toast.makeText(NewAdminActivity.this,"REGISTRATION ERROR!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewAdminActivity.this,"ERROR OCCURRED!!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewAdminActivity.this,"REGISTRATION ERROR OCCURRED!!!!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*
     * A void method to validate admin code
     * */
    private boolean isValidAdminCode(EditText code)
    {
        if(!code.getText().toString().trim().equals("666"))
        {
            Toast.makeText(NewAdminActivity.this,"ENTER ADMIN CODE", Toast.LENGTH_SHORT).show();
            et_adminCode.setError("Please enter admin code.");
            return false;
        }
        return true;
    }

    /*
     * A void method to validate password length
     * */
    private boolean isValidAdminPassword(EditText password)
    {
        if(password.getText().toString().trim().length() < 4)
        {
            Toast.makeText(NewAdminActivity.this,"ENTER 4 CHARACTERS FOR PASSWORD", Toast.LENGTH_SHORT).show();
            et_adminPassword.setError("Please enter at least 4 characters for a password.");
            return false;
        }
        return true;
    }

    /*
     * A String method to generate random student ID number.
     * */
    private String idAdminGenerator()
    {
        String newId;
        int random = (int)(Math.random() * 99999999 + 1);
        newId = Integer.toString(random);
        return newId;
    }
}
