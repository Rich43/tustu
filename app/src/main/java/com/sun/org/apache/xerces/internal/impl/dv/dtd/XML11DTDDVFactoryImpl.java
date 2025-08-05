package com.sun.org.apache.xerces.internal.impl.dv.dtd;

import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/dtd/XML11DTDDVFactoryImpl.class */
public class XML11DTDDVFactoryImpl extends DTDDVFactoryImpl {
    static Map<String, DatatypeValidator> XML11BUILTINTYPES;

    static {
        Map<String, DatatypeValidator> xml11BuiltInTypes = new HashMap<>();
        xml11BuiltInTypes.put("XML11ID", new XML11IDDatatypeValidator());
        DatatypeValidator dvTemp = new XML11IDREFDatatypeValidator();
        xml11BuiltInTypes.put("XML11IDREF", dvTemp);
        xml11BuiltInTypes.put("XML11IDREFS", new ListDatatypeValidator(dvTemp));
        DatatypeValidator dvTemp2 = new XML11NMTOKENDatatypeValidator();
        xml11BuiltInTypes.put("XML11NMTOKEN", dvTemp2);
        xml11BuiltInTypes.put("XML11NMTOKENS", new ListDatatypeValidator(dvTemp2));
        XML11BUILTINTYPES = Collections.unmodifiableMap(xml11BuiltInTypes);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl, com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory
    public DatatypeValidator getBuiltInDV(String name) {
        if (XML11BUILTINTYPES.get(name) != null) {
            return XML11BUILTINTYPES.get(name);
        }
        return fBuiltInTypes.get(name);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl, com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory
    public Map<String, DatatypeValidator> getBuiltInTypes() {
        HashMap<String, DatatypeValidator> toReturn = new HashMap<>(fBuiltInTypes);
        toReturn.putAll(XML11BUILTINTYPES);
        return toReturn;
    }
}
