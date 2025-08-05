package sun.java2d;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* loaded from: rt.jar:sun/java2d/Spans.class */
public class Spans {
    private static final int kMaxAddsSinceSort = 256;
    private List mSpans = new Vector(256);
    private int mAddsSinceSort = 0;

    public void add(float f2, float f3) {
        if (this.mSpans != null) {
            this.mSpans.add(new Span(f2, f3));
            int i2 = this.mAddsSinceSort + 1;
            this.mAddsSinceSort = i2;
            if (i2 >= 256) {
                sortAndCollapse();
            }
        }
    }

    public void addInfinite() {
        this.mSpans = null;
    }

    public boolean intersects(float f2, float f3) {
        boolean z2;
        if (this.mSpans != null) {
            if (this.mAddsSinceSort > 0) {
                sortAndCollapse();
            }
            z2 = Collections.binarySearch(this.mSpans, new Span(f2, f3), SpanIntersection.instance) >= 0;
        } else {
            z2 = true;
        }
        return z2;
    }

    private void sortAndCollapse() {
        Collections.sort(this.mSpans);
        this.mAddsSinceSort = 0;
        Iterator it = this.mSpans.iterator();
        Span span = null;
        if (it.hasNext()) {
            span = (Span) it.next();
        }
        while (it.hasNext()) {
            Span span2 = (Span) it.next();
            if (span.subsume(span2)) {
                it.remove();
            } else {
                span = span2;
            }
        }
    }

    /* loaded from: rt.jar:sun/java2d/Spans$Span.class */
    static class Span implements Comparable {
        private float mStart;
        private float mEnd;

        Span(float f2, float f3) {
            this.mStart = f2;
            this.mEnd = f3;
        }

        final float getStart() {
            return this.mStart;
        }

        final float getEnd() {
            return this.mEnd;
        }

        final void setStart(float f2) {
            this.mStart = f2;
        }

        final void setEnd(float f2) {
            this.mEnd = f2;
        }

        boolean subsume(Span span) {
            boolean zContains = contains(span.mStart);
            if (zContains && span.mEnd > this.mEnd) {
                this.mEnd = span.mEnd;
            }
            return zContains;
        }

        boolean contains(float f2) {
            return this.mStart <= f2 && f2 < this.mEnd;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object obj) {
            int i2;
            float start = ((Span) obj).getStart();
            if (this.mStart < start) {
                i2 = -1;
            } else if (this.mStart > start) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            return i2;
        }

        public String toString() {
            return "Span: " + this.mStart + " to " + this.mEnd;
        }
    }

    /* loaded from: rt.jar:sun/java2d/Spans$SpanIntersection.class */
    static class SpanIntersection implements Comparator {
        static final SpanIntersection instance = new SpanIntersection();

        private SpanIntersection() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            int i2;
            Span span = (Span) obj;
            Span span2 = (Span) obj2;
            if (span.getEnd() <= span2.getStart()) {
                i2 = -1;
            } else if (span.getStart() >= span2.getEnd()) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            return i2;
        }
    }
}
