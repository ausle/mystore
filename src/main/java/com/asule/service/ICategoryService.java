package com.asule.service;

import com.asule.common.ServerResponse;
import com.asule.entity.Category;
import java.util.List;

public interface ICategoryService{


    /**
     * 添加种类
     * @param categoryName
     * @param parentId
     * @return
     */
    ServerResponse addCategory(String categoryName,Integer parentId);


    /**
     * 更改种类名
     * @param categoryId
     * @param categoryName
     * @return
     */
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);


    /**
     * 获取子节点种类信息
     * @param categoryId
     * @return
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);


    /**
     * 获取节点和子节点的id
      * @param categoryId
     * @return
     */
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
