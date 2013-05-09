package starj;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import starj.dependencies.*;
import starj.events.Event;
import starj.io.logging.*;
import starj.io.traces.*;

import starj.toolkits.services.Propagation;

/* -------------------------------------------------------------------------- * 
 *                                 Operations                                 * 
 * -------------------------------------------------------------------------- */
// Services
import starj.toolkits.services.IDResolver;
import starj.toolkits.services.InstructionResolver;
import starj.toolkits.services.CallStackManager;
import starj.toolkits.services.CallSiteResolver;
import starj.toolkits.services.PropagationManager;
// Printers
import starj.toolkits.printers.ClassPrinter;
import starj.toolkits.printers.CallGraphPrinter;
import starj.toolkits.printers.ContentionPrinter;
import starj.toolkits.printers.InstructionPrinter;
import starj.toolkits.printers.EventPrinter;
import starj.toolkits.printers.ExtendedEventPrinter;
// Metrics
import starj.toolkits.metrics.ArrayMetrics;
import starj.toolkits.metrics.BaseMetrics;
import starj.toolkits.metrics.FloatingPointMetrics;
import starj.toolkits.metrics.PointerMetrics;
import starj.toolkits.metrics.MemoryMetrics;
import starj.toolkits.metrics.PolymorphismMetrics;
import starj.toolkits.metrics.ProgramSizeMetrics;
import starj.toolkits.metrics.SynchronizationMetrics;
import starj.toolkits.metrics.InstructionCountManager; // TODO: combine with InstructionResolver?
// Aspects
import starj.toolkits.aspects.AspectTagResolver;
import starj.toolkits.aspects.TagPrinter;
import starj.toolkits.aspects.AspectPrinter;
import starj.toolkits.aspects.AspectMetrics;

public class Scene {
    private static Scene instance;
    private RootPack root_pack;
    private List visitors;

    private Scene() {
        // no instances
        this.root_pack = new RootPack();
        this.populate(this.root_pack);
        this.visitors = new LinkedList();
    }

    private void populate(Container container) {
        Pack initial = new Pack("initial",
                "Contains operations which run before all other operations");
        Pack toolkits = new Pack("toolkits",
                "Contains packs for specific toolkits");
        Pack final_pack = new Pack("final",
                "Contains operations which run after all other operations");

        container.add(initial);
        container.add(toolkits);
        container.add(final_pack);
        
        Pack toolkits_services = new Pack("services",
                "Contains service operations");
        Pack toolkits_metrics = new Pack("metrics",
                "Contains metric-related operations");
        toolkits_metrics.disable();
        Pack toolkits_aspects = new Pack("aspects",
                "Contains aspect-related operations");
        toolkits_aspects.disable();
        Pack toolkits_printers = new Pack("printers",
                "Contains printer operations");

        toolkits.add(toolkits_services);
        toolkits.add(toolkits_metrics);
        toolkits.add(toolkits_aspects);
        toolkits.add(toolkits_printers);

        toolkits_services.add(IDResolver.v());
        toolkits_services.add(CallStackManager.v());
        toolkits_services.add(InstructionResolver.v());
        toolkits_services.add(CallSiteResolver.v());
        toolkits_services.add(PropagationManager.v());

        toolkits_metrics.add(InstructionCountManager.v());
        toolkits_metrics.add(new ArrayMetrics("array",
                "Computes array-related metrics"));
        toolkits_metrics.add(new BaseMetrics("base",
                "Computes basic metrics"));
        toolkits_metrics.add(new FloatingPointMetrics("fp",
                "Computes floating-point related metrics"));
        toolkits_metrics.add(new PointerMetrics("ptr",
                "Computes pointer-related metrics"));
        toolkits_metrics.add(new MemoryMetrics("mem",
                "Computes memory-related metrics"));
        toolkits_metrics.add(new PolymorphismMetrics("poly",
                "Computes polymorphism-related metrics"));
        toolkits_metrics.add(new ProgramSizeMetrics("size",
                "Computes size-related metrics"));
        toolkits_metrics.add(new SynchronizationMetrics("sync",
                "Computes synchronization-related metrics"));

        toolkits_aspects.add(AspectTagResolver.v());
        TagPrinter tag_prn = new TagPrinter("tagprn",
                "Outputs a representation of the trace with tagging information");
        tag_prn.disable();
        toolkits_aspects.add(tag_prn);
        AspectPrinter aspect_prn = new AspectPrinter("aprn",
                "Outputs the results from the kind tag propagation algorithm");
        aspect_prn.disable();
        toolkits_aspects.add(aspect_prn);
        toolkits_aspects.add(new AspectMetrics("metrics",
                "Computes aspect-related metrics"));

        InstructionPrinter inst_prn = new InstructionPrinter("instprn",
                "Outputs bytecode instruction execution information");
        inst_prn.disable();
        EventPrinter event_prn = new EventPrinter("evprn",
                "Outputs a representation of trace events");
        event_prn.disable();
        ExtendedEventPrinter ext_prn = new ExtendedEventPrinter("extprn",
                "Outputs a representation of trace events with "
                + " additional entity information");
        ext_prn.disable();
        ClassPrinter class_prn = new ClassPrinter("class",
                "Outputs information regarding loaded classes");
        class_prn.disable();
        ContentionPrinter cont_prn = new ContentionPrinter("contention",
                "Outputs information regarding lock contention");
        cont_prn.disable();
        CallGraphPrinter cg_prn = new CallGraphPrinter("cg",
                "Outputs a dynamic call graph");
        cg_prn.disable();
        toolkits_printers.add(inst_prn);
        toolkits_printers.add(event_prn);
        toolkits_printers.add(ext_prn);
        toolkits_printers.add(class_prn);
        toolkits_printers.add(cont_prn);
        toolkits_printers.add(cg_prn);
    }

