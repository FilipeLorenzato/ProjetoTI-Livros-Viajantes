package service;

import java.util.Arrays;

import com.azure.ai.vision.imageanalysis.ImageAnalysisClient;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClientBuilder;
import com.azure.ai.vision.imageanalysis.models.DetectedTextLine;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisOptions;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisResult;
import com.azure.ai.vision.imageanalysis.models.VisualFeatures;
import com.azure.core.credential.AzureKeyCredential;

import dao.LivroDAO;
import model.Livro;

public class ImageAnalysisService {

    public static String searchAndVerifyBook(String url) {
        String endpoint = System.getenv("VISION_ENDPOINT");
        String key = System.getenv("VISION_KEY");

        if (endpoint == null || key == null) {
            throw new IllegalStateException("As variáveis de ambiente VISION_ENDPOINT e VISION_KEY não foram configuradas.");
        }

        ImageAnalysisClient client = new ImageAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .buildClient();

        ImageAnalysisResult result = client.analyzeFromUrl(
                url,
                Arrays.asList(VisualFeatures.READ),
                new ImageAnalysisOptions().setGenderNeutralCaption(true));

        if (result.getRead() == null || result.getRead().getBlocks().isEmpty()) {
            return "Nenhum texto detectado na imagem.";
        }

        StringBuilder textResult = new StringBuilder();
        for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
            textResult.append(line.getText()).append(" ");
        }

        String extractedText = textResult.toString().trim();
        LivroDAO livroDAO = new LivroDAO();

        // Verificar no banco de dados pelo título do livro extraído
        Livro livro = livroDAO.buscarPorTitulo(extractedText);

        if (livro != null) {
            return "Livro encontrado: " + livro.getTitulo() + " - Autor: " + livro.getAutor();
        } else {
            return "Livro não encontrado no banco de dados.";
        }
    }
}

/*package service;

import java.util.Arrays;

import com.azure.ai.vision.imageanalysis.ImageAnalysisClient;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClientBuilder;
import com.azure.ai.vision.imageanalysis.models.DetectedTextLine;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisOptions;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisResult;
import com.azure.ai.vision.imageanalysis.models.VisualFeatures;
import com.azure.core.credential.AzureKeyCredential;

public class ImageAnalysisQuickStart {

    public static String searchText(String url) {
        String endpoint = "";
        String key = "";

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


        // Create a synchronous Image Analysis client.
        ImageAnalysisClient client = new ImageAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .buildClient();

        // This is a synchronous (blocking) call.
        ImageAnalysisResult result = client.analyzeFromUrl(
                "https://learn.microsoft.com/azure/ai-services/computer-vision/media/quickstarts/presentation.png",
                Arrays.asList(VisualFeatures.CAPTION, VisualFeatures.READ),
                new ImageAnalysisOptions().setGenderNeutralCaption(true));

        // Print analysis results to the console
        System.out.println("Image analysis results:");
        System.out.println(" Caption:");
        System.out.println("   \"" + result.getCaption().getText() + "\", Confidence "
                + String.format("%.4f", result.getCaption().getConfidence()));
        System.out.println(" Read:");
        for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
            System.out.println("   Line: '" + line.getText()
                    + "', Bounding polygon " + line.getBoundingPolygon());
            for (DetectedTextWord word : line.getWords()) {
                System.out.println("     Word: '" + word.getText()
                        + "', Bounding polygon " + word.getBoundingPolygon()
                        + ", Confidence " + String.format("%.4f", word.getConfidence()));
            }
        }
    }
}*/
