package javafx.scene.shape;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.shape.PathUtils;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPath;
import java.util.Collection;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/Path.class */
public class Path extends Shape {
    private Path2D path2d = null;
    private ObjectProperty<FillRule> fillRule;
    private boolean isPathValid;
    private final ObservableList<PathElement> elements;

    public Path() {
        ((StyleableProperty) fillProperty()).applyStyle(null, null);
        ((StyleableProperty) strokeProperty()).applyStyle(null, Color.BLACK);
        this.elements = new TrackableObservableList<PathElement>() { // from class: javafx.scene.shape.Path.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<PathElement> c2) {
                boolean firstElementChanged;
                List<PathElement> list = c2.getList();
                boolean z2 = false;
                while (true) {
                    firstElementChanged = z2;
                    if (!c2.next()) {
                        break;
                    }
                    List<PathElement> removed = c2.getRemoved();
                    for (int i2 = 0; i2 < c2.getRemovedSize(); i2++) {
                        removed.get(i2).removeNode(Path.this);
                    }
                    for (int i3 = c2.getFrom(); i3 < c2.getTo(); i3++) {
                        list.get(i3).addNode(Path.this);
                    }
                    z2 = firstElementChanged | (c2.getFrom() == 0);
                }
                if (Path.this.path2d != null) {
                    c2.reset();
                    c2.next();
                    if (c2.getFrom() != c2.getList().size() || c2.wasRemoved() || !c2.wasAdded()) {
                        Path.this.path2d = null;
                    } else {
                        for (int i4 = c2.getFrom(); i4 < c2.getTo(); i4++) {
                            list.get(i4).impl_addTo(Path.this.path2d);
                        }
                    }
                }
                if (firstElementChanged) {
                    Path.this.isPathValid = Path.this.impl_isFirstPathElementValid();
                }
                Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                Path.this.impl_geomChanged();
            }
        };
    }

    public Path(PathElement... elements) {
        ((StyleableProperty) fillProperty()).applyStyle(null, null);
        ((StyleableProperty) strokeProperty()).applyStyle(null, Color.BLACK);
        this.elements = new TrackableObservableList<PathElement>() { // from class: javafx.scene.shape.Path.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<PathElement> c2) {
                boolean firstElementChanged;
                List<PathElement> list = c2.getList();
                boolean z2 = false;
                while (true) {
                    firstElementChanged = z2;
                    if (!c2.next()) {
                        break;
                    }
                    List<PathElement> removed = c2.getRemoved();
                    for (int i2 = 0; i2 < c2.getRemovedSize(); i2++) {
                        removed.get(i2).removeNode(Path.this);
                    }
                    for (int i3 = c2.getFrom(); i3 < c2.getTo(); i3++) {
                        list.get(i3).addNode(Path.this);
                    }
                    z2 = firstElementChanged | (c2.getFrom() == 0);
                }
                if (Path.this.path2d != null) {
                    c2.reset();
                    c2.next();
                    if (c2.getFrom() != c2.getList().size() || c2.wasRemoved() || !c2.wasAdded()) {
                        Path.this.path2d = null;
                    } else {
                        for (int i4 = c2.getFrom(); i4 < c2.getTo(); i4++) {
                            list.get(i4).impl_addTo(Path.this.path2d);
                        }
                    }
                }
                if (firstElementChanged) {
                    Path.this.isPathValid = Path.this.impl_isFirstPathElementValid();
                }
                Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                Path.this.impl_geomChanged();
            }
        };
        if (elements != null) {
            this.elements.addAll(elements);
        }
    }

    public Path(Collection<? extends PathElement> elements) {
        ((StyleableProperty) fillProperty()).applyStyle(null, null);
        ((StyleableProperty) strokeProperty()).applyStyle(null, Color.BLACK);
        this.elements = new TrackableObservableList<PathElement>() { // from class: javafx.scene.shape.Path.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<PathElement> c2) {
                boolean firstElementChanged;
                List<PathElement> list = c2.getList();
                boolean z2 = false;
                while (true) {
                    firstElementChanged = z2;
                    if (!c2.next()) {
                        break;
                    }
                    List<PathElement> removed = c2.getRemoved();
                    for (int i2 = 0; i2 < c2.getRemovedSize(); i2++) {
                        removed.get(i2).removeNode(Path.this);
                    }
                    for (int i3 = c2.getFrom(); i3 < c2.getTo(); i3++) {
                        list.get(i3).addNode(Path.this);
                    }
                    z2 = firstElementChanged | (c2.getFrom() == 0);
                }
                if (Path.this.path2d != null) {
                    c2.reset();
                    c2.next();
                    if (c2.getFrom() != c2.getList().size() || c2.wasRemoved() || !c2.wasAdded()) {
                        Path.this.path2d = null;
                    } else {
                        for (int i4 = c2.getFrom(); i4 < c2.getTo(); i4++) {
                            list.get(i4).impl_addTo(Path.this.path2d);
                        }
                    }
                }
                if (firstElementChanged) {
                    Path.this.isPathValid = Path.this.impl_isFirstPathElementValid();
                }
                Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                Path.this.impl_geomChanged();
            }
        };
        if (elements != null) {
            this.elements.addAll(elements);
        }
    }

    void markPathDirty() {
        this.path2d = null;
        impl_markDirty(DirtyBits.NODE_CONTENTS);
        impl_geomChanged();
    }

    public final void setFillRule(FillRule value) {
        if (this.fillRule != null || value != FillRule.NON_ZERO) {
            fillRuleProperty().set(value);
        }
    }

    public final FillRule getFillRule() {
        return this.fillRule == null ? FillRule.NON_ZERO : this.fillRule.get();
    }

    public final ObjectProperty<FillRule> fillRuleProperty() {
        if (this.fillRule == null) {
            this.fillRule = new ObjectPropertyBase<FillRule>(FillRule.NON_ZERO) { // from class: javafx.scene.shape.Path.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                    Path.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Path.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fillRule";
                }
            };
        }
        return this.fillRule;
    }

    public final ObservableList<PathElement> getElements() {
        return this.elements;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGPath();
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public Path2D impl_configShape() {
        if (this.isPathValid) {
            if (this.path2d == null) {
                this.path2d = PathUtils.configShape(getElements(), getFillRule() == FillRule.EVEN_ODD);
            } else {
                this.path2d.setWindingRule(getFillRule() == FillRule.NON_ZERO ? 1 : 0);
            }
            return this.path2d;
        }
        return new Path2D();
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected Bounds impl_computeLayoutBounds() {
        if (this.isPathValid) {
            return super.impl_computeLayoutBounds();
        }
        return new BoundingBox(0.0d, 0.0d, -1.0d, -1.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean impl_isFirstPathElementValid() {
        ObservableList<PathElement> _elements = getElements();
        if (_elements != null && _elements.size() > 0) {
            PathElement firstElement = _elements.get(0);
            if (!firstElement.isAbsolute()) {
                System.err.printf("First element of the path can not be relative. Path: %s\n", this);
                return false;
            }
            if (firstElement instanceof MoveTo) {
                return true;
            }
            System.err.printf("Missing initial moveto in path definition. Path: %s\n", this);
            return false;
        }
        return true;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            NGPath peer = (NGPath) impl_getPeer();
            if (peer.acceptsPath2dOnUpdate()) {
                peer.updateWithPath2d(impl_configShape());
                return;
            }
            peer.reset();
            if (this.isPathValid) {
                peer.setFillRule(getFillRule());
                for (PathElement elt : getElements()) {
                    elt.addTo(peer);
                }
                peer.update();
            }
        }
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    protected Paint impl_cssGetFillInitialValue() {
        return null;
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    protected Paint impl_cssGetStrokeInitialValue() {
        return Color.BLACK;
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("Path[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("elements=").append((Object) getElements());
        sb.append(", fill=").append((Object) getFill());
        sb.append(", fillRule=").append((Object) getFillRule());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }
}
