package starj.options;

import starj.HierarchyElement;
import java.util.*;

public class ElementConfigOption extends Option {
    public ElementConfigOption(int id, String category, String short_desc,
            String long_desc) {
        super(id, category, short_desc, long_desc);
        this.init();
    }

    public ElementConfigOption(int id, boolean required, String category,
            String short_desc, String long_desc) {
        super(id, required, category, short_desc, long_desc);
        this.init();
    }

    public ElementConfigOption(int id, boolean required, boolean repeatable,
            String category, String short_desc, String long_desc) {
        super(id, required, repeatable, category, short_desc, long_desc);
        this.init();
    }

    private void init() {
        Argument element_patterns = new ElementPatternsArgument(
                0,
                "patterns",
                true,  // required
                false, // not repeatable
                "Comma-separated name patterns",
                "Comma-separated name patterns specifying which elements to "
                + "be configured",
                false  // not inlined
        );
        //CSVArgument element_patterns = new UnorderedCSVArgument(
        //        0,
        //        "patterns",
        //        true,  // required
        //        false, // not repeatable
        //        "Comma-separated name patterns",
        //        "Comma-separated name patterns specifying which elements to "
        //        + "be configured",
        //        false  // not inlined
        //);
        //Argument element_names_arg = new PatternArgument(
        //        0,
        //        "name",
        //        true, // Required
        //        true, // Repeatable
        //        "Name pattern",
        //        "Name pattern specifying which elements to be configured",
        //        true  // Inlined
        //);
        //element_patterns.addArgument(element_names_arg);
        super.addArgument(element_patterns);

        CSVArgument config_args = new UnorderedCSVArgument(
                0,
                "configurations",
                true,  // required
                false, // not repeatable
                "Comma-separated name[:value] pairs",
                "Comma-separated name[:value] pairs",
                false  // not inlined
        );
        Argument config_arg = new StringArgument(
                0,
                "configuration",
                true, // Required
                true, // Repeatable
                "Configuration",
                "name[:value] configuration pair",
                true  // inlined
        );
        config_args.addArgument(config_arg);
        super.addArgument(config_args);
    }

    public void addArgument(Argument argument) {
        throw new RuntimeException(this.getClass().getName()
                + " class does not allow adding new arguments");
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        ParsedResult parsed_result = new ParsedResult();
        super.parse(arg, parsed_result, queue, parser);
        ParsedOption parsed_opt = (ParsedOption) parsed_result.getChild(0);

        Object[] config_objs = parsed_opt.getValues(1);
        String[] config_strings = new String[config_objs.length];
        System.arraycopy(config_objs, 0, config_strings, 0, config_objs.length);

        ParsedOption new_result = new ParsedOption(this);

        // Now start the 'real' parsing work:
        for (Iterator i = parsed_opt.values(0); i.hasNext(); ) {
            HierarchyElement element = (HierarchyElement) i.next();
            ElementConfigSet set = element.getConfigurationSet();
            ArgumentContainer container = set.toArgumentContainer();
            StringQueue q = new StringQueueImpl(config_strings);
            ParsedElementConfig parsed_cfg = new ParsedElementConfig(element);
            
            parser.parseSet(container, parsed_cfg, q, true);
            new_result.addChild(parsed_cfg);
        }
        result.addChild(new_result);
    }
}
