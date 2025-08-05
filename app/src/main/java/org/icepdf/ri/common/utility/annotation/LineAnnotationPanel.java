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
import org.icepdf.core.pobjects.annotations.LineAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/LineAnnotationPanel.class */
public class LineAnnotationPanel extends AnnotationPanelAdapter implements ItemListener, ActionListener {
    private static final int DEFAULT_START_END_TYPE = 0;
    private static final int DEFAULT_END_END_TYPE = 0;
    private static final int DEFAULT_LINE_THICKNESS = 0;
    private static final int DEFAULT_LINE_STYLE = 0;
    private static final Color DEFAULT_BORDER_COLOR = Color.DARK_GRAY;
    private static final Color DEFAULT_FILL_COLOR = Color.DARK_GRAY;
    private static ValueLabelItem[] END_TYPE_LIST;
    private JComboBox startEndTypeBox;
    private JComboBox endEndTypeBox;
    private JComboBox lineThicknessBox;
    private JComboBox lineStyleBox;
    private JButton colorButton;
    private JButton internalColorButton;
    private LineAnnotation annotation;

    public LineAnnotationPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(6, 2, 5, 2));
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
        this.annotation = (LineAnnotation) this.currentAnnotationComponent.getAnnotation();
        applySelectedValue(this.startEndTypeBox, this.annotation.getStartArrow());
        applySelectedValue(this.endEndTypeBox, this.annotation.getEndArrow());
        applySelectedValue(this.lineThicknessBox, Float.valueOf(this.annotation.getLineThickness()));
        applySelectedValue(this.lineStyleBox, this.annotation.getLineStyle());
        this.colorButton.setBackground(this.annotation.getColor());
        this.internalColorButton.setBackground(this.annotation.getInteriorColor());
        safeEnable(this.startEndTypeBox, true);
        safeEnable(this.endEndTypeBox, true);
        safeEnable(this.lineThicknessBox, true);
        safeEnable(this.lineStyleBox, true);
        safeEnable(this.colorButton, true);
        safeEnable(this.internalColorButton, true);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        ValueLabelItem item = (ValueLabelItem) e2.getItem();
        if (e2.getStateChange() == 1) {
            if (e2.getSource() == this.startEndTypeBox) {
                this.annotation.setStartArrow((Name) item.getValue());
            } else if (e2.getSource() == this.endEndTypeBox) {
                this.annotation.setEndArrow((Name) item.getValue());
            } else if (e2.getSource() == this.lineThicknessBox) {
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
    public void actionPerformed(ActionEvent e2) throws HeadlessException {
        Color chosenColor;
        if (e2.getSource() == this.colorButton) {
            Color chosenColor2 = JColorChooser.showDialog(this.colorButton, this.messageBundle.getString("viewer.utilityPane.annotation.line.colorChooserTitle"), this.colorButton.getBackground());
            if (chosenColor2 != null) {
                this.colorButton.setBackground(chosenColor2);
                this.annotation.setColor(chosenColor2);
                updateCurrentAnnotation();
                this.currentAnnotationComponent.resetAppearanceShapes();
                this.currentAnnotationComponent.repaint();
                return;
            }
            return;
        }
        if (e2.getSource() == this.internalColorButton && (chosenColor = JColorChooser.showDialog(this.internalColorButton, this.messageBundle.getString("viewer.utilityPane.annotation.line.colorInternalChooserTitle"), this.internalColorButton.getBackground())) != null) {
            this.internalColorButton.setBackground(chosenColor);
            this.annotation.setInteriorColor(chosenColor);
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    private void createGUI() {
        if (END_TYPE_LIST == null) {
            END_TYPE_LIST = new ValueLabelItem[]{new ValueLabelItem(LineAnnotation.LINE_END_NONE, this.messageBundle.getString("viewer.utilityPane.annotation.line.end.none")), new ValueLabelItem(LineAnnotation.LINE_END_OPEN_ARROW, this.messageBundle.getString("viewer.utilityPane.annotation.line.end.openArrow")), new ValueLabelItem(LineAnnotation.LINE_END_CLOSED_ARROW, this.messageBundle.getString("viewer.utilityPane.annotation.line.end.closedArrow")), new ValueLabelItem(LineAnnotation.LINE_END_DIAMOND, this.messageBundle.getString("viewer.utilityPane.annotation.line.end.diamond")), new ValueLabelItem(LineAnnotation.LINE_END_SQUARE, this.messageBundle.getString("viewer.utilityPane.annotation.line.end.square")), new ValueLabelItem(LineAnnotation.LINE_END_CIRCLE, this.messageBundle.getString("viewer.utilityPane.annotation.line.end.circle"))};
        }
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.line.appearance.title"), 1, 0));
        this.startEndTypeBox = new JComboBox(END_TYPE_LIST);
        this.startEndTypeBox.setSelectedIndex(0);
        this.startEndTypeBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.line.startStyle")));
        add(this.startEndTypeBox);
        this.endEndTypeBox = new JComboBox(END_TYPE_LIST);
        this.endEndTypeBox.setSelectedIndex(0);
        this.endEndTypeBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.line.endStyle")));
        add(this.endEndTypeBox);
        this.lineThicknessBox = new JComboBox(LINE_THICKNESS_LIST);
        this.lineThicknessBox.setSelectedIndex(0);
        this.lineThicknessBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.line.lineThickness")));
        add(this.lineThicknessBox);
        this.lineStyleBox = new JComboBox(LINE_STYLE_LIST);
        this.lineStyleBox.setSelectedIndex(0);
        this.lineStyleBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.line.lineStyle")));
        add(this.lineStyleBox);
        this.colorButton = new JButton();
        this.colorButton.addActionListener(this);
        this.colorButton.setOpaque(true);
        this.colorButton.setBackground(DEFAULT_BORDER_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.line.colorLabel")));
        add(this.colorButton);
        this.internalColorButton = new JButton();
        this.internalColorButton.addActionListener(this);
        this.internalColorButton.setOpaque(true);
        this.internalColorButton.setBackground(DEFAULT_FILL_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.line.colorInternalLabel")));
        add(this.internalColorButton);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(this.startEndTypeBox, enabled);
        safeEnable(this.endEndTypeBox, enabled);
        safeEnable(this.lineThicknessBox, enabled);
        safeEnable(this.lineStyleBox, enabled);
        safeEnable(this.colorButton, enabled);
        safeEnable(this.internalColorButton, enabled);
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
