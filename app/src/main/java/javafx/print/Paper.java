package javafx.print;

import com.sun.javafx.print.Units;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/print/Paper.class */
public final class Paper {
    private String name;
    private double width;
    private double height;
    private Units units;
    public static final Paper A0 = new Paper("A0", 841.0d, 1189.0d, Units.MM);
    public static final Paper A1 = new Paper("A1", 594.0d, 841.0d, Units.MM);
    public static final Paper A2 = new Paper("A2", 420.0d, 594.0d, Units.MM);
    public static final Paper A3 = new Paper("A3", 297.0d, 420.0d, Units.MM);
    public static final Paper A4 = new Paper("A4", 210.0d, 297.0d, Units.MM);
    public static final Paper A5 = new Paper("A5", 148.0d, 210.0d, Units.MM);
    public static final Paper A6 = new Paper("A6", 105.0d, 148.0d, Units.MM);
    public static final Paper DESIGNATED_LONG = new Paper("Designated Long", 110.0d, 220.0d, Units.MM);
    public static final Paper NA_LETTER = new Paper("Letter", 8.5d, 11.0d, Units.INCH);
    public static final Paper LEGAL = new Paper("Legal", 8.4d, 14.0d, Units.INCH);
    public static final Paper TABLOID = new Paper("Tabloid", 11.0d, 17.0d, Units.INCH);
    public static final Paper EXECUTIVE = new Paper("Executive", 7.25d, 10.5d, Units.INCH);
    public static final Paper NA_8X10 = new Paper("8x10", 8.0d, 10.0d, Units.INCH);
    public static final Paper MONARCH_ENVELOPE = new Paper("Monarch Envelope", 3.87d, 7.5d, Units.INCH);
    public static final Paper NA_NUMBER_10_ENVELOPE = new Paper("Number 10 Envelope", 4.125d, 9.5d, Units.INCH);

    /* renamed from: C, reason: collision with root package name */
    public static final Paper f12644C = new Paper("C", 17.0d, 22.0d, Units.INCH);
    public static final Paper JIS_B4 = new Paper("B4", 257.0d, 364.0d, Units.MM);
    public static final Paper JIS_B5 = new Paper("B5", 182.0d, 257.0d, Units.MM);
    public static final Paper JIS_B6 = new Paper("B6", 128.0d, 182.0d, Units.MM);
    public static final Paper JAPANESE_POSTCARD = new Paper("Japanese Postcard", 100.0d, 148.0d, Units.MM);

    Paper(String paperName, double paperWidth, double paperHeight, Units units) throws IllegalArgumentException {
        if (paperWidth <= 0.0d || paperHeight <= 0.0d) {
            throw new IllegalArgumentException("Illegal dimension");
        }
        if (paperName == null) {
            throw new IllegalArgumentException("Null name");
        }
        this.name = paperName;
        this.width = paperWidth;
        this.height = paperHeight;
        this.units = units;
    }

    public final String getName() {
        return this.name;
    }

    private double getSizeInPoints(double dim) {
        switch (this.units) {
            case POINT:
                return (int) (dim + 0.5d);
            case INCH:
                return (int) ((dim * 72.0d) + 0.5d);
            case MM:
                return (int) (((dim * 72.0d) / 25.4d) + 0.5d);
            default:
                return dim;
        }
    }

    public final double getWidth() {
        return getSizeInPoints(this.width);
    }

    public final double getHeight() {
        return getSizeInPoints(this.height);
    }

    public final int hashCode() {
        return ((int) this.width) + (((int) this.height) << 16) + this.units.hashCode();
    }

    public final boolean equals(Object o2) {
        return o2 != null && (o2 instanceof Paper) && this.name.equals(((Paper) o2).name) && this.width == ((Paper) o2).width && this.height == ((Paper) o2).height && this.units == ((Paper) o2).units;
    }

    public final String toString() {
        return "Paper: " + this.name + " size=" + this.width + LanguageTag.PRIVATEUSE + this.height + " " + ((Object) this.units);
    }
}
