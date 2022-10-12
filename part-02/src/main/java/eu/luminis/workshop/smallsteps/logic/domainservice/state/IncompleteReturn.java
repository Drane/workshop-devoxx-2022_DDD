package eu.luminis.workshop.smallsteps.logic.domainservice.state;

import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoSetNumber;

import java.util.Map;
import java.util.Objects;

public class IncompleteReturn {
    private LegoSetNumber legoSetNumber;
    private LegoParts missingParts;

    public IncompleteReturn(LegoSetNumber legoSetNumber, LegoParts missingParts) {
        this.legoSetNumber = legoSetNumber;
        this.missingParts = missingParts;
    }

    public LegoSetNumber getLegoSetNumber() {
        return legoSetNumber;
    }

    public LegoParts getMissingParts() {
        return missingParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncompleteReturn that = (IncompleteReturn) o;
        return Objects.equals(legoSetNumber, that.legoSetNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(legoSetNumber);
    }
}
