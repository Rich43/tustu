package com.sun.corba.se.spi.orb;

import com.sun.corba.se.impl.orb.NormalParserData;
import com.sun.corba.se.impl.orb.PrefixParserData;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/ParserDataFactory.class */
public class ParserDataFactory {
    public static ParserData make(String str, Operation operation, String str2, Object obj, Object obj2, String str3) {
        return new NormalParserData(str, operation, str2, obj, obj2, str3);
    }

    public static ParserData make(String str, Operation operation, String str2, Object obj, Object obj2, StringPair[] stringPairArr, Class cls) {
        return new PrefixParserData(str, operation, str2, obj, obj2, stringPairArr, cls);
    }
}
