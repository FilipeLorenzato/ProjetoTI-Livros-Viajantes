package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64ToImageConverterService {

    // Método para converter Base64 para arquivo de imagem
    public static String convertBase64ToImage(String base64String, String outputPath) {
        if (base64String == null || base64String.isEmpty()) {
            return "A string Base64 fornecida está vazia ou nula.";
        }

        try {
            // Decodificar a string Base64
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);

            // Verificar se o diretório de saída existe, caso contrário, criar
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                boolean created = parentDir.mkdirs();  // Cria o diretório, se não existir
                if (!created) {
                    System.err.println("Falha ao criar diretório: " + parentDir.getAbsolutePath());
                    return "Erro ao criar diretório.";
                }
            }

            // Criar um arquivo de imagem a partir dos bytes decodificados
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(decodedBytes);
                fos.flush();
            }

            return "Imagem salva com sucesso em: " + outputPath;
        } catch (IOException e) {
            System.err.println("Erro ao converter Base64 para imagem no caminho " + outputPath + ": " + e.getMessage());
            return "Erro ao converter Base64 para imagem: " + e.getMessage();
        }
    }
}
