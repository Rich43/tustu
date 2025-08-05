package javax.imageio;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageOutputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.spi.ImageTranscoderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import sun.awt.AppContext;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:javax/imageio/ImageIO.class */
public final class ImageIO {
    private static final IIORegistry theRegistry = IIORegistry.getDefaultInstance();
    private static Method readerFormatNamesMethod;
    private static Method readerFileSuffixesMethod;
    private static Method readerMIMETypesMethod;
    private static Method writerFormatNamesMethod;
    private static Method writerFileSuffixesMethod;
    private static Method writerMIMETypesMethod;

    /* loaded from: rt.jar:javax/imageio/ImageIO$SpiInfo.class */
    private enum SpiInfo {
        FORMAT_NAMES { // from class: javax.imageio.ImageIO.SpiInfo.1
            @Override // javax.imageio.ImageIO.SpiInfo
            String[] info(ImageReaderWriterSpi imageReaderWriterSpi) {
                return imageReaderWriterSpi.getFormatNames();
            }
        },
        MIME_TYPES { // from class: javax.imageio.ImageIO.SpiInfo.2
            @Override // javax.imageio.ImageIO.SpiInfo
            String[] info(ImageReaderWriterSpi imageReaderWriterSpi) {
                return imageReaderWriterSpi.getMIMETypes();
            }
        },
        FILE_SUFFIXES { // from class: javax.imageio.ImageIO.SpiInfo.3
            @Override // javax.imageio.ImageIO.SpiInfo
            String[] info(ImageReaderWriterSpi imageReaderWriterSpi) {
                return imageReaderWriterSpi.getFileSuffixes();
            }
        };

        abstract String[] info(ImageReaderWriterSpi imageReaderWriterSpi);
    }

