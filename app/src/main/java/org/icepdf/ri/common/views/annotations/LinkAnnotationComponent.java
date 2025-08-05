package org.icepdf.ri.common.views.annotations;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.LinkAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/LinkAnnotationComponent.class */
public class LinkAnnotationComponent extends MarkupAnnotationComponent {
    public LinkAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(annotation, documentViewController, pageViewComponent, documentViewModel);
        this.isShowInvisibleBorder = true;
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
    public void resetAppearanceShapes() {
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
        this.isEditable = ((this.documentViewModel.getViewToolMode() != 6 && this.documentViewModel.getViewToolMode() != 7) || this.annotation.getFlagReadOnly() || this.annotation.getFlagLocked() || this.annotation.getFlagInvisible() || this.annotation.getFlagHidden()) ? false : true;
        if (this.isMousePressed && this.documentViewModel.getViewToolMode() != 6 && this.documentViewModel.getViewToolMode() != 7) {
            Graphics2D gg2 = (Graphics2D) g2;
            LinkAnnotation linkAnnotation = (LinkAnnotation) this.annotation;
            Name highlightMode = linkAnnotation.getHighlightMode();
            Rectangle2D rect = new Rectangle(0, 0, getWidth(), getHeight());
            if (LinkAnnotation.HIGHLIGHT_INVERT.equals(highlightMode)) {
                gg2.setColor(annotationHighlightColor);
                gg2.setComposite(AlphaComposite.getInstance(3, annotationHighlightAlpha));
                gg2.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            } else if (LinkAnnotation.HIGHLIGHT_OUTLINE.equals(highlightMode)) {
                gg2.setColor(annotationHighlightColor);
                gg2.setComposite(AlphaComposite.getInstance(3, annotationHighlightAlpha));
                gg2.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            } else if (LinkAnnotation.HIGHLIGHT_PUSH.equals(highlightMode)) {
                gg2.setColor(annotationHighlightColor);
                gg2.setComposite(AlphaComposite.getInstance(3, annotationHighlightAlpha));
                gg2.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            }
        }
    }
}
