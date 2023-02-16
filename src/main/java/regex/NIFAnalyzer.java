package regex;

import model.DQCustomEntity;
import utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NIFAnalyzer implements RegexAnalyzer{
    private static List<String> nifRegex = Arrays.asList("\\b[0-9]?[0-9]{7}[-]?[A-Z]\\b");
    @Override
    public List<DQCustomEntity> analyse(String text) {
        List<List<Integer>> matches = Helper.getMatches(text, nifRegex.get(0));
        List<DQCustomEntity> DQCustomEntities = new ArrayList<>();
        matches.forEach(match -> {
            DQCustomEntity DQCustomEntity = new DQCustomEntity();
            DQCustomEntity.setBegin(match.get(0));
            DQCustomEntity.setEnd(match.get(1));
            DQCustomEntity.setAnnotatorType("NIFAnalyzer");
            DQCustomEntities.add(DQCustomEntity);
        });
        return DQCustomEntities;
    }

    public static void main(String[] args) {
        String s = "1111111-G";
        String zx = s.replaceAll(nifRegex.get(0), "found");
        Matcher m = Pattern.compile(nifRegex.get(0)).matcher(s);
        while(m.find()) {
            System.out.println(m.start() + "  " + m.end());
        }
        System.out.println(zx);
    }
}
