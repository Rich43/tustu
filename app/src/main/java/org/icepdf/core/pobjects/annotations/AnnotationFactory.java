package org.icepdf.core.pobjects.annotations;

import java.awt.Rectangle;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/AnnotationFactory.class */
public class AnnotationFactory {
    private static final Logger logger = Logger.getLogger(AnnotationFactory.class.toString());

    public static Annotation buildAnnotation(Library library, Name subType, Rectangle rect) {
        if (subType.equals(Annotation.SUBTYPE_LINK)) {
            return LinkAnnotation.getInstance(library, rect);
        }
        if (subType.equals(TextMarkupAnnotation.SUBTYPE_HIGHLIGHT) || subType.equals(TextMarkupAnnotation.SUBTYPE_STRIKE_OUT) || subType.equals(TextMarkupAnnotation.SUBTYPE_UNDERLINE)) {
            return TextMarkupAnnotation.getInstance(library, rect, subType);
        }
        if (subType.equals(Annotation.SUBTYPE_LINE)) {
            return LineAnnotation.getInstance(library, rect);
        }
        if (subType.equals(Annotation.SUBTYPE_SQUARE)) {
            return SquareAnnotation.getInstance(library, rect);
        }
        if (subType.equals(Annotation.SUBTYPE_CIRCLE)) {
            return CircleAnnotation.getInstance(library, rect);
        }
        if (subType.equals(Annotation.SUBTYPE_INK)) {
            return InkAnnotation.getInstance(library, rect);
        }
        if (subType.equals(Annotation.SUBTYPE_FREE_TEXT)) {
            return FreeTextAnnotation.getInstance(library, rect);
        }
        if (subType.equals(Annotation.SUBTYPE_TEXT)) {
            return TextAnnotation.getInstance(library, rect);
        }
        if (subType.equals(Annotation.SUBTYPE_POPUP)) {
            return PopupAnnotation.getInstance(library, rect);
        }
        if (subType.equals(Annotation.SUBTYPE_WIDGET)) {
            return WidgetAnnotation.getInstance(library, rect);
        }
        logger.warning("Unsupported Annotation type. ");
        return null;
    }
}
