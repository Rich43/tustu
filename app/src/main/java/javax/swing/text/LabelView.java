package javax.swing.text;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Shape;
import java.awt.Toolkit;
import javax.swing.event.DocumentEvent;

/* loaded from: rt.jar:javax/swing/text/LabelView.class */
public class LabelView extends GlyphView implements TabableView {
    private Font font;
    private Color fg;

    /* renamed from: bg, reason: collision with root package name */
    private Color f12839bg;
    private boolean underline;
    private boolean strike;
    private boolean superscript;
    private boolean subscript;

    public LabelView(Element element) {
        super(element);
    }

    final void sync() {
        if (this.font == null) {
            setPropertiesFromAttributes();
        }
    }

    protected void setUnderline(boolean z2) {
        this.underline = z2;
    }

    protected void setStrikeThrough(boolean z2) {
        this.strike = z2;
    }

    protected void setSuperscript(boolean z2) {
        this.superscript = z2;
    }

    protected void setSubscript(boolean z2) {
        this.subscript = z2;
    }

    protected void setBackground(Color color) {
        this.f12839bg = color;
    }

    protected void setPropertiesFromAttributes() {
        AttributeSet attributes = getAttributes();
        if (attributes != null) {
            Document document = getDocument();
            if (document instanceof StyledDocument) {
                StyledDocument styledDocument = (StyledDocument) document;
                this.font = styledDocument.getFont(attributes);
                this.fg = styledDocument.getForeground(attributes);
                if (attributes.isDefined(StyleConstants.Background)) {
                    this.f12839bg = styledDocument.getBackground(attributes);
                } else {
                    this.f12839bg = null;
                }
                setUnderline(StyleConstants.isUnderline(attributes));
                setStrikeThrough(StyleConstants.isStrikeThrough(attributes));
                setSuperscript(StyleConstants.isSuperscript(attributes));
                setSubscript(StyleConstants.isSubscript(attributes));
                return;
            }
            throw new StateInvariantError("LabelView needs StyledDocument");
        }
    }

    @Deprecated
    protected FontMetrics getFontMetrics() {
        sync();
        Container container = getContainer();
        return container != null ? container.getFontMetrics(this.font) : Toolkit.getDefaultToolkit().getFontMetrics(this.font);
    }

    @Override // javax.swing.text.GlyphView
    public Color getBackground() {
        sync();
        return this.f12839bg;
    }

    @Override // javax.swing.text.GlyphView
    public Color getForeground() {
        sync();
        return this.fg;
    }

    @Override // javax.swing.text.GlyphView
    public Font getFont() {
        sync();
        return this.font;
    }

    @Override // javax.swing.text.GlyphView
    public boolean isUnderline() {
        sync();
        return this.underline;
    }

    @Override // javax.swing.text.GlyphView
    public boolean isStrikeThrough() {
        sync();
        return this.strike;
    }

    @Override // javax.swing.text.GlyphView
    public boolean isSubscript() {
        sync();
        return this.subscript;
    }

    @Override // javax.swing.text.GlyphView
    public boolean isSuperscript() {
        sync();
        return this.superscript;
    }

    @Override // javax.swing.text.GlyphView, javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        this.font = null;
        super.changedUpdate(documentEvent, shape, viewFactory);
    }
}
