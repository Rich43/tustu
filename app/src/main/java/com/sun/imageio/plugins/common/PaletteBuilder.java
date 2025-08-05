package com.sun.imageio.plugins.common;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import javax.imageio.ImageTypeSpecifier;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/PaletteBuilder.class */
public class PaletteBuilder {
    protected static final int MAXLEVEL = 8;
    protected RenderedImage src;
    protected ColorModel srcColorModel;
    protected Raster srcRaster;
    protected int requiredSize;
    protected ColorNode root;
    protected int numNodes;
    protected int maxNodes;
    protected int currLevel;
    protected int currSize;
    protected ColorNode[] reduceList;
    protected ColorNode[] palette;
    protected int transparency;
    protected ColorNode transColor;

    public static RenderedImage createIndexedImage(RenderedImage renderedImage) {
        PaletteBuilder paletteBuilder = new PaletteBuilder(renderedImage);
        paletteBuilder.buildPalette();
        return paletteBuilder.getIndexedImage();
    }

    public static IndexColorModel createIndexColorModel(RenderedImage renderedImage) {
        PaletteBuilder paletteBuilder = new PaletteBuilder(renderedImage);
        paletteBuilder.buildPalette();
        return paletteBuilder.getIndexColorModel();
    }

    public static boolean canCreatePalette(ImageTypeSpecifier imageTypeSpecifier) {
        if (imageTypeSpecifier == null) {
            throw new IllegalArgumentException("type == null");
        }
        return true;
    }

    public static boolean canCreatePalette(RenderedImage renderedImage) {
        if (renderedImage == null) {
            throw new IllegalArgumentException("image == null");
        }
        return canCreatePalette(new ImageTypeSpecifier(renderedImage));
    }

    protected RenderedImage getIndexedImage() {
        BufferedImage bufferedImage = new BufferedImage(this.src.getWidth(), this.src.getHeight(), 13, getIndexColorModel());
        WritableRaster raster = bufferedImage.getRaster();
        for (int i2 = 0; i2 < bufferedImage.getHeight(); i2++) {
            for (int i3 = 0; i3 < bufferedImage.getWidth(); i3++) {
                raster.setSample(i3, i2, 0, findColorIndex(this.root, getSrcColor(i3, i2)));
            }
        }
        return bufferedImage;
    }

    protected PaletteBuilder(RenderedImage renderedImage) {
        this(renderedImage, 256);
    }

    protected PaletteBuilder(RenderedImage renderedImage, int i2) {
        this.src = renderedImage;
        this.srcColorModel = renderedImage.getColorModel();
        this.srcRaster = renderedImage.getData();
        this.transparency = this.srcColorModel.getTransparency();
        this.requiredSize = i2;
    }

    private Color getSrcColor(int i2, int i3) {
        return new Color(this.srcColorModel.getRGB(this.srcRaster.getDataElements(i2, i3, null)), this.transparency != 1);
    }

    protected int findColorIndex(ColorNode colorNode, Color color) {
        if (this.transparency != 1 && color.getAlpha() != 255) {
            return 0;
        }
        if (colorNode.isLeaf) {
            return colorNode.paletteIndex;
        }
        return findColorIndex(colorNode.children[getBranchIndex(color, colorNode.level)], color);
    }

    protected void buildPalette() {
        this.reduceList = new ColorNode[9];
        for (int i2 = 0; i2 < this.reduceList.length; i2++) {
            this.reduceList[i2] = null;
        }
        this.numNodes = 0;
        this.maxNodes = 0;
        this.root = null;
        this.currSize = 0;
        this.currLevel = 8;
        int width = this.src.getWidth();
        int height = this.src.getHeight();
        for (int i3 = 0; i3 < height; i3++) {
            for (int i4 = 0; i4 < width; i4++) {
                Color srcColor = getSrcColor((width - i4) - 1, (height - i3) - 1);
                if (this.transparency != 1 && srcColor.getAlpha() != 255) {
                    if (this.transColor == null) {
                        this.requiredSize--;
                        this.transColor = new ColorNode();
                        this.transColor.isLeaf = true;
                    }
                    this.transColor = insertNode(this.transColor, srcColor, 0);
                } else {
                    this.root = insertNode(this.root, srcColor, 0);
                }
                if (this.currSize > this.requiredSize) {
                    reduceTree();
                }
            }
        }
    }

    protected ColorNode insertNode(ColorNode colorNode, Color color, int i2) {
        if (colorNode == null) {
            colorNode = new ColorNode();
            this.numNodes++;
            if (this.numNodes > this.maxNodes) {
                this.maxNodes = this.numNodes;
            }
            colorNode.level = i2;
            colorNode.isLeaf = i2 > 8;
            if (colorNode.isLeaf) {
                this.currSize++;
            }
        }
        colorNode.colorCount++;
        colorNode.red += color.getRed();
        colorNode.green += color.getGreen();
        colorNode.blue += color.getBlue();
        if (!colorNode.isLeaf) {
            int branchIndex = getBranchIndex(color, i2);
            if (colorNode.children[branchIndex] == null) {
                colorNode.childCount++;
                if (colorNode.childCount == 2) {
                    colorNode.nextReducible = this.reduceList[i2];
                    this.reduceList[i2] = colorNode;
                }
            }
            colorNode.children[branchIndex] = insertNode(colorNode.children[branchIndex], color, i2 + 1);
        }
        return colorNode;
    }

