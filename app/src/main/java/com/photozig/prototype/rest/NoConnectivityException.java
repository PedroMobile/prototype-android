package com.photozig.prototype.rest;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    public NoConnectivityException(String s) {
        super(s);
    }
}
