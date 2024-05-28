package com.example.contactroombasebasic.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.contactroombasebasic.data.ContactDao;
import com.example.contactroombasebasic.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactRoomDataBase extends RoomDatabase {
    public abstract ContactDao contactDao();
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    // it will create singleton class , only one instance will work
    private static volatile ContactRoomDataBase INSTANCE;
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecutor.execute(() ->{
                        ContactDao contactDao = INSTANCE.contactDao();
                        contactDao.deleteAll();

                        Contact contact = new Contact("Paulo","Teacher");
                        contactDao.insert(contact);

                        contact = new Contact("Bond","Spy");
                        contactDao.insert(contact);
                    });
                }
            };

    public static ContactRoomDataBase getDataBase(final Context context){
        // here we try to implement getInstance of that class
        if (INSTANCE == null) {
            synchronized (ContactRoomDataBase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ContactRoomDataBase.class,
                            "contact_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
