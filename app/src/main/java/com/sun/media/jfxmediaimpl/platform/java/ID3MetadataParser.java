package com.sun.media.jfxmediaimpl.platform.java;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MetadataParserImpl;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/java/ID3MetadataParser.class */
final class ID3MetadataParser extends MetadataParserImpl {
    private static final int ID3_VERSION_MIN = 2;
    private static final int ID3_VERSION_MAX = 4;
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    private static final String CHARSET_UTF_16 = "UTF-16";
    private static final String CHARSET_UTF_16BE = "UTF-16BE";
    private int COMMCount;
    private int TXXXCount;
    private int version;
    private boolean unsynchronized;

    public ID3MetadataParser(Locator locator) {
        super(locator);
        this.COMMCount = 0;
        this.TXXXCount = 0;
        this.version = 3;
        this.unsynchronized = false;
    }

    @Override // com.sun.media.jfxmediaimpl.MetadataParserImpl
    protected void parse() {
        byte[] idBytes;
        int frameSize;
        String comment;
        try {
            try {
                if (!Charset.isSupported("ISO-8859-1")) {
                    throw new UnsupportedCharsetException("ISO-8859-1");
                }
                byte[] buf = getBytes(10);
                this.version = buf[3] & 255;
                if (buf[0] == 73 && buf[1] == 68 && buf[2] == 51 && this.version >= 2 && this.version <= 4) {
                    int flags = buf[5] & 255;
                    if ((flags & 128) == 128) {
                        this.unsynchronized = true;
                    }
                    int tagSize = 0;
                    int shift = 21;
                    for (int i2 = 6; i2 < 10; i2++) {
                        tagSize += (buf[i2] & Byte.MAX_VALUE) << shift;
                        shift -= 7;
                    }
                    startRawMetadata(tagSize + 10);
                    stuffRawMetadata(buf, 0, 10);
                    readRawMetadata(tagSize);
                    setParseRawMetadata(true);
                    skipBytes(10);
                    while (true) {
                        if (getStreamPosition() >= tagSize) {
                            break;
                        }
                        if (2 == this.version) {
                            idBytes = getBytes(3);
                            frameSize = getU24();
                        } else {
                            idBytes = getBytes(4);
                            frameSize = getFrameSize();
                            skipBytes(2);
                        }
                        if (0 == idBytes[0]) {
                            if (Logger.canLog(1)) {
                                Logger.logMsg(1, "ID3MetadataParser", "parse", "ID3 parser: zero padding detected at " + getStreamPosition() + ", terminating");
                            }
                        } else {
                            String frameID = new String(idBytes, Charset.forName("ISO-8859-1"));
                            if (Logger.canLog(1)) {
                                Logger.logMsg(1, "ID3MetadataParser", "parse", getStreamPosition() + FXMLLoader.ESCAPE_PREFIX + tagSize + ": frame ID " + frameID + ", size " + frameSize);
                            }
                            if (frameID.equals("APIC") || frameID.equals("PIC")) {
                                byte[] data = getBytes(frameSize);
                                if (this.unsynchronized) {
                                    data = unsynchronizeBuffer(data);
                                }
                                byte[] image = frameID.equals("PIC") ? getImageFromPIC(data) : getImageFromAPIC(data);
                                if (image != null) {
                                    addMetadataItem(MetadataParser.IMAGE_TAG_NAME, image);
                                }
                            } else if (frameID.startsWith("T") && !frameID.equals("TXXX")) {
                                String encoding = getEncoding();
                                byte[] data2 = getBytes(frameSize - 1);
                                if (this.unsynchronized) {
                                    data2 = unsynchronizeBuffer(data2);
                                }
                                String value = new String(data2, encoding);
                                String[] tag = getTagFromFrameID(frameID);
                                if (tag != null) {
                                    for (int i3 = 0; i3 < tag.length; i3++) {
                                        Object tagValue = convertValue(tag[i3], value);
                                        if (tagValue != null) {
                                            addMetadataItem(tag[i3], tagValue);
                                        }
                                    }
                                }
                            } else if (frameID.equals("COMM") || frameID.equals("COM")) {
                                String encoding2 = getEncoding();
                                byte[] data3 = getBytes(3);
                                if (this.unsynchronized) {
                                    data3 = unsynchronizeBuffer(data3);
                                }
                                String language = new String(data3, Charset.forName("ISO-8859-1"));
                                byte[] data4 = getBytes(frameSize - 4);
                                if (this.unsynchronized) {
                                    data4 = unsynchronizeBuffer(data4);
                                }
                                String value2 = new String(data4, encoding2);
                                if (value2 != null) {
                                    int index = value2.indexOf(0);
                                    String content = "";
                                    if (index == 0) {
                                        if (isTwoByteEncoding(encoding2)) {
                                            comment = value2.substring(2);
                                        } else {
                                            comment = value2.substring(1);
                                        }
                                    } else {
                                        content = value2.substring(0, index);
                                        if (isTwoByteEncoding(encoding2)) {
                                            comment = value2.substring(index + 2);
                                        } else {
                                            comment = value2.substring(index + 1);
                                        }
                                    }
                                    String[] tag2 = getTagFromFrameID(frameID);
                                    if (tag2 != null) {
                                        for (String str : tag2) {
                                            addMetadataItem(str + LanguageTag.SEP + this.COMMCount, content + "[" + language + "]=" + comment);
                                            this.COMMCount++;
                                        }
                                    }
                                }
                            } else if (frameID.equals("TXX") || frameID.equals("TXXX")) {
                                String encoding3 = getEncoding();
                                byte[] data5 = getBytes(frameSize - 1);
                                if (this.unsynchronized) {
                                    data5 = unsynchronizeBuffer(data5);
                                }
                                String value3 = new String(data5, encoding3);
                                if (null != value3) {
                                    int index2 = value3.indexOf(0);
                                    String description = index2 != 0 ? value3.substring(0, index2) : "";
                                    String text = isTwoByteEncoding(encoding3) ? value3.substring(index2 + 2) : value3.substring(index2 + 1);
                                    String[] tag3 = getTagFromFrameID(frameID);
                                    if (tag3 != null) {
                                        for (int i4 = 0; i4 < tag3.length; i4++) {
                                            if (description.equals("")) {
                                                addMetadataItem(tag3[i4] + LanguageTag.SEP + this.TXXXCount, text);
                                            } else {
                                                addMetadataItem(tag3[i4] + LanguageTag.SEP + this.TXXXCount, description + "=" + text);
                                            }
                                            this.TXXXCount++;
                                        }
                                    }
                                }
                            } else {
                                skipBytes(frameSize);
                            }
                        }
                    }
                }
                if (null != this.rawMetaBlob) {
                    setParseRawMetadata(false);
                    addRawMetadata(MetadataParser.RAW_ID3_METADATA_NAME);
                    disposeRawMetadata();
                }
                done();
            } catch (Exception ex) {
                if (Logger.canLog(3)) {
                    Logger.logMsg(3, "ID3MetadataParser", "parse", "Exception while processing ID3v2 metadata: " + ((Object) ex));
                }
                if (null != this.rawMetaBlob) {
                    setParseRawMetadata(false);
                    addRawMetadata(MetadataParser.RAW_ID3_METADATA_NAME);
                    disposeRawMetadata();
                }
                done();
            }
        } catch (Throwable th) {
            if (null != this.rawMetaBlob) {
                setParseRawMetadata(false);
                addRawMetadata(MetadataParser.RAW_ID3_METADATA_NAME);
                disposeRawMetadata();
            }
            done();
            throw th;
        }
    }

