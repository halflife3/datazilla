package com.github.haflife3.test.transaction


import com.github.haflife3.datazilla.misc.GeneralThreadLocal
import com.github.haflife3.test.CommonInfo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@EnableTransactionManagement
@Configuration
class DBConfig {

    @Bean
    DataSource dataSource(){
        return new TransactionAwareDataSourceProxy(CommonInfo.getDataSource(GeneralThreadLocal.get("db_type")))
    }

    @Bean
    TransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource)
    }
}
