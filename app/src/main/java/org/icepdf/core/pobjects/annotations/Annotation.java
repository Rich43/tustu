package org.icepdf.core.pobjects.annotations;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.glassfish.external.amx.AMX;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Form;
import org.icepdf.core.pobjects.LiteralStringObject;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.PObject;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.pobjects.actions.Action;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.security.SecurityManager;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;
import org.icepdf.core.util.content.ContentParserFactory;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/Annotation.class */
public abstract class Annotation extends Dictionary {
    public static final int FLAG_INVISIBLE = 1;
    public static final int FLAG_HIDDEN = 2;
    public static final int FLAG_PRINT = 4;
    public static final int FLAG_NO_ZOOM = 8;
    public static final int FLAG_NO_ROTATE = 16;
    public static final int FLAG_NO_VIEW = 32;
    public static final int FLAG_READ_ONLY = 64;
    public static final int FLAG_LOCKED = 128;
    public static final int FLAG_TOGGLE_NO_VIEW = 256;
    public static final int FLAG_LOCKED_CONTENTS = 512;
    public static final int BORDER_HORIZONTAL_CORNER_RADIUS = 0;
    public static final int BORDER_VERTICAL_CORNER_RADIUS = 1;
    public static final int BORDER_WIDTH = 2;
    public static final int BORDER_DASH = 3;
    public static final int VISIBLE_RECTANGLE = 1;
    public static final int INVISIBLE_RECTANGLE = 0;
    protected Shapes shapes;
    protected AffineTransform matrix;
    protected Rectangle2D bbox;
    protected PDate modifiedDate;
    protected boolean hasBlendingMode;
    protected SecurityManager securityManager;
    protected Name subtype;
    protected String content;
    protected BorderStyle borderStyle;
    protected List border;
    protected Color color;
    protected Rectangle2D.Float userSpaceRectangle;
    protected boolean canDrawBorder;
    private static final Logger logger = Logger.getLogger(Annotation.class.toString());
    public static final Name TYPE = new Name("Annot");
    public static final Name RESOURCES_VALUE = new Name("Resources");
    public static final Name BBOX_VALUE = new Name("BBox");
    public static final Name PARENT_KEY = new Name(AMX.ATTR_PARENT);
    public static final Name TYPE_VALUE = new Name("Annot");
    public static final Name SUBTYPE_LINK = new Name("Link");
    public static final Name SUBTYPE_LINE = new Name("Line");
    public static final Name SUBTYPE_SQUARE = new Name("Square");
    public static final Name SUBTYPE_CIRCLE = new Name("Circle");
    public static final Name SUBTYPE_POLYGON = new Name("Polygon");
    public static final Name SUBTYPE_POLYLINE = new Name("PolyLine");
    public static final Name SUBTYPE_HIGHLIGHT = new Name("Highlight");
    public static final Name SUBTYPE_POPUP = new Name("Popup");
    public static final Name SUBTYPE_WIDGET = new Name("Widget");
    public static final Name SUBTYPE_INK = new Name("Ink");
    public static final Name SUBTYPE_FREE_TEXT = new Name("FreeText");
    public static final Name SUBTYPE_TEXT = new Name("Text");
    public static final Name BORDER_STYLE_KEY = new Name("BS");
    public static final Name RECTANGLE_KEY = new Name("Rect");
    public static final Name ACTION_KEY = new Name("A");
    public static final Name PARENT_PAGE_KEY = new Name(Constants._TAG_P);
    public static final Name BORDER_KEY = new Name("Border");
    public static final Name FLAG_KEY = new Name(PdfOps.F_TOKEN);
    public static final Name COLOR_KEY = new Name("C");
    public static final Name APPEARANCE_STREAM_KEY = new Name("AP");
    public static final Name APPEARANCE_STATE_KEY = new Name("AS");
    public static final Name APPEARANCE_STREAM_NORMAL_KEY = new Name("N");
    public static final Name APPEARANCE_STREAM_ROLLOVER_KEY = new Name("R");
    public static final Name APPEARANCE_STREAM_DOWN_KEY = new Name(PdfOps.D_TOKEN);
    public static final Name CONTENTS_KEY = new Name("Contents");
    public static final Name M_KEY = new Name(PdfOps.M_TOKEN);
    public static final Name NM_KEY = new Name("NM");
    protected static boolean compressAppearanceStream = true;

    public abstract void resetAppearanceStream(double d2, double d3, AffineTransform affineTransform);

