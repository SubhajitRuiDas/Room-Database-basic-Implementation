package com.example.contactroombasebasic.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactroombasebasic.R;
import com.example.contactroombasebasic.model.Contact;

import java.util.List;
import java.util.Objects;

public class RecyclerViewContactAdapter extends RecyclerView.Adapter<RecyclerViewContactAdapter.ViewHolder> {
    private List<Contact> contactList;
    private Context context;
    private onClickContactListener contactListener;

    public RecyclerViewContactAdapter(List<Contact> contactList, Context context, onClickContactListener contactListener) {
        this.contactList = contactList;
        this.context = context;
        this.contactListener = contactListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row,parent,false);
        return new ViewHolder(view, contactListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = Objects.requireNonNull(contactList.get(position));
        holder.contactName.setText(contact.getName());
        holder.contactOccupation.setText(contact.getOccupation());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        onClickContactListener clickContactListener;
        public TextView contactName;
        public TextView contactOccupation;
        public ViewHolder(@NonNull View view, onClickContactListener clickContactListener){
            super(view);
            contactName = view.findViewById(R.id.row_name_textView);
            contactOccupation = view.findViewById(R.id.row_occupation_textView);
            this.clickContactListener = clickContactListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickContactListener.onContactClick(getAdapterPosition());
        }
    }
    public interface onClickContactListener{
        void onContactClick(int position);
    }
}
