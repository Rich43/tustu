package java.awt.geom;

import java.awt.Shape;
import java.awt.geom.Path2D;

/* loaded from: rt.jar:java/awt/geom/GeneralPath.class */
public final class GeneralPath extends Path2D.Float {
    private static final long serialVersionUID = -8327096662768731142L;

    public GeneralPath() {
        super(1, 20);
    }

    public GeneralPath(int i2) {
        super(i2, 20);
    }

    public GeneralPath(int i2, int i3) {
        super(i2, i3);
    }

    public GeneralPath(Shape shape) {
        super(shape, (AffineTransform) null);
    }

    GeneralPath(int i2, byte[] bArr, int i3, float[] fArr, int i4) {
        this.windingRule = i2;
        this.pointTypes = bArr;
        this.numTypes = i3;
        this.floatCoords = fArr;
        this.numCoords = i4;
    }
}
