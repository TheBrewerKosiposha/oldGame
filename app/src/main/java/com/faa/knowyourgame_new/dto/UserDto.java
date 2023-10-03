package com.faa.knowyourgame_new.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDto implements Serializable {
    @SerializedName("login") @Expose
    private String login;
    @SerializedName("password") @Expose
    private String password;
    @SerializedName("score") @Expose
    private double score;
}