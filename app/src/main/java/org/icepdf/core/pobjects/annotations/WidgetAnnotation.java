package org.icepdf.core.pobjects.annotations;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.pobjects.acroform.FieldDictionary;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/WidgetAnnotation.class */
public class WidgetAnnotation extends Annotation {
    private static final Logger logger = Logger.getLogger(TextMarkupAnnotation.class.toString());
    public static final Name HIGHLIGHT_NONE = new Name("N");
    protected FieldDictionary fieldDictionary;
    protected Name highlightMode;

    public WidgetAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
        this.fieldDictionary = new FieldDictionary(this.library, this.entries);
        Object possibleName = getObject(LinkAnnotation.HIGHLIGHT_MODE_KEY);
        if (possibleName instanceof Name) {
            Name name = (Name) possibleName;
            if (HIGHLIGHT_NONE.equals(name.getName())) {
                this.highlightMode = HIGHLIGHT_NONE;
            } else if (LinkAnnotation.HIGHLIGHT_OUTLINE.equals(name.getName())) {
                this.highlightMode = LinkAnnotation.HIGHLIGHT_OUTLINE;
            } else if (LinkAnnotation.HIGHLIGHT_PUSH.equals(name.getName())) {
                this.highlightMode = LinkAnnotation.HIGHLIGHT_PUSH;
            }
        }
        this.highlightMode = LinkAnnotation.HIGHLIGHT_INVERT;
    }

    public static WidgetAnnotation getInstance(Library library, Rectangle rect) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        entries.put(Dictionary.TYPE_KEY, Annotation.TYPE_VALUE);
        entries.put(Dictionary.SUBTYPE_KEY, Annotation.SUBTYPE_WIDGET);
        if (rect != null) {
            entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(rect));
        } else {
            entries.put(Annotation.RECTANGLE_KEY, new Rectangle(10, 10, 50, 100));
        }
        WidgetAnnotation annotation = new WidgetAnnotation(library, entries);
        annotation.init();
        annotation.setPObjectReference(stateManager.getNewReferencNumber());
        annotation.setNew(true);
        annotation.setFlag(64, false);
        annotation.setFlag(16, false);
        annotation.setFlag(8, false);
        annotation.setFlag(4, true);
        return annotation;
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageSpace) {
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    protected void renderAppearanceStream(Graphics2D g2) {
        if (this.shapes != null) {
            super.renderAppearanceStream(g2);
        }
    }
}
