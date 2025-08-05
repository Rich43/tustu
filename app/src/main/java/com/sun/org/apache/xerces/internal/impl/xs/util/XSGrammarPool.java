package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.XSModelImpl;
import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XSGrammarPool.class */
public class XSGrammarPool extends XMLGrammarPoolImpl {
    public XSModel toXSModel() {
        return toXSModel((short) 1);
    }

    public XSModel toXSModel(short schemaVersion) {
        ArrayList list = new ArrayList();
        for (int i2 = 0; i2 < this.fGrammars.length; i2++) {
            XMLGrammarPoolImpl.Entry entry = this.fGrammars[i2];
            while (true) {
                XMLGrammarPoolImpl.Entry entry2 = entry;
                if (entry2 != null) {
                    if (entry2.desc.getGrammarType().equals("http://www.w3.org/2001/XMLSchema")) {
                        list.add(entry2.grammar);
                    }
                    entry = entry2.next;
                }
            }
        }
        int size = list.size();
        if (size == 0) {
            return toXSModel(new SchemaGrammar[0], schemaVersion);
        }
        SchemaGrammar[] gs = (SchemaGrammar[]) list.toArray(new SchemaGrammar[size]);
        return toXSModel(gs, schemaVersion);
    }

    protected XSModel toXSModel(SchemaGrammar[] grammars, short schemaVersion) {
        return new XSModelImpl(grammars, schemaVersion);
    }
}
