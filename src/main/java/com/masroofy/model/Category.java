package com.masroofy.model;

public class Category {
    private String type;

    public Category(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }
}
