package repository;

import io.ebean.*;
import models.Department;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import java.util.*;
/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class DepartmentRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public DepartmentRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * Return a paged list of department
     *
     * @param page     Page to display
     * @param pageSize Number of department per page
     * @param sortBy   Department property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     */
    public CompletionStage<PagedList<Department>> page(int page, int pageSize, String sortBy, String order, String filter) {
        return supplyAsync(() ->
                ebeanServer.find(Department.class).where()
                    .ilike("name", "%" + filter + "%")
                    .orderBy(sortBy + " " + order)
                    .fetch("company")
                    .setFirstRow(page * pageSize)
                    .setMaxRows(pageSize)
                    .findPagedList(), executionContext);
    }

    public CompletionStage<Optional<Department>> lookup(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(Department.class).setId(id).findOne()), executionContext);
    }

    public CompletionStage<Optional<Long>> update(Long id, Department newDepartmentData) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Long> value = Optional.empty();
            try {
                Department savedDepartment = ebeanServer.find(Department.class).setId(id).findOne();
                if (savedDepartment != null) {
                    savedDepartment.company = newDepartmentData.company;
                    savedDepartment.created_at = newDepartmentData.created_at;
                    savedDepartment.name = newDepartmentData.name;

                    savedDepartment.update();
                    txn.commit();
                    value = Optional.of(id);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }

    public CompletionStage<Optional<Long>>  delete(Long id) {
        return supplyAsync(() -> {
            try {
                final Optional<Department> departmentOptional = Optional.ofNullable(ebeanServer.find(Department.class).setId(id).findOne());
                departmentOptional.ifPresent(Model::delete);
                return departmentOptional.map(c -> c.id);
            } catch (Exception e) {
                return Optional.empty();
            }
        }, executionContext);
    }

    public CompletionStage<Long> insert(Department department) {
        return supplyAsync(() -> {
             //department.id = System.currentTimeMillis(); // not ideal, but it works
             ebeanServer.insert(department);
             return department.id;
        }, executionContext);
    }
}