    public static Annotation buildAnnotation(Library library, HashMap hashMap) {
        Annotation annot = null;
        Name subType = (Name) hashMap.get(SUBTYPE_KEY);
        if (subType != null) {
            if (subType.equals(SUBTYPE_LINK)) {
                annot = new LinkAnnotation(library, hashMap);
            } else if (subType.equals(TextMarkupAnnotation.SUBTYPE_HIGHLIGHT) || subType.equals(TextMarkupAnnotation.SUBTYPE_STRIKE_OUT) || subType.equals(TextMarkupAnnotation.SUBTYPE_UNDERLINE)) {
                annot = new TextMarkupAnnotation(library, hashMap);
            } else if (subType.equals(SUBTYPE_LINE)) {
                annot = new LineAnnotation(library, hashMap);
            } else if (subType.equals(SUBTYPE_SQUARE)) {
                annot = new SquareAnnotation(library, hashMap);
            } else if (subType.equals(SUBTYPE_CIRCLE)) {
                annot = new CircleAnnotation(library, hashMap);
            } else if (subType.equals(SUBTYPE_INK)) {
                annot = new InkAnnotation(library, hashMap);
            } else if (subType.equals(SUBTYPE_FREE_TEXT)) {
                annot = new FreeTextAnnotation(library, hashMap);
            } else if (subType.equals(SUBTYPE_TEXT)) {
                annot = new TextAnnotation(library, hashMap);
            } else if (subType.equals(SUBTYPE_POPUP)) {
                annot = new PopupAnnotation(library, hashMap);
            } else if (subType.equals(SUBTYPE_WIDGET)) {
                annot = new WidgetAnnotation(library, hashMap);
            }
        }
        if (annot == null) {
            annot = new GenericAnnotation(library, hashMap);
        }
        return annot;
    }

