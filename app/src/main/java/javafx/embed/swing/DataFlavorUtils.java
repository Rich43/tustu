package javafx.embed.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.input.DataFormat;

/* loaded from: jfxrt.jar:javafx/embed/swing/DataFlavorUtils.class */
final class DataFlavorUtils {
    DataFlavorUtils() {
    }

    static String getFxMimeType(DataFlavor flavor) {
        return flavor.getPrimaryType() + "/" + flavor.getSubType();
    }

    static DataFlavor[] getDataFlavors(String[] mimeTypes) {
        ArrayList<DataFlavor> flavors = new ArrayList<>(mimeTypes.length);
        for (String mime : mimeTypes) {
            try {
                DataFlavor flavor = new DataFlavor(mime);
                flavors.add(flavor);
            } catch (ClassNotFoundException e2) {
            }
        }
        return (DataFlavor[]) flavors.toArray(new DataFlavor[0]);
    }

    static DataFlavor getDataFlavor(DataFormat format) {
        DataFlavor[] flavors = getDataFlavors((String[]) format.getIdentifiers().toArray(new String[1]));
        if (flavors.length == 0) {
            return null;
        }
        return flavors[0];
    }

    static String getMimeType(DataFormat format) {
        Iterator<String> it = format.getIdentifiers().iterator();
        if (!it.hasNext()) {
            return null;
        }
        String id = it.next();
        return id;
    }

    static DataFormat getDataFormat(DataFlavor flavor) {
        String mimeType = getFxMimeType(flavor);
        DataFormat dataFormat = DataFormat.lookupMimeType(mimeType);
        if (dataFormat == null) {
            dataFormat = new DataFormat(mimeType);
        }
        return dataFormat;
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/DataFlavorUtils$ByteBufferInputStream.class */
    private static class ByteBufferInputStream extends InputStream {

        /* renamed from: bb, reason: collision with root package name */
        private final ByteBuffer f12631bb;

        private ByteBufferInputStream(ByteBuffer bb2) {
            this.f12631bb = bb2;
        }

        @Override // java.io.InputStream
        public int available() {
            return this.f12631bb.remaining();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.f12631bb.hasRemaining()) {
                return this.f12631bb.get() & 255;
            }
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] bytes, int off, int len) throws IOException {
            if (!this.f12631bb.hasRemaining()) {
                return -1;
            }
            int len2 = Math.min(len, this.f12631bb.remaining());
            this.f12631bb.get(bytes, off, len2);
            return len2;
        }
    }

    static Object adjustFxData(DataFlavor flavor, Object fxData) throws UnsupportedEncodingException {
        byte[] bytes;
        if (fxData instanceof String) {
            if (flavor.isRepresentationClassInputStream()) {
                String encoding = flavor.getParameter("charset");
                if (encoding != null) {
                    bytes = ((String) fxData).getBytes(encoding);
                } else {
                    bytes = ((String) fxData).getBytes();
                }
                return new ByteArrayInputStream(bytes);
            }
            if (flavor.isRepresentationClassByteBuffer()) {
            }
        }
        if ((fxData instanceof ByteBuffer) && flavor.isRepresentationClassInputStream()) {
            return new ByteBufferInputStream((ByteBuffer) fxData);
        }
        return fxData;
    }

    static Object adjustSwingData(DataFlavor flavor, String mimeType, Object swingData) {
        if (swingData == null) {
            return swingData;
        }
        if (flavor.isFlavorJavaFileListType()) {
            List<File> fileList = (List) swingData;
            String[] paths = new String[fileList.size()];
            int i2 = 0;
            for (File f2 : fileList) {
                int i3 = i2;
                i2++;
                paths[i3] = f2.getPath();
            }
            return paths;
        }
        DataFormat dataFormat = DataFormat.lookupMimeType(mimeType);
        if (DataFormat.PLAIN_TEXT.equals(dataFormat)) {
            if (flavor.isFlavorTextType()) {
                if (swingData instanceof InputStream) {
                    InputStream in = (InputStream) swingData;
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] bb2 = new byte[64];
                    try {
                        for (int len = in.read(bb2); len != -1; len = in.read(bb2)) {
                            out.write(bb2, 0, len);
                        }
                        out.close();
                        return new String(out.toByteArray());
                    } catch (Exception e2) {
                    }
                }
            } else if (swingData != null) {
                return swingData.toString();
            }
        }
        return swingData;
    }

    static Map<String, DataFlavor> adjustSwingDataFlavors(DataFlavor[] flavors) {
        Map<String, Set<DataFlavor>> mimeType2Flavors = new HashMap<>(flavors.length);
        for (DataFlavor flavor : flavors) {
            String mimeType = getFxMimeType(flavor);
            if (mimeType2Flavors.containsKey(mimeType)) {
                try {
                    mimeType2Flavors.get(mimeType).add(flavor);
                } catch (UnsupportedOperationException e2) {
                }
            } else {
                Set<DataFlavor> mimeTypeFlavors = new HashSet<>();
                if (flavor.isFlavorTextType()) {
                    mimeTypeFlavors.add(DataFlavor.stringFlavor);
                    mimeTypeFlavors = Collections.unmodifiableSet(mimeTypeFlavors);
                } else {
                    mimeTypeFlavors.add(flavor);
                }
                mimeType2Flavors.put(mimeType, mimeTypeFlavors);
            }
        }
        Map<String, DataFlavor> mimeType2Flavor = new HashMap<>();
        for (String mimeType2 : mimeType2Flavors.keySet()) {
            DataFlavor[] mimeTypeFlavors2 = (DataFlavor[]) mimeType2Flavors.get(mimeType2).toArray(new DataFlavor[0]);
            if (mimeTypeFlavors2.length == 1) {
                mimeType2Flavor.put(mimeType2, mimeTypeFlavors2[0]);
            } else {
                mimeType2Flavor.put(mimeType2, mimeTypeFlavors2[0]);
            }
        }
        return mimeType2Flavor;
    }

    private static Object readData(Transferable t2, DataFlavor flavor) {
        Object obj = null;
        try {
            obj = t2.getTransferData(flavor);
        } catch (UnsupportedFlavorException ex) {
            ex.printStackTrace(System.err);
        } catch (IOException ex2) {
            ex2.printStackTrace(System.err);
        }
        return obj;
    }

    static Map<String, Object> readAllData(Transferable t2, Map<String, DataFlavor> fxMimeType2DataFlavor, boolean fetchData) {
        Map<String, Object> fxMimeType2Data = new HashMap<>();
        for (DataFlavor flavor : t2.getTransferDataFlavors()) {
            Object obj = fetchData ? readData(t2, flavor) : null;
            if (obj != null || !fetchData) {
                String mimeType = getFxMimeType(flavor);
                fxMimeType2Data.put(mimeType, adjustSwingData(flavor, mimeType, obj));
            }
        }
        for (Map.Entry<String, DataFlavor> e2 : fxMimeType2DataFlavor.entrySet()) {
            String mimeType2 = e2.getKey();
            DataFlavor flavor2 = e2.getValue();
            Object obj2 = fetchData ? readData(t2, flavor2) : null;
            if (obj2 != null || !fetchData) {
                fxMimeType2Data.put(e2.getKey(), adjustSwingData(flavor2, mimeType2, obj2));
            }
        }
        return fxMimeType2Data;
    }
}
