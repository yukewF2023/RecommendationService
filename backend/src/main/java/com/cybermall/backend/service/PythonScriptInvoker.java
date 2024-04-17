package com.cybermall.backend.service;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class PythonScriptInvoker {

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Long> invokePythonScript(String scriptPath, String jsonInput) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        ProcessBuilder builder = new ProcessBuilder("python3", scriptPath);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            writer.write(jsonInput);
            writer.flush();
        }

        // Capture output from the script
        StringBuilder output = new StringBuilder();
        Callable<String> readOutput = new Callable<String>() {
            public String call() throws IOException {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append(System.lineSeparator());
                    }
                }
                return output.toString();
            }
        };
        
        // Use a separate thread to read output to avoid blocking the current thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(readOutput);

        try {
            // Wait for the script to finish and read its output
            String result = future.get(30, TimeUnit.SECONDS); // Timeout for safety
            List<Long> productIds = objectMapper.readValue(result, new TypeReference<List<Long>>() {});
            return productIds;
        } finally {
            executor.shutdown();
            process.destroy();
        }
    }
}

