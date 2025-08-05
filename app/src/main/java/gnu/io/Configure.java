package gnu.io;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.net.nntp.NNTPReply;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: RXTXcomm.jar:gnu/io/Configure.class */
class Configure extends Frame {
    Panel p1;
    static final int PORT_SERIAL = 1;
    static final int PORT_PARALLEL = 2;
    int PortType = 1;
    String EnumMessage = "gnu.io.rxtx.properties has not been detected.\n\nThere is no consistant means of detecting ports on this operating System.  It is necessary to indicate which ports are valid on this system before proper port enumeration can happen.  Please check the ports that are valid on this system and select Save";
    Checkbox[] cb = new Checkbox[128];

    /* JADX INFO: Access modifiers changed from: private */
    public void saveSpecifiedPorts() {
        String string;
        String property = System.getProperty("java.home");
        String property2 = System.getProperty("path.separator", CallSiteDescriptor.TOKEN_DELIMITER);
        String property3 = System.getProperty("file.separator", "/");
        String property4 = System.getProperty("line.separator");
        if (this.PortType == 1) {
            string = new StringBuffer().append(property).append(property3).append("lib").append(property3).append("gnu.io.rxtx.SerialPorts").toString();
        } else if (this.PortType == 2) {
            string = new StringBuffer().append(property).append("gnu.io.rxtx.ParallelPorts").toString();
        } else {
            System.out.println("Bad Port Type!");
            return;
        }
        System.out.println(string);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(string);
            for (int i2 = 0; i2 < 128; i2++) {
                if (this.cb[i2].getState()) {
                    fileOutputStream.write(new StringBuffer().append(this.cb[i2].getLabel()).append(property2).toString().getBytes());
                }
            }
            fileOutputStream.write(property4.getBytes());
            fileOutputStream.close();
        } catch (IOException e2) {
            System.out.println("IOException!");
        }
    }

    void addCheckBoxes(String str) {
        for (int i2 = 0; i2 < 128; i2++) {
            if (this.cb[i2] != null) {
                this.p1.remove(this.cb[i2]);
            }
        }
        for (int i3 = 1; i3 < 129; i3++) {
            this.cb[i3 - 1] = new Checkbox(new StringBuffer().append(str).append(i3).toString());
            this.p1.add("NORTH", this.cb[i3 - 1]);
        }
    }

    public Configure() {
        String str;
        Frame frame = new Frame("Configure gnu.io.rxtx.properties");
        if (System.getProperty("file.separator", "/").compareTo("/") != 0) {
            str = "COM";
        } else {
            str = "/dev/";
        }
        frame.setBounds(100, 50, 640, NNTPReply.AUTHENTICATION_REQUIRED);
        frame.setLayout(new BorderLayout());
        this.p1 = new Panel();
        this.p1.setLayout(new GridLayout(16, 4));
        ActionListener actionListener = new ActionListener(this) { // from class: gnu.io.Configure.1
            private final Configure this$0;

            {
                this.this$0 = this;
            }

            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getActionCommand().equals(ToolWindow.SAVE_POLICY_FILE)) {
                    this.this$0.saveSpecifiedPorts();
                }
            }
        };
        addCheckBoxes(str);
        TextArea textArea = new TextArea(this.EnumMessage, 5, 50, 3);
        textArea.setSize(50, 640);
        textArea.setEditable(false);
        Panel panel = new Panel();
        panel.add(new Label("Port Name:"));
        TextField textField = new TextField(str, 8);
        textField.addActionListener(new ActionListener(this, frame) { // from class: gnu.io.Configure.2
            private final Frame val$f;
            private final Configure this$0;

            {
                this.this$0 = this;
                this.val$f = frame;
            }

            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                this.this$0.addCheckBoxes(actionEvent.getActionCommand());
                this.val$f.setVisible(true);
            }
        });
        panel.add(textField);
        panel.add(new Checkbox("Keep Ports"));
        Button[] buttonArr = new Button[6];
        int i2 = 0;
        int i3 = 4;
        while (i3 < 129) {
            buttonArr[i2] = new Button(new StringBuffer().append("1-").append(i3).toString());
            buttonArr[i2].addActionListener(new ActionListener(this, frame) { // from class: gnu.io.Configure.3
                private final Frame val$f;
                private final Configure this$0;

                {
                    this.this$0 = this;
                    this.val$f = frame;
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
                    int i4 = Integer.parseInt(actionEvent.getActionCommand().substring(2));
                    for (int i5 = 0; i5 < i4; i5++) {
                        this.this$0.cb[i5].setState(!this.this$0.cb[i5].getState());
                        this.val$f.setVisible(true);
                    }
                }
            });
            panel.add(buttonArr[i2]);
            i3 *= 2;
            i2++;
        }
        Button button = new Button("More");
        Button button2 = new Button(ToolWindow.SAVE_POLICY_FILE);
        button.addActionListener(actionListener);
        button2.addActionListener(actionListener);
        panel.add(button);
        panel.add(button2);
        frame.add("South", panel);
        frame.add(BorderLayout.CENTER, this.p1);
        frame.add("North", textArea);
        frame.addWindowListener(new WindowAdapter(this) { // from class: gnu.io.Configure.4
            private final Configure this$0;

            {
                this.this$0 = this;
            }

            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    public static void main(String[] strArr) {
        new Configure();
    }
}
