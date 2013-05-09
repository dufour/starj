package starj.coffer;

import java.util.*;
import java.io.*;

import starj.io.logging.LogManager;

public class AttributeFactory {
    private static AttributeFactory instance;

    public static AttributeFactory getInstance() {
        if (instance == null) {
            instance = new AttributeFactory();
        }

        return instance;
    }
    
    private Map parsers;

    public AttributeFactory() {
        this.parsers = new HashMap();

        // Register standard attributes
        this.registerAttribute(ConstantValueAttribute.ATTRIBUTE_NAME,
                new ConstantValueAttributeParser());
        this.registerAttribute(CodeAttribute.ATTRIBUTE_NAME,
                new CodeAttributeParser());
        this.registerAttribute(ExceptionsAttribute.ATTRIBUTE_NAME,
                new ExceptionsAttributeParser());
        this.registerAttribute(InnerClassesAttribute.ATTRIBUTE_NAME,
                new InnerClassesAttributeParser());
        this.registerAttribute(SyntheticAttribute.ATTRIBUTE_NAME,
                new SyntheticAttributeParser());
        this.registerAttribute(SourceFileAttribute.ATTRIBUTE_NAME,
                new SourceFileAttributeParser());
        this.registerAttribute(LineNumberTableAttribute.ATTRIBUTE_NAME,
                new LineNumberTableAttributeParser());
        this.registerAttribute(LocalVariableTableAttribute.ATTRIBUTE_NAME,
                new LocalVariableTableAttributeParser());
        this.registerAttribute(DeprecatedAttribute.ATTRIBUTE_NAME,
                new DeprecatedAttributeParser());

    }

    public void registerAttribute(String name, AttributeParser parser) {
        this.parsers.put(name, parser);
    }

    public AttributeParser unregisterAttribute(String name) {
        return (AttributeParser) this.parsers.remove(name);
    }

    /*
    public Attribute[] parseAttributes(DataInput input,
            ConstantPool constant_pool) {
        try {
            int attribute_count = input.readUnsignedShort();
            Attribute[] attributes = new Attribute[attribute_count];
            for (int i = 0; i < attribute_count; i++) {
                attributes[i] = parse(input, constant_pool);
            }

            return attributes;
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
    */

    public Attribute parse(DataInput input, ConstantPool cp) {
        try {
            int name_index = input.readUnsignedShort();
            int length = input.readInt();

            Constant c = cp.get(name_index);
            if (c == null) {
                throw new ClassFileFormatException("Invalid constant pool index");
            }

            if (c.getTag() != Constant.CONSTANT_Utf8) {
                throw new ClassFileFormatException("Contant pool index does not point to a Utf8 constant");
            }

            String attr_name = ((Utf8Constant) c).getValue();
            AttributeParser parser
                    = (AttributeParser) this.parsers.get(attr_name);
            if (parser != null) {
                return parser.parseAttribute(name_index, length, input,
                        cp, this);
            }

            // Unknown attribute type
            LogManager.v().logWarning("Unknown attribute name: '" + attr_name
                    + "'");
            byte[] data = new byte[length];
            input.readFully(data);
            return new GenericAttribute(cp, name_index, data);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}

class ConstantValueAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        if (length != 2) {
            throw new ClassFileFormatException("Invalid attribute length");
        }

        try {
            return new ConstantValueAttribute(cp, input.readUnsignedShort());
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}

class SyntheticAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        if (length != 0) {
            throw new ClassFileFormatException("Invalid attribute length");
        }

        return new SyntheticAttribute(cp);
    }
}

class SourceFileAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        if (length != 2) {
            throw new ClassFileFormatException("Invalid attribute length");
        }

        try {
            return new SourceFileAttribute(cp, input.readUnsignedShort());
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}

class ExceptionsAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        try {
            int exception_count = input.readUnsignedShort();
            if (length != (exception_count * 2 + 2)) {
                throw new ClassFileFormatException("Invalid attribute length");
            }

            int[] exceptions = new int[exception_count];
            for (int i = 0; i < exception_count; i++) {
                exceptions[i] = input.readUnsignedShort();
            }

            return new ExceptionsAttribute(cp, exceptions);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}

class InnerClassesAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        try {
            int class_count = input.readUnsignedShort();
            if (length != (class_count * 8 + 2)) {
                throw new ClassFileFormatException("Invalid attribute length");
            }

            InnerClass[] classes = new InnerClass[class_count];
            for (int i = 0; i < class_count; i++) {
                classes[i] = new InnerClass(
                    input.readUnsignedShort(),
                    input.readUnsignedShort(),
                    input.readUnsignedShort(),
                    input.readShort()
                );
            }

            return new InnerClassesAttribute(cp, classes);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}

class DeprecatedAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        if (length != 0) {
            throw new ClassFileFormatException("Invalid attribute length");
        }

        return new DeprecatedAttribute(cp);
    }
}

class LineNumberTableAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        try {
            int line_number_count = input.readUnsignedShort();
            if (length != (line_number_count * 4 + 2)) {
                throw new ClassFileFormatException("Invalid attribute length");
            }

            LineNumber[] line_number_table = new LineNumber[line_number_count];
            for (int i = 0; i < line_number_count; i++) {
                line_number_table[i] = new LineNumber(
                    input.readUnsignedShort(),
                    input.readUnsignedShort()
                );
            }

            return new LineNumberTableAttribute(cp, line_number_table);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}

class LocalVariableTableAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        try {
            int local_variable_count = input.readUnsignedShort();
            if (length != (local_variable_count * 10 + 2)) {
                throw new ClassFileFormatException("Invalid attribute length");
            }

            LocalVariable[] local_variable_table = 
                    new LocalVariable[local_variable_count];
            for (int i = 0; i < local_variable_count; i++) {
                local_variable_table[i] = new LocalVariable(
                    input.readUnsignedShort(),
                    input.readUnsignedShort(),
                    input.readUnsignedShort(),
                    input.readUnsignedShort(),
                    input.readUnsignedShort()
                );
            }

            return new LocalVariableTableAttribute(cp, local_variable_table);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}

class CodeAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        try {
            int max_stack = input.readUnsignedShort();
            int max_locals = input.readUnsignedShort();
            Code code = new Code(input, cp);
            int exception_table_length = input.readUnsignedShort();
            
            ExceptionTableEntry[] exception_table =
                    new ExceptionTableEntry[exception_table_length];
            for (int i = 0; i < exception_table_length; i++) {
                exception_table[i] = new ExceptionTableEntry(
                    input.readUnsignedShort(),
                    input.readUnsignedShort(),
                    input.readUnsignedShort(),
                    input.readUnsignedShort()
                );
            }

            Attributes attributes = new Attributes(input,
                    cp, factory);
                
            return new CodeAttribute(cp, max_stack, max_locals, code, 
                    exception_table, attributes);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}
