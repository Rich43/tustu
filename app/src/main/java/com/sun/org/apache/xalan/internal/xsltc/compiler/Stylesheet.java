package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import com.sun.org.apache.bcel.internal.generic.BasicType;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.FieldGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.bcel.internal.generic.PUTSTATIC;
import com.sun.org.apache.bcel.internal.generic.TargetLostException;
import com.sun.org.apache.bcel.internal.util.InstructionFinder;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Stylesheet.class */
public final class Stylesheet extends SyntaxTreeNode {
    private String _version;
    private QName _name;
    private String _systemId;
    private Stylesheet _parentStylesheet;
    private String _className;
    private Mode _defaultMode;
    public static final int UNKNOWN_OUTPUT = 0;
    public static final int XML_OUTPUT = 1;
    public static final int HTML_OUTPUT = 2;
    public static final int TEXT_OUTPUT = 3;
    private Vector _globals = new Vector();
    private Boolean _hasLocalParams = null;
    private final Vector _templates = new Vector();
    private Vector _allValidTemplates = null;
    private int _nextModeSerial = 1;
    private final Map<String, Mode> _modes = new HashMap();
    private final Map<String, String> _extensions = new HashMap();
    public Stylesheet _importedFrom = null;
    public Stylesheet _includedFrom = null;
    private Vector _includedStylesheets = null;
    private int _importPrecedence = 1;
    private int _minimumDescendantPrecedence = -1;
    private Map<String, Key> _keys = new HashMap();
    private SourceLoader _loader = null;
    private boolean _numberFormattingUsed = false;
    private boolean _simplified = false;
    private boolean _multiDocument = false;
    private boolean _callsNodeset = false;
    private boolean _hasIdCall = false;
    private boolean _templateInlining = false;
    private Output _lastOutputElement = null;
    private Properties _outputProperties = null;
    private int _outputMethod = 0;

    public int getOutputMethod() {
        return this._outputMethod;
    }

    private void checkOutputMethod() {
        String method;
        if (this._lastOutputElement != null && (method = this._lastOutputElement.getOutputMethod()) != null) {
            if (method.equals("xml")) {
                this._outputMethod = 1;
            } else if (method.equals("html")) {
                this._outputMethod = 2;
            } else if (method.equals("text")) {
                this._outputMethod = 3;
            }
        }
    }

    public boolean getTemplateInlining() {
        return this._templateInlining;
    }

    public void setTemplateInlining(boolean flag) {
        this._templateInlining = flag;
    }

    public boolean isSimplified() {
        return this._simplified;
    }

    public void setSimplified() {
        this._simplified = true;
    }

    public void setHasIdCall(boolean flag) {
        this._hasIdCall = flag;
    }

    public void setOutputProperty(String key, String value) {
        if (this._outputProperties == null) {
            this._outputProperties = new Properties();
        }
        this._outputProperties.setProperty(key, value);
    }

    public void setOutputProperties(Properties props) {
        this._outputProperties = props;
    }

    public Properties getOutputProperties() {
        return this._outputProperties;
    }

    public Output getLastOutputElement() {
        return this._lastOutputElement;
    }

    public void setMultiDocument(boolean flag) {
        this._multiDocument = flag;
    }

    public boolean isMultiDocument() {
        return this._multiDocument;
    }

    public void setCallsNodeset(boolean flag) {
        if (flag) {
            setMultiDocument(flag);
        }
        this._callsNodeset = flag;
    }

    public boolean callsNodeset() {
        return this._callsNodeset;
    }

    public void numberFormattingUsed() {
        this._numberFormattingUsed = true;
        Stylesheet parent = getParentStylesheet();
        if (null != parent) {
            parent.numberFormattingUsed();
        }
    }

