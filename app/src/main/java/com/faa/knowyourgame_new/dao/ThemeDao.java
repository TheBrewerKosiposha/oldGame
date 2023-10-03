package com.faa.knowyourgame_new.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.faa.knowyourgame_new.dto.ThemeDto;
import com.faa.knowyourgame_new.entity.Answer;
import com.faa.knowyourgame_new.entity.Theme;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ThemeDao {
    @Query("SELECT * FROM Theme WHERE _id = :id")
    Theme getById(long id);

    @Query("SELECT * FROM Theme")
    List<Theme> getAll();

    @Query("SELECT name FROM Theme WHERE _id = :id")
    String getNameById(long id);

    @Insert
    void insert(Theme theme);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Theme> themes);

    @Update
    void update(Theme theme);

    @Update
    void updateMany(List<Theme> themes);

    @Delete
    void deleteMany(List<Theme> themes);
}
