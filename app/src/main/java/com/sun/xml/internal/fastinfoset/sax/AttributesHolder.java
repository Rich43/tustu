package com.sun.xml.internal.fastinfoset.sax;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
import java.io.IOException;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/sax/AttributesHolder.class */
public class AttributesHolder implements EncodingAlgorithmAttributes {
    private static final int DEFAULT_CAPACITY = 8;
    private Map _registeredEncodingAlgorithms;
    private int _attributeCount;
    private QualifiedName[] _names;
    private String[] _values;
    private String[] _algorithmURIs;
    private int[] _algorithmIds;
    private Object[] _algorithmData;

    public AttributesHolder() {
        this._names = new QualifiedName[8];
        this._values = new String[8];
        this._algorithmURIs = new String[8];
        this._algorithmIds = new int[8];
        this._algorithmData = new Object[8];
    }

    public AttributesHolder(Map registeredEncodingAlgorithms) {
        this();
        this._registeredEncodingAlgorithms = registeredEncodingAlgorithms;
    }

    @Override // org.xml.sax.Attributes
    public final int getLength() {
        return this._attributeCount;
    }

    @Override // org.xml.sax.Attributes
    public final String getLocalName(int index) {
        return this._names[index].localName;
    }

    @Override // org.xml.sax.Attributes
    public final String getQName(int index) {
        return this._names[index].getQNameString();
    }

    @Override // org.xml.sax.Attributes
    public final String getType(int index) {
        return "CDATA";
    }

    @Override // org.xml.sax.Attributes
    public final String getURI(int index) {
        return this._names[index].namespaceName;
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(int index) {
        String value = this._values[index];
        if (value != null) {
            return value;
        }
        if (this._algorithmData[index] == null) {
            return null;
        }
        if (this._algorithmIds[index] >= 32 && this._registeredEncodingAlgorithms == null) {
            return null;
        }
        try {
            String[] strArr = this._values;
            String string = convertEncodingAlgorithmDataToString(this._algorithmIds[index], this._algorithmURIs[index], this._algorithmData[index]).toString();
            strArr[index] = string;
            return string;
        } catch (FastInfosetException e2) {
            return null;
        } catch (IOException e3) {
            return null;
        }
    }

    @Override // org.xml.sax.Attributes
    public final int getIndex(String qName) {
        int i2 = qName.indexOf(58);
        String prefix = "";
        String localName = qName;
        if (i2 >= 0) {
            prefix = qName.substring(0, i2);
            localName = qName.substring(i2 + 1);
        }
        for (int i3 = 0; i3 < this._attributeCount; i3++) {
            QualifiedName name = this._names[i3];
            if (localName.equals(name.localName) && prefix.equals(name.prefix)) {
                return i3;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public final String getType(String qName) {
        int index = getIndex(qName);
        if (index >= 0) {
            return "CDATA";
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(String qName) {
        int index = getIndex(qName);
        if (index >= 0) {
            return this._values[index];
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final int getIndex(String uri, String localName) {
        for (int i2 = 0; i2 < this._attributeCount; i2++) {
            QualifiedName name = this._names[i2];
            if (localName.equals(name.localName) && uri.equals(name.namespaceName)) {
                return i2;
            }
        }
        return -1;
    }

    @Override // org.xml.sax.Attributes
    public final String getType(String uri, String localName) {
        int index = getIndex(uri, localName);
        if (index >= 0) {
            return "CDATA";
        }
        return null;
    }

    @Override // org.xml.sax.Attributes
    public final String getValue(String uri, String localName) {
        int index = getIndex(uri, localName);
        if (index >= 0) {
            return this._values[index];
        }
        return null;
    }

    public final void clear() {
        for (int i2 = 0; i2 < this._attributeCount; i2++) {
            this._values[i2] = null;
            this._algorithmData[i2] = null;
        }
        this._attributeCount = 0;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public final String getAlgorithmURI(int index) {
        return this._algorithmURIs[index];
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public final int getAlgorithmIndex(int index) {
        return this._algorithmIds[index];
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public final Object getAlgorithmData(int index) {
        return this._algorithmData[index];
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public String getAlpababet(int index) {
        return null;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
    public boolean getToIndex(int index) {
        return false;
    }

    public final void addAttribute(QualifiedName name, String value) {
        if (this._attributeCount == this._names.length) {
            resize();
        }
        this._names[this._attributeCount] = name;
        String[] strArr = this._values;
        int i2 = this._attributeCount;
        this._attributeCount = i2 + 1;
        strArr[i2] = value;
    }

    public final void addAttributeWithAlgorithmData(QualifiedName name, String URI, int id, Object data) {
        if (this._attributeCount == this._names.length) {
            resize();
        }
        this._names[this._attributeCount] = name;
        this._values[this._attributeCount] = null;
        this._algorithmURIs[this._attributeCount] = URI;
        this._algorithmIds[this._attributeCount] = id;
        Object[] objArr = this._algorithmData;
        int i2 = this._attributeCount;
        this._attributeCount = i2 + 1;
        objArr[i2] = data;
    }

    public final QualifiedName getQualifiedName(int index) {
        return this._names[index];
    }

    public final String getPrefix(int index) {
        return this._names[index].prefix;
    }

    private final void resize() {
        int newLength = ((this._attributeCount * 3) / 2) + 1;
        QualifiedName[] names = new QualifiedName[newLength];
        String[] values = new String[newLength];
        String[] algorithmURIs = new String[newLength];
        int[] algorithmIds = new int[newLength];
        Object[] algorithmData = new Object[newLength];
        System.arraycopy(this._names, 0, names, 0, this._attributeCount);
        System.arraycopy(this._values, 0, values, 0, this._attributeCount);
        System.arraycopy(this._algorithmURIs, 0, algorithmURIs, 0, this._attributeCount);
        System.arraycopy(this._algorithmIds, 0, algorithmIds, 0, this._attributeCount);
        System.arraycopy(this._algorithmData, 0, algorithmData, 0, this._attributeCount);
        this._names = names;
        this._values = values;
        this._algorithmURIs = algorithmURIs;
        this._algorithmIds = algorithmIds;
        this._algorithmData = algorithmData;
    }

    private final StringBuffer convertEncodingAlgorithmDataToString(int identifier, String URI, Object data) throws FastInfosetException, IOException {
        EncodingAlgorithm ea;
        if (identifier < 9) {
            ea = BuiltInEncodingAlgorithmFactory.getAlgorithm(identifier);
        } else {
            if (identifier == 9) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
            }
            if (identifier >= 32) {
                if (URI == null) {
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent") + identifier);
                }
                ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
                if (ea == null) {
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmNotRegistered") + URI);
                }
            } else {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
            }
        }
        StringBuffer sb = new StringBuffer();
        ea.convertToCharacters(data, sb);
        return sb;
    }
}
