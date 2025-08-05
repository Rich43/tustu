package javafx.scene.canvas;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.GrowableDataBuffer;
import com.sun.javafx.sg.prism.NGCanvas;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/canvas/Canvas.class */
public class Canvas extends Node {
    static final int DEFAULT_VAL_BUF_SIZE = 1024;
    static final int DEFAULT_OBJ_BUF_SIZE = 32;
    private static final int SIZE_HISTORY = 5;
    private GrowableDataBuffer current;
    private boolean rendererBehind;
    private int[] recentvalsizes;
    private int[] recentobjsizes;
    private int lastsizeindex;
    private GraphicsContext theContext;
    private DoubleProperty width;
    private DoubleProperty height;

    public Canvas() {
        this(0.0d, 0.0d);
    }

    public Canvas(double width, double height) {
        this.recentvalsizes = new int[5];
        this.recentobjsizes = new int[5];
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        setWidth(width);
        setHeight(height);
    }

    private static int max(int[] sizes, int defsize) {
        for (int s2 : sizes) {
            if (defsize < s2) {
                defsize = s2;
            }
        }
        return defsize;
    }

    GrowableDataBuffer getBuffer() {
        impl_markDirty(DirtyBits.NODE_CONTENTS);
        impl_markDirty(DirtyBits.NODE_FORCE_SYNC);
        if (this.current == null) {
            int vsize = max(this.recentvalsizes, 1024);
            int osize = max(this.recentobjsizes, 32);
            this.current = GrowableDataBuffer.getBuffer(vsize, osize);
            this.theContext.updateDimensions();
        }
        return this.current;
    }

    boolean isRendererFallingBehind() {
        return this.rendererBehind;
    }

    public GraphicsContext getGraphicsContext2D() {
        if (this.theContext == null) {
            this.theContext = new GraphicsContext(this);
        }
        return this.theContext;
    }

    public final void setWidth(double value) {
        widthProperty().set(value);
    }

    public final double getWidth() {
        if (this.width == null) {
            return 0.0d;
        }
        return this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new DoublePropertyBase() { // from class: javafx.scene.canvas.Canvas.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Canvas.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Canvas.this.impl_geomChanged();
                    if (Canvas.this.theContext != null) {
                        Canvas.this.theContext.updateDimensions();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Canvas.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.WIDTH_TAG_NAME;
                }
            };
        }
        return this.width;
    }

    public final void setHeight(double value) {
        heightProperty().set(value);
    }

    public final double getHeight() {
        if (this.height == null) {
            return 0.0d;
        }
        return this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new DoublePropertyBase() { // from class: javafx.scene.canvas.Canvas.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Canvas.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Canvas.this.impl_geomChanged();
                    if (Canvas.this.theContext != null) {
                        Canvas.this.theContext.updateDimensions();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Canvas.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.HEIGHT_TAG_NAME;
                }
            };
        }
        return this.height;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGCanvas();
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGCanvas peer = (NGCanvas) impl_getPeer();
            peer.updateBounds((float) getWidth(), (float) getHeight());
        }
        if (impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            NGCanvas peer2 = (NGCanvas) impl_getPeer();
            if (this.current != null && !this.current.isEmpty()) {
                int i2 = this.lastsizeindex - 1;
                this.lastsizeindex = i2;
                if (i2 < 0) {
                    this.lastsizeindex = 4;
                }
                this.recentvalsizes[this.lastsizeindex] = this.current.writeValuePosition();
                this.recentobjsizes[this.lastsizeindex] = this.current.writeObjectPosition();
                this.rendererBehind = peer2.updateRendering(this.current);
                this.current = null;
            }
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        double w2 = getWidth();
        double h2 = getHeight();
        return w2 > 0.0d && h2 > 0.0d && localX >= 0.0d && localY >= 0.0d && localX < w2 && localY < h2;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        BaseBounds bounds2 = new RectBounds(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
        return tx.transform(bounds2, bounds2);
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return alg.processLeafNode(this, ctx);
    }
}
