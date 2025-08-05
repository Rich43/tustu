package com.sun.xml.internal.messaging.saaj.util;

import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/ParserPool.class */
public class ParserPool {
    private final BlockingQueue queue;
    private SAXParserFactory factory = new SAXParserFactoryImpl();
    private int capacity;

    public ParserPool(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayBlockingQueue(capacity);
        this.factory.setNamespaceAware(true);
        for (int i2 = 0; i2 < capacity; i2++) {
            try {
                this.queue.put(this.factory.newSAXParser());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            } catch (ParserConfigurationException ex2) {
                throw new RuntimeException(ex2);
            } catch (SAXException ex3) {
                throw new RuntimeException(ex3);
            }
        }
    }

    public SAXParser get() throws ParserConfigurationException, SAXException {
        try {
            return (SAXParser) this.queue.take2();
        } catch (InterruptedException ex) {
            throw new SAXException(ex);
        }
    }

    public void put(SAXParser parser) {
        this.queue.offer(parser);
    }

    public void returnParser(SAXParser saxParser) {
        saxParser.reset();
        resetSaxParser(saxParser);
        put(saxParser);
    }

    private void resetSaxParser(SAXParser parser) {
        try {
            SymbolTable table = new SymbolTable();
            parser.setProperty("http://apache.org/xml/properties/internal/symbol-table", table);
        } catch (SAXNotRecognizedException e2) {
        } catch (SAXNotSupportedException e3) {
        }
    }
}
