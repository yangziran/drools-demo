package com.yangziran.drools.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.junit.jupiter.api.Test;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class DroolsFlowTests {

    @Test
    void contextLoads() {

        Resource bpmnResource = ResourceFactory.newClassPathResource("DroolsFlow.bpmn2");
        assertNotNull(bpmnResource);
        log.info("{}", bpmnResource.getSourcePath());
        Resource drlResource = ResourceFactory.newClassPathResource("DroolsFlow.drl");
        assertNotNull(drlResource);
        log.info("{}", drlResource.getSourcePath());

        KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kBuilder.add(bpmnResource, ResourceType.BPMN2);
        kBuilder.add(drlResource, ResourceType.DRL);
        assertNotNull(kBuilder);
        if (kBuilder.hasErrors()) {
            log.error("{}", kBuilder.getErrors());
        }

        InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addPackages(kBuilder.getKnowledgePackages());
        assertNotNull(knowledgeBase);

        KieSession kieSession = knowledgeBase.newKieSession();
        kieSession.startProcess("No1_Drools");
        kieSession.fireAllRules();
        kieSession.dispose();

    }

}
