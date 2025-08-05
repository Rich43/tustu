package com.sun.org.apache.xml.internal.dtm.ref;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/CoroutineParser.class */
public interface CoroutineParser {
    int getParserCoroutineID();

    CoroutineManager getCoroutineManager();

    void setContentHandler(ContentHandler contentHandler);

    void setLexHandler(LexicalHandler lexicalHandler);

    Object doParse(InputSource inputSource, int i2);

    Object doMore(boolean z2, int i2);

    void doTerminate(int i2);

    void init(CoroutineManager coroutineManager, int i2, XMLReader xMLReader);
}
