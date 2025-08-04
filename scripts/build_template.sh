
set -e
set -o pipefail
set -u

echo "##### Creating Flex Template Docker image and spec file in GCS"

gcloud dataflow flex-template build "gs://$_BUCKET/templates/csv-report-generator/$_ENV.json" \
  --image-gcr-path "$LOCATION-docker.pkg.dev/$PROJECT_ID/$_ARTIFACT_REPO/$_IMAGE_NAME/$_ENV:$SHORT_SHA" \
  --sdk-language "JAVA" \
  --cloud-build-service-account="projects/$PROJECT_ID/serviceAccounts/$_BUILD_SA" \
  --flex-template-base-image "JAVA21" \
  --metadata-file "metadata.json" \
  --jar "target/bcrp-csv-dataflow-1.0-SNAPSHOT.jar" \
  --env FLEX_TEMPLATE_JAVA_MAIN_CLASS="com.yp.bcrp.BigQueryToCSV" \
  --env FLEX_TEMPLATE_JAVA_OPTIONS="-Dfile.enconding=UTF-8" \
  --service-account-email="bcrp-csv-generation-trigger@$PROJECT_ID.iam.gserviceaccount.com" \
  --disable-public-ips \
  --gcs-log-dir="gs://$_BUCKET/logs/"
