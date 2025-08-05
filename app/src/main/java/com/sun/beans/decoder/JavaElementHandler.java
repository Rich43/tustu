package com.sun.beans.decoder;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.beans.XMLDecoder;

/* loaded from: rt.jar:com/sun/beans/decoder/JavaElementHandler.class */
final class JavaElementHandler extends ElementHandler {
    private Class<?> type;
    private ValueObject value;

    JavaElementHandler() {
    }

    @Override // com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (!str.equals("version")) {
            if (str.equals(Constants.ATTRNAME_CLASS)) {
                this.type = getOwner().findClass(str2);
            } else {
                super.addAttribute(str, str2);
            }
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected void addArgument(Object obj) {
        getOwner().addObject(obj);
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected boolean isArgument() {
        return false;
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected ValueObject getValueObject() {
        if (this.value == null) {
            this.value = ValueObjectImpl.create(getValue());
        }
        return this.value;
    }

    private Object getValue() {
        Object owner = getOwner().getOwner();
        if (this.type == null || isValid(owner)) {
            return owner;
        }
        if (owner instanceof XMLDecoder) {
            owner = ((XMLDecoder) owner).getOwner();
            if (isValid(owner)) {
                return owner;
            }
        }
        throw new IllegalStateException("Unexpected owner class: " + owner.getClass().getName());
    }

    private boolean isValid(Object obj) {
        return obj == null || this.type.isInstance(obj);
    }
}
