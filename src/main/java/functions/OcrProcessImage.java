/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package functions;

// [START functions_ocr_process]

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.translate.v3.DetectLanguageRequest;
import com.google.cloud.translate.v3.DetectLanguageResponse;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageRequest.Builder;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import functions.eventpojos.GcsEvent;
import main.StringWorks;
import vo.ResponseDavid;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

// [END functions_ocr_process]

// [START functions_ocr_setup]
public class OcrProcessImage implements BackgroundFunction<GcsEvent> {
  // TODO<developer> set these environment variables
  private static final String PROJECT_ID = "dynamic-branch-679";
  private static final String TRANSLATE_TOPIC_NAME ="renatotn7ocrtranslatepubsub";
  private static final String[] TO_LANGS = "pt".split(",");

  private static final Logger logger = Logger.getLogger(OcrProcessImage.class.getName());
  private static final String LOCATION_NAME = LocationName.of(PROJECT_ID, "global").toString();
  private Publisher publisher;
 public String outputOCR;
 public List<String> grp_perguntas = new ArrayList<String>();
 public List<String> paragrafos = new ArrayList<String>();


 public List<String> listPerguntas=new ArrayList<String>();
  public OcrProcessImage() throws IOException {
    publisher = Publisher.newBuilder(
        ProjectTopicName.of(PROJECT_ID, TRANSLATE_TOPIC_NAME)).build();
  }
  // [END functions_ocr_setup]

  // [START functions_ocr_process]
  @Override
  public void accept(GcsEvent gcsEvent, Context context) {

    // Validate parameters
    String bucket = gcsEvent.getBucket();
    if (bucket == null) {
      throw new IllegalArgumentException("Missing bucket parameter");
    }
    String filename = gcsEvent.getName();
    if (filename == null) {
      throw new IllegalArgumentException("Missing name parameter");
    }

    detectText(bucket, filename);
  }
  // [END functions_ocr_process]

  // [START functions_ocr_detect]
  private void detectText(String bucket, String filename) {
    logger.info("Looking for text in image " + filename);

    List<AnnotateImageRequest> visionRequests = new ArrayList<>();
    String gcsPath = String.format("gs://%s/%s", bucket, filename);

    ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
    Image img = Image.newBuilder().setSource(imgSource).build();

    Feature textFeature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
    Feature textFeature2 = Feature.newBuilder().setType(Feature.Type.CROP_HINTS).build();
    Feature textFeature3 = Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION).build();
   Builder builder =  AnnotateImageRequest.newBuilder();
   builder.addFeatures(textFeature);
   builder.addFeatures(textFeature2);
   builder.addFeatures(textFeature3);
    AnnotateImageRequest visionRequest =
    		builder.setImage(img).build();
  //  visionRequest.
    visionRequests.add(visionRequest);

    // Detect text in an image using the Cloud Vision API
    AnnotateImageResponse visionResponse;
    try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
    	System.out.println("responsesCount:"+client.batchAnnotateImages(visionRequests).getResponsesList().size());
    	System.out.println("responsesCount:"+client.batchAnnotateImages(visionRequests).getResponsesCount());
      visionResponse = client.batchAnnotateImages(visionRequests).getResponses(0);
      if (visionResponse == null || !visionResponse.hasFullTextAnnotation()) {
    	  System.out.println(visionResponse.getError().getMessage());
        logger.info(String.format("Image %s contains no text", filename));
        return;
      }

