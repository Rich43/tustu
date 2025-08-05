package sun.security.x509;

import java.util.Comparator;

/* compiled from: RDN.java */
/* loaded from: rt.jar:sun/security/x509/AVAComparator.class */
class AVAComparator implements Comparator<AVA> {
    private static final Comparator<AVA> INSTANCE = new AVAComparator();

    private AVAComparator() {
    }

    static Comparator<AVA> getInstance() {
        return INSTANCE;
    }

    @Override // java.util.Comparator
    public int compare(AVA ava, AVA ava2) {
        boolean zHasRFC2253Keyword = ava.hasRFC2253Keyword();
        if (zHasRFC2253Keyword == ava2.hasRFC2253Keyword()) {
            return ava.toRFC2253CanonicalString().compareTo(ava2.toRFC2253CanonicalString());
        }
        if (zHasRFC2253Keyword) {
            return -1;
        }
        return 1;
    }
}
