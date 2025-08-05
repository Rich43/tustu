package com.sun.org.apache.xml.internal.security.transforms;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/Transforms.class */
public class Transforms extends SignatureElementProxy {
    public static final String TRANSFORM_C14N_OMIT_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
    public static final String TRANSFORM_C14N_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
    public static final String TRANSFORM_C14N11_OMIT_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11";
    public static final String TRANSFORM_C14N11_WITH_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11#WithComments";
    public static final String TRANSFORM_C14N_EXCL_OMIT_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#";
    public static final String TRANSFORM_C14N_EXCL_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
    public static final String TRANSFORM_XSLT = "http://www.w3.org/TR/1999/REC-xslt-19991116";
    public static final String TRANSFORM_BASE64_DECODE = "http://www.w3.org/2000/09/xmldsig#base64";
    public static final String TRANSFORM_XPATH = "http://www.w3.org/TR/1999/REC-xpath-19991116";
    public static final String TRANSFORM_ENVELOPED_SIGNATURE = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
    public static final String TRANSFORM_XPOINTER = "http://www.w3.org/TR/2001/WD-xptr-20010108";
    public static final String TRANSFORM_XPATH2FILTER = "http://www.w3.org/2002/06/xmldsig-filter2";
    private static final Logger LOG = LoggerFactory.getLogger(Transforms.class);
    private Element[] transforms;
    private boolean secureValidation;

    protected Transforms() {
    }

    public Transforms(Document document) {
        super(document);
        addReturnToSelf();
    }

    public Transforms(Element element, String str) throws DOMException, XMLSecurityException {
        super(element, str);
        if (getLength() == 0) {
            throw new TransformationException("xml.WrongContent", new Object[]{Constants._TAG_TRANSFORM, Constants._TAG_TRANSFORMS});
        }
    }

    public void setSecureValidation(boolean z2) {
        this.secureValidation = z2;
    }

    public void addTransform(String str) throws TransformationException {
        try {
            LOG.debug("Transforms.addTransform({})", str);
            addTransform(new Transform(getDocument(), str));
        } catch (InvalidTransformException e2) {
            throw new TransformationException(e2);
        }
    }

    public void addTransform(String str, Element element) throws TransformationException {
        try {
            LOG.debug("Transforms.addTransform({})", str);
            addTransform(new Transform(getDocument(), str, element));
        } catch (InvalidTransformException e2) {
            throw new TransformationException(e2);
        }
    }

    public void addTransform(String str, NodeList nodeList) throws TransformationException {
        try {
            addTransform(new Transform(getDocument(), str, nodeList));
        } catch (InvalidTransformException e2) {
            throw new TransformationException(e2);
        }
    }

    private void addTransform(Transform transform) {
        LOG.debug("Transforms.addTransform({})", transform.getURI());
        appendSelf(transform.getElement());
        addReturnToSelf();
    }

    public XMLSignatureInput performTransforms(XMLSignatureInput xMLSignatureInput) throws TransformationException {
        return performTransforms(xMLSignatureInput, null);
    }

    public XMLSignatureInput performTransforms(XMLSignatureInput xMLSignatureInput, OutputStream outputStream) throws TransformationException {
        try {
            int length = getLength() - 1;
            for (int i2 = 0; i2 < length; i2++) {
                Transform transformItem = item(i2);
                LOG.debug("Perform the ({})th {} transform", Integer.valueOf(i2), transformItem.getURI());
                checkSecureValidation(transformItem);
                xMLSignatureInput = transformItem.performTransform(xMLSignatureInput);
            }
            if (length >= 0) {
                Transform transformItem2 = item(length);
                LOG.debug("Perform the ({})th {} transform", Integer.valueOf(length), transformItem2.getURI());
                checkSecureValidation(transformItem2);
                xMLSignatureInput = transformItem2.performTransform(xMLSignatureInput, outputStream);
            }
            return xMLSignatureInput;
        } catch (CanonicalizationException e2) {
            throw new TransformationException(e2);
        } catch (InvalidCanonicalizerException e3) {
            throw new TransformationException(e3);
        } catch (IOException e4) {
            throw new TransformationException(e4);
        }
    }

    private void checkSecureValidation(Transform transform) throws TransformationException {
        String uri = transform.getURI();
        if (this.secureValidation && "http://www.w3.org/TR/1999/REC-xslt-19991116".equals(uri)) {
            throw new TransformationException("signature.Transform.ForbiddenTransform", new Object[]{uri});
        }
        transform.setSecureValidation(this.secureValidation);
    }

    public int getLength() {
        initTransforms();
        return this.transforms.length;
    }

    public Transform item(int i2) throws TransformationException {
        try {
            initTransforms();
            return new Transform(this.transforms[i2], this.baseURI);
        } catch (XMLSecurityException e2) {
            throw new TransformationException(e2);
        }
    }

    private void initTransforms() {
        if (this.transforms == null) {
            this.transforms = XMLUtils.selectDsNodes(getFirstChild(), Constants._TAG_TRANSFORM);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_TRANSFORMS;
    }
}
