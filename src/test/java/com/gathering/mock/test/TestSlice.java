package com.gathering.mock.test;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class TestSlice<T> implements Slice<T> {

    private List<T> content = new ArrayList<>();
    private Pageable pageable;
    private boolean hasNext;

    public TestSlice(List<T> content, Pageable pageable, boolean hasNext) {
        this.content = content;
        this.pageable = pageable;
        this.hasNext = hasNext;
    }

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getNumberOfElements() {
        return 0;
    }

    @Override
    public List<T> getContent() {
        return this.content;
    }

    @Override
    public boolean hasContent() {
        return false;
    }

    @Override
    public Sort getSort() {
        return null;
    }

    @Override
    public boolean isFirst() {
        return false;
    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Pageable nextPageable() {
        return null;
    }

    @Override
    public Pageable previousPageable() {
        return null;
    }

    @Override
    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
