package org.slf4j.event;

/* loaded from: jssc.jar:org/slf4j/event/Level.class */
public enum Level {
    ERROR(40, "ERROR"),
    WARN(30, "WARN"),
    INFO(20, "INFO"),
    DEBUG(10, "DEBUG"),
    TRACE(0, "TRACE");

    private int levelInt;
    private String levelStr;

    Level(int i2, String s2) {
        this.levelInt = i2;
        this.levelStr = s2;
    }

    public int toInt() {
        return this.levelInt;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.levelStr;
    }
}
