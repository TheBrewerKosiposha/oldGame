package com.faa.knowyourgame_new.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class LogsDto implements Serializable {
    @SerializedName("login") @Expose
    String login;
    @SerializedName("answerStatus") @Expose
    int answer_status;
    @SerializedName("points") @Expose
    int points;
    @SerializedName("date") @Expose
    String date;
}
