package org.icepdf.core.pobjects.annotations;

import com.sun.org.glassfish.external.amx.AMX;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.util.Library;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/PopupAnnotation.class */
public class PopupAnnotation extends Annotation {
    private static final Logger logger = Logger.getLogger(PopupAnnotation.class.toString());
    public static final Name PARENT_KEY = new Name(AMX.ATTR_PARENT);
    public static final Name OPEN_KEY = new Name(ToolWindow.OPEN_POLICY_FILE);
    protected boolean open;
    protected MarkupAnnotation parent;

    public PopupAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
        this.open = this.library.getBoolean(this.entries, OPEN_KEY).booleanValue();
    }

    public static PopupAnnotation getInstance(Library library, Rectangle rect) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        entries.put(Dictionary.TYPE_KEY, Annotation.TYPE_VALUE);
        entries.put(Dictionary.SUBTYPE_KEY, Annotation.SUBTYPE_POPUP);
        if (rect != null) {
            entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(rect));
        } else {
            entries.put(Annotation.RECTANGLE_KEY, new Rectangle(10, 10, 50, 100));
        }
        PopupAnnotation popupAnnotation = new PopupAnnotation(library, entries);
        popupAnnotation.init();
        popupAnnotation.setPObjectReference(stateManager.getNewReferencNumber());
        popupAnnotation.setNew(true);
        popupAnnotation.setFlag(64, false);
        popupAnnotation.setFlag(16, false);
        popupAnnotation.setFlag(8, false);
        popupAnnotation.setFlag(4, false);
        return popupAnnotation;
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageTransform) {
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
        this.entries.put(OPEN_KEY, Boolean.valueOf(open));
    }

    public MarkupAnnotation getParent() {
        Object tmp = this.library.getObject(this.entries, PARENT_KEY);
        if (tmp != null && (tmp instanceof MarkupAnnotation)) {
            this.parent = (MarkupAnnotation) tmp;
        }
        return this.parent;
    }

    public void setParent(MarkupAnnotation parent) {
        this.parent = parent;
        this.entries.put(PARENT_KEY, parent.getPObjectReference());
    }
}