    private int getFrameSize() throws IOException {
        if (this.version == 4) {
            byte[] buf = getBytes(4);
            int size = 0;
            int shift = 21;
            for (int i2 = 0; i2 < 4; i2++) {
                size += (buf[i2] & Byte.MAX_VALUE) << shift;
                shift -= 7;
            }
            return size;
        }
        return getInteger();
    }

    private String getEncoding() throws IOException {
        byte encodingType = getNextByte();
        if (encodingType == 0) {
            return "ISO-8859-1";
        }
        if (encodingType == 1) {
            return CHARSET_UTF_16;
        }
        if (encodingType == 2) {
            return "UTF-16BE";
        }
        if (encodingType == 3) {
            return "UTF-8";
        }
        throw new IllegalArgumentException();
    }

    private boolean isTwoByteEncoding(String encoding) {
        if (encoding.equals("ISO-8859-1") || encoding.equals("UTF-8")) {
            return false;
        }
        if (encoding.equals(CHARSET_UTF_16) || encoding.equals("UTF-16BE")) {
            return true;
        }
        throw new IllegalArgumentException();
    }

    private String[] getTagFromFrameID(String frameID) {
        if (frameID.equals("TPE2") || frameID.equals("TP2")) {
            return new String[]{MetadataParser.ALBUMARTIST_TAG_NAME};
        }
        if (frameID.equals("TALB") || frameID.equals("TAL")) {
            return new String[]{MetadataParser.ALBUM_TAG_NAME};
        }
        if (frameID.equals("TPE1") || frameID.equals("TP1")) {
            return new String[]{MetadataParser.ARTIST_TAG_NAME};
        }
        if (frameID.equals("COMM") || frameID.equals("COM")) {
            return new String[]{"comment"};
        }
        if (frameID.equals("TCOM") || frameID.equals("TCM")) {
            return new String[]{MetadataParser.COMPOSER_TAG_NAME};
        }
        if (frameID.equals("TLEN") || frameID.equals("TLE")) {
            return new String[]{"duration"};
        }
        if (frameID.equals("TCON") || frameID.equals("TCO")) {
            return new String[]{MetadataParser.GENRE_TAG_NAME};
        }
        if (frameID.equals("TIT2") || frameID.equals("TT2")) {
            return new String[]{"title"};
        }
        if (frameID.equals("TRCK") || frameID.equals("TRK")) {
            return new String[]{MetadataParser.TRACKNUMBER_TAG_NAME, MetadataParser.TRACKCOUNT_TAG_NAME};
        }
        if (frameID.equals("TPOS") || frameID.equals("TPA")) {
            return new String[]{MetadataParser.DISCNUMBER_TAG_NAME, MetadataParser.DISCCOUNT_TAG_NAME};
        }
        if (frameID.equals("TYER") || frameID.equals("TDRC")) {
            return new String[]{MetadataParser.YEAR_TAG_NAME};
        }
        if (frameID.equals("TXX") || frameID.equals("TXXX")) {
            return new String[]{"text"};
        }
        return null;
    }

