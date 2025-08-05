package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NamedMethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import java.util.List;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Template.class */
public final class Template extends TopLevelElement {
    private QName _name;
    private QName _mode;
    private Pattern _pattern;
    private double _priority;
    private int _position;
    private boolean _disabled = false;
    private boolean _compiled = false;
    private boolean _simplified = false;
    private boolean _isSimpleNamedTemplate = false;
    private Vector<Param> _parameters = new Vector<>();
    private Stylesheet _stylesheet = null;

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement
    public /* bridge */ /* synthetic */ Vector getDependencies() {
        return super.getDependencies();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement
    public /* bridge */ /* synthetic */ void addDependency(TopLevelElement topLevelElement) {
        super.addDependency(topLevelElement);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement
    public /* bridge */ /* synthetic */ InstructionList compile(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        return super.compile(classGenerator, methodGenerator);
    }

    public boolean hasParams() {
        return this._parameters.size() > 0;
    }

    public boolean isSimplified() {
        return this._simplified;
    }

    public void setSimplified() {
        this._simplified = true;
    }

    public boolean isSimpleNamedTemplate() {
        return this._isSimpleNamedTemplate;
    }

    public void addParameter(Param param) {
        this._parameters.addElement(param);
    }

    public Vector<Param> getParameters() {
        return this._parameters;
    }

    public void disable() {
        this._disabled = true;
    }

    public boolean disabled() {
        return this._disabled;
    }

    public double getPriority() {
        return this._priority;
    }

    public int getPosition() {
        return this._position;
    }

    public boolean isNamed() {
        return this._name != null;
    }

    public Pattern getPattern() {
        return this._pattern;
    }

    public QName getName() {
        return this._name;
    }

    public void setName(QName qname) {
        if (this._name == null) {
            this._name = qname;
        }
    }

    public QName getModeName() {
        return this._mode;
    }

    public int compareTo(Object template) {
        Template other = (Template) template;
        if (this._priority > other._priority) {
            return 1;
        }
        if (this._priority < other._priority) {
            return -1;
        }
        if (this._position > other._position) {
            return 1;
        }
        if (this._position < other._position) {
            return -1;
        }
        return 0;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        Util.println('\n');
        indent(indent);
        if (this._name != null) {
            indent(indent);
            Util.println("name = " + ((Object) this._name));
        } else if (this._pattern != null) {
            indent(indent);
            Util.println("match = " + this._pattern.toString());
        }
        if (this._mode != null) {
            indent(indent);
            Util.println("mode = " + ((Object) this._mode));
        }
        displayContents(indent + 4);
    }

    private boolean resolveNamedTemplates(Template other, Parser parser) {
        if (other == null) {
            return true;
        }
        SymbolTable stable = parser.getSymbolTable();
        int us = getImportPrecedence();
        int them = other.getImportPrecedence();
        if (us > them) {
            other.disable();
            return true;
        }
        if (us < them) {
            stable.addTemplate(other);
            disable();
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Stylesheet getStylesheet() {
        return this._stylesheet;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String name = getAttribute("name");
        String mode = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MODE);
        String match = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MATCH);
        String priority = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_PRIORITY);
        this._stylesheet = super.getStylesheet();
        if (name.length() > 0) {
            if (!XML11Char.isXML11ValidQName(name)) {
                ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) name, (SyntaxTreeNode) this);
                parser.reportError(3, err);
            }
            this._name = parser.getQNameIgnoreDefaultNs(name);
        }
        if (mode.length() > 0) {
            if (!XML11Char.isXML11ValidQName(mode)) {
                ErrorMsg err2 = new ErrorMsg("INVALID_QNAME_ERR", (Object) mode, (SyntaxTreeNode) this);
                parser.reportError(3, err2);
            }
            this._mode = parser.getQNameIgnoreDefaultNs(mode);
        }
        if (match.length() > 0) {
            this._pattern = parser.parsePattern(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MATCH, null);
        }
        if (priority.length() > 0) {
            this._priority = Double.parseDouble(priority);
        } else if (this._pattern != null) {
            this._priority = this._pattern.getPriority();
        } else {
            this._priority = Double.NaN;
        }
        this._position = parser.getTemplateIndex();
        if (this._name != null) {
            Template other = parser.getSymbolTable().addTemplate(this);
            if (!resolveNamedTemplates(other, parser)) {
                ErrorMsg err3 = new ErrorMsg(ErrorMsg.TEMPLATE_REDEF_ERR, (Object) this._name, (SyntaxTreeNode) this);
                parser.reportError(3, err3);
            }
            if (this._pattern == null && this._mode == null) {
                this._isSimpleNamedTemplate = true;
            }
        }
        if (this._parent instanceof Stylesheet) {
            ((Stylesheet) this._parent).addTemplate(this);
        }
        parser.setTemplate(this);
        parseChildren(parser);
        parser.setTemplate(null);
    }

    public void parseSimplified(Stylesheet stylesheet, Parser parser) {
        this._stylesheet = stylesheet;
        setParent(stylesheet);
        this._name = null;
        this._mode = null;
        this._priority = Double.NaN;
        this._pattern = parser.parsePattern(this, "/");
        List<SyntaxTreeNode> contents = this._stylesheet.getContents();
        SyntaxTreeNode root = contents.get(0);
        if (root instanceof LiteralElement) {
            addElement(root);
            root.setParent(this);
            contents.set(0, this);
            parser.setTemplate(this);
            root.parseContents(parser);
            parser.setTemplate(null);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._pattern != null) {
            this._pattern.typeCheck(stable);
        }
        return typeCheckContents(stable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._disabled) {
            return;
        }
        String className = classGen.getClassName();
        if (this._compiled && isNamed()) {
            String methodName = Util.escape(this._name.toString());
            il.append(classGen.loadTranslet());
            il.append(methodGen.loadDOM());
            il.append(methodGen.loadIterator());
            il.append(methodGen.loadHandler());
            il.append(methodGen.loadCurrentNode());
            il.append(new INVOKEVIRTUAL(cpg.addMethodref(className, methodName, Constants.ATTR_SET_SIG)));
            return;
        }
        if (this._compiled) {
            return;
        }
        this._compiled = true;
        if (this._isSimpleNamedTemplate && (methodGen instanceof NamedMethodGenerator)) {
            int numParams = this._parameters.size();
            NamedMethodGenerator namedMethodGen = (NamedMethodGenerator) methodGen;
            for (int i2 = 0; i2 < numParams; i2++) {
                Param param = this._parameters.elementAt(i2);
                param.setLoadInstruction(namedMethodGen.loadParameter(i2));
                param.setStoreInstruction(namedMethodGen.storeParameter(i2));
            }
        }
        translateContents(classGen, methodGen);
        il.setPositions(true);
    }
}
