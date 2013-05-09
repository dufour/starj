package starj.util;

import java.util.*;

public class CollectionUtils {
    private CollectionUtils() {
        // no instances
    }

    public static Collection filter(Collection c, Filter f) {
        return CollectionUtils.filter(c, f, new ArrayList());
    }

    public static Collection filter(Collection c, Filter f, Collection result) {
        for (Iterator i = c.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            if (f.keep(obj)) {
                result.add(obj);
            }
        }

        return result;
    }

    public static long getTotalWeight(Collection c, WeightFunction w) {
        long rv = 0L;
        for (Iterator i = c.iterator(); i.hasNext(); ) {
            rv += w.getValue(i.next());
        }

        return rv;
            
    }
}
