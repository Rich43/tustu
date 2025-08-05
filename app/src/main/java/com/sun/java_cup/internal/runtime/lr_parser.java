package com.sun.java_cup.internal.runtime;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import java.util.Stack;

/* loaded from: rt.jar:com/sun/java_cup/internal/runtime/lr_parser.class */
public abstract class lr_parser {
    public static final int ID_GROUP = 1;
    public static final int ID_OPERATOR = 2;
    public static final int ID_TOTAL_OPERATOR = 3;
    private boolean isLiteral;
    private int grpCount;
    private int opCount;
    private int totalOpCount;
    protected static final int _error_sync_size = 3;
    protected boolean _done_parsing;
    protected int tos;
    protected Symbol cur_token;
    protected Stack stack;
    protected short[][] production_tab;
    protected short[][] action_tab;
    protected short[][] reduce_tab;
    private Scanner _scanner;
    protected Symbol[] lookahead;
    protected int lookahead_pos;

    public abstract short[][] production_table();

    public abstract short[][] action_table();

    public abstract short[][] reduce_table();

    public abstract int start_state();

    public abstract int start_production();

    public abstract int EOF_sym();

    public abstract int error_sym();

    public abstract Symbol do_action(int i2, lr_parser lr_parserVar, Stack stack, int i3) throws Exception;

    protected abstract void init_actions() throws Exception;

    public lr_parser() {
        this.isLiteral = false;
        this.grpCount = 0;
        this.opCount = 0;
        this.totalOpCount = 0;
        this._done_parsing = false;
        this.stack = new Stack();
    }

    public lr_parser(Scanner s2) {
        this();
        setScanner(s2);
    }

    protected int error_sync_size() {
        return 3;
    }

    public void done_parsing() {
        this._done_parsing = true;
    }

    public void setScanner(Scanner s2) {
        this._scanner = s2;
    }

    public Scanner getScanner() {
        return this._scanner;
    }

    public void user_init() throws Exception {
    }

    public Symbol scan() throws Exception {
        Symbol s2 = getScanner().next_token();
        if (s2.sym == 7) {
            if (!this.isLiteral) {
                this.grpCount++;
            }
            this.opCount++;
            this.isLiteral = false;
        } else if (contains(sym.OPERATORS, s2.sym)) {
            this.opCount++;
            this.isLiteral = false;
        }
        if (s2.sym == 26 || s2.sym == 27) {
            this.isLiteral = true;
        }
        return s2;
    }

    private boolean contains(int[] arr, int key) {
        for (int i2 : arr) {
            if (i2 == key) {
                return true;
            }
        }
        return false;
    }

    public void report_fatal_error(String message, Object info) throws Exception {
        done_parsing();
        report_error(message, info);
        throw new Exception("Can't recover from previous error(s)");
    }

    public void report_error(String message, Object info) {
        System.err.print(message);
        if (info instanceof Symbol) {
            if (((Symbol) info).left != -1) {
                System.err.println(" at character " + ((Symbol) info).left + " of input");
                return;
            } else {
                System.err.println("");
                return;
            }
        }
        System.err.println("");
    }

    public void syntax_error(Symbol cur_token) {
        report_error("Syntax error", cur_token);
    }

    public void unrecovered_syntax_error(Symbol cur_token) throws Exception {
        report_fatal_error("Couldn't repair and continue parse", cur_token);
    }

    protected final short get_action(int state, int sym) {
        short[] row = this.action_tab[state];
        if (row.length < 20) {
            int probe = 0;
            while (probe < row.length) {
                int i2 = probe;
                int probe2 = probe + 1;
                short tag = row[i2];
                if (tag != sym && tag != -1) {
                    probe = probe2 + 1;
                } else {
                    return row[probe2];
                }
            }
            return (short) 0;
        }
        int first = 0;
        int last = ((row.length - 1) / 2) - 1;
        while (first <= last) {
            int probe3 = (first + last) / 2;
            if (sym == row[probe3 * 2]) {
                return row[(probe3 * 2) + 1];
            }
            if (sym > row[probe3 * 2]) {
                first = probe3 + 1;
            } else {
                last = probe3 - 1;
            }
        }
        return row[row.length - 1];
    }

    protected final short get_reduce(int state, int sym) {
        short[] row = this.reduce_tab[state];
        if (row == null) {
            return (short) -1;
        }
        int probe = 0;
        while (probe < row.length) {
            int i2 = probe;
            int probe2 = probe + 1;
            short tag = row[i2];
            if (tag != sym && tag != -1) {
                probe = probe2 + 1;
            } else {
                return row[probe2];
            }
        }
        return (short) -1;
    }

