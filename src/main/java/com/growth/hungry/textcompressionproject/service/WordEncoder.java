package com.growth.hungry.textcompressionproject.service;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class WordEncoder {

    private Map<String, Integer> wordToCodeMap;
    private Map<Integer, String> codeToWordMap;
    private int nextCode;

    public WordEncoder() {
        wordToCodeMap = new HashMap<>();
        codeToWordMap = new HashMap<>();
        nextCode = 0;
    }

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
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            Integer code = wordToCodeMap.get(word);
            if (code != null) {
                compressedText.append(code).append(" ");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(compressedText.toString());
        }
    }
}

