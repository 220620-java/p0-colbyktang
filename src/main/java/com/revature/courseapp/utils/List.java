package com.revature.courseapp.utils;

public interface List <T> {
    public int size();
    public boolean isEmpty();
    public void set (int index, T value);
    public T get (int index);
    public void add (T value);
    public void addAll (List<T> arr);
    public T remove ();
    public T remove (T value);
    public T removeAtIndex(int index);
}
