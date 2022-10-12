package eu.luminis.workshop.smallsteps;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(
        packages = "eu.luminis.workshop.smallsteps",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ArchitectureTest {


    @ArchTest
    static final ArchRule onion_architecture_is_respected = onionArchitecture()
            .domainModels("..logic..domainModel..")
            .domainServices("..logic..domainServices..")
            .withOptionalLayers(true)
            .adapter("api", "..api..")
            .adapter("persistence", "..persistence..")
            .ignoreDependency(
                    resideInAPackage("eu.luminis.workshop.smallsteps"),
                    alwaysTrue()
            );
}
