package org.icepdf.core.pobjects;

import com.sun.org.glassfish.external.amx.AMX;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.events.PageInitializingEvent;
import org.icepdf.core.events.PageLoadingEvent;
import org.icepdf.core.events.PageLoadingListener;
import org.icepdf.core.events.PagePaintingEvent;
import org.icepdf.core.events.PaintPageEvent;
import org.icepdf.core.events.PaintPageListener;
import org.icepdf.core.io.SeekableInput;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.FreeTextAnnotation;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.graphics.WatermarkCallback;
import org.icepdf.core.pobjects.graphics.text.GlyphText;
import org.icepdf.core.pobjects.graphics.text.LineText;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.pobjects.graphics.text.WordText;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Utils;
import org.icepdf.core.util.content.ContentParser;
import org.icepdf.core.util.content.ContentParserFactory;
import org.icepdf.ri.util.PropertiesManager;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Page.class */
public class Page extends Dictionary {
    private static final Logger logger = Logger.getLogger(Page.class.toString());
    public static final float selectionAlpha = 0.3f;
    public static Color selectionColor;
    public static Color highlightColor;
    public static final Name TYPE;
    public static final Name ANNOTS_KEY;
    public static final Name CONTENTS_KEY;
    public static final Name RESOURCES_KEY;
    public static final Name THUMB_KEY;
    public static final Name PARENT_KEY;
    public static final Name ROTATE_KEY;
    public static final Name MEDIABOX_KEY;
    public static final Name CROPBOX_KEY;
    public static final Name ARTBOX_KEY;
    public static final Name BLEEDBOX_KEY;
    public static final Name TRIMBOX_KEY;
    public static final int BOUNDARY_MEDIABOX = 1;
    public static final int BOUNDARY_CROPBOX = 2;
    public static final int BOUNDARY_BLEEDBOX = 3;
    public static final int BOUNDARY_TRIMBOX = 4;
    public static final int BOUNDARY_ARTBOX = 5;
    private Resources resources;
    private List<Annotation> annotations;
    private List<Stream> contents;
    private Shapes shapes;
    private final List<PaintPageListener> paintPageListeners;
    private final List<PageLoadingListener> pageLoadingListeners;
    private PRectangle mediaBox;
    private PRectangle cropBox;
    private PRectangle bleedBox;
    private PRectangle trimBox;
    private PRectangle artBox;
    private float pageRotation;
    private int pageIndex;
    private int imageCount;
    private boolean pageInitialized;
    private boolean pagePainted;
    private WatermarkCallback watermarkCallback;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.text.selectionColor", "#0077FF");
            int colorValue = ColorUtil.convertColor(color);
            selectionColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("0077FF", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading text selection colour");
            }
        }
        try {
            String color2 = Defs.sysProperty(PropertiesManager.SYSPROPERTY_HIGHLIGHT_COLOR, "#CC00FF");
            int colorValue2 = ColorUtil.convertColor(color2);
            highlightColor = new Color(colorValue2 >= 0 ? colorValue2 : Integer.parseInt("FFF600", 16));
        } catch (NumberFormatException e3) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading text highlight colour");
            }
        }
        TYPE = new Name("Page");
        ANNOTS_KEY = new Name("Annots");
        CONTENTS_KEY = new Name("Contents");
        RESOURCES_KEY = new Name("Resources");
        THUMB_KEY = new Name("Thumb");
        PARENT_KEY = new Name(AMX.ATTR_PARENT);
        ROTATE_KEY = new Name("Rotate");
        MEDIABOX_KEY = new Name("MediaBox");
        CROPBOX_KEY = new Name("CropBox");
        ARTBOX_KEY = new Name("ArtBox");
        BLEEDBOX_KEY = new Name("BleedBox");
        TRIMBOX_KEY = new Name("TrimBox");
    }

    public Page(Library l2, HashMap h2) {
        super(l2, h2);
        this.shapes = null;
        this.paintPageListeners = new ArrayList(8);
        this.pageLoadingListeners = new ArrayList();
        this.pageRotation = 0.0f;
    }

    public boolean isInitiated() {
        return this.inited;
    }

    private void initPageContents() throws InterruptedException {
        Object pageContent = this.library.getObject(this.entries, CONTENTS_KEY);
        if (pageContent instanceof Stream) {
            this.contents = new ArrayList(1);
            Stream tmpStream = (Stream) pageContent;
            tmpStream.setPObjectReference(this.library.getObjectReference(this.entries, CONTENTS_KEY));
            this.contents.add(tmpStream);
            return;
        }
        if (pageContent instanceof List) {
            List conts = (List) pageContent;
            int sz = conts.size();
            this.contents = new ArrayList(Math.max(sz, 1));
            for (int i2 = 0; i2 < sz; i2++) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Page Content initialization thread interrupted");
                }
                Object tmp = this.library.getObject((Reference) conts.get(i2));
                if (tmp instanceof Stream) {
                    Stream tmpStream2 = (Stream) tmp;
                    if (tmpStream2.getRawBytes().length > 0) {
                        tmpStream2.setPObjectReference((Reference) conts.get(i2));
                        this.contents.add(tmpStream2);
                    }
                }
            }
        }
    }

    private void initPageResources() throws InterruptedException {
        Resources res = this.library.getResources(this.entries, RESOURCES_KEY);
        if (res == null) {
            PageTree parent = getParent();
            while (true) {
                PageTree pageTree = parent;
                if (pageTree == null) {
                    break;
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException("Page Resource initialization thread interrupted");
                }
                Resources parentResources = pageTree.getResources();
                if (parentResources != null) {
                    res = parentResources;
                    break;
                }
                parent = pageTree.getParent();
            }
        }
        this.resources = res;
    }

    private void initPageAnnotations() throws InterruptedException {
        Object annots = this.library.getObject(this.entries, ANNOTS_KEY);
        if (annots != null && (annots instanceof List)) {
            List v2 = (List) annots;
            this.annotations = new ArrayList(v2.size() + 1);
            Annotation a2 = null;
            for (int i2 = 0; i2 < v2.size(); i2++) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Page Annotation initialization thread interrupted");
                }
                Object annotObj = v2.get(i2);
                Reference ref = null;
                if (annotObj instanceof Reference) {
                    ref = (Reference) v2.get(i2);
                    annotObj = this.library.getObject(ref);
                }
                if (annotObj instanceof Annotation) {
                    a2 = (Annotation) annotObj;
                } else if (annotObj instanceof HashMap) {
                    a2 = Annotation.buildAnnotation(this.library, (HashMap) annotObj);
                }
                if (ref != null && a2 != null) {
                    a2.setPObjectReference(ref);
                    a2.init();
                }
                this.annotations.add(a2);
            }
        }
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public void setPObjectReference(Reference reference) {
        super.setPObjectReference(reference);
    }

    public void resetInitializedState() {
        this.inited = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v22, types: [byte[], byte[][]] */
    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        try {
        } catch (InterruptedException e2) {
            this.inited = false;
            logger.log(Level.SEVERE, "Page initializing thread interrupted.", (Throwable) e2);
        }
        if (this.inited) {
            return;
        }
        this.pageInitialized = false;
        this.inited = true;
        initPageResources();
        initPageAnnotations();
        initPageContents();
        this.imageCount = this.resources.getImageCount();
        notifyPageLoadingStarted(this.contents.size(), this.resources.getImageCount());
        notifyPageInitializationStarted();
        if (this.contents != null) {
            try {
                ContentParser contentParser = ContentParserFactory.getInstance().getContentParser(this.library, this.resources);
                ?? r0 = new byte[this.contents.size()];
                int max = this.contents.size();
                for (int i2 = 0; i2 < max; i2++) {
                    byte[] stream = this.contents.get(i2).getDecodedStreamBytes();
                    if (stream != null) {
                        r0[i2] = stream;
                    }
                }
                OptionalContent optionalContent = this.library.getCatalog().getOptionalContent();
                if (optionalContent != null) {
                    optionalContent.init();
                }
                if (r0.length > 0) {
                    this.shapes = contentParser.parse(r0, this).getShapes();
                }
            } catch (Exception e3) {
                this.shapes = new Shapes();
                logger.log(Level.FINE, "Error initializing Page.", (Throwable) e3);
            }
        } else {
            this.shapes = new Shapes();
        }
        notifyPageInitializationEnded(this.inited);
    }

    public Thumbnail getThumbnail() {
        Object thumb = this.library.getObject(this.entries, THUMB_KEY);
        if (thumb != null && (thumb instanceof Stream)) {
            return new Thumbnail(this.library, this.entries);
        }
        return null;
    }

    public void requestInterrupt() {
        if (this.shapes != null) {
            this.shapes.interruptPaint();
        }
    }

    public void setWatermarkCallback(WatermarkCallback watermarkCallback) {
        this.watermarkCallback = watermarkCallback;
    }

    public void paint(Graphics g2, int renderHintType, int boundary, float userRotation, float userZoom) {
        paint(g2, renderHintType, boundary, userRotation, userZoom, true, true);
    }

    public void paint(Graphics g2, int renderHintType, int boundary, float userRotation, float userZoom, boolean paintAnnotations, boolean paintSearchHighlight) {
        if (!this.inited) {
            return;
        }
        Graphics2D g22 = (Graphics2D) g2;
        GraphicsRenderingHints grh = GraphicsRenderingHints.getDefault();
        g22.setRenderingHints(grh.getRenderingHints(renderHintType));
        AffineTransform at2 = getPageTransform(boundary, userRotation, userZoom);
        g22.transform(at2);
        AffineTransform prePagePaintState = g22.getTransform();
        PRectangle pageBoundary = getPageBoundary(boundary);
        float x2 = 0.0f - pageBoundary.f12404x;
        float y2 = 0.0f - (pageBoundary.f12405y - pageBoundary.height);
        Color backgroundColor = grh.getPageBackgroundColor(renderHintType);
        if (backgroundColor != null) {
            g22.setColor(backgroundColor);
            g22.fillRect((int) (0.0f - x2), (int) (0.0f - y2), (int) pageBoundary.width, (int) pageBoundary.height);
        }
        Rectangle2D rect = new Rectangle2D.Double(-x2, -y2, pageBoundary.width, pageBoundary.height);
        Shape oldClip = g22.getClip();
        if (oldClip == null) {
            g22.setClip(rect);
        } else {
            Area area = new Area(oldClip);
            area.intersect(new Area(rect));
            g22.setClip(area);
        }
        paintPageContent(g22, renderHintType, userRotation, userZoom, paintAnnotations, paintSearchHighlight);
        notifyPaintPageListeners();
        g22.setTransform(prePagePaintState);
        if (this.watermarkCallback != null) {
            this.watermarkCallback.paintWatermark(g2, this, renderHintType, boundary, userRotation, userZoom);
        }
    }

    public void paintPageContent(Graphics g2, int renderHintType, float userRotation, float userZoom, boolean paintAnnotations, boolean paintSearchHighlight) {
        if (!this.inited) {
            init();
        }
        paintPageContent((Graphics2D) g2, renderHintType, userRotation, userZoom, paintAnnotations, paintSearchHighlight);
    }

    private void paintPageContent(Graphics2D g2, int renderHintType, float userRotation, float userZoom, boolean paintAnnotations, boolean paintSearchHighlight) {
        PageText pageText;
        if (this.shapes != null) {
            this.pagePainted = false;
            notifyPagePaintingStarted(this.shapes.getShapesCount());
            AffineTransform pageTransform = g2.getTransform();
            Shape pageClip = g2.getClip();
            this.shapes.setPageParent(this);
            this.shapes.paint(g2);
            this.shapes.setPageParent(null);
            g2.setTransform(pageTransform);
            g2.setClip(pageClip);
        } else {
            notifyPagePaintingStarted(0);
        }
        if (this.annotations != null && paintAnnotations) {
            float totalRotation = getTotalRotation(userRotation);
            int num = this.annotations.size();
            for (int i2 = 0; i2 < num; i2++) {
                Annotation annotation = this.annotations.get(i2);
                annotation.render(g2, renderHintType, totalRotation, userZoom, false);
            }
        }
        if (paintSearchHighlight && (pageText = getViewText()) != null) {
            g2.setComposite(AlphaComposite.getInstance(3, 0.3f));
            if (pageText.getPageLines() != null) {
                Iterator i$ = pageText.getPageLines().iterator();
                while (i$.hasNext()) {
                    LineText lineText = i$.next();
                    if (lineText != null) {
                        for (WordText wordText : lineText.getWords()) {
                            if (wordText.isHighlighted()) {
                                GeneralPath textPath = new GeneralPath(wordText.getBounds());
                                g2.setColor(highlightColor);
                                g2.fill(textPath);
                            } else {
                                Iterator i$2 = wordText.getGlyphs().iterator();
                                while (i$2.hasNext()) {
                                    GlyphText glyph = i$2.next();
                                    if (glyph.isHighlighted()) {
                                        GeneralPath textPath2 = new GeneralPath(glyph.getBounds());
                                        g2.setColor(highlightColor);
                                        g2.fill(textPath2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.pagePainted = true;
        notifyPagePaintingEnded(this.shapes.isInterrupted());
        notifyPaintPageListeners();
        if (this.imageCount == 0 || (this.pageInitialized && this.pagePainted)) {
            notifyPageLoadingEnded();
        }
    }

    public AffineTransform getPageTransform(int boundary, float userRotation, float userZoom) {
        AffineTransform at2 = new AffineTransform();
        Rectangle2D.Double boundingBox = getBoundingBox(boundary, userRotation, userZoom);
        at2.translate(0.0d, boundingBox.getHeight());
        at2.scale(1.0d, -1.0d);
        at2.scale(userZoom, userZoom);
        float totalRotation = getTotalRotation(userRotation);
        PRectangle pageBoundary = getPageBoundary(boundary);
        if (totalRotation != 0.0f) {
            if (totalRotation == 90.0f) {
                at2.translate(pageBoundary.height, 0.0d);
            } else if (totalRotation == 180.0f) {
                at2.translate(pageBoundary.width, pageBoundary.height);
            } else if (totalRotation == 270.0f) {
                at2.translate(0.0d, pageBoundary.width);
            } else if (totalRotation > 0.0f && totalRotation < 90.0f) {
                double xShift = pageBoundary.height * Math.cos(Math.toRadians(90.0f - totalRotation));
                at2.translate(xShift, 0.0d);
            } else if (totalRotation > 90.0f && totalRotation < 180.0f) {
                double rad = Math.toRadians(180.0f - totalRotation);
                double cosRad = Math.cos(rad);
                double sinRad = Math.sin(rad);
                double xShift2 = (pageBoundary.height * sinRad) + (pageBoundary.width * cosRad);
                double yShift = pageBoundary.height * cosRad;
                at2.translate(xShift2, yShift);
            } else if (totalRotation > 180.0f && totalRotation < 270.0f) {
                double rad2 = Math.toRadians(totalRotation - 180.0f);
                double cosRad2 = Math.cos(rad2);
                double sinRad2 = Math.sin(rad2);
                double xShift3 = pageBoundary.width * cosRad2;
                double yShift2 = (pageBoundary.width * sinRad2) + (pageBoundary.height * cosRad2);
                at2.translate(xShift3, yShift2);
            } else if (totalRotation > 270.0f && totalRotation < 360.0f) {
                double yShift3 = pageBoundary.width * Math.cos(Math.toRadians(totalRotation - 270.0f));
                at2.translate(0.0d, yShift3);
            }
        }
        at2.rotate((totalRotation * 3.141592653589793d) / 180.0d);
        float x2 = 0.0f - pageBoundary.f12404x;
        float y2 = 0.0f - (pageBoundary.f12405y - pageBoundary.height);
        at2.translate(x2, y2);
        return at2;
    }

    public Shape getPageShape(int boundary, float userRotation, float userZoom) {
        AffineTransform at2 = getPageTransform(boundary, userRotation, userZoom);
        PRectangle pageBoundary = getPageBoundary(boundary);
        float x2 = 0.0f - pageBoundary.f12404x;
        float y2 = 0.0f - (pageBoundary.f12405y - pageBoundary.height);
        Rectangle2D rect = new Rectangle2D.Double(-x2, -y2, pageBoundary.width, pageBoundary.height);
        GeneralPath path = new GeneralPath(rect);
        return path.createTransformedShape(at2);
    }

    public Annotation addAnnotation(Annotation newAnnotation) {
        if (!this.inited) {
            try {
                initPageAnnotations();
            } catch (InterruptedException e2) {
                logger.warning("Annotation Initialization interrupted");
            }
        }
        StateManager stateManager = this.library.getStateManager();
        List<Reference> annotations = this.library.getArray(this.entries, ANNOTS_KEY);
        boolean isAnnotAReference = this.library.isReference(this.entries, ANNOTS_KEY);
        if (!isAnnotAReference && annotations != null) {
            annotations.add(newAnnotation.getPObjectReference());
            stateManager.addChange(new PObject(this, getPObjectReference()));
        } else if (isAnnotAReference && annotations != null) {
            annotations.add(newAnnotation.getPObjectReference());
            stateManager.addChange(new PObject(annotations, this.library.getObjectReference(this.entries, ANNOTS_KEY)));
        } else {
            List<Reference> annotsVector = new ArrayList<>(4);
            annotsVector.add(newAnnotation.getPObjectReference());
            PObject annotsPObject = new PObject(annotsVector, stateManager.getNewReferencNumber());
            this.entries.put(ANNOTS_KEY, annotsPObject.getReference());
            this.library.addObject(annotsVector, annotsPObject.getReference());
            stateManager.addChange(new PObject(this, getPObjectReference()));
            stateManager.addChange(annotsPObject);
            this.annotations = new ArrayList();
        }
        newAnnotation.getEntries().put(Annotation.PARENT_PAGE_KEY, getPObjectReference());
        this.annotations.add(newAnnotation);
        this.library.addObject(newAnnotation, newAnnotation.getPObjectReference());
        stateManager.addChange(new PObject(newAnnotation, newAnnotation.getPObjectReference()));
        return newAnnotation;
    }

    public void deleteAnnotation(Annotation annot) {
        if (!this.inited) {
            try {
                initPageAnnotations();
            } catch (InterruptedException e2) {
                logger.warning("Annotation Initialization interupted");
            }
        }
        StateManager stateManager = this.library.getStateManager();
        Object annots = getObject(ANNOTS_KEY);
        boolean isAnnotAReference = this.library.isReference(this.entries, ANNOTS_KEY);
        annot.setDeleted(true);
        Stream nAp = annot.getAppearanceStream();
        if (nAp != null) {
            nAp.setDeleted(true);
            Object tmp = this.library.getObject(nAp.entries, RESOURCES_KEY);
            if (tmp instanceof Resources) {
                Resources resources = (Resources) tmp;
                Dictionary font = resources.getFont(FreeTextAnnotation.EMBEDDED_FONT_NAME);
                font.setDeleted(true);
            }
        }
        if (!annot.isNew() && !isAnnotAReference) {
            stateManager.addChange(new PObject(this, getPObjectReference()));
        } else if (!annot.isNew() && isAnnotAReference) {
            stateManager.addChange(new PObject(annots, this.library.getObjectReference(this.entries, ANNOTS_KEY)));
        } else if (annot.isNew()) {
            stateManager.removeChange(new PObject(annot, annot.getPObjectReference()));
            if (nAp != null) {
                stateManager.removeChange(new PObject(nAp, nAp.getPObjectReference()));
                this.library.removeObject(nAp.getPObjectReference());
            }
        }
        if (annots instanceof List) {
            ((List) annots).remove(annot.getPObjectReference());
        }
        if (this.annotations != null) {
            this.annotations.remove(annot);
        }
        this.library.removeObject(annot.getPObjectReference());
    }

    public boolean updateAnnotation(Annotation annotation) {
        if (annotation == null) {
            return false;
        }
        if (!this.inited) {
            try {
                initPageAnnotations();
            } catch (InterruptedException e2) {
                logger.warning("Annotation Initialization interrupted");
            }
        }
        StateManager stateManager = this.library.getStateManager();
        List<Reference> annotations = (List) this.library.getObject(this.entries, ANNOTS_KEY);
        boolean found = false;
        Iterator i$ = annotations.iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            Reference ref = i$.next();
            if (ref.equals(annotation.getPObjectReference())) {
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        if (stateManager.contains(annotation.getPObjectReference())) {
            stateManager.addChange(new PObject(annotation, annotation.getPObjectReference()));
            return true;
        }
        annotation.getEntries().put(Annotation.PARENT_PAGE_KEY, getPObjectReference());
        this.annotations.add(annotation);
        this.library.addObject(annotation, annotation.getPObjectReference());
        stateManager.addChange(new PObject(annotation, annotation.getPObjectReference()));
        return true;
    }

    protected Reference getParentReference() {
        return (Reference) this.entries.get(PARENT_KEY);
    }

    public PageTree getParent() {
        Object tmp = this.library.getObject(this.entries, PARENT_KEY);
        if (tmp instanceof PageTree) {
            return (PageTree) tmp;
        }
        return null;
    }

    public PDimension getSize(float userRotation) {
        return getSize(2, userRotation, 1.0f);
    }

    public PDimension getSize(float userRotation, float userZoom) {
        return getSize(2, userRotation, userZoom);
    }

    public PDimension getSize(int boundary, float userRotation, float userZoom) {
        float totalRotation = getTotalRotation(userRotation);
        PRectangle pageBoundary = getPageBoundary(boundary);
        float width = pageBoundary.width * userZoom;
        float height = pageBoundary.height * userZoom;
        if (totalRotation != 0.0f && totalRotation != 180.0f) {
            if (totalRotation == 90.0f || totalRotation == 270.0f) {
                width = height;
                height = width;
            } else {
                AffineTransform at2 = new AffineTransform();
                double radians = Math.toRadians(totalRotation);
                at2.rotate(radians);
                Rectangle2D.Double boundingBox = new Rectangle2D.Double(0.0d, 0.0d, 0.0d, 0.0d);
                Point2D.Double src = new Point2D.Double();
                Point2D.Double dst = new Point2D.Double();
                src.setLocation(0.0d, height);
                at2.transform(src, dst);
                boundingBox.add(dst);
                src.setLocation(width, height);
                at2.transform(src, dst);
                boundingBox.add(dst);
                src.setLocation(0.0d, 0.0d);
                at2.transform(src, dst);
                boundingBox.add(dst);
                src.setLocation(width, 0.0d);
                at2.transform(src, dst);
                boundingBox.add(dst);
                width = (float) boundingBox.getWidth();
                height = (float) boundingBox.getHeight();
            }
        }
        return new PDimension(width, height);
    }

    public Rectangle2D.Double getBoundingBox(float userRotation) {
        return getBoundingBox(2, userRotation, 1.0f);
    }

    public Rectangle2D.Double getBoundingBox(float userRotation, float userZoom) {
        return getBoundingBox(2, userRotation, userZoom);
    }

    public Rectangle2D.Double getBoundingBox(int boundary, float userRotation, float userZoom) {
        float totalRotation = getTotalRotation(userRotation);
        PRectangle pageBoundary = getPageBoundary(boundary);
        float width = pageBoundary.width * userZoom;
        float height = pageBoundary.height * userZoom;
        AffineTransform at2 = new AffineTransform();
        double radians = Math.toRadians(totalRotation);
        at2.rotate(radians);
        Rectangle2D.Double boundingBox = new Rectangle2D.Double(0.0d, 0.0d, 0.0d, 0.0d);
        Point2D.Double src = new Point2D.Double();
        Point2D.Double dst = new Point2D.Double();
        src.setLocation(0.0d, height);
        at2.transform(src, dst);
        boundingBox.add(dst);
        src.setLocation(width, height);
        at2.transform(src, dst);
        boundingBox.add(dst);
        src.setLocation(0.0d, 0.0d);
        at2.transform(src, dst);
        boundingBox.add(dst);
        src.setLocation(width, 0.0d);
        at2.transform(src, dst);
        boundingBox.add(dst);
        return boundingBox;
    }

    public PRectangle getPageBoundary(int specifiedBox) {
        PRectangle userSpecifiedBox;
        if (specifiedBox == 1) {
            userSpecifiedBox = (PRectangle) getMediaBox();
        } else if (specifiedBox == 2) {
            userSpecifiedBox = (PRectangle) getCropBox();
        } else if (specifiedBox == 3) {
            userSpecifiedBox = (PRectangle) getBleedBox();
        } else if (specifiedBox == 4) {
            userSpecifiedBox = (PRectangle) getTrimBox();
        } else if (specifiedBox == 5) {
            userSpecifiedBox = (PRectangle) getArtBox();
        } else {
            userSpecifiedBox = (PRectangle) getMediaBox();
        }
        if (userSpecifiedBox == null) {
            userSpecifiedBox = (PRectangle) getMediaBox();
        }
        return userSpecifiedBox;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return "PAGE= " + this.entries.toString();
    }

    public float getTotalRotation(float userRotation) {
        float totalRotation = (getPageRotation() + userRotation) % 360.0f;
        if (totalRotation < 0.0f) {
            totalRotation += 360.0f;
        }
        if (totalRotation >= -0.001f && totalRotation <= 0.001f) {
            return 0.0f;
        }
        if (totalRotation >= 89.99f && totalRotation <= 90.001f) {
            return 90.0f;
        }
        if (totalRotation >= 179.99f && totalRotation <= 180.001f) {
            return 180.0f;
        }
        if (totalRotation >= 269.99f && totalRotation <= 270.001f) {
            return 270.0f;
        }
        return totalRotation;
    }

    private float getPageRotation() {
        Object tmpRotation = this.library.getObject(this.entries, ROTATE_KEY);
        if (tmpRotation != null) {
            this.pageRotation = ((Number) tmpRotation).floatValue();
        } else {
            PageTree parent = getParent();
            while (true) {
                PageTree pageTree = parent;
                if (pageTree == null) {
                    break;
                }
                if (pageTree.isRotationFactor) {
                    this.pageRotation = pageTree.rotationFactor;
                    break;
                }
                parent = pageTree.getParent();
            }
        }
        this.pageRotation = 360.0f - this.pageRotation;
        this.pageRotation %= 360.0f;
        return this.pageRotation;
    }

    public List<Annotation> getAnnotations() {
        if (!this.inited) {
            try {
                initPageAnnotations();
            } catch (InterruptedException e2) {
                logger.finer("Interrupt exception getting annotations. ");
            }
        }
        return this.annotations;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String[] getDecodedContentSteam() {
        String content;
        try {
            initPageContents();
            if (this.contents == null) {
                return null;
            }
            String[] decodedContentStream = new String[this.contents.size()];
            int i2 = 0;
            for (Stream stream : this.contents) {
                Closeable decodedByteArrayInputStream = stream.getDecodedByteArrayInputStream();
                if (decodedByteArrayInputStream instanceof SeekableInput) {
                    content = Utils.getContentFromSeekableInput((SeekableInput) decodedByteArrayInputStream, false);
                } else {
                    content = Utils.getContentAndReplaceInputStream(new InputStream[]{decodedByteArrayInputStream}, false);
                }
                decodedContentStream[i2] = content;
                decodedByteArrayInputStream.close();
                i2++;
            }
            return decodedContentStream;
        } catch (IOException e2) {
            logger.log(Level.SEVERE, "Error closing content stream");
            return null;
        } catch (InterruptedException e3) {
            logger.log(Level.SEVERE, "Error initializing page Contents.", (Throwable) e3);
            return null;
        }
    }

    public Rectangle2D.Float getMediaBox() {
        if (this.mediaBox != null) {
            return this.mediaBox;
        }
        List boxDimensions = (List) this.library.getObject(this.entries, MEDIABOX_KEY);
        if (boxDimensions != null) {
            this.mediaBox = new PRectangle(boxDimensions);
        }
        if (this.mediaBox == null) {
            PageTree pageTree = getParent();
            while (pageTree != null && this.mediaBox == null) {
                this.mediaBox = pageTree.getMediaBox();
                if (this.mediaBox == null) {
                    pageTree = pageTree.getParent();
                }
            }
        }
        return this.mediaBox;
    }

    public Rectangle2D.Float getCropBox() {
        if (this.cropBox != null) {
            return this.cropBox;
        }
        List boxDimensions = (List) this.library.getObject(this.entries, CROPBOX_KEY);
        if (boxDimensions != null) {
            this.cropBox = new PRectangle(boxDimensions);
        }
        boolean isParentCropBox = false;
        if (this.cropBox == null) {
            PageTree parent = getParent();
            while (true) {
                PageTree pageTree = parent;
                if (pageTree == null || this.cropBox != null || pageTree.getCropBox() == null) {
                    break;
                }
                this.cropBox = pageTree.getCropBox();
                if (this.cropBox != null) {
                    isParentCropBox = true;
                }
                parent = pageTree.getParent();
            }
        }
        PRectangle mediaBox = (PRectangle) getMediaBox();
        if ((this.cropBox == null || isParentCropBox) && mediaBox != null) {
            this.cropBox = (PRectangle) mediaBox.clone();
        } else if (this.cropBox != null && mediaBox != null) {
            this.cropBox = mediaBox.createCartesianIntersection(this.cropBox);
        }
        return this.cropBox;
    }

    public Rectangle2D.Float getArtBox() {
        if (this.artBox != null) {
            return this.artBox;
        }
        List boxDimensions = (List) this.library.getObject(this.entries, ARTBOX_KEY);
        if (boxDimensions != null) {
            this.artBox = new PRectangle(boxDimensions);
        }
        if (this.artBox == null) {
            this.artBox = (PRectangle) getCropBox();
        }
        return this.artBox;
    }

    public Rectangle2D.Float getBleedBox() {
        if (this.bleedBox != null) {
            return this.bleedBox;
        }
        List boxDimensions = (List) this.library.getObject(this.entries, BLEEDBOX_KEY);
        if (boxDimensions != null) {
            this.bleedBox = new PRectangle(boxDimensions);
        }
        if (this.bleedBox == null) {
            this.bleedBox = (PRectangle) getCropBox();
        }
        return this.bleedBox;
    }

    public Rectangle2D.Float getTrimBox() {
        if (this.trimBox != null) {
            return this.trimBox;
        }
        List boxDimensions = (List) this.library.getObject(this.entries, TRIMBOX_KEY);
        if (boxDimensions != null) {
            this.trimBox = new PRectangle(boxDimensions);
        }
        if (this.trimBox == null) {
            this.trimBox = (PRectangle) getCropBox();
        }
        return this.trimBox;
    }

    public PageText getViewText() {
        if (!this.inited) {
            init();
        }
        if (this.shapes != null) {
            return this.shapes.getPageText();
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v15, types: [byte[], byte[][]] */
    public synchronized PageText getText() throws InterruptedException {
        if (this.inited && this.shapes != null && this.shapes.getPageText() != null) {
            return this.shapes.getPageText();
        }
        Shapes textBlockShapes = new Shapes();
        if (this.contents == null) {
            initPageContents();
        }
        if (this.resources == null) {
            initPageResources();
        }
        if (this.contents != null) {
            try {
                ContentParser contentParser = ContentParserFactory.getInstance().getContentParser(this.library, this.resources);
                ?? r0 = new byte[this.contents.size()];
                int max = this.contents.size();
                for (int i2 = 0; i2 < max; i2++) {
                    r0[i2] = this.contents.get(i2).getDecodedStreamBytes();
                }
                textBlockShapes = contentParser.parseTextBlocks(r0);
                if (logger.isLoggable(Level.FINER)) {
                    Stack<Object> stack = contentParser.getStack();
                    while (!stack.isEmpty()) {
                        String tmp = stack.pop().toString();
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("STACK=" + tmp);
                        }
                    }
                }
            } catch (Exception e2) {
                logger.log(Level.FINE, "Error getting page text.", (Throwable) e2);
            }
        }
        if (textBlockShapes.getPageText() != null) {
            return textBlockShapes.getPageText();
        }
        return null;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public int getImageCount() {
        return this.imageCount;
    }

    public boolean isPageInitialized() {
        return this.pageInitialized;
    }

    public boolean isPagePainted() {
        return this.pagePainted;
    }

    protected void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public synchronized List<Image> getImages() {
        if (!this.inited) {
            init();
        }
        return this.shapes.getImages();
    }

    public Resources getResources() {
        return this.resources;
    }

    public void addPaintPageListener(PaintPageListener listener) {
        synchronized (this.paintPageListeners) {
            if (!this.paintPageListeners.contains(listener)) {
                this.paintPageListeners.add(listener);
            }
        }
    }

    public void removePaintPageListener(PaintPageListener listener) {
        synchronized (this.paintPageListeners) {
            if (this.paintPageListeners.contains(listener)) {
                this.paintPageListeners.remove(listener);
            }
        }
    }

    public List<PageLoadingListener> getPageLoadingListeners() {
        return this.pageLoadingListeners;
    }

    public void addPageProcessingListener(PageLoadingListener listener) {
        synchronized (this.pageLoadingListeners) {
            if (!this.pageLoadingListeners.contains(listener)) {
                this.pageLoadingListeners.add(listener);
            }
        }
    }

    public void removePageProcessingListener(PageLoadingListener listener) {
        synchronized (this.pageLoadingListeners) {
            if (this.pageLoadingListeners.contains(listener)) {
                this.pageLoadingListeners.remove(listener);
            }
        }
    }

    private void notifyPageLoadingStarted(int contentCount, int imageCount) {
        PageLoadingEvent pageLoadingEvent = new PageLoadingEvent(this, contentCount, imageCount);
        for (int i2 = this.pageLoadingListeners.size() - 1; i2 >= 0; i2--) {
            PageLoadingListener client = this.pageLoadingListeners.get(i2);
            client.pageLoadingStarted(pageLoadingEvent);
        }
    }

    private void notifyPageInitializationStarted() {
        PageInitializingEvent pageLoadingEvent = new PageInitializingEvent(this, false);
        for (int i2 = this.pageLoadingListeners.size() - 1; i2 >= 0; i2--) {
            PageLoadingListener client = this.pageLoadingListeners.get(i2);
            client.pageInitializationStarted(pageLoadingEvent);
        }
    }

    private void notifyPagePaintingStarted(int shapesCount) {
        PagePaintingEvent pageLoadingEvent = new PagePaintingEvent(this, shapesCount);
        for (int i2 = this.pageLoadingListeners.size() - 1; i2 >= 0; i2--) {
            PageLoadingListener client = this.pageLoadingListeners.get(i2);
            client.pagePaintingStarted(pageLoadingEvent);
        }
    }

    private void notifyPagePaintingEnded(boolean interrupted) {
        this.pagePainted = true;
        PagePaintingEvent pageLoadingEvent = new PagePaintingEvent(this, interrupted);
        for (int i2 = this.pageLoadingListeners.size() - 1; i2 >= 0; i2--) {
            PageLoadingListener client = this.pageLoadingListeners.get(i2);
            client.pagePaintingEnded(pageLoadingEvent);
        }
    }

    private void notifyPageInitializationEnded(boolean interrupted) {
        this.pageInitialized = true;
        PageInitializingEvent pageLoadingEvent = new PageInitializingEvent(this, interrupted);
        for (int i2 = this.pageLoadingListeners.size() - 1; i2 >= 0; i2--) {
            PageLoadingListener client = this.pageLoadingListeners.get(i2);
            client.pageInitializationEnded(pageLoadingEvent);
        }
    }

    protected void notifyPageLoadingEnded() {
        PageLoadingEvent pageLoadingEvent = new PageLoadingEvent(this, this.inited);
        for (int i2 = this.pageLoadingListeners.size() - 1; i2 >= 0; i2--) {
            PageLoadingListener client = this.pageLoadingListeners.get(i2);
            client.pageLoadingEnded(pageLoadingEvent);
        }
    }

    public void notifyPaintPageListeners() {
        PaintPageEvent evt = new PaintPageEvent(this);
        for (int i2 = this.paintPageListeners.size() - 1; i2 >= 0; i2--) {
            PaintPageListener client = this.paintPageListeners.get(i2);
            client.paintPage(evt);
        }
    }
}
