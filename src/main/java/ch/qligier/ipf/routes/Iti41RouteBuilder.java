package ch.qligier.ipf.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.*;
import org.springframework.stereotype.Component;

/**
 * The Camel route configuration for ITI-41 requests using <a
 * href="http://camel.apache.org/dsl.html">Java DSL</a>.
 *
 * XDS ITI-41: Provide and Register Document Set-b.
 *
 * @author Quentin Ligier
 */
@Component
public class Iti41RouteBuilder extends RouteBuilder {

    /**
     * The Camel scheme, as defined by IPF.
     */
    private static final String CAMEL_SCHEME = "xds-iti41";

    /**
     * The HTTP path.
     */
    private static final String HTTP_PATH = "xds/iti41";

    /**
     * Called on initialization to build the routes using the fluent builder syntax.
     */
    @Override
    public void configure() {
        onException(Exception.class)
            .log("ITI-41 route: caught Exception: ${exception.message}")
            .stop();

        from(String.format("%s:%s", CAMEL_SCHEME, HTTP_PATH))
            .routeId("xds-iti41-consumer")
            .process((final Exchange exchange) -> {
                final ProvideAndRegisterDocumentSet request = exchange.getMessage().getBody(ProvideAndRegisterDocumentSet.class);
                log.info("Received an ITI-41 request:");
                log.info(request.toString());

                // Always report success
                final Response response = new Response(Status.SUCCESS);
                response.getErrors().add(new ErrorInfo(ErrorCode.REGISTRY_ERROR, "An exemple warning", Severity.WARNING, "Iti41RouteBuilder", null));
                exchange.getMessage().setBody(response);
            });
    }
}
