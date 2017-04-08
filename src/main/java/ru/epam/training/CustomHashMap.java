package ru.epam.training;

import java.util.*;

/**
 * Simple implementation of a Map interface, don`t have ability to change size.
 * It has only default capacity.
 *
 * @param <K> key for entry of the map
 * @param <V> value for entry of the map
 */

public class CustomHashMap<K, V> implements Map<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    private CustomEntry<K, V>[] buckets = new CustomEntry[DEFAULT_CAPACITY];

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
    public boolean containsKey(Object key) {
        CustomEntry<K, V> customEntry = getEntry(key);
        return (customEntry == null) ? false : true;
    }

    @Override
    public boolean containsValue(Object value) {
        for (CustomEntry<K, V> currentEntry : buckets) {
            while (currentEntry != null) {
                if (currentEntry.value == null) {
                    if (value == null) {
                        return true;
                    } else {
                        currentEntry = currentEntry.next;
                    }
                } else {
                    if (currentEntry.value.equals(value)) {
                        return true;
                    }
                    currentEntry = currentEntry.next;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        CustomEntry<K, V> currentEntry = getEntry(key);
        return (currentEntry == null) ? null : currentEntry.value;
    }

    @Override
    public V put(K key, V value) {
        CustomEntry<K, V> currentEntry = getEntry(key);
        int bucketNumber = hash(key);
        if (currentEntry == null) {
            CustomEntry<K, V> newEntry = new CustomEntry<>(key, value);
            newEntry.next = buckets[bucketNumber];
            buckets[bucketNumber] = newEntry;
            size++;
            return null;
        } else {
            V oldValue = currentEntry.setValue(value);
            return oldValue;
        }
    }

    @Override
    public V remove(Object key) {
        int bucketNumber = hash(key);
        CustomEntry<K, V> previousEntry = null;
        CustomEntry<K, V> currentEntry = buckets[bucketNumber];
        while (currentEntry != null) {
            if (currentEntry.key == null) {
                if (key == null) {
                    V oldValue = currentEntry.value;
                    buckets[bucketNumber] = null;
                    size--;
                    return oldValue;
                }
            }
            if (currentEntry.key.equals(key)) {
                V oldValue = currentEntry.value;
                if (previousEntry == null) {
                    buckets[bucketNumber] = currentEntry.next;
                    size--;
                    return oldValue;
                }
                previousEntry.next = currentEntry.next;
                size--;
                return oldValue;
            } else {
                previousEntry = currentEntry;
                currentEntry = currentEntry.next;
            }
        }
        return null;
    }

    /**
     * @param m map for insert
     * @throws NullPointerException if m == null
     */

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> insertionEntry : m.entrySet()) {
            this.put(insertionEntry.getKey(), insertionEntry.getValue());
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Collection<V> values() {
        return new ValueCollection();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(Objects.hashCode(key) % buckets.length);
    }

    private CustomEntry<K, V> getEntry(Object key) {
        int bucketNumber = hash(key);
        CustomEntry<K, V> currentEntry = buckets[bucketNumber];
        while (currentEntry != null) {
            if (currentEntry.key == null) {
                if (key == null) {
                    return currentEntry;
                }
            } else {
                if (key.equals(currentEntry.key)) {
                    return currentEntry;
                } else {
                    currentEntry = currentEntry.next;
                }
            }
        }
        return null;
    }

    private class CustomEntry<K, V> implements Map.Entry<K, V> {

        private final K key;
        private V value;
        private CustomEntry<K, V> next = null;

        CustomEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public CustomEntry<K, V> next() {
            return this.next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public int hashCode() {
            return Objects.hashCode(key) + Objects.hashCode(value);
        }
    }

    private class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return CustomHashMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return CustomHashMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return CustomHashMap.this.remove(o) != null;
        }
    }

    private class ValueCollection extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return CustomHashMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return CustomHashMap.this.containsValue(o);
        }
    }

    private class EntrySet extends AbstractSet<Entry<K, V>> {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return CustomHashMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                return CustomHashMap.this.containsKey(((Entry) o).getKey());
            }
            return false;
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                return CustomHashMap.this.remove(((Entry) o).getKey()) != null;
            }
            return false;
        }
    }

    private abstract class HashMapIterator implements Iterator {
        CustomEntry<K, V>[] entries = new CustomEntry[size];
        int position = 0;

        HashMapIterator() {
            for (CustomEntry currentEntry : buckets) {
                while (currentEntry != null) {
                    entries[position] = currentEntry;
                    position++;
                    currentEntry = currentEntry.next;
                }
            }
            position = 0;
        }

        @Override
        public boolean hasNext() {
            return position < entries.length;
        }

        @Override
        public void remove() {
            CustomHashMap.this.remove(entries[position++].key);
        }
    }

    private class KeyIterator extends HashMapIterator {
        @Override
        public K next() {
            return entries[position++].key;
        }
    }

    private class ValueIterator extends HashMapIterator {
        @Override
        public V next() {
            return entries[position++].value;
        }
    }

    private class EntryIterator extends HashMapIterator {
        @Override
        public CustomEntry<K, V> next() {
            return entries[position++];
        }
    }

}
