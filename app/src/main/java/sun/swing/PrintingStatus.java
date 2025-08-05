package sun.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/* loaded from: rt.jar:sun/swing/PrintingStatus.class */
public class PrintingStatus {
    private final PrinterJob job;
    private final Component parent;
    private JDialog abortDialog;
    private JButton abortButton;
    private JLabel statusLabel;
    private MessageFormat statusFormat;
    private final AtomicBoolean isAborted = new AtomicBoolean(false);
    private final Action abortAction = new AbstractAction() { // from class: sun.swing.PrintingStatus.1
        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws IllegalArgumentException {
            if (!PrintingStatus.this.isAborted.get()) {
                PrintingStatus.this.isAborted.set(true);
                PrintingStatus.this.abortButton.setEnabled(false);
                PrintingStatus.this.abortDialog.setTitle(UIManager.getString("PrintingDialog.titleAbortingText"));
                PrintingStatus.this.statusLabel.setText(UIManager.getString("PrintingDialog.contentAbortingText"));
                PrintingStatus.this.job.cancel();
            }
        }
    };
    private final WindowAdapter closeListener = new WindowAdapter() { // from class: sun.swing.PrintingStatus.2
        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
            PrintingStatus.this.abortAction.actionPerformed(null);
        }
    };
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PrintingStatus.class.desiredAssertionStatus();
    }

    public static PrintingStatus createPrintingStatus(Component component, PrinterJob printerJob) {
        return new PrintingStatus(component, printerJob);
    }

    protected PrintingStatus(Component component, PrinterJob printerJob) {
        this.job = printerJob;
        this.parent = component;
    }

    private void init() {
        String string = UIManager.getString("PrintingDialog.titleProgressText");
        String string2 = UIManager.getString("PrintingDialog.contentInitialText");
        this.statusFormat = new MessageFormat(UIManager.getString("PrintingDialog.contentProgressText"));
        String string3 = UIManager.getString("PrintingDialog.abortButtonText");
        String string4 = UIManager.getString("PrintingDialog.abortButtonToolTipText");
        int i2 = getInt("PrintingDialog.abortButtonMnemonic", -1);
        int i3 = getInt("PrintingDialog.abortButtonDisplayedMnemonicIndex", -1);
        this.abortButton = new JButton(string3);
        this.abortButton.addActionListener(this.abortAction);
        this.abortButton.setToolTipText(string4);
        if (i2 != -1) {
            this.abortButton.setMnemonic(i2);
        }
        if (i3 != -1) {
            this.abortButton.setDisplayedMnemonicIndex(i3);
        }
        this.statusLabel = new JLabel(string2);
        JOptionPane jOptionPane = new JOptionPane(this.statusLabel, 1, -1, null, new Object[]{this.abortButton}, this.abortButton);
        jOptionPane.getActionMap().put("close", this.abortAction);
        if (this.parent != null && (this.parent.getParent() instanceof JViewport)) {
            this.abortDialog = jOptionPane.createDialog(this.parent.getParent(), string);
        } else {
            this.abortDialog = jOptionPane.createDialog(this.parent, string);
        }
        this.abortDialog.setDefaultCloseOperation(0);
        this.abortDialog.addWindowListener(this.closeListener);
    }

    public void showModal(final boolean z2) {
        if (SwingUtilities.isEventDispatchThread()) {
            showModalOnEDT(z2);
            return;
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() { // from class: sun.swing.PrintingStatus.3
                @Override // java.lang.Runnable
                public void run() {
                    PrintingStatus.this.showModalOnEDT(z2);
                }
            });
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new RuntimeException(cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showModalOnEDT(boolean z2) {
        if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
            throw new AssertionError();
        }
        init();
        this.abortDialog.setModal(z2);
        this.abortDialog.setVisible(true);
    }

    public void dispose() {
        if (SwingUtilities.isEventDispatchThread()) {
            disposeOnEDT();
        } else {
            SwingUtilities.invokeLater(new Runnable() { // from class: sun.swing.PrintingStatus.4
                @Override // java.lang.Runnable
                public void run() {
                    PrintingStatus.this.disposeOnEDT();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disposeOnEDT() {
        if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
            throw new AssertionError();
        }
        if (this.abortDialog != null) {
            this.abortDialog.removeWindowListener(this.closeListener);
            this.abortDialog.dispose();
            this.abortDialog = null;
        }
    }

    public boolean isAborted() {
        return this.isAborted.get();
    }

    public Printable createNotificationPrintable(Printable printable) {
        return new NotificationPrintable(printable);
    }

    /* loaded from: rt.jar:sun/swing/PrintingStatus$NotificationPrintable.class */
    private class NotificationPrintable implements Printable {
        private final Printable printDelegatee;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !PrintingStatus.class.desiredAssertionStatus();
        }

        public NotificationPrintable(Printable printable) {
            if (printable == null) {
                throw new NullPointerException("Printable is null");
            }
            this.printDelegatee = printable;
        }

        @Override // java.awt.print.Printable
        public int print(Graphics graphics, PageFormat pageFormat, final int i2) throws PrinterException, IllegalArgumentException {
            int iPrint = this.printDelegatee.print(graphics, pageFormat, i2);
            if (iPrint != 1 && !PrintingStatus.this.isAborted()) {
                if (SwingUtilities.isEventDispatchThread()) {
                    updateStatusOnEDT(i2);
                } else {
                    SwingUtilities.invokeLater(new Runnable() { // from class: sun.swing.PrintingStatus.NotificationPrintable.1
                        @Override // java.lang.Runnable
                        public void run() throws IllegalArgumentException {
                            NotificationPrintable.this.updateStatusOnEDT(i2);
                        }
                    });
                }
            }
            return iPrint;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateStatusOnEDT(int i2) throws IllegalArgumentException {
            if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
                throw new AssertionError();
            }
            PrintingStatus.this.statusLabel.setText(PrintingStatus.this.statusFormat.format(new Object[]{new Integer(i2 + 1)}));
        }
    }

    static int getInt(Object obj, int i2) {
        Object obj2 = UIManager.get(obj);
        if (obj2 instanceof Integer) {
            return ((Integer) obj2).intValue();
        }
        if (obj2 instanceof String) {
            try {
                return Integer.parseInt((String) obj2);
            } catch (NumberFormatException e2) {
            }
        }
        return i2;
    }
}
