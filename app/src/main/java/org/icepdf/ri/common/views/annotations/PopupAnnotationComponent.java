package org.icepdf.ri.common.views.annotations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.MarkupAnnotation;
import org.icepdf.core.pobjects.annotations.PopupAnnotation;
import org.icepdf.core.pobjects.annotations.TextAnnotation;
import org.icepdf.ri.common.tools.TextAnnotationHandler;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.AnnotationComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.ResizableBorder;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/PopupAnnotationComponent.class */
public class PopupAnnotationComponent extends AbstractAnnotationComponent implements TreeSelectionListener, ActionListener, DocumentListener {
    public static Color backgroundColor = new Color(252, 253, 227);
    public static Color borderColor = new Color(153, 153, 153);
    protected PopupAnnotation popupAnnotation;
    private GridBagConstraints constraints;
    protected JPanel commentPanel;
    protected JTextArea textArea;
    protected JLabel creationLabel;
    protected JButton minimizeButton;
    protected JTree commentTree;
    protected JScrollPane commentTreeScrollPane;
    protected MarkupAnnotation selectedMarkupAnnotation;
    protected JMenuItem replyMenuItem;
    protected JMenuItem deleteMenuItem;
    protected JMenuItem statusNoneMenuItem;
    protected JMenuItem statusAcceptedItem;
    protected JMenuItem statusCancelledMenuItem;
    protected JMenuItem statusCompletedMenuItem;
    protected JMenuItem statusRejectedMenuItem;
    protected JMenuItem openAllMenuItem;
    protected JMenuItem minimizeAllMenuItem;
    protected JPopupMenu contextMenu;

