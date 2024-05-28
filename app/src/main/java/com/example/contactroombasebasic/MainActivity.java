package com.example.contactroombasebasic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactroombasebasic.adapters.RecyclerViewContactAdapter;
import com.example.contactroombasebasic.model.Contact;
import com.example.contactroombasebasic.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewContactAdapter.onClickContactListener{
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    public static final String CONTACT_ID = "contact_id";
    private ContactViewModel contactViewModel;
    //private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerViewContactAdapter recyclerViewContactAdapter;
    //private LiveData<List<Contact>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, contacts -> {
            // set up the adapter
            recyclerViewContactAdapter = new RecyclerViewContactAdapter(contacts,MainActivity.this,this);
            recyclerView.setAdapter(recyclerViewContactAdapter);
//            StringBuilder builder = new StringBuilder();
//            for(Contact contact : contacts){
//                builder.append(" - ").append(contact.getName()).append(" ").append(contact.getOccupation());
//                Log.d("TAG", "onCreate: " + contact.getName());
//                Log.d("TAG", "onCreate: " + contact.getOccupation());
//            }

        });

        FloatingActionButton fab = findViewById(R.id.add_contact_btn);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            startActivityForResult(intent,NEW_CONTACT_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            assert data != null;
            String name = data.getStringExtra(NewContact.NAME_REPLY);
            String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);

            assert name != null;
            Contact contact = new Contact(name,occupation);
            ContactViewModel.insert(contact);
        }
    }

    @Override
    public void onContactClick(int position) {
        Contact contact = Objects.requireNonNull(contactViewModel.getAllContacts().getValue()).get(position);
        Log.d("Click", "onContactClick: " + contact.getId());

        // By this line we can switch to NewContact screen on clicking any row
        //startActivity(new Intent(MainActivity.this,NewContact.class));
        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID,contact.getId());
        startActivity(intent);
    }
}