package com.sun.corba.se.impl.activation;

import java.io.PrintStream;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/CommandHandler.class */
public interface CommandHandler {
    public static final boolean shortHelp = true;
    public static final boolean longHelp = false;
    public static final boolean parseError = true;
    public static final boolean commandDone = false;

    String getCommandName();

    void printCommandHelp(PrintStream printStream, boolean z2);

    boolean processCommand(String[] strArr, ORB orb, PrintStream printStream);
}
