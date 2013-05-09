package starj.util;

import java.util.Arrays;

public class IntHashMap {
    public static final int DEFAULT_SIZE = 256;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private IntHashMapEntry data[];
    private int capacity;
    private int cutoff;
    private double loadFactor;
    private int size;

    public IntHashMap() {
        this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public IntHashMap(int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public IntHashMap(double loadFactor) {
        this(DEFAULT_SIZE, loadFactor);
    }

    public IntHashMap(int size, double loadFactor) {
        if (size < 1) {
            throw new RuntimeException("IntHashMap size has to be at least 1");
        }

        if (loadFactor <= 0.0) {
            throw new RuntimeException("IntHashMap load factor must be > 0.0");
        }

        this.size = 0;
        this.capacity = size;
        this.data = new IntHashMapEntry[size];
        this.loadFactor = loadFactor;
        this.cutoff = (int) (this.capacity * loadFactor);
    }

    public Object clone() {
        return new IntHashMap(this.capacity, this.loadFactor);
    }

    public boolean containsKey(int key) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntHashMapEntry e, tmp;

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

    public boolean containsValue(Object value) {
        IntHashMapEntry entry;

        for (int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                if ((value == entry.value) || (value != null && value.equals(entry.value))) {
                    return true;
                }
            }
        }

        return false;
    }


    public Object get(int key) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntHashMapEntry e;

        for (e = this.data[hash]; e != null; e = e.next) {
            if (e.key == key) {
                return e.value;
            }
        }

        return null;
    }

    public Object put(int key, Object value) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntHashMapEntry e, head;

        head = this.data[hash];
        for (e = head; e != null; e = e.next) {
            if (e.key == key) {
                Object v = e.value;
                e.value = value;
                return v;
            }
        }

        this.data[hash] = new IntHashMapEntry(key, value, head);
        if (++this.size >= this.cutoff) {
            rehash(2 * this.capacity + 1);
        }
        return null;
    }

    public Object remove(int key) {
        int hash = (key & 0x7FFFFFFF) % this.capacity;
        IntHashMapEntry e, tmp;

        for (tmp = null, e = this.data[hash]; e != null; tmp = e, e = e.next) {
            if (e.key == key) {
                if (tmp == null) {
                    this.data[hash] = e.next;
                } else {
                    tmp.next = e.next;
                }
                Object v = e.value;
                e = null;
                this.size--;
                return v;
            }
        }

        return null;
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
            IntHashMapEntry newData[];
            IntHashMapEntry entry, tmp;
            int newHash;

            newData = new IntHashMapEntry[newCapacity];
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
        IntHashMapEntry entry;
        for(int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                result[j++] = entry.key;
            }
        }

        return result;
    }

    public Object[] valueSet() {
        Object[] result;
        int j = 0;

        result = new Object[this.size];
        IntHashMapEntry entry;
        for(int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                result[j++] = entry.value;
            }
        }

        return result;
    }

    class IntHashMapEntry {
        int key;
        Object value;
        IntHashMapEntry next;

        public IntHashMapEntry(int key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public IntHashMapEntry(int key, Object value, IntHashMapEntry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
