package com.faa.knowyourgame_new.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.faa.knowyourgame_new.entity.Answer;
import com.faa.knowyourgame_new.entity.Question;
import com.faa.knowyourgame_new.entity.Theme;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface AnswerDao {
    @Query("SELECT * FROM Answers WHERE _id = :id")
    Answer getById(long id);

    @Query("SELECT * FROM Answers")
    List<Answer> getAll();

    @Query("SELECT * FROM Answers WHERE question_id = :questionId")
    List<Answer> getAnswersForQuestion(long questionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Answer answer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Answer> answers);

    @Update
    void update(Answer answer);

    @Update
    void updateMany(List<Answer> answers);

    @Delete
    void deleteMany(List<Answer> answers);
}
