/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 *
 */

 package org.opensearch.dataprepper.plugins.source.sqs;

 import com.fasterxml.jackson.annotation.JsonProperty;
 import jakarta.validation.constraints.Size;
 import software.amazon.awssdk.arns.Arn;
 import software.amazon.awssdk.regions.Region;

 import java.util.Map;
 import java.util.Optional;

 public class AwsAuthenticationOptions {
     private static final String AWS_IAM_ROLE = "role";
     private static final String AWS_IAM = "iam";

     @JsonProperty("region")
     @Size(min = 1, message = "Region cannot be empty string")
     private String awsRegion;

     @JsonProperty("sts_role_arn")
     @Size(min = 20, max = 2048, message = "awsStsRoleArn length should be between 1 and 2048 characters")
     private String awsStsRoleArn;

     @JsonProperty("sts_external_id")
     @Size(min = 2, max = 1224, message = "awsStsExternalId length should be between 2 and 1224 characters")
     private String awsStsExternalId;

     @JsonProperty("sts_header_overrides")
     @Size(max = 5, message = "sts_header_overrides supports a maximum of 5 headers to override")
     private Map<String, String> awsStsHeaderOverrides;

     void validateStsRoleArn() {
         final Arn arn = getArn();
         if (!AWS_IAM.equals(arn.service())) {
             throw new IllegalArgumentException("sts_role_arn must be an IAM Role");
         }
         final Optional<String> resourceType = arn.resource().resourceType();
         if (resourceType.isEmpty() || !resourceType.get().equals(AWS_IAM_ROLE)) {
             throw new IllegalArgumentException("sts_role_arn must be an IAM Role");
         }
     }

     private Arn getArn() {
         try {
             return Arn.fromString(awsStsRoleArn);
         } catch (final Exception e) {
             throw new IllegalArgumentException(String.format("The value provided for sts_role_arn is not a valid AWS ARN. Provided value: %s", awsStsRoleArn));         }
     }

     public String getAwsStsRoleArn() {
         return awsStsRoleArn;
     }

     public String getAwsStsExternalId() {
         return awsStsExternalId;
     }

     public Region getAwsRegion() {
         return awsRegion != null ? Region.of(awsRegion) : null;
     }

     public Map<String, String> getAwsStsHeaderOverrides() {
         return awsStsHeaderOverrides;
     }
 }