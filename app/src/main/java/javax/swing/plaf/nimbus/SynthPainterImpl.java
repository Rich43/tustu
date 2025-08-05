package javax.swing.plaf.nimbus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.Painter;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SynthPainterImpl.class */
class SynthPainterImpl extends SynthPainter {
    private NimbusStyle style;

    SynthPainterImpl(NimbusStyle nimbusStyle) {
        this.style = nimbusStyle;
    }

    private void paint(Painter painter, SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, AffineTransform affineTransform) {
        if (painter != null) {
            if (graphics instanceof Graphics2D) {
                Graphics2D graphics2D = (Graphics2D) graphics;
                if (affineTransform != null) {
                    graphics2D.transform(affineTransform);
                }
                graphics2D.translate(i2, i3);
                painter.paint(graphics2D, synthContext.getComponent(), i4, i5);
                graphics2D.translate(-i2, -i3);
                if (affineTransform != null) {
                    try {
                        graphics2D.transform(affineTransform.createInverse());
                        return;
                    } catch (NoninvertibleTransformException e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                return;
            }
            BufferedImage bufferedImage = new BufferedImage(i4, i5, 2);
            Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
            if (affineTransform != null) {
                graphics2DCreateGraphics.transform(affineTransform);
            }
            painter.paint(graphics2DCreateGraphics, synthContext.getComponent(), i4, i5);
            graphics2DCreateGraphics.dispose();
            graphics.drawImage(bufferedImage, i2, i3, null);
        }
    }

    private void paintBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, AffineTransform affineTransform) {
        Painter backgroundPainter;
        JComponent component = synthContext.getComponent();
        Color background = component != null ? component.getBackground() : null;
        if ((background == null || background.getAlpha() > 0) && (backgroundPainter = this.style.getBackgroundPainter(synthContext)) != null) {
            paint(backgroundPainter, synthContext, graphics, i2, i3, i4, i5, affineTransform);
        }
    }

    private void paintForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, AffineTransform affineTransform) {
        Painter foregroundPainter = this.style.getForegroundPainter(synthContext);
        if (foregroundPainter != null) {
            paint(foregroundPainter, synthContext, graphics, i2, i3, i4, i5, affineTransform);
        }
    }

