package javafx.scene.shape;

import com.sun.javafx.collections.FloatArraySyncer;
import com.sun.javafx.collections.IntegerArraySyncer;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.shape.ObservableFaceArrayImpl;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ArrayChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/shape/TriangleMesh.class */
public class TriangleMesh extends Mesh {
    private final ObservableFloatArray points;
    private final ObservableFloatArray normals;
    private final ObservableFloatArray texCoords;
    private final ObservableFaceArray faces;
    private final ObservableIntegerArray faceSmoothingGroups;
    private final Listener pointsSyncer;
    private final Listener normalsSyncer;
    private final Listener texCoordsSyncer;
    private final Listener facesSyncer;
    private final Listener faceSmoothingGroupsSyncer;
    private final boolean isPredefinedShape;
    private boolean isValidDirty;
    private boolean isPointsValid;
    private boolean isNormalsValid;
    private boolean isTexCoordsValid;
    private boolean isFacesValid;
    private boolean isFaceSmoothingGroupValid;
    private int refCount;
    private BaseBounds cachedBounds;
    private ObjectProperty<VertexFormat> vertexFormat;
    private NGTriangleMesh peer;

    public TriangleMesh() {
        this(false);
    }

    public TriangleMesh(VertexFormat vertexFormat) {
        this(false);
        setVertexFormat(vertexFormat);
    }

    TriangleMesh(boolean isPredefinedShape) {
        this.points = FXCollections.observableFloatArray();
        this.normals = FXCollections.observableFloatArray();
        this.texCoords = FXCollections.observableFloatArray();
        this.faces = new ObservableFaceArrayImpl();
        this.faceSmoothingGroups = FXCollections.observableIntegerArray();
        this.pointsSyncer = new Listener(this.points);
        this.normalsSyncer = new Listener(this.normals);
        this.texCoordsSyncer = new Listener(this.texCoords);
        this.facesSyncer = new Listener(this.faces);
        this.faceSmoothingGroupsSyncer = new Listener(this.faceSmoothingGroups);
        this.isValidDirty = true;
        this.refCount = 1;
        this.isPredefinedShape = isPredefinedShape;
        if (isPredefinedShape) {
            this.isPointsValid = true;
            this.isNormalsValid = true;
            this.isTexCoordsValid = true;
            this.isFacesValid = true;
            this.isFaceSmoothingGroupValid = true;
            return;
        }
        this.isPointsValid = false;
        this.isNormalsValid = false;
        this.isTexCoordsValid = false;
        this.isFacesValid = false;
        this.isFaceSmoothingGroupValid = false;
    }

    public final void setVertexFormat(VertexFormat value) {
        vertexFormatProperty().set(value);
    }

    public final VertexFormat getVertexFormat() {
        return this.vertexFormat == null ? VertexFormat.POINT_TEXCOORD : this.vertexFormat.get();
    }

