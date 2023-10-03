package com.faa.knowyourgame_new.retrofit.utils;

import com.faa.knowyourgame_new.retrofit.RetrofitClient;
import com.faa.knowyourgame_new.retrofit.ServerService;


public class ApiUtils {
    public static final String BASE_SERVER_URL = "http://10.0.2.2:8000/";
    public static final String BASE_SERVER_QUESTION_IMAGE_DIR = BASE_SERVER_URL + "images/questions/";
    public static final String BASE_SERVER_LEAGUE_IMAGE_DIR = BASE_SERVER_URL + "images/league/";

    public static ServerService getServerService() {
        return RetrofitClient.getClient(BASE_SERVER_URL).create(ServerService.class);
    }
}
