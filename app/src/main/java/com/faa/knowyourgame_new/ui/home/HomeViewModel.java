package com.faa.knowyourgame_new.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.faa.knowyourgame_new.MainActivity.userDao;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Your current score - " + userDao.getUserScore());
    }

    public LiveData<String> getText() {
        return mText;
    }
}