package javax.swing.text.html;

import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/text/html/LineView.class */
class LineView extends ParagraphView {
    int tabBase;

    public LineView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.html.ParagraphView, javax.swing.text.View
    public boolean isVisible() {
        return true;
    }

    @Override // javax.swing.text.html.ParagraphView, javax.swing.text.BoxView, javax.swing.text.View
    public float getMinimumSpan(int i2) {
        return getPreferredSpan(i2);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public int getResizeWeight(int i2) {
        switch (i2) {
            case 0:
                return 1;
            case 1:
                return 0;
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.ParagraphView, javax.swing.text.BoxView, javax.swing.text.View
    public float getAlignment(int i2) {
        if (i2 == 0) {
            return 0.0f;
        }
        return super.getAlignment(i2);
    }

    @Override // javax.swing.text.FlowView, javax.swing.text.BoxView
    protected void layout(int i2, int i3) {
        super.layout(2147483646, i3);
    }

    @Override // javax.swing.text.ParagraphView, javax.swing.text.TabExpander
    public float nextTabStop(float f2, int i2) {
        if (getTabSet() == null && StyleConstants.getAlignment(getAttributes()) == 0) {
            return getPreTab(f2, i2);
        }
        return super.nextTabStop(f2, i2);
    }

    protected float getPreTab(float f2, int i2) {
        Document document = getDocument();
        View viewAtPosition = getViewAtPosition(i2, null);
        if ((document instanceof StyledDocument) && viewAtPosition != null) {
            Font font = ((StyledDocument) document).getFont(viewAtPosition.getAttributes());
            Container container = getContainer();
            int charactersPerTab = getCharactersPerTab() * (container != null ? container.getFontMetrics(font) : Toolkit.getDefaultToolkit().getFontMetrics(font)).charWidth('W');
            int tabBase = (int) getTabBase();
            return ((((((int) f2) - tabBase) / charactersPerTab) + 1) * charactersPerTab) + tabBase;
        }
        return 10.0f + f2;
    }

    protected int getCharactersPerTab() {
        return 8;
    }
}
