package com.test.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public int increase(int num){
        return num+1;
    }
}
