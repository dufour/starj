package starj.util;

import java.util.Arrays;

public class ObjectToIntHashMap {
    public static final int DEFAULT_SIZE = 256;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private ObjectToIntHashMapEntry data[];
    private int capacity;
    private int cutoff;
    private double loadFactor;
    private int size;

    public ObjectToIntHashMap() {
        this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public ObjectToIntHashMap(int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public ObjectToIntHashMap(double loadFactor) {
        this(DEFAULT_SIZE, loadFactor);
    }

    public ObjectToIntHashMap(int size, double loadFactor) {
        if (size < 1) {
            throw new RuntimeException("ObjectToIntHashMap size has to be at least 1");
        }

        if (loadFactor <= 0.0) {
            throw new RuntimeException("ObjectToIntHashMap load factor must be > 0.0");
        }

        this.size = 0;
        this.capacity = size;
        this.data = new ObjectToIntHashMapEntry[size];
        this.loadFactor = loadFactor;
        this.cutoff = (int) (this.capacity * loadFactor);
    }

    public Object clone() {
        return new ObjectToIntHashMap(this.capacity, this.loadFactor);
    }

    public boolean containsKey(Object key) {
        int hash = ((key != null ? key.hashCode() : 0) & 0x7FFFFFFF) % this.capacity;
        ObjectToIntHashMapEntry e, tmp;

        for (tmp = null, e = this.data[hash]; e != null; tmp = e, e = e.next) {
            if ((e.key == key) || (e.key != null && e.key.equals(key))) {
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

    public boolean containsValue(int value) {
        ObjectToIntHashMapEntry entry;

        for (int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                if (value == entry.value) {
                    return true;
                }
            }
        }

        return false;
    }

    public int get(Object key) {
        int hash = ((key != null ? key.hashCode() : 0) & 0x7FFFFFFF) % this.capacity;
        ObjectToIntHashMapEntry e;

        for (e = this.data[hash]; e != null; e = e.next) {
            if ((e.key == key) || (e.key != null && e.key.equals(key))) {
                return e.value;
            }
        }

        return 0;
    }
    
    public int step(Object key) {
        return this.step(key, 1);
    }

    public int step(Object key, int delta) {
        int hash = ((key != null ? key.hashCode() : 0) & 0x7FFFFFFF) % this.capacity;
        ObjectToIntHashMapEntry e;
        
        // Look for the key
        for (e = this.data[hash]; e != null; e = e.next) {
            if ((e.key == key) || (e.key != null && e.key.equals(key))) {
                // Found it, just increase its value
                e.value += delta;
                return e.value;
            }
        }

        // Not found. Pretend stored value was 0
        this.put(key, delta);
        return delta;
    }

    public int put(Object key, int value) {
        int hash = ((key != null ? key.hashCode() : 0) & 0x7FFFFFFF) % this.capacity;
        ObjectToIntHashMapEntry e, head;

        head = this.data[hash];
        for (e = head; e != null; e = e.next) {
            if ((e.key == key) || (e.key != null && e.key.equals(key))) {
                int v = e.value;
                e.value = value;
                return v;
            }
        }

        this.data[hash] = new ObjectToIntHashMapEntry(key, value, head);
        if (++this.size >= this.cutoff) {
            rehash(2 * this.capacity + 1);
        }
        return 0;
    }

    public int remove(Object key) {
        int hash = ((key != null ? key.hashCode() : 0) & 0x7FFFFFFF) % this.capacity;
        ObjectToIntHashMapEntry e, tmp;

        for (tmp = null, e = this.data[hash]; e != null; tmp = e, e = e.next) {
            if ((e.key == key) || (e.key != null && e.key.equals(key))) {
                if (tmp == null) {
                    this.data[hash] = e.next;
                } else {
                    tmp.next = e.next;
                }
                int v = e.value;
                e = null;
                this.size--;
                return v;
            }
        }

        return 0;
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
            ObjectToIntHashMapEntry newData[];
            ObjectToIntHashMapEntry entry, tmp;
            int newHash;

            newData = new ObjectToIntHashMapEntry[newCapacity];
            for (int i = 0; i < this.capacity; i++) {
                for (entry = this.data[i]; entry != null; ) {
                    tmp = entry.next;
                    newHash = ((entry.key != null ? entry.key.hashCode() : 0) & 0x7FFFFFFF) % newCapacity;
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

    public Object[] keySet() {
        Object[] result;
        int j = 0;

        result = new Object[this.size];
        ObjectToIntHashMapEntry entry;
        for(int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                result[j++] = entry.key;
            }
        }

        return result;
    }

    public int[] valueSet() {
        int[] result;
        int j = 0;

        result = new int[this.size];
        ObjectToIntHashMapEntry entry;
        for(int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                result[j++] = entry.value;
            }
        }

        return result;
    }

    class ObjectToIntHashMapEntry {
        Object key;
        int value;
        ObjectToIntHashMapEntry next;

        public ObjectToIntHashMapEntry(Object key, int value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public ObjectToIntHashMapEntry(Object key, int value, ObjectToIntHashMapEntry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
