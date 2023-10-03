package com.faa.knowyourgame_new.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Entity(tableName = "User")
public class User {

    @PrimaryKey@NonNull
    String login;
    
    @ColumnInfo(name = "password")
    String password;

    @ColumnInfo(name = "score")
    int score;
}
