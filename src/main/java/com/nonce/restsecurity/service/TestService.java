package com.nonce.restsecurity.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Andon
 * 2022/7/13
 */
@Service
public class TestService {

    public void test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    }
}
