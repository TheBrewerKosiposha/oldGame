package com.faa.knowyourgame_new.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class LeagueDto implements Serializable {
    @SerializedName("name") @Expose
    String name;
    @SerializedName("image") @Expose
    String image;
    @SerializedName("rating") @Expose
    int rating;
}
