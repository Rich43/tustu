package com.sun.javafx.fxml;

import java.net.URL;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/ParseTraceElement.class */
public class ParseTraceElement {
    private URL location;
    private int lineNumber;

    public ParseTraceElement(URL location, int lineNumber) {
        this.location = location;
        this.lineNumber = lineNumber;
    }

    public URL getLocation() {
        return this.location;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String toString() {
        return (this.location == null ? "?" : this.location.getPath()) + ": " + this.lineNumber;
    }
}
