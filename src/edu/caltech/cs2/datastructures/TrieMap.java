package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Iterator;

public class TrieMap<A, K extends Iterable<A>, V> implements ITrieMap<A, K, V> {
    private TrieNode<A, V> root;
    private Function<IDeque<A>, K> collector;
    private int size;

    public TrieMap(Function<IDeque<A>, K> collector) {
        this.root = null;
        this.collector = collector;
        this.size = 0;
    }


    @Override
    public boolean isPrefix(K key) {
        TrieNode<A, V> current = root;
        if(root ==null){
            return false;
        }
        for (A c : key) {
            if (current == null) {
                return false;
            }
            if (!current.pointers.containsKey(c)) {
                return false;
            }
            current = current.pointers.get(c);
            //TrieNode childOfCurrentNode= new TrieNode(null);
        }
        return true;

    }










    @Override
    //static
    public ICollection<V> getCompletions(K prefix){
        TrieNode<A,V> current= this.root;
        ArrayDeque<A> letter = new ArrayDeque<A>();
        ICollection<K> keys = new ArrayDeque<K>();
        ICollection<V> values = new ArrayDeque<V>();
        //get all the values from the helper function, then get the values from them
        //return keys

        for(A c: prefix){
            if(current==null){
                return values;
            }
            current = current.pointers.get(c);
            letter.add(c);
        }
        ICollection<K> output = keySet(current, letter, keys);

        //iterate through output, for each key in output. check if get if key is null, if not add to values array
        for(K key: output){
            if(get(key) != null){
                values.add(get(key));
            }
        }
        return values;
    }



    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    //value to which key is mapped
    //else null
    public V get(K key) {
        TrieNode <A,V> current = root;
        if(current==null){
            return null;
        }
        for (A c : key) {
            if(current.pointers.containsKey(c)) {
                current = current.pointers.get(c);
            }
            else{
                return null;
            }

            //TrieNode childOfCurrentNode= new TrieNode(null);
        }
        if (current.value == null) {
            return null;
        } else {
            return current.value;
        }
    }


    //if(this.isEmpty())
    //   return null;

    //if key is contained, (is prefix?), then:
    //walk down trie until all chars in the key are used
    //once key.length ran through, retrieve the value stored at
    // whatever node you are on
    //if(pointers.)


    @Override
    public V remove(K key) {
        return null;
    }



    @Override
    public V put(K key, V value){

        if(root == null){
            root = new TrieNode<>();
        }
        //check if root is null and if it is make it a new TrieNode
        TrieNode <A,V> current = root;
        for(A c: key){
            if(!current.pointers.containsKey(c)){
                current.pointers.put(c, new TrieNode<A,V>());
            }
            current = current.pointers.get(c);
            //TrieNode childOfCurrentNode= new TrieNode(null);
        }

        if(current.value == null){
            this.size++;
            current.value = value;
            return null;
        }
        else{
            V oldValue = current.value;
            current.value = value;
            return oldValue;

        }
    }




