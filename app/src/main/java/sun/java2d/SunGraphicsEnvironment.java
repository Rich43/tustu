package sun.java2d;

import com.sun.corba.se.impl.util.Version;
import java.awt.AWTError;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.peer.ComponentPeer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Locale;
import java.util.TreeMap;
import org.apache.commons.net.ftp.FTP;
import sun.awt.DisplayChangedListener;
import sun.awt.SunDisplayChanger;
import sun.font.FontManagerFactory;
import sun.font.FontManagerForSGE;

/* loaded from: rt.jar:sun/java2d/SunGraphicsEnvironment.class */
public abstract class SunGraphicsEnvironment extends GraphicsEnvironment implements DisplayChangedListener {
    public static boolean isOpenSolaris;
    private static Font defaultFont;
    protected GraphicsDevice[] screens;
    protected SunDisplayChanger displayChanger = new SunDisplayChanger();

    protected abstract int getNumScreens();

    protected abstract GraphicsDevice makeScreenDevice(int i2);

    public abstract boolean isDisplayLocal();

    public SunGraphicsEnvironment() {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.java2d.SunGraphicsEnvironment.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                try {
                    if (Float.parseFloat(System.getProperty("os.version", Version.BUILD)) > 5.1f) {
                        FileInputStream fileInputStream = new FileInputStream(new File("/etc/release"));
                        if (new BufferedReader(new InputStreamReader(fileInputStream, FTP.DEFAULT_CONTROL_ENCODING)).readLine().indexOf("OpenSolaris") >= 0) {
                            SunGraphicsEnvironment.isOpenSolaris = true;
                        } else {
                            SunGraphicsEnvironment.isOpenSolaris = !new File("/usr/openwin/lib/X11/fonts/TrueType/CourierNew.ttf").exists();
                        }
                        fileInputStream.close();
                    }
                } catch (Exception e2) {
                }
                Font unused = SunGraphicsEnvironment.defaultFont = new Font(Font.DIALOG, 0, 12);
                return null;
            }
        });
    }

    @Override // java.awt.GraphicsEnvironment
    public synchronized GraphicsDevice[] getScreenDevices() {
        GraphicsDevice[] graphicsDeviceArr = this.screens;
        if (graphicsDeviceArr == null) {
            int numScreens = getNumScreens();
            graphicsDeviceArr = new GraphicsDevice[numScreens];
            for (int i2 = 0; i2 < numScreens; i2++) {
                graphicsDeviceArr[i2] = makeScreenDevice(i2);
            }
            this.screens = graphicsDeviceArr;
        }
        return graphicsDeviceArr;
    }

    @Override // java.awt.GraphicsEnvironment
    public GraphicsDevice getDefaultScreenDevice() {
        GraphicsDevice[] screenDevices = getScreenDevices();
        if (screenDevices.length == 0) {
            throw new AWTError("no screen devices");
        }
        return screenDevices[0];
    }

    @Override // java.awt.GraphicsEnvironment
    public Graphics2D createGraphics(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            throw new NullPointerException("BufferedImage cannot be null");
        }
        return new SunGraphics2D(SurfaceData.getPrimarySurfaceData(bufferedImage), Color.white, Color.black, defaultFont);
    }

    public static FontManagerForSGE getFontManagerForSGE() {
        return (FontManagerForSGE) FontManagerFactory.getInstance();
    }

    public static void useAlternateFontforJALocales() {
        getFontManagerForSGE().useAlternateFontforJALocales();
    }

    @Override // java.awt.GraphicsEnvironment
    public Font[] getAllFonts() {
        FontManagerForSGE fontManagerForSGE = getFontManagerForSGE();
        Font[] allInstalledFonts = fontManagerForSGE.getAllInstalledFonts();
        Font[] createdFonts = fontManagerForSGE.getCreatedFonts();
        if (createdFonts == null || createdFonts.length == 0) {
            return allInstalledFonts;
        }
        Font[] fontArr = (Font[]) Arrays.copyOf(allInstalledFonts, allInstalledFonts.length + createdFonts.length);
        System.arraycopy(createdFonts, 0, fontArr, allInstalledFonts.length, createdFonts.length);
        return fontArr;
    }

    @Override // java.awt.GraphicsEnvironment
    public String[] getAvailableFontFamilyNames(Locale locale) {
        FontManagerForSGE fontManagerForSGE = getFontManagerForSGE();
        String[] installedFontFamilyNames = fontManagerForSGE.getInstalledFontFamilyNames(locale);
        TreeMap<String, String> createdFontFamilyNames = fontManagerForSGE.getCreatedFontFamilyNames();
        if (createdFontFamilyNames == null || createdFontFamilyNames.size() == 0) {
            return installedFontFamilyNames;
        }
        for (int i2 = 0; i2 < installedFontFamilyNames.length; i2++) {
            createdFontFamilyNames.put(installedFontFamilyNames[i2].toLowerCase(locale), installedFontFamilyNames[i2]);
        }
        String[] strArr = new String[createdFontFamilyNames.size()];
        Object[] array = createdFontFamilyNames.keySet().toArray();
        for (int i3 = 0; i3 < array.length; i3++) {
            strArr[i3] = createdFontFamilyNames.get(array[i3]);
        }
        return strArr;
    }

    @Override // java.awt.GraphicsEnvironment
    public String[] getAvailableFontFamilyNames() {
        return getAvailableFontFamilyNames(Locale.getDefault());
    }

    public static Rectangle getUsableBounds(GraphicsDevice graphicsDevice) throws HeadlessException {
        GraphicsConfiguration defaultConfiguration = graphicsDevice.getDefaultConfiguration();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(defaultConfiguration);
        Rectangle bounds = defaultConfiguration.getBounds();
        bounds.f12372x += screenInsets.left;
        bounds.f12373y += screenInsets.top;
        bounds.width -= screenInsets.left + screenInsets.right;
        bounds.height -= screenInsets.top + screenInsets.bottom;
        return bounds;
    }

    public void displayChanged() {
        for (Object obj : getScreenDevices()) {
            if (obj instanceof DisplayChangedListener) {
                ((DisplayChangedListener) obj).displayChanged();
            }
        }
        this.displayChanger.notifyListeners();
    }

    @Override // sun.awt.DisplayChangedListener
    public void paletteChanged() {
        this.displayChanger.notifyPaletteChanged();
    }

    public void addDisplayChangedListener(DisplayChangedListener displayChangedListener) {
        this.displayChanger.add(displayChangedListener);
    }

    public void removeDisplayChangedListener(DisplayChangedListener displayChangedListener) {
        this.displayChanger.remove(displayChangedListener);
    }

    public boolean isFlipStrategyPreferred(ComponentPeer componentPeer) {
        return false;
    }
}