    protected IndexColorModel getIndexColorModel() {
        IndexColorModel indexColorModel;
        int i2 = this.currSize;
        if (this.transColor != null) {
            i2++;
        }
        byte[] bArr = new byte[i2];
        byte[] bArr2 = new byte[i2];
        byte[] bArr3 = new byte[i2];
        int i3 = 0;
        this.palette = new ColorNode[i2];
        if (this.transColor != null) {
            i3 = 0 + 1;
        }
        if (this.root != null) {
            findPaletteEntry(this.root, i3, bArr, bArr2, bArr3);
        }
        if (this.transColor != null) {
            indexColorModel = new IndexColorModel(8, i2, bArr, bArr2, bArr3, 0);
        } else {
            indexColorModel = new IndexColorModel(8, this.currSize, bArr, bArr2, bArr3);
        }
        return indexColorModel;
    }

    protected int findPaletteEntry(ColorNode colorNode, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (colorNode.isLeaf) {
            bArr[i2] = (byte) (colorNode.red / colorNode.colorCount);
            bArr2[i2] = (byte) (colorNode.green / colorNode.colorCount);
            bArr3[i2] = (byte) (colorNode.blue / colorNode.colorCount);
            colorNode.paletteIndex = i2;
            this.palette[i2] = colorNode;
            i2++;
        } else {
            for (int i3 = 0; i3 < 8; i3++) {
                if (colorNode.children[i3] != null) {
                    i2 = findPaletteEntry(colorNode.children[i3], i2, bArr, bArr2, bArr3);
                }
            }
        }
        return i2;
    }

    protected int getBranchIndex(Color color, int i2) {
        if (i2 > 8 || i2 < 0) {
            throw new IllegalArgumentException("Invalid octree node depth: " + i2);
        }
        int i3 = 8 - i2;
        return ((1 & ((255 & color.getRed()) >> i3)) << 2) | ((1 & ((255 & color.getGreen()) >> i3)) << 1) | (1 & ((255 & color.getBlue()) >> i3));
    }

    protected void reduceTree() {
        int length = this.reduceList.length - 1;
        while (this.reduceList[length] == null && length >= 0) {
            length--;
        }
        ColorNode colorNode = this.reduceList[length];
        if (colorNode == null) {
            return;
        }
        ColorNode colorNode2 = colorNode;
        int i2 = colorNode2.colorCount;
        int i3 = 1;
        while (colorNode2.nextReducible != null) {
            if (i2 > colorNode2.nextReducible.colorCount) {
                colorNode = colorNode2;
                i2 = colorNode2.colorCount;
            }
            colorNode2 = colorNode2.nextReducible;
            i3++;
        }
        if (colorNode == this.reduceList[length]) {
            this.reduceList[length] = colorNode.nextReducible;
        } else {
            ColorNode colorNode3 = colorNode.nextReducible;
            colorNode.nextReducible = colorNode3.nextReducible;
            colorNode = colorNode3;
        }
        if (colorNode.isLeaf) {
            return;
        }
        int leafChildCount = colorNode.getLeafChildCount();
        colorNode.isLeaf = true;
        this.currSize -= leafChildCount - 1;
        int i4 = colorNode.level;
        for (int i5 = 0; i5 < 8; i5++) {
            colorNode.children[i5] = freeTree(colorNode.children[i5]);
        }
        colorNode.childCount = 0;
    }

    protected ColorNode freeTree(ColorNode colorNode) {
        if (colorNode == null) {
            return null;
        }
        for (int i2 = 0; i2 < 8; i2++) {
            colorNode.children[i2] = freeTree(colorNode.children[i2]);
        }
        this.numNodes--;
        return null;
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/common/PaletteBuilder$ColorNode.class */
    protected class ColorNode {
        public int colorCount;
        public long red;
        public long blue;
        public long green;
        public int paletteIndex;
        ColorNode nextReducible;
        public boolean isLeaf = false;
        public int level = 0;
        public int childCount = 0;
        ColorNode[] children = new ColorNode[8];

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v0, types: [com.sun.imageio.plugins.common.PaletteBuilder$ColorNode] */
        public ColorNode() {
            for (int i2 = 0; i2 < 8; i2++) {
                this.children[i2] = null;
            }
            this.colorCount = 0;
            ?? r3 = 0;
            this.blue = 0L;
            this.green = 0L;
            r3.red = this;
            this.paletteIndex = 0;
        }

        public int getLeafChildCount() {
            if (this.isLeaf) {
                return 0;
            }
            int leafChildCount = 0;
            for (int i2 = 0; i2 < this.children.length; i2++) {
                if (this.children[i2] != null) {
                    if (this.children[i2].isLeaf) {
                        leafChildCount++;
                    } else {
                        leafChildCount += this.children[i2].getLeafChildCount();
                    }
                }
            }
            return leafChildCount;
        }

        public int getRGB() {
            return (-16777216) | ((255 & (((int) this.red) / this.colorCount)) << 16) | ((255 & (((int) this.green) / this.colorCount)) << 8) | (255 & (((int) this.blue) / this.colorCount));
        }
    }
}
