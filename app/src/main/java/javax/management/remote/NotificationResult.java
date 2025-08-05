package javax.management.remote;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/* loaded from: rt.jar:javax/management/remote/NotificationResult.class */
public class NotificationResult implements Serializable {
    private static final long serialVersionUID = 1191800228721395279L;
    private long earliestSequenceNumber;
    private long nextSequenceNumber;
    private TargetedNotification[] targetedNotifications;

    public NotificationResult(long j2, long j3, TargetedNotification[] targetedNotificationArr) throws IllegalArgumentException {
        validate(targetedNotificationArr, j2, j3);
        this.earliestSequenceNumber = j2;
        this.nextSequenceNumber = j3;
        this.targetedNotifications = targetedNotificationArr.length == 0 ? targetedNotificationArr : (TargetedNotification[]) targetedNotificationArr.clone();
    }

    public long getEarliestSequenceNumber() {
        return this.earliestSequenceNumber;
    }

    public long getNextSequenceNumber() {
        return this.nextSequenceNumber;
    }

    public TargetedNotification[] getTargetedNotifications() {
        return this.targetedNotifications.length == 0 ? this.targetedNotifications : (TargetedNotification[]) this.targetedNotifications.clone();
    }

    public String toString() {
        return "NotificationResult: earliest=" + getEarliestSequenceNumber() + "; next=" + getNextSequenceNumber() + "; nnotifs=" + getTargetedNotifications().length;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            validate(this.targetedNotifications, this.earliestSequenceNumber, this.nextSequenceNumber);
            this.targetedNotifications = this.targetedNotifications.length == 0 ? this.targetedNotifications : (TargetedNotification[]) this.targetedNotifications.clone();
        } catch (IllegalArgumentException e2) {
            throw new InvalidObjectException(e2.getMessage());
        }
    }

    private static void validate(TargetedNotification[] targetedNotificationArr, long j2, long j3) throws IllegalArgumentException {
        if (targetedNotificationArr == null) {
            throw new IllegalArgumentException("Notifications null");
        }
        if (j2 < 0 || j3 < 0) {
            throw new IllegalArgumentException("Bad sequence numbers");
        }
    }
}
