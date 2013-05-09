package starj.toolkits.metrics;

import starj.events.*;
import starj.toolkits.services.Entity;
import starj.toolkits.services.IDResolver;

public class StaticApplicationSampleSpace extends SampleSpace {
    private MetricSpace app_space;
    private MetricSpace lib_space;

    public StaticApplicationSampleSpace() {
        super("static application");
        this.app_space = this.newMetricSpace("app");
        this.lib_space = this.newMetricSpace("lib");
    }

    private MetricSpace getSpace(Entity entity) {
        if (entity != null && entity.isStandardLibrary()) {
            return this.lib_space;
        }

        return this.app_space;
    }

    public MetricSpace getMetricSpace(Event event) {
        if (event instanceof MethodEvent) {
            MethodEvent e = (MethodEvent) event;
            
            return this.getSpace(
                    IDResolver.v().getMethodEntity(e.getMethodID()));
        }
        
        switch (event.getID()) {
            case Event.CLASS_LOAD:
            case Event.CLASS_UNLOAD: {
                    ClassEvent e = (ClassEvent) event;
                    return this.getSpace(
                            IDResolver.v().getClassEntity(e.getClassID()));
                }
            case Event.OBJECT_ALLOC: {
                    ObjectAllocEvent e = (ObjectAllocEvent) event;
                    return this.getSpace(
                            IDResolver.v().getObjectEntity(e.getObjectID()));
                    
                }
            default:
                break;
        }  

        return this.app_space;
    }
}
