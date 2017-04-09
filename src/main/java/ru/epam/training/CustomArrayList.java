package ru.epam.training;

import java.util.*;

public class CustomArrayList<T> implements List<T> {

    public static final int CAPACITY = 10;

    private Object[] data = new Object[CAPACITY];
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i] == null) {
                if (o == null) {
                    return true;
                }
            } else if (data[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, size);
    }

    @Override
    public boolean add(T t) {
        ensureCapacity(size);
        data[size++] = t;
        return false;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i] == null){
                if (o == null){
                    remove(i);
                    return true;
                }
            }else if (data[i].equals(o)) {
                remove(i);
                return true;
            }

        }
        return false;
    }

    @Override
    public void clear() {
        data = new Object[CAPACITY];
        size = 0;
    }

    @Override
    public T get(int index) {
        checkBounds(index);
        return (T) data[index];
    }

    @Override
    public T set(int index, T element) {
        checkBounds(index);
        T oldValue = (T) data[index];
        data[index] = element;
        return oldValue;
    }

    @Override
    public T remove(int index) {
        checkBounds(index);
        int length = data.length - index;
        T value = (T) data[index];
        System.arraycopy(data, index + 1, data, index, length - 1);
        size--;
        return value;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T value : c) {
            this.add(value);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        Object[] arrayToInsert = c.toArray();
        int oldLength = data.length;
        int insertionLength = arrayToInsert.length;
        int newDataLength = oldLength + insertionLength;
        Object[] newData = new Object[newDataLength];
        System.arraycopy(data, 0, newData, 0, index);
        System.arraycopy(arrayToInsert, 0, newData, index, insertionLength);
        System.arraycopy(data, index, newData, index + insertionLength, oldLength - index);
        data = newData;
        size = size + insertionLength;
        return insertionLength > 0;
    }

    @Override
    public void add(int index, T element) {
        checkBounds(index);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, data.length - index - 1);
        data[index] = element;
        size++;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size)
            a = (T1[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        System.arraycopy(data, 0, a, 0, size);
        return a;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i] == null){
                if(o == null){
                    return i;
                }
            } else {
                if (data[i].equals(o)){
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (data[i] == null){
                if(o == null){
                    return i;
                }
            } else {
                if (data[i].equals(o)){
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    private void ensureCapacity(int size) {
        if (size == data.length) {
            int newLength = (data.length * 3) / 2 + 1;
            data = Arrays.copyOf(data, newLength);
        }
    }

    private void checkBounds(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }
}
