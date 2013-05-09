package starj.util.xml;

import java.io.*;

import starj.io.IndentingPrintStream;

public class XMLPrintStream extends IndentingPrintStream {
    public XMLPrintStream(OutputStream out) {
        super(out);
    }

    public XMLPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public XMLPrintStream(OutputStream out, boolean autoFlush,
            String encoding) throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
    }

    public XMLPrintStream(OutputStream out, int indentStep) {
        super(out, indentStep);
    }

    public XMLPrintStream(OutputStream out, boolean autoFlush,
            int indentStep) {
        super(out, autoFlush, indentStep);
    }

    public XMLPrintStream(OutputStream out, boolean autoFlush,
            String encoding, int indentStep)
            throws UnsupportedEncodingException {
        super(out, autoFlush, encoding, indentStep);
    }

    public static String escape(String s) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            switch (c) {
                case '<':
                    sb.replace(i, i + 1, "&lt;");
                    break;
                case '>':
                    sb.replace(i, i + 1, "&gt;");
                    break;
                case '\"':
                    sb.replace(i, i + 1, "&quot;");
                    break;
                case '\'':
                    sb.replace(i, i + 1, "&apos;");
                    break;
                case '&':
                    sb.replace(i, i + 1, "&amp;");
                    break;
                default:
                    break;
            }
        }

        return sb.toString();
    }

    private String make_open_tag(String name, XMLAttribute[] attribs) {
        String rv = "<" + name;
        if (attribs != null) {
            for (int i = 0; i < attribs.length; i++) {
                rv += " " + attribs[i];
            }
        }
        return rv + ">";
    }

    private String make_open_tag(String name, XMLAttributes attribs) {
        return this.make_open_tag(name, attribs.toArray());
    }

    private String make_close_tag(String name) {
        return "</" + name + ">";
    }

    private String make_standalone_tag(String name, XMLAttribute[] attribs) {
        String rv = "<" + name;
        if (attribs != null) {
            for (int i = 0; i < attribs.length; i++) {
                rv += " " + attribs[i];
            }
        }
        return rv + "/>";
    }

    private String make_standalone_tag(String name, XMLAttributes attribs) {
        return this.make_standalone_tag(name, attribs.toArray());
    }

    public void openln(String tag) {
        this.openln(tag, (XMLAttribute[]) null);
    }

    public void openln(String tag, XMLAttribute attrib) {
        XMLAttribute[] attribs = new XMLAttribute[1];
        attribs[0] = attrib;
        this.openln(tag, attribs);
    }

    public void openln(String tag, XMLAttribute[] attribs) {
        this.println(this.make_open_tag(tag, attribs));
        this.indent();
    }
    
    public void openln(String tag, XMLAttributes attribs) {
        this.openln(tag, attribs.toArray());
    }

    public void closeln(String tag) {
        this.unindent();
        this.println(this.make_close_tag(tag));
    }

    public void open(String tag) {
        this.open(tag, (XMLAttribute[]) null);
    }

    public void open(String tag, XMLAttribute attrib) {
        XMLAttribute[] attribs = new XMLAttribute[1];
        attribs[0] = attrib;
        this.open(tag, attribs);
    }

    public void open(String tag, XMLAttribute[] attribs) {
        this.print(this.make_open_tag(tag, attribs));
    }
    
    public void open(String tag, XMLAttributes attribs) {
        this.open(tag, attribs.toArray());
    }

    public void close(String tag) {
        this.print(this.make_close_tag(tag));
    }

    public void tag(String tag) {
        this.print(this.make_standalone_tag(tag, (XMLAttribute[]) null));
    }

    public void tag(String tag, boolean x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, char x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, char[] x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, double x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, float x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }
    
    public void tag(String tag, int x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, long x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, Object x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, String x) {
        this.open(tag);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute attrib, boolean x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute attrib, char x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute attrib, char[] x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute attrib, double x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute attrib, float x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }
    
    public void tag(String tag, XMLAttribute attrib, int x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute attrib, long x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute attrib, Object x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute attrib, String x) {
        this.open(tag, attrib);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute[] attribs, boolean x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute[] attribs, char x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute[] attribs, char[] x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute[] attribs, double x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute[] attribs, float x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }
    
    public void tag(String tag, XMLAttribute[] attribs, int x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute[] attribs, long x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute[] attribs, Object x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttribute[] attribs, String x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttributes attribs, boolean x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttributes attribs, char x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttributes attribs, char[] x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttributes attribs, double x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttributes attribs, float x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }
    
    public void tag(String tag, XMLAttributes attribs, int x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttributes attribs, long x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttributes attribs, Object x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tag(String tag, XMLAttributes attribs, String x) {
        this.open(tag, attribs);
        this.print(x);
        this.close(tag);
    }

    public void tagln(String tag) {
        this.tag(tag);
        this.println();
    }

    public void tagln(String tag, boolean x) {
        this.tag(tag, x);
        this.println();
    }

    public void tagln(String tag, char x) {
        this.tag(tag, x);
        this.println();
    }

    public void tagln(String tag, char[] x) {
        this.tag(tag, x);
        this.println();
    }

    public void tagln(String tag, double x) {
        this.tag(tag, x);
        this.println();
    }

    public void tagln(String tag, float x) {
        this.tag(tag, x);
        this.println();
    }
    
    public void tagln(String tag, int x) {
        this.tag(tag, x);
        this.println();
    }

    public void tagln(String tag, long x) {
        this.tag(tag, x);
        this.println();
    }

    public void tagln(String tag, Object x) {
        this.tag(tag, x);
        this.println();
    }

    public void tagln(String tag, String x) {
        this.tag(tag, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib) {
        this.tag(tag, attrib);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib, boolean x) {
        this.tag(tag, attrib, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib, char x) {
        this.tag(tag, attrib, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib, char[] x) {
        this.tag(tag, attrib, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib, double x) {
        this.tag(tag, attrib, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib, float x) {
        this.tag(tag, attrib, x);
        this.println();
    }
    
    public void tagln(String tag, XMLAttribute attrib, int x) {
        this.tag(tag, attrib, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib, long x) {
        this.tag(tag, attrib, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib, Object x) {
        this.tag(tag, attrib, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute attrib, String x) {
        this.tag(tag, attrib, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs) {
        this.tag(tag, attribs);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs, boolean x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs, char x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs, char[] x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs, double x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs, float x) {
        this.tag(tag, attribs, x);
        this.println();
    }
    
    public void tagln(String tag, XMLAttribute[] attribs, int x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs, long x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs, Object x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttribute[] attribs, String x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs) {
        this.tag(tag, attribs);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs, boolean x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs, char x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs, char[] x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs, double x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs, float x) {
        this.tag(tag, attribs, x);
        this.println();
    }
    
    public void tagln(String tag, XMLAttributes attribs, int x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs, long x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs, Object x) {
        this.tag(tag, attribs, x);
        this.println();
    }

    public void tagln(String tag, XMLAttributes attribs, String x) {
        this.tag(tag, attribs, x);
        this.println();
    }
}
