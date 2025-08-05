package com.sun.xml.internal.fastinfoset.vocab;

import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.util.CharArrayIntMap;
import com.sun.xml.internal.fastinfoset.util.FixedEntryStringIntMap;
import com.sun.xml.internal.fastinfoset.util.KeyIntMap;
import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.internal.fastinfoset.util.StringIntMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/vocab/SerializerVocabulary.class */
public class SerializerVocabulary extends Vocabulary {
    public final StringIntMap restrictedAlphabet;
    public final StringIntMap encodingAlgorithm;
    public final StringIntMap namespaceName;
    public final StringIntMap prefix;
    public final StringIntMap localName;
    public final StringIntMap otherNCName;
    public final StringIntMap otherURI;
    public final StringIntMap attributeValue;
    public final CharArrayIntMap otherString;
    public final CharArrayIntMap characterContentChunk;
    public final LocalNameQualifiedNamesMap elementName;
    public final LocalNameQualifiedNamesMap attributeName;
    public final KeyIntMap[] tables;
    protected boolean _useLocalNameAsKey;
    protected SerializerVocabulary _readOnlyVocabulary;

    public SerializerVocabulary() {
        this.tables = new KeyIntMap[12];
        KeyIntMap[] keyIntMapArr = this.tables;
        StringIntMap stringIntMap = new StringIntMap(4);
        this.restrictedAlphabet = stringIntMap;
        keyIntMapArr[0] = stringIntMap;
        KeyIntMap[] keyIntMapArr2 = this.tables;
        StringIntMap stringIntMap2 = new StringIntMap(4);
        this.encodingAlgorithm = stringIntMap2;
        keyIntMapArr2[1] = stringIntMap2;
        KeyIntMap[] keyIntMapArr3 = this.tables;
        FixedEntryStringIntMap fixedEntryStringIntMap = new FixedEntryStringIntMap("xml", 8);
        this.prefix = fixedEntryStringIntMap;
        keyIntMapArr3[2] = fixedEntryStringIntMap;
        KeyIntMap[] keyIntMapArr4 = this.tables;
        FixedEntryStringIntMap fixedEntryStringIntMap2 = new FixedEntryStringIntMap("http://www.w3.org/XML/1998/namespace", 8);
        this.namespaceName = fixedEntryStringIntMap2;
        keyIntMapArr4[3] = fixedEntryStringIntMap2;
        KeyIntMap[] keyIntMapArr5 = this.tables;
        StringIntMap stringIntMap3 = new StringIntMap();
        this.localName = stringIntMap3;
        keyIntMapArr5[4] = stringIntMap3;
        KeyIntMap[] keyIntMapArr6 = this.tables;
        StringIntMap stringIntMap4 = new StringIntMap(4);
        this.otherNCName = stringIntMap4;
        keyIntMapArr6[5] = stringIntMap4;
        KeyIntMap[] keyIntMapArr7 = this.tables;
        StringIntMap stringIntMap5 = new StringIntMap(4);
        this.otherURI = stringIntMap5;
        keyIntMapArr7[6] = stringIntMap5;
        KeyIntMap[] keyIntMapArr8 = this.tables;
        StringIntMap stringIntMap6 = new StringIntMap();
        this.attributeValue = stringIntMap6;
        keyIntMapArr8[7] = stringIntMap6;
        KeyIntMap[] keyIntMapArr9 = this.tables;
        CharArrayIntMap charArrayIntMap = new CharArrayIntMap(4);
        this.otherString = charArrayIntMap;
        keyIntMapArr9[8] = charArrayIntMap;
        KeyIntMap[] keyIntMapArr10 = this.tables;
        CharArrayIntMap charArrayIntMap2 = new CharArrayIntMap();
        this.characterContentChunk = charArrayIntMap2;
        keyIntMapArr10[9] = charArrayIntMap2;
        KeyIntMap[] keyIntMapArr11 = this.tables;
        LocalNameQualifiedNamesMap localNameQualifiedNamesMap = new LocalNameQualifiedNamesMap();
        this.elementName = localNameQualifiedNamesMap;
        keyIntMapArr11[10] = localNameQualifiedNamesMap;
        KeyIntMap[] keyIntMapArr12 = this.tables;
        LocalNameQualifiedNamesMap localNameQualifiedNamesMap2 = new LocalNameQualifiedNamesMap();
        this.attributeName = localNameQualifiedNamesMap2;
        keyIntMapArr12[11] = localNameQualifiedNamesMap2;
    }

    public SerializerVocabulary(com.sun.xml.internal.org.jvnet.fastinfoset.Vocabulary v2, boolean useLocalNameAsKey) {
        this();
        this._useLocalNameAsKey = useLocalNameAsKey;
        convertVocabulary(v2);
    }

