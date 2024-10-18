FROM registry.access.redhat.com/ubi8/openjdk-21:1.18-3.1705519633
COPY target/keycloak-rest-pep-1.0.jar /deployments
