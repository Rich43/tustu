package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import com.sun.org.apache.bcel.internal.generic.BasicType;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.DUP_X1;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.ICONST;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/SyntaxTreeNode.class */
public abstract class SyntaxTreeNode implements Constants {
    private Parser _parser;
    protected SyntaxTreeNode _parent;
    private Stylesheet _stylesheet;
    private Template _template;
    private final List<SyntaxTreeNode> _contents;
    protected QName _qname;
    private int _line;
    protected AttributesImpl _attributes;
    private Map<String, String> _prefixMapping;
    protected static final int IndentIncrement = 4;
    protected static final SyntaxTreeNode Dummy = new AbsolutePathPattern(null);
    private static final char[] _spaces = "                                                       ".toCharArray();

    public abstract Type typeCheck(SymbolTable symbolTable) throws TypeCheckError;

    public abstract void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator);

    public SyntaxTreeNode() {
        this._contents = new ArrayList(2);
        this._attributes = null;
        this._prefixMapping = null;
        this._line = 0;
        this._qname = null;
    }

    public SyntaxTreeNode(int line) {
        this._contents = new ArrayList(2);
        this._attributes = null;
        this._prefixMapping = null;
        this._line = line;
        this._qname = null;
    }

    public SyntaxTreeNode(String uri, String prefix, String local) {
        this._contents = new ArrayList(2);
        this._attributes = null;
        this._prefixMapping = null;
        this._line = 0;
        setQName(uri, prefix, local);
    }

    protected final void setLineNumber(int line) {
        this._line = line;
    }

    public final int getLineNumber() {
        if (this._line > 0) {
            return this._line;
        }
        SyntaxTreeNode parent = getParent();
        if (parent != null) {
            return parent.getLineNumber();
        }
        return 0;
    }

    protected void setQName(QName qname) {
        this._qname = qname;
    }

    protected void setQName(String uri, String prefix, String localname) {
        this._qname = new QName(uri, prefix, localname);
    }

    protected QName getQName() {
        return this._qname;
    }

    protected void setAttributes(AttributesImpl attributes) {
        this._attributes = attributes;
    }

    protected String getAttribute(String qname) {
        String value;
        return (this._attributes == null || (value = this._attributes.getValue(qname)) == null || value.equals("")) ? "" : value;
    }

    protected String getAttribute(String prefix, String localName) {
        return getAttribute(prefix + ':' + localName);
    }

    protected boolean hasAttribute(String qname) {
        return (this._attributes == null || this._attributes.getValue(qname) == null) ? false : true;
    }

    protected void addAttribute(String qname, String value) {
        int index = this._attributes.getIndex(qname);
        if (index != -1) {
            this._attributes.setAttribute(index, "", Util.getLocalName(qname), qname, "CDATA", value);
        } else {
            this._attributes.addAttribute("", Util.getLocalName(qname), qname, "CDATA", value);
        }
    }

    protected Attributes getAttributes() {
        return this._attributes;
    }

    protected void setPrefixMapping(Map<String, String> mapping) {
        this._prefixMapping = mapping;
    }

    protected Map<String, String> getPrefixMapping() {
        return this._prefixMapping;
    }

    protected void addPrefixMapping(String prefix, String uri) {
        if (this._prefixMapping == null) {
            this._prefixMapping = new HashMap();
        }
        this._prefixMapping.put(prefix, uri);
    }

    protected String lookupNamespace(String prefix) {
        String uri = null;
        if (this._prefixMapping != null) {
            uri = this._prefixMapping.get(prefix);
        }
        if (uri == null && this._parent != null) {
            uri = this._parent.lookupNamespace(prefix);
            if (prefix == "" && uri == null) {
                uri = "";
            }
        }
        return uri;
    }

    protected String lookupPrefix(String uri) {
        String prefix = null;
        if (this._prefixMapping != null && this._prefixMapping.containsValue(uri)) {
            for (Map.Entry<String, String> entry : this._prefixMapping.entrySet()) {
                prefix = entry.getKey();
                String mapsTo = entry.getValue();
                if (mapsTo.equals(uri)) {
                    return prefix;
                }
            }
        } else if (this._parent != null) {
            prefix = this._parent.lookupPrefix(uri);
            if (uri == "" && prefix == null) {
                prefix = "";
            }
        }
        return prefix;
    }

    protected void setParser(Parser parser) {
        this._parser = parser;
    }

    public final Parser getParser() {
        return this._parser;
    }

    protected void setParent(SyntaxTreeNode parent) {
        if (this._parent == null) {
            this._parent = parent;
        }
    }

    protected final SyntaxTreeNode getParent() {
        return this._parent;
    }

    protected final boolean isDummy() {
        return this == Dummy;
    }

    protected int getImportPrecedence() {
        Stylesheet stylesheet = getStylesheet();
        if (stylesheet == null) {
            return Integer.MIN_VALUE;
        }
        return stylesheet.getImportPrecedence();
    }

    public Stylesheet getStylesheet() {
        if (this._stylesheet == null) {
            SyntaxTreeNode parent = this;
            while (true) {
                SyntaxTreeNode parent2 = parent;
                if (parent2 != null) {
                    if (parent2 instanceof Stylesheet) {
                        return (Stylesheet) parent2;
                    }
                    parent = parent2.getParent();
                } else {
                    this._stylesheet = (Stylesheet) parent2;
                    break;
                }
            }
        }
        return this._stylesheet;
    }

    protected Template getTemplate() {
        SyntaxTreeNode parent;
        if (this._template == null) {
            SyntaxTreeNode parent2 = this;
            while (true) {
                parent = parent2;
                if (parent == null || (parent instanceof Template)) {
                    break;
                }
                parent2 = parent.getParent();
            }
            this._template = (Template) parent;
        }
        return this._template;
    }

    protected final XSLTC getXSLTC() {
        return this._parser.getXSLTC();
    }

    protected final SymbolTable getSymbolTable() {
        if (this._parser == null) {
            return null;
        }
        return this._parser.getSymbolTable();
    }

    public void parseContents(Parser parser) {
        parseChildren(parser);
    }

    protected final void parseChildren(Parser parser) {
        List<QName> locals = null;
        for (SyntaxTreeNode child : this._contents) {
            parser.getSymbolTable().setCurrentNode(child);
            child.parseContents(parser);
            QName varOrParamName = updateScope(parser, child);
            if (varOrParamName != null) {
                if (locals == null) {
                    locals = new ArrayList<>(2);
                }
                locals.add(varOrParamName);
            }
        }
        parser.getSymbolTable().setCurrentNode(this);
        if (locals != null) {
            Iterator<QName> it = locals.iterator();
            while (it.hasNext()) {
                parser.removeVariable(it.next());
            }
        }
    }

    protected QName updateScope(Parser parser, SyntaxTreeNode node) {
        if (node instanceof Variable) {
            Variable var = (Variable) node;
            parser.addVariable(var);
            return var.getName();
        }
        if (node instanceof Param) {
            Param param = (Param) node;
            parser.addParameter(param);
            return param.getName();
        }
        return null;
    }

    protected Type typeCheckContents(SymbolTable stable) throws TypeCheckError {
        for (SyntaxTreeNode item : this._contents) {
            item.typeCheck(stable);
        }
        return Type.Void;
    }

    protected void translateContents(ClassGenerator classGen, MethodGenerator methodGen) {
        int n2 = elementCount();
        for (SyntaxTreeNode item : this._contents) {
            methodGen.markChunkStart();
            item.translate(classGen, methodGen);
            methodGen.markChunkEnd();
        }
        for (int i2 = 0; i2 < n2; i2++) {
            if (this._contents.get(i2) instanceof VariableBase) {
                VariableBase var = (VariableBase) this._contents.get(i2);
                var.unmapRegister(classGen, methodGen);
            }
        }
    }

    private boolean isSimpleRTF(SyntaxTreeNode node) {
        List<SyntaxTreeNode> contents = node.getContents();
        for (SyntaxTreeNode item : contents) {
            if (!isTextElement(item, false)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAdaptiveRTF(SyntaxTreeNode node) {
        List<SyntaxTreeNode> contents = node.getContents();
        for (SyntaxTreeNode item : contents) {
            if (!isTextElement(item, true)) {
                return false;
            }
        }
        return true;
    }

    private boolean isTextElement(SyntaxTreeNode node, boolean doExtendedCheck) {
        if ((node instanceof ValueOf) || (node instanceof Number) || (node instanceof Text)) {
            return true;
        }
        if (node instanceof If) {
            return doExtendedCheck ? isAdaptiveRTF(node) : isSimpleRTF(node);
        }
        if (node instanceof Choose) {
            List<SyntaxTreeNode> contents = node.getContents();
            for (SyntaxTreeNode item : contents) {
                if (!(item instanceof Text)) {
                    if ((item instanceof When) || (item instanceof Otherwise)) {
                        if (!doExtendedCheck || !isAdaptiveRTF(item)) {
                            if (doExtendedCheck || !isSimpleRTF(item)) {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
        if (!doExtendedCheck) {
            return false;
        }
        if ((node instanceof CallTemplate) || (node instanceof ApplyTemplates)) {
            return true;
        }
        return false;
    }

    protected void compileResultTree(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        Stylesheet stylesheet = classGen.getStylesheet();
        boolean isSimple = isSimpleRTF(this);
        boolean isAdaptive = false;
        if (!isSimple) {
            isAdaptive = isAdaptiveRTF(this);
        }
        int rtfType = isSimple ? 0 : isAdaptive ? 1 : 2;
        il.append(methodGen.loadHandler());
        String DOM_CLASS = classGen.getDOMClass();
        il.append(methodGen.loadDOM());
        int index = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getResultTreeFrag", "(IIZ)Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
        il.append(new PUSH(cpg, 32));
        il.append(new PUSH(cpg, rtfType));
        il.append(new PUSH(cpg, stylesheet.callsNodeset()));
        il.append(new INVOKEINTERFACE(index, 4));
        il.append(DUP);
        int index2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getOutputDomBuilder", "()Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
        il.append(new INVOKEINTERFACE(index2, 1));
        il.append(DUP);
        il.append(methodGen.storeHandler());
        il.append(methodGen.startDocument());
        translateContents(classGen, methodGen);
        il.append(methodGen.loadHandler());
        il.append(methodGen.endDocument());
        if (stylesheet.callsNodeset() && !DOM_CLASS.equals(Constants.DOM_IMPL_CLASS)) {
            int index3 = cpg.addMethodref(Constants.DOM_ADAPTER_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;[Ljava/lang/String;[Ljava/lang/String;[I[Ljava/lang/String;)V");
            il.append(new NEW(cpg.addClass(Constants.DOM_ADAPTER_CLASS)));
            il.append(new DUP_X1());
            il.append(SWAP);
            if (!stylesheet.callsNodeset()) {
                il.append(new ICONST(0));
                il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
                il.append(DUP);
                il.append(DUP);
                il.append(new ICONST(0));
                il.append(new NEWARRAY(BasicType.INT));
                il.append(SWAP);
                il.append(new INVOKESPECIAL(index3));
            } else {
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.NAMES_INDEX, "[Ljava/lang/String;")));
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.URIS_INDEX, "[Ljava/lang/String;")));
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.TYPES_INDEX, Constants.TYPES_INDEX_SIG)));
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.NAMESPACE_INDEX, "[Ljava/lang/String;")));
                il.append(new INVOKESPECIAL(index3));
                il.append(DUP);
                il.append(methodGen.loadDOM());
                il.append(new CHECKCAST(cpg.addClass(classGen.getDOMClass())));
                il.append(SWAP);
                int index4 = cpg.addMethodref(Constants.MULTI_DOM_CLASS, "addDOMAdapter", "(Lcom/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter;)I");
                il.append(new INVOKEVIRTUAL(index4));
                il.append(POP);
            }
        }
        il.append(SWAP);
        il.append(methodGen.storeHandler());
    }

    protected boolean contextDependent() {
        return true;
    }

    protected boolean dependentContents() {
        for (SyntaxTreeNode item : this._contents) {
            if (item.contextDependent()) {
                return true;
            }
        }
        return false;
    }

    protected final void addElement(SyntaxTreeNode element) {
        this._contents.add(element);
        element.setParent(this);
    }

    protected final void setFirstElement(SyntaxTreeNode element) {
        this._contents.add(0, element);
        element.setParent(this);
    }

    protected final void removeElement(SyntaxTreeNode element) {
        this._contents.remove(element);
        element.setParent(null);
    }

    protected final List<SyntaxTreeNode> getContents() {
        return this._contents;
    }

    protected final boolean hasContents() {
        return elementCount() > 0;
    }

    protected final int elementCount() {
        return this._contents.size();
    }

    protected final Iterator<SyntaxTreeNode> elements() {
        return this._contents.iterator();
    }

    protected final SyntaxTreeNode elementAt(int pos) {
        return this._contents.get(pos);
    }

    protected final SyntaxTreeNode lastChild() {
        if (this._contents.isEmpty()) {
            return null;
        }
        return this._contents.get(this._contents.size() - 1);
    }

    public void display(int indent) {
        displayContents(indent);
    }

    protected void displayContents(int indent) {
        for (SyntaxTreeNode item : this._contents) {
            item.display(indent);
        }
    }

    protected final void indent(int indent) {
        System.out.print(new String(_spaces, 0, indent));
    }

    protected void reportError(SyntaxTreeNode element, Parser parser, String errorCode, String message) {
        ErrorMsg error = new ErrorMsg(errorCode, (Object) message, element);
        parser.reportError(3, error);
    }

    protected void reportWarning(SyntaxTreeNode element, Parser parser, String errorCode, String message) {
        ErrorMsg error = new ErrorMsg(errorCode, (Object) message, element);
        parser.reportError(4, error);
    }
}
