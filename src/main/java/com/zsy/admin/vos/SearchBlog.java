package com.zsy.admin.vos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/6 15:43
 * @desc
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class SearchBlog implements Serializable {

    private Integer id;

    private String title;

    private String description;
}
