package java.awt.font;

/* loaded from: rt.jar:java/awt/font/TextJustifier.class */
class TextJustifier {
    private GlyphJustificationInfo[] info;
    private int start;
    private int limit;
    static boolean DEBUG = false;
    public static final int MAX_PRIORITY = 3;

    TextJustifier(GlyphJustificationInfo[] glyphJustificationInfoArr, int i2, int i3) {
        this.info = glyphJustificationInfoArr;
        this.start = i2;
        this.limit = i3;
        if (DEBUG) {
            System.out.println("start: " + i2 + ", limit: " + i3);
            for (int i4 = i2; i4 < i3; i4++) {
                GlyphJustificationInfo glyphJustificationInfo = glyphJustificationInfoArr[i4];
                System.out.println("w: " + glyphJustificationInfo.weight + ", gp: " + glyphJustificationInfo.growPriority + ", gll: " + glyphJustificationInfo.growLeftLimit + ", grl: " + glyphJustificationInfo.growRightLimit);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:68:0x0173  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public float[] justify(float r6) {
        /*
            Method dump skipped, instructions count: 903
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.font.TextJustifier.justify(float):float[]");
    }
}
