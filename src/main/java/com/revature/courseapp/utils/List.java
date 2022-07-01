package com.revature.courseapp.utils;

/**
 * A dynamically allocated array.
 */
public interface List <T>  {
    public int size();
    public boolean isEmpty();
    public void set (int index, T value);
    public T get (int index);
    public int getIndex (T value);
    public void add (T value);
    public void addAll (List<T> arr);
    public T remove ();
    public T remove (T value);
    public T removeAtIndex(int index);
}