    public RootPack getRootPack() {
        return this.root_pack;
    }

    public void addVisitor(HierarchyVisitor visitor) {
        this.visitors.add(visitor);
    }

    private void launch_visitor(HierarchyVisitor visitor) {
        visitor.init();
        this.root_pack.accept(visitor);
        visitor.done();
    }
    
    private String formatTime(Date start, Date end) {
        long elapsed_time = end.getTime() - start.getTime();
        int num_millisec = (int) (elapsed_time % 1000);
        elapsed_time /= 1000;
        int num_seconds = (int) (elapsed_time % 60);
        elapsed_time /= 60;
        int num_minutes = (int) (elapsed_time % 60);
        elapsed_time /= 60;
        long num_hours = elapsed_time;
        DecimalFormat format = new DecimalFormat("00");
        return format.format(num_hours) + "h"
                + format.format(num_minutes) + "m"
                + format.format(num_seconds) + "."
                + (new DecimalFormat("000")).format(num_millisec)
                + "s";
    }

    public void performOperations(TraceReader reader)
            throws ProcessingDeathException {
        LogManager.v().logMessage("Collecting event dependencies");
        // Get event dependencies
        EventDependencyVisitor dep_visitor = new EventDependencyVisitor(reader);
        this.launch_visitor(dep_visitor);
        List[] event_to_op_lists = dep_visitor.getOperationLists();

        // Check operation dependencies
        LogManager.v().logMessage("Collecting operation dependencies");
        DependencyGraph dep_graph = new DependencyGraph(this.root_pack);
        dep_graph.checkDependencies(); // Disable all operations which have
                                       // unsatisfied dependencies

        // Prune the lists -- Remove disabled operations
        LogManager.v().logMessage("Pruning operation lists");
        for (int i = 0; i < event_to_op_lists.length; i++) {
            List l = event_to_op_lists[i];
            for (Iterator j = l.iterator(); j.hasNext(); ) {
                if (!((Operation) j.next()).isEnabled()) {
                    j.remove();
                }
            }
        }

        // Convert lists to arrays
        LogManager.v().logMessage("Flattening hierarchy");
        boolean work_found = false;
        Operation[][] event_to_operations = new Operation[Event.EVENT_COUNT][];
        for (int i = 0; i < Event.EVENT_COUNT; i++) {
            List l = event_to_op_lists[i];
            Operation[] op_array = new Operation[l.size()];
            l.toArray(op_array);
            event_to_operations[i] = op_array;
            if (!work_found) {
                work_found = !l.isEmpty();
            }
        }

        if (!work_found) {
            LogManager.v().logWarning("No operation found!");
            LogManager.v().logWarning("Exiting without processing trace file");
            return;
        }

        // Clear some references for GC purposes
        dep_visitor = null;
        event_to_op_lists  = null;
        dep_graph = null;
        
        // Initialize all operations
        LogManager.v().logMessage("Initializing operations");
        this.launch_visitor(new InitializationVisitor());

        // Main processing loop
        Event event = null;
        int id = Event.UNKNOWN;
        EventBox box = new SingleEventBox();
        Date start_date = new Date();
        try { // avoid null check within loop
            LogManager.v().logMessage("Processing trace ...");
            
            while (true) {
                event = reader.getNextEvent();
                box.setEvent(event);
                id = event.getID();

                // Send the event to all operations which registered
                // to receive it
                Operation[] ops = event_to_operations[id];
                try {
                    for (int i = 0; i < ops.length; i++) {
                        ops[i].apply(box);
                    }
                } catch (EventSkipException e) {
                    // Process the next event
                }
            }
        } catch (TraceFormatException e) {
            throw new ProcessingDeathException("Trace format error detected:"
                    + e.getMessage(), e);
        } catch (TraceIOException e) {
            throw new ProcessingDeathException("Trace I/O error detected:"
                    + e.getMessage(), e);
        } catch (IOException e) {
            throw new ProcessingDeathException("I/O error detected:"
                    + e.getMessage(), e);
        } catch (NullPointerException e) {
            if (event != null) {
                throw e;
            }
            Date stop_date = new Date();
            LogManager.v().logMessage(reader.getCurrentEventCount()
                    + " events processed in "
                    + this.formatTime(start_date, stop_date));
        } catch (RuntimeException e) {
            throw e;
        }
        
        // Call all 'done()' methods
        LogManager.v().logMessage("Finalizing");
        this.launch_visitor(new FinalizationVisitor());
        for (Iterator i = this.visitors.iterator(); i.hasNext(); ) {
            this.launch_visitor((HierarchyVisitor) i.next());
        }
    }

