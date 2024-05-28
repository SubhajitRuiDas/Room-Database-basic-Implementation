package com.example.contactroombasebasic.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.contactroombasebasic.model.Contact;
import com.example.contactroombasebasic.util.ContactRoomDataBase;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application){
        ContactRoomDataBase db = ContactRoomDataBase.getDataBase(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
    }
    public LiveData<List<Contact>> getAllData() {return allContacts;}

    public void insert(Contact contact){
        ContactRoomDataBase.databaseWriteExecutor.execute(()-> contactDao.insert(contact));
    }

    public LiveData<Contact> get(int id){
        return contactDao.get(id);
    }
    public void update(Contact contact){
        // we want to perform the update operation in the background
        ContactRoomDataBase.databaseWriteExecutor.execute(()-> contactDao.update(contact));
    }

    public void delete(Contact contact){
        ContactRoomDataBase.databaseWriteExecutor.execute(()-> contactDao.delete(contact));
    }
}
