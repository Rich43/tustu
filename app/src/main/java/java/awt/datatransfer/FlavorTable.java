package java.awt.datatransfer;

import java.util.List;

/* loaded from: rt.jar:java/awt/datatransfer/FlavorTable.class */
public interface FlavorTable extends FlavorMap {
    List<String> getNativesForFlavor(DataFlavor dataFlavor);

    List<DataFlavor> getFlavorsForNative(String str);
}
