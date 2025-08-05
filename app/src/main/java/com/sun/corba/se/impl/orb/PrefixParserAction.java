package com.sun.corba.se.impl.orb;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.StringPair;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/PrefixParserAction.class */
public class PrefixParserAction extends ParserActionBase {
    private Class componentType;
    private ORBUtilSystemException wrapper;

    public PrefixParserAction(String str, Operation operation, String str2, Class cls) {
        super(str, true, operation, str2);
        this.componentType = cls;
        this.wrapper = ORBUtilSystemException.get(CORBALogDomains.ORB_LIFECYCLE);
    }

    @Override // com.sun.corba.se.impl.orb.ParserActionBase, com.sun.corba.se.impl.orb.ParserAction
    public Object apply(Properties properties) {
        String propertyName = getPropertyName();
        int length = propertyName.length();
        if (propertyName.charAt(length - 1) != '.') {
            propertyName = propertyName + '.';
            length++;
        }
        LinkedList linkedList = new LinkedList();
        Iterator<Object> it = properties.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str.startsWith(propertyName)) {
                linkedList.add(getOperation().operate(new StringPair(str.substring(length), properties.getProperty(str))));
            }
        }
        int size = linkedList.size();
        if (size > 0) {
            try {
                Object objNewInstance = Array.newInstance((Class<?>) this.componentType, size);
                int i2 = 0;
                for (Object obj : linkedList) {
                    try {
                        Array.set(objNewInstance, i2, obj);
                        i2++;
                    } catch (Throwable th) {
                        throw this.wrapper.couldNotSetArray(th, getPropertyName(), new Integer(i2), this.componentType, new Integer(size), obj.toString());
                    }
                }
                return objNewInstance;
            } catch (Throwable th2) {
                throw this.wrapper.couldNotCreateArray(th2, getPropertyName(), this.componentType, new Integer(size));
            }
        }
        return null;
    }
}