    public Symbol parse() throws Exception {
        Symbol lhs_sym = null;
        this.production_tab = production_table();
        this.action_tab = action_table();
        this.reduce_tab = reduce_table();
        init_actions();
        user_init();
        this.isLiteral = false;
        this.grpCount = 0;
        this.opCount = 0;
        this.cur_token = scan();
        this.stack.removeAllElements();
        this.stack.push(new Symbol(0, start_state()));
        this.tos = 0;
        this._done_parsing = false;
        while (!this._done_parsing) {
            if (this.cur_token.used_by_parser) {
                throw new Error("Symbol recycling detected (fix your scanner).");
            }
            int act = get_action(((Symbol) this.stack.peek()).parse_state, this.cur_token.sym);
            if (act > 0) {
                this.cur_token.parse_state = act - 1;
                this.cur_token.used_by_parser = true;
                this.stack.push(this.cur_token);
                this.tos++;
                this.cur_token = scan();
            } else if (act < 0) {
                lhs_sym = do_action((-act) - 1, this, this.stack, this.tos);
                short lhs_sym_num = this.production_tab[(-act) - 1][0];
                short handle_size = this.production_tab[(-act) - 1][1];
                for (int i2 = 0; i2 < handle_size; i2++) {
                    this.stack.pop();
                    this.tos--;
                }
                lhs_sym.parse_state = get_reduce(((Symbol) this.stack.peek()).parse_state, lhs_sym_num);
                lhs_sym.used_by_parser = true;
                this.stack.push(lhs_sym);
                this.tos++;
            } else if (act == 0) {
                syntax_error(this.cur_token);
                if (!error_recovery(false)) {
                    unrecovered_syntax_error(this.cur_token);
                    done_parsing();
                } else {
                    lhs_sym = (Symbol) this.stack.peek();
                }
            }
        }
        this.totalOpCount += this.opCount;
        return lhs_sym;
    }

    public int getCount(int id) {
        switch (id) {
            case 1:
                return this.grpCount;
            case 2:
                return this.opCount;
            case 3:
                return this.totalOpCount;
            default:
                return 0;
        }
    }

