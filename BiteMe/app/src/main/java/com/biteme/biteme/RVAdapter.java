package com.biteme.biteme;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        TextView personlocation;
        TextView personadress;
        TextView persontime;
        ProfilePictureView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            personName = (TextView)itemView.findViewById(R.id.textViewNamecard);
            personlocation = (TextView)itemView.findViewById(R.id.textviewseneder);
            personPhoto = (ProfilePictureView)itemView.findViewById(R.id.imagecard);
            personadress= (TextView) itemView.findViewById(R.id.cardadress);
            persontime= (TextView) itemView.findViewById(R.id.cardstime);
        }
    }

    List<Person> persons;

    RVAdapter(List<Person> persons){
        this.persons = MainActivity.persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_layout, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).adress);
        personViewHolder.personlocation.setText(persons.get(i).age);
        personViewHolder.persontime.setText(persons.get(i).time);
        personViewHolder.personadress.setText(persons.get(i).name);
        personViewHolder.personPhoto.setProfileId(persons.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}
