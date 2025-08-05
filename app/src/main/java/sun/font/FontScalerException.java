package sun.font;

/* loaded from: rt.jar:sun/font/FontScalerException.class */
public class FontScalerException extends Exception {
    public FontScalerException() {
        super("Font scaler encountered runtime problem.");
    }

    public FontScalerException(String str) {
        super(str);
    }
}
