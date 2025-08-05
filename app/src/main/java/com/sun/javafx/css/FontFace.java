package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: jfxrt.jar:com/sun/javafx/css/FontFace.class */
public final class FontFace {
    private final Map<String, String> descriptors;
    private final List<FontFaceSrc> sources;

    /* loaded from: jfxrt.jar:com/sun/javafx/css/FontFace$FontFaceSrcType.class */
    public enum FontFaceSrcType {
        URL,
        LOCAL,
        REFERENCE
    }

    public FontFace(Map<String, String> descriptors, List<FontFaceSrc> sources) {
        this.descriptors = descriptors;
        this.sources = sources;
    }

    public Map<String, String> getDescriptors() {
        return this.descriptors;
    }

    public List<FontFaceSrc> getSources() {
        return this.sources;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("@font-face { ");
        for (Map.Entry<String, String> desc : this.descriptors.entrySet()) {
            sb.append(desc.getKey());
            sb.append(" : ");
            sb.append(desc.getValue());
            sb.append(VectorFormat.DEFAULT_SEPARATOR);
        }
        sb.append("src : ");
        for (FontFaceSrc src : this.sources) {
            sb.append((Object) src.getType());
            sb.append(" \"");
            sb.append(src.getSrc());
            sb.append("\", ");
        }
        sb.append(VectorFormat.DEFAULT_SEPARATOR);
        sb.append(" }");
        return sb.toString();
    }

    final void writeBinary(DataOutputStream os, StringStore stringStore) throws IOException {
        Set<Map.Entry<String, String>> entrySet = getDescriptors() != null ? getDescriptors().entrySet() : null;
        os.writeShort(entrySet != null ? entrySet.size() : 0);
        if (entrySet != null) {
            for (Map.Entry<String, String> entry : entrySet) {
                int index = stringStore.addString(entry.getKey());
                os.writeInt(index);
                int index2 = stringStore.addString(entry.getValue());
                os.writeInt(index2);
            }
        }
        List<FontFaceSrc> fontFaceSrcs = getSources();
        int nEntries = fontFaceSrcs != null ? fontFaceSrcs.size() : 0;
        os.writeShort(nEntries);
        for (int n2 = 0; n2 < nEntries; n2++) {
            FontFaceSrc fontFaceSrc = fontFaceSrcs.get(n2);
            fontFaceSrc.writeBinary(os, stringStore);
        }
    }

    static final FontFace readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
        int nEntries = is.readShort();
        Map<String, String> descriptors = new HashMap<>(nEntries);
        for (int n2 = 0; n2 < nEntries; n2++) {
            int index = is.readInt();
            String key = strings[index];
            int index2 = is.readInt();
            String value = strings[index2];
            descriptors.put(key, value);
        }
        int nEntries2 = is.readShort();
        List<FontFaceSrc> fontFaceSrcs = new ArrayList<>(nEntries2);
        for (int n3 = 0; n3 < nEntries2; n3++) {
            FontFaceSrc fontFaceSrc = FontFaceSrc.readBinary(bssVersion, is, strings);
            fontFaceSrcs.add(fontFaceSrc);
        }
        return new FontFace(descriptors, fontFaceSrcs);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/FontFace$FontFaceSrc.class */
    public static class FontFaceSrc {
        private final FontFaceSrcType type;
        private final String src;
        private final String format;

        public FontFaceSrc(FontFaceSrcType type, String src, String format) {
            this.type = type;
            this.src = src;
            this.format = format;
        }

        public FontFaceSrc(FontFaceSrcType type, String src) {
            this.type = type;
            this.src = src;
            this.format = null;
        }

        public FontFaceSrcType getType() {
            return this.type;
        }

        public String getSrc() {
            return this.src;
        }

        public String getFormat() {
            return this.format;
        }

        final void writeBinary(DataOutputStream os, StringStore stringStore) throws IOException {
            os.writeInt(stringStore.addString(this.type.name()));
            os.writeInt(stringStore.addString(this.src));
            os.writeInt(stringStore.addString(this.format));
        }

        static final FontFaceSrc readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
            int index = is.readInt();
            FontFaceSrcType type = strings[index] != null ? FontFaceSrcType.valueOf(strings[index]) : null;
            String src = strings[is.readInt()];
            String format = strings[is.readInt()];
            return new FontFaceSrc(type, src, format);
        }
    }
}
