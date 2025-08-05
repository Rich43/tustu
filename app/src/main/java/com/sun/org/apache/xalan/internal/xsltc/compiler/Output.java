package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.glass.ui.Clipboard;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.serializer.Encodings;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Output.class */
final class Output extends TopLevelElement {
    private String _version;
    private String _method;
    private String _encoding;
    private String _standalone;
    private String _doctypePublic;
    private String _doctypeSystem;
    private String _cdata;
    private String _mediaType;
    private String _indentamount;
    private static final String STRING_SIG = "Ljava/lang/String;";
    private static final String XML_VERSION = "1.0";
    private static final String HTML_VERSION = "4.0";
    private boolean _omitHeader = false;
    private boolean _indent = false;
    private boolean _disabled = false;

    Output() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("Output " + this._method);
    }

    public void disable() {
        this._disabled = true;
    }

    public boolean enabled() {
        return !this._disabled;
    }

    public String getCdata() {
        return this._cdata;
    }

    public String getOutputMethod() {
        return this._method;
    }

    private void transferAttribute(Output previous, String qname) {
        if (!hasAttribute(qname) && previous.hasAttribute(qname)) {
            addAttribute(qname, previous.getAttribute(qname));
        }
    }

    public void mergeOutput(Output previous) {
        transferAttribute(previous, "version");
        transferAttribute(previous, "method");
        transferAttribute(previous, "encoding");
        transferAttribute(previous, "doctype-system");
        transferAttribute(previous, "doctype-public");
        transferAttribute(previous, "media-type");
        transferAttribute(previous, "indent");
        transferAttribute(previous, "omit-xml-declaration");
        transferAttribute(previous, "standalone");
        if (previous.hasAttribute("cdata-section-elements")) {
            addAttribute("cdata-section-elements", previous.getAttribute("cdata-section-elements") + ' ' + getAttribute("cdata-section-elements"));
        }
        String prefix = lookupPrefix("http://xml.apache.org/xalan");
        if (prefix != null) {
            transferAttribute(previous, prefix + ":indent-amount");
        }
        String prefix2 = lookupPrefix(com.sun.org.apache.xml.internal.utils.Constants.S_BUILTIN_OLD_EXTENSIONS_URL);
        if (prefix2 != null) {
            transferAttribute(previous, prefix2 + ":indent-amount");
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        Properties outputProperties = new Properties();
        parser.setOutput(this);
        if (this._disabled) {
            return;
        }
        this._version = getAttribute("version");
        if (this._version.equals("")) {
            this._version = null;
        } else {
            outputProperties.setProperty("version", this._version);
        }
        this._method = getAttribute("method");
        if (this._method.equals("")) {
            this._method = null;
        }
        if (this._method != null) {
            this._method = this._method.toLowerCase();
            if (this._method.equals("xml") || this._method.equals("html") || this._method.equals("text") || (XML11Char.isXML11ValidQName(this._method) && this._method.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) > 0)) {
                outputProperties.setProperty("method", this._method);
            } else {
                reportError(this, parser, ErrorMsg.INVALID_METHOD_IN_OUTPUT, this._method);
            }
        }
        this._encoding = getAttribute("encoding");
        if (this._encoding.equals("")) {
            this._encoding = null;
        } else {
            try {
                String canonicalEncoding = Encodings.convertMime2JavaEncoding(this._encoding);
                new OutputStreamWriter(System.out, canonicalEncoding);
            } catch (UnsupportedEncodingException e2) {
                ErrorMsg msg = new ErrorMsg(ErrorMsg.UNSUPPORTED_ENCODING, (Object) this._encoding, (SyntaxTreeNode) this);
                parser.reportError(4, msg);
            }
            outputProperties.setProperty("encoding", this._encoding);
        }
        String attrib = getAttribute("omit-xml-declaration");
        if (!attrib.equals("")) {
            if (attrib.equals("yes")) {
                this._omitHeader = true;
            }
            outputProperties.setProperty("omit-xml-declaration", attrib);
        }
        this._standalone = getAttribute("standalone");
        if (this._standalone.equals("")) {
            this._standalone = null;
        } else {
            outputProperties.setProperty("standalone", this._standalone);
        }
        this._doctypeSystem = getAttribute("doctype-system");
        if (this._doctypeSystem.equals("")) {
            this._doctypeSystem = null;
        } else {
            outputProperties.setProperty("doctype-system", this._doctypeSystem);
        }
        this._doctypePublic = getAttribute("doctype-public");
        if (this._doctypePublic.equals("")) {
            this._doctypePublic = null;
        } else {
            outputProperties.setProperty("doctype-public", this._doctypePublic);
        }
        this._cdata = getAttribute("cdata-section-elements");
        if (this._cdata.equals("")) {
            this._cdata = null;
        } else {
            StringBuffer expandedNames = new StringBuffer();
            StringTokenizer tokens = new StringTokenizer(this._cdata);
            while (tokens.hasMoreTokens()) {
                String qname = tokens.nextToken();
                if (!XML11Char.isXML11ValidQName(qname)) {
                    ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) qname, (SyntaxTreeNode) this);
                    parser.reportError(3, err);
                }
                expandedNames.append(parser.getQName(qname).toString()).append(' ');
            }
            this._cdata = expandedNames.toString();
            outputProperties.setProperty("cdata-section-elements", this._cdata);
        }
        String attrib2 = getAttribute("indent");
        if (!attrib2.equals("")) {
            if (attrib2.equals("yes")) {
                this._indent = true;
            }
            outputProperties.setProperty("indent", attrib2);
        } else if (this._method != null && this._method.equals("html")) {
            this._indent = true;
        }
        this._indentamount = getAttribute(lookupPrefix("http://xml.apache.org/xalan"), "indent-amount");
        if (this._indentamount.equals("")) {
            this._indentamount = getAttribute(lookupPrefix(com.sun.org.apache.xml.internal.utils.Constants.S_BUILTIN_OLD_EXTENSIONS_URL), "indent-amount");
        }
        if (!this._indentamount.equals("")) {
            outputProperties.setProperty("indent_amount", this._indentamount);
        }
        this._mediaType = getAttribute("media-type");
        if (this._mediaType.equals("")) {
            this._mediaType = null;
        } else {
            outputProperties.setProperty("media-type", this._mediaType);
        }
        if (this._method != null) {
            if (this._method.equals("html")) {
                if (this._version == null) {
                    this._version = HTML_VERSION;
                }
                if (this._mediaType == null) {
                    this._mediaType = Clipboard.HTML_TYPE;
                }
            } else if (this._method.equals("text") && this._mediaType == null) {
                this._mediaType = Clipboard.TEXT_TYPE;
            }
        }
        parser.getCurrentStylesheet().setOutputProperties(outputProperties);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        if (this._disabled) {
            return;
        }
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(classGen.loadTranslet());
        if (this._version != null && !this._version.equals("1.0")) {
            int field = cpg.addFieldref(Constants.TRANSLET_CLASS, "_version", "Ljava/lang/String;");
            il.append(DUP);
            il.append(new PUSH(cpg, this._version));
            il.append(new PUTFIELD(field));
        }
        if (this._method != null) {
            int field2 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_method", "Ljava/lang/String;");
            il.append(DUP);
            il.append(new PUSH(cpg, this._method));
            il.append(new PUTFIELD(field2));
        }
        if (this._encoding != null) {
            int field3 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_encoding", "Ljava/lang/String;");
            il.append(DUP);
            il.append(new PUSH(cpg, this._encoding));
            il.append(new PUTFIELD(field3));
        }
        if (this._omitHeader) {
            int field4 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_omitHeader", Constants.HASIDCALL_INDEX_SIG);
            il.append(DUP);
            il.append(new PUSH(cpg, this._omitHeader));
            il.append(new PUTFIELD(field4));
        }
        if (this._standalone != null) {
            int field5 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_standalone", "Ljava/lang/String;");
            il.append(DUP);
            il.append(new PUSH(cpg, this._standalone));
            il.append(new PUTFIELD(field5));
        }
        int field6 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_doctypeSystem", "Ljava/lang/String;");
        il.append(DUP);
        il.append(new PUSH(cpg, this._doctypeSystem));
        il.append(new PUTFIELD(field6));
        int field7 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_doctypePublic", "Ljava/lang/String;");
        il.append(DUP);
        il.append(new PUSH(cpg, this._doctypePublic));
        il.append(new PUTFIELD(field7));
        if (this._mediaType != null) {
            int field8 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_mediaType", "Ljava/lang/String;");
            il.append(DUP);
            il.append(new PUSH(cpg, this._mediaType));
            il.append(new PUTFIELD(field8));
        }
        if (this._indent) {
            int field9 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_indent", Constants.HASIDCALL_INDEX_SIG);
            il.append(DUP);
            il.append(new PUSH(cpg, this._indent));
            il.append(new PUTFIELD(field9));
        }
        if (this._indentamount != null && !this._indentamount.equals("")) {
            int field10 = cpg.addFieldref(Constants.TRANSLET_CLASS, "_indentamount", "I");
            il.append(DUP);
            il.append(new PUSH(cpg, Integer.parseInt(this._indentamount)));
            il.append(new PUTFIELD(field10));
        }
        if (this._cdata != null) {
            int index = cpg.addMethodref(Constants.TRANSLET_CLASS, "addCdataElement", "(Ljava/lang/String;)V");
            StringTokenizer tokens = new StringTokenizer(this._cdata);
            while (tokens.hasMoreTokens()) {
                il.append(DUP);
                il.append(new PUSH(cpg, tokens.nextToken()));
                il.append(new INVOKEVIRTUAL(index));
            }
        }
        il.append(POP);
    }
}
