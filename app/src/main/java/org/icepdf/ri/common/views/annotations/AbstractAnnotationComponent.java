package org.icepdf.ri.common.views.annotations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.PageViewComponent;
import org.icepdf.ri.common.views.ResizableBorder;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/AbstractAnnotationComponent.class */
public abstract class AbstractAnnotationComponent extends JComponent implements FocusListener, MouseInputListener, AnnotationComponent {
    protected static final Logger logger = Logger.getLogger(AbstractAnnotationComponent.class.toString());
    protected static boolean isInteractiveAnnotationsEnabled = Defs.sysPropertyBoolean("org.icepdf.core.annotations.interactive.enabled", true);
    protected static Color annotationHighlightColor;
    protected static float annotationHighlightAlpha;
    public static final int resizeBoxSize = 4;
    protected static ResizableBorder resizableBorder;
    protected AbstractPageViewComponent pageViewComponent;
    protected DocumentViewController documentViewController;
    protected DocumentViewModel documentViewModel;
    protected float currentZoom;
    protected float currentRotation;
    protected Annotation annotation;
    protected boolean isMousePressed;
    protected boolean resized;
    protected boolean wasResized;
    protected boolean isEditable;
    protected boolean isRollover;
    protected boolean isMovable;
    protected boolean isResizable;
    protected boolean isShowInvisibleBorder;
    protected boolean isSelected;
    protected int cursor;
    protected Point startPos;
    protected AnnotationState previousAnnotationState;
    protected Point startOfMousePress;
    protected Point endOfMousePress;
    protected ResourceBundle messageBundle;

