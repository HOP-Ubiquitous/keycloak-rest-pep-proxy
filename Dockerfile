FROM registry.access.redhat.com/ubi8/openjdk-11:1.12
COPY target/keycloak-rest-pep-1.0.jar /deployments
