package com.faa.knowyourgame_new.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static androidx.room.ForeignKey.CASCADE;

@EqualsAndHashCode
@Getter
@Setter
@Entity(tableName = "Answers", foreignKeys = @ForeignKey(
        entity = Question.class,
        childColumns = "question_id",
        parentColumns = "_id",
        onDelete = CASCADE))
public class Answer {

    @PrimaryKey@NonNull
    int _id;

    int question_id;

    @ColumnInfo(name = "text")
    String text;

    @ColumnInfo(name = "trueness")
    int trueness;
}
