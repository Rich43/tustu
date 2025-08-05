package sun.awt.image;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.image.ImageObserver;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import sun.awt.image.ImageCache;

/* loaded from: rt.jar:sun/awt/image/MultiResolutionCachedImage.class */
public class MultiResolutionCachedImage extends AbstractMultiResolutionImage {
    private final int baseImageWidth;
    private final int baseImageHeight;
    private final Dimension2D[] sizes;
    private final BiFunction<Integer, Integer, Image> mapper;
    private int availableInfo;

    public MultiResolutionCachedImage(int i2, int i3, BiFunction<Integer, Integer, Image> biFunction) {
        this(i2, i3, new Dimension[]{new Dimension(i2, i3)}, biFunction);
    }

    public MultiResolutionCachedImage(int i2, int i3, Dimension2D[] dimension2DArr, BiFunction<Integer, Integer, Image> biFunction) {
        this.baseImageWidth = i2;
        this.baseImageHeight = i3;
        this.sizes = dimension2DArr == null ? null : (Dimension2D[]) Arrays.copyOf(dimension2DArr, dimension2DArr.length);
        this.mapper = biFunction;
    }

    @Override // sun.awt.image.MultiResolutionImage
    public Image getResolutionVariant(int i2, int i3) {
        ImageCache imageCache = ImageCache.getInstance();
        ImageCacheKey imageCacheKey = new ImageCacheKey(this, i2, i3);
        Image image = imageCache.getImage(imageCacheKey);
        if (image == null) {
            image = this.mapper.apply(Integer.valueOf(i2), Integer.valueOf(i3));
            imageCache.setImage(imageCacheKey, image);
        }
        preload(image, this.availableInfo);
        return image;
    }

    @Override // sun.awt.image.MultiResolutionImage
    public List<Image> getResolutionVariants() {
        return (List) Arrays.stream(this.sizes).map(dimension2D -> {
            return getResolutionVariant((int) dimension2D.getWidth(), (int) dimension2D.getHeight());
        }).collect(Collectors.toList());
    }

    public MultiResolutionCachedImage map(Function<Image, Image> function) {
        return new MultiResolutionCachedImage(this.baseImageWidth, this.baseImageHeight, this.sizes, (num, num2) -> {
            return (Image) function.apply(getResolutionVariant(num.intValue(), num2.intValue()));
        });
    }

    @Override // sun.awt.image.AbstractMultiResolutionImage, java.awt.Image
    public int getWidth(ImageObserver imageObserver) {
        updateInfo(imageObserver, 1);
        return super.getWidth(imageObserver);
    }

    @Override // sun.awt.image.AbstractMultiResolutionImage, java.awt.Image
    public int getHeight(ImageObserver imageObserver) {
        updateInfo(imageObserver, 2);
        return super.getHeight(imageObserver);
    }

    @Override // sun.awt.image.AbstractMultiResolutionImage, java.awt.Image
    public Object getProperty(String str, ImageObserver imageObserver) {
        updateInfo(imageObserver, 4);
        return super.getProperty(str, imageObserver);
    }

    @Override // sun.awt.image.AbstractMultiResolutionImage
    protected Image getBaseImage() {
        return getResolutionVariant(this.baseImageWidth, this.baseImageHeight);
    }

    private void updateInfo(ImageObserver imageObserver, int i2) {
        this.availableInfo |= imageObserver == null ? 32 : i2;
    }

    private static int getInfo(Image image) {
        if (image instanceof ToolkitImage) {
            return ((ToolkitImage) image).getImageRep().check((image2, i2, i3, i4, i5, i6) -> {
                return false;
            });
        }
        return 0;
    }

    private static void preload(Image image, final int i2) {
        if (i2 != 0 && (image instanceof ToolkitImage)) {
            ((ToolkitImage) image).preload(new ImageObserver() { // from class: sun.awt.image.MultiResolutionCachedImage.1
                int flags;

                {
                    this.flags = i2;
                }

                @Override // java.awt.image.ImageObserver
                public boolean imageUpdate(Image image2, int i3, int i4, int i5, int i6, int i7) {
                    this.flags &= i3 ^ (-1);
                    return this.flags != 0 && (i3 & 192) == 0;
                }
            });
        }
    }

    /* loaded from: rt.jar:sun/awt/image/MultiResolutionCachedImage$ImageCacheKey.class */
    private static class ImageCacheKey implements ImageCache.PixelsKey {
        private final int pixelCount;
        private final int hash = hash();

        /* renamed from: w, reason: collision with root package name */
        private final int f13547w;

        /* renamed from: h, reason: collision with root package name */
        private final int f13548h;
        private final Image baseImage;

        ImageCacheKey(Image image, int i2, int i3) {
            this.baseImage = image;
            this.f13547w = i2;
            this.f13548h = i3;
            this.pixelCount = i2 * i3;
        }

        @Override // sun.awt.image.ImageCache.PixelsKey
        public int getPixelCount() {
            return this.pixelCount;
        }

        private int hash() {
            return (31 * ((31 * this.baseImage.hashCode()) + this.f13547w)) + this.f13548h;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (obj instanceof ImageCacheKey) {
                ImageCacheKey imageCacheKey = (ImageCacheKey) obj;
                return this.baseImage == imageCacheKey.baseImage && this.f13547w == imageCacheKey.f13547w && this.f13548h == imageCacheKey.f13548h;
            }
            return false;
        }
    }
}
