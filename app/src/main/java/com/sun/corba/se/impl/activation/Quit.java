package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* compiled from: ServerTool.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/Quit.class */
class Quit implements CommandHandler {
    Quit() {
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public String getCommandName() {
        return "quit";
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public void printCommandHelp(PrintStream printStream, boolean z2) {
        if (!z2) {
            printStream.println(CorbaResourceUtil.getText("servertool.quit"));
        } else {
            printStream.println(CorbaResourceUtil.getText("servertool.quit1"));
        }
    }

    @Override // com.sun.corba.se.impl.activation.CommandHandler
    public boolean processCommand(String[] strArr, ORB orb, PrintStream printStream) {
        System.exit(0);
        return false;
    }
}
