package starj.util;

import java.util.EmptyStackException;

public class IntStack {
    private int[] data;
    private int p;
    private static final int DEFAULT_SIZE = 32;

    public IntStack() {
        this(DEFAULT_SIZE);
    }

    public IntStack(int initialSize) {
        if (initialSize <= 0) {
            throw new RuntimeException("Stack size cannot be <= 0");
        }
        this.data = new int[initialSize];
        this.p = -1;
    }

    public boolean isEmpty() {
        return (this.p < 0);
    }

    public void clear() {
        this.p = -1;
    }

    public void push(int value) {
        if (this.p == (this.data.length - 1)) {
            // increase array size
            int[] newData = new int[this.data.length * 2 + 1];
            System.arraycopy(this.data, 0, newData, 0, this.data.length);
            this.data = newData;
        }
        this.data[++this.p] = value;
    }

    public int pop() {
        if (this.p < 0) {
            throw new EmptyStackException();
        }
        return this.data[this.p--];
    }

    public int top() {
        if (this.p < 0) {
            throw new EmptyStackException();
        }
        return this.data[this.p];
    }

    public int peek() {
        /* same as top(), just added for compatibility with Sun's
           implementation */
        if (this.p < 0) {
            throw new EmptyStackException();
        }
        return this.data[this.p];
    }

    public int peek(int default_value) {
        if (this.p < 0) {
            return default_value;
        }
        return this.data[this.p];
    }

    public int top(int default_value) {
        if (this.p < 0) {
            return default_value;
        }
        return this.data[this.p];
    }

    public void dup() {
        if (this.p < 0) {
            throw new EmptyStackException();
        }
        
        this.push(this.data[this.p]);
    }
    
    public void dup(int default_value) {
        if (this.p < 0) {
            this.push(default_value);
        } else {
            this.push(this.data[this.p]);
        }
    }

    public void replace(int value) {
        if (this.p < 0) {
            throw new EmptyStackException();
        }

        this.data[this.p] = value;
    }

    public void pushOrReplace(int value) {
        if (this.p < 0) {
            this.data[++this.p] = value;
        } else {
            this.data[this.p] = value;
        }
    }

    public int capacity() {
        return this.data.length;
    }

    public int search(int value) {
        for (int q = this.p; q >= 0; q--) {
            if (this.data[q] == value) {
                return (this.p - q + 1);
            }
        }

        return -1;
    }

    public int size() {
        return (this.p + 1);
    }

    public String toString() {
        String result = "[";
        for (int i = 0; i < this.p; i++) {
            result = result + this.data[i] + ", ";
        }
        
        if (this.p >= 0) {
            result = result + this.data[this.p];
        }
        
        return result + "]";
    }
}
