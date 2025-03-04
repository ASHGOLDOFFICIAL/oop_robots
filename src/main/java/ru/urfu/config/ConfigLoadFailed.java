package ru.urfu.config;

public final class ConfigLoadFailed extends RuntimeException {
    public ConfigLoadFailed(String message) {
        super(message);
    }
}
