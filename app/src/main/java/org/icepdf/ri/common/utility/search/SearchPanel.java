package org.icepdf.ri.common.utility.search;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.graphics.text.LineText;
import org.icepdf.core.pobjects.graphics.text.WordText;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.images.Images;
import org.icepdf.ri.util.SearchTextTask;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/search/SearchPanel.class */
public class SearchPanel extends JPanel implements ActionListener, TreeSelectionListener {
    private static final String HTML_TAG_START = "<html>";
    private static final String HTML_TAG_END = "</html>";
    private static final String BOLD_TAG_START = "<b>";
    private static final String BOLD_TAG_END = "</b>";
    private GridBagConstraints constraints;
    private JTextField searchTextField;
    private Document document;
    private SwingController controller;
    private JTree tree;
    private DefaultMutableTreeNode rootTreeNode;
    private DefaultTreeModel treeModel;
    private JButton searchButton;
    private JButton clearSearchButton;
    private JCheckBox caseSensitiveCheckbox;
    private JCheckBox wholeWordCheckbox;
    private JCheckBox cumulativeCheckbox;
    private JCheckBox showPagesCheckbox;
    private int lastNodePageIndex;
    protected JProgressBar progressBar;
    protected SearchTextTask searchTextTask;
    protected JLabel findMessage;
    protected Timer timer;
    private static final int ONE_SECOND = 1000;
    private boolean isSearching;
    ResourceBundle messageBundle;
    MessageFormat searchResultMessageForm;

    public SearchPanel(SwingController controller) throws IllegalArgumentException {
        super(true);
        this.isSearching = false;
        setFocusable(true);
        this.controller = controller;
        this.messageBundle = this.controller.getMessageBundle();
        this.searchResultMessageForm = setupSearchResultMessageForm();
        setGui();
        setDocument(controller.getDocument());
    }

    public void setDocument(Document doc) throws IllegalArgumentException {
        if (this.timer != null) {
            this.timer.stop();
        }
        if (this.searchTextTask != null) {
            this.searchTextTask.stop();
            while (this.searchTextTask.isCurrentlySearching()) {
                try {
                    Thread.sleep(50L);
                } catch (Exception e2) {
                }
            }
        }
        this.document = doc;
        if (this.document != null && this.progressBar != null) {
            this.progressBar.setMaximum(this.document.getNumberOfPages());
        }
        if (this.searchTextField != null) {
            this.searchTextField.setText("");
        }
        if (this.searchButton != null) {
            this.searchButton.setText(this.messageBundle.getString("viewer.utilityPane.search.tab.title"));
        }
        if (this.rootTreeNode != null) {
            resetTree();
            String docTitle = getDocumentTitle();
            this.rootTreeNode.setUserObject(docTitle);
            this.rootTreeNode.setAllowsChildren(true);
            this.tree.setRootVisible(docTitle != null);
        }
        if (this.findMessage != null) {
            this.findMessage.setText("");
            this.findMessage.setVisible(false);
        }
        if (this.progressBar != null) {
            this.progressBar.setVisible(false);
        }
        this.isSearching = false;
    }

