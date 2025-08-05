package javax.swing.text.html.parser;

import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/parser/TagElement.class */
public class TagElement {
    Element elem;
    HTML.Tag htmlTag;
    boolean insertedByErrorRecovery;

    public TagElement(Element element) {
        this(element, false);
    }

    public TagElement(Element element, boolean z2) {
        this.elem = element;
        this.htmlTag = HTML.getTag(element.getName());
        if (this.htmlTag == null) {
            this.htmlTag = new HTML.UnknownTag(element.getName());
        }
        this.insertedByErrorRecovery = z2;
    }

    public boolean breaksFlow() {
        return this.htmlTag.breaksFlow();
    }

    public boolean isPreformatted() {
        return this.htmlTag.isPreformatted();
    }

    public Element getElement() {
        return this.elem;
    }

    public HTML.Tag getHTMLTag() {
        return this.htmlTag;
    }

    public boolean fictional() {
        return this.insertedByErrorRecovery;
    }
}
