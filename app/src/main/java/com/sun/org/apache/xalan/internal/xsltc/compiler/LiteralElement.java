package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.serializer.ElemDesc;
import com.sun.org.apache.xml.internal.serializer.ToHTMLStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/LiteralElement.class */
final class LiteralElement extends Instruction {
    private String _name;
    private LiteralElement _literalElemParent = null;
    private List<SyntaxTreeNode> _attributeElements = null;
    private Map<String, String> _accessedPrefixes = null;
    private boolean _allAttributesUnique = false;

    LiteralElement() {
    }

    public QName getName() {
        return this._qname;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("LiteralElement name = " + this._name);
        displayContents(indent + 4);
    }

    private String accessedNamespace(String prefix) {
        String result;
        if (this._literalElemParent != null && (result = this._literalElemParent.accessedNamespace(prefix)) != null) {
            return result;
        }
        if (this._accessedPrefixes != null) {
            return this._accessedPrefixes.get(prefix);
        }
        return null;
    }

    public void registerNamespace(String prefix, String uri, SymbolTable stable, boolean declared) {
        String old;
        String parentUri;
        if (this._literalElemParent != null && (parentUri = this._literalElemParent.accessedNamespace(prefix)) != null && parentUri.equals(uri)) {
            return;
        }
        if (this._accessedPrefixes == null) {
            this._accessedPrefixes = new Hashtable();
        } else if (!declared && (old = this._accessedPrefixes.get(prefix)) != null) {
            if (old.equals(uri)) {
                return;
            } else {
                prefix = stable.generateNamespacePrefix();
            }
        }
        if (!prefix.equals("xml")) {
            this._accessedPrefixes.put(prefix, uri);
        }
    }

    private String translateQName(QName qname, SymbolTable stable) {
        String localname = qname.getLocalPart();
        String prefix = qname.getPrefix();
        if (prefix == null) {
            prefix = "";
        } else if (prefix.equals("xmlns")) {
            return "xmlns";
        }
        String alternative = stable.lookupPrefixAlias(prefix);
        if (alternative != null) {
            stable.excludeNamespaces(prefix);
            prefix = alternative;
        }
        String uri = lookupNamespace(prefix);
        if (uri == null) {
            return localname;
        }
        registerNamespace(prefix, uri, stable, false);
        if (prefix != "") {
            return prefix + CallSiteDescriptor.TOKEN_DELIMITER + localname;
        }
        return localname;
    }

    public void addAttribute(SyntaxTreeNode attribute) {
        if (this._attributeElements == null) {
            this._attributeElements = new ArrayList(2);
        }
        this._attributeElements.add(attribute);
    }

