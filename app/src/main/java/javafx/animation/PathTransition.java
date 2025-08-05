package javafx.animation;

import com.sun.javafx.geom.PathIterator;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/PathTransition.class */
public final class PathTransition extends Transition {
    private ObjectProperty<Node> node;
    private double totalLength;
    private final ArrayList<Segment> segments;
    private static final int SMOOTH_ZONE = 10;
    private Node cachedNode;
    private ObjectProperty<Duration> duration;
    private ObjectProperty<Shape> path;
    private ObjectProperty<OrientationType> orientation;
    private boolean cachedIsNormalRequired;
    private static final Node DEFAULT_NODE = null;
    private static final Duration DEFAULT_DURATION = Duration.millis(400.0d);
    private static final Shape DEFAULT_PATH = null;
    private static final OrientationType DEFAULT_ORIENTATION = OrientationType.NONE;

    /* loaded from: jfxrt.jar:javafx/animation/PathTransition$OrientationType.class */
    public enum OrientationType {
        NONE,
        ORTHOGONAL_TO_TANGENT
    }

    public final void setNode(Node value) {
        if (this.node != null || value != null) {
            nodeProperty().set(value);
        }
    }

    public final Node getNode() {
        return this.node == null ? DEFAULT_NODE : this.node.get();
    }

    public final ObjectProperty<Node> nodeProperty() {
        if (this.node == null) {
            this.node = new SimpleObjectProperty(this, "node", DEFAULT_NODE);
        }
        return this.node;
    }

    public final void setDuration(Duration value) {
        if (this.duration != null || !DEFAULT_DURATION.equals(value)) {
            durationProperty().set(value);
        }
    }

    public final Duration getDuration() {
        return this.duration == null ? DEFAULT_DURATION : this.duration.get();
    }

