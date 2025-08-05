package com.sun.org.apache.xml.internal.security;

import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
import com.sun.org.apache.xml.internal.security.utils.I18n;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/Init.class */
public class Init {
    public static final String CONF_NS = "http://www.xmlsecurity.org/NS/#configuration";
    private static final Logger LOG = LoggerFactory.getLogger(Init.class);
    private static boolean alreadyInitialized = false;

    public static final synchronized boolean isInitialized() {
        return alreadyInitialized;
    }

    public static synchronized void init() {
        if (alreadyInitialized) {
            return;
        }
        InputStream inputStream = (InputStream) AccessController.doPrivileged(() -> {
            String property = System.getProperty("com.sun.org.apache.xml.internal.security.resource.config");
            if (property == null) {
                return null;
            }
            return Init.class.getResourceAsStream(property);
        });
        if (inputStream == null) {
            dynamicInit();
        } else {
            fileInit(inputStream);
        }
        alreadyInitialized = true;
    }

    private static void dynamicInit() {
        I18n.init("en", "US");
        LOG.debug("Registering default algorithms");
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: com.sun.org.apache.xml.internal.security.Init.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws XMLSecurityException {
                    ElementProxy.registerDefaultPrefixes();
                    Transform.registerDefaultAlgorithms();
                    SignatureAlgorithm.registerDefaultAlgorithms();
                    JCEMapper.registerDefaultAlgorithms();
                    Canonicalizer.registerDefaultAlgorithms();
                    ResourceResolver.registerDefaultResolvers();
                    KeyResolver.registerDefaultResolvers();
                    return null;
                }
            });
        } catch (PrivilegedActionException e2) {
            XMLSecurityException xMLSecurityException = (XMLSecurityException) e2.getException();
            LOG.error(xMLSecurityException.getMessage(), xMLSecurityException);
            xMLSecurityException.printStackTrace();
        }
    }

    private static void fileInit(InputStream inputStream) {
        Node nodeItem;
        try {
            Node firstChild = XMLUtils.createDocumentBuilder(false).parse(inputStream).getFirstChild();
            while (firstChild != null && !"Configuration".equals(firstChild.getLocalName())) {
                firstChild = firstChild.getNextSibling();
            }
            if (firstChild == null) {
                LOG.error("Error in reading configuration file - Configuration element not found");
                return;
            }
            for (Node firstChild2 = firstChild.getFirstChild(); firstChild2 != null; firstChild2 = firstChild2.getNextSibling()) {
                if (1 == firstChild2.getNodeType()) {
                    String localName = firstChild2.getLocalName();
                    if ("ResourceBundles".equals(localName)) {
                        Element element = (Element) firstChild2;
                        Attr attributeNodeNS = element.getAttributeNodeNS(null, "defaultLanguageCode");
                        Attr attributeNodeNS2 = element.getAttributeNodeNS(null, "defaultCountryCode");
                        I18n.init(attributeNodeNS == null ? null : attributeNodeNS.getNodeValue(), attributeNodeNS2 == null ? null : attributeNodeNS2.getNodeValue());
                    }
                    if ("CanonicalizationMethods".equals(localName)) {
                        for (Element element2 : XMLUtils.selectNodes(firstChild2.getFirstChild(), CONF_NS, Constants._TAG_CANONICALIZATIONMETHOD)) {
                            String attributeNS = element2.getAttributeNS(null, Constants._ATT_URI);
                            String attributeNS2 = element2.getAttributeNS(null, "JAVACLASS");
                            try {
                                Canonicalizer.register(attributeNS, attributeNS2);
                                LOG.debug("Canonicalizer.register({}, {})", attributeNS, attributeNS2);
                            } catch (ClassNotFoundException e2) {
                                LOG.error(I18n.translate("algorithm.classDoesNotExist", new Object[]{attributeNS, attributeNS2}));
                            }
                        }
                    }
                    if ("TransformAlgorithms".equals(localName)) {
                        for (Element element3 : XMLUtils.selectNodes(firstChild2.getFirstChild(), CONF_NS, "TransformAlgorithm")) {
                            String attributeNS3 = element3.getAttributeNS(null, Constants._ATT_URI);
                            String attributeNS4 = element3.getAttributeNS(null, "JAVACLASS");
                            try {
                                Transform.register(attributeNS3, attributeNS4);
                                LOG.debug("Transform.register({}, {})", attributeNS3, attributeNS4);
                            } catch (ClassNotFoundException e3) {
                                LOG.error(I18n.translate("algorithm.classDoesNotExist", new Object[]{attributeNS3, attributeNS4}));
                            } catch (NoClassDefFoundError e4) {
                                LOG.warn("Not able to found dependencies for algorithm, I'll keep working.");
                            }
                        }
                    }
                    if ("JCEAlgorithmMappings".equals(localName) && (nodeItem = ((Element) firstChild2).getElementsByTagName("Algorithms").item(0)) != null) {
                        for (Element element4 : XMLUtils.selectNodes(nodeItem.getFirstChild(), CONF_NS, Constants._ATT_ALGORITHM)) {
                            JCEMapper.register(element4.getAttributeNS(null, Constants._ATT_URI), new JCEMapper.Algorithm(element4));
                        }
                    }
                    if ("SignatureAlgorithms".equals(localName)) {
                        for (Element element5 : XMLUtils.selectNodes(firstChild2.getFirstChild(), CONF_NS, "SignatureAlgorithm")) {
                            String attributeNS5 = element5.getAttributeNS(null, Constants._ATT_URI);
                            String attributeNS6 = element5.getAttributeNS(null, "JAVACLASS");
                            try {
                                SignatureAlgorithm.register(attributeNS5, attributeNS6);
                                LOG.debug("SignatureAlgorithm.register({}, {})", attributeNS5, attributeNS6);
                            } catch (ClassNotFoundException e5) {
                                LOG.error(I18n.translate("algorithm.classDoesNotExist", new Object[]{attributeNS5, attributeNS6}));
                            }
                        }
                    }
                    if ("ResourceResolvers".equals(localName)) {
                        for (Element element6 : XMLUtils.selectNodes(firstChild2.getFirstChild(), CONF_NS, "Resolver")) {
                            String attributeNS7 = element6.getAttributeNS(null, "JAVACLASS");
                            String attributeNS8 = element6.getAttributeNS(null, "DESCRIPTION");
                            if (attributeNS8 != null && attributeNS8.length() > 0) {
                                LOG.debug("Register Resolver: {}: {}", attributeNS7, attributeNS8);
                            } else {
                                LOG.debug("Register Resolver: {}: For unknown purposes", attributeNS7);
                            }
                            try {
                                ResourceResolver.register(attributeNS7);
                            } catch (Throwable th) {
                                LOG.warn("Cannot register:" + attributeNS7 + " perhaps some needed jars are not installed", th);
                            }
                        }
                    }
                    if ("KeyResolver".equals(localName)) {
                        Element[] elementArrSelectNodes = XMLUtils.selectNodes(firstChild2.getFirstChild(), CONF_NS, "Resolver");
                        ArrayList arrayList = new ArrayList(elementArrSelectNodes.length);
                        for (Element element7 : elementArrSelectNodes) {
                            String attributeNS9 = element7.getAttributeNS(null, "JAVACLASS");
                            String attributeNS10 = element7.getAttributeNS(null, "DESCRIPTION");
                            if (attributeNS10 != null && attributeNS10.length() > 0) {
                                LOG.debug("Register Resolver: {}: {}", attributeNS9, attributeNS10);
                            } else {
                                LOG.debug("Register Resolver: {}: For unknown purposes", attributeNS9);
                            }
                            arrayList.add(attributeNS9);
                        }
                        KeyResolver.registerClassNames(arrayList);
                    }
                    if ("PrefixMappings".equals(localName)) {
                        LOG.debug("Now I try to bind prefixes:");
                        for (Element element8 : XMLUtils.selectNodes(firstChild2.getFirstChild(), CONF_NS, "PrefixMapping")) {
                            String attributeNS11 = element8.getAttributeNS(null, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NAMESPACE);
                            String attributeNS12 = element8.getAttributeNS(null, "prefix");
                            LOG.debug("Now I try to bind {} to {}", attributeNS12, attributeNS11);
                            ElementProxy.setDefaultPrefix(attributeNS11, attributeNS12);
                        }
                    }
                }
            }
        } catch (Exception e6) {
            LOG.error("Bad: ", e6);
        }
    }
}
