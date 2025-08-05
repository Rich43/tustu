package com.sun.corba.se.impl.orb;

import com.sun.corba.se.spi.orb.Operation;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserActionFactory.class */
public class ParserActionFactory {
    private ParserActionFactory() {
    }

    public static ParserAction makeNormalAction(String str, Operation operation, String str2) {
        return new NormalParserAction(str, operation, str2);
    }

    public static ParserAction makePrefixAction(String str, Operation operation, String str2, Class cls) {
        return new PrefixParserAction(str, operation, str2, cls);
    }
}
