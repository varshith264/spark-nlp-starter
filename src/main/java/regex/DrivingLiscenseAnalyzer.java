package regex;

import model.DQCustomEntity;
import utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrivingLiscenseAnalyzer implements RegexAnalyzer{

    private static List<String> drivingLiscenseRegex = Arrays.asList("\\b(?i)(([A-Z]{2}\\d{7}[A-Z])|(^[U]1[BCDEFGHLMNPRSTUWYXZ]\\w{6}[A-Z]))\\b");
    @Override
    public List<DQCustomEntity> analyse(String text) {
        List<List<Integer>> matches = new ArrayList<>();
        drivingLiscenseRegex.forEach(regex -> {
            matches.addAll(Helper.getMatches(text, regex));
        });
        List<DQCustomEntity> DQCustomEntities = new ArrayList<>();
        matches.forEach(match -> {
            DQCustomEntity DQCustomEntity = new DQCustomEntity();
            DQCustomEntity.setBegin(match.get(0));
            DQCustomEntity.setEnd(match.get(1));
            DQCustomEntity.setAnnotatorType("DrivingLiscenseAnalyzer");
            DQCustomEntities.add(DQCustomEntity);
        });
        return DQCustomEntities;
    }

    public static void main(String[] args) {
        String s = "AA0123456B and AA0123456B";
        String zx = s.replaceAll(drivingLiscenseRegex.get(0), "found");
        Matcher m = Pattern.compile(drivingLiscenseRegex.get(0)).matcher(s);
        while(m.find()) {
            System.out.println(m.start() + "  " + m.end());
        }
        System.out.println(zx);
    }
}
