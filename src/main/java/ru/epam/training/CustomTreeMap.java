package ru.epam.training;

import java.util.*;

public class CustomTreeMap<K extends Comparable<K>, V> implements Map<K, V> {
    private Node<K, V> root;
    V oldValue;
    int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);
        return find(root, (K) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (root == null) return false;
        if (root.value == null) {
            return value == null;
        } else {
            return root.value.equals(value);
            
        }
    }

    @Override
    public V get(Object key) {
        Objects.requireNonNull(key);
        return get(root, (K) key);
    }

    @Override
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        Node<K, V> node = root;
        if (node == null) {
            key.compareTo(key);
            root = new Node<>(key, value);
            size++;
            return null;
        }
        put(root, key, value);
        return this.oldValue;
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (oldValue != null) {
            oldValue = null;
        }
        if (node == null) {
            size++;
            return new Node<>(key, value);
        }
        if (node.key.equals(key)) {
            this.oldValue = node.value;
            node.value = value;
        } else if (node.key.compareTo(key) > 0) {
            node.left = put(node.left, key, value);
        } else {
            node.right = put(node.right, key, value);
        }
        return node;
    }

    private Node<K, V> find(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.equals(key)) {
            return node;
        } else if (node.key.compareTo(key) > 0) {
            return find(node.left, key);
        } else {
            return find(node.right, key);
        }
    }

    private V get(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) > 0) {
            return get(node.left, key);
        } else if (node.key.compareTo(key) < 0) {
            return get(node.right, key);
        } else return node.value;
    }

    @Override
    public V remove(Object key) {
        Objects.requireNonNull(key);
        root = removeNode(root, (K) key);
        return oldValue;
    }

    private Node<K, V> removeNode(Node<K, V> node, K key) {
        if (node == null) {
            oldValue = null;
            return null;
        }
        if (key.compareTo(node.key) > 0) {
            node = removeNode(node.right, key);
        } else if (key.compareTo(node.key) < 0) {
            node = removeNode(node.left, key);
        } else {
            oldValue = node.value;
            size--;
            if (node.right == null) {
                return node.left;
            }
            if (node.left == null) {
                return node.right;
            }
            Node<K, V> temp = node;
            node = getMinNode(node.right);
            node.right = deleteMinNode(temp.right);
            node.left = temp.left;
        }
        return node;
    }

    private Node<K, V> deleteMinNode(Node<K, V> node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMinNode(node.left);
        return node;
    }

    private Node<K, V> getMinNode(Node<K, V> node) {
        if (node.left == null) {
            return node;
        }
        return getMinNode(node.left);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> insertionEntry : m.entrySet()) {
            this.put(insertionEntry.getKey(), insertionEntry.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
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

    private class Node<K extends Comparable<K>, V> implements Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }


        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            V oldValue = this.value;
            this.value = (V) value;
            return oldValue;
        }
    }

    private class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return CustomTreeMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return CustomTreeMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return CustomTreeMap.this.remove(o) != null;
        }
    }

    private class ValueCollection extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return CustomTreeMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return CustomTreeMap.this.containsValue(o);
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new NodeIterator();
        }

        @Override
        public int size() {
            return CustomTreeMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                return CustomTreeMap.this.containsKey(((Map.Entry) o).getKey());
            }
            return false;
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                return CustomTreeMap.this.remove(((Map.Entry) o).getKey()) != null;
            }
            return false;
        }
    }

    private abstract class TreeMapIterator implements Iterator {
        private Node<K, V>[] nodeArray = new Node[size];
        private int addIndex = 0;
        private int currentIndex = 0;


        TreeMapIterator() {
            makeIterator(root);
        }

        private void makeIterator(Node<K, V> node) {
            if (node == null) {
                return;
            }

            makeIterator(node.left);
            nodeArray[addIndex++] = node;
            makeIterator(node.right);
        }

        @Override
        public boolean hasNext() {

            return currentIndex < nodeArray.length;
        }

        @Override
        public Object next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return nodeArray[currentIndex++];
        }

    }

    private class NodeIterator extends TreeMapIterator {
        @Override
        public Node<K, V> next() {
            return (Node<K, V>) super.next();
        }

    }

    private class KeyIterator extends TreeMapIterator {
        @Override
        public K next() {
            Node<K, V> node = (Node<K, V>) super.next();

            return node.key;
        }
    }

    private class ValueIterator extends TreeMapIterator {
        @Override
        public V next() {
            Node<K, V> node = (Node<K, V>) super.next();

            return node.value;
        }
    }

}
