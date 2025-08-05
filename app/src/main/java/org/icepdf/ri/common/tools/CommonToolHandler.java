package org.icepdf.ri.common.tools;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Page;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/CommonToolHandler.class */
public class CommonToolHandler {
    private static final Logger logger = Logger.getLogger(CommonToolHandler.class.toString());
    protected AbstractPageViewComponent pageViewComponent;
    protected DocumentViewController documentViewController;
    protected DocumentViewModel documentViewModel;

    public CommonToolHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        this.pageViewComponent = pageViewComponent;
        this.documentViewController = documentViewController;
        this.documentViewModel = documentViewModel;
    }

    protected AffineTransform getPageTransform() {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error page space transform", (Throwable) e2);
        }
        return at2;
    }

    protected Shape convertToPageSpace(Shape shape) {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error converting to page space", (Throwable) e2);
        }
        return at2.createTransformedShape(shape);
    }
}
