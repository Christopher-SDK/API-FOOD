package com.food.API_foodfromeachcountry.exception;



public class ValidationException  extends RuntimeException{
    public ValidationException(){
        super();
    }

    public ValidationException(String message){
        super(message);
    }
}