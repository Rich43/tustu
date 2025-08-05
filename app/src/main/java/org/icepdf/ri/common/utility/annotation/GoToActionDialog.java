package org.icepdf.ri.common.utility.annotation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.NameTree;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.actions.Action;
import org.icepdf.core.pobjects.actions.ActionFactory;
import org.icepdf.core.pobjects.actions.GoToAction;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.LinkAnnotation;
import org.icepdf.ri.common.FloatTextFieldInputVerifier;
import org.icepdf.ri.common.FloatTextFieldKeyListener;
import org.icepdf.ri.common.PageNumberTextFieldInputVerifier;
import org.icepdf.ri.common.PageNumberTextFieldKeyListener;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;
import sun.security.ssl.SSLRecord;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/GoToActionDialog.class */
public class GoToActionDialog extends AnnotationDialogAdapter implements ActionListener, ItemListener {
    public static final String EMPTY_DESTINATION = "      ";
    private SwingController controller;
    private ResourceBundle messageBundle;
    private AnnotationComponent currentAnnotation;
    private ActionsPanel actionsPanel;
    private GridBagConstraints constraints;
    private JButton okButton;
    private JButton cancelButton;
    private JRadioButton implicitDestination;
    private JRadioButton namedDestination;
    private JComboBox implicitDestTypeComboBox;
    private JTextField pageNumberTextField;
    private JTextField topTextField;
    private JTextField bottomTextField;
    private JTextField leftTextField;
    private JTextField rightTextField;
    private JTextField zoomTextField;
    private JButton viewPositionButton;
    private JLabel destinationName;
    private JButton viewNamedDesButton;
    private NameTreeDialog nameTreeDialog;

