package com.sun.corba.se.impl.orb;

import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.PropertyParser;
import com.sun.corba.se.spi.orb.StringPair;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/PrefixParserData.class */
public class PrefixParserData extends ParserDataBase {
    private StringPair[] testData;
    private Class componentType;

    public PrefixParserData(String str, Operation operation, String str2, Object obj, Object obj2, StringPair[] stringPairArr, Class cls) {
        super(str, operation, str2, obj, obj2);
        this.testData = stringPairArr;
        this.componentType = cls;
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public void addToParser(PropertyParser propertyParser) {
        propertyParser.addPrefix(getPropertyName(), getOperation(), getFieldName(), this.componentType);
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public void addToProperties(Properties properties) {
        for (int i2 = 0; i2 < this.testData.length; i2++) {
            StringPair stringPair = this.testData[i2];
            String propertyName = getPropertyName();
            if (propertyName.charAt(propertyName.length() - 1) != '.') {
                propertyName = propertyName + ".";
            }
            properties.setProperty(propertyName + stringPair.getFirst(), stringPair.getSecond());
        }
    }
}
