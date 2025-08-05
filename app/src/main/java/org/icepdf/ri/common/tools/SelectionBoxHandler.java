package org.icepdf.ri.common.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/SelectionBoxHandler.class */
public abstract class SelectionBoxHandler extends CommonToolHandler {
    protected Rectangle currentRect;
    protected Rectangle rectToDraw;
    protected Rectangle previousRectDrawn;
    protected static float[] dash1 = {1.0f};
    protected static BasicStroke stroke = new BasicStroke(1.0f, 0, 0, 1.0f, dash1, 0.0f);
    protected static Color selectionBoxColour = Color.lightGray;

    public abstract void setSelectionRectangle(Point point, Rectangle rectangle);

    protected SelectionBoxHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        this.currentRect = null;
        this.rectToDraw = null;
        this.previousRectDrawn = new Rectangle();
    }

    public static void paintSelectionBox(Graphics g2, Rectangle rectToDraw) {
        Graphics2D gg = (Graphics2D) g2;
        Color oldColor = gg.getColor();
        Stroke oldStroke = gg.getStroke();
        if (rectToDraw != null) {
            oldColor = g2.getColor();
            gg.setColor(selectionBoxColour);
            gg.setStroke(stroke);
            gg.drawRect(rectToDraw.f12372x, rectToDraw.f12373y, rectToDraw.width - 1, rectToDraw.height - 1);
            gg.setColor(oldColor);
        }
        gg.setColor(oldColor);
        gg.setStroke(oldStroke);
    }

    public void resetRectangle(int x2, int y2) {
        this.currentRect = new Rectangle(x2, y2, 0, 0);
    }

    public Rectangle getCurrentRect() {
        return this.currentRect;
    }

    public void setCurrentRect(Rectangle currentRect) {
        this.currentRect = currentRect;
    }

    public Rectangle getRectToDraw() {
        return this.rectToDraw;
    }

    public void setRectToDraw(Rectangle rectToDraw) {
        this.rectToDraw = rectToDraw;
    }

    public void clearRectangle(Component component) {
        this.currentRect = new Rectangle(0, 0, 0, 0);
        updateDrawableRect(component.getWidth(), component.getHeight());
    }

    public void updateSelectionSize(MouseEvent e2, Component component) {
        int x2 = e2.getX();
        int y2 = e2.getY();
        if (this.currentRect == null) {
            this.currentRect = new Rectangle(x2, y2, 0, 0);
        }
        this.currentRect.setSize(x2 - this.currentRect.f12372x, y2 - this.currentRect.f12373y);
        updateDrawableRect(component.getWidth(), component.getHeight());
        Rectangle totalRepaint = this.rectToDraw.union(this.previousRectDrawn);
        component.repaint(totalRepaint.f12372x, totalRepaint.f12373y, totalRepaint.width + 10, totalRepaint.height + 10);
    }

    public void setSelectionSize(Rectangle rect, Component component) {
        this.currentRect = rect;
        updateDrawableRect(component.getWidth(), component.getHeight());
        Rectangle totalRepaint = this.rectToDraw.union(this.previousRectDrawn);
        component.repaint(totalRepaint.f12372x, totalRepaint.f12373y, totalRepaint.width, totalRepaint.height);
    }

    public void updateDrawableRect(int compWidth, int compHeight) {
        int x2 = this.currentRect.f12372x;
        int y2 = this.currentRect.f12373y;
        int width = this.currentRect.width;
        int height = this.currentRect.height;
        if (width < 0) {
            width = 0 - width;
            x2 = (x2 - width) + 1;
            if (x2 < 0) {
                width += x2;
                x2 = 0;
            }
        }
        if (height < 0) {
            height = 0 - height;
            y2 = (y2 - height) + 1;
            if (y2 < 0) {
                height += y2;
                y2 = 0;
            }
        }
        if (x2 + width > compWidth) {
            width = compWidth - x2;
        }
        if (y2 + height > compHeight) {
            height = compHeight - y2;
        }
        if (this.rectToDraw != null) {
            this.previousRectDrawn.setBounds(this.rectToDraw.f12372x, this.rectToDraw.f12373y, this.rectToDraw.width, this.rectToDraw.height);
            this.rectToDraw.setBounds(x2, y2, width, height);
        } else {
            this.rectToDraw = new Rectangle(x2, y2, width, height);
        }
    }

    protected AbstractPageViewComponent isOverPageComponent(Container container, MouseEvent e2) {
        Component comp = container.findComponentAt(e2.getPoint());
        if (comp instanceof AbstractPageViewComponent) {
            return (AbstractPageViewComponent) comp;
        }
        return null;
    }
}
