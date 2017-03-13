package com.biteme.biteme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addSlide(AppIntroFragment.newInstance("Login", "Login using Facebook.", R.drawable.loginpic, Color.parseColor("#4CAF50")));

        addSlide(AppIntroFragment.newInstance("Connect", "Connect with new people or people you know.", R.drawable.recpic, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Chat", "Have a nice chat", R.drawable.chatpic, Color.parseColor("#F44336")));
        addSlide(AppIntroFragment.newInstance("Find", "Find the perfect place for a date.", R.drawable.mappic, Color.parseColor("#FFC107")));
        addSlide(AppIntroFragment.newInstance("M'eat", "If you get along go M'eat eachother at a restaurant", R.drawable.findpic, Color.parseColor("#2196F3")));
        addSlide(AppIntroFragment.newInstance("All set!", "We hope you will enjoy BiteMe!", R.drawable.approved, Color.parseColor("#9E9E9E")));

               // "#E91E63"
                //"#9C27B0"
          //      "#2196F3"
            //    "#03A9F4"
              //  "#00BCD4"
//                "#009688"
  //              "#4CAF50"
    //            "#8BC34A"
      //          "#CDDC39"
        //        "#FFEB3B"
          //      "#FFC107"
            //    "#FF9800"
              //  "#FF5722"
        //        "#795548"
          //      "#9E9E9E"
            //    "#607D8B"



        showSkipButton(true);
        setProgressButtonEnabled(true);

        setFadeAnimation();

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        final String PREFS_NAME = "MyPrefsFile";
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putBoolean("my_first_time", false).commit();
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        final String PREFS_NAME = "MyPrefsFile";
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putBoolean("my_first_time", false).commit();
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}