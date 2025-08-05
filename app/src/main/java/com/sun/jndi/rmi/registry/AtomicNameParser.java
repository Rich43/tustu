package com.sun.jndi.rmi.registry;

import java.util.Properties;
import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/* compiled from: RegistryContext.java */
/* loaded from: rt.jar:com/sun/jndi/rmi/registry/AtomicNameParser.class */
class AtomicNameParser implements NameParser {
    private static final Properties syntax = new Properties();

    AtomicNameParser() {
    }

    @Override // javax.naming.NameParser
    public Name parse(String str) throws NamingException {
        return new CompoundName(str, syntax);
    }
}
