package javax.swing.plaf.synth;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthOptionPaneUI.class */
public class SynthOptionPaneUI extends BasicOptionPaneUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthOptionPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected void installDefaults() {
        updateStyle(this.optionPane);
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected void installListeners() {
        super.installListeners();
        this.optionPane.addPropertyChangeListener(this);
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            this.minimumSize = (Dimension) this.style.get(context, "OptionPane.minimumSize");
            if (this.minimumSize == null) {
                this.minimumSize = new Dimension(262, 90);
            }
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.optionPane, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.optionPane.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected void installComponents() {
        this.optionPane.add(createMessageArea());
        Container containerCreateSeparator = createSeparator();
        if (containerCreateSeparator != null) {
            this.optionPane.add(containerCreateSeparator);
            SynthContext context = getContext(this.optionPane, 1);
            this.optionPane.add(Box.createVerticalStrut(context.getStyle().getInt(context, "OptionPane.separatorPadding", 6)));
            context.dispose();
        }
        this.optionPane.add(createButtonArea());
        this.optionPane.applyComponentOrientation(this.optionPane.getComponentOrientation());
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private int getComponentState(JComponent jComponent) {
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintOptionPaneBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintOptionPaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JOptionPane) propertyChangeEvent.getSource());
        }
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected boolean getSizeButtonsToSameWidth() {
        return DefaultLookup.getBoolean(this.optionPane, this, "OptionPane.sameSizeButtons", true);
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected Container createMessageArea() {
        Container jPanel = new JPanel();
        jPanel.setName("OptionPane.messageArea");
        jPanel.setLayout(new BorderLayout());
        Container jPanel2 = new JPanel(new GridBagLayout());
        JPanel jPanel3 = new JPanel(new BorderLayout());
        jPanel2.setName("OptionPane.body");
        jPanel3.setName("OptionPane.realBody");
        if (getIcon() != null) {
            JPanel jPanel4 = new JPanel();
            jPanel4.setName("OptionPane.separator");
            jPanel4.setPreferredSize(new Dimension(15, 1));
            jPanel3.add(jPanel4, "Before");
        }
        jPanel3.add(jPanel2, BorderLayout.CENTER);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.gridheight = 1;
        SynthContext context = getContext(this.optionPane, 1);
        gridBagConstraints.anchor = context.getStyle().getInt(context, "OptionPane.messageAnchor", 10);
        context.dispose();
        gridBagConstraints.insets = new Insets(0, 0, 3, 0);
        addMessageComponents(jPanel2, gridBagConstraints, getMessage(), getMaxCharactersPerLineCount(), false);
        jPanel.add(jPanel3, BorderLayout.CENTER);
        addIcon(jPanel);
        return jPanel;
    }

    @Override // javax.swing.plaf.basic.BasicOptionPaneUI
    protected Container createSeparator() {
        JSeparator jSeparator = new JSeparator(0);
        jSeparator.setName("OptionPane.separator");
        return jSeparator;
    }
}
