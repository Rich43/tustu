package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/EmptySerializer.class */
public class EmptySerializer implements SerializationHandler {
    protected static final String ERR = "EmptySerializer method not over-ridden";

    protected void couldThrowIOException() throws IOException {
    }

    protected void couldThrowSAXException() throws SAXException {
    }

    protected void couldThrowSAXException(char[] chars, int off, int len) throws SAXException {
    }

    protected void couldThrowSAXException(String elemQName) throws SAXException {
    }

    void aMethodIsCalled() {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public ContentHandler asContentHandler() throws IOException {
        couldThrowIOException();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setContentHandler(ContentHandler ch) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void close() {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public Properties getOutputFormat() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public OutputStream getOutputStream() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public Writer getWriter() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        aMethodIsCalled();
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler, com.sun.org.apache.xml.internal.serializer.DOMSerializer
    public void serialize(Node node) throws IOException {
        couldThrowIOException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setCdataSectionElements(ArrayList<String> URI_and_localNames) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public boolean setEscaping(boolean escape) throws SAXException {
        couldThrowSAXException();
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setIndent(boolean indent) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setIndentAmount(int spaces) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setIsStandalone(boolean isStandalone) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setOutputFormat(Properties format) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setOutputStream(OutputStream output) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setVersion(String version) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setWriter(Writer writer) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setTransformer(Transformer transformer) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public Transformer getTransformer() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void flushPending() throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttributes(Attributes atts) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String name, String value) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String chars) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String elemName) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String uri, String localName, String qName) throws SAXException {
        couldThrowSAXException(qName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String qName) throws SAXException {
        couldThrowSAXException(qName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String uri, String prefix) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
        couldThrowSAXException();
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void entityReference(String entityName) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public NamespaceMappings getNamespaceMappings() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getPrefix(String uri) {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getNamespaceURI(String name, boolean isElement) {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getNamespaceURIFromPrefix(String prefix) {
        aMethodIsCalled();
        return null;
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator arg0) {
        aMethodIsCalled();
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String arg0, String arg1) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String arg0) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
        couldThrowSAXException(arg0, arg1, arg2);
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String arg0, String arg1) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String arg0) throws SAXException {
        couldThrowSAXException();
    }

    public void comment(String comment) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String arg0, String arg1, String arg2) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String arg0) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String arg0) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        couldThrowSAXException();
    }

    public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getDoctypePublic() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getDoctypeSystem() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getEncoding() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public boolean getIndent() {
        aMethodIsCalled();
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public int getIndentAmount() {
        aMethodIsCalled();
        return 0;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getMediaType() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public boolean getOmitXMLDeclaration() {
        aMethodIsCalled();
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getStandalone() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getVersion() {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctype(String system, String pub) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctypePublic(String doctype) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctypeSystem(String doctype) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setEncoding(String encoding) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setMediaType(String mediatype) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setOmitXMLDeclaration(boolean b2) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setStandalone(String standalone) {
        aMethodIsCalled();
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void elementDecl(String arg0, String arg1) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void internalEntityDecl(String arg0, String arg1) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void externalEntityDecl(String arg0, String arg1, String arg2) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException arg0) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException arg0) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException arg0) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public DOMSerializer asDOMSerializer() throws IOException {
        couldThrowIOException();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setNamespaceMappings(NamespaceMappings mappings) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void setSourceLocator(SourceLocator locator) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addUniqueAttribute(String name, String value, int flags) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(Node node) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addXSLAttribute(String qName, String value, String uri) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String arg0, String arg1, String arg2) throws SAXException {
        couldThrowSAXException();
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String arg0, String arg1, String arg2, String arg3) throws SAXException {
        couldThrowSAXException();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setDTDEntityExpansion(boolean expand) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getOutputProperty(String name) {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getOutputPropertyDefault(String name) {
        aMethodIsCalled();
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setOutputProperty(String name, String val) {
        aMethodIsCalled();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setOutputPropertyDefault(String name, String val) {
        aMethodIsCalled();
    }
}
