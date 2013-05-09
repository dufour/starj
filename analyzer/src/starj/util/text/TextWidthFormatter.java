package starj.util.text;

import java.io.PrintStream;
import java.util.*;

public class TextWidthFormatter {
    public static final int DEFAULT_WIDTH = 80;
    /* FIXME: support tabs */
    public static final String DEFAULT_DELIMS = " \n\r";
    public static final boolean EXCEPTION_ON_OVERFLOW = false;
    
    private int max_width;
    private String delims;
    private String indent_string;
    private boolean exception_on_overflow;
    
    public TextWidthFormatter() {
        this(DEFAULT_WIDTH, null, EXCEPTION_ON_OVERFLOW, DEFAULT_DELIMS);
    }
    
    public TextWidthFormatter(int max_width) {
        this(max_width, null, EXCEPTION_ON_OVERFLOW, DEFAULT_DELIMS);
    }
    
    public TextWidthFormatter(int max_width, String indent_string) {
        this(max_width, indent_string, EXCEPTION_ON_OVERFLOW, DEFAULT_DELIMS);
    }

    public TextWidthFormatter(int max_width, String indent_string,
            boolean exception_on_overflow) {
        this(max_width, indent_string, exception_on_overflow, DEFAULT_DELIMS);
    }

    public TextWidthFormatter(int max_width, String indent_string,
            String delims) {
        this(max_width, indent_string, EXCEPTION_ON_OVERFLOW, delims);
    }
    
    public TextWidthFormatter(int max_width, String indent_string,
            boolean exception_on_overflow, String delims) {
        this.max_width = max_width;
        this.indent_string = indent_string;
        this.exception_on_overflow = exception_on_overflow;
        this.delims = delims;
    }

    public Strings format(String s) {
        Strings result = new Strings();
        if (s == null) {
            return result;
        }

        StringTokenizer st = new StringTokenizer(s, this.delims, true);
        /* Handle the special case where the string is empty */
        if (!st.hasMoreTokens()) {
            return result;
        }
        
        StringBuffer currentString = new StringBuffer();
        String previewToken = null;

    mainloop:
        while (true) {
            String token;
            if (previewToken != null) {
                token = previewToken;
                previewToken = null;
            } else {
                if (!st.hasMoreTokens()) {
                    break mainloop;
                }
                token = st.nextToken();
            }

            if (currentString == null) {
                // Skip blanks after a forced line break
                while (token.equals(" ")) {
                    if (!st.hasMoreTokens()) {
                        break mainloop;
                    }
                    token = st.nextToken();
                }
                
                if (this.indent_string == null) {
                    currentString = new StringBuffer();
                } else {
                    currentString = new StringBuffer(this.indent_string);
                }
            }

            
            int len = token.length();
            int sbLen = currentString.length();

            if (len > this.max_width) {
                if (this.exception_on_overflow) {
                    throw new TextWidthOverflowException("Token \"" + token
                            + "\" has more than " + this.max_width
                            + " characters");
                } else {
                    result.add(currentString.toString());
                    result.add(token);
                    currentString = null;
                    continue mainloop;
                }
            }

            if (token.equals("\r")) {
                /* Check for the MS Windows encoding "\r\n", which counts
                 * here as a single new line character */
                if (st.hasMoreTokens()) {
                    previewToken = st.nextToken();
                    if (previewToken.equals("\n")) {
                        previewToken = null;
                    }
                }
                result.add(currentString.toString());
                currentString = new StringBuffer();
            } else if (token.equals("\n")) {
                result.add(currentString.toString());
                currentString = new StringBuffer();
            } else if (len + sbLen <= this.max_width) {
                currentString.append(token);
            } else {
                /* Force a line break */
                result.add(currentString.toString());
                previewToken = token;
                currentString = null;
            }
        }
        
        if (currentString != null) {
            result.add(currentString.toString());
        }
        return result;
    }
    
    public void format(String s, PrintStream out) {
        Strings result = this.format(s);
        result.printTo(out);
    }
}

