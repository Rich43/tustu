package org.icepdf.ri.common.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.AbstractBorder;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/ResizableBorder.class */
public class ResizableBorder extends AbstractBorder {
    private static final Logger logger = Logger.getLogger(ResizableBorder.class.toString());
    private static Color selectColor;
    private static Color outlineColor;
    private static Color outlineResizeColor;
    public static final int INSETS = 5;
    private static final int[] locations;
    private static final int[] cursors;
    private static final Stroke dashedBorder;
    private static final Stroke solidBorder;
    protected int resizeWidgetDim;
    protected int originalResizeWidgetDim;
    protected int inset;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.annotation.select.color", "#999999");
            int colorValue = ColorUtil.convertColor(color);
            selectColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("999999", 16));
            String color2 = Defs.sysProperty("org.icepdf.core.views.page.annotation.outline.color", "#cccccc");
            int colorValue2 = ColorUtil.convertColor(color2);
            outlineColor = new Color(colorValue2 >= 0 ? colorValue2 : Integer.parseInt("cccccc", 16));
            String color3 = Defs.sysProperty("org.icepdf.core.views.page.annotation.outline.colorResize", "#666666");
            int colorValue3 = ColorUtil.convertColor(color3);
            outlineResizeColor = new Color(colorValue3 >= 0 ? colorValue3 : Integer.parseInt("666666", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading page annotation outline colour");
            }
        }
        locations = new int[]{1, 5, 7, 3, 8, 2, 6, 4};
        cursors = new int[]{8, 9, 10, 11, 6, 7, 4, 5};
        dashedBorder = new BasicStroke(1.0f, 0, 2, 0.0f, new float[]{2.0f, 1.0f}, 0.0f);
        solidBorder = new BasicStroke(1.0f, 0, 2, 0.0f);
    }

    public ResizableBorder(int resizeBoxSize) {
        this.originalResizeWidgetDim = resizeBoxSize;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return new Insets(this.inset, this.inset, this.inset, this.inset);
    }

    public void setZoom(float zoom) {
        this.resizeWidgetDim = (int) (this.originalResizeWidgetDim * zoom);
        this.inset = (int) ((5.0f * zoom) + 0.5d);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics g2, int x2, int y2, int w2, int h2) {
        boolean isSelected = false;
        boolean isBorderStyle = false;
        boolean isEditable = false;
        boolean isRollover = false;
        boolean isResizable = false;
        boolean isShowInvisibleBorder = false;
        if (component instanceof AnnotationComponent) {
            AnnotationComponent annot = (AnnotationComponent) component;
            isSelected = annot.isSelected();
            isBorderStyle = annot.isBorderStyle();
            isEditable = annot.isEditable();
            isRollover = annot.isRollover();
            annot.isMovable();
            isResizable = annot.isResizable();
            isShowInvisibleBorder = annot.isShowInvisibleBorder();
        }
        if (!isEditable) {
            return;
        }
        Graphics2D g22 = (Graphics2D) g2;
        g22.setStroke(dashedBorder);
        if (isSelected || component.hasFocus() || isRollover) {
            g22.setColor(selectColor);
        } else {
            g22.setColor(outlineColor);
        }
        if (isSelected || isRollover || (isShowInvisibleBorder && !isBorderStyle)) {
            g22.drawRect(x2, y2, w2 - 1, h2 - 1);
        }
        g22.setColor(outlineResizeColor);
        g22.setStroke(solidBorder);
        if ((isSelected || isRollover) && isResizable) {
            int[] arr$ = locations;
            for (int location : arr$) {
                Rectangle rect = getRectangle(x2, y2, w2, h2, location);
                g22.fillRect(rect.f12372x, rect.f12373y, rect.width - 1, rect.height - 1);
                g22.drawRect(rect.f12372x, rect.f12373y, rect.width - 1, rect.height - 1);
            }
        }
    }

    private Rectangle getRectangle(int x2, int y2, int w2, int h2, int location) {
        switch (location) {
            case 1:
                return new Rectangle((x2 + (w2 / 2)) - (this.resizeWidgetDim / 2), y2, this.resizeWidgetDim, this.resizeWidgetDim);
            case 2:
                return new Rectangle((x2 + w2) - this.resizeWidgetDim, y2, this.resizeWidgetDim, this.resizeWidgetDim);
            case 3:
                return new Rectangle((x2 + w2) - this.resizeWidgetDim, (y2 + (h2 / 2)) - (this.resizeWidgetDim / 2), this.resizeWidgetDim, this.resizeWidgetDim);
            case 4:
                return new Rectangle((x2 + w2) - this.resizeWidgetDim, (y2 + h2) - this.resizeWidgetDim, this.resizeWidgetDim, this.resizeWidgetDim);
            case 5:
                return new Rectangle((x2 + (w2 / 2)) - (this.resizeWidgetDim / 2), (y2 + h2) - this.resizeWidgetDim, this.resizeWidgetDim, this.resizeWidgetDim);
            case 6:
                return new Rectangle(x2, (y2 + h2) - this.resizeWidgetDim, this.resizeWidgetDim, this.resizeWidgetDim);
            case 7:
                return new Rectangle(x2, (y2 + (h2 / 2)) - (this.resizeWidgetDim / 2), this.resizeWidgetDim, this.resizeWidgetDim);
            case 8:
                return new Rectangle(x2, y2, this.resizeWidgetDim, this.resizeWidgetDim);
            default:
                return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int getCursor(MouseEvent me) {
        Component component = me.getComponent();
        boolean isMovable = false;
        boolean isResizable = false;
        if (component instanceof AnnotationComponent) {
            AnnotationComponent annot = (AnnotationComponent) component;
            annot.isEditable();
            isResizable = annot.isResizable();
            isMovable = annot.isMovable();
        }
        int w2 = component.getWidth();
        int h2 = component.getHeight();
        if (isResizable) {
            for (int i2 = 0; i2 < locations.length; i2++) {
                Rectangle rect = getRectangle(0, 0, w2, h2, locations[i2]);
                if (rect.contains(me.getPoint())) {
                    return cursors[i2];
                }
            }
        }
        if (isMovable) {
            return 13;
        }
        return 0;
    }
}
