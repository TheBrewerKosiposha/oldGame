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
@Entity(tableName = "Question", foreignKeys = {
        @ForeignKey(entity = Difficulty.class,
                parentColumns = "_id",
                childColumns = "difficulty_id"),

        @ForeignKey(entity = Theme.class,
                parentColumns = "_id",
                childColumns = "theme_id",
                onDelete = CASCADE)
})
public class Question {

    @PrimaryKey@NonNull
    int _id;

    int difficulty_id;

    int theme_id;

    @ColumnInfo(name = "text")
    String text;

    @ColumnInfo(name = "image")
    String image; // URL

    @ColumnInfo(name = "cost")
    int cost;
}
