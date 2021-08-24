package com.katanamajesty.exceptions;

public class NoneAccessAccountException extends GWException {
    public NoneAccessAccountException() {
        super("None получен при получении v2/account/access");
    }
}
