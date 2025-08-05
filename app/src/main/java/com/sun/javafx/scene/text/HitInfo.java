package com.sun.javafx.scene.text;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/text/HitInfo.class */
public class HitInfo {
    private int charIndex;
    private boolean leading;

    public int getCharIndex() {
        return this.charIndex;
    }

    public void setCharIndex(int charIndex) {
        this.charIndex = charIndex;
    }

    public boolean isLeading() {
        return this.leading;
    }

    public void setLeading(boolean leading) {
        this.leading = leading;
    }

    public int getInsertionIndex() {
        return this.leading ? this.charIndex : this.charIndex + 1;
    }

    public String toString() {
        return "charIndex: " + this.charIndex + ", isLeading: " + this.leading;
    }
}
