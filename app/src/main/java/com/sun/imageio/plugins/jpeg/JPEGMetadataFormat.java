package com.sun.imageio.plugins.jpeg;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.plugins.jpeg.JPEGQTable;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGMetadataFormat.class */
abstract class JPEGMetadataFormat extends IIOMetadataFormatImpl {
    private static final int MAX_JPEG_DATA_SIZE = 65533;
    String resourceBaseName;

    JPEGMetadataFormat(String str, int i2) {
        super(str, i2);
        this.resourceBaseName = getClass().getName() + "Resources";
        setResourceBaseName(this.resourceBaseName);
    }

    void addStreamElements(String str) {
        addElement("dqt", str, 1, 4);
        addElement("dqtable", "dqt", 0);
        addAttribute("dqtable", "elementPrecision", 2, false, "0");
        ArrayList arrayList = new ArrayList();
        arrayList.add("0");
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        addAttribute("dqtable", "qtableId", 2, true, (String) null, (List<String>) arrayList);
        addObjectValue("dqtable", (Class<boolean>) JPEGQTable.class, true, (boolean) null);
        addElement("dht", str, 1, 4);
        addElement("dhtable", "dht", 0);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("0");
        arrayList2.add("1");
        addAttribute("dhtable", Constants.ATTRNAME_CLASS, 2, true, (String) null, (List<String>) arrayList2);
        addAttribute("dhtable", "htableId", 2, true, (String) null, (List<String>) arrayList);
        addObjectValue("dhtable", (Class<boolean>) JPEGHuffmanTable.class, true, (boolean) null);
        addElement("dri", str, 0);
        addAttribute("dri", "interval", 2, true, null, "0", "65535", true, true);
        addElement("com", str, 0);
        addAttribute("com", "comment", 0, false, null);
        addObjectValue("com", byte[].class, 1, 65533);
        addElement("unknown", str, 0);
        addAttribute("unknown", "MarkerTag", 2, true, null, "0", "255", true, true);
        addObjectValue("unknown", byte[].class, 1, 65533);
    }

    @Override // javax.imageio.metadata.IIOMetadataFormatImpl, javax.imageio.metadata.IIOMetadataFormat
    public boolean canNodeAppear(String str, ImageTypeSpecifier imageTypeSpecifier) {
        if (isInSubtree(str, getRootName())) {
            return true;
        }
        return false;
    }

    protected boolean isInSubtree(String str, String str2) {
        if (str.equals(str2)) {
            return true;
        }
        for (String str3 : getChildNames(str)) {
            if (isInSubtree(str, str3)) {
                return true;
            }
        }
        return false;
    }
}