    @Override
    public boolean containsKey(K key) {
        TrieNode<A, V> current = root;

        if (current == null) {
            return false;
        }
        for (A c : key) {
            if (!current.pointers.containsKey(c)) {
                return false;
            } else {
                current = current.pointers.get(c);
            }

        }
        if(current.value != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {

        //TrieNode<A, V> node = root;
        ArrayDeque<V> array1 = (ArrayDeque<V>) values();
        return array1.contains(value);


    }



    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        TrieNode <A,V> current = root;
        ArrayDeque<A> letter = new ArrayDeque<A>();
        ICollection<K> keys = new ArrayDeque<K>();
        return keySet(current, letter, keys);
    }
    // for(A key: current.pointers.keySet()){
    private ICollection<K> keySet(TrieNode <A,V> current, ArrayDeque<A> letter, ICollection<K> keys) {
        // TrieNode<A,V> current = this.root;
        if(current== null){
            return keys;
        }

        if(current.value != null){
            keys.add((this.collector.apply(letter)));

        }

        for(A key: current.pointers.keySet()){
            letter.add(key);
            keySet(current.pointers.get(key), letter, keys);
            letter.removeBack();
        }
        return keys;

    }


    @Override
    public ICollection<V> values() {
        //TrieNode current = root;
        ICollection<V> values = new ArrayDeque<V>();
        for(K key: keys()){
            V value = get(key);
            if(value != null){
                values.add(value);
            }
        }
        return values;
    }



    @Override
    public Iterator<K> iterator() {
        //store current node
        //store current word
        //pos of children links
//keys.iterator
        //(keys).iterator();

        return keys().iterator();



    }

    @Override
    public String toString() {
        return this.root.toString();
    }

    private static class TrieNode<A, V> {
        public final Map<A, TrieNode<A, V>> pointers;
        public V value;

        public TrieNode() {
            this(null);
        }

        public TrieNode(V value) {
            this.pointers = new HashMap<>();
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (this.value != null) {
                b.append("[" + this.value + "]-> {\n");
                this.toString(b, 1);
                b.append("}");
            }
            else {
                this.toString(b, 0);
            }
            return b.toString();
        }

        private String spaces(int i) {
            StringBuilder sp = new StringBuilder();
            for (int x = 0; x < i; x++) {
                sp.append(" ");
            }
            return sp.toString();
        }

        protected boolean toString(StringBuilder s, int indent) {
            boolean isSmall = this.pointers.entrySet().size() == 0;

            for (Map.Entry<A, TrieNode<A, V>> entry : this.pointers.entrySet()) {
                A idx = entry.getKey();
                TrieNode<A, V> node = entry.getValue();

                if (node == null) {
                    continue;
                }

                V value = node.value;
                s.append(spaces(indent) + idx + (value != null ? "[" + value + "]" : ""));
                s.append("-> {\n");
                boolean bc = node.toString(s, indent + 2);
                if (!bc) {
                    s.append(spaces(indent) + "},\n");
                }
                else if (s.charAt(s.length() - 5) == '-') {
                    s.delete(s.length() - 5, s.length());
                    s.append(",\n");
                }
            }
            if (!isSmall) {
                s.deleteCharAt(s.length() - 2);
            }
            return isSmall;
        }
    }

    public static class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
        private Node head;
        private Node tail;
        private int size;
        private static class Node <E> {
            private final E data;
            private Node next;
            private Node previous;

            public Node(E val) {
                data = val;
            }

            public E getData() {
                return data;
            }

            public Node getNext() {
                return next;
            }
            public void setNext(Node next) {
                this.next = next;
            }

            public Node getPrevious() {
                return previous;
            }

            public void setPrevious(Node previous) {
                this.previous = previous;
            }
        }

        @Override
        public void addFront(E e) {
            Node newNode = new Node(e);
            //new version
            newNode.setNext(head);
            newNode.setPrevious(null);
            if(head == null){
                head = newNode;
                tail = newNode;
            }
            else {
                newNode.setNext(head);
                head.setPrevious(newNode);
                head = newNode;
            }
            size = size + 1;
        }

        @Override
        public void addBack(E e) {
            Node newNode = new Node(e);
            //new
            newNode.setNext(null);
            newNode.setPrevious(tail);
            if(tail == null) {
                head = newNode;
                tail = newNode;
            }
            else {
                tail.setNext(newNode);
                newNode.setPrevious(tail);
                tail = newNode;
            }
            size = size + 1;
        }

        @Override
        public E removeFront() {
            if(head == null){
                return null;
            }

            Node oldHead = head;  // current head
            Node newHead = head.getNext(); // successor node
            if(newHead == null){
                tail = null;
            }
            else{
                newHead.setPrevious(null);// no predecessor
            }


            head = newHead;
            size = size - 1;
            return (E) oldHead.getData();
        }

        @Override
        public E removeBack() {
            if(tail == null){
                return null;
            }
            Node oldTail = tail; // current tail
            Node newTail = tail.getPrevious();// successor node


            // oldTail.setPrevious(null);
            if(newTail==null){
                //newTail.setNext(null);
                head = null;

            }
            else{
                newTail.setNext(null);
            }
            tail = newTail;
            size = size - 1;
            return (E) oldTail.getData();
        }

        @Override
        public boolean enqueue(E e) {
            if(e == null){
                return false;
            }
            addBack(e);
            return true;
        }

        @Override
        public E dequeue() {
//        if(size==0){
//            return null;
//        }
            return removeFront();

        }

        @Override
        public boolean push(E e) {
            if(e == null){
                return false;
            }
            addFront(e);
            return true;
        }

        @Override
        public E pop() {
//        if(size==0){
//            return null;
//        }
            return removeFront();
        }

        @Override
        public E peekFront() {
            if(head != null){
                return (E) head.getData();
            }
            return null;
        }

        @Override
        public E peekBack() {
            if(tail != null){
                return (E) tail.getData();
            }
            return null;
        }

        @Override
        public E peek() {
            return peekFront();
        }

        @Override
        public Iterator<E> iterator() {
            Iterator<E> iter = new Iterator<E>() {
                Node currentNode = head;
                @Override
                public E next() {
                    if(currentNode == null){
                        return null;
                    }
                    Node toReturn = currentNode;// retriving current value
                    currentNode = toReturn.getNext();// setting the current node to the next
                    return (E) toReturn.getData();
                }

                @Override
                public boolean hasNext() {
                    if(currentNode == null){
                        return false;
                    }

                    else return true;


                }
            };
            return iter;

        }
        @Override
        public String toString() {
            if (head == null) {
                return "[]";
            }

            String result = "[";
            //Iterator iterator = this.iterator();
            Node follow = head;
            while(follow != null){
                result += follow.getData() + ", ";
                follow = follow.getNext();

            }

            result = result.substring(0, result.length() - 2);
            return result + "]";

        }


        @Override
        public int size() {
            return size;
        }


    }


