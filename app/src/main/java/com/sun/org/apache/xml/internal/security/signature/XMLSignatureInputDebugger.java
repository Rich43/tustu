package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Set;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/XMLSignatureInputDebugger.class */
public class XMLSignatureInputDebugger {
    private Set<Node> xpathNodeSet;
    private Set<String> inclusiveNamespaces;
    private Writer writer;
    static final String HTMLPrefix = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<title>Caninical XML node set</title>\n<style type=\"text/css\">\n<!-- \n.INCLUDED { \n   color: #000000; \n   background-color: \n   #FFFFFF; \n   font-weight: bold; } \n.EXCLUDED { \n   color: #666666; \n   background-color: \n   #999999; } \n.INCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #FFFFFF; \n   font-weight: bold; \n   font-style: italic; } \n.EXCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #999999; \n   font-style: italic; } \n--> \n</style> \n</head>\n<body bgcolor=\"#999999\">\n<h1>Explanation of the output</h1>\n<p>The following text contains the nodeset of the given Reference before it is canonicalized. There exist four different styles to indicate how a given node is treated.</p>\n<ul>\n<li class=\"INCLUDED\">A node which is in the node set is labeled using the INCLUDED style.</li>\n<li class=\"EXCLUDED\">A node which is <em>NOT</em> in the node set is labeled EXCLUDED style.</li>\n<li class=\"INCLUDEDINCLUSIVENAMESPACE\">A namespace which is in the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n<li class=\"EXCLUDEDINCLUSIVENAMESPACE\">A namespace which is in NOT the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n</ul>\n<h1>Output</h1>\n<pre>\n";
    static final String HTMLSuffix = "</pre></body></html>";
    static final String HTMLExcludePrefix = "<span class=\"EXCLUDED\">";
    static final String HTMLIncludePrefix = "<span class=\"INCLUDED\">";
    static final String HTMLIncludeOrExcludeSuffix = "</span>";
    static final String HTMLIncludedInclusiveNamespacePrefix = "<span class=\"INCLUDEDINCLUSIVENAMESPACE\">";
    static final String HTMLExcludedInclusiveNamespacePrefix = "<span class=\"EXCLUDEDINCLUSIVENAMESPACE\">";
    private static final int NODE_BEFORE_DOCUMENT_ELEMENT = -1;
    private static final int NODE_NOT_BEFORE_OR_AFTER_DOCUMENT_ELEMENT = 0;
    private static final int NODE_AFTER_DOCUMENT_ELEMENT = 1;
    static final AttrCompare ATTR_COMPARE = new AttrCompare();

    public XMLSignatureInputDebugger(XMLSignatureInput xMLSignatureInput) {
        if (!xMLSignatureInput.isNodeSet()) {
            this.xpathNodeSet = null;
        } else {
            this.xpathNodeSet = xMLSignatureInput.getInputNodeSet();
        }
    }

    public XMLSignatureInputDebugger(XMLSignatureInput xMLSignatureInput, Set<String> set) {
        this(xMLSignatureInput);
        this.inclusiveNamespaces = set;
    }

    public String getHTMLRepresentation() throws XMLSignatureException {
        if (this.xpathNodeSet == null || this.xpathNodeSet.isEmpty()) {
            return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<title>Caninical XML node set</title>\n<style type=\"text/css\">\n<!-- \n.INCLUDED { \n   color: #000000; \n   background-color: \n   #FFFFFF; \n   font-weight: bold; } \n.EXCLUDED { \n   color: #666666; \n   background-color: \n   #999999; } \n.INCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #FFFFFF; \n   font-weight: bold; \n   font-style: italic; } \n.EXCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #999999; \n   font-style: italic; } \n--> \n</style> \n</head>\n<body bgcolor=\"#999999\">\n<h1>Explanation of the output</h1>\n<p>The following text contains the nodeset of the given Reference before it is canonicalized. There exist four different styles to indicate how a given node is treated.</p>\n<ul>\n<li class=\"INCLUDED\">A node which is in the node set is labeled using the INCLUDED style.</li>\n<li class=\"EXCLUDED\">A node which is <em>NOT</em> in the node set is labeled EXCLUDED style.</li>\n<li class=\"INCLUDEDINCLUSIVENAMESPACE\">A namespace which is in the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n<li class=\"EXCLUDEDINCLUSIVENAMESPACE\">A namespace which is in NOT the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n</ul>\n<h1>Output</h1>\n<pre>\n<blink>no node set, sorry</blink></pre></body></html>";
        }
        Document ownerDocument = XMLUtils.getOwnerDocument(this.xpathNodeSet.iterator().next());
        try {
            try {
                this.writer = new StringWriter();
                canonicalizeXPathNodeSet(ownerDocument);
                this.writer.close();
                String string = this.writer.toString();
                this.xpathNodeSet = null;
                this.writer = null;
                return string;
            } catch (IOException e2) {
                throw new XMLSignatureException(e2);
            }
        } catch (Throwable th) {
            this.xpathNodeSet = null;
            this.writer = null;
            throw th;
        }
    }

