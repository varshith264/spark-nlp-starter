package regex;

import model.DQCustomEntity;
import utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreditCardAnalyzer implements RegexAnalyzer{
    private static List<String> creditCardRegex = Arrays.asList("\\b((4\\d{3})|(5[0-5]\\d{2})|(6\\d{3})|(1\\d{3})|(3\\d{3}))[- ]?(\\d{3,4})[- ]?(\\d{3,4})[- ]?(\\d{3,5})\\b");
    @Override
    public List<DQCustomEntity> analyse(String text) {
        List<List<Integer>> matches = Helper.getMatches(text, creditCardRegex.get(0));
        List<DQCustomEntity> DQCustomEntities = new ArrayList<>();
        matches.forEach(match -> {
            DQCustomEntity DQCustomEntity = new DQCustomEntity();
            DQCustomEntity.setBegin(match.get(0));
            DQCustomEntity.setEnd(match.get(1));
            DQCustomEntity.setAnnotatorType("CreditCardAnalyzer");

            DQCustomEntities.add(DQCustomEntity);
        });
        return DQCustomEntities;
    }

    public static void main(String[] args) {
        String s = "4012-8888-8888-1882 asdas a as dasd d ad  4012-8888-8888-1882";
        String zx = s.replaceAll(creditCardRegex.get(0), "found");

        Matcher m = Pattern.compile(creditCardRegex.get(0)).matcher(s);
//        List<Integer>
        while(m.find()) {
            System.out.println(m.start() + "  " + m.end());
        }

        System.out.println(zx);

    }

//    [
//            # fmt: off
//            (
//            "4012888888881881 4012-8888-8888-1881 4012 8888 8888 1881",
//                    3, (), ((0, 16), (17, 36), (37, 56),),
//            ),
//            ("122000000000003", 1, (), ((0, 15),),),
//            ("my credit card: 122000000000003", 1, (), ((16, 31),),),
//            ("371449635398431", 1, (), ((0, 15),),),
//            ("5555555555554444", 1, (), ((0, 16),),),
//            ("5019717010103742", 1, (), ((0, 16),),),
//            ("30569309025904", 1, (), ((0, 14),),),
//            ("6011000400000000", 1, (), ((0, 16),),),
//            ("3528000700000000", 1, (), ((0, 16),),),
//            ("6759649826438453", 1, (), ((0, 16),),),
//            ("5555555555554444", 1, (), ((0, 16),),),
//            ("4111111111111111", 1, (), ((0, 16),),),
//            ("4917300800000000", 1, (), ((0, 16),),),
//            ("4484070000000000", 1, (1.0,), ((0, 16),),),
//            ("4012-8888-8888-1882", 0, (), (),),
//            ("my credit card number is 4012-8888-8888-1882", 0, (), (),),
//            ("36168002586008", 0, (), (),),
//            ("my credit card number is 36168002586008", 0, (), (),),
//

}