    public PopupAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) throws IllegalArgumentException {
        super(annotation, documentViewController, pageViewComponent, documentViewModel);
        this.isEditable = true;
        this.isRollover = false;
        this.isMovable = true;
        this.isResizable = true;
        this.isShowInvisibleBorder = false;
        if (annotation instanceof PopupAnnotation) {
            this.popupAnnotation = (PopupAnnotation) annotation;
            this.popupAnnotation.init();
        }
        boolean isVisible = this.popupAnnotation.isOpen();
        setVisible(isVisible);
        buildGUI();
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent me) {
        if (!this.annotation.getFlagLocked() && !this.annotation.getFlagReadOnly()) {
            ResizableBorder border = (ResizableBorder) getBorder();
            setCursor(Cursor.getPredefinedCursor(border.getCursor(me)));
        }
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        this.isMousePressed = true;
        if (isInteractiveAnnotationsEnabled && !this.annotation.getFlagReadOnly()) {
            initiateMouseMoved(e2);
        }
        repaint();
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
        super.mouseEntered(e2);
        setCursor(Cursor.getPredefinedCursor(0));
    }

    private void buildGUI() throws IllegalArgumentException {
        List<Annotation> annotations = this.pageViewComponent.getPage().getAnnotations();
        MarkupAnnotation parentAnnotation = this.popupAnnotation.getParent();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        boolean isIRT = buildCommentTree(parentAnnotation, annotations, root);
        this.commentTree = new JTree(root);
        this.commentTree.setRootVisible(true);
        this.commentTree.setExpandsSelectedPaths(true);
        this.commentTree.setShowsRootHandles(true);
        this.commentTree.setScrollsOnExpand(true);
        this.commentTree.setRootVisible(false);
        this.commentTree.getSelectionModel().setSelectionMode(1);
        this.commentTree.addTreeSelectionListener(this);
        refreshTree(this.commentTree);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);
        this.commentTree.setCellRenderer(renderer);
        this.commentTree.addMouseListener(new PopupTreeListener());
        this.commentTreeScrollPane = new JScrollPane(this.commentTree);
        this.commentTree.setSelectionRow(0);
        this.selectedMarkupAnnotation = parentAnnotation;
        this.minimizeButton = new JButton("  _  ");
        this.minimizeButton.addActionListener(this);
        this.minimizeButton.setBackground(backgroundColor);
        this.minimizeButton.setOpaque(true);
        this.minimizeButton.setContentAreaFilled(false);
        this.minimizeButton.setBorder(BorderFactory.createLineBorder(borderColor));
        this.minimizeButton.setBorderPainted(true);
        this.minimizeButton.addActionListener(this);
        String contents = this.popupAnnotation.getParent() != null ? this.popupAnnotation.getParent().getContents() : "";
        this.textArea = new JTextArea(contents != null ? contents : "");
        this.textArea.setFont(new JLabel().getFont());
        this.textArea.setBorder(BorderFactory.createLineBorder(borderColor));
        this.textArea.setLineWrap(true);
        this.textArea.getDocument().addDocumentListener(this);
        this.creationLabel = new JLabel();
        if (this.selectedMarkupAnnotation != null && this.selectedMarkupAnnotation.getCreationDate() != null) {
            this.creationLabel.setText(this.selectedMarkupAnnotation.getCreationDate().toString());
        }
        GridBagLayout layout = new GridBagLayout();
        this.commentPanel = new JPanel(layout);
        this.commentPanel.setBackground(backgroundColor);
        this.commentPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        setLayout(new BorderLayout());
        add(this.commentPanel);
        this.constraints = new GridBagConstraints();
        this.constraints.fill = 0;
        this.constraints.weightx = 1.0d;
        this.constraints.weighty = 0.0d;
        this.constraints.anchor = 11;
        this.constraints.anchor = 17;
        this.constraints.insets = new Insets(1, 5, 1, 5);
        this.constraints.fill = 13;
        this.constraints.weightx = 0.0d;
        String titleText = (this.selectedMarkupAnnotation == null || this.selectedMarkupAnnotation.getTitleText() == null) ? "" : this.selectedMarkupAnnotation.getTitleText();
        String title = titleText;
        addGB(this.commentPanel, new JLabel(title), 0, 0, 1, 1);
        this.constraints.fill = 0;
        this.constraints.weightx = 0.0d;
        addGB(this.commentPanel, this.minimizeButton, 2, 0, 1, 1);
        this.constraints.fill = 1;
        this.constraints.insets = new Insets(1, 5, 1, 5);
        this.constraints.weightx = 1.0d;
        this.constraints.weighty = 0.6d;
        this.commentTreeScrollPane.setVisible(isIRT);
        addGB(this.commentPanel, this.commentTreeScrollPane, 0, 1, 3, 1);
        this.constraints.insets = new Insets(1, 5, 1, 5);
        this.constraints.weightx = 1.0d;
        this.constraints.weighty = 0.0d;
        this.constraints.fill = 13;
        addGB(this.commentPanel, this.creationLabel, 0, 2, 1, 1);
        this.constraints.fill = 1;
        this.constraints.insets = new Insets(1, 5, 5, 5);
        this.constraints.weightx = 1.0d;
        this.constraints.weighty = 0.4d;
        addGB(this.commentPanel, this.textArea, 0, 3, 3, 1);
        buildContextMenu();
    }

    private void refreshTree(JTree tree) {
        ((DefaultTreeModel) tree.getModel()).reload();
        for (int i2 = 0; i2 < tree.getRowCount(); i2++) {
            tree.expandRow(i2);
        }
    }

    public void buildContextMenu() {
        ResourceBundle messages = this.documentViewController.getParentController().getMessageBundle();
        this.replyMenuItem = new JMenuItem(messages.getString("viewer.annotation.popup.reply.label"));
        this.deleteMenuItem = new JMenuItem(messages.getString("viewer.annotation.popup.delete.label"));
        this.statusNoneMenuItem = new JMenuItem(messages.getString("viewer.annotation.popup.status.none.label"));
        this.statusAcceptedItem = new JMenuItem(messages.getString("viewer.annotation.popup.status.accepted.label"));
        this.statusCancelledMenuItem = new JMenuItem(messages.getString("viewer.annotation.popup.status.cancelled.label"));
        this.statusCompletedMenuItem = new JMenuItem(messages.getString("viewer.annotation.popup.status.completed.label"));
        this.statusRejectedMenuItem = new JMenuItem(messages.getString("viewer.annotation.popup.status.rejected.label"));
        this.openAllMenuItem = new JMenuItem(messages.getString("viewer.annotation.popup.openAll.label"));
        this.minimizeAllMenuItem = new JMenuItem(messages.getString("viewer.annotation.popup.minimizeAll.label"));
        this.contextMenu = new JPopupMenu();
        this.replyMenuItem.addActionListener(this);
        this.contextMenu.add(this.replyMenuItem);
        this.deleteMenuItem.addActionListener(this);
        this.contextMenu.add(this.deleteMenuItem);
        this.contextMenu.addSeparator();
        JMenu submenu = new JMenu(messages.getString("viewer.annotation.popup.status.label"));
        this.statusNoneMenuItem.addActionListener(this);
        submenu.add(this.statusNoneMenuItem);
        this.statusAcceptedItem.addActionListener(this);
        submenu.add(this.statusAcceptedItem);
        this.statusCancelledMenuItem.addActionListener(this);
        submenu.add(this.statusCancelledMenuItem);
        this.statusCompletedMenuItem.addActionListener(this);
        submenu.add(this.statusCompletedMenuItem);
        this.statusRejectedMenuItem.addActionListener(this);
        submenu.add(this.statusRejectedMenuItem);
        this.contextMenu.add((JMenuItem) submenu);
        this.contextMenu.addSeparator();
        this.openAllMenuItem.addActionListener(this);
        this.contextMenu.add(this.openAllMenuItem);
        this.minimizeAllMenuItem.addActionListener(this);
        this.contextMenu.add(this.minimizeAllMenuItem);
        MouseListener popupListener = new PopupListener();
        this.textArea.addMouseListener(popupListener);
        this.commentPanel.addMouseListener(popupListener);
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) {
        Object source = e2.getSource();
        if (source == null) {
            return;
        }
        if (source == this.minimizeButton) {
            setVisible(false);
            this.popupAnnotation.setOpen(false);
            return;
        }
        if (source == this.replyMenuItem) {
            Object[] argument = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.replyTo.label"));
            String annotationTitle = formatter.format(argument);
            createNewTextAnnotation(annotationTitle, "", TextAnnotation.STATE_MODEL_REVIEW, "None");
            return;
        }
        if (source == this.deleteMenuItem) {
            AnnotationComponent annotationComponent = findAnnotationComponent(this.selectedMarkupAnnotation);
            this.documentViewController.deleteAnnotation(annotationComponent);
            AnnotationComponent annotationComponent2 = findAnnotationComponent(this.selectedMarkupAnnotation.getPopupAnnotation());
            this.documentViewController.deleteAnnotation(annotationComponent2);
            removeMarkupInReplyTo(this.selectedMarkupAnnotation.getPObjectReference());
            List<Annotation> annotations = this.pageViewComponent.getPage().getAnnotations();
            MarkupAnnotation parentAnnotation = this.popupAnnotation.getParent();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
            boolean isIRT = buildCommentTree(parentAnnotation, annotations, root);
            this.commentTree.removeTreeSelectionListener(this);
            ((DefaultTreeModel) this.commentTree.getModel()).setRoot(root);
            this.commentTree.addTreeSelectionListener(this);
            refreshTree(this.commentTree);
            if (!isIRT) {
                this.commentTreeScrollPane.setVisible(false);
            }
            this.commentPanel.revalidate();
            return;
        }
        if (source == this.statusNoneMenuItem) {
            Object[] argument2 = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter2 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.none.title"));
            String title = formatter2.format(argument2);
            Object[] argument3 = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter3 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.none.msg"));
            String content = formatter3.format(argument3);
            createNewTextAnnotation(title, content, TextAnnotation.STATE_MODEL_REVIEW, "None");
            return;
        }
        if (source == this.statusAcceptedItem) {
            Object[] argument4 = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter4 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.accepted.title"));
            String title2 = formatter4.format(argument4);
            Object[] argument5 = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter5 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.accepted.msg"));
            String content2 = formatter5.format(argument5);
            createNewTextAnnotation(title2, content2, TextAnnotation.STATE_MODEL_REVIEW, "None");
            return;
        }
        if (source == this.statusCancelledMenuItem) {
            Object[] argument6 = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter6 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.cancelled.title"));
            String title3 = formatter6.format(argument6);
            Object[] argument7 = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter7 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.cancelled.msg"));
            String content3 = formatter7.format(argument7);
            createNewTextAnnotation(title3, content3, TextAnnotation.STATE_MODEL_REVIEW, "None");
            return;
        }
        if (source == this.statusCompletedMenuItem) {
            Object[] argument8 = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter8 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.completed.title"));
            String title4 = formatter8.format(argument8);
            Object[] argument9 = {this.selectedMarkupAnnotation.getTitleText()};
            MessageFormat formatter9 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.completed.msg"));
            String content4 = formatter9.format(argument9);
            createNewTextAnnotation(title4, content4, TextAnnotation.STATE_MODEL_REVIEW, "None");
            return;
        }
        if (source != this.statusRejectedMenuItem) {
            if (source == this.openAllMenuItem) {
                showHidePopupAnnotations(true);
                return;
            } else {
                if (source == this.minimizeAllMenuItem) {
                    showHidePopupAnnotations(false);
                    return;
                }
                return;
            }
        }
        Object[] argument10 = {this.selectedMarkupAnnotation.getTitleText()};
        MessageFormat formatter10 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.rejected.title"));
        String title5 = formatter10.format(argument10);
        Object[] argument11 = {this.selectedMarkupAnnotation.getTitleText()};
        MessageFormat formatter11 = new MessageFormat(this.messageBundle.getString("viewer.annotation.popup.status.rejected.msg"));
        String content5 = formatter11.format(argument11);
        createNewTextAnnotation(title5, content5, TextAnnotation.STATE_MODEL_REVIEW, "None");
    }

    private void showHidePopupAnnotations(boolean visible) {
        ArrayList<AnnotationComponent> annotationComponents = this.pageViewComponent.getAnnotationComponents();
        Iterator i$ = annotationComponents.iterator();
        while (i$.hasNext()) {
            AnnotationComponent annotationComponent = i$.next();
            if (annotationComponent instanceof PopupAnnotationComponent) {
                PopupAnnotationComponent popupAnnotationComponent = (PopupAnnotationComponent) annotationComponent;
                if (popupAnnotationComponent.getAnnotation() != null) {
                    PopupAnnotation popupAnnotation = (PopupAnnotation) popupAnnotationComponent.getAnnotation();
                    if (popupAnnotation.getParent() != null && popupAnnotation.getParent().getInReplyToAnnotation() == null) {
                        popupAnnotationComponent.setVisible(visible);
                    }
                }
            }
        }
    }

    private void createNewTextAnnotation(String title, String content, String stateModel, String state) {
        TextAnnotation markupAnnotation = TextAnnotationHandler.createTextAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), this.selectedMarkupAnnotation.getUserSpaceRectangle().getBounds(), getPageTransform());
        markupAnnotation.setTitleText(title);
        markupAnnotation.setContents(content);
        markupAnnotation.setState(state);
        markupAnnotation.setStateModel(stateModel);
        markupAnnotation.setInReplyToAnnotation(this.selectedMarkupAnnotation);
        addAnnotationComponent(markupAnnotation);
        PopupAnnotation popupAnnotation = TextAnnotationHandler.createPopupAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), this.popupAnnotation.getUserSpaceRectangle().getBounds(), markupAnnotation, getPageTransform());
        popupAnnotation.setOpen(false);
        addAnnotationComponent(popupAnnotation);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.commentTree.getLastSelectedPathComponent();
        DefaultMutableTreeNode replyToNode = new DefaultMutableTreeNode(markupAnnotation);
        if (node == null) {
            node = ((DefaultMutableTreeNode) this.commentTree.getModel().getRoot()).getFirstLeaf();
        }
        node.insert(replyToNode, node.getChildCount());
        this.commentTree.expandRow(replyToNode.getDepth() - 1);
        this.selectedMarkupAnnotation = markupAnnotation;
        refreshTree(this.commentTree);
        this.commentTreeScrollPane.setVisible(true);
        this.commentPanel.revalidate();
    }

    @Override // javax.swing.event.DocumentListener
    public void insertUpdate(DocumentEvent e2) {
        updateContent(e2);
    }

    @Override // javax.swing.event.DocumentListener
    public void removeUpdate(DocumentEvent e2) {
        updateContent(e2);
    }

    @Override // javax.swing.event.DocumentListener
    public void changedUpdate(DocumentEvent e2) {
        updateContent(e2);
    }

    private void updateContent(DocumentEvent e2) {
        Document document = e2.getDocument();
        try {
            if (document.getLength() > 0) {
                this.selectedMarkupAnnotation.setContents(document.getText(0, document.getLength()));
            }
        } catch (BadLocationException ex) {
            logger.log(Level.FINE, "Error updating markup annotation content", (Throwable) ex);
        }
    }

    @Override // javax.swing.event.TreeSelectionListener
    public void valueChanged(TreeSelectionEvent e2) throws IllegalArgumentException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.commentTree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        Object userObject = node.getUserObject();
        if (userObject instanceof MarkupAnnotation) {
            this.selectedMarkupAnnotation = (MarkupAnnotation) userObject;
            if (this.textArea != null) {
                this.textArea.getDocument().removeDocumentListener(this);
                this.textArea.setText(this.selectedMarkupAnnotation.getContents());
                this.textArea.getDocument().addDocumentListener(this);
            }
            if (this.creationLabel != null) {
                this.creationLabel.setText(this.selectedMarkupAnnotation.getCreationDate().toString());
            }
        }
    }

    private void addGB(JPanel panel, Component component, int x2, int y2, int rowSpan, int colSpan) {
        this.constraints.gridx = x2;
        this.constraints.gridy = y2;
        this.constraints.gridwidth = rowSpan;
        this.constraints.gridheight = colSpan;
        panel.add(component, this.constraints);
    }

    private boolean buildCommentTree(MarkupAnnotation parentAnnotation, List<Annotation> annotations, DefaultMutableTreeNode root) {
        boolean foundIRT = checkForIRT(parentAnnotation, annotations);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(parentAnnotation);
        root.add(node);
        if (!foundIRT) {
            return false;
        }
        buildRecursiveCommentTree(node, annotations);
        return true;
    }

    private void buildRecursiveCommentTree(DefaultMutableTreeNode root, List<Annotation> annotations) {
        MarkupAnnotation markupAnnotation;
        MarkupAnnotation inReplyToAnnotation;
        MarkupAnnotation currentMarkup = (MarkupAnnotation) root.getUserObject();
        Reference reference = currentMarkup.getPObjectReference();
        for (Annotation annotation : annotations) {
            if ((annotation instanceof MarkupAnnotation) && (inReplyToAnnotation = (markupAnnotation = (MarkupAnnotation) annotation).getInReplyToAnnotation()) != null && inReplyToAnnotation.getPObjectReference().equals(reference)) {
                root.add(new DefaultMutableTreeNode(markupAnnotation));
            }
        }
        int childCount = root.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            buildRecursiveCommentTree((DefaultMutableTreeNode) root.getChildAt(i2), annotations);
        }
    }

    private void removeMarkupInReplyTo(Reference reference) {
        if (reference != null) {
            ArrayList<AnnotationComponent> annotationComponents = this.pageViewComponent.getAnnotationComponents();
            for (int i2 = 0; i2 < annotationComponents.size(); i2++) {
                AnnotationComponent annotationComponent = annotationComponents.get(i2);
                if (annotationComponent instanceof MarkupAnnotationComponent) {
                    MarkupAnnotationComponent markupAnnotationComponent = (MarkupAnnotationComponent) annotationComponent;
                    MarkupAnnotation markupAnnotation = (MarkupAnnotation) markupAnnotationComponent.getAnnotation();
                    if (markupAnnotation.getInReplyToAnnotation() != null && markupAnnotation.getInReplyToAnnotation().getPObjectReference().equals(reference)) {
                        removeMarkupInReplyTo(markupAnnotation.getPObjectReference());
                        this.documentViewController.deleteAnnotation(markupAnnotationComponent);
                    }
                }
            }
        }
    }

    private boolean checkForIRT(MarkupAnnotation parentAnnotation, List<Annotation> annotations) {
        if (parentAnnotation != null) {
            Reference reference = parentAnnotation.getPObjectReference();
            for (Annotation annotation : annotations) {
                if (annotation instanceof MarkupAnnotation) {
                    MarkupAnnotation markupAnnotation = (MarkupAnnotation) annotation;
                    MarkupAnnotation inReplyToAnnotation = markupAnnotation.getInReplyToAnnotation();
                    if (inReplyToAnnotation != null && inReplyToAnnotation.getPObjectReference().equals(reference)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void addAnnotationComponent(Annotation annotation) {
        Rectangle bBox = new Rectangle(-20, -20, 20, 20);
        AbstractAnnotationComponent comp = AnnotationComponentFactory.buildAnnotationComponent(annotation, this.documentViewController, this.pageViewComponent, this.documentViewModel);
        comp.setBounds(bBox);
        comp.refreshAnnotationRect();
        if (this.documentViewController.getAnnotationCallback() != null) {
            AnnotationCallback annotationCallback = this.documentViewController.getAnnotationCallback();
            annotationCallback.newAnnotation(this.pageViewComponent, comp);
        }
    }

    private AnnotationComponent findAnnotationComponent(Annotation annotation) {
        ArrayList<AnnotationComponent> annotationComponents = this.pageViewComponent.getAnnotationComponents();
        Reference annotationReference = annotation.getPObjectReference();
        Iterator i$ = annotationComponents.iterator();
        while (i$.hasNext()) {
            AnnotationComponent annotationComponent = i$.next();
            Reference compReference = annotationComponent.getAnnotation().getPObjectReference();
            if (compReference.equals(annotationReference)) {
                return annotationComponent;
            }
        }
        return null;
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
    public void resetAppearanceShapes() {
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/PopupAnnotationComponent$PopupTreeListener.class */
    class PopupTreeListener extends MouseAdapter {
        PopupTreeListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseClicked(MouseEvent e2) {
            if (SwingUtilities.isRightMouseButton(e2)) {
                int row = PopupAnnotationComponent.this.commentTree.getClosestRowForLocation(e2.getX(), e2.getY());
                PopupAnnotationComponent.this.commentTree.setSelectionRow(row);
                PopupAnnotationComponent.this.contextMenu.show(e2.getComponent(), e2.getX(), e2.getY());
            }
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/PopupAnnotationComponent$PopupListener.class */
    class PopupListener extends MouseAdapter {
        PopupListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent e2) {
            maybeShowPopup(e2);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent e2) {
            maybeShowPopup(e2);
        }

        private void maybeShowPopup(MouseEvent e2) {
            if (e2.isPopupTrigger()) {
                PopupAnnotationComponent.this.contextMenu.show(e2.getComponent(), e2.getX(), e2.getY());
            }
        }
    }
}
