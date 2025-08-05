package com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/cmdline/getopt/GetOpt.class */
public class GetOpt {
    private Option theCurrentOption = null;
    private ListIterator theOptionsIterator;
    private List theOptions;
    private List theCmdArgs;
    private OptionMatcher theOptionMatcher;

    public GetOpt(String[] args, String optString) {
        this.theOptions = null;
        this.theCmdArgs = null;
        this.theOptionMatcher = null;
        this.theOptions = new ArrayList();
        int currOptIndex = 0;
        this.theCmdArgs = new ArrayList();
        this.theOptionMatcher = new OptionMatcher(optString);
        int i2 = 0;
        while (true) {
            if (i2 >= args.length) {
                break;
            }
            String token = args[i2];
            int tokenLength = token.length();
            if (token.equals("--")) {
                currOptIndex = i2 + 1;
                break;
            }
            if (token.startsWith(LanguageTag.SEP) && tokenLength == 2) {
                this.theOptions.add(new Option(token.charAt(1)));
            } else if (token.startsWith(LanguageTag.SEP) && tokenLength > 2) {
                for (int j2 = 1; j2 < tokenLength; j2++) {
                    this.theOptions.add(new Option(token.charAt(j2)));
                }
            } else if (token.startsWith(LanguageTag.SEP)) {
                continue;
            } else {
                if (this.theOptions.size() == 0) {
                    currOptIndex = i2;
                    break;
                }
                int indexoflast = this.theOptions.size() - 1;
                Option op = (Option) this.theOptions.get(indexoflast);
                char opLetter = op.getArgLetter();
                if (op.hasArg() || !this.theOptionMatcher.hasArg(opLetter)) {
                    break;
                } else {
                    op.setArg(token);
                }
            }
            i2++;
        }
        currOptIndex = i2;
        this.theOptionsIterator = this.theOptions.listIterator();
        for (int i3 = currOptIndex; i3 < args.length; i3++) {
            this.theCmdArgs.add(args[i3]);
        }
    }

    public void printOptions() {
        ListIterator it = this.theOptions.listIterator();
        while (it.hasNext()) {
            Option opt = (Option) it.next();
            System.out.print("OPT =" + opt.getArgLetter());
            String arg = opt.getArgument();
            if (arg != null) {
                System.out.print(" " + arg);
            }
            System.out.println();
        }
    }

    public int getNextOption() throws IllegalArgumentException, MissingOptArgException {
        int retval = -1;
        if (this.theOptionsIterator.hasNext()) {
            this.theCurrentOption = (Option) this.theOptionsIterator.next();
            char c2 = this.theCurrentOption.getArgLetter();
            boolean shouldHaveArg = this.theOptionMatcher.hasArg(c2);
            String arg = this.theCurrentOption.getArgument();
            if (!this.theOptionMatcher.match(c2)) {
                ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_CMDLINE_OPTION_ERR, new Character(c2));
                throw new IllegalArgumentException(msg.toString());
            }
            if (shouldHaveArg && arg == null) {
                ErrorMsg msg2 = new ErrorMsg(ErrorMsg.CMDLINE_OPT_MISSING_ARG_ERR, new Character(c2));
                throw new MissingOptArgException(msg2.toString());
            }
            retval = c2;
        }
        return retval;
    }

    public String getOptionArg() {
        String retval = null;
        String tmp = this.theCurrentOption.getArgument();
        char c2 = this.theCurrentOption.getArgLetter();
        if (this.theOptionMatcher.hasArg(c2)) {
            retval = tmp;
        }
        return retval;
    }

    public String[] getCmdArgs() {
        String[] retval = new String[this.theCmdArgs.size()];
        int i2 = 0;
        ListIterator it = this.theCmdArgs.listIterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            retval[i3] = (String) it.next();
        }
        return retval;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/cmdline/getopt/GetOpt$Option.class */
    class Option {
        private char theArgLetter;
        private String theArgument = null;

        public Option(char argLetter) {
            this.theArgLetter = argLetter;
        }

        public void setArg(String arg) {
            this.theArgument = arg;
        }

        public boolean hasArg() {
            return this.theArgument != null;
        }

        public char getArgLetter() {
            return this.theArgLetter;
        }

        public String getArgument() {
            return this.theArgument;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/cmdline/getopt/GetOpt$OptionMatcher.class */
    class OptionMatcher {
        private String theOptString;

        public OptionMatcher(String optString) {
            this.theOptString = null;
            this.theOptString = optString;
        }

        public boolean match(char c2) {
            boolean retval = false;
            if (this.theOptString.indexOf(c2) != -1) {
                retval = true;
            }
            return retval;
        }

        public boolean hasArg(char c2) {
            boolean retval = false;
            int index = this.theOptString.indexOf(c2) + 1;
            if (index == this.theOptString.length()) {
                retval = false;
            } else if (this.theOptString.charAt(index) == ':') {
                retval = true;
            }
            return retval;
        }
    }
}
