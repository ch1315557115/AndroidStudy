package com.example;

/**
 * Created by cao-hao on 17-9-30.
 */

public class Man extends Person {

    public static void main(String[] args) {
       Person person =  new Man();
        person.action();
    }

    @Override
    public void action() {
        super.action();
        System.out.println("man");
    }
}
