package javax.swing.plaf.synth;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.nimbus.NimbusStyle;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthArrowButton.class */
class SynthArrowButton extends JButton implements SwingConstants, UIResource {
    private int direction;

    public SynthArrowButton(int i2) {
        super.setFocusable(false);
        setDirection(i2);
        setDefaultCapable(false);
    }

    @Override // javax.swing.JButton, javax.swing.JComponent
    public String getUIClassID() {
        return "ArrowButtonUI";
    }

    @Override // javax.swing.JButton, javax.swing.AbstractButton, javax.swing.JComponent
    public void updateUI() {
        setUI((ButtonUI) new SynthArrowButtonUI());
    }

    public void setDirection(int i2) {
        this.direction = i2;
        putClientProperty("__arrow_direction__", Integer.valueOf(i2));
        repaint();
    }

    public int getDirection() {
        return this.direction;
    }

    @Override // java.awt.Component
    public void setFocusable(boolean z2) {
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthArrowButton$SynthArrowButtonUI.class */
    private static class SynthArrowButtonUI extends SynthButtonUI {
        private SynthArrowButtonUI() {
        }

        @Override // javax.swing.plaf.synth.SynthButtonUI, javax.swing.plaf.basic.BasicButtonUI
        protected void installDefaults(AbstractButton abstractButton) {
            super.installDefaults(abstractButton);
            updateStyle(abstractButton);
        }

        @Override // javax.swing.plaf.synth.SynthButtonUI
        protected void paint(SynthContext synthContext, Graphics graphics) {
            SynthArrowButton synthArrowButton = (SynthArrowButton) synthContext.getComponent();
            synthContext.getPainter().paintArrowButtonForeground(synthContext, graphics, 0, 0, synthArrowButton.getWidth(), synthArrowButton.getHeight(), synthArrowButton.getDirection());
        }

        @Override // javax.swing.plaf.synth.SynthButtonUI
        void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
            synthContext.getPainter().paintArrowButtonBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        }

        @Override // javax.swing.plaf.synth.SynthButtonUI, javax.swing.plaf.synth.SynthUI
        public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            synthContext.getPainter().paintArrowButtonBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        public Dimension getMinimumSize() {
            return new Dimension(5, 5);
        }

        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        @Override // javax.swing.plaf.synth.SynthButtonUI, javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
        public Dimension getPreferredSize(JComponent jComponent) {
            Object clientProperty;
            SynthContext context = getContext(jComponent);
            Dimension dimension = null;
            if (context.getComponent().getName() == "ScrollBar.button") {
                dimension = (Dimension) context.getStyle().get(context, "ScrollBar.buttonSize");
            }
            if (dimension == null) {
                int i2 = context.getStyle().getInt(context, "ArrowButton.size", 16);
                dimension = new Dimension(i2, i2);
            }
            Container parent = context.getComponent().getParent();
            if ((parent instanceof JComponent) && !(parent instanceof JComboBox) && (clientProperty = ((JComponent) parent).getClientProperty("JComponent.sizeVariant")) != null) {
                if (NimbusStyle.LARGE_KEY.equals(clientProperty)) {
                    dimension = new Dimension((int) (dimension.width * 1.15d), (int) (dimension.height * 1.15d));
                } else if (NimbusStyle.SMALL_KEY.equals(clientProperty)) {
                    dimension = new Dimension((int) (dimension.width * 0.857d), (int) (dimension.height * 0.857d));
                } else if (NimbusStyle.MINI_KEY.equals(clientProperty)) {
                    dimension = new Dimension((int) (dimension.width * 0.714d), (int) (dimension.height * 0.714d));
                }
            }
            context.dispose();
            return dimension;
        }
    }
}
