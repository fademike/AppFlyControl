package com.example.appflycontrol;

import java.util.ArrayList;
import java.util.List;

public class ParamList {
    public static int cntParam;
    public static int status;
    public static int updaterCnt;



    //static  ArrayList<Param> list;

    private static Param[] params;
    public static void ParamList()
    {
        cntParam = 0;
        status =0;
        updaterCnt = 0;
        //list = new ArrayList<>();
    }
    static public void clearParam()
    {
        cntParam = 0;
        status =0;
    }
    static public void setParam(int index, int all, String name, float value, int type)
    {
        int i;
        if (index < 1) return;
        if (cntParam != all) {
            status = 0;
            cntParam = all;
            params = new Param[all];
            for (i=0;i<all; i++) {params[i] = new Param(0,0,"",0,0);}
        }
        params[index-1].t_index = index;
        params[index-1].t_all = all;
        params[index-1].t_name = name;
        params[index-1].t_value = value;
        params[index-1].t_type = type;
        params[index-1].status = 1;

        if (all <= 0) {status = 0; return;}
        int status_probe =1;
        for (i=0;i<all;i++){if (params[i].status == 0) status_probe =0;}
        status = status_probe;
        updaterCnt ++;
    }
    public static float getProgressParam()
    {
        int i;
        float res = 0;

        if (cntParam <= 0) {res = 0; return res;}
        int cnt_ok = 0;
        int cnt_fail = 0;
        for (i=0;i<cntParam;i++){if (params[i].status != 0) cnt_ok++; else cnt_fail++;}
        res += (float)cnt_ok/(float)cntParam;
        return res;
    }
    public static String getParamName(int n)
    {
        return params[n-1].t_name;
    }
    public static float getParamValue(int n)
    {
        return params[n-1].t_value;
    }
    public static int getParamType(int n)
    {
        return params[n-1].t_type;
    }
    public static void deleteParam(int n){ params[n-1].status = 0; status = 0;}
    public static int getUpdateCnt(){ return updaterCnt;}
    public static int need2getParam()
    {
        if (cntParam == 0)return -1;
        if (status == 1) return 0;
        int i=0;
        for (i=0;i<cntParam;i++){
            if (params[i].status == 0) return (i+1);
        }
        return 0;
    }
}