      if (visionResponse.hasError()) {
        // Log error
        logger.log(
            Level.SEVERE, "Error in vision API call: " + visionResponse.getError().getMessage());
        return;
      }
    } catch (IOException e) {
      // Log error (since IOException cannot be thrown by a Cloud Function)
      logger.log(Level.SEVERE, "Error detecting text: " + e.getMessage(), e);
      return;
    }

    String text = visionResponse.getFullTextAnnotation().getText();
    System.out.println(visionResponse.getFullTextAnnotation().getPagesCount());
  //  System.out.println("Extracted text from image: " + text +"----");
   // TesteString.toText(text);
    this.outputOCR=text;
   String[] linhas=StringWorks.paragrafoBrutoParaLinha(text).split("\\r\\n");
   StringBuilder sb= new StringBuilder();
   
   for(int j=0;j<linhas.length;j++) {
	   String linha = linhas[j];
	  
	   System.out.println(linha);
	   if(linha.split(" ").length>10) {
		   paragrafos.add(linha);
	    HttpClient hclient = HttpClient.newBuilder().version(Version.HTTP_2).build();
		 HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.openai.com/v1/engines/text-davinci-002/completions"))
		.header("Content-Type", "application/json")
		.header("Authorization", "Bearer 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")
	   .POST(HttpRequest.BodyPublishers.ofString("{\r\n" + 
	   		"  \"prompt\": \"Crie 4 perguntas discursivas com reposta no próprio texto abaixo e suas respectivas respostas corretas. as perguntas devem estar precedidas de P_ mais o número e as respostas de R_ mais o número escreva em português:\\n\\n"+ linha+"\\n\\n\",\r\n" + 
	   		"  \"temperature\": 0.7,\r\n" + 
	   		"  \"max_tokens\": 2000,\r\n" + 
	   		"  \"top_p\": 1,\r\n" + 
	   		"  \"frequency_penalty\": 1,\r\n" + 
	   		"  \"presence_penalty\": 1\r\n" + 
	   		"}")).build();
	
			try {
				HttpResponse<String> response = hclient.send(request,HttpResponse.BodyHandlers.ofString());
				
				
				Gson gson = new GsonBuilder().create();
				
				ResponseDavid rdavid = gson.fromJson(response.body(), ResponseDavid.class);
				System.out.println("****"+rdavid.getChoices().get(0).getText()+"*****");
				grp_perguntas.add(rdavid.getChoices().get(0).getText());
				listPerguntas.add(rdavid.getChoices().get(0).getText());
				
				
				
				sb.append(response.body()+"\n");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
	   
   }
  //  logger.info("Extracted text from image: " + text);
//gsutil acl ch -g AllUsers:R gs://renatotn7hdocrimage/slide.png
    // Detect language using the Cloud Translation API
  /*  DetectLanguageRequest languageRequest =
        DetectLanguageRequest.newBuilder()
            .setParent(LOCATION_NAME)
            .setMimeType("text/plain")
            .setContent(text)
            .build();
    DetectLanguageResponse languageResponse;
    try (TranslationServiceClient client = TranslationServiceClient.create()) {
      languageResponse = client.detectLanguage(languageRequest);
    } catch (IOException e) {
      // Log error (since IOException cannot be thrown by a function)
      logger.log(Level.SEVERE, "Error detecting language: " + e.getMessage(), e);
      return;
    }

    if (languageResponse.getLanguagesCount() == 0) {
      logger.info("No languages were detected for text: " + text);
      return;
    }

    String languageCode = languageResponse.getLanguages(0).getLanguageCode();
    logger.info(String.format("Detected language %s for file %s", languageCode, filename));

    // Send a Pub/Sub translation request for every language we're going to translate to
    for (String targetLanguage : TO_LANGS) {
      logger.info("Sending translation request for language " + targetLanguage);
      OcrTranslateApiMessage message = new OcrTranslateApiMessage(text, filename, targetLanguage);
      ByteString byteStr = ByteString.copyFrom(message.toPubsubData());
      PubsubMessage pubsubApiMessage = PubsubMessage.newBuilder().setData(byteStr).build();
      try {
        publisher.publish(pubsubApiMessage).get();
      } catch (InterruptedException | ExecutionException e) {
        // Log error
        logger.log(Level.SEVERE, "Error publishing translation request: " + e.getMessage(), e);
        return;
      }
    }*/
  }
  // [END functions_ocr_detect]

  // [START functions_ocr_process]
  // [START functions_ocr_setup]
}
// [END functions_ocr_setup]
// [END functions_ocr_process]
