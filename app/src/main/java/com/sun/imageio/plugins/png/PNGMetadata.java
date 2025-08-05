package com.sun.imageio.plugins.png;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.glassfish.external.statistics.impl.StatisticImpl;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.pobjects.graphics.SoftMask;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/imageio/plugins/png/PNGMetadata.class */
public class PNGMetadata extends IIOMetadata implements Cloneable {
    public static final String nativeMetadataFormatName = "javax_imageio_png_1.0";
    protected static final String nativeMetadataFormatClassName = "com.sun.imageio.plugins.png.PNGMetadataFormat";
    public static final int PHYS_UNIT_UNKNOWN = 0;
    public static final int PHYS_UNIT_METER = 1;
    public boolean IHDR_present;
    public int IHDR_width;
    public int IHDR_height;
    public int IHDR_bitDepth;
    public int IHDR_colorType;
    public int IHDR_compressionMethod;
    public int IHDR_filterMethod;
    public int IHDR_interlaceMethod;
    public boolean PLTE_present;
    public byte[] PLTE_red;
    public byte[] PLTE_green;
    public byte[] PLTE_blue;
    public int[] PLTE_order;
    public boolean bKGD_present;
    public int bKGD_colorType;
    public int bKGD_index;
    public int bKGD_gray;
    public int bKGD_red;
    public int bKGD_green;
    public int bKGD_blue;
    public boolean cHRM_present;
    public int cHRM_whitePointX;
    public int cHRM_whitePointY;
    public int cHRM_redX;
    public int cHRM_redY;
    public int cHRM_greenX;
    public int cHRM_greenY;
    public int cHRM_blueX;
    public int cHRM_blueY;
    public boolean gAMA_present;
    public int gAMA_gamma;
    public boolean hIST_present;
    public char[] hIST_histogram;
    public boolean iCCP_present;
    public String iCCP_profileName;
    public int iCCP_compressionMethod;
    public byte[] iCCP_compressedProfile;
    public ArrayList<String> iTXt_keyword;
    public ArrayList<Boolean> iTXt_compressionFlag;
    public ArrayList<Integer> iTXt_compressionMethod;
    public ArrayList<String> iTXt_languageTag;
    public ArrayList<String> iTXt_translatedKeyword;
    public ArrayList<String> iTXt_text;
    public boolean pHYs_present;
    public int pHYs_pixelsPerUnitXAxis;
    public int pHYs_pixelsPerUnitYAxis;
    public int pHYs_unitSpecifier;
    public boolean sBIT_present;
    public int sBIT_colorType;
    public int sBIT_grayBits;
    public int sBIT_redBits;
    public int sBIT_greenBits;
    public int sBIT_blueBits;
    public int sBIT_alphaBits;
    public boolean sPLT_present;
    public String sPLT_paletteName;
    public int sPLT_sampleDepth;
    public int[] sPLT_red;
    public int[] sPLT_green;
    public int[] sPLT_blue;
    public int[] sPLT_alpha;
    public int[] sPLT_frequency;
    public boolean sRGB_present;
    public int sRGB_renderingIntent;
    public ArrayList<String> tEXt_keyword;
    public ArrayList<String> tEXt_text;
    public boolean tIME_present;
    public int tIME_year;
    public int tIME_month;
    public int tIME_day;
    public int tIME_hour;
    public int tIME_minute;
    public int tIME_second;
    public boolean tRNS_present;
    public int tRNS_colorType;
    public byte[] tRNS_alpha;
    public int tRNS_gray;
    public int tRNS_red;
    public int tRNS_green;
    public int tRNS_blue;
    public ArrayList<String> zTXt_keyword;
    public ArrayList<Integer> zTXt_compressionMethod;
    public ArrayList<String> zTXt_text;
    public ArrayList<String> unknownChunkType;
    public ArrayList<byte[]> unknownChunkData;
    static final String[] IHDR_colorTypeNames = {"Grayscale", null, "RGB", "Palette", "GrayAlpha", null, "RGBAlpha"};
    static final int[] IHDR_numChannels = {1, 0, 3, 3, 2, 0, 4};
    static final String[] IHDR_bitDepths = {"1", "2", "4", "8", "16"};
    static final String[] IHDR_compressionMethodNames = {"deflate"};
    static final String[] IHDR_filterMethodNames = {"adaptive"};
    static final String[] IHDR_interlaceMethodNames = {Separation.COLORANT_NONE, "adam7"};
    static final String[] iCCP_compressionMethodNames = {"deflate"};
    static final String[] zTXt_compressionMethodNames = {"deflate"};
    static final String[] unitSpecifierNames = {"unknown", "meter"};
    static final String[] renderingIntentNames = {"Perceptual", "Relative colorimetric", "Saturation", "Absolute colorimetric"};
    static final String[] colorSpaceTypeNames = {"GRAY", null, "RGB", "RGB", "GRAY", null, "RGB"};

    public PNGMetadata() {
        super(true, nativeMetadataFormatName, nativeMetadataFormatClassName, null, null);
        this.PLTE_order = null;
        this.iTXt_keyword = new ArrayList<>();
        this.iTXt_compressionFlag = new ArrayList<>();
        this.iTXt_compressionMethod = new ArrayList<>();
        this.iTXt_languageTag = new ArrayList<>();
        this.iTXt_translatedKeyword = new ArrayList<>();
        this.iTXt_text = new ArrayList<>();
        this.tEXt_keyword = new ArrayList<>();
        this.tEXt_text = new ArrayList<>();
        this.zTXt_keyword = new ArrayList<>();
        this.zTXt_compressionMethod = new ArrayList<>();
        this.zTXt_text = new ArrayList<>();
        this.unknownChunkType = new ArrayList<>();
        this.unknownChunkData = new ArrayList<>();
    }

    public PNGMetadata(IIOMetadata iIOMetadata) {
        this.PLTE_order = null;
        this.iTXt_keyword = new ArrayList<>();
        this.iTXt_compressionFlag = new ArrayList<>();
        this.iTXt_compressionMethod = new ArrayList<>();
        this.iTXt_languageTag = new ArrayList<>();
        this.iTXt_translatedKeyword = new ArrayList<>();
        this.iTXt_text = new ArrayList<>();
        this.tEXt_keyword = new ArrayList<>();
        this.tEXt_text = new ArrayList<>();
        this.zTXt_keyword = new ArrayList<>();
        this.zTXt_compressionMethod = new ArrayList<>();
        this.zTXt_text = new ArrayList<>();
        this.unknownChunkType = new ArrayList<>();
        this.unknownChunkData = new ArrayList<>();
    }

