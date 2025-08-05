package javafx.application;

import java.security.AccessController;

/* loaded from: jfxrt.jar:javafx/application/Preloader.class */
public abstract class Preloader extends Application {
    private static final String lineSeparator;

    /* loaded from: jfxrt.jar:javafx/application/Preloader$PreloaderNotification.class */
    public interface PreloaderNotification {
    }

    static {
        String prop = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("line.separator");
        });
        lineSeparator = prop != null ? prop : "\n";
    }

    public void handleProgressNotification(ProgressNotification info) {
    }

    public void handleStateChangeNotification(StateChangeNotification info) {
    }

    public void handleApplicationNotification(PreloaderNotification info) {
    }

    public boolean handleErrorNotification(ErrorNotification info) {
        return false;
    }

    /* loaded from: jfxrt.jar:javafx/application/Preloader$ErrorNotification.class */
    public static class ErrorNotification implements PreloaderNotification {
        private String location;
        private String details;
        private Throwable cause;

        public ErrorNotification(String location, String details, Throwable cause) {
            this.details = "";
            if (details == null) {
                throw new NullPointerException();
            }
            this.location = location;
            this.details = details;
            this.cause = cause;
        }

        public String getLocation() {
            return this.location;
        }

        public String getDetails() {
            return this.details;
        }

        public Throwable getCause() {
            return this.cause;
        }

        public String toString() {
            StringBuilder str = new StringBuilder("Preloader.ErrorNotification: ");
            str.append(this.details);
            if (this.cause != null) {
                str.append(Preloader.lineSeparator).append("Caused by: ").append(this.cause.toString());
            }
            if (this.location != null) {
                str.append(Preloader.lineSeparator).append("Location: ").append(this.location);
            }
            return str.toString();
        }
    }

    /* loaded from: jfxrt.jar:javafx/application/Preloader$ProgressNotification.class */
    public static class ProgressNotification implements PreloaderNotification {
        private final double progress;
        private final String details;

        public ProgressNotification(double progress) {
            this(progress, "");
        }

        private ProgressNotification(double progress, String details) {
            this.progress = progress;
            this.details = details;
        }

        public double getProgress() {
            return this.progress;
        }

        private String getDetails() {
            return this.details;
        }
    }

    /* loaded from: jfxrt.jar:javafx/application/Preloader$StateChangeNotification.class */
    public static class StateChangeNotification implements PreloaderNotification {
        private final Type notificationType;
        private final Application application;

        /* loaded from: jfxrt.jar:javafx/application/Preloader$StateChangeNotification$Type.class */
        public enum Type {
            BEFORE_LOAD,
            BEFORE_INIT,
            BEFORE_START
        }

        public StateChangeNotification(Type notificationType) {
            this.notificationType = notificationType;
            this.application = null;
        }

        public StateChangeNotification(Type notificationType, Application application) {
            this.notificationType = notificationType;
            this.application = application;
        }

        public Type getType() {
            return this.notificationType;
        }

        public Application getApplication() {
            return this.application;
        }
    }
}
