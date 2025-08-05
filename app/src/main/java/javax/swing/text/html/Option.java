package javax.swing.text.html;

import java.io.Serializable;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/Option.class */
public class Option implements Serializable {
    private boolean selected;
    private String label;
    private AttributeSet attr;

    public Option(AttributeSet attributeSet) {
        this.attr = attributeSet.copyAttributes();
        this.selected = attributeSet.getAttribute(HTML.Attribute.SELECTED) != null;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public String getLabel() {
        return this.label;
    }

    public AttributeSet getAttributes() {
        return this.attr;
    }

    public String toString() {
        return this.label;
    }

    protected void setSelection(boolean z2) {
        this.selected = z2;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public String getValue() {
        String str = (String) this.attr.getAttribute(HTML.Attribute.VALUE);
        if (str == null) {
            str = this.label;
        }
        return str;
    }
}
