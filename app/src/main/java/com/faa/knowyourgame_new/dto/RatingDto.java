package com.faa.knowyourgame_new.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class RatingDto implements Serializable {
    @SerializedName("users") @Expose
    private ArrayList<UserDto> users;
}
