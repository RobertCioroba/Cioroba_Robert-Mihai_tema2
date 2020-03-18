package com.example.tema2;

import android.content.Context;

import androidx.room.Room;

public class DatabaseUser {
    private Context context;
    private static DatabaseUser instance;
    private AppDatabase appDatabase;

    private DatabaseUser(Context context)
    {
        this.context=context;
        appDatabase= Room.databaseBuilder(context,AppDatabase.class,"MyUsers").build();
    }

    public  static  synchronized DatabaseUser getInstance(Context context)
    {
        if(instance == null)
            instance = new DatabaseUser(context);
        return instance;
    }

    public AppDatabase getAppDatabase()
    {
        return  appDatabase;
    }
}
