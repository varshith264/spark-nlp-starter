package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.DQCustomEntity;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.json.JSONObject;
import regex.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.udf;

public class Helper {
    public static List<List<Integer>> getMatches(String text, String regex) {
        Matcher m = Pattern.compile(regex).matcher(text);
        List<List<Integer>> matches = new ArrayList<>();
        while(m.find()) {
            matches.add(Arrays.asList(m.start(), m.end()));
        }
        return matches;
    }

    public static List<DQCustomEntity> mergeCustoms(List<DQCustomEntity> DQCustomEntity1, List<DQCustomEntity> DQCustomEntity2) {
        List<DQCustomEntity> mergedDQCustomEntity = new ArrayList<>();
        mergedDQCustomEntity.addAll(DQCustomEntity1);
        mergedDQCustomEntity.addAll(DQCustomEntity2);
        return mergedDQCustomEntity;
    }

    public static Dataset<Row> getEntitiesDf(Dataset<Row> df) {
        return df.select(functions.to_json(functions.struct("*")).as("serialised_Json"))
                .selectExpr(String.format("CAST(%s AS STRING)", "serialised_Json"))
                .withColumn("text", Helper.parseUdf("text").apply(col("serialised_json")))
                .withColumn("redacted Text", Helper.redactTextWithNLPAndRegexEntities().apply(col("serialised_json")))
                .drop("serialised_json");
    }

    public static class Compare implements Comparator<DQCustomEntity> {
        @Override
        public int compare(DQCustomEntity DQCustomEntity, DQCustomEntity DQCustomEntity1) {
                if(DQCustomEntity1.getBegin() == DQCustomEntity.getBegin()) {
                    return Integer.compare(DQCustomEntity1.getEnd(), DQCustomEntity.getEnd());
                }
                return Integer.compare(DQCustomEntity.getBegin(), DQCustomEntity1.getBegin());
        }
    }

    private static void sortCustoms(List<DQCustomEntity> DQCustomEntities) {
        DQCustomEntities.sort(new Compare());
    }

    public static String getRedactedText(String text, List<DQCustomEntity> DQCustomEntities, Map<String, String> redactWith) {
        sortCustoms(DQCustomEntities);
        StringBuilder redactedString = new StringBuilder();
        int start = 0;
        for (DQCustomEntity DQCustomEntity : DQCustomEntities) {
            if (start <= DQCustomEntity.getBegin()) {
                String prefix = "";
                if (start != DQCustomEntity.getBegin()) {
                    prefix = text.substring(start, DQCustomEntity.getBegin());
                    redactedString.append(prefix);
                }
                redactedString.append("<").append(DQCustomEntity.getAnnotatorType().equalsIgnoreCase("chunk") ?
                        DQCustomEntity.getMetadata().get("entity") : DQCustomEntity.getAnnotatorType()).append(">");
                start = DQCustomEntity.end + 1;
            }
        }
        if(start < text.length()) {
            redactedString.append(text, start, text.length());
        }
        return redactedString.toString();
    }

    public static UserDefinedFunction redactTextWithNLPAndRegexEntities() {
        return udf(
                (String jsonString) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String text = jsonObject.get("text").toString();
                    List<DQCustomEntity> nlpDQCustomEntities = mapper.readValue(jsonObject.get("entities").toString(),
                            new TypeReference<List<DQCustomEntity>>(){});
                    List<DQCustomEntity> regexDQCustomEntities = new ArrayList<>();
                    regexDQCustomEntities.addAll(new CreditCardAnalyzer().analyse(text));
                    regexDQCustomEntities.addAll(new EmailAnalyzer().analyse(text));
                    regexDQCustomEntities.addAll(new UsBankAccountAnalyzer().analyse(text));
                    regexDQCustomEntities.addAll(new DrivingLiscenseAnalyzer().analyse(text));
                    regexDQCustomEntities.addAll(new CryptoAnalyzer().analyse(text));
                    regexDQCustomEntities.addAll(new IpAnalyzer().analyse(text));

                    List<DQCustomEntity> allDQCustomEntities = Helper.mergeCustoms(nlpDQCustomEntities, regexDQCustomEntities);

                    String redactedString = Helper.getRedactedText(text, allDQCustomEntities, new HashMap<>());

//                    System.out.println();
//                    System.out.println("Input text: " + text);
//                    System.out.println("Redacted text: " + redactedString);
//                    System.out.println();
//                    allDQCustomEntities.forEach(System.out::println);
//                    System.out.println();


                    return redactedString;
                }, DataTypes.StringType
        );
    }

    public static UserDefinedFunction parseUdf(String field) {
        return udf(
                (String jsonString) -> {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    return jsonObject.get(field).toString();
                }, DataTypes.StringType
        );
    }

}
