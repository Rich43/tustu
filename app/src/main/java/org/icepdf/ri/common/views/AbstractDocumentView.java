package org.icepdf.ri.common.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.AdjustmentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.tools.AnnotationSelectionHandler;
import org.icepdf.ri.common.tools.DynamicZoomHandler;
import org.icepdf.ri.common.tools.MouseWheelZoom;
import org.icepdf.ri.common.tools.PanningHandler;
import org.icepdf.ri.common.tools.TextSelectionViewHandler;
import org.icepdf.ri.common.tools.ToolHandler;
import org.icepdf.ri.common.tools.ZoomInViewHandler;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/AbstractDocumentView.class */
public abstract class AbstractDocumentView extends JComponent implements DocumentView, PropertyChangeListener, MouseListener {
    private static final Logger logger = Logger.getLogger(AbstractDocumentView.class.toString());
    public static Color backgroundColor;
    public static int verticalSpace;
    public static int horizontalSpace;
    public static int layoutInserts;
    protected DocumentViewController documentViewController;
    protected JScrollPane documentScrollpane;
    protected Document currentDocument;
    protected DocumentViewModel documentViewModel;
    protected ToolHandler currentTool;
    protected MouseWheelZoom mouseWheelZoom;

    @Override // org.icepdf.ri.common.views.DocumentView
    public abstract void updateDocumentView();

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.background.color", "#808080");
            int colorValue = ColorUtil.convertColor(color);
            backgroundColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("808080", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading page shadow colour");
            }
        }
        verticalSpace = 2;
        horizontalSpace = 1;
        layoutInserts = 0;
    }

    public AbstractDocumentView(DocumentViewController documentViewController, JScrollPane documentScrollpane, DocumentViewModel documentViewModel) {
        this.documentViewController = documentViewController;
        this.documentScrollpane = documentScrollpane;
        this.documentViewModel = documentViewModel;
        this.currentDocument = this.documentViewModel.getDocument();
        setFocusable(true);
        addFocusListener(this);
        addMouseListener(this);
        this.mouseWheelZoom = new MouseWheelZoom(documentViewController, documentScrollpane);
        documentScrollpane.addMouseWheelListener(this.mouseWheelZoom);
        documentViewController.getHorizontalScrollBar().addAdjustmentListener(this);
        documentViewController.getVerticalScrollBar().addAdjustmentListener(this);
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(this);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        Object oldValue = evt.getOldValue();
        if ("focusOwner".equals(prop) && (newValue instanceof AnnotationComponent)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Selected Annotation " + newValue);
            }
            DocumentViewController documentViewController = getParentViewController();
            documentViewController.firePropertyChange(PropertyConstants.ANNOTATION_FOCUS_GAINED, evt.getOldValue(), evt.getNewValue());
            return;
        }
        if ("focusOwner".equals(prop) && (oldValue instanceof AnnotationComponent)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Deselected Annotation " + oldValue);
            }
            DocumentViewController documentViewController2 = getParentViewController();
            documentViewController2.firePropertyChange(PropertyConstants.ANNOTATION_FOCUS_LOST, evt.getOldValue(), evt.getNewValue());
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public DocumentViewController getParentViewController() {
        return this.documentViewController;
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public DocumentViewModel getViewModel() {
        return this.documentViewModel;
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public void dispose() {
        this.currentDocument = null;
        this.documentViewController.getHorizontalScrollBar().removeAdjustmentListener(this);
        this.documentViewController.getVerticalScrollBar().removeAdjustmentListener(this);
        if (this.currentTool != null) {
            removeMouseListener(this.currentTool);
            removeMouseMotionListener(this.currentTool);
        }
        this.documentScrollpane.removeMouseWheelListener(this.mouseWheelZoom);
        removeMouseListener(this);
        removeFocusListener(this);
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.removePropertyChangeListener(this);
    }

    public ToolHandler uninstallCurrentTool() {
        if (this.currentTool != null) {
            this.currentTool.uninstallTool();
            removeMouseListener(this.currentTool);
            removeMouseMotionListener(this.currentTool);
        }
        return this.currentTool;
    }

    public void installCurrentTool(ToolHandler currentTool) {
        if (currentTool != null) {
            currentTool.installTool();
            addMouseListener(currentTool);
            addMouseMotionListener(currentTool);
            this.currentTool = currentTool;
        }
    }

    public ToolHandler getCurrentToolHandler() {
        return this.currentTool;
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public void setToolMode(int viewToolMode) {
        uninstallCurrentTool();
        switch (viewToolMode) {
            case 1:
                this.currentTool = new PanningHandler(this.documentViewController, this.documentViewModel, this);
                break;
            case 2:
                this.currentTool = new ZoomInViewHandler(this.documentViewController, this.documentViewModel, this);
                break;
            case 3:
            default:
                this.currentTool = null;
                break;
            case 4:
                this.currentTool = new DynamicZoomHandler(this.documentViewController, this.documentScrollpane);
                break;
            case 5:
                this.currentTool = new TextSelectionViewHandler(this.documentViewController, this.documentViewModel, this);
                break;
            case 6:
                this.currentTool = new AnnotationSelectionHandler(this.documentViewController, null, this.documentViewModel);
                break;
        }
        if (this.currentTool != null) {
            this.currentTool.installTool();
            addMouseListener(this.currentTool);
            addMouseMotionListener(this.currentTool);
        }
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics g2) {
        if (this.currentTool != null) {
            this.currentTool.paintTool(g2);
        }
    }

    @Override // java.awt.event.AdjustmentListener
    public void adjustmentValueChanged(AdjustmentEvent e2) {
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent e2) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        requestFocus();
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    private AbstractPageViewComponent isOverPageComponent(MouseEvent e2) {
        Component comp = findComponentAt(e2.getPoint());
        if (comp instanceof AbstractPageViewComponent) {
            return (AbstractPageViewComponent) comp;
        }
        return null;
    }

    public JScrollPane getDocumentScrollpane() {
        return this.documentScrollpane;
    }
}
