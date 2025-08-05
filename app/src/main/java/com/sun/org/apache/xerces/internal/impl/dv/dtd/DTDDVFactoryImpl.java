package com.sun.org.apache.xerces.internal.impl.dv.dtd;

import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/dtd/DTDDVFactoryImpl.class */
public class DTDDVFactoryImpl extends DTDDVFactory {
    static final Map<String, DatatypeValidator> fBuiltInTypes;

    static {
        Map<String, DatatypeValidator> builtInTypes = new HashMap<>();
        builtInTypes.put("string", new StringDatatypeValidator());
        builtInTypes.put("ID", new IDDatatypeValidator());
        DatatypeValidator dvTemp = new IDREFDatatypeValidator();
        builtInTypes.put(SchemaSymbols.ATTVAL_IDREF, dvTemp);
        builtInTypes.put(SchemaSymbols.ATTVAL_IDREFS, new ListDatatypeValidator(dvTemp));
        DatatypeValidator dvTemp2 = new ENTITYDatatypeValidator();
        builtInTypes.put(SchemaSymbols.ATTVAL_ENTITY, new ENTITYDatatypeValidator());
        builtInTypes.put(SchemaSymbols.ATTVAL_ENTITIES, new ListDatatypeValidator(dvTemp2));
        builtInTypes.put(SchemaSymbols.ATTVAL_NOTATION, new NOTATIONDatatypeValidator());
        DatatypeValidator dvTemp3 = new NMTOKENDatatypeValidator();
        builtInTypes.put(SchemaSymbols.ATTVAL_NMTOKEN, dvTemp3);
        builtInTypes.put(SchemaSymbols.ATTVAL_NMTOKENS, new ListDatatypeValidator(dvTemp3));
        fBuiltInTypes = Collections.unmodifiableMap(builtInTypes);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory
    public DatatypeValidator getBuiltInDV(String name) {
        return fBuiltInTypes.get(name);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory
    public Map<String, DatatypeValidator> getBuiltInTypes() {
        return new HashMap(fBuiltInTypes);
    }
}
