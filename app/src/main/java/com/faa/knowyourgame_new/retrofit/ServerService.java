package com.faa.knowyourgame_new.retrofit;

import com.faa.knowyourgame_new.dto.DbDto;
import com.faa.knowyourgame_new.dto.LogoutDto;
import com.faa.knowyourgame_new.dto.LogsDto;
import com.faa.knowyourgame_new.dto.RatingDto;
import com.faa.knowyourgame_new.dto.RegisterDto;
import com.faa.knowyourgame_new.dto.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerService {

    @POST("/sign_up")
    Call<RegisterDto> signUp(@Query("login") String login, @Query("password") String password);

    @POST("/sign_in")
    Call<UserDto> signIn(@Query("login") String login, @Query("password") String password);

    @POST("/logout")
    Call<LogoutDto> logOut();

    @POST("/db/update/user")
    Call<UserDto> updateUser(@Query("login") String login, @Query("score") int score);

    @GET("/db/data")
    Call<DbDto> getData();

    @GET("/db/rating")
    Call<RatingDto> getRating();

    @POST("/db/add/log")
    Call<LogsDto> sendLogs(
            @Query("login") String login,
            @Query("answerStatus") int answerStatus,
            @Query("points") int points,
            @Query("date") String date);
}
