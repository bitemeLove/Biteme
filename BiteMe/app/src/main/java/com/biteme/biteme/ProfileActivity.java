package com.biteme.biteme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ProfileActivity extends AppCompatActivity {
    String key = "0k8j7h6g5f4d3s2ak8j7h6g5f4d3s2a";
    //iv string must be max 16 char
    String iv = "0k8j7h6g5f4d3s2a";
    Socket socket;
    String intrest1;
    String intrest2;
    String intrest3;
    String friendsid;
    String yourid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_profile);
        TextView friendstext = (TextView) findViewById(R.id.Friendstxt);
        Intent startingintent = getIntent();
        friendsid = startingintent.getStringExtra("Friendsid");
        yourid = startingintent.getStringExtra("yourid");
        String friendsname = startingintent.getStringExtra("friendsname");


        friendstext.setText(friendsname);
        ProfilePictureView friendspic = (ProfilePictureView) findViewById(R.id.friendsprofilepic);
        friendspic.setProfileId(friendsid);

        ImageView removefriend = (ImageView) findViewById(R.id.lookingImg);
        removefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });

        final Thread send2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //connect naar de server
                    socket = new Socket(InetAddress.getByName("94.226.137.151"), 9000);
                    //zend de api call naar de server
                    Profile profile = Profile.getCurrentProfile();
//  Profile profile = null;
                    if (profile == null) {
                        String output = Crypto.encrypts("get" + friendsid, key, iv);
                        OutputStream out = socket.getOutputStream();
                        out.write(output.getBytes(), 0, output.length());
                    }

                    //krijg data terug van de server en slaag deze op in response
                    int bytesRead;
                    String response = "";
                    byte[] buffer = new byte[10000];
                    InputStream in = socket.getInputStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10000);
                    while ((bytesRead = in.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                        response += byteArrayOutputStream.toString("UTF-8");

                        break;
                    }
                    response = Crypto.decrypts(response, key, iv);
                    //zorg er voor dat elk json object word gesplits in elks een object
                    Log.v("get" , response);
                    String[] respondArray = response.split("\\}");

                    //proces de json data en add een element van deze data in de arrays
                    try {
                        for (String res : respondArray) {
                            JSONObject json = new JSONObject(res + "}");

                            intrest1 = (String) json.get("intrest1").toString();
                            intrest2 = (String) json.get("intrest2").toString();
                            intrest3 = (String) json.get("intrest3").toString();
                            final String gender = (String) json.get("gender");
                            final String age = (String) json.get("age").toString();
                            final String loving = (String) json.get("intrestin").toString();
                            final String lookingFor = gender+loving;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView looking = (ImageView) findViewById(R.id.lookingImg);
                                    switch(lookingFor){
                                        case "womanmale":
                                            looking.setImageResource(R.drawable.woman_man);
                                            break;
                                        case "womanwoman":
                                            looking.setImageResource(R.drawable.woman_woman);
                                            break;
                                        case "malemale":
                                            looking.setImageResource(R.drawable.man_man);
                                            break;
                                        case "malewoman":
                                            looking.setImageResource(R.drawable.man_woman);
                                            break;
                                    }

                                    TextView friendsintrest1 = (TextView) findViewById(R.id.friendsintrest1);
                                    TextView friendsintrest2 = (TextView) findViewById(R.id.friendsintrest2);
                                    TextView friendsintrest3 = (TextView) findViewById(R.id.friendsintrest3);
                                    TextView friendsgender = (TextView) findViewById(R.id.friendsgender);
                                    TextView friendsage = (TextView) findViewById(R.id.friendsage);


                                    friendsintrest1.setText("Intrested in: "+intrest1);
                                    friendsintrest2.setText("Intrested in: "+intrest2);
                                    friendsintrest3.setText("Intrested in: "+intrest3);
                                    friendsage.setText(" Age: "+age);
                                    friendsgender.setText("Gender: "+gender + "    " + loving);

                                }
                            });
                        }

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
        send2.start();
    }

    private void remove(){
        Thread send = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //connect naar de server
                    socket = new Socket(InetAddress.getByName("94.226.137.151"), 9000);
                    //zend de api call naar de server
                    String output = Crypto.encrypts(("friendrm"+yourid+","+friendsid), key, iv);
                    OutputStream out = socket.getOutputStream();
                    out.write(output.getBytes(), 0, output.length());
                    int bytesRead;

                    byte[] buffer = new byte[10000];
                    InputStream in = socket.getInputStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10000);
                    while ((bytesRead = in.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);

                        break;
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
        send.start();
    }

}
