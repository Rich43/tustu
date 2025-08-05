package org.icepdf.ri.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.pobjects.Document;
import org.icepdf.ri.common.SwingWorker;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/TextExtractionTask.class */
public class TextExtractionTask {
    private static final Logger logger = Logger.getLogger(TextExtractionTask.class.toString());
    private int lengthOfTask;
    private String dialogMessage;
    private ResourceBundle messageBundle;
    private Document document;
    private File file;
    private int current = 0;
    private boolean done = false;
    private boolean canceled = false;

    public TextExtractionTask(Document document, File file, ResourceBundle messageBundle) {
        this.document = null;
        this.file = null;
        this.document = document;
        this.file = file;
        this.lengthOfTask = document.getNumberOfPages();
        this.messageBundle = messageBundle;
    }

    public void go() {
        SwingWorker worker = new SwingWorker() { // from class: org.icepdf.ri.util.TextExtractionTask.1
            @Override // org.icepdf.ri.common.SwingWorker
            public Object construct() {
                TextExtractionTask.this.current = 0;
                TextExtractionTask.this.done = false;
                TextExtractionTask.this.canceled = false;
                TextExtractionTask.this.dialogMessage = null;
                return TextExtractionTask.this.new ActualTask();
            }
        };
        worker.setThreadPriority(1);
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

    public String getMessage() {
        return this.dialogMessage;
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/TextExtractionTask$ActualTask.class */
    class ActualTask {
        ActualTask() {
            try {
                BufferedWriter fileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TextExtractionTask.this.file), InternalZipConstants.CHARSET_UTF8));
                String pageNumber = TextExtractionTask.this.messageBundle.getString("viewer.exportText.fileStamp.msg");
                fileOutputStream.write(pageNumber);
                fileOutputStream.write(10);
                for (int i2 = 0; i2 < TextExtractionTask.this.document.getNumberOfPages() && !TextExtractionTask.this.canceled && !TextExtractionTask.this.done; i2++) {
                    TextExtractionTask.this.current = i2;
                    MessageFormat messageForm = new MessageFormat(TextExtractionTask.this.messageBundle.getString("viewer.exportText.fileStamp.progress.msg"));
                    double[] fileLimits = {0.0d, 1.0d, 2.0d};
                    String[] fileStrings = {TextExtractionTask.this.messageBundle.getString("viewer.exportText.fileStamp.progress.moreFile.msg"), TextExtractionTask.this.messageBundle.getString("viewer.exportText.fileStamp.progress.oneFile.msg"), TextExtractionTask.this.messageBundle.getString("viewer.exportText.fileStamp.progress.moreFile.msg")};
                    ChoiceFormat choiceForm = new ChoiceFormat(fileLimits, fileStrings);
                    Format[] formats = {null, choiceForm, null};
                    messageForm.setFormats(formats);
                    Object[] messageArguments = {String.valueOf(TextExtractionTask.this.current + 1), Integer.valueOf(TextExtractionTask.this.lengthOfTask), Integer.valueOf(TextExtractionTask.this.lengthOfTask)};
                    TextExtractionTask.this.dialogMessage = messageForm.format(messageArguments);
                    MessageFormat messageForm2 = new MessageFormat(TextExtractionTask.this.messageBundle.getString("viewer.exportText.pageStamp.msg"));
                    Object[] messageArguments2 = {String.valueOf(TextExtractionTask.this.current + 1)};
                    String pageNumber2 = messageForm2.format(messageArguments2);
                    fileOutputStream.write(pageNumber2);
                    fileOutputStream.write(10);
                    String pageText = TextExtractionTask.this.document.getPageText(i2).toString();
                    fileOutputStream.write(pageText);
                    Thread.yield();
                }
                TextExtractionTask.this.done = true;
                TextExtractionTask.this.current = 0;
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Throwable e2) {
                TextExtractionTask.logger.log(Level.FINE, "Malformed URL Exception ", e2);
            }
        }
    }
}
