package javax.management.remote;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.management.Notification;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:javax/management/remote/TargetedNotification.class */
public class TargetedNotification implements Serializable {
    private static final long serialVersionUID = 7676132089779300926L;
    private Notification notif;
    private Integer id;

    public TargetedNotification(Notification notification, Integer num) throws IllegalArgumentException {
        validate(notification, num);
        this.notif = notification;
        this.id = num;
    }

    public Notification getNotification() {
        return this.notif;
    }

    public Integer getListenerID() {
        return this.id;
    }

    public String toString() {
        return VectorFormat.DEFAULT_PREFIX + ((Object) this.notif) + ", " + ((Object) this.id) + "}";
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            validate(this.notif, this.id);
        } catch (IllegalArgumentException e2) {
            throw new InvalidObjectException(e2.getMessage());
        }
    }

    private static void validate(Notification notification, Integer num) throws IllegalArgumentException {
        if (notification == null) {
            throw new IllegalArgumentException("Invalid notification: null");
        }
        if (num == null) {
            throw new IllegalArgumentException("Invalid listener ID: null");
        }
    }
}
