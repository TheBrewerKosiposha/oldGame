package com.faa.knowyourgame_new.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import lombok.Data;

@Data
public class LogoutDto implements Serializable {
    @SerializedName("logout") @Expose
    int logout;
}
