package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PermissionHelper;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.util.Pair;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.util.SecurityConstants;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/QuantumClipboard.class */
final class QuantumClipboard implements TKClipboard {
    private ClipboardAssistance systemAssistant;
    private boolean isCaching;
    private List<Pair<DataFormat, Object>> dataCache;
    private Set<TransferMode> transferModesCache;
    private static ClipboardAssistance currentDragboard;
    private static final Pattern findTagIMG = Pattern.compile("IMG\\s+SRC=\\\"([^\\\"]+)\\\"", 34);
    private AccessControlContext accessContext = null;
    private Image dragImage = null;
    private double dragOffsetX = 0.0d;
    private double dragOffsetY = 0.0d;

    private QuantumClipboard() {
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public void setSecurityContext(AccessControlContext acc) {
        if (this.accessContext != null) {
            throw new RuntimeException("Clipboard security context has been already set!");
        }
        this.accessContext = acc;
    }

    private AccessControlContext getAccessControlContext() {
        if (this.accessContext == null) {
            throw new RuntimeException("Clipboard security context has not been set!");
        }
        return this.accessContext;
    }

    public static QuantumClipboard getClipboardInstance(ClipboardAssistance assistant) {
        QuantumClipboard c2 = new QuantumClipboard();
        c2.systemAssistant = assistant;
        c2.isCaching = false;
        return c2;
    }

    static ClipboardAssistance getCurrentDragboard() {
        return currentDragboard;
    }

    static void releaseCurrentDragboard() {
        currentDragboard = null;
    }

    public static QuantumClipboard getDragboardInstance(ClipboardAssistance assistant, boolean isDragSource) {
        QuantumClipboard c2 = new QuantumClipboard();
        c2.systemAssistant = assistant;
        c2.isCaching = true;
        if (isDragSource) {
            currentDragboard = assistant;
        }
        return c2;
    }

    public static int transferModesToClipboardActions(Set<TransferMode> tms) {
        int actions = 0;
        for (TransferMode t2 : tms) {
            switch (t2) {
                case COPY:
                    actions |= 1;
                    break;
                case MOVE:
                    actions |= 2;
                    break;
                case LINK:
                    actions |= 1073741824;
                    break;
                default:
                    throw new IllegalArgumentException("unsupported TransferMode " + ((Object) tms));
            }
        }
        return actions;
    }

    public void setSupportedTransferMode(Set<TransferMode> tm) {
        if (this.isCaching) {
            this.transferModesCache = tm;
        }
        int actions = transferModesToClipboardActions(tm);
        this.systemAssistant.setSupportedActions(actions);
    }

    public static Set<TransferMode> clipboardActionsToTransferModes(int actions) {
        Set<TransferMode> tms = EnumSet.noneOf(TransferMode.class);
        if ((actions & 1) != 0) {
            tms.add(TransferMode.COPY);
        }
        if ((actions & 2) != 0) {
            tms.add(TransferMode.MOVE);
        }
        if ((actions & 1073741824) != 0) {
            tms.add(TransferMode.LINK);
        }
        return tms;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public Set<TransferMode> getTransferModes() {
        if (this.transferModesCache != null) {
            return EnumSet.copyOf(this.transferModesCache);
        }
        ClipboardAssistance assistant = currentDragboard != null ? currentDragboard : this.systemAssistant;
        Set<TransferMode> tms = clipboardActionsToTransferModes(assistant.getSupportedSourceActions());
        return tms;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public void setDragView(Image image) {
        this.dragImage = image;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public void setDragViewOffsetX(double offsetX) {
        this.dragOffsetX = offsetX;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public void setDragViewOffsetY(double offsetY) {
        this.dragOffsetY = offsetY;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public Image getDragView() {
        return this.dragImage;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public double getDragViewOffsetX() {
        return this.dragOffsetX;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public double getDragViewOffsetY() {
        return this.dragOffsetY;
    }

    public void close() {
        this.systemAssistant.close();
    }

    public void flush() {
        if (this.isCaching) {
            putContentToPeer((Pair[]) this.dataCache.toArray(new Pair[0]));
        }
        clearCache();
        clearDragView();
        this.systemAssistant.flush();
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public Object getContent(DataFormat dataFormat) {
        if (this.dataCache != null) {
            for (Pair<DataFormat, Object> pair : this.dataCache) {
                if (pair.getKey() == dataFormat) {
                    return pair.getValue();
                }
            }
            return null;
        }
        ClipboardAssistance assistant = currentDragboard != null ? currentDragboard : this.systemAssistant;
        if (dataFormat == DataFormat.IMAGE) {
            return readImage();
        }
        if (dataFormat == DataFormat.URL) {
            return assistant.getData(Clipboard.URI_TYPE);
        }
        if (dataFormat == DataFormat.FILES) {
            Object data = assistant.getData(Clipboard.FILE_LIST_TYPE);
            if (data == null) {
                return Collections.emptyList();
            }
            String[] paths = (String[]) data;
            List<File> list = new ArrayList<>(paths.length);
            for (String str : paths) {
                list.add(new File(str));
            }
            return list;
        }
        for (String mimeType : dataFormat.getIdentifiers()) {
            Object data2 = assistant.getData(mimeType);
            if (data2 instanceof ByteBuffer) {
                try {
                    ByteBuffer bb2 = (ByteBuffer) data2;
                    ByteArrayInputStream bis = new ByteArrayInputStream(bb2.array());
                    ObjectInput in = new ObjectInputStream(bis) { // from class: com.sun.javafx.tk.quantum.QuantumClipboard.1
                        @Override // java.io.ObjectInputStream
                        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                            return Class.forName(desc.getName(), false, Thread.currentThread().getContextClassLoader());
                        }
                    };
                    data2 = in.readObject();
                } catch (IOException e2) {
                } catch (ClassNotFoundException e3) {
                }
            }
            if (data2 != null) {
                return data2;
            }
        }
        return null;
    }

    private static Image convertObjectToImage(Object obj) {
        Pixels pixels;
        if (obj instanceof Image) {
            return (Image) obj;
        }
        if (obj instanceof ByteBuffer) {
            ByteBuffer bb2 = (ByteBuffer) obj;
            try {
                bb2.rewind();
                int width = bb2.getInt();
                int height = bb2.getInt();
                pixels = Application.GetApplication().createPixels(width, height, bb2.slice());
            } catch (Exception e2) {
                return null;
            }
        } else if (obj instanceof Pixels) {
            pixels = (Pixels) obj;
        } else {
            return null;
        }
        com.sun.prism.Image platformImage = PixelUtils.pixelsToImage(pixels);
        ImageLoader il = Toolkit.getToolkit().loadPlatformImage(platformImage);
        return Image.impl_fromPlatformImage(il);
    }

    private Image readImage() {
        String url;
        ClipboardAssistance assistant = currentDragboard != null ? currentDragboard : this.systemAssistant;
        Object rawData = assistant.getData(Clipboard.RAW_IMAGE_TYPE);
        if (rawData == null) {
            Object htmlData = assistant.getData(Clipboard.HTML_TYPE);
            if (htmlData != null && (url = parseIMG(htmlData)) != null) {
                try {
                    SecurityManager sm = System.getSecurityManager();
                    if (sm != null) {
                        AccessControlContext context = getAccessControlContext();
                        URL u2 = new URL(url);
                        String protocol = u2.getProtocol();
                        if (protocol.equalsIgnoreCase("jar")) {
                            String file = u2.getFile();
                            u2 = new URL(file);
                            protocol = u2.getProtocol();
                        }
                        if (protocol.equalsIgnoreCase(DeploymentDescriptorParser.ATTR_FILE)) {
                            FilePermission fp = new FilePermission(u2.getFile(), "read");
                            sm.checkPermission(fp, context);
                        } else if (protocol.equalsIgnoreCase("ftp") || protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https")) {
                            int port = u2.getPort();
                            String hoststr = port == -1 ? u2.getHost() : u2.getHost() + CallSiteDescriptor.TOKEN_DELIMITER + port;
                            SocketPermission sp = new SocketPermission(hoststr, SecurityConstants.SOCKET_CONNECT_ACTION);
                            sm.checkPermission(sp, context);
                        } else {
                            Permission clipboardPerm = PermissionHelper.getAccessClipboardPermission();
                            sm.checkPermission(clipboardPerm, context);
                        }
                    }
                    return new Image(url);
                } catch (SecurityException e2) {
                    return null;
                } catch (MalformedURLException e3) {
                    return null;
                }
            }
            return null;
        }
        return convertObjectToImage(rawData);
    }

    private String parseIMG(Object data) {
        if (data == null || !(data instanceof String)) {
            return null;
        }
        String str = (String) data;
        Matcher matcher = findTagIMG.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean placeImage(Image image) {
        if (image == null) {
            return false;
        }
        String url = image.impl_getUrl();
        if (url == null || PixelUtils.supportedFormatType(url)) {
            com.sun.prism.Image prismImage = (com.sun.prism.Image) image.impl_getPlatformImage();
            Pixels pixels = PixelUtils.imageToPixels(prismImage);
            if (pixels != null) {
                this.systemAssistant.setData(Clipboard.RAW_IMAGE_TYPE, pixels);
                return true;
            }
            return false;
        }
        this.systemAssistant.setData(Clipboard.URI_TYPE, url);
        return true;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public Set<DataFormat> getContentTypes() {
        Set<DataFormat> set = new HashSet<>();
        if (this.dataCache != null) {
            for (Pair<DataFormat, Object> pair : this.dataCache) {
                set.add(pair.getKey());
            }
            return set;
        }
        ClipboardAssistance assistant = currentDragboard != null ? currentDragboard : this.systemAssistant;
        String[] types = assistant.getMimeTypes();
        if (types == null) {
            return set;
        }
        for (String t2 : types) {
            if (t2.equalsIgnoreCase(Clipboard.RAW_IMAGE_TYPE)) {
                set.add(DataFormat.IMAGE);
            } else if (t2.equalsIgnoreCase(Clipboard.URI_TYPE)) {
                set.add(DataFormat.URL);
            } else if (t2.equalsIgnoreCase(Clipboard.FILE_LIST_TYPE)) {
                set.add(DataFormat.FILES);
            } else if (t2.equalsIgnoreCase(Clipboard.HTML_TYPE)) {
                set.add(DataFormat.HTML);
                try {
                    if (parseIMG(assistant.getData(Clipboard.HTML_TYPE)) != null) {
                        set.add(DataFormat.IMAGE);
                    }
                } catch (Exception e2) {
                }
            } else {
                DataFormat dataFormat = DataFormat.lookupMimeType(t2);
                if (dataFormat == null) {
                    dataFormat = new DataFormat(t2);
                }
                set.add(dataFormat);
            }
        }
        return set;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public boolean hasContent(DataFormat dataFormat) {
        if (this.dataCache != null) {
            for (Pair<DataFormat, Object> pair : this.dataCache) {
                if (pair.getKey() == dataFormat) {
                    return true;
                }
            }
            return false;
        }
        ClipboardAssistance assistant = currentDragboard != null ? currentDragboard : this.systemAssistant;
        String[] stypes = assistant.getMimeTypes();
        if (stypes == null) {
            return false;
        }
        for (String t2 : stypes) {
            if (dataFormat == DataFormat.IMAGE && t2.equalsIgnoreCase(Clipboard.RAW_IMAGE_TYPE)) {
                return true;
            }
            if (dataFormat == DataFormat.URL && t2.equalsIgnoreCase(Clipboard.URI_TYPE)) {
                return true;
            }
            if (dataFormat == DataFormat.IMAGE && t2.equalsIgnoreCase(Clipboard.HTML_TYPE) && parseIMG(assistant.getData(Clipboard.HTML_TYPE)) != null) {
                return true;
            }
            if (dataFormat == DataFormat.FILES && t2.equalsIgnoreCase(Clipboard.FILE_LIST_TYPE)) {
                return true;
            }
            DataFormat found = DataFormat.lookupMimeType(t2);
            if (found != null && found.equals(dataFormat)) {
                return true;
            }
        }
        return false;
    }

    private static ByteBuffer prepareImage(Image image) {
        PixelReader pr = image.getPixelReader();
        int w2 = (int) image.getWidth();
        int h2 = (int) image.getHeight();
        byte[] pixels = new byte[w2 * h2 * 4];
        pr.getPixels(0, 0, w2, h2, WritablePixelFormat.getByteBgraInstance(), pixels, 0, w2 * 4);
        ByteBuffer dragImageBuffer = ByteBuffer.allocate(8 + (w2 * h2 * 4));
        dragImageBuffer.putInt(w2);
        dragImageBuffer.putInt(h2);
        dragImageBuffer.put(pixels);
        return dragImageBuffer;
    }

    private static ByteBuffer prepareOffset(double offsetX, double offsetY) {
        ByteBuffer dragImageOffset = ByteBuffer.allocate(8);
        dragImageOffset.rewind();
        dragImageOffset.putInt((int) offsetX);
        dragImageOffset.putInt((int) offsetY);
        return dragImageOffset;
    }

    private boolean putContentToPeer(Pair<DataFormat, Object>... content) {
        this.systemAssistant.emptyCache();
        boolean dataSet = false;
        for (Pair<DataFormat, Object> pair : content) {
            DataFormat dataFormat = pair.getKey();
            Object data = pair.getValue();
            if (dataFormat == DataFormat.IMAGE) {
                dataSet = placeImage(convertObjectToImage(data));
            } else if (dataFormat == DataFormat.URL) {
                this.systemAssistant.setData(Clipboard.URI_TYPE, data);
                dataSet = true;
            } else if (dataFormat == DataFormat.RTF) {
                this.systemAssistant.setData(Clipboard.RTF_TYPE, data);
                dataSet = true;
            } else if (dataFormat == DataFormat.FILES) {
                List<File> list = (List) data;
                if (list.size() != 0) {
                    String[] paths = new String[list.size()];
                    int i2 = 0;
                    for (File f2 : list) {
                        int i3 = i2;
                        i2++;
                        paths[i3] = f2.getAbsolutePath();
                    }
                    this.systemAssistant.setData(Clipboard.FILE_LIST_TYPE, paths);
                    dataSet = true;
                }
            } else {
                if (data instanceof Serializable) {
                    if ((dataFormat != DataFormat.PLAIN_TEXT && dataFormat != DataFormat.HTML) || !(data instanceof String)) {
                        try {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ObjectOutput out = new ObjectOutputStream(bos);
                            out.writeObject(data);
                            out.close();
                            data = ByteBuffer.wrap(bos.toByteArray());
                        } catch (IOException e2) {
                            throw new IllegalArgumentException("Could not serialize the data", e2);
                        }
                    }
                } else if (data instanceof InputStream) {
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    try {
                        InputStream is = (InputStream) data;
                        Throwable th = null;
                        try {
                            try {
                                for (int i4 = is.read(); i4 != -1; i4 = is.read()) {
                                    bout.write(i4);
                                }
                                if (is != null) {
                                    if (0 != 0) {
                                        try {
                                            is.close();
                                        } catch (Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    } else {
                                        is.close();
                                    }
                                }
                                data = ByteBuffer.wrap(bout.toByteArray());
                            } finally {
                            }
                        } finally {
                        }
                    } catch (IOException e3) {
                        throw new IllegalArgumentException("Could not serialize the data", e3);
                    }
                } else if (!(data instanceof ByteBuffer)) {
                    throw new IllegalArgumentException("Only serializable objects or ByteBuffer can be used as data with data format " + ((Object) dataFormat));
                }
                for (String mimeType : dataFormat.getIdentifiers()) {
                    this.systemAssistant.setData(mimeType, data);
                    dataSet = true;
                }
            }
        }
        if (this.dragImage != null) {
            ByteBuffer imageBuffer = prepareImage(this.dragImage);
            ByteBuffer offsetBuffer = prepareOffset(this.dragOffsetX, this.dragOffsetY);
            this.systemAssistant.setData(Clipboard.DRAG_IMAGE, imageBuffer);
            this.systemAssistant.setData(Clipboard.DRAG_IMAGE_OFFSET, offsetBuffer);
        }
        return dataSet;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public boolean putContent(Pair<DataFormat, Object>... content) {
        for (Pair<DataFormat, Object> pair : content) {
            DataFormat format = pair.getKey();
            Object data = pair.getValue();
            if (format == null) {
                throw new NullPointerException("Clipboard.putContent: null data format");
            }
            if (data == null) {
                throw new NullPointerException("Clipboard.putContent: null data");
            }
        }
        boolean dataSet = false;
        if (this.isCaching) {
            if (this.dataCache == null) {
                this.dataCache = new ArrayList(content.length);
            }
            for (Pair<DataFormat, Object> pair2 : content) {
                this.dataCache.add(pair2);
                dataSet = true;
            }
        } else {
            dataSet = putContentToPeer(content);
            this.systemAssistant.flush();
        }
        return dataSet;
    }

    private void clearCache() {
        this.dataCache = null;
        this.transferModesCache = null;
    }

    private void clearDragView() {
        this.dragImage = null;
        this.dragOffsetY = 0.0d;
        this.dragOffsetX = 0.0d;
    }
}
