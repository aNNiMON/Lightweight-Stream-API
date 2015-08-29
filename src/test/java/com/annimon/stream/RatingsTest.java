package com.annimon.stream;

import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Function;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Real example of calculate ratings
 * @author aNNiMON
 */
public class RatingsTest {
    
    private static final BinaryOperator<Integer> sumOperator = new BinaryOperator<Integer>() {
        @Override
        public Integer apply(Integer value1, Integer value2) {
            return value1 + value2;
        }
    };
    private static Map<String, String> fileContents;
    
    @BeforeClass
    public static void setUpData() {
        fileContents = new HashMap<String, String>(5);
        fileContents.put("ahilla.txt", 
                "LongFlight     3 2 2 1 2\n" +
                "Units2D        2 1 1 0 1\n" +
                "SpaceCatcher   1 1 2 1 1\n" +
                "Galaxy         1 0 0 0 0\n" +
                "SonicTime3D    3 2 2 0 3\n" +
                "aPlatformer    1 1 0 1 0");
        fileContents.put("bogdan.txt", 
                "LongFlight     1 2 0 2 1\n" +
                "Units2D        0 1 0 1 0\n" +
                "SpaceCatcher   0 1 0 1 1\n" +
                "Galaxy         0 1 0 0 0\n" +
                "SonicTime3D    0 2 0 1 1\n" +
                "aPlatformer    1 1 0 1 1");
        fileContents.put("holdfast.txt", 
                "LongFlight     1 2 4 2 2\n" +
                "Units2D        0 2 1 2 1\n" +
                "SpaceCatcher   1 2 2 1 2\n" +
                "Galaxy         0 3 1 1 1\n" +
                "SonicTime3D    0 3 3 2 1\n" +
                "aPlatformer    1 0 0 1 1");
        fileContents.put("senderman.txt", 
                "LongFlight     1 3 2 3 2\n" +
                "Units2D        0 2 3 2 1\n" +
                "SpaceCatcher   0 2 5 3 1\n" +
                "Galaxy         0 3 0 0 0\n" +
                "SonicTime3D    0 3 1 1 0\n" +
                "aPlatformer    0 0 5 1 1");
    }

    @Test
    public void ratings() {
        String ratings = Stream.of(fileContents.keySet()) // list files
                // read content of files
                .flatMap(new Function<String, Stream<String>>() {
                    @Override
                    public Stream<String> apply(String filename) {
                        return readLines(filename);
                    }
                })
                // split line by whitespaces
                .map(new Function<String, String[]>() {
                    @Override
                    public String[] apply(String line) {
                        return line.split("\\s+");
                    }
                })
                // calculate sum by line and store in tuple <string, int>
                .map(new Function<String[], Map.Entry<String, Integer>>() {
                    @Override
                    public Map.Entry<String, Integer> apply(String[] arr) {
                        // <name, sum of marks>
                        return new AbstractMap.SimpleEntry<String, Integer>(
                                arr[0], Stream.of(arr)
                                        .skip(1)
                                        .map(new Function<String, Integer>() {
                                            @Override
                                            public Integer apply(String value) {
                                                return Integer.parseInt(value);
                                            }
                                        })
                                        .reduce(0, sumOperator)
                        );
                    }
                })
                // Group by name
                .groupBy(new Function<Map.Entry<String, Integer>, String>() {
                    @Override
                    public String apply(Map.Entry<String, Integer> entry) {
                        return entry.getKey();
                    }
                })
                // Calculate summary ratings
                .map(new Function<Map.Entry<String, List<Map.Entry<String, Integer>>>, Map.Entry<String, Integer>>() {
                    @Override
                    public Map.Entry<String, Integer> apply(Map.Entry<String, List<Map.Entry<String, Integer>>> entry) {
                        final String name = entry.getKey();
                        final int ratings = Stream.of(entry.getValue())
                                .map(new Function<Map.Entry<String, Integer>, Integer>() {
                                    @Override
                                    public Integer apply(Map.Entry<String, Integer> entry) {
                                        return entry.getValue();
                                    }
                                })
                                .reduce(0, sumOperator);
                        return new AbstractMap.SimpleEntry<String, Integer>(name, ratings);
                    }
                })
                // Sort by total rating descending
                .sortBy(new Function<Map.Entry<String, Integer>, Integer>() {
                    @Override
                    public Integer apply(Map.Entry<String, Integer> value) {
                        return -value.getValue();
                    }
                })
                // Convert to formatted string
                .map(new Function<Map.Entry<String, Integer>, String>() {
                    @Override
                    public String apply(Map.Entry<String, Integer> value) {
                        return String.format("%12s: %d", value.getKey(), value.getValue());
                    }
                })
                // lines to string
                .collect(Collectors.joining("\n"));
        
        /* Scala analog
        .flatMap(filename => Source.fromFile(filename, "UTF-8").getLines)
  	.map(s => s.split("\\s+"))
  	.map { arr => (arr(0), arr
  	    .drop(1)
  	    .map(_.toInt)
  	    .sum) }
  	.groupBy(_._1)
  	.map { case (name, ratings) => (name, ratings.map(_._2).sum)  }
  	.toSeq
  	.sortBy(- _._2)
  	.map { case (name, rating) => "%12s: %d".format(name, rating) }
  	.foreach(println);
        */
        
        Assert.assertEquals(
                "  LongFlight: 38\n" +
                " SonicTime3D: 28\n" +
                "SpaceCatcher: 28\n" +
                "     Units2D: 21\n" +
                " aPlatformer: 17\n" +
                "      Galaxy: 11", ratings);
    }
    
    /*
     * Emulates read lines from file
     */
    private static Stream<String> readLines(String filename) {
        return Stream.of(fileContents.get(filename).split("\n"));
    }
}
