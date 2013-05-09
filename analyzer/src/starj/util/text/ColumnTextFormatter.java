package starj.util.text;

import java.io.PrintStream;

import starj.util.StringUtils;

public class ColumnTextFormatter {
    public static final int DEFAULT_WIDTH = 40;
    public static final String DEFAULT_SEPARATOR = "   ";
    
    private int left_width;
    private int right_width;
    private String separator;
    private TextWidthFormatter left_format;
    private TextWidthFormatter right_format;
    
    public ColumnTextFormatter() {
        this(DEFAULT_WIDTH, DEFAULT_WIDTH, DEFAULT_SEPARATOR);
    }
    
    public ColumnTextFormatter(String separator) {
        this(DEFAULT_WIDTH, DEFAULT_WIDTH, separator);
    }

    public ColumnTextFormatter(int columnWidth) {
        this(columnWidth, columnWidth, DEFAULT_SEPARATOR);
    }
    
    public ColumnTextFormatter(int columnWidth, String separator) {
        this(columnWidth, columnWidth, separator);
    }

    public ColumnTextFormatter(int columnWidth, String separator,
            String indent_string) {
        this(columnWidth, indent_string, columnWidth, indent_string, separator);
    }

    public ColumnTextFormatter(int left_width, int right_width) {
        this(left_width, right_width, DEFAULT_SEPARATOR);
    }
    
    public ColumnTextFormatter(int left_width, int right_width,
            String separator) {
        this(left_width, null, right_width, null, separator);
    }

    public ColumnTextFormatter(int left_width, String left_indent,
            int right_width, String right_indent) {
        this(left_width, left_indent, right_width, right_indent,
                DEFAULT_SEPARATOR);
    }

    public ColumnTextFormatter(int left_width, String left_indent,
            int right_width, String right_indent, String separator) {
        this.left_width = left_width;
        this.right_width = right_width;
        this.separator = separator;
        this.left_format = new TextWidthFormatter(left_width, left_indent);
        this.right_format = new TextWidthFormatter(right_width, right_indent);
    }

    public int getTotalWidth() {
        return this.left_width + this.right_width
                + (this.separator != null ? this.separator.length()
                : String.valueOf(null).length());
    }

    public Strings format(String left, String right) {
        Strings result = new Strings();
        String sep = this.separator; // Local reference for efficiency

        StringsIterator sl = this.left_format.format(left).iterator();
        StringsIterator sr = this.right_format.format(right).iterator();

        while (true) {
            String l = "";
            String r = "";
            boolean input_avail = false;
            if (sl.hasNext()) {
                input_avail = true;
                l = StringUtils.justifyLeft(sl.next(), this.left_width);
            }
            if (sr.hasNext()) {
                input_avail = true;
                r = sr.next();
            }
            if (!input_avail) {
                break;
            }

            if (l.length() > this.left_width) {
                /* We have overflow */
                result.add(l);
                sr.pushBack();
                continue;
            }

            if (r != null) {
                result.add(l + sep + r);
            } else {
                result.add(l + sep);
            }
        }

        return result;
    }

    public void format(String left, String right, PrintStream out) {
        Strings result = this.format(left, right);
        result.printTo(out);
    }
}

