package com.faa.knowyourgame_new.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DbDto implements Serializable {
    @SerializedName("answers") @Expose
    private ArrayList<AnswersDto> answers;
    @SerializedName("leagues") @Expose
    private ArrayList<LeagueDto> leagues;
    @SerializedName("themes") @Expose
    private List<ThemeDto> themes;
    @SerializedName("difficulties") @Expose
    private ArrayList<DifficultyDto> difficulties;
    @SerializedName("questions") @Expose
    private ArrayList<QuestionDto> questions;
}
