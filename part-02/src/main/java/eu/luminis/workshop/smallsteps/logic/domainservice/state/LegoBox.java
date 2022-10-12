package eu.luminis.workshop.smallsteps.logic.domainservice.state;

import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoSetNumber;

import java.util.Map;
import java.util.Objects;

public class LegoBox {
    private final LegoSetNumber legoSetNumber;
    private final Integer boxNumber;
    private final LegoParts missingParts;

    public LegoBox(LegoSetNumber legoSetNumber, Integer boxNumber, LegoParts missingParts) {
        this.legoSetNumber = legoSetNumber;
        this.boxNumber = boxNumber;
        this.missingParts = missingParts;
    }

    public LegoSetNumber getLegoSetNumber() {
        return legoSetNumber;
    }

    public Integer getBoxNumber() {
        return boxNumber;
    }

    public LegoParts getMissingParts() {
        return missingParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegoBox legoBox = (LegoBox) o;
        return Objects.equals(legoSetNumber, legoBox.legoSetNumber) && Objects.equals(boxNumber, legoBox.boxNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(legoSetNumber, boxNumber);
    }
}
