package org.icepdf.ri.common.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/PageViewDecorator.class */
public class PageViewDecorator extends JComponent {
    private static final Logger log = Logger.getLogger(PageViewDecorator.class.toString());
    protected JComponent pageViewComponent;
    protected static final int SHADOW_SIZE = 3;
    protected Dimension preferredSize = new Dimension();
    private static Color pageBorderColor;
    private static Color pageShadowColor;
    private static Color pageColor;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.shadow.color", "#333333");
            int colorValue = ColorUtil.convertColor(color);
            pageShadowColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("333333", 16));
        } catch (NumberFormatException e2) {
            if (log.isLoggable(Level.WARNING)) {
                log.warning("Error reading page shadow colour");
            }
        }
        try {
            String color2 = Defs.sysProperty("org.icepdf.core.views.page.paper.color", "#FFFFFF");
            int colorValue2 = ColorUtil.convertColor(color2);
            pageColor = new Color(colorValue2 >= 0 ? colorValue2 : Integer.parseInt("FFFFFF", 16));
        } catch (NumberFormatException e3) {
            if (log.isLoggable(Level.WARNING)) {
                log.warning("Error reading page paper color.");
            }
        }
        try {
            String color3 = Defs.sysProperty("org.icepdf.core.views.page.border.color", "#000000");
            int colorValue3 = ColorUtil.convertColor(color3);
            pageBorderColor = new Color(colorValue3 >= 0 ? colorValue3 : Integer.parseInt("000000", 16));
        } catch (NumberFormatException e4) {
            if (log.isLoggable(Level.WARNING)) {
                log.warning("Error reading page paper color.");
            }
        }
    }

    public PageViewDecorator(JComponent pageViewComponent) {
        setLayout(new GridLayout(1, 1, 0, 0));
        this.pageViewComponent = pageViewComponent;
        Dimension size = pageViewComponent.getPreferredSize();
        this.preferredSize.setSize(size.width + 3, size.height + 3);
        add(pageViewComponent);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension size = this.pageViewComponent.getPreferredSize();
        this.preferredSize.setSize(size.width + 3, size.height + 3);
        return this.preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics g2) {
        Graphics2D g2d = (Graphics2D) g2;
        Point location = this.pageViewComponent.getLocation();
        Dimension size = this.pageViewComponent.getPreferredSize();
        g2d.setColor(pageColor);
        g2d.fillRect(location.f12370x, location.f12371y, size.width, size.height);
        g2d.setColor(pageShadowColor);
        g2d.fillRect(location.f12370x + 3, location.f12371y + size.height, size.width - 3, 3);
        g2d.fillRect(location.f12370x + size.width, location.f12371y + 3, 3, size.height);
        super.paint(g2);
        g2d.setColor(pageBorderColor);
        g2d.drawRect(location.f12370x, location.f12371y, size.width, size.height);
    }

    public PageViewComponent getPageViewComponent() {
        return (PageViewComponent) this.pageViewComponent;
    }
}
