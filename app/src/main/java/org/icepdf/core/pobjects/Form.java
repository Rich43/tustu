package org.icepdf.core.pobjects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.io.SeekableInputConstrainedWrapper;
import org.icepdf.core.pobjects.graphics.GraphicsState;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;
import org.icepdf.core.util.content.ContentParser;
import org.icepdf.core.util.content.ContentParserFactory;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Form.class */
public class Form extends Stream {
    private static final Logger logger = Logger.getLogger(Form.class.toString());
    public static final Name TYPE_VALUE = new Name("XObject");
    public static final Name SUB_TYPE_VALUE = new Name("Form");
    public static final Name GROUP_KEY = new Name("Group");
    public static final Name I_KEY = new Name("I");
    public static final Name K_KEY = new Name(PdfOps.K_TOKEN);
    public static final Name MATRIX_KEY = new Name("Matrix");
    public static final Name BBOX_KEY = new Name("BBox");
    public static final Name RESOURCES_KEY = new Name("Resources");
    private AffineTransform matrix;
    private Rectangle2D bbox;
    private Shapes shapes;
    private GraphicsState graphicsState;
    private Resources resources;
    private Resources parentResource;
    private boolean transparencyGroup;
    private boolean isolated;
    private boolean knockOut;
    private boolean inited;

    public Form(Library l2, HashMap h2, SeekableInputConstrainedWrapper streamInputWrapper) {
        super(l2, h2, streamInputWrapper);
        this.matrix = new AffineTransform();
        this.inited = false;
        HashMap group = this.library.getDictionary(this.entries, GROUP_KEY);
        if (group != null) {
            this.transparencyGroup = true;
            this.isolated = this.library.getBoolean(group, I_KEY).booleanValue();
            this.knockOut = this.library.getBoolean(group, K_KEY).booleanValue();
        }
    }

    public void setAppearance(Shapes shapes, AffineTransform matrix, Rectangle2D bbox) {
        this.shapes = shapes;
        this.matrix = matrix;
        this.bbox = bbox;
        this.entries.put(BBOX_KEY, PRectangle.getPRectangleVector(bbox));
        this.entries.put(MATRIX_KEY, matrix);
    }

    public void setGraphicsState(GraphicsState graphicsState) {
        if (graphicsState != null) {
            this.graphicsState = graphicsState;
        }
    }

    public GraphicsState getGraphicsState() {
        return this.graphicsState;
    }

    private static AffineTransform getAffineTransform(List v2) {
        float[] f2 = new float[6];
        for (int i2 = 0; i2 < 6; i2++) {
            f2[i2] = ((Number) v2.get(i2)).floatValue();
        }
        return new AffineTransform(f2);
    }

    public void setParentResources(Resources parentResource) {
        this.parentResource = parentResource;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v8, types: [byte[], byte[][]] */
    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        if (this.inited) {
            return;
        }
        Object v2 = this.library.getObject(this.entries, MATRIX_KEY);
        if (v2 != null && (v2 instanceof List)) {
            this.matrix = getAffineTransform((List) v2);
        } else if (v2 != null && (v2 instanceof AffineTransform)) {
            this.matrix = (AffineTransform) v2;
        }
        this.bbox = this.library.getRectangle(this.entries, BBOX_KEY);
        Resources leafResources = this.library.getResources(this.entries, RESOURCES_KEY);
        if (leafResources != null) {
            this.resources = leafResources;
        } else {
            leafResources = this.parentResource;
        }
        ContentParser contentParser = ContentParserFactory.getInstance().getContentParser(this.library, leafResources);
        contentParser.setGraphicsState(this.graphicsState);
        byte[] in = getDecodedStreamBytes();
        if (in != null) {
            try {
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("Parsing form " + ((Object) getPObjectReference()));
                }
                this.shapes = contentParser.parse(new byte[]{in}, null).getShapes();
            } catch (Throwable e2) {
                this.shapes = new Shapes();
                logger.log(Level.FINE, "Error parsing Form content stream.", e2);
            }
        }
        this.inited = true;
    }

    public Resources getResources() {
        Resources leafResources = this.library.getResources(this.entries, RESOURCES_KEY);
        if (this.resources == null) {
            leafResources = new Resources(this.library, new HashMap());
        }
        return leafResources;
    }

    public void setResources(Resources resources) {
        this.entries.put(RESOURCES_KEY, resources.getEntries());
    }

    public Shapes getShapes() {
        return this.shapes;
    }

    public Rectangle2D getBBox() {
        return this.bbox;
    }

    public AffineTransform getMatrix() {
        return this.matrix;
    }

    public boolean isTransparencyGroup() {
        return this.transparencyGroup;
    }

    public boolean isIsolated() {
        return this.isolated;
    }

    public boolean isKnockOut() {
        return this.knockOut;
    }
}
