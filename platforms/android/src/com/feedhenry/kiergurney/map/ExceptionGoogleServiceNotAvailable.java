package com.feedhenry.kiergurney.map;

/**
 * Created by kxiang on 21/02/2014.
 */
public class ExceptionGoogleServiceNotAvailable extends Throwable {
    public ExceptionGoogleServiceNotAvailable(){
        super("Google Service not available");
    }
}