package com.biteme.biteme;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.LikeView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import bolts.AppLinks;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static List<Person> persons;
    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<String> datafriends = new ArrayList<String>();
    private ArrayList<String> datacard = new ArrayList<String>();
    String profileName;
    Socket socket;
    String usergender;
    String usernamefirebase;
    ArrayList dateids = new ArrayList();
    GameRequestDialog requestDialog;
    static String profile_id;
    private TextView gendertxt;
    Profile profile;
    private AdView mAdView;
    ArrayList friend = new ArrayList();
    ArrayList friendsreq = new ArrayList();
    ArrayList friends = new ArrayList();
    ArrayList friendsname = new ArrayList();
    ArrayList friendsnames = new ArrayList();
    ArrayList<Boolean> friendsonline = new ArrayList<Boolean>();
    boolean doubleBackToExitPressedOnce = false;
    ArrayList friendsnamelist = new ArrayList();
    SharedPreferences mPrefs;
    ArrayList user = new ArrayList();
    //key string must be max 32 char
    String key = "0k8j7h6g5f4d3s2ak8j7h6g5f4d3s2a";
    //iv string must be max 16 char
    String iv = "0k8j7h6g5f4d3s2a";
    //"94.226.137.151"
    String ip = "94.226.137.151";

    ArrayList friendsnamecard = new ArrayList();
    ArrayList cardlocation = new ArrayList();
    ArrayList friendsidcard = new ArrayList();
    ArrayList carddatetime = new ArrayList();

    ArrayList cardsnamelist = new ArrayList();
    ArrayList cardsidlist = new ArrayList();
    ArrayList cardslocationlist = new ArrayList();
    ArrayList cardsplacelist = new ArrayList();

    MyListadapterfriends friendsAdapter;
    MyListadapterfriendsreq findAdapter;

    private RecyclerView rv;


    ArrayList<String> usersList = new ArrayList<String>();
    private static RecyclerView.Adapter adaptercard;
    private RecyclerView.LayoutManager layoutManagercard;
    private static RecyclerView recyclerViewcard;

    static View.OnClickListener myOnClickListenercard;
    private static ArrayList<Integer> removedItemscard;
    private TextView mTextDetails;
    private TextView useragetxt;
    private LoginButton loginButton = null;
    private ProfilePictureView profilePicture = null;
    private String userAge;
    private Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private CallbackManager mCallbackManager;
    Firebase firebase_regusers = null;
    ListView listView;
    ArrayAdapter<String> mAdapter = null;
    ProfilePictureView profilepichome = null;
    String gender;

    //interested in view components
    EditText intres1;
    EditText intres2;
    EditText intres3;

    String LoggedInUser;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            profilePicture = (ProfilePictureView) findViewById(R.id.user_profile_pic);

            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {

                profilePicture.setDrawingCacheEnabled(true);
                profilePicture.setProfileId(profile.getId());
                firebase_regusers.setAndroidContext(getApplicationContext());
                ProfilePictureView profilepichome = (ProfilePictureView) findViewById(R.id.profilepichome);
                profilepichome.setDrawingCacheEnabled((true));
                profilepichome.setProfileId(profile.getId());

                firebase_regusers = new Firebase("https://biteme-67467.firebaseio.com/Registered_Users/");
                firebase_regusers.push().setValue(profile_id);
                Firebase firebase_onlineusers = new Firebase("https://biteme-67467.firebaseio.com/Online_Users/");
                firebase_onlineusers.setAndroidContext(getApplicationContext());
                firebase_onlineusers.push().setValue(profile_id);
                mTextDetails = (TextView) findViewById(R.id.text_details);

                Firebase firebasefriendtoken = new Firebase("https://biteme-67467.firebaseio.com/Tokens/" + profile.getId() + "/");
                firebasefriendtoken.removeValue();
                firebasefriendtoken.push().setValue(FirebaseInstanceId.getInstance().getToken());

                mTextDetails.setText("welcome" + profile.getId());
                Toast.makeText(getApplicationContext(), "Succesfully logged in! Welcome to BiteMe!!",
                        Toast.LENGTH_SHORT).show();
                profile_id = profile.getId();
                profileName = profile.getName();
                //zet de online satus naar online
                generateListContent();
                setOnline(true);
                generateListContentfriendsreq();
                generatedates();
                send();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                // Application code
                                try {
                                    gender = object.getString("gender");
                                    gendertxt = (TextView) findViewById(R.id.mygender);
                                    gendertxt.setText("Gender: " + gender);
                                    usergender = gender;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    useragetxt = (TextView) findViewById(R.id.userage);
                                    useragetxt.setText("Birthdate: " + birthday);
                                    userAge = birthday;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                if (profile != null) {
                    loginButton.clearPermissions();
                    loginButton.setPublishPermissions(Arrays.asList("publish_actions"));
                }
                loginButton.setVisibility(View.GONE);
            }
        }


        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "In order to use BiteMe you have to give the app access to your facebook. Please try again",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getApplicationContext(), "An error occured , please check your internet connection or try again later! sorry for the inconvenience",
                    Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        super.onCreate(savedInstanceState);
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activityurl", "App Link Target URL: " + targetUrl.toString());
        }
        final String PREFS_NAME = "MyPrefsFile";
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    MainActivity.this);
// Setting Dialog Title
            alertDialog2.setTitle("First time");

// Setting Dialog Message
            alertDialog2.setMessage("BiteMe is being worked on , meaning it is unstable , do u wanna continue?");

// Setting Positive "Yes" Btn
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                            startActivity(intent);

                        }
                    });

// Setting Negative "NO" Btn
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            System.exit(1);
                        }
                    });

