package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315OmitComments;
import com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityRuntimeException;
import com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/XMLSignatureInput.class */
public class XMLSignatureInput {
    private InputStream inputOctetStreamProxy;
    private Set<Node> inputNodeSet;
    private Node subNode;
    private Node excludeNode;
    private byte[] bytes;
    private boolean secureValidation;
    private String mimeType;
    private String sourceURI;
    private OutputStream outputStream;
    private String preCalculatedDigest;
    private boolean excludeComments = false;
    private boolean isNodeSet = false;
    private List<NodeFilter> nodeFilters = new ArrayList();
    private boolean needsToBeExpanded = false;

    public XMLSignatureInput(byte[] bArr) {
        this.bytes = bArr;
    }

    public XMLSignatureInput(InputStream inputStream) {
        this.inputOctetStreamProxy = inputStream;
    }

    public XMLSignatureInput(Node node) {
        this.subNode = node;
    }

    public XMLSignatureInput(Set<Node> set) {
        this.inputNodeSet = set;
    }

    public XMLSignatureInput(String str) {
        this.preCalculatedDigest = str;
    }

    public boolean isNeedsToBeExpanded() {
        return this.needsToBeExpanded;
    }

    public void setNeedsToBeExpanded(boolean z2) {
        this.needsToBeExpanded = z2;
    }

    public Set<Node> getNodeSet() throws CanonicalizationException, ParserConfigurationException, SAXException, IOException {
        return getNodeSet(false);
    }

    public Set<Node> getInputNodeSet() {
        return this.inputNodeSet;
    }

    public Set<Node> getNodeSet(boolean z2) throws CanonicalizationException, ParserConfigurationException, DOMException, SAXException, IOException {
        if (this.inputNodeSet != null) {
            return this.inputNodeSet;
        }
        if (this.inputOctetStreamProxy == null && this.subNode != null) {
            if (z2) {
                XMLUtils.circumventBug2650(XMLUtils.getOwnerDocument(this.subNode));
            }
            this.inputNodeSet = new LinkedHashSet();
            XMLUtils.getSet(this.subNode, this.inputNodeSet, this.excludeNode, this.excludeComments);
            return this.inputNodeSet;
        }
        if (isOctetStream()) {
            convertToNodes();
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            XMLUtils.getSet(this.subNode, linkedHashSet, null, false);
            return linkedHashSet;
        }
        throw new RuntimeException("getNodeSet() called but no input data present");
    }

    public InputStream getOctetStream() throws IOException {
        if (this.inputOctetStreamProxy != null) {
            return this.inputOctetStreamProxy;
        }
        if (this.bytes != null) {
            this.inputOctetStreamProxy = new ByteArrayInputStream(this.bytes);
            return this.inputOctetStreamProxy;
        }
        return null;
    }

    public InputStream getOctetStreamReal() {
        return this.inputOctetStreamProxy;
    }

    public byte[] getBytes() throws CanonicalizationException, IOException {
        byte[] bytesFromInputStream = getBytesFromInputStream();
        if (bytesFromInputStream != null) {
            return bytesFromInputStream;
        }
        this.bytes = new Canonicalizer20010315OmitComments().engineCanonicalize(this);
        return this.bytes;
    }

    public boolean isNodeSet() {
        return (this.inputOctetStreamProxy == null && this.inputNodeSet != null) || this.isNodeSet;
    }

    public boolean isElement() {
        return this.inputOctetStreamProxy == null && this.subNode != null && this.inputNodeSet == null && !this.isNodeSet;
    }

    public boolean isOctetStream() {
        return !(this.inputOctetStreamProxy == null && this.bytes == null) && this.inputNodeSet == null && this.subNode == null;
    }

    public boolean isOutputStreamSet() {
        return this.outputStream != null;
    }

    public boolean isByteArray() {
        return this.bytes != null && this.inputNodeSet == null && this.subNode == null;
    }

    public boolean isPreCalculatedDigest() {
        return this.preCalculatedDigest != null;
    }

    public boolean isInitialized() {
        return isOctetStream() || isNodeSet();
    }

    public String getMIMEType() {
        return this.mimeType;
    }

    public void setMIMEType(String str) {
        this.mimeType = str;
    }

    public String getSourceURI() {
        return this.sourceURI;
    }

    public void setSourceURI(String str) {
        this.sourceURI = str;
    }

    public String toString() {
        if (isNodeSet()) {
            return "XMLSignatureInput/NodeSet/" + this.inputNodeSet.size() + " nodes/" + getSourceURI();
        }
        if (isElement()) {
            return "XMLSignatureInput/Element/" + ((Object) this.subNode) + " exclude " + ((Object) this.excludeNode) + " comments:" + this.excludeComments + "/" + getSourceURI();
        }
        try {
            return "XMLSignatureInput/OctetStream/" + getBytes().length + " octets/" + getSourceURI();
        } catch (CanonicalizationException e2) {
            return "XMLSignatureInput/OctetStream//" + getSourceURI();
        } catch (IOException e3) {
            return "XMLSignatureInput/OctetStream//" + getSourceURI();
        }
    }

    public String getHTMLRepresentation() throws XMLSignatureException {
        return new XMLSignatureInputDebugger(this).getHTMLRepresentation();
    }

