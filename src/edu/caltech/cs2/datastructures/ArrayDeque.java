package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
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


