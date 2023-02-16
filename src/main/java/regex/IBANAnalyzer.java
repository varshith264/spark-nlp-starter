package regex;

import model.DQCustomEntity;
import utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IBANAnalyzer implements RegexAnalyzer{
    private static List<String> ibanRegex = Arrays.asList("\\b([A-Z]{2}[ \\-]?[0-9]{2})(?=(?:[ \\-]?[A-Z0-9]){9,30})((?:[ \\-]?[A-Z0-9]{3,5}){2})");
    @Override
    public List<DQCustomEntity> analyse(String text) {
        List<List<Integer>> matches = new ArrayList<>();
        ibanRegex.forEach(regex -> {
            matches.addAll(Helper.getMatches(text, regex));
        });
        List<DQCustomEntity> DQCustomEntities = new ArrayList<>();
        matches.forEach(match -> {
            DQCustomEntity DQCustomEntity = new DQCustomEntity();
            DQCustomEntity.setBegin(match.get(0));
            DQCustomEntity.setEnd(match.get(1));
            DQCustomEntity.setAnnotatorType("IBANAnalyzer");
            DQCustomEntities.add(DQCustomEntity);
        });
        return DQCustomEntities;
    }

    public static void main(String[] args) {
        String s = "my wallet address is: BE685390075470";
        String zx = s.replaceAll(ibanRegex.get(0), "found");
        Matcher m = Pattern.compile(ibanRegex.get(0)).matcher(s);
        while(m.find()) {
            System.out.println(m.start() + "  " + m.end());
        }
        System.out.println(zx);
    }


}
