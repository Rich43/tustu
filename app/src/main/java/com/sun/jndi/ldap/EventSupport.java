package com.sun.jndi.ldap;

import java.util.EventObject;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.NamingListener;
import javax.naming.event.ObjectChangeListener;
import javax.naming.ldap.UnsolicitedNotification;
import javax.naming.ldap.UnsolicitedNotificationEvent;
import javax.naming.ldap.UnsolicitedNotificationListener;

/* loaded from: rt.jar:com/sun/jndi/ldap/EventSupport.class */
final class EventSupport {
    private static final boolean debug = false;
    private LdapCtx ctx;
    private Hashtable<NotifierArgs, NamingEventNotifier> notifiers = new Hashtable<>(11);
    private Vector<UnsolicitedNotificationListener> unsolicited = null;
    private EventQueue eventQueue;

    EventSupport(LdapCtx ldapCtx) {
        this.ctx = ldapCtx;
    }

    synchronized void addNamingListener(String str, int i2, NamingListener namingListener) throws NamingException {
        if ((namingListener instanceof ObjectChangeListener) || (namingListener instanceof NamespaceChangeListener)) {
            NotifierArgs notifierArgs = new NotifierArgs(str, i2, namingListener);
            NamingEventNotifier namingEventNotifier = this.notifiers.get(notifierArgs);
            if (namingEventNotifier == null) {
                this.notifiers.put(notifierArgs, new NamingEventNotifier(this, this.ctx, notifierArgs, namingListener));
            } else {
                namingEventNotifier.addNamingListener(namingListener);
            }
        }
        if (namingListener instanceof UnsolicitedNotificationListener) {
            if (this.unsolicited == null) {
                this.unsolicited = new Vector<>(3);
            }
            this.unsolicited.addElement((UnsolicitedNotificationListener) namingListener);
        }
    }

    synchronized void addNamingListener(String str, String str2, SearchControls searchControls, NamingListener namingListener) throws NamingException {
        if ((namingListener instanceof ObjectChangeListener) || (namingListener instanceof NamespaceChangeListener)) {
            NotifierArgs notifierArgs = new NotifierArgs(str, str2, searchControls, namingListener);
            NamingEventNotifier namingEventNotifier = this.notifiers.get(notifierArgs);
            if (namingEventNotifier == null) {
                this.notifiers.put(notifierArgs, new NamingEventNotifier(this, this.ctx, notifierArgs, namingListener));
            } else {
                namingEventNotifier.addNamingListener(namingListener);
            }
        }
        if (namingListener instanceof UnsolicitedNotificationListener) {
            if (this.unsolicited == null) {
                this.unsolicited = new Vector<>(3);
            }
            this.unsolicited.addElement((UnsolicitedNotificationListener) namingListener);
        }
    }

    synchronized void removeNamingListener(NamingListener namingListener) {
        Iterator<NamingEventNotifier> it = this.notifiers.values().iterator();
        while (it.hasNext()) {
            NamingEventNotifier next = it.next();
            if (next != null) {
                next.removeNamingListener(namingListener);
                if (!next.hasNamingListeners()) {
                    next.stop();
                    it.remove();
                }
            }
        }
        if (this.unsolicited != null) {
            this.unsolicited.removeElement(namingListener);
        }
    }

    synchronized boolean hasUnsolicited() {
        return this.unsolicited != null && this.unsolicited.size() > 0;
    }

    synchronized void removeDeadNotifier(NotifierArgs notifierArgs) {
        this.notifiers.remove(notifierArgs);
    }

    synchronized void fireUnsolicited(Object obj) {
        if (this.unsolicited == null || this.unsolicited.size() == 0) {
            return;
        }
        if (obj instanceof UnsolicitedNotification) {
            queueEvent(new UnsolicitedNotificationEvent(this.ctx, (UnsolicitedNotification) obj), this.unsolicited);
        } else if (obj instanceof NamingException) {
            queueEvent(new NamingExceptionEvent(this.ctx, (NamingException) obj), this.unsolicited);
            this.unsolicited = null;
        }
    }

    synchronized void cleanup() {
        if (this.notifiers != null) {
            Iterator<NamingEventNotifier> it = this.notifiers.values().iterator();
            while (it.hasNext()) {
                it.next().stop();
            }
            this.notifiers = null;
        }
        if (this.eventQueue != null) {
            this.eventQueue.stop();
            this.eventQueue = null;
        }
    }

    synchronized void queueEvent(EventObject eventObject, Vector<? extends NamingListener> vector) {
        if (this.eventQueue == null) {
            this.eventQueue = new EventQueue();
        }
        this.eventQueue.enqueue(eventObject, (Vector) vector.clone());
    }
}
