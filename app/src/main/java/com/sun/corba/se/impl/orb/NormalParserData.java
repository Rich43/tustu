package com.sun.corba.se.impl.orb;

import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.PropertyParser;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/NormalParserData.class */
public class NormalParserData extends ParserDataBase {
    private String testData;

    public NormalParserData(String str, Operation operation, String str2, Object obj, Object obj2, String str3) {
        super(str, operation, str2, obj, obj2);
        this.testData = str3;
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public void addToParser(PropertyParser propertyParser) {
        propertyParser.add(getPropertyName(), getOperation(), getFieldName());
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public void addToProperties(Properties properties) {
        properties.setProperty(getPropertyName(), this.testData);
    }
}
