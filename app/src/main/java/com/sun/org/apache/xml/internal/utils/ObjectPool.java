package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/ObjectPool.class */
public class ObjectPool implements Serializable {
    static final long serialVersionUID = -8519013691660936643L;
    private final Class objectType;
    private final ArrayList freeStack;

    public ObjectPool(Class type) {
        this.objectType = type;
        this.freeStack = new ArrayList();
    }

    public ObjectPool(String className) {
        try {
            this.objectType = ObjectFactory.findProviderClass(className, true);
            this.freeStack = new ArrayList();
        } catch (ClassNotFoundException cnfe) {
            throw new WrappedRuntimeException(cnfe);
        }
    }

    public ObjectPool(Class type, int size) {
        this.objectType = type;
        this.freeStack = new ArrayList(size);
    }

    public ObjectPool() {
        this.objectType = null;
        this.freeStack = new ArrayList();
    }

    public synchronized Object getInstanceIfFree() {
        if (!this.freeStack.isEmpty()) {
            Object result = this.freeStack.remove(this.freeStack.size() - 1);
            return result;
        }
        return null;
    }

    public synchronized Object getInstance() {
        if (this.freeStack.isEmpty()) {
            try {
                return this.objectType.newInstance();
            } catch (IllegalAccessException | InstantiationException e2) {
                throw new RuntimeException(XMLMessages.createXMLMessage("ER_EXCEPTION_CREATING_POOL", null));
            }
        }
        Object result = this.freeStack.remove(this.freeStack.size() - 1);
        return result;
    }

    public synchronized void freeInstance(Object obj) {
        this.freeStack.add(obj);
    }
}