    @Override // javax.swing.JComponent
    public abstract void paintComponent(Graphics graphics);

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public abstract void resetAppearanceShapes();

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.annotation.highlight.color", "#000000");
            int colorValue = ColorUtil.convertColor(color);
            annotationHighlightColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("000000", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading page annotation highlight colour");
            }
        }
        try {
            String alpha = Defs.sysProperty("org.icepdf.core.views.page.annotation.highlight.alpha", "0.4");
            annotationHighlightAlpha = Float.parseFloat(alpha);
        } catch (NumberFormatException e3) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading page annotation highlight alpha");
            }
            annotationHighlightAlpha = 0.4f;
        }
        resizableBorder = new ResizableBorder(4);
    }

    public AbstractAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        this.pageViewComponent = pageViewComponent;
        this.documentViewModel = documentViewModel;
        this.documentViewController = documentViewController;
        this.annotation = annotation;
        this.messageBundle = documentViewController.getParentController().getMessageBundle();
        this.isEditable = !annotation.getFlagReadOnly();
        this.isRollover = false;
        this.isMovable = (annotation.getFlagReadOnly() || annotation.getFlagLocked()) ? false : true;
        this.isResizable = (annotation.getFlagReadOnly() || annotation.getFlagLocked()) ? false : true;
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        addFocusListener(this);
        setLayout(new BorderLayout());
        setBorder(resizableBorder);
        Page currentPage = pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(documentViewModel.getPageBoundary(), documentViewModel.getViewRotation(), documentViewModel.getViewZoom());
        final Rectangle location = at2.createTransformedShape(annotation.getUserSpaceRectangle()).getBounds();
        SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent.1
            @Override // java.lang.Runnable
            public void run() {
                AbstractAnnotationComponent.this.setBounds(location);
            }
        });
        this.currentRotation = documentViewModel.getViewRotation();
        this.currentZoom = documentViewModel.getViewZoom();
        resizableBorder.setZoom(this.currentZoom);
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public Document getDocument() {
        return this.documentViewModel.getDocument();
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public int getPageIndex() {
        return this.pageViewComponent.getPageIndex();
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public PageViewComponent getParentPageView() {
        return this.pageViewComponent;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public AbstractPageViewComponent getPageViewComponent() {
        return this.pageViewComponent;
    }

    public void removeMouseListeners() {
        removeMouseListener(this);
        removeMouseMotionListener(this);
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public Annotation getAnnotation() {
        return this.annotation;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent e2) {
        repaint();
        this.isSelected = true;
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent e2) {
        repaint();
        this.isSelected = false;
    }

    protected void resize() {
        if (getParent() != null) {
            getParent().validate();
        }
        this.resized = true;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public void refreshDirtyBounds() {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        setBounds(commonBoundsNormalization(new GeneralPath(this.annotation.getUserSpaceRectangle()), at2));
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public void refreshAnnotationRect() {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error refreshing annotation rectangle", (Throwable) e2);
        }
        Rectangle2D rect = this.annotation.getUserSpaceRectangle();
        Rectangle2D rect2 = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        Rectangle bounds = getBounds();
        rect2.setRect(commonBoundsNormalization(new GeneralPath(bounds), at2));
        this.annotation.syncBBoxToUserSpaceRectangle(rect2);
    }

    protected Rectangle commonBoundsNormalization(GeneralPath shapePath, AffineTransform at2) {
        shapePath.transform(at2);
        Rectangle2D pageSpaceBound = shapePath.getBounds2D();
        return new Rectangle((int) Math.round(pageSpaceBound.getX()), (int) Math.round(pageSpaceBound.getY()), (int) Math.round(pageSpaceBound.getWidth()), (int) Math.round(pageSpaceBound.getHeight()));
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
        if (this.currentZoom != this.documentViewModel.getViewZoom() || this.currentRotation != this.documentViewModel.getViewRotation()) {
            refreshDirtyBounds();
            this.currentRotation = this.documentViewModel.getViewRotation();
            this.currentZoom = this.documentViewModel.getViewZoom();
            resizableBorder.setZoom(this.currentZoom);
        }
        if (this.resized) {
            refreshAnnotationRect();
            if (getParent() != null) {
                getParent().validate();
                getParent().repaint();
            }
            this.resized = false;
            this.wasResized = true;
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent me) {
        int toolMode = this.documentViewModel.getViewToolMode();
        if (toolMode == 6 && !this.annotation.getFlagLocked() && !this.annotation.getFlagReadOnly()) {
            ResizableBorder border = (ResizableBorder) getBorder();
            setCursor(Cursor.getPredefinedCursor(border.getCursor(me)));
        } else {
            setCursor(this.documentViewController.getViewCursor(9));
        }
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public void dispose() {
        removeMouseListener(this);
        removeMouseMotionListener(this);
        removeFocusListener(this);
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        setCursor(Cursor.getDefaultCursor());
        this.isRollover = false;
        repaint();
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        requestFocus();
        if (!AbstractPageViewComponent.isAnnotationTool(this.documentViewModel.getViewToolMode()) && isInteractiveAnnotationsEnabled && this.documentViewController.getAnnotationCallback() != null) {
            this.documentViewController.getAnnotationCallback().processAnnotationAction(this.annotation);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
        this.isRollover = this.documentViewModel.getViewToolMode() == 6 || (this instanceof PopupAnnotationComponent);
        repaint();
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        this.isMousePressed = true;
        this.startOfMousePress = e2.getPoint();
        this.endOfMousePress = e2.getPoint();
        if (this.documentViewModel.getViewToolMode() == 6 && isInteractiveAnnotationsEnabled && !this.annotation.getFlagReadOnly()) {
            initiateMouseMoved(e2);
        }
        repaint();
    }

    protected void initiateMouseMoved(MouseEvent e2) {
        ResizableBorder border = (ResizableBorder) getBorder();
        this.cursor = border.getCursor(e2);
        this.startPos = e2.getPoint();
        this.previousAnnotationState = new AnnotationState(this);
        this.documentViewController.assignSelectedAnnotation(this);
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent me) {
        if (this.startPos != null && this.isMovable && !this.annotation.getFlagLocked() && !this.annotation.getFlagReadOnly()) {
            int x2 = getX();
            int y2 = getY();
            int w2 = getWidth();
            int h2 = getHeight();
            int dx = me.getX() - this.startPos.f12370x;
            int dy = me.getY() - this.startPos.f12371y;
            if (this.endOfMousePress != null) {
                this.endOfMousePress.setLocation(this.endOfMousePress.f12370x + dx, this.endOfMousePress.f12371y + dy);
            }
            switch (this.cursor) {
                case 4:
                    if (this.isResizable && w2 - dx >= 18 && h2 + dy >= 18) {
                        setBounds(x2 + dx, y2, w2 - dx, h2 + dy);
                        this.startPos = new Point(this.startPos.f12370x, me.getY());
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
                case 5:
                    if (this.isResizable && w2 + dx >= 18 && h2 + dy >= 18) {
                        setBounds(x2, y2, w2 + dx, h2 + dy);
                        this.startPos = me.getPoint();
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
                case 6:
                    if (this.isResizable && w2 - dx >= 18 && h2 - dy >= 18) {
                        setBounds(x2 + dx, y2 + dy, w2 - dx, h2 - dy);
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
                case 7:
                    if (this.isResizable && w2 + dx >= 18 && h2 - dy >= 18) {
                        setBounds(x2, y2 + dy, w2 + dx, h2 - dy);
                        this.startPos = new Point(me.getX(), this.startPos.f12371y);
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
                case 8:
                    if (this.isResizable && h2 - dy >= 12) {
                        setBounds(x2, y2 + dy, w2, h2 - dy);
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
                case 9:
                    if (this.isResizable && h2 + dy >= 12) {
                        setBounds(x2, y2, w2, h2 + dy);
                        this.startPos = me.getPoint();
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
                case 10:
                    if (this.isResizable && w2 - dx >= 18) {
                        setBounds(x2 + dx, y2, w2 - dx, h2);
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
                case 11:
                    if (this.isResizable && w2 + dx >= 18) {
                        setBounds(x2, y2, w2 + dx, h2);
                        this.startPos = me.getPoint();
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
                case 13:
                    if (this.isMovable) {
                        Rectangle bounds = getBounds();
                        bounds.translate(dx, dy);
                        setBounds(bounds);
                        resize();
                        setCursor(Cursor.getPredefinedCursor(this.cursor));
                        break;
                    }
                    break;
            }
            validate();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        this.startPos = null;
        this.isMousePressed = false;
        if (this.wasResized) {
            this.wasResized = false;
            refreshAnnotationRect();
            double dx = 0.0d;
            double dy = 0.0d;
            if (this.startOfMousePress != null && this.endOfMousePress != null) {
                dx = this.endOfMousePress.getX() - this.startOfMousePress.getX();
                dy = this.endOfMousePress.getY() - this.startOfMousePress.getY();
            }
            this.annotation.resetAppearanceStream(dx, -dy, getPageTransform());
            this.documentViewController.firePropertyChange(PropertyConstants.ANNOTATION_BOUNDS, this.previousAnnotationState, new AnnotationState(this));
        }
        repaint();
    }

    protected Rectangle convertToPageSpace(Rectangle rect) {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error converting to page space.", (Throwable) e2);
        }
        Rectangle tBbox = new Rectangle(rect.f12372x, rect.f12373y, rect.width, rect.height);
        return at2.createTransformedShape(tBbox).getBounds();
    }

    protected AffineTransform getPageTransform() {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error getting page transform.", (Throwable) e2);
        }
        return at2;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public boolean isEditable() {
        return this.isEditable;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public boolean isRollover() {
        return this.isRollover;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public boolean isBorderStyle() {
        return this.annotation.isBorder();
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public boolean isSelected() {
        return this.isSelected;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public boolean isMovable() {
        return this.isMovable;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public boolean isResizable() {
        return this.isResizable;
    }

    @Override // org.icepdf.ri.common.views.AnnotationComponent
    public boolean isShowInvisibleBorder() {
        return this.isShowInvisibleBorder;
    }
}
