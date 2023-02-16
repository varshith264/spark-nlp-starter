package regex;

import model.DQCustomEntity;

import java.util.List;

public interface RegexAnalyzer {
    List<DQCustomEntity> analyse(String text);
}
