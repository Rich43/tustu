package com.sun.beans.decoder;

import java.lang.reflect.Array;

/* loaded from: rt.jar:com/sun/beans/decoder/ArrayElementHandler.class */
final class ArrayElementHandler extends NewElementHandler {
    private Integer length;

    ArrayElementHandler() {
    }

    @Override // com.sun.beans.decoder.NewElementHandler, com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (str.equals("length")) {
            this.length = Integer.valueOf(str2);
        } else {
            super.addAttribute(str, str2);
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    public void startElement() {
        if (this.length != null) {
            getValueObject();
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected boolean isArgument() {
        return true;
    }

    @Override // com.sun.beans.decoder.NewElementHandler
    protected ValueObject getValueObject(Class<?> cls, Object[] objArr) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        if (cls == null) {
            cls = Object.class;
        }
        if (this.length != null) {
            return ValueObjectImpl.create(Array.newInstance(cls, this.length.intValue()));
        }
        Object objNewInstance = Array.newInstance(cls, objArr.length);
        for (int i2 = 0; i2 < objArr.length; i2++) {
            Array.set(objNewInstance, i2, objArr[i2]);
        }
        return ValueObjectImpl.create(objNewInstance);
    }
}
