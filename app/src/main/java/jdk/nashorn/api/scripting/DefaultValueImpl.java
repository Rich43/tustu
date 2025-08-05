package jdk.nashorn.api.scripting;

import com.sun.javafx.fxml.BeanAdapter;
import jdk.nashorn.internal.runtime.JSType;

/* loaded from: nashorn.jar:jdk/nashorn/api/scripting/DefaultValueImpl.class */
class DefaultValueImpl {
    private static final String[] DEFAULT_VALUE_FNS_NUMBER = {BeanAdapter.VALUE_OF_METHOD_NAME, "toString"};
    private static final String[] DEFAULT_VALUE_FNS_STRING = {"toString", BeanAdapter.VALUE_OF_METHOD_NAME};

    DefaultValueImpl() {
    }

    static Object getDefaultValue(JSObject jsobj, Class<?> hint) throws UnsupportedOperationException {
        boolean isNumber = hint == null || hint == Number.class;
        for (String methodName : isNumber ? DEFAULT_VALUE_FNS_NUMBER : DEFAULT_VALUE_FNS_STRING) {
            Object objMember = jsobj.getMember(methodName);
            if (objMember instanceof JSObject) {
                JSObject member = (JSObject) objMember;
                if (member.isFunction()) {
                    Object value = member.call(jsobj, new Object[0]);
                    if (JSType.isPrimitive(value)) {
                        return value;
                    }
                } else {
                    continue;
                }
            }
        }
        throw new UnsupportedOperationException(isNumber ? "cannot.get.default.number" : "cannot.get.default.string");
    }
}
