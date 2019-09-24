package com.example.myspeed.ui.message.vm;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.myspeed.ui.message.model.RemindModel;
import com.example.myspeed.ui.message.view.adapter.RemindRvAdapter;

public class RemindItemViewModel extends BaseObservable {

    private RemindModel remindModel;


    public void setRemindModel(RemindModel remindModel) {
        this.remindModel = remindModel;
        Log.d(RemindRvAdapter.TAG, "setRemindModel: ");
        notifyChange();
    }

    @Bindable
    public String getRemindText(){
        return remindModel.getRemindText();
    }

    @Bindable
    public String getRemindTime(){
        return remindModel.getRemindTime();
    }


}
