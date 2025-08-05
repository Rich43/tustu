package org.icepdf.ri.common.utility.annotation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.icepdf.core.pobjects.actions.Action;
import org.icepdf.core.pobjects.actions.ActionFactory;
import org.icepdf.core.pobjects.actions.GoToAction;
import org.icepdf.core.pobjects.actions.LaunchAction;
import org.icepdf.core.pobjects.actions.URIAction;
import org.icepdf.core.pobjects.annotations.LinkAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/ActionsPanel.class */
public class ActionsPanel extends AnnotationPanelAdapter implements ListSelectionListener, ActionListener {
    private static final Logger logger = Logger.getLogger(ActionsPanel.class.toString());
    private DefaultListModel actionListModel;
    private JList actionList;
    private JButton addAction;
    private JButton editAction;
    private JButton removeAction;
    private String destinationLabel;
    private String uriActionLabel;
    private String goToActionLabel;
    private String launchActionLabel;
    private GoToActionDialog goToActionDialog;

    public ActionsPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(2, 1, 5, 5));
        setFocusable(true);
        createGUI();
        setEnabled(false);
        this.destinationLabel = this.messageBundle.getString("viewer.utilityPane.action.type.destination.label");
        this.uriActionLabel = this.messageBundle.getString("viewer.utilityPane.action.type.uriAction.label");
        this.goToActionLabel = this.messageBundle.getString("viewer.utilityPane.action.type.goToAction.label");
        this.launchActionLabel = this.messageBundle.getString("viewer.utilityPane.action.type.launchAction.label");
    }

    @Override // org.icepdf.ri.common.utility.annotation.AnnotationProperties
    public void setAnnotationComponent(AnnotationComponent annotation) {
        this.currentAnnotationComponent = annotation;
        this.actionListModel.clear();
        if (annotation.getAnnotation() != null && annotation.getAnnotation().getAction() != null) {
            addActionToList(annotation.getAnnotation().getAction());
            if (this.actionListModel.size() > 0) {
                this.actionList.setSelectedIndex(0);
            }
        } else if (annotation.getAnnotation() != null && (annotation.getAnnotation() instanceof LinkAnnotation)) {
            LinkAnnotation linkAnnotaiton = (LinkAnnotation) annotation.getAnnotation();
            if (linkAnnotaiton.getDestination() != null) {
                this.actionListModel.addElement(new ActionEntry(this.destinationLabel, null));
            }
        }
        refreshActionCrud();
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) throws HeadlessException, IllegalArgumentException {
        Object source = e2.getSource();
        if (this.currentAnnotationComponent == null) {
            logger.warning("No annotation was selected, edit is not possible.");
            return;
        }
        if (source == this.addAction) {
            addAction();
        } else if (source == this.editAction) {
            editAction();
        } else if (source == this.removeAction) {
            int option = JOptionPane.showConfirmDialog(this.controller.getViewerFrame(), this.messageBundle.getString("viewer.utilityPane.action.dialog.delete.msgs"), this.messageBundle.getString("viewer.utilityPane.action.dialog.delete.title"), 0);
            if (0 == option) {
                removeAction();
            }
            refreshActionCrud();
        }
        updateCurrentAnnotation();
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent e2) {
        if (!e2.getValueIsAdjusting()) {
            if (this.actionList.getSelectedIndex() == -1) {
                this.addAction.setEnabled(false);
                this.editAction.setEnabled(false);
                this.removeAction.setEnabled(false);
                return;
            }
            refreshActionCrud();
        }
    }

    private void addAction() throws IllegalArgumentException {
        String fileString;
        Object[] possibilities = {new ActionChoice(this.messageBundle.getString("viewer.utilityPane.action.type.goToAction.label"), 1), new ActionChoice(this.messageBundle.getString("viewer.utilityPane.action.type.launchAction.label"), 3), new ActionChoice(this.messageBundle.getString("viewer.utilityPane.action.type.uriAction.label"), 2)};
        ActionChoice actionType = (ActionChoice) JOptionPane.showInputDialog(this.controller.getViewerFrame(), this.messageBundle.getString("viewer.utilityPane.action.dialog.new.msgs"), this.messageBundle.getString("viewer.utilityPane.action.dialog.new.title"), -1, null, possibilities, null);
        if (actionType != null && actionType.getActionType() == 1) {
            showGoToActionDialog();
            return;
        }
        if (actionType != null && actionType.getActionType() == 2) {
            String uriString = showURIActionDialog(null);
            if (uriString != null && this.currentAnnotationComponent != null) {
                URIAction uriAction = (URIAction) ActionFactory.buildAction(this.currentAnnotationComponent.getAnnotation().getLibrary(), 2);
                uriAction.setURI(uriString);
                this.currentAnnotationComponent.getAnnotation().addAction(uriAction);
                this.actionListModel.addElement(new ActionEntry(this.messageBundle.getString("viewer.utilityPane.action.type.uriAction.label"), uriAction));
                return;
            }
            return;
        }
        if (actionType != null && actionType.getActionType() == 3 && (fileString = showLaunchActionDialog(null)) != null && this.currentAnnotationComponent != null) {
            LaunchAction launchAction = (LaunchAction) ActionFactory.buildAction(this.currentAnnotationComponent.getAnnotation().getLibrary(), 3);
            launchAction.setExternalFile(fileString);
            this.currentAnnotationComponent.getAnnotation().addAction(launchAction);
            this.actionListModel.addElement(new ActionEntry(this.messageBundle.getString("viewer.utilityPane.action.type.launchAction.label"), launchAction));
        }
    }

    private void editAction() throws IllegalArgumentException {
        LaunchAction launchAction;
        String oldLaunchValue;
        String newLaunchValue;
        ActionEntry actionEntry = (ActionEntry) this.actionListModel.getElementAt(this.actionList.getSelectedIndex());
        Action action = actionEntry.getAction();
        if (action instanceof URIAction) {
            URIAction uriAction = (URIAction) action;
            String oldURIValue = uriAction.getURI();
            String newURIValue = showURIActionDialog(oldURIValue);
            if (newURIValue != null && !oldURIValue.equals(newURIValue)) {
                uriAction.setURI(newURIValue);
                this.currentAnnotationComponent.getAnnotation().updateAction(uriAction);
            }
        } else if ((action instanceof GoToAction) || action == null) {
            showGoToActionDialog();
        }
        if ((action instanceof LaunchAction) && (newLaunchValue = showLaunchActionDialog((oldLaunchValue = (launchAction = (LaunchAction) action).getExternalFile()))) != null && !oldLaunchValue.equals(newLaunchValue)) {
            launchAction.setExternalFile(newLaunchValue);
            this.currentAnnotationComponent.getAnnotation().updateAction(launchAction);
        }
    }

    private void removeAction() {
        ActionEntry actionEntry = (ActionEntry) this.actionListModel.getElementAt(this.actionList.getSelectedIndex());
        Action action = actionEntry.getAction();
        if (action != null) {
            boolean success = this.currentAnnotationComponent.getAnnotation().deleteAction(action);
            if (success) {
                this.actionListModel.removeElementAt(this.actionList.getSelectedIndex());
                this.actionList.setSelectedIndex(-1);
                return;
            }
            return;
        }
        if (this.currentAnnotationComponent.getAnnotation() instanceof LinkAnnotation) {
            LinkAnnotation linkAnnotation = (LinkAnnotation) this.currentAnnotationComponent.getAnnotation();
            linkAnnotation.getEntries().remove(LinkAnnotation.DESTINATION_KEY);
            updateCurrentAnnotation();
            this.actionListModel.removeElementAt(this.actionList.getSelectedIndex());
            this.actionList.setSelectedIndex(-1);
        }
    }

    private String showURIActionDialog(String oldURIValue) {
        return (String) JOptionPane.showInputDialog(this.controller.getViewerFrame(), this.messageBundle.getString("viewer.utilityPane.action.dialog.uri.msgs"), this.messageBundle.getString("viewer.utilityPane.action.dialog.uri.title"), -1, null, null, oldURIValue);
    }

    private String showLaunchActionDialog(String oldLaunchValue) {
        return (String) JOptionPane.showInputDialog(this.controller.getViewerFrame(), this.messageBundle.getString("viewer.utilityPane.action.dialog.launch.msgs"), this.messageBundle.getString("viewer.utilityPane.action.dialog.launch.title"), -1, null, null, oldLaunchValue);
    }

    private void showGoToActionDialog() throws IllegalArgumentException {
        if (this.goToActionDialog != null) {
            this.goToActionDialog.dispose();
        }
        this.goToActionDialog = new GoToActionDialog(this.controller, this);
        this.goToActionDialog.setAnnotationComponent(this.currentAnnotationComponent);
        this.goToActionDialog.setVisible(true);
    }

    private void refreshActionCrud() {
        this.addAction.setEnabled(this.actionListModel.getSize() == 0);
        this.editAction.setEnabled(this.actionListModel.getSize() > 0);
        this.removeAction.setEnabled(this.actionListModel.getSize() > 0);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.actionList.setEnabled(enabled);
        this.actionList.setSelectedIndex(-1);
        boolean isSelectedIndex = this.actionList.getSelectedIndex() != -1;
        this.addAction.setEnabled(enabled && this.actionListModel.getSize() == 0);
        this.editAction.setEnabled(enabled && isSelectedIndex);
        this.removeAction.setEnabled(enabled && isSelectedIndex);
    }

    private void createGUI() {
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.action.selectionTitle"), 1, 0));
        this.actionListModel = new DefaultListModel();
        this.actionList = new JList(this.actionListModel);
        this.actionList.setSelectionMode(1);
        this.actionList.setVisibleRowCount(-1);
        this.actionList.addListSelectionListener(this);
        JScrollPane listScroller = new JScrollPane(this.actionList);
        listScroller.setPreferredSize(new Dimension(150, 50));
        add(listScroller);
        this.addAction = new JButton(this.messageBundle.getString("viewer.utilityPane.action.addAction"));
        this.addAction.setEnabled(false);
        this.addAction.addActionListener(this);
        this.editAction = new JButton(this.messageBundle.getString("viewer.utilityPane.action.editAction"));
        this.editAction.setEnabled(false);
        this.editAction.addActionListener(this);
        this.removeAction = new JButton(this.messageBundle.getString("viewer.utilityPane.action.removeAction"));
        this.removeAction.setEnabled(false);
        this.removeAction.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(this.addAction);
        buttonPanel.add(this.editAction);
        buttonPanel.add(this.removeAction);
        add(buttonPanel);
        revalidate();
    }

    public void clearActionList() {
        this.actionListModel.clear();
    }

    public void addActionToList(Action action) {
        if (action instanceof GoToAction) {
            this.actionListModel.addElement(new ActionEntry(this.goToActionLabel, action));
        } else if (action instanceof URIAction) {
            this.actionListModel.addElement(new ActionEntry(this.uriActionLabel, action));
        } else if (action instanceof LaunchAction) {
            this.actionListModel.addElement(new ActionEntry(this.launchActionLabel, action));
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/ActionsPanel$ActionEntry.class */
    class ActionEntry {
        String title;
        Action action;

        ActionEntry(String title) {
            this.title = title;
        }

        ActionEntry(String title, Action action) {
            this.action = action;
            this.title = title;
        }

        Action getAction() {
            return this.action;
        }

        public String toString() {
            return this.title;
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/ActionsPanel$ActionChoice.class */
    class ActionChoice {
        String title;
        int actionType;

        ActionChoice(String title, int actionType) {
            this.actionType = actionType;
            this.title = title;
        }

        int getActionType() {
            return this.actionType;
        }

        public String toString() {
            return this.title;
        }
    }
}
