package eu.luminis.workshop.smallsteps.api;

import eu.luminis.workshop.smallsteps.helpers.TestLegoStockRepository;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoSetNumber;
import eu.luminis.workshop.smallsteps.logic.domainservice.helper.SetupLegoTestApp;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.IncompleteReturn;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.LegoBox;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.StockState;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class LegoStockReportControllerTest {
    private final LegoSetNumber millenniumFalcon = new LegoSetNumber(75192);
    private final LegoSetNumber atAt = new LegoSetNumber(75288);
    private final LegoSetNumber r2d2 = new LegoSetNumber(75308);

    SetupLegoTestApp config;

    @Inject
    TestLegoStockRepository repository;

    @BeforeEach
    void setUp() {
        config = new SetupLegoTestApp();

        repository.setInitialState(new StockState(
                null,
                null,
                null,
                List.of(
                        new LegoBox(millenniumFalcon, 42, LegoParts.of("3022", 7, "20105", 1)),
                        new LegoBox(millenniumFalcon, 37, LegoParts.of("3022", 3, "60581", 1)),
                        new LegoBox(atAt, 5, LegoParts.of("3022", 2, "18674", 1)),
                        new LegoBox(r2d2, 8, LegoParts.of("3666", 2, "64799", 1))
                ),
                List.of(
                        new IncompleteReturn(millenniumFalcon, LegoParts.of("3022", 5, "20105", 1)),
                        new IncompleteReturn(millenniumFalcon, LegoParts.of("3022", 3, "60581", 1)),
                        new IncompleteReturn(atAt, LegoParts.of("3022", 2, "18674", 1)),
                        new IncompleteReturn(r2d2, LegoParts.of("3666", 2, "64799", 1))
                ))
        );
    }

    @Test
    public void should_return_missing_parts_report() {
        given()
                .header("X-API-GATEWAY-AUTH", config.bussum.toString())
                .header("X-SELECTED-LEGO-STORE", config.bussum.toString())
                .when()
                .get("/stock/report/current-missing-parts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("{\"partsMap\":{\"3022\":12,\"3666\":2,\"18674\":1,\"20105\":1,\"60581\":1,\"64799\":1}}"));
    }

    @Test
    public void should_return_historically_most_lost_parts_report() {
        given()
                .header("X-API-GATEWAY-AUTH", config.bussum.toString())
                .header("X-SELECTED-LEGO-STORE", config.bussum.toString())
                .when()
                .get("/stock/report/historically-missing-parts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("{\"partsMap\":{\"3022\":10,\"3666\":2,\"18674\":1,\"20105\":1,\"60581\":1,\"64799\":1}}"));
    }

    @Test
    public void should_fail_with_access_denied_eception() {
        given()
                .header("X-API-GATEWAY-AUTH", config.harry.toString())
                .header("X-SELECTED-LEGO-STORE", config.bussum.toString())
                .when()
                .get("/stock/report/historically-missing-parts")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }
}
