package com.jidong.productselection.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by sang on 2017/12/28.
 */
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication auth, Object o, Collection<ConfigAttribute> cas){

    }
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}