package java.time.format;

/* loaded from: rt.jar:java/time/format/SignStyle.class */
public enum SignStyle {
    NORMAL,
    ALWAYS,
    NEVER,
    NOT_NEGATIVE,
    EXCEEDS_PAD;

    boolean parse(boolean z2, boolean z3, boolean z4) {
        switch (this) {
            case NORMAL:
                return (z2 && z3) ? false : true;
            case ALWAYS:
            case EXCEEDS_PAD:
                return true;
            case NEVER:
            case NOT_NEGATIVE:
            default:
                return (z3 || z4) ? false : true;
        }
    }
}
