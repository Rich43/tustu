package org.icepdf.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.swing.text.Document;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.io.BufferedMarkedInputStream;
import org.icepdf.core.io.ConservativeSizingByteArrayOutputStream;
import org.icepdf.core.io.SeekableByteArrayInputStream;
import org.icepdf.core.io.SeekableInput;
import org.icepdf.core.io.SeekableInputConstrainedWrapper;
import org.icepdf.core.pobjects.Catalog;
import org.icepdf.core.pobjects.CrossReference;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Form;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.ObjectStream;
import org.icepdf.core.pobjects.OptionalContentGroup;
import org.icepdf.core.pobjects.OptionalContentMembership;
import org.icepdf.core.pobjects.PObject;
import org.icepdf.core.pobjects.PTrailer;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.fonts.CMap;
import org.icepdf.core.pobjects.fonts.Font;
import org.icepdf.core.pobjects.fonts.FontDescriptor;
import org.icepdf.core.pobjects.fonts.FontFactory;
import org.icepdf.core.pobjects.graphics.TilingPattern;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/Parser.class */
public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class.toString());
    public static final int PARSE_MODE_NORMAL = 0;
    public static final int PARSE_MODE_OBJECT_STREAM = 1;
    private InputStream reader;
    boolean lastTokenHString;
    private Stack<Object> stack;
    private int parseMode;
    private boolean isTrailer;
    private int linearTraversalOffset;

    public Parser(SeekableInput r2) {
        this(r2, 0);
    }

    public Parser(SeekableInput r2, int pm) {
        this.lastTokenHString = false;
        this.stack = new Stack<>();
        this.reader = r2.getInputStream();
        this.parseMode = pm;
    }

    public Parser(InputStream r2) {
        this(r2, 0);
    }

    public Parser(InputStream r2, int pm) {
        this.lastTokenHString = false;
        this.stack = new Stack<>();
        this.reader = new BufferedMarkedInputStream(r2);
        this.parseMode = pm;
    }

    /* JADX WARN: Finally extract failed */
    public Object getObject(Library library) throws PDFException {
        HashMap streamHash;
        ConservativeSizingByteArrayOutputStream out;
        SeekableInputConstrainedWrapper streamInputWrapper;
        int currRead;
        long lengthOfStreamData;
        int deepnessCount = 0;
        boolean inObject = false;
        Reference objectReference = null;
        try {
            this.reader.mark(1);
            if (library.isLinearTraversal() && (this.reader instanceof BufferedMarkedInputStream)) {
                this.linearTraversalOffset = ((BufferedMarkedInputStream) this.reader).getMarkedPosition();
            }
            do {
                try {
                    Object nextToken = getToken();
                    if ((nextToken instanceof StringObject) || (nextToken instanceof Name) || (nextToken instanceof Number)) {
                        if (nextToken instanceof StringObject) {
                            ((StringObject) nextToken).setReference(objectReference);
                        }
                        this.stack.push(nextToken);
                    } else if (nextToken.equals("obj")) {
                        if (inObject) {
                            this.stack.pop();
                            this.stack.pop();
                            return addPObject(library, objectReference);
                        }
                        deepnessCount = 0;
                        inObject = true;
                        Number generationNumber = (Number) this.stack.pop();
                        Number objectNumber = (Number) this.stack.pop();
                        objectReference = new Reference(objectNumber, generationNumber);
                    } else if (nextToken.equals("endobj") || nextToken.equals("endobject") || nextToken.equals("enbobj")) {
                        if (inObject) {
                            return addPObject(library, objectReference);
                        }
                    } else if (nextToken.equals("endstream")) {
                        deepnessCount--;
                        if (inObject) {
                            return addPObject(library, objectReference);
                        }
                    } else if (nextToken.equals(Document.StreamDescriptionProperty)) {
                        deepnessCount++;
                        Object tmp = this.stack.pop();
                        if (tmp instanceof Dictionary) {
                            streamHash = ((Dictionary) tmp).getEntries();
                        } else {
                            streamHash = (HashMap) tmp;
                        }
                        int streamLength = library.getInt(streamHash, Dictionary.LENGTH_KEY);
                        try {
                            this.reader.mark(2);
                            int curChar = this.reader.read();
                            if (curChar == 13) {
                                this.reader.mark(1);
                                if (this.reader.read() != 10) {
                                    this.reader.reset();
                                }
                            } else if (curChar != 10) {
                                this.reader.reset();
                            }
                            if (this.reader instanceof SeekableInput) {
                                SeekableInput streamDataInput = (SeekableInput) this.reader;
                                long filePositionOfStreamData = streamDataInput.getAbsolutePosition();
                                if (streamLength > 0) {
                                    long lengthOfStreamData2 = streamLength;
                                    streamDataInput.seekRelative(streamLength);
                                    lengthOfStreamData = lengthOfStreamData2 + skipUntilEndstream(null);
                                } else {
                                    lengthOfStreamData = captureStreamData(null);
                                }
                                streamInputWrapper = new SeekableInputConstrainedWrapper(streamDataInput, filePositionOfStreamData, lengthOfStreamData);
                            } else {
                                if (!library.isLinearTraversal() && streamLength > 0) {
                                    byte[] buffer = new byte[streamLength];
                                    int totalRead = 0;
                                    while (totalRead < buffer.length && (currRead = this.reader.read(buffer, totalRead, buffer.length - totalRead)) > 0) {
                                        totalRead += currRead;
                                    }
                                    out = new ConservativeSizingByteArrayOutputStream(buffer);
                                    skipUntilEndstream(out);
                                } else {
                                    out = new ConservativeSizingByteArrayOutputStream(16384);
                                    captureStreamData(out);
                                }
                                int size = out.size();
                                out.trim();
                                streamInputWrapper = new SeekableInputConstrainedWrapper(new SeekableByteArrayInputStream(out.relinquishByteArray()), 0L, size);
                            }
                            PTrailer trailer = null;
                            Stream stream = null;
                            Name type = (Name) library.getObject(streamHash, Dictionary.TYPE_KEY);
                            Name subtype = (Name) library.getObject(streamHash, Dictionary.SUBTYPE_KEY);
                            if (type != null) {
                                if (type.equals("XRef")) {
                                    stream = new Stream(library, streamHash, streamInputWrapper);
                                    stream.init();
                                    InputStream in = stream.getDecodedByteArrayInputStream();
                                    CrossReference xrefStream = new CrossReference();
                                    if (in != null) {
                                        try {
                                            xrefStream.addXRefStreamEntries(library, streamHash, in);
                                            try {
                                                in.close();
                                            } catch (Throwable e2) {
                                                logger.log(Level.WARNING, "Error appending stream entries.", e2);
                                            }
                                        } catch (Throwable th) {
                                            try {
                                                in.close();
                                            } catch (Throwable e3) {
                                                logger.log(Level.WARNING, "Error appending stream entries.", e3);
                                            }
                                            throw th;
                                        }
                                    }
                                    HashMap trailerHash = (HashMap) streamHash.clone();
                                    trailer = new PTrailer(library, trailerHash, null, xrefStream);
                                } else if (type.equals("ObjStm")) {
                                    stream = new ObjectStream(library, streamHash, streamInputWrapper);
                                } else if (type.equals("XObject") && subtype.equals("Image")) {
                                    stream = new ImageStream(library, streamHash, streamInputWrapper);
                                } else if (type.equals("Pattern")) {
                                    stream = new TilingPattern(library, streamHash, streamInputWrapper);
                                }
                            }
                            if (stream == null && subtype != null) {
                                if (subtype.equals("Image")) {
                                    stream = new ImageStream(library, streamHash, streamInputWrapper);
                                } else if (subtype.equals("Form") && !"Pattern".equals(type)) {
                                    stream = new Form(library, streamHash, streamInputWrapper);
                                } else if (subtype.equals("Form") && "Pattern".equals(type)) {
                                    stream = new TilingPattern(library, streamHash, streamInputWrapper);
                                }
                            }
                            if (trailer != null) {
                                this.stack.push(trailer);
                            } else {
                                if (stream == null) {
                                    stream = new Stream(library, streamHash, streamInputWrapper);
                                }
                                this.stack.push(stream);
                                return addPObject(library, objectReference);
                            }
                        } catch (IOException e4) {
                            if (logger.isLoggable(Level.FINE)) {
                                logger.log(Level.FINE, "Error getting next object", (Throwable) e4);
                                return null;
                            }
                            return null;
                        }
                    } else if (nextToken.equals("true")) {
                        this.stack.push(true);
                    } else if (nextToken.equals("false")) {
                        this.stack.push(false);
                    } else if (nextToken.equals("R")) {
                        Number generationNumber2 = (Number) this.stack.pop();
                        Number objectNumber2 = (Number) this.stack.pop();
                        this.stack.push(new Reference(objectNumber2, generationNumber2));
                    } else if (nextToken.equals("[")) {
                        deepnessCount++;
                        this.stack.push(nextToken);
                    } else if (nextToken.equals("]")) {
                        deepnessCount--;
                        int searchPosition = this.stack.search("[");
                        int size2 = searchPosition - 1;
                        if (size2 < 0) {
                            logger.warning("Negative array size, a  malformed content stream has likely been encountered.");
                            size2 = 0;
                        }
                        List<Object> v2 = new ArrayList<>(size2);
                        Object[] tmp2 = new Object[size2];
                        if (searchPosition > 0) {
                            for (int i2 = size2 - 1; i2 >= 0; i2--) {
                                tmp2[i2] = this.stack.pop();
                            }
                            for (int i3 = 0; i3 < size2; i3++) {
                                v2.add(tmp2[i3]);
                            }
                            this.stack.pop();
                        } else {
                            this.stack.clear();
                        }
                        this.stack.push(v2);
                    } else if (nextToken.equals("<<")) {
                        deepnessCount++;
                        this.stack.push(nextToken);
                    } else if (nextToken.equals(">>")) {
                        deepnessCount--;
                        if (!this.isTrailer && deepnessCount >= 0) {
                            if (!this.stack.isEmpty()) {
                                HashMap<Object, Object> hashMap = new HashMap<>();
                                Object obj = this.stack.pop();
                                while (true) {
                                    if (((obj instanceof String) && obj.equals("<<")) || this.stack.isEmpty()) {
                                        break;
                                    }
                                    Object key = this.stack.pop();
                                    hashMap.put(key, obj);
                                    if (this.stack.isEmpty()) {
                                        break;
                                    }
                                    obj = this.stack.pop();
                                }
                                Object obj2 = hashMap.get(Dictionary.TYPE_KEY);
                                if (obj2 != null && (obj2 instanceof Name)) {
                                    Name n2 = (Name) obj2;
                                    if (n2.equals(Catalog.TYPE)) {
                                        this.stack.push(new Catalog(library, hashMap));
                                    } else if (n2.equals(PageTree.TYPE)) {
                                        this.stack.push(new PageTree(library, hashMap));
                                    } else if (n2.equals(Page.TYPE)) {
                                        this.stack.push(new Page(library, hashMap));
                                    } else if (n2.equals(Font.TYPE)) {
                                        boolean fontDescriptor = (hashMap.get(FontDescriptor.FONT_FILE) == null && hashMap.get(FontDescriptor.FONT_FILE_2) == null && hashMap.get(FontDescriptor.FONT_FILE_3) == null) ? false : true;
                                        if (!fontDescriptor) {
                                            this.stack.push(FontFactory.getInstance().getFont(library, hashMap));
                                        } else {
                                            this.stack.push(new FontDescriptor(library, hashMap));
                                        }
                                    } else if (n2.equals(FontDescriptor.TYPE)) {
                                        this.stack.push(new FontDescriptor(library, hashMap));
                                    } else if (n2.equals(CMap.TYPE)) {
                                        this.stack.push(hashMap);
                                    } else if (n2.equals(Annotation.TYPE)) {
                                        this.stack.push(Annotation.buildAnnotation(library, hashMap));
                                    } else if (n2.equals(OptionalContentGroup.TYPE)) {
                                        this.stack.push(new OptionalContentGroup(library, hashMap));
                                    } else if (n2.equals(OptionalContentMembership.TYPE)) {
                                        this.stack.push(new OptionalContentMembership(library, hashMap));
                                    } else {
                                        this.stack.push(hashMap);
                                    }
                                } else {
                                    this.stack.push(hashMap);
                                }
                            }
                        } else if (this.isTrailer && deepnessCount == 0) {
                            HashMap<Object, Object> hashMap2 = new HashMap<>();
                            Object obj3 = this.stack.pop();
                            while (true) {
                                if (((obj3 instanceof String) && obj3.equals("<<")) || this.stack.isEmpty()) {
                                    break;
                                }
                                Object key2 = this.stack.pop();
                                hashMap2.put(key2, obj3);
                                if (this.stack.isEmpty()) {
                                    break;
                                }
                                obj3 = this.stack.pop();
                            }
                            return hashMap2;
                        }
                    } else if (nextToken.equals("xref")) {
                        CrossReference xrefTable = new CrossReference();
                        xrefTable.addXRefTableEntries(this);
                        this.stack.push(xrefTable);
                    } else {
                        if (nextToken.equals("trailer")) {
                            CrossReference xrefTable2 = null;
                            if (this.stack.peek() instanceof CrossReference) {
                                xrefTable2 = (CrossReference) this.stack.pop();
                            }
                            this.stack.clear();
                            this.isTrailer = true;
                            HashMap trailerDictionary = (HashMap) getObject(library);
                            this.isTrailer = false;
                            return new PTrailer(library, trailerDictionary, xrefTable2, null);
                        }
                        if (!(nextToken instanceof String) || !((String) nextToken).startsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                            if ((nextToken instanceof String) && ((String) nextToken).startsWith("endobj")) {
                                if (inObject) {
                                    return addPObject(library, objectReference);
                                }
                            } else {
                                this.stack.push(nextToken);
                            }
                        }
                    }
                    if (this.parseMode == 1 && deepnessCount == 0 && this.stack.size() > 0) {
                        return this.stack.pop();
                    }
                } catch (IOException e5) {
                    return null;
                }
            } while (0 == 0);
            return this.stack.pop();
        } catch (Exception e6) {
            logger.log(Level.WARNING, "Fatal error parsing PDF file stream.", (Throwable) e6);
            return null;
        }
    }

    public String peek2() throws IOException {
        this.reader.mark(2);
        char[] c2 = {(char) this.reader.read(), (char) this.reader.read()};
        String s2 = new String(c2);
        this.reader.reset();
        return s2;
    }

    public boolean readLineForInlineImage(OutputStream out) throws IOException {
        int state = 0;
        while (true) {
            int c2 = this.reader.read();
            if (c2 < 0) {
                break;
            }
            if (state == 0 && c2 == 69) {
                state++;
            } else if (state == 1 && c2 == 73) {
                state++;
            } else if (state == 2 && isWhitespace((char) (255 & c2))) {
                boolean imageDataFound = isStillInlineImageData(this.reader, 32);
                if (imageDataFound) {
                    out.write(69);
                    out.write(73);
                    out.write(c2);
                    state = 0;
                    if (c2 == 13 || c2 == 10) {
                        break;
                    }
                } else {
                    return true;
                }
            } else {
                if (state > 0) {
                    out.write(69);
                }
                if (state > 1) {
                    out.write(73);
                }
                state = 0;
                out.write((byte) c2);
                if (c2 == 13 || c2 == 10) {
                    break;
                }
            }
        }
        return state == 2;
    }

    private static boolean isStillInlineImageData(InputStream reader, int numBytesToCheck) throws IOException {
        boolean imageDataFound = false;
        boolean onlyWhitespaceSoFar = true;
        reader.mark(numBytesToCheck);
        byte[] toCheck = new byte[numBytesToCheck];
        int numReadToCheck = reader.read(toCheck);
        int i2 = 0;
        while (true) {
            if (i2 < numReadToCheck) {
                char charToCheck = (char) (toCheck[i2] & 255);
                boolean typicalTextTokenInContentStream = charToCheck == 'Q' || charToCheck == 'q' || charToCheck == 'S' || charToCheck == 's';
                if (onlyWhitespaceSoFar && typicalTextTokenInContentStream && i2 + 1 < numReadToCheck && isWhitespace((char) (toCheck[i2 + 1] & 255))) {
                    break;
                }
                if (!isWhitespace(charToCheck)) {
                    onlyWhitespaceSoFar = false;
                }
                if (isExpectedInContentStream(charToCheck)) {
                    i2++;
                } else {
                    imageDataFound = true;
                    break;
                }
            } else {
                break;
            }
        }
        reader.reset();
        return imageDataFound;
    }

    private static boolean isExpectedInContentStream(char c2) {
        return (c2 >= 'a' && c2 <= 'Z') || (c2 >= 'A' && c2 <= 'Z') || ((c2 >= '0' && c2 <= '9') || isWhitespace(c2) || isDelimiter(c2) || c2 == '\\' || c2 == '\'' || c2 == '\"' || c2 == '*' || c2 == '.');
    }

    public PObject addPObject(Library library, Reference objectReference) {
        Object o2 = this.stack.pop();
        if (o2 instanceof Stream) {
            Stream tmp = (Stream) o2;
            tmp.setPObjectReference(objectReference);
        } else if (o2 instanceof Dictionary) {
            Dictionary tmp2 = (Dictionary) o2;
            tmp2.setPObjectReference(objectReference);
        }
        library.addObject(o2, objectReference);
        return new PObject(o2, objectReference);
    }

    public Object getStreamObject() throws IOException {
        Object o2 = getToken();
        if (o2 instanceof String) {
            if (o2.equals("<<")) {
                HashMap<Object, Object> h2 = new HashMap<>();
                Object streamObject = getStreamObject();
                while (true) {
                    Object o1 = streamObject;
                    if (o1.equals(">>")) {
                        break;
                    }
                    h2.put(o1, getStreamObject());
                    streamObject = getStreamObject();
                }
                o2 = h2;
            } else if (o2.equals("[")) {
                List<Object> v2 = new ArrayList<>();
                Object streamObject2 = getStreamObject();
                while (true) {
                    Object o12 = streamObject2;
                    if (o12.equals("]")) {
                        break;
                    }
                    v2.add(o12);
                    streamObject2 = getStreamObject();
                }
                o2 = v2;
            }
        }
        return o2;
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x029e A[PHI: r7 r13 r15
  0x029e: PHI (r7v4 'currentChar' char) = 
  (r7v3 'currentChar' char)
  (r7v3 'currentChar' char)
  (r7v5 'currentChar' char)
  (r7v5 'currentChar' char)
  (r7v5 'currentChar' char)
  (r7v5 'currentChar' char)
  (r7v5 'currentChar' char)
  (r7v5 'currentChar' char)
  (r7v6 'currentChar' char)
  (r7v7 'currentChar' char)
  (r7v8 'currentChar' char)
  (r7v9 'currentChar' char)
  (r7v10 'currentChar' char)
  (r7v11 'currentChar' char)
  (r7v3 'currentChar' char)
 binds: [B:136:0x0291, B:84:0x0165, B:101:0x01ee, B:103:0x01f4, B:105:0x01fa, B:126:0x0251, B:127:0x0254, B:124:0x0242, B:121:0x0236, B:118:0x022a, B:115:0x021e, B:112:0x0212, B:109:0x0206, B:99:0x01e4, B:72:0x0134] A[DONT_GENERATE, DONT_INLINE]
  0x029e: PHI (r13v2 'parenthesisCount' int) = 
  (r13v1 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v4 'parenthesisCount' int)
  (r13v1 'parenthesisCount' int)
 binds: [B:136:0x0291, B:84:0x0165, B:101:0x01ee, B:103:0x01f4, B:105:0x01fa, B:126:0x0251, B:127:0x0254, B:124:0x0242, B:121:0x0236, B:118:0x022a, B:115:0x021e, B:112:0x0212, B:109:0x0206, B:99:0x01e4, B:72:0x0134] A[DONT_GENERATE, DONT_INLINE]
  0x029e: PHI (r15v2 'ignoreChar' boolean) = 
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v5 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
  (r15v1 'ignoreChar' boolean)
 binds: [B:136:0x0291, B:84:0x0165, B:101:0x01ee, B:103:0x01f4, B:105:0x01fa, B:126:0x0251, B:127:0x0254, B:124:0x0242, B:121:0x0236, B:118:0x022a, B:115:0x021e, B:112:0x0212, B:109:0x0206, B:99:0x01e4, B:72:0x0134] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:140:0x02a3  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x02c2  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x02ca A[EDGE_INSN: B:174:0x02ca->B:149:0x02ca BREAK  A[LOOP:1: B:62:0x010d->B:178:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:178:? A[LOOP:1: B:62:0x010d->B:178:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object getToken() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 791
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.util.Parser.getToken():java.lang.Object");
    }

    public Object getNumberOrStringWithMark(int maxLength) throws IOException {
        int curr;
        this.reader.mark(maxLength);
        StringBuilder sb = new StringBuilder(maxLength);
        boolean readNonWhitespaceYet = false;
        boolean foundDigit = false;
        int i2 = 0;
        while (true) {
            if (i2 >= maxLength || (curr = this.reader.read()) < 0) {
                break;
            }
            char currChar = (char) curr;
            if (isWhitespace(currChar)) {
                if (readNonWhitespaceYet) {
                    break;
                }
                i2++;
            } else if (isDelimiter(currChar)) {
                this.reader.reset();
                this.reader.mark(maxLength);
                for (int j2 = 0; j2 < i2; j2++) {
                    this.reader.read();
                }
            } else {
                readNonWhitespaceYet = true;
                if (currChar != '.' && currChar >= '0' && curr <= 57) {
                    foundDigit = true;
                }
                sb.append(currChar);
                i2++;
            }
        }
        if (foundDigit) {
            return getNumber(sb);
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    public void ungetNumberOrStringWithReset() throws IOException {
        this.reader.reset();
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x006b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getIntSurroundedByWhitespace() {
        /*
            r5 = this;
            r0 = 0
            r6 = r0
            r0 = 0
            r7 = r0
            r0 = 0
            r8 = r0
        L6:
            r0 = r5
            java.io.InputStream r0 = r0.reader     // Catch: java.io.IOException -> L58
            int r0 = r0.read()     // Catch: java.io.IOException -> L58
            r9 = r0
            r0 = r9
            if (r0 >= 0) goto L17
            goto L55
        L17:
            r0 = r9
            char r0 = (char) r0     // Catch: java.io.IOException -> L58
            boolean r0 = java.lang.Character.isWhitespace(r0)     // Catch: java.io.IOException -> L58
            if (r0 == 0) goto L27
            r0 = r8
            if (r0 == 0) goto L52
            goto L55
        L27:
            r0 = r9
            r1 = 45
            if (r0 != r1) goto L35
            r0 = 1
            r7 = r0
            r0 = 1
            r8 = r0
            goto L52
        L35:
            r0 = r9
            r1 = 48
            if (r0 < r1) goto L55
            r0 = r9
            r1 = 57
            if (r0 > r1) goto L55
            r0 = r6
            r1 = 10
            int r0 = r0 * r1
            r6 = r0
            r0 = r6
            r1 = r9
            r2 = 48
            int r1 = r1 - r2
            int r0 = r0 + r1
            r6 = r0
            r0 = 1
            r8 = r0
        L52:
            goto L6
        L55:
            goto L67
        L58:
            r9 = move-exception
            java.util.logging.Logger r0 = org.icepdf.core.util.Parser.logger
            java.util.logging.Level r1 = java.util.logging.Level.FINE
            java.lang.String r2 = "Error detecting int."
            r3 = r9
            r0.log(r1, r2, r3)
        L67:
            r0 = r7
            if (r0 == 0) goto L6f
            r0 = r6
            r1 = -1
            int r0 = r0 * r1
            r6 = r0
        L6f:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.util.Parser.getIntSurroundedByWhitespace():int");
    }

    public Number getNumber(StringBuilder value) {
        int digit = 0;
        float decimal = 0.0f;
        float divisor = 10.0f;
        boolean isDecimal = false;
        byte[] streamBytes = value.toString().getBytes();
        boolean singed = streamBytes[0] == 45;
        boolean positive = streamBytes[0] == 43;
        int startTokenPos = (singed || positive) ? 0 + 1 : 0;
        if (singed && streamBytes[startTokenPos] == 45) {
            startTokenPos++;
        }
        int max = streamBytes.length;
        for (int i2 = startTokenPos; i2 < max; i2++) {
            int current = streamBytes[i2] - 48;
            boolean isDigit = streamBytes[i2] >= 48 && streamBytes[i2] <= 57;
            if (!isDecimal && isDigit) {
                digit = (digit * 10) + current;
            } else if (isDecimal && isDigit) {
                decimal += current / divisor;
                divisor *= 10.0f;
            } else {
                if (streamBytes[i2] != 46) {
                    break;
                }
                isDecimal = true;
            }
        }
        if (singed) {
            if (isDecimal) {
                return Float.valueOf(-(digit + decimal));
            }
            return Integer.valueOf(-digit);
        }
        if (isDecimal) {
            return Float.valueOf(digit + decimal);
        }
        return Integer.valueOf(digit);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0071  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long getLongSurroundedByWhitespace() {
        /*
            r5 = this;
            r0 = 0
            r6 = r0
            r0 = 0
            r8 = r0
            r0 = 0
            r9 = r0
        L7:
            r0 = r5
            java.io.InputStream r0 = r0.reader     // Catch: java.io.IOException -> L5e
            int r0 = r0.read()     // Catch: java.io.IOException -> L5e
            r10 = r0
            r0 = r10
            if (r0 >= 0) goto L18
            goto L5b
        L18:
            r0 = r10
            char r0 = (char) r0     // Catch: java.io.IOException -> L5e
            boolean r0 = java.lang.Character.isWhitespace(r0)     // Catch: java.io.IOException -> L5e
            if (r0 == 0) goto L29
            r0 = r9
            if (r0 == 0) goto L58
            goto L5b
        L29:
            r0 = r10
            r1 = 45
            if (r0 != r1) goto L38
            r0 = 1
            r8 = r0
            r0 = 1
            r9 = r0
            goto L58
        L38:
            r0 = r10
            r1 = 48
            if (r0 < r1) goto L5b
            r0 = r10
            r1 = 57
            if (r0 > r1) goto L5b
            r0 = r6
            r1 = 10
            long r0 = r0 * r1
            r6 = r0
            r0 = r6
            r1 = r10
            r2 = 48
            int r1 = r1 - r2
            long r1 = (long) r1     // Catch: java.io.IOException -> L5e
            long r0 = r0 + r1
            r6 = r0
            r0 = 1
            r9 = r0
        L58:
            goto L7
        L5b:
            goto L6d
        L5e:
            r10 = move-exception
            java.util.logging.Logger r0 = org.icepdf.core.util.Parser.logger
            java.util.logging.Level r1 = java.util.logging.Level.FINER
            java.lang.String r2 = "Error detecting long."
            r3 = r10
            r0.log(r1, r2, r3)
        L6d:
            r0 = r8
            if (r0 == 0) goto L77
            r0 = r6
            r1 = -1
            long r0 = r0 * r1
            r6 = r0
        L77:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.util.Parser.getLongSurroundedByWhitespace():long");
    }

    public int getLinearTraversalOffset() {
        return this.linearTraversalOffset;
    }

    public char getCharSurroundedByWhitespace() {
        char alpha = 0;
        while (true) {
            try {
                int curr = this.reader.read();
                if (curr < 0) {
                    break;
                }
                char c2 = (char) curr;
                if (!Character.isWhitespace(c2)) {
                    alpha = c2;
                    break;
                }
            } catch (IOException e2) {
                logger.log(Level.FINE, "Error detecting char.", (Throwable) e2);
            }
        }
        return alpha;
    }

    public static boolean isWhitespace(char c2) {
        return c2 == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n' || c2 == '\f';
    }

    private static boolean isDelimiter(char c2) {
        return c2 == '[' || c2 == ']' || c2 == '(' || c2 == ')' || c2 == '<' || c2 == '>' || c2 == '{' || c2 == '}' || c2 == '/' || c2 == '%';
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private long captureStreamData(java.io.OutputStream r6) throws java.io.IOException {
        /*
            r5 = this;
            r0 = 0
            r7 = r0
        L2:
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r9 = r0
            r0 = r9
            r1 = 101(0x65, float:1.42E-43)
            if (r0 != r1) goto L88
            r0 = r5
            java.io.InputStream r0 = r0.reader
            r1 = 10
            r0.mark(r1)
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r1 = 110(0x6e, float:1.54E-43)
            if (r0 != r1) goto L7e
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r1 = 100
            if (r0 != r1) goto L7e
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r1 = 115(0x73, float:1.61E-43)
            if (r0 != r1) goto L7e
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r1 = 116(0x74, float:1.63E-43)
            if (r0 != r1) goto L7e
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r1 = 114(0x72, float:1.6E-43)
            if (r0 != r1) goto L7e
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r1 = 101(0x65, float:1.42E-43)
            if (r0 != r1) goto L7e
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r1 = 97
            if (r0 != r1) goto L7e
            r0 = r5
            java.io.InputStream r0 = r0.reader
            int r0 = r0.read()
            r1 = 109(0x6d, float:1.53E-43)
            if (r0 != r1) goto L7e
            goto La1
        L7e:
            r0 = r5
            java.io.InputStream r0 = r0.reader
            r0.reset()
            goto L90
        L88:
            r0 = r9
            if (r0 >= 0) goto L90
            goto La1
        L90:
            r0 = r6
            if (r0 == 0) goto L9a
            r0 = r6
            r1 = r9
            r0.write(r1)
        L9a:
            r0 = r7
            r1 = 1
            long r0 = r0 + r1
            r7 = r0
            goto L2
        La1:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.util.Parser.captureStreamData(java.io.OutputStream):long");
    }

    private long skipUntilEndstream(OutputStream out) throws IOException {
        long skipped = 0;
        while (true) {
            this.reader.mark(10);
            int nextByte = this.reader.read();
            if (nextByte == 101 && this.reader.read() == 110 && this.reader.read() == 100 && this.reader.read() == 115 && this.reader.read() == 116 && this.reader.read() == 114 && this.reader.read() == 101 && this.reader.read() == 97 && this.reader.read() == 109) {
                this.reader.reset();
                break;
            }
            if (nextByte < 0) {
                break;
            }
            if (nextByte != 10 && nextByte != 13 && nextByte != 32) {
                if (out != null) {
                    out.write(nextByte);
                }
                skipped++;
            }
        }
        return skipped;
    }

    private float parseNumber(StringBuilder stringBuilder) {
        float digit = 0.0f;
        float divisor = 10.0f;
        boolean isDecimal = false;
        int length = stringBuilder.length();
        char[] streamBytes = new char[length];
        stringBuilder.getChars(0, length, streamBytes, 0);
        boolean singed = streamBytes[0] == '-';
        int startTokenPos = singed ? 0 + 1 : 0;
        for (int i2 = startTokenPos; i2 < length; i2++) {
            int current = streamBytes[i2] - '0';
            boolean isDigit = streamBytes[i2] >= '0' && streamBytes[i2] <= '9';
            if (!isDecimal && isDigit) {
                digit = (digit * 10.0f) + current;
            } else if (isDecimal && isDigit) {
                digit += current / divisor;
                divisor *= 10.0f;
            } else {
                if (streamBytes[i2] != '.') {
                    break;
                }
                isDecimal = true;
            }
        }
        if (singed) {
            return -digit;
        }
        return digit;
    }
}
