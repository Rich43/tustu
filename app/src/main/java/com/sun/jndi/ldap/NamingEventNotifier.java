package com.sun.jndi.ldap;

import com.sun.jndi.toolkit.ctx.Continuation;
import java.io.IOException;
import java.util.Vector;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.InterruptedNamingException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import javax.naming.event.EventContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.NamingListener;
import javax.naming.ldap.Control;
import javax.naming.ldap.HasControls;

/* loaded from: rt.jar:com/sun/jndi/ldap/NamingEventNotifier.class */
final class NamingEventNotifier implements Runnable {
    private static final boolean debug = false;
    private Vector<NamingListener> namingListeners;
    private Thread worker;
    private LdapCtx context;
    private EventContext eventSrc;
    private EventSupport support;
    private NamingEnumeration<SearchResult> results;
    NotifierArgs info;

    NamingEventNotifier(EventSupport eventSupport, LdapCtx ldapCtx, NotifierArgs notifierArgs, NamingListener namingListener) throws NamingException {
        this.info = notifierArgs;
        this.support = eventSupport;
        try {
            this.context = (LdapCtx) ldapCtx.newInstance(new Control[]{new PersistentSearchControl(notifierArgs.mask, true, true, true)});
            this.eventSrc = ldapCtx;
            this.namingListeners = new Vector<>();
            this.namingListeners.addElement(namingListener);
            this.worker = Obj.helper.createThread(this);
            this.worker.setDaemon(true);
            this.worker.start();
        } catch (IOException e2) {
            NamingException namingException = new NamingException("Problem creating persistent search control");
            namingException.setRootCause(e2);
            throw namingException;
        }
    }

    void addNamingListener(NamingListener namingListener) {
        this.namingListeners.addElement(namingListener);
    }

    void removeNamingListener(NamingListener namingListener) {
        this.namingListeners.removeElement(namingListener);
    }

    boolean hasNamingListeners() {
        return this.namingListeners.size() > 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                Continuation continuation = new Continuation();
                continuation.setError(this, this.info.name);
                this.results = this.context.searchAux((this.info.name == null || this.info.name.equals("")) ? new CompositeName() : new CompositeName().add(this.info.name), this.info.filter, this.info.controls, true, false, continuation);
                ((LdapSearchEnumeration) this.results).setStartName(this.context.currentParsedDN);
                while (this.results.hasMore()) {
                    SearchResult next = this.results.next();
                    Control[] controls = next instanceof HasControls ? ((HasControls) next).getControls() : null;
                    if (controls != null) {
                        if (0 < controls.length && (controls[0] instanceof EntryChangeResponseControl)) {
                            EntryChangeResponseControl entryChangeResponseControl = (EntryChangeResponseControl) controls[0];
                            long changeNumber = entryChangeResponseControl.getChangeNumber();
                            switch (entryChangeResponseControl.getChangeType()) {
                                case 1:
                                    fireObjectAdded(next, changeNumber);
                                    break;
                                case 2:
                                    fireObjectRemoved(next, changeNumber);
                                    break;
                                case 4:
                                    fireObjectChanged(next, changeNumber);
                                    break;
                                case 8:
                                    fireObjectRenamed(next, entryChangeResponseControl.getPreviousDN(), changeNumber);
                                    break;
                            }
                        }
                    }
                }
                cleanup();
            } catch (InterruptedNamingException e2) {
                cleanup();
            } catch (NamingException e3) {
                fireNamingException(e3);
                this.support.removeDeadNotifier(this.info);
                cleanup();
            }
        } catch (Throwable th) {
            cleanup();
            throw th;
        }
    }

    private void cleanup() {
        try {
            if (this.results != null) {
                this.results.close();
                this.results = null;
            }
            if (this.context != null) {
                this.context.close();
                this.context = null;
            }
        } catch (NamingException e2) {
        }
    }

    void stop() {
        if (this.worker != null) {
            this.worker.interrupt();
            this.worker = null;
        }
    }

    private void fireObjectAdded(Binding binding, long j2) {
        if (this.namingListeners == null || this.namingListeners.size() == 0) {
            return;
        }
        this.support.queueEvent(new NamingEvent(this.eventSrc, 0, binding, null, new Long(j2)), this.namingListeners);
    }

    private void fireObjectRemoved(Binding binding, long j2) {
        if (this.namingListeners == null || this.namingListeners.size() == 0) {
            return;
        }
        this.support.queueEvent(new NamingEvent(this.eventSrc, 1, null, binding, new Long(j2)), this.namingListeners);
    }

    private void fireObjectChanged(Binding binding, long j2) {
        if (this.namingListeners == null || this.namingListeners.size() == 0) {
            return;
        }
        this.support.queueEvent(new NamingEvent(this.eventSrc, 3, binding, new Binding(binding.getName(), (Object) null, binding.isRelative()), new Long(j2)), this.namingListeners);
    }

    private void fireObjectRenamed(Binding binding, String str, long j2) {
        if (this.namingListeners == null || this.namingListeners.size() == 0) {
            return;
        }
        Binding binding2 = null;
        try {
            javax.naming.ldap.LdapName ldapName = new javax.naming.ldap.LdapName(str);
            if (ldapName.startsWith(this.context.currentParsedDN)) {
                binding2 = new Binding(ldapName.getSuffix(this.context.currentParsedDN.size()).toString(), null);
            }
        } catch (NamingException e2) {
        }
        if (binding2 == null) {
            binding2 = new Binding(str, (Object) null, false);
        }
        this.support.queueEvent(new NamingEvent(this.eventSrc, 2, binding, binding2, new Long(j2)), this.namingListeners);
    }

    private void fireNamingException(NamingException namingException) {
        if (this.namingListeners == null || this.namingListeners.size() == 0) {
            return;
        }
        this.support.queueEvent(new NamingExceptionEvent(this.eventSrc, namingException), this.namingListeners);
    }
}
