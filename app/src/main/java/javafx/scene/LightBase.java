package javafx.scene;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.tk.Toolkit;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/LightBase.class */
public abstract class LightBase extends Node {
    private Affine3D localToSceneTx;
    private ObjectProperty<Color> color;
    private BooleanProperty lightOn;
    private ObservableList<Node> scope;

    protected LightBase() {
        this(Color.WHITE);
    }

    protected LightBase(Color color) {
        this.localToSceneTx = new Affine3D();
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String logname = LightBase.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
        setColor(color);
        localToSceneTransformProperty().addListener(observable -> {
            impl_markDirty(DirtyBits.NODE_LIGHT_TRANSFORM);
        });
    }

    public final void setColor(Color value) {
        colorProperty().set(value);
    }

    public final Color getColor() {
        if (this.color == null) {
            return null;
        }
        return this.color.get();
    }

    public final ObjectProperty<Color> colorProperty() {
        if (this.color == null) {
            this.color = new SimpleObjectProperty<Color>(this, "color") { // from class: javafx.scene.LightBase.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT);
                }
            };
        }
        return this.color;
    }

    public final void setLightOn(boolean value) {
        lightOnProperty().set(value);
    }

    public final boolean isLightOn() {
        if (this.lightOn == null) {
            return true;
        }
        return this.lightOn.get();
    }

    public final BooleanProperty lightOnProperty() {
        if (this.lightOn == null) {
            this.lightOn = new SimpleBooleanProperty(this, "lightOn", true) { // from class: javafx.scene.LightBase.2
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT);
                }
            };
        }
        return this.lightOn;
    }

    public ObservableList<Node> getScope() {
        if (this.scope == null) {
            this.scope = new TrackableObservableList<Node>() { // from class: javafx.scene.LightBase.3
                @Override // com.sun.javafx.collections.TrackableObservableList
                protected void onChanged(ListChangeListener.Change<Node> c2) {
                    LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT_SCOPE);
                    while (c2.next()) {
                        for (Node node : c2.getRemoved()) {
                            if ((node instanceof Parent) || (node instanceof Shape3D)) {
                                LightBase.this.markChildrenDirty(node);
                            }
                        }
                        for (Node node2 : c2.getAddedSubList()) {
                            if ((node2 instanceof Parent) || (node2 instanceof Shape3D)) {
                                LightBase.this.markChildrenDirty(node2);
                            }
                        }
                    }
                }
            };
        }
        return this.scope;
    }

    @Override // javafx.scene.Node
    void scenesChanged(Scene newScene, SubScene newSubScene, Scene oldScene, SubScene oldSubScene) {
        if (oldSubScene != null) {
            oldSubScene.removeLight(this);
        } else if (oldScene != null) {
            oldScene.removeLight(this);
        }
        if (newSubScene != null) {
            newSubScene.addLight(this);
        } else if (newScene != null) {
            newScene.addLight(this);
        }
    }

    private void markOwnerDirty() {
        SubScene subScene = getSubScene();
        if (subScene != null) {
            subScene.markContentDirty();
            return;
        }
        Scene scene = getScene();
        if (scene != null) {
            scene.setNeedsRepaint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markChildrenDirty(Node node) {
        if (node instanceof Shape3D) {
            ((Shape3D) node).impl_markDirty(DirtyBits.NODE_DRAWMODE);
        } else if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildren()) {
                markChildrenDirty(child);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javafx.scene.Node
    @Deprecated
    public void impl_markDirty(DirtyBits dirtyBit) {
        super.impl_markDirty(dirtyBit);
        if (this.scope == null || getScope().isEmpty()) {
            markOwnerDirty();
            return;
        }
        if (dirtyBit != DirtyBits.NODE_LIGHT_SCOPE) {
            ObservableList<Node> tmpScope = getScope();
            int max = tmpScope.size();
            for (int i2 = 0; i2 < max; i2++) {
                markChildrenDirty(tmpScope.get(i2));
            }
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        Object platformPaint;
        super.impl_updatePeer();
        NGLightBase peer = (NGLightBase) impl_getPeer();
        if (impl_isDirty(DirtyBits.NODE_LIGHT)) {
            if (getColor() == null) {
                platformPaint = Toolkit.getPaintAccessor().getPlatformPaint(Color.WHITE);
            } else {
                platformPaint = Toolkit.getPaintAccessor().getPlatformPaint(getColor());
            }
            peer.setColor(platformPaint);
            peer.setLightOn(isLightOn());
        }
        if (impl_isDirty(DirtyBits.NODE_LIGHT_SCOPE) && this.scope != null) {
            ObservableList<Node> tmpScope = getScope();
            if (tmpScope.isEmpty()) {
                peer.setScope(null);
            } else {
                Object[] ngList = new Object[tmpScope.size()];
                for (int i2 = 0; i2 < tmpScope.size(); i2++) {
                    Node n2 = tmpScope.get(i2);
                    ngList[i2] = n2.impl_getPeer();
                }
                peer.setScope(ngList);
            }
        }
        if (impl_isDirty(DirtyBits.NODE_LIGHT_TRANSFORM)) {
            this.localToSceneTx.setToIdentity();
            getLocalToSceneTransform().impl_apply(this.localToSceneTx);
            peer.setWorldTransform(this.localToSceneTx);
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return new BoxBounds();
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        return false;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
