package com.growth.hungry.textcompressionproject.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CompressionService {

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
}
