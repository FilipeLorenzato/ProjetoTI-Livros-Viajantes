package service;

import java.util.Arrays;

import com.azure.ai.vision.imageanalysis.ImageAnalysisClient;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClientBuilder;
import com.azure.ai.vision.imageanalysis.models.DetectedTextLine;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisOptions;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisResult;
import com.azure.ai.vision.imageanalysis.models.VisualFeatures;
import com.azure.core.credential.AzureKeyCredential;

public class OCRService {

    public static String searchText(String url) {
        String endpoint = System.getenv("VISION_ENDPOINT");
        String key = System.getenv("VISION_KEY");

        if (endpoint == null || key == null) {
            System.out.println(endpoint);
            System.out.println(key);

            System.out.println("As variáveis de ambiente VISION_ENDPOINT e VISION_KEY não foram configuradas.");
            System.out.println("Para configurar, execute os comandos:");
            System.out.println("export VISION_ENDPOINT=<ENDPOINT>");
            System.out.println("export VISION_KEY=<CHAVE>");

            System.exit(1);
        }

        // Create a synchronous Image Analysis client.
        ImageAnalysisClient client = new ImageAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .buildClient();

        // This is a synchronous (blocking) call.
        ImageAnalysisResult result = client.analyzeFromUrl(
                url,
                Arrays.asList(VisualFeatures.CAPTION, VisualFeatures.READ),
                new ImageAnalysisOptions().setGenderNeutralCaption(true));

        String textResult = "";
        for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
            textResult += line.getText() + "\n";
        }

        return textResult;
    }
}
