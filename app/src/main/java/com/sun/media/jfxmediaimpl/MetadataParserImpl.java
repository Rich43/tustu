package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MetadataListener;
import com.sun.media.jfxmedia.locator.ConnectionHolder;
import com.sun.media.jfxmedia.locator.Locator;
import java.io.EOFException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/MetadataParserImpl.class */
public abstract class MetadataParserImpl extends Thread implements MetadataParser {
    private Locator locator;
    private String[] FLV_VIDEO_CODEC_NAME = {"Unsupported", "JPEG Video (Unsupported)", "Sorenson H.263 Video", "Flash Screen Video", "On2 VP6 Video", "On2 VP6-Alpha Video", "Unsupported", "H.264 Video", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported"};
    private final List<WeakReference<MetadataListener>> listeners = new ArrayList();
    private Map<String, Object> metadata = new HashMap();
    private ConnectionHolder connectionHolder = null;
    private ByteBuffer buffer = null;
    private Map<String, ByteBuffer> rawMetaMap = null;
    protected ByteBuffer rawMetaBlob = null;
    private boolean parsingRawMetadata = false;
    private int length = 0;
    private int index = 0;
    private int streamPosition = 0;

    protected abstract void parse();

    public MetadataParserImpl(Locator locator) {
        this.locator = null;
        this.locator = locator;
    }

    @Override // com.sun.media.jfxmedia.MetadataParser
    public void addListener(MetadataListener listener) {
        synchronized (this.listeners) {
            if (listener != null) {
                this.listeners.add(new WeakReference<>(listener));
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MetadataParser
    public void removeListener(MetadataListener listener) {
        synchronized (this.listeners) {
            if (listener != null) {
                ListIterator<WeakReference<MetadataListener>> it = this.listeners.listIterator();
                while (it.hasNext()) {
                    MetadataListener l2 = it.next().get();
                    if (l2 == null || l2 == listener) {
                        it.remove();
                    }
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MetadataParser
    public void startParser() throws IOException {
        start();
    }

    @Override // com.sun.media.jfxmedia.MetadataParser
    public void stopParser() {
        if (this.connectionHolder != null) {
            this.connectionHolder.closeConnection();
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.connectionHolder = this.locator.createConnectionHolder();
            parse();
        } catch (IOException e2) {
        }
    }

    protected void addMetadataItem(String tag, Object value) {
        this.metadata.put(tag, value);
    }

    protected void done() {
        synchronized (this.listeners) {
            if (!this.metadata.isEmpty()) {
                ListIterator<WeakReference<MetadataListener>> it = this.listeners.listIterator();
                while (it.hasNext()) {
                    MetadataListener l2 = it.next().get();
                    if (l2 != null) {
                        l2.onMetadata(this.metadata);
                    } else {
                        it.remove();
                    }
                }
            }
        }
    }

    protected int getStreamPosition() {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.position();
        }
        return this.streamPosition;
    }

    protected void startRawMetadata(int sizeHint) {
        this.rawMetaBlob = ByteBuffer.allocate(sizeHint);
    }

    private void adjustRawMetadataSize(int addSize) {
        if (this.rawMetaBlob.remaining() < addSize) {
            int pos = this.rawMetaBlob.position();
            int newSize = pos + addSize;
            ByteBuffer newBuffer = ByteBuffer.allocate(newSize);
            this.rawMetaBlob.position(0);
            newBuffer.put(this.rawMetaBlob.array(), 0, pos);
            this.rawMetaBlob = newBuffer;
        }
    }

    protected void readRawMetadata(int size) throws IOException {
        byte[] data = getBytes(size);
        adjustRawMetadataSize(size);
        if (null != data) {
            this.rawMetaBlob.put(data);
        }
    }

    protected void stuffRawMetadata(byte[] data, int offset, int size) {
        if (null != this.rawMetaBlob) {
            adjustRawMetadataSize(size);
            this.rawMetaBlob.put(data, offset, size);
        }
    }

    protected void disposeRawMetadata() {
        this.parsingRawMetadata = false;
        this.rawMetaBlob = null;
    }

    protected void setParseRawMetadata(boolean state) {
        if (null == this.rawMetaBlob) {
            this.parsingRawMetadata = false;
            return;
        }
        if (state) {
            this.rawMetaBlob.position(0);
        }
        this.parsingRawMetadata = state;
    }

    protected void addRawMetadata(String type) {
        if (null == this.rawMetaBlob) {
            return;
        }
        if (null == this.rawMetaMap) {
            this.rawMetaMap = new HashMap();
            this.metadata.put(MetadataParser.RAW_METADATA_TAG_NAME, Collections.unmodifiableMap(this.rawMetaMap));
        }
        this.rawMetaMap.put(type, this.rawMetaBlob.asReadOnlyBuffer());
    }

    protected void skipBytes(int num) throws IOException {
        if (this.parsingRawMetadata) {
            this.rawMetaBlob.position(this.rawMetaBlob.position() + num);
            return;
        }
        for (int i2 = 0; i2 < num; i2++) {
            getNextByte();
        }
    }

    protected byte getNextByte() throws IOException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.get();
        }
        if (this.buffer == null) {
            this.buffer = this.connectionHolder.getBuffer();
            this.length = this.connectionHolder.readNextBlock();
        }
        if (this.index >= this.length) {
            this.length = this.connectionHolder.readNextBlock();
            if (this.length < 1) {
                throw new EOFException();
            }
            this.index = 0;
        }
        byte b2 = this.buffer.get(this.index);
        this.index++;
        this.streamPosition++;
        return b2;
    }

    protected byte[] getBytes(int size) throws IOException {
        byte[] bytes = new byte[size];
        if (this.parsingRawMetadata) {
            this.rawMetaBlob.get(bytes);
            return bytes;
        }
        for (int i2 = 0; i2 < size; i2++) {
            bytes[i2] = getNextByte();
        }
        return bytes;
    }

    protected long getLong() throws IOException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.getLong();
        }
        long value = 0 | (getNextByte() & 255);
        return (((((((((((((value << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255);
    }

    protected int getInteger() throws IOException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.getInt();
        }
        int value = 0 | (getNextByte() & 255);
        return (((((value << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255);
    }

    protected short getShort() throws IOException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.getShort();
        }
        short value = (short) (0 | (getNextByte() & 255));
        return (short) (((short) (value << 8)) | (getNextByte() & 255));
    }

    protected double getDouble() throws IOException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.getDouble();
        }
        long bits = getLong();
        return Double.longBitsToDouble(bits);
    }

    protected String getString(int length, Charset charset) throws IOException {
        byte[] bytes = getBytes(length);
        return new String(bytes, 0, length, charset);
    }

    protected int getU24() throws IOException {
        int value = 0 | (getNextByte() & 255);
        return (((value << 8) | (getNextByte() & 255)) << 8) | (getNextByte() & 255);
    }

    protected Object convertValue(String tag, Object value) {
        if (tag.equals("duration") && (value instanceof Double)) {
            return Long.valueOf(Double.valueOf(((Double) value).doubleValue() * 1000.0d).longValue());
        }
        if (tag.equals("duration") && (value instanceof String)) {
            return Long.valueOf(((String) value).trim());
        }
        if (tag.equals(MetadataParser.WIDTH_TAG_NAME) || tag.equals(MetadataParser.HEIGHT_TAG_NAME)) {
            return Integer.valueOf(((Double) value).intValue());
        }
        if (tag.equals(MetadataParser.FRAMERATE_TAG_NAME)) {
            return value;
        }
        if (tag.equals("videocodecid")) {
            int codecid = ((Double) value).intValue();
            if (codecid < this.FLV_VIDEO_CODEC_NAME.length) {
                return this.FLV_VIDEO_CODEC_NAME[codecid];
            }
            return null;
        }
        if (tag.equals("audiocodecid")) {
            return "MPEG 1 Audio";
        }
        if (tag.equals(MetadataParser.CREATIONDATE_TAG_NAME)) {
            return ((String) value).trim();
        }
        if (tag.equals(MetadataParser.TRACKNUMBER_TAG_NAME) || tag.equals(MetadataParser.DISCNUMBER_TAG_NAME)) {
            String[] v2 = ((String) value).split("/");
            if (v2.length == 2) {
                return Integer.valueOf(v2[0].trim());
            }
            return null;
        }
        if (tag.equals(MetadataParser.TRACKCOUNT_TAG_NAME) || tag.equals(MetadataParser.DISCCOUNT_TAG_NAME)) {
            String[] tc = ((String) value).split("/");
            if (tc.length == 2) {
                return Integer.valueOf(tc[1].trim());
            }
            return null;
        }
        if (tag.equals(MetadataParser.ALBUM_TAG_NAME)) {
            return value;
        }
        if (tag.equals(MetadataParser.ARTIST_TAG_NAME)) {
            return value;
        }
        if (tag.equals(MetadataParser.GENRE_TAG_NAME)) {
            return value;
        }
        if (tag.equals("title")) {
            return value;
        }
        if (tag.equals(MetadataParser.ALBUMARTIST_TAG_NAME)) {
            return value;
        }
        if (tag.equals("comment")) {
            return value;
        }
        if (tag.equals(MetadataParser.COMPOSER_TAG_NAME)) {
            return value;
        }
        if (tag.equals(MetadataParser.YEAR_TAG_NAME)) {
            return Integer.valueOf(((String) value).trim());
        }
        return null;
    }
}
