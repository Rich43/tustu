package org.icepdf.ri.common.utility.annotation;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
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
import org.icepdf.core.pobjects.annotations.CircleAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/CircleAnnotationPanel.class */
public class CircleAnnotationPanel extends AnnotationPanelAdapter implements ItemListener, ActionListener {
    private static final int DEFAULT_LINE_THICKNESS = 0;
    private static final int DEFAULT_LINE_STYLE = 0;
    private static final int DEFAULT_STROKE_TYPE = 0;
    private static final int DEFAULT_FILL_TYPE = 1;
    private JComboBox lineThicknessBox;
    private JComboBox lineStyleBox;
    private JComboBox fillTypeBox;
    private JButton colorFillButton;
    private JButton colorBorderButton;
    private CircleAnnotation annotation;
    private static final Color DEFAULT_BORDER_COLOR = Color.RED;
    private static final Color DEFAULT_INTERIOR_COLOR = new Color(1, 1, 1);

    public CircleAnnotationPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(5, 2, 5, 2));
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
        this.annotation = (CircleAnnotation) this.currentAnnotationComponent.getAnnotation();
        applySelectedValue(this.lineThicknessBox, Float.valueOf(this.annotation.getLineThickness()));
        applySelectedValue(this.lineStyleBox, this.annotation.getLineStyle());
        applySelectedValue(this.fillTypeBox, Boolean.valueOf(this.annotation.isFillColor()));
        this.colorBorderButton.setBackground(this.annotation.getColor());
        this.colorFillButton.setBackground(this.annotation.getFillColor());
        safeEnable(this.lineThicknessBox, true);
        safeEnable(this.lineStyleBox, true);
        safeEnable(this.colorFillButton, true);
        safeEnable(this.fillTypeBox, true);
        safeEnable(this.colorBorderButton, true);
        setStrokeFillColorButtons();
    }

    private void setStrokeFillColorButtons() {
        CircleAnnotation circleAnnotation = (CircleAnnotation) this.currentAnnotationComponent.getAnnotation();
        if (this.annotation.isFillColor()) {
            this.colorFillButton.setBackground(circleAnnotation.getFillColor());
            safeEnable(this.colorFillButton, true);
        } else {
            safeEnable(this.colorFillButton, false);
        }
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        ValueLabelItem item = (ValueLabelItem) e2.getItem();
        if (e2.getStateChange() == 1) {
            if (e2.getSource() == this.lineThicknessBox) {
                this.annotation.getBorderStyle().setStrokeWidth(((Float) item.getValue()).floatValue());
            } else if (e2.getSource() == this.lineStyleBox) {
                this.annotation.getBorderStyle().setBorderStyle((Name) item.getValue());
            } else if (e2.getSource() == this.fillTypeBox) {
                this.annotation.setFillColor(((Boolean) item.getValue()).booleanValue());
                setStrokeFillColorButtons();
            }
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) throws HeadlessException {
        Color chosenColor;
        if (e2.getSource() == this.colorBorderButton) {
            Color chosenColor2 = JColorChooser.showDialog(this.colorBorderButton, this.messageBundle.getString("viewer.utilityPane.annotation.circle.colorBorderChooserTitle"), this.colorBorderButton.getBackground());
            if (chosenColor2 != null) {
                this.colorBorderButton.setBackground(chosenColor2);
                this.annotation.setColor(chosenColor2);
            }
        } else if (e2.getSource() == this.colorFillButton && (chosenColor = JColorChooser.showDialog(this.colorFillButton, this.messageBundle.getString("viewer.utilityPane.annotation.circle.colorInteriorChooserTitle"), this.colorFillButton.getBackground())) != null) {
            this.colorFillButton.setBackground(chosenColor);
            this.annotation.setFillColor(chosenColor);
        }
        updateCurrentAnnotation();
        this.currentAnnotationComponent.resetAppearanceShapes();
        this.currentAnnotationComponent.repaint();
    }

    private void createGUI() {
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.circle.appearance.title"), 1, 0));
        this.lineThicknessBox = new JComboBox(LINE_THICKNESS_LIST);
        this.lineThicknessBox.setSelectedIndex(0);
        this.lineThicknessBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.circle.lineThickness")));
        add(this.lineThicknessBox);
        this.lineStyleBox = new JComboBox(LINE_STYLE_LIST);
        this.lineStyleBox.setSelectedIndex(0);
        this.lineStyleBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.circle.lineStyle")));
        add(this.lineStyleBox);
        this.colorBorderButton = new JButton();
        this.colorBorderButton.addActionListener(this);
        this.colorBorderButton.setOpaque(true);
        this.colorBorderButton.setBackground(DEFAULT_BORDER_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.circle.colorBorderLabel")));
        add(this.colorBorderButton);
        this.fillTypeBox = new JComboBox(VISIBLE_TYPE_LIST);
        this.fillTypeBox.setSelectedIndex(1);
        this.fillTypeBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.circle.fillTypeLabel")));
        add(this.fillTypeBox);
        this.colorFillButton = new JButton();
        this.colorFillButton.addActionListener(this);
        this.colorFillButton.setOpaque(true);
        this.colorFillButton.setBackground(DEFAULT_INTERIOR_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.circle.colorInteriorLabel")));
        add(this.colorFillButton);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(this.lineThicknessBox, enabled);
        safeEnable(this.lineStyleBox, enabled);
        safeEnable(this.fillTypeBox, enabled);
        safeEnable(this.colorBorderButton, enabled);
        safeEnable(this.colorFillButton, enabled);
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
