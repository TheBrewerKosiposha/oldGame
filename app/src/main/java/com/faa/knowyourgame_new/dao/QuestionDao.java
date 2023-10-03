package com.faa.knowyourgame_new.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.faa.knowyourgame_new.entity.Question;
import com.faa.knowyourgame_new.entity.Theme;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM Question WHERE _id = :id")
    Question getById(long id);

    @Query("SELECT * FROM Question")
    List<Question> getAll();

    @Query("SELECT * FROM Question WHERE difficulty_id = :diff_id")
    List<Question> getQuestionsByDiffId(long diff_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Question> questions);

    @Update
    void update(Question question);

    @Update
    void updateMany(List<Question> questions);

    @Delete
    void deleteMany(List<Question> questions);
}
