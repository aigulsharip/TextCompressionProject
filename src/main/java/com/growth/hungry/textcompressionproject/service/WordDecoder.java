package com.growth.hungry.textcompressionproject.service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WordDecoder {
    private Map<Integer, String> codeToWordMap = new HashMap<>();

    public void loadCodeToWordMap(String compressedFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(compressedFile))) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                String[] parts = line.split("-", 2);
                int code = Integer.parseInt(parts[0]);
                String word = parts[1].trim();
                codeToWordMap.put(code, word);
            }
        }
    }

    public void decodeText(String compressedFile, String outputFile) throws IOException {
        StringBuilder decompressedText = new StringBuilder();
        boolean isCodeMappingsRead = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(compressedFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!isCodeMappingsRead && line.isEmpty()) {
                    isCodeMappingsRead = true;
                    continue;
                }
                if (!isCodeMappingsRead) {
                    continue;
                }
                String[] codes = line.split(" ");
                for (String code : codes) {
                    decompressedText.append(codeToWordMap.get(Integer.parseInt(code))).append(" ");
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(decompressedText.toString());
        }
    }

}
