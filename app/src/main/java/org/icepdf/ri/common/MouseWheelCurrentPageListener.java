package org.icepdf.ri.common;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollPane;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/MouseWheelCurrentPageListener.class */
public class MouseWheelCurrentPageListener implements MouseWheelListener {
    private JScrollPane scrollpane;
    private CurrentPageChanger currentPageChanger;
    private boolean calculatingCurrentPage = false;

    public static Object install(JScrollPane scrollpane, CurrentPageChanger currentPageChanger) {
        MouseWheelCurrentPageListener listener = null;
        if (scrollpane != null && currentPageChanger != null) {
            listener = new MouseWheelCurrentPageListener(scrollpane, currentPageChanger);
            scrollpane.addMouseWheelListener(listener);
        }
        return listener;
    }

    public static void uninstall(JScrollPane scrollpane, Object listener) {
        if (scrollpane != null && listener != null && (listener instanceof MouseWheelCurrentPageListener)) {
            scrollpane.removeMouseWheelListener((MouseWheelCurrentPageListener) listener);
        }
    }

    protected MouseWheelCurrentPageListener(JScrollPane scrollpane, CurrentPageChanger currentPageChanger) {
        this.scrollpane = scrollpane;
        this.currentPageChanger = currentPageChanger;
    }

    @Override // java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent e2) {
        if (this.calculatingCurrentPage) {
            return;
        }
        int amount = e2.getScrollAmount();
        if (amount > 0) {
            this.calculatingCurrentPage = true;
            this.currentPageChanger.calculateCurrentPage();
            this.calculatingCurrentPage = false;
        }
    }
}
