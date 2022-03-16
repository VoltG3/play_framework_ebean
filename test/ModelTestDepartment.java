import io.ebean.PagedList;
import models.Department;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import repository.DepartmentRepository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class ModelTestDepartment extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    private String formatted(Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    @Test
    public void findById() {
        final DepartmentRepository departmentRepository = app.injector().instanceOf(DepartmentRepository.class);
        final CompletionStage<Optional<Department>> stage = departmentRepository.lookup(1L);

        await().atMost(1, SECONDS).until(() -> {
            final Optional<Department> unknownDepartment1 = stage.toCompletableFuture().get();
            return unknownDepartment1
                .map(mac -> mac.name.equals("unknown department 1") && formatted(mac.created_at).equals("1983-06-27"))
                .orElseGet(() -> false);
        });
    }
    
    @Test
    public void pagination() {
        final DepartmentRepository departmentRepository = app.injector().instanceOf(DepartmentRepository.class);
        CompletionStage<PagedList<Department>> stage = departmentRepository.page(1, 2, "name", "ASC", "");

        // Test the completed result
        await().atMost(1, SECONDS).until(() -> {
            PagedList<Department> department = stage.toCompletableFuture().get();
            return department.getTotalCount() == 2 &&
                   department.getTotalPageCount() == 1; // &&
                // department.getList().size() == 2;
        });
    }
    
}