// Showing Alert Dialog
            alertDialog2.show();
        }

        setContentView(R.layout.activity_main);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network

        } else {
            AlertDialog.Builder alertDialoga = new AlertDialog.Builder(
                    MainActivity.this);
// Setting Dialog Title

// Setting Dialog Title
            alertDialoga.setTitle("First time");

// Setting Dialog Message
            alertDialoga.setMessage("BiteMe needs an active internet connection , do u want to put on wifi? After putting it on you need to manually restart the app.");

// Setting Positive "Yes" Btn
            alertDialoga.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            wifiManager.setWifiEnabled(true);
                            ;
                        }
                    });

// Setting Negative "NO" Btn
            alertDialoga.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            System.exit(1);
                        }
                    });

// Showing Alert Dialog
            alertDialoga.show();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Profile profile = Profile.getCurrentProfile();

        mTextDetails = (TextView) findViewById(R.id.text_details);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ret = sharedPreferences.getString("intrest1", "");
        String ret2 = sharedPreferences.getString("intrest2", "");
        String ret3 = sharedPreferences.getString("intrest3", "");
        final String ret4 = sharedPreferences.getString("inerestin", "");

        ListView lv = (ListView) findViewById(R.id.listview);
        ListView lvfriends = (ListView) findViewById(R.id.listviewfriends);
        ListView lvfriendsreq = (ListView) findViewById(R.id.listview_req);

        //set a friendsadapter on the friends listview
        friendsAdapter = new MyListadapterfriends(this, R.layout.list_itemfriends, friends);
        lvfriends.setAdapter(friendsAdapter);

        //set a findAdapter onthe find listview
        findAdapter = new MyListadapterfriendsreq(this, R.layout.list_itemfriendsreq, friendsreq);
        lvfriendsreq.setAdapter(findAdapter);

        lv.setAdapter(new MyListadapter(this, R.layout.list_item, data));

        ProfilePictureView profilepict = (ProfilePictureView) findViewById(R.id.user_profile_pic);
        profilepict.setVisibility(View.GONE);
        ProfilePictureView profilepichome = (ProfilePictureView) findViewById(R.id.profilepichome);

        generatedates();
        TextView usergender = (TextView) findViewById(R.id.mygender);
        usergender.setVisibility(View.GONE);


        lv.setVisibility(View.VISIBLE);
        RatingBar rt = (RatingBar) findViewById(R.id.ratingBar);
        rt.setVisibility(View.GONE);


        TextView intrests = (TextView) findViewById(R.id.intrests);
        intrests.setVisibility(View.GONE);
        TextView intrested_in = (TextView) findViewById(R.id.txt_intrests);
        intrested_in.setVisibility(View.GONE);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        TextView popular = (TextView) findViewById(R.id.txt_popularity);
        popular.setVisibility(View.GONE);
        TextView userage = (TextView) findViewById(R.id.userage);
        userage.setVisibility(View.GONE);

        lvfriends.setVisibility(View.GONE);
        TextView friendstxt = (TextView) findViewById(R.id.friendstxt);
        friendstxt.setVisibility(View.VISIBLE);
        TextView requesttxt = (TextView) findViewById(R.id.frienrequesttxt);
        requesttxt.setVisibility(View.VISIBLE);
        CircularProgressButton updatebtn = (CircularProgressButton) findViewById(R.id.update);
        updatebtn.setVisibility(View.GONE);
        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);






        initializeData();
        setSupportActionBar(toolbar);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{user-id}/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).executeAsync();

        LikeView likeView = (LikeView) findViewById(R.id.likeView);
        likeView.setObjectIdAndType(
                "https://www.facebook.com/pg/BiteMe-Community-824570954349973",
                LikeView.ObjectType.PAGE);

        listView = (ListView) findViewById(R.id.listviewchat);
        Firebase.setAndroidContext(getApplicationContext());


        listView = (ListView) findViewById(R.id.listviewchat);
        mAdapter = new ArrayAdapter<String>(this, R.layout.chatonlinelist, usersList);
        listView.setAdapter(mAdapter);
        listView.setVisibility(View.GONE);
        Firebase.setAndroidContext(getApplicationContext());

        Crypto.DEBUG_LOG_ENABLED = false;
        Button invitebutton = (Button) findViewById(R.id.invitebtn);
        invitebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitefriendsfb();
            }
        });

        //set the spinners current selected item to the last one set
        spinner.post(new Runnable() {
            @Override
            public void run() {
                switch (ret4) {
                    case "Male":
                        spinner.setSelection(0);
                        break;

                    case "Female":
                        spinner.setSelection(1);
                        break;

                    case "Suprise me!": //het is surprise me
                        spinner.setSelection(2);
                        break;

                    default:
                        spinner.setSelection(0);
                        break;
                }
            }
        });


        intres1 = (EditText) findViewById(R.id.intrest1);
        intres1.setText(ret);
        intres2 = (EditText) findViewById(R.id.interest2);
        intres2.setText(ret2);
        intres3 = (EditText) findViewById(R.id.interest3);
        intres3.setText(ret3);

        intres1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intres1.setText("");
            }
        });

        intres2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intres2.setText("");
            }
        });

        intres3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intres3.setText("");
            }
        });

        intres1.setVisibility(View.GONE);
        intres3.setVisibility(View.GONE);
        intres2.setVisibility(View.GONE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        ProfilePictureView navbarpic = (ProfilePictureView) hView.findViewById(R.id.profilepicnavbar);
        navbarpic.setProfileId(profile_id);
        TextView navbartext = (TextView) hView.findViewById(R.id.navbartext);
        navbartext.setText("Welcome " + profileName);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.VISIBLE);
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.intrests, R.layout.chatonlinelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position) + " partners selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RVAdapter adaptercard = new RVAdapter(persons);
        rv.setAdapter(adaptercard);
        persons = new ArrayList<>();
        for (int i=0 ;i< cardsplacelist.size();i++) {
            persons.add(new Person(cardsnamelist.get(i).toString(), cardslocationlist.get(i).toString(), cardsidlist.get(i).toString() ,carddatetime.get(i).toString(),cardsplacelist.get(i).toString()));

        }
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        if (profile != null) {
            loginButton.clearPermissions();
            loginButton.setPublishPermissions(Arrays.asList("publish_actions"));
        }
        if (profile != null) {
            loginButton.setVisibility(View.GONE);
        } else {
            loginButton.setVisibility(View.VISIBLE);
        }

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, mCallback);
        AccessTokenTracker tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        ProfileTracker profileTracker = new ProfileTracker() {


            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (newProfile == null) {

                    profilePicture = (ProfilePictureView) findViewById(R.id.user_profile_pic);
                    profilePicture.setProfileId(null);
                    ProfilePictureView profilepichome = (ProfilePictureView) findViewById(R.id.profilepichome);
                    profilepichome.setProfileId(null);
                    Toast.makeText(getApplicationContext(), "Logged out from Facebook! We hope to see you back soon!", Toast.LENGTH_SHORT).show();

                    mTextDetails = (TextView) findViewById(R.id.text_details);
                    mTextDetails.setText("Hello! Please login using Facebook.");
                    useragetxt = (TextView) findViewById(R.id.userage);
                    useragetxt.setText("Birthdate: ");
                    gendertxt = (TextView) findViewById(R.id.mygender);
                    gendertxt.setText("Gender: ");
                }
            }
        };


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.map_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Going to the BiteMe Map!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
        navigationView2.setNavigationItemSelectedListener(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        final CircularProgressButton circularButton1 = (CircularProgressButton) findViewById(R.id.update);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {

                    circularButton1.setProgress(50);
                    Thread update = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String intr1 = (String) intres1.getText().toString();
                                String intr2 = (String) intres2.getText().toString();
                                String intr3 = (String) intres3.getText().toString();
                                String interstin = spinner.getSelectedItem().toString().toLowerCase();

                                //connect naar de server
                                socket = new Socket(InetAddress.getByName(ip), 9000);
                                //zend de api call naar de server

                                String output = Crypto.encrypts(("update{\"id\":\"" + profile_id + "\",\"age\":\"" + userAge + "\",\"name\":\"" + profileName + "\",\"address\":\"to be continued\",\"intrest1\":\"" + intr1 + "\",\"intrest2\":\"" + intr2 + "\",\"intrest3\":\"" + intr3 + "\",\"gender\":\"" + gender.toString() + "\" ,\"intrestin\":\"" + interstin + "\"}"), key, iv);
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

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            circularButton1.setProgress(100);
                        }
                    }, 1000);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("intrest1", intres1.getText().toString());
                    editor.putString("intrest2", intres2.getText().toString());
                    editor.putString("intrest3", intres3.getText().toString());
                    editor.putString("inerestin", spinner.getSelectedItem().toString());
                    editor.commit();

                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                }
            }
        });
        generatedates();
    }

    private void setOnline(final boolean online) {
        Thread onlineThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(InetAddress.getByName(ip), 9000);
                    //zend de api call naar de server
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        String output = Crypto.encrypts(("on" + profile.getId() + "," + online), key, iv);
                        OutputStream out = socket.getOutputStream();
                        out.write(output.getBytes(), 0, output.length());
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        onlineThread.start();
    }

    //BUG: by het terug hallen van de data van de server krijgt de client soms de data niet maar de server heeft deze wel verstuurd
    private void generateListContentfriendsreq() {

        Thread send2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    friendsreq.clear();
                    friendsname.clear();
                    //connect naar de server
                    socket = new Socket(InetAddress.getByName(ip), 9000);
                    //zend de api call naar de server
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        String output = Crypto.encrypts("find" + profile.getId(), key, iv);
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
                    if (response.endsWith("£")) {

                        response = response.substring(0, response.length() - 1);
                        //zorg er voor dat elk json object word gesplits in elks een object
                        Log.v("kobe", response);
                        String[] respondArray = response.split("\\}");
                        //proces de json data en add een element van deze data in de arrays
                        if (response!=null|| response!="")
                        {
                        try {
                            for (String res : respondArray) {
                                if (res.charAt(0) == ',') {
                                    res = res.substring(1);
                                }
                                JSONObject json = new JSONObject(res + "}");

                                friendsreq.add(json.get("friendid"));
                                friendsname.add(json.get("friendname"));
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();

                        } catch ( Exception e){}
                    }}
                } catch (UnknownHostException e) {
                    e.printStackTrace();

                } catch (IOException e) {

                    generateListContentfriendsreq();
                    e.printStackTrace();
                }

                finally {
                    if (socket != null) {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findAdapter.notifyDataSetChanged();
                                }
                            });
                            Thread.sleep(300);
                            socket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        send2.start();
    }

    private void generateListContentfriends() {

        Thread send7 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    friends.clear();
                    friendsnames.clear();
                    friendsonline.clear();
                    //connect naar de server
                    socket = new Socket(InetAddress.getByName(ip), 9000);
                    //zend de api call naar de server

                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        String output = Crypto.encrypts("friends" + profile.getId(), key, iv);
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

                    if (response.endsWith("$")) {
                        //zorg er voor dat elk json object word gesplits in elks een object
                        response = response.substring(0, response.length() - 1);
Log.v("friendresponse ", response);
                        String[] respondArray = response.split("\\}");
                        //proces de json data en add een element van deze data in de arrays
                        try {
                            for (String res : respondArray) {
                                JSONObject json = new JSONObject(res + "}");

                                friendsonline.add(Boolean.parseBoolean(json.get("online").toString()));
                                friends.add(json.get("friendid"));
                                friendsnames.add(json.get("friendname"));
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    generateListContentfriends();
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    friendsAdapter.notifyDataSetChanged();
                                    if (friends.size() <= 0) {
                                        TextView nofriendsreq = (TextView) findViewById(R.id.frienrequesttxt);
                                        nofriendsreq.setText("Friend Requests: No new friend requests :(");
                                    }
                                }
                            });
                            Thread.sleep(300);
                            socket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        send7.start();
    }

    private void generateListContent() {

        Thread send = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //connect naar de server
                    socket = new Socket(InetAddress.getByName(ip), 9000);
                    //zend de api call naar de server
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        String output = Crypto.encrypts("reqget" + profile.getId(), key, iv);
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

                            friend.add(json.get("friendid"));
                        }
                        for (int i = 0; i < friend.size(); i++) {
                            if (!data.contains(String.valueOf(friend.get(i)))) {
                                data.add(String.valueOf(friend.get(i)));
                            }
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    friendsAdapter.notifyDataSetChanged();
                                    if (friend.size() <= 0) {
                                        TextView nofriendsreq = (TextView) findViewById(R.id.friendstxt);
                                        nofriendsreq.setText("Recommended friends: No recommended friends :(");
                                    }
                                }
                            });
                            Thread.sleep(300);
                            socket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        send.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.user_profile_pic);
                profilePicture.setDrawingCacheEnabled(true);
                profilePicture.setProfileId(profile.getId());
                ProfilePictureView profilepichome = (ProfilePictureView) findViewById(R.id.profilepichome);
                profilepichome.setProfileId(profile.getId());
                mTextDetails = (TextView) findViewById(R.id.text_details);
                mTextDetails.setText("welcome " + profile.getName());
                profile_id = profile.getId();
                profileName = profile.getName();
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    gender = object.getString("gender");
                                    gendertxt = (TextView) findViewById(R.id.mygender);
                                    gendertxt.setText("Gender: " + gender);
                                    usergender = gender;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    useragetxt = (TextView) findViewById(R.id.userage);
                                    useragetxt.setText("Birthdate: " + birthday);
                                    userAge = birthday;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                setOnline(true);
                generateListContentfriendsreq();
                generateListContentfriends();
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView = navigationView.getHeaderView(0);
            ProfilePictureView navbarpic = (ProfilePictureView) hView.findViewById(R.id.profilepicnavbar);
            navbarpic.setProfileId(profile_id);
            TextView navbartext = (TextView) hView.findViewById(R.id.navbartext);
            navbartext.setText("Welcome " + profileName);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        setOnline(false);

        Toast.makeText(getApplicationContext(), "We hope to see you back soon!", Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        setOnline(false);
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.app.FragmentManager fragmentManager = getFragmentManager();

//profile
        if (id == R.id.nav_first_layout) {
            Button invitebutton = (Button) findViewById(R.id.invitebtn);
            invitebutton.setVisibility(View.GONE);
            TextView invitetxt = (TextView) findViewById(R.id.invitetxt);
            invitetxt.setVisibility(View.GONE);
            TextView usergender = (TextView) findViewById(R.id.mygender);
            usergender.setVisibility(View.VISIBLE);
            mTextDetails = (TextView) findViewById(R.id.text_details);
            mTextDetails.setVisibility(View.GONE);
            ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.user_profile_pic);
            ProfilePictureView profilepichome = (ProfilePictureView) findViewById(R.id.profilepichome);
            profilepichome.setVisibility(View.GONE);
            profilePicture.setVisibility((View.VISIBLE));
            loginButton.setVisibility(View.VISIBLE);
            ListView lv = (ListView) findViewById(R.id.listview);
            lv.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            RatingBar rt = (RatingBar) findViewById(R.id.ratingBar);
            rt.setVisibility(View.VISIBLE);
            intres1.setVisibility(View.VISIBLE);
            intres3.setVisibility(View.VISIBLE);
            intres2.setVisibility(View.VISIBLE);
            TextView intrests = (TextView) findViewById(R.id.intrests);
            intrests.setVisibility(View.VISIBLE);
            TextView intrested_in = (TextView) findViewById(R.id.txt_intrests);
            intrested_in.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            TextView popular = (TextView) findViewById(R.id.txt_popularity);
            popular.setVisibility(View.VISIBLE);
            TextView userage = (TextView) findViewById(R.id.userage);
            userage.setVisibility(View.VISIBLE);
            ListView lvfriends = (ListView) findViewById(R.id.listviewfriends);
            lvfriends.setVisibility(View.GONE);
            TextView friendstxt = (TextView) findViewById(R.id.friendstxt);
            friendstxt.setVisibility(View.GONE);
            TextView requesttxt = (TextView) findViewById(R.id.frienrequesttxt);
            requesttxt.setVisibility(View.GONE);
            CircularProgressButton updatebtn = (CircularProgressButton) findViewById(R.id.update);
            updatebtn.setVisibility(View.VISIBLE);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylcer);
            recyclerView.setVisibility(View.GONE);
            ListView lvfriendsreq = (ListView) findViewById(R.id.listview_req);
            lvfriendsreq.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            LikeView likeView = (LikeView) findViewById(R.id.likeView);
            likeView.setVisibility(View.GONE);
            TextView likeu = (TextView) findViewById(R.id.likeus);
            likeu.setVisibility(View.GONE);
            RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
            rv.setVisibility(View.GONE);

