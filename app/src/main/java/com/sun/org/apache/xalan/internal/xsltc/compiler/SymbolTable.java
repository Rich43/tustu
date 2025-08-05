package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/SymbolTable.class */
final class SymbolTable {
    private final Map<String, Stylesheet> _stylesheets = new HashMap();
    private final Map<String, Vector> _primops = new HashMap();
    private Map<String, VariableBase> _variables = null;
    private Map<String, Template> _templates = null;
    private Map<String, AttributeSet> _attributeSets = null;
    private Map<String, String> _aliases = null;
    private Map<String, Integer> _excludedURI = null;
    private Stack<Map<String, Integer>> _excludedURIStack = null;
    private Map<String, DecimalFormatting> _decimalFormats = null;
    private Map<String, Key> _keys = null;
    private int _nsCounter = 0;
    private SyntaxTreeNode _current = null;

    SymbolTable() {
    }

    public DecimalFormatting getDecimalFormatting(QName name) {
        if (this._decimalFormats == null) {
            return null;
        }
        return this._decimalFormats.get(name.getStringRep());
    }

    public void addDecimalFormatting(QName name, DecimalFormatting symbols) {
        if (this._decimalFormats == null) {
            this._decimalFormats = new HashMap();
        }
        this._decimalFormats.put(name.getStringRep(), symbols);
    }

    public Key getKey(QName name) {
        if (this._keys == null) {
            return null;
        }
        return this._keys.get(name.getStringRep());
    }

    public void addKey(QName name, Key key) {
        if (this._keys == null) {
            this._keys = new HashMap();
        }
        this._keys.put(name.getStringRep(), key);
    }

    public Stylesheet addStylesheet(QName name, Stylesheet node) {
        return this._stylesheets.put(name.getStringRep(), node);
    }

    public Stylesheet lookupStylesheet(QName name) {
        return this._stylesheets.get(name.getStringRep());
    }

    public Template addTemplate(Template template) {
        QName name = template.getName();
        if (this._templates == null) {
            this._templates = new HashMap();
        }
        return this._templates.put(name.getStringRep(), template);
    }

    public Template lookupTemplate(QName name) {
        if (this._templates == null) {
            return null;
        }
        return this._templates.get(name.getStringRep());
    }

    public Variable addVariable(Variable variable) {
        if (this._variables == null) {
            this._variables = new HashMap();
        }
        String name = variable.getName().getStringRep();
        return (Variable) this._variables.put(name, variable);
    }

    public Param addParam(Param parameter) {
        if (this._variables == null) {
            this._variables = new HashMap();
        }
        String name = parameter.getName().getStringRep();
        return (Param) this._variables.put(name, parameter);
    }

    public Variable lookupVariable(QName qname) {
        if (this._variables == null) {
            return null;
        }
        String name = qname.getStringRep();
        VariableBase obj = this._variables.get(name);
        if (obj instanceof Variable) {
            return (Variable) obj;
        }
        return null;
    }

    public Param lookupParam(QName qname) {
        if (this._variables == null) {
            return null;
        }
        String name = qname.getStringRep();
        VariableBase obj = this._variables.get(name);
        if (obj instanceof Param) {
            return (Param) obj;
        }
        return null;
    }

    public SyntaxTreeNode lookupName(QName qname) {
        if (this._variables == null) {
            return null;
        }
        String name = qname.getStringRep();
        return this._variables.get(name);
    }

    public AttributeSet addAttributeSet(AttributeSet atts) {
        if (this._attributeSets == null) {
            this._attributeSets = new HashMap();
        }
        return this._attributeSets.put(atts.getName().getStringRep(), atts);
    }

    public AttributeSet lookupAttributeSet(QName name) {
        if (this._attributeSets == null) {
            return null;
        }
        return this._attributeSets.get(name.getStringRep());
    }

    public void addPrimop(String name, MethodType mtype) {
        Vector methods = this._primops.get(name);
        if (methods == null) {
            Map<String, Vector> map = this._primops;
            Vector vector = new Vector();
            methods = vector;
            map.put(name, vector);
        }
        methods.addElement(mtype);
    }

    public Vector lookupPrimop(String name) {
        return this._primops.get(name);
    }

    public String generateNamespacePrefix() {
        StringBuilder sbAppend = new StringBuilder().append(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NS);
        int i2 = this._nsCounter;
        this._nsCounter = i2 + 1;
        return sbAppend.append(i2).toString();
    }

    public void setCurrentNode(SyntaxTreeNode node) {
        this._current = node;
    }

    public String lookupNamespace(String prefix) {
        return this._current == null ? "" : this._current.lookupNamespace(prefix);
    }

    public void addPrefixAlias(String prefix, String alias) {
        if (this._aliases == null) {
            this._aliases = new HashMap();
        }
        this._aliases.put(prefix, alias);
    }

    public String lookupPrefixAlias(String prefix) {
        if (this._aliases == null) {
            return null;
        }
        return this._aliases.get(prefix);
    }

    public void excludeURI(String uri) {
        Integer refcnt;
        if (uri == null) {
            return;
        }
        if (this._excludedURI == null) {
            this._excludedURI = new HashMap();
        }
        Integer refcnt2 = this._excludedURI.get(uri);
        if (refcnt2 == null) {
            refcnt = 1;
        } else {
            refcnt = Integer.valueOf(refcnt2.intValue() + 1);
        }
        this._excludedURI.put(uri, refcnt);
    }

    public void excludeNamespaces(String prefixes) {
        String uri;
        if (prefixes != null) {
            StringTokenizer tokens = new StringTokenizer(prefixes);
            while (tokens.hasMoreTokens()) {
                String prefix = tokens.nextToken();
                if (prefix.equals("#default")) {
                    uri = lookupNamespace("");
                } else {
                    uri = lookupNamespace(prefix);
                }
                if (uri != null) {
                    excludeURI(uri);
                }
            }
        }
    }

    public boolean isExcludedNamespace(String uri) {
        Integer refcnt;
        return (uri == null || this._excludedURI == null || (refcnt = this._excludedURI.get(uri)) == null || refcnt.intValue() <= 0) ? false : true;
    }

    public void unExcludeNamespaces(String prefixes) {
        String uri;
        if (this._excludedURI != null && prefixes != null) {
            StringTokenizer tokens = new StringTokenizer(prefixes);
            while (tokens.hasMoreTokens()) {
                String prefix = tokens.nextToken();
                if (prefix.equals("#default")) {
                    uri = lookupNamespace("");
                } else {
                    uri = lookupNamespace(prefix);
                }
                Integer refcnt = this._excludedURI.get(uri);
                if (refcnt != null) {
                    this._excludedURI.put(uri, Integer.valueOf(refcnt.intValue() - 1));
                }
            }
        }
    }

    public void pushExcludedNamespacesContext() {
        if (this._excludedURIStack == null) {
            this._excludedURIStack = new Stack<>();
        }
        this._excludedURIStack.push(this._excludedURI);
        this._excludedURI = null;
    }

    public void popExcludedNamespacesContext() {
        this._excludedURI = this._excludedURIStack.pop();
        if (this._excludedURIStack.isEmpty()) {
            this._excludedURIStack = null;
        }
    }
}
