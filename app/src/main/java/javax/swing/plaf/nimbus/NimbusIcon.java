package javax.swing.plaf.nimbus;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.SynthContext;
import sun.swing.plaf.synth.SynthIcon;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusIcon.class */
class NimbusIcon extends SynthIcon {
    private int width;
    private int height;
    private String prefix;
    private String key;

    NimbusIcon(String str, String str2, int i2, int i3) {
        this.width = i2;
        this.height = i3;
        this.prefix = str;
        this.key = str2;
    }

    @Override // sun.swing.plaf.synth.SynthIcon
    public void paintIcon(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        Painter painter = null;
        if (synthContext != null) {
            painter = (Painter) synthContext.getStyle().get(synthContext, this.key);
        }
        if (painter == null) {
            painter = (Painter) UIManager.get(this.prefix + "[Enabled]." + this.key);
        }
        if (painter != null && synthContext != null) {
            JComponent component = synthContext.getComponent();
            boolean z2 = false;
            boolean z3 = false;
            int i6 = 0;
            int i7 = 0;
            if (component instanceof JToolBar) {
                JToolBar jToolBar = (JToolBar) component;
                z2 = jToolBar.getOrientation() == 1;
                z3 = !jToolBar.getComponentOrientation().isLeftToRight();
                Object objResolveToolbarConstraint = NimbusLookAndFeel.resolveToolbarConstraint(jToolBar);
                if (jToolBar.getBorder() instanceof UIResource) {
                    if (objResolveToolbarConstraint == "South") {
                        i7 = 1;
                    } else if (objResolveToolbarConstraint == "East") {
                        i6 = 1;
                    }
                }
            } else if (component instanceof JMenu) {
                z3 = !component.getComponentOrientation().isLeftToRight();
            }
            if (graphics instanceof Graphics2D) {
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.translate(i2, i3);
                graphics2D.translate(i6, i7);
                if (z2) {
                    graphics2D.rotate(Math.toRadians(90.0d));
                    graphics2D.translate(0, -i4);
                    painter.paint(graphics2D, synthContext.getComponent(), i5, i4);
                    graphics2D.translate(0, i4);
                    graphics2D.rotate(Math.toRadians(-90.0d));
                } else if (z3) {
                    graphics2D.scale(-1.0d, 1.0d);
                    graphics2D.translate(-i4, 0);
                    painter.paint(graphics2D, synthContext.getComponent(), i4, i5);
                    graphics2D.translate(i4, 0);
                    graphics2D.scale(-1.0d, 1.0d);
                } else {
                    painter.paint(graphics2D, synthContext.getComponent(), i4, i5);
                }
                graphics2D.translate(-i6, -i7);
                graphics2D.translate(-i2, -i3);
                return;
            }
            BufferedImage bufferedImage = new BufferedImage(i4, i5, 2);
            Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
            if (z2) {
                graphics2DCreateGraphics.rotate(Math.toRadians(90.0d));
                graphics2DCreateGraphics.translate(0, -i4);
                painter.paint(graphics2DCreateGraphics, synthContext.getComponent(), i5, i4);
            } else if (z3) {
                graphics2DCreateGraphics.scale(-1.0d, 1.0d);
                graphics2DCreateGraphics.translate(-i4, 0);
                painter.paint(graphics2DCreateGraphics, synthContext.getComponent(), i4, i5);
            } else {
                painter.paint(graphics2DCreateGraphics, synthContext.getComponent(), i4, i5);
            }
            graphics2DCreateGraphics.dispose();
            graphics.drawImage(bufferedImage, i2, i3, null);
        }
    }

    @Override // sun.swing.plaf.synth.SynthIcon, javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        Painter painter = (Painter) UIManager.get(this.prefix + "[Enabled]." + this.key);
        if (painter != null) {
            JComponent jComponent = component instanceof JComponent ? (JComponent) component : null;
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(i2, i3);
            painter.paint(graphics2D, jComponent, this.width, this.height);
            graphics2D.translate(-i2, -i3);
        }
    }

    @Override // sun.swing.plaf.synth.SynthIcon
    public int getIconWidth(SynthContext synthContext) {
        if (synthContext == null) {
            return this.width;
        }
        JComponent component = synthContext.getComponent();
        if ((component instanceof JToolBar) && ((JToolBar) component).getOrientation() == 1) {
            if (component.getBorder() instanceof UIResource) {
                return component.getWidth() - 1;
            }
            return component.getWidth();
        }
        return scale(synthContext, this.width);
    }

    @Override // sun.swing.plaf.synth.SynthIcon
    public int getIconHeight(SynthContext synthContext) {
        if (synthContext == null) {
            return this.height;
        }
        JComponent component = synthContext.getComponent();
        if (component instanceof JToolBar) {
            JToolBar jToolBar = (JToolBar) component;
            if (jToolBar.getOrientation() == 0) {
                if (jToolBar.getBorder() instanceof UIResource) {
                    return component.getHeight() - 1;
                }
                return component.getHeight();
            }
            return scale(synthContext, this.width);
        }
        return scale(synthContext, this.height);
    }

    private int scale(SynthContext synthContext, int i2) {
        if (synthContext == null || synthContext.getComponent() == null) {
            return i2;
        }
        String str = (String) synthContext.getComponent().getClientProperty("JComponent.sizeVariant");
        if (str != null) {
            if (NimbusStyle.LARGE_KEY.equals(str)) {
                i2 = (int) (i2 * 1.15d);
            } else if (NimbusStyle.SMALL_KEY.equals(str)) {
                i2 = (int) (i2 * 0.857d);
            } else if (NimbusStyle.MINI_KEY.equals(str)) {
                i2 = (int) (i2 * 0.784d);
            }
        }
        return i2;
    }
}
