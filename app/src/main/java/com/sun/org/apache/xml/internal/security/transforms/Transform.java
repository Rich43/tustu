package com.sun.org.apache.xml.internal.security.transforms;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.exceptions.AlgorithmAlreadyRegisteredException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformBase64Decode;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N11;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N11_WithComments;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NExclusive;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NExclusiveWithComments;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NWithComments;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformEnvelopedSignature;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXPath;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXPath2Filter;
import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXSLT;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/Transform.class */
public final class Transform extends SignatureElementProxy {
    private static final Logger LOG = LoggerFactory.getLogger(Transform.class);
    private static Map<String, Class<? extends TransformSpi>> transformSpiHash = new ConcurrentHashMap();
    private final TransformSpi transformSpi;
    private boolean secureValidation;

    public Transform(Document document, String str) throws InvalidTransformException {
        this(document, str, (NodeList) null);
    }

    public Transform(Document document, String str, Element element) throws InvalidTransformException, IllegalArgumentException {
        super(document);
        HelperNodeList helperNodeList = null;
        if (element != null) {
            helperNodeList = new HelperNodeList();
            XMLUtils.addReturnToElement(document, helperNodeList);
            helperNodeList.appendChild(element);
            XMLUtils.addReturnToElement(document, helperNodeList);
        }
        this.transformSpi = initializeTransform(str, helperNodeList);
    }

    public Transform(Document document, String str, NodeList nodeList) throws InvalidTransformException {
        super(document);
        this.transformSpi = initializeTransform(str, nodeList);
    }

    public Transform(Element element, String str) throws DOMException, XMLSecurityException {
        super(element, str);
        String attributeNS = element.getAttributeNS(null, Constants._ATT_ALGORITHM);
        if (attributeNS == null || attributeNS.length() == 0) {
            throw new TransformationException("xml.WrongContent", new Object[]{Constants._ATT_ALGORITHM, Constants._TAG_TRANSFORM});
        }
        Class<? extends TransformSpi> cls = transformSpiHash.get(attributeNS);
        if (cls == null) {
            throw new InvalidTransformException("signature.Transform.UnknownTransform", new Object[]{attributeNS});
        }
        try {
            this.transformSpi = cls.newInstance();
        } catch (IllegalAccessException e2) {
            throw new InvalidTransformException(e2, "signature.Transform.UnknownTransform", new Object[]{attributeNS});
        } catch (InstantiationException e3) {
            throw new InvalidTransformException(e3, "signature.Transform.UnknownTransform", new Object[]{attributeNS});
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void register(String str, String str2) throws AlgorithmAlreadyRegisteredException, ClassNotFoundException, InvalidTransformException {
        JavaUtils.checkRegisterPermission();
        Class<? extends TransformSpi> cls = transformSpiHash.get(str);
        if (cls != null) {
            throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", new Object[]{str, cls});
        }
        transformSpiHash.put(str, ClassLoaderUtils.loadClass(str2, Transform.class));
    }

    public static void register(String str, Class<? extends TransformSpi> cls) throws AlgorithmAlreadyRegisteredException {
        JavaUtils.checkRegisterPermission();
        Class<? extends TransformSpi> cls2 = transformSpiHash.get(str);
        if (cls2 != null) {
            throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", new Object[]{str, cls2});
        }
        transformSpiHash.put(str, cls);
    }

    public static void registerDefaultAlgorithms() {
        transformSpiHash.put("http://www.w3.org/2000/09/xmldsig#base64", TransformBase64Decode.class);
        transformSpiHash.put("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", TransformC14N.class);
        transformSpiHash.put("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", TransformC14NWithComments.class);
        transformSpiHash.put("http://www.w3.org/2006/12/xml-c14n11", TransformC14N11.class);
        transformSpiHash.put("http://www.w3.org/2006/12/xml-c14n11#WithComments", TransformC14N11_WithComments.class);
        transformSpiHash.put("http://www.w3.org/2001/10/xml-exc-c14n#", TransformC14NExclusive.class);
        transformSpiHash.put("http://www.w3.org/2001/10/xml-exc-c14n#WithComments", TransformC14NExclusiveWithComments.class);
        transformSpiHash.put("http://www.w3.org/TR/1999/REC-xpath-19991116", TransformXPath.class);
        transformSpiHash.put("http://www.w3.org/2000/09/xmldsig#enveloped-signature", TransformEnvelopedSignature.class);
        transformSpiHash.put("http://www.w3.org/TR/1999/REC-xslt-19991116", TransformXSLT.class);
        transformSpiHash.put("http://www.w3.org/2002/06/xmldsig-filter2", TransformXPath2Filter.class);
    }

    public String getURI() {
        return getLocalAttribute(Constants._ATT_ALGORITHM);
    }

    public XMLSignatureInput performTransform(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, IOException, InvalidCanonicalizerException, TransformationException {
        return performTransform(xMLSignatureInput, null);
    }

    public XMLSignatureInput performTransform(XMLSignatureInput xMLSignatureInput, OutputStream outputStream) throws CanonicalizationException, IOException, InvalidCanonicalizerException, TransformationException {
        try {
            this.transformSpi.secureValidation = this.secureValidation;
            return this.transformSpi.enginePerformTransform(xMLSignatureInput, outputStream, this);
        } catch (ParserConfigurationException e2) {
            throw new CanonicalizationException(e2, "signature.Transform.ErrorDuringTransform", new Object[]{getURI(), "ParserConfigurationException"});
        } catch (SAXException e3) {
            throw new CanonicalizationException(e3, "signature.Transform.ErrorDuringTransform", new Object[]{getURI(), "SAXException"});
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_TRANSFORM;
    }

    private TransformSpi initializeTransform(String str, NodeList nodeList) throws InvalidTransformException {
        setLocalAttribute(Constants._ATT_ALGORITHM, str);
        Class<? extends TransformSpi> cls = transformSpiHash.get(str);
        if (cls == null) {
            throw new InvalidTransformException("signature.Transform.UnknownTransform", new Object[]{str});
        }
        try {
            TransformSpi transformSpiNewInstance = cls.newInstance();
            LOG.debug("Create URI \"{}\" class \"{}\"", str, transformSpiNewInstance.getClass());
            LOG.debug("The NodeList is {}", nodeList);
            if (nodeList != null) {
                int length = nodeList.getLength();
                for (int i2 = 0; i2 < length; i2++) {
                    appendSelf(nodeList.item(i2).cloneNode(true));
                }
            }
            return transformSpiNewInstance;
        } catch (IllegalAccessException e2) {
            throw new InvalidTransformException(e2, "signature.Transform.UnknownTransform", new Object[]{str});
        } catch (InstantiationException e3) {
            throw new InvalidTransformException(e3, "signature.Transform.UnknownTransform", new Object[]{str});
        }
    }

    public boolean isSecureValidation() {
        return this.secureValidation;
    }

    public void setSecureValidation(boolean z2) {
        this.secureValidation = z2;
    }
}
