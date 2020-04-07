# IPF example application

This is a simple IPF example application. It does not use any Groovy code or XML configuration, everything is done in Java code.

It exposes a single ITI-43 endpoint that always returns the same document as an example.

## Build the WAR

1. Install the dependencies: `mvn install`
2. Build the WAR: `mvn clean package`
3. The WAR is created in _target/ipf-example.war_ and ready to be deployed to an Apache Tomcat by example.

## Run the JAR

1. Install the dependencies: `mvn install`
2. Either run it from your favorite IDE or in the CLI: `mvn clean compile && mvn exec:java -Dexec.mainClass="ch.qligier.ipf.IpfApplication"`

The IDE configuration is the one of a regular Spring Boot application. In IntelliJ IDEA by example, there is a Spring Boot template in which
you just need to specify the main class (_ch.qligier.ipf.IpfApplication_) to have it working.

## Test the ITI-43 transaction

The endpoint is accessible at the address _host:8080/services/xds/iti43_. The port and the path prefix are configured in the _application
.yml_, the path suffix is defined in _Iti43RouteBuilder.java_.

A sample ITI-43 request could look like this:
```xml
<?xml version='1.0' encoding='UTF-8'?>
<soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
    xmlns:wsa="http://www.w3.org/2005/08/addressing">
    <soapenv:Header>
        <wsa:Action>urn:ihe:iti:2007:RetrieveDocumentSet</wsa:Action>
        <wsa:To soapenv:mustUnderstand="1">host:8080/services/xds/iti43</wsa:To>
        <wsa:MessageID soapenv:mustUnderstand="1">urn:uuid:3448B7F8EA6E8B9DFC1289514997508</wsa:MessageID>
    </soapenv:Header>
    <soapenv:Body>
        <RetrieveDocumentSetRequest xmlns="urn:ihe:iti:xds-b:2007"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <DocumentRequest>
                <RepositoryUniqueId>1.19.6.24.109.42.1.5</RepositoryUniqueId>
                <DocumentUniqueId>1.2.820.99999.174003127040043086.1572886054.1</DocumentUniqueId>
            </DocumentRequest>
        </RetrieveDocumentSetRequest>
    </soapenv:Body>
</soapenv:Envelope>
```
