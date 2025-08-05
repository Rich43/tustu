package javafx.stage;

import com.sun.javafx.stage.ScreenHelper;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.Toolkit;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;

/* loaded from: jfxrt.jar:javafx/stage/Screen.class */
public final class Screen {
    private static final ScreenConfigurationAccessor accessor;
    private static Screen primary;
    private Rectangle2D bounds = Rectangle2D.EMPTY;
    private Rectangle2D visualBounds = Rectangle2D.EMPTY;
    private double dpi;
    private float renderScale;
    private static final AtomicBoolean configurationDirty = new AtomicBoolean(true);
    private static final ObservableList<Screen> screens = FXCollections.observableArrayList();
    private static final ObservableList<Screen> unmodifiableScreens = FXCollections.unmodifiableObservableList(screens);

    static {
        ScreenHelper.setScreenAccessor(new ScreenHelper.ScreenAccessor() { // from class: javafx.stage.Screen.1
            @Override // com.sun.javafx.stage.ScreenHelper.ScreenAccessor
            public float getRenderScale(Screen screen) {
                return screen.getRenderScale();
            }
        });
        accessor = Toolkit.getToolkit().setScreenConfigurationListener(() -> {
            updateConfiguration();
        });
    }

    private Screen() {
    }

    private static void checkDirty() {
        if (configurationDirty.compareAndSet(true, false)) {
            updateConfiguration();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateConfiguration() {
        Object primaryScreen = Toolkit.getToolkit().getPrimaryScreen();
        Screen screenTmp = nativeToScreen(primaryScreen, primary);
        if (screenTmp != null) {
            primary = screenTmp;
        }
        List<?> screens2 = Toolkit.getToolkit().getScreens();
        ObservableList<Screen> newScreens = FXCollections.observableArrayList();
        boolean canKeepOld = screens.size() == screens2.size();
        for (int i2 = 0; i2 < screens2.size(); i2++) {
            Object obj = screens2.get(i2);
            Screen origScreen = null;
            if (canKeepOld) {
                origScreen = screens.get(i2);
            }
            Screen newScreen = nativeToScreen(obj, origScreen);
            if (newScreen != null) {
                if (canKeepOld) {
                    canKeepOld = false;
                    newScreens.setAll(screens.subList(0, i2));
                }
                newScreens.add(newScreen);
            }
        }
        if (!canKeepOld) {
            screens.setAll(newScreens);
        }
        configurationDirty.set(false);
    }

    private static Screen nativeToScreen(Object obj, Screen screen) {
        int minX = accessor.getMinX(obj);
        int minY = accessor.getMinY(obj);
        int width = accessor.getWidth(obj);
        int height = accessor.getHeight(obj);
        int visualMinX = accessor.getVisualMinX(obj);
        int visualMinY = accessor.getVisualMinY(obj);
        int visualWidth = accessor.getVisualWidth(obj);
        int visualHeight = accessor.getVisualHeight(obj);
        double dpi = accessor.getDPI(obj);
        float renderScale = accessor.getRenderScale(obj);
        if (screen == null || screen.bounds.getMinX() != minX || screen.bounds.getMinY() != minY || screen.bounds.getWidth() != width || screen.bounds.getHeight() != height || screen.visualBounds.getMinX() != visualMinX || screen.visualBounds.getMinY() != visualMinY || screen.visualBounds.getWidth() != visualWidth || screen.visualBounds.getHeight() != visualHeight || screen.dpi != dpi || screen.renderScale != renderScale) {
            Screen s2 = new Screen();
            s2.bounds = new Rectangle2D(minX, minY, width, height);
            s2.visualBounds = new Rectangle2D(visualMinX, visualMinY, visualWidth, visualHeight);
            s2.dpi = dpi;
            s2.renderScale = renderScale;
            return s2;
        }
        return null;
    }

    static Screen getScreenForNative(Object obj) {
        double x2 = accessor.getMinX(obj);
        double y2 = accessor.getMinY(obj);
        double w2 = accessor.getWidth(obj);
        double h2 = accessor.getHeight(obj);
        Screen intScr = null;
        for (int i2 = 0; i2 < screens.size(); i2++) {
            Screen scr = screens.get(i2);
            if (scr.bounds.contains(x2, y2, w2, h2)) {
                return scr;
            }
            if (intScr == null && scr.bounds.intersects(x2, y2, w2, h2)) {
                intScr = scr;
            }
        }
        return intScr == null ? getPrimary() : intScr;
    }

    public static Screen getPrimary() {
        checkDirty();
        return primary;
    }

    public static ObservableList<Screen> getScreens() {
        checkDirty();
        return unmodifiableScreens;
    }

    public static ObservableList<Screen> getScreensForRectangle(double x2, double y2, double width, double height) {
        checkDirty();
        ObservableList<Screen> results = FXCollections.observableArrayList();
        for (Screen screen : screens) {
            if (screen.bounds.intersects(x2, y2, width, height)) {
                results.add(screen);
            }
        }
        return results;
    }

    public static ObservableList<Screen> getScreensForRectangle(Rectangle2D r2) {
        checkDirty();
        return getScreensForRectangle(r2.getMinX(), r2.getMinY(), r2.getWidth(), r2.getHeight());
    }

    public final Rectangle2D getBounds() {
        return this.bounds;
    }

    public final Rectangle2D getVisualBounds() {
        return this.visualBounds;
    }

    public final double getDpi() {
        return this.dpi;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getRenderScale() {
        return this.renderScale;
    }

    public int hashCode() {
        long bits = (37 * ((37 * ((37 * ((37 * 7) + this.bounds.hashCode())) + this.visualBounds.hashCode())) + Double.doubleToLongBits(this.dpi))) + Float.floatToIntBits(this.renderScale);
        return (int) (bits ^ (bits >> 32));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Screen) {
            Screen other = (Screen) obj;
            if (this.bounds != null ? this.bounds.equals(other.bounds) : other.bounds == null) {
                if (this.visualBounds != null ? this.visualBounds.equals(other.visualBounds) : other.visualBounds == null) {
                    if (other.dpi == this.dpi && other.renderScale == this.renderScale) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public String toString() {
        return super.toString() + " bounds:" + ((Object) this.bounds) + " visualBounds:" + ((Object) this.visualBounds) + " dpi:" + this.dpi + " renderScale:" + this.renderScale;
    }
}
