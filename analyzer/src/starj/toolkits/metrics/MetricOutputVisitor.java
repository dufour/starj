package starj.toolkits.metrics;

import java.util.*;

import starj.*;

public class MetricOutputVisitor extends OperationVisitor {
    private MetricVisitor visitor;
    private ElementSet elements;
    private List metric_ops;
    
    MetricOutputVisitor(MetricVisitor visitor) {
        this(visitor, null);
    }

    public MetricOutputVisitor(MetricVisitor visitor, ElementSet elements) {
        this.visitor = visitor;
        this.elements = elements;
    }

    public void init() {
        this.visitor.init();
        this.metric_ops = new LinkedList();
    }

    public void visitOperation(Operation operation) {
        if (operation instanceof MetricOperation) {
            if (this.elements == null
                    || this.elements.contains(operation)) {
                this.metric_ops.add(operation);
            }
        }
    }

    public void done() {
        SampleSpace[] sample_spaces = SampleSpaceManager.v().getSampleSpaces();
        if (sample_spaces != null) {
            for (int i = 0; i < sample_spaces.length; i++) {
                SampleSpace sample_space = sample_spaces[i];

                // Notify the visitor that we entered a sample space
                this.visitor.enter(sample_space);

                MetricSpace[] metric_spaces = sample_space.getMetricSpaces();
                if (metric_spaces != null) {
                    for (int j = 0; j < metric_spaces.length; j++) {
                        MetricSpace metric_space = metric_spaces[j];

                        // Notify the visitor that we entered a metric space
                        this.visitor.enter(metric_space);

                        for (Iterator k = this.metric_ops.iterator();
                                k.hasNext(); ) {
                            MetricOperation op = (MetricOperation) k.next();
                            MetricRecord r = metric_space.getRecord(
                                    op.getRecordsIndex());
                            op.accept(this.visitor, r);
                        }

                        // Notify the visitor that we exited a metric space
                        this.visitor.exit(metric_space);
                    }
                }

                // Notify the visitor that we exited a sample space
                this.visitor.exit(sample_space);
            }
        }
        this.visitor.done();
    }
}
