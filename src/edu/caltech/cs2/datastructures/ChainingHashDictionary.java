package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;
import java.util.function.Supplier;


    public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {

        private static final int[] PRIMES = new int[]{2, 5, 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853, 25717, 51437, 102877,
                205759, 411527, 823117, 1646237, 3292489, 6584983, 13169977, 26339969, 52679969, 105359939, 210719881, 421439783, 842879579, 1685759167};
        private Supplier<IDictionary<K, V>> chain;
        private IDictionary<K,V>[] table;

        private int placeInPrimes = 0;

        private int elements;


        public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {


            this.elements = 0;
            this.chain = chain;
            this.table = new IDictionary[2];
            for(int i = 0; i< table.length; i++){
                table[i] = chain.get();
            }

        }


        /**
         * @param key
         * @return value corresponding to key
         */
        @Override
        public V get(K key) {
            int Hashindex = Math.abs(key.hashCode()) % table.length;
            return table[Hashindex].get(key);

        }






        @Override
        public V remove(K key) {
            int HashIndex = Math.abs(key.hashCode()) % table.length;
            // if(this.table[Hashindex] != null) {
            V value = this.table[HashIndex].remove(key);
            if (value != null) {
                this.elements--;
                return value;
            }
            return null;
        }

        @Override
        public V put(K key, V value) {
            int Hashindex = Math.abs(key.hashCode()) % table.length;
            V result = table[Hashindex].put(key, value);
            if (result == null) {
                this.elements++;
                //elements size
                if ((double)this.elements / (double) table.length >= 1.00) {
                    this.resize();
                }
            }
            return result;
        }
        private void resize() {

            IDictionary<K, V>[] newTable = new IDictionary[PRIMES[this.placeInPrimes+1]];

            for (int j = 0; j < newTable.length; j++) {
                newTable[j] = chain.get();
            }
            for(K Key: this.keys()){
                int Hashindex = Math.abs(Key.hashCode()) % newTable.length;
                newTable[Hashindex].put(Key, this.get(Key));
                //table[Key.hashCode()]

            }
            this.placeInPrimes ++;

            this.table = newTable;
            // this.capacity = newTable.length;
        }


        @Override
        public boolean containsKey(K key) {
            int Hashindex = Math.abs(key.hashCode()) % table.length;
            //if(t )
            return table[Hashindex].containsKey(key);

//        if (chain == null) {
//            return false;
//        }

        }

        /**
         * @param value
         * @return true if the HashDictionary contains a key-value pair with
         * this value, and false otherwise
         */
        @Override
        public boolean containsValue(V value) {
            for (IDictionary<K,V> bucket : this.table) {
                //go thorugh all the buckets and return false at the end
                if (bucket != null) {
                    //IDictionary<K, V> dict = bucket.get();
                    if (bucket.containsValue(value)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * @return number of key-value pairs in the HashDictionary
         */
        @Override
        public int size() {
            return this.elements;
        }

        @Override
        public ICollection<K> keys() {
            ICollection<K> keys = new ArrayDeque<>();
            for (IDictionary<K,V> bucket : this.table) {
                if (bucket != null) {
                    //IDictionary<K, V> dict = bucket.get();
                    for (K key : bucket.keys()) {
                        keys.add(key);
                    }
                }
            }
            return keys;
        }

        @Override
        public ICollection<V> values() {
            ICollection<V> values = new ArrayDeque<>();
            for (IDictionary<K,V> bucket : this.table) {
                if (bucket != null) {
                    for (V value : bucket.values()) {
                        values.add(value);
                    }
                }
            }
            return values;
        }

        /**
         * @return An iterator for all entries in the HashDictionary
         */
        @Override
        public Iterator<K> iterator() {
            return this.keys().iterator();
        }

        @Override
        public String toString() {
//        if (this.root == null) {
//            return "{}";
//        }
            StringBuilder contents = new StringBuilder();
            for (IDictionary<K, V> bucket : this.table) {
                contents.append(bucket.toString());
            }
            return "{" + contents + "}";

        }

    }


