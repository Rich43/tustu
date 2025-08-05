package com.sun.imageio.plugins.jpeg;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageMetadataFormatResources.class */
public class JPEGImageMetadataFormatResources extends JPEGMetadataFormatResources {
    static final Object[][] imageContents = {new Object[]{"JPEGvariety", "A node grouping all marker segments specific to the variety of stream being read/written (e.g. JFIF) - may be empty"}, new Object[]{"markerSequence", "A node grouping all non-jfif marker segments"}, new Object[]{"app0jfif", "A JFIF APP0 marker segment"}, new Object[]{"app14Adobe", "An Adobe APP14 marker segment"}, new Object[]{"sof", "A Start Of Frame marker segment"}, new Object[]{"sos", "A Start Of Scan marker segment"}, new Object[]{"app0JFXX", "A JFIF extension marker segment"}, new Object[]{"app2ICC", "An ICC profile APP2 marker segment"}, new Object[]{"JFIFthumbJPEG", "A JFIF thumbnail in JPEG format (no JFIF segments permitted)"}, new Object[]{"JFIFthumbPalette", "A JFIF thumbnail as an RGB indexed image"}, new Object[]{"JFIFthumbRGB", "A JFIF thumbnail as an RGB image"}, new Object[]{"componentSpec", "A component specification for a frame"}, new Object[]{"scanComponentSpec", "A component specification for a scan"}, new Object[]{"app0JFIF/majorVersion", "The major JFIF version number"}, new Object[]{"app0JFIF/minorVersion", "The minor JFIF version number"}, new Object[]{"app0JFIF/resUnits", "The resolution units for Xdensity and Ydensity (0 = no units, just aspect ratio; 1 = dots/inch; 2 = dots/cm)"}, new Object[]{"app0JFIF/Xdensity", "The horizontal density or aspect ratio numerator"}, new Object[]{"app0JFIF/Ydensity", "The vertical density or aspect ratio denominator"}, new Object[]{"app0JFIF/thumbWidth", "The width of the thumbnail, or 0 if there isn't one"}, new Object[]{"app0JFIF/thumbHeight", "The height of the thumbnail, or 0 if there isn't one"}, new Object[]{"app0JFXX/extensionCode", "The JFXX extension code identifying thumbnail type: (16 = JPEG, 17 = indexed, 19 = RGB"}, new Object[]{"JFIFthumbPalette/thumbWidth", "The width of the thumbnail"}, new Object[]{"JFIFthumbPalette/thumbHeight", "The height of the thumbnail"}, new Object[]{"JFIFthumbRGB/thumbWidth", "The width of the thumbnail"}, new Object[]{"JFIFthumbRGB/thumbHeight", "The height of the thumbnail"}, new Object[]{"app14Adobe/version", "The version of Adobe APP14 marker segment"}, new Object[]{"app14Adobe/flags0", "The flags0 variable of an APP14 marker segment"}, new Object[]{"app14Adobe/flags1", "The flags1 variable of an APP14 marker segment"}, new Object[]{"app14Adobe/transform", "The color transform applied to the image (0 = Unknown, 1 = YCbCr, 2 = YCCK)"}, new Object[]{"sof/process", "The JPEG process (0 = Baseline sequential, 1 = Extended sequential, 2 = Progressive)"}, new Object[]{"sof/samplePrecision", "The number of bits per sample"}, new Object[]{"sof/numLines", "The number of lines in the image"}, new Object[]{"sof/samplesPerLine", "The number of samples per line"}, new Object[]{"sof/numFrameComponents", "The number of components in the image"}, new Object[]{"componentSpec/componentId", "The id for this component"}, new Object[]{"componentSpec/HsamplingFactor", "The horizontal sampling factor for this component"}, new Object[]{"componentSpec/VsamplingFactor", "The vertical sampling factor for this component"}, new Object[]{"componentSpec/QtableSelector", "The quantization table to use for this component"}, new Object[]{"sos/numScanComponents", "The number of components in the scan"}, new Object[]{"sos/startSpectralSelection", "The first spectral band included in this scan"}, new Object[]{"sos/endSpectralSelection", "The last spectral band included in this scan"}, new Object[]{"sos/approxHigh", "The highest bit position included in this scan"}, new Object[]{"sos/approxLow", "The lowest bit position included in this scan"}, new Object[]{"scanComponentSpec/componentSelector", "The id of this component"}, new Object[]{"scanComponentSpec/dcHuffTable", "The huffman table to use for encoding DC coefficients"}, new Object[]{"scanComponentSpec/acHuffTable", "The huffman table to use for encoding AC coefficients"}};

    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        Object[][] objArr = new Object[commonContents.length + imageContents.length][2];
        int i2 = 0;
        int i3 = 0;
        while (i3 < commonContents.length) {
            objArr[i2][0] = commonContents[i3][0];
            objArr[i2][1] = commonContents[i3][1];
            i3++;
            i2++;
        }
        int i4 = 0;
        while (i4 < imageContents.length) {
            objArr[i2][0] = imageContents[i4][0];
            objArr[i2][1] = imageContents[i4][1];
            i4++;
            i2++;
        }
        return objArr;
    }
}
