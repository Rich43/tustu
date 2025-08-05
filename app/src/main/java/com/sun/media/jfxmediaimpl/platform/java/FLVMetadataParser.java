package com.sun.media.jfxmediaimpl.platform.java;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MetadataParserImpl;
import java.io.IOException;
import java.nio.charset.Charset;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/java/FLVMetadataParser.class */
final class FLVMetadataParser extends MetadataParserImpl {
    private int dataSize;
    private static final String CHARSET_UTF_8 = "UTF-8";

    public FLVMetadataParser(Locator locator) {
        super(locator);
        this.dataSize = 0;
    }

    @Override // com.sun.media.jfxmediaimpl.MetadataParserImpl
    protected void parse() {
        try {
            if (getNextByte() == 70 && getNextByte() == 76 && getNextByte() == 86) {
                skipBytes(2);
                int dataOffset = getInteger();
                skipBytes(dataOffset - 9);
                for (int tagCount = 0; tagCount < 10; tagCount++) {
                    skipBytes(4);
                    byte tagType = getNextByte();
                    this.dataSize = getU24();
                    skipBytes(7);
                    if (tagType == 18) {
                        int expectedEndPosition = getStreamPosition() + this.dataSize;
                        if (parseDataTag()) {
                            break;
                        } else if (getStreamPosition() < expectedEndPosition) {
                            skipBytes(expectedEndPosition - getStreamPosition());
                        }
                    } else {
                        skipBytes(this.dataSize);
                    }
                }
            }
        } catch (IOException e2) {
        }
    }

