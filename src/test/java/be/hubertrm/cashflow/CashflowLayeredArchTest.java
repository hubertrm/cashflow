package be.hubertrm.cashflow;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.properties.HasModifiers.Predicates.modifier;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "be.hubertrm.cashflow", importOptions = {ImportOption.DoNotIncludeTests.class })
public class CashflowLayeredArchTest {
    @ArchTest
    private final ArchRule layer_dependencies_are_respected_with_exception = layeredArchitecture()
            .layer("Controllers").definedBy("..controller..")
            .layer("Managers").definedBy("..manager..")
            .layer("Mappers").definedBy("..mapper..")

            .layer("Services").definedBy("..service..")
            .layer("Persistence").definedBy("..domain.*.repository..")
            .layer("Models").definedBy("..model..")

            .layer("Infrastructures").definedBy("..infra..")
            .layer("Repositories").definedBy("..infra.repository..")
            .layer("Entities").definedBy("..entity..")

            .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
            .whereLayer("Managers").mayOnlyBeAccessedByLayers("Controllers")
            .whereLayer("Mappers").mayOnlyBeAccessedByLayers("Managers", "Controllers")
            .whereLayer("Services").mayOnlyBeAccessedByLayers("Services", "Controllers", "Managers")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services", "Infrastructures")

            .ignoreDependency(modifier(JavaModifier.SYNTHETIC), DescribedPredicate.alwaysTrue());
}
