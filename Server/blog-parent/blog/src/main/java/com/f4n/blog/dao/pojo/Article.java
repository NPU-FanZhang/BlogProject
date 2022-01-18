package com.f4n.blog.dao.pojo;

import lombok.Data;

@Data
public class Article {
    public static final int Article_TOP = 1;
    public static final int Article_Common = 0;
    private Long id;
    private String title;
    private String summary;
    // 不能用 int 等基本类型,要用Integer等封装类型
    // 会出现在 更新操作时,会给 int 等基本类型给默认值,会产生错误
    private Integer commentCounts;
//    private int commentCounts;
    private Integer viewCounts;
    //作者id
    private Long authorId;
    //内容id
    private Long bodyId;
    //类别id
    private Long categoryId;
    //置顶
    private Integer weight = Article_Common;
    // 创建时间
    private Long createDate;

}
