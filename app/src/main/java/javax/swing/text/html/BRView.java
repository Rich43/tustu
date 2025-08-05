package javax.swing.text.html;

import javax.swing.text.Element;

/* loaded from: rt.jar:javax/swing/text/html/BRView.class */
class BRView extends InlineView {
    public BRView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.html.InlineView, javax.swing.text.GlyphView, javax.swing.text.View
    public int getBreakWeight(int i2, float f2, float f3) {
        if (i2 == 0) {
            return 3000;
        }
        return super.getBreakWeight(i2, f2, f3);
    }
}
