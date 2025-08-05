package javax.imageio.spi;

import com.sun.imageio.plugins.bmp.BMPImageReaderSpi;
import com.sun.imageio.plugins.bmp.BMPImageWriterSpi;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import com.sun.imageio.plugins.gif.GIFImageWriterSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import com.sun.imageio.plugins.png.PNGImageReaderSpi;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;
import com.sun.imageio.plugins.wbmp.WBMPImageReaderSpi;
import com.sun.imageio.plugins.wbmp.WBMPImageWriterSpi;
import com.sun.imageio.spi.FileImageInputStreamSpi;
import com.sun.imageio.spi.FileImageOutputStreamSpi;
import com.sun.imageio.spi.InputStreamImageInputStreamSpi;
import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;
import com.sun.imageio.spi.RAFImageInputStreamSpi;
import com.sun.imageio.spi.RAFImageOutputStreamSpi;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Vector;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/imageio/spi/IIORegistry.class */
public final class IIORegistry extends ServiceRegistry {
    private static final Vector initialCategories = new Vector(5);

    static {
        initialCategories.add(ImageReaderSpi.class);
        initialCategories.add(ImageWriterSpi.class);
        initialCategories.add(ImageTranscoderSpi.class);
        initialCategories.add(ImageInputStreamSpi.class);
        initialCategories.add(ImageOutputStreamSpi.class);
    }

    private IIORegistry() {
        super(initialCategories.iterator());
        registerStandardSpis();
        registerApplicationClasspathSpis();
    }

    public static IIORegistry getDefaultInstance() {
        AppContext appContext = AppContext.getAppContext();
        IIORegistry iIORegistry = (IIORegistry) appContext.get(IIORegistry.class);
        if (iIORegistry == null) {
            iIORegistry = new IIORegistry();
            appContext.put(IIORegistry.class, iIORegistry);
        }
        return iIORegistry;
    }

    private void registerStandardSpis() {
        registerServiceProvider(new GIFImageReaderSpi());
        registerServiceProvider(new GIFImageWriterSpi());
        registerServiceProvider(new BMPImageReaderSpi());
        registerServiceProvider(new BMPImageWriterSpi());
        registerServiceProvider(new WBMPImageReaderSpi());
        registerServiceProvider(new WBMPImageWriterSpi());
        registerServiceProvider(new PNGImageReaderSpi());
        registerServiceProvider(new PNGImageWriterSpi());
        registerServiceProvider(new JPEGImageReaderSpi());
        registerServiceProvider(new JPEGImageWriterSpi());
        registerServiceProvider(new FileImageInputStreamSpi());
        registerServiceProvider(new FileImageOutputStreamSpi());
        registerServiceProvider(new InputStreamImageInputStreamSpi());
        registerServiceProvider(new OutputStreamImageOutputStreamSpi());
        registerServiceProvider(new RAFImageInputStreamSpi());
        registerServiceProvider(new RAFImageOutputStreamSpi());
        registerInstalledProviders();
    }

    public void registerApplicationClasspathSpis() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Iterator<Class<?>> categories = getCategories();
        while (categories.hasNext()) {
            Iterator it = ServiceLoader.load(categories.next(), contextClassLoader).iterator();
            while (it.hasNext()) {
                try {
                    registerServiceProvider((IIOServiceProvider) it.next());
                } catch (ServiceConfigurationError e2) {
                    if (System.getSecurityManager() != null) {
                        e2.printStackTrace();
                    } else {
                        throw e2;
                    }
                }
            }
        }
    }

    private void registerInstalledProviders() {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.imageio.spi.IIORegistry.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                Iterator<Class<?>> categories = IIORegistry.this.getCategories();
                while (categories.hasNext()) {
                    Iterator it = ServiceLoader.loadInstalled(categories.next()).iterator();
                    while (it.hasNext()) {
                        IIORegistry.this.registerServiceProvider((IIOServiceProvider) it.next());
                    }
                }
                return this;
            }
        });
    }
}
