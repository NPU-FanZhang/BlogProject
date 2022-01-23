package com.f4n.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    //    直接改成String 解决问题
//    @JsonSerialize(using = ToStringSerializer.class)
//    private Long id;
    private String id;
    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;
}
