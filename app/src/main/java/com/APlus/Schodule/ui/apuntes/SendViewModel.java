package com.APlus.Schodule.ui.apuntes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/* renamed from: com.App.SchoduleT.ui.send.SendViewModel */
public class SendViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SendViewModel() {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        this.mText = mutableLiveData;
        mutableLiveData.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return this.mText;
    }
}
