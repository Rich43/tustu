package com.sun.javafx.tk;

import java.io.InputStream;
import java.util.List;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/FontLoader.class */
public abstract class FontLoader {
    public abstract void loadFont(Font font);

    public abstract List<String> getFamilies();

    public abstract List<String> getFontNames();

    public abstract List<String> getFontNames(String str);

    public abstract Font font(String str, FontWeight fontWeight, FontPosture fontPosture, float f2);

    public abstract Font loadFont(InputStream inputStream, double d2);

    public abstract Font loadFont(String str, double d2);

    public abstract FontMetrics getFontMetrics(Font font);

    public abstract float computeStringWidth(String str, Font font);

    public abstract float getSystemFontSize();
}
