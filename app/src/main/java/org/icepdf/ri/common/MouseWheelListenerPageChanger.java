package org.icepdf.ri.common;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.icepdf.ri.common.tools.DynamicZoomHandler;
import org.icepdf.ri.common.views.AbstractDocumentView;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/MouseWheelListenerPageChanger.class */
public class MouseWheelListenerPageChanger implements MouseWheelListener {
    private SwingController controller;
    private JScrollPane scrollpane;
    private AbstractDocumentView documentView;
    private boolean changingPage = false;

    public static Object install(SwingController c2, JScrollPane s2, AbstractDocumentView documentView) {
        MouseWheelListenerPageChanger listener = null;
        if (c2 != null && s2 != null) {
            listener = new MouseWheelListenerPageChanger(c2, s2, documentView);
            s2.addMouseWheelListener(listener);
        }
        return listener;
    }

    protected MouseWheelListenerPageChanger(SwingController c2, JScrollPane s2, AbstractDocumentView documentView) {
        this.controller = c2;
        this.scrollpane = s2;
        this.documentView = documentView;
    }

    public static void uninstall(JScrollPane scrollpane, Object listener) {
        if (scrollpane != null && listener != null && (listener instanceof MouseWheelListenerPageChanger)) {
            scrollpane.removeMouseWheelListener((MouseWheelListenerPageChanger) listener);
        }
    }

    @Override // java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent e2) {
        if (this.changingPage) {
            return;
        }
        int deltaPage = 0;
        JScrollBar visibleVerticalScrollBar = (this.scrollpane.getVerticalScrollBar() == null || !this.scrollpane.getVerticalScrollBar().isVisible()) ? null : this.scrollpane.getVerticalScrollBar();
        if ((e2.getModifiers() & 2) != 2 && !(this.documentView.getCurrentToolHandler() instanceof DynamicZoomHandler)) {
            int amount = e2.getScrollAmount();
            int rotation = e2.getWheelRotation();
            if (amount > 0 && rotation > 0) {
                if (visibleVerticalScrollBar != null) {
                    int value = visibleVerticalScrollBar.getModel().getValue();
                    int extent = visibleVerticalScrollBar.getModel().getExtent();
                    int max = visibleVerticalScrollBar.getModel().getMaximum();
                    if (value + extent >= max) {
                        deltaPage = this.documentView.getPreviousPageIncrement();
                    }
                } else {
                    deltaPage = this.documentView.getPreviousPageIncrement();
                }
            } else if (amount > 0 && rotation < 0) {
                if (visibleVerticalScrollBar != null) {
                    int value2 = visibleVerticalScrollBar.getModel().getValue();
                    if (value2 <= 0) {
                        deltaPage = -this.documentView.getPreviousPageIncrement();
                    }
                } else {
                    deltaPage = -this.documentView.getPreviousPageIncrement();
                }
            }
        }
        if (deltaPage == 0) {
            return;
        }
        int newPage = this.controller.getCurrentPageNumber() + deltaPage;
        if (this.controller.getDocument() == null) {
            return;
        }
        if (newPage < 0) {
            deltaPage = -this.controller.getCurrentPageNumber();
        }
        if (newPage >= this.controller.getDocument().getNumberOfPages()) {
            deltaPage = (this.controller.getDocument().getNumberOfPages() - this.controller.getCurrentPageNumber()) - 1;
        }
        if (deltaPage == 0) {
            return;
        }
        this.changingPage = true;
        final int dp = deltaPage;
        SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.MouseWheelListenerPageChanger.1
            @Override // java.lang.Runnable
            public void run() {
                MouseWheelListenerPageChanger.this.changingPage = false;
                MouseWheelListenerPageChanger.this.controller.goToDeltaPage(dp);
            }
        });
    }
}
