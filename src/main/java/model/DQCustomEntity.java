package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class DQCustomEntity {
    public String annotatorType;
    public int begin;
    public int end;
    public String result;
    public Map<String, String> metadata = new HashMap<>();
    public List<Float> embeddings = new ArrayList<>();

}