    public static Scene v() {
        if (instance == null) {
            instance = new Scene();
        }
        return instance;
    }

    private class EventDependencyVisitor extends OperationVisitor {
        private TraceReader reader;
        private List[] operations;

        EventDependencyVisitor(TraceReader reader) {
            this.reader = reader;
            this.operations = new List[Event.EVENT_COUNT];
            for (int i = 0; i < this.operations.length; i++) {
                this.operations[i] = new LinkedList();
            }
        }

        public void visitOperation(Operation operation) {
            EventDependencySet set = operation.eventDependencies();
            boolean disable_operation = true;
            String missing_dependency = null;

dep_loop:
            for (EventDependencyIterator i = set.iterator(); i.hasNext(); ) {
                EventDependency dep = i.next();
                int event_id = dep.getEventID();
                int mask = this.reader.getFieldMask(event_id);
                while (dep != null) {
                    if (dep.matches(mask)) {
                        disable_operation = false;
                        this.operations[event_id].add(operation);
                        break;
                    } else if (dep.isRequired()) {
                        disable_operation = true;
                        missing_dependency = " (Dependency on event " + event_id
                                + " cannot be satisfied)";
                        break dep_loop;
                    } else {
                        dep = dep.getAlternate();
                    }
                }
            }

disable_block:
            if (disable_operation) {
                if (missing_dependency == null) {
                    if (operation instanceof Propagation) {
                        // The operation is a Propagation, so don't disable it
                        break disable_block;
                    }
                    missing_dependency = " (No registered dependency)";
                }
                LogManager.v().logMessage("Disabling: " + operation.getName()
                        + missing_dependency);
                operation.disable();
            }
        }

        List[] getOperationLists() {
            return this.operations;
        }
    }

    private class InitializationVisitor extends OperationVisitor {
        InitializationVisitor() {
            
        }
        
        public void visitOperation(Operation operation) {
            if (operation.isEnabled()) {
                operation.init();
            }
        }
    }

    private class FinalizationVisitor extends OperationVisitor {
        FinalizationVisitor() {
            
        }
        
        public void visitOperation(Operation operation) {
            if (operation.isEnabled()) {
                operation.done();
            }
        }
    }
}
