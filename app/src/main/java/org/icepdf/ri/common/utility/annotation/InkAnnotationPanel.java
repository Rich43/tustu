package org.icepdf.ri.common.utility.annotation;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.annotations.InkAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/InkAnnotationPanel.class */
public class InkAnnotationPanel extends AnnotationPanelAdapter implements ItemListener, ActionListener {
    private static final int DEFAULT_LINE_THICKNESS = 0;
    private static final int DEFAULT_LINE_STYLE = 0;
    private static final Color DEFAULT_BORDER_COLOR = Color.RED;
    private JComboBox lineThicknessBox;
    private JComboBox lineStyleBox;
    private JButton colorBorderButton;
    private InkAnnotation annotation;

    public InkAnnotationPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(3, 2, 5, 2));
        setFocusable(true);
        createGUI();
        setEnabled(false);
        revalidate();
    }

    @Override // org.icepdf.ri.common.utility.annotation.AnnotationProperties
    public void setAnnotationComponent(AnnotationComponent newAnnotation) {
        if (newAnnotation == null || newAnnotation.getAnnotation() == null) {
            setEnabled(false);
            return;
        }
        this.currentAnnotationComponent = newAnnotation;
        this.annotation = (InkAnnotation) this.currentAnnotationComponent.getAnnotation();
        applySelectedValue(this.lineThicknessBox, Float.valueOf(this.annotation.getLineThickness()));
        applySelectedValue(this.lineStyleBox, this.annotation.getLineStyle());
        this.colorBorderButton.setBackground(this.annotation.getColor());
        safeEnable(this.lineThicknessBox, true);
        safeEnable(this.lineStyleBox, true);
        safeEnable(this.colorBorderButton, true);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        ValueLabelItem item = (ValueLabelItem) e2.getItem();
        if (e2.getStateChange() == 1) {
            if (e2.getSource() == this.lineThicknessBox) {
                this.annotation.getBorderStyle().setStrokeWidth(((Float) item.getValue()).floatValue());
            } else if (e2.getSource() == this.lineStyleBox) {
                this.annotation.getBorderStyle().setBorderStyle((Name) item.getValue());
            }
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) {
        Color chosenColor;
        if (e2.getSource() == this.colorBorderButton && (chosenColor = JColorChooser.showDialog(this.colorBorderButton, this.messageBundle.getString("viewer.utilityPane.annotation.ink.colorBorderChooserTitle"), this.colorBorderButton.getBackground())) != null) {
            this.colorBorderButton.setBackground(chosenColor);
            this.annotation.setColor(chosenColor);
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    private void createGUI() {
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.ink.appearance.title"), 1, 0));
        this.lineThicknessBox = new JComboBox(LINE_THICKNESS_LIST);
        this.lineThicknessBox.setSelectedIndex(0);
        this.lineThicknessBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.ink.lineThickness")));
        add(this.lineThicknessBox);
        this.lineStyleBox = new JComboBox(LINE_STYLE_LIST);
        this.lineStyleBox.setSelectedIndex(0);
        this.lineStyleBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.ink.lineStyle")));
        add(this.lineStyleBox);
        this.colorBorderButton = new JButton();
        this.colorBorderButton.addActionListener(this);
        this.colorBorderButton.setOpaque(true);
        this.colorBorderButton.setBackground(DEFAULT_BORDER_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.ink.colorBorderLabel")));
        add(this.colorBorderButton);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(this.lineThicknessBox, enabled);
        safeEnable(this.lineStyleBox, enabled);
        safeEnable(this.colorBorderButton, enabled);
    }

    protected boolean safeEnable(JComponent comp, boolean enabled) {
        if (comp != null) {
            comp.setEnabled(enabled);
            return true;
        }
        return false;
    }

    private void applySelectedValue(JComboBox comboBox, Object value) {
        comboBox.removeItemListener(this);
        int i2 = 0;
        while (true) {
            if (i2 >= comboBox.getItemCount()) {
                break;
            }
            ValueLabelItem currentItem = (ValueLabelItem) comboBox.getItemAt(i2);
            if (!currentItem.getValue().equals(value)) {
                i2++;
            } else {
                comboBox.setSelectedIndex(i2);
                break;
            }
        }
        comboBox.addItemListener(this);
    }
}
