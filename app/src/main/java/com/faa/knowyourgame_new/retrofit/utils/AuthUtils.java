package com.faa.knowyourgame_new.retrofit.utils;

import android.util.Log;

import com.faa.knowyourgame_new.dao.UserDao;
import com.faa.knowyourgame_new.dto.LogoutDto;
import com.faa.knowyourgame_new.dto.RegisterDto;
import com.faa.knowyourgame_new.dto.UserDto;
import com.faa.knowyourgame_new.entity.User;

import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.faa.knowyourgame_new.retrofit.RetrofitClient.myService;

public class AuthUtils {

    private static final String TAG = "AuthUtils";

    public interface RegistrationCallBack<T>{
        void register(String signUpResponse);
    }

    public interface LoginCallBack<T>{
        void login(UserDto signInResponse);
    }

    public interface LogoutCallBack<T>{
        void logout(String logoutResponse);
    }

    public static void updateUserData(UserDao userDao){
        User currentUser = userDao.getCurrentUser();
        myService.updateUser(currentUser.getLogin(), currentUser.getScore()).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(
                    @NotNull Call<UserDto> call,
                    @NotNull Response<UserDto> response) {
                Log.d(TAG, "User update status: " + response.body());
            }

            @Override
            public void onFailure(
                    @NotNull Call<UserDto> call,
                    @NotNull Throwable t) {
                Log.e(TAG, "Error loading from API (Update User)");
            }
        });
    }

    public static void logoutUser(LogoutCallBack logoutCallBack){
        myService.logOut().enqueue(new Callback<LogoutDto>() {

            @Override
            public void onResponse(
                    @NotNull Call<LogoutDto> call,
                    @NotNull Response<LogoutDto> response) {
                Log.d(TAG, "Logout user status: " + response.body().getLogout());
                logoutCallBack.logout(String.valueOf(response.body().getLogout()));
            }

            @Override
            public void onFailure(
                    @NotNull Call<LogoutDto> call,
                    @NotNull Throwable t) {
                Log.e(TAG, "Error loading from API (Logout)");
            }
        });
    }

    public static void signIn(String login,
                              String password,
                              LoginCallBack loginCallBack) {

        myService.signIn(login, password).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(
                    @NotNull Call<UserDto> call,
                    @NotNull Response<UserDto> response) {

                if(response.isSuccessful()) {
                    Log.d(TAG, "Sign in user: " + response.body());
                    loginCallBack.login(response.body());
                }
                else { Log.d(TAG, "Sign in status(code): " + response.code()); }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Log.e(TAG, "Error loading from API");
            }
        });
    }

    public static void signUp(String login,
                              String password,
                              RegistrationCallBack registrationCallBack) {

        myService.signUp(login, password).enqueue(new Callback<RegisterDto>() {
            @Override
            public void onResponse(
                    @NotNull Call<RegisterDto> call,
                    @NotNull Response<RegisterDto> response) {

                if(response.isSuccessful()) {
                    Log.d(TAG, "Sign up status: " + response.body().getStatus());
                    registrationCallBack.register(response.body().getStatus());
                }
                else { Log.d(TAG, "Sign up status(code): " + response.code()); }
            }

            @Override
            public void onFailure(
                    @NotNull Call<RegisterDto> call,
                    @NotNull Throwable t) {
                Log.e(TAG, "Error loading from API");
            }
        });
    }


    public static void addUser(UserDto userDto, UserDao userDao) {

        ModelMapper modelMapper = new ModelMapper();

        User loginUser = modelMapper.map(userDto, User.class);
        userDao.insert(loginUser);
    }

    public static void updateUser(UserDto userDto, UserDao userDao){
        ModelMapper modelMapper = new ModelMapper();

        User loginUser = modelMapper.map(userDto, User.class);
        userDao.update(loginUser);
    }

    public static void deleteUser(UserDao userDao){
        userDao.deleteTempData();
    }
}
