package com.example.demo.dataSource;


import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
//@MapperScan("com.example.demo.dao")
public class HikariCustomConfig {

    public static final Logger logger = LoggerFactory.getLogger(HikariCustomConfig.class);

    @Value("${mybatis.mapper-locations}")
    private String configLocation;
    @Value("${mybatis.type-aliases-package}")
    private String typeAliasespackage;

    @Bean("master")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public HikariDataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("second")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.second")
    public HikariDataSource slaveDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(@Qualifier("master") DataSource masterDataSource, @Qualifier("second") DataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(AllDatasource.MASTER.name(), masterDataSource);
        targetDataSources.put(AllDatasource.SECOND.name(), slaveDataSource);
        DynamicDataSourceContextHolder.setDateSoureType(AllDatasource.MASTER.name());
        logger.info("注册默认数据源成功:"+AllDatasource.MASTER.name());
        logger.info("获取默认数据源:"+DynamicDataSourceContextHolder.getDateSoureType());
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }

    /**
     * @description: 配置mybatis的mapper和dao的位置
     */
    @Bean("sqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(configLocation));
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasespackage);
        return sqlSessionFactoryBean;
    }

    /**
     * 事务管理
     */
//    @Bean
//    public PlatformTransactionManager transactionManager(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) {
//        return new DataSourceTransactionManager(dynamicDataSource);
//    }


}
