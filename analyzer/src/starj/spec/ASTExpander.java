package starj.spec;

class ASTExpander {
    private SpecFormat format;

    public ASTExpander(SpecFormat format) {
        this.format = format;
    }

    public TraceSpecification expand(AST ast) {
        TraceSpecification trace = new TraceSpecification();
        ASTDefaultEventDeclaration default_decl = null;
        
        // Get the list of declared events from the AST
        ASTEventDeclarationSequence event_seq = ast.getEvents();
        ASTEventDeclaration[] events = event_seq.getEvents();
        
        // For each event declaration
        for (int i = 0; i < events.length; i++) {
            ASTEventDeclaration event = events[i];
            
            if (event instanceof ASTDefaultEventDeclaration) {
                // This is a default declaration. Just remember it for later use
                default_decl = (ASTDefaultEventDeclaration) event;
            } else if (event instanceof ASTPatternEventDeclaration) {
                // This is a proper event declaration. Get the event pattern
                ASTPatternEventDeclaration decl
                        = (ASTPatternEventDeclaration) event;
                String pattern = decl.getPattern().getPattern();
                // Figure out which events match the pattern
                EventDefinition[] defs = this.format.getMatching(pattern);
                // For all matching events ...
                for (int j = 0; j < defs.length; j++) {
                    // ... add its field declarations ...
                    EventSpecification spec = trace.getByDefinition(defs[j]);
                    if (spec == null) {
                        spec = new EventSpecification(defs[j]);
                    }
                    this.populate(spec, decl, true, true);
                    // ... and patch with the default declarations accordingly
                    this.populate(spec, default_decl, false, false);
                    trace.add(spec);
                }
            } else {
                throw new RuntimeException("Unknown AST event declaration "
                        + "node class: '" + event.getClass().getName() + "'");
            }
        }
        return trace;
    }

    /**
     * Adds the proper fields to the <code>EventDeclaration</code> instance.
     *
     * @param event The event declaration to be generated.
     * @param decl The event declaration from the AST
     * @param strict Indicates whether non-matching field patterns should
     *          be flagged as an error.
     * @param overwrite indicates whether field definitions should be
     *          allowed to overwrite previous definitions
     */
    private void populate(EventSpecification event,
            ASTEventDeclaration decl, boolean strict, boolean overwrite) {
        // Skip if no work to do
        if (decl == null) {
            return;
        }
        
        EventDefinition def = event.getDefinition();
        ASTFieldDeclarationSequence field_seq = decl.getFields();

        // Iterator over all field declarations (from AST)
        ASTFieldDeclaration[] fields = field_seq.getFields();
        for (int i = 0; i < fields.length; i++) {
            ASTFieldDeclaration field = fields[i];
            String pattern = field.getPattern().getPattern();

            FieldDefinition[] field_defs = def.getMatching(pattern);
            if (strict && (field_defs == null || field_defs.length < 1)) {
                throw new RuntimeException("Invalid field pattern "
                        + "for event " + def.getName() + ": " + pattern);
            }
            
            // Add all fields matching the given pattern
            this.addFields(event, field_defs, field.getValue().getValue(),
                    overwrite);
        }
    }
    
    public void addFields(EventSpecification event, FieldDefinition[] fields,
            boolean value, boolean overwrite) {
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                FieldDefinition def = fields[i];
                FieldSpecification f = event.getByDefinition(def);
                if (f != null) {
                    if (overwrite) {
                        f.setValue(value);
                    }
                    // else: do nothing
                } else {
                    event.add(new FieldSpecification(def, value));
                }
            }
        }
    }
}
