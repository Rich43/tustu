package java.awt.datatransfer;

import java.util.Map;

/* loaded from: rt.jar:java/awt/datatransfer/FlavorMap.class */
public interface FlavorMap {
    Map<DataFlavor, String> getNativesForFlavors(DataFlavor[] dataFlavorArr);

    Map<String, DataFlavor> getFlavorsForNatives(String[] strArr);
}
