package javafx.application;

import com.sun.javafx.application.LauncherImpl;
import com.sun.javafx.application.ParametersImpl;
import com.sun.javafx.application.PlatformImpl;
import java.util.List;
import java.util.Map;
import javafx.application.Preloader;
import javafx.stage.Stage;

/* loaded from: jfxrt.jar:javafx/application/Application.class */
public abstract class Application {
    public static final String STYLESHEET_CASPIAN = "CASPIAN";
    public static final String STYLESHEET_MODENA = "MODENA";
    private HostServices hostServices = null;
    private static String userAgentStylesheet = null;

    /* loaded from: jfxrt.jar:javafx/application/Application$Parameters.class */
    public static abstract class Parameters {
        public abstract List<String> getRaw();

        public abstract List<String> getUnnamed();

        public abstract Map<String, String> getNamed();
    }

    public abstract void start(Stage stage) throws Exception;

    public static void launch(Class<? extends Application> appClass, String... args) {
        LauncherImpl.launchApplication(appClass, args);
    }

    public static void launch(String... args) {
        StackTraceElement[] cause = Thread.currentThread().getStackTrace();
        boolean foundThisMethod = false;
        String callingClassName = null;
        int length = cause.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            StackTraceElement se = cause[i2];
            String className = se.getClassName();
            String methodName = se.getMethodName();
            if (foundThisMethod) {
                callingClassName = className;
                break;
            }
            if (Application.class.getName().equals(className) && "launch".equals(methodName)) {
                foundThisMethod = true;
            }
            i2++;
        }
        if (callingClassName == null) {
            throw new RuntimeException("Error: unable to determine Application class");
        }
        try {
            Class theClass = Class.forName(callingClassName, false, Thread.currentThread().getContextClassLoader());
            if (Application.class.isAssignableFrom(theClass)) {
                LauncherImpl.launchApplication(theClass, args);
                return;
            }
            throw new RuntimeException("Error: " + ((Object) theClass) + " is not a subclass of javafx.application.Application");
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw new RuntimeException(ex2);
        }
    }

    public void init() throws Exception {
    }

    public void stop() throws Exception {
    }

    public final HostServices getHostServices() {
        HostServices hostServices;
        synchronized (this) {
            if (this.hostServices == null) {
                this.hostServices = new HostServices(this);
            }
            hostServices = this.hostServices;
        }
        return hostServices;
    }

    public final Parameters getParameters() {
        return ParametersImpl.getParameters(this);
    }

    public final void notifyPreloader(Preloader.PreloaderNotification info) {
        LauncherImpl.notifyPreloader(this, info);
    }

    public static String getUserAgentStylesheet() {
        return userAgentStylesheet;
    }

    public static void setUserAgentStylesheet(String url) {
        userAgentStylesheet = url;
        if (url == null) {
            PlatformImpl.setDefaultPlatformUserAgentStylesheet();
        } else {
            PlatformImpl.setPlatformUserAgentStylesheet(url);
        }
    }
}
