package com.growth.hungry.textcompressionproject.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class WordEncoder {

    private Map<String, Integer> wordToCodeMap = new HashMap<>();

    private int nextCode = 0;

    public void encodeWords(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if (!wordToCodeMap.containsKey(word)) {
                wordToCodeMap.put(word, nextCode);
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
            writer.newLine();
            writer.write(compressedText.toString().trim());
        }
    }
}
