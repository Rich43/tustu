package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.xml.internal.stream.XMLBufferListener;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLAttributesImpl.class */
public class XMLAttributesImpl implements XMLAttributes, XMLBufferListener {
    protected static final int TABLE_SIZE = 101;
    protected static final int MAX_HASH_COLLISIONS = 40;
    protected static final int MULTIPLIERS_SIZE = 32;
    protected static final int MULTIPLIERS_MASK = 31;
    protected static final int SIZE_LIMIT = 20;
    protected boolean fNamespaces;
    protected int fLargeCount;
    protected int fLength;
    protected Attribute[] fAttributes;
    protected Attribute[] fAttributeTableView;
    protected int[] fAttributeTableViewChainState;
    protected int fTableViewBuckets;
    protected boolean fIsTableViewConsistent;
    protected int[] fHashMultipliers;

    public XMLAttributesImpl() {
        this(101);
    }

    public XMLAttributesImpl(int tableSize) {
        this.fNamespaces = true;
        this.fLargeCount = 1;
        this.fAttributes = new Attribute[4];
        this.fTableViewBuckets = tableSize;
        for (int i2 = 0; i2 < this.fAttributes.length; i2++) {
            this.fAttributes[i2] = new Attribute();
        }
    }

    public void setNamespaces(boolean namespaces) {
        this.fNamespaces = namespaces;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public int addAttribute(QName name, String type, String value) {
        return addAttribute(name, type, value, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int addAttribute(com.sun.org.apache.xerces.internal.xni.QName r7, java.lang.String r8, java.lang.String r9, com.sun.org.apache.xerces.internal.xni.XMLString r10) {
        /*
            Method dump skipped, instructions count: 681
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.util.XMLAttributesImpl.addAttribute(com.sun.org.apache.xerces.internal.xni.QName, java.lang.String, java.lang.String, com.sun.org.apache.xerces.internal.xni.XMLString):int");
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void removeAllAttributes() {
        this.fLength = 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void removeAttributeAt(int attrIndex) {
        this.fIsTableViewConsistent = false;
        if (attrIndex < this.fLength - 1) {
            Attribute removedAttr = this.fAttributes[attrIndex];
            System.arraycopy(this.fAttributes, attrIndex + 1, this.fAttributes, attrIndex, (this.fLength - attrIndex) - 1);
            this.fAttributes[this.fLength - 1] = removedAttr;
        }
        this.fLength--;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void setName(int attrIndex, QName attrName) {
        this.fAttributes[attrIndex].name.setValues(attrName);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void getName(int attrIndex, QName attrName) {
        attrName.setValues(this.fAttributes[attrIndex].name);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void setType(int attrIndex, String attrType) {
        this.fAttributes[attrIndex].type = attrType;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void setValue(int attrIndex, String attrValue) {
        setValue(attrIndex, attrValue, null);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void setValue(int attrIndex, String attrValue, XMLString value) {
        Attribute attribute = this.fAttributes[attrIndex];
        attribute.value = attrValue;
        attribute.nonNormalizedValue = attrValue;
        attribute.xmlValue = value;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void setNonNormalizedValue(int attrIndex, String attrValue) {
        if (attrValue == null) {
            attrValue = this.fAttributes[attrIndex].value;
        }
        this.fAttributes[attrIndex].nonNormalizedValue = attrValue;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getNonNormalizedValue(int attrIndex) {
        String value = this.fAttributes[attrIndex].nonNormalizedValue;
        return value;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void setSpecified(int attrIndex, boolean specified) {
        this.fAttributes[attrIndex].specified = specified;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public boolean isSpecified(int attrIndex) {
        return this.fAttributes[attrIndex].specified;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public int getLength() {
        return this.fLength;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getType(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        return getReportableType(this.fAttributes[index].type);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getType(String qname) {
        int index = getIndex(qname);
        if (index != -1) {
            return getReportableType(this.fAttributes[index].type);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getValue(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        if (this.fAttributes[index].value == null && this.fAttributes[index].xmlValue != null) {
            this.fAttributes[index].value = this.fAttributes[index].xmlValue.toString();
        }
        return this.fAttributes[index].value;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getValue(String qname) {
        int index = getIndex(qname);
        if (index == -1) {
            return null;
        }
        if (this.fAttributes[index].value == null) {
            this.fAttributes[index].value = this.fAttributes[index].xmlValue.toString();
        }
        return this.fAttributes[index].value;
    }

    public String getName(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        return this.fAttributes[index].name.rawname;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public int getIndex(String qName) {
        for (int i2 = 0; i2 < this.fLength; i2++) {
            Attribute attribute = this.fAttributes[i2];
            if (attribute.name.rawname != null && attribute.name.rawname.equals(qName)) {
                return i2;
            }
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public int getIndex(String uri, String localPart) {
        for (int i2 = 0; i2 < this.fLength; i2++) {
            Attribute attribute = this.fAttributes[i2];
            if (attribute.name.localpart != null && attribute.name.localpart.equals(localPart) && (uri == attribute.name.uri || (uri != null && attribute.name.uri != null && attribute.name.uri.equals(uri)))) {
                return i2;
            }
        }
        return -1;
    }

    public int getIndexByLocalName(String localPart) {
        for (int i2 = 0; i2 < this.fLength; i2++) {
            Attribute attribute = this.fAttributes[i2];
            if (attribute.name.localpart != null && attribute.name.localpart.equals(localPart)) {
                return i2;
            }
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getLocalName(int index) {
        if (!this.fNamespaces) {
            return "";
        }
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        return this.fAttributes[index].name.localpart;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getQName(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        String rawname = this.fAttributes[index].name.rawname;
        return rawname != null ? rawname : "";
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public QName getQualifiedName(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        return this.fAttributes[index].name;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getType(String uri, String localName) {
        int index;
        if (this.fNamespaces && (index = getIndex(uri, localName)) != -1) {
            return getType(index);
        }
        return null;
    }

    public int getIndexFast(String qName) {
        for (int i2 = 0; i2 < this.fLength; i2++) {
            Attribute attribute = this.fAttributes[i2];
            if (attribute.name.rawname == qName) {
                return i2;
            }
        }
        return -1;
    }

    public void addAttributeNS(QName name, String type, String value) {
        Attribute[] attributes;
        int index = this.fLength;
        int i2 = this.fLength;
        this.fLength = i2 + 1;
        if (i2 == this.fAttributes.length) {
            if (this.fLength < 20) {
                attributes = new Attribute[this.fAttributes.length + 4];
            } else {
                attributes = new Attribute[this.fAttributes.length << 1];
            }
            System.arraycopy(this.fAttributes, 0, attributes, 0, this.fAttributes.length);
            for (int i3 = this.fAttributes.length; i3 < attributes.length; i3++) {
                attributes[i3] = new Attribute();
            }
            this.fAttributes = attributes;
        }
        Attribute attribute = this.fAttributes[index];
        attribute.name.setValues(name);
        attribute.type = type;
        attribute.value = value;
        attribute.nonNormalizedValue = value;
        attribute.specified = false;
        attribute.augs.removeAllItems();
    }

    public QName checkDuplicatesNS() {
        int length = this.fLength;
        if (length <= 20) {
            Attribute[] attributes = this.fAttributes;
            for (int i2 = 0; i2 < length - 1; i2++) {
                Attribute att1 = attributes[i2];
                for (int j2 = i2 + 1; j2 < length; j2++) {
                    Attribute att2 = attributes[j2];
                    if (att1.name.localpart == att2.name.localpart && att1.name.uri == att2.name.uri) {
                        return att2.name;
                    }
                }
            }
            return null;
        }
        return checkManyDuplicatesNS();
    }

    private QName checkManyDuplicatesNS() {
        this.fIsTableViewConsistent = false;
        prepareTableView();
        int length = this.fLength;
        Attribute[] attributes = this.fAttributes;
        Attribute[] attributeTableView = this.fAttributeTableView;
        int[] attributeTableViewChainState = this.fAttributeTableViewChainState;
        int largeCount = this.fLargeCount;
        for (int i2 = 0; i2 < length; i2++) {
            Attribute attr = attributes[i2];
            int bucket = getTableViewBucket(attr.name.localpart, attr.name.uri);
            if (attributeTableViewChainState[bucket] != largeCount) {
                attributeTableViewChainState[bucket] = largeCount;
                attr.next = null;
                attributeTableView[bucket] = attr;
            } else {
                int collisionCount = 0;
                Attribute found = attributeTableView[bucket];
                while (found != null) {
                    if (found.name.localpart == attr.name.localpart && found.name.uri == attr.name.uri) {
                        return attr.name;
                    }
                    found = found.next;
                    collisionCount++;
                }
                if (collisionCount >= 40) {
                    rebalanceTableViewNS(i2 + 1);
                    largeCount = this.fLargeCount;
                } else {
                    attr.next = attributeTableView[bucket];
                    attributeTableView[bucket] = attr;
                }
            }
        }
        return null;
    }

    public int getIndexFast(String uri, String localPart) {
        for (int i2 = 0; i2 < this.fLength; i2++) {
            Attribute attribute = this.fAttributes[i2];
            if (attribute.name.localpart == localPart && attribute.name.uri == uri) {
                return i2;
            }
        }
        return -1;
    }

    private String getReportableType(String type) {
        if (type.charAt(0) == '(') {
            return SchemaSymbols.ATTVAL_NMTOKEN;
        }
        return type;
    }

    protected int getTableViewBucket(String qname) {
        return (hash(qname) & Integer.MAX_VALUE) % this.fTableViewBuckets;
    }

    protected int getTableViewBucket(String localpart, String uri) {
        if (uri == null) {
            return (hash(localpart) & Integer.MAX_VALUE) % this.fTableViewBuckets;
        }
        return (hash(localpart, uri) & Integer.MAX_VALUE) % this.fTableViewBuckets;
    }

    private int hash(String localpart) {
        if (this.fHashMultipliers == null) {
            return localpart.hashCode();
        }
        return hash0(localpart);
    }

    private int hash(String localpart, String uri) {
        if (this.fHashMultipliers == null) {
            return localpart.hashCode() + (uri.hashCode() * 31);
        }
        return hash0(localpart) + (hash0(uri) * this.fHashMultipliers[32]);
    }

    private int hash0(String symbol) {
        int code = 0;
        int length = symbol.length();
        int[] multipliers = this.fHashMultipliers;
        for (int i2 = 0; i2 < length; i2++) {
            code = (code * multipliers[i2 & 31]) + symbol.charAt(i2);
        }
        return code;
    }

    protected void cleanTableView() {
        int i2 = this.fLargeCount + 1;
        this.fLargeCount = i2;
        if (i2 < 0) {
            if (this.fAttributeTableViewChainState != null) {
                for (int i3 = this.fTableViewBuckets - 1; i3 >= 0; i3--) {
                    this.fAttributeTableViewChainState[i3] = 0;
                }
            }
            this.fLargeCount = 1;
        }
    }

    private void growTableView() {
        int length = this.fLength;
        int tableViewBuckets = this.fTableViewBuckets;
        while (true) {
            tableViewBuckets = (tableViewBuckets << 1) + 1;
            if (tableViewBuckets < 0) {
                tableViewBuckets = Integer.MAX_VALUE;
                break;
            } else if (length <= tableViewBuckets) {
                break;
            }
        }
        this.fTableViewBuckets = tableViewBuckets;
        this.fAttributeTableView = null;
        this.fLargeCount = 1;
    }

    protected void prepareTableView() {
        if (this.fLength > this.fTableViewBuckets) {
            growTableView();
        }
        if (this.fAttributeTableView == null) {
            this.fAttributeTableView = new Attribute[this.fTableViewBuckets];
            this.fAttributeTableViewChainState = new int[this.fTableViewBuckets];
        } else {
            cleanTableView();
        }
    }

    protected void prepareAndPopulateTableView() {
        prepareAndPopulateTableView(this.fLength);
    }

    private void prepareAndPopulateTableView(int count) {
        prepareTableView();
        for (int i2 = 0; i2 < count; i2++) {
            Attribute attr = this.fAttributes[i2];
            int bucket = getTableViewBucket(attr.name.rawname);
            if (this.fAttributeTableViewChainState[bucket] != this.fLargeCount) {
                this.fAttributeTableViewChainState[bucket] = this.fLargeCount;
                attr.next = null;
                this.fAttributeTableView[bucket] = attr;
            } else {
                attr.next = this.fAttributeTableView[bucket];
                this.fAttributeTableView[bucket] = attr;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getPrefix(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        String prefix = this.fAttributes[index].name.prefix;
        return prefix != null ? prefix : "";
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getURI(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        String uri = this.fAttributes[index].name.uri;
        return uri;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public String getValue(String uri, String localName) {
        int index = getIndex(uri, localName);
        if (index != -1) {
            return getValue(index);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public Augmentations getAugmentations(String uri, String localName) {
        int index = getIndex(uri, localName);
        if (index != -1) {
            return this.fAttributes[index].augs;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public Augmentations getAugmentations(String qName) {
        int index = getIndex(qName);
        if (index != -1) {
            return this.fAttributes[index].augs;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public Augmentations getAugmentations(int attributeIndex) {
        if (attributeIndex < 0 || attributeIndex >= this.fLength) {
            return null;
        }
        return this.fAttributes[attributeIndex].augs;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
    public void setAugmentations(int attrIndex, Augmentations augs) {
        this.fAttributes[attrIndex].augs = augs;
    }

    public void setURI(int attrIndex, String uri) {
        this.fAttributes[attrIndex].name.uri = uri;
    }

    public void setSchemaId(int attrIndex, boolean schemaId) {
        this.fAttributes[attrIndex].schemaId = schemaId;
    }

    public boolean getSchemaId(int index) {
        if (index < 0 || index >= this.fLength) {
            return false;
        }
        return this.fAttributes[index].schemaId;
    }

    public boolean getSchemaId(String qname) {
        int index = getIndex(qname);
        if (index != -1) {
            return this.fAttributes[index].schemaId;
        }
        return false;
    }

    public boolean getSchemaId(String uri, String localName) {
        int index;
        if (this.fNamespaces && (index = getIndex(uri, localName)) != -1) {
            return this.fAttributes[index].schemaId;
        }
        return false;
    }

    @Override // com.sun.xml.internal.stream.XMLBufferListener
    public void refresh() {
        if (this.fLength > 0) {
            for (int i2 = 0; i2 < this.fLength; i2++) {
                getValue(i2);
            }
        }
    }

    @Override // com.sun.xml.internal.stream.XMLBufferListener
    public void refresh(int pos) {
    }

    private void prepareAndPopulateTableViewNS(int count) {
        prepareTableView();
        for (int i2 = 0; i2 < count; i2++) {
            Attribute attr = this.fAttributes[i2];
            int bucket = getTableViewBucket(attr.name.localpart, attr.name.uri);
            if (this.fAttributeTableViewChainState[bucket] != this.fLargeCount) {
                this.fAttributeTableViewChainState[bucket] = this.fLargeCount;
                attr.next = null;
                this.fAttributeTableView[bucket] = attr;
            } else {
                attr.next = this.fAttributeTableView[bucket];
                this.fAttributeTableView[bucket] = attr;
            }
        }
    }

    private void rebalanceTableView(int count) {
        if (this.fHashMultipliers == null) {
            this.fHashMultipliers = new int[33];
        }
        PrimeNumberSequenceGenerator.generateSequence(this.fHashMultipliers);
        prepareAndPopulateTableView(count);
    }

    private void rebalanceTableViewNS(int count) {
        if (this.fHashMultipliers == null) {
            this.fHashMultipliers = new int[33];
        }
        PrimeNumberSequenceGenerator.generateSequence(this.fHashMultipliers);
        prepareAndPopulateTableViewNS(count);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLAttributesImpl$Attribute.class */
    static class Attribute {
        public String type;
        public String value;
        public XMLString xmlValue;
        public String nonNormalizedValue;
        public boolean specified;
        public boolean schemaId;
        public Attribute next;
        public QName name = new QName();
        public Augmentations augs = new AugmentationsImpl();

        Attribute() {
        }
    }
}
