package com.sun.corba.se.spi.orb;

import com.sun.corba.se.impl.orb.ParserAction;
import com.sun.corba.se.impl.orb.ParserActionFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/PropertyParser.class */
public class PropertyParser {
    private List actions = new LinkedList();

    public PropertyParser add(String str, Operation operation, String str2) {
        this.actions.add(ParserActionFactory.makeNormalAction(str, operation, str2));
        return this;
    }

    public PropertyParser addPrefix(String str, Operation operation, String str2, Class cls) {
        this.actions.add(ParserActionFactory.makePrefixAction(str, operation, str2, cls));
        return this;
    }

    public Map parse(Properties properties) {
        HashMap map = new HashMap();
        for (ParserAction parserAction : this.actions) {
            Object objApply = parserAction.apply(properties);
            if (objApply != null) {
                map.put(parserAction.getFieldName(), objApply);
            }
        }
        return map;
    }

    public Iterator iterator() {
        return this.actions.iterator();
    }
}
