package eu.luminis.workshop.smallsteps.logic.appservice;

import eu.luminis.workshop.smallsteps.helpers.TestLegoStockRepository;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoSetNumber;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoStoreId;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.IncompleteReturn;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.LegoBox;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.StockState;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class LegoStockQueriesTest {
    private final LegoSetNumber millenniumFalcon = new LegoSetNumber(75192);
    private final LegoSetNumber atAt = new LegoSetNumber(75288);
    private final LegoSetNumber r2d2 = new LegoSetNumber(75308);
    private final LegoStoreId bussum = new LegoStoreId();

    @Test
    public void should_return_report_with_of_all_currently_missing_parts() {
        TestLegoStockRepository repository = new TestLegoStockRepository();
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
                null)
        );
        LegoStockQueries queries = new LegoStockQueries(
                repository);

        LegoParts pairs = queries.currentlyMissingPartsReport(bussum);

        assertThat(pairs.getPartsMap()).containsExactlyEntriesOf(new LinkedHashMap<>() {{
            put("3022", 12);
            put("3666", 2);
            put("18674", 1);
            put("20105", 1);
            put("60581", 1);
            put("64799", 1);
        }});
    }

    @Test
    public void should_return_report_with_of_all_historically_missing_parts() {
        TestLegoStockRepository repository = new TestLegoStockRepository();
        repository.setInitialState(new StockState(
                null,
                null,
                null,
                null,
                List.of(
                        new IncompleteReturn(millenniumFalcon, LegoParts.of("3022", 7, "20105", 1)),
                        new IncompleteReturn(millenniumFalcon, LegoParts.of("3022", 3, "60581", 1)),
                        new IncompleteReturn(atAt, LegoParts.of("3022", 2, "18674", 1)),
                        new IncompleteReturn(r2d2, LegoParts.of("3666", 2, "64799", 1))
                ))
        );
        LegoStockQueries queries = new LegoStockQueries(repository);

        LegoParts historicallyMostLostParts = queries.historicallyMostLostParts(bussum);

        assertThat(historicallyMostLostParts.getPartsMap())
                .containsExactlyEntriesOf(new LinkedHashMap<>() {{
                    put("3022", 12);
                    put("3666", 2);
                    put("18674", 1);
                    put("20105", 1);
                    put("60581", 1);
                    put("64799", 1);
                }});
    }
}