    public String getHTMLRepresentation(Set<String> set) throws XMLSignatureException {
        return new XMLSignatureInputDebugger(this, set).getHTMLRepresentation();
    }

    public Node getExcludeNode() {
        return this.excludeNode;
    }

    public void setExcludeNode(Node node) {
        this.excludeNode = node;
    }

    public Node getSubNode() {
        return this.subNode;
    }

    public boolean isExcludeComments() {
        return this.excludeComments;
    }

    public void setExcludeComments(boolean z2) {
        this.excludeComments = z2;
    }

    public void updateOutputStream(OutputStream outputStream) throws CanonicalizationException, IOException {
        updateOutputStream(outputStream, false);
    }

    public void updateOutputStream(OutputStream outputStream, boolean z2) throws CanonicalizationException, IOException {
        CanonicalizerBase canonicalizer20010315OmitComments;
        if (outputStream == this.outputStream) {
            return;
        }
        if (this.bytes != null) {
            outputStream.write(this.bytes);
            return;
        }
        if (this.inputOctetStreamProxy == null) {
            if (z2) {
                canonicalizer20010315OmitComments = new Canonicalizer11_OmitComments();
            } else {
                canonicalizer20010315OmitComments = new Canonicalizer20010315OmitComments();
            }
            canonicalizer20010315OmitComments.setWriter(outputStream);
            canonicalizer20010315OmitComments.engineCanonicalize(this);
            return;
        }
        byte[] bArr = new byte[4096];
        while (true) {
            try {
                int i2 = this.inputOctetStreamProxy.read(bArr);
                if (i2 != -1) {
                    outputStream.write(bArr, 0, i2);
                } else {
                    return;
                }
            } catch (IOException e2) {
                this.inputOctetStreamProxy.close();
                throw e2;
            }
        }
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    private byte[] getBytesFromInputStream() throws IOException {
        if (this.bytes != null) {
            return this.bytes;
        }
        if (this.inputOctetStreamProxy == null) {
            return null;
        }
        try {
            this.bytes = JavaUtils.getBytesFromStream(this.inputOctetStreamProxy);
            return this.bytes;
        } finally {
            this.inputOctetStreamProxy.close();
        }
    }

    public void addNodeFilter(NodeFilter nodeFilter) {
        if (isOctetStream()) {
            try {
                convertToNodes();
            } catch (Exception e2) {
                throw new XMLSecurityRuntimeException("signature.XMLSignatureInput.nodesetReference");
            }
        }
        this.nodeFilters.add(nodeFilter);
    }

    public List<NodeFilter> getNodeFilters() {
        return this.nodeFilters;
    }

    public void setNodeSet(boolean z2) {
        this.isNodeSet = z2;
    }

    void convertToNodes() throws CanonicalizationException, ParserConfigurationException, SAXException, IOException {
        DocumentBuilder documentBuilderCreateDocumentBuilder = XMLUtils.createDocumentBuilder(false, this.secureValidation);
        try {
            try {
                documentBuilderCreateDocumentBuilder.setErrorHandler(new IgnoreAllErrorHandler());
                this.subNode = documentBuilderCreateDocumentBuilder.parse(getOctetStream());
                if (this.inputOctetStreamProxy != null) {
                    this.inputOctetStreamProxy.close();
                }
                this.inputOctetStreamProxy = null;
                this.bytes = null;
            } catch (SAXException e2) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Throwable th = null;
                try {
                    try {
                        byteArrayOutputStream.write("<container>".getBytes(StandardCharsets.UTF_8));
                        byteArrayOutputStream.write(getBytes());
                        byteArrayOutputStream.write("</container>".getBytes(StandardCharsets.UTF_8));
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        if (byteArrayOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                byteArrayOutputStream.close();
                            }
                        }
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                        Throwable th3 = null;
                        try {
                            try {
                                this.subNode = documentBuilderCreateDocumentBuilder.parse(byteArrayInputStream).getDocumentElement().getFirstChild().getFirstChild();
                                if (byteArrayInputStream != null) {
                                    if (0 != 0) {
                                        try {
                                            byteArrayInputStream.close();
                                        } catch (Throwable th4) {
                                            th3.addSuppressed(th4);
                                        }
                                    } else {
                                        byteArrayInputStream.close();
                                    }
                                }
                                if (this.inputOctetStreamProxy != null) {
                                    this.inputOctetStreamProxy.close();
                                }
                                this.inputOctetStreamProxy = null;
                                this.bytes = null;
                            } finally {
                            }
                        } catch (Throwable th5) {
                            th3 = th5;
                            throw th5;
                        }
                    } finally {
                    }
                } catch (Throwable th6) {
                    th = th6;
                    throw th6;
                }
            }
        } catch (Throwable th7) {
            if (this.inputOctetStreamProxy != null) {
                this.inputOctetStreamProxy.close();
            }
            this.inputOctetStreamProxy = null;
            this.bytes = null;
            throw th7;
        }
    }

    public boolean isSecureValidation() {
        return this.secureValidation;
    }

    public void setSecureValidation(boolean z2) {
        this.secureValidation = z2;
    }

    public String getPreCalculatedDigest() {
        return this.preCalculatedDigest;
    }
}
