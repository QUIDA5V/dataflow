FROM gcr.io/dataflow-templates-base/java21-template-launcher-base:latest

ENV FLEX_TEMPLATE_JAVA_CLASSPATH=/template/*
ENV FLEX_TEMPLATE_JAVA_MAIN_CLASS=com.yp.bcrp.BigQueryToCSV
ENV FLEX_TEMPLATE_JAVA_OPTIONS=-Dfile.encoding=UTF-8base

COPY target/bq-to-csv-dataflow-bundled-1.0.0.jar /template/

ENTRYPOINT ["/opt/google/dataflow/java_template_launcher"]