package piplinelib

import com.johnsnowlabs.nlp.annotator.{DistilBertEmbeddings, NerConverter, NerDLModel, Tokenizer}
import com.johnsnowlabs.nlp.base.DocumentAssembler
import piplinelib.NlpPipelineLib.{getDistilBertEmbeddings, getNerDLModelInstance}
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.DataFrame

class NlpPipelineLib {


  /**
   * Generates Spark NLP Pipeline for a dataframe and specific columns only
   * Optimally loading BERT and NER models only once in memory
   *
   * @param dataframe
   */
  def getNlpPipelineForDf(dataframe: DataFrame, columns: Array[String]): Pipeline = {
    val pipeline = new Pipeline()
    val stages = columns.map(col => {
      getNlpPipelineForCol(col)
    })

    pipeline.setStages(stages)
  }


  /**
   * Generates Spark NLP Pipeline for a dataframe for all columns
   * Optimally loading BERT and NER models only once in memory
   *
   * @param dataframe
   */
  def getNlpPipelineForDf(dataframe: DataFrame): Pipeline = {
    getNlpPipelineForDf(dataframe, dataframe.columns)
  }


  def getNlpPipelineForCol(colName: String): Pipeline = {
    val document_assembler = new DocumentAssembler()
      .setInputCol(colName)
      .setOutputCol("document_" + colName)

    val tokenizer = new Tokenizer()
      .setInputCols("document_" + colName)
      .setOutputCol("token_" + colName)

    val embeddings = getDistilBertEmbeddings
      .setInputCols("document_" + colName, "token_" + colName)
      .setOutputCol("embeddings_" + colName)

    val ner_model = getNerDLModelInstance
      .setInputCols("document_" + colName, "token_" + colName, "embeddings_" + colName)
      .setOutputCol("ner_" + colName)

    val ner_converter = new NerConverter()
      .setInputCols("document_" + colName, "token_" + colName, "ner_" + colName)
      .setOutputCol("entities_" + colName)

    new Pipeline().setStages(Array(document_assembler, tokenizer, embeddings, ner_model, ner_converter))
  }

}

/**
 * Maintains static instances of DistlBert and NerDl models
 * Models are loaded in memory only on demand
 */
object NlpPipelineLib {
  def getDistilBertEmbeddings: DistilBertEmbeddings = {
    if(distilBertEmbeddingsInstance == null) {
      distilBertEmbeddingsInstance = DistilBertEmbeddings.load("src/main/NLPModels/distilbert_base_cased_en_3.1.0_2.4_1621521790187")
    }

    distilBertEmbeddingsInstance
  }

  def getNerDLModelInstance: NerDLModel = {
    if (nerDLModelInstance == null) {
      nerDLModelInstance = NerDLModel.load("src/main/NLPModels/distilbert_base_cased_en_3.1.0_2.4_1621521790187")
    }

    nerDLModelInstance
  }

  private var distilBertEmbeddingsInstance: DistilBertEmbeddings = null
  private var nerDLModelInstance: NerDLModel = null
}
