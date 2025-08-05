package com.yp.bcrp;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.yp.bcrp.util.CSVOperations.*;
import static org.apache.beam.sdk.io.TextIO.write;

public class BigQueryToCSV {

    public static void main(String[] args) {
        System.out.println("*********");
        try {
            Compression compression = Compression.GZIP;
        BigQueryToCSVOptions options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(BigQueryToCSVOptions.class);

            System.out.println("*********1");
        Pipeline pipeline = Pipeline.create(options);
            System.out.println("*********2");
        PCollection<String> csv = pipeline.apply(Create.of("apple", "banana", "cherry"));
        csv.apply("WriteToGCS", write()
                        .to(shardsFilenamePolicy(options.getCsvName(),
                                options.getCsvLocation(),
                                LocalDate.parse(options.getReportDate()),
                                compression))
                .withTempDirectory(tempPathResource(options.getCsvLocation()))
                .withNumShards(Optional.ofNullable(options.getNumShards()).orElse(10))
                .withCompression(compression));

        //pipeline.run();
        pipeline.run().waitUntilFinish();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
