package com.biteme.biteme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Gebruiker on 4/02/2017.
 */

public class customAdapter extends ArrayAdapter {
    private int layout;
    public static ArrayList<String> selectedfriendslist = new ArrayList<>();
    public static ArrayList<String> selectedfriendslistids = new ArrayList<>();

    public customAdapter(Context context2, int resource2, List<String> objects2) {
        super(context2, resource2, objects2);
        layout = resource2;
    }

    //BUG: parent2 is de list datafriends en deze is 2X groot den aantal friends dus zullen er nulleble friends zijn
    @NonNull
    @Override
    public View getView(final int position2, View convertView2, final ViewGroup parent2) {
        ViewHolder mainViewholder2 = null;

        final Profile profile = Profile.getCurrentProfile();
        if (convertView2 == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView2 = inflater.inflate(layout, parent2, false);
            customAdapter.ViewHolder viewHolder2 = new customAdapter.ViewHolder();
            viewHolder2.thumbnail4 = (ProfilePictureView) convertView2.findViewById(R.id.list_item_find);
            viewHolder2.findsnametext = (TextView) convertView2.findViewById(R.id.list_item_textfind);

            viewHolder2.circleBoomMenuButtonfind = (CheckBox) convertView2.findViewById(R.id.Invitecheckbox);
            convertView2.setTag(viewHolder2);
        }
        mainViewholder2 = (ViewHolder) convertView2.getTag();
        final ViewHolder finalMainViewholder1 = mainViewholder2;
        mainViewholder2.circleBoomMenuButtonfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalMainViewholder1.circleBoomMenuButtonfind.isChecked())
                {
                 if (!selectedfriendslistids.contains(FindActivity.datalistid.get(position2))) {
                    selectedfriendslist.add(FindActivity.datalistname.get(position2 * 2));
                    selectedfriendslistids.add(FindActivity.datalistid.get(position2*2));
                }
             }
            }
        });

        // init the BMB with delay

        final ViewHolder finalMainViewholder = mainViewholder2;

        if (position2 < FindActivity.datalistid.size()) {
            mainViewholder2.thumbnail4.setProfileId(FindActivity.datalistid.get(position2));
            mainViewholder2.findsnametext.setText(FindActivity.datalistname.get(position2));
        }
         else if (mainViewholder2.thumbnail4 == null || FindActivity.datalistname.size() == 0) {
            mainViewholder2.thumbnail4.setVisibility(View.GONE);
            mainViewholder2.findsnametext.setVisibility(View.GONE);
            mainViewholder2.circleBoomMenuButtonfind.setVisibility(View.GONE);
        }

        return convertView2;
    }


    public class ViewHolder {

        ProfilePictureView thumbnail4;
        TextView findsnametext;
        public CheckBox circleBoomMenuButtonfind;

    }
}

