package com.viwcy.userserver.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * TODO //
 *
 * <p> Title: DataSourceConfiguration </p >
 * <p> Description: DataSourceConfiguration </p >
 * <p> History: 2021/4/9 17:39 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Primary
    @Bean("dataSource")
    public DataSourceProxy dataSource(DataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    //TODO  seata+MP，不能二次构建SqlSessionFactory，不然分布式事务失效，因为MP底层也是构造了一个SqlSessionFactory数据源
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSourceProxy);
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath*:mapper/**/*Mapper.xml"));
//        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
//        return sqlSessionFactoryBean.getObject();
//    }
}
