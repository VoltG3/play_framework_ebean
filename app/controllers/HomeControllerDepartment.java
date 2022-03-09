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
}
