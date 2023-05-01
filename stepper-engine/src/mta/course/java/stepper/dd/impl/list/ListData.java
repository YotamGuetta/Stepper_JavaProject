package mta.course.java.stepper.dd.impl.list;


import java.util.*;

public class ListData<T> implements List<T> {
    private final List<T> list;
    public ListData(List<T> list) {
        this.list = list;
    }
    public ListData() {
        this.list =new ArrayList<>();
    }
    @Override
    public T get(int index) {
        return list.get(index-1);
    }

    @Override
    public T set(int index, T element) {
        return list.set(index-1,element);
    }

    @Override
    public void add(int index, T element) {
        list.add(index-1,element);
    }
    @Override
    public boolean add(T item) {
        return list.add(item);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return list.addAll(index-1,c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }
    @Override
    public T remove(int index) {
        return list.remove(index-1);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(0)+1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(0)+1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index-1);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex-1, toIndex-1);
    }
    @Override
    public int size(){
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
            ret.append(i + 1).append(". ").append(list.get(i));
        return ret.toString();
    }


}