    public void setFirstAttribute(SyntaxTreeNode attribute) {
        if (this._attributeElements == null) {
            this._attributeElements = new ArrayList(2);
        }
        this._attributeElements.add(0, attribute);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._attributeElements != null) {
            for (SyntaxTreeNode node : this._attributeElements) {
                node.typeCheck(stable);
            }
        }
        typeCheckContents(stable);
        return Type.Void;
    }

    public Set<Map.Entry<String, String>> getNamespaceScope(SyntaxTreeNode node) {
        Map<String, String> all = new HashMap<>();
        while (node != null) {
            Map<String, String> mapping = node.getPrefixMapping();
            if (mapping != null) {
                for (String prefix : mapping.keySet()) {
                    if (!all.containsKey(prefix)) {
                        all.put(prefix, mapping.get(prefix));
                    }
                }
            }
            node = node.getParent();
        }
        return all.entrySet();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String uri;
        SymbolTable stable = parser.getSymbolTable();
        stable.setCurrentNode(this);
        SyntaxTreeNode parent = getParent();
        if (parent != null && (parent instanceof LiteralElement)) {
            this._literalElemParent = (LiteralElement) parent;
        }
        this._name = translateQName(this._qname, stable);
        int count = this._attributes.getLength();
        for (int i2 = 0; i2 < count; i2++) {
            QName qname = parser.getQName(this._attributes.getQName(i2));
            String uri2 = qname.getNamespace();
            String val = this._attributes.getValue(i2);
            if (qname.equals(parser.getUseAttributeSets())) {
                if (!Util.isValidQNames(val)) {
                    ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) val, (SyntaxTreeNode) this);
                    parser.reportError(3, err);
                }
                setFirstAttribute(new UseAttributeSets(val, parser));
            } else if (qname.equals(parser.getExtensionElementPrefixes())) {
                stable.excludeNamespaces(val);
            } else if (qname.equals(parser.getExcludeResultPrefixes())) {
                stable.excludeNamespaces(val);
            } else {
                String prefix = qname.getPrefix();
                if ((prefix == null || !prefix.equals("xmlns")) && ((prefix != null || !qname.getLocalPart().equals("xmlns")) && (uri2 == null || !uri2.equals("http://www.w3.org/1999/XSL/Transform")))) {
                    String name = translateQName(qname, stable);
                    LiteralAttribute attr = new LiteralAttribute(name, val, parser, this);
                    addAttribute(attr);
                    attr.setParent(this);
                    attr.parseContents(parser);
                }
            }
        }
        Set<Map.Entry<String, String>> include = getNamespaceScope(this);
        for (Map.Entry<String, String> entry : include) {
            String prefix2 = entry.getKey();
            if (!prefix2.equals("xml") && (uri = lookupNamespace(prefix2)) != null && !stable.isExcludedNamespace(uri)) {
                registerNamespace(prefix2, uri, stable, true);
            }
        }
        parseChildren(parser);
        for (int i3 = 0; i3 < count; i3++) {
            QName qname2 = parser.getQName(this._attributes.getQName(i3));
            String val2 = this._attributes.getValue(i3);
            if (qname2.equals(parser.getExtensionElementPrefixes())) {
                stable.unExcludeNamespaces(val2);
            } else if (qname2.equals(parser.getExcludeResultPrefixes())) {
                stable.unExcludeNamespaces(val2);
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    protected boolean contextDependent() {
        return dependentContents();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        this._allAttributesUnique = checkAttributesUnique();
        il.append(methodGen.loadHandler());
        il.append(new PUSH(cpg, this._name));
        il.append(DUP2);
        il.append(methodGen.startElement());
        for (int j2 = 0; j2 < elementCount(); j2++) {
            SyntaxTreeNode item = elementAt(j2);
            if (item instanceof Variable) {
                item.translate(classGen, methodGen);
            }
        }
        if (this._accessedPrefixes != null) {
            for (Map.Entry<String, String> entry : this._accessedPrefixes.entrySet()) {
                String prefix = entry.getKey();
                String uri = entry.getValue();
                il.append(methodGen.loadHandler());
                il.append(new PUSH(cpg, prefix));
                il.append(new PUSH(cpg, uri));
                il.append(methodGen.namespace());
            }
        }
        if (this._attributeElements != null) {
            for (SyntaxTreeNode node : this._attributeElements) {
                if (!(node instanceof XslAttribute)) {
                    node.translate(classGen, methodGen);
                }
            }
        }
        translateContents(classGen, methodGen);
        il.append(methodGen.endElement());
    }

    private boolean isHTMLOutput() {
        return getStylesheet().getOutputMethod() == 2;
    }

    public ElemDesc getElemDesc() {
        if (isHTMLOutput()) {
            return ToHTMLStream.getElemDesc(this._name);
        }
        return null;
    }

    public boolean allAttributesUnique() {
        return this._allAttributesUnique;
    }

    private boolean checkAttributesUnique() {
        boolean hasHiddenXslAttribute = canProduceAttributeNodes(this, true);
        if (hasHiddenXslAttribute) {
            return false;
        }
        if (this._attributeElements != null) {
            int numAttrs = this._attributeElements.size();
            Map<String, SyntaxTreeNode> attrsTable = null;
            for (int i2 = 0; i2 < numAttrs; i2++) {
                SyntaxTreeNode node = this._attributeElements.get(i2);
                if (node instanceof UseAttributeSets) {
                    return false;
                }
                if (node instanceof XslAttribute) {
                    if (attrsTable == null) {
                        attrsTable = new HashMap<>();
                        for (int k2 = 0; k2 < i2; k2++) {
                            SyntaxTreeNode n2 = this._attributeElements.get(k2);
                            if (n2 instanceof LiteralAttribute) {
                                LiteralAttribute literalAttr = (LiteralAttribute) n2;
                                attrsTable.put(literalAttr.getName(), literalAttr);
                            }
                        }
                    }
                    XslAttribute xslAttr = (XslAttribute) node;
                    AttributeValue attrName = xslAttr.getName();
                    if (attrName instanceof AttributeValueTemplate) {
                        return false;
                    }
                    if (attrName instanceof SimpleAttributeValue) {
                        SimpleAttributeValue simpleAttr = (SimpleAttributeValue) attrName;
                        String name = simpleAttr.toString();
                        if (name != null && attrsTable.get(name) != null) {
                            return false;
                        }
                        if (name != null) {
                            attrsTable.put(name, xslAttr);
                        }
                    } else {
                        continue;
                    }
                }
            }
            return true;
        }
        return true;
    }

    private boolean canProduceAttributeNodes(SyntaxTreeNode node, boolean ignoreXslAttribute) {
        List<SyntaxTreeNode> contents = node.getContents();
        for (SyntaxTreeNode child : contents) {
            if (child instanceof Text) {
                Text text = (Text) child;
                if (!text.isIgnore()) {
                    return false;
                }
            } else {
                if ((child instanceof LiteralElement) || (child instanceof ValueOf) || (child instanceof XslElement) || (child instanceof Comment) || (child instanceof Number) || (child instanceof ProcessingInstruction)) {
                    return false;
                }
                if (child instanceof XslAttribute) {
                    if (!ignoreXslAttribute) {
                        return true;
                    }
                } else {
                    if ((child instanceof CallTemplate) || (child instanceof ApplyTemplates) || (child instanceof Copy) || (child instanceof CopyOf)) {
                        return true;
                    }
                    if (((child instanceof If) || (child instanceof ForEach)) && canProduceAttributeNodes(child, false)) {
                        return true;
                    }
                    if (child instanceof Choose) {
                        List<SyntaxTreeNode> chooseContents = child.getContents();
                        for (SyntaxTreeNode chooseChild : chooseContents) {
                            if ((chooseChild instanceof When) || (chooseChild instanceof Otherwise)) {
                                if (canProduceAttributeNodes(chooseChild, false)) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return false;
    }
}
