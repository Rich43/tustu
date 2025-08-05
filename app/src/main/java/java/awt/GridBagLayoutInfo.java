package java.awt;

import java.awt.Component;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/GridBagLayoutInfo.class */
public class GridBagLayoutInfo implements Serializable {
    private static final long serialVersionUID = -4899416460737170217L;
    int width;
    int height;
    int startx;
    int starty;
    int[] minWidth;
    int[] minHeight;
    double[] weightX;
    double[] weightY;
    boolean hasBaseline;
    short[] baselineType;
    int[] maxAscent;
    int[] maxDescent;

    GridBagLayoutInfo(int i2, int i3) {
        this.width = i2;
        this.height = i3;
    }

    boolean hasConstantDescent(int i2) {
        return (this.baselineType[i2] & (1 << Component.BaselineResizeBehavior.CONSTANT_DESCENT.ordinal())) != 0;
    }

    boolean hasBaseline(int i2) {
        return this.hasBaseline && this.baselineType[i2] != 0;
    }
}
