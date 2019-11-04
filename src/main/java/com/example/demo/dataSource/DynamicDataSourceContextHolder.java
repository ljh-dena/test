package com.example.demo.dataSource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceContextHolder {
    public static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    private static final ThreadLocal<String> DATASOURCE_CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源
     */
    public static void setDateSoureType(String dateSoureType) {
        logger.info("设置数据源={}", dateSoureType);
        DATASOURCE_CONTEXT_HOLDER.set(dateSoureType);
    }

    /**
     * 获得数据源
     */
    public static String getDateSoureType() {
        return DATASOURCE_CONTEXT_HOLDER.get();
    }

    /**
     * 清空数据源
     */
    public static void clearDateSoureType() {
        logger.info("清空数据源：{}", DATASOURCE_CONTEXT_HOLDER.get());
        DATASOURCE_CONTEXT_HOLDER.remove();
    }
}
