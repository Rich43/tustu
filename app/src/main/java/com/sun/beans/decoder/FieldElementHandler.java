package com.sun.beans.decoder;

import com.sun.beans.finder.FieldFinder;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.reflect.Field;

/* loaded from: rt.jar:com/sun/beans/decoder/FieldElementHandler.class */
final class FieldElementHandler extends AccessorElementHandler {
    private Class<?> type;

    FieldElementHandler() {
    }

    @Override // com.sun.beans.decoder.AccessorElementHandler, com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (str.equals(Constants.ATTRNAME_CLASS)) {
            this.type = getOwner().findClass(str2);
        } else {
            super.addAttribute(str, str2);
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected boolean isArgument() {
        return super.isArgument() && this.type != null;
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected Object getContextBean() {
        return this.type != null ? this.type : super.getContextBean();
    }

    @Override // com.sun.beans.decoder.AccessorElementHandler
    protected Object getValue(String str) {
        try {
            return getFieldValue(getContextBean(), str);
        } catch (Exception e2) {
            getOwner().handleException(e2);
            return null;
        }
    }

    @Override // com.sun.beans.decoder.AccessorElementHandler
    protected void setValue(String str, Object obj) {
        try {
            setFieldValue(getContextBean(), str, obj);
        } catch (Exception e2) {
            getOwner().handleException(e2);
        }
    }

    static Object getFieldValue(Object obj, String str) throws IllegalAccessException, NoSuchFieldException {
        return findField(obj, str).get(obj);
    }

    private static void setFieldValue(Object obj, String str, Object obj2) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        findField(obj, str).set(obj, obj2);
    }

    private static Field findField(Object obj, String str) throws NoSuchFieldException {
        if (obj instanceof Class) {
            return FieldFinder.findStaticField((Class) obj, str);
        }
        return FieldFinder.findField(obj.getClass(), str);
    }
}
