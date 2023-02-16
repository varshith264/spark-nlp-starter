package regex;

import model.DQCustomEntity;
import utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CryptoAnalyzer implements RegexAnalyzer{
    private static List<String> cryptoRegex = Arrays.asList("\\b[13][a-km-zA-HJ-NP-Z1-9]{26,33}\\b");
    @Override
    public List<DQCustomEntity> analyse(String text) {
        List<List<Integer>> matches = new ArrayList<>();
        cryptoRegex.forEach(regex -> {
            matches.addAll(Helper.getMatches(text, regex));
        });
        List<DQCustomEntity> DQCustomEntities = new ArrayList<>();
        matches.forEach(match -> {
            DQCustomEntity DQCustomEntity = new DQCustomEntity();
            DQCustomEntity.setBegin(match.get(0));
            DQCustomEntity.setEnd(match.get(1));
            DQCustomEntity.setAnnotatorType("CryptoAnalyzer");
            DQCustomEntities.add(DQCustomEntity);
        });
        return DQCustomEntities;
    }

    public static void main(String[] args) {
        String s = "my wallet address is: 16Yeky6GMjeNkAiNcBY7ZhrLoMSgg1BoyZ";
        String zx = s.replaceAll(cryptoRegex.get(0), "found");
        Matcher m = Pattern.compile(cryptoRegex.get(0)).matcher(s);
        while(m.find()) {
            System.out.println(m.start() + "  " + m.end());
        }
        System.out.println(zx);
    }
}
