package starj.util.text;

import java.util.Comparator;

public class ReverseStringLengthComparator implements Comparator {
    private static ReverseStringLengthComparator instance;
    
    public int compare(Object obj1, Object obj2) {
        String s1 = (String) obj1;
        String s2 = (String) obj2;
        int l1 = s1.length();
        int l2 = s2.length();

        if (l1 == l2) {
            // Reverse lexicographical ordering
            return s2.compareTo(s1);
        }
        
        return l2 - l1;
    }
    
    public boolean equals(Object obj) {
        return (obj instanceof ReverseStringLengthComparator);
    }

    public static ReverseStringLengthComparator v() {
        if (ReverseStringLengthComparator.instance == null) {
            ReverseStringLengthComparator.instance
                    = new ReverseStringLengthComparator();
        }

        return ReverseStringLengthComparator.instance;
    }
}
