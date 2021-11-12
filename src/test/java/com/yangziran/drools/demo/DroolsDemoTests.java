package com.yangziran.drools.demo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.junit.jupiter.api.Test;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class DroolsDemoTests {

    static final String GLOBAL_KEY = "globalResult";

    @Test
    void contextLoads() throws IOException {

        Resource resource = ResourceFactory.newClassPathResource("DroolsDemo.drl");
        assertNotNull(resource);
        log.info("{}", resource.getSourcePath());

        Map<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put("amout", 1000);
        log.info("{}", JSON.toJSONString(paramsMap));

        KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kBuilder.add(resource, ResourceType.DRL);
        assertNotNull(kBuilder);
        if (kBuilder.hasErrors()) {
            log.error("{}", kBuilder.getErrors());
        }

        InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addPackages(kBuilder.getKnowledgePackages());
        assertNotNull(knowledgeBase);

        KieSession kieSession = knowledgeBase.newKieSession();
        kieSession.setGlobal(GLOBAL_KEY, Maps.newHashMap());
        kieSession.insert(paramsMap);
        kieSession.fireAllRules();
        Map<String, Object> resultMap = (Map<String, Object>) kieSession.getGlobal(GLOBAL_KEY);
        kieSession.dispose();

        log.info("{}", JSON.toJSONString(paramsMap));
        log.info("{}", JSON.toJSONString(resultMap));
    }

}
