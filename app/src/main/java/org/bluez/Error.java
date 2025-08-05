package org.bluez;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.exceptions.DBusExecutionException;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error.class */
public interface Error extends DBusInterface {

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$DeviceUnreachable.class */
    public static class DeviceUnreachable extends DBusExecutionException {
        public DeviceUnreachable(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$AlreadyConnected.class */
    public static class AlreadyConnected extends DBusExecutionException {
        public AlreadyConnected(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$ConnectionAttemptFailed.class */
    public static class ConnectionAttemptFailed extends DBusExecutionException {
        public ConnectionAttemptFailed(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$Failed.class */
    public static class Failed extends DBusExecutionException {
        public Failed(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$InvalidArguments.class */
    public static class InvalidArguments extends DBusExecutionException {
        public InvalidArguments(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$NotAuthorized.class */
    public static class NotAuthorized extends DBusExecutionException {
        public NotAuthorized(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$OutOfMemory.class */
    public static class OutOfMemory extends DBusExecutionException {
        public OutOfMemory(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$NoSuchAdapter.class */
    public static class NoSuchAdapter extends DBusExecutionException {
        public NoSuchAdapter(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$NotReady.class */
    public static class NotReady extends DBusExecutionException {
        public NotReady(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$UnknwownMethod.class */
    public static class UnknwownMethod extends DBusExecutionException {
        public UnknwownMethod(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$NotAvailable.class */
    public static class NotAvailable extends DBusExecutionException {
        public NotAvailable(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$NotConnected.class */
    public static class NotConnected extends DBusExecutionException {
        public NotConnected(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$AlreadyExists.class */
    public static class AlreadyExists extends DBusExecutionException {
        public AlreadyExists(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$DoesNotExist.class */
    public static class DoesNotExist extends DBusExecutionException {
        public DoesNotExist(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$InProgress.class */
    public static class InProgress extends DBusExecutionException {
        public InProgress(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$Rejected.class */
    public static class Rejected extends DBusExecutionException {
        public Rejected(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$Canceled.class */
    public static class Canceled extends DBusExecutionException {
        public Canceled(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$NotSupported.class */
    public static class NotSupported extends DBusExecutionException {
        public NotSupported(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$NoSuchService.class */
    public static class NoSuchService extends DBusExecutionException {
        public NoSuchService(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$RequestDeferred.class */
    public static class RequestDeferred extends DBusExecutionException {
        public RequestDeferred(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$NotInProgress.class */
    public static class NotInProgress extends DBusExecutionException {
        public NotInProgress(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$UnsupportedMajorClass.class */
    public static class UnsupportedMajorClass extends DBusExecutionException {
        public UnsupportedMajorClass(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$AuthenticationFailed.class */
    public static class AuthenticationFailed extends DBusExecutionException {
        public AuthenticationFailed(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$AuthenticationTimeout.class */
    public static class AuthenticationTimeout extends DBusExecutionException {
        public AuthenticationTimeout(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$AuthenticationRejected.class */
    public static class AuthenticationRejected extends DBusExecutionException {
        public AuthenticationRejected(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$AuthenticationCanceled.class */
    public static class AuthenticationCanceled extends DBusExecutionException {
        public AuthenticationCanceled(String message) {
            super(message);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/Error$RepeatedAttempts.class */
    public static class RepeatedAttempts extends DBusExecutionException {
        public RepeatedAttempts(String message) {
            super(message);
        }
    }
}
