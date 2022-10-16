package eu.luminis.workshop.smallsteps.persistence;

import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoSetNumber;
import eu.luminis.workshop.smallsteps.logic.domainservice.catalog.LegoPartCatalog;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InMemoryLegoPartCatalog implements LegoPartCatalog {
    private final Map<LegoSetNumber, LegoParts> partsForLegoSets;

    public InMemoryLegoPartCatalog() {
        this(null);
    }

    public InMemoryLegoPartCatalog(Map<LegoSetNumber, LegoParts> partsForLegoSets) {
        this.partsForLegoSets = partsForLegoSets;
    }

    @Override
    public LegoParts allPartsForLegoSet(LegoSetNumber legoSetNumber) {
        return partsForLegoSets.getOrDefault(legoSetNumber, LegoParts.empty());
    }
}
