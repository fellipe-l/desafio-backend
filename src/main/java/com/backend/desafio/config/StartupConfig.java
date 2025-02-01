package com.backend.desafio.config;

import com.backend.desafio.user.UserType;
import com.backend.desafio.user.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupConfig {
    private final UserTypeRepository userTypeRepository;

    @Autowired
    public StartupConfig(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userTypeRepository.count() <= 0) {
            userTypeRepository.save(new UserType(1L, "CUSTOMER"));
            userTypeRepository.save(new UserType(2L, "SHOPKEEPER"));
        }
    }
}
