package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGShape3D;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.Node;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/shape/Shape3D.class */
public abstract class Shape3D extends Node {
    private static final PhongMaterial DEFAULT_MATERIAL = new PhongMaterial();
    PredefinedMeshManager manager = PredefinedMeshManager.getInstance();
    int key = 0;
    private ObjectProperty<Material> material;
    private ObjectProperty<DrawMode> drawMode;
    private ObjectProperty<CullFace> cullFace;

    protected Shape3D() {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String logname = Shape3D.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
    }

    public final void setMaterial(Material value) {
        materialProperty().set(value);
    }

    public final Material getMaterial() {
        if (this.material == null) {
            return null;
        }
        return this.material.get();
    }

    public final ObjectProperty<Material> materialProperty() {
        if (this.material == null) {
            this.material = new SimpleObjectProperty<Material>(this, "material") { // from class: javafx.scene.shape.Shape3D.1
                private Material old = null;
                private final ChangeListener<Boolean> materialChangeListener = (observable, oldValue, newValue) -> {
                    if (newValue.booleanValue()) {
                        Shape3D.this.impl_markDirty(DirtyBits.MATERIAL);
                    }
                };
                private final WeakChangeListener<Boolean> weakMaterialChangeListener = new WeakChangeListener<>(this.materialChangeListener);

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.old != null) {
                        this.old.impl_dirtyProperty().removeListener(this.weakMaterialChangeListener);
                    }
                    Material newMaterial = get();
                    if (newMaterial != null) {
                        newMaterial.impl_dirtyProperty().addListener(this.weakMaterialChangeListener);
                    }
                    Shape3D.this.impl_markDirty(DirtyBits.MATERIAL);
                    Shape3D.this.impl_geomChanged();
                    this.old = newMaterial;
                }
            };
        }
        return this.material;
    }

    public final void setDrawMode(DrawMode value) {
        drawModeProperty().set(value);
    }

    public final DrawMode getDrawMode() {
        return this.drawMode == null ? DrawMode.FILL : this.drawMode.get();
    }

    public final ObjectProperty<DrawMode> drawModeProperty() {
        if (this.drawMode == null) {
            this.drawMode = new SimpleObjectProperty<DrawMode>(this, "drawMode", DrawMode.FILL) { // from class: javafx.scene.shape.Shape3D.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Shape3D.this.impl_markDirty(DirtyBits.NODE_DRAWMODE);
                }
            };
        }
        return this.drawMode;
    }

    public final void setCullFace(CullFace value) {
        cullFaceProperty().set(value);
    }

    public final CullFace getCullFace() {
        return this.cullFace == null ? CullFace.BACK : this.cullFace.get();
    }

    public final ObjectProperty<CullFace> cullFaceProperty() {
        if (this.cullFace == null) {
            this.cullFace = new SimpleObjectProperty<CullFace>(this, "cullFace", CullFace.BACK) { // from class: javafx.scene.shape.Shape3D.3
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Shape3D.this.impl_markDirty(DirtyBits.NODE_CULLFACE);
                }
            };
        }
        return this.cullFace;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return new BoxBounds(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        return false;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        NGShape3D peer = (NGShape3D) impl_getPeer();
        if (impl_isDirty(DirtyBits.MATERIAL)) {
            Material mat = getMaterial() == null ? DEFAULT_MATERIAL : getMaterial();
            mat.impl_updatePG();
            peer.setMaterial(mat.impl_getNGMaterial());
        }
        if (impl_isDirty(DirtyBits.NODE_DRAWMODE)) {
            peer.setDrawMode(getDrawMode() == null ? DrawMode.FILL : getDrawMode());
        }
        if (impl_isDirty(DirtyBits.NODE_CULLFACE)) {
            peer.setCullFace(getCullFace() == null ? CullFace.BACK : getCullFace());
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
