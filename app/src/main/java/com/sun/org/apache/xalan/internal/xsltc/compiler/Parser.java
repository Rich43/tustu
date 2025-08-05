package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.java_cup.internal.runtime.Symbol;
import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.xml.internal.ws.encoding.MtomCodec;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import javax.xml.XMLConstants;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.xml.internal.JdkXmlUtils;
import jdk.xml.internal.XMLSecurityManager;
import org.slf4j.Marker;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Parser.class */
public class Parser implements Constants, ContentHandler {
    private static final String XSL = "xsl";
    private static final String TRANSLET = "translet";
    private XSLTC _xsltc;
    private XPathParser _xpathParser;
    private ArrayList<ErrorMsg> _errors;
    private ArrayList<ErrorMsg> _warnings;
    private Map<String, String> _instructionClasses;
    private Map<String, String[]> _instructionAttrs;
    private Map<String, QName> _qNames;
    private Map<String, Map<String, QName>> _namespaces;
    private QName _useAttributeSets;
    private QName _excludeResultPrefixes;
    private QName _extensionElementPrefixes;
    private Map<String, Object> _variableScope;
    private Stylesheet _currentStylesheet;
    private SymbolTable _symbolTable;
    private Output _output;
    private Template _template;
    private boolean _rootNamespaceDef;
    private SyntaxTreeNode _root;
    private String _target;
    private int _currentImportPrecedence;
    private boolean _overrideDefaultParser;
    private Locator _locator = null;
    private String _PImedia = null;
    private String _PItitle = null;
    private String _PIcharset = null;
    private int _templateIndex = 0;
    private boolean versionIsOne = true;
    private Stack<SyntaxTreeNode> _parentStack = null;
    private Map<String, String> _prefixMapping = null;

    public Parser(XSLTC xsltc, boolean useOverrideDefaultParser) {
        this._xsltc = xsltc;
        this._overrideDefaultParser = useOverrideDefaultParser;
    }

    public void init() {
        this._qNames = new HashMap(512);
        this._namespaces = new HashMap();
        this._instructionClasses = new HashMap();
        this._instructionAttrs = new HashMap();
        this._variableScope = new HashMap();
        this._template = null;
        this._errors = new ArrayList<>();
        this._warnings = new ArrayList<>();
        this._symbolTable = new SymbolTable();
        this._xpathParser = new XPathParser(this);
        this._currentStylesheet = null;
        this._output = null;
        this._root = null;
        this._rootNamespaceDef = false;
        this._currentImportPrecedence = 1;
        initStdClasses();
        initInstructionAttrs();
        initExtClasses();
        initSymbolTable();
        this._useAttributeSets = getQName("http://www.w3.org/1999/XSL/Transform", XSL, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_USEATTRIBUTESETS);
        this._excludeResultPrefixes = getQName("http://www.w3.org/1999/XSL/Transform", XSL, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXCLUDE_RESULT_PREFIXES);
        this._extensionElementPrefixes = getQName("http://www.w3.org/1999/XSL/Transform", XSL, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXTENSIONELEMENTPREFIXES);
    }

    public void setOutput(Output output) {
        if (this._output != null) {
            if (this._output.getImportPrecedence() <= output.getImportPrecedence()) {
                output.mergeOutput(this._output);
                this._output.disable();
                this._output = output;
                return;
            }
            output.disable();
            return;
        }
        this._output = output;
    }

    public Output getOutput() {
        return this._output;
    }

    public Properties getOutputProperties() {
        return getTopLevelStylesheet().getOutputProperties();
    }

    public void addVariable(Variable var) {
        addVariableOrParam(var);
    }

    public void addParameter(Param param) {
        addVariableOrParam(param);
    }

    private void addVariableOrParam(VariableBase var) {
        Object existing = this._variableScope.get(var.getName().getStringRep());
        if (existing != null) {
            if (existing instanceof Stack) {
                ((Stack) existing).push(var);
                return;
            } else {
                if (existing instanceof VariableBase) {
                    Stack<VariableBase> stack = new Stack<>();
                    stack.push((VariableBase) existing);
                    stack.push(var);
                    this._variableScope.put(var.getName().getStringRep(), stack);
                    return;
                }
                return;
            }
        }
        this._variableScope.put(var.getName().getStringRep(), var);
    }

    public void removeVariable(QName name) {
        Object existing = this._variableScope.get(name.getStringRep());
        if (existing instanceof Stack) {
            Stack<VariableBase> stack = (Stack) existing;
            if (!stack.isEmpty()) {
                stack.pop();
            }
            if (!stack.isEmpty()) {
                return;
            }
        }
        this._variableScope.remove(name.getStringRep());
    }

    public VariableBase lookupVariable(QName name) {
        Object existing = this._variableScope.get(name.getStringRep());
        if (existing instanceof VariableBase) {
            return (VariableBase) existing;
        }
        if (existing instanceof Stack) {
            Stack<VariableBase> stack = (Stack) existing;
            return stack.peek();
        }
        return null;
    }

    public void setXSLTC(XSLTC xsltc) {
        this._xsltc = xsltc;
    }

    public XSLTC getXSLTC() {
        return this._xsltc;
    }

    public int getCurrentImportPrecedence() {
        return this._currentImportPrecedence;
    }

