package eu.luminis.workshop.smallsteps.logic.domainmodel.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class LegoParts {

    @Getter
    private final Map<String, Integer> partsMap;

    private LegoParts(Map<String, Integer> partsMap) {
/*        partsMap.keySet().forEach(partNumber -> {
            require(isNumberString(partNumber),
                    String.format("Part number \"%s\" is not a number.", partNumber));
        });*/
        partsMap.values().forEach(count -> {
            require(count >= 0,
                    String.format("Part count \"%s\" can not be negative.", count));
        });

        this.partsMap = partsMap;
    }

    private static Boolean isNumberString(String partNumber) {
        try {
            Integer.parseInt(partNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static LegoParts from(Map<String, Integer> initial) {
        return new LegoParts(initial);
    }

    private static void require(boolean check, String message) {
        if (!check) {
            throw new IllegalArgumentException(message);
        }
    }

    public static LegoParts of(String k1, int v1, String k2, int v2) {
        return from(Map.of(k1, v1, k2, v2));
    }

    public static LegoParts mergeAndSum(List<LegoParts> legoPartList) {
        Map<String, Integer> mergedLegoParts = new HashMap<>();
        legoPartList.forEach(legoParts -> legoParts.keySet().forEach(key -> {
            mergedLegoParts.merge(key, legoParts.get(key), Integer::sum);
        }));
        return LegoParts.from(mergedLegoParts);
    }

    public static LegoParts empty() {
        return from(Collections.emptyMap());
    }

    public boolean contains(String partNumber) {
        return partsMap.containsKey(partNumber);
    }

    public int countPart(String partNumber) {
        return contains(partNumber) ? partsMap.get(partNumber) : 0;
    }

    public LegoParts mergeAndSum(LegoParts legoParts) {
        Map<String, Integer> mergedLegoParts = new HashMap<>(this.partsMap);

        legoParts.keySet().forEach(key -> {
            mergedLegoParts.merge(key, legoParts.get(key), Integer::sum);
        });

        return LegoParts.from(mergedLegoParts);
    }

    private Integer get(String key) {
        return partsMap.get(key);
    }

    public Set<String> keySet() {
        return partsMap.keySet();
    }

    public Integer count() {
        return partsMap.values().stream()
                .mapToInt(Integer::valueOf)
                .sum();
    }

    public LegoParts toSorted() {
        return from(partsMap.entrySet().stream()
                .sorted(Comparator.comparing(o -> Integer.valueOf(o.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> {
                            throw new RuntimeException(String.format("Duplicate key for values %s and %s", v1, v2));
                        },
                        LinkedHashMap::new)));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        partsMap.entrySet().forEach(stringIntegerEntry -> {
            sb.append(String.format("Part %s numberOfParts %s\n", stringIntegerEntry.getKey(), stringIntegerEntry.getValue()));
        });
        return sb.toString();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return partsMap.isEmpty();
    }

    public void forEach(BiConsumer<String, Integer> action) {
        partsMap.forEach(action);
    }

    public int getOrDefault(String partNumber, int defaultCount) {
        return Optional.ofNullable(get(partNumber)).orElse(defaultCount);
    }
}
