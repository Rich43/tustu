package javafx.scene.text;

import com.sun.javafx.tk.Toolkit;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.FilePermission;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/text/Font.class */
public final class Font {
    private static final String DEFAULT_FAMILY = "System";
    private static final String DEFAULT_FULLNAME = "System Regular";
    private static float defaultSystemFontSize = -1.0f;
    private static Font DEFAULT;
    private String name;
    private String family;
    private String style;
    private double size;
    private int hash;
    private Object nativeFont;

    private static float getDefaultSystemFontSize() {
        if (defaultSystemFontSize == -1.0f) {
            defaultSystemFontSize = Toolkit.getToolkit().getFontLoader().getSystemFontSize();
        }
        return defaultSystemFontSize;
    }

    public static synchronized Font getDefault() {
        if (DEFAULT == null) {
            DEFAULT = new Font("System Regular", getDefaultSystemFontSize());
        }
        return DEFAULT;
    }

    public static List<String> getFamilies() {
        return Toolkit.getToolkit().getFontLoader().getFamilies();
    }

    public static List<String> getFontNames() {
        return Toolkit.getToolkit().getFontLoader().getFontNames();
    }

    public static List<String> getFontNames(String family) {
        return Toolkit.getToolkit().getFontLoader().getFontNames(family);
    }

    public static Font font(String family, FontWeight weight, FontPosture posture, double size) {
        String fam = (family == null || "".equals(family)) ? "System" : family;
        double sz = size < 0.0d ? getDefaultSystemFontSize() : size;
        return Toolkit.getToolkit().getFontLoader().font(fam, weight, posture, (float) sz);
    }

    public static Font font(String family, FontWeight weight, double size) {
        return font(family, weight, null, size);
    }

    public static Font font(String family, FontPosture posture, double size) {
        return font(family, null, posture, size);
    }

    public static Font font(String family, double size) {
        return font(family, null, null, size);
    }

    public static Font font(String family) {
        return font(family, null, null, -1.0d);
    }

    public static Font font(double size) {
        return font(null, null, null, size);
    }

    public final String getName() {
        return this.name;
    }

    public final String getFamily() {
        return this.family;
    }

    public final String getStyle() {
        return this.style;
    }

    public final double getSize() {
        return this.size;
    }

    public Font(@NamedArg("size") double size) {
        this(null, size);
    }

    public Font(@NamedArg("name") String name, @NamedArg("size") double size) {
        this.hash = 0;
        this.name = name;
        this.size = size;
        if (name == null || "".equals(name)) {
            this.name = "System Regular";
        }
        if (size < 0.0d) {
            this.size = getDefaultSystemFontSize();
        }
        Toolkit.getToolkit().getFontLoader().loadFont(this);
    }

    private Font(Object f2, String family, String name, String style, double size) {
        this.hash = 0;
        this.nativeFont = f2;
        this.family = family;
        this.name = name;
        this.style = style;
        this.size = size;
    }

    public static Font loadFont(String urlStr, double size) {
        try {
            URL url = new URL(urlStr);
            if (size <= 0.0d) {
                size = getDefaultSystemFontSize();
            }
            if (url.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
                String path = new File(url.getFile()).getPath();
                try {
                    SecurityManager sm = System.getSecurityManager();
                    if (sm != null) {
                        FilePermission filePermission = new FilePermission(path, "read");
                        sm.checkPermission(filePermission);
                    }
                    return Toolkit.getToolkit().getFontLoader().loadFont(path, size);
                } catch (Exception e2) {
                    return null;
                }
            }
            InputStream in = null;
            try {
                URLConnection connection = url.openConnection();
                in = connection.getInputStream();
                Font font = Toolkit.getToolkit().getFontLoader().loadFont(in, size);
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e3) {
                    }
                }
                return font;
            } catch (Exception e4) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e5) {
                        return null;
                    }
                }
                return null;
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e6) {
                        throw th;
                    }
                }
                throw th;
            }
        } catch (Exception e7) {
            return null;
        }
    }

    public static Font loadFont(InputStream in, double size) {
        if (size <= 0.0d) {
            size = getDefaultSystemFontSize();
        }
        return Toolkit.getToolkit().getFontLoader().loadFont(in, size);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Font[name=");
        return builder.append(this.name).append(", family=").append(this.family).append(", style=").append(this.style).append(", size=").append(this.size).append("]").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Font) {
            Font other = (Font) obj;
            if (this.name != null ? this.name.equals(other.name) : other.name == null) {
                if (this.size == other.size) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (37 * ((37 * 17) + this.name.hashCode())) + Double.doubleToLongBits(this.size);
            this.hash = (int) (bits ^ (bits >> 32));
        }
        return this.hash;
    }

    @Deprecated
    public Object impl_getNativeFont() {
        return this.nativeFont;
    }

    @Deprecated
    public void impl_setNativeFont(Object f2, String nam, String fam, String styl) {
        this.nativeFont = f2;
        this.name = nam;
        this.family = fam;
        this.style = styl;
    }

    @Deprecated
    public static Font impl_NativeFont(Object f2, String name, String family, String style, double size) {
        Font retFont = new Font(f2, family, name, style, size);
        return retFont;
    }
}
