package bH;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: TunerStudioMS.jar:bH/X.class */
public class X extends JFrame {

    /* renamed from: a, reason: collision with root package name */
    JEditorPane f7030a = new JEditorPane();

    public X() {
        a();
        setVisible(true);
    }

    private void a() {
        JScrollPane jScrollPane = new JScrollPane(this.f7030a);
        jScrollPane.setHorizontalScrollBarPolicy(31);
        jScrollPane.setVerticalScrollBarPolicy(22);
        add(BorderLayout.CENTER, jScrollPane);
        this.f7030a.setBackground(Color.BLACK);
        this.f7030a.setForeground(Color.WHITE);
        this.f7030a.setFont(new Font("Monospaced", 1, eJ.a(13)));
        this.f7030a.addKeyListener(new Y(this));
        setSize(680, NNTPReply.AUTHENTICATION_REQUIRED);
        this.f7030a.setText(">");
        this.f7030a.setCaretColor(Color.WHITE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        String strC = c();
        if (strC != null && !strC.isEmpty()) {
            BufferedReader bufferedReader = null;
            try {
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(strC, d(), e()).getInputStream()));
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        if (!line.endsWith("\n")) {
                        }
                        a(line);
                    }
                    try {
                        bufferedReader.close();
                    } catch (Exception e2) {
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                    a("Error executing command '" + strC + "'\n" + e3.getLocalizedMessage() + "\n>");
                    try {
                        bufferedReader.close();
                        return;
                    } catch (Exception e4) {
                        return;
                    }
                }
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (Exception e5) {
                }
                throw th;
            }
        }
        a("\n>");
    }

    private void a(String str) {
        Document document = this.f7030a.getDocument();
        try {
            document.insertString(document.getLength(), str, null);
            this.f7030a.setCaretPosition(document.getLength());
        } catch (BadLocationException e2) {
            Logger.getLogger(X.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private String c() {
        StringTokenizer stringTokenizer = new StringTokenizer(this.f7030a.getText(), "\n");
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (!stringTokenizer.hasMoreTokens()) {
                if (strNextToken.startsWith(">")) {
                    strNextToken = strNextToken.substring(1);
                }
                return strNextToken;
            }
        }
        return null;
    }

    private static String[] d() {
        return new String[0];
    }

    private static File e() throws IOException {
        File file = new File(System.getenv("SystemRoot"), "System32" + File.separatorChar + "wbem");
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        throw new IOException('\"' + file.getAbsolutePath() + "\" does not exist or is not a directory!");
    }
}
