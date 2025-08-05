package com.sun.xml.internal.ws.spi.db;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/PropertyGetterBase.class */
public abstract class PropertyGetterBase implements PropertyGetter {
    protected Class type;

    @Override // com.sun.xml.internal.ws.spi.db.PropertyGetter
    public Class getType() {
        return this.type;
    }

    public static boolean getterPattern(Method method) {
        if (!method.getReturnType().equals(Void.TYPE)) {
            if (method.getParameterTypes() == null || method.getParameterTypes().length == 0) {
                if (method.getName().startsWith("get") && method.getName().length() > 3) {
                    return true;
                }
                if (method.getReturnType().equals(Boolean.TYPE) && method.getName().startsWith(BeanAdapter.IS_PREFIX) && method.getName().length() > 2) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
