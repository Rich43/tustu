package org.icepdf.ri.util;

import java.awt.Container;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.SwingUtilities;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.graphics.text.LineText;
import org.icepdf.core.search.DocumentSearchController;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingWorker;
import org.icepdf.ri.common.utility.search.SearchPanel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/SearchTextTask.class */
public class SearchTextTask {
    private int lengthOfTask;
    private String dialogMessage;
    private MessageFormat searchingMessageForm;
    private MessageFormat searchResultMessageForm;
    private MessageFormat searchCompletionMessageForm;
    private String pattern;
    private boolean wholeWord;
    private boolean caseSensitive;
    private boolean cumulative;
    private boolean showPages;
    private boolean r2L;
    SwingController controller;
    private SearchPanel searchPanel;
    private ResourceBundle messageBundle;
    private boolean currentlySearching;
    private Container viewContainer;
    private int current = 0;
    private boolean done = false;
    private boolean canceled = false;
    private int totalHitCount = 0;

    static /* synthetic */ int access$612(SearchTextTask x0, int x1) {
        int i2 = x0.totalHitCount + x1;
        x0.totalHitCount = i2;
        return i2;
    }

    public SearchTextTask(SearchPanel searchPanel, SwingController controller, String pattern, boolean wholeWord, boolean caseSensitive, boolean cumulative, boolean showPages, boolean r2L, ResourceBundle messageBundle) {
        this.pattern = "";
        this.controller = controller;
        this.pattern = pattern;
        this.searchPanel = searchPanel;
        this.lengthOfTask = controller.getDocument().getNumberOfPages();
        this.messageBundle = messageBundle;
        this.viewContainer = controller.getDocumentViewController().getViewContainer();
        this.wholeWord = wholeWord;
        this.caseSensitive = caseSensitive;
        this.cumulative = cumulative;
        this.showPages = showPages;
        this.r2L = r2L;
        if (searchPanel != null) {
            this.searchingMessageForm = searchPanel.setupSearchingMessageForm();
            this.searchResultMessageForm = searchPanel.setupSearchResultMessageForm();
            this.searchCompletionMessageForm = searchPanel.setupSearchCompletionMessageForm();
        }
    }

    public void go() {
        SwingWorker worker = new SwingWorker() { // from class: org.icepdf.ri.util.SearchTextTask.1
            @Override // org.icepdf.ri.common.SwingWorker
            public Object construct() {
                SearchTextTask.this.current = 0;
                SearchTextTask.this.done = false;
                SearchTextTask.this.canceled = false;
                SearchTextTask.this.dialogMessage = null;
                return SearchTextTask.this.new ActualTask();
            }
        };
        worker.setThreadPriority(5);
        worker.start();
    }

    public int getLengthOfTask() {
        return this.lengthOfTask;
    }

    public int getCurrent() {
        return this.current;
    }

    public void stop() {
        this.canceled = true;
        this.dialogMessage = null;
    }

    public boolean isDone() {
        return this.done;
    }

    public boolean isCurrentlySearching() {
        return this.currentlySearching;
    }

    public String getMessage() {
        return this.dialogMessage;
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/SearchTextTask$ActualTask.class */
    class ActualTask {
        ActualTask() {
            if (!"".equals(SearchTextTask.this.pattern) && !" ".equals(SearchTextTask.this.pattern)) {
                try {
                    SearchTextTask.this.currentlySearching = true;
                    SearchTextTask.this.totalHitCount = 0;
                    SearchTextTask.this.current = 0;
                    DocumentSearchController searchController = SearchTextTask.this.controller.getDocumentSearchController();
                    if (!SearchTextTask.this.cumulative) {
                        searchController.clearAllSearchHighlight();
                    }
                    searchController.addSearchTerm(SearchTextTask.this.pattern, SearchTextTask.this.caseSensitive, SearchTextTask.this.wholeWord);
                    Document document = SearchTextTask.this.controller.getDocument();
                    for (int i2 = 0; i2 < document.getNumberOfPages(); i2++) {
                        if (SearchTextTask.this.canceled || SearchTextTask.this.done) {
                            SearchTextTask.this.setDialogMessage();
                            break;
                        }
                        SearchTextTask.this.current = i2;
                        Object[] messageArguments = {String.valueOf(SearchTextTask.this.current + 1), Integer.valueOf(SearchTextTask.this.lengthOfTask), Integer.valueOf(SearchTextTask.this.lengthOfTask)};
                        SearchTextTask.this.dialogMessage = SearchTextTask.this.searchingMessageForm.format(messageArguments);
                        final List<LineText> lineItems = searchController.searchHighlightPage(SearchTextTask.this.current, 6);
                        int hitCount = lineItems.size();
                        SearchTextTask.access$612(SearchTextTask.this, hitCount);
                        if (hitCount > 0) {
                            Object[] messageArguments2 = {String.valueOf(SearchTextTask.this.current + 1), Integer.valueOf(hitCount), Integer.valueOf(hitCount)};
                            final String nodeText = SearchTextTask.this.searchResultMessageForm.format(messageArguments2);
                            final int currentPage = i2;
                            SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.util.SearchTextTask.ActualTask.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SearchTextTask.this.searchPanel.addFoundEntry(nodeText, currentPage, lineItems, SearchTextTask.this.showPages);
                                    SearchTextTask.this.viewContainer.repaint();
                                }
                            });
                        }
                        Thread.yield();
                    }
                    SearchTextTask.this.setDialogMessage();
                    SearchTextTask.this.done = true;
                    SearchTextTask.this.currentlySearching = false;
                    SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.util.SearchTextTask.ActualTask.2
                        @Override // java.lang.Runnable
                        public void run() {
                            SearchTextTask.this.viewContainer.validate();
                        }
                    });
                } catch (Throwable th) {
                    SearchTextTask.this.currentlySearching = false;
                    throw th;
                }
            }
        }
    }

    public String getFinalMessage() {
        setDialogMessage();
        return this.dialogMessage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDialogMessage() {
        Object[] messageArguments = {String.valueOf(this.current + 1), Integer.valueOf(this.current + 1), Integer.valueOf(this.totalHitCount)};
        this.dialogMessage = this.searchCompletionMessageForm.format(messageArguments);
    }
}
