package com.example.philipkim.pcc;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {
    static String URL_GETAPPOINTMENT = "http://10.0.2.2:8080/pccphp/admin_getappointment.php";
    static String URL_EDITAPPOINTMENT = "http://10.0.2.2:8080/pccphp/admin_editappointment.php";
    static String URL_STUDENTSETAPPOINTMENT = "http://10.0.2.2:8080/pccphp/student_setappointment.php";
    static String URL_STUDENTGETAPPOINTMENT = "http://10.0.2.2:8080/pccphp/student_getappointment.php";
    static String URL_STUDENTEDITAPPOINTMENT = "http://10.0.2.2:8080/pccphp/student_editappointment.php";
    static String adminName, adminAppDate, adminIDNum, adminAppTime, studentName, studentID, studentSID,
            studentAppTime, studentAppDate, studentAppDean, dateSelection, appScheduleTime, scheduleAmPm,
            scheduleDay, appCheckInTime, checkInAmPm, checkDay, appReason, appWaitTime;
    int removePosition, removeAppPosition, removeAppCheckInPosition, checkPosition, checkCheckInPosition, makePosition, counter;
    String[] times, appointments;
    Button btn_button, btn_reset, btn_logout;
    private ImageView iv_logo;
    private ListView lv_appointment;
    private TextView tv_text;
    private AppointmentListAdapter adapter;
    private ArrayList<AppointmentInformation> appointmentList;
    private ArrayList<StudentAppointmentInformation> studentAppointmentList;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Intent stu = getIntent();
        lv_appointment = findViewById(R.id.appListView);
        iv_logo = findViewById(R.id.appImageVIew);
        tv_text = findViewById(R.id.textView);
        btn_button = findViewById(R.id.button);
        btn_reset = findViewById(R.id.button2);
        btn_logout = findViewById(R.id.studentHomePageBtn);
        appointmentList = new ArrayList<>();
        studentAppointmentList = new ArrayList<>();
        studentName = stu.getStringExtra("studentName");
        studentID = stu.getStringExtra("studentID");
        counter = 0;

        // invoke current time and get student appointment list
        currentTime();
        getStudentAppointment();

        // list of available appointments
        lv_appointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectAppointmentTime(position);
            }
        });

        // check appointments
        btn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_logo.setVisibility(View.GONE);
                checkAppointments();
            }
        });

        // date selection
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_logo.setVisibility(View.GONE);
                setCalendar();
            }
        });

        // set date on edit text and then get date from database
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year, month, dayOfMonth);
            }
        };

        // logout to student home page
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StudentActivity.class));
            }
        });
    }
    
    // set date to date listener
    private void setDate(int y, int m, int d)
    {
        if(counter <= 1)
        {
            setDateET(y, m, d);
            getAppointmentData();
        }
        else
        {
            appointmentList.clear();
            adapter.notifyDataSetChanged();
            setDateET(y, m, d);
            getAppointmentData();
        }
    }

    // set date with calendar
    public void setCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(AppointmentActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
        dialog.setTitle("Select appointment dates");
        dialog.setIcon(R.drawable.dates);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    if(counter <= 1)
                    {
                        dialog.cancel();
                    }
                    else
                    {
                        appointmentList.clear();
                        adapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                    iv_logo.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // set date onto edit text
    public void setDateET(int y, int m, int d)
    {
        dateSelection = (m+1) + "/" + d + "/" + y;
    }

    // check appointments
    private void checkAppointments()
    {
        if(appointments[0].equals("EMPTY"))
        {
            final AlertDialog.Builder statusBuilder = new AlertDialog.Builder(AppointmentActivity.this);
            statusBuilder.setTitle(studentName);
            statusBuilder.setIcon(R.drawable.status);
            statusBuilder.setMessage("No appointments");
            statusBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    iv_logo.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = statusBuilder.create();
            alertDialog.show();
        }
        else
        {
            final AlertDialog.Builder statusBuilder = new AlertDialog.Builder(AppointmentActivity.this);
            statusBuilder.setTitle(studentName);
            statusBuilder.setIcon(R.drawable.status);
            statusBuilder.setSingleChoiceItems(appointments , -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            // Click cancel appointment button for status
            statusBuilder.setNegativeButton("Cancel-Appointment", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    removeAppPosition = checkPosition;
                    studentSID = studentAppointmentList.get(removeAppPosition).getStudentID();
                    studentAppTime = studentAppointmentList.get(removeAppPosition).getStudentAppTime();
                    studentAppDate = studentAppointmentList.get(removeAppPosition).getStudentAppDate();
                    studentAppDean = studentAppointmentList.get(removeAppPosition).getStudentName();
                    final String strMessage = " is cancelled.";
                    final AlertDialog.Builder cancelAppBuilder = new AlertDialog.Builder(AppointmentActivity.this);
                    cancelAppBuilder.setTitle("CANCEL APPOINTMENT");
                    cancelAppBuilder.setIcon(R.drawable.cancelapp);
                    cancelAppBuilder.setMessage("SID: " + studentSID + "\nDATE: " + studentAppDate + "\nTIME:" + studentAppTime + "\nDEAN: " + studentAppDean);
                    cancelAppBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            completeAppointmentDate(strMessage);
                            startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                        }
                    });
                    cancelAppBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            iv_logo.setVisibility(View.VISIBLE);
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = cancelAppBuilder.create();
                    alertDialog.show();
                }
            });
            // Click check-in appointment button for status
            statusBuilder.setPositiveButton("Check-in-Appointment", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkCheckInPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    removeAppCheckInPosition = checkCheckInPosition;
                    studentSID = studentAppointmentList.get(removeAppCheckInPosition).getStudentID();
                    studentAppTime = studentAppointmentList.get(removeAppCheckInPosition).getStudentAppTime();
                    studentAppDean = studentAppointmentList.get(removeAppCheckInPosition).getStudentName();
                    final String strMessageRdy = " is now ready.";
                    final String strMessageReschedule = " past due and be can be rescheduled.";
                    final String strMessageLate = " past due.";
                    final AlertDialog.Builder checkInAppBuilder = new AlertDialog.Builder(AppointmentActivity.this);
                    checkInAppBuilder.setTitle("CHECK-IN APPOINTMENT");
                    checkInAppBuilder.setIcon(R.drawable.checkinapp);
                    checkInAppBuilder.setMessage(studentAppointmentList.get(removeAppCheckInPosition).getStudentName() + "\nCheck-in time: " + studentAppointmentList.get(removeAppCheckInPosition).getStudentAppTime().substring(0, 8).trim() + "\n" + "Current time: " + appScheduleTime);
                    checkInAppBuilder.setPositiveButton("check-in", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appCheckInTime = studentAppointmentList.get(removeAppCheckInPosition).getStudentAppTime().substring(0, 8).trim();
                            checkInAmPm = studentAppointmentList.get(removeAppCheckInPosition).getStudentAppTime().substring(5, 8).trim();
                            checkDay = studentAppointmentList.get(removeAppCheckInPosition).getStudentAppDate().substring(3, 5).trim();
                             if(appCheckInTime.length() < 8)
                             {
                                 appCheckInTime = "0" + studentAppointmentList.get(removeAppCheckInPosition).getStudentAppTime().substring(0, 8).trim();
                                 checkInAmPm = studentAppointmentList.get(removeAppCheckInPosition).getStudentAppTime().substring(4, 7).trim();
                             }
                            String str = appCheckInTime.substring(0, 5).replace(":", "");
                            String str1 = appScheduleTime.substring(0, 5).replace(":", "");
                            int istr = Integer.parseInt(str);
                            int istr1 = Integer.parseInt(str1);
                            int dayStr = Integer.parseInt(checkDay);
                            int dayStr1 = Integer.parseInt(scheduleDay);
                            if(isOnTime(istr, dayStr, checkInAmPm, istr1, dayStr1, scheduleAmPm))
                            {
                                appWaitTime = isOnTimeMsg(appCheckInTime, appScheduleTime);
                                final AlertDialog.Builder checkinAppBuilder = new AlertDialog.Builder(AppointmentActivity.this);
                                final String[] choices = new String[] {"Transferring", "Academic Appeal", "Grade Appeal", "Complaints", "Other"};
                                checkinAppBuilder.setTitle("Reason for appointment");
                                checkinAppBuilder.setIcon(R.drawable.checkinapp);
                                checkinAppBuilder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        appReason = choices[which];
                                    }
                                });
                                checkinAppBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final AlertDialog.Builder checkinConfirmAppBuilder = new AlertDialog.Builder(AppointmentActivity.this);
                                        checkinConfirmAppBuilder.setTitle(studentName);
                                        checkinConfirmAppBuilder.setIcon(R.drawable.checkinapp);
                                        checkinConfirmAppBuilder.setMessage(studentAppointmentList.get(removeAppCheckInPosition).getStudentName() + "\nCheck-in time: " + studentAppointmentList.get(removeAppCheckInPosition).getStudentAppTime().substring(0, 8).trim() + "\n" + "Current time: " + appScheduleTime + "\nWaiting time: " + appWaitTime + "\nAppointment reason: " + appReason);
                                        checkinConfirmAppBuilder.setPositiveButton("CHECK-IN", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                completeAppointmentDate(strMessageRdy);
                                                Toast.makeText(AppointmentActivity.this, "You are on time! " +  studentAppDean + " will be with you shortly.", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                                            }
                                        });
                                        AlertDialog alertDialog = checkinConfirmAppBuilder.create();
                                        alertDialog.show();
                                    }
                                });
                                AlertDialog alertDialog = checkinAppBuilder.create();
                                alertDialog.show();
                            }
                            else
                            {
                                final AlertDialog.Builder rescheduleAppBuilder = new AlertDialog.Builder(AppointmentActivity.this);
                                rescheduleAppBuilder.setTitle("LATE APPOINTMENT");
                                rescheduleAppBuilder.setIcon(R.drawable.rescheduleinapp);
                                rescheduleAppBuilder.setMessage(studentAppointmentList.get(removeAppCheckInPosition).getStudentName() + "\nCheck-in time: " + studentAppointmentList.get(removeAppCheckInPosition).getStudentAppTime().substring(0, 8).trim() + "\n" + "Current time: " + appScheduleTime + "\n\nYou're late, would you like to reschedule?");
                                rescheduleAppBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        completeAppointmentDate(strMessageReschedule);
                                        Intent intent = new Intent(AppointmentActivity.this, AppointmentActivity.class);
                                        intent.putExtra("studentName", studentName);
                                        intent.putExtra("studentID", studentID);
                                        startActivity(intent);
                                    }
                                });
                                rescheduleAppBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        completeAppointmentDate(strMessageLate);
                                        startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                                    }
                                });
                                AlertDialog alertDialog = rescheduleAppBuilder.create();
                                alertDialog.show();
                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = checkInAppBuilder.create();
                    alertDialog.show();
                }
            });
            // Click back button for status
            statusBuilder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    iv_logo.setVisibility(View.VISIBLE);
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = statusBuilder.create();
            alertDialog.show();
        }
    }

    // Remove appointment in MySQL
    private void completeAppointmentDate(final String msg)
    {
        final String _strStudentID = studentSID;
        final String _strStudentAppTime = studentAppTime;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_STUDENTEDITAPPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1"))
                    {
                        Toast.makeText(AppointmentActivity.this, "Appointment on " + studentAppTime +  msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AppointmentActivity.this, "ERROR! " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AppointmentActivity.this, "ERROR! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sid", _strStudentID);
                params.put("time", _strStudentAppTime);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // A void method to get user details.
    private void getStudentAppointment()
    {
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL_STUDENTGETAPPOINTMENT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    if(jsonArray.length() < 1)
                    {
                        appointments = new String[jsonArray.length()+1];
                        appointments[0] = "EMPTY";
                    }
                    else
                    {
                        appointments = new String[jsonArray.length()];
                        for(int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String _strID = object.getString("SEND_SID");
                            String _strName = object.getString("SEND_DEAN");
                            String _strAppDate = object.getString("SEND_DATE");
                            String _strAppTime = object.getString("SEND_TIME");
                            studentAppointmentList.add(new StudentAppointmentInformation(_strID, _strName, _strAppDate, _strAppTime));
                            appointments[i] = "[SID:" + _strID + "]\n[Dean: " + _strName + "]\n[Date: " + _strAppDate + "]\n[Time: " + _strAppTime + "]\n";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AppointmentActivity.this, "Error reading appointment data." + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AppointmentActivity.this, "Error response for appointment data." + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Add appointments to MySQL
    private void AddStudentAppointment()
    {
        final String id = studentID;
        final String name = studentName;
        final String dean = adminName.replace("Dean:\n", " ");
        final String date = adminAppDate.replace("Date: ", "");
        final String time = adminAppTime;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_STUDENTSETAPPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(!success.equals("1"))
                            {
                                Toast.makeText(AppointmentActivity.this,"STUDENT APPOINTMENT ERROR!", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AppointmentActivity.this,"ERROR OCCURRED!!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AppointmentActivity.this,"APPOINTMENT ERROR OCCURRED!!!!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("dean", dean);
                params.put("date", date);
                params.put("time", time);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Select appointment time
    private void selectAppointmentTime(int i)
    {
        adminName = appointmentList.get(i).getDeanName();
        adminAppDate = appointmentList.get(i).getAppDate();
        adminIDNum = appointmentList.get(i).getDeanID();
        final AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentActivity.this);
        if(adminName.equals("EMPTY"))
        {
            builder.setTitle(adminName);
            builder.setIcon(R.drawable.icon);
            times = new String[]{"EMPTY"};
            builder.setMessage("Sorry no appointments are available.");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AppointmentActivity.this, AppointmentActivity.class);
                    intent.putExtra("studentName", studentName);
                    intent.putExtra("studentID", studentID);
                    startActivity(intent);
                }
            });
        }
        else
        {
            builder.setTitle(adminName + "\n" + adminAppDate);
            builder.setIcon(R.drawable.icon);
            times = appointmentList.get(i).getAllTimes().split(";");
            builder.setSingleChoiceItems(times, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("Make Appointment", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    makePosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    removePosition = makePosition;
                    adminAppTime = times[removePosition];

                    if(!adminAppTime.equals("LUNCH BREAK"))
                    {
                        final AlertDialog.Builder makeAppBuilder = new AlertDialog.Builder(AppointmentActivity.this);
                        makeAppBuilder.setTitle("MAKE APPOINTMENT");
                        makeAppBuilder.setIcon(R.drawable.makeapp);
                        makeAppBuilder.setMessage("DATE: " + adminAppDate + "\nTIME: " + adminAppTime + "\nDEAN: " + adminName);
                        makeAppBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // save updated appointments times to database
                                editAppointmentDate();
                                AddStudentAppointment();
                                refresh();
                                startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                            }
                        });
                        makeAppBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //refresh();
                                appointmentList.clear();
                                adapter.notifyDataSetChanged();
                                iv_logo.setVisibility(View.VISIBLE);
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = makeAppBuilder.create();
                        alertDialog.show();
                    }
                    else
                    {
                        Toast.makeText(AppointmentActivity.this, "Sorry, " + adminName + " is having lunch at this time.", Toast.LENGTH_LONG).show();
                        appointmentList.clear();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });
            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //refresh();
                    appointmentList.clear();
                    adapter.notifyDataSetChanged();
                    iv_logo.setVisibility(View.VISIBLE);
                    dialog.cancel();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Edit appointment in MySQL
    private void editAppointmentDate()
    {
        final String _strAdminID = adminIDNum;
        final String _strAdminDate = adminAppDate.replace("Date: ", "");
        StringBuilder sb = new StringBuilder();
        for(String i : removeAppointment(times, removePosition))
            sb.append(i).append(";");
        final String _strAdminAllTime = sb.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDITAPPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1"))
                    {
                        Toast.makeText(AppointmentActivity.this, "Appointment on " + times[removePosition] + " was successfully added.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AppointmentActivity.this, "ERROR! " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AppointmentActivity.this, "ERROR! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", _strAdminID);
                params.put("date", _strAdminDate);
                params.put("times", _strAdminAllTime);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Get appointment data from MySQL
    private void getAppointmentData()
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GETAPPOINTMENT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    if(jsonArray.length() < 1)
                    {
                        appointmentList.add(0, new AppointmentInformation("EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"));
                        adapter = new AppointmentListAdapter(AppointmentActivity.this, R.layout.adapter_appointmentview_layout, appointmentList);
                        lv_appointment.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        for(int i = 0; i < jsonArray.length(); ++i)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String strDeanID = object.getString("SEND_ID");
                            String strDeanName = "Dean: " + object.getString("SEND_NAME");
                            String strAppDate = "Date: " + object.getString("SEND_DATE");
                            String strStartTime = "Start: " + object.getString("SEND_STIME");
                            String strEndTime = "End: " + object.getString("SEND_ETIME");
                            String strAllTime = object.getString("SEND_ATIME");
                            String strHr = strStartTime;
                            if(strHr.length() < 2)
                                strHr = "0" + strStartTime;
                            String strMin = strEndTime;
                            if(strMin.length() < 2)
                                strMin = "0" + strEndTime;
                            if(strAppDate.substring(6, 16).equals(dateSelection))
                            {
                                appointmentList.add(new AppointmentInformation(strDeanID, strDeanName, strAppDate, strHr, strMin, strAllTime));
                                ++counter;
                            }
                        }
                        adapter = new AppointmentListAdapter(AppointmentActivity.this, R.layout.adapter_appointmentview_layout, appointmentList);
                        lv_appointment.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AppointmentActivity.this, "Error reading appointment data." + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AppointmentActivity.this, "Error response for appointment data." + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // refresh list view and add new list to list view
    private void refresh()
    {
        // clear and update list view
        lv_appointment.invalidate();
        adapter.clear();
        lv_appointment.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getAppointmentData();
        getStudentAppointment();
    }

    // check to see if they student is on time for appointment
    private boolean isOnTime(int app, int appDay, String appAP, int check, int checkDay, String checkAP)
    {
        return ((app >= check) && (appDay <= checkDay) && (appAP.equals(checkAP)));
    }

    // display the time left until appointment
    private static String isOnTimeMsg(String app, String cur)
    {
        String strAppHour = app.substring(0, 2);
        String strCurHour = cur.substring(0, 2);
        int strAppHourInt = Integer.parseInt(strAppHour);
        int strCurHourInt = Integer.parseInt(strCurHour);
        String strAppMin = app.substring(3, 5);
        String strCurMin = cur.substring(3, 5);
        int strAppMinInt = Integer.parseInt(strAppMin);
        int strCurMinInt = Integer.parseInt(strCurMin);
        int countHour = strAppHourInt - strCurHourInt;
        String msg;
        if(strAppHourInt > strCurHourInt && strAppMin.equals("00"))
        {
            int minWait = 60 - strCurMinInt;
            msg = Math.abs(minWait) + " minutes";
        }
        else if(strAppHourInt > strCurHourInt)
        {
            int minWait = (60 - strCurMinInt) + strAppMinInt;
            msg = Math.abs(minWait) + " minutes";
        }
        else if(countHour > 1)
        {
            msg = "You are " + countHour + " hours early.";
        }
        else
        {
            int minWait = strAppMinInt - strCurMinInt;
            msg = Math.abs(minWait) + " minutes";
        }
        return msg;
    }

    // Display current time
    private void currentTime()
    {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh:mm:ss a");
                                String dateString = sdf.format(date);
                                appScheduleTime = dateString.substring(11, 17).trim() + " " + dateString.substring(21, 23).trim();
                                scheduleAmPm = dateString.substring(21, 23).trim();
                                scheduleDay = dateString.substring(4, 6).trim();
                                tv_text.setBackgroundColor(Color.parseColor("#e7e7e7"));
                                tv_text.setText(String.format("Welcome %s!\n\nDate: %s", studentName, dateString.replace("\n", "\nTime: ")));
                            }
                        });
                    }
                }
                catch (InterruptedException e)
                {
                    Toast.makeText(AppointmentActivity.this, "Error with current time." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        t.start();
    }

    // ArrayList to remove selected appointment.
    private ArrayList<String> removeAppointment(String[] appointments, int removeApp)
    {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(appointments));
        list.remove(removeApp);
        return list;
    }
}