package sun.text;

import sun.text.normalizer.NormalizerBase;

/* loaded from: rt.jar:sun/text/CollatorUtilities.class */
public class CollatorUtilities {
    static NormalizerBase.Mode[] legacyModeMap = {NormalizerBase.NONE, NormalizerBase.NFD, NormalizerBase.NFKD};

    public static int toLegacyMode(NormalizerBase.Mode mode) {
        int length = legacyModeMap.length;
        while (length > 0) {
            length--;
            if (legacyModeMap[length] == mode) {
                break;
            }
        }
        return length;
    }

    public static NormalizerBase.Mode toNormalizerMode(int i2) {
        NormalizerBase.Mode mode;
        try {
            mode = legacyModeMap[i2];
        } catch (ArrayIndexOutOfBoundsException e2) {
            mode = NormalizerBase.NONE;
        }
        return mode;
    }
}
