package com.sun.glass.ui.win;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.SystemClipboard;
import com.sun.istack.internal.localization.Localizable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinSystemClipboard.class */
class WinSystemClipboard extends SystemClipboard {
    private long ptr;
    static final byte[] terminator;
    static final String defaultCharset = "UTF-16LE";
    static final String RTFCharset = "US-ASCII";

    private static native void initIDs();

    @Override // com.sun.glass.ui.SystemClipboard
    protected native boolean isOwner();

    protected native void create();

    protected native void dispose();

    protected native void push(Object[] objArr, int i2);

    protected native boolean pop();

    private native byte[] popBytes(String str, long j2);

    private native String[] popMimesFromSystem();

    @Override // com.sun.glass.ui.SystemClipboard
    protected native void pushTargetActionToSystem(int i2);

    private native int popSupportedSourceActions();

    static {
        initIDs();
        terminator = new byte[]{0, 0};
    }

    protected WinSystemClipboard(String name) {
        super(name);
        this.ptr = 0L;
        create();
    }

    protected final long getPtr() {
        return this.ptr;
    }

    private byte[] fosSerialize(String mime, long index) {
        Pixels pxls;
        Object data = getLocalData(mime);
        if (data instanceof ByteBuffer) {
            byte[] b2 = ((ByteBuffer) data).array();
            if (Clipboard.HTML_TYPE.equals(mime)) {
                b2 = WinHTMLCodec.encode(b2);
            }
            return b2;
        }
        if (data instanceof String) {
            String st = ((String) data).replaceAll("(\r\n|\r|\n)", "\r\n");
            if (Clipboard.HTML_TYPE.equals(mime)) {
                try {
                    byte[] bytes = st.getBytes("UTF-8");
                    ByteBuffer ba2 = ByteBuffer.allocate(bytes.length + 1);
                    ba2.put(bytes);
                    ba2.put((byte) 0);
                    return WinHTMLCodec.encode(ba2.array());
                } catch (UnsupportedEncodingException e2) {
                    return null;
                }
            }
            if (Clipboard.RTF_TYPE.equals(mime)) {
                try {
                    byte[] bytes2 = st.getBytes(RTFCharset);
                    ByteBuffer ba3 = ByteBuffer.allocate(bytes2.length + 1);
                    ba3.put(bytes2);
                    ba3.put((byte) 0);
                    return ba3.array();
                } catch (UnsupportedEncodingException e3) {
                    return null;
                }
            }
            ByteBuffer ba4 = ByteBuffer.allocate((st.length() + 1) * 2);
            try {
                ba4.put(st.getBytes(defaultCharset));
            } catch (UnsupportedEncodingException e4) {
            }
            ba4.put(terminator);
            return ba4.array();
        }
        if (Clipboard.FILE_LIST_TYPE.equals(mime)) {
            String[] ast = (String[]) data;
            if (ast != null && ast.length > 0) {
                int size = 0;
                for (String st2 : ast) {
                    size += (st2.length() + 1) * 2;
                }
                try {
                    ByteBuffer ba5 = ByteBuffer.allocate(size + 2);
                    for (String st3 : ast) {
                        ba5.put(st3.getBytes(defaultCharset));
                        ba5.put(terminator);
                    }
                    ba5.put(terminator);
                    return ba5.array();
                } catch (UnsupportedEncodingException e5) {
                    return null;
                }
            }
            return null;
        }
        if (Clipboard.RAW_IMAGE_TYPE.equals(mime) && (pxls = (Pixels) data) != null) {
            ByteBuffer ba6 = ByteBuffer.allocate((pxls.getWidth() * pxls.getHeight() * 4) + 8);
            ba6.putInt(pxls.getWidth());
            ba6.putInt(pxls.getHeight());
            ba6.put(pxls.asByteBuffer());
            return ba6.array();
        }
        return null;
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinSystemClipboard$MimeTypeParser.class */
    private static final class MimeTypeParser {
        protected static final String externalBodyMime = "message/external-body";
        protected String mime;
        protected boolean bInMemoryFile;
        protected int index;

        public MimeTypeParser() throws NumberFormatException {
            parse("");
        }

        public MimeTypeParser(String mimeFull) throws NumberFormatException {
            parse(mimeFull);
        }

        public void parse(String mimeFull) throws NumberFormatException {
            this.mime = mimeFull;
            this.bInMemoryFile = false;
            this.index = -1;
            if (mimeFull.startsWith(externalBodyMime)) {
                String[] mimeParts = mimeFull.split(";");
                String accessType = "";
                int indexValue = -1;
                for (int i2 = 1; i2 < mimeParts.length; i2++) {
                    String[] params = mimeParts[i2].split("=");
                    if (params.length == 2) {
                        if (params[0].trim().equalsIgnoreCase("index")) {
                            indexValue = Integer.parseInt(params[1].trim());
                        } else if (params[0].trim().equalsIgnoreCase("access-type")) {
                            accessType = params[1].trim();
                        }
                    }
                    if (indexValue != -1 && !accessType.isEmpty()) {
                        break;
                    }
                }
                if (accessType.equalsIgnoreCase("clipboard")) {
                    this.bInMemoryFile = true;
                    this.mime = mimeParts[0];
                    this.index = indexValue;
                }
            }
        }

        public String getMime() {
            return this.mime;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean isInMemoryFile() {
            return this.bInMemoryFile;
        }
    }

    @Override // com.sun.glass.ui.SystemClipboard
    protected final void pushToSystem(HashMap<String, Object> cacheData, int supportedActions) throws NumberFormatException {
        Set<String> mimes = cacheData.keySet();
        Set<String> mimesForSystem = new HashSet<>();
        MimeTypeParser parser = new MimeTypeParser();
        for (String mime : mimes) {
            parser.parse(mime);
            if (!parser.isInMemoryFile()) {
                mimesForSystem.add(mime);
            }
        }
        push(mimesForSystem.toArray(), supportedActions);
    }

    @Override // com.sun.glass.ui.SystemClipboard
    protected final Object popFromSystem(String mimeFull) {
        byte[] data;
        String[] ret;
        if (!pop()) {
            return null;
        }
        MimeTypeParser parser = new MimeTypeParser(mimeFull);
        String mime = parser.getMime();
        byte[] data2 = popBytes(mime, parser.getIndex());
        if (data2 != null) {
            if (Clipboard.TEXT_TYPE.equals(mime) || Clipboard.URI_TYPE.equals(mime)) {
                try {
                    return new String(data2, 0, data2.length - 2, defaultCharset);
                } catch (UnsupportedEncodingException e2) {
                    return null;
                }
            }
            if (Clipboard.HTML_TYPE.equals(mime)) {
                try {
                    byte[] data3 = WinHTMLCodec.decode(data2);
                    return new String(data3, 0, data3.length, "UTF-8");
                } catch (UnsupportedEncodingException e3) {
                    return null;
                }
            }
            if (Clipboard.RTF_TYPE.equals(mime)) {
                try {
                    return new String(data2, 0, data2.length, RTFCharset);
                } catch (UnsupportedEncodingException e4) {
                    return null;
                }
            }
            if (Clipboard.FILE_LIST_TYPE.equals(mime)) {
                try {
                    String st = new String(data2, 0, data2.length, defaultCharset);
                    return st.split(Localizable.NOT_LOCALIZABLE);
                } catch (UnsupportedEncodingException e5) {
                    return null;
                }
            }
            if (Clipboard.RAW_IMAGE_TYPE.equals(mime)) {
                ByteBuffer size = ByteBuffer.wrap(data2, 0, 8);
                return Application.GetApplication().createPixels(size.getInt(), size.getInt(), ByteBuffer.wrap(data2, 8, data2.length - 8));
            }
            return ByteBuffer.wrap(data2);
        }
        if ((Clipboard.URI_TYPE.equals(mime) || Clipboard.TEXT_TYPE.equals(mime)) && (data = popBytes(mime + ";locale", parser.getIndex())) != null) {
            try {
                return new String(data, 0, data.length - 1, "UTF-8");
            } catch (UnsupportedEncodingException e6) {
            }
        }
        if (Clipboard.URI_TYPE.equals(mime) && (ret = (String[]) popFromSystem(Clipboard.FILE_LIST_TYPE)) != null) {
            StringBuilder out = new StringBuilder();
            for (String fileName : ret) {
                String fileName2 = fileName.replace(FXMLLoader.ESCAPE_PREFIX, "/");
                if (out.length() > 0) {
                    out.append("\r\n");
                }
                out.append("file:/").append(fileName2);
            }
            return out.toString();
        }
        return null;
    }

    @Override // com.sun.glass.ui.SystemClipboard
    protected final String[] mimesFromSystem() {
        if (!pop()) {
            return null;
        }
        return popMimesFromSystem();
    }

    @Override // com.sun.glass.ui.SystemClipboard, com.sun.glass.ui.Clipboard
    public String toString() {
        return "Windows System Clipboard";
    }

    @Override // com.sun.glass.ui.Clipboard
    protected final void close() {
        dispose();
        this.ptr = 0L;
    }

    @Override // com.sun.glass.ui.SystemClipboard
    protected int supportedSourceActionsFromSystem() {
        if (!pop()) {
            return 0;
        }
        return popSupportedSourceActions();
    }
}
