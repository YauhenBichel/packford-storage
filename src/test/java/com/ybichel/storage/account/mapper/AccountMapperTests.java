package com.ybichel.storage.account.mapper;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.authorization.vo.RegistrationRequestVO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)//needs for Autowired
@ContextConfiguration(classes = {AccountMapper.class})
class AccountMapperTests {

    @Autowired
    private AccountMapper accountMapper;

    @BeforeAll
    static void setup() {
        //log.info("@BeforeAll - executes once before all test methods in this class");
    }

    @BeforeEach
    void init() {
        //log.info("@BeforeEach - executes before each test method in this class");
    }

    @AfterEach
    void tearDown() {
        //log.info("@AfterEach - executed after each test method.");
    }

    @AfterAll
    static void done() {
        //log.info("@AfterAll - executed after all test methods.");
    }

    @Test
    void testToAccount() {
        UUID accountId = UUID.randomUUID();
        String hashedPassWithSalt = "testHashedPassWithSalt";

        RegistrationRequestVO registrationRequestVO = RegistrationRequestVO.builder()
                .email("test@gmail.com")
                .firstName("testFirstName")
                .password("testPassword")
                .build();

        Account actualAccount = accountMapper.toAccount(accountId, hashedPassWithSalt, registrationRequestVO);

        assertEquals(registrationRequestVO.getFirstName(), actualAccount.getFirstName());
        assertEquals(registrationRequestVO.getEmail(), actualAccount.getEmail());
    }

}
