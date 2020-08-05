package com.github.haflife3.test.transaction


import com.github.haflife3.datazilla.QueryEntry
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Slf4j
@Service
class TxService {

    @Resource
    private QueryEntry queryEntry

    @Transactional
    void txTest(Closure closure){
        closure(queryEntry)
    }
}
