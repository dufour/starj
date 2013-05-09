package starj.util;

import java.util.Arrays;

public class IntHashSet {
    public static final int DEFAULT_SIZE = 256;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private IntHashSetEntry data[];
    private int capacity;
    private int cutoff;
    private double loadFactor;
    private int size;
    
    public IntHashSet() {
        this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public IntHashSet(int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public IntHashSet(double loadFactor) {
        this(DEFAULT_SIZE, loadFactor);
    }

    public IntHashSet(int size, double loadFactor) {
        if (size < 1) {
            throw new RuntimeException("IntHashSet size has to be at least 1");
        }

        if (loadFactor <= 0.0) {
            throw new RuntimeException("IntHashSet load factor must be > 0.0");
        }

        this.size = 0;
        this.capacity = size;
        this.data = new IntHashSetEntry[size];
        this.loadFactor = loadFactor;
        this.cutoff = (int) (this.capacity * loadFactor);
    }

    public Object clone() {
        return new IntHashSet(this.capacity, this.loadFactor);
    }

    public boolean contains(int value) {
        int hash = (value & 0x7FFFFFFF) % this.capacity;
        IntHashSetEntry e;

        for (e = this.data[hash]; e != null; e = e.next) {
            if (e.value == value) {
                return true;
            }
        }

        return false;
    }

    public void add(int value) {
        int hash = (value & 0x7FFFFFFF) % this.capacity;
        IntHashSetEntry e, head;

        head = this.data[hash];
        for (e = head; e != null; e = e.next) {
            if (e.value == value) {
                return ;
            }
        }
        
        this.data[hash] = new IntHashSetEntry(value, head);
        if (++this.size >= this.cutoff) {
            rehash(2 * this.capacity + 1);
        }
    }

    public boolean remove(int value) {
        int hash = (value & 0x7FFFFFFF) % this.capacity;
        IntHashSetEntry e, tmp;

        for (tmp = null, e = this.data[hash]; e != null; tmp = e, e = e.next) {
            if (e.value == value) {
                if (tmp == null) {
                    this.data[hash] = e.next;
                } else {
                    tmp.next = e.next;
                }
                e = null;
                this.size--;
                return true;
            }
        }

        return false;
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
            IntHashSetEntry newData[];
            IntHashSetEntry entry, tmp;
            int newHash;

            newData = new IntHashSetEntry[newCapacity];
            for (int i = 0; i < this.capacity; i++) {
                for (entry = this.data[i]; entry != null; ) {
                    tmp = entry.next;
                    newHash = (entry.value & 0x7FFFFFFF) % newCapacity;
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

    public int[] toArray() {
        int[] result;
        int j = 0;

        result = new int[this.size];
        IntHashSetEntry entry;
        for(int i = 0; i < this.capacity; i++) {
            for (entry = this.data[i]; entry != null; entry = entry.next) {
                result[j++] = entry.value;
            }
        }

        return result;
    }
    
    class IntHashSetEntry {
        int value;
        IntHashSetEntry next;

        public IntHashSetEntry(int value) {
            this.value = value;
            this.next = null;
        }
        
        public IntHashSetEntry(int value, IntHashSetEntry next) {
            this.value = value;
            this.next = next;
        }
    }
}