    private void canonicalizeXPathNodeSet(Node node) throws IOException, XMLSignatureException {
        short nodeType = node.getNodeType();
        switch (nodeType) {
            case 1:
                Element element = (Element) node;
                if (this.xpathNodeSet.contains(node)) {
                    this.writer.write(HTMLIncludePrefix);
                } else {
                    this.writer.write(HTMLExcludePrefix);
                }
                this.writer.write(SerializerConstants.ENTITY_LT);
                this.writer.write(element.getTagName());
                this.writer.write(HTMLIncludeOrExcludeSuffix);
                NamedNodeMap attributes = element.getAttributes();
                int length = attributes.getLength();
                Attr[] attrArr = new Attr[length];
                for (int i2 = 0; i2 < length; i2++) {
                    attrArr[i2] = (Attr) attributes.item(i2);
                }
                Arrays.sort(attrArr, ATTR_COMPARE);
                for (int i3 = 0; i3 < length; i3++) {
                    Attr attr = attrArr[i3];
                    boolean zContains = this.xpathNodeSet.contains(attr);
                    boolean zContains2 = this.inclusiveNamespaces.contains(attr.getName());
                    if (zContains) {
                        if (zContains2) {
                            this.writer.write(HTMLIncludedInclusiveNamespacePrefix);
                        } else {
                            this.writer.write(HTMLIncludePrefix);
                        }
                    } else if (zContains2) {
                        this.writer.write(HTMLExcludedInclusiveNamespacePrefix);
                    } else {
                        this.writer.write(HTMLExcludePrefix);
                    }
                    outputAttrToWriter(attr.getNodeName(), attr.getNodeValue());
                    this.writer.write(HTMLIncludeOrExcludeSuffix);
                }
                if (this.xpathNodeSet.contains(node)) {
                    this.writer.write(HTMLIncludePrefix);
                } else {
                    this.writer.write(HTMLExcludePrefix);
                }
                this.writer.write(SerializerConstants.ENTITY_GT);
                this.writer.write(HTMLIncludeOrExcludeSuffix);
                Node firstChild = node.getFirstChild();
                while (true) {
                    Node node2 = firstChild;
                    if (node2 != null) {
                        canonicalizeXPathNodeSet(node2);
                        firstChild = node2.getNextSibling();
                    } else {
                        if (this.xpathNodeSet.contains(node)) {
                            this.writer.write(HTMLIncludePrefix);
                        } else {
                            this.writer.write(HTMLExcludePrefix);
                        }
                        this.writer.write("&lt;/");
                        this.writer.write(element.getTagName());
                        this.writer.write(SerializerConstants.ENTITY_GT);
                        this.writer.write(HTMLIncludeOrExcludeSuffix);
                        return;
                    }
                }
            case 2:
            case 6:
            case 11:
            case 12:
                throw new XMLSignatureException(Constants.ELEMNAME_EMPTY_STRING, new Object[]{"An incorrect node was provided for c14n: " + ((int) nodeType)});
            case 3:
            case 4:
                if (this.xpathNodeSet.contains(node)) {
                    this.writer.write(HTMLIncludePrefix);
                } else {
                    this.writer.write(HTMLExcludePrefix);
                }
                outputTextToWriter(node.getNodeValue());
                Node nextSibling = node.getNextSibling();
                while (true) {
                    Node node3 = nextSibling;
                    if (node3 != null && (node3.getNodeType() == 3 || node3.getNodeType() == 4)) {
                        outputTextToWriter(node3.getNodeValue());
                        nextSibling = node3.getNextSibling();
                    }
                }
                this.writer.write(HTMLIncludeOrExcludeSuffix);
                return;
            case 5:
            case 10:
            default:
                return;
            case 7:
                if (this.xpathNodeSet.contains(node)) {
                    this.writer.write(HTMLIncludePrefix);
                } else {
                    this.writer.write(HTMLExcludePrefix);
                }
                int positionRelativeToDocumentElement = getPositionRelativeToDocumentElement(node);
                if (positionRelativeToDocumentElement == 1) {
                    this.writer.write("\n");
                }
                outputPItoWriter((ProcessingInstruction) node);
                if (positionRelativeToDocumentElement == -1) {
                    this.writer.write("\n");
                }
                this.writer.write(HTMLIncludeOrExcludeSuffix);
                return;
            case 8:
                if (this.xpathNodeSet.contains(node)) {
                    this.writer.write(HTMLIncludePrefix);
                } else {
                    this.writer.write(HTMLExcludePrefix);
                }
                int positionRelativeToDocumentElement2 = getPositionRelativeToDocumentElement(node);
                if (positionRelativeToDocumentElement2 == 1) {
                    this.writer.write("\n");
                }
                outputCommentToWriter((Comment) node);
                if (positionRelativeToDocumentElement2 == -1) {
                    this.writer.write("\n");
                }
                this.writer.write(HTMLIncludeOrExcludeSuffix);
                return;
            case 9:
                this.writer.write(HTMLPrefix);
                Node firstChild2 = node.getFirstChild();
                while (true) {
                    Node node4 = firstChild2;
                    if (node4 != null) {
                        canonicalizeXPathNodeSet(node4);
                        firstChild2 = node4.getNextSibling();
                    } else {
                        this.writer.write(HTMLSuffix);
                        return;
                    }
                }
        }
    }

