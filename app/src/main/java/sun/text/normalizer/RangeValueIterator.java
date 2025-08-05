package sun.text.normalizer;

/* loaded from: rt.jar:sun/text/normalizer/RangeValueIterator.class */
public interface RangeValueIterator {

    /* loaded from: rt.jar:sun/text/normalizer/RangeValueIterator$Element.class */
    public static class Element {
        public int start;
        public int limit;
        public int value;
    }

    boolean next(Element element);

    void reset();
}
