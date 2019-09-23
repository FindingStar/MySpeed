package com.example.myspeed.ui.message.vm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.myspeed.ui.message.model.RemindModel;

public class RemindItemViewModel extends BaseObservable {

    private RemindModel remindModel;


    public void setRemindModel(RemindModel remindModel) {
        this.remindModel = remindModel;
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
