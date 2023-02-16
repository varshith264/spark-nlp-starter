package regex;

import model.DQCustomEntity;
import utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailAnalyzer implements RegexAnalyzer{


    private static List<String> emailRegex = Arrays.asList("\\b((([!#$%&'*+\\-/=?^_`{|}~\\w])|([!#$%&'*+\\-/=?^_`{|}~\\w][!#$%&'*+\\-/=?^_`{|}~\\.\\w]{0,}[!#$%&'*+\\-/=?^_`{|}~\\w]))[@]\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)\\b");
    @Override
    public List<DQCustomEntity> analyse(String text) {
        List<List<Integer>> matches = new ArrayList<>();
        emailRegex.forEach(regex -> {
            matches.addAll(Helper.getMatches(text, regex));
        });
        List<DQCustomEntity> DQCustomEntities = new ArrayList<>();
        matches.forEach(match -> {
            DQCustomEntity DQCustomEntity = new DQCustomEntity();
            DQCustomEntity.setBegin(match.get(0));
            DQCustomEntity.setEnd(match.get(1));
            DQCustomEntity.setAnnotatorType("EmailAnalyzer");
            DQCustomEntities.add(DQCustomEntity);
        });
        return DQCustomEntities;
    }

    public static void main(String[] args) {
        String s = "try one of these emails: info@presidio.site or anotherinfo@presidio.site";
        String zx = s.replaceAll(emailRegex.get(0), "found");
        Matcher m = Pattern.compile(emailRegex.get(0)).matcher(s);
        while(m.find()) {
            System.out.println(m.start() + "  " + m.end());
        }
        System.out.println(zx);
    }

}
