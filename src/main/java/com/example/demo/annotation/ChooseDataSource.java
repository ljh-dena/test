package com.example.demo.annotation;

import com.example.demo.dataSource.AllDatasource;

import java.lang.annotation.*;

/**
 * @Description:设置数据源 优先级：1.方法注解 2.类注解 3.根据包路径设置的数据源
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChooseDataSource {

    AllDatasource dataSource() default AllDatasource.MASTER;
}