//home
        } else if (id == R.id.home) {
            Log.d("kobehome", "home");
            setOnline(true);
            generateListContentfriendsreq();
            generateListContent();
            ImageView overlay = (ImageView) findViewById(R.id.overlaypic);
            overlay.setVisibility(View.VISIBLE);
            ListView lvfriendsreq = (ListView) findViewById(R.id.listview_req);
            lvfriendsreq.setVisibility(View.VISIBLE);
            Button invitebutton = (Button) findViewById(R.id.invitebtn);
            invitebutton.setVisibility(View.VISIBLE);
            TextView invitetxt = (TextView) findViewById(R.id.invitetxt);
            invitetxt.setVisibility(View.VISIBLE);
            ProfilePictureView profilepichome = (ProfilePictureView) findViewById(R.id.profilepichome);
            profilepichome.setVisibility(View.VISIBLE);
            TextView likeu = (TextView) findViewById(R.id.likeus);
            likeu.setVisibility(View.VISIBLE);
            LikeView likeView = (LikeView) findViewById(R.id.likeView);
            likeView.setVisibility(View.VISIBLE);
            TextView usergender = (TextView) findViewById(R.id.mygender);
            usergender.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            mTextDetails = (TextView) findViewById(R.id.text_details);
            mTextDetails.setVisibility(View.VISIBLE);
            ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.user_profile_pic);
            profilePicture.setVisibility((View.GONE));
            loginButton.setVisibility(View.GONE);
            ListView lv = (ListView) findViewById(R.id.listview);
            lv.setVisibility(View.VISIBLE);
            RatingBar rt = (RatingBar) findViewById(R.id.ratingBar);
            rt.setVisibility(View.GONE);
            intres1.setVisibility(View.GONE);
            intres3.setVisibility(View.GONE);
            intres2.setVisibility(View.GONE);
            TextView intrests = (TextView) findViewById(R.id.intrests);
            intrests.setVisibility(View.GONE);
            TextView intrested_in = (TextView) findViewById(R.id.txt_intrests);
            intrested_in.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            TextView popular = (TextView) findViewById(R.id.txt_popularity);
            popular.setVisibility(View.GONE);
            TextView userage = (TextView) findViewById(R.id.userage);
            userage.setVisibility(View.GONE);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylcer);
            recyclerView.setVisibility(View.GONE);
            ListView lvfriends = (ListView) findViewById(R.id.listviewfriends);
            lvfriends.setVisibility(View.GONE);
            TextView friendstxt = (TextView) findViewById(R.id.friendstxt);
            friendstxt.setVisibility(View.VISIBLE);
            TextView requesttxt = (TextView) findViewById(R.id.frienrequesttxt);
            requesttxt.setVisibility(View.VISIBLE);
            CircularProgressButton updatebtn = (CircularProgressButton) findViewById(R.id.update);
            updatebtn.setVisibility(View.GONE);
            RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
            rv.setVisibility(View.GONE);

