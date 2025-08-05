package javax.swing.text.html;

import java.awt.Color;
import java.awt.Shape;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.CSS;

/* loaded from: rt.jar:javax/swing/text/html/InlineView.class */
public class InlineView extends LabelView {
    private boolean nowrap;
    private AttributeSet attr;

    public InlineView(Element element) {
        super(element);
        this.attr = getStyleSheet().getViewAttributes(this);
    }

    @Override // javax.swing.text.GlyphView, javax.swing.text.View
    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.insertUpdate(documentEvent, shape, viewFactory);
    }

    @Override // javax.swing.text.GlyphView, javax.swing.text.View
    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.removeUpdate(documentEvent, shape, viewFactory);
    }

    @Override // javax.swing.text.LabelView, javax.swing.text.GlyphView, javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.changedUpdate(documentEvent, shape, viewFactory);
        this.attr = getStyleSheet().getViewAttributes(this);
        preferenceChanged(null, true, true);
    }

    @Override // javax.swing.text.View
    public AttributeSet getAttributes() {
        return this.attr;
    }

    @Override // javax.swing.text.GlyphView, javax.swing.text.View
    public int getBreakWeight(int i2, float f2, float f3) {
        if (this.nowrap) {
            return 0;
        }
        return super.getBreakWeight(i2, f2, f3);
    }

    @Override // javax.swing.text.GlyphView, javax.swing.text.View
    public View breakView(int i2, int i3, float f2, float f3) {
        return super.breakView(i2, i3, f2, f3);
    }

    @Override // javax.swing.text.LabelView
    protected void setPropertiesFromAttributes() {
        super.setPropertiesFromAttributes();
        AttributeSet attributes = getAttributes();
        Object attribute = attributes.getAttribute(CSS.Attribute.TEXT_DECORATION);
        boolean z2 = attribute != null && attribute.toString().indexOf("underline") >= 0;
        setUnderline(z2);
        boolean z3 = attribute != null && attribute.toString().indexOf("line-through") >= 0;
        setStrikeThrough(z3);
        Object attribute2 = attributes.getAttribute(CSS.Attribute.VERTICAL_ALIGN);
        boolean z4 = attribute2 != null && attribute2.toString().indexOf("sup") >= 0;
        setSuperscript(z4);
        boolean z5 = attribute2 != null && attribute2.toString().indexOf("sub") >= 0;
        setSubscript(z5);
        Object attribute3 = attributes.getAttribute(CSS.Attribute.WHITE_SPACE);
        if (attribute3 != null && attribute3.equals("nowrap")) {
            this.nowrap = true;
        } else {
            this.nowrap = false;
        }
        Color background = ((HTMLDocument) getDocument()).getBackground(attributes);
        if (background != null) {
            setBackground(background);
        }
    }

    protected StyleSheet getStyleSheet() {
        return ((HTMLDocument) getDocument()).getStyleSheet();
    }
}
