package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifSplitPaneDivider.class */
public class MotifSplitPaneDivider extends BasicSplitPaneDivider {
    private static final Cursor defaultCursor = Cursor.getPredefinedCursor(0);
    public static final int minimumThumbSize = 6;
    public static final int defaultDividerSize = 18;
    protected static final int pad = 6;
    private int hThumbOffset;
    private int vThumbOffset;
    protected int hThumbWidth;
    protected int hThumbHeight;
    protected int vThumbWidth;
    protected int vThumbHeight;
    protected Color highlightColor;
    protected Color shadowColor;
    protected Color focusedColor;

    public MotifSplitPaneDivider(BasicSplitPaneUI basicSplitPaneUI) {
        super(basicSplitPaneUI);
        this.hThumbOffset = 30;
        this.vThumbOffset = 40;
        this.hThumbWidth = 12;
        this.hThumbHeight = 18;
        this.vThumbWidth = 18;
        this.vThumbHeight = 12;
        this.highlightColor = UIManager.getColor("SplitPane.highlight");
        this.shadowColor = UIManager.getColor("SplitPane.shadow");
        this.focusedColor = UIManager.getColor("SplitPane.activeThumb");
        setDividerSize(this.hThumbWidth + 6);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    public void setDividerSize(int i2) {
        Insets insets = getInsets();
        int i3 = 0;
        if (getBasicSplitPaneUI().getOrientation() == 1) {
            if (insets != null) {
                i3 = insets.left + insets.right;
            }
        } else if (insets != null) {
            i3 = insets.top + insets.bottom;
        }
        if (i2 < 12 + i3) {
            setDividerSize(12 + i3);
            return;
        }
        int i4 = (i2 - 6) - i3;
        this.hThumbWidth = i4;
        this.vThumbHeight = i4;
        super.setDividerSize(i2);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        getBackground();
        Dimension size = getSize();
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, size.width, size.height);
        if (getBasicSplitPaneUI().getOrientation() == 1) {
            int i2 = size.width / 2;
            int i3 = i2 - (this.hThumbWidth / 2);
            int i4 = this.hThumbOffset;
            graphics.setColor(this.shadowColor);
            graphics.drawLine(i2 - 1, 0, i2 - 1, size.height);
            graphics.setColor(this.highlightColor);
            graphics.drawLine(i2, 0, i2, size.height);
            graphics.setColor(this.splitPane.hasFocus() ? this.focusedColor : getBackground());
            graphics.fillRect(i3 + 1, i4 + 1, this.hThumbWidth - 2, this.hThumbHeight - 1);
            graphics.setColor(this.highlightColor);
            graphics.drawLine(i3, i4, (i3 + this.hThumbWidth) - 1, i4);
            graphics.drawLine(i3, i4 + 1, i3, (i4 + this.hThumbHeight) - 1);
            graphics.setColor(this.shadowColor);
            graphics.drawLine(i3 + 1, (i4 + this.hThumbHeight) - 1, (i3 + this.hThumbWidth) - 1, (i4 + this.hThumbHeight) - 1);
            graphics.drawLine((i3 + this.hThumbWidth) - 1, i4 + 1, (i3 + this.hThumbWidth) - 1, (i4 + this.hThumbHeight) - 2);
        } else {
            int i5 = size.height / 2;
            int i6 = size.width - this.vThumbOffset;
            int i7 = (size.height / 2) - (this.vThumbHeight / 2);
            graphics.setColor(this.shadowColor);
            graphics.drawLine(0, i5 - 1, size.width, i5 - 1);
            graphics.setColor(this.highlightColor);
            graphics.drawLine(0, i5, size.width, i5);
            graphics.setColor(this.splitPane.hasFocus() ? this.focusedColor : getBackground());
            graphics.fillRect(i6 + 1, i7 + 1, this.vThumbWidth - 1, this.vThumbHeight - 1);
            graphics.setColor(this.highlightColor);
            graphics.drawLine(i6, i7, i6 + this.vThumbWidth, i7);
            graphics.drawLine(i6, i7 + 1, i6, i7 + this.vThumbHeight);
            graphics.setColor(this.shadowColor);
            graphics.drawLine(i6 + 1, i7 + this.vThumbHeight, i6 + this.vThumbWidth, i7 + this.vThumbHeight);
            graphics.drawLine(i6 + this.vThumbWidth, i7 + 1, i6 + this.vThumbWidth, (i7 + this.vThumbHeight) - 1);
        }
        super.paint(graphics);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    public void setBasicSplitPaneUI(BasicSplitPaneUI basicSplitPaneUI) {
        if (this.splitPane != null) {
            this.splitPane.removePropertyChangeListener(this);
            if (this.mouseHandler != null) {
                this.splitPane.removeMouseListener(this.mouseHandler);
                this.splitPane.removeMouseMotionListener(this.mouseHandler);
                removeMouseListener(this.mouseHandler);
                removeMouseMotionListener(this.mouseHandler);
                this.mouseHandler = null;
            }
        }
        this.splitPaneUI = basicSplitPaneUI;
        if (basicSplitPaneUI != null) {
            this.splitPane = basicSplitPaneUI.getSplitPane();
            if (this.splitPane != null) {
                if (this.mouseHandler == null) {
                    this.mouseHandler = new MotifMouseHandler();
                }
                this.splitPane.addMouseListener(this.mouseHandler);
                this.splitPane.addMouseMotionListener(this.mouseHandler);
                addMouseListener(this.mouseHandler);
                addMouseMotionListener(this.mouseHandler);
                this.splitPane.addPropertyChangeListener(this);
                if (this.splitPane.isOneTouchExpandable()) {
                    oneTouchExpandableChanged();
                    return;
                }
                return;
            }
            return;
        }
        this.splitPane = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInThumb(int i2, int i3) {
        int i4;
        int i5;
        int i6;
        int i7;
        Dimension size = getSize();
        if (getBasicSplitPaneUI().getOrientation() == 1) {
            i4 = (size.width / 2) - (this.hThumbWidth / 2);
            i5 = this.hThumbOffset;
            i6 = this.hThumbWidth;
            i7 = this.hThumbHeight;
        } else {
            int i8 = size.height / 2;
            i4 = size.width - this.vThumbOffset;
            i5 = (size.height / 2) - (this.vThumbHeight / 2);
            i6 = this.vThumbWidth;
            i7 = this.vThumbHeight;
        }
        return i2 >= i4 && i2 < i4 + i6 && i3 >= i5 && i3 < i5 + i7;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BasicSplitPaneDivider.DragController getDragger() {
        return this.dragger;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JSplitPane getSplitPane() {
        return this.splitPane;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifSplitPaneDivider$MotifMouseHandler.class */
    private class MotifMouseHandler extends BasicSplitPaneDivider.MouseHandler {
        private MotifMouseHandler() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicSplitPaneDivider.MouseHandler, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (mouseEvent.getSource() == MotifSplitPaneDivider.this && MotifSplitPaneDivider.this.getDragger() == null && MotifSplitPaneDivider.this.getSplitPane().isEnabled() && MotifSplitPaneDivider.this.isInThumb(mouseEvent.getX(), mouseEvent.getY())) {
                super.mousePressed(mouseEvent);
            }
        }

        @Override // javax.swing.plaf.basic.BasicSplitPaneDivider.MouseHandler, java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (MotifSplitPaneDivider.this.getDragger() == null) {
                if (!MotifSplitPaneDivider.this.isInThumb(mouseEvent.getX(), mouseEvent.getY())) {
                    if (MotifSplitPaneDivider.this.getCursor() != MotifSplitPaneDivider.defaultCursor) {
                        MotifSplitPaneDivider.this.setCursor(MotifSplitPaneDivider.defaultCursor);
                        return;
                    }
                    return;
                }
                super.mouseMoved(mouseEvent);
            }
        }
    }
}
