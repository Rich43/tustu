package org.icepdf.ri.common.views.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.BorderStyle;
import org.icepdf.core.pobjects.annotations.FreeTextAnnotation;
import org.icepdf.core.pobjects.fonts.FontFile;
import org.icepdf.core.pobjects.fonts.FontManager;
import org.icepdf.core.pobjects.graphics.TextSprite;
import org.icepdf.core.pobjects.graphics.commands.DrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TextSpriteDrawCmd;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.annotations.FreeTextArea;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/FreeTextAnnotationComponent.class */
public class FreeTextAnnotationComponent extends MarkupAnnotationComponent implements PropertyChangeListener {
    private static final Logger logger = Logger.getLogger(FreeTextAnnotation.class.toString());
    private FreeTextArea freeTextPane;
    private boolean contentTextChange;
    private FreeTextAnnotation freeTextAnnotation;
    protected Font fontFile;

    public FreeTextAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, final DocumentViewModel documentViewModel) {
        super(annotation, documentViewController, pageViewComponent, documentViewModel);
        this.isRollover = false;
        this.isShowInvisibleBorder = false;
        this.freeTextAnnotation = (FreeTextAnnotation) annotation;
        if (annotation.getShapes() != null) {
            ArrayList<DrawCmd> shapes = annotation.getShapes().getShapes();
            for (int i2 = 0; i2 < shapes.size(); i2++) {
                DrawCmd cmd = shapes.get(i2);
                if (cmd instanceof TextSpriteDrawCmd) {
                    TextSprite tmp = ((TextSpriteDrawCmd) cmd).getTextSprite();
                    FontFile font = tmp.getFont();
                    this.freeTextAnnotation.setFontSize((int) font.getSize());
                    this.freeTextAnnotation.setFontColor(tmp.getStrokeColor());
                    shapes.remove(i2);
                }
            }
            ((FreeTextAnnotation) annotation).clearShapes();
        }
        this.freeTextPane = new FreeTextArea(new FreeTextArea.ZoomProvider() { // from class: org.icepdf.ri.common.views.annotations.FreeTextAnnotationComponent.1
            private DocumentViewModel model;

            {
                this.model = documentViewModel;
            }

            @Override // org.icepdf.ri.common.views.annotations.FreeTextArea.ZoomProvider
            public float getZoom() {
                return this.model.getViewZoom();
            }
        });
        this.freeTextPane.setLineWrap(false);
        this.freeTextPane.setBackground(new Color(0, 0, 0, 0));
        this.freeTextPane.setMargin(new Insets(0, 0, 0, 0));
        this.freeTextPane.setEditable(false);
        String contents = this.freeTextAnnotation.getContents();
        if (contents != null) {
            this.freeTextPane.setText(contents.replace('\r', '\n'));
        }
        this.freeTextPane.getDocument().addDocumentListener(new DocumentListener() { // from class: org.icepdf.ri.common.views.annotations.FreeTextAnnotationComponent.2
            @Override // javax.swing.event.DocumentListener
            public void insertUpdate(DocumentEvent e2) {
                FreeTextAnnotationComponent.this.contentTextChange = true;
            }

            @Override // javax.swing.event.DocumentListener
            public void removeUpdate(DocumentEvent e2) {
                FreeTextAnnotationComponent.this.contentTextChange = true;
            }

            @Override // javax.swing.event.DocumentListener
            public void changedUpdate(DocumentEvent e2) {
                FreeTextAnnotationComponent.this.contentTextChange = true;
            }
        });
        GridLayout grid = new GridLayout(1, 1, 0, 0);
        setLayout(grid);
        add(this.freeTextPane);
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(this);
        if (annotation.getBbox() != null) {
            setBounds(annotation.getBbox().getBounds());
        }
        resetAppearanceShapes();
        revalidate();
    }

    public void setAppearanceStream() {
        if (this.fontFile == null || this.freeTextAnnotation.isFontPropertyChanged()) {
            this.fontFile = FontManager.getInstance().getType1AWTFont(this.freeTextAnnotation.getFontName(), this.freeTextAnnotation.getFontSize());
        }
        this.freeTextPane.setFont(this.fontFile);
        this.freeTextPane.setForeground(this.freeTextAnnotation.getFontColor());
        if (this.freeTextAnnotation.isFillType()) {
            this.freeTextPane.setOpaque(true);
            this.freeTextPane.setBackground(this.freeTextAnnotation.getFillColor());
        } else {
            this.freeTextPane.setOpaque(false);
        }
        if (this.freeTextAnnotation.isStrokeType()) {
            if (this.freeTextAnnotation.getBorderStyle().isStyleSolid()) {
                this.freeTextPane.setBorder(BorderFactory.createLineBorder(this.freeTextAnnotation.getColor(), (int) this.freeTextAnnotation.getBorderStyle().getStrokeWidth()));
            } else if (this.freeTextAnnotation.getBorderStyle().isStyleDashed()) {
                this.freeTextPane.setBorder(new DashedBorder(this.freeTextAnnotation.getBorderStyle(), this.freeTextAnnotation.getColor()));
            }
        } else {
            this.freeTextPane.setBorder(BorderFactory.createEmptyBorder());
        }
        String content = null;
        try {
            content = this.freeTextPane.getDocument().getText(0, this.freeTextPane.getDocument().getLength());
        } catch (BadLocationException e2) {
            logger.warning("Error getting rich text.");
        }
        Rectangle tBbox = convertToPageSpace(getBounds());
        this.freeTextAnnotation.setBBox(tBbox);
        this.freeTextAnnotation.setContents(content);
        this.freeTextAnnotation.setRichText(this.freeTextPane.getText());
        this.freeTextPane.revalidate();
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent me) {
        super.mouseDragged(me);
        resetAppearanceShapes();
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        Object oldValue = evt.getOldValue();
        if ("focusOwner".equals(prop) && (oldValue instanceof JTextArea)) {
            JTextArea freeText = (JTextArea) oldValue;
            if (freeText.equals(this.freeTextPane)) {
                freeText.setEditable(false);
                if (this.contentTextChange) {
                    this.contentTextChange = false;
                    resetAppearanceShapes();
                }
                if (freeText instanceof FreeTextArea) {
                    ((FreeTextArea) freeText).setActive(false);
                    return;
                }
                return;
            }
            return;
        }
        if ("focusOwner".equals(prop) && (newValue instanceof JTextArea)) {
            JTextArea freeText2 = (JTextArea) newValue;
            if (freeText2.equals(this.freeTextPane) && !this.annotation.getFlagReadOnly()) {
                freeText2.setEditable(true);
                if (freeText2 instanceof FreeTextArea) {
                    ((FreeTextArea) freeText2).setActive(true);
                }
            }
        }
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent me) {
        super.mouseMoved(me);
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
        this.isShowInvisibleBorder = ((this.documentViewModel.getViewToolMode() != 6 && this.documentViewModel.getViewToolMode() != 17) || this.annotation.getFlagReadOnly() || this.annotation.getFlagLocked() || this.annotation.getFlagInvisible() || this.annotation.getFlagHidden()) ? false : true;
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
    public void dispose() {
        super.dispose();
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.removePropertyChangeListener(this);
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
    public void resetAppearanceShapes() {
        setAppearanceStream();
        this.annotation.resetAppearanceStream(getPageTransform());
    }

    public boolean isActive() {
        return this.freeTextPane.isActive();
    }

    public String clearXMLHeader(String strXML) {
        return strXML.replaceFirst("[<][?]\\s*[xml].*[?][>]", "");
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/FreeTextAnnotationComponent$MyHtml2Text.class */
    public class MyHtml2Text extends HTMLEditorKit.ParserCallback {

        /* renamed from: s, reason: collision with root package name */
        StringBuffer f13130s;

        public MyHtml2Text() {
        }

        public void parse(Reader in) throws IOException {
            this.f13130s = new StringBuffer();
            ParserDelegator delegator = new ParserDelegator();
            delegator.parse(in, this, Boolean.TRUE.booleanValue());
        }

        @Override // javax.swing.text.html.HTMLEditorKit.ParserCallback
        public void handleText(char[] text, int pos) {
            this.f13130s.append(text);
            this.f13130s.append("\n");
        }

        public String getText() {
            return this.f13130s.toString();
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/FreeTextAnnotationComponent$DashedBorder.class */
    private class DashedBorder extends AbstractBorder {
        private BasicStroke stroke;
        private Color color;

        public DashedBorder(BorderStyle borderStyle, Color color) {
            int thickness = (int) borderStyle.getStrokeWidth();
            this.stroke = new BasicStroke(thickness, 2, 0, thickness * 2.0f, FreeTextAnnotationComponent.this.freeTextAnnotation.getBorderStyle().getDashArray(), 0.0f);
            this.color = color;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
            float size = this.stroke.getLineWidth();
            if (size > 0.0f) {
                Graphics g3 = g2.create();
                if (g3 instanceof Graphics2D) {
                    Graphics2D g2d = (Graphics2D) g3;
                    g2d.setStroke(this.stroke);
                    g2d.setPaint(this.color != null ? this.color : c2 == null ? null : c2.getForeground());
                    g2d.draw(new Rectangle2D.Float(x2 + (size / 2.0f), y2 + (size / 2.0f), width - size, height - size));
                }
                g3.dispose();
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component c2, Insets insets) {
            int lineWidth = (int) this.stroke.getLineWidth();
            insets.bottom = lineWidth;
            insets.right = lineWidth;
            insets.top = lineWidth;
            insets.left = lineWidth;
            return insets;
        }
    }
}
