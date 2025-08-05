package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGCylinder;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

/* loaded from: jfxrt.jar:javafx/scene/shape/Cylinder.class */
public class Cylinder extends Shape3D {
    static final int DEFAULT_DIVISIONS = 64;
    static final double DEFAULT_RADIUS = 1.0d;
    static final double DEFAULT_HEIGHT = 2.0d;
    private int divisions;
    private TriangleMesh mesh;
    private DoubleProperty height;
    private DoubleProperty radius;

    public Cylinder() {
        this(1.0d, 2.0d, 64);
    }

    public Cylinder(double radius, double height) {
        this(radius, height, 64);
    }

    public Cylinder(double radius, double height, int divisions) {
        this.divisions = 64;
        this.divisions = divisions < 3 ? 3 : divisions;
        setRadius(radius);
        setHeight(height);
    }

    public final void setHeight(double value) {
        heightProperty().set(value);
    }

    public final double getHeight() {
        if (this.height == null) {
            return 2.0d;
        }
        return this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new SimpleDoubleProperty(this, MetadataParser.HEIGHT_TAG_NAME, 2.0d) { // from class: javafx.scene.shape.Cylinder.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Cylinder.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Cylinder.this.manager.invalidateCylinderMesh(Cylinder.this.key);
                    Cylinder.this.key = 0;
                    Cylinder.this.impl_geomChanged();
                }
            };
        }
        return this.height;
    }

    public final void setRadius(double value) {
        radiusProperty().set(value);
    }

    public final double getRadius() {
        if (this.radius == null) {
            return 1.0d;
        }
        return this.radius.get();
    }

    public final DoubleProperty radiusProperty() {
        if (this.radius == null) {
            this.radius = new SimpleDoubleProperty(this, "radius", 1.0d) { // from class: javafx.scene.shape.Cylinder.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Cylinder.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Cylinder.this.manager.invalidateCylinderMesh(Cylinder.this.key);
                    Cylinder.this.key = 0;
                    Cylinder.this.impl_geomChanged();
                }
            };
        }
        return this.radius;
    }

    public int getDivisions() {
        return this.divisions;
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.MESH_GEOM)) {
            NGCylinder peer = (NGCylinder) impl_getPeer();
            float h2 = (float) getHeight();
            float r2 = (float) getRadius();
            if (h2 < 0.0f || r2 < 0.0f) {
                peer.updateMesh(null);
                return;
            }
            if (this.key == 0) {
                this.key = generateKey(h2, r2, this.divisions);
            }
            this.mesh = this.manager.getCylinderMesh(h2, r2, this.divisions, this.key);
            this.mesh.impl_updatePG();
            peer.updateMesh(this.mesh.impl_getPGTriangleMesh());
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGCylinder();
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        float h2 = (float) getHeight();
        float r2 = (float) getRadius();
        if (r2 < 0.0f || h2 < 0.0f) {
            return bounds.makeEmpty();
        }
        float hh = h2 * 0.5f;
        BaseBounds bounds2 = bounds.deriveWithNewBounds(-r2, -hh, -r2, r2, hh, r2);
        return tx.transform(bounds2, bounds2);
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        double w2 = getRadius();
        double hh = getHeight() * 0.5d;
        return (-w2) <= localX && localX <= w2 && (-hh) <= localY && localY <= hh;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResult) {
        double t0;
        double t1;
        Point2D txCoords;
        boolean exactPicking = this.divisions < 64 && this.mesh != null;
        double r2 = getRadius();
        Vec3d dir = pickRay.getDirectionNoClone();
        double dirX = dir.f11930x;
        double dirY = dir.f11931y;
        double dirZ = dir.f11932z;
        Vec3d origin = pickRay.getOriginNoClone();
        double originX = origin.f11930x;
        double originY = origin.f11931y;
        double originZ = origin.f11932z;
        double h2 = getHeight();
        double halfHeight = h2 / 2.0d;
        CullFace cullFace = getCullFace();
        double a2 = (dirX * dirX) + (dirZ * dirZ);
        double b2 = 2.0d * ((dirX * originX) + (dirZ * originZ));
        double c2 = ((originX * originX) + (originZ * originZ)) - (r2 * r2);
        double discriminant = (b2 * b2) - ((4.0d * a2) * c2);
        double t2 = Double.POSITIVE_INFINITY;
        double minDistance = pickRay.getNearClip();
        double maxDistance = pickRay.getFarClip();
        if (discriminant >= 0.0d && (dirX != 0.0d || dirZ != 0.0d)) {
            double distSqrt = Math.sqrt(discriminant);
            double q2 = b2 < 0.0d ? ((-b2) - distSqrt) / 2.0d : ((-b2) + distSqrt) / 2.0d;
            double t02 = q2 / a2;
            double t12 = c2 / q2;
            if (t02 > t12) {
                t02 = t12;
                t12 = t02;
            }
            double y0 = originY + (t02 * dirY);
            if (t02 < minDistance || y0 < (-halfHeight) || y0 > halfHeight || cullFace == CullFace.FRONT) {
                double y1 = originY + (t12 * dirY);
                if (t12 >= minDistance && t12 <= maxDistance && y1 >= (-halfHeight) && y1 <= halfHeight && (cullFace != CullFace.BACK || exactPicking)) {
                    t2 = t12;
                }
            } else if (t02 <= maxDistance) {
                t2 = t02;
            }
        }
        boolean topCap = false;
        boolean bottomCap = false;
        if (t2 == Double.POSITIVE_INFINITY || !exactPicking) {
            double tBottom = ((-halfHeight) - originY) / dirY;
            double tTop = (halfHeight - originY) / dirY;
            boolean isT0Bottom = false;
            if (tBottom < tTop) {
                t0 = tBottom;
                t1 = tTop;
                isT0Bottom = true;
            } else {
                t0 = tTop;
                t1 = tBottom;
            }
            if (t0 >= minDistance && t0 <= maxDistance && t0 < t2 && cullFace != CullFace.FRONT) {
                double tX = originX + (dirX * t0);
                double tZ = originZ + (dirZ * t0);
                if ((tX * tX) + (tZ * tZ) <= r2 * r2) {
                    bottomCap = isT0Bottom;
                    topCap = !isT0Bottom;
                    t2 = t0;
                }
            }
            if (t1 >= minDistance && t1 <= maxDistance && t1 < t2 && (cullFace != CullFace.BACK || exactPicking)) {
                double tX2 = originX + (dirX * t1);
                double tZ2 = originZ + (dirZ * t1);
                if ((tX2 * tX2) + (tZ2 * tZ2) <= r2 * r2) {
                    topCap = isT0Bottom;
                    bottomCap = !isT0Bottom;
                    t2 = t1;
                }
            }
        }
        if (Double.isInfinite(t2) || Double.isNaN(t2)) {
            return false;
        }
        if (exactPicking) {
            return this.mesh.impl_computeIntersects(pickRay, pickResult, this, cullFace, false);
        }
        if (pickResult != null && pickResult.isCloser(t2)) {
            Point3D point = PickResultChooser.computePoint(pickRay, t2);
            if (topCap) {
                txCoords = new Point2D(0.5d + (point.getX() / (2.0d * r2)), 0.5d + (point.getZ() / (2.0d * r2)));
            } else if (bottomCap) {
                txCoords = new Point2D(0.5d + (point.getX() / (2.0d * r2)), 0.5d - (point.getZ() / (2.0d * r2)));
            } else {
                Point3D proj = new Point3D(point.getX(), 0.0d, point.getZ());
                Point3D cross = proj.crossProduct(Rotate.Z_AXIS);
                double angle = proj.angle(Rotate.Z_AXIS);
                if (cross.getY() > 0.0d) {
                    angle = 360.0d - angle;
                }
                txCoords = new Point2D(1.0d - (angle / 360.0d), 0.5d + (point.getY() / h2));
            }
            pickResult.offer(this, t2, -1, point, txCoords);
            return true;
        }
        return true;
    }

    static TriangleMesh createMesh(int div, float h2, float r2) {
        int nPonits = (div * 2) + 2;
        int tcCount = ((div + 1) * 4) + 1;
        int faceCount = div * 4;
        float dA = 1.0f / div;
        float h3 = h2 * 0.5f;
        float[] points = new float[nPonits * 3];
        float[] tPoints = new float[tcCount * 2];
        int[] faces = new int[faceCount * 6];
        int[] smoothing = new int[faceCount];
        int pPos = 0;
        int tPos = 0;
        for (int i2 = 0; i2 < div; i2++) {
            double a2 = dA * i2 * 2.0f * 3.141592653589793d;
            points[pPos + 0] = (float) (Math.sin(a2) * r2);
            points[pPos + 2] = (float) (Math.cos(a2) * r2);
            points[pPos + 1] = h3;
            tPoints[tPos + 0] = 1.0f - (dA * i2);
            tPoints[tPos + 1] = 1.0f - 0.00390625f;
            pPos += 3;
            tPos += 2;
        }
        tPoints[tPos + 0] = 0.0f;
        tPoints[tPos + 1] = 1.0f - 0.00390625f;
        int tPos2 = tPos + 2;
        for (int i3 = 0; i3 < div; i3++) {
            double a3 = dA * i3 * 2.0f * 3.141592653589793d;
            points[pPos + 0] = (float) (Math.sin(a3) * r2);
            points[pPos + 2] = (float) (Math.cos(a3) * r2);
            points[pPos + 1] = -h3;
            tPoints[tPos2 + 0] = 1.0f - (dA * i3);
            tPoints[tPos2 + 1] = 0.00390625f;
            pPos += 3;
            tPos2 += 2;
        }
        tPoints[tPos2 + 0] = 0.0f;
        tPoints[tPos2 + 1] = 0.00390625f;
        int tPos3 = tPos2 + 2;
        points[pPos + 0] = 0.0f;
        points[pPos + 1] = h3;
        points[pPos + 2] = 0.0f;
        points[pPos + 3] = 0.0f;
        points[pPos + 4] = -h3;
        points[pPos + 5] = 0.0f;
        int i4 = pPos + 6;
        int i5 = 0;
        while (i5 <= div) {
            double a4 = i5 < div ? dA * i5 * 2.0f * 3.141592653589793d : 0.0d;
            tPoints[tPos3 + 0] = ((float) (Math.sin(a4) * 0.5d)) + 0.5f;
            tPoints[tPos3 + 1] = ((float) (Math.cos(a4) * 0.5d)) + 0.5f;
            tPos3 += 2;
            i5++;
        }
        int i6 = 0;
        while (i6 <= div) {
            double a5 = i6 < div ? dA * i6 * 2.0f * 3.141592653589793d : 0.0d;
            tPoints[tPos3 + 0] = 0.5f + ((float) (Math.sin(a5) * 0.5d));
            tPoints[tPos3 + 1] = 0.5f - ((float) (Math.cos(a5) * 0.5d));
            tPos3 += 2;
            i6++;
        }
        tPoints[tPos3 + 0] = 0.5f;
        tPoints[tPos3 + 1] = 0.5f;
        int i7 = tPos3 + 2;
        int fIndex = 0;
        for (int p0 = 0; p0 < div; p0++) {
            int p1 = p0 + 1;
            int p2 = p0 + div;
            int p3 = p1 + div;
            faces[fIndex + 0] = p0;
            faces[fIndex + 1] = p0;
            faces[fIndex + 2] = p2;
            faces[fIndex + 3] = p2 + 1;
            faces[fIndex + 4] = p1 == div ? 0 : p1;
            faces[fIndex + 5] = p1;
            int fIndex2 = fIndex + 6;
            faces[fIndex2 + 0] = p3 % div == 0 ? p3 - div : p3;
            faces[fIndex2 + 1] = p3 + 1;
            faces[fIndex2 + 2] = p1 == div ? 0 : p1;
            faces[fIndex2 + 3] = p1;
            faces[fIndex2 + 4] = p2;
            faces[fIndex2 + 5] = p2 + 1;
            fIndex = fIndex2 + 6;
        }
        int tStart = (div + 1) * 2;
        int t1 = (div + 1) * 4;
        int p12 = div * 2;
        for (int p02 = 0; p02 < div; p02++) {
            int p22 = p02 + 1;
            int t0 = tStart + p02;
            int t2 = t0 + 1;
            faces[fIndex + 0] = p02;
            faces[fIndex + 1] = t0;
            faces[fIndex + 2] = p22 == div ? 0 : p22;
            faces[fIndex + 3] = t2;
            faces[fIndex + 4] = p12;
            faces[fIndex + 5] = t1;
            fIndex += 6;
        }
        int p13 = (div * 2) + 1;
        int tStart2 = (div + 1) * 3;
        for (int p03 = 0; p03 < div; p03++) {
            int p23 = p03 + 1 + div;
            int t02 = tStart2 + p03;
            int t22 = t02 + 1;
            faces[fIndex + 0] = p03 + div;
            faces[fIndex + 1] = t02;
            faces[fIndex + 2] = p13;
            faces[fIndex + 3] = t1;
            faces[fIndex + 4] = p23 % div == 0 ? p23 - div : p23;
            faces[fIndex + 5] = t22;
            fIndex += 6;
        }
        for (int i8 = 0; i8 < div * 2; i8++) {
            smoothing[i8] = 1;
        }
        for (int i9 = div * 2; i9 < div * 4; i9++) {
            smoothing[i9] = 2;
        }
        TriangleMesh m2 = new TriangleMesh(true);
        m2.getPoints().setAll(points);
        m2.getTexCoords().setAll(tPoints);
        m2.getFaces().setAll(faces);
        m2.getFaceSmoothingGroups().setAll(smoothing);
        return m2;
    }

    private static int generateKey(float h2, float r2, int div) {
        int hash = (47 * 7) + Float.floatToIntBits(h2);
        return (47 * ((47 * hash) + Float.floatToIntBits(r2))) + div;
    }
}
