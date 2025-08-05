package org.icepdf.ri.common.tools;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.AnnotationFactory;
import org.icepdf.core.pobjects.annotations.TextMarkupAnnotation;
import org.icepdf.core.pobjects.graphics.text.GlyphText;
import org.icepdf.core.pobjects.graphics.text.LineText;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.pobjects.graphics.text.WordText;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.AnnotationComponentFactory;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/HighLightAnnotationHandler.class */
public class HighLightAnnotationHandler extends TextSelectionPageHandler {
    protected Name highLightType;

    public HighLightAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        this.highLightType = Annotation.SUBTYPE_HIGHLIGHT;
    }

    @Override // org.icepdf.ri.common.tools.TextSelectionPageHandler, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
    }

    @Override // org.icepdf.ri.common.tools.TextSelectionPageHandler, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        ArrayList<Shape> highlightBounds = getSelectedTextBounds();
        super.mouseReleased(e2);
        createTextMarkupAnnotation(highlightBounds);
    }

    public void createTextMarkupAnnotation(ArrayList<Shape> highlightBounds) {
        if (this.documentViewModel.isSelectAll()) {
            this.documentViewController.clearSelectedText();
        }
        if (highlightBounds == null) {
            highlightBounds = getSelectedTextBounds();
        }
        this.documentViewController.clearSelectedText();
        if (highlightBounds != null) {
            GeneralPath highlightPath = new GeneralPath();
            Iterator i$ = highlightBounds.iterator();
            while (i$.hasNext()) {
                Shape bounds = i$.next();
                highlightPath.append(bounds, false);
            }
            Rectangle bounds2 = highlightPath.getBounds();
            Rectangle tBbox = convertToPageSpace(highlightBounds, highlightPath);
            TextMarkupAnnotation annotation = (TextMarkupAnnotation) AnnotationFactory.buildAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), this.highLightType, tBbox);
            annotation.setContents(this.highLightType.toString());
            annotation.setColor(annotation.getTextMarkupColor());
            annotation.setCreationDate(PDate.formatDateTime(new Date()));
            annotation.setTitleText(System.getProperty("user.name"));
            annotation.setMarkupBounds(highlightBounds);
            annotation.setMarkupPath(highlightPath);
            annotation.setBBox(tBbox);
            annotation.resetAppearanceStream(getPageTransform());
            AbstractAnnotationComponent comp = AnnotationComponentFactory.buildAnnotationComponent(annotation, this.documentViewController, this.pageViewComponent, this.documentViewModel);
            comp.setBounds(bounds2);
            comp.refreshAnnotationRect();
            if (this.documentViewController.getAnnotationCallback() != null) {
                AnnotationCallback annotationCallback = this.documentViewController.getAnnotationCallback();
                annotationCallback.newAnnotation(this.pageViewComponent, comp);
            }
        }
        this.pageViewComponent.repaint();
    }

    @Override // org.icepdf.ri.common.tools.TextSelectionPageHandler, org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
        paintSelectionBox(g2, this.rectToDraw);
    }

    private ArrayList<Shape> getSelectedTextBounds() {
        PageText pageText;
        Page currentPage = this.pageViewComponent.getPage();
        ArrayList<Shape> highlightBounds = null;
        if (currentPage != null && currentPage.isInitiated() && (pageText = currentPage.getViewText()) != null) {
            AffineTransform pageTransform = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
            ArrayList<LineText> pageLines = pageText.getPageLines();
            if (pageLines != null) {
                Iterator i$ = pageLines.iterator();
                while (i$.hasNext()) {
                    LineText lineText = i$.next();
                    List<WordText> words = lineText.getWords();
                    if (words != null) {
                        for (WordText wordText : words) {
                            if (wordText.isSelected() || wordText.isHighlighted()) {
                                GeneralPath textPath = new GeneralPath(wordText.getBounds());
                                textPath.transform(pageTransform);
                                if (wordText.isSelected()) {
                                    if (highlightBounds == null) {
                                        highlightBounds = new ArrayList<>();
                                    }
                                    highlightBounds.add(textPath.getBounds2D());
                                }
                            } else {
                                Iterator i$2 = wordText.getGlyphs().iterator();
                                while (i$2.hasNext()) {
                                    GlyphText glyph = i$2.next();
                                    if (glyph.isSelected()) {
                                        GeneralPath textPath2 = new GeneralPath(glyph.getBounds());
                                        textPath2.transform(pageTransform);
                                        if (highlightBounds == null) {
                                            highlightBounds = new ArrayList<>();
                                        }
                                        highlightBounds.add(textPath2.getBounds2D());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return highlightBounds;
    }

    protected Rectangle convertToPageSpace(ArrayList<Shape> bounds, GeneralPath path) {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error converting to page space.", (Throwable) e2);
        }
        Rectangle tBbox = at2.createTransformedShape(path).getBounds();
        for (int i2 = 0; i2 < bounds.size(); i2++) {
            Shape bound = bounds.get(i2);
            bounds.set(i2, at2.createTransformedShape(bound));
        }
        path.transform(at2);
        return tBbox;
    }
}