    public Annotation(Library l2, HashMap h2) {
        super(l2, h2);
        this.matrix = new AffineTransform();
        this.bbox = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v32, types: [byte[], byte[][]] */
    @Override // org.icepdf.core.pobjects.Dictionary
    public void init() {
        Object AS;
        super.init();
        this.subtype = (Name) getObject(SUBTYPE_KEY);
        this.securityManager = this.library.getSecurityManager();
        this.content = this.library.getString(this.entries, CONTENTS_KEY);
        this.canDrawBorder = (SUBTYPE_LINE.equals(this.subtype) || SUBTYPE_CIRCLE.equals(this.subtype) || SUBTYPE_SQUARE.equals(this.subtype) || SUBTYPE_POLYGON.equals(this.subtype) || SUBTYPE_POLYLINE.equals(this.subtype)) ? false : true;
        Object BS = getObject(BORDER_STYLE_KEY);
        if (BS != null) {
            if (BS instanceof HashMap) {
                this.borderStyle = new BorderStyle(this.library, (HashMap) BS);
            } else if (BS instanceof BorderStyle) {
                this.borderStyle = (BorderStyle) BS;
            }
        } else {
            HashMap<Name, Object> borderMap = new HashMap<>();
            Object borderObject = getObject(BORDER_KEY);
            if (borderObject != null && (borderObject instanceof List)) {
                this.border = (List) borderObject;
                if (this.border.size() == 3) {
                    borderMap.put(BorderStyle.BORDER_STYLE_KEY, BorderStyle.BORDER_STYLE_SOLID);
                    borderMap.put(BorderStyle.BORDER_WIDTH_KEY, this.border.get(2));
                } else if (this.border.size() == 4) {
                    borderMap.put(BorderStyle.BORDER_STYLE_KEY, BorderStyle.BORDER_STYLE_DASHED);
                    borderMap.put(BorderStyle.BORDER_WIDTH_KEY, this.border.get(2));
                    borderMap.put(BorderStyle.BORDER_DASH_KEY, Arrays.asList(Float.valueOf(3.0f)));
                }
            } else {
                borderMap.put(BorderStyle.BORDER_STYLE_KEY, BorderStyle.BORDER_STYLE_SOLID);
                borderMap.put(BorderStyle.BORDER_WIDTH_KEY, Float.valueOf(0.0f));
            }
            this.borderStyle = new BorderStyle(this.library, borderMap);
            this.entries.put(BORDER_STYLE_KEY, this.borderStyle);
        }
        this.color = null;
        List C2 = (List) getObject(COLOR_KEY);
        if (C2 != null && C2.size() >= 3) {
            float red = ((Number) C2.get(0)).floatValue();
            float green = ((Number) C2.get(1)).floatValue();
            float blue = ((Number) C2.get(2)).floatValue();
            this.color = new Color(Math.max(0.0f, Math.min(1.0f, red)), Math.max(0.0f, Math.min(1.0f, green)), Math.max(0.0f, Math.min(1.0f, blue)));
        }
        Object value = this.library.getObject(this.entries, M_KEY);
        if (value != null && (value instanceof StringObject)) {
            StringObject text = (StringObject) value;
            this.modifiedDate = new PDate(this.securityManager, text.getDecryptedLiteralString(this.securityManager));
        }
        Object AP = getObject(APPEARANCE_STREAM_KEY);
        if (AP instanceof HashMap) {
            Object N2 = this.library.getObject((HashMap) AP, APPEARANCE_STREAM_NORMAL_KEY);
            if ((N2 instanceof HashMap) && (AS = getObject(APPEARANCE_STATE_KEY)) != null && (AS instanceof Name)) {
                N2 = this.library.getObject((HashMap) N2, (Name) AS);
            }
            if (N2 instanceof Form) {
                Form form = (Form) N2;
                form.init();
                this.shapes = form.getShapes();
                this.matrix = form.getMatrix();
                this.bbox = form.getBBox();
                return;
            }
            if (N2 instanceof Stream) {
                Stream stream = (Stream) N2;
                Resources res = this.library.getResources(stream.getEntries(), RESOURCES_VALUE);
                this.bbox = this.library.getRectangle(stream.getEntries(), BBOX_VALUE);
                this.matrix = new AffineTransform();
                try {
                    this.shapes = ContentParserFactory.getInstance().getContentParser(this.library, res).parse(new byte[]{stream.getDecodedStreamBytes()}, null).getShapes();
                } catch (Exception e2) {
                    this.shapes = new Shapes();
                    logger.log(Level.FINE, "Error initializing annotation content stream.", (Throwable) e2);
                }
            }
        }
    }

    public Name getSubType() {
        return this.library.getName(this.entries, SUBTYPE_KEY);
    }

    public void setSubtype(Name subtype) {
        this.entries.put(SUBTYPE_KEY, subtype);
        this.subtype = subtype;
    }

    public Rectangle2D.Float getUserSpaceRectangle() {
        if (this.userSpaceRectangle == null) {
            Object tmp = getObject(RECTANGLE_KEY);
            if (tmp instanceof List) {
                this.userSpaceRectangle = this.library.getRectangle(this.entries, RECTANGLE_KEY);
            }
        }
        return this.userSpaceRectangle;
    }

    public void setBBox(Rectangle bbox) {
        this.bbox = bbox;
    }

    public Rectangle2D getBbox() {
        return this.bbox;
    }

    public Stroke getBorderStyleStroke() {
        if (this.borderStyle.isStyleDashed()) {
            return new BasicStroke(this.borderStyle.getStrokeWidth(), 0, 0, this.borderStyle.getStrokeWidth() * 2.0f, this.borderStyle.getDashArray(), 0.0f);
        }
        return new BasicStroke(this.borderStyle.getStrokeWidth());
    }

    public void setUserSpaceRectangle(Rectangle2D.Float rect) {
        if (this.userSpaceRectangle != null && rect != null) {
            this.userSpaceRectangle = new Rectangle2D.Float(rect.f12404x, rect.f12405y, rect.width, rect.height);
            this.entries.put(RECTANGLE_KEY, PRectangle.getPRectangleVector(this.userSpaceRectangle));
        }
    }

    public Action getAction() {
        Object tmp = this.library.getDictionary(this.entries, ACTION_KEY);
        if (tmp != null) {
            Action action = Action.buildAction(this.library, (HashMap) tmp);
            if (action != null && this.library.isReference(this.entries, ACTION_KEY)) {
                action.setPObjectReference(this.library.getReference(this.entries, ACTION_KEY));
            }
            return action;
        }
        Object tmp2 = getObject(ACTION_KEY);
        if (tmp2 != null && (tmp2 instanceof Action)) {
            return (Action) tmp2;
        }
        return null;
    }

    public Action addAction(Action action) {
        if (action.getPObjectReference() == null) {
            logger.severe("Addition of action was rejected null Object reference " + ((Object) action));
            return null;
        }
        StateManager stateManager = this.library.getStateManager();
        boolean isDestKey = getObject(LinkAnnotation.DESTINATION_KEY) != null;
        if (getObject(ACTION_KEY) != null) {
            boolean isReference = this.library.isReference(getEntries(), ACTION_KEY);
            if (isReference) {
                Action oldAction = (Action) action.getObject(ACTION_KEY);
                oldAction.setDeleted(true);
                stateManager.addChange(new PObject(oldAction, oldAction.getPObjectReference()));
            } else {
                getEntries().remove(ACTION_KEY);
                stateManager.addChange(new PObject(this, getPObjectReference()));
            }
        }
        this.entries.put(ACTION_KEY, action.getPObjectReference());
        stateManager.addChange(new PObject(this, getPObjectReference()));
        if (isDestKey && (this instanceof LinkAnnotation)) {
            getEntries().remove(LinkAnnotation.DESTINATION_KEY);
            stateManager.addChange(new PObject(this, getPObjectReference()));
        }
        action.setNew(true);
        stateManager.addChange(new PObject(action, action.getPObjectReference()));
        this.library.addObject(action, action.getPObjectReference());
        return action;
    }

    public boolean deleteAction(Action action) {
        StateManager stateManager = this.library.getStateManager();
        if (getObject(ACTION_KEY) != null) {
            Action currentAction = getAction();
            if (currentAction.similar(action)) {
                getEntries().remove(ACTION_KEY);
                currentAction.setDeleted(true);
                stateManager.addChange(new PObject(currentAction, currentAction.getPObjectReference()));
                stateManager.addChange(new PObject(this, getPObjectReference()));
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean updateAction(Action action) {
        StateManager stateManager = this.library.getStateManager();
        if (getObject(ACTION_KEY) != null) {
            Action currentAction = getAction();
            if (!currentAction.similar(action)) {
                stateManager.addChange(new PObject(action, action.getPObjectReference()));
                currentAction.setDeleted(true);
                stateManager.addChange(new PObject(currentAction, currentAction.getPObjectReference()));
            }
            this.entries.put(ACTION_KEY, action.getPObjectReference());
            stateManager.addChange(new PObject(action, action.getPObjectReference()));
            return true;
        }
        return false;
    }

    public boolean allowScreenNormalMode() {
        return allowScreenOrPrintRenderingOrInteraction() && !getFlagNoView();
    }

    public boolean allowScreenRolloverMode() {
        return allowScreenOrPrintRenderingOrInteraction() && (!getFlagNoView() || getFlagToggleNoView()) && !getFlagReadOnly();
    }

    public boolean allowScreenDownMode() {
        return allowScreenOrPrintRenderingOrInteraction() && (!getFlagNoView() || getFlagToggleNoView()) && !getFlagReadOnly();
    }

    public boolean allowPrintNormalMode() {
        return allowScreenOrPrintRenderingOrInteraction() && getFlagPrint();
    }

    public boolean allowAlterProperties() {
        return !getFlagLocked();
    }

    public void setBorderStyle(BorderStyle borderStyle) {
        this.borderStyle = borderStyle;
        this.entries.put(BORDER_STYLE_KEY, this.borderStyle);
    }

    public BorderStyle getBorderStyle() {
        return this.borderStyle;
    }

    public List<Number> getBorder() {
        return this.border;
    }

    public Annotation getParentAnnotation() {
        Annotation parent = null;
        Object ob = getObject(PARENT_KEY);
        if (ob instanceof Reference) {
            ob = this.library.getObject((Reference) ob);
        }
        if (ob instanceof Annotation) {
            parent = (Annotation) ob;
        } else if (ob instanceof HashMap) {
            parent = buildAnnotation(this.library, (HashMap) ob);
        }
        return parent;
    }

    public Page getPage() {
        Annotation annot;
        Page page = (Page) getObject(PARENT_PAGE_KEY);
        if (page == null && (annot = getParentAnnotation()) != null) {
            page = annot.getPage();
        }
        return page;
    }

    public int getBorderType() {
        if (this.borderStyle != null) {
            if (this.borderStyle.getStrokeWidth() > 0.0f) {
                return 1;
            }
            return 0;
        }
        if (this.border != null && this.border.size() >= 3 && ((Number) this.border.get(2)).floatValue() > 0.0f) {
            return 1;
        }
        return 0;
    }

    public Name getLineStyle() {
        if (this.borderStyle != null) {
            return this.borderStyle.getBorderStyle();
        }
        if (this.border != null) {
            if (this.border.size() > 3) {
                return BorderStyle.BORDER_STYLE_DASHED;
            }
            if (((Number) this.border.get(2)).floatValue() > 1.0f) {
                return BorderStyle.BORDER_STYLE_SOLID;
            }
        }
        return BorderStyle.BORDER_STYLE_SOLID;
    }

    public float getLineThickness() {
        if (this.borderStyle != null) {
            return this.borderStyle.getStrokeWidth();
        }
        if (this.border != null && this.border.size() >= 3) {
            return ((Number) this.border.get(2)).floatValue();
        }
        return 0.0f;
    }

    public boolean isBorder() {
        boolean borderWidth = false;
        Object border = getObject(BORDER_KEY);
        if (border != null && (border instanceof List)) {
            List borderProps = (List) border;
            if (borderProps.size() == 3) {
                borderWidth = ((Number) borderProps.get(2)).floatValue() > 0.0f;
            }
        }
        return (getBorderStyle() != null && getBorderStyle().getStrokeWidth() > 0.0f) || borderWidth;
    }

    public void render(Graphics2D origG, int renderHintType, float totalRotation, float userZoom, boolean tabSelected) {
        float unRotation;
        if (!allowScreenOrPrintRenderingOrInteraction()) {
            return;
        }
        if (renderHintType == 1 && !allowScreenNormalMode()) {
            return;
        }
        if (renderHintType == 2 && !allowPrintNormalMode()) {
            return;
        }
        Rectangle2D.Float rect = getUserSpaceRectangle();
        AffineTransform oldAT = origG.getTransform();
        Shape oldClip = origG.getClip();
        AffineTransform at2 = new AffineTransform(oldAT);
        at2.translate(rect.getMinX(), rect.getMinY());
        boolean noRotate = getFlagNoRotate();
        if (noRotate) {
            float f2 = -totalRotation;
            while (true) {
                unRotation = f2;
                if (unRotation >= 0.0f) {
                    break;
                } else {
                    f2 = unRotation + 360.0f;
                }
            }
            while (unRotation > 360.0f) {
                unRotation -= 360.0f;
            }
            if (unRotation == -0.0f) {
                unRotation = 0.0f;
            }
            if (unRotation != 0.0d) {
                double radians = Math.toRadians(unRotation);
                AffineTransform rotationTransform = AffineTransform.getRotateInstance(radians);
                Point2D.Double origTopLeftCorner = new Point2D.Double(0.0d, Math.abs(rect.getHeight()));
                Point2D rotatedTopLeftCorner = rotationTransform.transform(origTopLeftCorner, null);
                at2.translate(origTopLeftCorner.getX() - rotatedTopLeftCorner.getX(), origTopLeftCorner.getY() - rotatedTopLeftCorner.getY());
                at2.rotate(radians);
            }
        }
        boolean noZoom = getFlagNoZoom();
        if (noZoom) {
            double scaleY = Math.abs(at2.getScaleY());
            if (scaleY != 1.0d) {
                double scaleX = Math.abs(at2.getScaleX());
                double rectHeight = Math.abs(rect.getHeight());
                double resizedY = rectHeight * ((scaleY - 1.0d) / scaleY);
                at2.translate(0.0d, resizedY);
                at2.scale(1.0d / scaleX, 1.0d / scaleY);
            }
        }
        GraphicsRenderingHints grh = GraphicsRenderingHints.getDefault();
        origG.setRenderingHints(grh.getRenderingHints(renderHintType));
        origG.setTransform(at2);
        Shape preAppearanceStreamClip = origG.getClip();
        origG.clip(deriveDrawingRectangle());
        renderAppearanceStream(origG);
        origG.setTransform(at2);
        origG.setClip(preAppearanceStreamClip);
        if (tabSelected) {
            renderBorderTabSelected(origG);
        } else {
            renderBorder(origG);
        }
        origG.setTransform(oldAT);
        origG.setClip(oldClip);
    }

    protected void renderAppearanceStream(Graphics2D g2) {
        if (this.shapes != null) {
            if (this.bbox == null) {
                this.bbox = this.userSpaceRectangle;
            }
            Rectangle2D tBbox = this.matrix.createTransformedShape(this.bbox).getBounds2D();
            Rectangle2D rect = getUserSpaceRectangle();
            AffineTransform tAs = AffineTransform.getScaleInstance(rect.getWidth() / tBbox.getWidth(), rect.getHeight() / tBbox.getHeight());
            tAs.concatenate(this.matrix);
            g2.transform(tAs);
            this.shapes.paint(g2);
        }
    }

    protected void renderBorder(Graphics2D g2) {
        if ((this instanceof SquareAnnotation) || (this instanceof CircleAnnotation) || (this instanceof LineAnnotation) || (this instanceof FreeTextAnnotation) || (this instanceof InkAnnotation)) {
            return;
        }
        Color borderColor = getColor();
        if (borderColor != null) {
            g2.setColor(borderColor);
        }
        BorderStyle bs2 = getBorderStyle();
        if (bs2 != null) {
            float width = bs2.getStrokeWidth();
            if (width > 0.0f && borderColor != null && this.canDrawBorder) {
                Rectangle2D.Float jrect = deriveBorderDrawingRectangle(width);
                if (bs2.isStyleSolid()) {
                    g2.setStroke(new BasicStroke(width));
                    g2.draw(jrect);
                    return;
                }
                if (bs2.isStyleDashed()) {
                    BasicStroke stroke = new BasicStroke(width, 0, 0, width * 2.0f, bs2.getDashArray(), 0.0f);
                    g2.setStroke(stroke);
                    g2.draw(jrect);
                    return;
                }
                if (bs2.isStyleBeveled()) {
                    Rectangle2D.Float jrect2 = deriveDrawingRectangle();
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.setColor(BorderStyle.LIGHT);
                    Line2D.Double line = new Line2D.Double(jrect2.getMinX() + 1.0d, jrect2.getMaxY() - 1.0d, jrect2.getMaxX() - 2.0d, jrect2.getMaxY() - 1.0d);
                    g2.draw(line);
                    Line2D.Double line2 = new Line2D.Double(jrect2.getMinX() + 1.0d, jrect2.getMinY() + 2.0d, jrect2.getMinX() + 1.0d, jrect2.getMaxY() - 1.0d);
                    g2.draw(line2);
                    g2.setColor(BorderStyle.LIGHTEST);
                    Line2D.Double line3 = new Line2D.Double(jrect2.getMinX() + 2.0d, jrect2.getMaxY() - 2.0d, jrect2.getMaxX() - 3.0d, jrect2.getMaxY() - 2.0d);
                    g2.draw(line3);
                    Line2D.Double line4 = new Line2D.Double(jrect2.getMinX() + 2.0d, jrect2.getMinY() + 3.0d, jrect2.getMinX() + 2.0d, jrect2.getMaxY() - 2.0d);
                    g2.draw(line4);
                    g2.setColor(BorderStyle.DARK);
                    Line2D.Double line5 = new Line2D.Double(jrect2.getMinX() + 2.0d, jrect2.getMinY() + 2.0d, jrect2.getMaxX() - 2.0d, jrect2.getMinY() + 2.0d);
                    g2.draw(line5);
                    Line2D.Double line6 = new Line2D.Double(jrect2.getMaxX() - 2.0d, jrect2.getMinY() + 2.0d, jrect2.getMaxX() - 2.0d, jrect2.getMaxY() - 2.0d);
                    g2.draw(line6);
                    g2.setColor(BorderStyle.DARKEST);
                    Line2D.Double line7 = new Line2D.Double(jrect2.getMinX() + 1.0d, jrect2.getMinY() + 1.0d, jrect2.getMaxX() - 1.0d, jrect2.getMinY() + 1.0d);
                    g2.draw(line7);
                    Line2D.Double line8 = new Line2D.Double(jrect2.getMaxX() - 1.0d, jrect2.getMinY() + 1.0d, jrect2.getMaxX() - 1.0d, jrect2.getMaxY() - 1.0d);
                    g2.draw(line8);
                    return;
                }
                if (!bs2.isStyleInset()) {
                    if (bs2.isStyleUnderline()) {
                        g2.setStroke(new BasicStroke(width));
                        Line2D.Double line9 = new Line2D.Double(jrect.getMinX(), jrect.getMinY(), jrect.getMaxX(), jrect.getMinY());
                        g2.draw(line9);
                        return;
                    }
                    return;
                }
                Rectangle2D.Float jrect3 = deriveDrawingRectangle();
                g2.setStroke(new BasicStroke(1.0f));
                g2.setColor(BorderStyle.DARK);
                Line2D.Double line10 = new Line2D.Double(jrect3.getMinX() + 1.0d, jrect3.getMaxY() - 1.0d, jrect3.getMaxX() - 1.0d, jrect3.getMaxY() - 1.0d);
                g2.draw(line10);
                Line2D.Double line11 = new Line2D.Double(jrect3.getMinX() + 1.0d, jrect3.getMinY() + 1.0d, jrect3.getMinX() + 1.0d, jrect3.getMaxY() - 1.0d);
                g2.draw(line11);
                g2.setColor(BorderStyle.DARKEST);
                Line2D.Double line12 = new Line2D.Double(jrect3.getMinX() + 2.0d, jrect3.getMaxY() - 2.0d, jrect3.getMaxX() - 2.0d, jrect3.getMaxY() - 2.0d);
                g2.draw(line12);
                Line2D.Double line13 = new Line2D.Double(jrect3.getMinX() + 2.0d, jrect3.getMinY() + 2.0d, jrect3.getMinX() + 2.0d, jrect3.getMaxY() - 2.0d);
                g2.draw(line13);
                g2.setColor(BorderStyle.LIGHTEST);
                Line2D.Double line14 = new Line2D.Double(jrect3.getMinX() + 3.0d, jrect3.getMinY() + 2.0d, jrect3.getMaxX() - 2.0d, jrect3.getMinY() + 2.0d);
                g2.draw(line14);
                Line2D.Double line15 = new Line2D.Double(jrect3.getMaxX() - 2.0d, jrect3.getMinY() + 2.0d, jrect3.getMaxX() - 2.0d, jrect3.getMaxY() - 3.0d);
                g2.draw(line15);
                g2.setColor(BorderStyle.LIGHT);
                Line2D.Double line16 = new Line2D.Double(jrect3.getMinX() + 2.0d, jrect3.getMinY() + 1.0d, jrect3.getMaxX() - 1.0d, jrect3.getMinY() + 1.0d);
                g2.draw(line16);
                Line2D.Double line17 = new Line2D.Double(jrect3.getMaxX() - 1.0d, jrect3.getMinY() + 1.0d, jrect3.getMaxX() - 1.0d, jrect3.getMaxY() - 2.0d);
                g2.draw(line17);
                return;
            }
            return;
        }
        List borderVector = (List) getObject(BORDER_KEY);
        if (borderVector != null) {
            if (borderColor != null) {
                float horizRadius = 0.0f;
                float vertRadius = 0.0f;
                float width2 = 1.0f;
                float[] dashArray = null;
                if (borderVector.size() >= 1) {
                    horizRadius = ((Number) borderVector.get(0)).floatValue();
                }
                if (borderVector.size() >= 2) {
                    vertRadius = ((Number) borderVector.get(1)).floatValue();
                }
                if (borderVector.size() >= 3) {
                    width2 = ((Number) borderVector.get(2)).floatValue();
                }
                if (borderVector.size() >= 4) {
                    Object dashObj = borderVector.get(3);
                    if (dashObj instanceof Number) {
                        width2 = 0.0f;
                    } else if (dashObj instanceof List) {
                        List dashVector = (List) borderVector.get(3);
                        int sz = dashVector.size();
                        dashArray = new float[sz];
                        for (int i2 = 0; i2 < sz; i2++) {
                            Number num = (Number) dashVector.get(i2);
                            dashArray[i2] = num.floatValue();
                        }
                    }
                }
                if (width2 > 0.0f) {
                    Rectangle2D.Float jrect4 = deriveBorderDrawingRectangle(width2);
                    RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double(jrect4.getX(), jrect4.getY(), jrect4.getWidth(), jrect4.getHeight(), horizRadius, vertRadius);
                    BasicStroke stroke2 = new BasicStroke(width2, 0, 0, 10.0f, dashArray, 0.0f);
                    g2.setStroke(stroke2);
                    g2.draw(roundRect);
                    return;
                }
                return;
            }
            return;
        }
        if (borderColor != null && SUBTYPE_LINK.equals(this.subtype)) {
            Rectangle2D.Float jrect5 = deriveBorderDrawingRectangle(1.0f);
            g2.setStroke(new BasicStroke(1.0f));
            g2.draw(jrect5);
        }
    }

    protected void renderBorderTabSelected(Graphics2D g2) {
        Rectangle2D.Float jrect = deriveBorderDrawingRectangle(1.0f);
        g2.setColor(Color.black);
        float[] dashArray = {2.0f};
        BasicStroke stroke = new BasicStroke(1.0f, 0, 0, 10.0f, dashArray, 0.0f);
        g2.setStroke(stroke);
        g2.draw(jrect);
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = new Color(color.getRGB());
        float[] compArray = new float[3];
        this.color.getColorComponents(compArray);
        List<Float> colorValues = new ArrayList<>(compArray.length);
        for (float comp : compArray) {
            colorValues.add(Float.valueOf(comp));
        }
        this.entries.put(COLOR_KEY, colorValues);
    }

    protected Rectangle2D.Float deriveDrawingRectangle() {
        Rectangle2D.Float origRect = getUserSpaceRectangle();
        Rectangle2D.Float jrect = new Rectangle2D.Float(origRect.f12404x, origRect.f12405y, origRect.width, origRect.height);
        jrect.f12404x = 0.0f;
        jrect.f12405y = 0.0f;
        return jrect;
    }

    private Rectangle2D.Float deriveBorderDrawingRectangle(float borderWidth) {
        Rectangle2D.Float jrect = deriveDrawingRectangle();
        float halfBorderWidth = borderWidth / 2.0f;
        double minX = jrect.getMinX() + halfBorderWidth;
        double minY = jrect.getMinY() + halfBorderWidth;
        double maxX = jrect.getMaxX() - halfBorderWidth;
        double maxY = jrect.getMaxY() - halfBorderWidth;
        jrect.setFrameFromDiagonal(minX, minY, maxX, maxY);
        return jrect;
    }

    protected boolean allowScreenOrPrintRenderingOrInteraction() {
        return (getFlagHidden() || (getFlagInvisible() && isSupportedAnnotationType())) ? false : true;
    }

    protected boolean isSupportedAnnotationType() {
        return true;
    }

    public boolean getFlagInvisible() {
        return (getInt(FLAG_KEY) & 1) != 0;
    }

    public boolean getFlagHidden() {
        return (getInt(FLAG_KEY) & 2) != 0;
    }

    public boolean getFlagPrint() {
        return (getInt(FLAG_KEY) & 4) != 0;
    }

    public boolean getFlagNoZoom() {
        return (getInt(FLAG_KEY) & 8) != 0;
    }

    public boolean getFlagNoRotate() {
        return (getInt(FLAG_KEY) & 16) != 0;
    }

    public boolean getFlagNoView() {
        return (getInt(FLAG_KEY) & 32) != 0;
    }

    public boolean getFlagReadOnly() {
        return (getInt(FLAG_KEY) & 64) != 0;
    }

    public boolean getFlagToggleNoView() {
        return (getInt(FLAG_KEY) & 256) != 0;
    }

    public boolean getFlagLockedContents() {
        return (getInt(FLAG_KEY) & 512) != 0;
    }

    public boolean getFlagLocked() {
        return (getInt(FLAG_KEY) & 128) != 0;
    }

    public void setFlag(int flagKey, boolean enable) {
        int flag = getInt(FLAG_KEY);
        boolean isEnabled = (flag & flagKey) != 0;
        if (!enable && isEnabled) {
            this.entries.put(FLAG_KEY, Integer.valueOf(flag ^ flagKey));
        } else if (enable && !isEnabled) {
            this.entries.put(FLAG_KEY, Integer.valueOf(flag | flagKey));
        }
    }

    public void setModifiedDate(String modifiedDate) {
        this.entries.put(M_KEY, new LiteralStringObject(modifiedDate));
        this.modifiedDate = new PDate(this.securityManager, modifiedDate);
    }

    public boolean hasAppearanceStream() {
        return this.library.getObject(this.entries, APPEARANCE_STREAM_KEY) != null;
    }

    public Stream getAppearanceStream() {
        Object AS;
        Object AP = getObject(APPEARANCE_STREAM_KEY);
        if (AP instanceof HashMap) {
            Object N2 = this.library.getObject((HashMap) AP, APPEARANCE_STREAM_NORMAL_KEY);
            if ((N2 instanceof HashMap) && (AS = getObject(APPEARANCE_STATE_KEY)) != null && (AS instanceof Name)) {
                N2 = this.library.getObject((HashMap) N2, (Name) AS);
            }
            if (N2 instanceof Stream) {
                return (Stream) N2;
            }
            return null;
        }
        return null;
    }

    public String getContents() {
        return this.content;
    }

    public void setContents(String content) {
        this.content = content;
        this.entries.put(CONTENTS_KEY, new LiteralStringObject(content));
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ANNOTATION= {");
        Set keys = this.entries.keySet();
        for (Object key : keys) {
            Object value = this.entries.get(key);
            sb.append(key.toString());
            sb.append('=');
            if (value == null) {
                sb.append(FXMLLoader.NULL_KEYWORD);
            } else if (value instanceof StringObject) {
                sb.append(((StringObject) value).getDecryptedLiteralString(this.library.securityManager));
            } else {
                sb.append(value.toString());
            }
            sb.append(',');
        }
        sb.append('}');
        if (getPObjectReference() != null) {
            sb.append(sun.security.pkcs11.wrapper.Constants.INDENT);
            sb.append((Object) getPObjectReference());
        }
        for (int i2 = sb.length() - 1; i2 >= 0; i2--) {
            if (sb.charAt(i2) < ' ' || sb.charAt(i2) >= 127) {
                sb.deleteCharAt(i2);
            }
        }
        return sb.toString();
    }

    public void syncBBoxToUserSpaceRectangle(Rectangle2D bbox) {
        this.bbox = bbox;
        Rectangle2D tBbox = this.matrix.createTransformedShape(bbox).getBounds2D();
        setUserSpaceRectangle(new Rectangle2D.Float((float) tBbox.getX(), (float) tBbox.getY(), (float) tBbox.getWidth(), (float) tBbox.getHeight()));
    }

    public void resetAppearanceStream(AffineTransform pageSpace) {
        resetAppearanceStream(0.0d, 0.0d, pageSpace);
    }

    public Shapes getShapes() {
        return this.shapes;
    }

    public static void setCompressAppearanceStream(boolean compressAppearanceStream2) {
        compressAppearanceStream = compressAppearanceStream2;
    }
}