//chat
        } else if (id == R.id.nav_third_layout) {
            ImageView overlay = (ImageView) findViewById(R.id.overlaypic);
            overlay.setVisibility(View.GONE);
            ListView lvfriendsreq = (ListView) findViewById(R.id.listview_req);
            lvfriendsreq.setVisibility(View.GONE);
            Button invitebutton = (Button) findViewById(R.id.invitebtn);
            invitebutton.setVisibility(View.GONE);
            TextView invitetxt = (TextView) findViewById(R.id.invitetxt);
            invitetxt.setVisibility(View.GONE);
            ProfilePictureView profilepichome = (ProfilePictureView) findViewById(R.id.profilepichome);
            profilepichome.setVisibility(View.GONE);
            TextView likeu = (TextView) findViewById(R.id.likeus);
            likeu.setVisibility(View.GONE);
            LikeView likeView = (LikeView) findViewById(R.id.likeView);
            likeView.setVisibility(View.GONE);
            TextView usergender = (TextView) findViewById(R.id.mygender);
            usergender.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
            rv.setVisibility(View.VISIBLE);

            mTextDetails = (TextView) findViewById(R.id.text_details);
            mTextDetails.setVisibility(View.GONE);
            ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.user_profile_pic);
            profilePicture.setVisibility((View.GONE));
            loginButton.setVisibility(View.GONE);
            ListView lv = (ListView) findViewById(R.id.listview);
            lv.setVisibility(View.GONE);
            RatingBar rt = (RatingBar) findViewById(R.id.ratingBar);
            rt.setVisibility(View.GONE);
            intres1.setVisibility(View.GONE);
            intres3.setVisibility(View.GONE);
            intres2.setVisibility(View.GONE);
            TextView intrests = (TextView) findViewById(R.id.intrests);
            intrests.setVisibility(View.GONE);
            TextView intrested_in = (TextView) findViewById(R.id.txt_intrests);
            intrested_in.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            TextView popular = (TextView) findViewById(R.id.txt_popularity);
            popular.setVisibility(View.GONE);
            TextView userage = (TextView) findViewById(R.id.userage);
            userage.setVisibility(View.GONE);
            ListView lvfriends = (ListView) findViewById(R.id.listviewfriends);
            lvfriends.setVisibility(View.GONE);
            TextView friendstxt = (TextView) findViewById(R.id.friendstxt);
            friendstxt.setVisibility(View.GONE);
            TextView requesttxt = (TextView) findViewById(R.id.frienrequesttxt);
            requesttxt.setVisibility(View.GONE);
            CircularProgressButton updatebtn = (CircularProgressButton) findViewById(R.id.update);
            updatebtn.setVisibility(View.GONE);

            Firebase.setAndroidContext(this);
            firebase_regusers = new Firebase("https://biteme-67467.firebaseio.com/Registered_Users/");
            initializeData();

