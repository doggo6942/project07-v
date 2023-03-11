
package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

    public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
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


