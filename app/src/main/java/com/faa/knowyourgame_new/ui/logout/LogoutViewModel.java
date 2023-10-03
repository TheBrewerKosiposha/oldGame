package com.faa.knowyourgame_new.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> logoutText;

    public LogoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Here you will leave us...");

        logoutText = new MutableLiveData<>();
        logoutText.setValue("Logout");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getLogoutText() {
        return logoutText;
    }
}