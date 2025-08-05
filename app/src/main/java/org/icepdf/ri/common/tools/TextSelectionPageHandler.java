package org.icepdf.ri.common.tools;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.text.GlyphText;
import org.icepdf.core.pobjects.graphics.text.LineText;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.pobjects.graphics.text.WordText;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.util.PropertiesManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/TextSelectionPageHandler.class */
public class TextSelectionPageHandler extends SelectionBoxHandler implements ToolHandler {
    protected static final Logger logger = Logger.getLogger(TextSelectionPageHandler.class.toString());
    public static final float selectionAlpha = 0.3f;
    public static Color selectionColor;
    public int selectedCount;
    public static Color highlightColor;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.text.selectionColor", "#0077FF");
            int colorValue = ColorUtil.convertColor(color);
            selectionColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("0077FF", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading text selection colour");
            }
        }
        try {
            String color2 = Defs.sysProperty(PropertiesManager.SYSPROPERTY_HIGHLIGHT_COLOR, "#CC00FF");
            int colorValue2 = ColorUtil.convertColor(color2);
            highlightColor = new Color(colorValue2 >= 0 ? colorValue2 : Integer.parseInt("FFF600", 16));
        } catch (NumberFormatException e3) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading text highlight colour");
            }
        }
    }

    public TextSelectionPageHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
    }

    public void mouseClicked(MouseEvent e2) {
        if (e2.getClickCount() == 3) {
            Page currentPage = this.pageViewComponent.getPage();
            Point mouseLocation = (Point) e2.getPoint().clone();
            lineSelectHandler(currentPage, mouseLocation);
        } else if (e2.getClickCount() == 2) {
            Page currentPage2 = this.pageViewComponent.getPage();
            Point mouseLocation2 = (Point) e2.getPoint().clone();
            wordSelectHandler(currentPage2, mouseLocation2);
        }
        if (logger.isLoggable(Level.FINE)) {
            Page currentPage3 = this.pageViewComponent.getPage();
            if (currentPage3.getViewText() != null) {
                logger.fine(currentPage3.getViewText().getSelected().toString());
            }
        }
        this.documentViewController.clearSelectedAnnotations();
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        this.documentViewController.clearSelectedText();
        this.selectedCount = 0;
        int x2 = e2.getX();
        int y2 = e2.getY();
        this.currentRect = new Rectangle(x2, y2, 0, 0);
        updateDrawableRect(this.pageViewComponent.getWidth(), this.pageViewComponent.getHeight());
        this.pageViewComponent.repaint();
    }

    public void mouseReleased(MouseEvent e2) {
        updateSelectionSize(e2, this.pageViewComponent);
        if (logger.isLoggable(Level.FINE)) {
            Page currentPage = this.pageViewComponent.getPage();
            if (currentPage.getViewText() != null) {
                logger.fine(currentPage.getViewText().getSelected().toString());
            }
        }
        if (this.selectedCount > 0) {
            this.documentViewModel.addSelectedPageText(this.pageViewComponent);
            this.documentViewController.firePropertyChange(PropertyConstants.TEXT_SELECTED, (Object) null, (Object) null);
        }
        clearRectangle(this.pageViewComponent);
        this.pageViewComponent.repaint();
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        updateSelectionSize(e2, this.pageViewComponent);
        Page currentPage = this.pageViewComponent.getPage();
        multiLineSelectHandler(currentPage, e2.getPoint());
    }

    @Override // org.icepdf.ri.common.tools.SelectionBoxHandler
    public void setSelectionRectangle(Point cursorLocation, Rectangle selection) {
        setSelectionSize(selection, this.pageViewComponent);
        Page currentPage = this.pageViewComponent.getPage();
        multiLineSelectHandler(currentPage, cursorLocation);
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent e2) {
        Page currentPage = this.pageViewComponent.getPage();
        selectionMouseCursor(currentPage, e2.getPoint());
    }

    private void selectionMouseCursor(Page currentPage, Point mouseLocation) {
        PageText pageText;
        if (currentPage != null && currentPage.isInitiated() && (pageText = currentPage.getViewText()) != null) {
            AffineTransform pageTransform = currentPage.getPageTransform(2, this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
            ArrayList<LineText> pageLines = pageText.getPageLines();
            if (pageLines != null) {
                boolean found = false;
                Point2D.Float pageMouseLocation = convertMouseToPageSpace(mouseLocation, pageTransform);
                Iterator i$ = pageLines.iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        break;
                    }
                    LineText pageLine = i$.next();
                    if (pageLine.getBounds().contains(pageMouseLocation)) {
                        found = true;
                        this.documentViewController.setViewCursor(10);
                        break;
                    }
                }
                if (!found) {
                    this.documentViewController.setViewCursor(7);
                }
            }
        }
    }

    private Point2D.Float convertMouseToPageSpace(Point mousePoint, AffineTransform pageTransform) {
        Point2D.Float pageMouseLocation = new Point2D.Float();
        try {
            pageTransform.createInverse().transform(mousePoint, pageMouseLocation);
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.SEVERE, "Error converting mouse point to page space.", (Throwable) e2);
        }
        return pageMouseLocation;
    }

    private Rectangle2D convertRectangleToPageSpace(Rectangle mouseRect, AffineTransform pageTransform) {
        try {
            AffineTransform tranform = pageTransform.createInverse();
            GeneralPath shapePath = new GeneralPath(mouseRect);
            shapePath.transform(tranform);
            return shapePath.getBounds2D();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.SEVERE, "Error converting mouse point to page space.", (Throwable) e2);
            return null;
        }
    }

    private void multiLineSelectHandler(Page currentPage, Point mouseLocation) {
        PageText pageText;
        this.selectedCount = 0;
        if (currentPage != null && currentPage.isInitiated() && (pageText = currentPage.getViewText()) != null) {
            pageText.clearSelected();
            AffineTransform pageTransform = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
            LineText firstPageLine = null;
            Point2D.Float pageMouseLocation = convertMouseToPageSpace(mouseLocation, pageTransform);
            Rectangle2D pageRectToDraw = convertRectangleToPageSpace(this.rectToDraw, pageTransform);
            ArrayList<LineText> pageLines = pageText.getPageLines();
            if (pageLines != null) {
                Iterator i$ = pageLines.iterator();
                while (i$.hasNext()) {
                    LineText pageLine = i$.next();
                    if (pageLine.intersects(pageRectToDraw)) {
                        pageLine.setHasSelected(true);
                        this.selectedCount++;
                        if (firstPageLine == null) {
                            firstPageLine = pageLine;
                        }
                        if (pageLine.getBounds().contains(pageMouseLocation)) {
                            List<WordText> lineWords = pageLine.getWords();
                            for (WordText word : lineWords) {
                                if (word.intersects(pageRectToDraw)) {
                                    word.setHasHighlight(true);
                                    this.selectedCount++;
                                    ArrayList<GlyphText> glyphs = word.getGlyphs();
                                    Iterator i$2 = glyphs.iterator();
                                    while (i$2.hasNext()) {
                                        GlyphText glyph = i$2.next();
                                        if (glyph.intersects(pageRectToDraw)) {
                                            glyph.setSelected(true);
                                            this.selectedCount++;
                                            this.pageViewComponent.repaint();
                                        }
                                    }
                                }
                            }
                        } else if (firstPageLine == pageLine) {
                            selectLeftToRight(pageLine, pageTransform);
                        } else {
                            pageLine.selectAll();
                        }
                    }
                }
            }
        }
    }

    private void selectRightToLeft(LineText pageLine, AffineTransform pageTransform) {
    }

    private void selectLeftToRight(LineText pageLine, AffineTransform pageTransform) {
        GlyphText fistGlyph = null;
        Rectangle2D pageRectToDraw = convertRectangleToPageSpace(this.rectToDraw, pageTransform);
        List<WordText> lineWords = pageLine.getWords();
        for (WordText word : lineWords) {
            if (word.intersects(pageRectToDraw)) {
                word.setHasHighlight(true);
                ArrayList<GlyphText> glyphs = word.getGlyphs();
                Iterator i$ = glyphs.iterator();
                while (i$.hasNext()) {
                    GlyphText glyph = i$.next();
                    if (glyph.intersects(pageRectToDraw)) {
                        if (fistGlyph == null) {
                            fistGlyph = glyph;
                        }
                        glyph.setSelected(true);
                    } else if (fistGlyph != null) {
                        glyph.setSelected(true);
                    }
                }
            } else if (fistGlyph != null) {
                word.selectAll();
            }
        }
        this.pageViewComponent.repaint();
    }

    private void rectangleSelectHandler(Page currentPage, Point mouseLocation) {
        PageText pageText;
        if (currentPage != null && currentPage.isInitiated() && (pageText = currentPage.getViewText()) != null) {
            pageText.clearSelected();
            AffineTransform pageTransform = currentPage.getPageTransform(2, this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
            Rectangle2D pageRectToDraw = convertRectangleToPageSpace(this.rectToDraw, pageTransform);
            ArrayList<LineText> pageLines = pageText.getPageLines();
            if (pageLines != null) {
                Iterator i$ = pageLines.iterator();
                while (i$.hasNext()) {
                    LineText pageLine = i$.next();
                    if (pageLine.intersects(pageRectToDraw)) {
                        pageLine.setHasSelected(true);
                        List<WordText> lineWords = pageLine.getWords();
                        for (WordText word : lineWords) {
                            if (word.intersects(pageRectToDraw)) {
                                word.setHasHighlight(true);
                                ArrayList<GlyphText> glyphs = word.getGlyphs();
                                Iterator i$2 = glyphs.iterator();
                                while (i$2.hasNext()) {
                                    GlyphText glyph = i$2.next();
                                    if (glyph.intersects(pageRectToDraw)) {
                                        glyph.setSelected(true);
                                        this.pageViewComponent.repaint();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void wordSelectHandler(Page currentPage, Point mouseLocation) {
        PageText pageText;
        if (currentPage != null && currentPage.isInitiated() && (pageText = currentPage.getViewText()) != null) {
            pageText.clearSelected();
            AffineTransform pageTransform = currentPage.getPageTransform(2, this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
            Point2D.Float pageMouseLocation = convertMouseToPageSpace(mouseLocation, pageTransform);
            ArrayList<LineText> pageLines = pageText.getPageLines();
            if (pageLines != null) {
                Iterator i$ = pageLines.iterator();
                while (i$.hasNext()) {
                    LineText pageLine = i$.next();
                    if (pageLine.getBounds().contains(pageMouseLocation)) {
                        pageLine.setHasSelected(true);
                        List<WordText> lineWords = pageLine.getWords();
                        Iterator i$2 = lineWords.iterator();
                        while (true) {
                            if (i$2.hasNext()) {
                                WordText word = i$2.next();
                                if (word.getBounds().contains(pageMouseLocation)) {
                                    word.selectAll();
                                    this.documentViewModel.addSelectedPageText(this.pageViewComponent);
                                    this.documentViewController.firePropertyChange(PropertyConstants.TEXT_SELECTED, (Object) null, (Object) null);
                                    this.pageViewComponent.repaint();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void lineSelectHandler(Page currentPage, Point mouseLocation) {
        PageText pageText;
        if (currentPage != null && currentPage.isInitiated() && (pageText = currentPage.getViewText()) != null) {
            pageText.clearSelected();
            AffineTransform pageTransform = currentPage.getPageTransform(2, this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
            Point2D.Float pageMouseLocation = convertMouseToPageSpace(mouseLocation, pageTransform);
            ArrayList<LineText> pageLines = pageText.getPageLines();
            if (pageLines != null) {
                Iterator i$ = pageLines.iterator();
                while (i$.hasNext()) {
                    LineText pageLine = i$.next();
                    if (pageLine.getBounds().contains(pageMouseLocation)) {
                        pageLine.selectAll();
                        this.documentViewModel.addSelectedPageText(this.pageViewComponent);
                        this.documentViewController.firePropertyChange(PropertyConstants.TEXT_SELECTED, (Object) null, (Object) null);
                        this.pageViewComponent.repaint();
                        return;
                    }
                }
            }
        }
    }

    public static void paintSelectedText(Graphics g2, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        PageText pageText;
        Graphics2D gg = (Graphics2D) g2;
        AffineTransform prePaintTransform = gg.getTransform();
        Color oldColor = gg.getColor();
        Stroke oldStroke = gg.getStroke();
        gg.setComposite(AlphaComposite.getInstance(3, 0.3f));
        gg.setColor(selectionColor);
        gg.setStroke(new BasicStroke(1.0f));
        Page currentPage = pageViewComponent.getPage();
        if (currentPage != null && currentPage.isInitiated() && (pageText = currentPage.getViewText()) != null) {
            AffineTransform pageTransform = currentPage.getPageTransform(documentViewModel.getPageBoundary(), documentViewModel.getViewRotation(), documentViewModel.getViewZoom());
            ArrayList<LineText> visiblePageLines = pageText.getPageLines();
            if (visiblePageLines != null) {
                Iterator i$ = visiblePageLines.iterator();
                while (i$.hasNext()) {
                    LineText lineText = i$.next();
                    for (WordText wordText : lineText.getWords()) {
                        if (wordText.isSelected() || wordText.isHighlighted()) {
                            GeneralPath textPath = new GeneralPath(wordText.getBounds());
                            textPath.transform(pageTransform);
                            if (wordText.isSelected()) {
                                gg.setColor(selectionColor);
                                gg.fill(textPath);
                            }
                            if (wordText.isHighlighted()) {
                                gg.setColor(highlightColor);
                                gg.fill(textPath);
                            }
                        } else {
                            Iterator i$2 = wordText.getGlyphs().iterator();
                            while (i$2.hasNext()) {
                                GlyphText glyph = i$2.next();
                                if (glyph.isSelected()) {
                                    GeneralPath textPath2 = new GeneralPath(glyph.getBounds());
                                    textPath2.transform(pageTransform);
                                    gg.setColor(selectionColor);
                                    gg.fill(textPath2);
                                }
                            }
                        }
                    }
                }
            }
        }
        gg.setComposite(AlphaComposite.getInstance(3, 1.0f));
        gg.setTransform(prePaintTransform);
        gg.setStroke(oldStroke);
        gg.setColor(oldColor);
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void installTool() {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void uninstallTool() {
    }

    private void paintTextBounds(Graphics g2) {
        ArrayList<LineText> pageLines;
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform pageTransform = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        Graphics2D gg = (Graphics2D) g2;
        Color oldColor = g2.getColor();
        g2.setColor(Color.red);
        PageText pageText = currentPage.getViewText();
        if (pageText != null && (pageLines = pageText.getPageLines()) != null) {
            Iterator i$ = pageLines.iterator();
            while (i$.hasNext()) {
                LineText lineText = i$.next();
                for (WordText wordText : lineText.getWords()) {
                    Iterator i$2 = wordText.getGlyphs().iterator();
                    while (i$2.hasNext()) {
                        GlyphText glyph = i$2.next();
                        g2.setColor(Color.black);
                        GeneralPath glyphSpritePath = new GeneralPath(glyph.getBounds());
                        glyphSpritePath.transform(pageTransform);
                        gg.draw(glyphSpritePath);
                    }
                }
                g2.setColor(Color.red);
                GeneralPath glyphSpritePath2 = new GeneralPath(lineText.getBounds());
                glyphSpritePath2.transform(pageTransform);
                gg.draw(glyphSpritePath2);
            }
        }
        g2.setColor(oldColor);
    }

    public void paintTool(Graphics g2) {
        paintSelectionBox(g2, this.rectToDraw);
    }
}