    static {
        try {
            readerFormatNamesMethod = ImageReaderSpi.class.getMethod("getFormatNames", new Class[0]);
            readerFileSuffixesMethod = ImageReaderSpi.class.getMethod("getFileSuffixes", new Class[0]);
            readerMIMETypesMethod = ImageReaderSpi.class.getMethod("getMIMETypes", new Class[0]);
            writerFormatNamesMethod = ImageWriterSpi.class.getMethod("getFormatNames", new Class[0]);
            writerFileSuffixesMethod = ImageWriterSpi.class.getMethod("getFileSuffixes", new Class[0]);
            writerMIMETypesMethod = ImageWriterSpi.class.getMethod("getMIMETypes", new Class[0]);
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
    }

    private ImageIO() {
    }

    public static void scanForPlugins() {
        theRegistry.registerApplicationClasspathSpis();
    }

    /* loaded from: rt.jar:javax/imageio/ImageIO$CacheInfo.class */
    static class CacheInfo {
        boolean useCache = true;
        File cacheDirectory = null;
        Boolean hasPermission = null;

        public boolean getUseCache() {
            return this.useCache;
        }

        public void setUseCache(boolean z2) {
            this.useCache = z2;
        }

        public File getCacheDirectory() {
            return this.cacheDirectory;
        }

        public void setCacheDirectory(File file) {
            this.cacheDirectory = file;
        }

        public Boolean getHasPermission() {
            return this.hasPermission;
        }

        public void setHasPermission(Boolean bool) {
            this.hasPermission = bool;
        }
    }

    private static synchronized CacheInfo getCacheInfo() {
        AppContext appContext = AppContext.getAppContext();
        CacheInfo cacheInfo = (CacheInfo) appContext.get(CacheInfo.class);
        if (cacheInfo == null) {
            cacheInfo = new CacheInfo();
            appContext.put(CacheInfo.class, cacheInfo);
        }
        return cacheInfo;
    }

    private static String getTempDir() {
        return (String) AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir"));
    }

    private static boolean hasCachePermission() {
        String tempDir;
        Boolean hasPermission = getCacheInfo().getHasPermission();
        if (hasPermission != null) {
            return hasPermission.booleanValue();
        }
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                File cacheDirectory = getCacheDirectory();
                if (cacheDirectory != null) {
                    tempDir = cacheDirectory.getPath();
                } else {
                    tempDir = getTempDir();
                    if (tempDir == null || tempDir.isEmpty()) {
                        getCacheInfo().setHasPermission(Boolean.FALSE);
                        return false;
                    }
                }
                String str = tempDir;
                if (!str.endsWith(File.separator)) {
                    str = str + File.separator;
                }
                securityManager.checkPermission(new FilePermission(str + "*", "read, write, delete"));
            }
            getCacheInfo().setHasPermission(Boolean.TRUE);
            return true;
        } catch (SecurityException e2) {
            getCacheInfo().setHasPermission(Boolean.FALSE);
            return false;
        }
    }

    public static void setUseCache(boolean z2) {
        getCacheInfo().setUseCache(z2);
    }

    public static boolean getUseCache() {
        return getCacheInfo().getUseCache();
    }

    public static void setCacheDirectory(File file) {
        if (file != null && !file.isDirectory()) {
            throw new IllegalArgumentException("Not a directory!");
        }
        getCacheInfo().setCacheDirectory(file);
        getCacheInfo().setHasPermission(null);
    }

    public static File getCacheDirectory() {
        return getCacheInfo().getCacheDirectory();
    }

    public static ImageInputStream createImageInputStream(Object obj) throws IOException {
        if (obj == null) {
            throw new IllegalArgumentException("input == null!");
        }
        try {
            Iterator serviceProviders = theRegistry.getServiceProviders(ImageInputStreamSpi.class, true);
            boolean z2 = getUseCache() && hasCachePermission();
            while (serviceProviders.hasNext()) {
                ImageInputStreamSpi imageInputStreamSpi = (ImageInputStreamSpi) serviceProviders.next();
                if (imageInputStreamSpi.getInputClass().isInstance(obj)) {
                    try {
                        return imageInputStreamSpi.createInputStreamInstance(obj, z2, getCacheDirectory());
                    } catch (IOException e2) {
                        throw new IIOException("Can't create cache file!", e2);
                    }
                }
            }
            return null;
        } catch (IllegalArgumentException e3) {
            return null;
        }
    }

    public static ImageOutputStream createImageOutputStream(Object obj) throws IOException {
        if (obj == null) {
            throw new IllegalArgumentException("output == null!");
        }
        try {
            Iterator serviceProviders = theRegistry.getServiceProviders(ImageOutputStreamSpi.class, true);
            boolean z2 = getUseCache() && hasCachePermission();
            while (serviceProviders.hasNext()) {
                ImageOutputStreamSpi imageOutputStreamSpi = (ImageOutputStreamSpi) serviceProviders.next();
                if (imageOutputStreamSpi.getOutputClass().isInstance(obj)) {
                    try {
                        return imageOutputStreamSpi.createOutputStreamInstance(obj, z2, getCacheDirectory());
                    } catch (IOException e2) {
                        throw new IIOException("Can't create cache file!", e2);
                    }
                }
            }
            return null;
        } catch (IllegalArgumentException e3) {
            return null;
        }
    }

    private static <S extends ImageReaderWriterSpi> String[] getReaderWriterInfo(Class<S> cls, SpiInfo spiInfo) {
        try {
            Iterator serviceProviders = theRegistry.getServiceProviders(cls, true);
            HashSet hashSet = new HashSet();
            while (serviceProviders.hasNext()) {
                Collections.addAll(hashSet, spiInfo.info((ImageReaderWriterSpi) serviceProviders.next()));
            }
            return (String[]) hashSet.toArray(new String[hashSet.size()]);
        } catch (IllegalArgumentException e2) {
            return new String[0];
        }
    }

    public static String[] getReaderFormatNames() {
        return getReaderWriterInfo(ImageReaderSpi.class, SpiInfo.FORMAT_NAMES);
    }

    public static String[] getReaderMIMETypes() {
        return getReaderWriterInfo(ImageReaderSpi.class, SpiInfo.MIME_TYPES);
    }

    public static String[] getReaderFileSuffixes() {
        return getReaderWriterInfo(ImageReaderSpi.class, SpiInfo.FILE_SUFFIXES);
    }

    /* loaded from: rt.jar:javax/imageio/ImageIO$ImageReaderIterator.class */
    static class ImageReaderIterator implements Iterator<ImageReader> {
        public Iterator iter;

        public ImageReaderIterator(Iterator it) {
            this.iter = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ImageReader next() {
            ImageReaderSpi imageReaderSpi = null;
            try {
                imageReaderSpi = (ImageReaderSpi) this.iter.next();
                return imageReaderSpi.createReaderInstance();
            } catch (IOException e2) {
                ImageIO.theRegistry.deregisterServiceProvider(imageReaderSpi, ImageReaderSpi.class);
                return null;
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:javax/imageio/ImageIO$CanDecodeInputFilter.class */
    static class CanDecodeInputFilter implements ServiceRegistry.Filter {
        Object input;

        public CanDecodeInputFilter(Object obj) {
            this.input = obj;
        }

        @Override // javax.imageio.spi.ServiceRegistry.Filter
        public boolean filter(Object obj) {
            try {
                ImageReaderSpi imageReaderSpi = (ImageReaderSpi) obj;
                ImageInputStream imageInputStream = null;
                if (this.input instanceof ImageInputStream) {
                    imageInputStream = (ImageInputStream) this.input;
                }
                if (imageInputStream != null) {
                    imageInputStream.mark();
                }
                boolean zCanDecodeInput = imageReaderSpi.canDecodeInput(this.input);
                if (imageInputStream != null) {
                    imageInputStream.reset();
                }
                return zCanDecodeInput;
            } catch (IOException e2) {
                return false;
            }
        }
    }

    /* loaded from: rt.jar:javax/imageio/ImageIO$CanEncodeImageAndFormatFilter.class */
    static class CanEncodeImageAndFormatFilter implements ServiceRegistry.Filter {
        ImageTypeSpecifier type;
        String formatName;

        public CanEncodeImageAndFormatFilter(ImageTypeSpecifier imageTypeSpecifier, String str) {
            this.type = imageTypeSpecifier;
            this.formatName = str;
        }

        @Override // javax.imageio.spi.ServiceRegistry.Filter
        public boolean filter(Object obj) {
            ImageWriterSpi imageWriterSpi = (ImageWriterSpi) obj;
            return Arrays.asList(imageWriterSpi.getFormatNames()).contains(this.formatName) && imageWriterSpi.canEncodeImage(this.type);
        }
    }

    /* loaded from: rt.jar:javax/imageio/ImageIO$ContainsFilter.class */
    static class ContainsFilter implements ServiceRegistry.Filter {
        Method method;
        String name;

        public ContainsFilter(Method method, String str) {
            this.method = method;
            this.name = str;
        }

        @Override // javax.imageio.spi.ServiceRegistry.Filter
        public boolean filter(Object obj) {
            try {
                return ImageIO.contains((String[]) this.method.invoke(obj, new Object[0]), this.name);
            } catch (Exception e2) {
                return false;
            }
        }
    }

    public static Iterator<ImageReader> getImageReaders(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("input == null!");
        }
        try {
            return new ImageReaderIterator(theRegistry.getServiceProviders(ImageReaderSpi.class, new CanDecodeInputFilter(obj), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    public static Iterator<ImageReader> getImageReadersByFormatName(String str) {
        if (str == null) {
            throw new IllegalArgumentException("formatName == null!");
        }
        try {
            return new ImageReaderIterator(theRegistry.getServiceProviders(ImageReaderSpi.class, new ContainsFilter(readerFormatNamesMethod, str), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    public static Iterator<ImageReader> getImageReadersBySuffix(String str) {
        if (str == null) {
            throw new IllegalArgumentException("fileSuffix == null!");
        }
        try {
            return new ImageReaderIterator(theRegistry.getServiceProviders(ImageReaderSpi.class, new ContainsFilter(readerFileSuffixesMethod, str), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    public static Iterator<ImageReader> getImageReadersByMIMEType(String str) {
        if (str == null) {
            throw new IllegalArgumentException("MIMEType == null!");
        }
        try {
            return new ImageReaderIterator(theRegistry.getServiceProviders(ImageReaderSpi.class, new ContainsFilter(readerMIMETypesMethod, str), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    public static String[] getWriterFormatNames() {
        return getReaderWriterInfo(ImageWriterSpi.class, SpiInfo.FORMAT_NAMES);
    }

    public static String[] getWriterMIMETypes() {
        return getReaderWriterInfo(ImageWriterSpi.class, SpiInfo.MIME_TYPES);
    }

    public static String[] getWriterFileSuffixes() {
        return getReaderWriterInfo(ImageWriterSpi.class, SpiInfo.FILE_SUFFIXES);
    }

    /* loaded from: rt.jar:javax/imageio/ImageIO$ImageWriterIterator.class */
    static class ImageWriterIterator implements Iterator<ImageWriter> {
        public Iterator iter;

        public ImageWriterIterator(Iterator it) {
            this.iter = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ImageWriter next() {
            ImageWriterSpi imageWriterSpi = null;
            try {
                imageWriterSpi = (ImageWriterSpi) this.iter.next();
                return imageWriterSpi.createWriterInstance();
            } catch (IOException e2) {
                ImageIO.theRegistry.deregisterServiceProvider(imageWriterSpi, ImageWriterSpi.class);
                return null;
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean contains(String[] strArr, String str) {
        for (String str2 : strArr) {
            if (str.equalsIgnoreCase(str2)) {
                return true;
            }
        }
        return false;
    }

    public static Iterator<ImageWriter> getImageWritersByFormatName(String str) {
        if (str == null) {
            throw new IllegalArgumentException("formatName == null!");
        }
        try {
            return new ImageWriterIterator(theRegistry.getServiceProviders(ImageWriterSpi.class, new ContainsFilter(writerFormatNamesMethod, str), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    public static Iterator<ImageWriter> getImageWritersBySuffix(String str) {
        if (str == null) {
            throw new IllegalArgumentException("fileSuffix == null!");
        }
        try {
            return new ImageWriterIterator(theRegistry.getServiceProviders(ImageWriterSpi.class, new ContainsFilter(writerFileSuffixesMethod, str), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    public static Iterator<ImageWriter> getImageWritersByMIMEType(String str) {
        if (str == null) {
            throw new IllegalArgumentException("MIMEType == null!");
        }
        try {
            return new ImageWriterIterator(theRegistry.getServiceProviders(ImageWriterSpi.class, new ContainsFilter(writerMIMETypesMethod, str), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    public static ImageWriter getImageWriter(ImageReader imageReader) {
        if (imageReader == null) {
            throw new IllegalArgumentException("reader == null!");
        }
        ImageReaderSpi originatingProvider = imageReader.getOriginatingProvider();
        if (originatingProvider == null) {
            try {
                Iterator serviceProviders = theRegistry.getServiceProviders(ImageReaderSpi.class, false);
                while (true) {
                    if (!serviceProviders.hasNext()) {
                        break;
                    }
                    ImageReaderSpi imageReaderSpi = (ImageReaderSpi) serviceProviders.next();
                    if (imageReaderSpi.isOwnReader(imageReader)) {
                        originatingProvider = imageReaderSpi;
                        break;
                    }
                }
                if (originatingProvider == null) {
                    return null;
                }
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
        String[] imageWriterSpiNames = originatingProvider.getImageWriterSpiNames();
        if (imageWriterSpiNames == null) {
            return null;
        }
        try {
            ImageWriterSpi imageWriterSpi = (ImageWriterSpi) theRegistry.getServiceProviderByClass(Class.forName(imageWriterSpiNames[0], true, ClassLoader.getSystemClassLoader()));
            if (imageWriterSpi == null) {
                return null;
            }
            try {
                return imageWriterSpi.createWriterInstance();
            } catch (IOException e3) {
                theRegistry.deregisterServiceProvider(imageWriterSpi, ImageWriterSpi.class);
                return null;
            }
        } catch (ClassNotFoundException e4) {
            return null;
        }
    }

    public static ImageReader getImageReader(ImageWriter imageWriter) {
        if (imageWriter == null) {
            throw new IllegalArgumentException("writer == null!");
        }
        ImageWriterSpi originatingProvider = imageWriter.getOriginatingProvider();
        if (originatingProvider == null) {
            try {
                Iterator serviceProviders = theRegistry.getServiceProviders(ImageWriterSpi.class, false);
                while (true) {
                    if (!serviceProviders.hasNext()) {
                        break;
                    }
                    ImageWriterSpi imageWriterSpi = (ImageWriterSpi) serviceProviders.next();
                    if (imageWriterSpi.isOwnWriter(imageWriter)) {
                        originatingProvider = imageWriterSpi;
                        break;
                    }
                }
                if (originatingProvider == null) {
                    return null;
                }
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
        String[] imageReaderSpiNames = originatingProvider.getImageReaderSpiNames();
        if (imageReaderSpiNames == null) {
            return null;
        }
        try {
            ImageReaderSpi imageReaderSpi = (ImageReaderSpi) theRegistry.getServiceProviderByClass(Class.forName(imageReaderSpiNames[0], true, ClassLoader.getSystemClassLoader()));
            if (imageReaderSpi == null) {
                return null;
            }
            try {
                return imageReaderSpi.createReaderInstance();
            } catch (IOException e3) {
                theRegistry.deregisterServiceProvider(imageReaderSpi, ImageReaderSpi.class);
                return null;
            }
        } catch (ClassNotFoundException e4) {
            return null;
        }
    }

    public static Iterator<ImageWriter> getImageWriters(ImageTypeSpecifier imageTypeSpecifier, String str) {
        if (imageTypeSpecifier == null) {
            throw new IllegalArgumentException("type == null!");
        }
        if (str == null) {
            throw new IllegalArgumentException("formatName == null!");
        }
        try {
            return new ImageWriterIterator(theRegistry.getServiceProviders(ImageWriterSpi.class, new CanEncodeImageAndFormatFilter(imageTypeSpecifier, str), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    /* loaded from: rt.jar:javax/imageio/ImageIO$ImageTranscoderIterator.class */
    static class ImageTranscoderIterator implements Iterator<ImageTranscoder> {
        public Iterator iter;

        public ImageTranscoderIterator(Iterator it) {
            this.iter = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ImageTranscoder next() {
            return ((ImageTranscoderSpi) this.iter.next()).createTranscoderInstance();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:javax/imageio/ImageIO$TranscoderFilter.class */
    static class TranscoderFilter implements ServiceRegistry.Filter {
        String readerSpiName;
        String writerSpiName;

        public TranscoderFilter(ImageReaderSpi imageReaderSpi, ImageWriterSpi imageWriterSpi) {
            this.readerSpiName = imageReaderSpi.getClass().getName();
            this.writerSpiName = imageWriterSpi.getClass().getName();
        }

        @Override // javax.imageio.spi.ServiceRegistry.Filter
        public boolean filter(Object obj) {
            ImageTranscoderSpi imageTranscoderSpi = (ImageTranscoderSpi) obj;
            return imageTranscoderSpi.getReaderServiceProviderName().equals(this.readerSpiName) && imageTranscoderSpi.getWriterServiceProviderName().equals(this.writerSpiName);
        }
    }

    public static Iterator<ImageTranscoder> getImageTranscoders(ImageReader imageReader, ImageWriter imageWriter) {
        if (imageReader == null) {
            throw new IllegalArgumentException("reader == null!");
        }
        if (imageWriter == null) {
            throw new IllegalArgumentException("writer == null!");
        }
        try {
            return new ImageTranscoderIterator(theRegistry.getServiceProviders(ImageTranscoderSpi.class, new TranscoderFilter(imageReader.getOriginatingProvider(), imageWriter.getOriginatingProvider()), true));
        } catch (IllegalArgumentException e2) {
            return Collections.emptyIterator();
        }
    }

    public static BufferedImage read(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("input == null!");
        }
        if (!file.canRead()) {
            throw new IIOException("Can't read input file!");
        }
        ImageInputStream imageInputStreamCreateImageInputStream = createImageInputStream(file);
        if (imageInputStreamCreateImageInputStream == null) {
            throw new IIOException("Can't create an ImageInputStream!");
        }
        BufferedImage bufferedImage = read(imageInputStreamCreateImageInputStream);
        if (bufferedImage == null) {
            imageInputStreamCreateImageInputStream.close();
        }
        return bufferedImage;
    }

    public static BufferedImage read(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("input == null!");
        }
        ImageInputStream imageInputStreamCreateImageInputStream = createImageInputStream(inputStream);
        if (imageInputStreamCreateImageInputStream == null) {
            throw new IIOException("Can't create an ImageInputStream!");
        }
        BufferedImage bufferedImage = read(imageInputStreamCreateImageInputStream);
        if (bufferedImage == null) {
            imageInputStreamCreateImageInputStream.close();
        }
        return bufferedImage;
    }

    public static BufferedImage read(URL url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("input == null!");
        }
        try {
            InputStream inputStreamOpenStream = url.openStream();
            ImageInputStream imageInputStreamCreateImageInputStream = createImageInputStream(inputStreamOpenStream);
            if (imageInputStreamCreateImageInputStream == null) {
                inputStreamOpenStream.close();
                throw new IIOException("Can't create an ImageInputStream!");
            }
            try {
                BufferedImage bufferedImage = read(imageInputStreamCreateImageInputStream);
                if (bufferedImage == null) {
                    imageInputStreamCreateImageInputStream.close();
                }
                return bufferedImage;
            } finally {
                inputStreamOpenStream.close();
            }
        } catch (IOException e2) {
            throw new IIOException("Can't get input stream from URL!", e2);
        }
    }

    public static BufferedImage read(ImageInputStream imageInputStream) throws IOException {
        if (imageInputStream == null) {
            throw new IllegalArgumentException("stream == null!");
        }
        Iterator<ImageReader> imageReaders = getImageReaders(imageInputStream);
        if (!imageReaders.hasNext()) {
            return null;
        }
        ImageReader next = imageReaders.next();
        ImageReadParam defaultReadParam = next.getDefaultReadParam();
        next.setInput(imageInputStream, true, true);
        try {
            BufferedImage bufferedImage = next.read(0, defaultReadParam);
            next.dispose();
            imageInputStream.close();
            return bufferedImage;
        } catch (Throwable th) {
            next.dispose();
            imageInputStream.close();
            throw th;
        }
    }

    public static boolean write(RenderedImage renderedImage, String str, ImageOutputStream imageOutputStream) throws IOException {
        if (renderedImage == null) {
            throw new IllegalArgumentException("im == null!");
        }
        if (str == null) {
            throw new IllegalArgumentException("formatName == null!");
        }
        if (imageOutputStream == null) {
            throw new IllegalArgumentException("output == null!");
        }
        return doWrite(renderedImage, getWriter(renderedImage, str), imageOutputStream);
    }

    public static boolean write(RenderedImage renderedImage, String str, File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("output == null!");
        }
        ImageWriter writer = getWriter(renderedImage, str);
        if (writer == null) {
            return false;
        }
        file.delete();
        ImageOutputStream imageOutputStreamCreateImageOutputStream = createImageOutputStream(file);
        if (imageOutputStreamCreateImageOutputStream == null) {
            throw new IIOException("Can't create an ImageOutputStream!");
        }
        try {
            boolean zDoWrite = doWrite(renderedImage, writer, imageOutputStreamCreateImageOutputStream);
            imageOutputStreamCreateImageOutputStream.close();
            return zDoWrite;
        } catch (Throwable th) {
            imageOutputStreamCreateImageOutputStream.close();
            throw th;
        }
    }

    public static boolean write(RenderedImage renderedImage, String str, OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("output == null!");
        }
        ImageOutputStream imageOutputStreamCreateImageOutputStream = createImageOutputStream(outputStream);
        if (imageOutputStreamCreateImageOutputStream == null) {
            throw new IIOException("Can't create an ImageOutputStream!");
        }
        try {
            boolean zDoWrite = doWrite(renderedImage, getWriter(renderedImage, str), imageOutputStreamCreateImageOutputStream);
            imageOutputStreamCreateImageOutputStream.close();
            return zDoWrite;
        } catch (Throwable th) {
            imageOutputStreamCreateImageOutputStream.close();
            throw th;
        }
    }

    private static ImageWriter getWriter(RenderedImage renderedImage, String str) {
        Iterator<ImageWriter> imageWriters = getImageWriters(ImageTypeSpecifier.createFromRenderedImage(renderedImage), str);
        if (imageWriters.hasNext()) {
            return imageWriters.next();
        }
        return null;
    }

    private static boolean doWrite(RenderedImage renderedImage, ImageWriter imageWriter, ImageOutputStream imageOutputStream) throws IOException {
        if (imageWriter == null) {
            return false;
        }
        imageWriter.setOutput(imageOutputStream);
        try {
            imageWriter.write(renderedImage);
            return true;
        } finally {
            imageWriter.dispose();
            imageOutputStream.flush();
        }
    }
}
