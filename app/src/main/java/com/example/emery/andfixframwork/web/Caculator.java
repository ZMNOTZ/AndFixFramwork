package com.example.emery.andfixframwork.web;

import com.example.emery.andfixframwork.Replace;

/**
 * Created by emery on 2017/9/10.
 */

public class Caculator {
    @Replace(jazz ="com.example.emery.andfixframwork.Caculator",method ="caculate")
    public int caculate(){
        int j=10;
        int n=1;
        return j/n;
    }
}
