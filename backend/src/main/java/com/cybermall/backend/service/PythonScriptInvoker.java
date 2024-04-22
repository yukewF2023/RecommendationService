package com.cybermall.backend.service;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * This class is responsible for invoking a Python script and capturing its output.
 * It provides a method to invoke a Python script with a given script path and JSON input,
 * and returns a list of product IDs as the result.
 */
public class PythonScriptInvoker {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Invokes a Python script with the given script path and JSON input.
     *
     * @param scriptPath The path to the Python script to invoke.
     * @param jsonInput The JSON input to pass to the script.
     * @return A list of product IDs as the result of the script.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the current thread is interrupted.
     * @throws ExecutionException If an exception occurs during script execution.
     * @throws TimeoutException If the script execution times out.
     */
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

