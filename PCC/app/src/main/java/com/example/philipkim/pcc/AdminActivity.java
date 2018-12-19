package com.example.philipkim.pcc;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    static String URL_ADMINREAD = "http://10.0.2.2:8080/pccphp/admin_readdetail.php";
    static String URL_ADMINAPPOINTMENT = "http://10.0.2.2:8080/pccphp/admin_setappointment.php";
    static String URL_ADMINCHECKDATE = "http://10.0.2.2:8080/pccphp/admin_checkappointment.php";
    private static String getAdminID, getAdminName, getAdminDate, checkAdminName;
    static String formatTime;
    private ArrayList<String> saveDateName;
    private TextView tv_adminGreeting;
    private EditText et_appDate, et_appStartTime, et_appEndTime;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener startTimeSetListener;
    private TimePickerDialog.OnTimeSetListener endTimeSetListener;
    private RequestQueue requestQueue;
    Button btn_appDate, btn_appStartTime, btn_appEndTime, btn_adminLogout, btn_setAppointment, btn_addFaculty;
    SessionAdminManager sessionAdminManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Create session object
        sessionAdminManager = new SessionAdminManager(this);
        sessionAdminManager.checkLogin();
        HashMap<String, String> user = sessionAdminManager.getAdminDetail();
        getAdminID = user.get(sessionAdminManager.ID);
        getAdminName = user.get(sessionAdminManager.NAME);
        getAdminDate = user.get(sessionAdminManager.DATE);

        // Initialized object
        saveDateName = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        // Cast components
        tv_adminGreeting = findViewById(R.id.adminTextView);
        btn_appDate = findViewById(R.id.dateBtn);
        btn_appStartTime = findViewById(R.id.startTimeBtn);
        btn_appEndTime = findViewById(R.id.endTimeBtn);
        btn_setAppointment = findViewById(R.id.makeAppBtn);
        btn_addFaculty = findViewById(R.id.addFacultyBtn);
        btn_adminLogout = findViewById(R.id.adminLogoutBtn);
        et_appDate = findViewById(R.id.appDateEditText);
        et_appStartTime = findViewById(R.id.startTimeEditText);
        et_appEndTime = findViewById(R.id.endTimeEditText);

        // Set edit text enable to false
        et_appDate.setEnabled(false);
        et_appStartTime.setEnabled(false);
        et_appEndTime.setEnabled(false);

        // Set make appointment button enable to false
        btn_setAppointment.setEnabled(false);

        getAppointmentDateData();

        // Set date for appointments
        btn_appDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalendar();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDateET(year, month, dayOfMonth, et_appDate);
            }
        };


        // Set starting time for appointments
        btn_appStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(startTimeSetListener);
            }
        });
        startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                getTime(hourOfDay, minute, et_appStartTime);
            }
        };


        // Set ending time for appointments
        btn_appEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_setAppointment.setEnabled(true);
                setTime(endTimeSetListener);
            }
        });
        endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                getTime(hourOfDay, minute, et_appEndTime);
            }
        };


        // Add appointment to MySQL
        btn_setAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateAppointment();
            }
        });

        // Add faculty
        btn_addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFacultyToDB();
            }
        });

        // Admin logout to login page
        btn_adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveDateName.clear();
                sessionAdminManager.logout();
            }
        });
    }

    // Add appointment
    private void ValidateAppointment()
    {
        if(!saveDateName.contains(checkAdminName + "$" + et_appDate.getText().toString().trim()))
        {
            if(isValidScheduleTime(et_appStartTime.getText().toString().trim(), et_appEndTime.getText().toString().trim()))
            {
                AddAppointment();
            }
            else
            {
                et_appStartTime.setError("You must work 6 - 10 hours!");
                et_appEndTime.setError("You must work 6 - 10 hours!");
                Toast.makeText(AdminActivity.this, "You must work 8 hours.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(AdminActivity.this, "Appointment is already set that day.", Toast.LENGTH_SHORT).show();
        }
    }

    // Add appointments to MySQL
    private void AddAppointment()
    {
        final String id = getAdminID;
        final String name = getAdminName;
        final String date = et_appDate.getText().toString().trim();
        final String stime = et_appStartTime.getText().toString().trim();
        final String etime = et_appEndTime.getText().toString().trim();
        StringBuilder sb = new StringBuilder();
        for(String i : addAppointment())
            sb.append(i).append(";");
        final String _remaining = sb.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADMINAPPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1"))
                            {
                                Toast.makeText(AdminActivity.this,"APPOINTMENT ADDED!", Toast.LENGTH_SHORT).show();
                                saveDateName.clear();
                                sessionAdminManager.logout();
                            }
                            else
                            {
                                Toast.makeText(AdminActivity.this,"APPOINTMENT ERROR!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AdminActivity.this,"ERROR OCCURRED!!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminActivity.this,"APPOINTMENT ERROR OCCURRED!!!!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("date", date);
                params.put("stime", stime);
                params.put("etime", etime);
                params.put("times", _remaining);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // get appointment data from database
    private void getAppointmentDateData()
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ADMINCHECKDATE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    for(int i = 0; i < jsonArray.length(); ++i)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strAppDate = object.getString("SEND_DATE");
                        String strDeanName = object.getString("SEND_NAME");
                        saveDateName.add(strDeanName + "$" + strAppDate);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdminActivity.this, "Error reading date." + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminActivity.this, "Error response for date." + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    // check for admin schedule hours is valid & check validation for admins selected hours
    private static boolean isValidScheduleTime(String start, String end)
    {
        int countHours = validateHours(start, end);
        int startTime = Integer.parseInt(start.substring(0, 2));
        int endTime = Integer.parseInt(end.substring(0, 2));
        String am = start.substring(6, 8);
        String pm = end.substring(6, 8);
        // Working hours need to be in the range of 6-10 hours & Working time needs to at least start at 8:00 AM and end at most end at 6:00 PM
        return ((countHours >= 6 && countHours <= 10) && (am.contains("AM") && pm.contains("PM")) && (startTime >= 8 && endTime <= 6));
    }

    // count the hours admin selected
    private static int validateHours(String start, String end)
    {
        int countTime;
        int begTime;
        int endTime;
        int tempTime = Integer.parseInt(start.substring(0, 2));
        String strAM = start.substring(6, 8);
        String strPM = end.substring(6, 8);
        if(strAM.equals("AM") && strPM.equals("PM"))
        {
            begTime = 12 - Integer.parseInt(start.substring(0, 2));
            endTime = Integer.parseInt(end.substring(0, 2));
            countTime = begTime + endTime;
            if(countTime > 12)
                countTime -= 12;
        }
        else
        {
            if(tempTime == 12)
                begTime = 0;
            else
                begTime = Integer.parseInt(start.substring(0, 2));
            endTime = Integer.parseInt(end.substring(0, 2));
            countTime = endTime - begTime;
            if(countTime > 12)
                countTime -= 12;
        }
        return countTime;
    }

    // set date with calendar
    private void setCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(AdminActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // set date onto edit text
    public void setDateET(int y, int m, int d, EditText t)
    {
        String strYear = Integer.toString(y);
        strYear.replace(",", "");
        t.setText(MessageFormat.format("{0}/{1}/{2}", m + 1, d, strYear));
    }

    // set time with timer
    private void setTime(TimePickerDialog.OnTimeSetListener time)
    {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(AdminActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, time, hour, minute,false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // get time from edit text
    private void getTime(int hour, int min, EditText et)
    {
        if(hour == 0)
        {
            hour += 12;
            if(min >= 30)
                hour = 1;
            formatTime = "AM";
        }
        else if(hour == 12)
        {
            if(min >= 30)
                hour = 1;
            formatTime = "PM";
        }
        else if(hour > 12)
        {
            hour -= 12;
            if(min >= 30)
                ++hour;
            formatTime = "PM";
        }
        else
        {
            if(min >= 30)
                ++hour;
            formatTime = "AM";
        }
        String strHr = Integer.toString(hour);
        if(strHr.length() < 2)
            strHr = "0" + hour;
        et.setText(new StringBuilder().append(strHr).append(":").append("00").append(" ").append(formatTime));
    }

    // add faculty
    private void addFacultyToDB()
    {
        String _date = et_appDate.getText().toString();
        String _stime = et_appStartTime.getText().toString();
        String _etime = et_appEndTime.getText().toString();
        if(!_date.isEmpty() || !_stime.isEmpty() || !_etime.isEmpty())
        {
            Toast.makeText(AdminActivity.this,"Select set appointment!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            startActivity(new Intent(getApplicationContext(), NewFacultActivity.class));
        }
    }

    // A void method to get the users information
    @Override
    protected void onResume() {
        super.onResume();
        getAdminDetail();
    }

    // A void method to get user details.
    private void getAdminDetail()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADMINREAD, new Response.Listener<String>() {
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
                            String strAdminID = object.getString("id").trim();
                            String strAdminName = object.getString("name").trim();
                            checkAdminName = strAdminName;
                            tv_adminGreeting.setText(String.format("Welcome %s\nID#: %s", strAdminName, strAdminID));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdminActivity.this, "Cannot read admin detail" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminActivity.this, "Error reading detail!!!!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", getAdminID);
                params.put("name", getAdminName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // convert working hours into intervals of 20 minutes
    private String[] timeIntervals(String stime, int length)
    {
        String AMPM = "0 AM";
        String[] times = new String[length];
        int hour = Integer.parseInt(stime.substring(0, 2));
        int minute = 0;
        // Increment appointment schedule
        for(int i = 0; i < length; ++i)
        {
            if(hour > 11)
            {
                AMPM = "0 PM";
                hour -= 12;
            }
            if((i % 2 == 0))
            {
                // add 20min
                times[i] = Integer.toString(hour) + ":" + Integer.toString(minute) + AMPM + " - " +
                        Integer.toString(hour) + ":" + Integer.toString(minute+2) + AMPM;
                --hour;
            }
            else
            {
                // add 10min
                times[i] = Integer.toString(hour) + ":" + Integer.toString(minute+3) + AMPM + " - " +
                        Integer.toString(hour) + ":" + Integer.toString(minute+5) + AMPM;
            }
            ++hour;
            if(hour == 1)
            {
                times[i-1] = "LUNCH BREAK";
            }
        }
        return times;
    }

    private ArrayList<String> addAppointment()
    {
        return new ArrayList<>(Arrays.asList(timeIntervals(et_appStartTime.getText().toString().trim(), validateHours(et_appStartTime.getText().toString().trim(), et_appEndTime.getText().toString().trim())*2)));
    }
}