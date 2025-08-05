package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/StringElementHandler.class */
public class StringElementHandler extends ElementHandler {
    private StringBuilder sb = new StringBuilder();
    private ValueObject value = ValueObjectImpl.NULL;

    @Override // com.sun.beans.decoder.ElementHandler
    public final void addCharacter(char c2) {
        if (this.sb == null) {
            throw new IllegalStateException("Could not add chararcter to evaluated string element");
        }
        this.sb.append(c2);
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected final void addArgument(Object obj) {
        if (this.sb == null) {
            throw new IllegalStateException("Could not add argument to evaluated string element");
        }
        this.sb.append(obj);
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected final ValueObject getValueObject() {
        try {
        } catch (RuntimeException e2) {
            getOwner().handleException(e2);
        } finally {
            this.sb = null;
        }
        if (this.sb != null) {
            this.value = ValueObjectImpl.create(getValue(this.sb.toString()));
        }
        return this.value;
    }

    protected Object getValue(String str) {
        return str;
    }
}
