package com.example.philipkim.pcc;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FacultyActivity extends AppCompatActivity {
    static String URL_FACULTYINFO = "http://10.0.2.2:8080/pccphp/faculty_readdetail.php";
    static String studentName, studentPhone, studentEmail;
    private FacultyListAdapter adapter;
    private ListView facultyListView;
    private RequestQueue requestQueue;
    private ArrayList<FacultyInformation> facultyList;
    Button btn_facultyInfoLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        btn_facultyInfoLogout = findViewById(R.id.button3);
        facultyListView = findViewById(R.id.facultyListView);
        requestQueue = Volley.newRequestQueue(this);
        facultyList = new ArrayList<>();
        Intent studentInfo = getIntent();
        studentName = studentInfo.getStringExtra("studentName");
        studentPhone = studentInfo.getStringExtra("studentPhone");
        studentEmail = studentInfo.getStringExtra("studentEmail");
        getFacultyInfo();

        btn_facultyInfoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyActivity.this, QuestionsActivity.class);
                intent.putExtra("studentName", studentName);
                intent.putExtra("studentPhone", studentPhone);
                intent.putExtra("studentEmail", studentEmail);
                startActivity(intent);
            }
        });
    }
    // Get faculty information from database and add to ArrayList<FacultyInformation>
    private void getFacultyInfo()
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_FACULTYINFO, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    for(int i = 0; i < jsonArray.length(); ++i)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strFacultyName = object.getString("SEND_NAME");
                        String strFacultyTitle = "Title: " + object.getString("SEND_TITLE");
                        String strFacultyPhone = "Phone: " + object.getString("SEND_PHONE");
                        String strFacultyEmail = "Email: " + object.getString("SEND_EMAIL");
                        facultyList.add(new FacultyInformation(strFacultyName, strFacultyTitle, strFacultyPhone, strFacultyEmail));
                    }
                    adapter = new FacultyListAdapter(FacultyActivity.this, R.layout.adapter_appointmentview_layout, facultyList);
                    facultyListView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(FacultyActivity.this, "Error reading faculty data." + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacultyActivity.this, "Error response for faculty data." + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}