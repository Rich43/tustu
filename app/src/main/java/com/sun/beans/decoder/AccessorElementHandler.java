package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/AccessorElementHandler.class */
abstract class AccessorElementHandler extends ElementHandler {
    private String name;
    private ValueObject value;

    protected abstract Object getValue(String str);

    protected abstract void setValue(String str, Object obj);

    AccessorElementHandler() {
    }

    @Override // com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (str.equals("name")) {
            this.name = str2;
        } else {
            super.addAttribute(str, str2);
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected final void addArgument(Object obj) {
        if (this.value != null) {
            throw new IllegalStateException("Could not add argument to evaluated element");
        }
        setValue(this.name, obj);
        this.value = ValueObjectImpl.VOID;
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected final ValueObject getValueObject() {
        if (this.value == null) {
            this.value = ValueObjectImpl.create(getValue(this.name));
        }
        return this.value;
    }
}