    public SerializerVocabulary getReadOnlyVocabulary() {
        return this._readOnlyVocabulary;
    }

    protected void setReadOnlyVocabulary(SerializerVocabulary readOnlyVocabulary, boolean clear) {
        for (int i2 = 0; i2 < this.tables.length; i2++) {
            this.tables[i2].setReadOnlyMap(readOnlyVocabulary.tables[i2], clear);
        }
    }

    public void setInitialVocabulary(SerializerVocabulary initialVocabulary, boolean clear) {
        setExternalVocabularyURI(null);
        setInitialReadOnlyVocabulary(true);
        setReadOnlyVocabulary(initialVocabulary, clear);
    }

    public void setExternalVocabulary(String externalVocabularyURI, SerializerVocabulary externalVocabulary, boolean clear) {
        setInitialReadOnlyVocabulary(false);
        setExternalVocabularyURI(externalVocabularyURI);
        setReadOnlyVocabulary(externalVocabulary, clear);
    }

    public void clear() {
        for (int i2 = 0; i2 < this.tables.length; i2++) {
            this.tables[i2].clear();
        }
    }

    private void convertVocabulary(com.sun.xml.internal.org.jvnet.fastinfoset.Vocabulary v2) {
        addToTable(v2.restrictedAlphabets.iterator(), this.restrictedAlphabet);
        addToTable(v2.encodingAlgorithms.iterator(), this.encodingAlgorithm);
        addToTable(v2.prefixes.iterator(), this.prefix);
        addToTable(v2.namespaceNames.iterator(), this.namespaceName);
        addToTable(v2.localNames.iterator(), this.localName);
        addToTable(v2.otherNCNames.iterator(), this.otherNCName);
        addToTable(v2.otherURIs.iterator(), this.otherURI);
        addToTable(v2.attributeValues.iterator(), this.attributeValue);
        addToTable(v2.otherStrings.iterator(), this.otherString);
        addToTable(v2.characterContentChunks.iterator(), this.characterContentChunk);
        addToTable(v2.elements.iterator(), this.elementName);
        addToTable(v2.attributes.iterator(), this.attributeName);
    }

    private void addToTable(Iterator i2, StringIntMap m2) {
        while (i2.hasNext()) {
            addToTable((String) i2.next(), m2);
        }
    }

    private void addToTable(String s2, StringIntMap m2) {
        if (s2.length() == 0) {
            return;
        }
        m2.obtainIndex(s2);
    }

    private void addToTable(Iterator i2, CharArrayIntMap m2) {
        while (i2.hasNext()) {
            addToTable((String) i2.next(), m2);
        }
    }

    private void addToTable(String s2, CharArrayIntMap m2) {
        if (s2.length() == 0) {
            return;
        }
        char[] c2 = s2.toCharArray();
        m2.obtainIndex(c2, 0, c2.length, false);
    }

    private void addToTable(Iterator i2, LocalNameQualifiedNamesMap m2) {
        while (i2.hasNext()) {
            addToNameTable((QName) i2.next(), m2);
        }
    }

    private void addToNameTable(QName n2, LocalNameQualifiedNamesMap m2) {
        String localPart;
        LocalNameQualifiedNamesMap.Entry entry;
        int namespaceURIIndex = -1;
        int prefixIndex = -1;
        if (n2.getNamespaceURI().length() > 0) {
            namespaceURIIndex = this.namespaceName.obtainIndex(n2.getNamespaceURI());
            if (namespaceURIIndex == -1) {
                namespaceURIIndex = this.namespaceName.get(n2.getNamespaceURI());
            }
            if (n2.getPrefix().length() > 0) {
                prefixIndex = this.prefix.obtainIndex(n2.getPrefix());
                if (prefixIndex == -1) {
                    prefixIndex = this.prefix.get(n2.getPrefix());
                }
            }
        }
        int localNameIndex = this.localName.obtainIndex(n2.getLocalPart());
        if (localNameIndex == -1) {
            localNameIndex = this.localName.get(n2.getLocalPart());
        }
        QualifiedName name = new QualifiedName(n2.getPrefix(), n2.getNamespaceURI(), n2.getLocalPart(), m2.getNextIndex(), prefixIndex, namespaceURIIndex, localNameIndex);
        if (this._useLocalNameAsKey) {
            entry = m2.obtainEntry(n2.getLocalPart());
        } else {
            if (prefixIndex == -1) {
                localPart = n2.getLocalPart();
            } else {
                localPart = n2.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + n2.getLocalPart();
            }
            String qName = localPart;
            entry = m2.obtainEntry(qName);
        }
        entry.addQualifiedName(name);
    }
}
