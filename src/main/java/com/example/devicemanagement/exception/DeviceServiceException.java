package com.example.devicemanagement.exception;

public class DeviceServiceException extends RuntimeException {
    public DeviceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}