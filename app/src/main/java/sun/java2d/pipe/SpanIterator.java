package sun.java2d.pipe;

/* loaded from: rt.jar:sun/java2d/pipe/SpanIterator.class */
public interface SpanIterator {
    void getPathBox(int[] iArr);

    void intersectClipBox(int i2, int i3, int i4, int i5);

    boolean nextSpan(int[] iArr);

    void skipDownTo(int i2);

    long getNativeIterator();
}