    private int getPositionRelativeToDocumentElement(Node node) {
        Document ownerDocument;
        Element documentElement;
        if (node == null || node.getParentNode() != (ownerDocument = node.getOwnerDocument()) || (documentElement = ownerDocument.getDocumentElement()) == null || documentElement == node) {
            return 0;
        }
        Node nextSibling = node;
        while (true) {
            Node node2 = nextSibling;
            if (node2 != null) {
                if (node2 != documentElement) {
                    nextSibling = node2.getNextSibling();
                } else {
                    return -1;
                }
            } else {
                return 1;
            }
        }
    }

    private void outputAttrToWriter(String str, String str2) throws IOException {
        this.writer.write(" ");
        this.writer.write(str);
        this.writer.write("=\"");
        int length = str2.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str2.charAt(i2);
            switch (cCharAt) {
                case '\t':
                    this.writer.write("&amp;#x9;");
                    break;
                case '\n':
                    this.writer.write("&amp;#xA;");
                    break;
                case '\r':
                    this.writer.write("&amp;#xD;");
                    break;
                case '\"':
                    this.writer.write("&amp;quot;");
                    break;
                case '&':
                    this.writer.write("&amp;amp;");
                    break;
                case '<':
                    this.writer.write("&amp;lt;");
                    break;
                default:
                    this.writer.write(cCharAt);
                    break;
            }
        }
        this.writer.write(PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    private void outputPItoWriter(ProcessingInstruction processingInstruction) throws IOException {
        if (processingInstruction == null) {
            return;
        }
        this.writer.write("&lt;?");
        String target = processingInstruction.getTarget();
        int length = target.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = target.charAt(i2);
            switch (cCharAt) {
                case '\n':
                    this.writer.write("&para;\n");
                    break;
                case '\r':
                    this.writer.write("&amp;#xD;");
                    break;
                case ' ':
                    this.writer.write("&middot;");
                    break;
                default:
                    this.writer.write(cCharAt);
                    break;
            }
        }
        String data = processingInstruction.getData();
        int length2 = data.length();
        if (length2 > 0) {
            this.writer.write(" ");
            for (int i3 = 0; i3 < length2; i3++) {
                char cCharAt2 = data.charAt(i3);
                switch (cCharAt2) {
                    case '\r':
                        this.writer.write("&amp;#xD;");
                        break;
                    default:
                        this.writer.write(cCharAt2);
                        break;
                }
            }
        }
        this.writer.write("?&gt;");
    }

    private void outputCommentToWriter(Comment comment) throws IOException {
        if (comment == null) {
            return;
        }
        this.writer.write("&lt;!--");
        String data = comment.getData();
        int length = data.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = data.charAt(i2);
            switch (cCharAt) {
                case '\n':
                    this.writer.write("&para;\n");
                    break;
                case '\r':
                    this.writer.write("&amp;#xD;");
                    break;
                case ' ':
                    this.writer.write("&middot;");
                    break;
                default:
                    this.writer.write(cCharAt);
                    break;
            }
        }
        this.writer.write("--&gt;");
    }

    private void outputTextToWriter(String str) throws IOException {
        if (str == null) {
            return;
        }
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            switch (cCharAt) {
                case '\n':
                    this.writer.write("&para;\n");
                    break;
                case '\r':
                    this.writer.write("&amp;#xD;");
                    break;
                case ' ':
                    this.writer.write("&middot;");
                    break;
                case '&':
                    this.writer.write("&amp;amp;");
                    break;
                case '<':
                    this.writer.write("&amp;lt;");
                    break;
                case '>':
                    this.writer.write("&amp;gt;");
                    break;
                default:
                    this.writer.write(cCharAt);
                    break;
            }
        }
    }
}
