package com.biteme.biteme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatPage extends AppCompatActivity {

    EditText editText;
    Button sendbutton;
    Socket socket;
    boolean onlyonce=false;
    boolean childadded=false;
    ArrayList friend = new ArrayList();
    ArrayList user = new ArrayList();
    //key string must be max 32 char
    String key = "0k8j7h6g5f4d3s2ak8j7h6g5f4d3s2a";
    //iv string must be max 16 char
    String iv = "0k8j7h6g5f4d3s2a";
    ArrayList<ChatModel> chatmsgsList = new ArrayList<ChatModel>();
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<ChatModel, ChatMessageViewHolder> mFirebaseAdapter1 = null;
    private FirebaseRecyclerAdapter<ChatModel, ChatMessageViewHolder> mFirebaseAdapter2 = null;
    String  friendtoken;
    Firebase firebase_chatnode = new Firebase("https://biteme-67467.firebaseio.com/Chats");
    Firebase ref_chatchildnode1 = null;
    Firebase ref_chatchildnode2 = null;
    String from_user, to_user, newmsg, LoggedInUser;
    String profilename;
    private LinearLayoutManager mLinearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        Firebase.setAndroidContext(this);



        editText = (EditText) findViewById(R.id.editText);

        sendbutton = (Button) findViewById(R.id.button);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final RecyclerView.LayoutManager layoutmgr = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutmgr);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Intent startingintent = getIntent();
        from_user = startingintent.getStringExtra("FROM_USER");
        to_user = startingintent.getStringExtra("TO_USER");
        profilename = startingintent.getStringExtra("profilename");
        LoggedInUser = startingintent.getStringExtra("LOG_IN_USER");

        setTitle("Private Chat");



        Log.v("NODE CREATED:", from_user + " " + to_user);


        ref_chatchildnode1 = firebase_chatnode.child(from_user + " " + to_user);

        ref_chatchildnode2 = firebase_chatnode.child(to_user + " " + from_user);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newmsg = editText.getText().toString().trim();
                if(!newmsg.isEmpty()) {
                    ChatModel m = new ChatModel();
                    m.setSender(LoggedInUser);
                    m.setMessage(newmsg);
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    ref_chatchildnode1.push().setValue(m);
                    ref_chatchildnode2.push().setValue(m);
                    editText.setText("");
                    Thread update = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                //connect naar de server
                                socket = new Socket(InetAddress.getByName("94.226.137.151"), 9000);
                                //zend de api call naar de server
                                String output = Crypto.encrypts(("noti{\"id\":\"" + friendtoken + "\",\"body\":\"" + newmsg + "\",\"sendername\":\"" + profilename + "\",\"senderid\":\"" + from_user + "\"}"), key, iv);

                                OutputStream out = socket.getOutputStream();
                                out.write(output.getBytes(), 0, output.length());

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
                    update.start();
                }
            }
        });
        ref_chatchildnode1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatModel chatmsg = dataSnapshot.getValue(ChatModel.class);
                chatmsgsList.add(chatmsg);
                childadded=true;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
        ref_chatchildnode2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatModel chatmsg = dataSnapshot.getValue(ChatModel.class);
                chatmsgsList.add(chatmsg);
                childadded=true;
                ;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }






    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseAdapter1 = new FirebaseRecyclerAdapter<ChatModel, ChatMessageViewHolder>(ChatModel.class,
                R.layout.textview,
                ChatMessageViewHolder.class,
                ref_chatchildnode1) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatModel m, int i) {

                    chatMessageViewHolder.msg.setText(m.getMessage());
                    chatMessageViewHolder.user_profile_pic_chat.setProfileId(m.getSender());

            }
        };
        mFirebaseAdapter2 = new FirebaseRecyclerAdapter<ChatModel, ChatMessageViewHolder>(ChatModel.class,
                R.layout.textview,
                ChatMessageViewHolder.class,
                ref_chatchildnode2) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatModel m, int i) {

                    chatMessageViewHolder.msg.setText(m.getMessage());
                    chatMessageViewHolder.user_profile_pic_chat.setProfileId(m.getSender());



                if (onlyonce==false||childadded==true) {
                    mRecyclerView.smoothScrollToPosition(mFirebaseAdapter2.getItemCount() - 1);
                    onlyonce=true;
                    childadded=false;

                }

            }

        };

        mRecyclerView.setAdapter(mFirebaseAdapter1);

        mRecyclerView.setAdapter(mFirebaseAdapter2);

        Firebase firebase_onlineusers = new Firebase("https://biteme-67467.firebaseio.com/Tokens/"+to_user+"/");
        firebase_onlineusers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                s = dataSnapshot.getValue(String.class);
                Log.v("VALUEid", s);
                friendtoken=s;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


}
