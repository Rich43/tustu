package com.sun.security.auth.callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import jdk.Exported;
import sun.security.util.Password;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/callback/TextCallbackHandler.class */
public class TextCallbackHandler implements CallbackHandler {
    @Override // javax.security.auth.callback.CallbackHandler
    public void handle(Callback[] callbackArr) throws UnsupportedCallbackException, IOException {
        String str;
        ConfirmationCallback confirmationCallback = null;
        for (int i2 = 0; i2 < callbackArr.length; i2++) {
            if (callbackArr[i2] instanceof TextOutputCallback) {
                TextOutputCallback textOutputCallback = (TextOutputCallback) callbackArr[i2];
                switch (textOutputCallback.getMessageType()) {
                    case 0:
                        str = "";
                        break;
                    case 1:
                        str = "Warning: ";
                        break;
                    case 2:
                        str = "Error: ";
                        break;
                    default:
                        throw new UnsupportedCallbackException(callbackArr[i2], "Unrecognized message type");
                }
                String message = textOutputCallback.getMessage();
                if (message != null) {
                    str = str + message;
                }
                if (str != null) {
                    System.err.println(str);
                }
            } else if (callbackArr[i2] instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callbackArr[i2];
                if (nameCallback.getDefaultName() == null) {
                    System.err.print(nameCallback.getPrompt());
                } else {
                    System.err.print(nameCallback.getPrompt() + " [" + nameCallback.getDefaultName() + "] ");
                }
                System.err.flush();
                String line = readLine();
                if (line.equals("")) {
                    line = nameCallback.getDefaultName();
                }
                nameCallback.setName(line);
            } else if (callbackArr[i2] instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) callbackArr[i2];
                System.err.print(passwordCallback.getPrompt());
                System.err.flush();
                passwordCallback.setPassword(Password.readPassword(System.in, passwordCallback.isEchoOn()));
            } else if (callbackArr[i2] instanceof ConfirmationCallback) {
                confirmationCallback = (ConfirmationCallback) callbackArr[i2];
            } else {
                throw new UnsupportedCallbackException(callbackArr[i2], "Unrecognized Callback");
            }
        }
        if (confirmationCallback != null) {
            doConfirmation(confirmationCallback);
        }
    }

    private String readLine() throws IOException {
        String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
        if (line == null) {
            throw new IOException("Cannot read from System.in");
        }
        return line;
    }

    private void doConfirmation(ConfirmationCallback confirmationCallback) throws UnsupportedCallbackException, IOException {
        String str;
        C1OptionInfo[] c1OptionInfoArr;
        int i2;
        int messageType = confirmationCallback.getMessageType();
        switch (messageType) {
            case 0:
                str = "";
                break;
            case 1:
                str = "Warning: ";
                break;
            case 2:
                str = "Error: ";
                break;
            default:
                throw new UnsupportedCallbackException(confirmationCallback, "Unrecognized message type: " + messageType);
        }
        int optionType = confirmationCallback.getOptionType();
        switch (optionType) {
            case -1:
                String[] options = confirmationCallback.getOptions();
                c1OptionInfoArr = new C1OptionInfo[options.length];
                for (int i3 = 0; i3 < c1OptionInfoArr.length; i3++) {
                    c1OptionInfoArr[i3] = new C1OptionInfo(options[i3], i3);
                }
                break;
            case 0:
                c1OptionInfoArr = new C1OptionInfo[]{new C1OptionInfo("Yes", 0), new C1OptionInfo("No", 1)};
                break;
            case 1:
                c1OptionInfoArr = new C1OptionInfo[]{new C1OptionInfo("Yes", 0), new C1OptionInfo("No", 1), new C1OptionInfo("Cancel", 2)};
                break;
            case 2:
                c1OptionInfoArr = new C1OptionInfo[]{new C1OptionInfo("OK", 3), new C1OptionInfo("Cancel", 2)};
                break;
            default:
                throw new UnsupportedCallbackException(confirmationCallback, "Unrecognized option type: " + optionType);
        }
        int defaultOption = confirmationCallback.getDefaultOption();
        String prompt = confirmationCallback.getPrompt();
        if (prompt == null) {
            prompt = "";
        }
        String str2 = str + prompt;
        if (!str2.equals("")) {
            System.err.println(str2);
        }
        int i4 = 0;
        while (i4 < c1OptionInfoArr.length) {
            if (optionType == -1) {
                System.err.println(i4 + ". " + c1OptionInfoArr[i4].name + (i4 == defaultOption ? " [default]" : ""));
            } else {
                System.err.println(i4 + ". " + c1OptionInfoArr[i4].name + (c1OptionInfoArr[i4].value == defaultOption ? " [default]" : ""));
            }
            i4++;
        }
        System.err.print("Enter a number: ");
        System.err.flush();
        try {
            int i5 = Integer.parseInt(readLine());
            if (i5 < 0 || i5 > c1OptionInfoArr.length - 1) {
                i5 = defaultOption;
            }
            i2 = c1OptionInfoArr[i5].value;
        } catch (NumberFormatException e2) {
            i2 = defaultOption;
        }
        confirmationCallback.setSelectedIndex(i2);
    }

    /* renamed from: com.sun.security.auth.callback.TextCallbackHandler$1OptionInfo, reason: invalid class name */
    /* loaded from: rt.jar:com/sun/security/auth/callback/TextCallbackHandler$1OptionInfo.class */
    class C1OptionInfo {
        String name;
        int value;

        C1OptionInfo(String str, int i2) {
            this.name = str;
            this.value = i2;
        }
    }
}