    public final ObjectProperty<VertexFormat> vertexFormatProperty() {
        if (this.vertexFormat == null) {
            this.vertexFormat = new SimpleObjectProperty<VertexFormat>(this, "vertexFormat") { // from class: javafx.scene.shape.TriangleMesh.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TriangleMesh.this.setDirty(true);
                    TriangleMesh.this.facesSyncer.setDirty(true);
                    TriangleMesh.this.faceSmoothingGroupsSyncer.setDirty(true);
                }
            };
        }
        return this.vertexFormat;
    }

    public final int getPointElementSize() {
        return getVertexFormat().getPointElementSize();
    }

    public final int getNormalElementSize() {
        return getVertexFormat().getNormalElementSize();
    }

    public final int getTexCoordElementSize() {
        return getVertexFormat().getTexCoordElementSize();
    }

    public final int getFaceElementSize() {
        return getVertexFormat().getVertexIndexSize() * 3;
    }

    public final ObservableFloatArray getPoints() {
        return this.points;
    }

    public final ObservableFloatArray getNormals() {
        return this.normals;
    }

    public final ObservableFloatArray getTexCoords() {
        return this.texCoords;
    }

    public final ObservableFaceArray getFaces() {
        return this.faces;
    }

    public final ObservableIntegerArray getFaceSmoothingGroups() {
        return this.faceSmoothingGroups;
    }

    @Override // javafx.scene.shape.Mesh
    void setDirty(boolean value) {
        super.setDirty(value);
        if (!value) {
            this.pointsSyncer.setDirty(false);
            this.normalsSyncer.setDirty(false);
            this.texCoordsSyncer.setDirty(false);
            this.facesSyncer.setDirty(false);
            this.faceSmoothingGroupsSyncer.setDirty(false);
        }
    }

    int getRefCount() {
        return this.refCount;
    }

    synchronized void incRef() {
        this.refCount++;
    }

    synchronized void decRef() {
        this.refCount--;
    }

    @Deprecated
    NGTriangleMesh impl_getPGTriangleMesh() {
        if (this.peer == null) {
            this.peer = new NGTriangleMesh();
        }
        return this.peer;
    }

    @Override // javafx.scene.shape.Mesh
    NGTriangleMesh getPGMesh() {
        return impl_getPGTriangleMesh();
    }

    private boolean validatePoints() {
        if (this.points.size() == 0) {
            return false;
        }
        if (this.points.size() % getVertexFormat().getPointElementSize() != 0) {
            String logname = TriangleMesh.class.getName();
            PlatformLogger.getLogger(logname).warning("points.size() has to be divisible by getPointElementSize(). It is to store multiple x, y, and z coordinates of this mesh");
            return false;
        }
        return true;
    }

    private boolean validateNormals() {
        if (getVertexFormat() != VertexFormat.POINT_NORMAL_TEXCOORD) {
            return true;
        }
        if (this.normals.size() == 0) {
            return false;
        }
        if (this.normals.size() % getVertexFormat().getNormalElementSize() != 0) {
            String logname = TriangleMesh.class.getName();
            PlatformLogger.getLogger(logname).warning("normals.size() has to be divisible by getNormalElementSize(). It is to store multiple nx, ny, and nz coordinates of this mesh");
            return false;
        }
        return true;
    }

    private boolean validateTexCoords() {
        if (this.texCoords.size() == 0) {
            return false;
        }
        if (this.texCoords.size() % getVertexFormat().getTexCoordElementSize() != 0) {
            String logname = TriangleMesh.class.getName();
            PlatformLogger.getLogger(logname).warning("texCoords.size() has to be divisible by getTexCoordElementSize(). It is to store multiple u and v texture coordinates of this mesh");
            return false;
        }
        return true;
    }

    private boolean validateFaces() {
        if (this.faces.size() == 0) {
            return false;
        }
        String logname = TriangleMesh.class.getName();
        if (this.faces.size() % getFaceElementSize() != 0) {
            PlatformLogger.getLogger(logname).warning("faces.size() has to be divisible by getFaceElementSize().");
            return false;
        }
        if (getVertexFormat() == VertexFormat.POINT_TEXCOORD) {
            int nVerts = this.points.size() / getVertexFormat().getPointElementSize();
            int nTVerts = this.texCoords.size() / getVertexFormat().getTexCoordElementSize();
            for (int i2 = 0; i2 < this.faces.size(); i2++) {
                if ((i2 % 2 == 0 && (this.faces.get(i2) >= nVerts || this.faces.get(i2) < 0)) || (i2 % 2 != 0 && (this.faces.get(i2) >= nTVerts || this.faces.get(i2) < 0))) {
                    PlatformLogger.getLogger(logname).warning("The values in the faces array must be within the range of the number of vertices in the points array (0 to points.length / 3 - 1) for the point indices and within the range of the number of the vertices in the texCoords array (0 to texCoords.length / 2 - 1) for the texture coordinate indices.");
                    return false;
                }
            }
            return true;
        }
        if (getVertexFormat() == VertexFormat.POINT_NORMAL_TEXCOORD) {
            int nVerts2 = this.points.size() / getVertexFormat().getPointElementSize();
            int nNVerts = this.normals.size() / getVertexFormat().getNormalElementSize();
            int nTVerts2 = this.texCoords.size() / getVertexFormat().getTexCoordElementSize();
            for (int i3 = 0; i3 < this.faces.size(); i3 += 3) {
                if (this.faces.get(i3) >= nVerts2 || this.faces.get(i3) < 0 || this.faces.get(i3 + 1) >= nNVerts || this.faces.get(i3 + 1) < 0 || this.faces.get(i3 + 2) >= nTVerts2 || this.faces.get(i3 + 2) < 0) {
                    PlatformLogger.getLogger(logname).warning("The values in the faces array must be within the range of the number of vertices in the points array (0 to points.length / 3 - 1) for the point indices, and within the range of the number of the vertices in the normals array (0 to normals.length / 3 - 1) for the normals indices, and number of the vertices in the texCoords array (0 to texCoords.length / 2 - 1) for the texture coordinate indices.");
                    return false;
                }
            }
            return true;
        }
        PlatformLogger.getLogger(logname).warning("Unsupported VertexFormat: " + getVertexFormat().toString());
        return false;
    }

    private boolean validateFaceSmoothingGroups() {
        if (this.faceSmoothingGroups.size() != 0 && this.faceSmoothingGroups.size() != this.faces.size() / getFaceElementSize()) {
            String logname = TriangleMesh.class.getName();
            PlatformLogger.getLogger(logname).warning("faceSmoothingGroups.size() has to equal to number of faces.");
            return false;
        }
        return true;
    }

    private boolean validate() {
        if (this.isPredefinedShape) {
            return true;
        }
        if (this.isValidDirty) {
            if (this.pointsSyncer.dirtyInFull) {
                this.isPointsValid = validatePoints();
            }
            if (this.normalsSyncer.dirtyInFull) {
                this.isNormalsValid = validateNormals();
            }
            if (this.texCoordsSyncer.dirtyInFull) {
                this.isTexCoordsValid = validateTexCoords();
            }
            if (this.facesSyncer.dirty || this.pointsSyncer.dirtyInFull || this.normalsSyncer.dirtyInFull || this.texCoordsSyncer.dirtyInFull) {
                this.isFacesValid = this.isPointsValid && this.isNormalsValid && this.isTexCoordsValid && validateFaces();
            }
            if (this.faceSmoothingGroupsSyncer.dirtyInFull || this.facesSyncer.dirtyInFull) {
                this.isFaceSmoothingGroupValid = this.isFacesValid && validateFaceSmoothingGroups();
            }
            this.isValidDirty = false;
        }
        return this.isPointsValid && this.isNormalsValid && this.isTexCoordsValid && this.isFaceSmoothingGroupValid && this.isFacesValid;
    }

    @Override // javafx.scene.shape.Mesh
    @Deprecated
    void impl_updatePG() {
        if (!isDirty()) {
            return;
        }
        NGTriangleMesh pgTriMesh = impl_getPGTriangleMesh();
        if (validate()) {
            pgTriMesh.setUserDefinedNormals(getVertexFormat() == VertexFormat.POINT_NORMAL_TEXCOORD);
            pgTriMesh.syncPoints(this.pointsSyncer);
            pgTriMesh.syncNormals(this.normalsSyncer);
            pgTriMesh.syncTexCoords(this.texCoordsSyncer);
            pgTriMesh.syncFaces(this.facesSyncer);
            pgTriMesh.syncFaceSmoothingGroups(this.faceSmoothingGroupsSyncer);
        } else {
            pgTriMesh.setUserDefinedNormals(false);
            pgTriMesh.syncPoints(null);
            pgTriMesh.syncNormals(null);
            pgTriMesh.syncTexCoords(null);
            pgTriMesh.syncFaces(null);
            pgTriMesh.syncFaceSmoothingGroups(null);
        }
        setDirty(false);
    }

    @Override // javafx.scene.shape.Mesh
    BaseBounds computeBounds(BaseBounds bounds) {
        if (isDirty() || this.cachedBounds == null) {
            this.cachedBounds = new BoxBounds();
            if (validate()) {
                double len = this.points.size();
                int pointElementSize = 0;
                while (true) {
                    int i2 = pointElementSize;
                    if (i2 >= len) {
                        break;
                    }
                    this.cachedBounds.add(this.points.get(i2), this.points.get(i2 + 1), this.points.get(i2 + 2));
                    pointElementSize = i2 + getVertexFormat().getPointElementSize();
                }
            }
        }
        return bounds.deriveWithNewBounds(this.cachedBounds);
    }

    private Point3D computeCentroid(double v0x, double v0y, double v0z, double v1x, double v1y, double v1z, double v2x, double v2y, double v2z) {
        return new Point3D(v0x + (((v2x + ((v1x - v2x) / 2.0d)) - v0x) / 3.0d), v0y + (((v2y + ((v1y - v2y) / 2.0d)) - v0y) / 3.0d), v0z + (((v2z + ((v1z - v2z) / 2.0d)) - v0z) / 3.0d));
    }

    private Point2D computeCentroid(Point2D v0, Point2D v1, Point2D v2) {
        Point2D center = v1.midpoint(v2);
        Point2D vec = center.subtract(v0);
        return v0.add(new Point2D(vec.getX() / 3.0d, vec.getY() / 3.0d));
    }

    private boolean computeIntersectsFace(PickRay pickRay, Vec3d origin, Vec3d dir, int faceIndex, CullFace cullFace, Node candidate, boolean reportFace, PickResultChooser result) {
        int vertexIndexSize = getVertexFormat().getVertexIndexSize();
        int pointElementSize = getVertexFormat().getPointElementSize();
        int v0Idx = this.faces.get(faceIndex) * pointElementSize;
        int v1Idx = this.faces.get(faceIndex + vertexIndexSize) * pointElementSize;
        int v2Idx = this.faces.get(faceIndex + (2 * vertexIndexSize)) * pointElementSize;
        float v0x = this.points.get(v0Idx);
        float v0y = this.points.get(v0Idx + 1);
        float v0z = this.points.get(v0Idx + 2);
        float v1x = this.points.get(v1Idx);
        float v1y = this.points.get(v1Idx + 1);
        float v1z = this.points.get(v1Idx + 2);
        float v2x = this.points.get(v2Idx);
        float v2y = this.points.get(v2Idx + 1);
        float v2z = this.points.get(v2Idx + 2);
        float e1x = v1x - v0x;
        float e1y = v1y - v0y;
        float e1z = v1z - v0z;
        float e2x = v2x - v0x;
        float e2y = v2y - v0y;
        float e2z = v2z - v0z;
        double hx = (dir.f11931y * e2z) - (dir.f11932z * e2y);
        double hy = (dir.f11932z * e2x) - (dir.f11930x * e2z);
        double hz = (dir.f11930x * e2y) - (dir.f11931y * e2x);
        double a2 = (e1x * hx) + (e1y * hy) + (e1z * hz);
        if (a2 == 0.0d) {
            return false;
        }
        double f2 = 1.0d / a2;
        double sx = origin.f11930x - v0x;
        double sy = origin.f11931y - v0y;
        double sz = origin.f11932z - v0z;
        double u2 = f2 * ((sx * hx) + (sy * hy) + (sz * hz));
        if (u2 < 0.0d || u2 > 1.0d) {
            return false;
        }
        double qx = (sy * e1z) - (sz * e1y);
        double qy = (sz * e1x) - (sx * e1z);
        double qz = (sx * e1y) - (sy * e1x);
        double v2 = f2 * ((dir.f11930x * qx) + (dir.f11931y * qy) + (dir.f11932z * qz));
        if (v2 < 0.0d || u2 + v2 > 1.0d) {
            return false;
        }
        double t2 = f2 * ((e2x * qx) + (e2y * qy) + (e2z * qz));
        if (t2 >= pickRay.getNearClip() && t2 <= pickRay.getFarClip()) {
            if (cullFace != CullFace.NONE) {
                Point3D normal = new Point3D((e1y * e2z) - (e1z * e2y), (e1z * e2x) - (e1x * e2z), (e1x * e2y) - (e1y * e2x));
                double nangle = normal.angle(new Point3D(-dir.f11930x, -dir.f11931y, -dir.f11932z));
                if ((nangle >= 90.0d || cullFace != CullFace.BACK) && (nangle <= 90.0d || cullFace != CullFace.FRONT)) {
                    return false;
                }
            }
            if (Double.isInfinite(t2) || Double.isNaN(t2)) {
                return false;
            }
            if (result == null || !result.isCloser(t2)) {
                return true;
            }
            Point3D point = PickResultChooser.computePoint(pickRay, t2);
            Point3D centroid = computeCentroid(v0x, v0y, v0z, v1x, v1y, v1z, v2x, v2y, v2z);
            Point3D cv0 = new Point3D(v0x - centroid.getX(), v0y - centroid.getY(), v0z - centroid.getZ());
            Point3D cv1 = new Point3D(v1x - centroid.getX(), v1y - centroid.getY(), v1z - centroid.getZ());
            Point3D cv2 = new Point3D(v2x - centroid.getX(), v2y - centroid.getY(), v2z - centroid.getZ());
            Point3D ce1 = cv1.subtract(cv0);
            Point3D ce2 = cv2.subtract(cv0);
            Point3D n2 = ce1.crossProduct(ce2);
            if (n2.getZ() < 0.0d) {
                n2 = new Point3D(-n2.getX(), -n2.getY(), -n2.getZ());
            }
            Point3D ax2 = n2.crossProduct(Rotate.Z_AXIS);
            double angle = Math.atan2(ax2.magnitude(), n2.dotProduct(Rotate.Z_AXIS));
            Rotate r2 = new Rotate(Math.toDegrees(angle), ax2);
            Point3D crv0 = r2.transform(cv0);
            Point3D crv1 = r2.transform(cv1);
            Point3D crv2 = r2.transform(cv2);
            Point3D rPoint = r2.transform(point.subtract(centroid));
            Point2D flatV0 = new Point2D(crv0.getX(), crv0.getY());
            Point2D flatV1 = new Point2D(crv1.getX(), crv1.getY());
            Point2D flatV2 = new Point2D(crv2.getX(), crv2.getY());
            Point2D flatPoint = new Point2D(rPoint.getX(), rPoint.getY());
            int texCoordElementSize = getVertexFormat().getTexCoordElementSize();
            int texCoordOffset = getVertexFormat().getTexCoordIndexOffset();
            int t0Idx = this.faces.get(faceIndex + texCoordOffset) * texCoordElementSize;
            int t1Idx = this.faces.get(faceIndex + vertexIndexSize + texCoordOffset) * texCoordElementSize;
            int t2Idx = this.faces.get(faceIndex + (vertexIndexSize * 2) + texCoordOffset) * texCoordElementSize;
            Point2D u0 = new Point2D(this.texCoords.get(t0Idx), this.texCoords.get(t0Idx + 1));
            Point2D u1 = new Point2D(this.texCoords.get(t1Idx), this.texCoords.get(t1Idx + 1));
            Point2D u22 = new Point2D(this.texCoords.get(t2Idx), this.texCoords.get(t2Idx + 1));
            Point2D txCentroid = computeCentroid(u0, u1, u22);
            Point2D cu0 = u0.subtract(txCentroid);
            Point2D cu1 = u1.subtract(txCentroid);
            Point2D cu2 = u22.subtract(txCentroid);
            Affine src = new Affine(flatV0.getX(), flatV1.getX(), flatV2.getX(), flatV0.getY(), flatV1.getY(), flatV2.getY());
            Affine trg = new Affine(cu0.getX(), cu1.getX(), cu2.getX(), cu0.getY(), cu1.getY(), cu2.getY());
            Point2D txCoords = null;
            try {
                src.invert();
                trg.append(src);
                txCoords = txCentroid.add(trg.transform(flatPoint));
            } catch (NonInvertibleTransformException e2) {
            }
            result.offer(candidate, t2, reportFace ? faceIndex / getFaceElementSize() : -1, point, txCoords);
            return true;
        }
        return false;
    }

    @Override // javafx.scene.shape.Mesh
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResult, Node candidate, CullFace cullFace, boolean reportFace) {
        boolean found = false;
        if (validate()) {
            int size = this.faces.size();
            Vec3d o2 = pickRay.getOriginNoClone();
            Vec3d d2 = pickRay.getDirectionNoClone();
            int faceElementSize = 0;
            while (true) {
                int i2 = faceElementSize;
                if (i2 >= size) {
                    break;
                }
                if (computeIntersectsFace(pickRay, o2, d2, i2, cullFace, candidate, reportFace, pickResult)) {
                    found = true;
                }
                faceElementSize = i2 + getFaceElementSize();
            }
        }
        return found;
    }

    /* loaded from: jfxrt.jar:javafx/scene/shape/TriangleMesh$Listener.class */
    private class Listener<T extends ObservableArray<T>> implements ArrayChangeListener<T>, FloatArraySyncer, IntegerArraySyncer {
        protected final T array;
        protected boolean dirty = true;
        protected boolean dirtyInFull = true;
        protected int dirtyRangeFrom;
        protected int dirtyRangeLength;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !TriangleMesh.class.desiredAssertionStatus();
        }

        public Listener(T array) {
            this.array = array;
            array.addListener(this);
        }

        protected final void addDirtyRange(int from, int length) {
            if (length > 0 && !this.dirtyInFull) {
                markDirty();
                if (this.dirtyRangeLength == 0) {
                    this.dirtyRangeFrom = from;
                    this.dirtyRangeLength = length;
                } else {
                    int fromIndex = Math.min(this.dirtyRangeFrom, from);
                    int toIndex = Math.max(this.dirtyRangeFrom + this.dirtyRangeLength, from + length);
                    this.dirtyRangeFrom = fromIndex;
                    this.dirtyRangeLength = toIndex - fromIndex;
                }
            }
        }

        protected void markDirty() {
            this.dirty = true;
            TriangleMesh.this.setDirty(true);
        }

        @Override // javafx.collections.ArrayChangeListener
        public void onChanged(T observableArray, boolean sizeChanged, int from, int to) {
            if (sizeChanged) {
                setDirty(true);
            } else {
                addDirtyRange(from, to - from);
            }
            TriangleMesh.this.isValidDirty = true;
        }

        public final void setDirty(boolean dirty) {
            this.dirtyInFull = dirty;
            if (dirty) {
                markDirty();
                this.dirtyRangeFrom = 0;
                this.dirtyRangeLength = this.array.size();
            } else {
                this.dirty = false;
                this.dirtyRangeLength = 0;
                this.dirtyRangeFrom = 0;
            }
        }

        @Override // com.sun.javafx.collections.FloatArraySyncer
        public float[] syncTo(float[] array, int[] fromAndLengthIndices) {
            if (!$assertionsDisabled && (fromAndLengthIndices == null || fromAndLengthIndices.length != 2)) {
                throw new AssertionError();
            }
            ObservableFloatArray floatArray = (ObservableFloatArray) this.array;
            if (this.dirtyInFull || array == null || array.length != floatArray.size()) {
                fromAndLengthIndices[0] = 0;
                fromAndLengthIndices[1] = floatArray.size();
                return floatArray.toArray(null);
            }
            fromAndLengthIndices[0] = this.dirtyRangeFrom;
            fromAndLengthIndices[1] = this.dirtyRangeLength;
            floatArray.copyTo(this.dirtyRangeFrom, array, this.dirtyRangeFrom, this.dirtyRangeLength);
            return array;
        }

        @Override // com.sun.javafx.collections.IntegerArraySyncer
        public int[] syncTo(int[] array, int[] fromAndLengthIndices) {
            if (!$assertionsDisabled && (fromAndLengthIndices == null || fromAndLengthIndices.length != 2)) {
                throw new AssertionError();
            }
            ObservableIntegerArray intArray = (ObservableIntegerArray) this.array;
            if (this.dirtyInFull || array == null || array.length != intArray.size()) {
                fromAndLengthIndices[0] = 0;
                fromAndLengthIndices[1] = intArray.size();
                return intArray.toArray(null);
            }
            fromAndLengthIndices[0] = this.dirtyRangeFrom;
            fromAndLengthIndices[1] = this.dirtyRangeLength;
            intArray.copyTo(this.dirtyRangeFrom, array, this.dirtyRangeFrom, this.dirtyRangeLength);
            return array;
        }
    }
}