    public void debug_message(String mess) {
        System.err.println(mess);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dump_stack() {
        if (this.stack == null) {
            debug_message("# Stack dump requested, but stack is null");
            return;
        }
        debug_message("============ Parse Stack Dump ============");
        for (int i2 = 0; i2 < this.stack.size(); i2++) {
            debug_message("Symbol: " + ((Symbol) this.stack.elementAt(i2)).sym + " State: " + ((Symbol) this.stack.elementAt(i2)).parse_state);
        }
        debug_message("==========================================");
    }

    public void debug_reduce(int prod_num, int nt_num, int rhs_size) {
        debug_message("# Reduce with prod #" + prod_num + " [NT=" + nt_num + ", SZ=" + rhs_size + "]");
    }

    public void debug_shift(Symbol shift_tkn) {
        debug_message("# Shift under term #" + shift_tkn.sym + " to state #" + shift_tkn.parse_state);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void debug_stack() {
        StringBuffer sb = new StringBuffer("## STACK:");
        for (int i2 = 0; i2 < this.stack.size(); i2++) {
            Symbol s2 = (Symbol) this.stack.elementAt(i2);
            sb.append(" <state " + s2.parse_state + ", sym " + s2.sym + ">");
            if (i2 % 3 == 2 || i2 == this.stack.size() - 1) {
                debug_message(sb.toString());
                sb = new StringBuffer("         ");
            }
        }
    }

    public Symbol debug_parse() throws Exception {
        Symbol lhs_sym = null;
        this.production_tab = production_table();
        this.action_tab = action_table();
        this.reduce_tab = reduce_table();
        debug_message("# Initializing parser");
        init_actions();
        user_init();
        this.cur_token = scan();
        debug_message("# Current Symbol is #" + this.cur_token.sym);
        this.stack.removeAllElements();
        this.stack.push(new Symbol(0, start_state()));
        this.tos = 0;
        this._done_parsing = false;
        while (!this._done_parsing) {
            if (this.cur_token.used_by_parser) {
                throw new Error("Symbol recycling detected (fix your scanner).");
            }
            int act = get_action(((Symbol) this.stack.peek()).parse_state, this.cur_token.sym);
            if (act > 0) {
                this.cur_token.parse_state = act - 1;
                this.cur_token.used_by_parser = true;
                debug_shift(this.cur_token);
                this.stack.push(this.cur_token);
                this.tos++;
                this.cur_token = scan();
                debug_message("# Current token is " + ((Object) this.cur_token));
            } else if (act < 0) {
                lhs_sym = do_action((-act) - 1, this, this.stack, this.tos);
                short lhs_sym_num = this.production_tab[(-act) - 1][0];
                short handle_size = this.production_tab[(-act) - 1][1];
                debug_reduce((-act) - 1, lhs_sym_num, handle_size);
                for (int i2 = 0; i2 < handle_size; i2++) {
                    this.stack.pop();
                    this.tos--;
                }
                int act2 = get_reduce(((Symbol) this.stack.peek()).parse_state, lhs_sym_num);
                debug_message("# Reduce rule: top state " + ((Symbol) this.stack.peek()).parse_state + ", lhs sym " + ((int) lhs_sym_num) + " -> state " + act2);
                lhs_sym.parse_state = act2;
                lhs_sym.used_by_parser = true;
                this.stack.push(lhs_sym);
                this.tos++;
                debug_message("# Goto state #" + act2);
            } else if (act == 0) {
                syntax_error(this.cur_token);
                if (!error_recovery(true)) {
                    unrecovered_syntax_error(this.cur_token);
                    done_parsing();
                } else {
                    lhs_sym = (Symbol) this.stack.peek();
                }
            }
        }
        return lhs_sym;
    }

    protected boolean error_recovery(boolean debug) throws Exception {
        if (debug) {
            debug_message("# Attempting error recovery");
        }
        if (!find_recovery_config(debug)) {
            if (debug) {
                debug_message("# Error recovery fails");
                return false;
            }
            return false;
        }
        read_lookahead();
        while (true) {
            if (debug) {
                debug_message("# Trying to parse ahead");
            }
            if (!try_parse_ahead(debug)) {
                if (this.lookahead[0].sym == EOF_sym()) {
                    if (debug) {
                        debug_message("# Error recovery fails at EOF");
                        return false;
                    }
                    return false;
                }
                if (debug) {
                    debug_message("# Consuming Symbol #" + cur_err_token().sym);
                }
                restart_lookahead();
            } else {
                if (debug) {
                    debug_message("# Parse-ahead ok, going back to normal parse");
                }
                parse_lookahead(debug);
                return true;
            }
        }
    }

    protected boolean shift_under_error() {
        return get_action(((Symbol) this.stack.peek()).parse_state, error_sym()) > 0;
    }

    protected boolean find_recovery_config(boolean debug) {
        if (debug) {
            debug_message("# Finding recovery state on stack");
        }
        int right_pos = ((Symbol) this.stack.peek()).right;
        int left_pos = ((Symbol) this.stack.peek()).left;
        while (!shift_under_error()) {
            if (debug) {
                debug_message("# Pop stack by one, state was # " + ((Symbol) this.stack.peek()).parse_state);
            }
            left_pos = ((Symbol) this.stack.pop()).left;
            this.tos--;
            if (this.stack.empty()) {
                if (debug) {
                    debug_message("# No recovery state found on stack");
                    return false;
                }
                return false;
            }
        }
        int act = get_action(((Symbol) this.stack.peek()).parse_state, error_sym());
        if (debug) {
            debug_message("# Recover state found (#" + ((Symbol) this.stack.peek()).parse_state + ")");
            debug_message("# Shifting on error to state #" + (act - 1));
        }
        Symbol error_token = new Symbol(error_sym(), left_pos, right_pos);
        error_token.parse_state = act - 1;
        error_token.used_by_parser = true;
        this.stack.push(error_token);
        this.tos++;
        return true;
    }

    protected void read_lookahead() throws Exception {
        this.lookahead = new Symbol[error_sync_size()];
        for (int i2 = 0; i2 < error_sync_size(); i2++) {
            this.lookahead[i2] = this.cur_token;
            this.cur_token = scan();
        }
        this.lookahead_pos = 0;
    }

    protected Symbol cur_err_token() {
        return this.lookahead[this.lookahead_pos];
    }

    protected boolean advance_lookahead() {
        this.lookahead_pos++;
        return this.lookahead_pos < error_sync_size();
    }

    protected void restart_lookahead() throws Exception {
        for (int i2 = 1; i2 < error_sync_size(); i2++) {
            this.lookahead[i2 - 1] = this.lookahead[i2];
        }
        this.cur_token = scan();
        this.lookahead[error_sync_size() - 1] = this.cur_token;
        this.lookahead_pos = 0;
    }

    protected boolean try_parse_ahead(boolean debug) throws Exception {
        virtual_parse_stack vstack = new virtual_parse_stack(this.stack);
        while (true) {
            int act = get_action(vstack.top(), cur_err_token().sym);
            if (act == 0) {
                return false;
            }
            if (act > 0) {
                vstack.push(act - 1);
                if (debug) {
                    debug_message("# Parse-ahead shifts Symbol #" + cur_err_token().sym + " into state #" + (act - 1));
                }
                if (!advance_lookahead()) {
                    return true;
                }
            } else {
                if ((-act) - 1 == start_production()) {
                    if (debug) {
                        debug_message("# Parse-ahead accepts");
                        return true;
                    }
                    return true;
                }
                short lhs = this.production_tab[(-act) - 1][0];
                short rhs_size = this.production_tab[(-act) - 1][1];
                for (int i2 = 0; i2 < rhs_size; i2++) {
                    vstack.pop();
                }
                if (debug) {
                    debug_message("# Parse-ahead reduces: handle size = " + ((int) rhs_size) + " lhs = #" + ((int) lhs) + " from state #" + vstack.top());
                }
                vstack.push(get_reduce(vstack.top(), lhs));
                if (debug) {
                    debug_message("# Goto state #" + vstack.top());
                }
            }
        }
    }

    protected void parse_lookahead(boolean debug) throws Exception {
        Symbol lhs_sym = null;
        this.lookahead_pos = 0;
        if (debug) {
            debug_message("# Reparsing saved input with actions");
            debug_message("# Current Symbol is #" + cur_err_token().sym);
            debug_message("# Current state is #" + ((Symbol) this.stack.peek()).parse_state);
        }
        while (!this._done_parsing) {
            int act = get_action(((Symbol) this.stack.peek()).parse_state, cur_err_token().sym);
            if (act > 0) {
                cur_err_token().parse_state = act - 1;
                cur_err_token().used_by_parser = true;
                if (debug) {
                    debug_shift(cur_err_token());
                }
                this.stack.push(cur_err_token());
                this.tos++;
                if (!advance_lookahead()) {
                    if (debug) {
                        debug_message("# Completed reparse");
                        return;
                    }
                    return;
                } else if (debug) {
                    debug_message("# Current Symbol is #" + cur_err_token().sym);
                }
            } else if (act < 0) {
                lhs_sym = do_action((-act) - 1, this, this.stack, this.tos);
                short lhs_sym_num = this.production_tab[(-act) - 1][0];
                short handle_size = this.production_tab[(-act) - 1][1];
                if (debug) {
                    debug_reduce((-act) - 1, lhs_sym_num, handle_size);
                }
                for (int i2 = 0; i2 < handle_size; i2++) {
                    this.stack.pop();
                    this.tos--;
                }
                int act2 = get_reduce(((Symbol) this.stack.peek()).parse_state, lhs_sym_num);
                lhs_sym.parse_state = act2;
                lhs_sym.used_by_parser = true;
                this.stack.push(lhs_sym);
                this.tos++;
                if (debug) {
                    debug_message("# Goto state #" + act2);
                }
            } else if (act == 0) {
                report_fatal_error("Syntax error", lhs_sym);
                return;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [short[], short[][]] */
    protected static short[][] unpackFromStrings(String[] sa) {
        StringBuffer sb = new StringBuffer(sa[0]);
        for (int i2 = 1; i2 < sa.length; i2++) {
            sb.append(sa[i2]);
        }
        int size1 = (sb.charAt(0) << 16) | sb.charAt(0 + 1);
        int n2 = 0 + 2;
        ?? r0 = new short[size1];
        for (int i3 = 0; i3 < size1; i3++) {
            int size2 = (sb.charAt(n2) << 16) | sb.charAt(n2 + 1);
            n2 += 2;
            r0[i3] = new short[size2];
            for (int j2 = 0; j2 < size2; j2++) {
                int i4 = n2;
                n2++;
                r0[i3][j2] = (short) (sb.charAt(i4) - 2);
            }
        }
        return r0;
    }
}
