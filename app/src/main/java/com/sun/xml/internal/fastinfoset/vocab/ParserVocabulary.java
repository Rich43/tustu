package com.sun.xml.internal.fastinfoset.vocab;

import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.util.CharArray;
import com.sun.xml.internal.fastinfoset.util.CharArrayArray;
import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
import com.sun.xml.internal.fastinfoset.util.FixedEntryStringIntMap;
import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.sun.xml.internal.fastinfoset.util.StringIntMap;
import com.sun.xml.internal.fastinfoset.util.ValueArray;
import java.util.Iterator;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/vocab/ParserVocabulary.class */
public class ParserVocabulary extends Vocabulary {
    public final CharArrayArray restrictedAlphabet;
    public final StringArray encodingAlgorithm;
    public final StringArray namespaceName;
    public final PrefixArray prefix;
    public final StringArray localName;
    public final StringArray otherNCName;
    public final StringArray otherURI;
    public final StringArray attributeValue;
    public final CharArrayArray otherString;
    public final ContiguousCharArrayArray characterContentChunk;
    public final QualifiedNameArray elementName;
    public final QualifiedNameArray attributeName;
    public final ValueArray[] tables;
    protected SerializerVocabulary _readOnlyVocabulary;
    public static final String IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS_PEOPERTY = "com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.IdentifyingStringTable.maximumItems";
    protected static final int IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS = getIntegerValueFromProperty(IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS_PEOPERTY);
    public static final String NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS_PEOPERTY = "com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.NonIdentifyingStringTable.maximumItems";
    protected static final int NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS = getIntegerValueFromProperty(NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS_PEOPERTY);
    public static final String NON_IDENTIFYING_STRING_TABLE_MAXIMUM_CHARACTERS_PEOPERTY = "com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.NonIdentifyingStringTable.maximumCharacters";
    protected static final int NON_IDENTIFYING_STRING_TABLE_MAXIMUM_CHARACTERS = getIntegerValueFromProperty(NON_IDENTIFYING_STRING_TABLE_MAXIMUM_CHARACTERS_PEOPERTY);

    private static int getIntegerValueFromProperty(String property) {
        String value = System.getProperty(property);
        if (value == null) {
            return Integer.MAX_VALUE;
        }
        try {
            return Math.max(Integer.parseInt(value), 10);
        } catch (NumberFormatException e2) {
            return Integer.MAX_VALUE;
        }
    }

