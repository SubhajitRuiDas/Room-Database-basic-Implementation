package com.example.contactroombasebasic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.contactroombasebasic.model.Contact;
import com.example.contactroombasebasic.model.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NewContact extends AppCompatActivity {

    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION_REPLY = "occupation_reply";
    private EditText enterName;
    private EditText enterOccupation;
    private Button saveInfoButton;
    private int contactId = 0;
    private Boolean isEdited = false;
    private Button updateButton;
    private Button deleteButton;
    private ContactViewModel contactViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        enterName = findViewById(R.id.enter_name);
        enterOccupation = findViewById(R.id.enter_occupation);
        saveInfoButton = findViewById(R.id.save_button);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContact.this.getApplication())
                .create(ContactViewModel.class);

        if(getIntent().hasExtra(MainActivity.CONTACT_ID)){
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            contactViewModel.get(contactId).observe(this, contact -> {
                if(contact != null){
                    enterName.setText(contact.getName());
                    enterOccupation.setText(contact.getOccupation());
                }
            });
            isEdited = true;
        }
        //Save button
        saveInfoButton.setOnClickListener(v -> {
            Intent replyIntent = new Intent(); // it will collect all sort of data from this activity and move all the data to another activity
            if(!TextUtils.isEmpty(enterName.getText()) && !TextUtils.isEmpty(enterOccupation.getText())){
                String name = enterName.getText().toString();
                String occupation = enterOccupation.getText().toString();

                replyIntent.putExtra(NAME_REPLY,name);
                replyIntent.putExtra(OCCUPATION_REPLY,occupation);
                setResult(RESULT_OK,replyIntent);
                //Contact contact = new Contact(enterName.getText().toString(),enterOccupation.getText().toString());
                //ContactViewModel.insert(contact);
                //Log.d("INFO", "new added data: "+contact.getName());
                //Log.d("INFO", "new added data: "+contact.getOccupation());

            }else{
                setResult(RESULT_CANCELED,replyIntent);
                //Toast.makeText(this,R.string.empty,Toast.LENGTH_SHORT).show();
            }
            finish();
        });
        //update button
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
        updateButton.setOnClickListener(v -> {
            int id = contactId;
            String name = enterName.getText().toString().trim();
            String occupation = enterOccupation.getText().toString().trim();
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)){
                Snackbar.make(enterName,R.string.empty,Snackbar.LENGTH_SHORT).show();
            }else{
                Contact contact = new Contact();
                contact.setId(id);
                contact.setName(name);
                contact.setOccupation(occupation);
                ContactViewModel.update(contact);
                finish();
            }
        });
        deleteButton.setOnClickListener(v -> {
            int id = contactId;
            String name = enterName.getText().toString().trim();
            String occupation = enterOccupation.getText().toString().trim();
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)){
                Snackbar.make(enterName,R.string.empty,Snackbar.LENGTH_SHORT).show();
            }else{
                Contact contact = new Contact();
                contact.setId(id);
                contact.setName(name);
                contact.setOccupation(occupation);
                ContactViewModel.delete(contact);
                finish();
            }
        });

        if(isEdited){
            saveInfoButton.setVisibility(View.GONE);
        }else{
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }

        // I tried this to check whether all the data are saved or not...
//        contactViewModel.getAllContacts().observe(this,contacts -> {
//            for(Contact contact : contacts){
//                Log.d("INFO", "all the added data: "+contact.getName());
//            }
//        });
    }
}