package com.sun.glass.ui;

import com.sun.glass.ui.delegate.ClipboardDelegate;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Clipboard.class */
public class Clipboard {
    public static final String TEXT_TYPE = "text/plain";
    public static final String HTML_TYPE = "text/html";
    public static final String RTF_TYPE = "text/rtf";
    public static final String URI_TYPE = "text/uri-list";
    public static final String FILE_LIST_TYPE = "application/x-java-file-list";
    public static final String RAW_IMAGE_TYPE = "application/x-java-rawimage";
    public static final String DRAG_IMAGE = "application/x-java-drag-image";
    public static final String DRAG_IMAGE_OFFSET = "application/x-java-drag-image-offset";
    public static final String IE_URL_SHORTCUT_FILENAME = "text/ie-shortcut-filename";
    public static final int ACTION_NONE = 0;
    public static final int ACTION_COPY = 1;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_REFERENCE = 1073741824;
    public static final int ACTION_COPY_OR_MOVE = 3;
    public static final int ACTION_ANY = 1342177279;
    public static final String DND = "DND";
    public static final String SYSTEM = "SYSTEM";
    public static final String SELECTION = "SELECTION";
    private static final Map<String, Clipboard> clipboards = new HashMap();
    private static final ClipboardDelegate delegate = PlatformFactory.getPlatformFactory().createClipboardDelegate();
    private final String name;
    private HashMap<String, Object> localSharedData;
    private ClipboardAssistance dataSource;
    private final HashSet<ClipboardAssistance> assistants = new HashSet<>();
    private final Object localDataProtector = new Object();
    protected int supportedActions = 1;

    protected Clipboard(String name) {
        Application.checkEventThread();
        this.name = name;
    }

    public void add(ClipboardAssistance assistant) {
        Application.checkEventThread();
        synchronized (this.assistants) {
            this.assistants.add(assistant);
        }
    }

    public void remove(ClipboardAssistance assistant) {
        boolean needClose;
        Application.checkEventThread();
        synchronized (this.localDataProtector) {
            if (assistant == this.dataSource) {
                this.dataSource = null;
            }
        }
        synchronized (this.assistants) {
            this.assistants.remove(assistant);
            needClose = this.assistants.isEmpty();
        }
        if (needClose) {
            synchronized (clipboards) {
                clipboards.remove(this.name);
            }
            close();
        }
    }

    protected void setSharedData(ClipboardAssistance dataSource, HashMap<String, Object> cacheData, int supportedActions) {
        Application.checkEventThread();
        synchronized (this.localDataProtector) {
            this.localSharedData = (HashMap) cacheData.clone();
            this.supportedActions = supportedActions;
            this.dataSource = dataSource;
        }
    }

    public void flush(ClipboardAssistance dataSource, HashMap<String, Object> cacheData, int supportedActions) {
        Application.checkEventThread();
        setSharedData(dataSource, cacheData, supportedActions);
        contentChanged();
    }

    public int getSupportedSourceActions() {
        Application.checkEventThread();
        return this.supportedActions;
    }

    public void setTargetAction(int actionDone) {
        Application.checkEventThread();
        actionPerformed(actionDone);
    }

    public void contentChanged() {
        HashSet<ClipboardAssistance> _assistants;
        Application.checkEventThread();
        synchronized (this.assistants) {
            _assistants = (HashSet) this.assistants.clone();
        }
        Iterator<ClipboardAssistance> it = _assistants.iterator();
        while (it.hasNext()) {
            ClipboardAssistance assistant = it.next();
            assistant.contentChanged();
        }
    }

    public void actionPerformed(int action) {
        Application.checkEventThread();
        synchronized (this.localDataProtector) {
            if (null != this.dataSource) {
                this.dataSource.actionPerformed(action);
            }
        }
    }

    public Object getData(String mimeType) {
        Application.checkEventThread();
        synchronized (this.localDataProtector) {
            if (this.localSharedData == null) {
                return null;
            }
            Object ret = this.localSharedData.get(mimeType);
            return ret instanceof DelayedCallback ? ((DelayedCallback) ret).providedData() : ret;
        }
    }

    public String[] getMimeTypes() {
        Application.checkEventThread();
        synchronized (this.localDataProtector) {
            if (this.localSharedData == null) {
                return null;
            }
            Set<String> mimes = this.localSharedData.keySet();
            String[] ret = new String[mimes.size()];
            int i2 = 0;
            for (String mime : mimes) {
                int i3 = i2;
                i2++;
                ret[i3] = mime;
            }
            return ret;
        }
    }

    protected static Clipboard get(String clipboardName) {
        Clipboard clipboard;
        Application.checkEventThread();
        synchronized (clipboards) {
            if (!clipboards.keySet().contains(clipboardName)) {
                Clipboard newClipboard = delegate.createClipboard(clipboardName);
                if (newClipboard == null) {
                    newClipboard = new Clipboard(clipboardName);
                }
                clipboards.put(clipboardName, newClipboard);
            }
            clipboard = clipboards.get(clipboardName);
        }
        return clipboard;
    }

    public Pixels getPixelsForRawImage(byte[] rawimage) {
        Application.checkEventThread();
        ByteBuffer size = ByteBuffer.wrap(rawimage, 0, 8);
        int width = size.getInt();
        int height = size.getInt();
        ByteBuffer pixels = ByteBuffer.wrap(rawimage, 8, rawimage.length - 8);
        return Application.GetApplication().createPixels(width, height, pixels.slice());
    }

    public String toString() {
        return "Clipboard: " + this.name + "@" + hashCode();
    }

    protected void close() {
        Application.checkEventThread();
        synchronized (this.localDataProtector) {
            this.dataSource = null;
        }
    }

    public String getName() {
        Application.checkEventThread();
        return this.name;
    }

    public static String getActionString(int action) {
        Application.checkEventThread();
        StringBuilder ret = new StringBuilder("");
        int[] test = {1, 2, 1073741824};
        String[] canDo = {"copy", "move", "link"};
        for (int i2 = 0; i2 < 3; i2++) {
            if ((test[i2] & action) > 0) {
                if (ret.length() > 0) {
                    ret.append(",");
                }
                ret.append(canDo[i2]);
            }
        }
        return ret.toString();
    }
}
