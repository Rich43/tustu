package java.awt;

import java.awt.Dialog;
import java.awt.EventFilter;
import sun.awt.AppContext;
import sun.awt.ModalExclude;

/* loaded from: rt.jar:java/awt/ModalEventFilter.class */
abstract class ModalEventFilter implements EventFilter {
    protected Dialog modalDialog;
    protected boolean disabled = false;

    protected abstract EventFilter.FilterAction acceptWindow(Window window);

    protected ModalEventFilter(Dialog dialog) {
        this.modalDialog = dialog;
    }

    Dialog getModalDialog() {
        return this.modalDialog;
    }

    @Override // java.awt.EventFilter
    public EventFilter.FilterAction acceptEvent(AWTEvent aWTEvent) {
        Component component;
        if (this.disabled || !this.modalDialog.isVisible()) {
            return EventFilter.FilterAction.ACCEPT;
        }
        int id = aWTEvent.getID();
        if ((id >= 500 && id <= 507) || ((id >= 1001 && id <= 1001) || id == 201)) {
            Object source = aWTEvent.getSource();
            if (!(source instanceof ModalExclude) && (source instanceof Component)) {
                Component parent_NoClientCode = (Component) source;
                while (true) {
                    component = parent_NoClientCode;
                    if (component == null || (component instanceof Window)) {
                        break;
                    }
                    parent_NoClientCode = component.getParent_NoClientCode();
                }
                if (component != null) {
                    return acceptWindow((Window) component);
                }
            }
        }
        return EventFilter.FilterAction.ACCEPT;
    }

    void disable() {
        this.disabled = true;
    }

    int compareTo(ModalEventFilter modalEventFilter) {
        Dialog modalDialog = modalEventFilter.getModalDialog();
        Container parent_NoClientCode = this.modalDialog;
        while (true) {
            Dialog dialog = parent_NoClientCode;
            if (dialog != null) {
                if (dialog == modalDialog) {
                    return 1;
                }
                parent_NoClientCode = dialog.getParent_NoClientCode();
            } else {
                Container parent_NoClientCode2 = modalDialog;
                while (true) {
                    Container container = parent_NoClientCode2;
                    if (container != null) {
                        if (container == this.modalDialog) {
                            return -1;
                        }
                        parent_NoClientCode2 = container.getParent_NoClientCode();
                    } else {
                        Dialog modalBlocker = this.modalDialog.getModalBlocker();
                        while (true) {
                            Dialog dialog2 = modalBlocker;
                            if (dialog2 != null) {
                                if (dialog2 == modalDialog) {
                                    return -1;
                                }
                                modalBlocker = dialog2.getModalBlocker();
                            } else {
                                Dialog modalBlocker2 = modalDialog.getModalBlocker();
                                while (true) {
                                    Dialog dialog3 = modalBlocker2;
                                    if (dialog3 != null) {
                                        if (dialog3 == this.modalDialog) {
                                            return 1;
                                        }
                                        modalBlocker2 = dialog3.getModalBlocker();
                                    } else {
                                        return this.modalDialog.getModalityType().compareTo(modalDialog.getModalityType());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static ModalEventFilter createFilterForDialog(Dialog dialog) {
        switch (dialog.getModalityType()) {
            case DOCUMENT_MODAL:
                return new DocumentModalEventFilter(dialog);
            case APPLICATION_MODAL:
                return new ApplicationModalEventFilter(dialog);
            case TOOLKIT_MODAL:
                return new ToolkitModalEventFilter(dialog);
            default:
                return null;
        }
    }

    /* loaded from: rt.jar:java/awt/ModalEventFilter$ToolkitModalEventFilter.class */
    private static class ToolkitModalEventFilter extends ModalEventFilter {
        private AppContext appContext;

        ToolkitModalEventFilter(Dialog dialog) {
            super(dialog);
            this.appContext = dialog.appContext;
        }

        @Override // java.awt.ModalEventFilter
        protected EventFilter.FilterAction acceptWindow(Window window) {
            if (window.isModalExcluded(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE)) {
                return EventFilter.FilterAction.ACCEPT;
            }
            if (window.appContext != this.appContext) {
                return EventFilter.FilterAction.REJECT;
            }
            while (window != null) {
                if (window == this.modalDialog) {
                    return EventFilter.FilterAction.ACCEPT_IMMEDIATELY;
                }
                window = window.getOwner();
            }
            return EventFilter.FilterAction.REJECT;
        }
    }

    /* loaded from: rt.jar:java/awt/ModalEventFilter$ApplicationModalEventFilter.class */
    private static class ApplicationModalEventFilter extends ModalEventFilter {
        private AppContext appContext;

        ApplicationModalEventFilter(Dialog dialog) {
            super(dialog);
            this.appContext = dialog.appContext;
        }

        @Override // java.awt.ModalEventFilter
        protected EventFilter.FilterAction acceptWindow(Window window) {
            if (window.isModalExcluded(Dialog.ModalExclusionType.APPLICATION_EXCLUDE)) {
                return EventFilter.FilterAction.ACCEPT;
            }
            if (window.appContext == this.appContext) {
                while (window != null) {
                    if (window == this.modalDialog) {
                        return EventFilter.FilterAction.ACCEPT_IMMEDIATELY;
                    }
                    window = window.getOwner();
                }
                return EventFilter.FilterAction.REJECT;
            }
            return EventFilter.FilterAction.ACCEPT;
        }
    }

    /* loaded from: rt.jar:java/awt/ModalEventFilter$DocumentModalEventFilter.class */
    private static class DocumentModalEventFilter extends ModalEventFilter {
        private Window documentRoot;

        DocumentModalEventFilter(Dialog dialog) {
            super(dialog);
            this.documentRoot = dialog.getDocumentRoot();
        }

        @Override // java.awt.ModalEventFilter
        protected EventFilter.FilterAction acceptWindow(Window window) {
            if (window.isModalExcluded(Dialog.ModalExclusionType.APPLICATION_EXCLUDE)) {
                Window owner = this.modalDialog.getOwner();
                while (true) {
                    Window window2 = owner;
                    if (window2 != null) {
                        if (window2 == window) {
                            return EventFilter.FilterAction.REJECT;
                        }
                        owner = window2.getOwner();
                    } else {
                        return EventFilter.FilterAction.ACCEPT;
                    }
                }
            } else {
                while (window != null) {
                    if (window == this.modalDialog) {
                        return EventFilter.FilterAction.ACCEPT_IMMEDIATELY;
                    }
                    if (window == this.documentRoot) {
                        return EventFilter.FilterAction.REJECT;
                    }
                    window = window.getOwner();
                }
                return EventFilter.FilterAction.ACCEPT;
            }
        }
    }
}
