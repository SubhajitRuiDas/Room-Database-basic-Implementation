package com.example.contactroombasebasic.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactroombasebasic.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    // here i will perform all the CRUD operation -> create read update and delete
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);
    @Query("DELETE FROM contact_table")
    void deleteAll();
    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    LiveData<List<Contact>> getAllContacts();
    @Query("SELECT * FROM contact_table WHERE contact_table.id == :id")
    LiveData<Contact> get(int id);
    @Update
    void update(Contact contact);
    @Delete
    void delete(Contact contact);  // to delete one single item or object of Contact
}
