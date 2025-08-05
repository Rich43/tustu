package javax.xml.crypto.dom;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.XMLCryptoContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:javax/xml/crypto/dom/DOMCryptoContext.class */
public class DOMCryptoContext implements XMLCryptoContext {
    private String baseURI;
    private KeySelector ks;
    private URIDereferencer dereferencer;
    private String defaultPrefix;
    private HashMap<String, String> nsMap = new HashMap<>();
    private HashMap<String, Element> idMap = new HashMap<>();
    private HashMap<Object, Object> objMap = new HashMap<>();
    private HashMap<String, Object> propMap = new HashMap<>();

    protected DOMCryptoContext() {
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public String getNamespacePrefix(String str, String str2) {
        if (str == null) {
            throw new NullPointerException("namespaceURI cannot be null");
        }
        String str3 = this.nsMap.get(str);
        return str3 != null ? str3 : str2;
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public String putNamespacePrefix(String str, String str2) {
        if (str == null) {
            throw new NullPointerException("namespaceURI is null");
        }
        return this.nsMap.put(str, str2);
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public String getDefaultNamespacePrefix() {
        return this.defaultPrefix;
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public void setDefaultNamespacePrefix(String str) {
        this.defaultPrefix = str;
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public String getBaseURI() {
        return this.baseURI;
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public void setBaseURI(String str) {
        if (str != null) {
            URI.create(str);
        }
        this.baseURI = str;
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public URIDereferencer getURIDereferencer() {
        return this.dereferencer;
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public void setURIDereferencer(URIDereferencer uRIDereferencer) {
        this.dereferencer = uRIDereferencer;
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public Object getProperty(String str) {
        if (str == null) {
            throw new NullPointerException("name is null");
        }
        return this.propMap.get(str);
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public Object setProperty(String str, Object obj) {
        if (str == null) {
            throw new NullPointerException("name is null");
        }
        return this.propMap.put(str, obj);
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public KeySelector getKeySelector() {
        return this.ks;
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public void setKeySelector(KeySelector keySelector) {
        this.ks = keySelector;
    }

    public Element getElementById(String str) {
        if (str == null) {
            throw new NullPointerException("idValue is null");
        }
        return this.idMap.get(str);
    }

    public void setIdAttributeNS(Element element, String str, String str2) throws DOMException {
        if (element == null) {
            throw new NullPointerException("element is null");
        }
        if (str2 == null) {
            throw new NullPointerException("localName is null");
        }
        String attributeNS = element.getAttributeNS(str, str2);
        if (attributeNS == null || attributeNS.length() == 0) {
            throw new IllegalArgumentException(str2 + " is not an attribute");
        }
        this.idMap.put(attributeNS, element);
    }

    public Iterator iterator() {
        return Collections.unmodifiableMap(this.idMap).entrySet().iterator();
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public Object get(Object obj) {
        return this.objMap.get(obj);
    }

    @Override // javax.xml.crypto.XMLCryptoContext
    public Object put(Object obj, Object obj2) {
        return this.objMap.put(obj, obj2);
    }
}