    public final ObjectProperty<Duration> durationProperty() {
        if (this.duration == null) {
            this.duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION) { // from class: javafx.animation.PathTransition.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    try {
                        PathTransition.this.setCycleDuration(PathTransition.this.getDuration());
                    } catch (IllegalArgumentException e2) {
                        if (isBound()) {
                            unbind();
                        }
                        set(PathTransition.this.getCycleDuration());
                        throw e2;
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PathTransition.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "duration";
                }
            };
        }
        return this.duration;
    }

    public final void setPath(Shape value) {
        if (this.path != null || value != null) {
            pathProperty().set(value);
        }
    }

    public final Shape getPath() {
        return this.path == null ? DEFAULT_PATH : this.path.get();
    }

    public final ObjectProperty<Shape> pathProperty() {
        if (this.path == null) {
            this.path = new SimpleObjectProperty(this, "path", DEFAULT_PATH);
        }
        return this.path;
    }

    public final void setOrientation(OrientationType value) {
        if (this.orientation != null || !DEFAULT_ORIENTATION.equals(value)) {
            orientationProperty().set(value);
        }
    }

    public final OrientationType getOrientation() {
        return this.orientation == null ? OrientationType.NONE : this.orientation.get();
    }

    public final ObjectProperty<OrientationType> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new SimpleObjectProperty(this, "orientation", DEFAULT_ORIENTATION);
        }
        return this.orientation;
    }

    public PathTransition(Duration duration, Shape path, Node node) {
        this.totalLength = 0.0d;
        this.segments = new ArrayList<>();
        setDuration(duration);
        setPath(path);
        setNode(node);
        setCycleDuration(duration);
    }

    public PathTransition(Duration duration, Shape path) {
        this(duration, path, null);
    }

    public PathTransition() {
        this(DEFAULT_DURATION, null, null);
    }

    @Override // javafx.animation.Transition
    public void interpolate(double frac) {
        double part = this.totalLength * Math.min(1.0d, Math.max(0.0d, frac));
        int segIdx = findSegment(0, this.segments.size() - 1, part);
        Segment seg = this.segments.get(segIdx);
        double lengthBefore = seg.accumLength - seg.length;
        double partLength = part - lengthBefore;
        double ratio = partLength / seg.length;
        Segment prevSeg = seg.prevSeg;
        double x2 = prevSeg.toX + ((seg.toX - prevSeg.toX) * ratio);
        double y2 = prevSeg.toY + ((seg.toY - prevSeg.toY) * ratio);
        double rotateAngle = seg.rotateAngle;
        double z2 = Math.min(10.0d, seg.length / 2.0d);
        if (partLength < z2 && !prevSeg.isMoveTo) {
            rotateAngle = interpolate(prevSeg.rotateAngle, seg.rotateAngle, ((partLength / z2) / 2.0d) + 0.5d);
        } else {
            double dist = seg.length - partLength;
            Segment nextSeg = seg.nextSeg;
            if (dist < z2 && nextSeg != null && !nextSeg.isMoveTo) {
                rotateAngle = interpolate(seg.rotateAngle, nextSeg.rotateAngle, ((z2 - dist) / z2) / 2.0d);
            }
        }
        this.cachedNode.setTranslateX(x2 - this.cachedNode.impl_getPivotX());
        this.cachedNode.setTranslateY(y2 - this.cachedNode.impl_getPivotY());
        if (this.cachedIsNormalRequired) {
            this.cachedNode.setRotate(rotateAngle);
        }
    }

    private Node getTargetNode() {
        Node node = getNode();
        return node != null ? node : getParentTargetNode();
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    boolean impl_startable(boolean forceSync) {
        return super.impl_startable(forceSync) && !((getTargetNode() == null || getPath() == null || getPath().getLayoutBounds().isEmpty()) && (forceSync || this.cachedNode == null));
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    void impl_sync(boolean forceSync) {
        super.impl_sync(forceSync);
        if (forceSync || this.cachedNode == null) {
            this.cachedNode = getTargetNode();
            recomputeSegments();
            this.cachedIsNormalRequired = getOrientation() == OrientationType.ORTHOGONAL_TO_TANGENT;
        }
    }

    private void recomputeSegments() {
        this.segments.clear();
        Shape p2 = getPath();
        Segment moveToSeg = Segment.getZeroSegment();
        Segment lastSeg = Segment.getZeroSegment();
        float[] coords = new float[6];
        PathIterator i2 = p2.impl_configShape().getPathIterator(p2.impl_getLeafTransform(), 1.0f);
        while (!i2.isDone()) {
            Segment newSeg = null;
            int segType = i2.currentSegment(coords);
            double x2 = coords[0];
            double y2 = coords[1];
            switch (segType) {
                case 0:
                    moveToSeg = Segment.newMoveTo(x2, y2, lastSeg.accumLength);
                    newSeg = moveToSeg;
                    break;
                case 1:
                    newSeg = Segment.newLineTo(lastSeg, x2, y2);
                    break;
                case 4:
                    newSeg = Segment.newClosePath(lastSeg, moveToSeg);
                    if (newSeg == null) {
                        lastSeg.convertToClosePath(moveToSeg);
                        break;
                    }
                    break;
            }
            if (newSeg != null) {
                this.segments.add(newSeg);
                lastSeg = newSeg;
            }
            i2.next();
        }
        this.totalLength = lastSeg.accumLength;
    }

    private int findSegment(int begin, int end, double length) {
        if (begin == end) {
            return (!this.segments.get(begin).isMoveTo || begin <= 0) ? begin : findSegment(begin - 1, begin - 1, length);
        }
        int middle = begin + ((end - begin) / 2);
        if (this.segments.get(middle).accumLength > length) {
            return findSegment(begin, middle, length);
        }
        return findSegment(middle + 1, end, length);
    }

    private static double interpolate(double fromAngle, double toAngle, double ratio) {
        double delta = toAngle - fromAngle;
        if (Math.abs(delta) > 180.0d) {
            toAngle += delta > 0.0d ? -360.0d : 360.0d;
        }
        return normalize(fromAngle + (ratio * (toAngle - fromAngle)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double normalize(double angle) {
        while (angle > 360.0d) {
            angle -= 360.0d;
        }
        while (angle < 0.0d) {
            angle += 360.0d;
        }
        return angle;
    }

    /* loaded from: jfxrt.jar:javafx/animation/PathTransition$Segment.class */
    private static class Segment {
        private static final Segment zeroSegment = new Segment(true, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
        boolean isMoveTo;
        double length;
        double accumLength;
        double toX;
        double toY;
        double rotateAngle;
        Segment prevSeg;
        Segment nextSeg;

        private Segment(boolean isMoveTo, double toX, double toY, double length, double lengthBefore, double rotateAngle) {
            this.isMoveTo = isMoveTo;
            this.toX = toX;
            this.toY = toY;
            this.length = length;
            this.accumLength = lengthBefore + length;
            this.rotateAngle = rotateAngle;
        }

        public static Segment getZeroSegment() {
            return zeroSegment;
        }

        public static Segment newMoveTo(double toX, double toY, double accumLength) {
            return new Segment(true, toX, toY, 0.0d, accumLength, 0.0d);
        }

        public static Segment newLineTo(Segment fromSeg, double toX, double toY) {
            double deltaX = toX - fromSeg.toX;
            double deltaY = toY - fromSeg.toY;
            double length = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
            if (length >= 1.0d || fromSeg.isMoveTo) {
                double sign = Math.signum(deltaY == 0.0d ? deltaX : deltaY);
                double angle = sign * Math.acos(deltaX / length);
                Segment newSeg = new Segment(false, toX, toY, length, fromSeg.accumLength, PathTransition.normalize((angle / 3.141592653589793d) * 180.0d));
                fromSeg.nextSeg = newSeg;
                newSeg.prevSeg = fromSeg;
                return newSeg;
            }
            return null;
        }

        public static Segment newClosePath(Segment fromSeg, Segment moveToSeg) {
            Segment newSeg = newLineTo(fromSeg, moveToSeg.toX, moveToSeg.toY);
            if (newSeg != null) {
                newSeg.convertToClosePath(moveToSeg);
            }
            return newSeg;
        }

        public void convertToClosePath(Segment moveToSeg) {
            Segment firstLineToSeg = moveToSeg.nextSeg;
            this.nextSeg = firstLineToSeg;
            firstLineToSeg.prevSeg = this;
        }
    }
}
