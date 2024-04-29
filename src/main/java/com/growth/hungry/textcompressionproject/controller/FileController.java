package com.growth.hungry.textcompressionproject.controller;

import com.growth.hungry.textcompressionproject.service.CompressionService;
import com.growth.hungry.textcompressionproject.service.FileReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    private FileReadingService fileReadingService;

    @Autowired
    private CompressionService compressionService;

    @GetMapping("/readFile")
    public String readFile(@RequestParam String fileName) throws IOException {
        return fileReadingService.readFileFromResources(fileName);
    }

    @GetMapping("/compress")
    public String compressText() {
        try {
            String text = fileReadingService.readFileFromResources("input.txt");
            compressionService.compressText(text);
            return "Text compressed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error compressing text: " + e.getMessage();
        }
    }

    @GetMapping("/decompress")
    public String decompressText() {
        try {
            compressionService.decompressText("output.sc", "readable.txt");
            return "Text decompressed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error decompressing text: " + e.getMessage();
        }
    }

}