    private boolean parseDataTag() throws IOException {
        int nameSize;
        Object value;
        if (this.dataSize < 14) {
            return false;
        }
        byte[] header = new byte[14];
        for (int ii = 0; ii < 14; ii++) {
            header[ii] = getNextByte();
        }
        if (header[0] != 2 || (nameSize = ((header[1] & 255) << 8) | (header[2] & 255)) != 10 || !Charset.isSupported("UTF-8")) {
            return false;
        }
        String methodName = new String(header, 3, nameSize, Charset.forName("UTF-8"));
        if (!methodName.equals("onMetaData")) {
            return false;
        }
        if (header[13] != 8) {
            if (Logger.canLog(3)) {
                Logger.logMsg(3, "FLV metadata must be in an ECMA array");
                return false;
            }
            return false;
        }
        startRawMetadata(this.dataSize);
        if (null == this.rawMetaBlob) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Unable to allocate buffer for FLV metadata");
                return false;
            }
            return false;
        }
        stuffRawMetadata(header, 0, 14);
        readRawMetadata(this.dataSize - 14);
        setParseRawMetadata(true);
        skipBytes(14);
        try {
            try {
                int arrayCount = getInteger();
                int parseCount = 0;
                boolean done = false;
                boolean warnMalformed = false;
                do {
                    String attribute = getString(getShort(), Charset.forName("UTF-8"));
                    FlvDataValue flvValue = readDataValue(false);
                    parseCount++;
                    String tag = convertTag(attribute);
                    if (Logger.canLog(1) && !attribute.equals("")) {
                        Logger.logMsg(1, parseCount + ": \"" + attribute + "\" -> " + (null == tag ? "(unsupported)" : PdfOps.DOUBLE_QUOTE__TOKEN + tag + PdfOps.DOUBLE_QUOTE__TOKEN));
                    }
                    if (tag != null && (value = convertValue(attribute, flvValue.obj)) != null) {
                        addMetadataItem(tag, value);
                    }
                    if (parseCount >= arrayCount) {
                        if (getStreamPosition() < this.dataSize) {
                            if (!warnMalformed && Logger.canLog(3)) {
                                Logger.logMsg(3, "FLV Source has malformed metadata, invalid ECMA element count");
                                warnMalformed = true;
                            }
                        } else {
                            done = true;
                        }
                    }
                } while (!done);
                if (null != this.rawMetaBlob) {
                    setParseRawMetadata(false);
                    addRawMetadata(MetadataParser.RAW_FLV_METADATA_NAME);
                    disposeRawMetadata();
                }
                done();
                return true;
            } catch (Exception e2) {
                if (Logger.canLog(3)) {
                    Logger.logMsg(3, "Exception while processing FLV metadata: " + ((Object) e2));
                }
                if (null != this.rawMetaBlob) {
                    setParseRawMetadata(false);
                    addRawMetadata(MetadataParser.RAW_FLV_METADATA_NAME);
                    disposeRawMetadata();
                }
                done();
                return true;
            }
        } catch (Throwable th) {
            if (null != this.rawMetaBlob) {
                setParseRawMetadata(false);
                addRawMetadata(MetadataParser.RAW_FLV_METADATA_NAME);
                disposeRawMetadata();
            }
            done();
            throw th;
        }
    }

    private FlvDataValue readDataValue(boolean hasName) throws IOException {
        FlvDataValue sdv = new FlvDataValue();
        if (hasName) {
            skipBytes(getShort());
        }
        sdv.type = getNextByte();
        switch (sdv.type) {
            case 0:
                sdv.obj = Double.valueOf(getDouble());
                break;
            case 1:
                boolean b2 = getNextByte() != 0;
                sdv.obj = Boolean.valueOf(b2);
                break;
            case 2:
                sdv.obj = getString(getShort(), Charset.forName("UTF-8"));
                break;
            case 3:
                skipObject();
                break;
            case 4:
                getString(getShort(), Charset.forName("UTF-8"));
                break;
            case 7:
                skipBytes(2);
                break;
            case 8:
                skipArray();
                break;
            case 9:
                sdv.scriptDataObjectEnd = true;
                break;
            case 10:
                skipStrictArray();
                break;
            case 11:
                sdv.obj = Double.valueOf(getDouble());
                skipBytes(2);
                break;
            case 12:
                sdv.obj = getString(getInteger(), Charset.forName("UTF-8"));
                break;
        }
        return sdv;
    }

    private void skipObject() throws IOException {
        FlvDataValue value;
        do {
            value = readDataValue(true);
        } while (!value.scriptDataObjectEnd);
    }

    private void skipArray() throws IOException {
        int arrayCount = getInteger();
        for (int parseCount = 0; parseCount < arrayCount; parseCount++) {
            readDataValue(true);
        }
    }

    private void skipStrictArray() throws IOException {
        long arrayLen = getInteger();
        for (int i2 = 0; i2 < arrayLen; i2++) {
            readDataValue(false);
        }
    }

    private String convertTag(String tag) {
        if (tag.equals("duration")) {
            return "duration";
        }
        if (tag.equals(MetadataParser.WIDTH_TAG_NAME)) {
            return MetadataParser.WIDTH_TAG_NAME;
        }
        if (tag.equals(MetadataParser.HEIGHT_TAG_NAME)) {
            return MetadataParser.HEIGHT_TAG_NAME;
        }
        if (tag.equals(MetadataParser.FRAMERATE_TAG_NAME)) {
            return MetadataParser.FRAMERATE_TAG_NAME;
        }
        if (tag.equals("videocodecid")) {
            return MetadataParser.VIDEOCODEC_TAG_NAME;
        }
        if (tag.equals("audiocodecid")) {
            return MetadataParser.AUDIOCODEC_TAG_NAME;
        }
        if (tag.equals(MetadataParser.CREATIONDATE_TAG_NAME)) {
            return MetadataParser.CREATIONDATE_TAG_NAME;
        }
        return null;
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/java/FLVMetadataParser$FlvDataValue.class */
    private static class FlvDataValue {
        static final byte NUMBER = 0;
        static final byte BOOLEAN = 1;
        static final byte STRING = 2;
        static final byte OBJECT = 3;
        static final byte MOVIE_CLIP = 4;
        static final byte NULL = 5;
        static final byte UNDEFINED = 6;
        static final byte REFERENCE = 7;
        static final byte ECMA_ARRAY = 8;
        static final byte END_OF_DATA = 9;
        static final byte STRICT_ARRAY = 10;
        static final byte DATE = 11;
        static final byte LONG_STRING = 12;
        boolean scriptDataObjectEnd;
        Object obj;
        byte type;

        private FlvDataValue() {
            this.scriptDataObjectEnd = false;
        }
    }
}
