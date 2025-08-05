package sun.text;

import java.text.Normalizer;
import sun.text.normalizer.NormalizerBase;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/text/Normalizer.class */
public final class Normalizer {
    public static final int UNICODE_3_2 = 262432;

    private Normalizer() {
    }

    public static String normalize(CharSequence charSequence, Normalizer.Form form, int i2) {
        return NormalizerBase.normalize(charSequence.toString(), form, i2);
    }

    public static boolean isNormalized(CharSequence charSequence, Normalizer.Form form, int i2) {
        return NormalizerBase.isNormalized(charSequence.toString(), form, i2);
    }

    public static final int getCombiningClass(int i2) {
        return NormalizerImpl.getCombiningClass(i2);
    }
}
