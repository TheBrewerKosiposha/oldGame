package com.faa.knowyourgame_new.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class ThemeDto implements Serializable {
    @SerializedName("_id") @Expose
    int _id;
    @SerializedName("name") @Expose
    String name;
}
