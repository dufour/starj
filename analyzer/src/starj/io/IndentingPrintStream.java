package starj.io;

import java.io.*;

import starj.util.StringUtils;

public class IndentingPrintStream extends PrintStream {
    public static final int DEFAULT_INDENT_STEP = 4;
    private int indent_step; // Indentation step
    private int level; // Current indentation level
    private boolean need_indent; // If true, indentation is needed before
                                 // printing new contents

    public IndentingPrintStream(OutputStream out) {
        this(out, IndentingPrintStream.DEFAULT_INDENT_STEP);
    }

    public IndentingPrintStream(OutputStream out, boolean autoFlush) {
        this(out, autoFlush, IndentingPrintStream.DEFAULT_INDENT_STEP);
    }

    public IndentingPrintStream(OutputStream out, boolean autoFlush,
            String encoding) throws UnsupportedEncodingException {
        this(out, autoFlush, encoding,
                IndentingPrintStream.DEFAULT_INDENT_STEP);
    }

    public IndentingPrintStream(OutputStream out, int indentStep) {
        super(out);
        this.init(indentStep);
    }

    public IndentingPrintStream(OutputStream out, boolean autoFlush,
            int indentStep) {
        super(out, autoFlush);
        this.init(indentStep);
    }

    public IndentingPrintStream(OutputStream out, boolean autoFlush,
            String encoding, int indentStep)
            throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
        this.init(indentStep);
    }

    protected void init(int indentStep) {
        this.indent_step = indentStep;
        this.level = 0;
        this.need_indent = false;
    }

    public void indent() {
        this.indent(this.indent_step);
    }

    public void indent(int step) {
        this.level += step;
    }

    public void unindent() {
        this.unindent(this.indent_step);
    }

    public void unindent(int step) {
        this.level -= step;
        if (this.level < 0) {
            this.level = 0;
        }
    }

    public void setIndentStep(int step) {
        this.indent_step = step;
    }

    public int getIndentStep() {
        return this.indent_step;
    }

    protected void clearIndent() {
        if (this.need_indent) {
            super.print(StringUtils.stringOf(' ', this.level));
            this.need_indent = false;
        }
    }

    public void print(boolean b) {
        this.clearIndent();
        super.print(b);
    }

    public void print(char c) {
        this.clearIndent();
        super.print(c);
    }

    public void print(char[] s) {
        this.clearIndent();
        super.print(s);
        if (s != null && s.length > 0 && s[s.length - 1] == '\n') {
            this.need_indent = true;
        }
    }

    public void print(double d) {
        this.clearIndent();
        super.print(d);
    }

    public void print(float f) {
        this.clearIndent();
        super.print(f);
    }

    public void print(int i) {
        this.clearIndent();
        super.print(i);
    }

    public void print(long l) {
        this.clearIndent();
        super.print(l);
    }

    public void print(Object obj) {
        this.clearIndent();
        String s = (obj != null ? obj.toString() : null);
        super.print(s);
        if (s != null && s.endsWith("\n")) {
            this.need_indent = true;
        }
    }

    public void print(String s) {
        this.clearIndent();
        super.print(s);
        if (s != null && s.endsWith("\n")) {
            this.need_indent = true;
        }
    }

    public void println() {
        this.clearIndent();
        super.println();
        this.need_indent = true;
    }

    public void println(boolean b) {
        this.clearIndent();
        super.println();
        this.need_indent = true;
    }

    public void println(char c) {
        this.clearIndent();
        super.println(c);
        this.need_indent = true;
    }

    public void println(char[] s) {
        this.clearIndent();
        super.println(s);
        this.need_indent = true;
    }

    public void println(double d) {
        this.clearIndent();
        super.println(d);
        this.need_indent = true;
    }

    public void println(float f) {
        this.clearIndent();
        super.println(f);
        this.need_indent = true;
    }

    public void println(int i) {
        this.clearIndent();
        super.println(i);
        this.need_indent = true;
    }

    public void println(long l) {
        this.clearIndent();
        super.println(l);
        this.need_indent = true;
    }

    public void println(Object obj) {
        this.clearIndent();
        super.println(obj);
        this.need_indent = true;
    }

    public void println(String s) {
        this.clearIndent();
        super.println(s);
        this.need_indent = true;
    }
}
