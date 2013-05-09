package starj.util;

import java.util.Arrays;

public class IntToLongHashMap {
    public static final int DEFAULT_SIZE = 256;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private IntToLongHashMapEntry data[];
    private int capacity;
    private int cutoff;
    private double loadFactor;
    private int size;
    private long defaultValue;

    public IntToLongHashMap() {
        this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public IntToLongHashMap(int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public IntToLongHashMap(double loadFactor) {
        this(DEFAULT_SIZE, loadFactor);
    }

    public IntToLongHashMap(int size, double loadFactor) {
        this(size, loadFactor, 0L);
    }

    public IntToLongHashMap(int size, long defaultValue) {
        this(size, DEFAULT_LOAD_FACTOR, defaultValue);
    }
    public IntToLongHashMap(int size, double loadFactor, long defaultValue) {
        if (size < 1) {
            throw new RuntimeException("IntToLongHashMap size has to be at least 1");
        }

        if (loadFactor <= 0.0) {
            throw new RuntimeException("IntToLongHashMap load factor must be > 0.0");
        }

        this.size = 0;
        this.capacity = size;
        this.data = new IntToLongHashMapEntry[size];
        this.loadFactor = loadFactor;
        this.cutoff = (int) (this.capacity * loadFactor);
        this.defaultValue = defaultValue;
    }

    public Object clone() {
        return new IntToLongHashMap(this.capacity, this.loadFactor);
    }

    public long getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(long value) {
        this.defaultValue = value;
    }

    public boolean containsKey(int key) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntToLongHashMapEntry e, tmp;

        for (tmp = null, e = this.data[hash]; e != null; tmp = e, e = e.next) {
            if (e.key == key) {
                /* Assume that it is likely that
                 * an eventual call to get() for the same
                 * key is near */
                if (tmp != null) {
                    tmp.next = e.next;
                    e.next = this.data[hash];
                    this.data[hash] = e;
                }
                return true;
            }
        }

        return false;
    }

    public boolean containsValue(long value) {
        IntToLongHashMapEntry entry;

        for (int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                if (value == entry.value) {
                    return true;
                }
            }
        }

        return false;
    }

    public long get(int key) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntToLongHashMapEntry e;

        for (e = this.data[hash]; e != null; e = e.next) {
            if (e.key == key) {
                return e.value;
            }
        }

        return this.defaultValue;
    }
    
    public long step(int key) {
        return this.step(key, 1L);
    }

    public long step(int key, long delta) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntToLongHashMapEntry e;
        
        // Look for the key
        for (e = this.data[hash]; e != null; e = e.next) {
            if (e.key == key) {
                // Found it, just increase its value
                e.value += delta;
                return e.value;
            }
        }

        // Not found. Pretend stored value was 0
        this.put(key, delta);
        return delta;
    }

    public long put(int key, long value) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntToLongHashMapEntry e, head;

        head = this.data[hash];
        for (e = head; e != null; e = e.next) {
            if (e.key == key) {
                long v = e.value;
                e.value = value;
                return v;
            }
        }

        this.data[hash] = new IntToLongHashMapEntry(key, value, head);
        if (++this.size >= this.cutoff) {
            rehash(2 * this.capacity + 1);
        }
        return this.defaultValue;
    }

    public long remove(int key) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntToLongHashMapEntry e, tmp;

        for (tmp = null, e = this.data[hash]; e != null; tmp = e, e = e.next) {
            if (e.key == key) {
                if (tmp == null) {
                    this.data[hash] = e.next;
                } else {
                    tmp.next = e.next;
                }
                long v = e.value;
                e = null;
                this.size--;
                return v;
            }
        }

        return this.defaultValue;
    }

    public boolean isEmpty() {
        return (this.size == 0);
    }

    public int size() {
        return this.size;
    }

    public void clear() {
        Arrays.fill(this.data, null);
        this.size = 0;
    }

    private void rehash(int newCapacity) {
        if (newCapacity > this.capacity) {
            IntToLongHashMapEntry newData[];
            IntToLongHashMapEntry entry, tmp;
            int newHash;

            newData = new IntToLongHashMapEntry[newCapacity];
            for (int i = 0; i < this.capacity; i++) {
                for (entry = this.data[i]; entry != null; ) {
                    tmp = entry.next;
                    newHash = (entry.key & 0x7FFFFFFF) % newCapacity;
                    entry.next = newData[newHash];
                    newData[newHash] = entry;
                    entry = tmp;
                }
            }

            this.data = newData;
            this.capacity = newCapacity;
            this.cutoff = (int)(newCapacity * this.loadFactor);
        }
    }

    public int[] keySet() {
        int[] result;
        int j = 0;

        result = new int[this.size];
        IntToLongHashMapEntry entry;
        for(int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                result[j++] = entry.key;
            }
        }

        return result;
    }

    public long[] valueSet() {
        long[] result;
        int j = 0;

        result = new long[this.size];
        IntToLongHashMapEntry entry;
        for(int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                result[j++] = entry.value;
            }
        }

        return result;
    }

    class IntToLongHashMapEntry {
        int key;
        long value;
        IntToLongHashMapEntry next;

        public IntToLongHashMapEntry(int key, long value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public IntToLongHashMapEntry(int key, long value, IntToLongHashMapEntry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
