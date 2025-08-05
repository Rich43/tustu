package org.icepdf.core.pobjects.annotations;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/LinkAnnotation.class */
public class LinkAnnotation extends Annotation {
    public static final Name DESTINATION_KEY = new Name("Dest");
    public static final Name HIGHLIGHT_MODE_KEY = new Name(PdfOps.H_TOKEN);
    public static final Name HIGHLIGHT_NONE = new Name("N");
    public static final Name HIGHLIGHT_INVERT = new Name("I");
    public static final Name HIGHLIGHT_OUTLINE = new Name("O");
    public static final Name HIGHLIGHT_PUSH = new Name(Constants._TAG_P);

    public LinkAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
    }

    public static LinkAnnotation getInstance(Library library, Rectangle rect) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        entries.put(Dictionary.TYPE_KEY, Annotation.TYPE_VALUE);
        entries.put(Dictionary.SUBTYPE_KEY, Annotation.SUBTYPE_LINK);
        if (rect != null) {
            entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(rect));
        } else {
            entries.put(Annotation.RECTANGLE_KEY, new Rectangle(10, 10, 50, 100));
        }
        entries.put(HIGHLIGHT_MODE_KEY, HIGHLIGHT_INVERT);
        LinkAnnotation linkAnnotation = new LinkAnnotation(library, entries);
        linkAnnotation.init();
        linkAnnotation.setPObjectReference(stateManager.getNewReferencNumber());
        linkAnnotation.setNew(true);
        linkAnnotation.setFlag(64, false);
        linkAnnotation.setFlag(16, false);
        linkAnnotation.setFlag(8, false);
        linkAnnotation.setFlag(4, true);
        return linkAnnotation;
    }

    public Name getHighlightMode() {
        Object possibleName = getObject(HIGHLIGHT_MODE_KEY);
        if (possibleName instanceof Name) {
            Name name = (Name) possibleName;
            if (HIGHLIGHT_NONE.equals(name)) {
                return HIGHLIGHT_NONE;
            }
            if (HIGHLIGHT_OUTLINE.equals(name)) {
                return HIGHLIGHT_OUTLINE;
            }
            if (HIGHLIGHT_PUSH.equals(name)) {
                return HIGHLIGHT_PUSH;
            }
        }
        return HIGHLIGHT_INVERT;
    }

    public Destination getDestination() {
        Object obj = getObject(DESTINATION_KEY);
        if (obj != null) {
            return new Destination(this.library, obj);
        }
        return null;
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageTransform) {
    }
}
