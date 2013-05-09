package starj;

import java.util.*;

public class StorageFactory {
    private int flag_count;
    private int counter_count;
    private int storage_space_count;
    private Collection children;

    public StorageFactory() {
        this.flag_count = 0;
        this.counter_count = 0;
        this.storage_space_count = 0;
        this.children = new ArrayList();
    }
    
    public Storage[] getChildren() {
        Storage[] rv = new Storage[this.children.size()];
        return (Storage[]) this.children.toArray(rv);
    }
    
    public int size() {
        return this.children.size();
    }

    public int registerFlag() {
        int total_flags = this.flag_count + 1;
        for (Iterator i = this.children.iterator(); i.hasNext(); ) {
            ((StorageImpl) i.next()).growFlags(total_flags);
        }

        return this.flag_count++;
    }

    public int registerCounter() {
        int total_counters = this.counter_count + 1;
        for (Iterator i = this.children.iterator(); i.hasNext(); ) {
            ((StorageImpl) i.next()).growCounters(total_counters);
        }

        return this.counter_count++;
    }

    public int registerStorageSpace() {
        int total_storage_spaces = this.storage_space_count + 1;
        for (Iterator i = this.children.iterator(); i.hasNext(); ) {
            ((StorageImpl) i.next()).growStorageSpaces(total_storage_spaces);
        }

        return this.storage_space_count++;
    }

    public Storage newStorage() {
        Storage storage = new StorageImpl(this);
        this.children.add(storage);
        return storage;
    }

    private class StorageImpl implements Storage {
        private StorageFactory parent;
        private boolean[] flags;
        private long[] counters;
        private Object[] storage_spaces;

        StorageImpl(StorageFactory parent) {
            this.parent = parent;
        }
        
        private void growFlags(int count) {
            boolean[] flags = this.flags;
            if (flags != null) {
                boolean[] new_flags = new boolean[count];
                System.arraycopy(flags, 0, new_flags, 0, flags.length);
                this.flags = new_flags;
            }
            // else: don't grow the array, since we use a lazy
            // instantiation scheme
        }

        public boolean getFlag(int storage_id) {
            if (this.flags == null) {
                this.flags = new boolean[this.parent.flag_count];
            }
            
            return this.flags[storage_id];
        }

        public void setFlag(int storage_id, boolean value) {
            if (this.flags == null) {
                this.flags = new boolean[this.parent.flag_count];
            }
            this.flags[storage_id] = value;
        }
        
        private void growCounters(int count) {
            long[] counters = this.counters;
            if (counters != null) {
                long[] new_counters = new long[count];
                System.arraycopy(counters, 0, new_counters, 0, counters.length);
                this.counters = new_counters;
            }
            // else: don't grow the array, since we use a lazy
            // instantiation scheme
        }

        public long getCounter(int storage_id) {
            if (this.counters == null) {
                this.counters = new long[this.parent.counter_count];
            }
            return this.counters[storage_id];
        }

        public void stepCounter(int storage_id) {
            if (this.counters == null) {
                this.counters = new long[this.parent.counter_count];
            }
            this.counters[storage_id] += 1L;
        }

        public void decrCounter(int storage_id) {
            if (this.counters == null) {
                this.counters = new long[this.parent.counter_count];
            }
            this.counters[storage_id] -= 1L;
        }

        public void addToCounter(int storage_id, long delta) {
            if (this.counters == null) {
                this.counters = new long[this.parent.counter_count];
            }
            this.counters[storage_id] += delta;
        }

        public void setCounter(int storage_id, long value) {
            if (this.counters == null) {
                this.counters = new long[this.parent.counter_count];
            }
            this.counters[storage_id] = value;
        }
        
        private void growStorageSpaces(int count) {
            Object[] storage_spaces = this.storage_spaces;
            if (storage_spaces != null) {
                Object[] new_storage_spaces = new Object[count];
                System.arraycopy(storage_spaces, 0, new_storage_spaces, 0,
                        storage_spaces.length);
                this.storage_spaces = new_storage_spaces;
            }
            // else: don't grow the array, since we use a lazy
            // instantiation scheme
        }

        public Object getStorageSpace(int storage_id) {
            if (this.storage_spaces == null) {
                this.storage_spaces
                        = new Object[this.parent.storage_space_count];
            }
            return this.storage_spaces[storage_id];
        }

        public void setStorageSpace(int storage_id, Object value) {
            if (this.storage_spaces == null) {
                this.storage_spaces
                        = new Object[this.parent.storage_space_count];
            }
            this.storage_spaces[storage_id] = value;
        }
    }
}
