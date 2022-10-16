package eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LegoPartsTest {

    public static final String RED_BRICK = "321";
    public static final int TEN_NUMBER_OF_PARTS = 10;
    private static final String BLUE_BRICK = "123";
    private static final int FIVE_NUMBER_OF_PARTS = 5;

    @Test
    public void should_contain_initially_added_parts() {
        Map<String, Integer> initial = new HashMap<>();

        LegoParts legoParts = LegoParts.from(getPartsMap(RED_BRICK, TEN_NUMBER_OF_PARTS));
        assertTrue(legoParts.contains(RED_BRICK));
//        assertFalse(legoParts.contains(part_name));
        assertEquals(legoParts.countPart(RED_BRICK), TEN_NUMBER_OF_PARTS);
    }

    private Map<String, Integer> getPartsMap(String partName, int amount) {
        Map<String, Integer> partsMap = new HashMap<>();
        partsMap.put(partName, amount);
        return partsMap;
    }

    @Test
    public void should_be_possible_to_merge_two_lego_parts_objects() {
        LegoParts tenRedBricks = LegoParts.from(getPartsMap(RED_BRICK, TEN_NUMBER_OF_PARTS));
        LegoParts fiveBlueBricks = LegoParts.from(getPartsMap(BLUE_BRICK, FIVE_NUMBER_OF_PARTS));


        LegoParts tenRedAndFiveBlueBricks = tenRedBricks.mergeAndSum(fiveBlueBricks);
        assertEquals(tenRedAndFiveBlueBricks.countPart(RED_BRICK), TEN_NUMBER_OF_PARTS);
        assertEquals(tenRedAndFiveBlueBricks.countPart(BLUE_BRICK), FIVE_NUMBER_OF_PARTS);
        assertEquals(tenRedAndFiveBlueBricks.count(), FIVE_NUMBER_OF_PARTS + TEN_NUMBER_OF_PARTS);

        LegoParts tenRedAndTenBlueBricks = tenRedAndFiveBlueBricks.mergeAndSum(fiveBlueBricks);
        assertEquals(tenRedAndTenBlueBricks.countPart(RED_BRICK), TEN_NUMBER_OF_PARTS);
        assertEquals(tenRedAndTenBlueBricks.countPart(BLUE_BRICK), TEN_NUMBER_OF_PARTS);
        assertEquals(tenRedAndTenBlueBricks.count(), TEN_NUMBER_OF_PARTS + TEN_NUMBER_OF_PARTS);

    }

    @Test
    public void should_be_possible_to_sort() {
        LegoParts tenRedBricks = LegoParts.from(getPartsMap("123", TEN_NUMBER_OF_PARTS));
        LegoParts tenGreenBricks = LegoParts.from(getPartsMap("666", TEN_NUMBER_OF_PARTS));

        LegoParts fiveBlueBricks = LegoParts.from(getPartsMap("667", FIVE_NUMBER_OF_PARTS));
        LegoParts tenRedAndFiveBlueBricks = tenRedBricks.mergeAndSum(tenGreenBricks).mergeAndSum(fiveBlueBricks);

//        System.out.println(tenRedAndFiveBlueBricks.toString());

        LegoParts sortedTenRedAndFiveBlueBricks = tenRedAndFiveBlueBricks.toSorted();

//        System.out.println("Sorted:\n" + sortedTenRedAndFiveBlueBricks.toString());

        final List<String> strings = new ArrayList(sortedTenRedAndFiveBlueBricks.keySet());
        assertTrue(Integer.parseInt(strings.get(0)) < Integer.parseInt(strings.get(1))
                && Integer.parseInt(strings.get(1)) < Integer.parseInt(strings.get(2)));
    }

    @Test
    public void should_throw_exception_on_negative_count() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> LegoParts.from(getPartsMap("123", -1)),
                "Expected LegoParts.from() to throw IllegalArgumentException on negative count"
        );
        assertEquals("Part count \"-1\" can not be negative.", thrown.getMessage());
    }

}
