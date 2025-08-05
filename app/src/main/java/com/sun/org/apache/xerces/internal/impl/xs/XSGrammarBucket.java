package com.sun.org.apache.xerces.internal.impl.xs;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSGrammarBucket.class */
public class XSGrammarBucket {
    Map<String, SchemaGrammar> fGrammarRegistry = new HashMap();
    SchemaGrammar fNoNSGrammar = null;

    public SchemaGrammar getGrammar(String namespace) {
        if (namespace == null) {
            return this.fNoNSGrammar;
        }
        return this.fGrammarRegistry.get(namespace);
    }

    public void putGrammar(SchemaGrammar grammar) {
        if (grammar.getTargetNamespace() == null) {
            this.fNoNSGrammar = grammar;
        } else {
            this.fGrammarRegistry.put(grammar.getTargetNamespace(), grammar);
        }
    }

    public boolean putGrammar(SchemaGrammar grammar, boolean deep) {
        SchemaGrammar sg = getGrammar(grammar.fTargetNamespace);
        if (sg != null) {
            return sg == grammar;
        }
        if (!deep) {
            putGrammar(grammar);
            return true;
        }
        Vector currGrammars = grammar.getImportedGrammars();
        if (currGrammars == null) {
            putGrammar(grammar);
            return true;
        }
        Vector grammars = (Vector) currGrammars.clone();
        for (int i2 = 0; i2 < grammars.size(); i2++) {
            SchemaGrammar sg1 = (SchemaGrammar) grammars.elementAt(i2);
            SchemaGrammar sg2 = getGrammar(sg1.fTargetNamespace);
            if (sg2 == null) {
                Vector gs = sg1.getImportedGrammars();
                if (gs != null) {
                    for (int j2 = gs.size() - 1; j2 >= 0; j2--) {
                        SchemaGrammar sg22 = (SchemaGrammar) gs.elementAt(j2);
                        if (!grammars.contains(sg22)) {
                            grammars.addElement(sg22);
                        }
                    }
                }
            } else if (sg2 != sg1) {
                return false;
            }
        }
        putGrammar(grammar);
        for (int i3 = grammars.size() - 1; i3 >= 0; i3--) {
            putGrammar((SchemaGrammar) grammars.elementAt(i3));
        }
        return true;
    }

    public boolean putGrammar(SchemaGrammar grammar, boolean deep, boolean ignoreConflict) {
        Vector currGrammars;
        if (!ignoreConflict) {
            return putGrammar(grammar, deep);
        }
        SchemaGrammar sg = getGrammar(grammar.fTargetNamespace);
        if (sg == null) {
            putGrammar(grammar);
        }
        if (!deep || (currGrammars = grammar.getImportedGrammars()) == null) {
            return true;
        }
        Vector grammars = (Vector) currGrammars.clone();
        for (int i2 = 0; i2 < grammars.size(); i2++) {
            SchemaGrammar sg1 = (SchemaGrammar) grammars.elementAt(i2);
            if (getGrammar(sg1.fTargetNamespace) == null) {
                Vector gs = sg1.getImportedGrammars();
                if (gs != null) {
                    for (int j2 = gs.size() - 1; j2 >= 0; j2--) {
                        SchemaGrammar sg2 = (SchemaGrammar) gs.elementAt(j2);
                        if (!grammars.contains(sg2)) {
                            grammars.addElement(sg2);
                        }
                    }
                }
            } else {
                grammars.remove(sg1);
            }
        }
        for (int i3 = grammars.size() - 1; i3 >= 0; i3--) {
            putGrammar((SchemaGrammar) grammars.elementAt(i3));
        }
        return true;
    }

    public SchemaGrammar[] getGrammars() {
        int count = this.fGrammarRegistry.size() + (this.fNoNSGrammar == null ? 0 : 1);
        SchemaGrammar[] grammars = new SchemaGrammar[count];
        int i2 = 0;
        for (Map.Entry<String, SchemaGrammar> entry : this.fGrammarRegistry.entrySet()) {
            int i3 = i2;
            i2++;
            grammars[i3] = entry.getValue();
        }
        if (this.fNoNSGrammar != null) {
            grammars[count - 1] = this.fNoNSGrammar;
        }
        return grammars;
    }

    public void reset() {
        this.fNoNSGrammar = null;
        this.fGrammarRegistry.clear();
    }
}
