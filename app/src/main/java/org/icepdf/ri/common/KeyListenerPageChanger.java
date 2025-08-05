package org.icepdf.ri.common;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.icepdf.ri.common.views.AbstractDocumentView;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/KeyListenerPageChanger.class */
public class KeyListenerPageChanger extends KeyAdapter {
    private SwingController controller;
    private JScrollPane scroll;
    private AbstractDocumentView documentView;
    private boolean changingPage = false;

    public static KeyListenerPageChanger install(SwingController c2, JScrollPane s2, AbstractDocumentView documentView) {
        KeyListenerPageChanger listener = null;
        if (c2 != null && s2 != null) {
            listener = new KeyListenerPageChanger(c2, s2, documentView);
            s2.addKeyListener(listener);
        }
        return listener;
    }

    public void uninstall() {
        if (this.scroll != null) {
            this.scroll.removeKeyListener(this);
        }
    }

    protected KeyListenerPageChanger(SwingController c2, JScrollPane s2, AbstractDocumentView documentView) {
        this.controller = c2;
        this.scroll = s2;
        this.documentView = documentView;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent e2) {
        if (this.changingPage) {
            return;
        }
        int deltaPage = 0;
        JScrollBar visibleVerticalScrollBar = (this.scroll.getVerticalScrollBar() == null || !this.scroll.getVerticalScrollBar().isVisible()) ? null : this.scroll.getVerticalScrollBar();
        int keyCode = e2.getKeyCode();
        if (keyCode == 34 || keyCode == 40) {
            if (visibleVerticalScrollBar != null) {
                int value = visibleVerticalScrollBar.getModel().getValue();
                int extent = visibleVerticalScrollBar.getModel().getExtent();
                int max = visibleVerticalScrollBar.getModel().getMaximum();
                if (value + extent >= max) {
                    deltaPage = this.documentView.getNextPageIncrement();
                }
            } else {
                deltaPage = this.documentView.getNextPageIncrement();
            }
        } else if (keyCode == 33 || keyCode == 38) {
            if (visibleVerticalScrollBar != null) {
                int value2 = visibleVerticalScrollBar.getModel().getValue();
                if (value2 <= 0) {
                    deltaPage = -this.documentView.getPreviousPageIncrement();
                }
            } else {
                deltaPage = -this.documentView.getPreviousPageIncrement();
            }
        } else if (keyCode == 36) {
            deltaPage = -this.controller.getCurrentPageNumber();
        } else if (keyCode == 35) {
            deltaPage = (this.controller.getDocument().getNumberOfPages() - this.controller.getCurrentPageNumber()) - 1;
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
        SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.KeyListenerPageChanger.1
            @Override // java.lang.Runnable
            public void run() {
                KeyListenerPageChanger.this.changingPage = false;
                KeyListenerPageChanger.this.controller.goToDeltaPage(dp);
            }
        });
    }
}
