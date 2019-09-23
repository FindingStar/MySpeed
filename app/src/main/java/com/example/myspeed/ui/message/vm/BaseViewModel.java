package com.example.myspeed.ui.message.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public abstract class BaseViewModel<M> extends ViewModel {

    private LiveData<M> model=new MutableLiveData<>();



    public LiveData<M> getModel() {
        return model;
    }
}
