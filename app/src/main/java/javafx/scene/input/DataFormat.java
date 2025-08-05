package javafx.scene.input;

import com.sun.javafx.util.WeakReferenceQueue;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/input/DataFormat.class */
public class DataFormat {
    private static final WeakReferenceQueue<DataFormat> DATA_FORMAT_LIST = new WeakReferenceQueue<>();
    public static final DataFormat PLAIN_TEXT = new DataFormat(com.sun.glass.ui.Clipboard.TEXT_TYPE);
    public static final DataFormat HTML = new DataFormat(com.sun.glass.ui.Clipboard.HTML_TYPE);
    public static final DataFormat RTF = new DataFormat(com.sun.glass.ui.Clipboard.RTF_TYPE);
    public static final DataFormat URL = new DataFormat(com.sun.glass.ui.Clipboard.URI_TYPE);
    public static final DataFormat IMAGE = new DataFormat(com.sun.glass.ui.Clipboard.RAW_IMAGE_TYPE);
    public static final DataFormat FILES = new DataFormat(com.sun.glass.ui.Clipboard.FILE_LIST_TYPE, "java.file-list");
    private static final DataFormat DRAG_IMAGE = new DataFormat(com.sun.glass.ui.Clipboard.DRAG_IMAGE);
    private static final DataFormat DRAG_IMAGE_OFFSET = new DataFormat(com.sun.glass.ui.Clipboard.DRAG_IMAGE_OFFSET);
    private final Set<String> identifier;

    public DataFormat(@NamedArg("ids") String... ids) {
        DATA_FORMAT_LIST.cleanup();
        if (ids != null) {
            for (String id : ids) {
                if (lookupMimeType(id) != null) {
                    throw new IllegalArgumentException("DataFormat '" + id + "' already exists.");
                }
            }
            this.identifier = Collections.unmodifiableSet(new HashSet(Arrays.asList(ids)));
        } else {
            this.identifier = Collections.emptySet();
        }
        DATA_FORMAT_LIST.add(this);
    }

    public final Set<String> getIdentifiers() {
        return this.identifier;
    }

    public String toString() {
        if (this.identifier.isEmpty()) {
            return "[]";
        }
        if (this.identifier.size() == 1) {
            StringBuilder sb = new StringBuilder("[");
            sb.append(this.identifier.iterator().next());
            return sb.append("]").toString();
        }
        StringBuilder b2 = new StringBuilder("[");
        Iterator<String> itr = this.identifier.iterator();
        while (itr.hasNext()) {
            b2 = b2.append(itr.next());
            if (itr.hasNext()) {
                b2 = b2.append(", ");
            }
        }
        return b2.append("]").toString();
    }

    public int hashCode() {
        int hash = 7;
        for (String id : this.identifier) {
            hash = (31 * hash) + id.hashCode();
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DataFormat)) {
            return false;
        }
        DataFormat otherDataFormat = (DataFormat) obj;
        if (this.identifier.equals(otherDataFormat.identifier)) {
            return true;
        }
        return false;
    }

    public static DataFormat lookupMimeType(String mimeType) {
        if (mimeType == null || mimeType.length() == 0) {
            return null;
        }
        Iterator itr = DATA_FORMAT_LIST.iterator();
        while (itr.hasNext()) {
            DataFormat dataFormat = itr.next();
            if (dataFormat.getIdentifiers().contains(mimeType)) {
                return dataFormat;
            }
        }
        return null;
    }
}
