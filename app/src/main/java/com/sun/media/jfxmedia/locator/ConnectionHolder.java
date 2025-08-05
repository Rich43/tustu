package com.sun.media.jfxmedia.locator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/ConnectionHolder.class */
public abstract class ConnectionHolder {
    private static int DEFAULT_BUFFER_SIZE = 4096;
    ReadableByteChannel channel;
    ByteBuffer buffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);

    abstract int readBlock(long j2, int i2) throws IOException;

    abstract boolean needBuffer();

    abstract boolean isSeekable();

    abstract boolean isRandomAccess();

    public abstract long seek(long j2);

    static ConnectionHolder createMemoryConnectionHolder(ByteBuffer buffer) {
        return new MemoryConnectionHolder(buffer);
    }

    static ConnectionHolder createURIConnectionHolder(URI uri, Map<String, Object> connectionProperties) throws IOException {
        return new URIConnectionHolder(uri, connectionProperties);
    }

    static ConnectionHolder createFileConnectionHolder(URI uri) throws IOException {
        return new FileConnectionHolder(uri);
    }

    static ConnectionHolder createHLSConnectionHolder(URI uri) throws IOException {
        return new HLSConnectionHolder(uri);
    }

    public int readNextBlock() throws IOException {
        this.buffer.rewind();
        if (this.buffer.limit() < this.buffer.capacity()) {
            this.buffer.limit(this.buffer.capacity());
        }
        if (null == this.channel) {
            throw new ClosedChannelException();
        }
        return this.channel.read(this.buffer);
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public void closeConnection() {
        try {
            if (this.channel != null) {
                this.channel.close();
            }
        } catch (IOException e2) {
        } finally {
            this.channel = null;
        }
    }

    int property(int prop, int value) {
        return 0;
    }

    int getStreamSize() {
        return -1;
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/ConnectionHolder$FileConnectionHolder.class */
    private static class FileConnectionHolder extends ConnectionHolder {
        private RandomAccessFile file = null;

        FileConnectionHolder(URI uri) throws IOException {
            this.channel = openFile(uri);
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean needBuffer() {
            return false;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean isRandomAccess() {
            return true;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean isSeekable() {
            return true;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        public long seek(long position) {
            try {
                ((FileChannel) this.channel).position(position);
                return position;
            } catch (IOException e2) {
                return -1L;
            }
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        int readBlock(long position, int size) throws IOException {
            if (null == this.channel) {
                throw new ClosedChannelException();
            }
            if (this.buffer.capacity() < size) {
                this.buffer = ByteBuffer.allocateDirect(size);
            }
            this.buffer.rewind().limit(size);
            return ((FileChannel) this.channel).read(this.buffer, position);
        }

        private ReadableByteChannel openFile(URI uri) throws IOException {
            if (this.file != null) {
                this.file.close();
            }
            this.file = new RandomAccessFile(new File(uri), InternalZipConstants.READ_MODE);
            return this.file.getChannel();
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        public void closeConnection() {
            super.closeConnection();
            if (this.file != null) {
                try {
                    this.file.close();
                } catch (IOException e2) {
                } finally {
                    this.file = null;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/ConnectionHolder$URIConnectionHolder.class */
    private static class URIConnectionHolder extends ConnectionHolder {
        private URI uri;
        private URLConnection urlConnection;

        URIConnectionHolder(URI uri, Map<String, Object> connectionProperties) throws IOException {
            this.uri = uri;
            this.urlConnection = uri.toURL().openConnection();
            if (connectionProperties != null) {
                for (Map.Entry<String, Object> entry : connectionProperties.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        this.urlConnection.setRequestProperty(entry.getKey(), (String) value);
                    }
                }
            }
            this.channel = openChannel(null);
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean needBuffer() {
            String scheme = this.uri.getScheme().toLowerCase();
            return "http".equals(scheme) || "https".equals(scheme);
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean isSeekable() {
            return (this.urlConnection instanceof HttpURLConnection) || (this.urlConnection instanceof JarURLConnection);
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean isRandomAccess() {
            return false;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        int readBlock(long position, int size) throws IOException {
            throw new IOException();
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        public long seek(long position) {
            if (!(this.urlConnection instanceof HttpURLConnection)) {
                if (!(this.urlConnection instanceof JarURLConnection)) {
                    return -1L;
                }
                try {
                    closeConnection();
                    this.urlConnection = this.uri.toURL().openConnection();
                    long skip_left = position;
                    InputStream inputStream = this.urlConnection.getInputStream();
                    do {
                        long skip = inputStream.skip(skip_left);
                        skip_left -= skip;
                    } while (skip_left > 0);
                    this.channel = openChannel(inputStream);
                    return position;
                } catch (IOException e2) {
                    return -1L;
                }
            }
            URLConnection tmpURLConnection = null;
            try {
                URLConnection tmpURLConnection2 = this.uri.toURL().openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) tmpURLConnection2;
                httpConnection.setRequestMethod("GET");
                httpConnection.setUseCaches(false);
                httpConnection.setRequestProperty("Range", "bytes=" + position + LanguageTag.SEP);
                if (httpConnection.getResponseCode() != 206) {
                    if (tmpURLConnection2 != null) {
                        Locator.closeConnection(tmpURLConnection2);
                    }
                    return -1L;
                }
                closeConnection();
                this.urlConnection = tmpURLConnection2;
                tmpURLConnection = null;
                this.channel = openChannel(null);
                if (0 != 0) {
                    Locator.closeConnection(null);
                }
                return position;
            } catch (IOException e3) {
                if (tmpURLConnection != null) {
                    Locator.closeConnection(tmpURLConnection);
                }
                return -1L;
            } catch (Throwable th) {
                if (tmpURLConnection != null) {
                    Locator.closeConnection(tmpURLConnection);
                }
                throw th;
            }
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        public void closeConnection() {
            super.closeConnection();
            Locator.closeConnection(this.urlConnection);
            this.urlConnection = null;
        }

        private ReadableByteChannel openChannel(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return Channels.newChannel(this.urlConnection.getInputStream());
            }
            return Channels.newChannel(inputStream);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/ConnectionHolder$MemoryConnectionHolder.class */
    private static class MemoryConnectionHolder extends ConnectionHolder {
        private final ByteBuffer backingBuffer;

        public MemoryConnectionHolder(ByteBuffer buf) {
            if (null == buf) {
                throw new IllegalArgumentException("Can't connect to null buffer...");
            }
            if (buf.isDirect()) {
                this.backingBuffer = buf.duplicate();
            } else {
                this.backingBuffer = ByteBuffer.allocateDirect(buf.capacity());
                this.backingBuffer.put(buf);
            }
            this.backingBuffer.rewind();
            this.channel = new ReadableByteChannel() { // from class: com.sun.media.jfxmedia.locator.ConnectionHolder.MemoryConnectionHolder.1
                @Override // java.nio.channels.ReadableByteChannel
                public int read(ByteBuffer bb2) throws IOException {
                    int actual;
                    if (MemoryConnectionHolder.this.backingBuffer.remaining() <= 0) {
                        return -1;
                    }
                    if (bb2.equals(MemoryConnectionHolder.this.buffer)) {
                        actual = Math.min(ConnectionHolder.DEFAULT_BUFFER_SIZE, MemoryConnectionHolder.this.backingBuffer.remaining());
                        if (actual > 0) {
                            MemoryConnectionHolder.this.buffer = MemoryConnectionHolder.this.backingBuffer.slice();
                            MemoryConnectionHolder.this.buffer.limit(actual);
                        }
                    } else {
                        actual = Math.min(bb2.remaining(), MemoryConnectionHolder.this.backingBuffer.remaining());
                        if (actual > 0) {
                            MemoryConnectionHolder.this.backingBuffer.limit(MemoryConnectionHolder.this.backingBuffer.position() + actual);
                            bb2.put(MemoryConnectionHolder.this.backingBuffer);
                            MemoryConnectionHolder.this.backingBuffer.limit(MemoryConnectionHolder.this.backingBuffer.capacity());
                        }
                    }
                    return actual;
                }

                @Override // java.nio.channels.Channel
                public boolean isOpen() {
                    return true;
                }

                @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                }
            };
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        int readBlock(long position, int size) throws IOException {
            if (null == this.channel) {
                throw new ClosedChannelException();
            }
            if (((int) position) > this.backingBuffer.capacity()) {
                return -1;
            }
            this.backingBuffer.position((int) position);
            this.buffer = this.backingBuffer.slice();
            int actual = Math.min(this.backingBuffer.remaining(), size);
            this.buffer.limit(actual);
            this.backingBuffer.position(this.backingBuffer.position() + actual);
            return actual;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean needBuffer() {
            return false;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean isSeekable() {
            return true;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        boolean isRandomAccess() {
            return true;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        public long seek(long position) {
            if (((int) position) < this.backingBuffer.capacity()) {
                this.backingBuffer.limit(this.backingBuffer.capacity());
                this.backingBuffer.position((int) position);
                return position;
            }
            return -1L;
        }

        @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
        public void closeConnection() {
            this.channel = null;
        }
    }
}
