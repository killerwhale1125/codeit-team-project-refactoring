package com.gathering.mock.test;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
public class TestPageable implements Pageable {

    private long page;
    private long size;

    public static TestPageable of(int page, int size) {
        TestPageable testPageable = new TestPageable();
        testPageable.page = page;
        testPageable.size = size;
        return testPageable;
    }

    @Override
    public int getPageNumber() {
        return (int) this.page;
    }

    @Override
    public int getPageSize() {
        return (int) this.size;
    }

    @Override
    public long getOffset() {
        return page * size;
    }

    @Override
    public Sort getSort() {
        return Sort.unsorted(); // 기본 정렬로 설정
    }

    @Override
    public Pageable next() {
        return new TestPageable(page + 1, size);
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new TestPageable(page - 1, size) : first();
    }

    @Override
    public Pageable first() {
        return new TestPageable(0, size);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new TestPageable(pageNumber, size);
    }

    @Override
    public boolean hasPrevious() {
        return page > 0;
    }

}
