package com.sun.corba.se.impl.orb;

import com.sun.corba.se.spi.orb.Operation;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/NormalParserAction.class */
public class NormalParserAction extends ParserActionBase {
    public NormalParserAction(String str, Operation operation, String str2) {
        super(str, false, operation, str2);
    }

    @Override // com.sun.corba.se.impl.orb.ParserActionBase, com.sun.corba.se.impl.orb.ParserAction
    public Object apply(Properties properties) {
        String property = properties.getProperty(getPropertyName());
        if (property != null) {
            return getOperation().operate(property);
        }
        return null;
    }
}
