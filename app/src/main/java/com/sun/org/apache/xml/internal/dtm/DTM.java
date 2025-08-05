package com.sun.org.apache.xml.internal.dtm;

import com.sun.org.apache.xml.internal.utils.XMLString;
import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/DTM.class */
public interface DTM {
    public static final int NULL = -1;
    public static final short ROOT_NODE = 0;
    public static final short ELEMENT_NODE = 1;
    public static final short ATTRIBUTE_NODE = 2;
    public static final short TEXT_NODE = 3;
    public static final short CDATA_SECTION_NODE = 4;
    public static final short ENTITY_REFERENCE_NODE = 5;
    public static final short ENTITY_NODE = 6;
    public static final short PROCESSING_INSTRUCTION_NODE = 7;
    public static final short COMMENT_NODE = 8;
    public static final short DOCUMENT_NODE = 9;
    public static final short DOCUMENT_TYPE_NODE = 10;
    public static final short DOCUMENT_FRAGMENT_NODE = 11;
    public static final short NOTATION_NODE = 12;
    public static final short NAMESPACE_NODE = 13;
    public static final short NTYPES = 14;

    void setFeature(String str, boolean z2);

    void setProperty(String str, Object obj);

    DTMAxisTraverser getAxisTraverser(int i2);

    DTMAxisIterator getAxisIterator(int i2);

    DTMAxisIterator getTypedAxisIterator(int i2, int i3);

    boolean hasChildNodes(int i2);

    int getFirstChild(int i2);

    int getLastChild(int i2);

    int getAttributeNode(int i2, String str, String str2);

    int getFirstAttribute(int i2);

    int getFirstNamespaceNode(int i2, boolean z2);

    int getNextSibling(int i2);

    int getPreviousSibling(int i2);

    int getNextAttribute(int i2);

    int getNextNamespaceNode(int i2, int i3, boolean z2);

    int getParent(int i2);

    int getDocument();

    int getOwnerDocument(int i2);

    int getDocumentRoot(int i2);

    XMLString getStringValue(int i2);

    int getStringValueChunkCount(int i2);

    char[] getStringValueChunk(int i2, int i3, int[] iArr);

    int getExpandedTypeID(int i2);

    int getExpandedTypeID(String str, String str2, int i2);

    String getLocalNameFromExpandedNameID(int i2);

    String getNamespaceFromExpandedNameID(int i2);

    String getNodeName(int i2);

    String getNodeNameX(int i2);

    String getLocalName(int i2);

    String getPrefix(int i2);

    String getNamespaceURI(int i2);

    String getNodeValue(int i2);

    short getNodeType(int i2);

    short getLevel(int i2);

    boolean isSupported(String str, String str2);

    String getDocumentBaseURI();

    void setDocumentBaseURI(String str);

    String getDocumentSystemIdentifier(int i2);

    String getDocumentEncoding(int i2);

    String getDocumentStandalone(int i2);

    String getDocumentVersion(int i2);

    boolean getDocumentAllDeclarationsProcessed();

    String getDocumentTypeDeclarationSystemIdentifier();

    String getDocumentTypeDeclarationPublicIdentifier();

    int getElementById(String str);

    String getUnparsedEntityURI(String str);

    boolean supportsPreStripping();

    boolean isNodeAfter(int i2, int i3);

    boolean isCharacterElementContentWhitespace(int i2);

    boolean isDocumentAllDeclarationsProcessed(int i2);

    boolean isAttributeSpecified(int i2);

    void dispatchCharactersEvents(int i2, ContentHandler contentHandler, boolean z2) throws SAXException;

    void dispatchToEvents(int i2, ContentHandler contentHandler) throws SAXException;

    Node getNode(int i2);

    boolean needsTwoThreads();

    ContentHandler getContentHandler();

    LexicalHandler getLexicalHandler();

    EntityResolver getEntityResolver();

    DTDHandler getDTDHandler();

    ErrorHandler getErrorHandler();

    DeclHandler getDeclHandler();

    void appendChild(int i2, boolean z2, boolean z3);

    void appendTextChild(String str);

    SourceLocator getSourceLocatorFor(int i2);

    void documentRegistration();

    void documentRelease();

    void migrateTo(DTMManager dTMManager);
}
