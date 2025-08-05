package javafx.application;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.Toolkit;
import java.util.Optional;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.input.KeyCode;

/* loaded from: jfxrt.jar:javafx/application/Platform.class */
public final class Platform {
    private static ReadOnlyBooleanWrapper accessibilityActiveProperty;

    private Platform() {
    }

    public static void runLater(Runnable runnable) {
        PlatformImpl.runLater(runnable);
    }

    public static boolean isFxApplicationThread() {
        return PlatformImpl.isFxApplicationThread();
    }

    public static void exit() {
        PlatformImpl.exit();
    }

    public static void setImplicitExit(boolean implicitExit) {
        PlatformImpl.setImplicitExit(implicitExit);
    }

    public static boolean isImplicitExit() {
        return PlatformImpl.isImplicitExit();
    }

    public static boolean isSupported(ConditionalFeature feature) {
        return PlatformImpl.isSupported(feature);
    }

    public static Optional<Boolean> isKeyLocked(KeyCode keyCode) {
        Toolkit.getToolkit().checkFxUserThread();
        switch (keyCode) {
            case CAPS:
            case NUM_LOCK:
                return Toolkit.getToolkit().isKeyLocked(keyCode);
            default:
                throw new IllegalArgumentException("Invalid KeyCode");
        }
    }

    public static boolean isAccessibilityActive() {
        if (accessibilityActiveProperty == null) {
            return false;
        }
        return accessibilityActiveProperty.get();
    }

    public static ReadOnlyBooleanProperty accessibilityActiveProperty() {
        if (accessibilityActiveProperty == null) {
            accessibilityActiveProperty = new ReadOnlyBooleanWrapper(Platform.class, "accessibilityActive");
            accessibilityActiveProperty.bind(PlatformImpl.accessibilityActiveProperty());
        }
        return accessibilityActiveProperty.getReadOnlyProperty();
    }
}
