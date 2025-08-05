package org.icepdf.core.pobjects.fonts.ofont;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.io.SeekableInput;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Parser;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/ofont/CMap.class */
class CMap extends Dictionary implements org.icepdf.core.pobjects.fonts.CMap {
    private static final Logger logger = Logger.getLogger(CMap.class.toString());
    private HashMap cIdSystemInfo;
    private String cMapName;
    private float cMapType;
    private Object useCMap;
    private int wMode;
    private int[][] codeSpaceRange;
    private boolean oneByte;
    private HashMap<Integer, char[]> bfChars;
    private List<CMapRange> bfRange;
    private HashMap cIdChars;
    private HashMap cIdRange;
    private HashMap notDefChars;
    private HashMap notDefRange;
    private Stream cMapStream;
    private InputStream cMapInputStream;

    public CMap(Library library, HashMap entries, Stream cMapStream) {
        super(library, entries);
        this.cMapStream = cMapStream;
    }

    public CMap(Library l2, HashMap h2, InputStream cMapInputStream) {
        super(l2, h2);
        this.cMapInputStream = cMapInputStream;
    }

    @Override // org.icepdf.core.pobjects.fonts.CMap
    public boolean isOneByte(int cid) {
        return this.oneByte;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public void init() {
        String content;
        try {
            try {
                if (this.cMapInputStream == null) {
                    this.cMapInputStream = this.cMapStream.getDecodedByteArrayInputStream();
                }
                if (logger.isLoggable(Level.FINER)) {
                    if (this.cMapInputStream instanceof SeekableInput) {
                        content = Utils.getContentFromSeekableInput((SeekableInput) this.cMapInputStream, false);
                    } else {
                        InputStream[] inArray = {this.cMapInputStream};
                        content = Utils.getContentAndReplaceInputStream(inArray, false);
                        this.cMapInputStream = inArray[0];
                    }
                    logger.finer("<------------------------ CMap");
                    logger.finer(content);
                    logger.finer("CMap ------------------------>  ");
                }
                Parser parser = new Parser(this.cMapInputStream);
                Object previousToken = null;
                while (true) {
                    Object token = parser.getStreamObject();
                    if (token == null) {
                        break;
                    }
                    if (token.toString().toLowerCase().indexOf("cidsysteminfo") >= 0) {
                        token = parser.getStreamObject();
                        if (token instanceof HashMap) {
                            this.cIdSystemInfo = (HashMap) token;
                            token = parser.getStreamObject();
                        }
                    }
                    if (token instanceof Name) {
                        String nameString = token.toString();
                        if (nameString.toLowerCase().indexOf("cmapname") >= 0) {
                            this.cMapName = parser.getStreamObject().toString();
                            token = parser.getStreamObject();
                        }
                        if (nameString.toLowerCase().indexOf("cmaptype") >= 0) {
                            this.cMapType = Float.parseFloat(parser.getStreamObject().toString());
                            token = parser.getStreamObject();
                        }
                        if (nameString.toLowerCase().indexOf("usemap") >= 0) {
                        }
                    }
                    if (token instanceof String) {
                        String stringToken = (String) token;
                        if (stringToken.equalsIgnoreCase("begincodespacerange")) {
                            int numberOfRanges = (int) Float.parseFloat(previousToken.toString());
                            this.codeSpaceRange = new int[numberOfRanges][2];
                            for (int i2 = 0; i2 < numberOfRanges; i2++) {
                                StringObject hexToken = (StringObject) parser.getStreamObject();
                                int startRange = hexToken.getUnsignedInt(0, hexToken.getLength());
                                token = parser.getStreamObject();
                                StringObject hexToken2 = (StringObject) token;
                                int length = hexToken2.getLength();
                                int endRange = hexToken2.getUnsignedInt(0, length);
                                this.codeSpaceRange[i2][0] = startRange;
                                this.codeSpaceRange[i2][1] = endRange;
                                if (length == 2) {
                                    this.oneByte = true;
                                }
                            }
                        }
                        if (stringToken.equalsIgnoreCase("beginbfchar")) {
                            int numberOfbfChar = (int) Float.parseFloat(previousToken.toString());
                            if (this.bfChars == null) {
                                this.bfChars = new HashMap<>(numberOfbfChar);
                            }
                            for (int i3 = 0; i3 < numberOfbfChar; i3++) {
                                StringObject hexToken3 = (StringObject) parser.getStreamObject();
                                Integer key = Integer.valueOf(hexToken3.getUnsignedInt(0, hexToken3.getLength()));
                                token = parser.getStreamObject();
                                char[] value = null;
                                try {
                                    value = convertToString(((StringObject) token).getLiteralStringBuffer());
                                } catch (NumberFormatException e2) {
                                    logger.log(Level.FINE, "CMAP: ", (Throwable) e2);
                                }
                                this.bfChars.put(key, value);
                            }
                        }
                        if (stringToken.equalsIgnoreCase("beginbfrange")) {
                            int numberOfbfRanges = (int) Float.parseFloat(previousToken.toString());
                            if (this.bfRange == null) {
                                this.bfRange = new ArrayList(numberOfbfRanges);
                            }
                            for (int i4 = 0; i4 < numberOfbfRanges; i4++) {
                                token = parser.getStreamObject();
                                if (!(token instanceof StringObject)) {
                                    break;
                                }
                                StringObject hexToken4 = (StringObject) token;
                                Integer startRange2 = Integer.valueOf(hexToken4.getUnsignedInt(0, hexToken4.getLength()));
                                token = parser.getStreamObject();
                                if (!(token instanceof StringObject)) {
                                    break;
                                }
                                StringObject hexToken5 = (StringObject) token;
                                Integer endRange2 = Integer.valueOf(hexToken5.getUnsignedInt(0, hexToken5.getLength()));
                                token = parser.getStreamObject();
                                if (token instanceof List) {
                                    this.bfRange.add(new CMapRange(startRange2.intValue(), endRange2.intValue(), (List) token));
                                } else {
                                    StringObject hexToken6 = (StringObject) token;
                                    Integer offset = Integer.valueOf(hexToken6.getUnsignedInt(0, hexToken6.getLength()));
                                    this.bfRange.add(new CMapRange(startRange2.intValue(), endRange2.intValue(), offset.intValue()));
                                }
                            }
                        }
                        if (stringToken.equalsIgnoreCase("begincidchar")) {
                        }
                        if (stringToken.equalsIgnoreCase("begincidrange")) {
                        }
                        if (stringToken.equalsIgnoreCase("beginnotdefchar")) {
                        }
                        if (stringToken.equalsIgnoreCase("beginnotdefrange")) {
                        }
                    }
                    previousToken = token;
                }
                if (this.cMapInputStream != null) {
                    try {
                        this.cMapInputStream.close();
                    } catch (IOException e3) {
                        logger.log(Level.FINE, "Error clossing cmap stream", (Throwable) e3);
                    }
                }
            } catch (UnsupportedEncodingException e4) {
                logger.log(Level.SEVERE, "CMap parsing error", (Throwable) e4);
                if (this.cMapInputStream != null) {
                    try {
                        this.cMapInputStream.close();
                    } catch (IOException e5) {
                        logger.log(Level.FINE, "Error clossing cmap stream", (Throwable) e5);
                    }
                }
            } catch (IOException e6) {
                if (this.cMapInputStream != null) {
                    try {
                        this.cMapInputStream.close();
                    } catch (IOException e7) {
                        logger.log(Level.FINE, "Error clossing cmap stream", (Throwable) e7);
                    }
                }
            }
        } catch (Throwable th) {
            if (this.cMapInputStream != null) {
                try {
                    this.cMapInputStream.close();
                } catch (IOException e8) {
                    logger.log(Level.FINE, "Error clossing cmap stream", (Throwable) e8);
                }
            }
            throw th;
        }
    }

    @Override // org.icepdf.core.pobjects.fonts.CMap
    public String toUnicode(char ch) {
        char[] tmp;
        if (this.bfChars != null && (tmp = this.bfChars.get(Integer.valueOf(ch))) != null) {
            return String.valueOf(tmp);
        }
        if (this.bfRange != null) {
            for (CMapRange aBfRange : this.bfRange) {
                if (aBfRange.inRange(ch)) {
                    return String.valueOf(aBfRange.getCMapValue(ch));
                }
            }
        }
        return String.valueOf(ch);
    }

    @Override // org.icepdf.core.pobjects.fonts.CMap
    public char toSelector(char charMap) {
        char[] tmp;
        if (this.bfChars != null && (tmp = this.bfChars.get(Integer.valueOf(charMap))) != null) {
            return tmp[0];
        }
        if (this.bfRange != null) {
            for (CMapRange aBfRange : this.bfRange) {
                if (aBfRange.inRange(charMap)) {
                    return aBfRange.getCMapValue(charMap)[0];
                }
            }
        }
        return charMap;
    }

    @Override // org.icepdf.core.pobjects.fonts.CMap
    public char toSelector(char charMap, boolean isCFF) {
        return toSelector(charMap);
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/ofont/CMap$CMapRange.class */
    class CMapRange {
        int startRange;
        int endRange;
        int offsetValue;
        List offsetVecor;

        public CMapRange(int startRange, int endRange, int offsetValue) {
            this.startRange = 0;
            this.endRange = 0;
            this.offsetValue = 0;
            this.offsetVecor = null;
            this.startRange = startRange;
            this.endRange = endRange;
            this.offsetValue = offsetValue;
        }

        public CMapRange(int startRange, int endRange, List offsetVecor) {
            this.startRange = 0;
            this.endRange = 0;
            this.offsetValue = 0;
            this.offsetVecor = null;
            this.startRange = startRange;
            this.endRange = endRange;
            this.offsetVecor = offsetVecor;
        }

        public boolean inRange(int value) {
            return value >= this.startRange && value <= this.endRange;
        }

        public char[] getCMapValue(int value) {
            if (this.offsetVecor == null) {
                return new char[]{(char) (this.offsetValue + (value - this.startRange))};
            }
            StringObject hexToken = (StringObject) this.offsetVecor.get(value - this.startRange);
            char[] test = CMap.this.convertToString(hexToken.getLiteralStringBuffer());
            return test;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public char[] convertToString(CharSequence s2) {
        if (s2 == null && s2.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        int len = s2.length();
        char[] dest = new char[len / 2];
        int i2 = 0;
        int j2 = 0;
        while (i2 < len) {
            dest[j2] = (char) ((s2.charAt(i2) << '\b') | s2.charAt(i2 + 1));
            i2 += 2;
            j2++;
        }
        return dest;
    }
}
