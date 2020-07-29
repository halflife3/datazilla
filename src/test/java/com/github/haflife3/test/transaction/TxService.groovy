package com.github.haflife3.test.transaction


import com.github.haflife3.datazilla.QueryEntry
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Slf4j
@Service
class TxService {

    @Transactional
    void txTest(QueryEntry queryEntry, Closure closure){
        closure(queryEntry)
    }
}
