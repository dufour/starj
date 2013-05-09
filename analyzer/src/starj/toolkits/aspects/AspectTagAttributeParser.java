package starj.toolkits.aspects;

import java.io.*;
import java.util.Arrays;

import starj.coffer.*;

public abstract class AspectTagAttributeParser implements AttributeParser {
    private static final int PAIR_LENGTH = 6; // unsigned short + int = 6 bytes
    
    protected OffsetTagPair[] parsePairs(int length, DataInput input) {
        // Sanity check
        if (length % PAIR_LENGTH != 0) {
            throw new ClassFileFormatException("Invalid table length: "
                    + length);
        }

        // Parse the table
        OffsetTagPair[] pairs = new OffsetTagPair[length / PAIR_LENGTH];
        try {
            for (int i = 0; i < pairs.length; i++) {
                int offset = input.readUnsignedShort();
                int kind = input.readInt();
                pairs[i] = new OffsetTagPair(offset, kind);
            }
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }

        Arrays.sort(pairs);
        return pairs;
    }
}
