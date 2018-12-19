package com.example.philipkim.pcc;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class StudentActivity extends AppCompatActivity {
    static String URL_READ = "http://10.0.2.2:8080/pccphp/student_readdetail.php";
    static String URL_EDIT = "http://10.0.2.2:8080/pccphp/student_editdetail.php";
    private String getStudentID, getStudentName, getStudentPhone, getStudentEmail;
    private EditText et_stuFullName, et_stuName, et_stuEmail, et_stuPhone, et_stuMajor;
    private TextView et_greeting;
    private Menu action;
    Button btn_studentLogout, btn_studentAppointment, btn_studentQuestion;
    SessionStudentManager sessionStudentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        // Create session object
        sessionStudentManager = new SessionStudentManager(this);
        sessionStudentManager.checkLogin();
        HashMap<String, String> user = sessionStudentManager.getUserDetail();
        getStudentID = user.get(sessionStudentManager.ID);
        getStudentName = user.get(sessionStudentManager.NAME);
        getStudentPhone = user.get(sessionStudentManager.PHONE);
        getStudentEmail = user.get(sessionStudentManager.EMAIL);


        // Cast components
        et_greeting = findViewById(R.id.studentTextView);
        et_stuFullName = findViewById(R.id.stuNameEditEditText);
        et_stuName = findViewById(R.id.stuEditEditText);
        et_stuEmail = findViewById(R.id.stuEmailEditEditText);
        et_stuPhone = findViewById(R.id.stuPhoneEditEditText);
        et_stuMajor = findViewById(R.id.stuMajorEditEditText);
        btn_studentAppointment = findViewById(R.id.appointmentBtn);
        btn_studentQuestion = findViewById(R.id.studentQuestionBtn);
        btn_studentLogout = findViewById(R.id.logoutBtn);

        // Set edit text enabled to false
        et_stuFullName.setEnabled(false);
        et_stuName.setEnabled(false);
        et_stuEmail.setEnabled(false);
        et_stuPhone.setEnabled(false);
        et_stuMajor.setEnabled(false);

        // Appointment page
        btn_studentAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, AppointmentActivity.class);
                intent.putExtra("studentName", getStudentName);
                intent.putExtra("studentID", getStudentID);
                startActivity(intent);

            }
        });

        // Questions & Answers page
        btn_studentQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, QuestionsActivity.class);
                intent.putExtra("studentName", getStudentName);
                intent.putExtra("studentPhone", getStudentPhone);
                intent.putExtra("studentEmail", getStudentEmail);
                startActivity(intent);
            }
        });

        // Logout to homepage
        btn_studentLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionStudentManager.logout();
                //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    /*
     * A void method to get the users information
     * */
    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    /*
     * A void method to get user details.
     * */
    private void getUserDetail()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("read");
                    if(success.equals("1"))
                    {
                        for(int i = 0; i < jsonArray.length(); ++i)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String strID = object.getString("id").trim();
                            String strName = object.getString("name").trim();
                            String strUser = object.getString("username").trim();
                            String strEmail = object.getString("email").trim();
                            String strPhone = object.getString("phone").trim();
                            String strMajor = object.getString("major").trim();
                            et_greeting.setText(String.format("Welcome %s\nID #: %s", strName, strID));
                            et_stuFullName.setText(strName);
                            et_stuName.setText(strUser);
                            et_stuEmail.setText(strEmail);
                            et_stuPhone.setText(strPhone);
                            et_stuMajor.setText(strMajor);
                            //Intent intent = new Intent(StudentActivity.this, AppointmentActivity.class);
                            //intent.putExtra("studentName", strName);
                            //startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentActivity.this, "Error getting student detail" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentActivity.this, "Error reading detail!!!!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", getStudentID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*
     * A void method to save user details.
     * */
    private void SaveEditDetail()
    {
        final String id = getStudentID;
        final String name = et_stuFullName.getText().toString().trim();
        final String username = et_stuName.getText().toString().trim();
        final String email = et_stuEmail.getText().toString().trim();
        final String phone = et_stuPhone.getText().toString().trim();
        final String major = et_stuMajor.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1"))
                    {
                        Toast.makeText(StudentActivity.this, "SAVED!", Toast.LENGTH_SHORT).show();
                        sessionStudentManager.createSession(id, name, username, email, phone, major);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentActivity.this, "ERROR! " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentActivity.this, "ERROR! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("username", username);
                params.put("email", email);
                params.put("phone", phone);
                params.put("major", major);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*
     * A boolean method to save the information if edited.
     * @params Menu menu
     * @return true if clicked on edit button
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        return true;
    }

    /*
     * A boolean method to save the information if edited.
     * @params MenuItem item
     * @return true if clicked on save button
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_edit:
                et_stuFullName.setEnabled(true);
                et_stuName.setEnabled(true);
                et_stuEmail.setEnabled(true);
                et_stuPhone.setEnabled(true);
                et_stuMajor.setEnabled(true);
                et_stuFullName.setFocusableInTouchMode(true);
                et_stuName.setFocusableInTouchMode(true);
                et_stuEmail.setFocusableInTouchMode(true);
                et_stuPhone.setFocusableInTouchMode(true);
                et_stuMajor.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_stuName, InputMethodManager.SHOW_IMPLICIT);
                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);
                return true;
            case R.id.menu_save:
                SaveEditDetail();
                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);
                et_stuFullName.setEnabled(false);
                et_stuName.setEnabled(false);
                et_stuEmail.setEnabled(false);
                et_stuPhone.setEnabled(false);
                et_stuMajor.setEnabled(false);
                et_stuFullName.setFocusableInTouchMode(false);
                et_stuName.setFocusableInTouchMode(false);
                et_stuEmail.setFocusableInTouchMode(false);
                et_stuPhone.setFocusableInTouchMode(false);
                et_stuMajor.setFocusableInTouchMode(false);
                et_stuFullName.setFocusable(false);
                et_stuName.setFocusable(false);
                et_stuEmail.setFocusable(false);
                et_stuPhone.setFocusable(false);
                et_stuMajor.setFocusable(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}