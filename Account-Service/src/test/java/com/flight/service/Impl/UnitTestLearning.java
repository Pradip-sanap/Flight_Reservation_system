package com.flight.service.Impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Learning testing")
public class UnitTestLearning {

    @BeforeAll
    static void start(){
        System.out.println("Start");
    }
    @AfterAll
    static void end(){
        System.out.println("End");
    }

    @BeforeEach
    void setup(){
        System.out.println("Before each");
    }
    @AfterEach
    void clanup(){
        System.out.println("After each");
    }

    @Test
    void test1(){
        System.out.println("Test 1");
    }
    @Test
    void test2(){
        System.out.println("Test 2");
    }
    @Test
    void test3(){
        System.out.println("Test 3");
    }
}
