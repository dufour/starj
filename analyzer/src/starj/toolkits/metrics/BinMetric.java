package starj.toolkits.metrics;

import java.util.*;

public class BinMetric extends Metric {
    private List bins;
    
    public BinMetric(String category, String name) {
        super(category, name);
        this.bins = new LinkedList();
    }

    public String getKind() {
        return "bin";
    }

    public void addBin(Bin bin) {
        this.bins.add(bin);
    }

    public Bin[] getBins() {
        Bin[] rv = new Bin[this.bins.size()];
        this.bins.toArray(rv);
        return rv;
    }
}
