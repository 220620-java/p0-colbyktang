package com.revature.courseapp.utils;

import java.lang.Exception;
import java.lang.UnsupportedOperationException;

public class LinkedList <T> implements List<T> {
    Node<T> head;
    Node<T> tail;
    int size;

    public LinkedList () {}

    public LinkedList (T[] arr) {
        for (T item : arr) {
            add(item);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size <= 0;
    }

    // Retrieve a value at the index
    public T get (int index) {
        if (isEmpty()) {
            return null;
        }
        if (index == size() - 1) {
            return tail.value;
        }
        Node<T> returnNode = head;
        for (int i = 0; i < index; i++) {
            if (returnNode.next == null) {
                // throw new RunTimeException (String.format("List index out of range at [%i]", i+1));
            }
            returnNode = returnNode.next;
        }
        return returnNode.value;
    }

    // Change a value at an index
    public void set (int index, T value) {
        Node<T> returnNode = head;
        for (int i = 0; i < index; i++) {
            if (returnNode.next == null) {
                // throw new RunTimeException (String.format("List index out of range at [%i]", i+1));
            }
            returnNode = returnNode.next;
        }
        returnNode.value = value;
    }

    // Add new node at the end
    public void add (T value) {
        if (head == null) {
            head = new Node<T>(value);
            tail = head;
            size += 1;
            return;
        }
        else if (head.next == null) {
            tail.next = new Node<T>(value);
            tail.next.prev = tail;
            tail = tail.next;
            head.next = tail;
            size += 1;
        }
        else {
            tail.next = new Node<T>(value);
            tail.next.prev = tail;
            tail = tail.next;
            size += 1;
        }
    }

    // Adds the other list at the end
    public void addAll (List<T> arr) {
        try {
            LinkedList<T> linkedList = (LinkedList<T>) arr;
            linkedList.head.prev = tail;
            tail.next = linkedList.head;
            tail = linkedList.tail;
            size += linkedList.size();
        }
        catch (Exception e) {
            throw new UnsupportedOperationException("Not implemented", e);
        }
        /*
        if (arr instanceof LinkedList) {
            
        }
        */
    }

    // Remove the tail
    public T remove () {
        if (tail == null) {
            return null;
        }
        T value = head.value;
        if (tail.prev == null) {
            head = null;
            tail = null;
            size = 0;
            return value;
        }
        value = tail.value;
        tail = tail.prev;
        tail.next = null;
        size -= 1;
        return value;
    }

    // Find and remove a specific value
    public T remove (T value) {
        Node<T> returnNode = head;
        for (int i = 0; i < size(); i++) {
            if (returnNode.next == null) {
                return null;
                // throw new RunTimeException (String.format("List index out of range at [%i]", i+1));
            }
            if (returnNode.value == value) {
                
            }
            returnNode = returnNode.next;
        }
        if (returnNode.next != null) {
            returnNode.prev = returnNode.next;
        }
        else {
            returnNode.prev = null;
        }
        size -= 1;
        return returnNode.value;
    }

    public T removeAtIndex (int index) {
        if (index < 0) {
            return null;
        }

        if (isEmpty()) {
            return null;
        }

        if (index == size() -1) {
            return remove();
        }

        Node<T> returnNode = head;
        if (index == 0) {
            if (returnNode.next == null) {
                head = null;
                tail = null;
                size -=1;
                return returnNode.value;
            }
            else {
                head = returnNode.next;
                head.prev = null;
                size -=1;
                return returnNode.value;
            }
        }
        for (int i = 0; i < index; i++) {
            if (returnNode.next == null) {
                // throw new RunTimeException (String.format("List index out of range at [%i]", i+1));
                return null;
            }
            returnNode = returnNode.next;
        }
        if (returnNode.next != null) {
            returnNode.next.prev = returnNode.prev;
            returnNode.prev.next = returnNode.next;
        }
        else {
            returnNode.prev.next = null;
        }
        size -= 1;
        return returnNode.value;
    }

    @Override
    public String toString () {
        String retString = "{";
        Node<T> current = head;

        while (current != null) {
            retString += (current.value.toString());
            if (current.next != null) {
                retString += ", ";
            }
            current = current.next;
        }
        retString += "}";
        return retString;
    }
}
