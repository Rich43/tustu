package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xml.internal.serializer.utils.MsgKey;
import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import java.io.IOException;
import java.io.Writer;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToXMLStream.class */
public final class ToXMLStream extends ToStream {
    boolean m_cdataTagOpen = false;
    private static CharInfo m_xmlcharInfo = CharInfo.getCharInfoInternal(CharInfo.XML_ENTITIES_RESOURCE, "xml");

    public ToXMLStream() {
        this.m_charInfo = m_xmlcharInfo;
        initCDATA();
        this.m_prefixMap = new NamespaceMappings();
    }

    public void CopyFrom(ToXMLStream xmlListener) {
        this.m_writer = xmlListener.m_writer;
        String encoding = xmlListener.getEncoding();
        setEncoding(encoding);
        setOmitXMLDeclaration(xmlListener.getOmitXMLDeclaration());
        this.m_ispreserve = xmlListener.m_ispreserve;
        this.m_preserves = xmlListener.m_preserves;
        this.m_isprevtext = xmlListener.m_isprevtext;
        this.m_doIndent = xmlListener.m_doIndent;
        setIndentAmount(xmlListener.getIndentAmount());
        this.m_startNewLine = xmlListener.m_startNewLine;
        this.m_needToOutputDocTypeDecl = xmlListener.m_needToOutputDocTypeDecl;
        setDoctypeSystem(xmlListener.getDoctypeSystem());
        setDoctypePublic(xmlListener.getDoctypePublic());
        setStandalone(xmlListener.getStandalone());
        setMediaType(xmlListener.getMediaType());
        this.m_maxCharacter = xmlListener.m_maxCharacter;
        this.m_encodingInfo = xmlListener.m_encodingInfo;
        this.m_spaceBeforeClose = xmlListener.m_spaceBeforeClose;
        this.m_cdataStartCalled = xmlListener.m_cdataStartCalled;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase
    public void startDocumentInternal() throws SAXException {
        String standalone;
        if (this.m_needToCallStartDocument) {
            super.startDocumentInternal();
            this.m_needToCallStartDocument = false;
            if (this.m_inEntityRef) {
                return;
            }
            this.m_needToOutputDocTypeDecl = true;
            this.m_startNewLine = false;
            if (!getOmitXMLDeclaration()) {
                String encoding = Encodings.getMimeEncoding(getEncoding());
                String version = getVersion();
                if (version == null) {
                    version = "1.0";
                }
                if (this.m_standaloneWasSpecified) {
                    standalone = " standalone=\"" + getStandalone() + PdfOps.DOUBLE_QUOTE__TOKEN;
                } else {
                    standalone = "";
                }
                try {
                    Writer writer = this.m_writer;
                    writer.write("<?xml version=\"");
                    writer.write(version);
                    writer.write("\" encoding=\"");
                    writer.write(encoding);
                    writer.write(34);
                    writer.write(standalone);
                    writer.write("?>");
                    if (this.m_doIndent && (this.m_standaloneWasSpecified || getDoctypePublic() != null || getDoctypeSystem() != null || this.m_isStandalone)) {
                        writer.write(this.m_lineSep, 0, this.m_lineSepLen);
                    }
                } catch (IOException e2) {
                    throw new SAXException(e2);
                }
            }
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        flushPending();
        if (this.m_doIndent && !this.m_isprevtext) {
            try {
                outputLineSep();
            } catch (IOException e2) {
                throw new SAXException(e2);
            }
        }
        flushWriter();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    public void startPreserving() throws SAXException {
        this.m_preserves.push(true);
        this.m_ispreserve = true;
    }

    public void endPreserving() throws SAXException {
        this.m_ispreserve = this.m_preserves.isEmpty() ? false : this.m_preserves.pop();
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        flushPending();
        if (target.equals(Result.PI_DISABLE_OUTPUT_ESCAPING)) {
            startNonEscaping();
        } else if (target.equals(Result.PI_ENABLE_OUTPUT_ESCAPING)) {
            endNonEscaping();
        } else {
            try {
                if (this.m_elemContext.m_startTagOpen) {
                    closeStartTag();
                    this.m_elemContext.m_startTagOpen = false;
                } else if (this.m_needToCallStartDocument) {
                    startDocumentInternal();
                }
                if (shouldIndent()) {
                    indent();
                }
                Writer writer = this.m_writer;
                writer.write("<?");
                writer.write(target);
                if (data.length() > 0 && !Character.isSpaceChar(data.charAt(0))) {
                    writer.write(32);
                }
                int indexOfQLT = data.indexOf("?>");
                if (indexOfQLT >= 0) {
                    if (indexOfQLT > 0) {
                        writer.write(data.substring(0, indexOfQLT));
                    }
                    writer.write("? >");
                    if (indexOfQLT + 2 < data.length()) {
                        writer.write(data.substring(indexOfQLT + 2));
                    }
                } else {
                    writer.write(data);
                }
                writer.write(63);
                writer.write(62);
                if (this.m_elemContext.m_currentElemDepth <= 0 && this.m_isStandalone) {
                    writer.write(this.m_lineSep, 0, this.m_lineSepLen);
                }
                this.m_startNewLine = true;
            } catch (IOException e2) {
                throw new SAXException(e2);
            }
        }
        if (this.m_tracer != null) {
            super.fireEscapingEvent(target, data);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void entityReference(String name) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        try {
            if (shouldIndent()) {
                indent();
            }
            Writer writer = this.m_writer;
            writer.write(38);
            writer.write(name);
            writer.write(59);
            if (this.m_tracer != null) {
                super.fireEntityReference(name);
            }
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addUniqueAttribute(String name, String value, int flags) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            try {
                String patchedName = patchName(name);
                Writer writer = this.m_writer;
                if ((flags & 1) > 0 && m_xmlcharInfo.onlyQuotAmpLtGt) {
                    writer.write(32);
                    writer.write(patchedName);
                    writer.write("=\"");
                    writer.write(value);
                    writer.write(34);
                } else {
                    writer.write(32);
                    writer.write(patchedName);
                    writer.write("=\"");
                    writeAttrString(writer, value, getEncoding());
                    writer.write(34);
                }
            } catch (IOException e2) {
                throw new SAXException(e2);
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean xslAttribute) throws SAXException {
        String prefixUsed;
        if (this.m_elemContext.m_startTagOpen) {
            boolean was_added = addAttributeAlways(uri, localName, rawName, type, value, xslAttribute);
            if (was_added && !xslAttribute && !rawName.startsWith("xmlns") && (prefixUsed = ensureAttributesNamespaceIsDeclared(uri, localName, rawName)) != null && rawName != null && !rawName.startsWith(prefixUsed)) {
                rawName = prefixUsed + CallSiteDescriptor.TOKEN_DELIMITER + localName;
            }
            addAttributeAlways(uri, localName, rawName, type, value, xslAttribute);
            return;
        }
        String msg = Utils.messages.createMessage("ER_ILLEGAL_ATTRIBUTE_POSITION", new Object[]{localName});
        try {
            Transformer tran = super.getTransformer();
            ErrorListener errHandler = tran.getErrorListener();
            if (null != errHandler && this.m_sourceLocator != null) {
                errHandler.warning(new TransformerException(msg, this.m_sourceLocator));
            } else {
                System.out.println(msg);
            }
        } catch (Exception e2) {
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String elemName) throws SAXException {
        endElement(null, null, elemName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
        if (this.m_elemContext.m_elementURI == null) {
            String prefix1 = getPrefixPart(this.m_elemContext.m_elementName);
            if (prefix1 == null && "".equals(prefix)) {
                this.m_elemContext.m_elementURI = uri;
            }
        }
        startPrefixMapping(prefix, uri, false);
    }

    protected boolean pushNamespace(String prefix, String uri) {
        try {
            if (this.m_prefixMap.pushNamespace(prefix, uri, this.m_elemContext.m_currentElemDepth)) {
                startPrefixMapping(prefix, uri);
                return true;
            }
            return false;
        } catch (SAXException e2) {
            return false;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        boolean wasReset = false;
        if (super.reset()) {
            resetToXMLStream();
            wasReset = true;
        }
        return wasReset;
    }

    private void resetToXMLStream() {
        this.m_cdataTagOpen = false;
    }

    private String getXMLVersion() {
        String xmlVersion;
        String xmlVersion2 = getVersion();
        if (xmlVersion2 == null || xmlVersion2.equals("1.0")) {
            xmlVersion = "1.0";
        } else if (xmlVersion2.equals(SerializerConstants.XMLVERSION11)) {
            xmlVersion = SerializerConstants.XMLVERSION11;
        } else {
            String msg = Utils.messages.createMessage(MsgKey.ER_XML_VERSION_NOT_SUPPORTED, new Object[]{xmlVersion2});
            try {
                Transformer tran = super.getTransformer();
                ErrorListener errHandler = tran.getErrorListener();
                if (null != errHandler && this.m_sourceLocator != null) {
                    errHandler.warning(new TransformerException(msg, this.m_sourceLocator));
                } else {
                    System.out.println(msg);
                }
            } catch (Exception e2) {
            }
            xmlVersion = "1.0";
        }
        return xmlVersion;
    }
}
