package eu.luminis.workshop.smallsteps.logic.appservice;

import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoStoreId;
import eu.luminis.workshop.smallsteps.logic.domainservice.state.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts.mergeAndSum;

@Service
public class LegoStockQueries {
    private final LegoStockRepository repository;

    @Autowired
    public LegoStockQueries(LegoStockRepository repository) {
        this.repository = repository;
    }

    public LegoParts currentlyMissingPartsReport(LegoStoreId legoStoreId) {
        StockState foundState = repository.find(legoStoreId);

        List<LegoParts> collect = foundState.getIncompleteStock().stream()
                .map(LegoBox::getMissingParts)
                .collect(Collectors.toList());

        /*LegoParts merged = mergeAndSum(collect);

        return toSortedMap(merged);*/

        return  mergeAndSum(collect).toSorted();
    }

    public LegoParts historicallyMostLostParts(LegoStoreId legoStoreId) {
        StockState foundState = repository.find(legoStoreId);

        List<LegoParts> collect = foundState.getIncompleteReturnHistory().stream()
                .map(IncompleteReturn::getMissingParts)
                .collect(Collectors.toList());

/*        LegoParts merged = mergeAndSum(collect);

        return toSortedMap(merged);*/

        return mergeAndSum(collect).toSorted();



    }

/*    private LegoParts mergeAndSum(List<LegoParts> collect) {
        LegoParts missingParts = new HashMap<>();
        collect.forEach(item -> {
            item.keySet().forEach(key -> {
                missingParts.mergeAndSum(key, item.get(key), Integer::sum);
            });
        });
        return missingParts;
    }*/

/*    private LegoParts toSortedMap(LegoParts missingParts) {
        return missingParts.entrySet().stream()
                .sorted(Comparator.comparing(o -> Integer.valueOf(o.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> {
                            throw new RuntimeException(String.format("Duplicate key for values %s and %s", v1, v2));
                        },
                        LinkedHashMap::new));
    }*/
}
