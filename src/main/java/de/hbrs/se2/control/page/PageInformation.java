package de.hbrs.se2.control.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;


/**
 * this class is intended to be used as a track if it is needed to go through the db table by pages.
 * for example only 10 entities per page should to be sent via a request. This calls helps to keep trak of the current page number and page size, after each retrieved page
 * <p>
 * sort: holds onto the order of paging
 */
@AllArgsConstructor
@Getter
@Setter
public class PageInformation {
    private int pageNumber;
    private int pageSize;
    private Sort sort;

    public PageInformation(int pageNumber, int pageSize, Order... orders) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        sort = Sort.by(orders);

    }


}
