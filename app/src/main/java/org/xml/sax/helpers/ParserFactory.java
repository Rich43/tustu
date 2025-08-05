package org.xml.sax.helpers;

import org.xml.sax.Parser;

/* loaded from: rt.jar:org/xml/sax/helpers/ParserFactory.class */
public class ParserFactory {
    private static SecuritySupport ss = new SecuritySupport();

    private ParserFactory() {
    }

    public static Parser makeParser() throws IllegalAccessException, InstantiationException, ClassNotFoundException, ClassCastException, NullPointerException {
        String className = ss.getSystemProperty("org.xml.sax.parser");
        if (className == null) {
            throw new NullPointerException("No value for sax.parser property");
        }
        return makeParser(className);
    }

    public static Parser makeParser(String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException, ClassCastException {
        return (Parser) NewInstance.newInstance(ss.getContextClassLoader(), className);
    }
}
