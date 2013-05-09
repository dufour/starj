package starj.options;

import java.util.*;

public class ArgumentCollector implements PostProcessor {
    /* The following comparator performs the collection
     * work. It will make all ParsedOption instances
     * 'smaller' than all other non-ParsedOption instances.
     * Any two ParsedOption instances will be considered
     * egual; the same is true of any two non-ParsedOptions.
     * Because we use a stable sort, the relative order of parsed
     * arguments within their respective groups (option & non-option)
     * will remain unchanged.
     */
    private static Comparator comparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            if (o1 instanceof ParsedOption) {
                if (o2 instanceof ParsedOption) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (o2 instanceof ParsedOption) {
                return 1;
            } 

            return 0;
        }
    };
    
    public ArgumentCollector() {
        super();
    }

    public void process(ParsedResult result) {
        result.sort(comparator);
    }
}
