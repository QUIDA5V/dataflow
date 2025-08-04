package com.yp.bcrp;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.Validation.Required;

public interface BigQueryToCSVOptions extends GcpOptions{

    @Description("Big Query KMS Key")
    @Required
    String getBqKmsKey();

    void setBqKmsKey(String value);

    @Description("Big Query DataSet")
    @Required
    String getBqDataset();

    void setBqDataset(String value);

    @Description("Big Query Table")
    @Required
    String getBqTable();

    void setBqTable(String value);

    @Description("fields to extract from BQ")
    @Required
    String getBqFields();

    void setBqFields(String value);

    @Description("CSV Report Name")
    @Required
    String getCsvName();

    void setCsvName(String value);

    @Description("CSV Header")
    @Required
    String getCsvHeaderB64();

    void setCsvHeaderB64(String value);

    @Description("CSV Location")
    @Required
    String getCsvLocation();

    void setCsvLocation(String value);

    @Description("CSV Delimiter")
    @Required
    String getCsvDelimiter();

    void setCsvDelimiter(String value);

    @Description("Record to generate the csv file")
    @Required
    String getReportDate();

    void setReportDate(String value);

    @Description("Number of Shards")
    @Required
    Integer getNumShards();

    void setNumShards(Integer value);

    @Description("BigQuery dataset")
    @Required
    String getTempDataset();

    void setTempDataset(String value);
}
