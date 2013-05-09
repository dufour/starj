package starj.toolkits.metrics;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

import starj.util.xml.*;

public class XMLMetricVisitor implements MetricVisitor {
    private static DecimalFormat threshold_format;
    
    private OutputStream out;
    private MetricValueFormat format;
    private Map category_map;
    private XMLPrintStream xml;
    
    public XMLMetricVisitor() {
        this(System.out);
    }
    
    public XMLMetricVisitor(OutputStream out) {
        this(out, new DefaultMetricValueFormat(new DecimalFormat("0.00000"),
                    null, null));
    }
    
    public XMLMetricVisitor(OutputStream out, MetricValueFormat format) {
        this.out = out;
        this.format = format;
    }

    public void init() {
        this.xml = new XMLPrintStream(this.out);
        this.xml.openln("benchmark");
    }
    
    public void enter(SampleSpace space) {
        this.xml.openln("sample-space",
                new XMLAttribute("name", space.getName()));
    }
    
    public void exit(SampleSpace space) {
        this.xml.closeln("sample-space");
    }

    public void enter(MetricSpace space) {
        this.category_map = new HashMap();
        this.xml.openln("metric-space",
                new XMLAttribute("name", space.getName()));
    }
    
    public void exit(MetricSpace space) {
        XMLPrintStream xml = this.xml;
        xml.openln("metrics");

        // For each category
        List categories = new ArrayList(this.category_map.keySet());
        Collections.sort(categories);
        for (Iterator i = categories.iterator(); i.hasNext(); ) {
            String category = (String) i.next();
            xml.openln("category", new XMLAttribute("name", category));

            // For each metric name
            Map name_map = (Map) this.category_map.get(category);
            List names = new ArrayList(name_map.keySet());
            Collections.sort(names);
            for (Iterator j = names.iterator(); j.hasNext(); ) {
                String name = (String) j.next();
                xml.openln("metric", new XMLAttribute("name", name));

                Map kind_map = (Map) name_map.get(name);
                List kinds = new ArrayList(kind_map.keySet());
                Collections.sort(kinds);
                for (Iterator k = kinds.iterator(); k.hasNext(); ) {
                    String kind = (String) k.next();
                    Metric metric = (Metric) kind_map.get(kind);

                    if (metric instanceof ValueMetric) {
                        this.visitValueMetric((ValueMetric) metric, xml);
                    } else if (metric instanceof PercentileMetric) {
                        this.visitPercentileMetric((PercentileMetric) metric,
                                xml);
                    } else if (metric instanceof BinMetric) {
                        this.visitBinMetric((BinMetric) metric, xml);
                    }
                }
                // For each kind
                xml.closeln("metric");
            }
            
            
            xml.closeln("category");
        }

        xml.closeln("metrics");
        xml.closeln("metric-space");
    }

    public void visit(Metric metric) {
        String category = metric.getCategory();
        String name = metric.getName();
        String kind = metric.getKind();
        
        Map name_map;
        if (this.category_map.containsKey(category)) {
            name_map = (Map) this.category_map.get(category);
        } else {
            name_map = new HashMap();
            this.category_map.put(category, name_map);
        }

        Map kind_map;
        if (name_map.containsKey(name)) {
            kind_map = (Map) name_map.get(name);
        } else {
            kind_map = new HashMap();
            name_map.put(name, kind_map);
        }

        if (kind_map.containsKey(kind)) {
            throw new RuntimeException("Metric name already registered: '"
                    + metric.getFullName());
        } else { 
            kind_map.put(kind, metric);
        }

    }

    public void done() {
        this.xml.closeln("benchmark");
    }

    public void visitValueMetric(ValueMetric metric, XMLPrintStream xml) {
        this.printMetricValue("value", metric.getValue(), xml);
    }
    
    public void visitPercentileMetric(PercentileMetric metric,
            XMLPrintStream xml) {
        if (threshold_format == null) {
            threshold_format = new DecimalFormat("0.000");
        }
        XMLAttribute threshold = new XMLAttribute("threshold",
                threshold_format.format(metric.getThreshold()));
        this.printMetricValue("percentile", threshold, metric.getValue(), xml);
    }
    
    public void visitBinMetric(BinMetric metric, XMLPrintStream xml) {
        xml.openln("bins");
        Bin[] bins = metric.getBins();
        if (bins != null) {
            for (int i = 0; i < bins.length; i++) {
                this.printBin(bins[i], xml);
            }
        }
        xml.closeln("bins");
    }
    
    private void printMetricValue(String arg, MetricValue value,
            XMLPrintStream xml) {
        this.printMetricValue(arg, new XMLAttributes(), value, xml);
    }
    
    private void printMetricValue(String arg, XMLAttribute attrib,
            MetricValue value, XMLPrintStream xml) {
        this.printMetricValue(arg, new XMLAttributes(attrib), value, xml);
    }

    private void printMetricValue(String tag, XMLAttributes attribs,
            MetricValue value, XMLPrintStream xml) {
        attribs.add(new XMLAttribute("type", value.getType()));
        xml.tagln(tag, attribs, this.format.format(value));
    }

    private void printBin(Bin bin, XMLPrintStream xml) {
        XMLAttributes attribs = new XMLAttributes();
        if (bin instanceof SimpleBin) {
            BinKey k = ((SimpleBin) bin).getKey();
            attribs.add(new XMLAttribute("key_type", k.getType()));
            attribs.add(new XMLAttribute("key", String.valueOf(k)));
        } else if (bin instanceof RangeBin) {
            RangeBin range_bin = (RangeBin) bin;
            attribs.add(new XMLAttribute("key_type", "range"));
            
            BinKey low_key = range_bin.getLowKey();
            attribs.add(new XMLAttribute("low_key", String.valueOf(low_key)));
            BinKey high_key = range_bin.getHighKey();
            attribs.add(new XMLAttribute("high_key", String.valueOf(high_key)));
        } else {
            throw new RuntimeException("Unknown bin class: '"
                    + bin.getClass().getName() + "'");
        }
        this.printMetricValue("bin", attribs, bin.getValue(), xml);
    }
}
