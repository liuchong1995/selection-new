package com.jidong.productselection.dao;

import com.jidong.productselection.entity.JdCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author: LiuChong
 * @date: 2018/12/5 23:17
 * @desciption:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JdCategoryMapperTest {

    @Autowired
    private JdCategoryMapper categoryMapper;

    @Test
    public void test1(){
        List<JdCategory> newMenuTree = categoryMapper.getNewMenuTree(1, 0);
        System.out.println();
    }

}