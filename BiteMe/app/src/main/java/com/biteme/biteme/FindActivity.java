package com.biteme.biteme;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.Profile;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FindActivity extends AppCompatActivity {

    static int hour, min;

    TextView txtdate, txttime;
    Button btntimepicker, btndatepicker;
    TextView selectedfriends;

    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;
    int year, month, day;
    Socket socket;
    String newstringid="";
    String key = "0k8j7h6g5f4d3s2ak8j7h6g5f4d3s2a";
    //iv string must be max 16 char
    String iv = "0k8j7h6g5f4d3s2a";
    String name;
    public static List<String> datalistname = new ArrayList<String>();
    public static List<String> datalistid = new ArrayList<String>();
    ListView lview = null;
    String fromuser="";
    customAdapter adapter;
    String meetingname="";
    String meetingadress = "";
    String dateInString;
    String dtStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_find);

        Intent startingintent = getIntent();
         String from_user = startingintent.getStringExtra("FROM_USER");
        fromuser = from_user;
        String place = startingintent.getStringExtra("place");
        String[] parts = place.split(":");
         meetingname = parts[0]; // 004
         meetingadress = parts[1];
        TextView meetingplace = (TextView) findViewById(R.id.Meetingplace);
        meetingplace.setText("Restaurant: "+meetingname);
        TextView meetingadres = (TextView) findViewById(R.id.Meetingadress);
        meetingadres.setText("Adress: "+meetingadress);

        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);


        lview = (ListView) findViewById(R.id.listviewFind);
        adapter = new customAdapter(this, R.layout.list_find, datalistname);
        lview.setAdapter(adapter);
        final EditText find_txt = (EditText) findViewById(R.id.find_txt);
        Button senddatetofriend = (Button) findViewById(R.id.invitefriendsbutton);
        senddatetofriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senddaterequest();
            }
        });


        find_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String findtext = find_txt.getText().toString();
                if(!findtext.isEmpty()){
                    if(findtext.startsWith(" ") == false){
                        find(findtext);
                    }
                }
                else{
                    datalistname.clear();
                    datalistid.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime = (TextView) findViewById(R.id.txttime);

        btndatepicker = (Button) findViewById(R.id.btndatepicker);
        btntimepicker = (Button) findViewById(R.id.btntimepicker);

        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(FindActivity.this, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
                                    dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);


                                    formatter = new SimpleDateFormat("dd/mm/yyyy");

                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                                } catch (Exception ex) {

                                }
                            }
                        }, year, month, day);
                dd.show();
            }
        });

        btntimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog td = new TimePickerDialog(FindActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    txttime.setText(String.valueOf(timeValue));
                                } catch (Exception ex) {
                                    txttime.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(FindActivity.this)
                );
                td.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    private void senddaterequest()  {
        final Thread sendfind = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    socket = new Socket(InetAddress.getByName("94.226.137.151"), 9000);
                    //zend de api call naar de server
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        String output = Crypto.encrypts(("add{\"yourid\":\"" + Profile.getCurrentProfile().getId()+ "\",\"friendid\":\"" + newstringid + "\",\"place\":\"" + meetingadress + "\",\"res\":\""+meetingname+"\",\"time\":\""+dateInString+" "+ dtStart +"\"}"), key, iv);

                        OutputStream out = socket.getOutputStream();
                        out.write(output.getBytes(), 0, output.length());
                    }

                    Intent i = new Intent(FindActivity.this, MapsActivity.class);
                    startActivity(i);

                    runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Date requests have been sent.", Toast.LENGTH_SHORT).show();
                            }
                        });

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        sendfind.start();
        customAdapter.selectedfriendslistids.clear();
        customAdapter.selectedfriendslist.clear();
    }


    private void find(String data){
        name = data;
        final Thread sendfind = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    datalistname.clear();
                    datalistid.clear();
                    //connect naar de server
                    socket = new Socket(InetAddress.getByName("94.226.137.151"), 9000);
                    //zend de api call naar de server
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        String output = Crypto.encrypts("findname" + name, key, iv);
                        OutputStream out = socket.getOutputStream();
                        out.write(output.getBytes(), 0, output.length());
                    }

                    //krijg data terug van de server en slaag deze op in response
                    int bytesRead;
                    String response = "";
                    byte[] buffer = new byte[1000];
                    InputStream in = socket.getInputStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);
                    while ((bytesRead = in.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                        response += byteArrayOutputStream.toString("UTF-8");

                        break;
                    }
                    response = Crypto.decrypts(response, key, iv);
                    //zorg er voor dat elk json object word gesplits in elks een object
                    String[] respondArray = response.split("\\}");

                    //proces de json data en add een element van deze data in de arrays
                    try {
                        for (String res : respondArray) {
                            JSONObject json = new JSONObject(res + "}");

                            String id = (String) json.get("id").toString();
                            newstringid = id;
                            String name = (String) json.get("name").toString();
                            datalistid.add(id);
                            datalistname.add(name);
                            Log.v("get" , "id: "+id+"      name: "+name);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                selectedfriends = (TextView) findViewById(R.id.dateselectedfriends);
                                String old= "Selected Friends: "+customAdapter.selectedfriendslist;
                                String newstring = old.replace("[", "").replace("]","");
                                selectedfriends.setText(newstring);
                            }
                        });
                    } catch (org.json.JSONException e) {
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        sendfind.start();
    }
}


