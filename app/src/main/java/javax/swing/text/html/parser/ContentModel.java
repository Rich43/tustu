package javax.swing.text.html.parser;

import java.io.Serializable;
import java.util.Vector;
import org.slf4j.Marker;

/* loaded from: rt.jar:javax/swing/text/html/parser/ContentModel.class */
public final class ContentModel implements Serializable {
    public int type;
    public Object content;
    public ContentModel next;
    private boolean[] valSet;
    private boolean[] val;

    public ContentModel() {
    }

    public ContentModel(Element element) {
        this(0, element, null);
    }

    public ContentModel(int i2, ContentModel contentModel) {
        this(i2, contentModel, null);
    }

    public ContentModel(int i2, Object obj, ContentModel contentModel) {
        this.type = i2;
        this.content = obj;
        this.next = contentModel;
    }

    public boolean empty() {
        switch (this.type) {
            case 38:
            case 44:
                ContentModel contentModel = (ContentModel) this.content;
                while (true) {
                    ContentModel contentModel2 = contentModel;
                    if (contentModel2 == null) {
                        break;
                    } else if (!contentModel2.empty()) {
                        break;
                    } else {
                        contentModel = contentModel2.next;
                    }
                }
            case 42:
            case 63:
                break;
            case 43:
            case 124:
                ContentModel contentModel3 = (ContentModel) this.content;
                while (true) {
                    ContentModel contentModel4 = contentModel3;
                    if (contentModel4 == null) {
                        break;
                    } else if (contentModel4.empty()) {
                        break;
                    } else {
                        contentModel3 = contentModel4.next;
                    }
                }
        }
        return true;
    }

    public void getElements(Vector<Element> vector) {
        switch (this.type) {
            case 38:
            case 44:
            case 124:
                ContentModel contentModel = (ContentModel) this.content;
                while (true) {
                    ContentModel contentModel2 = contentModel;
                    if (contentModel2 == null) {
                        break;
                    } else {
                        contentModel2.getElements(vector);
                        contentModel = contentModel2.next;
                    }
                }
            case 42:
            case 43:
            case 63:
                ((ContentModel) this.content).getElements(vector);
                break;
            default:
                vector.addElement((Element) this.content);
                break;
        }
    }

    public boolean first(Object obj) {
        switch (this.type) {
            case 38:
            case 124:
                Element element = (Element) obj;
                if (this.valSet == null || this.valSet.length <= Element.getMaxIndex()) {
                    this.valSet = new boolean[Element.getMaxIndex() + 1];
                    this.val = new boolean[this.valSet.length];
                }
                if (this.valSet[element.index]) {
                    return this.val[element.index];
                }
                ContentModel contentModel = (ContentModel) this.content;
                while (true) {
                    ContentModel contentModel2 = contentModel;
                    if (contentModel2 != null) {
                        if (!contentModel2.first(obj)) {
                            contentModel = contentModel2.next;
                        } else {
                            this.val[element.index] = true;
                        }
                    }
                }
                this.valSet[element.index] = true;
                return this.val[element.index];
            case 42:
            case 43:
            case 63:
                return ((ContentModel) this.content).first(obj);
            case 44:
                ContentModel contentModel3 = (ContentModel) this.content;
                while (true) {
                    ContentModel contentModel4 = contentModel3;
                    if (contentModel4 != null) {
                        if (contentModel4.first(obj)) {
                            return true;
                        }
                        if (contentModel4.empty()) {
                            contentModel3 = contentModel4.next;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            default:
                return this.content == obj;
        }
    }

    public Element first() {
        switch (this.type) {
            case 38:
            case 42:
            case 63:
            case 124:
                return null;
            case 43:
            case 44:
                return ((ContentModel) this.content).first();
            default:
                return (Element) this.content;
        }
    }

    public String toString() {
        switch (this.type) {
            case 38:
            case 44:
            case 124:
                char[] cArr = {' ', (char) this.type, ' '};
                String str = "";
                ContentModel contentModel = (ContentModel) this.content;
                while (true) {
                    ContentModel contentModel2 = contentModel;
                    if (contentModel2 != null) {
                        str = str + ((Object) contentModel2);
                        if (contentModel2.next != null) {
                            str = str + new String(cArr);
                        }
                        contentModel = contentModel2.next;
                    } else {
                        return "(" + str + ")";
                    }
                }
            case 42:
                return this.content + "*";
            case 43:
                return this.content + Marker.ANY_NON_NULL_MARKER;
            case 63:
                return this.content + "?";
            default:
                return this.content.toString();
        }
    }
}
