package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;

import java.util.Iterator;

public class MinFourHeap<E> implements IPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private int size;
    private PQElement<E>[] data;
    private IDictionary<E, Integer> keyToIndexMap;

    /**
     * Creates a new empty heap with DEFAULT_CAPACITY.
     */
    public MinFourHeap() {
        this.size = 0;
        this.data = new PQElement[DEFAULT_CAPACITY];
        this.keyToIndexMap = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }
    private void resize() {
        PQElement[] copy = new PQElement[this.size * 2];
        for (int i = 0; i < size ; i++) {
            copy[i] = this.data[i];
        }
        this.data = copy;
    }

    @Override
    public void increaseKey(PQElement<E> key) {
        if(!keyToIndexMap.containsKey(key.data)){
            throw new IllegalArgumentException();
        }
        int index = this.keyToIndexMap.get(key.data);
        if(key.priority < this.data[index].priority){
            throw new IllegalArgumentException();
        }
        this.data[index] = key;
        this.heapifyDown(index);

        // elements = Arrays.copyOf(elements, newCapacity);
        //get a list of the children?
    }


    //        }
//    }
    private void swap(int i, int j) {
        // swap the elements at indices i and j
        PQElement<E> temp = data[i];
        data[i] = data[j];
        data[j] = temp;

        // update the key-to-index map
        keyToIndexMap.put(data[i].data, i);
        keyToIndexMap.put(data[j].data, j);
    }



    //if element at inx2 in data is equal to key.data then return key
    //else, check parent--> then check child till you are at the end (end==lowest prioirty?)
    //get prioirty (prioity is the value, key.data is the key
    //if greater than parent--> return key
    //get prioirty
    //check if contains key
    //  if(keyToIndexMap.containsKey(key.data))
//chaining for ties?
    //priority of every node is greater than the prioirty of its parent
    //save last
    //delete first
    //set last to first
    //size is lasst accesed.


    @Override
    public void decreaseKey(PQElement<E> key) {
        if (!keyToIndexMap.containsKey(key.data)) {
            throw new IllegalArgumentException();
        }
        int index = keyToIndexMap.get(key.data);
        if (data[index].priority <= key.priority) {
            throw new IllegalArgumentException();
        }
        data[index]= key;
        this.heapifyUp(index);
    }
    private void heapifyDown(int index){
        int child = 4 * index + 1;
        while (child < this.size) {
            int minChild = child;
            for (int i = child; i <= child + 3; i++) {
                if (i < this.size && data[i].priority < data[minChild].priority) {
                    //data[child + i]
                    minChild = i;
                }
            }
            if (this.data[minChild].priority < this.data[index].priority) {
                this.swap(index, minChild);
                index = minChild;
                child = 4 * index + 1;
            } else {
                break;
            }
        }
    }
    private void heapifyUp(int index) {
        if(this.data[index] != null) {
            int parent = (index - 1) / 4;
            while (index >= 0 && data[index].priority < data[parent].priority) {
                this.swap(index, parent);
                index = parent;
                parent = (index - 1) / 4;
            }
        }
    }

    @Override
    public boolean enqueue(PQElement<E> epqElement) {
        if(keyToIndexMap.containsKey(epqElement.data)){
            throw new IllegalArgumentException();
        }
        if (this.size >= data.length) {
            this.resize();
        }
        //size = size +1;
        data[size] = epqElement;
        keyToIndexMap.put(epqElement.data, this.size);
        this.size++;
        heapifyUp(this.size-1);
        return true;
    }

    @Override
    public PQElement<E> dequeue() {
        if (size <= 0) {
            return null;
        }
        PQElement<E> removed = data[0];
        keyToIndexMap.remove(removed.data);
        //PQElement<E> last = data[size];
        data[0] = data[size - 1];
        size--;
        if (size > 0) {
            keyToIndexMap.put(data[0].data, 0);
            heapifyDown(0);
        }

        return removed;
    }



//       data[size] = data[size]
//       keyToIndexMap()
//
//        //size = size +1;
//        data[size] = epqElement;
//        keyToIndexMap.put(epqElement.data, this.size);
//        this.size++;
//        this.heapifyUp(this.size);
    //new key is larger, throw an error
    //heapify down
    //dequeue last
    //lowest delete min
    //put back in front
    //


    @Override
    public PQElement<E> peek() {

        return data[0];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<PQElement<E>> iterator() {
        return null;
    }
}
