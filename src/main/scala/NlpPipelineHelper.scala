import com.johnsnowlabs.nlp.annotator._
import com.johnsnowlabs.nlp.base._
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.{DataFrame, SparkSession}

object NlpPipelineHelper {

  val spark: SparkSession = SparkSession.active

  def getNLPPipeline: Pipeline = {
    val document_assembler = new DocumentAssembler()
      .setInputCol("text")
      .setOutputCol("document")

    val tokenizer = new Tokenizer()
      .setInputCols("document")
      .setOutputCol("token")

    val embeddings = DistilBertEmbeddings.load("/home/varshith/IdeaProjects/spark-nlp-starter/src/main/NLPModels/distilbert_base_cased_en_3.1.0_2.4_1621521790187")
      //    val embeddings = DistilBertEmbeddings.pretrained("distilbert_base_cased", "en")
      // output vector size 1 x 768 for each token
      .setInputCols("document", "token")
      .setOutputCol("embeddings")

    //    val ner_model = NerDLModel.pretrained()
    val ner_model = NerDLModel.load("/home/varshith/IdeaProjects/spark-nlp-starter/src/main/NLPModels/ner_ontonotes_distilbert_base_cased_en_3.2.0_2.4_1628079072311")
      //    val ner_model = NerDLModel.pretrained("ner_ontonotes_distilbert_base_cased", "en")
      .setInputCols("document", "token", "embeddings")
      .setOutputCol("ner")

    val ner_converter = new NerConverter()
      .setInputCols("document", "token", "ner")
      .setOutputCol("entities")

    new Pipeline().setStages(Array(document_assembler, tokenizer, embeddings, ner_model, ner_converter))

  }

  def getInputDf: DataFrame = {

//    val csvPath = "/home/varshith/IdeaProjects/spark-nlp-starter/src/test/testdata/sample_100k_rows.csv";
//    val csvPath = "/home/varshith/IdeaProjects/spark-nlp-starter/src/test/testdata/Sales_100_rows.csv";
//    val csvPath = "/home/varshith/IdeaProjects/spark-nlp-starter/src/test/testdata/sample_1k_rows.csv";
    val csvPath = "/home/varshith/IdeaProjects/spark-nlp-starter/src/test/testdata/sample_Test_10_Dataset.csv";
    spark.read.option("header", value = true).csv(csvPath);

//    spark
//      .createDataFrame(
//        Seq(
//          (1, "Mr. Zukenberg (born 2000-10-23T13:13:00) is an American business magnate, his credit card number is 4012-8888-8888-1882, He is co-founder of Microsoft Corporation."),
//          (2, "Chadha Siddhant is not a terrorist. My name is terrorist and I am Khan, his credit card is 36168002586008"),
//          (3, "Nothing is sensitive there in the text"),
//          (4, "it went on to become the world's largest personal computer software company. Gates led the company as chairman and CEO until stepping down as CEO in January 2000" ),
//          (5, "Hello, my name is David Johnson and I live in Maine. My credit card number is 4095-2609-9393-4932 and my crypto wallet id is 16Yeky6GMjeNkAiNcBY7ZhrLoMSgg1BoyZ."),
//          (6, "On September 18 I visited microsoft.com and sent an email to test@presidio.site,  from the IP 192.168.0.1."),
//          (7, "For more information see the Code of Conduct FAQ or contact opencode@microsoft.com with any additional questions or comments."),
//          (8, "president and chief software architect, while also being the largest individual shareholder until May 2014.")))
//      .toDF("id", "text")


  }
}
