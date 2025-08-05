package javax.swing.text;

import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:javax/swing/text/SegmentCache.class */
class SegmentCache {
    private static SegmentCache sharedCache = new SegmentCache();
    private List<Segment> segments = new ArrayList(11);

    public static SegmentCache getSharedInstance() {
        return sharedCache;
    }

    public static Segment getSharedSegment() {
        return getSharedInstance().getSegment();
    }

    public static void releaseSharedSegment(Segment segment) {
        getSharedInstance().releaseSegment(segment);
    }

    public Segment getSegment() {
        synchronized (this) {
            int size = this.segments.size();
            if (size > 0) {
                return this.segments.remove(size - 1);
            }
            return new CachedSegment();
        }
    }

    public void releaseSegment(Segment segment) {
        if (segment instanceof CachedSegment) {
            synchronized (this) {
                segment.array = null;
                segment.count = 0;
                this.segments.add(segment);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/SegmentCache$CachedSegment.class */
    private static class CachedSegment extends Segment {
        private CachedSegment() {
        }
    }
}