    private byte[] getImageFromPIC(byte[] data) {
        int imgOffset = 5;
        while (0 != data[imgOffset] && imgOffset < data.length) {
            imgOffset++;
        }
        if (imgOffset == data.length) {
            return null;
        }
        String type = new String(data, 1, 3, Charset.forName("ISO-8859-1"));
        if (Logger.canLog(1)) {
            Logger.logMsg(1, "ID3MetadataParser", "getImageFromPIC", "PIC type: " + type);
        }
        if (type.equalsIgnoreCase("PNG") || type.equalsIgnoreCase("JPG")) {
            return Arrays.copyOfRange(data, imgOffset + 1, data.length);
        }
        if (Logger.canLog(3)) {
            Logger.logMsg(3, "ID3MetadataParser", "getImageFromPIC", "Unsupported picture type found \"" + type + PdfOps.DOUBLE_QUOTE__TOKEN);
            return null;
        }
        return null;
    }

    private byte[] getImageFromAPIC(byte[] data) {
        boolean isImageJPEG = false;
        boolean isImagePNG = false;
        int maxIndex = data.length - 10;
        int offset = 0;
        int j2 = 0;
        while (true) {
            if (j2 >= maxIndex) {
                break;
            }
            if (data[j2] == 105 && data[j2 + 1] == 109 && data[j2 + 2] == 97 && data[j2 + 3] == 103 && data[j2 + 4] == 101 && data[j2 + 5] == 47) {
                j2 += 6;
                if (data[j2] == 106 && data[j2 + 1] == 112 && data[j2 + 2] == 101 && data[j2 + 3] == 103) {
                    isImageJPEG = true;
                    offset = j2 + 4;
                    break;
                }
                if (data[j2] == 112 && data[j2 + 1] == 110 && data[j2 + 2] == 103) {
                    isImagePNG = true;
                    offset = j2 + 3;
                    break;
                }
            }
            j2++;
        }
        if (isImageJPEG) {
            boolean isSignatureFound = false;
            int upperBound = data.length - 1;
            int j3 = offset;
            while (true) {
                if (j3 < upperBound) {
                    if (-1 != data[j3] || -40 != data[j3 + 1]) {
                        j3++;
                    } else {
                        isSignatureFound = true;
                        offset = j3;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (isSignatureFound) {
                return Arrays.copyOfRange(data, offset, data.length);
            }
        }
        if (isImagePNG) {
            boolean isSignatureFound2 = false;
            int upperBound2 = data.length - 7;
            int j4 = offset;
            while (true) {
                if (j4 < upperBound2) {
                    if (-119 != data[j4] || 80 != data[j4 + 1] || 78 != data[j4 + 2] || 71 != data[j4 + 3] || 13 != data[j4 + 4] || 10 != data[j4 + 5] || 26 != data[j4 + 6] || 10 != data[j4 + 7]) {
                        j4++;
                    } else {
                        isSignatureFound2 = true;
                        offset = j4;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (isSignatureFound2) {
                return Arrays.copyOfRange(data, offset, data.length);
            }
            return null;
        }
        return null;
    }

    private byte[] unsynchronizeBuffer(byte[] data) {
        byte[] udata = new byte[data.length];
        int udatalen = 0;
        int i2 = 0;
        while (i2 < data.length) {
            if (((data[i2] & 255) == 255 && data[i2 + 1] == 0 && data[i2 + 2] == 0) || ((data[i2] & 255) == 255 && data[i2 + 1] == 0 && (data[i2 + 2] & 224) == 224)) {
                udata[udatalen] = data[i2];
                int udatalen2 = udatalen + 1;
                udata[udatalen2] = data[i2 + 2];
                udatalen = udatalen2 + 1;
                i2 += 2;
            } else {
                udata[udatalen] = data[i2];
                udatalen++;
            }
            i2++;
        }
        return Arrays.copyOf(udata, udatalen);
    }
}
