package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmediaimpl.MediaUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/HLSConnectionHolder.class */
final class HLSConnectionHolder extends ConnectionHolder {
    private URLConnection urlConnection = null;
    private PlaylistThread playlistThread = new PlaylistThread();
    private VariantPlaylist variantPlaylist = null;
    private Playlist currentPlaylist = null;
    private int mediaFileIndex = -1;
    private CountDownLatch readySignal = new CountDownLatch(1);
    private Semaphore liveSemaphore = new Semaphore(0);
    private boolean isPlaylistClosed = false;
    private boolean isBitrateAdjustable = false;
    private long startTime = -1;
    private static final long HLS_VALUE_FLOAT_MULTIPLIER = 1000;
    private static final int HLS_PROP_GET_DURATION = 1;
    private static final int HLS_PROP_GET_HLS_MODE = 2;
    private static final int HLS_PROP_GET_MIMETYPE = 3;
    private static final int HLS_VALUE_MIMETYPE_MP2T = 1;
    private static final int HLS_VALUE_MIMETYPE_MP3 = 2;
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CHARSET_US_ASCII = "US-ASCII";

    static /* synthetic */ int access$4310(HLSConnectionHolder x0) {
        int i2 = x0.mediaFileIndex;
        x0.mediaFileIndex = i2 - 1;
        return i2;
    }

    static /* synthetic */ int access$4308(HLSConnectionHolder x0) {
        int i2 = x0.mediaFileIndex;
        x0.mediaFileIndex = i2 + 1;
        return i2;
    }

    HLSConnectionHolder(URI uri) throws IOException {
        this.playlistThread.setPlaylistURI(uri);
        init();
    }

