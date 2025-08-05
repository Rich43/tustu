package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.WhiteSpaceProcessor;
import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/FastInfosetConnector.class */
final class FastInfosetConnector extends StAXConnector {
    private final StAXDocumentParser fastInfosetStreamReader;
    private boolean textReported;
    private final Base64Data base64Data;
    private final StringBuilder buffer;
    private final CharSequenceImpl charArray;

    public FastInfosetConnector(StAXDocumentParser fastInfosetStreamReader, XmlVisitor visitor) {
        super(visitor);
        this.base64Data = new Base64Data();
        this.buffer = new StringBuilder();
        this.charArray = new CharSequenceImpl();
        fastInfosetStreamReader.setStringInterning(true);
        this.fastInfosetStreamReader = fastInfosetStreamReader;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXConnector
    public void bridge() throws FastInfosetException, XMLStreamException {
        try {
            int depth = 0;
            int event = this.fastInfosetStreamReader.getEventType();
            if (event == 7) {
                while (!this.fastInfosetStreamReader.isStartElement()) {
                    event = this.fastInfosetStreamReader.next();
                }
            }
            if (event != 1) {
                throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
            }
            handleStartDocument(this.fastInfosetStreamReader.getNamespaceContext());
            while (true) {
                switch (event) {
                    case 1:
                        handleStartElement();
                        depth++;
                        break;
                    case 2:
                        depth--;
                        handleEndElement();
                        if (depth != 0) {
                            break;
                        } else {
                            this.fastInfosetStreamReader.next();
                            handleEndDocument();
                            return;
                        }
                    case 4:
                    case 6:
                    case 12:
                        if (!this.predictor.expectText()) {
                            break;
                        } else {
                            int event2 = this.fastInfosetStreamReader.peekNext();
                            if (event2 == 2) {
                                processNonIgnorableText();
                                break;
                            } else if (event2 == 1) {
                                processIgnorableText();
                                break;
                            } else {
                                handleFragmentedCharacters();
                                break;
                            }
                        }
                }
                event = this.fastInfosetStreamReader.next();
            }
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXConnector
    protected Location getCurrentLocation() {
        return this.fastInfosetStreamReader.getLocation();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXConnector
    protected String getCurrentQName() {
        return this.fastInfosetStreamReader.getNameString();
    }

    private void handleStartElement() throws SAXException {
        processUnreportedText();
        for (int i2 = 0; i2 < this.fastInfosetStreamReader.accessNamespaceCount(); i2++) {
            this.visitor.startPrefixMapping(this.fastInfosetStreamReader.getNamespacePrefix(i2), this.fastInfosetStreamReader.getNamespaceURI(i2));
        }
        this.tagName.uri = this.fastInfosetStreamReader.accessNamespaceURI();
        this.tagName.local = this.fastInfosetStreamReader.accessLocalName();
        this.tagName.atts = this.fastInfosetStreamReader.getAttributesHolder();
        this.visitor.startElement(this.tagName);
    }

    private void handleFragmentedCharacters() throws FastInfosetException, SAXException, XMLStreamException {
        this.buffer.setLength(0);
        this.buffer.append(this.fastInfosetStreamReader.getTextCharacters(), this.fastInfosetStreamReader.getTextStart(), this.fastInfosetStreamReader.getTextLength());
        while (true) {
            switch (this.fastInfosetStreamReader.peekNext()) {
                case 1:
                    processBufferedText(true);
                    return;
                case 2:
                    processBufferedText(false);
                    return;
                case 3:
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    this.fastInfosetStreamReader.next();
                    break;
                case 4:
                case 6:
                case 12:
                    this.fastInfosetStreamReader.next();
                    this.buffer.append(this.fastInfosetStreamReader.getTextCharacters(), this.fastInfosetStreamReader.getTextStart(), this.fastInfosetStreamReader.getTextLength());
                    break;
            }
        }
    }

    private void handleEndElement() throws SAXException {
        processUnreportedText();
        this.tagName.uri = this.fastInfosetStreamReader.accessNamespaceURI();
        this.tagName.local = this.fastInfosetStreamReader.accessLocalName();
        this.visitor.endElement(this.tagName);
        for (int i2 = this.fastInfosetStreamReader.accessNamespaceCount() - 1; i2 >= 0; i2--) {
            this.visitor.endPrefixMapping(this.fastInfosetStreamReader.getNamespacePrefix(i2));
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/FastInfosetConnector$CharSequenceImpl.class */
    private final class CharSequenceImpl implements CharSequence {
        char[] ch;
        int start;
        int length;

        CharSequenceImpl() {
        }

        CharSequenceImpl(char[] ch, int start, int length) {
            this.ch = ch;
            this.start = start;
            this.length = length;
        }

        public void set() {
            this.ch = FastInfosetConnector.this.fastInfosetStreamReader.getTextCharacters();
            this.start = FastInfosetConnector.this.fastInfosetStreamReader.getTextStart();
            this.length = FastInfosetConnector.this.fastInfosetStreamReader.getTextLength();
        }

        @Override // java.lang.CharSequence
        public final int length() {
            return this.length;
        }

        @Override // java.lang.CharSequence
        public final char charAt(int index) {
            return this.ch[this.start + index];
        }

        @Override // java.lang.CharSequence
        public final CharSequence subSequence(int start, int end) {
            return FastInfosetConnector.this.new CharSequenceImpl(this.ch, this.start + start, end - start);
        }

        @Override // java.lang.CharSequence
        public String toString() {
            return new String(this.ch, this.start, this.length);
        }
    }

    private void processNonIgnorableText() throws SAXException {
        this.textReported = true;
        boolean isTextAlgorithmAplied = this.fastInfosetStreamReader.getTextAlgorithmBytes() != null;
        if (isTextAlgorithmAplied && this.fastInfosetStreamReader.getTextAlgorithmIndex() == 1) {
            this.base64Data.set(this.fastInfosetStreamReader.getTextAlgorithmBytesClone(), null);
            this.visitor.text(this.base64Data);
        } else {
            if (isTextAlgorithmAplied) {
                this.fastInfosetStreamReader.getText();
            }
            this.charArray.set();
            this.visitor.text(this.charArray);
        }
    }

    private void processIgnorableText() throws SAXException {
        boolean isTextAlgorithmAplied = this.fastInfosetStreamReader.getTextAlgorithmBytes() != null;
        if (isTextAlgorithmAplied && this.fastInfosetStreamReader.getTextAlgorithmIndex() == 1) {
            this.base64Data.set(this.fastInfosetStreamReader.getTextAlgorithmBytesClone(), null);
            this.visitor.text(this.base64Data);
            this.textReported = true;
            return;
        }
        if (isTextAlgorithmAplied) {
            this.fastInfosetStreamReader.getText();
        }
        this.charArray.set();
        if (!WhiteSpaceProcessor.isWhiteSpace(this.charArray)) {
            this.visitor.text(this.charArray);
            this.textReported = true;
        }
    }

    private void processBufferedText(boolean ignorable) throws SAXException {
        if (!ignorable || !WhiteSpaceProcessor.isWhiteSpace(this.buffer)) {
            this.visitor.text(this.buffer);
            this.textReported = true;
        }
    }

    private void processUnreportedText() throws SAXException {
        if (!this.textReported && this.predictor.expectText()) {
            this.visitor.text("");
        }
        this.textReported = false;
    }
}
