package com.sun.beans.decoder;

import java.beans.Expression;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/beans/decoder/ObjectElementHandler.class */
class ObjectElementHandler extends NewElementHandler {
    private String idref;
    private String field;
    private Integer index;
    private String property;
    private String method;

    ObjectElementHandler() {
    }

    @Override // com.sun.beans.decoder.NewElementHandler, com.sun.beans.decoder.ElementHandler
    public final void addAttribute(String str, String str2) {
        if (str.equals("idref")) {
            this.idref = str2;
            return;
        }
        if (str.equals("field")) {
            this.field = str2;
            return;
        }
        if (str.equals("index")) {
            this.index = Integer.valueOf(str2);
            addArgument(this.index);
        } else if (str.equals("property")) {
            this.property = str2;
        } else if (str.equals("method")) {
            this.method = str2;
        } else {
            super.addAttribute(str, str2);
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    public final void startElement() {
        if (this.field != null || this.idref != null) {
            getValueObject();
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected boolean isArgument() {
        return true;
    }

    @Override // com.sun.beans.decoder.NewElementHandler
    protected final ValueObject getValueObject(Class<?> cls, Object[] objArr) throws Exception {
        String str;
        if (this.field != null) {
            return ValueObjectImpl.create(FieldElementHandler.getFieldValue(getContextBean(), this.field));
        }
        if (this.idref != null) {
            return ValueObjectImpl.create(getVariable(this.idref));
        }
        Object contextBean = getContextBean();
        if (this.index != null) {
            str = objArr.length == 2 ? "set" : "get";
        } else if (this.property != null) {
            str = objArr.length == 1 ? "set" : "get";
            if (0 < this.property.length()) {
                str = str + this.property.substring(0, 1).toUpperCase(Locale.ENGLISH) + this.property.substring(1);
            }
        } else {
            str = (this.method == null || 0 >= this.method.length()) ? "new" : this.method;
        }
        return ValueObjectImpl.create(new Expression(contextBean, str, objArr).getValue());
    }
}
