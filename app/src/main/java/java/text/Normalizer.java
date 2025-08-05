package java.text;

import sun.text.normalizer.NormalizerBase;

/* loaded from: rt.jar:java/text/Normalizer.class */
public final class Normalizer {

    /* loaded from: rt.jar:java/text/Normalizer$Form.class */
    public enum Form {
        NFD,
        NFC,
        NFKD,
        NFKC
    }

    private Normalizer() {
    }

    public static String normalize(CharSequence charSequence, Form form) {
        return NormalizerBase.normalize(charSequence.toString(), form);
    }

    public static boolean isNormalized(CharSequence charSequence, Form form) {
        return NormalizerBase.isNormalized(charSequence.toString(), form);
    }
}
