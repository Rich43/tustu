package org.icepdf.ri.common;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.icepdf.ri.common.views.AbstractDocumentView;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/KeyListenerPageColumnChanger.class */
public class KeyListenerPageColumnChanger extends KeyAdapter {
    private SwingController controller;
    private JScrollPane scroll;
    private AbstractDocumentView documentView;
    private CurrentPageChanger currentPageChanger;
    private boolean changingPage = false;

    public static KeyListenerPageColumnChanger install(SwingController c2, JScrollPane s2, AbstractDocumentView documentView, CurrentPageChanger currentPageChanger) {
        KeyListenerPageColumnChanger listener = null;
        if (c2 != null && s2 != null) {
            listener = new KeyListenerPageColumnChanger(c2, s2, documentView, currentPageChanger);
            s2.addKeyListener(listener);
        }
        return listener;
    }

    public void uninstall() {
        if (this.scroll != null) {
            this.scroll.removeKeyListener(this);
        }
    }

    protected KeyListenerPageColumnChanger(SwingController c2, JScrollPane s2, AbstractDocumentView documentView, CurrentPageChanger currentPageChanger) {
        this.controller = c2;
        this.scroll = s2;
        this.documentView = documentView;
        this.currentPageChanger = currentPageChanger;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent e2) {
        if (this.changingPage) {
            return;
        }
        int deltaPage = 0;
        int keyCode = e2.getKeyCode();
        if (keyCode == 34) {
            deltaPage = this.documentView.getPreviousPageIncrement();
        } else if (keyCode == 33) {
            deltaPage = -this.documentView.getNextPageIncrement();
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
        this.changingPage = true;
        final int dp = deltaPage;
        SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.KeyListenerPageColumnChanger.1
            @Override // java.lang.Runnable
            public void run() {
                KeyListenerPageColumnChanger.this.changingPage = false;
                KeyListenerPageColumnChanger.this.controller.goToDeltaPage(dp);
            }
        });
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent e2) {
        int keyCode = e2.getKeyCode();
        if (keyCode == 38 || keyCode == 40) {
            SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.KeyListenerPageColumnChanger.2
                @Override // java.lang.Runnable
                public void run() {
                    KeyListenerPageColumnChanger.this.currentPageChanger.calculateCurrentPage();
                }
            });
        }
    }
}