    public int getNextImportPrecedence() {
        int i2 = this._currentImportPrecedence + 1;
        this._currentImportPrecedence = i2;
        return i2;
    }

    public void setCurrentStylesheet(Stylesheet stylesheet) {
        this._currentStylesheet = stylesheet;
    }

    public Stylesheet getCurrentStylesheet() {
        return this._currentStylesheet;
    }

    public Stylesheet getTopLevelStylesheet() {
        return this._xsltc.getStylesheet();
    }

    public QName getQNameSafe(String stringRep) {
        int colon = stringRep.lastIndexOf(58);
        if (colon != -1) {
            String prefix = stringRep.substring(0, colon);
            String localname = stringRep.substring(colon + 1);
            String namespace = null;
            if (!prefix.equals("xmlns")) {
                namespace = this._symbolTable.lookupNamespace(prefix);
                if (namespace == null) {
                    namespace = "";
                }
            }
            return getQName(namespace, prefix, localname);
        }
        String uri = stringRep.equals("xmlns") ? null : this._symbolTable.lookupNamespace("");
        return getQName(uri, (String) null, stringRep);
    }

    public QName getQName(String stringRep) {
        return getQName(stringRep, true, false);
    }

    public QName getQNameIgnoreDefaultNs(String stringRep) {
        return getQName(stringRep, true, true);
    }

    public QName getQName(String stringRep, boolean reportError) {
        return getQName(stringRep, reportError, false);
    }

    private QName getQName(String stringRep, boolean reportError, boolean ignoreDefaultNs) {
        int colon = stringRep.lastIndexOf(58);
        if (colon != -1) {
            String prefix = stringRep.substring(0, colon);
            String localname = stringRep.substring(colon + 1);
            String namespace = null;
            if (!prefix.equals("xmlns")) {
                namespace = this._symbolTable.lookupNamespace(prefix);
                if (namespace == null && reportError) {
                    int line = getLineNumber();
                    ErrorMsg err = new ErrorMsg(ErrorMsg.NAMESPACE_UNDEF_ERR, line, prefix);
                    reportError(3, err);
                }
            }
            return getQName(namespace, prefix, localname);
        }
        if (stringRep.equals("xmlns")) {
            ignoreDefaultNs = true;
        }
        String defURI = ignoreDefaultNs ? null : this._symbolTable.lookupNamespace("");
        return getQName(defURI, (String) null, stringRep);
    }

    public QName getQName(String namespace, String prefix, String localname) {
        if (namespace == null || namespace.equals("")) {
            QName name = this._qNames.get(localname);
            if (name == null) {
                name = new QName(null, prefix, localname);
                this._qNames.put(localname, name);
            }
            return name;
        }
        Map<String, QName> space = this._namespaces.get(namespace);
        String lexicalQName = (prefix == null || prefix.length() == 0) ? localname : prefix + ':' + localname;
        if (space == null) {
            QName name2 = new QName(namespace, prefix, localname);
            Map<String, Map<String, QName>> map = this._namespaces;
            Map<String, QName> space2 = new HashMap<>();
            map.put(namespace, space2);
            space2.put(lexicalQName, name2);
            return name2;
        }
        QName name3 = space.get(lexicalQName);
        if (name3 == null) {
            name3 = new QName(namespace, prefix, localname);
            space.put(lexicalQName, name3);
        }
        return name3;
    }

    public QName getQName(String scope, String name) {
        return getQName(scope + name);
    }

    public QName getQName(QName scope, QName name) {
        return getQName(scope.toString() + name.toString());
    }

    public QName getUseAttributeSets() {
        return this._useAttributeSets;
    }

    public QName getExtensionElementPrefixes() {
        return this._extensionElementPrefixes;
    }

    public QName getExcludeResultPrefixes() {
        return this._excludeResultPrefixes;
    }

