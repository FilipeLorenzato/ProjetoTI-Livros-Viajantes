package service;

import com.azure.ai.vision.imageanalysis.ImageAnalysisClient;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClientBuilder;
import com.azure.ai.vision.imageanalysis.models.*;
import com.azure.ai.vision.imageanalysis.models.DetectedTextBlock;
import com.azure.core.credential.AzureKeyCredential;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageAnalysisService {

    public static String searchAndVerifyBook(String base64Image) {
        String endpoint = "Seu Endpoint";
        String key = "Sua Chave";

        try {
            // Verificar e logar a string Base64 recebida
            System.out.println("Base64 recebido: " + base64Image);
            System.out.println("Base64 recebido (primeiros 100 caracteres): " + base64Image.substring(0, 100)); // Log dos primeiros 100 caracteres para evitar excesso de dados

            // Criando o cliente de análise de imagem
            ImageAnalysisClient client = new ImageAnalysisClientBuilder()
                    .endpoint(endpoint)
                    .credential(new AzureKeyCredential(key))
                    .buildClient();

            // Validando a imagem Base64
            if (base64Image == null || base64Image.isEmpty()) {
                return "Imagem Base64 não fornecida ou inválida.";
            }

            // Convertendo Base64 para InputStream
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            InputStream imageStream = new ByteArrayInputStream(decodedBytes);

            // Configurando as opções de análise
            ImageAnalysisOptions options = new ImageAnalysisOptions()
                    .setVisualFeatures(Arrays.asList(VisualFeature.CAPTION, VisualFeature.READ));

            // Analisando a imagem a partir do InputStream
            ImageAnalysisResult result = client.analyzeImageInStream(imageStream, options);

            // Verificando se a análise foi bem-sucedida
            if (result == null || result.getRead().getBlocks().isEmpty()) {
                return "Nenhum texto detectado na imagem.";
            }

            // Processando os resultados de texto extraído
            StringBuilder extractedText = new StringBuilder();
            for (DetectedTextBlock textBlock : result.getRead().getBlocks()) {
                for (DetectedTextLine line : textBlock.getLines()) {
                    extractedText.append(line.getText()).append(" ");
                }
            }

            String text = extractedText.toString().trim();
            if (text.isEmpty()) {
                return "Nenhum texto legível encontrado na imagem.";
            }

            // Verificando se o texto extraído corresponde a algum livro no banco de dados
            if (isBookInDatabase(text)) { // isBookInDatabase seria uma função para verificar no banco
                return "Livro encontrado";
            } else {
                return "Livro não encontrado";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao analisar a imagem: " + e.getMessage();
        }
    }

    private static boolean isBookInDatabase(String text) {

        // A conexão com o banco de dados precisa ser implementada
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Criar conexão com o banco de dados
            connection = DatabaseConnection.getConnection(); // Obtenha a conexão com o banco

            // Consulta SQL para buscar um livro com base no título
            String query = "SELECT * FROM Livro WHERE titulo LIKE ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + text + "%"); // Procurar livros com título contendo o texto extraído da imagem

            // Executar a consulta
            rs = stmt.executeQuery();

            // Verificar se encontrou algum livro
            if (rs.next()) {
                return true; // Livro encontrado
            } else {
                return false; // Livro não encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Se ocorrer erro, assume-se que o livro não foi encontrado
        } finally {
            // Fechar os recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Exemplo de conexão com o banco de dados (ajuste para o seu banco)
    private static class DatabaseConnection {

        public static Connection getConnection() throws SQLException {
            // Aqui você configuraria a string de conexão para o seu banco de dados
            // Exemplo com MySQL (substitua com as suas credenciais)
            String driverName = "org.postgresql.Driver";
            String serverName = "localhost";
            String mydatabase = "postgres";
            int porta = 5432;
            String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
            String username = "ti2cc";
            String password = "ti@cc";
            Connection conn = null;
            return java.sql.DriverManager.getConnection(url, username, password);
        }
    }
}
