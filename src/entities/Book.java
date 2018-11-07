package entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@Entity(name = "BOOKS")
@Table(name = "BOOKS")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String author;

    private Date publishedAt;

    private int pages;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
}

enum StockStatus {
    IN_STOCK,
    OUT_OF_STOCK,
    UNKNOWN
}