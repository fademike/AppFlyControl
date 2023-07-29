package com.example.appflycontrol;

public class Param {

    int t_index;
    int t_all;
    String t_name;
    float t_value;
    int t_type;
    int status; // status of load 0 - no data, 1 - full load
    public Param(int index, int all, String name, float value, int type){
        t_index = index;
        t_all = all;
        t_name = name;
        t_value = value;
        t_type = type;
        status = 0;
    }

    public String getName (){
        return t_name;
    }


}
