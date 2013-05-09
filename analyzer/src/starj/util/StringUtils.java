package starj.util;

import java.util.Arrays;

public class StringUtils {
    private StringUtils() {
        // no instances
    }
    
    public static String join(String[] words) {
        return join(words, " ");
    }
        
    public static String join(String[] words, String sep) {
        if (words != null) {
            String rv = "";
            for (int i = 0; i < words.length; i++) {
                if (i != 0) {
                    rv += sep;
                }
                rv += words[i];
            }

            return rv;
        } else {
            return null;
        }
    }

    public static String quote(String value) {
        return "\'" + escape(value) + "\'";
    }
    
    public static String escape(String value) {
        if (value == null) {
            return null;
        }

        String result = "";
        char[] s = value.toCharArray();
        for (int i = 0; i < s.length; i++) {
            char c = s[i];
            switch (c) {
                case '\b':
                    result += "\\b";
                    break;
                case '\t':
                    result += "\\t";
                    break;
                case '\n':
                    result += "\\n";
                    break;
                case '\f':
                    result += "\\f";
                    break;
                case '\r':
                    result += "\\r";
                    break;
                case '"':
                    result += "\\\"";
                    break;
                case '\'':
                    result += "\\'";
                    break;
                case '\\':
                    result += "\\\\";
                    break;
                default:
                    if (c < 32 || c > 126) {
                        result += "\\x" + Integer.toHexString(((int)c) & 0x7FFFFFFF);
                    }
                    result += c;
                    break;
            }
        }

        return result;
    }

    public static String unescape(String value) {
        if (value == null) {
            return null;
        }

        String result = "";
        char[] s = value.toCharArray();
        for (int i = 0; i < s.length; i++) {
            char c = s[i];
            if (c == '\\') {
                if (i == (s.length - 1)) {
                    return result + '\\';
                } else {
                    c = s[++i];
                    switch (c) {
                        case 'b':
                            result += '\b';
                            break;
                        case 't':
                            result += '\t';
                            break;
                        case 'n':
                            result += '\n';
                            break;
                        case 'f':
                            result += '\f';
                            break;
                        case 'r':
                            result += '\r';
                        case 'x':
                            {
                                i++;
                                String hexString = "";
                                while (i < s.length) {
                                    c = s[i];
                                    if (Character.digit(c, 16) >= 0) {
                                        hexString += c;
                                        i++;
                                    } else {
                                        break;
                                    }
                                }

                                if (hexString.length() <= 0) {
                                    i--;
                                    result += "\\x";
                                } else {
                                    result += (char) Short.parseShort(hexString, 16);
                                }
                            }
                            break;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            {
                                String octalString = "";
                                while (i < s.length) {
                                    c = s[i];
                                    if (Character.digit(c, 8) >= 0) {
                                        octalString += c;
                                        i++;
                                    } else {
                                        break;
                                    }
                                }

                                if (octalString.length() <= 0) {
                                    result += "\\";
                                } else {
                                    result += (char) Short.parseShort(octalString, 8);
                                }
                            }
                            break;
                        default:
                            result += '\\' + c;
                            break;
                    }
                }
            } else {
                result += c;
            }
        }

        return result;
    }

    public static String stringOf(char c, int width) {
        if (width < 0) {
            return "";
        }
        
        char[] s = new char[width];
        Arrays.fill(s, c);
        return String.valueOf(s);
    }

    public static String justifyLeft(String s, int width) {
        return justifyLeft(s, width, ' ');
    }

    public static String justifyLeft(String s, int width, char c) {
        if (s == null) {
            s = String.valueOf(null);
        }

        int pad_width = width - s.length();
        return s + stringOf(c, pad_width);
    }

    public static String justifyRight(String s, int width) {
        return justifyRight(s, width, ' ');
    }

    public static String justifyRight(String s, int width, char c) {
        if (s == null) {
            s = String.valueOf(null);
        }

        int pad_width = width - s.length();
        return stringOf(c, pad_width) + s;
    }

    public static String center(String s, int width) {
        return center(s, width, ' ');
    }

    public static String center(String s, int width, char c) {
        if (s == null) {
            s = String.valueOf(null);
        }

        int lwidth = (width - s.length()) / 2;
        int rwidth = width - lwidth;
        return stringOf(c, lwidth) + s + stringOf(c, rwidth);
    }

    public static String indent(String s, int width) {
        return indent(s, width, ' ');
    }

    public static String indent(String s, int width, char c) {
        if (s == null) {
            s = String.valueOf(null);
        }
        
        return stringOf(c, width) + s;
    }
}
