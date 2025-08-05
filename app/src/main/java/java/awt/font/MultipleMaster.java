package java.awt.font;

import java.awt.Font;

/* loaded from: rt.jar:java/awt/font/MultipleMaster.class */
public interface MultipleMaster {
    int getNumDesignAxes();

    float[] getDesignAxisRanges();

    float[] getDesignAxisDefaults();

    String[] getDesignAxisNames();

    Font deriveMMFont(float[] fArr);

    Font deriveMMFont(float[] fArr, float f2, float f3, float f4, float f5);
}
