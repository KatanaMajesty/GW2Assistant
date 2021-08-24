package com.katanamajesty.exceptions;

public class GWException extends Exception {

    public GWException() {
        super("GW2bot ran in an exception!");
    }

    public GWException(String s) {
        super("GW2bot ran in an exception! " + s);
    }

}
