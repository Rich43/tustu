package com.sun.org.apache.xerces.internal.impl.dv;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl;
import com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl;
import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/DTDDVFactory.class */
public abstract class DTDDVFactory {
    private static final String DEFAULT_FACTORY_CLASS = "com.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl";
    private static final String XML11_DATATYPE_VALIDATOR_FACTORY = "com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl";

    public abstract DatatypeValidator getBuiltInDV(String str);

    public abstract Map<String, DatatypeValidator> getBuiltInTypes();

    public static final DTDDVFactory getInstance() throws DVFactoryException {
        return getInstance(DEFAULT_FACTORY_CLASS);
    }

    public static final DTDDVFactory getInstance(String factoryClass) throws DVFactoryException {
        try {
            if (DEFAULT_FACTORY_CLASS.equals(factoryClass)) {
                return new DTDDVFactoryImpl();
            }
            if (XML11_DATATYPE_VALIDATOR_FACTORY.equals(factoryClass)) {
                return new XML11DTDDVFactoryImpl();
            }
            return (DTDDVFactory) ObjectFactory.newInstance(factoryClass, true);
        } catch (ClassCastException e2) {
            throw new DVFactoryException("DTD factory class " + factoryClass + " does not extend from DTDDVFactory.");
        }
    }

    protected DTDDVFactory() {
    }
}