    public GoToActionDialog(SwingController controller, ActionsPanel actionsPanel) {
        super(controller.getViewerFrame(), true);
        this.controller = controller;
        this.messageBundle = this.controller.getMessageBundle();
        this.actionsPanel = actionsPanel;
        setTitle(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.title"));
        setGui();
    }

    @Override // org.icepdf.ri.common.utility.annotation.AnnotationProperties
    public void setAnnotationComponent(AnnotationComponent annotation) throws IllegalArgumentException {
        this.currentAnnotation = annotation;
        Action action = this.currentAnnotation.getAnnotation().getAction();
        Destination dest = null;
        if (action != null && (action instanceof GoToAction)) {
            dest = ((GoToAction) action).getDestination();
        } else if (action == null && (this.currentAnnotation.getAnnotation() instanceof LinkAnnotation)) {
            LinkAnnotation linkAnnotation = (LinkAnnotation) this.currentAnnotation.getAnnotation();
            dest = linkAnnotation.getDestination();
        }
        if (this.controller.getDocument().getCatalog().getNames() == null || this.controller.getDocument().getCatalog().getNames().getDestsNameTree() == null) {
            implicitDestinationFieldsEnabled(true);
            clearImplicitDestinations(true);
            this.namedDestination.setEnabled(false);
        } else {
            this.namedDestination.setEnabled(true);
        }
        if (dest != null) {
            clearImplicitDestinations(false);
            clearImplicitDestinations(true);
            if (dest.getNamedDestination() == null) {
                implicitDestinationFieldsEnabled(true);
                Name type = dest.getType();
                applySelectedValue(this.implicitDestTypeComboBox, type);
                enableFitTypeFields(type);
                applyTypeValues(dest, type);
                this.pageNumberTextField.setText(String.valueOf(this.controller.getDocument().getPageTree().getPageNumber(dest.getPageReference()) + 1));
                return;
            }
            implicitDestinationFieldsEnabled(false);
            this.destinationName.setText(dest.getNamedDestination().toString());
            return;
        }
        applySelectedValue(this.implicitDestTypeComboBox, Destination.TYPE_FIT);
        enableFitTypeFields(Destination.TYPE_FIT);
    }

    private void saveActionState() throws NumberFormatException {
        Destination destination;
        Annotation annotation = this.currentAnnotation.getAnnotation();
        if (this.implicitDestination.isSelected()) {
            Name fitType = (Name) ((ValueLabelItem) this.implicitDestTypeComboBox.getSelectedItem()).getValue();
            int pageNumber = Integer.parseInt(this.pageNumberTextField.getText());
            Reference pageReference = this.controller.getDocument().getPageTree().getPageReference(pageNumber - 1);
            List destArray = null;
            if (fitType.equals(Destination.TYPE_FIT) || fitType.equals(Destination.TYPE_FITB)) {
                destArray = Destination.destinationSyntax(pageReference, fitType);
            } else if (fitType.equals(Destination.TYPE_FITH) || fitType.equals(Destination.TYPE_FITBH) || fitType.equals(Destination.TYPE_FITV) || fitType.equals(Destination.TYPE_FITBV)) {
                Object top = parseDestCoordinate(this.topTextField.getText());
                destArray = Destination.destinationSyntax(pageReference, fitType, top);
            } else if (fitType.equals(Destination.TYPE_XYZ)) {
                Object left = parseDestCoordinate(this.leftTextField.getText());
                Object top2 = parseDestCoordinate(this.topTextField.getText());
                Object zoom = parseDestCoordinate(this.zoomTextField.getText());
                destArray = Destination.destinationSyntax(pageReference, fitType, left, top2, zoom);
            } else if (fitType.equals(Destination.TYPE_FITR)) {
                Object left2 = parseDestCoordinate(this.leftTextField.getText());
                Object bottom = parseDestCoordinate(this.leftTextField.getText());
                Object right = parseDestCoordinate(this.leftTextField.getText());
                Object top3 = parseDestCoordinate(this.leftTextField.getText());
                destArray = Destination.destinationSyntax(pageReference, fitType, left2, bottom, right, top3);
            }
            destination = new Destination(annotation.getLibrary(), destArray);
        } else {
            destination = new Destination(annotation.getLibrary(), new Name(this.destinationName.getText()));
        }
        GoToAction action = (GoToAction) annotation.getAction();
        if (action == null) {
            GoToAction action2 = (GoToAction) ActionFactory.buildAction(annotation.getLibrary(), 1);
            action2.setDestination(destination);
            annotation.addAction(action2);
            this.actionsPanel.clearActionList();
            this.actionsPanel.addActionToList(action2);
            return;
        }
        action.setDestination(destination);
        annotation.updateAction(action);
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) throws NumberFormatException {
        if (e2.getSource() == this.okButton) {
            saveActionState();
            dispose();
            return;
        }
        if (e2.getSource() == this.cancelButton) {
            dispose();
            return;
        }
        if (e2.getSource() == this.viewNamedDesButton) {
            NameTree nameTree = this.controller.getDocument().getCatalog().getNames().getDestsNameTree();
            if (nameTree != null) {
                this.nameTreeDialog = new NameTreeDialog(this.controller, true, nameTree);
                this.nameTreeDialog.setDestinationName(this.destinationName);
                this.nameTreeDialog.setVisible(true);
                this.nameTreeDialog.dispose();
                return;
            }
            return;
        }
        if (e2.getSource() == this.viewPositionButton) {
        }
    }

    @Override // java.awt.Window
    public void dispose() {
        setVisible(false);
        super.dispose();
        if (this.nameTreeDialog != null) {
            this.nameTreeDialog.dispose();
        }
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        if (e2.getStateChange() == 1 || e2.getStateChange() == 2) {
            if (e2.getSource() == this.implicitDestination) {
                implicitDestinationFieldsEnabled(e2.getStateChange() == 1);
                if (this.implicitDestination.isSelected() && this.implicitDestTypeComboBox.getSelectedItem() == null) {
                    applySelectedValue(this.implicitDestTypeComboBox, Destination.TYPE_FIT);
                    enableFitTypeFields(Destination.TYPE_FIT);
                    return;
                }
                return;
            }
            if (e2.getSource() == this.implicitDestTypeComboBox) {
                ValueLabelItem valueItem = (ValueLabelItem) e2.getItem();
                Name fitType = (Name) valueItem.getValue();
                enableFitTypeFields(fitType);
            }
        }
    }

    protected void setGui() {
        JPanel goToActionPanel = new JPanel();
        goToActionPanel.setAlignmentY(0.0f);
        GridBagLayout layout = new GridBagLayout();
        goToActionPanel.setLayout(layout);
        this.constraints = new GridBagConstraints();
        this.constraints.fill = 0;
        this.constraints.weightx = 1.0d;
        this.constraints.anchor = 11;
        this.constraints.anchor = 13;
        this.constraints.insets = new Insets(5, 5, 5, 5);
        JPanel explicitDestinationSubpane = new JPanel(new GridLayout(4, 4, 10, 5));
        explicitDestinationSubpane.setBorder(new EmptyBorder(0, 40, 0, 0));
        explicitDestinationSubpane.add(new JLabel(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.label")));
        this.implicitDestTypeComboBox = buildImplicitDestTypes();
        this.implicitDestTypeComboBox.addItemListener(this);
        explicitDestinationSubpane.add(this.implicitDestTypeComboBox);
        explicitDestinationSubpane.add(new JLabel(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.page.label")));
        this.pageNumberTextField = buildDocumentPageNumbers();
        explicitDestinationSubpane.add(this.pageNumberTextField);
        explicitDestinationSubpane.add(new JLabel(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.top.label")));
        this.topTextField = buildFloatTextField();
        explicitDestinationSubpane.add(this.topTextField);
        explicitDestinationSubpane.add(new JLabel(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.bottom.label")));
        this.bottomTextField = buildFloatTextField();
        explicitDestinationSubpane.add(this.bottomTextField);
        explicitDestinationSubpane.add(new JLabel(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.left.label")));
        this.leftTextField = buildFloatTextField();
        explicitDestinationSubpane.add(this.leftTextField);
        explicitDestinationSubpane.add(new JLabel(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.right.label")));
        this.rightTextField = buildFloatTextField();
        explicitDestinationSubpane.add(this.rightTextField);
        explicitDestinationSubpane.add(new JLabel(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.zoom.label")));
        this.zoomTextField = buildFloatTextField();
        explicitDestinationSubpane.add(this.zoomTextField);
        explicitDestinationSubpane.add(new JLabel());
        explicitDestinationSubpane.add(new JLabel());
        JPanel pageNumberPane = new JPanel(new BorderLayout(5, 5));
        this.implicitDestination = new JRadioButton(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.explicitDestination.title"), true);
        this.implicitDestination.addItemListener(this);
        pageNumberPane.add(this.implicitDestination, "North");
        pageNumberPane.add(explicitDestinationSubpane, BorderLayout.CENTER);
        JPanel namedDestSubpane = new JPanel(new FlowLayout(0, 5, 5));
        namedDestSubpane.setBorder(new EmptyBorder(0, 40, 0, 0));
        namedDestSubpane.add(new JLabel(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.name.label")));
        this.destinationName = new JLabel(EMPTY_DESTINATION);
        namedDestSubpane.add(this.destinationName);
        this.viewNamedDesButton = new JButton(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.browse"));
        this.viewNamedDesButton.addActionListener(this);
        namedDestSubpane.add(this.viewNamedDesButton);
        JPanel namedDestPane = new JPanel(new BorderLayout(5, 5));
        this.namedDestination = new JRadioButton(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.nameDestination.title"), false);
        namedDestPane.add(this.namedDestination, "North");
        namedDestPane.add(namedDestSubpane, BorderLayout.CENTER);
        ButtonGroup actionButtonGroup = new ButtonGroup();
        actionButtonGroup.add(this.implicitDestination);
        actionButtonGroup.add(this.namedDestination);
        this.okButton = new JButton(this.messageBundle.getString("viewer.button.ok.label"));
        this.okButton.setMnemonic(this.messageBundle.getString("viewer.button.ok.mnemonic").charAt(0));
        this.okButton.addActionListener(this);
        this.cancelButton = new JButton(this.messageBundle.getString("viewer.button.cancel.label"));
        this.cancelButton.setMnemonic(this.messageBundle.getString("viewer.button.cancel.mnemonic").charAt(0));
        this.cancelButton.addActionListener(this);
        JPanel okCancelPanel = new JPanel(new FlowLayout());
        okCancelPanel.add(this.okButton);
        okCancelPanel.add(this.cancelButton);
        this.constraints.insets = new Insets(5, 5, 5, 5);
        this.constraints.anchor = 17;
        addGB(goToActionPanel, pageNumberPane, 0, 0, 1, 1);
        addGB(goToActionPanel, namedDestPane, 0, 1, 1, 1);
        this.constraints.insets = new Insets(15, 5, 5, 5);
        this.constraints.anchor = 10;
        addGB(goToActionPanel, okCancelPanel, 0, 2, 1, 1);
        getContentPane().add(goToActionPanel);
        setSize(new Dimension(500, SSLRecord.maxPlaintextPlusSize));
        setLocationRelativeTo(this.controller.getViewerFrame());
    }

    private Object parseDestCoordinate(String fieldValue) {
        try {
            return Float.valueOf(Float.parseFloat(fieldValue));
        } catch (NumberFormatException e2) {
            return null;
        }
    }

    private String getDestCoordinate(Float coord) {
        if (coord != null) {
            return String.valueOf(coord);
        }
        return "";
    }

    private void applyTypeValues(Destination dest, Name type) {
        if (Destination.TYPE_XYZ.equals(type)) {
            this.leftTextField.setText(getDestCoordinate(dest.getLeft()));
            this.topTextField.setText(getDestCoordinate(dest.getTop()));
            this.zoomTextField.setText(getDestCoordinate(dest.getZoom()));
            return;
        }
        if (!Destination.TYPE_FIT.equals(type)) {
            if (Destination.TYPE_FITH.equals(type)) {
                this.topTextField.setText(getDestCoordinate(dest.getTop()));
                return;
            }
            if (Destination.TYPE_FITV.equals(type)) {
                this.leftTextField.setText(getDestCoordinate(dest.getLeft()));
                return;
            }
            if (Destination.TYPE_FITR.equals(type)) {
                this.leftTextField.setText(getDestCoordinate(dest.getLeft()));
                this.rightTextField.setText(getDestCoordinate(dest.getRight()));
                this.topTextField.setText(getDestCoordinate(dest.getTop()));
                this.bottomTextField.setText(getDestCoordinate(dest.getBottom()));
                return;
            }
            if (!Destination.TYPE_FITB.equals(type)) {
                if (Destination.TYPE_FITH.equals(type)) {
                    this.topTextField.setText(getDestCoordinate(dest.getTop()));
                } else if (Destination.TYPE_FITBV.equals(type)) {
                    this.leftTextField.setText(getDestCoordinate(dest.getLeft()));
                }
            }
        }
    }

    private JTextField buildDocumentPageNumbers() {
        final JTextField textField = new JTextField();
        textField.setInputVerifier(new PageNumberTextFieldInputVerifier());
        textField.addKeyListener(new PageNumberTextFieldKeyListener());
        textField.addFocusListener(new FocusAdapter() { // from class: org.icepdf.ri.common.utility.annotation.GoToActionDialog.1
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusLost(FocusEvent e2) throws NumberFormatException {
                Object src = e2.getSource();
                if (src != null && src == textField) {
                    String fieldValue = textField.getText();
                    int currentValue = Integer.parseInt(fieldValue);
                    int maxValue = GoToActionDialog.this.controller.getDocument().getNumberOfPages();
                    if (currentValue > maxValue) {
                        textField.setText(String.valueOf(maxValue));
                    }
                }
            }
        });
        textField.setText("1");
        return textField;
    }

    private JTextField buildFloatTextField() {
        final JTextField textField = new JTextField();
        textField.setInputVerifier(new FloatTextFieldInputVerifier());
        textField.addKeyListener(new FloatTextFieldKeyListener());
        textField.addFocusListener(new FocusAdapter() { // from class: org.icepdf.ri.common.utility.annotation.GoToActionDialog.2
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusLost(FocusEvent e2) throws NumberFormatException {
                Object src = e2.getSource();
                if (src != null && src == textField) {
                    String fieldValue = textField.getText();
                    if ("".equals(fieldValue)) {
                        return;
                    }
                    float currentValue = Float.parseFloat(fieldValue);
                    textField.setText(String.valueOf(currentValue));
                }
            }
        });
        return textField;
    }

    private JComboBox buildImplicitDestTypes() {
        ValueLabelItem[] destTypes = {new ValueLabelItem(Destination.TYPE_XYZ, this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.xyz.label")), new ValueLabelItem(Destination.TYPE_FITH, this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.fith.label")), new ValueLabelItem(Destination.TYPE_FITR, this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.fitr.label")), new ValueLabelItem(Destination.TYPE_FIT, this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.fit.label")), new ValueLabelItem(Destination.TYPE_FITB, this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.fitb.label")), new ValueLabelItem(Destination.TYPE_FITBH, this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.fitbh.label")), new ValueLabelItem(Destination.TYPE_FITBV, this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.fitbv.label")), new ValueLabelItem(Destination.TYPE_FITBV, this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.type.fitbv.label"))};
        return new JComboBox(destTypes);
    }

    private void addGB(JPanel layout, Component component, int x2, int y2, int rowSpan, int colSpan) {
        this.constraints.gridx = x2;
        this.constraints.gridy = y2;
        this.constraints.gridwidth = rowSpan;
        this.constraints.gridheight = colSpan;
        layout.add(component, this.constraints);
    }

    private void implicitDestinationFieldsEnabled(boolean isImplictDestSelected) {
        this.implicitDestination.setSelected(isImplictDestSelected);
        this.namedDestination.setSelected(!isImplictDestSelected);
        this.pageNumberTextField.setEnabled(isImplictDestSelected);
        this.implicitDestTypeComboBox.setEnabled(isImplictDestSelected);
        this.leftTextField.setEnabled(isImplictDestSelected);
        this.topTextField.setEnabled(isImplictDestSelected);
        this.zoomTextField.setEnabled(isImplictDestSelected);
        this.destinationName.setEnabled(!isImplictDestSelected);
        this.viewNamedDesButton.setEnabled(!isImplictDestSelected);
    }

    private void clearImplicitDestinations(boolean isImplictDestSelected) throws IllegalArgumentException {
        if (!isImplictDestSelected) {
            this.pageNumberTextField.setText("");
            this.implicitDestTypeComboBox.setSelectedIndex(-1);
            this.leftTextField.setText("");
            this.topTextField.setText("");
            this.zoomTextField.setText("");
            return;
        }
        this.destinationName.setText(EMPTY_DESTINATION);
    }

    private void enableFitTypeFields(Name fitType) {
        if (fitType.equals(Destination.TYPE_FIT) || fitType.equals(Destination.TYPE_FITB)) {
            setFitTypesEnabled(false, false, false, false, false);
            return;
        }
        if (fitType.equals(Destination.TYPE_FITH) || fitType.equals(Destination.TYPE_FITBH)) {
            setFitTypesEnabled(true, false, false, false, false);
            return;
        }
        if (fitType.equals(Destination.TYPE_FITV) || fitType.equals(Destination.TYPE_FITBV)) {
            setFitTypesEnabled(false, false, true, false, false);
        } else if (fitType.equals(Destination.TYPE_XYZ)) {
            setFitTypesEnabled(true, false, true, false, true);
        } else if (fitType.equals(Destination.TYPE_FITR)) {
            setFitTypesEnabled(true, true, true, true, false);
        }
    }

    private void setFitTypesEnabled(boolean top, boolean bottom, boolean left, boolean right, boolean zoom) {
        this.topTextField.setEnabled(top);
        this.bottomTextField.setEnabled(bottom);
        this.leftTextField.setEnabled(left);
        this.rightTextField.setEnabled(right);
        this.zoomTextField.setEnabled(zoom);
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
