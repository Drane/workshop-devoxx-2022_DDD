package eu.luminis.workshop.smallsteps.logic.domainservice;

import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoBuilderId;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoSetNumber;
import eu.luminis.workshop.smallsteps.logic.domainservice.auth.AuthProvider;
import eu.luminis.workshop.smallsteps.logic.domainservice.catalog.LegoPartCatalog;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.IncompleteReturn;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.LegoBox;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.LegoSetForBuilder;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.StockState;

import java.util.*;

public class LegoStock {
    private final AuthProvider authProvider;
    private final LegoPartCatalog legoPartCatalog;
    private final StockState stockState;

    public LegoStock(AuthProvider authProvider, LegoPartCatalog legoPartCatalog, StockState stockState) {
        this.authProvider = authProvider;
        this.legoPartCatalog = legoPartCatalog;
        this.stockState = stockState;
    }

    public StockState reserve(LegoSetNumber legoSetNumber) {
        LegoBuilderId forBuilder = authProvider.currentAuthentication().requiredBuilderId();

        Integer inStock = stockState.getAvailableStock().getOrDefault(legoSetNumber, 0);
        require(inStock > 0, String.format("LegoSet %d is not in stock", legoSetNumber.getNumber()));

        return stockState.copy(
                reduceStockByOne(legoSetNumber, inStock),
                addReservation(legoSetNumber, forBuilder),
                null,
                null,
                null);
    }

    public StockState ship(LegoSetNumber legoSetNumber, LegoBuilderId forBuilder) {
        authProvider.currentAuthentication().mustBeStore();
        check(stockState.getReserved().contains(new LegoSetForBuilder(legoSetNumber, forBuilder)),
                String.format("Legoset %d has not been reserved by builder %s",
                        legoSetNumber.getNumber(), forBuilder.toString()));


        return stockState.copy(
                null,
                removeReservation(legoSetNumber, forBuilder),
                addAtBuilder(legoSetNumber, forBuilder),
                null,
                null);
    }

    public StockState returnComplete(LegoSetNumber legoSetNumber) {
        LegoBuilderId byBuilder = authProvider.currentAuthentication().requiredBuilderId();

        check(stockState.getAtBuilder().contains(new LegoSetForBuilder(legoSetNumber, byBuilder)),
                String.format("Cannot return %s as builder %s does not have it.",
                        legoSetNumber.getNumber(), byBuilder.toString()));

        return stockState.copy(
                null,
                null,
                null,
                null,
                null);
    }

    public StockState returnInComplete(LegoBox legoBox) {
        LegoBuilderId byBuilder = authProvider.currentAuthentication().requiredBuilderId();
        check(stockState.getAtBuilder().contains(new LegoSetForBuilder(legoBox.getLegoSetNumber(), byBuilder)),
                String.format("Cannot return %s as builder %s does not have it.",
                        legoBox.getLegoSetNumber().getNumber(), byBuilder.toString()));

        this.requireValidMissingLegoParts(legoBox);

        return stockState.copy(
                null,
                null,
                null,
                addIncompleteStock(legoBox),
                addIncompleteReturnHistory(legoBox));
    }

    private List<IncompleteReturn> addIncompleteReturnHistory(LegoBox legoBox) {
        return new ArrayList<>() {
            {
                addAll(stockState.getIncompleteReturnHistory());
                add(new IncompleteReturn(legoBox.getLegoSetNumber(), legoBox.getMissingParts()));
            }
        };
    }

    private List<LegoBox> addIncompleteStock(LegoBox legoBox) {
        return new ArrayList<>() {
            {
                addAll(stockState.getIncompleteStock());
                add(legoBox);
            }
        };
    }

    private Set<LegoSetForBuilder> addReservation(LegoSetNumber legoSetNumber, LegoBuilderId forBuilder) {
        return new HashSet<>() {
            {
                addAll(stockState.getReserved());
                add(new LegoSetForBuilder(legoSetNumber, forBuilder));
            }
        };
    }

    private Map<LegoSetNumber, Integer> reduceStockByOne(LegoSetNumber legoSetNumber, Integer inStock) {
        return new HashMap<>() {
            {
                putAll(stockState.getAvailableStock());
                put(legoSetNumber, inStock - 1);
            }
        };
    }

    private List<LegoSetForBuilder> addAtBuilder(LegoSetNumber legoSetNumber, LegoBuilderId forBuilder) {
        return new ArrayList<>() {
            {
                addAll(stockState.getAtBuilder());
                add(new LegoSetForBuilder(legoSetNumber, forBuilder));
            }
        };
    }

    private Set<LegoSetForBuilder> removeReservation(LegoSetNumber legoSetNumber, LegoBuilderId forBuilder) {
        return new HashSet<>() {
            {
                addAll(stockState.getReserved());
                remove(new LegoSetForBuilder(legoSetNumber, forBuilder));
            }
        };
    }

    private void requireValidMissingLegoParts(LegoBox legoBox) {
        require(!legoBox.getMissingParts().isEmpty(), "No missing parts specified");
        legoBox.getMissingParts().forEach((partNumber, numberOfParts) -> {
            require(numberOfParts > 0, "You must specify how many parts are missing");
        });

        LegoParts expectedParts = legoPartCatalog.allPartsForLegoSet(legoBox.getLegoSetNumber());
        require(!expectedParts.isEmpty(),
                String.format("Unable to determine parts for lego set %s", legoBox.getLegoSetNumber()));

        legoBox.getMissingParts().forEach((partNumber, numberOfParts) -> {
            require(expectedParts.contains(partNumber),
                    String.format("Some parts reported missing don't belong to lego set %s",
                            legoBox.getLegoSetNumber()));

            require(expectedParts.getOrDefault(partNumber, 0) >= numberOfParts,
                    String.format("Too many parts reported missing, the original lego set %s did not contain that " +
                            "many of this part %s", legoBox.getLegoSetNumber().getNumber(), partNumber));
        });
    }

    private static void require(boolean check, String message) {
        if (!check) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void check(boolean check, String message) {
        if (!check) {
            throw new IllegalStateException(message);
        }
    }
}