    public Stylesheet makeStylesheet(SyntaxTreeNode element) throws CompilerException {
        Stylesheet stylesheet;
        try {
            if (element instanceof Stylesheet) {
                stylesheet = (Stylesheet) element;
            } else {
                stylesheet = new Stylesheet();
                stylesheet.setSimplified();
                stylesheet.addElement(element);
                stylesheet.setAttributes((AttributesImpl) element.getAttributes());
                if (element.lookupNamespace("") == null) {
                    element.addPrefixMapping("", "");
                }
            }
            stylesheet.setParser(this);
            return stylesheet;
        } catch (ClassCastException e2) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.NOT_STYLESHEET_ERR, element);
            throw new CompilerException(err.toString());
        }
    }

    public void createAST(Stylesheet stylesheet) {
        if (stylesheet != null) {
            try {
                stylesheet.parseContents(this);
                Iterator<SyntaxTreeNode> elements = stylesheet.elements();
                while (elements.hasNext()) {
                    SyntaxTreeNode child = elements.next();
                    if (child instanceof Text) {
                        int l2 = getLineNumber();
                        ErrorMsg err = new ErrorMsg(ErrorMsg.ILLEGAL_TEXT_NODE_ERR, l2, (Object) null);
                        reportError(3, err);
                    }
                }
                if (!errorsFound()) {
                    stylesheet.typeCheck(this._symbolTable);
                }
            } catch (TypeCheckError e2) {
                reportError(3, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e2));
            }
        }
    }

    public SyntaxTreeNode parse(XMLReader reader, InputSource input) {
        try {
            reader.setContentHandler(this);
            reader.parse(input);
            return getStylesheet(this._root);
        } catch (CompilerException e2) {
            if (this._xsltc.debug()) {
                e2.printStackTrace();
            }
            reportError(3, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e2));
            return null;
        } catch (IOException e3) {
            if (this._xsltc.debug()) {
                e3.printStackTrace();
            }
            reportError(3, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e3));
            return null;
        } catch (SAXException e4) {
            Throwable ex = e4.getException();
            if (this._xsltc.debug()) {
                e4.printStackTrace();
                if (ex != null) {
                    ex.printStackTrace();
                }
            }
            reportError(3, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e4));
            return null;
        } catch (Exception e5) {
            if (this._xsltc.debug()) {
                e5.printStackTrace();
            }
            reportError(3, new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR, (Throwable) e5));
            return null;
        }
    }

    public SyntaxTreeNode parse(InputSource input) {
        XMLReader reader = JdkXmlUtils.getXMLReader(this._overrideDefaultParser, this._xsltc.isSecureProcessing());
        JdkXmlUtils.setXMLReaderPropertyIfSupport(reader, "http://javax.xml.XMLConstants/property/accessExternalDTD", this._xsltc.getProperty("http://javax.xml.XMLConstants/property/accessExternalDTD"), true);
        String lastProperty = "";
        try {
            XMLSecurityManager securityManager = (XMLSecurityManager) this._xsltc.getProperty("http://apache.org/xml/properties/security-manager");
            for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
                if (limit.isSupported(XMLSecurityManager.Processor.PARSER)) {
                    String lastProperty2 = limit.apiProperty();
                    reader.setProperty(lastProperty2, securityManager.getLimitValueAsString(limit));
                }
            }
            if (securityManager.printEntityCountInfo()) {
                lastProperty = "http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo";
                reader.setProperty("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo", "yes");
            }
        } catch (SAXException se) {
            XMLSecurityManager.printWarning(reader.getClass().getName(), lastProperty, se);
        }
        return parse(reader, input);
    }

    public SyntaxTreeNode getDocumentRoot() {
        return this._root;
    }

    protected void setPIParameters(String media, String title, String charset) {
        this._PImedia = media;
        this._PItitle = title;
        this._PIcharset = charset;
    }

    private SyntaxTreeNode getStylesheet(SyntaxTreeNode root) throws CompilerException {
        if (this._target == null) {
            if (!this._rootNamespaceDef) {
                ErrorMsg msg = new ErrorMsg(ErrorMsg.MISSING_XSLT_URI_ERR);
                throw new CompilerException(msg.toString());
            }
            return root;
        }
        if (this._target.charAt(0) == '#') {
            SyntaxTreeNode element = findStylesheet(root, this._target.substring(1));
            if (element == null) {
                ErrorMsg msg2 = new ErrorMsg(ErrorMsg.MISSING_XSLT_TARGET_ERR, (Object) this._target, root);
                throw new CompilerException(msg2.toString());
            }
            return element;
        }
        try {
            String path = this._target;
            if (path.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) == -1) {
                path = "file:" + path;
            }
            String accessError = SecuritySupport.checkAccess(SystemIDResolver.getAbsoluteURI(path), (String) this._xsltc.getProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET), "all");
            if (accessError != null) {
                ErrorMsg msg3 = new ErrorMsg(ErrorMsg.ACCESSING_XSLT_TARGET_ERR, SecuritySupport.sanitizePath(this._target), accessError, root);
                throw new CompilerException(msg3.toString());
            }
            return loadExternalStylesheet(this._target);
        } catch (IOException ex) {
            throw new CompilerException(ex);
        }
    }

    private SyntaxTreeNode findStylesheet(SyntaxTreeNode root, String href) {
        if (root == null) {
            return null;
        }
        if (root instanceof Stylesheet) {
            String id = root.getAttribute("id");
            if (id.equals(href)) {
                return root;
            }
        }
        List<SyntaxTreeNode> children = root.getContents();
        if (children != null) {
            int count = children.size();
            for (int i2 = 0; i2 < count; i2++) {
                SyntaxTreeNode child = children.get(i2);
                SyntaxTreeNode node = findStylesheet(child, href);
                if (node != null) {
                    return node;
                }
            }
            return null;
        }
        return null;
    }

    private SyntaxTreeNode loadExternalStylesheet(String location) throws CompilerException {
        InputSource source;
        if (new File(location).exists()) {
            source = new InputSource("file:" + location);
        } else {
            source = new InputSource(location);
        }
        SyntaxTreeNode external = parse(source);
        return external;
    }

    private void initAttrTable(String elementName, String[] attrs) {
        this._instructionAttrs.put(getQName("http://www.w3.org/1999/XSL/Transform", XSL, elementName).getStringRep(), attrs);
    }

    private void initInstructionAttrs() {
        initAttrTable("template", new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MATCH, "name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_PRIORITY, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MODE});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_STYLESHEET_STRING, new String[]{"id", "version", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXTENSIONELEMENTPREFIXES, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXCLUDE_RESULT_PREFIXES});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_TRANSFORM_STRING, new String[]{"id", "version", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXTENSIONELEMENTPREFIXES, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_EXCLUDE_RESULT_PREFIXES});
        initAttrTable("text", new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DISABLE_OUTPUT_ESCAPING});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_IF_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_TEST});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_CHOOSE_STRING, new String[0]);
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_WHEN_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_TEST});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_OTHERWISE_STRING, new String[0]);
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_FOREACH_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT});
        initAttrTable("message", new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_TERMINATE});
        initAttrTable("number", new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_LEVEL, "count", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_FROM, "value", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_FORMAT, "lang", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_LETTERVALUE, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_GROUPINGSEPARATOR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_GROUPINGSIZE});
        initAttrTable("comment", new String[0]);
        initAttrTable("copy", new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_USEATTRIBUTESETS});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_COPY_OF_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PARAMVARIABLE_STRING, new String[]{"name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_WITHPARAM_STRING, new String[]{"name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_VARIABLE_STRING, new String[]{"name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_OUTPUT_STRING, new String[]{"method", "version", "encoding", "omit-xml-declaration", "standalone", "doctype-public", "doctype-system", "cdata-section-elements", "indent", "media-type"});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_SORT_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ORDER, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_CASEORDER, "lang", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DATATYPE});
        initAttrTable("key", new String[]{"name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MATCH, "use"});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_FALLBACK_STRING, new String[0]);
        initAttrTable("attribute", new String[]{"name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NAMESPACE});
        initAttrTable("attribute-set", new String[]{"name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_USEATTRIBUTESETS});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_VALUEOF_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DISABLE_OUTPUT_ESCAPING});
        initAttrTable("element", new String[]{"name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NAMESPACE, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_USEATTRIBUTESETS});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_CALLTEMPLATE_STRING, new String[]{"name"});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_APPLY_TEMPLATES_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MODE});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_APPLY_IMPORTS_STRING, new String[0]);
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_DECIMALFORMAT_STRING, new String[]{"name", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DECIMALSEPARATOR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_GROUPINGSEPARATOR, "infinity", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MINUSSIGN, "NaN", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_PERCENT, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_PERMILLE, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ZERODIGIT, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DIGIT, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_PATTERNSEPARATOR});
        initAttrTable("import", new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_HREF});
        initAttrTable("include", new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_HREF});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_STRIPSPACE_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ELEMENTS});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PRESERVESPACE_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ELEMENTS});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PI_STRING, new String[]{"name"});
        initAttrTable(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_NSALIAS_STRING, new String[]{com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_STYLESHEET_PREFIX, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_RESULT_PREFIX});
    }

    private void initStdClasses() {
        initStdClass("template", "Template");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_STYLESHEET_STRING, "Stylesheet");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_TRANSFORM_STRING, "Stylesheet");
        initStdClass("text", "Text");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_IF_STRING, "If");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_CHOOSE_STRING, "Choose");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_WHEN_STRING, "When");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_OTHERWISE_STRING, "Otherwise");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_FOREACH_STRING, "ForEach");
        initStdClass("message", "Message");
        initStdClass("number", "Number");
        initStdClass("comment", "Comment");
        initStdClass("copy", "Copy");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_COPY_OF_STRING, "CopyOf");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PARAMVARIABLE_STRING, "Param");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_WITHPARAM_STRING, "WithParam");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_VARIABLE_STRING, "Variable");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_OUTPUT_STRING, "Output");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_SORT_STRING, "Sort");
        initStdClass("key", "Key");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_FALLBACK_STRING, "Fallback");
        initStdClass("attribute", "XslAttribute");
        initStdClass("attribute-set", "AttributeSet");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_VALUEOF_STRING, "ValueOf");
        initStdClass("element", "XslElement");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_CALLTEMPLATE_STRING, "CallTemplate");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_APPLY_TEMPLATES_STRING, "ApplyTemplates");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_APPLY_IMPORTS_STRING, "ApplyImports");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_DECIMALFORMAT_STRING, "DecimalFormatting");
        initStdClass("import", "Import");
        initStdClass("include", MtomCodec.XOP_LOCALNAME);
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_STRIPSPACE_STRING, "Whitespace");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PRESERVESPACE_STRING, "Whitespace");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PI_STRING, "ProcessingInstruction");
        initStdClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_NSALIAS_STRING, "NamespaceAlias");
    }

    private void initStdClass(String elementName, String className) {
        this._instructionClasses.put(getQName("http://www.w3.org/1999/XSL/Transform", XSL, elementName).getStringRep(), "com.sun.org.apache.xalan.internal.xsltc.compiler." + className);
    }

    public boolean elementSupported(String namespace, String localName) {
        return this._instructionClasses.get(getQName(namespace, XSL, localName).getStringRep()) != null;
    }

    public boolean functionSupported(String fname) {
        return this._symbolTable.lookupPrimop(fname) != null;
    }

    private void initExtClasses() {
        initExtClass(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_OUTPUT_STRING, "TransletOutput");
        initExtClass("http://xml.apache.org/xalan/redirect", "write", "TransletOutput");
    }

    private void initExtClass(String elementName, String className) {
        this._instructionClasses.put(getQName(Constants.TRANSLET_URI, "translet", elementName).getStringRep(), "com.sun.org.apache.xalan.internal.xsltc.compiler." + className);
    }

    private void initExtClass(String namespace, String elementName, String className) {
        this._instructionClasses.put(getQName(namespace, "translet", elementName).getStringRep(), "com.sun.org.apache.xalan.internal.xsltc.compiler." + className);
    }

    private void initSymbolTable() {
        MethodType I_V = new MethodType(Type.Int, Type.Void);
        new MethodType(Type.Int, Type.Real);
        MethodType I_S = new MethodType(Type.Int, Type.String);
        MethodType I_D = new MethodType(Type.Int, Type.NodeSet);
        new MethodType(Type.Real, Type.Int);
        MethodType R_V = new MethodType(Type.Real, Type.Void);
        MethodType R_R = new MethodType(Type.Real, Type.Real);
        MethodType R_D = new MethodType(Type.Real, Type.NodeSet);
        MethodType R_O = new MethodType(Type.Real, Type.Reference);
        MethodType I_I = new MethodType(Type.Int, Type.Int);
        MethodType D_O = new MethodType(Type.NodeSet, Type.Reference);
        MethodType D_V = new MethodType(Type.NodeSet, Type.Void);
        MethodType D_S = new MethodType(Type.NodeSet, Type.String);
        MethodType D_D = new MethodType(Type.NodeSet, Type.NodeSet);
        MethodType A_V = new MethodType(Type.Node, Type.Void);
        MethodType S_V = new MethodType(Type.String, Type.Void);
        MethodType S_S = new MethodType(Type.String, Type.String);
        MethodType S_A = new MethodType(Type.String, Type.Node);
        MethodType S_D = new MethodType(Type.String, Type.NodeSet);
        MethodType S_O = new MethodType(Type.String, Type.Reference);
        MethodType B_O = new MethodType(Type.Boolean, Type.Reference);
        MethodType B_V = new MethodType(Type.Boolean, Type.Void);
        MethodType B_B = new MethodType(Type.Boolean, Type.Boolean);
        MethodType B_S = new MethodType(Type.Boolean, Type.String);
        new MethodType(Type.NodeSet, Type.Object);
        MethodType R_RR = new MethodType(Type.Real, Type.Real, Type.Real);
        MethodType I_II = new MethodType(Type.Int, Type.Int, Type.Int);
        MethodType B_RR = new MethodType(Type.Boolean, Type.Real, Type.Real);
        MethodType B_II = new MethodType(Type.Boolean, Type.Int, Type.Int);
        MethodType S_SS = new MethodType(Type.String, Type.String, Type.String);
        MethodType S_DS = new MethodType(Type.String, Type.Real, Type.String);
        MethodType S_SR = new MethodType(Type.String, Type.String, Type.Real);
        MethodType O_SO = new MethodType(Type.Reference, Type.String, Type.Reference);
        MethodType D_SS = new MethodType(Type.NodeSet, Type.String, Type.String);
        MethodType D_SD = new MethodType(Type.NodeSet, Type.String, Type.NodeSet);
        MethodType B_BB = new MethodType(Type.Boolean, Type.Boolean, Type.Boolean);
        MethodType B_SS = new MethodType(Type.Boolean, Type.String, Type.String);
        new MethodType(Type.String, Type.String, Type.NodeSet);
        MethodType S_DSS = new MethodType(Type.String, Type.Real, Type.String, Type.String);
        MethodType S_SRR = new MethodType(Type.String, Type.String, Type.Real, Type.Real);
        MethodType S_SSS = new MethodType(Type.String, Type.String, Type.String, Type.String);
        this._symbolTable.addPrimop(Keywords.FUNC_CURRENT_STRING, A_V);
        this._symbolTable.addPrimop(Keywords.FUNC_LAST_STRING, I_V);
        this._symbolTable.addPrimop(Keywords.FUNC_POSITION_STRING, I_V);
        this._symbolTable.addPrimop("true", B_V);
        this._symbolTable.addPrimop("false", B_V);
        this._symbolTable.addPrimop(Keywords.FUNC_NOT_STRING, B_B);
        this._symbolTable.addPrimop("name", S_V);
        this._symbolTable.addPrimop("name", S_A);
        this._symbolTable.addPrimop(Keywords.FUNC_GENERATE_ID_STRING, S_V);
        this._symbolTable.addPrimop(Keywords.FUNC_GENERATE_ID_STRING, S_A);
        this._symbolTable.addPrimop(Keywords.FUNC_CEILING_STRING, R_R);
        this._symbolTable.addPrimop(Keywords.FUNC_FLOOR_STRING, R_R);
        this._symbolTable.addPrimop(Keywords.FUNC_ROUND_STRING, R_R);
        this._symbolTable.addPrimop(Keywords.FUNC_CONTAINS_STRING, B_SS);
        this._symbolTable.addPrimop("number", R_O);
        this._symbolTable.addPrimop("number", R_V);
        this._symbolTable.addPrimop("boolean", B_O);
        this._symbolTable.addPrimop("string", S_O);
        this._symbolTable.addPrimop("string", S_V);
        this._symbolTable.addPrimop(Keywords.FUNC_TRANSLATE_STRING, S_SSS);
        this._symbolTable.addPrimop(Keywords.FUNC_STRING_LENGTH_STRING, I_V);
        this._symbolTable.addPrimop(Keywords.FUNC_STRING_LENGTH_STRING, I_S);
        this._symbolTable.addPrimop(Keywords.FUNC_STARTS_WITH_STRING, B_SS);
        this._symbolTable.addPrimop("format-number", S_DS);
        this._symbolTable.addPrimop("format-number", S_DSS);
        this._symbolTable.addPrimop(Keywords.FUNC_UNPARSED_ENTITY_URI_STRING, S_S);
        this._symbolTable.addPrimop("key", D_SS);
        this._symbolTable.addPrimop("key", D_SD);
        this._symbolTable.addPrimop("id", D_S);
        this._symbolTable.addPrimop("id", D_D);
        this._symbolTable.addPrimop(Keywords.FUNC_NAMESPACE_STRING, S_V);
        this._symbolTable.addPrimop(Keywords.FUNC_EXT_FUNCTION_AVAILABLE_STRING, B_S);
        this._symbolTable.addPrimop(Keywords.FUNC_EXT_ELEM_AVAILABLE_STRING, B_S);
        this._symbolTable.addPrimop(Constants.DOCUMENT_PNAME, D_S);
        this._symbolTable.addPrimop(Constants.DOCUMENT_PNAME, D_V);
        this._symbolTable.addPrimop("count", I_D);
        this._symbolTable.addPrimop(Keywords.FUNC_SUM_STRING, R_D);
        this._symbolTable.addPrimop(Keywords.FUNC_LOCAL_PART_STRING, S_V);
        this._symbolTable.addPrimop(Keywords.FUNC_LOCAL_PART_STRING, S_D);
        this._symbolTable.addPrimop(Keywords.FUNC_NAMESPACE_STRING, S_V);
        this._symbolTable.addPrimop(Keywords.FUNC_NAMESPACE_STRING, S_D);
        this._symbolTable.addPrimop(Keywords.FUNC_SUBSTRING_STRING, S_SR);
        this._symbolTable.addPrimop(Keywords.FUNC_SUBSTRING_STRING, S_SRR);
        this._symbolTable.addPrimop(Keywords.FUNC_SUBSTRING_AFTER_STRING, S_SS);
        this._symbolTable.addPrimop(Keywords.FUNC_SUBSTRING_BEFORE_STRING, S_SS);
        this._symbolTable.addPrimop(Keywords.FUNC_NORMALIZE_SPACE_STRING, S_V);
        this._symbolTable.addPrimop(Keywords.FUNC_NORMALIZE_SPACE_STRING, S_S);
        this._symbolTable.addPrimop(Keywords.FUNC_SYSTEM_PROPERTY_STRING, S_S);
        this._symbolTable.addPrimop("nodeset", D_O);
        this._symbolTable.addPrimop("objectType", S_O);
        this._symbolTable.addPrimop("cast", O_SO);
        this._symbolTable.addPrimop(Marker.ANY_NON_NULL_MARKER, R_RR);
        this._symbolTable.addPrimop(LanguageTag.SEP, R_RR);
        this._symbolTable.addPrimop("*", R_RR);
        this._symbolTable.addPrimop("/", R_RR);
        this._symbolTable.addPrimop(FXMLLoader.RESOURCE_KEY_PREFIX, R_RR);
        this._symbolTable.addPrimop(Marker.ANY_NON_NULL_MARKER, I_II);
        this._symbolTable.addPrimop(LanguageTag.SEP, I_II);
        this._symbolTable.addPrimop("*", I_II);
        this._symbolTable.addPrimop("<", B_RR);
        this._symbolTable.addPrimop("<=", B_RR);
        this._symbolTable.addPrimop(">", B_RR);
        this._symbolTable.addPrimop(">=", B_RR);
        this._symbolTable.addPrimop("<", B_II);
        this._symbolTable.addPrimop("<=", B_II);
        this._symbolTable.addPrimop(">", B_II);
        this._symbolTable.addPrimop(">=", B_II);
        this._symbolTable.addPrimop("<", B_BB);
        this._symbolTable.addPrimop("<=", B_BB);
        this._symbolTable.addPrimop(">", B_BB);
        this._symbolTable.addPrimop(">=", B_BB);
        this._symbolTable.addPrimop("or", B_BB);
        this._symbolTable.addPrimop("and", B_BB);
        this._symbolTable.addPrimop("u-", R_R);
        this._symbolTable.addPrimop("u-", I_I);
    }

    public SymbolTable getSymbolTable() {
        return this._symbolTable;
    }

    public Template getTemplate() {
        return this._template;
    }

    public void setTemplate(Template template) {
        this._template = template;
    }

    public int getTemplateIndex() {
        int i2 = this._templateIndex;
        this._templateIndex = i2 + 1;
        return i2;
    }

    public SyntaxTreeNode makeInstance(String uri, String prefix, String local, Attributes attributes) throws ConfigurationError {
        SyntaxTreeNode node = null;
        QName qname = getQName(uri, prefix, local);
        String className = this._instructionClasses.get(qname.getStringRep());
        if (className != null) {
            try {
                Class<?> clazz = ObjectFactory.findProviderClass(className, true);
                node = (SyntaxTreeNode) clazz.newInstance();
                node.setQName(qname);
                node.setParser(this);
                if (this._locator != null) {
                    node.setLineNumber(getLineNumber());
                }
                if (node instanceof Stylesheet) {
                    this._xsltc.setStylesheet((Stylesheet) node);
                }
                checkForSuperfluousAttributes(node, attributes);
            } catch (ClassNotFoundException e2) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, node);
                reportError(3, err);
            } catch (Exception e3) {
                ErrorMsg err2 = new ErrorMsg(ErrorMsg.INTERNAL_ERR, (Object) e3.getMessage(), node);
                reportError(2, err2);
            }
        } else {
            if (uri != null) {
                if (uri.equals("http://www.w3.org/1999/XSL/Transform")) {
                    node = new UnsupportedElement(uri, prefix, local, false);
                    UnsupportedElement element = (UnsupportedElement) node;
                    ErrorMsg msg = new ErrorMsg("UNSUPPORTED_XSL_ERR", getLineNumber(), local);
                    element.setErrorMessage(msg);
                    if (this.versionIsOne) {
                        reportError(1, msg);
                    }
                } else if (uri.equals(Constants.TRANSLET_URI)) {
                    node = new UnsupportedElement(uri, prefix, local, true);
                    UnsupportedElement element2 = (UnsupportedElement) node;
                    ErrorMsg msg2 = new ErrorMsg("UNSUPPORTED_EXT_ERR", getLineNumber(), local);
                    element2.setErrorMessage(msg2);
                } else {
                    Stylesheet sheet = this._xsltc.getStylesheet();
                    if (sheet != null && sheet.isExtension(uri) && sheet != this._parentStack.peek()) {
                        node = new UnsupportedElement(uri, prefix, local, true);
                        UnsupportedElement elem = (UnsupportedElement) node;
                        ErrorMsg msg3 = new ErrorMsg("UNSUPPORTED_EXT_ERR", getLineNumber(), prefix + CallSiteDescriptor.TOKEN_DELIMITER + local);
                        elem.setErrorMessage(msg3);
                    }
                }
            }
            if (node == null) {
                node = new LiteralElement();
                node.setLineNumber(getLineNumber());
            }
        }
        if (node != null && (node instanceof LiteralElement)) {
            ((LiteralElement) node).setQName(qname);
        }
        return node;
    }

    private void checkForSuperfluousAttributes(SyntaxTreeNode node, Attributes attrs) {
        QName qname = node.getQName();
        boolean isStylesheet = node instanceof Stylesheet;
        String[] legal = this._instructionAttrs.get(qname.getStringRep());
        if (this.versionIsOne && legal != null) {
            int n2 = attrs.getLength();
            for (int i2 = 0; i2 < n2; i2++) {
                String attrQName = attrs.getQName(i2);
                if (isStylesheet && attrQName.equals("version")) {
                    this.versionIsOne = attrs.getValue(i2).equals("1.0");
                }
                if (!attrQName.startsWith("xml") && attrQName.indexOf(58) <= 0) {
                    int j2 = 0;
                    while (j2 < legal.length && !attrQName.equalsIgnoreCase(legal[j2])) {
                        j2++;
                    }
                    if (j2 == legal.length) {
                        ErrorMsg err = new ErrorMsg(ErrorMsg.ILLEGAL_ATTRIBUTE_ERR, (Object) attrQName, node);
                        err.setWarningError(true);
                        reportError(4, err);
                    }
                }
            }
        }
    }

    public Expression parseExpression(SyntaxTreeNode parent, String exp) {
        return (Expression) parseTopLevel(parent, "<EXPRESSION>" + exp, null);
    }

    public Expression parseExpression(SyntaxTreeNode parent, String attr, String def) {
        String exp = parent.getAttribute(attr);
        if (exp.length() == 0 && def != null) {
            exp = def;
        }
        return (Expression) parseTopLevel(parent, "<EXPRESSION>" + exp, exp);
    }

    public Pattern parsePattern(SyntaxTreeNode parent, String pattern) {
        return (Pattern) parseTopLevel(parent, "<PATTERN>" + pattern, pattern);
    }

    public Pattern parsePattern(SyntaxTreeNode parent, String attr, String def) {
        String pattern = parent.getAttribute(attr);
        if (pattern.length() == 0 && def != null) {
            pattern = def;
        }
        return (Pattern) parseTopLevel(parent, "<PATTERN>" + pattern, pattern);
    }

    private SyntaxTreeNode parseTopLevel(SyntaxTreeNode parent, String text, String expression) {
        Symbol result;
        SyntaxTreeNode node;
        int line = getLineNumber();
        try {
            this._xpathParser.setScanner(new XPathLexer(new StringReader(text)));
            result = this._xpathParser.parse(expression, line);
        } catch (Exception e2) {
            if (ErrorMsg.XPATH_LIMIT.equals(e2.getMessage())) {
                throw new RuntimeException(ErrorMsg.XPATH_LIMIT);
            }
            if (this._xsltc.debug()) {
                e2.printStackTrace();
            }
            reportError(3, new ErrorMsg(ErrorMsg.XPATH_PARSER_ERR, (Object) expression, parent));
        }
        if (result != null && (node = (SyntaxTreeNode) result.value) != null) {
            node.setParser(this);
            node.setParent(parent);
            node.setLineNumber(line);
            return node;
        }
        reportError(3, new ErrorMsg(ErrorMsg.XPATH_PARSER_ERR, (Object) expression, parent));
        SyntaxTreeNode.Dummy.setParser(this);
        return SyntaxTreeNode.Dummy;
    }

    public boolean errorsFound() {
        return this._errors.size() > 0;
    }

    public void printErrors() {
        int size = this._errors.size();
        if (size > 0) {
            System.err.println(new ErrorMsg(ErrorMsg.COMPILER_ERROR_KEY));
            for (int i2 = 0; i2 < size; i2++) {
                System.err.println(sun.security.pkcs11.wrapper.Constants.INDENT + ((Object) this._errors.get(i2)));
            }
        }
    }

    public void printWarnings() {
        int size = this._warnings.size();
        if (size > 0) {
            System.err.println(new ErrorMsg(ErrorMsg.COMPILER_WARNING_KEY));
            for (int i2 = 0; i2 < size; i2++) {
                System.err.println(sun.security.pkcs11.wrapper.Constants.INDENT + ((Object) this._warnings.get(i2)));
            }
        }
    }

    public void reportError(int category, ErrorMsg error) {
        switch (category) {
            case 0:
                this._errors.add(error);
                break;
            case 1:
                this._errors.add(error);
                break;
            case 2:
                this._errors.add(error);
                break;
            case 3:
                this._errors.add(error);
                break;
            case 4:
                this._warnings.add(error);
                break;
        }
    }

    public ArrayList<ErrorMsg> getErrors() {
        return this._errors;
    }

    public ArrayList<ErrorMsg> getWarnings() {
        return this._warnings;
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() {
        this._root = null;
        this._target = null;
        this._prefixMapping = null;
        this._parentStack = new Stack<>();
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() {
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        if (this._prefixMapping == null) {
            this._prefixMapping = new HashMap();
        }
        this._prefixMapping.put(prefix, uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localname, String qname, Attributes attributes) throws SAXException, ConfigurationError {
        int col = qname.lastIndexOf(58);
        String prefix = col == -1 ? null : qname.substring(0, col);
        SyntaxTreeNode element = makeInstance(uri, prefix, localname, attributes);
        if (element == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.ELEMENT_PARSE_ERR, prefix + ':' + localname);
            throw new SAXException(err.toString());
        }
        if (this._root == null) {
            if (this._prefixMapping == null || !this._prefixMapping.containsValue("http://www.w3.org/1999/XSL/Transform")) {
                this._rootNamespaceDef = false;
            } else {
                this._rootNamespaceDef = true;
            }
            this._root = element;
        } else {
            SyntaxTreeNode parent = this._parentStack.peek();
            parent.addElement(element);
            element.setParent(parent);
        }
        element.setAttributes(new AttributesImpl(attributes));
        element.setPrefixMapping(this._prefixMapping);
        if (element instanceof Stylesheet) {
            getSymbolTable().setCurrentNode(element);
            ((Stylesheet) element).declareExtensionPrefixes(this);
        }
        this._prefixMapping = null;
        this._parentStack.push(element);
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localname, String qname) {
        this._parentStack.pop();
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) {
        String string = new String(ch, start, length);
        SyntaxTreeNode parent = this._parentStack.peek();
        if (string.length() == 0) {
            return;
        }
        if (parent instanceof Text) {
            ((Text) parent).setText(string);
            return;
        }
        if (parent instanceof Stylesheet) {
            return;
        }
        SyntaxTreeNode bro = parent.lastChild();
        if (bro != null && (bro instanceof Text)) {
            Text text = (Text) bro;
            if (!text.isTextElement() && (length > 1 || ch[0] < 256)) {
                text.setText(string);
                return;
            }
        }
        parent.addElement(new Text(string));
    }

    private String getTokenValue(String token) {
        int start = token.indexOf(34);
        int stop = token.lastIndexOf(34);
        return token.substring(start + 1, stop);
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String name, String value) {
        if (this._target == null && name.equals("xml-stylesheet")) {
            String href = null;
            String media = null;
            String title = null;
            String charset = null;
            StringTokenizer tokens = new StringTokenizer(value);
            while (tokens.hasMoreElements()) {
                String token = (String) tokens.nextElement2();
                if (token.startsWith(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_HREF)) {
                    href = getTokenValue(token);
                } else if (token.startsWith("media")) {
                    media = getTokenValue(token);
                } else if (token.startsWith("title")) {
                    title = getTokenValue(token);
                } else if (token.startsWith("charset")) {
                    charset = getTokenValue(token);
                }
            }
            if (this._PImedia == null || this._PImedia.equals(media)) {
                if (this._PItitle == null || this._PImedia.equals(title)) {
                    if (this._PIcharset == null || this._PImedia.equals(charset)) {
                        this._target = href;
                    }
                }
            }
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) {
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this._locator = locator;
    }

    private int getLineNumber() {
        int line = 0;
        if (this._locator != null) {
            line = this._locator.getLineNumber();
        }
        return line;
    }
}
