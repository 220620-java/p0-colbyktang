package com.revature.courseapp.utils;

public class Node <T> {
    T value;
    Node<T> next;
    Node<T> prev;

    public Node (T value) {
        this.value = value;
    }
}
