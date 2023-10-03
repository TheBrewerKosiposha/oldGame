package com.faa.knowyourgame_new.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.faa.knowyourgame_new.entity.Logs;

import java.util.List;

@Dao
public interface LogsDao {

    @Query("SELECT * FROM Logs")
    List<Logs> getLogs();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Logs logs);
}
