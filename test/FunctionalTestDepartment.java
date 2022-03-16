import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static play.test.Helpers.*;

// Use FixMethodOrder to run the tests sequentially
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionalTestDepartment extends WithApplication {

    @Test
    public void listDepartmentOnTheFirstPage() {
        Result result = route(app, controllers.routes.HomeControllerDepartment.list(0, "name", "asc", ""));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).contains("2 departments found");
    }

    @Test
    public void filterDepartmentByName() {
        Result result = route(app, controllers.routes.HomeControllerDepartment.list(0, "name", "asc", "unknown"));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).contains("2 departments found");
    }

}