    public void setImportPrecedence(int precedence) {
        Stylesheet included;
        this._importPrecedence = precedence;
        Iterator<SyntaxTreeNode> elements = elements();
        while (elements.hasNext()) {
            SyntaxTreeNode child = elements.next();
            if ((child instanceof Include) && (included = ((Include) child).getIncludedStylesheet()) != null && included._includedFrom == this) {
                included.setImportPrecedence(precedence);
            }
        }
        if (this._importedFrom != null) {
            if (this._importedFrom.getImportPrecedence() < precedence) {
                Parser parser = getParser();
                int nextPrecedence = parser.getNextImportPrecedence();
                this._importedFrom.setImportPrecedence(nextPrecedence);
                return;
            }
            return;
        }
        if (this._includedFrom != null && this._includedFrom.getImportPrecedence() != precedence) {
            this._includedFrom.setImportPrecedence(precedence);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public int getImportPrecedence() {
        return this._importPrecedence;
    }

    public int getMinimumDescendantPrecedence() {
        if (this._minimumDescendantPrecedence == -1) {
            int min = getImportPrecedence();
            int inclImpCount = this._includedStylesheets != null ? this._includedStylesheets.size() : 0;
            for (int i2 = 0; i2 < inclImpCount; i2++) {
                int prec = ((Stylesheet) this._includedStylesheets.elementAt(i2)).getMinimumDescendantPrecedence();
                if (prec < min) {
                    min = prec;
                }
            }
            this._minimumDescendantPrecedence = min;
        }
        return this._minimumDescendantPrecedence;
    }

    public boolean checkForLoop(String systemId) {
        if (this._systemId != null && this._systemId.equals(systemId)) {
            return true;
        }
        if (this._parentStylesheet != null) {
            return this._parentStylesheet.checkForLoop(systemId);
        }
        return false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._name = makeStylesheetName("__stylesheet_");
    }

    public void setParentStylesheet(Stylesheet parent) {
        this._parentStylesheet = parent;
    }

    public Stylesheet getParentStylesheet() {
        return this._parentStylesheet;
    }

    public void setImportingStylesheet(Stylesheet parent) {
        this._importedFrom = parent;
        parent.addIncludedStylesheet(this);
    }

    public void setIncludingStylesheet(Stylesheet parent) {
        this._includedFrom = parent;
        parent.addIncludedStylesheet(this);
    }

    public void addIncludedStylesheet(Stylesheet child) {
        if (this._includedStylesheets == null) {
            this._includedStylesheets = new Vector();
        }
        this._includedStylesheets.addElement(child);
    }

    public void setSystemId(String systemId) {
        if (systemId != null) {
            this._systemId = SystemIDResolver.getAbsoluteURI(systemId);
        }
    }

    public String getSystemId() {
        return this._systemId;
    }

    public void setSourceLoader(SourceLoader loader) {
        this._loader = loader;
    }

    public SourceLoader getSourceLoader() {
        return this._loader;
    }

    private QName makeStylesheetName(String prefix) {
        return getParser().getQName(prefix + getXSLTC().nextStylesheetSerial());
    }

    public boolean hasGlobals() {
        return this._globals.size() > 0;
    }

    public boolean hasLocalParams() {
        if (this._hasLocalParams == null) {
            Vector templates = getAllValidTemplates();
            int n2 = templates.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Template template = (Template) templates.elementAt(i2);
                if (template.hasParams()) {
                    this._hasLocalParams = Boolean.TRUE;
                    return true;
                }
            }
            this._hasLocalParams = Boolean.FALSE;
            return false;
        }
        return this._hasLocalParams.booleanValue();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    protected void addPrefixMapping(String prefix, String uri) {
        if (prefix.equals("") && uri.equals("http://www.w3.org/1999/xhtml")) {
            return;
        }
        super.addPrefixMapping(prefix, uri);
    }

    private void extensionURI(String prefixes, SymbolTable stable) {
        if (prefixes != null) {
            StringTokenizer tokens = new StringTokenizer(prefixes);
            while (tokens.hasMoreTokens()) {
                String prefix = tokens.nextToken();
                String uri = lookupNamespace(prefix);
                if (uri != null) {
                    this._extensions.put(uri, prefix);
                }
            }
        }
    }

    public boolean isExtension(String uri) {
        return this._extensions.get(uri) != null;
    }

    public void declareExtensionPrefixes(Parser parser) {
        SymbolTable stable = parser.getSymbolTable();
        String extensionPrefixes = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXTENSIONELEMENTPREFIXES);
        extensionURI(extensionPrefixes, stable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        SymbolTable stable = parser.getSymbolTable();
        addPrefixMapping("xml", "http://www.w3.org/XML/1998/namespace");
        Stylesheet sheet = stable.addStylesheet(this._name, this);
        if (sheet != null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.MULTIPLE_STYLESHEET_ERR, (SyntaxTreeNode) this);
            parser.reportError(3, err);
        }
        if (this._simplified) {
            stable.excludeURI("http://www.w3.org/1999/XSL/Transform");
            Template template = new Template();
            template.parseSimplified(this, parser);
            return;
        }
        parseOwnChildren(parser);
    }

    public final void parseOwnChildren(Parser parser) {
        SymbolTable stable = parser.getSymbolTable();
        String excludePrefixes = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXCLUDE_RESULT_PREFIXES);
        String extensionPrefixes = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXTENSIONELEMENTPREFIXES);
        stable.pushExcludedNamespacesContext();
        stable.excludeURI("http://www.w3.org/1999/XSL/Transform");
        stable.excludeNamespaces(excludePrefixes);
        stable.excludeNamespaces(extensionPrefixes);
        List<SyntaxTreeNode> contents = getContents();
        int count = contents.size();
        for (int i2 = 0; i2 < count; i2++) {
            SyntaxTreeNode child = contents.get(i2);
            if ((child instanceof VariableBase) || (child instanceof NamespaceAlias)) {
                parser.getSymbolTable().setCurrentNode(child);
                child.parseContents(parser);
            }
        }
        for (int i3 = 0; i3 < count; i3++) {
            SyntaxTreeNode child2 = contents.get(i3);
            if (!(child2 instanceof VariableBase) && !(child2 instanceof NamespaceAlias)) {
                parser.getSymbolTable().setCurrentNode(child2);
                child2.parseContents(parser);
            }
            if (!this._templateInlining && (child2 instanceof Template)) {
                Template template = (Template) child2;
                String name = "template$dot$" + template.getPosition();
                template.setName(parser.getQName(name));
            }
        }
        stable.popExcludedNamespacesContext();
    }

    public void processModes() {
        if (this._defaultMode == null) {
            this._defaultMode = new Mode(null, this, "");
        }
        this._defaultMode.processPatterns(this._keys);
        for (Mode mode : this._modes.values()) {
            mode.processPatterns(this._keys);
        }
    }

    private void compileModes(ClassGenerator classGen) {
        this._defaultMode.compileApplyTemplates(classGen);
        for (Mode mode : this._modes.values()) {
            mode.compileApplyTemplates(classGen);
        }
    }

    public Mode getMode(QName modeName) {
        if (modeName == null) {
            if (this._defaultMode == null) {
                this._defaultMode = new Mode(null, this, "");
            }
            return this._defaultMode;
        }
        Mode mode = this._modes.get(modeName.getStringRep());
        if (mode == null) {
            int i2 = this._nextModeSerial;
            this._nextModeSerial = i2 + 1;
            String suffix = Integer.toString(i2);
            Map<String, Mode> map = this._modes;
            String stringRep = modeName.getStringRep();
            Mode mode2 = new Mode(modeName, this, suffix);
            mode = mode2;
            map.put(stringRep, mode2);
        }
        return mode;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        int count = this._globals.size();
        for (int i2 = 0; i2 < count; i2++) {
            VariableBase var = (VariableBase) this._globals.elementAt(i2);
            var.typeCheck(stable);
        }
        return typeCheckContents(stable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        translate();
    }

    private void addDOMField(ClassGenerator classGen) {
        FieldGen fgen = new FieldGen(1, Util.getJCRefType(Constants.DOM_INTF_SIG), Constants.DOM_FIELD, classGen.getConstantPool());
        classGen.addField(fgen.getField());
    }

    private void addStaticField(ClassGenerator classGen, String type, String name) {
        FieldGen fgen = new FieldGen(12, Util.getJCRefType(type), name, classGen.getConstantPool());
        classGen.addField(fgen.getField());
    }

    public void translate() {
        this._className = getXSLTC().getClassName();
        ClassGenerator classGen = new ClassGenerator(this._className, Constants.TRANSLET_CLASS, "", 33, null, this);
        addDOMField(classGen);
        compileTransform(classGen);
        Iterator<SyntaxTreeNode> elements = elements();
        while (elements.hasNext()) {
            SyntaxTreeNode element = elements.next();
            if (element instanceof Template) {
                Template template = (Template) element;
                getMode(template.getModeName()).addTemplate(template);
            } else if (element instanceof AttributeSet) {
                ((AttributeSet) element).translate(classGen, null);
            } else if (element instanceof Output) {
                Output output = (Output) element;
                if (output.enabled()) {
                    this._lastOutputElement = output;
                }
            }
        }
        checkOutputMethod();
        processModes();
        compileModes(classGen);
        compileStaticInitializer(classGen);
        compileConstructor(classGen, this._lastOutputElement);
        if (!getParser().errorsFound()) {
            getXSLTC().dumpClass(classGen.getJavaClass());
        }
    }

    private void compileStaticInitializer(ClassGenerator classGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = new InstructionList();
        MethodGenerator staticConst = new MethodGenerator(9, com.sun.org.apache.bcel.internal.generic.Type.VOID, null, null, com.sun.org.apache.bcel.internal.Constants.STATIC_INITIALIZER_NAME, this._className, il, cpg);
        addStaticField(classGen, "[Ljava/lang/String;", Constants.STATIC_NAMES_ARRAY_FIELD);
        addStaticField(classGen, "[Ljava/lang/String;", Constants.STATIC_URIS_ARRAY_FIELD);
        addStaticField(classGen, Constants.TYPES_INDEX_SIG, Constants.STATIC_TYPES_ARRAY_FIELD);
        addStaticField(classGen, "[Ljava/lang/String;", Constants.STATIC_NAMESPACE_ARRAY_FIELD);
        int charDataFieldCount = getXSLTC().getCharacterDataCount();
        for (int i2 = 0; i2 < charDataFieldCount; i2++) {
            addStaticField(classGen, Constants.STATIC_CHAR_DATA_FIELD_SIG, Constants.STATIC_CHAR_DATA_FIELD + i2);
        }
        Vector namesIndex = getXSLTC().getNamesIndex();
        int size = namesIndex.size();
        String[] namesArray = new String[size];
        String[] urisArray = new String[size];
        int[] typesArray = new int[size];
        for (int i3 = 0; i3 < size; i3++) {
            String encodedName = (String) namesIndex.elementAt(i3);
            int index = encodedName.lastIndexOf(58);
            if (index > -1) {
                urisArray[i3] = encodedName.substring(0, index);
            }
            int index2 = index + 1;
            if (encodedName.charAt(index2) == '@') {
                typesArray[i3] = 2;
                index2++;
            } else if (encodedName.charAt(index2) == '?') {
                typesArray[i3] = 13;
                index2++;
            } else {
                typesArray[i3] = 1;
            }
            if (index2 == 0) {
                namesArray[i3] = encodedName;
            } else {
                namesArray[i3] = encodedName.substring(index2);
            }
        }
        staticConst.markChunkStart();
        il.append(new PUSH(cpg, size));
        il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
        int namesArrayRef = cpg.addFieldref(this._className, Constants.STATIC_NAMES_ARRAY_FIELD, "[Ljava/lang/String;");
        il.append(new PUTSTATIC(namesArrayRef));
        staticConst.markChunkEnd();
        for (int i4 = 0; i4 < size; i4++) {
            String name = namesArray[i4];
            staticConst.markChunkStart();
            il.append(new GETSTATIC(namesArrayRef));
            il.append(new PUSH(cpg, i4));
            il.append(new PUSH(cpg, name));
            il.append(AASTORE);
            staticConst.markChunkEnd();
        }
        staticConst.markChunkStart();
        il.append(new PUSH(cpg, size));
        il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
        int urisArrayRef = cpg.addFieldref(this._className, Constants.STATIC_URIS_ARRAY_FIELD, "[Ljava/lang/String;");
        il.append(new PUTSTATIC(urisArrayRef));
        staticConst.markChunkEnd();
        for (int i5 = 0; i5 < size; i5++) {
            String uri = urisArray[i5];
            staticConst.markChunkStart();
            il.append(new GETSTATIC(urisArrayRef));
            il.append(new PUSH(cpg, i5));
            il.append(new PUSH(cpg, uri));
            il.append(AASTORE);
            staticConst.markChunkEnd();
        }
        staticConst.markChunkStart();
        il.append(new PUSH(cpg, size));
        il.append(new NEWARRAY(BasicType.INT));
        int typesArrayRef = cpg.addFieldref(this._className, Constants.STATIC_TYPES_ARRAY_FIELD, Constants.TYPES_INDEX_SIG);
        il.append(new PUTSTATIC(typesArrayRef));
        staticConst.markChunkEnd();
        for (int i6 = 0; i6 < size; i6++) {
            int nodeType = typesArray[i6];
            staticConst.markChunkStart();
            il.append(new GETSTATIC(typesArrayRef));
            il.append(new PUSH(cpg, i6));
            il.append(new PUSH(cpg, nodeType));
            il.append(IASTORE);
        }
        Vector namespaces = getXSLTC().getNamespaceIndex();
        staticConst.markChunkStart();
        il.append(new PUSH(cpg, namespaces.size()));
        il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
        int namespaceArrayRef = cpg.addFieldref(this._className, Constants.STATIC_NAMESPACE_ARRAY_FIELD, "[Ljava/lang/String;");
        il.append(new PUTSTATIC(namespaceArrayRef));
        staticConst.markChunkEnd();
        for (int i7 = 0; i7 < namespaces.size(); i7++) {
            String ns = (String) namespaces.elementAt(i7);
            staticConst.markChunkStart();
            il.append(new GETSTATIC(namespaceArrayRef));
            il.append(new PUSH(cpg, i7));
            il.append(new PUSH(cpg, ns));
            il.append(AASTORE);
            staticConst.markChunkEnd();
        }
        int charDataCount = getXSLTC().getCharacterDataCount();
        int toCharArray = cpg.addMethodref("java.lang.String", "toCharArray", "()[C");
        for (int i8 = 0; i8 < charDataCount; i8++) {
            staticConst.markChunkStart();
            il.append(new PUSH(cpg, getXSLTC().getCharacterData(i8)));
            il.append(new INVOKEVIRTUAL(toCharArray));
            il.append(new PUTSTATIC(cpg.addFieldref(this._className, Constants.STATIC_CHAR_DATA_FIELD + i8, Constants.STATIC_CHAR_DATA_FIELD_SIG)));
            staticConst.markChunkEnd();
        }
        il.append(RETURN);
        classGen.addMethod(staticConst);
    }

    private void compileConstructor(ClassGenerator classGen, Output output) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = new InstructionList();
        MethodGenerator constructor = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, null, null, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, this._className, il, cpg);
        il.append(classGen.loadTranslet());
        il.append(new INVOKESPECIAL(cpg.addMethodref(Constants.TRANSLET_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V")));
        constructor.markChunkStart();
        il.append(classGen.loadTranslet());
        il.append(new GETSTATIC(cpg.addFieldref(this._className, Constants.STATIC_NAMES_ARRAY_FIELD, "[Ljava/lang/String;")));
        il.append(new PUTFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.NAMES_INDEX, "[Ljava/lang/String;")));
        il.append(classGen.loadTranslet());
        il.append(new GETSTATIC(cpg.addFieldref(this._className, Constants.STATIC_URIS_ARRAY_FIELD, "[Ljava/lang/String;")));
        il.append(new PUTFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.URIS_INDEX, "[Ljava/lang/String;")));
        constructor.markChunkEnd();
        constructor.markChunkStart();
        il.append(classGen.loadTranslet());
        il.append(new GETSTATIC(cpg.addFieldref(this._className, Constants.STATIC_TYPES_ARRAY_FIELD, Constants.TYPES_INDEX_SIG)));
        il.append(new PUTFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.TYPES_INDEX, Constants.TYPES_INDEX_SIG)));
        constructor.markChunkEnd();
        constructor.markChunkStart();
        il.append(classGen.loadTranslet());
        il.append(new GETSTATIC(cpg.addFieldref(this._className, Constants.STATIC_NAMESPACE_ARRAY_FIELD, "[Ljava/lang/String;")));
        il.append(new PUTFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.NAMESPACE_INDEX, "[Ljava/lang/String;")));
        constructor.markChunkEnd();
        constructor.markChunkStart();
        il.append(classGen.loadTranslet());
        il.append(new PUSH(cpg, 101));
        il.append(new PUTFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.TRANSLET_VERSION_INDEX, "I")));
        constructor.markChunkEnd();
        if (this._hasIdCall) {
            constructor.markChunkStart();
            il.append(classGen.loadTranslet());
            il.append(new PUSH(cpg, Boolean.TRUE));
            il.append(new PUTFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.HASIDCALL_INDEX, Constants.HASIDCALL_INDEX_SIG)));
            constructor.markChunkEnd();
        }
        if (output != null) {
            constructor.markChunkStart();
            output.translate(classGen, constructor);
            constructor.markChunkEnd();
        }
        if (this._numberFormattingUsed) {
            constructor.markChunkStart();
            DecimalFormatting.translateDefaultDFS(classGen, constructor);
            constructor.markChunkEnd();
        }
        il.append(RETURN);
        classGen.addMethod(constructor);
    }

    private String compileTopLevel(ClassGenerator classGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        com.sun.org.apache.bcel.internal.generic.Type[] argTypes = {Util.getJCRefType(Constants.DOM_INTF_SIG), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;")};
        String[] argNames = {Constants.DOCUMENT_PNAME, Constants.ITERATOR_PNAME, Constants.TRANSLET_OUTPUT_PNAME};
        InstructionList il = new InstructionList();
        MethodGenerator toplevel = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, "topLevel", this._className, il, classGen.getConstantPool());
        toplevel.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
        LocalVariableGen current = toplevel.addLocalVariable(Keywords.FUNC_CURRENT_STRING, com.sun.org.apache.bcel.internal.generic.Type.INT, null, null);
        int setFilter = cpg.addInterfaceMethodref(Constants.DOM_INTF, "setFilter", "(Lcom/sun/org/apache/xalan/internal/xsltc/StripFilter;)V");
        int gitr = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(toplevel.loadDOM());
        il.append(new INVOKEINTERFACE(gitr, 1));
        il.append(toplevel.nextNode());
        current.setStart(il.append(new ISTORE(current.getIndex())));
        Vector varDepElements = new Vector(this._globals);
        Iterator<SyntaxTreeNode> elements = elements();
        while (elements.hasNext()) {
            SyntaxTreeNode element = elements.next();
            if (element instanceof Key) {
                varDepElements.add(element);
            }
        }
        Vector varDepElements2 = resolveDependencies(varDepElements);
        int count = varDepElements2.size();
        for (int i2 = 0; i2 < count; i2++) {
            TopLevelElement tle = (TopLevelElement) varDepElements2.elementAt(i2);
            tle.translate(classGen, toplevel);
            if (tle instanceof Key) {
                Key key = (Key) tle;
                this._keys.put(key.getName(), key);
            }
        }
        Vector whitespaceRules = new Vector();
        Iterator<SyntaxTreeNode> elements2 = elements();
        while (elements2.hasNext()) {
            SyntaxTreeNode element2 = elements2.next();
            if (element2 instanceof DecimalFormatting) {
                ((DecimalFormatting) element2).translate(classGen, toplevel);
            } else if (element2 instanceof Whitespace) {
                whitespaceRules.addAll(((Whitespace) element2).getRules());
            }
        }
        if (whitespaceRules.size() > 0) {
            Whitespace.translateRules(whitespaceRules, classGen);
        }
        if (classGen.containsMethod(Constants.STRIP_SPACE, Constants.STRIP_SPACE_PARAMS) != null) {
            il.append(toplevel.loadDOM());
            il.append(classGen.loadTranslet());
            il.append(new INVOKEINTERFACE(setFilter, 2));
        }
        il.append(RETURN);
        classGen.addMethod(toplevel);
        return "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V";
    }

    private Vector resolveDependencies(Vector input) {
        Vector result = new Vector();
        while (input.size() > 0) {
            boolean changed = false;
            int i2 = 0;
            while (i2 < input.size()) {
                TopLevelElement vde = (TopLevelElement) input.elementAt(i2);
                Vector dep = vde.getDependencies();
                if (dep == null || result.containsAll(dep)) {
                    result.addElement(vde);
                    input.remove(i2);
                    changed = true;
                } else {
                    i2++;
                }
            }
            if (!changed) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.CIRCULAR_VARIABLE_ERR, (Object) input.toString(), (SyntaxTreeNode) this);
                getParser().reportError(3, err);
                return result;
            }
        }
        return result;
    }

    private String compileBuildKeys(ClassGenerator classGen) {
        classGen.getConstantPool();
        com.sun.org.apache.bcel.internal.generic.Type[] argTypes = {Util.getJCRefType(Constants.DOM_INTF_SIG), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;"), com.sun.org.apache.bcel.internal.generic.Type.INT};
        String[] argNames = {Constants.DOCUMENT_PNAME, Constants.ITERATOR_PNAME, Constants.TRANSLET_OUTPUT_PNAME, Keywords.FUNC_CURRENT_STRING};
        InstructionList il = new InstructionList();
        MethodGenerator buildKeys = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, "buildKeys", this._className, il, classGen.getConstantPool());
        buildKeys.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
        Iterator<SyntaxTreeNode> elements = elements();
        while (elements.hasNext()) {
            SyntaxTreeNode element = elements.next();
            if (element instanceof Key) {
                Key key = (Key) element;
                key.translate(classGen, buildKeys);
                this._keys.put(key.getName(), key);
            }
        }
        il.append(RETURN);
        buildKeys.stripAttributes(true);
        buildKeys.setMaxLocals();
        buildKeys.setMaxStack();
        buildKeys.removeNOPs();
        classGen.addMethod(buildKeys.getMethod());
        return Constants.ATTR_SET_SIG;
    }

    private void compileTransform(ClassGenerator classGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        com.sun.org.apache.bcel.internal.generic.Type[] argTypes = {Util.getJCRefType(Constants.DOM_INTF_SIG), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;")};
        String[] argNames = {Constants.DOCUMENT_PNAME, Constants.ITERATOR_PNAME, Constants.TRANSLET_OUTPUT_PNAME};
        InstructionList il = new InstructionList();
        MethodGenerator transf = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_TRANSFORM_STRING, this._className, il, classGen.getConstantPool());
        transf.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
        int check = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "resetPrefixIndex", "()V");
        il.append(new INVOKESTATIC(check));
        LocalVariableGen current = transf.addLocalVariable(Keywords.FUNC_CURRENT_STRING, com.sun.org.apache.bcel.internal.generic.Type.INT, null, null);
        String applyTemplatesSig = classGen.getApplyTemplatesSig();
        int applyTemplates = cpg.addMethodref(getClassName(), Constants.APPLY_TEMPLATES, applyTemplatesSig);
        int domField = cpg.addFieldref(getClassName(), Constants.DOM_FIELD, Constants.DOM_INTF_SIG);
        il.append(classGen.loadTranslet());
        if (isMultiDocument()) {
            il.append(new NEW(cpg.addClass(Constants.MULTI_DOM_CLASS)));
            il.append(DUP);
        }
        il.append(classGen.loadTranslet());
        il.append(transf.loadDOM());
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.TRANSLET_CLASS, "makeDOMAdapter", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter;")));
        if (isMultiDocument()) {
            int init = cpg.addMethodref(Constants.MULTI_DOM_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
            il.append(new INVOKESPECIAL(init));
        }
        il.append(new PUTFIELD(domField));
        int gitr = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(transf.loadDOM());
        il.append(new INVOKEINTERFACE(gitr, 1));
        il.append(transf.nextNode());
        current.setStart(il.append(new ISTORE(current.getIndex())));
        il.append(classGen.loadTranslet());
        il.append(transf.loadHandler());
        int index = cpg.addMethodref(Constants.TRANSLET_CLASS, "transferOutputSettings", "(Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
        il.append(new INVOKEVIRTUAL(index));
        String keySig = compileBuildKeys(classGen);
        cpg.addMethodref(getClassName(), "buildKeys", keySig);
        Iterator<SyntaxTreeNode> toplevel = elements();
        if (this._globals.size() > 0 || toplevel.hasNext()) {
            String topLevelSig = compileTopLevel(classGen);
            int topLevelIdx = cpg.addMethodref(getClassName(), "topLevel", topLevelSig);
            il.append(classGen.loadTranslet());
            il.append(classGen.loadTranslet());
            il.append(new GETFIELD(domField));
            il.append(transf.loadIterator());
            il.append(transf.loadHandler());
            il.append(new INVOKEVIRTUAL(topLevelIdx));
        }
        il.append(transf.loadHandler());
        il.append(transf.startDocument());
        il.append(classGen.loadTranslet());
        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(domField));
        il.append(transf.loadIterator());
        il.append(transf.loadHandler());
        il.append(new INVOKEVIRTUAL(applyTemplates));
        il.append(transf.loadHandler());
        il.append(transf.endDocument());
        il.append(RETURN);
        classGen.addMethod(transf);
    }

    private void peepHoleOptimization(MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        InstructionFinder find = new InstructionFinder(il);
        Iterator iter = find.search("`aload'`pop'`instruction'");
        while (iter.hasNext()) {
            InstructionHandle[] match = (InstructionHandle[]) iter.next();
            try {
                il.delete(match[0], match[1]);
            } catch (TargetLostException e2) {
            }
        }
    }

    public int addParam(Param param) {
        this._globals.addElement(param);
        return this._globals.size() - 1;
    }

    public int addVariable(Variable global) {
        this._globals.addElement(global);
        return this._globals.size() - 1;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("Stylesheet");
        displayContents(indent + 4);
    }

    public String getNamespace(String prefix) {
        return lookupNamespace(prefix);
    }

    public String getClassName() {
        return this._className;
    }

    public Vector getTemplates() {
        return this._templates;
    }

    public Vector getAllValidTemplates() {
        if (this._includedStylesheets == null) {
            return this._templates;
        }
        if (this._allValidTemplates == null) {
            Vector templates = new Vector();
            templates.addAll(this._templates);
            int size = this._includedStylesheets.size();
            for (int i2 = 0; i2 < size; i2++) {
                Stylesheet included = (Stylesheet) this._includedStylesheets.elementAt(i2);
                templates.addAll(included.getAllValidTemplates());
            }
            if (this._parentStylesheet != null) {
                return templates;
            }
            this._allValidTemplates = templates;
        }
        return this._allValidTemplates;
    }

    protected void addTemplate(Template template) {
        this._templates.addElement(template);
    }
}
