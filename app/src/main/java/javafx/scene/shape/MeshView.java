package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGMeshView;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;

/* loaded from: jfxrt.jar:javafx/scene/shape/MeshView.class */
public class MeshView extends Shape3D {
    private ObjectProperty<Mesh> mesh;

    public MeshView() {
    }

    public MeshView(Mesh mesh) {
        setMesh(mesh);
    }

    public final void setMesh(Mesh value) {
        meshProperty().set(value);
    }

    public final Mesh getMesh() {
        if (this.mesh == null) {
            return null;
        }
        return this.mesh.get();
    }

    public final ObjectProperty<Mesh> meshProperty() {
        if (this.mesh == null) {
            this.mesh = new SimpleObjectProperty<Mesh>(this, "mesh") { // from class: javafx.scene.shape.MeshView.1
                private Mesh old = null;
                private final ChangeListener<Boolean> meshChangeListener = (observable, oldValue, newValue) -> {
                    if (newValue.booleanValue()) {
                        MeshView.this.impl_markDirty(DirtyBits.MESH_GEOM);
                        MeshView.this.impl_geomChanged();
                    }
                };
                private final WeakChangeListener<Boolean> weakMeshChangeListener = new WeakChangeListener<>(this.meshChangeListener);

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.old != null) {
                        this.old.dirtyProperty().removeListener(this.weakMeshChangeListener);
                    }
                    Mesh newMesh = get();
                    if (newMesh != null) {
                        newMesh.dirtyProperty().addListener(this.weakMeshChangeListener);
                    }
                    MeshView.this.impl_markDirty(DirtyBits.MESH);
                    MeshView.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    MeshView.this.impl_geomChanged();
                    this.old = newMesh;
                }
            };
        }
        return this.mesh;
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        NGMeshView peer = (NGMeshView) impl_getPeer();
        if (impl_isDirty(DirtyBits.MESH_GEOM) && getMesh() != null) {
            getMesh().impl_updatePG();
        }
        if (impl_isDirty(DirtyBits.MESH)) {
            peer.setMesh(getMesh() == null ? null : getMesh().getPGMesh());
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGMeshView();
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        if (getMesh() != null) {
            BaseBounds bounds2 = getMesh().computeBounds(bounds);
            bounds = tx.transform(bounds2, bounds2);
        } else {
            bounds.makeEmpty();
        }
        return bounds;
    }

    @Override // javafx.scene.shape.Shape3D, javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResult) {
        return getMesh().impl_computeIntersects(pickRay, pickResult, this, getCullFace(), true);
    }
}
