package javax.swing.text.html.parser;

import java.util.BitSet;

/* loaded from: rt.jar:javax/swing/text/html/parser/TagStack.class */
final class TagStack implements DTDConstants {
    TagElement tag;
    Element elem;
    ContentModelState state;
    TagStack next;
    BitSet inclusions;
    BitSet exclusions;

    /* renamed from: net, reason: collision with root package name */
    boolean f12856net;
    boolean pre;

    TagStack(TagElement tagElement, TagStack tagStack) {
        this.tag = tagElement;
        this.elem = tagElement.getElement();
        this.next = tagStack;
        Element element = tagElement.getElement();
        if (element.getContent() != null) {
            this.state = new ContentModelState(element.getContent());
        }
        if (tagStack != null) {
            this.inclusions = tagStack.inclusions;
            this.exclusions = tagStack.exclusions;
            this.pre = tagStack.pre;
        }
        if (tagElement.isPreformatted()) {
            this.pre = true;
        }
        if (element.inclusions != null) {
            if (this.inclusions != null) {
                this.inclusions = (BitSet) this.inclusions.clone();
                this.inclusions.or(element.inclusions);
            } else {
                this.inclusions = element.inclusions;
            }
        }
        if (element.exclusions != null) {
            if (this.exclusions != null) {
                this.exclusions = (BitSet) this.exclusions.clone();
                this.exclusions.or(element.exclusions);
            } else {
                this.exclusions = element.exclusions;
            }
        }
    }

    public Element first() {
        if (this.state != null) {
            return this.state.first();
        }
        return null;
    }

    public ContentModel contentModel() {
        if (this.state == null) {
            return null;
        }
        return this.state.getModel();
    }

    boolean excluded(int i2) {
        return this.exclusions != null && this.exclusions.get(this.elem.getIndex());
    }

    boolean advance(Element element) {
        if (this.exclusions != null && this.exclusions.get(element.getIndex())) {
            return false;
        }
        if (this.state != null) {
            ContentModelState contentModelStateAdvance = this.state.advance(element);
            if (contentModelStateAdvance != null) {
                this.state = contentModelStateAdvance;
                return true;
            }
        } else if (this.elem.getType() == 19) {
            return true;
        }
        return this.inclusions != null && this.inclusions.get(element.getIndex());
    }

    boolean terminate() {
        return this.state == null || this.state.terminate();
    }

    public String toString() {
        if (this.next == null) {
            return "<" + this.tag.getElement().getName() + ">";
        }
        return ((Object) this.next) + " <" + this.tag.getElement().getName() + ">";
    }
}
