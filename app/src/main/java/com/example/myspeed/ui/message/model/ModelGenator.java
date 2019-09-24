package com.example.myspeed.ui.message.model;

import java.util.Collection;

public class ModelGenator<M>{

    private Class<M> type;

    public ModelGenator(Class<M> type){
        this.type=type;
    }

    public M next(){
        try{
            return type.newInstance();
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public Collection<M> fill(Collection<M> coll,int n){
        for (int i = 0; i < n; i++) {
            coll.add(this.next());
        }
        return coll;
    }


}
