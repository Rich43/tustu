package com.sun.javafx.css;

import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/javafx/css/SizeUnits.class */
public enum SizeUnits {
    PERCENT(false) { // from class: com.sun.javafx.css.SizeUnits.1
        @Override // java.lang.Enum
        public String toString() {
            return FXMLLoader.RESOURCE_KEY_PREFIX;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier, Font font_not_used) {
            return (value / 100.0d) * multiplier;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier, Font font_not_used) {
            return (value / 100.0d) * multiplier;
        }
    },
    IN(true) { // from class: com.sun.javafx.css.SizeUnits.2
        @Override // java.lang.Enum
        public String toString() {
            return "in";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return value * SizeUnits.POINTS_PER_INCH;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return value * SizeUnits.DOTS_PER_INCH;
        }
    },
    CM(true) { // from class: com.sun.javafx.css.SizeUnits.3
        @Override // java.lang.Enum
        public String toString() {
            return PdfOps.cm_TOKEN;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return (value / SizeUnits.CM_PER_INCH) * SizeUnits.POINTS_PER_INCH;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return (value / SizeUnits.CM_PER_INCH) * SizeUnits.DOTS_PER_INCH;
        }
    },
    MM(true) { // from class: com.sun.javafx.css.SizeUnits.4
        @Override // java.lang.Enum
        public String toString() {
            return "mm";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return (value / SizeUnits.MM_PER_INCH) * SizeUnits.POINTS_PER_INCH;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return (value / SizeUnits.MM_PER_INCH) * SizeUnits.DOTS_PER_INCH;
        }
    },
    EM(false) { // from class: com.sun.javafx.css.SizeUnits.5
        @Override // java.lang.Enum
        public String toString() {
            return "em";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font) {
            return SizeUnits.round(value * SizeUnits.pointSize(font));
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font) {
            return SizeUnits.round(value * SizeUnits.pixelSize(font));
        }
    },
    EX(false) { // from class: com.sun.javafx.css.SizeUnits.6
        @Override // java.lang.Enum
        public String toString() {
            return "ex";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font) {
            return SizeUnits.round((value / 2.0d) * SizeUnits.pointSize(font));
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font) {
            return SizeUnits.round((value / 2.0d) * SizeUnits.pixelSize(font));
        }
    },
    PT(true) { // from class: com.sun.javafx.css.SizeUnits.7
        @Override // java.lang.Enum
        public String toString() {
            return "pt";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return value;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return value * 1.3333333333333333d;
        }
    },
    PC(true) { // from class: com.sun.javafx.css.SizeUnits.8
        @Override // java.lang.Enum
        public String toString() {
            return "pc";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return value * 12.0d;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return value * 12.0d * 1.3333333333333333d;
        }
    },
    PX(true) { // from class: com.sun.javafx.css.SizeUnits.9
        @Override // java.lang.Enum
        public String toString() {
            return "px";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return value * 0.75d;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return value;
        }
    },
    DEG(true) { // from class: com.sun.javafx.css.SizeUnits.10
        @Override // java.lang.Enum
        public String toString() {
            return "deg";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return SizeUnits.round(value);
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return SizeUnits.round(value);
        }
    },
    GRAD(true) { // from class: com.sun.javafx.css.SizeUnits.11
        @Override // java.lang.Enum
        public String toString() {
            return "grad";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return SizeUnits.round((value * 9.0d) / 10.0d);
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return SizeUnits.round((value * 9.0d) / 10.0d);
        }
    },
    RAD(true) { // from class: com.sun.javafx.css.SizeUnits.12
        @Override // java.lang.Enum
        public String toString() {
            return "rad";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return SizeUnits.round((value * 180.0d) / 3.141592653589793d);
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return SizeUnits.round((value * 180.0d) / 3.141592653589793d);
        }
    },
    TURN(true) { // from class: com.sun.javafx.css.SizeUnits.13
        @Override // java.lang.Enum
        public String toString() {
            return "turn";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return SizeUnits.round(value * 360.0d);
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return SizeUnits.round(value * 360.0d);
        }
    },
    S(true) { // from class: com.sun.javafx.css.SizeUnits.14
        @Override // java.lang.Enum
        public String toString() {
            return PdfOps.s_TOKEN;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return value;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return value;
        }
    },
    MS(true) { // from class: com.sun.javafx.css.SizeUnits.15
        @Override // java.lang.Enum
        public String toString() {
            return "ms";
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double points(double value, double multiplier_not_used, Font font_not_used) {
            return value;
        }

        @Override // com.sun.javafx.css.SizeUnits
        public double pixels(double value, double multiplier_not_used, Font font_not_used) {
            return value;
        }
    };

    private final boolean absolute;
    private static final double DOTS_PER_INCH = 96.0d;
    private static final double POINTS_PER_INCH = 72.0d;
    private static final double CM_PER_INCH = 2.54d;
    private static final double MM_PER_INCH = 25.4d;
    private static final double POINTS_PER_PICA = 12.0d;

    abstract double points(double d2, double d3, Font font);

    abstract double pixels(double d2, double d3, Font font);

    SizeUnits(boolean absolute) {
        this.absolute = absolute;
    }

    public boolean isAbsolute() {
        return this.absolute;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double pointSize(Font font) {
        return pixelSize(font) * 0.75d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double pixelSize(Font font) {
        return font != null ? font.getSize() : Font.getDefault().getSize();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double round(double d2) {
        if (d2 == 0.0d) {
            return d2;
        }
        double r2 = d2 < 0.0d ? -0.05d : 0.05d;
        return ((long) ((d2 + r2) * 10.0d)) / 10.0d;
    }
}
