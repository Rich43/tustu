package com.sun.org.apache.xml.internal.security.c14n;

import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_WithComments;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclOmitComments;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclWithComments;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315OmitComments;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315WithComments;
import com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerPhysical;
import com.sun.org.apache.xml.internal.security.exceptions.AlgorithmAlreadyRegisteredException;
import com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/Canonicalizer.class */
public class Canonicalizer {
    public static final String XPATH_C14N_WITH_COMMENTS_SINGLE_NODE = "(.//. | .//@* | .//namespace::*)";
    public static final String ALGO_ID_C14N_OMIT_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
    public static final String ALGO_ID_C14N_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
    public static final String ALGO_ID_C14N_EXCL_OMIT_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#";
    public static final String ALGO_ID_C14N_EXCL_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
    public static final String ALGO_ID_C14N11_OMIT_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11";
    public static final String ALGO_ID_C14N11_WITH_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11#WithComments";
    public static final String ALGO_ID_C14N_PHYSICAL = "http://santuario.apache.org/c14n/physical";
    private final CanonicalizerSpi canonicalizerSpi;
    private boolean secureValidation;
    public static final String ENCODING = StandardCharsets.UTF_8.name();
    private static Map<String, Class<? extends CanonicalizerSpi>> canonicalizerHash = new ConcurrentHashMap();

    private Canonicalizer(String str) throws InvalidCanonicalizerException {
        try {
            this.canonicalizerSpi = canonicalizerHash.get(str).newInstance();
            this.canonicalizerSpi.reset = true;
        } catch (Exception e2) {
            throw new InvalidCanonicalizerException(e2, "signature.Canonicalizer.UnknownCanonicalizer", new Object[]{str});
        }
    }

    public static final Canonicalizer getInstance(String str) throws InvalidCanonicalizerException {
        return new Canonicalizer(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void register(String str, String str2) throws AlgorithmAlreadyRegisteredException, ClassNotFoundException {
        JavaUtils.checkRegisterPermission();
        Class<? extends CanonicalizerSpi> cls = canonicalizerHash.get(str);
        if (cls != null) {
            throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", new Object[]{str, cls});
        }
        canonicalizerHash.put(str, ClassLoaderUtils.loadClass(str2, Canonicalizer.class));
    }

    public static void register(String str, Class<? extends CanonicalizerSpi> cls) throws AlgorithmAlreadyRegisteredException, ClassNotFoundException {
        JavaUtils.checkRegisterPermission();
        Class<? extends CanonicalizerSpi> cls2 = canonicalizerHash.get(str);
        if (cls2 != null) {
            throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", new Object[]{str, cls2});
        }
        canonicalizerHash.put(str, cls);
    }

    public static void registerDefaultAlgorithms() {
        canonicalizerHash.put("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", Canonicalizer20010315OmitComments.class);
        canonicalizerHash.put("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", Canonicalizer20010315WithComments.class);
        canonicalizerHash.put("http://www.w3.org/2001/10/xml-exc-c14n#", Canonicalizer20010315ExclOmitComments.class);
        canonicalizerHash.put("http://www.w3.org/2001/10/xml-exc-c14n#WithComments", Canonicalizer20010315ExclWithComments.class);
        canonicalizerHash.put("http://www.w3.org/2006/12/xml-c14n11", Canonicalizer11_OmitComments.class);
        canonicalizerHash.put("http://www.w3.org/2006/12/xml-c14n11#WithComments", Canonicalizer11_WithComments.class);
        canonicalizerHash.put(ALGO_ID_C14N_PHYSICAL, CanonicalizerPhysical.class);
    }

    public final String getURI() {
        return this.canonicalizerSpi.engineGetURI();
    }

    public boolean getIncludeComments() {
        return this.canonicalizerSpi.engineGetIncludeComments();
    }

    public byte[] canonicalize(byte[] bArr) throws CanonicalizationException, ParserConfigurationException, SAXException, IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Throwable th = null;
        try {
            InputSource inputSource = new InputSource(byteArrayInputStream);
            DocumentBuilder documentBuilderCreateDocumentBuilder = XMLUtils.createDocumentBuilder(true, this.secureValidation);
            documentBuilderCreateDocumentBuilder.setErrorHandler(new IgnoreAllErrorHandler());
            Document document = documentBuilderCreateDocumentBuilder.parse(inputSource);
            if (byteArrayInputStream != null) {
                if (0 != 0) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    byteArrayInputStream.close();
                }
            }
            return canonicalizeSubtree(document);
        } catch (Throwable th3) {
            if (byteArrayInputStream != null) {
                if (0 != 0) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    byteArrayInputStream.close();
                }
            }
            throw th3;
        }
    }

    public byte[] canonicalizeSubtree(Node node) throws CanonicalizationException {
        this.canonicalizerSpi.secureValidation = this.secureValidation;
        return this.canonicalizerSpi.engineCanonicalizeSubTree(node);
    }

    public byte[] canonicalizeSubtree(Node node, String str) throws CanonicalizationException {
        this.canonicalizerSpi.secureValidation = this.secureValidation;
        return this.canonicalizerSpi.engineCanonicalizeSubTree(node, str);
    }

    public byte[] canonicalizeSubtree(Node node, String str, boolean z2) throws CanonicalizationException {
        this.canonicalizerSpi.secureValidation = this.secureValidation;
        return this.canonicalizerSpi.engineCanonicalizeSubTree(node, str, z2);
    }

    public byte[] canonicalizeXPathNodeSet(NodeList nodeList) throws CanonicalizationException {
        this.canonicalizerSpi.secureValidation = this.secureValidation;
        return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(nodeList);
    }

    public byte[] canonicalizeXPathNodeSet(NodeList nodeList, String str) throws CanonicalizationException {
        this.canonicalizerSpi.secureValidation = this.secureValidation;
        return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(nodeList, str);
    }

    public byte[] canonicalizeXPathNodeSet(Set<Node> set) throws CanonicalizationException {
        this.canonicalizerSpi.secureValidation = this.secureValidation;
        return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(set);
    }

    public byte[] canonicalizeXPathNodeSet(Set<Node> set, String str) throws CanonicalizationException {
        this.canonicalizerSpi.secureValidation = this.secureValidation;
        return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(set, str);
    }

    public void setWriter(OutputStream outputStream) {
        this.canonicalizerSpi.setWriter(outputStream);
    }

    public String getImplementingCanonicalizerClass() {
        return this.canonicalizerSpi.getClass().getName();
    }

    public void notReset() {
        this.canonicalizerSpi.reset = false;
    }

    public boolean isSecureValidation() {
        return this.secureValidation;
    }

    public void setSecureValidation(boolean z2) {
        this.secureValidation = z2;
    }
}
