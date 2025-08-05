package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGSphere;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

/* loaded from: jfxrt.jar:javafx/scene/shape/Sphere.class */
public class Sphere extends Shape3D {
    static final int DEFAULT_DIVISIONS = 64;
    static final double DEFAULT_RADIUS = 1.0d;
    private int divisions;
    private TriangleMesh mesh;
    private DoubleProperty radius;

    public Sphere() {
        this(1.0d, 64);
    }

    public Sphere(double radius) {
        this(radius, 64);
    }

    public Sphere(double radius, int divisions) {
        this.divisions = 64;
        this.divisions = divisions < 1 ? 1 : divisions;
        setRadius(radius);
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
            this.radius = new SimpleDoubleProperty(this, "radius", 1.0d) { // from class: javafx.scene.shape.Sphere.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Sphere.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Sphere.this.manager.invalidateSphereMesh(Sphere.this.key);
                    Sphere.this.key = 0;
                    Sphere.this.impl_geomChanged();
                }
            };
        }
        return this.radius;
    }

    public int getDivisions() {
        return this.divisions;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGSphere();
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.MESH_GEOM)) {
            NGSphere pgSphere = (NGSphere) impl_getPeer();
            float r2 = (float) getRadius();
            if (r2 < 0.0f) {
                pgSphere.updateMesh(null);
                return;
            }
            if (this.key == 0) {
                this.key = generateKey(r2, this.divisions);
            }
            this.mesh = this.manager.getSphereMesh(r2, this.divisions, this.key);
            this.mesh.impl_updatePG();
            pgSphere.updateMesh(this.mesh.impl_getPGTriangleMesh());
        }
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        float r2 = (float) getRadius();
        if (r2 < 0.0f) {
            return bounds.makeEmpty();
        }
        BaseBounds bounds2 = bounds.deriveWithNewBounds(-r2, -r2, -r2, r2, r2, r2);
        return tx.transform(bounds2, bounds2);
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        double r2 = getRadius();
        double n2 = (localX * localX) + (localY * localY);
        return n2 <= r2 * r2;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResult) {
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
        double a2 = (dirX * dirX) + (dirY * dirY) + (dirZ * dirZ);
        double b2 = 2.0d * ((dirX * originX) + (dirY * originY) + (dirZ * originZ));
        double c2 = (((originX * originX) + (originY * originY)) + (originZ * originZ)) - (r2 * r2);
        double discriminant = (b2 * b2) - ((4.0d * a2) * c2);
        if (discriminant < 0.0d) {
            return false;
        }
        double distSqrt = Math.sqrt(discriminant);
        double q2 = b2 < 0.0d ? ((-b2) - distSqrt) / 2.0d : ((-b2) + distSqrt) / 2.0d;
        double t0 = q2 / a2;
        double t1 = c2 / q2;
        if (t0 > t1) {
            t0 = t1;
            t1 = t0;
        }
        double minDistance = pickRay.getNearClip();
        double maxDistance = pickRay.getFarClip();
        if (t1 < minDistance || t0 > maxDistance) {
            return false;
        }
        double t2 = t0;
        CullFace cullFace = getCullFace();
        if (t0 < minDistance || cullFace == CullFace.FRONT) {
            if (t1 <= maxDistance && getCullFace() != CullFace.BACK) {
                t2 = t1;
            } else if (!exactPicking) {
                return false;
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
            Point3D proj = new Point3D(point.getX(), 0.0d, point.getZ());
            Point3D cross = proj.crossProduct(Rotate.Z_AXIS);
            double angle = proj.angle(Rotate.Z_AXIS);
            if (cross.getY() > 0.0d) {
                angle = 360.0d - angle;
            }
            Point2D txtCoords = new Point2D(1.0d - (angle / 360.0d), 0.5d + (point.getY() / (2.0d * r2)));
            pickResult.offer(this, t2, -1, point, txtCoords);
            return true;
        }
        return true;
    }

    private static int correctDivisions(int div) {
        return ((div + 3) / 4) * 4;
    }

    static TriangleMesh createMesh(int div, float r2) {
        int div2 = correctDivisions(div);
        int div22 = div2 / 2;
        int nPoints = (div2 * (div22 - 1)) + 2;
        int nTPoints = ((div2 + 1) * (div22 - 1)) + (div2 * 2);
        int nFaces = (div2 * (div22 - 2) * 2) + (div2 * 2);
        float rDiv = 1.0f / div2;
        float[] points = new float[nPoints * 3];
        float[] tPoints = new float[nTPoints * 2];
        int[] faces = new int[nFaces * 6];
        int pPos = 0;
        int tPos = 0;
        for (int y2 = 0; y2 < div22 - 1; y2++) {
            float va = rDiv * ((y2 + 1) - (div22 / 2)) * 2.0f * 3.1415927f;
            float sin_va = (float) Math.sin(va);
            float cos_va = (float) Math.cos(va);
            float ty = 0.5f + (sin_va * 0.5f);
            for (int i2 = 0; i2 < div2; i2++) {
                double a2 = rDiv * i2 * 2.0f * 3.1415927f;
                float hSin = (float) Math.sin(a2);
                float hCos = (float) Math.cos(a2);
                points[pPos + 0] = hSin * cos_va * r2;
                points[pPos + 2] = hCos * cos_va * r2;
                points[pPos + 1] = sin_va * r2;
                tPoints[tPos + 0] = 1.0f - (rDiv * i2);
                tPoints[tPos + 1] = ty;
                pPos += 3;
                tPos += 2;
            }
            tPoints[tPos + 0] = 0.0f;
            tPoints[tPos + 1] = ty;
            tPos += 2;
        }
        points[pPos + 0] = 0.0f;
        points[pPos + 1] = -r2;
        points[pPos + 2] = 0.0f;
        points[pPos + 3] = 0.0f;
        points[pPos + 4] = r2;
        points[pPos + 5] = 0.0f;
        int i3 = pPos + 6;
        int pS = (div22 - 1) * div2;
        for (int i4 = 0; i4 < div2; i4++) {
            tPoints[tPos + 0] = 1.0f - (rDiv * (0.5f + i4));
            tPoints[tPos + 1] = 0.00390625f;
            tPos += 2;
        }
        for (int i5 = 0; i5 < div2; i5++) {
            tPoints[tPos + 0] = 1.0f - (rDiv * (0.5f + i5));
            tPoints[tPos + 1] = 1.0f - 0.00390625f;
            tPos += 2;
        }
        int fIndex = 0;
        for (int y3 = 0; y3 < div22 - 2; y3++) {
            for (int x2 = 0; x2 < div2; x2++) {
                int p0 = (y3 * div2) + x2;
                int p1 = p0 + 1;
                int p2 = p0 + div2;
                int p3 = p1 + div2;
                int t0 = p0 + y3;
                int t1 = t0 + 1;
                int t2 = t0 + div2 + 1;
                int t3 = t1 + div2 + 1;
                faces[fIndex + 0] = p0;
                faces[fIndex + 1] = t0;
                faces[fIndex + 2] = p1 % div2 == 0 ? p1 - div2 : p1;
                faces[fIndex + 3] = t1;
                faces[fIndex + 4] = p2;
                faces[fIndex + 5] = t2;
                int fIndex2 = fIndex + 6;
                faces[fIndex2 + 0] = p3 % div2 == 0 ? p3 - div2 : p3;
                faces[fIndex2 + 1] = t3;
                faces[fIndex2 + 2] = p2;
                faces[fIndex2 + 3] = t2;
                faces[fIndex2 + 4] = p1 % div2 == 0 ? p1 - div2 : p1;
                faces[fIndex2 + 5] = t1;
                fIndex = fIndex2 + 6;
            }
        }
        int tB = (div22 - 1) * (div2 + 1);
        for (int x3 = 0; x3 < div2; x3++) {
            int p22 = x3;
            int p12 = x3 + 1;
            int t02 = tB + x3;
            faces[fIndex + 0] = pS;
            faces[fIndex + 1] = t02;
            faces[fIndex + 2] = p12 == div2 ? 0 : p12;
            faces[fIndex + 3] = p12;
            faces[fIndex + 4] = p22;
            faces[fIndex + 5] = p22;
            fIndex += 6;
        }
        int p02 = pS + 1;
        int tB2 = tB + div2;
        int pB = (div22 - 2) * div2;
        for (int x4 = 0; x4 < div2; x4++) {
            int p13 = pB + x4;
            int p23 = pB + x4 + 1;
            int t03 = tB2 + x4;
            int t12 = ((div22 - 2) * (div2 + 1)) + x4;
            int t22 = t12 + 1;
            faces[fIndex + 0] = p02;
            faces[fIndex + 1] = t03;
            faces[fIndex + 2] = p13;
            faces[fIndex + 3] = t12;
            faces[fIndex + 4] = p23 % div2 == 0 ? p23 - div2 : p23;
            faces[fIndex + 5] = t22;
            fIndex += 6;
        }
        TriangleMesh m2 = new TriangleMesh(true);
        m2.getPoints().setAll(points);
        m2.getTexCoords().setAll(tPoints);
        m2.getFaces().setAll(faces);
        return m2;
    }

    private static int generateKey(float r2, int div) {
        int hash = (23 * 5) + Float.floatToIntBits(r2);
        return (23 * hash) + div;
    }
}
