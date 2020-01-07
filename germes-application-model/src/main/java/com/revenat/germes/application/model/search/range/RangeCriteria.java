package com.revenat.germes.application.model.search.range;

import com.revenat.germes.application.infrastructure.helper.Checker;

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

    public RangeCriteria(final int page, final int rowCount) {
        final Checker checker = new Checker();
        checker.checkParameter(page >= 0, "Incorrect page index: %s", page);
        checker.checkParameter(rowCount >= 0, "Incorrect row count: %s", rowCount);

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