    private void setGui() {
        this.rootTreeNode = new DefaultMutableTreeNode();
        this.treeModel = new DefaultTreeModel(this.rootTreeNode);
        this.tree = new JTree(this.treeModel);
        this.tree.setRootVisible(true);
        this.tree.setExpandsSelectedPaths(true);
        this.tree.setShowsRootHandles(true);
        this.tree.setScrollsOnExpand(true);
        this.tree.getSelectionModel().setSelectionMode(1);
        this.tree.addTreeSelectionListener(this);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(new ImageIcon(Images.get("page.gif")));
        renderer.setClosedIcon(new ImageIcon(Images.get("page.gif")));
        renderer.setLeafIcon(new ImageIcon(Images.get("page.gif")));
        this.tree.setCellRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(this.tree);
        scrollPane.setPreferredSize(new Dimension(150, 75));
        JLabel searchLabel = new JLabel(this.messageBundle.getString("viewer.utilityPane.search.searchText.label"));
        this.searchTextField = new JTextField("", 15);
        this.searchTextField.addActionListener(this);
        this.progressBar = new JProgressBar(0, 1);
        this.progressBar.setValue(0);
        this.progressBar.setVisible(false);
        this.findMessage = new JLabel(this.messageBundle.getString("viewer.utilityPane.search.searching.msg"));
        this.findMessage.setVisible(false);
        this.timer = new Timer(1000, new TimerListener());
        this.searchButton = new JButton(this.messageBundle.getString("viewer.utilityPane.search.searchButton.label"));
        this.searchButton.addActionListener(this);
        this.clearSearchButton = new JButton(this.messageBundle.getString("viewer.utilityPane.search.clearSearchButton.label"));
        this.clearSearchButton.addActionListener(this);
        this.wholeWordCheckbox = new JCheckBox(this.messageBundle.getString("viewer.utilityPane.search.wholeWordCheckbox.label"));
        this.caseSensitiveCheckbox = new JCheckBox(this.messageBundle.getString("viewer.utilityPane.search.caseSenstiveCheckbox.label"));
        this.cumulativeCheckbox = new JCheckBox(this.messageBundle.getString("viewer.utilityPane.search.cumlitiveCheckbox.label"));
        this.showPagesCheckbox = new JCheckBox(this.messageBundle.getString("viewer.utilityPane.search.showPagesCheckbox.label"), true);
        this.showPagesCheckbox.addActionListener(new ActionListener() { // from class: org.icepdf.ri.common.utility.search.SearchPanel.1
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v50, types: [javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.MutableTreeNode] */
            /* JADX WARN: Type inference failed for: r0v64, types: [javax.swing.tree.DefaultTreeModel] */
            /* JADX WARN: Type inference failed for: r0v72, types: [javax.swing.tree.DefaultTreeModel] */
            /* JADX WARN: Type inference failed for: r0v79, types: [javax.swing.tree.DefaultTreeModel] */
            /* JADX WARN: Type inference failed for: r0v80, types: [javax.swing.tree.DefaultMutableTreeNode] */
            /* JADX WARN: Type inference failed for: r11v1 */
            /* JADX WARN: Type inference failed for: r11v2 */
            /* JADX WARN: Type inference failed for: r11v3 */
            /* JADX WARN: Type inference failed for: r11v4, types: [javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.MutableTreeNode] */
            /* JADX WARN: Type inference failed for: r11v5 */
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() != null) {
                    if (((JCheckBox) actionEvent.getSource()).isSelected()) {
                        if (SearchPanel.this.rootTreeNode != null && SearchPanel.this.rootTreeNode.getChildCount() > 0) {
                            boolean defaultMutableTreeNode = 0;
                            int i2 = -1;
                            int i3 = 0;
                            int i4 = 0;
                            while (i4 < SearchPanel.this.rootTreeNode.getChildCount()) {
                                ?? r0 = (DefaultMutableTreeNode) SearchPanel.this.rootTreeNode.getChildAt(i4);
                                if (r0.getUserObject() instanceof FindEntry) {
                                    int pageNumber = ((FindEntry) r0.getUserObject()).getPageNumber();
                                    if (i2 == pageNumber) {
                                        i3++;
                                        if (defaultMutableTreeNode) {
                                            SearchPanel.this.treeModel.removeNodeFromParent(r0);
                                            defaultMutableTreeNode.add(r0);
                                            r0.setParent(defaultMutableTreeNode);
                                            i4--;
                                        }
                                    } else {
                                        if (defaultMutableTreeNode) {
                                            defaultMutableTreeNode.setUserObject(SearchPanel.this.new FindEntry(SearchPanel.this.searchResultMessageForm.format(new Object[]{String.valueOf(i2 + 1), Integer.valueOf(i3), Integer.valueOf(i3)}), i2));
                                        }
                                        i2 = pageNumber;
                                        i3 = 1;
                                        SearchPanel.this.treeModel.removeNodeFromParent(r0);
                                        defaultMutableTreeNode = new DefaultMutableTreeNode(SearchPanel.this.new FindEntry(SearchPanel.this.searchResultMessageForm.format(new Object[]{String.valueOf(i2 + 1), 1, 1}), i2), true);
                                        defaultMutableTreeNode.add(r0);
                                        r0.setParent(defaultMutableTreeNode);
                                        SearchPanel.this.treeModel.insertNodeInto(defaultMutableTreeNode, SearchPanel.this.rootTreeNode, i4);
                                    }
                                }
                                i4++;
                                defaultMutableTreeNode = defaultMutableTreeNode;
                            }
                            return;
                        }
                        return;
                    }
                    if (SearchPanel.this.rootTreeNode != null && SearchPanel.this.rootTreeNode.getChildCount() > 0) {
                        int childCount = SearchPanel.this.rootTreeNode.getChildCount();
                        for (int i5 = 0; i5 < childCount; i5++) {
                            DefaultMutableTreeNode defaultMutableTreeNode2 = (DefaultMutableTreeNode) SearchPanel.this.rootTreeNode.getChildAt(0);
                            if (defaultMutableTreeNode2.getChildCount() > 0) {
                                for (int i6 = 0; i6 < defaultMutableTreeNode2.getChildCount(); i6++) {
                                    SearchPanel.this.treeModel.insertNodeInto(new DefaultMutableTreeNode(((DefaultMutableTreeNode) defaultMutableTreeNode2.getChildAt(i6)).getUserObject(), false), SearchPanel.this.rootTreeNode, SearchPanel.this.rootTreeNode.getChildCount());
                                }
                            }
                            SearchPanel.this.treeModel.removeNodeFromParent(defaultMutableTreeNode2);
                        }
                    }
                }
            }
        });
        GridBagLayout layout = new GridBagLayout();
        this.constraints = new GridBagConstraints();
        this.constraints.fill = 0;
        this.constraints.weightx = 1.0d;
        this.constraints.weighty = 0.0d;
        this.constraints.anchor = 11;
        this.constraints.anchor = 17;
        this.constraints.insets = new Insets(10, 5, 1, 5);
        JPanel searchPanel = new JPanel(layout);
        setLayout(new BorderLayout());
        add(new JScrollPane(searchPanel, 20, 30), BorderLayout.CENTER);
        addGB(searchPanel, searchLabel, 0, 0, 2, 1);
        this.constraints.insets = new Insets(1, 1, 1, 5);
        this.constraints.weightx = 1.0d;
        this.constraints.fill = 2;
        addGB(searchPanel, this.searchTextField, 0, 1, 2, 1);
        this.constraints.insets = new Insets(1, 1, 1, 5);
        this.constraints.weightx = 1.0d;
        this.constraints.fill = 13;
        addGB(searchPanel, this.searchButton, 0, 2, 1, 1);
        this.constraints.insets = new Insets(1, 1, 1, 5);
        this.constraints.weightx = 0.0d;
        this.constraints.fill = 0;
        addGB(searchPanel, this.clearSearchButton, 1, 2, 1, 1);
        this.constraints.insets = new Insets(5, 1, 1, 5);
        this.constraints.weightx = 1.0d;
        this.constraints.fill = 2;
        addGB(searchPanel, this.caseSensitiveCheckbox, 0, 3, 2, 1);
        this.constraints.insets = new Insets(1, 1, 1, 5);
        this.constraints.fill = 2;
        addGB(searchPanel, this.wholeWordCheckbox, 0, 4, 2, 1);
        this.constraints.insets = new Insets(1, 1, 1, 5);
        this.constraints.fill = 2;
        addGB(searchPanel, this.cumulativeCheckbox, 0, 5, 2, 1);
        this.constraints.insets = new Insets(1, 1, 1, 5);
        this.constraints.fill = 2;
        addGB(searchPanel, this.showPagesCheckbox, 0, 6, 2, 1);
        this.constraints.insets = new Insets(10, 5, 1, 5);
        this.constraints.fill = 0;
        addGB(searchPanel, new JLabel(this.messageBundle.getString("viewer.utilityPane.search.results.label")), 0, 7, 2, 1);
        this.constraints.fill = 1;
        this.constraints.insets = new Insets(1, 5, 1, 5);
        this.constraints.weightx = 1.0d;
        this.constraints.weighty = 1.0d;
        addGB(searchPanel, scrollPane, 0, 8, 2, 1);
        this.constraints.insets = new Insets(1, 5, 1, 5);
        this.constraints.weighty = 0.0d;
        this.constraints.fill = 0;
        this.constraints.anchor = 13;
        this.findMessage.setAlignmentX(1.0f);
        addGB(searchPanel, this.findMessage, 0, 9, 2, 1);
        this.constraints.insets = new Insets(5, 5, 1, 5);
        this.constraints.fill = 2;
        addGB(searchPanel, this.progressBar, 0, 10, 2, 1);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setVisible(boolean flag) {
        super.setVisible(flag);
        if (isShowing()) {
            this.searchTextField.requestFocus(true);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void requestFocus() {
        super.requestFocus();
        this.searchTextField.requestFocus();
    }

    public void dispose() {
        this.document = null;
        this.controller = null;
        this.searchTextTask = null;
        this.timer = null;
    }

    @Override // javax.swing.event.TreeSelectionListener
    public void valueChanged(TreeSelectionEvent e2) {
        if (this.tree.getLastSelectedPathComponent() != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            if (selectedNode.getUserObject() instanceof FindEntry) {
                FindEntry tmp = (FindEntry) selectedNode.getUserObject();
                if (this.controller != null) {
                    int oldTool = this.controller.getDocumentViewToolMode();
                    try {
                        this.controller.setDisplayTool(51);
                        this.controller.showPage(tmp.getPageNumber());
                        this.controller.setDisplayTool(oldTool);
                    } catch (Throwable th) {
                        this.controller.setDisplayTool(oldTool);
                        throw th;
                    }
                }
            }
        }
    }

    public void addFoundEntry(String title, int pageNumber, List<LineText> textResults, boolean showPages) {
        DefaultMutableTreeNode parentNode;
        if (textResults != null && textResults.size() > 0) {
            if (showPages && this.lastNodePageIndex != pageNumber) {
                parentNode = new DefaultMutableTreeNode(new FindEntry(title, pageNumber), true);
                this.treeModel.insertNodeInto(parentNode, this.rootTreeNode, this.rootTreeNode.getChildCount());
            } else {
                parentNode = this.rootTreeNode;
            }
            for (LineText currentText : textResults) {
                addObject(parentNode, new DefaultMutableTreeNode(new FindEntry(generateResultPreview(currentText.getWords()), pageNumber), false), false);
            }
            if (this.lastNodePageIndex == -1) {
                this.tree.expandPath(new TreePath(this.rootTreeNode));
            }
            this.lastNodePageIndex = pageNumber;
        }
    }

    private void addObject(DefaultMutableTreeNode parent, DefaultMutableTreeNode childNode, boolean shouldBeVisible) {
        if (parent == null) {
            parent = this.rootTreeNode;
        }
        this.treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        if (shouldBeVisible) {
            this.tree.scrollPathToVisible(new TreePath((Object[]) childNode.getPath()));
        }
    }

    private static String generateResultPreview(List<WordText> allText) {
        StringBuilder toReturn = new StringBuilder(HTML_TAG_START);
        for (WordText currentText : allText) {
            if (currentText.isHighlighted()) {
                toReturn.append(BOLD_TAG_START);
                toReturn.append(currentText.getText());
                toReturn.append(BOLD_TAG_END);
            } else {
                toReturn.append(currentText.getText());
            }
        }
        toReturn.append(HTML_TAG_END);
        return toReturn.toString();
    }

    protected void resetTree() {
        this.tree.setSelectionPath(null);
        this.rootTreeNode.removeAllChildren();
        this.treeModel.nodeStructureChanged(this.rootTreeNode);
        this.lastNodePageIndex = -1;
    }

    private String getDocumentTitle() {
        String documentTitle = null;
        if (this.document != null && this.document.getInfo() != null) {
            documentTitle = this.document.getInfo().getTitle();
        }
        if (documentTitle == null || documentTitle.trim().length() == 0) {
            return null;
        }
        return documentTitle;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (this.searchTextField.getText().length() > 0 && (source == this.searchTextField || source == this.searchButton)) {
            if (!this.timer.isRunning()) {
                this.findMessage.setVisible(true);
                this.progressBar.setVisible(true);
                resetTree();
                this.searchTextTask = new SearchTextTask(this, this.controller, this.searchTextField.getText(), this.wholeWordCheckbox.isSelected(), this.caseSensitiveCheckbox.isSelected(), this.cumulativeCheckbox.isSelected(), this.showPagesCheckbox.isSelected(), false, this.messageBundle);
                this.isSearching = true;
                this.searchButton.setText(this.messageBundle.getString("viewer.utilityPane.search.stopButton.label"));
                this.clearSearchButton.setEnabled(false);
                this.caseSensitiveCheckbox.setEnabled(false);
                this.wholeWordCheckbox.setEnabled(false);
                this.cumulativeCheckbox.setEnabled(false);
                this.showPagesCheckbox.setEnabled(false);
                this.searchTextTask.go();
                this.timer.start();
                return;
            }
            this.isSearching = false;
            this.clearSearchButton.setEnabled(true);
            this.caseSensitiveCheckbox.setEnabled(true);
            this.wholeWordCheckbox.setEnabled(true);
            this.cumulativeCheckbox.setEnabled(true);
            this.showPagesCheckbox.setEnabled(true);
            return;
        }
        if (source == this.clearSearchButton) {
            this.searchTextField.setText("");
            resetTree();
            this.controller.getDocumentSearchController().clearAllSearchHighlight();
            this.controller.getDocumentViewController().getViewContainer().repaint();
        }
    }

    private void addGB(JPanel panel, Component component, int x2, int y2, int rowSpan, int colSpan) {
        this.constraints.gridx = x2;
        this.constraints.gridy = y2;
        this.constraints.gridwidth = rowSpan;
        this.constraints.gridheight = colSpan;
        panel.add(component, this.constraints);
    }

    public MessageFormat setupSearchResultMessageForm() {
        MessageFormat messageForm = new MessageFormat(this.messageBundle.getString("viewer.utilityPane.search.result.msg"));
        double[] pageLimits = {0.0d, 1.0d, 2.0d};
        String[] resultsStrings = {this.messageBundle.getString("viewer.utilityPane.search.result.moreFile.msg"), this.messageBundle.getString("viewer.utilityPane.search.result.oneFile.msg"), this.messageBundle.getString("viewer.utilityPane.search.result.moreFile.msg")};
        ChoiceFormat resultsChoiceForm = new ChoiceFormat(pageLimits, resultsStrings);
        Format[] formats = {null, resultsChoiceForm};
        messageForm.setFormats(formats);
        return messageForm;
    }

    public MessageFormat setupSearchingMessageForm() {
        MessageFormat messageForm = new MessageFormat(this.messageBundle.getString("viewer.utilityPane.search.searching1.msg"));
        double[] fileLimits = {0.0d, 1.0d, 2.0d};
        String[] fileStrings = {this.messageBundle.getString("viewer.utilityPane.search.searching1.moreFile.msg"), this.messageBundle.getString("viewer.utilityPane.search.searching1.oneFile.msg"), this.messageBundle.getString("viewer.utilityPane.search.searching1.moreFile.msg")};
        ChoiceFormat choiceForm = new ChoiceFormat(fileLimits, fileStrings);
        Format[] formats = {null, choiceForm, null};
        messageForm.setFormats(formats);
        return messageForm;
    }

    public MessageFormat setupSearchCompletionMessageForm() {
        MessageFormat messageForm = new MessageFormat(this.messageBundle.getString("viewer.utilityPane.search.progress.msg"));
        double[] pageLimits = {0.0d, 1.0d, 2.0d};
        String[] pageStrings = {this.messageBundle.getString("viewer.utilityPane.search.progress.morePage.msg"), this.messageBundle.getString("viewer.utilityPane.search.progress.onePage.msg"), this.messageBundle.getString("viewer.utilityPane.search.progress.morePage.msg")};
        ChoiceFormat pageChoiceForm = new ChoiceFormat(pageLimits, pageStrings);
        String[] resultsStrings = {this.messageBundle.getString("viewer.utilityPane.search.progress.moreMatch.msg"), this.messageBundle.getString("viewer.utilityPane.search.progress.oneMatch.msg"), this.messageBundle.getString("viewer.utilityPane.search.progress.moreMatch.msg")};
        ChoiceFormat resultsChoiceForm = new ChoiceFormat(pageLimits, resultsStrings);
        Format[] formats = {null, pageChoiceForm, resultsChoiceForm};
        messageForm.setFormats(formats);
        return messageForm;
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/search/SearchPanel$TimerListener.class */
    class TimerListener implements ActionListener {
        TimerListener() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent evt) throws IllegalArgumentException {
            SearchPanel.this.progressBar.setValue(SearchPanel.this.searchTextTask.getCurrent());
            String s2 = SearchPanel.this.searchTextTask.getMessage();
            if (s2 != null) {
                SearchPanel.this.findMessage.setText(s2);
            }
            if (SearchPanel.this.searchTextTask.isDone() || !SearchPanel.this.isSearching) {
                SearchPanel.this.findMessage.setText(SearchPanel.this.searchTextTask.getFinalMessage());
                SearchPanel.this.timer.stop();
                SearchPanel.this.searchTextTask.stop();
                SearchPanel.this.searchButton.setText(SearchPanel.this.messageBundle.getString("viewer.utilityPane.search.searchButton.label"));
                SearchPanel.this.clearSearchButton.setEnabled(true);
                SearchPanel.this.caseSensitiveCheckbox.setEnabled(true);
                SearchPanel.this.wholeWordCheckbox.setEnabled(true);
                SearchPanel.this.cumulativeCheckbox.setEnabled(true);
                SearchPanel.this.showPagesCheckbox.setEnabled(true);
                SearchPanel.this.progressBar.setValue(SearchPanel.this.progressBar.getMinimum());
                SearchPanel.this.progressBar.setVisible(false);
            }
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/search/SearchPanel$FindEntry.class */
    class FindEntry extends DefaultMutableTreeNode {
        String title;
        int pageNumber;

        FindEntry(String title) {
            this.pageNumber = 0;
            this.title = title;
            setUserObject(title);
        }

        FindEntry(String title, int pageNumber) {
            this.pageNumber = pageNumber;
            this.title = title;
            setUserObject(title);
        }

        public int getPageNumber() {
            return this.pageNumber;
        }
    }
}
