package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xs.XSObject;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XSInputSource.class */
public final class XSInputSource extends XMLInputSource {
    private SchemaGrammar[] fGrammars;
    private XSObject[] fComponents;

    public XSInputSource(SchemaGrammar[] grammars) {
        super(null, null, null);
        this.fGrammars = grammars;
        this.fComponents = null;
    }

    public XSInputSource(XSObject[] component) {
        super(null, null, null);
        this.fGrammars = null;
        this.fComponents = component;
    }

    public SchemaGrammar[] getGrammars() {
        return this.fGrammars;
    }

    public void setGrammars(SchemaGrammar[] grammars) {
        this.fGrammars = grammars;
    }

    public XSObject[] getComponents() {
        return this.fComponents;
    }

    public void setComponents(XSObject[] components) {
        this.fComponents = components;
    }
}