    public void initialize(ImageTypeSpecifier imageTypeSpecifier, int i2) {
        ColorModel colorModel = imageTypeSpecifier.getColorModel();
        int[] sampleSize = imageTypeSpecifier.getSampleModel().getSampleSize();
        int i3 = sampleSize[0];
        for (int i4 = 1; i4 < sampleSize.length; i4++) {
            if (sampleSize[i4] > i3) {
                i3 = sampleSize[i4];
            }
        }
        if (sampleSize.length > 1 && i3 < 8) {
            i3 = 8;
        }
        if (i3 > 2 && i3 < 4) {
            i3 = 4;
        } else if (i3 > 4 && i3 < 8) {
            i3 = 8;
        } else if (i3 > 8 && i3 < 16) {
            i3 = 16;
        } else if (i3 > 16) {
            throw new RuntimeException("bitDepth > 16!");
        }
        this.IHDR_bitDepth = i3;
        if (colorModel instanceof IndexColorModel) {
            IndexColorModel indexColorModel = (IndexColorModel) colorModel;
            int mapSize = indexColorModel.getMapSize();
            byte[] bArr = new byte[mapSize];
            indexColorModel.getReds(bArr);
            byte[] bArr2 = new byte[mapSize];
            indexColorModel.getGreens(bArr2);
            byte[] bArr3 = new byte[mapSize];
            indexColorModel.getBlues(bArr3);
            boolean z2 = false;
            if (!this.IHDR_present || this.IHDR_colorType != 3) {
                z2 = true;
                int i5 = 255 / ((1 << this.IHDR_bitDepth) - 1);
                for (int i6 = 0; i6 < mapSize; i6++) {
                    byte b2 = bArr[i6];
                    if (b2 != ((byte) (i6 * i5)) || b2 != bArr2[i6] || b2 != bArr3[i6]) {
                        z2 = false;
                        break;
                    }
                }
            }
            boolean zHasAlpha = colorModel.hasAlpha();
            byte[] bArr4 = null;
            if (zHasAlpha) {
                bArr4 = new byte[mapSize];
                indexColorModel.getAlphas(bArr4);
            }
            if (z2 && zHasAlpha && (i3 == 8 || i3 == 16)) {
                this.IHDR_colorType = 4;
            } else if (z2 && !zHasAlpha) {
                this.IHDR_colorType = 0;
            } else {
                this.IHDR_colorType = 3;
                this.PLTE_present = true;
                this.PLTE_order = null;
                this.PLTE_red = (byte[]) bArr.clone();
                this.PLTE_green = (byte[]) bArr2.clone();
                this.PLTE_blue = (byte[]) bArr3.clone();
                if (zHasAlpha) {
                    this.tRNS_present = true;
                    this.tRNS_colorType = 3;
                    this.PLTE_order = new int[bArr4.length];
                    byte[] bArr5 = new byte[bArr4.length];
                    int i7 = 0;
                    for (int i8 = 0; i8 < bArr4.length; i8++) {
                        if (bArr4[i8] != -1) {
                            this.PLTE_order[i8] = i7;
                            bArr5[i7] = bArr4[i8];
                            i7++;
                        }
                    }
                    int i9 = i7;
                    for (int i10 = 0; i10 < bArr4.length; i10++) {
                        if (bArr4[i10] == -1) {
                            int i11 = i7;
                            i7++;
                            this.PLTE_order[i10] = i11;
                        }
                    }
                    byte[] bArr6 = this.PLTE_red;
                    byte[] bArr7 = this.PLTE_green;
                    byte[] bArr8 = this.PLTE_blue;
                    int length = bArr6.length;
                    this.PLTE_red = new byte[length];
                    this.PLTE_green = new byte[length];
                    this.PLTE_blue = new byte[length];
                    for (int i12 = 0; i12 < length; i12++) {
                        this.PLTE_red[this.PLTE_order[i12]] = bArr6[i12];
                        this.PLTE_green[this.PLTE_order[i12]] = bArr7[i12];
                        this.PLTE_blue[this.PLTE_order[i12]] = bArr8[i12];
                    }
                    this.tRNS_alpha = new byte[i9];
                    System.arraycopy(bArr5, 0, this.tRNS_alpha, 0, i9);
                }
            }
        } else if (i2 == 1) {
            this.IHDR_colorType = 0;
        } else if (i2 == 2) {
            this.IHDR_colorType = 4;
        } else if (i2 == 3) {
            this.IHDR_colorType = 2;
        } else if (i2 == 4) {
            this.IHDR_colorType = 6;
        } else {
            throw new RuntimeException("Number of bands not 1-4!");
        }
        this.IHDR_present = true;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public boolean isReadOnly() {
        return false;
    }

    private ArrayList<byte[]> cloneBytesArrayList(ArrayList<byte[]> arrayList) {
        if (arrayList == null) {
            return null;
        }
        ArrayList<byte[]> arrayList2 = new ArrayList<>(arrayList.size());
        Iterator<byte[]> it = arrayList.iterator();
        while (it.hasNext()) {
            byte[] next = it.next();
            arrayList2.add(next == null ? null : (byte[]) next.clone());
        }
        return arrayList2;
    }

    public Object clone() {
        try {
            PNGMetadata pNGMetadata = (PNGMetadata) super.clone();
            pNGMetadata.unknownChunkData = cloneBytesArrayList(this.unknownChunkData);
            return pNGMetadata;
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public Node getAsTree(String str) {
        if (str.equals(nativeMetadataFormatName)) {
            return getNativeTree();
        }
        if (str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
            return getStandardTree();
        }
        throw new IllegalArgumentException("Not a recognized format!");
    }

    private Node getNativeTree() throws DOMException, CloneNotSupportedException {
        IIOMetadataNode iIOMetadataNode = null;
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode(nativeMetadataFormatName);
        if (this.IHDR_present) {
            IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("IHDR");
            iIOMetadataNode3.setAttribute(MetadataParser.WIDTH_TAG_NAME, Integer.toString(this.IHDR_width));
            iIOMetadataNode3.setAttribute(MetadataParser.HEIGHT_TAG_NAME, Integer.toString(this.IHDR_height));
            iIOMetadataNode3.setAttribute("bitDepth", Integer.toString(this.IHDR_bitDepth));
            iIOMetadataNode3.setAttribute("colorType", IHDR_colorTypeNames[this.IHDR_colorType]);
            iIOMetadataNode3.setAttribute("compressionMethod", IHDR_compressionMethodNames[this.IHDR_compressionMethod]);
            iIOMetadataNode3.setAttribute("filterMethod", IHDR_filterMethodNames[this.IHDR_filterMethod]);
            iIOMetadataNode3.setAttribute("interlaceMethod", IHDR_interlaceMethodNames[this.IHDR_interlaceMethod]);
            iIOMetadataNode2.appendChild(iIOMetadataNode3);
        }
        if (this.PLTE_present) {
            Node iIOMetadataNode4 = new IIOMetadataNode("PLTE");
            int length = this.PLTE_red.length;
            for (int i2 = 0; i2 < length; i2++) {
                IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("PLTEEntry");
                iIOMetadataNode5.setAttribute("index", Integer.toString(i2));
                iIOMetadataNode5.setAttribute("red", Integer.toString(this.PLTE_red[i2] & 255));
                iIOMetadataNode5.setAttribute("green", Integer.toString(this.PLTE_green[i2] & 255));
                iIOMetadataNode5.setAttribute("blue", Integer.toString(this.PLTE_blue[i2] & 255));
                iIOMetadataNode4.appendChild(iIOMetadataNode5);
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode4);
        }
        if (this.bKGD_present) {
            Node iIOMetadataNode6 = new IIOMetadataNode("bKGD");
            if (this.bKGD_colorType == 3) {
                iIOMetadataNode = new IIOMetadataNode("bKGD_Palette");
                iIOMetadataNode.setAttribute("index", Integer.toString(this.bKGD_index));
            } else if (this.bKGD_colorType == 0) {
                iIOMetadataNode = new IIOMetadataNode("bKGD_Grayscale");
                iIOMetadataNode.setAttribute("gray", Integer.toString(this.bKGD_gray));
            } else if (this.bKGD_colorType == 2) {
                iIOMetadataNode = new IIOMetadataNode("bKGD_RGB");
                iIOMetadataNode.setAttribute("red", Integer.toString(this.bKGD_red));
                iIOMetadataNode.setAttribute("green", Integer.toString(this.bKGD_green));
                iIOMetadataNode.setAttribute("blue", Integer.toString(this.bKGD_blue));
            }
            iIOMetadataNode6.appendChild(iIOMetadataNode);
            iIOMetadataNode2.appendChild(iIOMetadataNode6);
        }
        if (this.cHRM_present) {
            IIOMetadataNode iIOMetadataNode7 = new IIOMetadataNode("cHRM");
            iIOMetadataNode7.setAttribute("whitePointX", Integer.toString(this.cHRM_whitePointX));
            iIOMetadataNode7.setAttribute("whitePointY", Integer.toString(this.cHRM_whitePointY));
            iIOMetadataNode7.setAttribute("redX", Integer.toString(this.cHRM_redX));
            iIOMetadataNode7.setAttribute("redY", Integer.toString(this.cHRM_redY));
            iIOMetadataNode7.setAttribute("greenX", Integer.toString(this.cHRM_greenX));
            iIOMetadataNode7.setAttribute("greenY", Integer.toString(this.cHRM_greenY));
            iIOMetadataNode7.setAttribute("blueX", Integer.toString(this.cHRM_blueX));
            iIOMetadataNode7.setAttribute("blueY", Integer.toString(this.cHRM_blueY));
            iIOMetadataNode2.appendChild(iIOMetadataNode7);
        }
        if (this.gAMA_present) {
            IIOMetadataNode iIOMetadataNode8 = new IIOMetadataNode("gAMA");
            iIOMetadataNode8.setAttribute("value", Integer.toString(this.gAMA_gamma));
            iIOMetadataNode2.appendChild(iIOMetadataNode8);
        }
        if (this.hIST_present) {
            Node iIOMetadataNode9 = new IIOMetadataNode("hIST");
            for (int i3 = 0; i3 < this.hIST_histogram.length; i3++) {
                IIOMetadataNode iIOMetadataNode10 = new IIOMetadataNode("hISTEntry");
                iIOMetadataNode10.setAttribute("index", Integer.toString(i3));
                iIOMetadataNode10.setAttribute("value", Integer.toString(this.hIST_histogram[i3]));
                iIOMetadataNode9.appendChild(iIOMetadataNode10);
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode9);
        }
        if (this.iCCP_present) {
            IIOMetadataNode iIOMetadataNode11 = new IIOMetadataNode("iCCP");
            iIOMetadataNode11.setAttribute("profileName", this.iCCP_profileName);
            iIOMetadataNode11.setAttribute("compressionMethod", iCCP_compressionMethodNames[this.iCCP_compressionMethod]);
            Object objClone = this.iCCP_compressedProfile;
            if (objClone != null) {
                objClone = ((byte[]) objClone).clone();
            }
            iIOMetadataNode11.setUserObject(objClone);
            iIOMetadataNode2.appendChild(iIOMetadataNode11);
        }
        if (this.iTXt_keyword.size() > 0) {
            Node iIOMetadataNode12 = new IIOMetadataNode("iTXt");
            for (int i4 = 0; i4 < this.iTXt_keyword.size(); i4++) {
                IIOMetadataNode iIOMetadataNode13 = new IIOMetadataNode("iTXtEntry");
                iIOMetadataNode13.setAttribute("keyword", this.iTXt_keyword.get(i4));
                iIOMetadataNode13.setAttribute("compressionFlag", this.iTXt_compressionFlag.get(i4).booleanValue() ? "TRUE" : "FALSE");
                iIOMetadataNode13.setAttribute("compressionMethod", this.iTXt_compressionMethod.get(i4).toString());
                iIOMetadataNode13.setAttribute("languageTag", this.iTXt_languageTag.get(i4));
                iIOMetadataNode13.setAttribute("translatedKeyword", this.iTXt_translatedKeyword.get(i4));
                iIOMetadataNode13.setAttribute("text", this.iTXt_text.get(i4));
                iIOMetadataNode12.appendChild(iIOMetadataNode13);
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode12);
        }
        if (this.pHYs_present) {
            IIOMetadataNode iIOMetadataNode14 = new IIOMetadataNode("pHYs");
            iIOMetadataNode14.setAttribute("pixelsPerUnitXAxis", Integer.toString(this.pHYs_pixelsPerUnitXAxis));
            iIOMetadataNode14.setAttribute("pixelsPerUnitYAxis", Integer.toString(this.pHYs_pixelsPerUnitYAxis));
            iIOMetadataNode14.setAttribute("unitSpecifier", unitSpecifierNames[this.pHYs_unitSpecifier]);
            iIOMetadataNode2.appendChild(iIOMetadataNode14);
        }
        if (this.sBIT_present) {
            Node iIOMetadataNode15 = new IIOMetadataNode("sBIT");
            if (this.sBIT_colorType == 0) {
                iIOMetadataNode = new IIOMetadataNode("sBIT_Grayscale");
                iIOMetadataNode.setAttribute("gray", Integer.toString(this.sBIT_grayBits));
            } else if (this.sBIT_colorType == 4) {
                iIOMetadataNode = new IIOMetadataNode("sBIT_GrayAlpha");
                iIOMetadataNode.setAttribute("gray", Integer.toString(this.sBIT_grayBits));
                iIOMetadataNode.setAttribute("alpha", Integer.toString(this.sBIT_alphaBits));
            } else if (this.sBIT_colorType == 2) {
                iIOMetadataNode = new IIOMetadataNode("sBIT_RGB");
                iIOMetadataNode.setAttribute("red", Integer.toString(this.sBIT_redBits));
                iIOMetadataNode.setAttribute("green", Integer.toString(this.sBIT_greenBits));
                iIOMetadataNode.setAttribute("blue", Integer.toString(this.sBIT_blueBits));
            } else if (this.sBIT_colorType == 6) {
                iIOMetadataNode = new IIOMetadataNode("sBIT_RGBAlpha");
                iIOMetadataNode.setAttribute("red", Integer.toString(this.sBIT_redBits));
                iIOMetadataNode.setAttribute("green", Integer.toString(this.sBIT_greenBits));
                iIOMetadataNode.setAttribute("blue", Integer.toString(this.sBIT_blueBits));
                iIOMetadataNode.setAttribute("alpha", Integer.toString(this.sBIT_alphaBits));
            } else if (this.sBIT_colorType == 3) {
                iIOMetadataNode = new IIOMetadataNode("sBIT_Palette");
                iIOMetadataNode.setAttribute("red", Integer.toString(this.sBIT_redBits));
                iIOMetadataNode.setAttribute("green", Integer.toString(this.sBIT_greenBits));
                iIOMetadataNode.setAttribute("blue", Integer.toString(this.sBIT_blueBits));
            }
            iIOMetadataNode15.appendChild(iIOMetadataNode);
            iIOMetadataNode2.appendChild(iIOMetadataNode15);
        }
        if (this.sPLT_present) {
            IIOMetadataNode iIOMetadataNode16 = new IIOMetadataNode("sPLT");
            iIOMetadataNode16.setAttribute("name", this.sPLT_paletteName);
            iIOMetadataNode16.setAttribute("sampleDepth", Integer.toString(this.sPLT_sampleDepth));
            int length2 = this.sPLT_red.length;
            for (int i5 = 0; i5 < length2; i5++) {
                IIOMetadataNode iIOMetadataNode17 = new IIOMetadataNode("sPLTEntry");
                iIOMetadataNode17.setAttribute("index", Integer.toString(i5));
                iIOMetadataNode17.setAttribute("red", Integer.toString(this.sPLT_red[i5]));
                iIOMetadataNode17.setAttribute("green", Integer.toString(this.sPLT_green[i5]));
                iIOMetadataNode17.setAttribute("blue", Integer.toString(this.sPLT_blue[i5]));
                iIOMetadataNode17.setAttribute("alpha", Integer.toString(this.sPLT_alpha[i5]));
                iIOMetadataNode17.setAttribute("frequency", Integer.toString(this.sPLT_frequency[i5]));
                iIOMetadataNode16.appendChild(iIOMetadataNode17);
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode16);
        }
        if (this.sRGB_present) {
            IIOMetadataNode iIOMetadataNode18 = new IIOMetadataNode("sRGB");
            iIOMetadataNode18.setAttribute("renderingIntent", renderingIntentNames[this.sRGB_renderingIntent]);
            iIOMetadataNode2.appendChild(iIOMetadataNode18);
        }
        if (this.tEXt_keyword.size() > 0) {
            Node iIOMetadataNode19 = new IIOMetadataNode("tEXt");
            for (int i6 = 0; i6 < this.tEXt_keyword.size(); i6++) {
                IIOMetadataNode iIOMetadataNode20 = new IIOMetadataNode("tEXtEntry");
                iIOMetadataNode20.setAttribute("keyword", this.tEXt_keyword.get(i6));
                iIOMetadataNode20.setAttribute("value", this.tEXt_text.get(i6));
                iIOMetadataNode19.appendChild(iIOMetadataNode20);
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode19);
        }
        if (this.tIME_present) {
            IIOMetadataNode iIOMetadataNode21 = new IIOMetadataNode("tIME");
            iIOMetadataNode21.setAttribute(MetadataParser.YEAR_TAG_NAME, Integer.toString(this.tIME_year));
            iIOMetadataNode21.setAttribute("month", Integer.toString(this.tIME_month));
            iIOMetadataNode21.setAttribute("day", Integer.toString(this.tIME_day));
            iIOMetadataNode21.setAttribute("hour", Integer.toString(this.tIME_hour));
            iIOMetadataNode21.setAttribute("minute", Integer.toString(this.tIME_minute));
            iIOMetadataNode21.setAttribute(StatisticImpl.UNIT_SECOND, Integer.toString(this.tIME_second));
            iIOMetadataNode2.appendChild(iIOMetadataNode21);
        }
        if (this.tRNS_present) {
            Node iIOMetadataNode22 = new IIOMetadataNode("tRNS");
            if (this.tRNS_colorType == 3) {
                iIOMetadataNode = new IIOMetadataNode("tRNS_Palette");
                for (int i7 = 0; i7 < this.tRNS_alpha.length; i7++) {
                    IIOMetadataNode iIOMetadataNode23 = new IIOMetadataNode("tRNS_PaletteEntry");
                    iIOMetadataNode23.setAttribute("index", Integer.toString(i7));
                    iIOMetadataNode23.setAttribute("alpha", Integer.toString(this.tRNS_alpha[i7] & 255));
                    iIOMetadataNode.appendChild(iIOMetadataNode23);
                }
            } else if (this.tRNS_colorType == 0) {
                iIOMetadataNode = new IIOMetadataNode("tRNS_Grayscale");
                iIOMetadataNode.setAttribute("gray", Integer.toString(this.tRNS_gray));
            } else if (this.tRNS_colorType == 2) {
                iIOMetadataNode = new IIOMetadataNode("tRNS_RGB");
                iIOMetadataNode.setAttribute("red", Integer.toString(this.tRNS_red));
                iIOMetadataNode.setAttribute("green", Integer.toString(this.tRNS_green));
                iIOMetadataNode.setAttribute("blue", Integer.toString(this.tRNS_blue));
            }
            iIOMetadataNode22.appendChild(iIOMetadataNode);
            iIOMetadataNode2.appendChild(iIOMetadataNode22);
        }
        if (this.zTXt_keyword.size() > 0) {
            Node iIOMetadataNode24 = new IIOMetadataNode("zTXt");
            for (int i8 = 0; i8 < this.zTXt_keyword.size(); i8++) {
                IIOMetadataNode iIOMetadataNode25 = new IIOMetadataNode("zTXtEntry");
                iIOMetadataNode25.setAttribute("keyword", this.zTXt_keyword.get(i8));
                iIOMetadataNode25.setAttribute("compressionMethod", zTXt_compressionMethodNames[this.zTXt_compressionMethod.get(i8).intValue()]);
                iIOMetadataNode25.setAttribute("text", this.zTXt_text.get(i8));
                iIOMetadataNode24.appendChild(iIOMetadataNode25);
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode24);
        }
        if (this.unknownChunkType.size() > 0) {
            Node iIOMetadataNode26 = new IIOMetadataNode("UnknownChunks");
            for (int i9 = 0; i9 < this.unknownChunkType.size(); i9++) {
                IIOMetadataNode iIOMetadataNode27 = new IIOMetadataNode("UnknownChunk");
                iIOMetadataNode27.setAttribute("type", this.unknownChunkType.get(i9));
                iIOMetadataNode27.setUserObject(this.unknownChunkData.get(i9));
                iIOMetadataNode26.appendChild(iIOMetadataNode27);
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode26);
        }
        return iIOMetadataNode2;
    }

    private int getNumChannels() {
        int i2 = IHDR_numChannels[this.IHDR_colorType];
        if (this.IHDR_colorType == 3 && this.tRNS_present && this.tRNS_colorType == this.IHDR_colorType) {
            i2 = 4;
        }
        return i2;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardChromaNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode;
        int i2;
        int i3;
        int i4;
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("Chroma");
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("ColorSpaceType");
        iIOMetadataNode3.setAttribute("name", colorSpaceTypeNames[this.IHDR_colorType]);
        iIOMetadataNode2.appendChild(iIOMetadataNode3);
        IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("NumChannels");
        iIOMetadataNode4.setAttribute("value", Integer.toString(getNumChannels()));
        iIOMetadataNode2.appendChild(iIOMetadataNode4);
        if (this.gAMA_present) {
            IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("Gamma");
            iIOMetadataNode5.setAttribute("value", Float.toString(this.gAMA_gamma * 1.0E-5f));
            iIOMetadataNode2.appendChild(iIOMetadataNode5);
        }
        IIOMetadataNode iIOMetadataNode6 = new IIOMetadataNode("BlackIsZero");
        iIOMetadataNode6.setAttribute("value", "TRUE");
        iIOMetadataNode2.appendChild(iIOMetadataNode6);
        if (this.PLTE_present) {
            boolean z2 = this.tRNS_present && this.tRNS_colorType == 3;
            Node iIOMetadataNode7 = new IIOMetadataNode("Palette");
            int i5 = 0;
            while (i5 < this.PLTE_red.length) {
                IIOMetadataNode iIOMetadataNode8 = new IIOMetadataNode("PaletteEntry");
                iIOMetadataNode8.setAttribute("index", Integer.toString(i5));
                iIOMetadataNode8.setAttribute("red", Integer.toString(this.PLTE_red[i5] & 255));
                iIOMetadataNode8.setAttribute("green", Integer.toString(this.PLTE_green[i5] & 255));
                iIOMetadataNode8.setAttribute("blue", Integer.toString(this.PLTE_blue[i5] & 255));
                if (z2) {
                    iIOMetadataNode8.setAttribute("alpha", Integer.toString(i5 < this.tRNS_alpha.length ? this.tRNS_alpha[i5] & 255 : 255));
                }
                iIOMetadataNode7.appendChild(iIOMetadataNode8);
                i5++;
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode7);
        }
        if (this.bKGD_present) {
            if (this.bKGD_colorType == 3) {
                iIOMetadataNode = new IIOMetadataNode("BackgroundIndex");
                iIOMetadataNode.setAttribute("value", Integer.toString(this.bKGD_index));
            } else {
                iIOMetadataNode = new IIOMetadataNode("BackgroundColor");
                if (this.bKGD_colorType == 0) {
                    int i6 = this.bKGD_gray;
                    i4 = i6;
                    i3 = i6;
                    i2 = i6;
                } else {
                    i2 = this.bKGD_red;
                    i3 = this.bKGD_green;
                    i4 = this.bKGD_blue;
                }
                iIOMetadataNode.setAttribute("red", Integer.toString(i2));
                iIOMetadataNode.setAttribute("green", Integer.toString(i3));
                iIOMetadataNode.setAttribute("blue", Integer.toString(i4));
            }
            iIOMetadataNode2.appendChild(iIOMetadataNode);
        }
        return iIOMetadataNode2;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardCompressionNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Compression");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
        iIOMetadataNode2.setAttribute("value", "deflate");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("Lossless");
        iIOMetadataNode3.setAttribute("value", "TRUE");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("NumProgressiveScans");
        iIOMetadataNode4.setAttribute("value", this.IHDR_interlaceMethod == 0 ? "1" : "7");
        iIOMetadataNode.appendChild(iIOMetadataNode4);
        return iIOMetadataNode;
    }

    private String repeat(String str, int i2) {
        if (i2 == 1) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(((str.length() + 1) * i2) - 1);
        stringBuffer.append(str);
        for (int i3 = 1; i3 < i2; i3++) {
            stringBuffer.append(" ");
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardDataNode() throws DOMException {
        String string;
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Data");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("PlanarConfiguration");
        iIOMetadataNode2.setAttribute("value", "PixelInterleaved");
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("SampleFormat");
        iIOMetadataNode3.setAttribute("value", this.IHDR_colorType == 3 ? "Index" : "UnsignedIntegral");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        String string2 = Integer.toString(this.IHDR_bitDepth);
        IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("BitsPerSample");
        iIOMetadataNode4.setAttribute("value", repeat(string2, getNumChannels()));
        iIOMetadataNode.appendChild(iIOMetadataNode4);
        if (this.sBIT_present) {
            IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("SignificantBitsPerSample");
            if (this.sBIT_colorType == 0 || this.sBIT_colorType == 4) {
                string = Integer.toString(this.sBIT_grayBits);
            } else {
                string = Integer.toString(this.sBIT_redBits) + " " + Integer.toString(this.sBIT_greenBits) + " " + Integer.toString(this.sBIT_blueBits);
            }
            if (this.sBIT_colorType == 4 || this.sBIT_colorType == 6) {
                string = string + " " + Integer.toString(this.sBIT_alphaBits);
            }
            iIOMetadataNode5.setAttribute("value", string);
            iIOMetadataNode.appendChild(iIOMetadataNode5);
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardDimensionNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Dimension");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("PixelAspectRatio");
        iIOMetadataNode2.setAttribute("value", Float.toString(this.pHYs_present ? this.pHYs_pixelsPerUnitXAxis / this.pHYs_pixelsPerUnitYAxis : 1.0f));
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("ImageOrientation");
        iIOMetadataNode3.setAttribute("value", "Normal");
        iIOMetadataNode.appendChild(iIOMetadataNode3);
        if (this.pHYs_present && this.pHYs_unitSpecifier == 1) {
            IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("HorizontalPixelSize");
            iIOMetadataNode4.setAttribute("value", Float.toString(1000.0f / this.pHYs_pixelsPerUnitXAxis));
            iIOMetadataNode.appendChild(iIOMetadataNode4);
            IIOMetadataNode iIOMetadataNode5 = new IIOMetadataNode("VerticalPixelSize");
            iIOMetadataNode5.setAttribute("value", Float.toString(1000.0f / this.pHYs_pixelsPerUnitYAxis));
            iIOMetadataNode.appendChild(iIOMetadataNode5);
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardDocumentNode() throws DOMException {
        if (!this.tIME_present) {
            return null;
        }
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Document");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("ImageModificationTime");
        iIOMetadataNode2.setAttribute(MetadataParser.YEAR_TAG_NAME, Integer.toString(this.tIME_year));
        iIOMetadataNode2.setAttribute("month", Integer.toString(this.tIME_month));
        iIOMetadataNode2.setAttribute("day", Integer.toString(this.tIME_day));
        iIOMetadataNode2.setAttribute("hour", Integer.toString(this.tIME_hour));
        iIOMetadataNode2.setAttribute("minute", Integer.toString(this.tIME_minute));
        iIOMetadataNode2.setAttribute(StatisticImpl.UNIT_SECOND, Integer.toString(this.tIME_second));
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardTextNode() throws DOMException {
        if (this.tEXt_keyword.size() + this.iTXt_keyword.size() + this.zTXt_keyword.size() == 0) {
            return null;
        }
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Text");
        for (int i2 = 0; i2 < this.tEXt_keyword.size(); i2++) {
            IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode("TextEntry");
            iIOMetadataNode2.setAttribute("keyword", this.tEXt_keyword.get(i2));
            iIOMetadataNode2.setAttribute("value", this.tEXt_text.get(i2));
            iIOMetadataNode2.setAttribute("encoding", FTP.DEFAULT_CONTROL_ENCODING);
            iIOMetadataNode2.setAttribute("compression", Separation.COLORANT_NONE);
            iIOMetadataNode.appendChild(iIOMetadataNode2);
        }
        for (int i3 = 0; i3 < this.iTXt_keyword.size(); i3++) {
            IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("TextEntry");
            iIOMetadataNode3.setAttribute("keyword", this.iTXt_keyword.get(i3));
            iIOMetadataNode3.setAttribute("value", this.iTXt_text.get(i3));
            iIOMetadataNode3.setAttribute("language", this.iTXt_languageTag.get(i3));
            if (this.iTXt_compressionFlag.get(i3).booleanValue()) {
                iIOMetadataNode3.setAttribute("compression", "zip");
            } else {
                iIOMetadataNode3.setAttribute("compression", Separation.COLORANT_NONE);
            }
            iIOMetadataNode.appendChild(iIOMetadataNode3);
        }
        for (int i4 = 0; i4 < this.zTXt_keyword.size(); i4++) {
            IIOMetadataNode iIOMetadataNode4 = new IIOMetadataNode("TextEntry");
            iIOMetadataNode4.setAttribute("keyword", this.zTXt_keyword.get(i4));
            iIOMetadataNode4.setAttribute("value", this.zTXt_text.get(i4));
            iIOMetadataNode4.setAttribute("compression", "zip");
            iIOMetadataNode.appendChild(iIOMetadataNode4);
        }
        return iIOMetadataNode;
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public IIOMetadataNode getStandardTransparencyNode() throws DOMException {
        IIOMetadataNode iIOMetadataNode = new IIOMetadataNode("Transparency");
        IIOMetadataNode iIOMetadataNode2 = new IIOMetadataNode(SoftMask.SOFT_MASK_TYPE_ALPHA);
        iIOMetadataNode2.setAttribute("value", this.IHDR_colorType == 6 || this.IHDR_colorType == 4 || (this.IHDR_colorType == 3 && this.tRNS_present && this.tRNS_colorType == this.IHDR_colorType && this.tRNS_alpha != null) ? "nonpremultipled" : Separation.COLORANT_NONE);
        iIOMetadataNode.appendChild(iIOMetadataNode2);
        if (this.tRNS_present) {
            IIOMetadataNode iIOMetadataNode3 = new IIOMetadataNode("TransparentColor");
            if (this.tRNS_colorType == 2) {
                iIOMetadataNode3.setAttribute("value", Integer.toString(this.tRNS_red) + " " + Integer.toString(this.tRNS_green) + " " + Integer.toString(this.tRNS_blue));
            } else if (this.tRNS_colorType == 0) {
                iIOMetadataNode3.setAttribute("value", Integer.toString(this.tRNS_gray));
            }
            iIOMetadataNode.appendChild(iIOMetadataNode3);
        }
        return iIOMetadataNode;
    }

    private void fatal(Node node, String str) throws IIOInvalidTreeException {
        throw new IIOInvalidTreeException(str, node);
    }

    private String getStringAttribute(Node node, String str, String str2, boolean z2) throws IIOInvalidTreeException {
        Node namedItem = node.getAttributes().getNamedItem(str);
        if (namedItem == null) {
            if (!z2) {
                return str2;
            }
            fatal(node, "Required attribute " + str + " not present!");
        }
        return namedItem.getNodeValue();
    }

    private int getIntAttribute(Node node, String str, int i2, boolean z2) throws IIOInvalidTreeException {
        String stringAttribute = getStringAttribute(node, str, null, z2);
        if (stringAttribute == null) {
            return i2;
        }
        return Integer.parseInt(stringAttribute);
    }

    private float getFloatAttribute(Node node, String str, float f2, boolean z2) throws IIOInvalidTreeException {
        String stringAttribute = getStringAttribute(node, str, null, z2);
        if (stringAttribute == null) {
            return f2;
        }
        return Float.parseFloat(stringAttribute);
    }

    private int getIntAttribute(Node node, String str) throws IIOInvalidTreeException {
        return getIntAttribute(node, str, -1, true);
    }

    private float getFloatAttribute(Node node, String str) throws IIOInvalidTreeException {
        return getFloatAttribute(node, str, -1.0f, true);
    }

    private boolean getBooleanAttribute(Node node, String str, boolean z2, boolean z3) throws IIOInvalidTreeException, DOMException {
        Node namedItem = node.getAttributes().getNamedItem(str);
        if (namedItem == null) {
            if (!z3) {
                return z2;
            }
            fatal(node, "Required attribute " + str + " not present!");
        }
        String nodeValue = namedItem.getNodeValue();
        if (nodeValue.equals("TRUE") || nodeValue.equals("true")) {
            return true;
        }
        if (nodeValue.equals("FALSE") || nodeValue.equals("false")) {
            return false;
        }
        fatal(node, "Attribute " + str + " must be 'TRUE' or 'FALSE'!");
        return false;
    }

    private boolean getBooleanAttribute(Node node, String str) throws IIOInvalidTreeException {
        return getBooleanAttribute(node, str, false, true);
    }

    private int getEnumeratedAttribute(Node node, String str, String[] strArr, int i2, boolean z2) throws IIOInvalidTreeException, DOMException {
        Node namedItem = node.getAttributes().getNamedItem(str);
        if (namedItem == null) {
            if (!z2) {
                return i2;
            }
            fatal(node, "Required attribute " + str + " not present!");
        }
        String nodeValue = namedItem.getNodeValue();
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (nodeValue.equals(strArr[i3])) {
                return i3;
            }
        }
        fatal(node, "Illegal value for attribute " + str + "!");
        return -1;
    }

    private int getEnumeratedAttribute(Node node, String str, String[] strArr) throws IIOInvalidTreeException {
        return getEnumeratedAttribute(node, str, strArr, -1, true);
    }

    private String getAttribute(Node node, String str, String str2, boolean z2) throws IIOInvalidTreeException {
        Node namedItem = node.getAttributes().getNamedItem(str);
        if (namedItem == null) {
            if (!z2) {
                return str2;
            }
            fatal(node, "Required attribute " + str + " not present!");
        }
        return namedItem.getNodeValue();
    }

    private String getAttribute(Node node, String str) throws IIOInvalidTreeException {
        return getAttribute(node, str, null, true);
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void mergeTree(String str, Node node) throws IIOInvalidTreeException, NumberFormatException {
        if (str.equals(nativeMetadataFormatName)) {
            if (node == null) {
                throw new IllegalArgumentException("root == null!");
            }
            mergeNativeTree(node);
        } else {
            if (str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
                if (node == null) {
                    throw new IllegalArgumentException("root == null!");
                }
                mergeStandardTree(node);
                return;
            }
            throw new IllegalArgumentException("Not a recognized format!");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void mergeNativeTree(Node node) throws IIOInvalidTreeException {
        if (!node.getNodeName().equals(nativeMetadataFormatName)) {
            fatal(node, "Root must be javax_imageio_png_1.0");
        }
        Node firstChild = node.getFirstChild();
        while (true) {
            Node node2 = firstChild;
            if (node2 != null) {
                String nodeName = node2.getNodeName();
                if (nodeName.equals("IHDR")) {
                    this.IHDR_width = getIntAttribute(node2, MetadataParser.WIDTH_TAG_NAME);
                    this.IHDR_height = getIntAttribute(node2, MetadataParser.HEIGHT_TAG_NAME);
                    this.IHDR_bitDepth = Integer.valueOf(IHDR_bitDepths[getEnumeratedAttribute(node2, "bitDepth", IHDR_bitDepths)]).intValue();
                    this.IHDR_colorType = getEnumeratedAttribute(node2, "colorType", IHDR_colorTypeNames);
                    this.IHDR_compressionMethod = getEnumeratedAttribute(node2, "compressionMethod", IHDR_compressionMethodNames);
                    this.IHDR_filterMethod = getEnumeratedAttribute(node2, "filterMethod", IHDR_filterMethodNames);
                    this.IHDR_interlaceMethod = getEnumeratedAttribute(node2, "interlaceMethod", IHDR_interlaceMethodNames);
                    this.IHDR_present = true;
                } else if (nodeName.equals("PLTE")) {
                    byte[] bArr = new byte[256];
                    byte[] bArr2 = new byte[256];
                    byte[] bArr3 = new byte[256];
                    int i2 = -1;
                    Node firstChild2 = node2.getFirstChild();
                    if (firstChild2 == null) {
                        fatal(node2, "Palette has no entries!");
                    }
                    while (firstChild2 != null) {
                        if (!firstChild2.getNodeName().equals("PLTEEntry")) {
                            fatal(node2, "Only a PLTEEntry may be a child of a PLTE!");
                        }
                        int intAttribute = getIntAttribute(firstChild2, "index");
                        if (intAttribute < 0 || intAttribute > 255) {
                            fatal(node2, "Bad value for PLTEEntry attribute index!");
                        }
                        if (intAttribute > i2) {
                            i2 = intAttribute;
                        }
                        bArr[intAttribute] = (byte) getIntAttribute(firstChild2, "red");
                        bArr2[intAttribute] = (byte) getIntAttribute(firstChild2, "green");
                        bArr3[intAttribute] = (byte) getIntAttribute(firstChild2, "blue");
                        firstChild2 = firstChild2.getNextSibling();
                    }
                    int i3 = i2 + 1;
                    this.PLTE_red = new byte[i3];
                    this.PLTE_green = new byte[i3];
                    this.PLTE_blue = new byte[i3];
                    System.arraycopy(bArr, 0, this.PLTE_red, 0, i3);
                    System.arraycopy(bArr2, 0, this.PLTE_green, 0, i3);
                    System.arraycopy(bArr3, 0, this.PLTE_blue, 0, i3);
                    this.PLTE_present = true;
                } else if (nodeName.equals("bKGD")) {
                    this.bKGD_present = false;
                    Node firstChild3 = node2.getFirstChild();
                    if (firstChild3 == null) {
                        fatal(node2, "bKGD node has no children!");
                    }
                    String nodeName2 = firstChild3.getNodeName();
                    if (nodeName2.equals("bKGD_Palette")) {
                        this.bKGD_index = getIntAttribute(firstChild3, "index");
                        this.bKGD_colorType = 3;
                    } else if (nodeName2.equals("bKGD_Grayscale")) {
                        this.bKGD_gray = getIntAttribute(firstChild3, "gray");
                        this.bKGD_colorType = 0;
                    } else if (nodeName2.equals("bKGD_RGB")) {
                        this.bKGD_red = getIntAttribute(firstChild3, "red");
                        this.bKGD_green = getIntAttribute(firstChild3, "green");
                        this.bKGD_blue = getIntAttribute(firstChild3, "blue");
                        this.bKGD_colorType = 2;
                    } else {
                        fatal(node2, "Bad child of a bKGD node!");
                    }
                    if (firstChild3.getNextSibling() != null) {
                        fatal(node2, "bKGD node has more than one child!");
                    }
                    this.bKGD_present = true;
                } else if (nodeName.equals("cHRM")) {
                    this.cHRM_whitePointX = getIntAttribute(node2, "whitePointX");
                    this.cHRM_whitePointY = getIntAttribute(node2, "whitePointY");
                    this.cHRM_redX = getIntAttribute(node2, "redX");
                    this.cHRM_redY = getIntAttribute(node2, "redY");
                    this.cHRM_greenX = getIntAttribute(node2, "greenX");
                    this.cHRM_greenY = getIntAttribute(node2, "greenY");
                    this.cHRM_blueX = getIntAttribute(node2, "blueX");
                    this.cHRM_blueY = getIntAttribute(node2, "blueY");
                    this.cHRM_present = true;
                } else if (nodeName.equals("gAMA")) {
                    this.gAMA_gamma = getIntAttribute(node2, "value");
                    this.gAMA_present = true;
                } else if (nodeName.equals("hIST")) {
                    char[] cArr = new char[256];
                    int i4 = -1;
                    Node firstChild4 = node2.getFirstChild();
                    if (firstChild4 == null) {
                        fatal(node2, "hIST node has no children!");
                    }
                    while (firstChild4 != null) {
                        if (!firstChild4.getNodeName().equals("hISTEntry")) {
                            fatal(node2, "Only a hISTEntry may be a child of a hIST!");
                        }
                        int intAttribute2 = getIntAttribute(firstChild4, "index");
                        if (intAttribute2 < 0 || intAttribute2 > 255) {
                            fatal(node2, "Bad value for histEntry attribute index!");
                        }
                        if (intAttribute2 > i4) {
                            i4 = intAttribute2;
                        }
                        cArr[intAttribute2] = (char) getIntAttribute(firstChild4, "value");
                        firstChild4 = firstChild4.getNextSibling();
                    }
                    int i5 = i4 + 1;
                    this.hIST_histogram = new char[i5];
                    System.arraycopy(cArr, 0, this.hIST_histogram, 0, i5);
                    this.hIST_present = true;
                } else if (nodeName.equals("iCCP")) {
                    this.iCCP_profileName = getAttribute(node2, "profileName");
                    this.iCCP_compressionMethod = getEnumeratedAttribute(node2, "compressionMethod", iCCP_compressionMethodNames);
                    Object userObject = ((IIOMetadataNode) node2).getUserObject();
                    if (userObject == null) {
                        fatal(node2, "No ICCP profile present in user object!");
                    }
                    if (!(userObject instanceof byte[])) {
                        fatal(node2, "User object not a byte array!");
                    }
                    this.iCCP_compressedProfile = (byte[]) ((byte[]) userObject).clone();
                    this.iCCP_present = true;
                } else if (nodeName.equals("iTXt")) {
                    Node firstChild5 = node2.getFirstChild();
                    while (true) {
                        Node node3 = firstChild5;
                        if (node3 != null) {
                            if (!node3.getNodeName().equals("iTXtEntry")) {
                                fatal(node2, "Only an iTXtEntry may be a child of an iTXt!");
                            }
                            String attribute = getAttribute(node3, "keyword");
                            if (isValidKeyword(attribute)) {
                                this.iTXt_keyword.add(attribute);
                                this.iTXt_compressionFlag.add(Boolean.valueOf(getBooleanAttribute(node3, "compressionFlag")));
                                this.iTXt_compressionMethod.add(Integer.valueOf(getAttribute(node3, "compressionMethod")));
                                this.iTXt_languageTag.add(getAttribute(node3, "languageTag"));
                                this.iTXt_translatedKeyword.add(getAttribute(node3, "translatedKeyword"));
                                this.iTXt_text.add(getAttribute(node3, "text"));
                            }
                            firstChild5 = node3.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("pHYs")) {
                    this.pHYs_pixelsPerUnitXAxis = getIntAttribute(node2, "pixelsPerUnitXAxis");
                    this.pHYs_pixelsPerUnitYAxis = getIntAttribute(node2, "pixelsPerUnitYAxis");
                    this.pHYs_unitSpecifier = getEnumeratedAttribute(node2, "unitSpecifier", unitSpecifierNames);
                    this.pHYs_present = true;
                } else if (nodeName.equals("sBIT")) {
                    this.sBIT_present = false;
                    Node firstChild6 = node2.getFirstChild();
                    if (firstChild6 == null) {
                        fatal(node2, "sBIT node has no children!");
                    }
                    String nodeName3 = firstChild6.getNodeName();
                    if (nodeName3.equals("sBIT_Grayscale")) {
                        this.sBIT_grayBits = getIntAttribute(firstChild6, "gray");
                        this.sBIT_colorType = 0;
                    } else if (nodeName3.equals("sBIT_GrayAlpha")) {
                        this.sBIT_grayBits = getIntAttribute(firstChild6, "gray");
                        this.sBIT_alphaBits = getIntAttribute(firstChild6, "alpha");
                        this.sBIT_colorType = 4;
                    } else if (nodeName3.equals("sBIT_RGB")) {
                        this.sBIT_redBits = getIntAttribute(firstChild6, "red");
                        this.sBIT_greenBits = getIntAttribute(firstChild6, "green");
                        this.sBIT_blueBits = getIntAttribute(firstChild6, "blue");
                        this.sBIT_colorType = 2;
                    } else if (nodeName3.equals("sBIT_RGBAlpha")) {
                        this.sBIT_redBits = getIntAttribute(firstChild6, "red");
                        this.sBIT_greenBits = getIntAttribute(firstChild6, "green");
                        this.sBIT_blueBits = getIntAttribute(firstChild6, "blue");
                        this.sBIT_alphaBits = getIntAttribute(firstChild6, "alpha");
                        this.sBIT_colorType = 6;
                    } else if (nodeName3.equals("sBIT_Palette")) {
                        this.sBIT_redBits = getIntAttribute(firstChild6, "red");
                        this.sBIT_greenBits = getIntAttribute(firstChild6, "green");
                        this.sBIT_blueBits = getIntAttribute(firstChild6, "blue");
                        this.sBIT_colorType = 3;
                    } else {
                        fatal(node2, "Bad child of an sBIT node!");
                    }
                    if (firstChild6.getNextSibling() != null) {
                        fatal(node2, "sBIT node has more than one child!");
                    }
                    this.sBIT_present = true;
                } else if (nodeName.equals("sPLT")) {
                    this.sPLT_paletteName = getAttribute(node2, "name");
                    this.sPLT_sampleDepth = getIntAttribute(node2, "sampleDepth");
                    int[] iArr = new int[256];
                    int[] iArr2 = new int[256];
                    int[] iArr3 = new int[256];
                    int[] iArr4 = new int[256];
                    int[] iArr5 = new int[256];
                    int i6 = -1;
                    Node firstChild7 = node2.getFirstChild();
                    if (firstChild7 == null) {
                        fatal(node2, "sPLT node has no children!");
                    }
                    while (firstChild7 != null) {
                        if (!firstChild7.getNodeName().equals("sPLTEntry")) {
                            fatal(node2, "Only an sPLTEntry may be a child of an sPLT!");
                        }
                        int intAttribute3 = getIntAttribute(firstChild7, "index");
                        if (intAttribute3 < 0 || intAttribute3 > 255) {
                            fatal(node2, "Bad value for PLTEEntry attribute index!");
                        }
                        if (intAttribute3 > i6) {
                            i6 = intAttribute3;
                        }
                        iArr[intAttribute3] = getIntAttribute(firstChild7, "red");
                        iArr2[intAttribute3] = getIntAttribute(firstChild7, "green");
                        iArr3[intAttribute3] = getIntAttribute(firstChild7, "blue");
                        iArr4[intAttribute3] = getIntAttribute(firstChild7, "alpha");
                        iArr5[intAttribute3] = getIntAttribute(firstChild7, "frequency");
                        firstChild7 = firstChild7.getNextSibling();
                    }
                    int i7 = i6 + 1;
                    this.sPLT_red = new int[i7];
                    this.sPLT_green = new int[i7];
                    this.sPLT_blue = new int[i7];
                    this.sPLT_alpha = new int[i7];
                    this.sPLT_frequency = new int[i7];
                    System.arraycopy(iArr, 0, this.sPLT_red, 0, i7);
                    System.arraycopy(iArr2, 0, this.sPLT_green, 0, i7);
                    System.arraycopy(iArr3, 0, this.sPLT_blue, 0, i7);
                    System.arraycopy(iArr4, 0, this.sPLT_alpha, 0, i7);
                    System.arraycopy(iArr5, 0, this.sPLT_frequency, 0, i7);
                    this.sPLT_present = true;
                } else if (nodeName.equals("sRGB")) {
                    this.sRGB_renderingIntent = getEnumeratedAttribute(node2, "renderingIntent", renderingIntentNames);
                    this.sRGB_present = true;
                } else if (nodeName.equals("tEXt")) {
                    Node firstChild8 = node2.getFirstChild();
                    while (true) {
                        Node node4 = firstChild8;
                        if (node4 != null) {
                            if (!node4.getNodeName().equals("tEXtEntry")) {
                                fatal(node2, "Only an tEXtEntry may be a child of an tEXt!");
                            }
                            this.tEXt_keyword.add(getAttribute(node4, "keyword"));
                            this.tEXt_text.add(getAttribute(node4, "value"));
                            firstChild8 = node4.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("tIME")) {
                    this.tIME_year = getIntAttribute(node2, MetadataParser.YEAR_TAG_NAME);
                    this.tIME_month = getIntAttribute(node2, "month");
                    this.tIME_day = getIntAttribute(node2, "day");
                    this.tIME_hour = getIntAttribute(node2, "hour");
                    this.tIME_minute = getIntAttribute(node2, "minute");
                    this.tIME_second = getIntAttribute(node2, StatisticImpl.UNIT_SECOND);
                    this.tIME_present = true;
                } else if (nodeName.equals("tRNS")) {
                    this.tRNS_present = false;
                    Node firstChild9 = node2.getFirstChild();
                    if (firstChild9 == null) {
                        fatal(node2, "tRNS node has no children!");
                    }
                    String nodeName4 = firstChild9.getNodeName();
                    if (nodeName4.equals("tRNS_Palette")) {
                        byte[] bArr4 = new byte[256];
                        int i8 = -1;
                        Node firstChild10 = firstChild9.getFirstChild();
                        if (firstChild10 == null) {
                            fatal(node2, "tRNS_Palette node has no children!");
                        }
                        while (firstChild10 != null) {
                            if (!firstChild10.getNodeName().equals("tRNS_PaletteEntry")) {
                                fatal(node2, "Only a tRNS_PaletteEntry may be a child of a tRNS_Palette!");
                            }
                            int intAttribute4 = getIntAttribute(firstChild10, "index");
                            if (intAttribute4 < 0 || intAttribute4 > 255) {
                                fatal(node2, "Bad value for tRNS_PaletteEntry attribute index!");
                            }
                            if (intAttribute4 > i8) {
                                i8 = intAttribute4;
                            }
                            bArr4[intAttribute4] = (byte) getIntAttribute(firstChild10, "alpha");
                            firstChild10 = firstChild10.getNextSibling();
                        }
                        int i9 = i8 + 1;
                        this.tRNS_alpha = new byte[i9];
                        this.tRNS_colorType = 3;
                        System.arraycopy(bArr4, 0, this.tRNS_alpha, 0, i9);
                    } else if (nodeName4.equals("tRNS_Grayscale")) {
                        this.tRNS_gray = getIntAttribute(firstChild9, "gray");
                        this.tRNS_colorType = 0;
                    } else if (nodeName4.equals("tRNS_RGB")) {
                        this.tRNS_red = getIntAttribute(firstChild9, "red");
                        this.tRNS_green = getIntAttribute(firstChild9, "green");
                        this.tRNS_blue = getIntAttribute(firstChild9, "blue");
                        this.tRNS_colorType = 2;
                    } else {
                        fatal(node2, "Bad child of a tRNS node!");
                    }
                    if (firstChild9.getNextSibling() != null) {
                        fatal(node2, "tRNS node has more than one child!");
                    }
                    this.tRNS_present = true;
                } else if (nodeName.equals("zTXt")) {
                    Node firstChild11 = node2.getFirstChild();
                    while (true) {
                        Node node5 = firstChild11;
                        if (node5 != null) {
                            if (!node5.getNodeName().equals("zTXtEntry")) {
                                fatal(node2, "Only an zTXtEntry may be a child of an zTXt!");
                            }
                            this.zTXt_keyword.add(getAttribute(node5, "keyword"));
                            this.zTXt_compressionMethod.add(new Integer(getEnumeratedAttribute(node5, "compressionMethod", zTXt_compressionMethodNames)));
                            this.zTXt_text.add(getAttribute(node5, "text"));
                            firstChild11 = node5.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("UnknownChunks")) {
                    Node firstChild12 = node2.getFirstChild();
                    while (true) {
                        Node node6 = firstChild12;
                        if (node6 != null) {
                            if (!node6.getNodeName().equals("UnknownChunk")) {
                                fatal(node2, "Only an UnknownChunk may be a child of an UnknownChunks!");
                            }
                            String attribute2 = getAttribute(node6, "type");
                            Object userObject2 = ((IIOMetadataNode) node6).getUserObject();
                            if (attribute2.length() != 4) {
                                fatal(node6, "Chunk type must be 4 characters!");
                            }
                            if (userObject2 == null) {
                                fatal(node6, "No chunk data present in user object!");
                            }
                            if (!(userObject2 instanceof byte[])) {
                                fatal(node6, "User object not a byte array!");
                            }
                            this.unknownChunkType.add(attribute2);
                            this.unknownChunkData.add(((byte[]) userObject2).clone());
                            firstChild12 = node6.getNextSibling();
                        }
                    }
                } else {
                    fatal(node2, "Unknown child of root node!");
                }
                firstChild = node2.getNextSibling();
            } else {
                return;
            }
        }
    }

    private boolean isValidKeyword(String str) {
        int length = str.length();
        if (length < 1 || length >= 80 || str.startsWith(" ") || str.endsWith(" ") || str.contains(Constants.INDENT)) {
            return false;
        }
        return isISOLatin(str, false);
    }

    private boolean isISOLatin(String str, boolean z2) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if ((cCharAt < ' ' || cCharAt > 255 || (cCharAt > '~' && cCharAt < 161)) && (!z2 || cCharAt != 16)) {
                return false;
            }
        }
        return true;
    }

    private void mergeStandardTree(Node node) throws IIOInvalidTreeException, NumberFormatException {
        if (!node.getNodeName().equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
            fatal(node, "Root must be javax_imageio_1.0");
        }
        Node firstChild = node.getFirstChild();
        while (true) {
            Node node2 = firstChild;
            if (node2 != null) {
                String nodeName = node2.getNodeName();
                if (nodeName.equals("Chroma")) {
                    Node firstChild2 = node2.getFirstChild();
                    while (true) {
                        Node node3 = firstChild2;
                        if (node3 != null) {
                            String nodeName2 = node3.getNodeName();
                            if (nodeName2.equals("Gamma")) {
                                float floatAttribute = getFloatAttribute(node3, "value");
                                this.gAMA_present = true;
                                this.gAMA_gamma = (int) ((floatAttribute * 100000.0f) + 0.5d);
                            } else if (nodeName2.equals("Palette")) {
                                byte[] bArr = new byte[256];
                                byte[] bArr2 = new byte[256];
                                byte[] bArr3 = new byte[256];
                                int i2 = -1;
                                Node firstChild3 = node3.getFirstChild();
                                while (true) {
                                    Node node4 = firstChild3;
                                    if (node4 == null) {
                                        break;
                                    }
                                    int intAttribute = getIntAttribute(node4, "index");
                                    if (intAttribute >= 0 && intAttribute <= 255) {
                                        bArr[intAttribute] = (byte) getIntAttribute(node4, "red");
                                        bArr2[intAttribute] = (byte) getIntAttribute(node4, "green");
                                        bArr3[intAttribute] = (byte) getIntAttribute(node4, "blue");
                                        if (intAttribute > i2) {
                                            i2 = intAttribute;
                                        }
                                    }
                                    firstChild3 = node4.getNextSibling();
                                }
                                int i3 = i2 + 1;
                                this.PLTE_red = new byte[i3];
                                this.PLTE_green = new byte[i3];
                                this.PLTE_blue = new byte[i3];
                                System.arraycopy(bArr, 0, this.PLTE_red, 0, i3);
                                System.arraycopy(bArr2, 0, this.PLTE_green, 0, i3);
                                System.arraycopy(bArr3, 0, this.PLTE_blue, 0, i3);
                                this.PLTE_present = true;
                            } else if (nodeName2.equals("BackgroundIndex")) {
                                this.bKGD_present = true;
                                this.bKGD_colorType = 3;
                                this.bKGD_index = getIntAttribute(node3, "value");
                            } else if (nodeName2.equals("BackgroundColor")) {
                                int intAttribute2 = getIntAttribute(node3, "red");
                                int intAttribute3 = getIntAttribute(node3, "green");
                                int intAttribute4 = getIntAttribute(node3, "blue");
                                if (intAttribute2 == intAttribute3 && intAttribute2 == intAttribute4) {
                                    this.bKGD_colorType = 0;
                                    this.bKGD_gray = intAttribute2;
                                } else {
                                    this.bKGD_red = intAttribute2;
                                    this.bKGD_green = intAttribute3;
                                    this.bKGD_blue = intAttribute4;
                                }
                                this.bKGD_present = true;
                            }
                            firstChild2 = node3.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Compression")) {
                    Node firstChild4 = node2.getFirstChild();
                    while (true) {
                        Node node5 = firstChild4;
                        if (node5 != null) {
                            if (node5.getNodeName().equals("NumProgressiveScans")) {
                                this.IHDR_interlaceMethod = getIntAttribute(node5, "value") > 1 ? 1 : 0;
                            }
                            firstChild4 = node5.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Data")) {
                    Node firstChild5 = node2.getFirstChild();
                    while (true) {
                        Node node6 = firstChild5;
                        if (node6 != null) {
                            String nodeName3 = node6.getNodeName();
                            if (nodeName3.equals("BitsPerSample")) {
                                StringTokenizer stringTokenizer = new StringTokenizer(getAttribute(node6, "value"));
                                int i4 = -1;
                                while (stringTokenizer.hasMoreTokens()) {
                                    int i5 = Integer.parseInt(stringTokenizer.nextToken());
                                    if (i5 > i4) {
                                        i4 = i5;
                                    }
                                }
                                if (i4 < 1) {
                                    i4 = 1;
                                }
                                if (i4 == 3) {
                                    i4 = 4;
                                }
                                if (i4 > 4 || i4 < 8) {
                                    i4 = 8;
                                }
                                if (i4 > 8) {
                                    i4 = 16;
                                }
                                this.IHDR_bitDepth = i4;
                            } else if (nodeName3.equals("SignificantBitsPerSample")) {
                                StringTokenizer stringTokenizer2 = new StringTokenizer(getAttribute(node6, "value"));
                                int iCountTokens = stringTokenizer2.countTokens();
                                if (iCountTokens == 1) {
                                    this.sBIT_colorType = 0;
                                    this.sBIT_grayBits = Integer.parseInt(stringTokenizer2.nextToken());
                                } else if (iCountTokens == 2) {
                                    this.sBIT_colorType = 4;
                                    this.sBIT_grayBits = Integer.parseInt(stringTokenizer2.nextToken());
                                    this.sBIT_alphaBits = Integer.parseInt(stringTokenizer2.nextToken());
                                } else if (iCountTokens == 3) {
                                    this.sBIT_colorType = 2;
                                    this.sBIT_redBits = Integer.parseInt(stringTokenizer2.nextToken());
                                    this.sBIT_greenBits = Integer.parseInt(stringTokenizer2.nextToken());
                                    this.sBIT_blueBits = Integer.parseInt(stringTokenizer2.nextToken());
                                } else if (iCountTokens == 4) {
                                    this.sBIT_colorType = 6;
                                    this.sBIT_redBits = Integer.parseInt(stringTokenizer2.nextToken());
                                    this.sBIT_greenBits = Integer.parseInt(stringTokenizer2.nextToken());
                                    this.sBIT_blueBits = Integer.parseInt(stringTokenizer2.nextToken());
                                    this.sBIT_alphaBits = Integer.parseInt(stringTokenizer2.nextToken());
                                }
                                if (iCountTokens >= 1 && iCountTokens <= 4) {
                                    this.sBIT_present = true;
                                }
                            }
                            firstChild5 = node6.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Dimension")) {
                    boolean z2 = false;
                    boolean z3 = false;
                    boolean z4 = false;
                    float floatAttribute2 = -1.0f;
                    float floatAttribute3 = -1.0f;
                    float floatAttribute4 = -1.0f;
                    Node firstChild6 = node2.getFirstChild();
                    while (true) {
                        Node node7 = firstChild6;
                        if (node7 == null) {
                            break;
                        }
                        String nodeName4 = node7.getNodeName();
                        if (nodeName4.equals("PixelAspectRatio")) {
                            floatAttribute4 = getFloatAttribute(node7, "value");
                            z4 = true;
                        } else if (nodeName4.equals("HorizontalPixelSize")) {
                            floatAttribute2 = getFloatAttribute(node7, "value");
                            z2 = true;
                        } else if (nodeName4.equals("VerticalPixelSize")) {
                            floatAttribute3 = getFloatAttribute(node7, "value");
                            z3 = true;
                        }
                        firstChild6 = node7.getNextSibling();
                    }
                    if (z2 && z3) {
                        this.pHYs_present = true;
                        this.pHYs_unitSpecifier = 1;
                        this.pHYs_pixelsPerUnitXAxis = (int) ((floatAttribute2 * 1000.0f) + 0.5f);
                        this.pHYs_pixelsPerUnitYAxis = (int) ((floatAttribute3 * 1000.0f) + 0.5f);
                    } else if (z4) {
                        this.pHYs_present = true;
                        this.pHYs_unitSpecifier = 0;
                        int i6 = 1;
                        while (i6 < 100 && Math.abs((((int) (floatAttribute4 * i6)) / i6) - floatAttribute4) >= 0.001d) {
                            i6++;
                        }
                        this.pHYs_pixelsPerUnitXAxis = (int) (floatAttribute4 * i6);
                        this.pHYs_pixelsPerUnitYAxis = i6;
                    }
                } else if (nodeName.equals("Document")) {
                    Node firstChild7 = node2.getFirstChild();
                    while (true) {
                        Node node8 = firstChild7;
                        if (node8 != null) {
                            if (node8.getNodeName().equals("ImageModificationTime")) {
                                this.tIME_present = true;
                                this.tIME_year = getIntAttribute(node8, MetadataParser.YEAR_TAG_NAME);
                                this.tIME_month = getIntAttribute(node8, "month");
                                this.tIME_day = getIntAttribute(node8, "day");
                                this.tIME_hour = getIntAttribute(node8, "hour", 0, false);
                                this.tIME_minute = getIntAttribute(node8, "minute", 0, false);
                                this.tIME_second = getIntAttribute(node8, StatisticImpl.UNIT_SECOND, 0, false);
                            }
                            firstChild7 = node8.getNextSibling();
                        }
                    }
                } else if (nodeName.equals("Text")) {
                    Node firstChild8 = node2.getFirstChild();
                    while (true) {
                        Node node9 = firstChild8;
                        if (node9 != null) {
                            if (node9.getNodeName().equals("TextEntry")) {
                                String attribute = getAttribute(node9, "keyword", "", false);
                                String attribute2 = getAttribute(node9, "value");
                                String attribute3 = getAttribute(node9, "language", "", false);
                                String attribute4 = getAttribute(node9, "compression", Separation.COLORANT_NONE, false);
                                if (isValidKeyword(attribute)) {
                                    if (isISOLatin(attribute2, true)) {
                                        if (attribute4.equals("zip")) {
                                            this.zTXt_keyword.add(attribute);
                                            this.zTXt_text.add(attribute2);
                                            this.zTXt_compressionMethod.add(0);
                                        } else {
                                            this.tEXt_keyword.add(attribute);
                                            this.tEXt_text.add(attribute2);
                                        }
                                    } else {
                                        this.iTXt_keyword.add(attribute);
                                        this.iTXt_compressionFlag.add(Boolean.valueOf(attribute4.equals("zip")));
                                        this.iTXt_compressionMethod.add(0);
                                        this.iTXt_languageTag.add(attribute3);
                                        this.iTXt_translatedKeyword.add(attribute);
                                        this.iTXt_text.add(attribute2);
                                    }
                                }
                            }
                            firstChild8 = node9.getNextSibling();
                        }
                    }
                }
                firstChild = node2.getNextSibling();
            } else {
                return;
            }
        }
    }

    @Override // javax.imageio.metadata.IIOMetadata
    public void reset() {
        this.IHDR_present = false;
        this.PLTE_present = false;
        this.bKGD_present = false;
        this.cHRM_present = false;
        this.gAMA_present = false;
        this.hIST_present = false;
        this.iCCP_present = false;
        this.iTXt_keyword = new ArrayList<>();
        this.iTXt_compressionFlag = new ArrayList<>();
        this.iTXt_compressionMethod = new ArrayList<>();
        this.iTXt_languageTag = new ArrayList<>();
        this.iTXt_translatedKeyword = new ArrayList<>();
        this.iTXt_text = new ArrayList<>();
        this.pHYs_present = false;
        this.sBIT_present = false;
        this.sPLT_present = false;
        this.sRGB_present = false;
        this.tEXt_keyword = new ArrayList<>();
        this.tEXt_text = new ArrayList<>();
        this.tIME_present = false;
        this.tRNS_present = false;
        this.zTXt_keyword = new ArrayList<>();
        this.zTXt_compressionMethod = new ArrayList<>();
        this.zTXt_text = new ArrayList<>();
        this.unknownChunkType = new ArrayList<>();
        this.unknownChunkData = new ArrayList<>();
    }
}