    public static class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
        private int size = 0;
        private int back = 0;

        private static final int STR = 10;
        private static final int GROW_FACTOR = 2;
        private E[] elements;


        public ArrayDeque() {
            this(STR);

        }
        public ArrayDeque(int intialCapacity) {

            this.elements = (E[]) new Object[intialCapacity];
            this.size = 0;

        }


        @Override
        public void addFront(E e) {
            if (size + 1> this.elements.length) {
                E[] newElements = (E[]) new Object[this.elements.length*GROW_FACTOR];

                for(int i = 0; i < size; i++) {
                    newElements[i + 1] = this.elements[i];
                }

                this.elements = newElements;
                this.elements[0] = e;

            }
            else {
                for(int i = size-1; i >= 0; i--) {
                    this.elements[i+1] = this.elements[i];
                }
                this.elements[0] = e;

            }

            size = size + 1;
        }



        @Override
        public void addBack(E e) {
            if (size + 1 > this.elements.length) {
                E[] newElements = (E[]) new Object[this.elements.length * GROW_FACTOR];
                for (int i = 0; i < this.elements.length; i++) {
                    newElements[i] = this.elements[i];
                }
                this.elements = newElements;
                this.elements[size] = e;
            }
            else {

                this.elements[size] = e;

            }
            size = size + 1;

        }

        @Override
        public E removeFront() {
            if (size == 0) {
                return null;
            } else {
                int i = 0;
                E first = elements[i];
                for (i = 0; i < elements.length - 1; i++) {
                    elements[i] = elements[i+1];

                }
                size = size - 1;
                return first;
            }

        }

        @Override
        public E removeBack() {
            if (size == 0) {
                return null;
            } else {
                E last = elements[size - 1];
                //elements[elements.length- 1] = null;
                size = size - 1;
                return last;
            }
        }

        @Override
        public boolean enqueue(E e) {
            addFront(e);
            return true;
        }

        @Override
        public E dequeue() {
            return removeBack();

        }

        @Override
        public boolean push(E e) {
            addBack(e);
            return true;
        }

        @Override
        public E pop() {
            return removeBack();

        }

        @Override
        public E peekFront() {
            if(size==0) {
                return null;
            }
            return elements[0];
        }

        @Override
        public E peekBack() {
            if(size==0) {
                return null;
            }
            return elements[size-1];
        }

        @Override
        public E peek() {
            return peekBack();
        }

        @Override
        public Iterator<E> iterator() {
            Iterator<E> iter = new Iterator<E>() {
                private int index = 0;

                @Override
                public E next() {
                    index += 1;
                    return elements[index-1];
                }
                @Override
                public boolean hasNext() {
                    if (index < size) {
                        return true;
                    }
                    return false;
                }
            };
            return iter;
        }


        @Override
        public int size() {
            return size;
        }

        @Override
        public String toString() {
            if (this.size == 0) {
                return "[]";
            }

            String result = "[";
            for (int i = 0; i < this.size; i++) {
                result += this.elements[i] + ", ";
            }

            result = result.substring(0, result.length() - 2);
            return result + "]";
        }


    }



}
