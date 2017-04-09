package ru.epam.training;

import java.util.*;

public class CustomLinkedList<T> implements List<T> {

    private Node<T> head = new Node<>(null);
    private Node<T> last;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return !head.hasNext();
    }

    @Override
    public boolean contains(Object o) {
        Node<T> node = head;
        while (node.hasNext()) {
            node = node.next;
            if (node.value == null) {
                if (o == null) {
                    return true;
                }
            } else if (node.value.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(T t) {
        Node<T> newNode = new Node<>(t);
        if (last == null) {
            head.next = newNode;
            newNode.previous = head;
            last = newNode;
            size++;
            return true;
        } else {
            last.next = newNode;
            newNode.previous = last;
            last = newNode;
            size++;
            return true;
        }
    }

    @Override
    public void add(int index, T element) {
        checkBounds(index);
        Node<T> previous = head;
        Node<T> current = head.next;
        for (int i = 0; i < index; i++) {
            previous = current;
            current = current.next;
        }
        previous.next = new Node<T>(element);
        previous = previous.next;
        previous.next = current;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node<T> current = head.next;
        while (current != null) {
            if (current.value == null) {
                if (o == null) {
                    removeNode(current);
                    return true;
                }

            } else if (current.value.equals(o)) {
                removeNode(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        head = new Node<>(null);
        last = null;
        size = 0;
    }

    @Override
    public T set(int index, T element) {
        return getNodeByIndex(index).setValue(element);
    }

    @Override
    public T get(int index) {
        return getNodeByIndex(index).value;
    }

    @Override
    public T remove(int index) {
        Node<T> toRemove = getNodeByIndex(index);
        T value = toRemove.value;
        removeNode(toRemove);
        return value;
    }

    @Override
    public int indexOf(Object o) {
        Node<T> currentNode = head.next;
        int index = 0;
        while (currentNode != null) {
            if (currentNode.value == null) {
                if (o == null) {
                    return index;
                } else {
                    currentNode = currentNode.next;
                    index++;
                }
            } else {
                if (currentNode.value.equals(o)) {
                    return index;
                } else {
                    currentNode = currentNode.next;
                    index++;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<T> currentNode = last;
        int index = 0;
        while (currentNode != null) {
            if (currentNode.value == null) {
                if (o == null) {
                    return size - index - 1;
                } else {
                    currentNode = currentNode.previous;
                    index++;
                }
            } else {
                if (currentNode.value.equals(o)) {
                    return size - index - 1;
                } else {
                    currentNode = currentNode.previous;
                    index++;
                }
            }
        }
        return -1;
    }

    @Override
    public Object[] toArray() {
        Object[] listArray = new Object[size];
        Node currentNode = head.next;
        for (int i = 0; i < size; i++) {
            listArray[i] = currentNode.value;
            currentNode = currentNode.next;
        }
        return listArray;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size)
            a = (T1[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        Node<T> currentNode = head.next;
        int iter = 0;
        Object[] array = a;
        while (currentNode != null) {
            array[iter++] = currentNode.value;
            currentNode = currentNode.next;
        }
        return a;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        checkBounds(index);
        if (c.size() == 0) {
            return false;
        }
        Node<T> previous = head;
        Node<T> current = head.next;
        for (int i = 0; i < index; i++) {
            previous = current;
            current = current.next;
        }
        for (T value : c) {
            previous.next = new Node<T>(value);
            previous = previous.next;
            size++;
        }
        previous.next = current;
        return true;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIter(index);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object fromC : c) {
            if (!contains(fromC)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        for (Object fromC : c) {
            if (remove(fromC)) {
                isChanged = true;
            }
        }
        return isChanged;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isChanged = false;
        for (T fromThisColl : this) {
            if (!c.contains(fromThisColl)) {
                remove(fromThisColl);
                isChanged = true;
            }
        }
        return isChanged;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if ((fromIndex > toIndex) || (toIndex > size) || (fromIndex < 0)) {
            throw new IndexOutOfBoundsException();
        }
        if ((toIndex < 0) || (fromIndex < 0)) {
            throw new IndexOutOfBoundsException();
        }
        List<T> newList = new CustomLinkedList<>();
        Node<T> currentNode = getNodeByIndex(fromIndex);
        for (int i = fromIndex; i <= toIndex; i++) {
            newList.add(currentNode.value);
            currentNode = currentNode.next;
        }
        return newList;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIter(0);
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIter(0);
    }

    private void checkBounds(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private Node<T> getNodeByIndex(int index) {
        checkBounds(index);
        Node<T> current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private void removeNode(Node<T> toRemove) {
        final Node<T> next = toRemove.next;
        final Node<T> previous = toRemove.previous;

        previous.next = next;
        toRemove.previous = null;

        if (next == null) {
            last = previous;
        } else {
            next.previous = previous;
            toRemove.next = null;
        }

        toRemove.value = null;
        size--;
    }

    private class ListIter implements ListIterator<T> {
        Node<T> returned;
        private Node<T> next;
        private int nextIndex;

        ListIter(int index) {
            // assert isPositionIndex(index);
            if (index == size) {
                next = null;
            } else {
                next = getNodeByIndex(index);
            }
            nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            returned = next;
            next = next.next;
            nextIndex++;
            return returned.value;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public T previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (next == null) {
                next = last;
                returned = next;
            } else {
                next = next.previous;
                returned = next;
            }
            nextIndex--;
            return returned.value;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (returned == null)
                throw new IllegalStateException();
            CustomLinkedList.this.removeNode(returned);
            nextIndex--;
        }

        @Override
        public void set(T t) {
            if (returned == null) {
                throw new IllegalStateException();
            }
            returned.value = t;
        }

        @Override
        public void add(T t) {
            CustomLinkedList.this.add(nextIndex++, t);
        }
    }

    private class Node<T> {

        private Node<T> next;
        private Node<T> previous;
        private T value;

        public Node(T value) {
            this.value = value;
        }

        public boolean hasNext() {
            return next != null;
        }

        public T setValue(T value) {
            T oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
