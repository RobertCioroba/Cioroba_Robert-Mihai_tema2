package com.example.tema2;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO {
    @Query("Select * from user")
    List<User> getAll();
    @Insert
    void insert(User user);
    @Delete
    void delete(User user);

    @Query("SELECT * FROM user WHERE name LIKE :altParametru")
    User findByName(String altParametru);

    @Query("DELETE FROM user WHERE name LIKE :parametru")
    void deleteByName(String parametru);

    @Update
    void update(User user);
}
