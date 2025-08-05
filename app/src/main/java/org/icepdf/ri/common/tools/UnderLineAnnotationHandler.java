package org.icepdf.ri.common.tools;

import org.icepdf.core.pobjects.annotations.TextMarkupAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/UnderLineAnnotationHandler.class */
public class UnderLineAnnotationHandler extends HighLightAnnotationHandler {
    public UnderLineAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        this.highLightType = TextMarkupAnnotation.SUBTYPE_UNDERLINE;
    }
}
