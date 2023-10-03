package com.faa.knowyourgame_new.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "Logs")
public class Logs {

    @PrimaryKey(autoGenerate = true)@NonNull
    int id;

    @ColumnInfo(name = "login")
    String login;

    @ColumnInfo(name = "answer_status")
    int answerStatus;

    @ColumnInfo(name = "points")
    int points;

    @ColumnInfo(name = "date_time")
    String dateTime;
}