    private void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, AffineTransform affineTransform) {
        Painter borderPainter = this.style.getBorderPainter(synthContext);
        if (borderPainter != null) {
            paint(borderPainter, synthContext, graphics, i2, i3, i4, i5, affineTransform);
        }
    }

    private void paintBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        boolean zIsLeftToRight = synthContext.getComponent().getComponentOrientation().isLeftToRight();
        if (synthContext.getComponent() instanceof JSlider) {
            zIsLeftToRight = true;
        }
        if (i6 == 1 && zIsLeftToRight) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(-1.0d, 1.0d);
            affineTransform.rotate(Math.toRadians(90.0d));
            paintBackground(synthContext, graphics, i3, i2, i5, i4, affineTransform);
            return;
        }
        if (i6 == 1) {
            AffineTransform affineTransform2 = new AffineTransform();
            affineTransform2.rotate(Math.toRadians(90.0d));
            affineTransform2.translate(0.0d, -(i2 + i4));
            paintBackground(synthContext, graphics, i3, i2, i5, i4, affineTransform2);
            return;
        }
        if (i6 == 0 && zIsLeftToRight) {
            paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
            return;
        }
        AffineTransform affineTransform3 = new AffineTransform();
        affineTransform3.translate(i2, i3);
        affineTransform3.scale(-1.0d, 1.0d);
        affineTransform3.translate(-i4, 0.0d);
        paintBackground(synthContext, graphics, 0, 0, i4, i5, affineTransform3);
    }

    private void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        boolean zIsLeftToRight = synthContext.getComponent().getComponentOrientation().isLeftToRight();
        if (i6 == 1 && zIsLeftToRight) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(-1.0d, 1.0d);
            affineTransform.rotate(Math.toRadians(90.0d));
            paintBorder(synthContext, graphics, i3, i2, i5, i4, affineTransform);
            return;
        }
        if (i6 == 1) {
            AffineTransform affineTransform2 = new AffineTransform();
            affineTransform2.rotate(Math.toRadians(90.0d));
            affineTransform2.translate(0.0d, -(i2 + i4));
            paintBorder(synthContext, graphics, i3, 0, i5, i4, affineTransform2);
            return;
        }
        if (i6 == 0 && zIsLeftToRight) {
            paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
        } else {
            paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
        }
    }

    private void paintForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        boolean zIsLeftToRight = synthContext.getComponent().getComponentOrientation().isLeftToRight();
        if (i6 == 1 && zIsLeftToRight) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(-1.0d, 1.0d);
            affineTransform.rotate(Math.toRadians(90.0d));
            paintForeground(synthContext, graphics, i3, i2, i5, i4, affineTransform);
            return;
        }
        if (i6 == 1) {
            AffineTransform affineTransform2 = new AffineTransform();
            affineTransform2.rotate(Math.toRadians(90.0d));
            affineTransform2.translate(0.0d, -(i2 + i4));
            paintForeground(synthContext, graphics, i3, 0, i5, i4, affineTransform2);
            return;
        }
        if (i6 == 0 && zIsLeftToRight) {
            paintForeground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
        } else {
            paintForeground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
        }
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintArrowButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (synthContext.getComponent().getComponentOrientation().isLeftToRight()) {
            paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
            return;
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(i2, i3);
        affineTransform.scale(-1.0d, 1.0d);
        affineTransform.translate(-i4, 0.0d);
        paintBackground(synthContext, graphics, 0, 0, i4, i5, affineTransform);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintArrowButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintArrowButtonForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        String name = synthContext.getComponent().getName();
        boolean zIsLeftToRight = synthContext.getComponent().getComponentOrientation().isLeftToRight();
        if ("Spinner.nextButton".equals(name) || "Spinner.previousButton".equals(name)) {
            if (zIsLeftToRight) {
                paintForeground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
                return;
            }
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(i4, 0.0d);
            affineTransform.scale(-1.0d, 1.0d);
            paintForeground(synthContext, graphics, i2, i3, i4, i5, affineTransform);
            return;
        }
        if (i6 == 7) {
            paintForeground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
            return;
        }
        if (i6 == 1) {
            if (zIsLeftToRight) {
                AffineTransform affineTransform2 = new AffineTransform();
                affineTransform2.scale(-1.0d, 1.0d);
                affineTransform2.rotate(Math.toRadians(90.0d));
                paintForeground(synthContext, graphics, i3, 0, i5, i4, affineTransform2);
                return;
            }
            AffineTransform affineTransform3 = new AffineTransform();
            affineTransform3.rotate(Math.toRadians(90.0d));
            affineTransform3.translate(0.0d, -(i2 + i4));
            paintForeground(synthContext, graphics, i3, 0, i5, i4, affineTransform3);
            return;
        }
        if (i6 == 3) {
            AffineTransform affineTransform4 = new AffineTransform();
            affineTransform4.translate(i4, 0.0d);
            affineTransform4.scale(-1.0d, 1.0d);
            paintForeground(synthContext, graphics, i2, i3, i4, i5, affineTransform4);
            return;
        }
        if (i6 == 5) {
            if (zIsLeftToRight) {
                AffineTransform affineTransform5 = new AffineTransform();
                affineTransform5.rotate(Math.toRadians(-90.0d));
                affineTransform5.translate(-i5, 0.0d);
                paintForeground(synthContext, graphics, i3, i2, i5, i4, affineTransform5);
                return;
            }
            AffineTransform affineTransform6 = new AffineTransform();
            affineTransform6.scale(-1.0d, 1.0d);
            affineTransform6.rotate(Math.toRadians(-90.0d));
            affineTransform6.translate(-(i5 + i3), -(i4 + i2));
            paintForeground(synthContext, graphics, i3, i2, i5, i4, affineTransform6);
        }
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintCheckBoxMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintCheckBoxMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintCheckBoxBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintCheckBoxBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintColorChooserBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintColorChooserBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintComboBoxBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (synthContext.getComponent().getComponentOrientation().isLeftToRight()) {
            paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
            return;
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(i2, i3);
        affineTransform.scale(-1.0d, 1.0d);
        affineTransform.translate(-i4, 0.0d);
        paintBackground(synthContext, graphics, 0, 0, i4, i5, affineTransform);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintComboBoxBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintDesktopIconBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintDesktopIconBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintDesktopPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintDesktopPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintEditorPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintEditorPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintFileChooserBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintFileChooserBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintFormattedTextFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (synthContext.getComponent().getComponentOrientation().isLeftToRight()) {
            paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
            return;
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(i2, i3);
        affineTransform.scale(-1.0d, 1.0d);
        affineTransform.translate(-i4, 0.0d);
        paintBackground(synthContext, graphics, 0, 0, i4, i5, affineTransform);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintFormattedTextFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (synthContext.getComponent().getComponentOrientation().isLeftToRight()) {
            paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
            return;
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(i2, i3);
        affineTransform.scale(-1.0d, 1.0d);
        affineTransform.translate(-i4, 0.0d);
        paintBorder(synthContext, graphics, 0, 0, i4, i5, affineTransform);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintInternalFrameTitlePaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintInternalFrameTitlePaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintInternalFrameBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintInternalFrameBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintLabelBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintLabelBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintListBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintListBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintOptionPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintOptionPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPanelBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPanelBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPasswordFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPasswordFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPopupMenuBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPopupMenuBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintForeground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRadioButtonMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRadioButtonMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRadioButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRadioButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRootPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRootPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarThumbBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarThumbBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintForeground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderThumbBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        int i7;
        if (synthContext.getComponent().getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE) {
            if (i6 == 0) {
                i7 = 1;
            } else {
                i7 = 0;
            }
            paintBackground(synthContext, graphics, i2, i3, i4, i5, i7);
            return;
        }
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderThumbBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSpinnerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSpinnerBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneDividerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneDividerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        if (i6 == 1) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(-1.0d, 1.0d);
            affineTransform.rotate(Math.toRadians(90.0d));
            paintBackground(synthContext, graphics, i3, i2, i5, i4, affineTransform);
            return;
        }
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneDividerForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintForeground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneDragDivider(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        if (i6 == 2) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(-1.0d, 1.0d);
            affineTransform.rotate(Math.toRadians(90.0d));
            paintBackground(synthContext, graphics, i3, i2, i5, i4, affineTransform);
            return;
        }
        if (i6 == 4) {
            AffineTransform affineTransform2 = new AffineTransform();
            affineTransform2.rotate(Math.toRadians(90.0d));
            affineTransform2.translate(0.0d, -(i2 + i4));
            paintBackground(synthContext, graphics, i3, 0, i5, i4, affineTransform2);
            return;
        }
        if (i6 == 3) {
            AffineTransform affineTransform3 = new AffineTransform();
            affineTransform3.translate(i2, i3);
            affineTransform3.scale(1.0d, -1.0d);
            affineTransform3.translate(0.0d, -i5);
            paintBackground(synthContext, graphics, 0, 0, i4, i5, affineTransform3);
            return;
        }
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (i7 == 2) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(-1.0d, 1.0d);
            affineTransform.rotate(Math.toRadians(90.0d));
            paintBackground(synthContext, graphics, i3, i2, i5, i4, affineTransform);
            return;
        }
        if (i7 == 4) {
            AffineTransform affineTransform2 = new AffineTransform();
            affineTransform2.rotate(Math.toRadians(90.0d));
            affineTransform2.translate(0.0d, -(i2 + i4));
            paintBackground(synthContext, graphics, i3, 0, i5, i4, affineTransform2);
            return;
        }
        if (i7 == 3) {
            AffineTransform affineTransform3 = new AffineTransform();
            affineTransform3.translate(i2, i3);
            affineTransform3.scale(1.0d, -1.0d);
            affineTransform3.translate(0.0d, -i5);
            paintBackground(synthContext, graphics, 0, 0, i4, i5, affineTransform3);
            return;
        }
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTableHeaderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTableHeaderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTableBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTableBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (synthContext.getComponent().getComponentOrientation().isLeftToRight()) {
            paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
            return;
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(i2, i3);
        affineTransform.scale(-1.0d, 1.0d);
        affineTransform.translate(-i4, 0.0d);
        paintBackground(synthContext, graphics, 0, 0, i4, i5, affineTransform);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (synthContext.getComponent().getComponentOrientation().isLeftToRight()) {
            paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
            return;
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(i2, i3);
        affineTransform.scale(-1.0d, 1.0d);
        affineTransform.translate(-i4, 0.0d);
        paintBorder(synthContext, graphics, 0, 0, i4, i5, affineTransform);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToggleButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToggleButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarDragWindowBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarDragWindowBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarDragWindowBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarDragWindowBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolTipBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolTipBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeCellBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeCellBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeCellFocus(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintViewportBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBackground(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintViewportBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paintBorder(synthContext, graphics, i2, i3, i4, i5, (AffineTransform) null);
    }
}
