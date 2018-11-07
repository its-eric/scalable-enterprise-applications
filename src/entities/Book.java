package entities;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity(name = "books")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;

/*    title: string, at most 255 characters long, must be present
    author: string, at most 255 characters long, must be present
    publishedAt: date, optional
    pages: integer, optional
    price: java.math.BigDecimal with precision "10,2", optional
    StockStatus: enum values stored as strings
            IN_STOCK
            OUT_OF_STOCK
            UNKNOWN*/

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String author;

    private Date publishedAt;

    private int pages;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
}

enum StockStatus {
    IN_STOCK,
    OUT_OF_STOCK,
    UNKNOWN
}