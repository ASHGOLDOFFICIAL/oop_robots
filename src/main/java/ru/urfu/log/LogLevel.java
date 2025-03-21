package ru.urfu.log;

@SuppressWarnings({"MissingJavadocMethod", "MissingJavadocType"})
public enum LogLevel {
    Trace(0),
    Debug(1),
    Info(2),
    Warning(3),
    Error(4),
    Fatal(5);

    private final int iLevel;

    LogLevel(int iLevel) {
        this.iLevel = iLevel;
    }

    public int level() {
        return iLevel;
    }
}

