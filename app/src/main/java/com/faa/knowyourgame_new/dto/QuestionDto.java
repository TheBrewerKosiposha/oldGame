package com.faa.knowyourgame_new.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class QuestionDto implements Serializable {
    @SerializedName("_id") @Expose
    int _id;
    @SerializedName("difficulty_id") @Expose
    int difficulty_id;
    @SerializedName("theme_id") @Expose
    int theme_id;
    @SerializedName("text") @Expose
    String text;
    @SerializedName("image") @Expose
    String image;
    @SerializedName("cost") @Expose
    int cost;
}
