package controllers;

import models.Department;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.CompanyRepository;
import repository.DepartmentRepository;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.CompletionStage;


/**
 * Manage a database of department
 */
public class HomeControllerDepartment extends Controller {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    @Inject
    public HomeControllerDepartment(FormFactory formFactory,
                          DepartmentRepository departmentRepository,
                          CompanyRepository companyRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi) {
        this.departmentRepository = departmentRepository;
        this.formFactory = formFactory;
        this.companyRepository = companyRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     * This result directly redirect to application home.
     */
    private Result GO_HOME = Results.redirect(
        routes.HomeControllerDepartment.list(0, "name", "asc", "")
    );

    /**
     * Handle default path requests, redirect to department list
     */
    public Result index() {
        return GO_HOME;
    }

    /**
     * Display the paginated list of department.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    public CompletionStage<Result> list(Http.Request request, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return departmentRepository.page(page, 20, sortBy, order, filter).thenApplyAsync(list -> {
            // This is the HTTP rendering thread context

            // return ok(Json.toJson(list, sortBy, order, filter, request, messagesApi.preferred(request)));
            return ok(views.html.department.render(list, sortBy, order, filter, request, messagesApi.preferred(request)));

        }, httpExecutionContext.current());
    }

    /**
     * Display the 'edit form' of a existing Department.
     *
     * @param id Id of the department to edit
     */
    public CompletionStage<Result> edit(Http.Request request,Long id) {

        // Run a db operation in another thread (using DatabaseExecutionContext)
        CompletionStage<Map<String, String>> companiesFuture = companyRepository.options();

        // Run the lookup also in another thread, then combine the results:
        return departmentRepository.lookup(id).thenCombineAsync(companiesFuture, (departmentOptional, companies) -> {
            // This is the HTTP rendering thread context
            Department c = departmentOptional.get();
            Form<Department> departmentForm = formFactory.form(Department.class).fill(c);
            return ok(views.html.editFormDepartment.render(id, departmentForm, companies, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the department to edit
     */
    public CompletionStage<Result> update(Http.Request request, Long id) throws PersistenceException {
        Form<Department> departmentForm = formFactory.form(Department.class).bindFromRequest(request);
        if (departmentForm.hasErrors()) {
            // Run companies db operation and then render the failure case
            return companyRepository.options().thenApplyAsync(companies -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.editFormDepartment.render(id, departmentForm, companies, request, messagesApi.preferred(request)));
            }, httpExecutionContext.current());
        } else {
            Department newDepartmentData = departmentForm.get();
            // Run update operation and then flash and then redirect
            return departmentRepository.update(id, newDepartmentData).thenApplyAsync(data -> {
                // This is the HTTP rendering thread context
                return GO_HOME
                    .flashing("success", "Departmnet " + newDepartmentData.name + " has been updated");
            }, httpExecutionContext.current());
        }
    }

    /**
     * Display the 'new department form'.
     */
    public CompletionStage<Result> create(Http.Request request) {
        Form<Department> departmentForm = formFactory.form(Department.class);
        // Run companies db operation and then render the form
        return companyRepository.options().thenApplyAsync((Map<String, String> companies) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.createFormDepartment.render(departmentForm, companies, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'new department form' submission
     */
    public CompletionStage<Result> save(Http.Request request) {
        Form<Department> departmentForm = formFactory.form(Department.class).bindFromRequest(request);
        if (departmentForm.hasErrors()) {
            // Run companies db operation and then render the form
            return companyRepository.options().thenApplyAsync(companies -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.createFormDepartment.render(departmentForm, companies, request, messagesApi.preferred(request)));
            }, httpExecutionContext.current());
        }

        Department department = departmentForm.get();
        // Run insert db operation, then redirect
        return departmentRepository.insert(department).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                .flashing("success", "Department " + department.name + " has been created");
        }, httpExecutionContext.current());
    }

    /**
     * Handle department deletion
     */
    public CompletionStage<Result> delete(Long id) {
        // Run delete db operation, then redirect
        return departmentRepository.delete(id).thenApplyAsync(v -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                .flashing("success", "Department has been deleted");
        }, httpExecutionContext.current());
    }
}
