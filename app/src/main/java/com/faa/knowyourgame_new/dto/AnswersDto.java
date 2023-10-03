package com.faa.knowyourgame_new.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class AnswersDto implements Serializable {
    @SerializedName("_id") @Expose
    int _id;
    @SerializedName("question_id") @Expose
    int question_id;
    @SerializedName("text") @Expose
    String text;
    @SerializedName("trueness") @Expose
    int trueness;
}
