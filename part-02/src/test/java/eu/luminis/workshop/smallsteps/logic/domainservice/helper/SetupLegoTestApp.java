package eu.luminis.workshop.smallsteps.logic.domainservice.helper;

import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoBuilderId;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoParts;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoSetNumber;
import eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects.LegoStoreId;
import eu.luminis.workshop.smallsteps.logic.domainservice.auth.AuthProvider;
import eu.luminis.workshop.smallsteps.logic.domainservice.auth.LegoBuilderAuthentication;
import eu.luminis.workshop.smallsteps.logic.domainservice.auth.LegoStoreAuthentication;

import java.util.Map;

public class SetupLegoTestApp {
    public LegoSetNumber millenniumFalcon;
    public LegoBuilderId harry;
    public AuthProvider harryAuth;
    public LegoBuilderId sally;
    public LegoStoreId bussum;
    public AuthProvider bussumAuth;
    public LegoParts missingParts;

    public SetupLegoTestApp() {
        millenniumFalcon = new LegoSetNumber(75192);
        harry = new LegoBuilderId();
        harryAuth = () -> new LegoBuilderAuthentication(harry);
        sally = new LegoBuilderId();
        bussum = new LegoStoreId();
        bussumAuth = () -> new LegoStoreAuthentication(bussum);
        missingParts = LegoParts.from(Map.of("3898b", 1,"818622",3));
    }

}
