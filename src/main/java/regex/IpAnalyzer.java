package regex;

import model.DQCustomEntity;
import utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAnalyzer implements RegexAnalyzer{

    private static List<String> ipRegex = Arrays.asList("\\b(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
    @Override
    public List<DQCustomEntity> analyse(String text) {
        List<List<Integer>> matches = new ArrayList<>();
        ipRegex.forEach(regex -> {
            matches.addAll(Helper.getMatches(text, regex));
        });
        List<DQCustomEntity> DQCustomEntities = new ArrayList<>();
        matches.forEach(match -> {
            DQCustomEntity DQCustomEntity = new DQCustomEntity();
            DQCustomEntity.setBegin(match.get(0));
            DQCustomEntity.setEnd(match.get(1));
            DQCustomEntity.setAnnotatorType("IpAnalyzer");
            DQCustomEntities.add(DQCustomEntity);
        });
        return DQCustomEntities;
    }

    public static void main(String[] args) {
        String s = "microsoft.com 192.168.0.1";
        String zx = s.replaceAll(ipRegex.get(0), "found");
        Matcher m = Pattern.compile(ipRegex.get(0)).matcher(s);
        while(m.find()) {
            System.out.println(m.start() + "  " + m.end());
        }
        System.out.println(zx);
    }
}
