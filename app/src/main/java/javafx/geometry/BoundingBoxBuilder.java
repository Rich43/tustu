package javafx.geometry;

import javafx.geometry.BoundingBoxBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/geometry/BoundingBoxBuilder.class */
public class BoundingBoxBuilder<B extends BoundingBoxBuilder<B>> implements Builder<BoundingBox> {
    private double depth;
    private double height;
    private double minX;
    private double minY;
    private double minZ;
    private double width;

    protected BoundingBoxBuilder() {
    }

    public static BoundingBoxBuilder<?> create() {
        return new BoundingBoxBuilder<>();
    }

    public B depth(double x2) {
        this.depth = x2;
        return this;
    }

    public B height(double x2) {
        this.height = x2;
        return this;
    }

    public B minX(double x2) {
        this.minX = x2;
        return this;
    }

    public B minY(double x2) {
        this.minY = x2;
        return this;
    }

    public B minZ(double x2) {
        this.minZ = x2;
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public BoundingBox build() {
        BoundingBox x2 = new BoundingBox(this.minX, this.minY, this.minZ, this.width, this.height, this.depth);
        return x2;
    }
}
