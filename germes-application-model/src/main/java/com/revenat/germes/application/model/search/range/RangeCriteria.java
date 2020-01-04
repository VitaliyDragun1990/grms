package com.revenat.germes.application.model.search.range;

/**
 * Pagination parameters for data retrieval operations
 *
 * @author Vitaliy Dragun
 */
public final class RangeCriteria {

    /**
     * Page index(0-based)
     */
    private final int page;

    /**
     * Number of elements per page
     */
    private final int rowCount;

    public RangeCriteria(int page, int rowCount) {
        this.page = page;
        this.rowCount = rowCount;
    }

    public int getPage() {
        return page;
    }

    public int getRowCount() {
        return rowCount;
    }
}
