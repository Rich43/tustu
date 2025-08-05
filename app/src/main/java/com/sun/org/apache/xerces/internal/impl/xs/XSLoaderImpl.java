package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSGrammarPool;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xs.LSInputList;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSLoader;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.ls.LSInput;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSLoaderImpl.class */
public final class XSLoaderImpl implements XSLoader, DOMConfiguration {
    private final XSGrammarPool fGrammarPool = new XSGrammarMerger();
    private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader();

    public XSLoaderImpl() throws XMLConfigurationException {
        this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public DOMConfiguration getConfig() {
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public XSModel loadURIList(StringList uriList) {
        int length = uriList.getLength();
        try {
            this.fGrammarPool.clear();
            for (int i2 = 0; i2 < length; i2++) {
                this.fSchemaLoader.loadGrammar(new XMLInputSource(null, uriList.item(i2), null));
            }
            return this.fGrammarPool.toXSModel();
        } catch (Exception e2) {
            this.fSchemaLoader.reportDOMFatalError(e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public XSModel loadInputList(LSInputList is) {
        int length = is.getLength();
        try {
            this.fGrammarPool.clear();
            for (int i2 = 0; i2 < length; i2++) {
                this.fSchemaLoader.loadGrammar(this.fSchemaLoader.dom2xmlInputSource(is.item(i2)));
            }
            return this.fGrammarPool.toXSModel();
        } catch (Exception e2) {
            this.fSchemaLoader.reportDOMFatalError(e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public XSModel loadURI(String uri) {
        try {
            this.fGrammarPool.clear();
            return ((XSGrammar) this.fSchemaLoader.loadGrammar(new XMLInputSource(null, uri, null))).toXSModel();
        } catch (Exception e2) {
            this.fSchemaLoader.reportDOMFatalError(e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public XSModel load(LSInput is) {
        try {
            this.fGrammarPool.clear();
            return ((XSGrammar) this.fSchemaLoader.loadGrammar(this.fSchemaLoader.dom2xmlInputSource(is))).toXSModel();
        } catch (Exception e2) {
            this.fSchemaLoader.reportDOMFatalError(e2);
            return null;
        }
    }

    @Override // org.w3c.dom.DOMConfiguration
    public void setParameter(String name, Object value) throws DOMException {
        this.fSchemaLoader.setParameter(name, value);
    }

    @Override // org.w3c.dom.DOMConfiguration
    public Object getParameter(String name) throws DOMException {
        return this.fSchemaLoader.getParameter(name);
    }

    @Override // org.w3c.dom.DOMConfiguration
    public boolean canSetParameter(String name, Object value) {
        return this.fSchemaLoader.canSetParameter(name, value);
    }

    @Override // org.w3c.dom.DOMConfiguration
    public DOMStringList getParameterNames() {
        return this.fSchemaLoader.getParameterNames();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSLoaderImpl$XSGrammarMerger.class */
    private static final class XSGrammarMerger extends XSGrammarPool {
        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl
        public void putGrammar(Grammar grammar) {
            SchemaGrammar cachedGrammar = toSchemaGrammar(super.getGrammar(grammar.getGrammarDescription()));
            if (cachedGrammar != null) {
                SchemaGrammar newGrammar = toSchemaGrammar(grammar);
                if (newGrammar != null) {
                    mergeSchemaGrammars(cachedGrammar, newGrammar);
                    return;
                }
                return;
            }
            super.putGrammar(grammar);
        }

        private SchemaGrammar toSchemaGrammar(Grammar grammar) {
            if (grammar instanceof SchemaGrammar) {
                return (SchemaGrammar) grammar;
            }
            return null;
        }

        private void mergeSchemaGrammars(SchemaGrammar cachedGrammar, SchemaGrammar newGrammar) {
            XSNamedMap map = newGrammar.getComponents((short) 2);
            int length = map.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                XSElementDecl decl = (XSElementDecl) map.item(i2);
                if (cachedGrammar.getGlobalElementDecl(decl.getName()) == null) {
                    cachedGrammar.addGlobalElementDecl(decl);
                }
            }
            XSNamedMap map2 = newGrammar.getComponents((short) 1);
            int length2 = map2.getLength();
            for (int i3 = 0; i3 < length2; i3++) {
                XSAttributeDecl decl2 = (XSAttributeDecl) map2.item(i3);
                if (cachedGrammar.getGlobalAttributeDecl(decl2.getName()) == null) {
                    cachedGrammar.addGlobalAttributeDecl(decl2);
                }
            }
            XSNamedMap map3 = newGrammar.getComponents((short) 3);
            int length3 = map3.getLength();
            for (int i4 = 0; i4 < length3; i4++) {
                XSTypeDefinition decl3 = (XSTypeDefinition) map3.item(i4);
                if (cachedGrammar.getGlobalTypeDecl(decl3.getName()) == null) {
                    cachedGrammar.addGlobalTypeDecl(decl3);
                }
            }
            XSNamedMap map4 = newGrammar.getComponents((short) 5);
            int length4 = map4.getLength();
            for (int i5 = 0; i5 < length4; i5++) {
                XSAttributeGroupDecl decl4 = (XSAttributeGroupDecl) map4.item(i5);
                if (cachedGrammar.getGlobalAttributeGroupDecl(decl4.getName()) == null) {
                    cachedGrammar.addGlobalAttributeGroupDecl(decl4);
                }
            }
            XSNamedMap map5 = newGrammar.getComponents((short) 7);
            int length5 = map5.getLength();
            for (int i6 = 0; i6 < length5; i6++) {
                XSGroupDecl decl5 = (XSGroupDecl) map5.item(i6);
                if (cachedGrammar.getGlobalGroupDecl(decl5.getName()) == null) {
                    cachedGrammar.addGlobalGroupDecl(decl5);
                }
            }
            XSNamedMap map6 = newGrammar.getComponents((short) 11);
            int length6 = map6.getLength();
            for (int i7 = 0; i7 < length6; i7++) {
                XSNotationDecl decl6 = (XSNotationDecl) map6.item(i7);
                if (cachedGrammar.getGlobalNotationDecl(decl6.getName()) == null) {
                    cachedGrammar.addGlobalNotationDecl(decl6);
                }
            }
            XSObjectList annotations = newGrammar.getAnnotations();
            int length7 = annotations.getLength();
            for (int i8 = 0; i8 < length7; i8++) {
                cachedGrammar.addAnnotation((XSAnnotationImpl) annotations.item(i8));
            }
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl
        public boolean containsGrammar(XMLGrammarDescription desc) {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl
        public Grammar getGrammar(XMLGrammarDescription desc) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl, com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar retrieveGrammar(XMLGrammarDescription desc) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl, com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar[] retrieveInitialGrammarSet(String grammarType) {
            return new Grammar[0];
        }
    }
}
