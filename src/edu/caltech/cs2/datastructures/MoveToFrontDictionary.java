package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;


import java.util.Iterator;

    public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
        private Node head;
        private int size;

        private static class Node<K,V> {
            K key;
            V value;
            Node next;

            Node(K key, V value, Node next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }
        }

        public MoveToFrontDictionary() {
            this.head = null;
            this.size = 0;
        }

        //previous = current    previous --> going to where current is
        @Override
        public V remove(K key) {
            Node<K,V> prev = null;
            Node<K,V> curr = this.head;
            while (curr != null) {
                if (curr.key.equals(key)) {
                    if (prev == null) {
                        this.head = curr.next;
                    } else {
                        prev.next = curr.next;
                    }
                    this.size--;
                    return curr.value;
                }
                prev = curr;
                curr = curr.next;
            }
            return null;
        }

        @Override
        public V put(K key, V value) {
            //return null;
            if (this.containsKey(key)) {
                V oldValue = this.get(key);
                this.remove(key);
                this.head = new Node(key, value, this.head);
                this.size++;
                return oldValue;
            } else {
                this.head = new Node(key, value, this.head);
                this.size++;
                return null;
            }

        }

        @Override
        public boolean containsKey(K key) {
            return get(key) != null;
        }

        @Override
        public boolean containsValue(V value) {
            for (Node <K,V> curr = head; curr != null; curr = curr.next) {
                if (curr.value.equals(value)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public ICollection<K> keys() {
            ICollection<K> keys = new LinkedDeque<>();

            for (Node<K,V> curr = head; curr != null; curr = curr.next) {
                keys.add(curr.key);
            }
            return keys;
        }

        @Override
        public ICollection<V> values() {
            ICollection<V> values = new LinkedDeque<>();
            for (Node<K,V> curr = head; curr != null; curr = curr.next) {
                values.add(curr.value);
            }
            return values;
        }

        public V get(K key) {

            Node<K,V> prev = null;
            Node<K,V> curr = this.head;
            while (curr != null) {
                if (curr.key.equals(key)) {
                    if (prev != null) {
                        prev.next = curr.next;
                        curr.next = this.head;
                        this.head = curr;
                    }
                    return curr.value;
                }
                prev = curr;
                curr = curr.next;
            }
            return null;
        }

        @Override
        public Iterator<K> iterator() {
            return keys().iterator();
        }

        public String toString() {
            if (this.head == null) {
                return "{}";
            }

            StringBuilder contents = new StringBuilder();

            Node<K,V> current = this.head;
            while (current != null) {
                contents.append(current.key + ": " + current.value + ", ");


            }
            return "{" + contents.toString() + "}";
        }
    }


