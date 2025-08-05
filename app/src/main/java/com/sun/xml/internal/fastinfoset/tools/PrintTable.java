package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.util.CharArrayArray;
import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/PrintTable.class */
public class PrintTable {
    public static void printVocabulary(ParserVocabulary vocabulary) {
        printArray("Attribute Name Table", vocabulary.attributeName);
        printArray("Attribute Value Table", vocabulary.attributeValue);
        printArray("Character Content Chunk Table", vocabulary.characterContentChunk);
        printArray("Element Name Table", vocabulary.elementName);
        printArray("Local Name Table", vocabulary.localName);
        printArray("Namespace Name Table", vocabulary.namespaceName);
        printArray("Other NCName Table", vocabulary.otherNCName);
        printArray("Other String Table", vocabulary.otherString);
        printArray("Other URI Table", vocabulary.otherURI);
        printArray("Prefix Table", vocabulary.prefix);
    }

    public static void printArray(String title, StringArray a2) {
        System.out.println(title);
        for (int i2 = 0; i2 < a2.getSize(); i2++) {
            System.out.println("" + (i2 + 1) + ": " + a2.getArray()[i2]);
        }
    }

    public static void printArray(String title, PrefixArray a2) {
        System.out.println(title);
        for (int i2 = 0; i2 < a2.getSize(); i2++) {
            System.out.println("" + (i2 + 1) + ": " + a2.getArray()[i2]);
        }
    }

    public static void printArray(String title, CharArrayArray a2) {
        System.out.println(title);
        for (int i2 = 0; i2 < a2.getSize(); i2++) {
            System.out.println("" + (i2 + 1) + ": " + ((Object) a2.getArray()[i2]));
        }
    }

    public static void printArray(String title, ContiguousCharArrayArray a2) {
        System.out.println(title);
        for (int i2 = 0; i2 < a2.getSize(); i2++) {
            System.out.println("" + (i2 + 1) + ": " + a2.getString(i2));
        }
    }

    public static void printArray(String title, QualifiedNameArray a2) {
        System.out.println(title);
        for (int i2 = 0; i2 < a2.getSize(); i2++) {
            QualifiedName name = a2.getArray()[i2];
            System.out.println("" + (name.index + 1) + ": {" + name.namespaceName + "}" + name.prefix + CallSiteDescriptor.TOKEN_DELIMITER + name.localName);
        }
    }

    public static void main(String[] args) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            ParserVocabulary referencedVocabulary = new ParserVocabulary();
            VocabularyGenerator vocabularyGenerator = new VocabularyGenerator(referencedVocabulary);
            File f2 = new File(args[0]);
            saxParser.parse(f2, vocabularyGenerator);
            printVocabulary(referencedVocabulary);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
