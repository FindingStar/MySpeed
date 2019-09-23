package com.example.myspeed.ui.message.vm;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.myspeed.ui.message.model.Conversation;

public class ConversationViewModel extends BaseObservable {

    private Conversation conversation;

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        notifyChange();
    }

    @Bindable
    public String getTime(){
        return conversation.getTime();
    }

    @Bindable
    public String getNickname(){
        return conversation.getNickName();
    }

    @Bindable
    public String getMes(){
        return conversation.getMes();
    }

}
