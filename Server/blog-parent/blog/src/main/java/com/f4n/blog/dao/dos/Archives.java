package com.f4n.blog.dao.dos;

import lombok.Data;

@Data
public class Archives {
    private Integer year;
    private Integer month;
    private Long count;
}
