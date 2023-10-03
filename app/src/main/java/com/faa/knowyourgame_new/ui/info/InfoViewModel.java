package com.faa.knowyourgame_new.ui.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("How to play this game:\n" +
                "0) Tap on home icon to open main page\n" +
                "1) Choose difficulty\n" +
                "2) Answer on questions\n" +
                "3) Try to get higher rating!\n\n" +
                "What about points?\n" +
                "Easy, if you answer correct, you earn them, else, lose.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}