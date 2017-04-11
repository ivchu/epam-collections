package ru.epam.training;

import java.util.*;

public class CustomTreeMap<K extends Comparable<K>, V> implements Map<K, V> {
    private Node<K, V> root;
    V oldValue;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);

        if (root == null) return false;
        root.key.compareTo((K) key);
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
        Node<K, V> node = root;
        if (node == null) {
            return null;
        } else {
            if (node.key.compareTo((K) key) > 0) {
                node = find(node.left, (K) key);
            } else if (node.key.compareTo((K) key) < 0) {
                node = find(node.right, (K) key);
            }
        }
        return node.value;
    }

    @Override
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        Node<K, V> node = root;
        if (node == null) {
            key.compareTo(key);
            root = new Node<>(key, value);
            return null;
        }
        put(root, key, value);
        return this.oldValue;
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) {
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

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private class Node<K extends Comparable<K>, V> {
        private final K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }


    }

}
