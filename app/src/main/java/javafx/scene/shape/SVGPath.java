package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGSVGPath;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.paint.Paint;
import javax.swing.text.AbstractDocument;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:javafx/scene/shape/SVGPath.class */
public class SVGPath extends Shape {
    private ObjectProperty<FillRule> fillRule;
    private Path2D path2d;
    private StringProperty content;
    private Object svgPathObject;

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
            this.fillRule = new ObjectPropertyBase<FillRule>(FillRule.NON_ZERO) { // from class: javafx.scene.shape.SVGPath.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    SVGPath.this.impl_markDirty(DirtyBits.SHAPE_FILLRULE);
                    SVGPath.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SVGPath.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fillRule";
                }
            };
        }
        return this.fillRule;
    }

    public final void setContent(String value) {
        contentProperty().set(value);
    }

    public final String getContent() {
        return this.content == null ? "" : this.content.get();
    }

    public final StringProperty contentProperty() {
        if (this.content == null) {
            this.content = new StringPropertyBase("") { // from class: javafx.scene.shape.SVGPath.2
                @Override // javafx.beans.property.StringPropertyBase
                public void invalidated() {
                    SVGPath.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                    SVGPath.this.impl_geomChanged();
                    SVGPath.this.path2d = null;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SVGPath.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return AbstractDocument.ContentElementName;
                }
            };
        }
        return this.content;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGSVGPath();
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public Path2D impl_configShape() {
        if (this.path2d == null) {
            this.path2d = createSVGPath2D();
        } else {
            this.path2d.setWindingRule(getFillRule() == FillRule.NON_ZERO ? 1 : 0);
        }
        return this.path2d;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.SHAPE_FILLRULE) || impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            NGSVGPath peer = (NGSVGPath) impl_getPeer();
            if (peer.acceptsPath2dOnUpdate()) {
                if (this.svgPathObject == null) {
                    this.svgPathObject = new Path2D();
                }
                Path2D tempPathObject = (Path2D) this.svgPathObject;
                tempPathObject.setTo(impl_configShape());
            } else {
                this.svgPathObject = createSVGPathObject();
            }
            peer.setContent(this.svgPathObject);
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("SVGPath[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("content=\"").append(getContent()).append(PdfOps.DOUBLE_QUOTE__TOKEN);
        sb.append(", fill=").append((Object) getFill());
        sb.append(", fillRule=").append((Object) getFillRule());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }

    private Path2D createSVGPath2D() {
        try {
            return Toolkit.getToolkit().createSVGPath2D(this);
        } catch (RuntimeException e2) {
            Logging.getJavaFXLogger().warning("Failed to configure svg path \"{0}\": {1}", getContent(), e2.getMessage());
            return Toolkit.getToolkit().createSVGPath2D(new SVGPath());
        }
    }

    private Object createSVGPathObject() {
        try {
            return Toolkit.getToolkit().createSVGPathObject(this);
        } catch (RuntimeException e2) {
            Logging.getJavaFXLogger().warning("Failed to configure svg path \"{0}\": {1}", getContent(), e2.getMessage());
            return Toolkit.getToolkit().createSVGPathObject(new SVGPath());
        }
    }
}
