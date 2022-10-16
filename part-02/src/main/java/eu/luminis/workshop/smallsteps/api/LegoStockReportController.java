package eu.luminis.workshop.smallsteps.api;

import eu.luminis.workshop.smallsteps.logic.appservice.LegoStockQueries;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoStoreId;
import eu.luminis.workshop.smallsteps.logic.domainservice.auth.AuthProvider;
import org.jboss.resteasy.reactive.RestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static eu.luminis.workshop.smallsteps.api.support.HeaderReader.authoriseAndHandleStockMutation;

@RestController
public class LegoStockReportController {
    @Autowired
    LegoStockQueries legoStockQueries;

    @GetMapping(path = "/stock/report/current-missing-parts")
    public LegoParts currentMissingParts(
            @RestHeader("X-SELECTED-LEGO-STORE") String legoStoreIdParam,
            @RestHeader("X-API-GATEWAY-AUTH") String legoAuthParam) {

        AuthProvider authProvider = authoriseAndHandleStockMutation(legoAuthParam);
        authProvider.currentAuthentication().mustBeStore();

        return legoStockQueries.currentlyMissingPartsReport(new LegoStoreId(legoStoreIdParam));
    }

    @GetMapping(path = "/stock/report/historically-missing-parts")
    public LegoParts historicallyMissingParts(
            @RestHeader("X-SELECTED-LEGO-STORE") String legoStoreIdParam,
            @RestHeader("X-API-GATEWAY-AUTH") String legoAuthParam) {

        AuthProvider authProvider = authoriseAndHandleStockMutation(legoAuthParam);
        authProvider.currentAuthentication().mustBeStore();

        return legoStockQueries.historicallyMostLostParts(new LegoStoreId(legoStoreIdParam));
    }
}
