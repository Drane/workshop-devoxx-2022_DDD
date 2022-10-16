package eu.luminis.workshop.smallsteps.logic.domainservice;

import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoSetNumber;
import eu.luminis.workshop.smallsteps.logic.domainservice.helper.SetupLegoTestApp;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.IncompleteReturn;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.LegoBox;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.LegoSetForBuilder;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.StockState;
import eu.luminis.workshop.smallsteps.persistence.InMemoryLegoPartCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class LegoStockReturningInCompleteTest {
    SetupLegoTestApp app;
    Map<LegoSetNumber, LegoParts> partsForLegoSets;
    LegoParts missingPartsForMilleniumFalcon;
    LegoParts nonMilleniumFalconParts;


    @BeforeEach
    void setUp() {
        app = new SetupLegoTestApp();
        partsForLegoSets = Map.of(
                app.millenniumFalcon, LegoParts.from(Map.of(
                        "3022", 8,
                        "2420", 2,
                        "60581", 2,
                        "4865b", 2,
                        "20105", 1
                )
        ));
        missingPartsForMilleniumFalcon = LegoParts.from(Map.of("20105", 1, "3022", 3));
        nonMilleniumFalconParts = LegoParts.from(Map.of("00000", 1,"99999c",2));
    }

    @Test
    public void should_register_returning_incomplete_lego_set() {
        StockState initialState = new StockState(
                null,
                null,
                List.of(new LegoSetForBuilder(app.millenniumFalcon, app.harry)),
                null,
                null
        );
        LegoStock handler = new LegoStock(app.harryAuth, new InMemoryLegoPartCatalog(partsForLegoSets), initialState);

        StockState newState = handler.returnInComplete(
                new LegoBox(app.millenniumFalcon, 42, missingPartsForMilleniumFalcon));

        assertThat(List.of(new LegoBox(app.millenniumFalcon, 42, missingPartsForMilleniumFalcon)))
                .isEqualTo(newState.getIncompleteStock());
        assertThat(List.of(new IncompleteReturn(app.millenniumFalcon, missingPartsForMilleniumFalcon)))
                .isEqualTo(newState.getIncompleteReturnHistory());

        assertThat(newState.getAtBuilder()).isEqualTo(initialState.getAtBuilder());
        assertThat(newState.getAvailableStock()).isEqualTo(initialState.getAvailableStock());
        assertThat(newState.getReserved()).isEqualTo(initialState.getReserved());
    }

    @Test
    public void should_fail_to_register_incomplete_lego_set_when_no_missing_parts_specified() {
        StockState initialState = new StockState(
                null,
                null,
                List.of(new LegoSetForBuilder(app.millenniumFalcon, app.harry)),
                null,
                null
        );
        LegoStock handler = new LegoStock(app.harryAuth, new InMemoryLegoPartCatalog(partsForLegoSets), initialState);

        assertThatThrownBy(() -> {
            handler.returnInComplete(new LegoBox(app.millenniumFalcon, 42, LegoParts.from(Collections.emptyMap())));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_fail_to_register_incomplete_lego_set_when_missing_parts_specified_do_not_belong_to_legoset() {
        StockState initialState = new StockState(
                null,
                null,
                List.of(new LegoSetForBuilder(app.millenniumFalcon, app.harry)),
                null,
                null
        );
        LegoStock handler = new LegoStock(app.harryAuth, new InMemoryLegoPartCatalog(partsForLegoSets), initialState);

        assertThatThrownBy(() -> {
            handler.returnInComplete(new LegoBox(app.millenniumFalcon, 42, nonMilleniumFalconParts));
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void should_fail_to_register_incomplete_lego_set_when_missing_parts_specified_more_than_in_set() {
        StockState initialState = new StockState(
                null,
                null,
                List.of(new LegoSetForBuilder(app.millenniumFalcon, app.harry)),
                null,
                null
        );
        LegoStock handler = new LegoStock(app.harryAuth, new InMemoryLegoPartCatalog(partsForLegoSets), initialState);

        assertThatThrownBy(() -> {
            handler.returnInComplete(new LegoBox(app.millenniumFalcon, 42, LegoParts.from(Map.of("20105",5))));
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            handler.returnInComplete(new LegoBox(app.millenniumFalcon, 42, LegoParts.from(Map.of("20105",0))));
        }).isInstanceOf(IllegalArgumentException.class);

    }

}
