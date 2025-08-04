package com.yp.bcrp.util;

import org.antlr.v4.runtime.misc.NotNull;
import org.apache.beam.sdk.io.FileBasedSink;
import org.apache.beam.sdk.io.fs.ResolveOptions;
import org.apache.beam.sdk.io.FileBasedSink.FilenamePolicy;
import org.apache.beam.sdk.io.FileBasedSink.OutputFileHints;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.beam.sdk.transforms.windowing.PaneInfo;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;

import org.apache.beam.sdk.io.fs.ResourceId;

import org.apache.beam.sdk.io.Compression;

public class CSVOperations {
    private static final String[] CSV_DELIMITER = {"||",",",";"};
    private static final DateTimeFormatter DDMMYYYY_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");
    private CSVOperations(){}

    public static String decodeBase64(String base64){
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static @UnknownKeyFor @NonNull @Initialized ResourceId headerPathResource
            (String name, String location, LocalDate reportDate){
        String directoryAndSuffix = reportDate.format(DDMMYYYY_FORMATTER);
        String filename = filename(name, directoryAndSuffix,0);
        return shardsPathResource(location, directoryAndSuffix).resolve(filename, ResolveOptions.StandardResolveOptions.RESOLVE_FILE);
    }

    public static @NotNull FileBasedSink.FilenamePolicy shardsFilenamePolicy(String name, String location, LocalDate reportDate, Compression compression){
        String directoryAndSuffix = reportDate.format(DDMMYYYY_FORMATTER);
        return new FilenamePolicy() {
            @Override
            public @UnknownKeyFor @NonNull @Initialized ResourceId windowedFilename(
                    @UnknownKeyFor @Initialized int shardNumber,
                    @UnknownKeyFor @Initialized int numShards,
                    @UnknownKeyFor @NonNull @Initialized BoundedWindow window,
                    @UnknownKeyFor @NonNull @Initialized PaneInfo paneInfo, OutputFileHints outputFileHints)
            {
                String filename = filename(name , directoryAndSuffix, shardNumber + 1)
                        .concat(compression.getSuggestedSuffix());
                return shardsPathResource(location,directoryAndSuffix).resolve(filename, ResolveOptions.StandardResolveOptions.RESOLVE_FILE);
            }

            @Override
            public @Nullable @UnknownKeyFor @Initialized ResourceId unwindowedFilename(
                    @UnknownKeyFor @Initialized int shardNumber,
                    @UnknownKeyFor @Initialized int numShards, OutputFileHints outputFileHints) {
                String filename = filename(name , directoryAndSuffix, shardNumber + 1)
                        .concat(compression.getSuggestedSuffix());
                return shardsPathResource(location,directoryAndSuffix).resolve(filename, ResolveOptions.StandardResolveOptions.RESOLVE_FILE);
            }
        };
    }
    private static @NotNull String filename(String name,String suffix, int shardNumber){
        return "%s_%s_%s.csv".formatted(name,suffix,shardNumber);
    }

    private static @UnknownKeyFor @NotNull @Initialized ResourceId shardsPathResource(String location, String directory){
        String path = "%s/%s/".formatted(location , directory);
        return FileBasedSink.convertToFileResourceIfPossible(path);
    }
    public static @UnknownKeyFor @NotNull @Initialized ResourceId tempPathResource(String location){
        String path = "%s/%temp".formatted(location);
        return FileBasedSink.convertToFileResourceIfPossible(path);
    }

    private static String validateDelimiter(String csvDelimiter){
        String defaultDelimiter = ";";
        for (String delimiter : CSV_DELIMITER) {
            if(Objects.equals(csvDelimiter, delimiter)){
                defaultDelimiter = delimiter;
            }
        }
        return defaultDelimiter;
    }

}
