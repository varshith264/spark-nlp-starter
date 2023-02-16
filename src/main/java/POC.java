import org.apache.spark.ml.Pipeline;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import utils.Helper;

import java.io.IOException;

public class POC {
    public static void main(String[] args) throws IOException {
        SparkSession sparkSession = SparkSession.builder()
                .appName("spark-nlp-starter")
                .master("local[*]")
                .config("spark.driver.memory", "1g")
                .getOrCreate();

        Dataset<Row> inputDf = NlpPipelineHelper.getInputDf();
        inputDf.show(100000, false);

        Pipeline nlpPipeline = NlpPipelineHelper.getNLPPipeline();
        Dataset<Row> nlpTransformedDf = nlpPipeline.fit(inputDf).transform(inputDf);
//
//        nlpTransformedDf.show(10000, false);
//
        nlpTransformedDf.select("text", "entities").show(10000, false);

        Dataset<Row> resultsDf = Helper.getEntitiesDf(nlpTransformedDf.select("text", "entities"));

        resultsDf.show(100000, false);
        System.in.read();
        sparkSession.stop();
    }
}
