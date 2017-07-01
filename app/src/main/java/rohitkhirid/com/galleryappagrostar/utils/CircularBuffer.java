package rohitkhirid.com.galleryappagrostar.utils;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * NOTE : OPEN SOURCE FILE : COPIED FROM INTERNET
 */
public class CircularBuffer<E> {
    /**
     * Reflects the capacity of the buffer
     */
    protected int mCapacity;
    /**
     * Reflects the current size of the buffer
     */
    protected int mSize;
    /**
     * Holds the count of modifications done to the buffer
     */
    protected int mModCount;
    /**
     * Holds the objects of the buffer
     */
    protected Object[] mArray;
    /**
     * Points to the starting index of the circular buffer
     */
    protected int mStartIndex;

    CircularBuffer(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity < 0: " + capacity);
        }
        mArray = (capacity == 0 ? new Object[]{} : new Object[capacity]);
        this.mCapacity = capacity;
        this.mSize = 0;
        this.mStartIndex = 0;
    }

    public boolean add(E object) {
        try {
            if (mSize == mCapacity) {
                int additionIndex = mStartIndex % mSize;
                mArray[additionIndex] = object;
                mStartIndex = additionIndex + 1;
                mModCount++;
                return true;
            } else {
                mArray[mSize] = object;
                mSize += 1;
                mStartIndex = mSize;
                mModCount++;
                return true;
            }
        } catch (Exception e) {
            DebugLog.e("Could not add object to buffer. Reason : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void clear() {
        try {
            if (mSize != 0) {
                Arrays.fill(mArray, 0, mSize, null);
                mSize = 0;
                mModCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CircularBuffer)) {
            return false;
        }
        CircularBuffer<?> that = (CircularBuffer<?>) o;
        int s = mSize;
        if (that.size() != s) {
            return false;
        }
        Object[] a = mArray;
        Iterator<?> it = that.iterator();
        for (int i = 0; i < s; i++) {
            Object eThis = a[i];
            Object eThat = it.next();
            if (eThis == null ? eThat != null : !eThis.equals(eThat)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return mSize == 0;
    }

    @NonNull
    public Iterator<E> iterator() {
        return new CircularBufferIterator();
    }

    private class CircularBufferIterator implements Iterator {
        /**
         * Number of elements remaining in this iteration
         */
        private int remaining = mSize;

        /**
         * Index of element that remove() would remove, or -1 if no such elt
         */
        private int removalIndex = -1;

        /**
         * The expected ModCount value
         */
        private int expectedModCount = mModCount;

        @Override
        public boolean hasNext() {
            return remaining != 0;
        }

        @Override
        public Object next() {
            CircularBuffer<E> ourBuffer = CircularBuffer.this;
            int rem = remaining;
            if (ourBuffer.mModCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (rem == 0) {
                throw new NoSuchElementException();
            }
            remaining = rem - 1;
            removalIndex = ourBuffer.mSize - rem;
            return (E) ourBuffer.mArray[removalIndex];
        }

        @Override
        public void remove() {
            Object[] a = mArray;
            int removalIdx = removalIndex;
            if (mModCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (removalIdx < 0) {
                throw new IllegalStateException();
            }
            System.arraycopy(a, removalIdx + 1, a, removalIdx, remaining);
            a[--mSize] = null;  // Prevent memory leak
            removalIndex = -1;
            expectedModCount = ++mModCount;
        }
    }

    public E remove(int index) {
        Object[] a = mArray;
        int s = mSize;
        if (index >= s) {
            throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + mSize);
        }
        @SuppressWarnings("unchecked") E result = (E) a[index];
        System.arraycopy(a, index + 1, a, index, --s - index);
        a[s] = null;  // Prevent memory leak
        mSize = s;
        mModCount++;
        return result;
    }

    public int size() {
        return mSize;
    }

    @Override
    public String toString() {
        E[] buffer = getBuffer();
        int bufferSize = buffer.length;
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append("\n");
        builder.append(buffer[0]);
        for (int i = 1; i < bufferSize; i++) {
            builder.append("\n");
            builder.append(buffer[i]);
        }
        builder.append("\n");
        builder.append("]");
        return builder.toString();
    }

    public E[] getBuffer() {
        Object[] buffer = new Object[mSize];
        int loopSize = mStartIndex + mSize;
        for (int i = mStartIndex, j = 0; i < loopSize; i++, j++) {
            buffer[j] = (mArray[i % mSize]);
        }
        return (E[]) buffer;
    }
}