    public ParserVocabulary() {
        this.restrictedAlphabet = new CharArrayArray(10, 256);
        this.encodingAlgorithm = new StringArray(10, 256, true);
        this.tables = new ValueArray[12];
        this.namespaceName = new StringArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, false);
        this.prefix = new PrefixArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS);
        this.localName = new StringArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, false);
        this.otherNCName = new StringArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, false);
        this.otherURI = new StringArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, true);
        this.attributeValue = new StringArray(10, NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, true);
        this.otherString = new CharArrayArray(10, NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS);
        this.characterContentChunk = new ContiguousCharArrayArray(10, NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, 512, NON_IDENTIFYING_STRING_TABLE_MAXIMUM_CHARACTERS);
        this.elementName = new QualifiedNameArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS);
        this.attributeName = new QualifiedNameArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS);
        this.tables[0] = this.restrictedAlphabet;
        this.tables[1] = this.encodingAlgorithm;
        this.tables[2] = this.prefix;
        this.tables[3] = this.namespaceName;
        this.tables[4] = this.localName;
        this.tables[5] = this.otherNCName;
        this.tables[6] = this.otherURI;
        this.tables[7] = this.attributeValue;
        this.tables[8] = this.otherString;
        this.tables[9] = this.characterContentChunk;
        this.tables[10] = this.elementName;
        this.tables[11] = this.attributeName;
    }

    public ParserVocabulary(com.sun.xml.internal.org.jvnet.fastinfoset.Vocabulary v2) {
        this();
        convertVocabulary(v2);
    }

    void setReadOnlyVocabulary(ParserVocabulary readOnlyVocabulary, boolean clear) {
        for (int i2 = 0; i2 < this.tables.length; i2++) {
            this.tables[i2].setReadOnlyArray(readOnlyVocabulary.tables[i2], clear);
        }
    }

    public void setInitialVocabulary(ParserVocabulary initialVocabulary, boolean clear) {
        setExternalVocabularyURI(null);
        setInitialReadOnlyVocabulary(true);
        setReadOnlyVocabulary(initialVocabulary, clear);
    }

    public void setReferencedVocabulary(String referencedVocabularyURI, ParserVocabulary referencedVocabulary, boolean clear) {
        if (!referencedVocabularyURI.equals(getExternalVocabularyURI())) {
            setInitialReadOnlyVocabulary(false);
            setExternalVocabularyURI(referencedVocabularyURI);
            setReadOnlyVocabulary(referencedVocabulary, clear);
        }
    }

    public void clear() {
        for (int i2 = 0; i2 < this.tables.length; i2++) {
            this.tables[i2].clear();
        }
    }

    private void convertVocabulary(com.sun.xml.internal.org.jvnet.fastinfoset.Vocabulary v2) {
        StringIntMap prefixMap = new FixedEntryStringIntMap("xml", 8);
        StringIntMap namespaceNameMap = new FixedEntryStringIntMap("http://www.w3.org/XML/1998/namespace", 8);
        StringIntMap localNameMap = new StringIntMap();
        addToTable(v2.restrictedAlphabets.iterator(), this.restrictedAlphabet);
        addToTable(v2.encodingAlgorithms.iterator(), this.encodingAlgorithm);
        addToTable(v2.prefixes.iterator(), this.prefix, prefixMap);
        addToTable(v2.namespaceNames.iterator(), this.namespaceName, namespaceNameMap);
        addToTable(v2.localNames.iterator(), this.localName, localNameMap);
        addToTable(v2.otherNCNames.iterator(), this.otherNCName);
        addToTable(v2.otherURIs.iterator(), this.otherURI);
        addToTable(v2.attributeValues.iterator(), this.attributeValue);
        addToTable(v2.otherStrings.iterator(), this.otherString);
        addToTable(v2.characterContentChunks.iterator(), this.characterContentChunk);
        addToTable(v2.elements.iterator(), this.elementName, false, prefixMap, namespaceNameMap, localNameMap);
        addToTable(v2.attributes.iterator(), this.attributeName, true, prefixMap, namespaceNameMap, localNameMap);
    }

    private void addToTable(Iterator i2, StringArray a2) {
        while (i2.hasNext()) {
            addToTable((String) i2.next(), a2, (StringIntMap) null);
        }
    }

    private void addToTable(Iterator i2, StringArray a2, StringIntMap m2) {
        while (i2.hasNext()) {
            addToTable((String) i2.next(), a2, m2);
        }
    }

    private void addToTable(String s2, StringArray a2, StringIntMap m2) {
        if (s2.length() == 0) {
            return;
        }
        if (m2 != null) {
            m2.obtainIndex(s2);
        }
        a2.add(s2);
    }

    private void addToTable(Iterator i2, PrefixArray a2, StringIntMap m2) {
        while (i2.hasNext()) {
            addToTable((String) i2.next(), a2, m2);
        }
    }

    private void addToTable(String s2, PrefixArray a2, StringIntMap m2) {
        if (s2.length() == 0) {
            return;
        }
        if (m2 != null) {
            m2.obtainIndex(s2);
        }
        a2.add(s2);
    }

    private void addToTable(Iterator i2, ContiguousCharArrayArray a2) {
        while (i2.hasNext()) {
            addToTable((String) i2.next(), a2);
        }
    }

    private void addToTable(String s2, ContiguousCharArrayArray a2) {
        if (s2.length() == 0) {
            return;
        }
        char[] c2 = s2.toCharArray();
        a2.add(c2, c2.length);
    }

    private void addToTable(Iterator i2, CharArrayArray a2) {
        while (i2.hasNext()) {
            addToTable((String) i2.next(), a2);
        }
    }

    private void addToTable(String s2, CharArrayArray a2) {
        if (s2.length() == 0) {
            return;
        }
        char[] c2 = s2.toCharArray();
        a2.add(new CharArray(c2, 0, c2.length, false));
    }

    private void addToTable(Iterator i2, QualifiedNameArray a2, boolean isAttribute, StringIntMap prefixMap, StringIntMap namespaceNameMap, StringIntMap localNameMap) {
        while (i2.hasNext()) {
            addToNameTable((QName) i2.next(), a2, isAttribute, prefixMap, namespaceNameMap, localNameMap);
        }
    }

    private void addToNameTable(QName n2, QualifiedNameArray a2, boolean isAttribute, StringIntMap prefixMap, StringIntMap namespaceNameMap, StringIntMap localNameMap) {
        int namespaceURIIndex = -1;
        int prefixIndex = -1;
        if (n2.getNamespaceURI().length() > 0) {
            namespaceURIIndex = namespaceNameMap.obtainIndex(n2.getNamespaceURI());
            if (namespaceURIIndex == -1) {
                namespaceURIIndex = this.namespaceName.getSize();
                this.namespaceName.add(n2.getNamespaceURI());
            }
            if (n2.getPrefix().length() > 0) {
                prefixIndex = prefixMap.obtainIndex(n2.getPrefix());
                if (prefixIndex == -1) {
                    prefixIndex = this.prefix.getSize();
                    this.prefix.add(n2.getPrefix());
                }
            }
        }
        int localNameIndex = localNameMap.obtainIndex(n2.getLocalPart());
        if (localNameIndex == -1) {
            localNameIndex = this.localName.getSize();
            this.localName.add(n2.getLocalPart());
        }
        QualifiedName name = new QualifiedName(n2.getPrefix(), n2.getNamespaceURI(), n2.getLocalPart(), a2.getSize(), prefixIndex, namespaceURIIndex, localNameIndex);
        if (isAttribute) {
            name.createAttributeValues(256);
        }
        a2.add(name);
    }
}
