package com.sun.corba.se.spi.orb;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/ParserImplBase.class */
public abstract class ParserImplBase {
    private ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.ORB_LIFECYCLE);

    protected abstract PropertyParser makeParser();

    protected void complete() {
    }

    public void init(DataCollector dataCollector) {
        PropertyParser propertyParserMakeParser = makeParser();
        dataCollector.setParser(propertyParserMakeParser);
        setFields(propertyParserMakeParser.parse(dataCollector.getProperties()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Field getAnyField(String str) {
        try {
            Class<?> superclass = getClass();
            Field declaredField = superclass.getDeclaredField(str);
            while (declaredField == null) {
                superclass = superclass.getSuperclass();
                if (superclass == null) {
                    break;
                }
                declaredField = superclass.getDeclaredField(str);
            }
            if (declaredField == null) {
                throw this.wrapper.fieldNotFound(str);
            }
            return declaredField;
        } catch (Exception e2) {
            throw this.wrapper.fieldNotFound(e2, str);
        }
    }

    protected void setFields(Map map) {
        for (Map.Entry entry : map.entrySet()) {
            final String str = (String) entry.getKey();
            final Object value = entry.getValue();
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.corba.se.spi.orb.ParserImplBase.1
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() throws IllegalAccessException, IllegalArgumentException {
                        Field anyField = ParserImplBase.this.getAnyField(str);
                        anyField.setAccessible(true);
                        anyField.set(ParserImplBase.this, value);
                        return null;
                    }
                });
            } catch (PrivilegedActionException e2) {
                throw this.wrapper.errorSettingField(e2.getCause(), str, value.toString());
            }
        }
        complete();
    }
}
