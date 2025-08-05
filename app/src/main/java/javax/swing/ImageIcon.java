package javax.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.beans.ConstructorProperties;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/ImageIcon.class */
public class ImageIcon implements Icon, Serializable, Accessible {
    private transient String filename;
    private transient URL location;
    transient Image image;
    transient int loadStatus;
    ImageObserver imageObserver;
    String description;
    private static int mediaTrackerID;
    int width;
    int height;
    private AccessibleImageIcon accessibleContext;

    @Deprecated
    protected static final Component component = (Component) AccessController.doPrivileged(new PrivilegedAction<Component>() { // from class: javax.swing.ImageIcon.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Component run() {
            try {
                Component componentCreateNoPermsComponent = ImageIcon.createNoPermsComponent();
                Field declaredField = Component.class.getDeclaredField("appContext");
                declaredField.setAccessible(true);
                declaredField.set(componentCreateNoPermsComponent, null);
                return componentCreateNoPermsComponent;
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }
    });

    @Deprecated
    protected static final MediaTracker tracker = new MediaTracker(component);
    private static final Object TRACKER_KEY = new StringBuilder("TRACKER_KEY");

    /* JADX INFO: Access modifiers changed from: private */
    public static Component createNoPermsComponent() {
        return (Component) AccessController.doPrivileged(new PrivilegedAction<Component>() { // from class: javax.swing.ImageIcon.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Component run() {
                return new Component() { // from class: javax.swing.ImageIcon.2.1
                };
            }
        }, new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, null)}));
    }

    public ImageIcon(String str, String str2) {
        this.loadStatus = 0;
        this.description = null;
        this.width = -1;
        this.height = -1;
        this.accessibleContext = null;
        this.image = Toolkit.getDefaultToolkit().getImage(str);
        if (this.image == null) {
            return;
        }
        this.filename = str;
        this.description = str2;
        loadImage(this.image);
    }

    @ConstructorProperties({"description"})
    public ImageIcon(String str) {
        this(str, str);
    }

    public ImageIcon(URL url, String str) {
        this.loadStatus = 0;
        this.description = null;
        this.width = -1;
        this.height = -1;
        this.accessibleContext = null;
        this.image = Toolkit.getDefaultToolkit().getImage(url);
        if (this.image == null) {
            return;
        }
        this.location = url;
        this.description = str;
        loadImage(this.image);
    }

    public ImageIcon(URL url) {
        this(url, url.toExternalForm());
    }

    public ImageIcon(Image image, String str) {
        this(image);
        this.description = str;
    }

    public ImageIcon(Image image) {
        this.loadStatus = 0;
        this.description = null;
        this.width = -1;
        this.height = -1;
        this.accessibleContext = null;
        this.image = image;
        Object property = image.getProperty("comment", this.imageObserver);
        if (property instanceof String) {
            this.description = (String) property;
        }
        loadImage(image);
    }

    public ImageIcon(byte[] bArr, String str) {
        this.loadStatus = 0;
        this.description = null;
        this.width = -1;
        this.height = -1;
        this.accessibleContext = null;
        this.image = Toolkit.getDefaultToolkit().createImage(bArr);
        if (this.image == null) {
            return;
        }
        this.description = str;
        loadImage(this.image);
    }

    public ImageIcon(byte[] bArr) {
        this.loadStatus = 0;
        this.description = null;
        this.width = -1;
        this.height = -1;
        this.accessibleContext = null;
        this.image = Toolkit.getDefaultToolkit().createImage(bArr);
        if (this.image == null) {
            return;
        }
        Object property = this.image.getProperty("comment", this.imageObserver);
        if (property instanceof String) {
            this.description = (String) property;
        }
        loadImage(this.image);
    }

    public ImageIcon() {
        this.loadStatus = 0;
        this.description = null;
        this.width = -1;
        this.height = -1;
        this.accessibleContext = null;
    }

    protected void loadImage(Image image) {
        MediaTracker tracker2 = getTracker();
        synchronized (tracker2) {
            int nextID = getNextID();
            tracker2.addImage(image, nextID);
            try {
                tracker2.waitForID(nextID, 0L);
            } catch (InterruptedException e2) {
                System.out.println("INTERRUPTED while loading Image");
            }
            this.loadStatus = tracker2.statusID(nextID, false);
            tracker2.removeImage(image, nextID);
            this.width = image.getWidth(this.imageObserver);
            this.height = image.getHeight(this.imageObserver);
        }
    }

    private int getNextID() {
        int i2;
        synchronized (getTracker()) {
            i2 = mediaTrackerID + 1;
            mediaTrackerID = i2;
        }
        return i2;
    }

    private MediaTracker getTracker() {
        Object mediaTracker;
        AppContext appContext = AppContext.getAppContext();
        synchronized (appContext) {
            mediaTracker = appContext.get(TRACKER_KEY);
            if (mediaTracker == null) {
                mediaTracker = new MediaTracker(new Component() { // from class: javax.swing.ImageIcon.3
                });
                appContext.put(TRACKER_KEY, mediaTracker);
            }
        }
        return (MediaTracker) mediaTracker;
    }

    public int getImageLoadStatus() {
        return this.loadStatus;
    }

    @Transient
    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
        loadImage(image);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    @Override // javax.swing.Icon
    public synchronized void paintIcon(Component component2, Graphics graphics, int i2, int i3) {
        if (this.imageObserver == null) {
            graphics.drawImage(this.image, i2, i3, component2);
        } else {
            graphics.drawImage(this.image, i2, i3, this.imageObserver);
        }
    }

    @Override // javax.swing.Icon
    public int getIconWidth() {
        return this.width;
    }

    @Override // javax.swing.Icon
    public int getIconHeight() {
        return this.height;
    }

    public void setImageObserver(ImageObserver imageObserver) {
        this.imageObserver = imageObserver;
    }

    @Transient
    public ImageObserver getImageObserver() {
        return this.imageObserver;
    }

    public String toString() {
        if (this.description != null) {
            return this.description;
        }
        return super.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int i2 = objectInputStream.readInt();
        int i3 = objectInputStream.readInt();
        int[] iArr = (int[]) objectInputStream.readObject();
        if (iArr != null) {
            this.image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(i2, i3, ColorModel.getRGBdefault(), iArr, 0, i2));
            loadImage(this.image);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        int iconWidth = getIconWidth();
        int iconHeight = getIconHeight();
        int[] iArr = this.image != null ? new int[iconWidth * iconHeight] : null;
        if (this.image != null) {
            try {
                PixelGrabber pixelGrabber = new PixelGrabber(this.image, 0, 0, iconWidth, iconHeight, iArr, 0, iconWidth);
                pixelGrabber.grabPixels();
                if ((pixelGrabber.getStatus() & 128) != 0) {
                    throw new IOException("failed to load image contents");
                }
            } catch (InterruptedException e2) {
                throw new IOException("image load interrupted");
            }
        }
        objectOutputStream.writeInt(iconWidth);
        objectOutputStream.writeInt(iconHeight);
        objectOutputStream.writeObject(iArr);
    }

    @Override // javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleImageIcon();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/ImageIcon$AccessibleImageIcon.class */
    protected class AccessibleImageIcon extends AccessibleContext implements AccessibleIcon, Serializable {
        protected AccessibleImageIcon() {
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.ICON;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleParent() {
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleIndexInParent() {
            return -1;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public Locale getLocale() throws IllegalComponentStateException {
            return null;
        }

        @Override // javax.accessibility.AccessibleIcon
        public String getAccessibleIconDescription() {
            return ImageIcon.this.getDescription();
        }

        @Override // javax.accessibility.AccessibleIcon
        public void setAccessibleIconDescription(String str) {
            ImageIcon.this.setDescription(str);
        }

        @Override // javax.accessibility.AccessibleIcon
        public int getAccessibleIconHeight() {
            return ImageIcon.this.height;
        }

        @Override // javax.accessibility.AccessibleIcon
        public int getAccessibleIconWidth() {
            return ImageIcon.this.width;
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
        }
    }
}
