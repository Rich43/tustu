package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/ElementHandler.class */
public abstract class ElementHandler {
    private DocumentHandler owner;
    private ElementHandler parent;
    private String id;

    protected abstract ValueObject getValueObject();

    public final DocumentHandler getOwner() {
        return this.owner;
    }

    final void setOwner(DocumentHandler documentHandler) {
        if (documentHandler == null) {
            throw new IllegalArgumentException("Every element should have owner");
        }
        this.owner = documentHandler;
    }

    public final ElementHandler getParent() {
        return this.parent;
    }

    final void setParent(ElementHandler elementHandler) {
        this.parent = elementHandler;
    }

    protected final Object getVariable(String str) {
        if (str.equals(this.id)) {
            ValueObject valueObject = getValueObject();
            if (valueObject.isVoid()) {
                throw new IllegalStateException("The element does not return value");
            }
            return valueObject.getValue();
        }
        if (this.parent != null) {
            return this.parent.getVariable(str);
        }
        return this.owner.getVariable(str);
    }

    protected Object getContextBean() {
        if (this.parent != null) {
            ValueObject valueObject = this.parent.getValueObject();
            if (!valueObject.isVoid()) {
                return valueObject.getValue();
            }
            throw new IllegalStateException("The outer element does not return value");
        }
        Object owner = this.owner.getOwner();
        if (owner != null) {
            return owner;
        }
        throw new IllegalStateException("The topmost element does not have context");
    }

    public void addAttribute(String str, String str2) {
        if (str.equals("id")) {
            this.id = str2;
            return;
        }
        throw new IllegalArgumentException("Unsupported attribute: " + str);
    }

    public void startElement() {
    }

    public void endElement() {
        ValueObject valueObject = getValueObject();
        if (!valueObject.isVoid()) {
            if (this.id != null) {
                this.owner.setVariable(this.id, valueObject.getValue());
            }
            if (isArgument()) {
                if (this.parent != null) {
                    this.parent.addArgument(valueObject.getValue());
                } else {
                    this.owner.addObject(valueObject.getValue());
                }
            }
        }
    }

    public void addCharacter(char c2) {
        if (c2 != ' ' && c2 != '\n' && c2 != '\t' && c2 != '\r') {
            throw new IllegalStateException("Illegal character with code " + ((int) c2));
        }
    }

    protected void addArgument(Object obj) {
        throw new IllegalStateException("Could not add argument to simple element");
    }

    protected boolean isArgument() {
        return this.id == null;
    }
}