//friends
        } else if (id == R.id.nav_fourht_layout) {
            generateListContentfriends();

            ImageView overlay = (ImageView) findViewById(R.id.overlaypic);
            overlay.setVisibility(View.GONE);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylcer);
            recyclerView.setVisibility(View.GONE);
            ListView lvfriendsreq = (ListView) findViewById(R.id.listview_req);
            lvfriendsreq.setVisibility(View.GONE);
            Button invitebutton = (Button) findViewById(R.id.invitebtn);
            invitebutton.setVisibility(View.GONE);
            TextView invitetxt = (TextView) findViewById(R.id.invitetxt);
            invitetxt.setVisibility(View.GONE);
            ProfilePictureView profilepichome = (ProfilePictureView) findViewById(R.id.profilepichome);
            profilepichome.setVisibility(View.GONE);
            TextView likeu = (TextView) findViewById(R.id.likeus);
            likeu.setVisibility(View.GONE);
            LikeView likeView = (LikeView) findViewById(R.id.likeView);
            likeView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            mTextDetails = (TextView) findViewById(R.id.text_details);
            mTextDetails.setVisibility(View.GONE);
            ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.user_profile_pic);
            profilePicture.setVisibility((View.GONE));
            loginButton.setVisibility(View.GONE);
            ListView lv = (ListView) findViewById(R.id.listview);
            lv.setVisibility(View.GONE);
            RatingBar rt = (RatingBar) findViewById(R.id.ratingBar);
            rt.setVisibility(View.GONE);
            intres1.setVisibility(View.GONE);
            intres3.setVisibility(View.GONE);
            intres2.setVisibility(View.GONE);
            TextView intrests = (TextView) findViewById(R.id.intrests);
            intrests.setVisibility(View.GONE);
            TextView intrested_in = (TextView) findViewById(R.id.txt_intrests);
            intrested_in.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            TextView popular = (TextView) findViewById(R.id.txt_popularity);
            popular.setVisibility(View.GONE);
            TextView userage = (TextView) findViewById(R.id.userage);
            userage.setVisibility(View.GONE);
            TextView usergender = (TextView) findViewById(R.id.mygender);
            usergender.setVisibility(View.GONE);
            ListView lvfriends = (ListView) findViewById(R.id.listviewfriends);
            lvfriends.setVisibility(View.VISIBLE);
            TextView friendstxt = (TextView) findViewById(R.id.friendstxt);
            friendstxt.setVisibility(View.GONE);
            TextView requesttxt = (TextView) findViewById(R.id.frienrequesttxt);
            requesttxt.setVisibility(View.GONE);
            CircularProgressButton updatebtn = (CircularProgressButton) findViewById(R.id.update);
            updatebtn.setVisibility(View.GONE);
            RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
            rv.setVisibility(View.GONE);


        } else if (id == R.id.nav_share) {
            invitefriendsfb();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void send() {
        Thread send = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String intr1 = (String) intres1.getText().toString();
                    String intr2 = (String) intres2.getText().toString();
                    String intr3 = (String) intres3.getText().toString();

                    //connect naar de server
                    socket = new Socket(InetAddress.getByName(ip), 9000);
                    //zend de api call naar de server
                    String output = Crypto.encrypts(("set{\"id\":\"" + profile_id + "\",\"age\":\"" + userAge + "\",\"name\":\"" + profileName + "\",\"address\":\"to be continued\"}"), key, iv);
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
        send.start();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onStart() {
        super.onStart();
        Firebase firebase_onlineusers = new Firebase("https://biteme-67467.firebaseio.com/Online_Users/");
        firebase_onlineusers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                s = dataSnapshot.getValue(String.class);

                usersList.add(s);
                mAdapter.notifyDataSetChanged();
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
        onListItemClick();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());

    }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    private class MyListadapter extends ArrayAdapter<String> {
        private int layout;

        public MyListadapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            final Profile profile = Profile.getCurrentProfile();

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ProfilePictureView) convertView.findViewById(R.id.list_item_friendschat);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                viewHolder.button2 = (Button) convertView.findViewById(R.id.list_item_decline);
                convertView.setTag(viewHolder);
            }

            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "added friend  " + profile.getName(), Toast.LENGTH_SHORT).show();

                            Thread send = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        socket = new Socket(InetAddress.getByName("94.226.137.151"), 9000);
                                        //zend de api call naar de server
                                        String output = Crypto.encrypts(("accept" + profile_id + "," + data.get(position)), key, iv);
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

                                        //zorg er voor dat elk json object word gesplits in elks een object

                                        //proces de json data en add een element van deze data in de arrays


                                        //krijg data terug van de server en slaag deze op in response


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
                            data.remove(data.get(position));
                        }
                    });


                    mainViewholder.button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View m) {
                            Toast.makeText(getContext(), "declined friend  " + profile.getName(), Toast.LENGTH_SHORT).show();
                            Thread send = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        socket = new Socket(InetAddress.getByName("94.226.137.151"), 9000);
                                        //zend de api call naar de server
                                        String output = Crypto.encrypts(("decline" + profile_id + "," + data.get(position)), key, iv);
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

                                        //zorg er voor dat elk json object word gesplits in elks een object
                                        //zorg er voor dat elk json object word gesplits in elks een object

                                        //proces de json data en add een element van deze data in de arrays


                                        //krijg data terug van de server en slaag deze op in response


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
                            data.remove(data.get(position));
                        }
                    });


            if (profile != null) {
                mainViewholder.title.setText(data.get(position));
                mainViewholder.thumbnail.setProfileId(data.get(position));
            }

            return convertView;
        }
    }


    //bug gefix nu zal de friends list juist worden getoond
    private class MyListadapterfriends extends ArrayAdapter<String> {
        private int layout;

        public MyListadapterfriends(Context context2, int resource2, List<String> objects2) {
            super(context2, resource2, objects2);
            layout = resource2;
        }

        @NonNull
        @Override
        public View getView(final int position2, View convertView2, final ViewGroup parent2) {
            ViewHolder mainViewholder2 = null;
            final Profile profile = Profile.getCurrentProfile();
            if (convertView2 == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView2 = inflater.inflate(layout, parent2, false);
                ViewHolder viewHolder2 = new ViewHolder();
                viewHolder2.thumbnail2 = (ProfilePictureView) convertView2.findViewById(R.id.list_item_friends);
                viewHolder2.friendsnametext = (TextView) convertView2.findViewById(R.id.list_item_textfriends);
                viewHolder2.onlineChecker = (RadioButton) convertView2.findViewById(R.id.onlineCheck);

                viewHolder2.circleBoomMenuButtonfriends = (BoomMenuButton) convertView2.findViewById(R.id.boom_circlefriends);
                convertView2.setTag(viewHolder2);
            }
            mainViewholder2 = (ViewHolder) convertView2.getTag();

            final Drawable[] circleSubButtonDrawablesfriends = new Drawable[3];
            int[] drawablesResource = new int[]{
                    R.mipmap.ic_launcher,
                    R.drawable.profile_buttons,
                    R.drawable.invitedate
            };
            for (int i = 0; i < 3; i++)
                circleSubButtonDrawablesfriends[i] = ContextCompat.getDrawable(parent2.getContext(), drawablesResource[i]);

            final String[] circleSubButtonTextsfriends = new String[]{
                    "Chat",
                    "Profile",
                    "Invite"
            };


            final int[][] subButtonColorsfriends = new int[3][2];
            for (int i = 0; i < 3; i++) {
                subButtonColorsfriends[i][1] = GetRandomColor();
                subButtonColorsfriends[i][0] = Util.getInstance().getPressedColor(subButtonColorsfriends[i][1]);
            }

            // init the BMB with delay

            final ViewHolder finalMainViewholder = mainViewholder2;
            mainViewholder2.circleBoomMenuButtonfriends.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Now with Builder, you can init BMB more convenient
                    new BoomMenuButton.Builder()
                            .subButtons(circleSubButtonDrawablesfriends, subButtonColorsfriends, circleSubButtonTextsfriends)
                            .button(ButtonType.CIRCLE)
                            .boom(BoomType.PARABOLA)
                            .place(PlaceType.CIRCLE_3_1)
                            .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                            .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                                @Override
                                public void onClick(int buttonIndexfriends) {
                                    if (buttonIndexfriends == 0) {
                                        Intent intent = new Intent(MainActivity.this, ChatPage.class);
                                        intent.putExtra("FROM_USER", profile_id);
                                        intent.putExtra("TO_USER", String.valueOf(friends.get(position2)));
                                        intent.putExtra("LOG_IN_USER", profile_id);
                                        intent.putExtra("profilename", profileName);
                                        startActivity(intent);

                                    } else if (buttonIndexfriends == 1) {
                                        if (position2 < friends.size()) {
                                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                            intent.putExtra("Friendsid", String.valueOf(friends.get(position2)));
                                            intent.putExtra("friendsname", String.valueOf(friendsnames.get(position2)));
                                            intent.putExtra("yourid", profile_id);

                                            startActivity(intent);
                                        }
                                    } else if (buttonIndexfriends == 2) {
                                        Toast.makeText(getContext(), "3rd" + String.valueOf(friendsnames.get(position2)), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .init(finalMainViewholder.circleBoomMenuButtonfriends);
                }
            }, 1);

            if (position2 < friends.size()) {
                if (Boolean.valueOf(friendsonline.get(position2)) == true) {
                    mainViewholder2.onlineChecker.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
                    // mainViewholder2.onlineChecker.setBackgroundColor(Color.GREEN);
                    mainViewholder2.onlineChecker.toggle();
                } else {
                    mainViewholder2.onlineChecker.setButtonTintList(ColorStateList.valueOf(Color.RED));
                    //mainViewholder2.onlineChecker.setBackgroundColor(Color.RED);
                    mainViewholder2.onlineChecker.toggle();
                }
                mainViewholder2.thumbnail2.setProfileId(String.valueOf(friends.get(position2)));
                mainViewholder2.friendsnametext.setText(String.valueOf(friendsnames.get(position2)));
            }
            if (mainViewholder2.thumbnail2 == null) {
                mainViewholder2.thumbnail2.setVisibility(View.GONE);
                mainViewholder2.friendsnametext.setVisibility(View.GONE);
                mainViewholder2.circleBoomMenuButtonfriends.setVisibility(View.GONE);
            }
            return convertView2;
        }
    }

    //recomended friends adapter
    private class MyListadapterfriendsreq extends ArrayAdapter<String> {
        private int layout;

        public MyListadapterfriendsreq(Context context3, int resource3, List<String> objects3) {
            super(context3, resource3, objects3);
            layout = resource3;
        }

        @NonNull
        @Override
        public View getView(final int position3, View convertView3, final ViewGroup parent3) {
            ViewHolder mainViewholder3 = null;
            final Profile profile = Profile.getCurrentProfile();


            if (convertView3 == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView3 = inflater.inflate(layout, parent3, false);
                ViewHolder viewHolder3 = new ViewHolder();
                viewHolder3.thumbnail3 = (ProfilePictureView) convertView3.findViewById(R.id.list_item_friendsreq);
                viewHolder3.title2 = (TextView) convertView3.findViewById(R.id.namefriends);

                viewHolder3.circleBoomMenuButton = (BoomMenuButton) convertView3.findViewById(R.id.boom_circle);
                convertView3.setTag(viewHolder3);
            }

            mainViewholder3 = (ViewHolder) convertView3.getTag();


            final Drawable[] circleSubButtonDrawables = new Drawable[3];
            int[] drawablesResource = new int[]{
                    R.drawable.add,
                    R.drawable.profile_buttons,
                    R.drawable.invitedate
            };
            for (int i = 0; i < 3; i++)
                circleSubButtonDrawables[i]
                        = ContextCompat.getDrawable(parent3.getContext(), drawablesResource[i]);


            final String[] circleSubButtonTexts = new String[]{
                    "Add",
                    "Profile",
                    "Invite"
            };


            final int[][] subButtonColors = new int[3][2];
            for (int i = 0; i < 3; i++) {
                subButtonColors[i][1] = GetRandomColor();
                subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
            }

            // init the BMB with delay

            final ViewHolder finalMainViewholder = mainViewholder3;
            mainViewholder3.circleBoomMenuButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Now with Builder, you can init BMB more convenient
                    new BoomMenuButton.Builder()
                            .subButtons(circleSubButtonDrawables, subButtonColors, circleSubButtonTexts)
                            .button(ButtonType.CIRCLE)
                            .boom(BoomType.PARABOLA)
                            .place(PlaceType.CIRCLE_3_1)
                            .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                            .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                                @Override
                                public void onClick(int buttonIndex) {
                                    if (buttonIndex == 0) {
                                        Toast.makeText(getContext(), "request sent to " + String.valueOf(friendsname.get(position3)), Toast.LENGTH_SHORT).show();

                                        Thread send = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {

                                                    socket = new Socket(InetAddress.getByName(ip), 9000);
                                                    //zend de api call naar de server
                                                    Log.d("kobelog", position3 + "");
                                                    String output = Crypto.encrypts(("request" + profile_id + "," + String.valueOf(friendsreq.get(position3))), key, iv);
                                                    OutputStream out = socket.getOutputStream();
                                                    out.write(output.getBytes(), 0, output.length());

                                                } catch (UnknownHostException e) {
                                                    e.printStackTrace();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    if (socket != null) {
                                                        try {
                                                            socket.close();
                                                            friendsname.remove(friendsname.get(position3));
                                                            friendsreq.remove(friendsreq.get(position3));
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            findAdapter.notifyDataSetChanged();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        send.start();

                                    } else if (buttonIndex == 1) {
                                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                        intent.putExtra("Friendsid", String.valueOf(friendsreq.get(position3)));
                                        intent.putExtra("friendsname", String.valueOf(friendsname.get(position3)));
                                        intent.putExtra("yourid", profile_id);

                                        startActivity(intent);
                                    } else if (buttonIndex == 2) {
                                        Toast.makeText(getContext(), "invite" + String.valueOf(friendsname.get(position3)), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            })
                            .init(finalMainViewholder.circleBoomMenuButton);
                }
            }, 1);
            if (position3 < friendsreq.size()) {
                mainViewholder3.thumbnail3.setProfileId(String.valueOf(friendsreq.get(position3)));
                mainViewholder3.title2.setText(String.valueOf(friendsname.get(position3)));
            }
            if (mainViewholder3.title2.getText() == "") {
                mainViewholder3.thumbnail3.setVisibility(View.GONE);
                mainViewholder3.title2.setVisibility(View.GONE);
                mainViewholder3.circleBoomMenuButton.setVisibility(View.GONE);
            } else if (mainViewholder3.thumbnail3 == null) {
                mainViewholder3.thumbnail3.setVisibility(View.GONE);
                mainViewholder3.title2.setVisibility(View.GONE);
                mainViewholder3.circleBoomMenuButton.setVisibility(View.GONE);
            }
            return convertView3;
        }

    }

    public class ViewHolder {
        //requests
        ProfilePictureView thumbnail;
        TextView title;
        Button button;
        Button button2;
        TextView title2;

        //friends
        ProfilePictureView thumbnail2;
        TextView friendsnametext;
        RadioButton onlineChecker;
        public BoomMenuButton circleBoomMenuButtonfriends;

        ProfilePictureView thumbnailcard;
        TextView friendsnametextcard;

        public BoomMenuButton circleBoomMenuButton;

        ProfilePictureView thumbnail3;
    }

    private void onListItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String to_user = (String) listView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, ChatPage.class);
                intent.putExtra("FROM_USER", profile_id);
                intent.putExtra("TO_USER", to_user);
                intent.putExtra("LOG_IN_USER", profile_id);
                intent.putExtra("profilename", profileName);

                usersList.clear();
                startActivity(intent);
            }
        });
    }
    private CallbackManager sCallbackManager;
    public void invitefriendsfb() {
        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://fb.me/1893536294191128";
        previewImageUrl = "https://scontent-bru2-1.xx.fbcdn.net/v/t1.0-9/15621707_824600874346981_2338594205749336932_n.png?oh=9ca092e31fe492d92e30b9d77540eaff&oe=58EA7B6F";



        sCallbackManager = CallbackManager.Factory.create();

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl).setPreviewImageUrl(previewImageUrl)
                    .build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(this);
            appInviteDialog.registerCallback(sCallbackManager,
                    new FacebookCallback<AppInviteDialog.Result>() {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result) {
                            Log.d("Invitation", "Invitation Sent Successfully");

                        }

                        @Override
                        public void onCancel() {
                            Log.d("Invitation", "Error cancel");
                        }

                        @Override
                        public void onError(FacebookException e) {
                            Log.d("Invitation", "Error Occured");
                        }
                    });

            appInviteDialog.show(content);
        }


    }

    private static String[] Colors = {
            "#F44336",
            "#E91E63",
            "#9C27B0",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#9E9E9E",
            "#607D8B"};

    public static int GetRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        return Color.parseColor(Colors[p]);
    }




    public void generatedates() {
        Thread sendcard = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //connect naar de server
                    socket = new Socket(InetAddress.getByName("94.226.137.151"), 9000);
                    //zend de api call naar de server
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        String output = Crypto.encrypts("alldate" + profile.getId(), key, iv);
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
                    Log.v("cardresponse", response);
                    String[] respondArray = response.split("\\}");


                    //proces de json data en add een element van deze data in de arrays
                    try {
                        for (String res : respondArray) {

                            JSONObject json = new JSONObject(res + "}");
                            if (!dateids.contains(json.get("id"))) {
                                cardsplacelist.add(json.get("place"));
                                cardslocationlist.add(json.get("res"));
                                cardsnamelist.add(json.get("friendname"));
                                cardsidlist.add(json.get("friendid"));
                                carddatetime.add(json.get("time"));
                                dateids.add(json.get("id"));
                            }
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
        sendcard.start();
    }


    private void initializeData() {
        persons = new ArrayList<>();
        if (cardsnamelist.size() != 0) {
            for (int i = 0; i < cardsnamelist.size(); i++) {

                persons.add(new Person(cardsplacelist.get(i).toString(), cardslocationlist.get(i).toString(), cardsidlist.get(i).toString(), carddatetime.get(i).toString(), cardsnamelist.get(i).toString()));
                initializeAdapter();
            }
        }
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);


    }
}
