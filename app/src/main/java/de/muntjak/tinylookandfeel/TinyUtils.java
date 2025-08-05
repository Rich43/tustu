package de.muntjak.tinylookandfeel;

import com.intel.bluetooth.BlueCoveImpl;
import com.sun.jmx.defaults.ServiceName;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import javax.swing.JComponent;
import sun.awt.SunToolkit;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyUtils.class */
public class TinyUtils {
    private static Map osSettings;
    private static boolean is1dot4;
    private static boolean is1dot5;
    private static boolean is1dot6;
    private static final String OS_NAME = getSystemPropertyPrivileged("os.name");
    private static final RenderingHints SAVED_HINTS = new RenderingHints(null);
    public static final Object AA_TEXT_PROPERTY_KEY = new StringBuffer("TinyAATextPropertyKey");
    private static String javaVersion = getSystemPropertyPrivileged("java.version");

    public static String getSystemPropertyPrivileged(String str) {
        try {
            return System.getProperty(str);
        } catch (SecurityException e2) {
            return (String) AccessController.doPrivileged(new PrivilegedAction(str) { // from class: de.muntjak.tinylookandfeel.TinyUtils.2
                private final String val$key;

                {
                    this.val$key = str;
                }

                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    return System.getProperty(this.val$key);
                }
            });
        }
    }

    public static boolean isOSLinux() {
        return OS_NAME.toLowerCase().startsWith("linux");
    }

    public static boolean isOSMac() {
        return OS_NAME.toLowerCase().startsWith(BlueCoveImpl.STACK_OSX);
    }

    public static boolean is1dot4() {
        return is1dot4;
    }

    public static boolean is1dot5() {
        return is1dot5;
    }

    public static boolean is1dot6() {
        return is1dot6;
    }

    public static String getJavaVersion() {
        return javaVersion;
    }

    public static void drawStringUnderlineCharAt(JComponent jComponent, Graphics graphics, String str, int i2, int i3, int i4) {
        if (str == null || str.length() <= 0) {
            return;
        }
        if (graphics instanceof Graphics2D) {
            drawString(jComponent, (Graphics2D) graphics, str, i3, i4);
        } else {
            graphics.drawString(str, i3, i4);
        }
        if (i2 < 0 || i2 >= str.length()) {
            return;
        }
        FontMetrics fontMetrics = graphics.getFontMetrics();
        graphics.fillRect(i3 + fontMetrics.stringWidth(str.substring(0, i2)), (i4 + fontMetrics.getDescent()) - 1, fontMetrics.charWidth(str.charAt(i2)), 1);
    }

    private static void drawString(JComponent jComponent, Graphics2D graphics2D, String str, int i2, int i3) {
        if (jComponent != null && Boolean.TRUE.equals(jComponent.getClientProperty(AA_TEXT_PROPERTY_KEY))) {
            Object renderingHint = graphics2D.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.drawString(str, i2, i3);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, renderingHint);
            return;
        }
        if (osSettings != null) {
            RenderingHints renderingHints = getRenderingHints(graphics2D, osSettings, SAVED_HINTS);
            graphics2D.addRenderingHints(osSettings);
            graphics2D.drawString(str, i2, i3);
            graphics2D.addRenderingHints(renderingHints);
            return;
        }
        if (!is1dot5() || !"true".equals(System.getProperty("swing.aatext"))) {
            graphics2D.drawString(str, i2, i3);
            return;
        }
        Object renderingHint2 = graphics2D.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString(str, i2, i3);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, renderingHint2);
    }

    private static RenderingHints getRenderingHints(Graphics2D graphics2D, Map map, RenderingHints renderingHints) {
        if (renderingHints == null) {
            renderingHints = new RenderingHints(null);
        } else {
            renderingHints.clear();
        }
        if (map == null || map.size() == 0) {
            return renderingHints;
        }
        for (RenderingHints.Key key : map.keySet()) {
            renderingHints.put(key, graphics2D.getRenderingHint(key));
        }
        return renderingHints;
    }

    public static String getSystemProperty(String str) {
        try {
            return System.getProperty(str);
        } catch (SecurityException e2) {
            System.out.println(new StringBuffer().append("Exception while trying to get ").append(str).append(" system property. ").append((Object) e2).toString());
            return null;
        }
    }

    static {
        is1dot4 = false;
        is1dot5 = false;
        is1dot6 = false;
        if (javaVersion != null) {
            is1dot4 = javaVersion.startsWith("1.0") || javaVersion.startsWith(SerializerConstants.XMLVERSION11) || javaVersion.startsWith("1.2") || javaVersion.startsWith("1.3") || javaVersion.startsWith(ServiceName.JMX_SPEC_VERSION);
            is1dot5 = javaVersion.startsWith("1.5");
            is1dot6 = javaVersion.startsWith("1.6");
        }
        osSettings = (Map) Toolkit.getDefaultToolkit().getDesktopProperty(SunToolkit.DESKTOPFONTHINTS);
        Toolkit.getDefaultToolkit().addPropertyChangeListener(SunToolkit.DESKTOPFONTHINTS, new PropertyChangeListener() { // from class: de.muntjak.tinylookandfeel.TinyUtils.1
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                Map unused = TinyUtils.osSettings = (Map) propertyChangeEvent.getNewValue();
            }
        });
    }
}
