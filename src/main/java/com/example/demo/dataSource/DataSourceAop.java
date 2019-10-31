package com.example.demo.dataSource;

import com.example.demo.annotation.ChooseDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Configuration
public class DataSourceAop {

    @Value("${spring.datasource.hikari.master.default-package}")
    private String masterPackage;
    @Value("${spring.datasource.hikari.second.default-package}")
    private String secondPackage;

    public static final Logger logger = LoggerFactory.getLogger(DataSourceAop.class);
//execution(* com.example.demo.dao.UserMapper.selectAllUser())  ||
    @Pointcut("@annotation(com.example.demo.annotation.ChooseDataSource)")
    public void switchDataSource() {
    }

    @Before("switchDataSource()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("切换数据源");
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ChooseDataSource chooseDataSource = method.getAnnotation(ChooseDataSource.class);//获取方法上的注解
        if (chooseDataSource == null) {
            chooseDataSource = joinPoint.getTarget().getClass().getAnnotation(ChooseDataSource.class);//获取类上面的注解
            if (chooseDataSource == null) {
                String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
                if (declaringTypeName.startsWith(masterPackage)) {
                    DynamicDataSourceContextHolder.setDateSoureType(AllDatasource.MASTER.name());
                    logger.info("使用包默认数据源，包={}，数据源={}", masterPackage, AllDatasource.MASTER.name());
                } else if (declaringTypeName.startsWith(secondPackage)) {
                    DynamicDataSourceContextHolder.setDateSoureType(AllDatasource.SECOND.name());
                    logger.info("使用包默认数据源，包={}，数据源={}", secondPackage, AllDatasource.SECOND.name());
                }
                return;
            } else {
                logger.info("类注解生效，切换数据源={}", chooseDataSource.dataSource().name());
            }
        } else {
            logger.info("方法注解生效，切换数据源={}", chooseDataSource.dataSource().name());
        }
        //获取注解上的数据源的值的信息
        String dataSourceName = chooseDataSource.dataSource().name();
        if (dataSourceName != null) {
            //给当前的执行SQL的操作设置特殊的数据源的信息
            DynamicDataSourceContextHolder.setDateSoureType(dataSourceName);
        }
        String nowDatasource = "".equals(dataSourceName) ? "默认数据源master" : dataSourceName;
        logger.info("AOP注解切换数据源，className" + joinPoint.getTarget().getClass().getName() + "methodName" + method.getName() + ";dataSourceName:" + nowDatasource);
    }

    @After("switchDataSource()")
    public void after(JoinPoint point) {
        //清理掉当前设置的数据源，让默认的数据源不受影响
        DynamicDataSourceContextHolder.clearDateSoureType();
    }
}
