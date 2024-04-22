package com.growth.hungry.textcompressionproject.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Service
public class CompressionService {

    private Map<String, Integer> wordToCodeMap;
    private Map<Integer, String> codeToWordMap;
    private int nextCode;

    public CompressionService() {
        wordToCodeMap = new HashMap<>();
        codeToWordMap = new HashMap<>();
        nextCode = 0;
    }

    public void compressText(String text) throws IOException {
        WordEncoder encoder = new WordEncoder();
        encoder.encodeWords(text);
        encoder.compressText(text, "output.sc");
    }

    public void decompressText(String compressedFile, String outputFile) throws IOException {
        WordDecoder decoder = new WordDecoder();
        decoder.loadCodeToWordMap(compressedFile);
        decoder.decodeText(compressedFile, outputFile);
    }

    private class WordEncoder {

        public void encodeWords(String text) {
            StringTokenizer tokenizer = new StringTokenizer(text);
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                if (!wordToCodeMap.containsKey(word)) {
                    wordToCodeMap.put(word, nextCode);
                    codeToWordMap.put(nextCode, word);
                    nextCode++;
                }
            }
        }

        public void compressText(String text, String outputFile) throws IOException {
            StringBuilder compressedText = new StringBuilder();
            StringBuilder codeMappings = new StringBuilder();
            StringTokenizer tokenizer = new StringTokenizer(text);
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                Integer code = wordToCodeMap.get(word);
                if (code != null) {
                    compressedText.append(code).append(" ");
                    codeMappings.append(code).append("-").append(word).append("\n");
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(codeMappings.toString());
                writer.newLine(); // Add newline separator
                writer.write(compressedText.toString().trim()); // Write compressed codes
            }
        }
    }

    private class WordDecoder {

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
                        continue; // Skip empty line
                    }
                    if (!isCodeMappingsRead) {
                        continue; // Skip code mappings
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

    public static void main(String[] args) {
        try {
            String text = "This is a sample text. This text will be encoded.";

            CompressionService compressionService = new CompressionService();
            compressionService.compressText(text);
            System.out.println("Text compressed successfully.");

            compressionService.decompressText("output.sc", "readable.txt");
            System.out.println("Text decompressed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}