    private void init() {
        this.playlistThread.putState(0);
        this.playlistThread.start();
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    public int readNextBlock() throws IOException {
        if (this.isBitrateAdjustable && this.startTime == -1) {
            this.startTime = System.currentTimeMillis();
        }
        int read = super.readNextBlock();
        if (this.isBitrateAdjustable && read == -1) {
            long readTime = System.currentTimeMillis() - this.startTime;
            this.startTime = -1L;
            adjustBitrate(readTime);
        }
        return read;
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    int readBlock(long position, int size) throws IOException {
        throw new IOException();
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    boolean needBuffer() {
        return true;
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    boolean isSeekable() {
        return true;
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    boolean isRandomAccess() {
        return false;
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    public long seek(long position) {
        try {
            this.readySignal.await();
            return (long) (this.currentPlaylist.seek(position) * 1000.0d);
        } catch (Exception e2) {
            return -1L;
        }
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    public void closeConnection() {
        this.currentPlaylist.close();
        super.closeConnection();
        resetConnection();
        this.playlistThread.putState(1);
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    int property(int prop, int value) {
        try {
            this.readySignal.await();
            if (prop != 1) {
                if (prop == 2) {
                    return 1;
                }
                if (prop != 3) {
                    return -1;
                }
                return this.currentPlaylist.getMimeType();
            }
            return (int) (this.currentPlaylist.getDuration() * 1000.0d);
        } catch (Exception e2) {
            return -1;
        }
    }

    @Override // com.sun.media.jfxmedia.locator.ConnectionHolder
    int getStreamSize() {
        try {
            this.readySignal.await();
            return loadNextSegment();
        } catch (Exception e2) {
            return -1;
        }
    }

    private void resetConnection() {
        super.closeConnection();
        Locator.closeConnection(this.urlConnection);
        this.urlConnection = null;
    }

    private int loadNextSegment() {
        resetConnection();
        String mediaFile = this.currentPlaylist.getNextMediaFile();
        if (mediaFile == null) {
            return -1;
        }
        try {
            URI uri = new URI(mediaFile);
            this.urlConnection = uri.toURL().openConnection();
            this.channel = openChannel();
            if (this.currentPlaylist.isCurrentMediaFileDiscontinuity()) {
                return (-1) * this.urlConnection.getContentLength();
            }
            return this.urlConnection.getContentLength();
        } catch (Exception e2) {
            return -1;
        }
    }

    private ReadableByteChannel openChannel() throws IOException {
        return Channels.newChannel(this.urlConnection.getInputStream());
    }

    private void adjustBitrate(long readTime) {
        int avgBitrate = (int) (((this.urlConnection.getContentLength() * 8) * 1000) / readTime);
        Playlist playlist = this.variantPlaylist.getPlaylistBasedOnBitrate(avgBitrate);
        if (playlist == null || playlist == this.currentPlaylist) {
            return;
        }
        if (this.currentPlaylist.isLive()) {
            playlist.update(this.currentPlaylist.getNextMediaFile());
            this.playlistThread.setReloadPlaylist(playlist);
        }
        playlist.setForceDiscontinuity(true);
        this.currentPlaylist = playlist;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String stripParameters(String mediaFile) {
        int qp = mediaFile.indexOf(63);
        if (qp > 0) {
            mediaFile = mediaFile.substring(0, qp);
        }
        return mediaFile;
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/HLSConnectionHolder$PlaylistThread.class */
    private class PlaylistThread extends Thread {
        public static final int STATE_INIT = 0;
        public static final int STATE_EXIT = 1;
        public static final int STATE_RELOAD_PLAYLIST = 2;
        private BlockingQueue<Integer> stateQueue;
        private URI playlistURI;
        private Playlist reloadPlaylist;
        private final Object reloadLock;
        private volatile boolean stopped;

        private PlaylistThread() {
            this.stateQueue = new LinkedBlockingQueue();
            this.playlistURI = null;
            this.reloadPlaylist = null;
            this.reloadLock = new Object();
            this.stopped = false;
            setName("JFXMedia HLS Playlist Thread");
            setDaemon(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPlaylistURI(URI playlistURI) {
            this.playlistURI = playlistURI;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setReloadPlaylist(Playlist playlist) {
            synchronized (this.reloadLock) {
                this.reloadPlaylist = playlist;
            }
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0018. Please report as an issue. */
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.stopped) {
                try {
                    int state = this.stateQueue.take2().intValue();
                    switch (state) {
                        case 0:
                            stateInit();
                            break;
                        case 1:
                            this.stopped = true;
                            break;
                        case 2:
                            stateReloadPlaylist();
                            break;
                    }
                } catch (Exception e2) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void putState(int state) {
            if (this.stateQueue != null) {
                try {
                    this.stateQueue.put(Integer.valueOf(state));
                } catch (InterruptedException e2) {
                }
            }
        }

        private void stateInit() {
            if (this.playlistURI == null) {
                return;
            }
            PlaylistParser parser = new PlaylistParser();
            parser.load(this.playlistURI);
            if (!parser.isVariantPlaylist()) {
                if (HLSConnectionHolder.this.currentPlaylist == null) {
                    HLSConnectionHolder.this.currentPlaylist = new Playlist(parser.isLivePlaylist(), parser.getTargetDuration());
                    HLSConnectionHolder.this.currentPlaylist.setPlaylistURI(this.playlistURI);
                }
                if (HLSConnectionHolder.this.currentPlaylist.setSequenceNumber(parser.getSequenceNumber())) {
                    while (parser.hasNext()) {
                        HLSConnectionHolder.this.currentPlaylist.addMediaFile(parser.getString(), parser.getDouble().doubleValue(), parser.getBoolean().booleanValue());
                    }
                }
                if (HLSConnectionHolder.this.variantPlaylist != null) {
                    HLSConnectionHolder.this.variantPlaylist.addPlaylist(HLSConnectionHolder.this.currentPlaylist);
                }
            } else {
                HLSConnectionHolder.this.variantPlaylist = new VariantPlaylist(this.playlistURI);
                while (parser.hasNext()) {
                    HLSConnectionHolder.this.variantPlaylist.addPlaylistInfo(parser.getString(), parser.getInteger().intValue());
                }
            }
            if (HLSConnectionHolder.this.variantPlaylist != null) {
                while (HLSConnectionHolder.this.variantPlaylist.hasNext()) {
                    try {
                        HLSConnectionHolder.this.currentPlaylist = new Playlist(HLSConnectionHolder.this.variantPlaylist.getPlaylistURI());
                        HLSConnectionHolder.this.currentPlaylist.update(null);
                        HLSConnectionHolder.this.variantPlaylist.addPlaylist(HLSConnectionHolder.this.currentPlaylist);
                    } catch (MalformedURLException e2) {
                    } catch (URISyntaxException e3) {
                    }
                }
            }
            if (HLSConnectionHolder.this.variantPlaylist != null) {
                HLSConnectionHolder.this.currentPlaylist = HLSConnectionHolder.this.variantPlaylist.getPlaylist(0);
                HLSConnectionHolder.this.isBitrateAdjustable = true;
            }
            if (HLSConnectionHolder.this.currentPlaylist.isLive()) {
                setReloadPlaylist(HLSConnectionHolder.this.currentPlaylist);
                putState(2);
            }
            HLSConnectionHolder.this.readySignal.countDown();
        }

        private void stateReloadPlaylist() {
            long timeout;
            try {
                synchronized (this.reloadLock) {
                    timeout = this.reloadPlaylist.getTargetDuration() / 2;
                }
                Thread.sleep(timeout);
                synchronized (this.reloadLock) {
                    this.reloadPlaylist.update(null);
                }
                putState(2);
            } catch (InterruptedException e2) {
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/HLSConnectionHolder$PlaylistParser.class */
    private static class PlaylistParser {
        private boolean isFirstLine;
        private boolean isLineMediaFileURI;
        private boolean isEndList;
        private boolean isLinePlaylistURI;
        private boolean isVariantPlaylist;
        private boolean isDiscontinuity;
        private int targetDuration;
        private int sequenceNumber;
        private int dataListIndex;
        private List<String> dataListString;
        private List<Integer> dataListInteger;
        private List<Double> dataListDouble;
        private List<Boolean> dataListBoolean;

        private PlaylistParser() {
            this.isFirstLine = true;
            this.isLineMediaFileURI = false;
            this.isEndList = false;
            this.isLinePlaylistURI = false;
            this.isVariantPlaylist = false;
            this.isDiscontinuity = false;
            this.targetDuration = 0;
            this.sequenceNumber = 0;
            this.dataListIndex = -1;
            this.dataListString = new ArrayList();
            this.dataListInteger = new ArrayList();
            this.dataListDouble = new ArrayList();
            this.dataListBoolean = new ArrayList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void load(URI uri) {
            boolean result;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                connection = (HttpURLConnection) uri.toURL().openConnection();
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() != 200) {
                    MediaUtils.error(this, MediaError.ERROR_LOCATOR_CONNECTION_LOST.code(), "HTTP responce code: " + connection.getResponseCode(), null);
                }
                Charset charset = getCharset(uri.toURL().toExternalForm(), connection.getContentType());
                if (charset != null) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
                }
                if (reader != null) {
                    do {
                        result = parseLine(reader.readLine());
                    } while (result);
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e2) {
                    }
                    Locator.closeConnection(connection);
                }
            } catch (MalformedURLException e3) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e4) {
                    }
                    Locator.closeConnection(connection);
                }
            } catch (IOException e5) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e6) {
                    }
                    Locator.closeConnection(connection);
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e7) {
                    }
                    Locator.closeConnection(connection);
                }
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isVariantPlaylist() {
            return this.isVariantPlaylist;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isLivePlaylist() {
            return !this.isEndList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getTargetDuration() {
            return this.targetDuration;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSequenceNumber() {
            return this.sequenceNumber;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasNext() {
            this.dataListIndex++;
            if (this.dataListString.size() > this.dataListIndex || this.dataListInteger.size() > this.dataListIndex || this.dataListDouble.size() > this.dataListIndex || this.dataListBoolean.size() > this.dataListIndex) {
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getString() {
            return this.dataListString.get(this.dataListIndex);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Integer getInteger() {
            return this.dataListInteger.get(this.dataListIndex);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Double getDouble() {
            return this.dataListDouble.get(this.dataListIndex);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Boolean getBoolean() {
            return this.dataListBoolean.get(this.dataListIndex);
        }

        private boolean parseLine(String line) throws NumberFormatException {
            if (line == null) {
                return false;
            }
            if (this.isFirstLine) {
                if (line.compareTo("#EXTM3U") != 0) {
                    return false;
                }
                this.isFirstLine = false;
                return true;
            }
            if (line.isEmpty()) {
                return true;
            }
            if (line.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) && !line.startsWith("#EXT")) {
                return true;
            }
            if (line.startsWith("#EXTINF")) {
                String[] s1 = line.split(CallSiteDescriptor.TOKEN_DELIMITER);
                if (s1.length == 2 && s1[1].length() > 0) {
                    String[] s2 = s1[1].split(",");
                    if (s2.length >= 1) {
                        this.dataListDouble.add(Double.valueOf(Double.parseDouble(s2[0])));
                    }
                }
                this.isLineMediaFileURI = true;
                return true;
            }
            if (line.startsWith("#EXT-X-TARGETDURATION")) {
                String[] s12 = line.split(CallSiteDescriptor.TOKEN_DELIMITER);
                if (s12.length == 2 && s12[1].length() > 0) {
                    this.targetDuration = Integer.parseInt(s12[1]);
                    return true;
                }
                return true;
            }
            if (line.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                String[] s13 = line.split(CallSiteDescriptor.TOKEN_DELIMITER);
                if (s13.length == 2 && s13[1].length() > 0) {
                    this.sequenceNumber = Integer.parseInt(s13[1]);
                    return true;
                }
                return true;
            }
            if (!line.startsWith("#EXT-X-STREAM-INF")) {
                if (line.startsWith("#EXT-X-ENDLIST")) {
                    this.isEndList = true;
                    return true;
                }
                if (line.startsWith("#EXT-X-DISCONTINUITY")) {
                    this.isDiscontinuity = true;
                    return true;
                }
                if (this.isLinePlaylistURI) {
                    this.isLinePlaylistURI = false;
                    this.dataListString.add(line);
                    return true;
                }
                if (this.isLineMediaFileURI) {
                    this.isLineMediaFileURI = false;
                    this.dataListString.add(line);
                    this.dataListBoolean.add(Boolean.valueOf(this.isDiscontinuity));
                    this.isDiscontinuity = false;
                    return true;
                }
                return true;
            }
            this.isVariantPlaylist = true;
            int bitrate = 0;
            String[] s14 = line.split(CallSiteDescriptor.TOKEN_DELIMITER);
            if (s14.length == 2 && s14[1].length() > 0) {
                String[] s22 = s14[1].split(",");
                if (s22.length > 0) {
                    for (int i2 = 0; i2 < s22.length; i2++) {
                        s22[i2] = s22[i2].trim();
                        if (s22[i2].startsWith("BANDWIDTH")) {
                            String[] s3 = s22[i2].split("=");
                            if (s3.length == 2 && s3[1].length() > 0) {
                                bitrate = Integer.parseInt(s3[1]);
                            }
                        }
                    }
                }
            }
            if (bitrate < 1) {
                return false;
            }
            this.dataListInteger.add(Integer.valueOf(bitrate));
            this.isLinePlaylistURI = true;
            return true;
        }

        private Charset getCharset(String url, String mimeType) {
            if ((url != null && HLSConnectionHolder.stripParameters(url).endsWith(".m3u8")) || (mimeType != null && mimeType.equals(MediaUtils.CONTENT_TYPE_M3U8))) {
                if (Charset.isSupported("UTF-8")) {
                    return Charset.forName("UTF-8");
                }
                return null;
            }
            if (((url != null && HLSConnectionHolder.stripParameters(url).endsWith(".m3u")) || (mimeType != null && mimeType.equals(MediaUtils.CONTENT_TYPE_M3U))) && Charset.isSupported(HLSConnectionHolder.CHARSET_US_ASCII)) {
                return Charset.forName(HLSConnectionHolder.CHARSET_US_ASCII);
            }
            return null;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/HLSConnectionHolder$VariantPlaylist.class */
    private static class VariantPlaylist {
        private URI playlistURI;
        private int infoIndex;
        private List<String> playlistsLocations;
        private List<Integer> playlistsBitrates;
        private List<Playlist> playlists;
        private String mediaFileExtension;

        private VariantPlaylist(URI uri) {
            this.playlistURI = null;
            this.infoIndex = -1;
            this.playlistsLocations = new ArrayList();
            this.playlistsBitrates = new ArrayList();
            this.playlists = new ArrayList();
            this.mediaFileExtension = null;
            this.playlistURI = uri;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addPlaylistInfo(String location, int bitrate) {
            this.playlistsLocations.add(location);
            this.playlistsBitrates.add(Integer.valueOf(bitrate));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addPlaylist(Playlist playlist) {
            if (this.mediaFileExtension == null) {
                this.mediaFileExtension = playlist.getMediaFileExtension();
            } else if (!this.mediaFileExtension.equals(playlist.getMediaFileExtension())) {
                this.playlistsLocations.remove(this.infoIndex);
                this.playlistsBitrates.remove(this.infoIndex);
                this.infoIndex--;
                return;
            }
            this.playlists.add(playlist);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Playlist getPlaylist(int index) {
            if (this.playlists.size() > index) {
                return this.playlists.get(index);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasNext() {
            this.infoIndex++;
            if (this.playlistsLocations.size() > this.infoIndex && this.playlistsBitrates.size() > this.infoIndex) {
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public URI getPlaylistURI() throws MalformedURLException, URISyntaxException {
            String location = this.playlistsLocations.get(this.infoIndex);
            if (location.startsWith("http://") || location.startsWith("https://")) {
                return new URI(location);
            }
            return new URI(this.playlistURI.toURL().toString().substring(0, this.playlistURI.toURL().toString().lastIndexOf("/") + 1) + location);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Playlist getPlaylistBasedOnBitrate(int bitrate) {
            int playlistIndex = -1;
            int playlistBitrate = 0;
            for (int i2 = 0; i2 < this.playlistsBitrates.size(); i2++) {
                int b2 = this.playlistsBitrates.get(i2).intValue();
                if (b2 < bitrate) {
                    if (playlistIndex != -1) {
                        if (b2 > playlistBitrate) {
                            playlistBitrate = b2;
                            playlistIndex = i2;
                        }
                    } else {
                        playlistIndex = i2;
                    }
                }
            }
            if (playlistIndex == -1) {
                for (int i3 = 0; i3 < this.playlistsBitrates.size(); i3++) {
                    int b3 = this.playlistsBitrates.get(i3).intValue();
                    if (b3 < playlistBitrate || playlistIndex == -1) {
                        playlistBitrate = b3;
                        playlistIndex = i3;
                    }
                }
            }
            if (playlistIndex < 0 || playlistIndex >= this.playlists.size()) {
                return null;
            }
            return this.playlists.get(playlistIndex);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/HLSConnectionHolder$Playlist.class */
    private class Playlist {
        private boolean isLive;
        private volatile boolean isLiveWaiting;
        private volatile boolean isLiveStop;
        private long targetDuration;
        private URI playlistURI;
        private final Object lock;
        private List<String> mediaFiles;
        private List<Double> mediaFilesStartTimes;
        private List<Boolean> mediaFilesDiscontinuities;
        private boolean needBaseURI;
        private String baseURI;
        private double duration;
        private int sequenceNumber;
        private int sequenceNumberStart;
        private boolean sequenceNumberUpdated;
        private boolean forceDiscontinuity;

        private Playlist(boolean isLive, int targetDuration) {
            this.isLive = false;
            this.isLiveWaiting = false;
            this.isLiveStop = false;
            this.targetDuration = 0L;
            this.playlistURI = null;
            this.lock = new Object();
            this.mediaFiles = new ArrayList();
            this.mediaFilesStartTimes = new ArrayList();
            this.mediaFilesDiscontinuities = new ArrayList();
            this.needBaseURI = true;
            this.baseURI = null;
            this.duration = 0.0d;
            this.sequenceNumber = -1;
            this.sequenceNumberStart = -1;
            this.sequenceNumberUpdated = false;
            this.forceDiscontinuity = false;
            this.isLive = isLive;
            this.targetDuration = targetDuration * 1000;
            if (isLive) {
                this.duration = -1.0d;
            }
        }

        private Playlist(URI uri) {
            this.isLive = false;
            this.isLiveWaiting = false;
            this.isLiveStop = false;
            this.targetDuration = 0L;
            this.playlistURI = null;
            this.lock = new Object();
            this.mediaFiles = new ArrayList();
            this.mediaFilesStartTimes = new ArrayList();
            this.mediaFilesDiscontinuities = new ArrayList();
            this.needBaseURI = true;
            this.baseURI = null;
            this.duration = 0.0d;
            this.sequenceNumber = -1;
            this.sequenceNumberStart = -1;
            this.sequenceNumberUpdated = false;
            this.forceDiscontinuity = false;
            this.playlistURI = uri;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void update(String nextMediaFile) {
            PlaylistParser parser = new PlaylistParser();
            parser.load(this.playlistURI);
            this.isLive = parser.isLivePlaylist();
            this.targetDuration = parser.getTargetDuration() * 1000;
            if (this.isLive) {
                this.duration = -1.0d;
            }
            if (setSequenceNumber(parser.getSequenceNumber())) {
                while (parser.hasNext()) {
                    addMediaFile(parser.getString(), parser.getDouble().doubleValue(), parser.getBoolean().booleanValue());
                }
            }
            if (nextMediaFile != null) {
                synchronized (this.lock) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= this.mediaFiles.size()) {
                            break;
                        }
                        String mediaFile = this.mediaFiles.get(i2);
                        if (nextMediaFile.endsWith(mediaFile)) {
                            HLSConnectionHolder.this.mediaFileIndex = i2 - 1;
                            break;
                        }
                        i2++;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isLive() {
            return this.isLive;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long getTargetDuration() {
            return this.targetDuration;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPlaylistURI(URI uri) {
            this.playlistURI = uri;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addMediaFile(String URI, double duration, boolean isDiscontinuity) {
            synchronized (this.lock) {
                if (this.needBaseURI) {
                    setBaseURI(this.playlistURI.toString(), URI);
                }
                if (this.isLive) {
                    if (this.sequenceNumberUpdated) {
                        int index = this.mediaFiles.indexOf(URI);
                        if (index != -1) {
                            for (int i2 = 0; i2 < index; i2++) {
                                this.mediaFiles.remove(0);
                                this.mediaFilesDiscontinuities.remove(0);
                                if (HLSConnectionHolder.this.mediaFileIndex == -1) {
                                    this.forceDiscontinuity = true;
                                }
                                if (HLSConnectionHolder.this.mediaFileIndex >= 0) {
                                    HLSConnectionHolder.access$4310(HLSConnectionHolder.this);
                                }
                            }
                        }
                        this.sequenceNumberUpdated = false;
                    }
                    if (this.mediaFiles.contains(URI)) {
                        return;
                    }
                }
                this.mediaFiles.add(URI);
                this.mediaFilesDiscontinuities.add(Boolean.valueOf(isDiscontinuity));
                if (this.isLive) {
                    if (this.isLiveWaiting) {
                        HLSConnectionHolder.this.liveSemaphore.release();
                    }
                } else {
                    this.mediaFilesStartTimes.add(Double.valueOf(this.duration));
                    this.duration += duration;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getNextMediaFile() {
            if (this.isLive) {
                synchronized (this.lock) {
                    this.isLiveWaiting = HLSConnectionHolder.this.mediaFileIndex + 1 >= this.mediaFiles.size();
                }
                if (this.isLiveWaiting) {
                    try {
                        HLSConnectionHolder.this.liveSemaphore.acquire();
                        this.isLiveWaiting = false;
                        if (this.isLiveStop) {
                            this.isLiveStop = false;
                            return null;
                        }
                    } catch (InterruptedException e2) {
                        this.isLiveWaiting = false;
                        return null;
                    }
                }
                if (HLSConnectionHolder.this.isPlaylistClosed) {
                    return null;
                }
            }
            synchronized (this.lock) {
                HLSConnectionHolder.access$4308(HLSConnectionHolder.this);
                if (HLSConnectionHolder.this.mediaFileIndex < this.mediaFiles.size()) {
                    if (this.baseURI != null) {
                        return this.baseURI + this.mediaFiles.get(HLSConnectionHolder.this.mediaFileIndex);
                    }
                    return this.mediaFiles.get(HLSConnectionHolder.this.mediaFileIndex);
                }
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getDuration() {
            return this.duration;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setForceDiscontinuity(boolean value) {
            this.forceDiscontinuity = value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isCurrentMediaFileDiscontinuity() {
            if (!this.forceDiscontinuity) {
                return this.mediaFilesDiscontinuities.get(HLSConnectionHolder.this.mediaFileIndex).booleanValue();
            }
            this.forceDiscontinuity = false;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double seek(long time) {
            synchronized (this.lock) {
                if (!this.isLive) {
                    long time2 = time + (this.targetDuration / 2000);
                    int mediaFileStartTimeSize = this.mediaFilesStartTimes.size();
                    for (int index = 0; index < mediaFileStartTimeSize; index++) {
                        if (time2 >= this.mediaFilesStartTimes.get(index).doubleValue()) {
                            if (index + 1 < mediaFileStartTimeSize) {
                                if (time2 < this.mediaFilesStartTimes.get(index + 1).doubleValue()) {
                                    HLSConnectionHolder.this.mediaFileIndex = index - 1;
                                    return this.mediaFilesStartTimes.get(index).doubleValue();
                                }
                            } else {
                                if (time2 - (this.targetDuration / 2000) < this.duration) {
                                    HLSConnectionHolder.this.mediaFileIndex = index - 1;
                                    return this.mediaFilesStartTimes.get(index).doubleValue();
                                }
                                if (Double.compare(time2 - (this.targetDuration / 2000), this.duration) == 0) {
                                    return this.duration;
                                }
                            }
                        }
                    }
                } else if (time == 0) {
                    HLSConnectionHolder.this.mediaFileIndex = -1;
                    if (this.isLiveWaiting) {
                        this.isLiveStop = true;
                        HLSConnectionHolder.this.liveSemaphore.release();
                    }
                    return 0.0d;
                }
                return -1.0d;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getMimeType() {
            synchronized (this.lock) {
                if (this.mediaFiles.size() > 0) {
                    if (!HLSConnectionHolder.stripParameters(this.mediaFiles.get(0)).endsWith(".ts")) {
                        if (HLSConnectionHolder.stripParameters(this.mediaFiles.get(0)).endsWith(".mp3")) {
                            return 2;
                        }
                    } else {
                        return 1;
                    }
                }
                return -1;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getMediaFileExtension() {
            String mediaFile;
            int index;
            synchronized (this.lock) {
                if (this.mediaFiles.size() > 0 && (index = (mediaFile = HLSConnectionHolder.stripParameters(this.mediaFiles.get(0))).lastIndexOf(".")) != -1) {
                    return mediaFile.substring(index);
                }
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setSequenceNumber(int value) {
            if (this.sequenceNumberStart == -1) {
                this.sequenceNumberStart = value;
                return true;
            }
            if (this.sequenceNumber != value) {
                this.sequenceNumberUpdated = true;
                this.sequenceNumber = value;
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void close() {
            if (this.isLive) {
                HLSConnectionHolder.this.isPlaylistClosed = true;
                HLSConnectionHolder.this.liveSemaphore.release();
            }
        }

        private void setBaseURI(String playlistURI, String URI) {
            if (!URI.startsWith("http://") && !URI.startsWith("https://")) {
                this.baseURI = playlistURI.substring(0, playlistURI.lastIndexOf("/") + 1);
            }
            this.needBaseURI = false;
        }
    }
}
