package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGBox;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/* loaded from: jfxrt.jar:javafx/scene/shape/Box.class */
public class Box extends Shape3D {
    private TriangleMesh mesh;
    public static final double DEFAULT_SIZE = 2.0d;
    private DoubleProperty depth;
    private DoubleProperty height;
    private DoubleProperty width;

    public Box() {
        this(2.0d, 2.0d, 2.0d);
    }

    public Box(double width, double height, double depth) {
        setWidth(width);
        setHeight(height);
        setDepth(depth);
    }

    public final void setDepth(double value) {
        depthProperty().set(value);
    }

    public final double getDepth() {
        if (this.depth == null) {
            return 2.0d;
        }
        return this.depth.get();
    }

    public final DoubleProperty depthProperty() {
        if (this.depth == null) {
            this.depth = new SimpleDoubleProperty(this, "depth", 2.0d) { // from class: javafx.scene.shape.Box.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Box.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Box.this.manager.invalidateBoxMesh(Box.this.key);
                    Box.this.key = 0;
                    Box.this.impl_geomChanged();
                }
            };
        }
        return this.depth;
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
            this.height = new SimpleDoubleProperty(this, MetadataParser.HEIGHT_TAG_NAME, 2.0d) { // from class: javafx.scene.shape.Box.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Box.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Box.this.manager.invalidateBoxMesh(Box.this.key);
                    Box.this.key = 0;
                    Box.this.impl_geomChanged();
                }
            };
        }
        return this.height;
    }

    public final void setWidth(double value) {
        widthProperty().set(value);
    }

    public final double getWidth() {
        if (this.width == null) {
            return 2.0d;
        }
        return this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new SimpleDoubleProperty(this, MetadataParser.WIDTH_TAG_NAME, 2.0d) { // from class: javafx.scene.shape.Box.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Box.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Box.this.manager.invalidateBoxMesh(Box.this.key);
                    Box.this.key = 0;
                    Box.this.impl_geomChanged();
                }
            };
        }
        return this.width;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGBox();
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.MESH_GEOM)) {
            NGBox peer = (NGBox) impl_getPeer();
            float w2 = (float) getWidth();
            float h2 = (float) getHeight();
            float d2 = (float) getDepth();
            if (w2 < 0.0f || h2 < 0.0f || d2 < 0.0f) {
                peer.updateMesh(null);
                return;
            }
            if (this.key == 0) {
                this.key = generateKey(w2, h2, d2);
            }
            this.mesh = this.manager.getBoxMesh(w2, h2, d2, this.key);
            this.mesh.impl_updatePG();
            peer.updateMesh(this.mesh.impl_getPGTriangleMesh());
        }
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        float w2 = (float) getWidth();
        float h2 = (float) getHeight();
        float d2 = (float) getDepth();
        if (w2 < 0.0f || h2 < 0.0f || d2 < 0.0f) {
            return bounds.makeEmpty();
        }
        float hw = w2 * 0.5f;
        float hh = h2 * 0.5f;
        float hd = d2 * 0.5f;
        BaseBounds bounds2 = bounds.deriveWithNewBounds(-hw, -hh, -hd, hw, hh, hd);
        return tx.transform(bounds2, bounds2);
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        double w2 = getWidth();
        double h2 = getHeight();
        return (-w2) <= localX && localX <= w2 && (-h2) <= localY && localY <= h2;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResult) {
        Point2D txtCoords;
        double w2 = getWidth();
        double h2 = getHeight();
        double d2 = getDepth();
        double hWidth = w2 / 2.0d;
        double hHeight = h2 / 2.0d;
        double hDepth = d2 / 2.0d;
        Vec3d dir = pickRay.getDirectionNoClone();
        double invDirX = dir.f11930x == 0.0d ? Double.POSITIVE_INFINITY : 1.0d / dir.f11930x;
        double invDirY = dir.f11931y == 0.0d ? Double.POSITIVE_INFINITY : 1.0d / dir.f11931y;
        double invDirZ = dir.f11932z == 0.0d ? Double.POSITIVE_INFINITY : 1.0d / dir.f11932z;
        Vec3d origin = pickRay.getOriginNoClone();
        double originX = origin.f11930x;
        double originY = origin.f11931y;
        double originZ = origin.f11932z;
        boolean signX = invDirX < 0.0d;
        boolean signY = invDirY < 0.0d;
        boolean signZ = invDirZ < 0.0d;
        double t0 = Double.NEGATIVE_INFINITY;
        double t1 = Double.POSITIVE_INFINITY;
        char side0 = '0';
        char side1 = '0';
        if (!Double.isInfinite(invDirX)) {
            t0 = ((signX ? hWidth : -hWidth) - originX) * invDirX;
            t1 = ((signX ? -hWidth : hWidth) - originX) * invDirX;
            side0 = signX ? 'X' : 'x';
            side1 = signX ? 'x' : 'X';
        } else if ((-hWidth) > originX || hWidth < originX) {
            return false;
        }
        if (!Double.isInfinite(invDirY)) {
            double ty0 = ((signY ? hHeight : -hHeight) - originY) * invDirY;
            double ty1 = ((signY ? -hHeight : hHeight) - originY) * invDirY;
            if (t0 > ty1 || ty0 > t1) {
                return false;
            }
            if (ty0 > t0) {
                side0 = signY ? 'Y' : 'y';
                t0 = ty0;
            }
            if (ty1 < t1) {
                side1 = signY ? 'y' : 'Y';
                t1 = ty1;
            }
        } else if ((-hHeight) > originY || hHeight < originY) {
            return false;
        }
        if (!Double.isInfinite(invDirZ)) {
            double tz0 = ((signZ ? hDepth : -hDepth) - originZ) * invDirZ;
            double tz1 = ((signZ ? -hDepth : hDepth) - originZ) * invDirZ;
            if (t0 > tz1 || tz0 > t1) {
                return false;
            }
            if (tz0 > t0) {
                side0 = signZ ? 'Z' : 'z';
                t0 = tz0;
            }
            if (tz1 < t1) {
                side1 = signZ ? 'z' : 'Z';
                t1 = tz1;
            }
        } else if ((-hDepth) > originZ || hDepth < originZ) {
            return false;
        }
        char side = side0;
        double t2 = t0;
        CullFace cullFace = getCullFace();
        double minDistance = pickRay.getNearClip();
        double maxDistance = pickRay.getFarClip();
        if (t0 > maxDistance) {
            return false;
        }
        if (t0 < minDistance || cullFace == CullFace.FRONT) {
            if (t1 >= minDistance && t1 <= maxDistance && cullFace != CullFace.BACK) {
                side = side1;
                t2 = t1;
            } else {
                return false;
            }
        }
        if (Double.isInfinite(t2) || Double.isNaN(t2)) {
            return false;
        }
        if (pickResult != null && pickResult.isCloser(t2)) {
            Point3D point = PickResultChooser.computePoint(pickRay, t2);
            switch (side) {
                case 'X':
                    txtCoords = new Point2D(0.5d + (point.getZ() / d2), 0.5d + (point.getY() / h2));
                    break;
                case 'Y':
                    txtCoords = new Point2D(0.5d + (point.getX() / w2), 0.5d + (point.getZ() / d2));
                    break;
                case 'Z':
                    txtCoords = new Point2D(0.5d - (point.getX() / w2), 0.5d + (point.getY() / h2));
                    break;
                case 'x':
                    txtCoords = new Point2D(0.5d - (point.getZ() / d2), 0.5d + (point.getY() / h2));
                    break;
                case 'y':
                    txtCoords = new Point2D(0.5d + (point.getX() / w2), 0.5d - (point.getZ() / d2));
                    break;
                case 'z':
                    txtCoords = new Point2D(0.5d + (point.getX() / w2), 0.5d + (point.getY() / h2));
                    break;
                default:
                    return false;
            }
            pickResult.offer(this, t2, -1, point, txtCoords);
            return true;
        }
        return true;
    }

    static TriangleMesh createMesh(float w2, float h2, float d2) {
        float hw = w2 / 2.0f;
        float hh = h2 / 2.0f;
        float hd = d2 / 2.0f;
        float[] points = {-hw, -hh, -hd, hw, -hh, -hd, hw, hh, -hd, -hw, hh, -hd, -hw, -hh, hd, hw, -hh, hd, hw, hh, hd, -hw, hh, hd};
        float[] texCoords = {0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
        int[] faceSmoothingGroups = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] faces = {0, 0, 2, 2, 1, 1, 2, 2, 0, 0, 3, 3, 1, 0, 6, 2, 5, 1, 6, 2, 1, 0, 2, 3, 5, 0, 7, 2, 4, 1, 7, 2, 5, 0, 6, 3, 4, 0, 3, 2, 0, 1, 3, 2, 4, 0, 7, 3, 3, 0, 6, 2, 2, 1, 6, 2, 3, 0, 7, 3, 4, 0, 1, 2, 5, 1, 1, 2, 4, 0, 0, 3};
        TriangleMesh mesh = new TriangleMesh(true);
        mesh.getPoints().setAll(points);
        mesh.getTexCoords().setAll(texCoords);
        mesh.getFaces().setAll(faces);
        mesh.getFaceSmoothingGroups().setAll(faceSmoothingGroups);
        return mesh;
    }

    private static int generateKey(float w2, float h2, float d2) {
        int hash = (97 * 3) + Float.floatToIntBits(w2);
        return (97 * ((97 * hash) + Float.floatToIntBits(h2))) + Float.floatToIntBits(d2);
    }
}
