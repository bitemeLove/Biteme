package com.biteme.biteme;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by Vivek on 01-Aug-16.
 */
public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    TextView  msg;
    ProfilePictureView user_profile_pic_chat;
    public ChatMessageViewHolder(View itemView) {
        super(itemView);

        msg = (TextView) itemView.findViewById(R.id.msg);
        user_profile_pic_chat = (ProfilePictureView) itemView.findViewById(R.id.user_profile_pic_chat);
    }
}
