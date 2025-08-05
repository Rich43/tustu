package com.efiAnalytics.ui.calculators.calculator;

import com.efiAnalytics.ui.eJ;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.apache.commons.net.ftp.FTPReply;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/calculators/calculator/Calculator.class */
public final class Calculator extends JDialog implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    private final Expression f11192a;

    /* renamed from: b, reason: collision with root package name */
    private Display f11193b;

    /* renamed from: c, reason: collision with root package name */
    private MyKeyDispacher f11194c;

    /* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/calculators/calculator/Calculator$MyKeyDispacher.class */
    class MyKeyDispacher implements KeyEventDispatcher {
        MyKeyDispacher() {
        }

        @Override // java.awt.KeyEventDispatcher
        public boolean dispatchKeyEvent(KeyEvent keyEvent) throws IllegalArgumentException {
            boolean z2 = false;
            if (keyEvent.getID() == 401) {
                if (keyEvent.getKeyChar() == '0' || keyEvent.getKeyChar() == '1' || keyEvent.getKeyChar() == '2' || keyEvent.getKeyChar() == '3' || keyEvent.getKeyChar() == '4' || keyEvent.getKeyChar() == '5' || keyEvent.getKeyChar() == '6' || keyEvent.getKeyChar() == '7' || keyEvent.getKeyChar() == '8' || keyEvent.getKeyChar() == '9') {
                    Calculator.this.a(keyEvent.getKeyChar() + "");
                    z2 = true;
                } else if (keyEvent.getKeyChar() == '/' || keyEvent.getKeyChar() == '+' || keyEvent.getKeyChar() == '-' || keyEvent.getKeyChar() == 'x' || keyEvent.getKeyChar() == '=') {
                    Calculator.this.c(keyEvent.getKeyChar() + "");
                    z2 = true;
                } else if (keyEvent.getKeyChar() == '*' || keyEvent.getKeyChar() == 'X') {
                    Calculator.this.c(LanguageTag.PRIVATEUSE);
                    z2 = true;
                } else if (keyEvent.getKeyChar() == '.') {
                    Calculator.this.d(".");
                    z2 = true;
                } else if (keyEvent.getKeyCode() == 27) {
                    Calculator.this.b("");
                    z2 = true;
                } else if (keyEvent.getKeyCode() == 10) {
                    Calculator.this.c("=");
                    z2 = true;
                }
            }
            return z2;
        }
    }

    public Calculator() {
        this(null);
    }

    public Calculator(Window window) {
        super(window, "Calculator");
        this.f11192a = new Expression();
        this.f11194c = new MyKeyDispacher();
        a();
        setSize(eJ.a(300), eJ.a(FTPReply.FILE_ACTION_PENDING));
        setResizable(false);
        if (window != null) {
            setDefaultCloseOperation(2);
            setLocationRelativeTo(window);
        } else {
            setLocationRelativeTo(null);
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f11194c);
        addWindowListener(new WindowAdapter() { // from class: com.efiAnalytics.ui.calculators.calculator.Calculator.1
            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosed(WindowEvent windowEvent) {
                if (Calculator.this.getParent() == null) {
                    System.exit(0);
                } else {
                    Calculator.this.dispose();
                }
            }
        });
        enableEvents(262144L);
        setVisible(true);
    }

    @Override // java.awt.Window
    public void dispose() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f11194c);
        super.dispose();
    }

    private void a() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        this.f11193b = new Display();
        jPanel.add(this.f11193b);
        jPanel.add(new Keypad(this));
        add(jPanel);
    }

    void a(String str) throws IllegalArgumentException {
        this.f11193b.b(str);
    }

    void b(String str) throws IllegalArgumentException {
        this.f11192a.a();
        this.f11193b.d();
        this.f11193b.b();
    }

    void c(String str) throws IllegalArgumentException {
        this.f11193b.a(str);
        Optional map = Operator.a(str).map(operator -> {
            return Double.valueOf(this.f11192a.a(operator, this.f11193b.a()));
        });
        Display display = this.f11193b;
        display.getClass();
        map.ifPresent((v1) -> {
            r1.a(v1);
        });
    }

    void d(String str) {
        this.f11193b.e();
    }

    void e(String str) {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        ((Command) actionEvent.getSource()).a();
    }
}
