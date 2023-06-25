package com.zsy.admin.repository;

import com.zsy.admin.entity.Category;
import com.zsy.admin.vos.CategoryVo;
import com.zsy.admin.vos.TagVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT new com.zsy.admin.vos.CategoryVo(category.id,category.name) from Category category")
    List<CategoryVo> findAllCategory();
}