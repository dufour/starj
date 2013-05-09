package starj.util.text;

import java.util.Comparator;

public class StringLengthComparator implements Comparator {
    private static StringLengthComparator instance;
    
    public int compare(Object obj1, Object obj2) {
        String s1 = (String) obj1;
        String s2 = (String) obj2;
        int l1 = s1.length();
        int l2 = s2.length();

        if (l1 == l2) {
            // Lexicographical ordering
            return s1.compareTo(s2);
        }
        
        return l1 - l2;
    }
    
    public boolean equals(Object obj) {
        return (obj instanceof StringLengthComparator);
    }

    public static StringLengthComparator v() {
        if (StringLengthComparator.instance == null) {
            StringLengthComparator.instance = new StringLengthComparator();
        }

        return StringLengthComparator.instance;
    }
}

