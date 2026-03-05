package com.buildwithrani.backend.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class LayerArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.buildwithrani.backend");
    }

    @Test
    void servicesShouldNotAccessControllers() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..service..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..controller..");

        rule.check(importedClasses);
    }

    @Test
    void controllersShouldNotAccessEntitiesDirectly() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..controller..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..entity..");

        rule.check(importedClasses);
    }

    @Test
    void entitiesShouldNotDependOnServices() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..entity..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..service..");

        rule.check(importedClasses);
    }
}