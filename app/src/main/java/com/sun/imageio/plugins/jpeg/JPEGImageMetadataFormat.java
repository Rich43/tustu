package com.sun.imageio.plugins.jpeg;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.color.ICC_Profile;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormat;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageMetadataFormat.class */
public class JPEGImageMetadataFormat extends JPEGMetadataFormat {
    private static JPEGImageMetadataFormat theInstance = null;

    private JPEGImageMetadataFormat() {
        super(JPEG.nativeImageMetadataFormatName, 1);
        addElement("JPEGvariety", JPEG.nativeImageMetadataFormatName, 3);
        addElement("markerSequence", JPEG.nativeImageMetadataFormatName, 4);
        addElement("app0JFIF", "JPEGvariety", 2);
        addStreamElements("markerSequence");
        addElement("app14Adobe", "markerSequence", 0);
        addElement("sof", "markerSequence", 1, 4);
        addElement("sos", "markerSequence", 1, 4);
        addElement("JFXX", "app0JFIF", 1, Integer.MAX_VALUE);
        addElement("app0JFXX", "JFXX", 3);
        addElement("app2ICC", "app0JFIF", 0);
        addAttribute("app0JFIF", "majorVersion", 2, false, "1", "0", "255", true, true);
        addAttribute("app0JFIF", "minorVersion", 2, false, "2", "0", "255", true, true);
        ArrayList arrayList = new ArrayList();
        arrayList.add("0");
        arrayList.add("1");
        arrayList.add("2");
        addAttribute("app0JFIF", "resUnits", 2, false, "0", (List<String>) arrayList);
        addAttribute("app0JFIF", "Xdensity", 2, false, "1", "1", "65535", true, true);
        addAttribute("app0JFIF", "Ydensity", 2, false, "1", "1", "65535", true, true);
        addAttribute("app0JFIF", "thumbWidth", 2, false, "0", "0", "255", true, true);
        addAttribute("app0JFIF", "thumbHeight", 2, false, "0", "0", "255", true, true);
        addElement("JFIFthumbJPEG", "app0JFXX", 2);
        addElement("JFIFthumbPalette", "app0JFXX", 0);
        addElement("JFIFthumbRGB", "app0JFXX", 0);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("16");
        arrayList2.add("17");
        arrayList2.add("19");
        addAttribute("app0JFXX", "extensionCode", 2, false, (String) null, (List<String>) arrayList2);
        addChildElement("markerSequence", "JFIFthumbJPEG");
        addAttribute("JFIFthumbPalette", "thumbWidth", 2, false, null, "0", "255", true, true);
        addAttribute("JFIFthumbPalette", "thumbHeight", 2, false, null, "0", "255", true, true);
        addAttribute("JFIFthumbRGB", "thumbWidth", 2, false, null, "0", "255", true, true);
        addAttribute("JFIFthumbRGB", "thumbHeight", 2, false, null, "0", "255", true, true);
        addObjectValue("app2ICC", (Class<boolean>) ICC_Profile.class, false, (boolean) null);
        addAttribute("app14Adobe", "version", 2, false, "100", "100", "255", true, true);
        addAttribute("app14Adobe", "flags0", 2, false, "0", "0", "65535", true, true);
        addAttribute("app14Adobe", "flags1", 2, false, "0", "0", "65535", true, true);
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("0");
        arrayList3.add("1");
        arrayList3.add("2");
        addAttribute("app14Adobe", Constants.ELEMNAME_TRANSFORM_STRING, 2, true, (String) null, (List<String>) arrayList3);
        addElement("componentSpec", "sof", 0);
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("0");
        arrayList4.add("1");
        arrayList4.add("2");
        addAttribute("sof", "process", 2, false, (String) null, (List<String>) arrayList4);
        addAttribute("sof", "samplePrecision", 2, false, "8");
        addAttribute("sof", "numLines", 2, false, null, "0", "65535", true, true);
        addAttribute("sof", "samplesPerLine", 2, false, null, "0", "65535", true, true);
        ArrayList arrayList5 = new ArrayList();
        arrayList5.add("1");
        arrayList5.add("2");
        arrayList5.add("3");
        arrayList5.add("4");
        addAttribute("sof", "numFrameComponents", 2, false, (String) null, (List<String>) arrayList5);
        addAttribute("componentSpec", "componentId", 2, true, null, "0", "255", true, true);
        addAttribute("componentSpec", "HsamplingFactor", 2, true, null, "1", "255", true, true);
        addAttribute("componentSpec", "VsamplingFactor", 2, true, null, "1", "255", true, true);
        ArrayList arrayList6 = new ArrayList();
        arrayList6.add("0");
        arrayList6.add("1");
        arrayList6.add("2");
        arrayList6.add("3");
        addAttribute("componentSpec", "QtableSelector", 2, true, (String) null, (List<String>) arrayList6);
        addElement("scanComponentSpec", "sos", 0);
        addAttribute("sos", "numScanComponents", 2, true, (String) null, (List<String>) arrayList5);
        addAttribute("sos", "startSpectralSelection", 2, false, "0", "0", "63", true, true);
        addAttribute("sos", "endSpectralSelection", 2, false, "63", "0", "63", true, true);
        addAttribute("sos", "approxHigh", 2, false, "0", "0", "15", true, true);
        addAttribute("sos", "approxLow", 2, false, "0", "0", "15", true, true);
        addAttribute("scanComponentSpec", "componentSelector", 2, true, null, "0", "255", true, true);
        addAttribute("scanComponentSpec", "dcHuffTable", 2, true, (String) null, (List<String>) arrayList6);
        addAttribute("scanComponentSpec", "acHuffTable", 2, true, (String) null, (List<String>) arrayList6);
    }

    @Override // com.sun.imageio.plugins.jpeg.JPEGMetadataFormat, javax.imageio.metadata.IIOMetadataFormatImpl, javax.imageio.metadata.IIOMetadataFormat
    public boolean canNodeAppear(String str, ImageTypeSpecifier imageTypeSpecifier) {
        if (str.equals(getRootName()) || str.equals("JPEGvariety") || isInSubtree(str, "markerSequence")) {
            return true;
        }
        if (isInSubtree(str, "app0JFIF") && JPEG.isJFIFcompliant(imageTypeSpecifier, true)) {
            return true;
        }
        return false;
    }

    public static synchronized IIOMetadataFormat getInstance() {
        if (theInstance == null) {
            theInstance = new JPEGImageMetadataFormat();
        }
        return theInstance;
    }
}
