package com.imooc.sell.service.impl;

import com.imooc.sell.dataobject.ProductCategory;
import com.imooc.sell.repository.ProductCategoryRepository;
import com.imooc.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer categoryID) {
        return repository.findById(categoryID).orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }



    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
//        List<ProductCategory> productCategories = new ArrayList<>();
//        for (Integer integer:categoryTypeList){
//            List<ProductCategory> productCategoryList = repository.findByCategoryType(integer);
//            for (ProductCategory p: productCategoryList){
//                productCategories.add(p);
//            }
//        }
//        return productCategories;
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
