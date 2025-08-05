package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/VarElementHandler.class */
final class VarElementHandler extends ElementHandler {
    private ValueObject value;

    VarElementHandler() {
    }

    @Override // com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (str.equals("idref")) {
            this.value = ValueObjectImpl.create(getVariable(str2));
        } else {
            super.addAttribute(str, str2);
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected ValueObject getValueObject() {
        if (this.value == null) {
            throw new IllegalArgumentException("Variable name is not set");
        }
        return this.value;
    }
}
