package com.asule.service.impl;

import com.asule.common.Constant;
import com.asule.common.ResponseCode;
import com.asule.common.ServerResponse;
import com.asule.dao.CategoryMapper;
import com.asule.entity.Category;
import com.asule.entity.User;
import com.asule.service.ICategoryService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Service
public class CategoryService implements ICategoryService{


    Logger logger=Logger.getLogger(CategoryService.class);



    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的
        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {

        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");

    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {

        HashSet<Category> categories = new HashSet<>();
        HashSet<Category> allSubCategorys = getAllSubCategorys(categories, categoryId);

        Iterator<Category> iterator = allSubCategorys.iterator();
        List<Integer> categoryIds=new ArrayList<>();
        while (iterator.hasNext()){
            Category category=iterator.next();
            categoryIds.add(category.getId());
        }

        return ServerResponse.createBySuccess(categoryIds);
    }

    public HashSet<Category> getAllSubCategorys(HashSet<Category> categories,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (categories!=null){
            categories.add(category);
        }

        //查询该节点的子节点(子节点再往下没有层级关系，那么会跳过for循环，直接返回)
        List<Category> subCategories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category temp:subCategories){
            getAllSubCategorys(categories,temp.getId());
        }
        return categories;
    }


}
