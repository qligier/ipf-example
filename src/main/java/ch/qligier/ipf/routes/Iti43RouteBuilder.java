package ch.qligier.ipf.routes;

import lombok.NonNull;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.*;
import org.springframework.stereotype.Component;

/**
 * The Camel route configuration for ITI-43 requests using <a
 * href="http://camel.apache.org/dsl.html">Java DSL</a>.
 *
 * ITI-43: Retrieve Document Set.
 *
 * @author Quentin Ligier
 */
@Component
public class Iti43RouteBuilder extends RouteBuilder {

    /**
     * The Camel scheme, as defined by IPF.
     */
    private static final String CAMEL_SCHEME = "xds-iti43";

    /**
     * The HTTP path.
     */
    private static final String HTTP_PATH = "xds/iti43";

    /**
     * The processor for ITI-43 transactions.
     */
    private final Iti43TransactionProcessor iti43TransactionProcessor;

    /**
     * Constructor. All dependencies will be autowired by Spring but could also be supplied manually.
     *
     * @param iti43TransactionProcessor The processor for ITI-43 transactions.
     */
    public Iti43RouteBuilder(@NonNull final Iti43TransactionProcessor iti43TransactionProcessor) {
        this.iti43TransactionProcessor = iti43TransactionProcessor;
    }

    /**
     * Called on initialization to build the routes using the fluent builder syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {
        // Here we configure the Camel route as an ITI-43 endpoint (before the colon) and we set the HTTP path (after the colon)
        from(String.format("%s:%s", CAMEL_SCHEME, HTTP_PATH)).process(
            (final Exchange exchange) -> {
                log.info("Received an ITI-43 request");

                // The type of the objects to retrieve from the Camel message and to put back as a response depends on the transaction and
                // can be found in IPF's doc:
                // https://oehf.github.io/ipf-docs/docs/ihe/xdsMessageTypes/
                final RetrieveDocumentSet request = exchange.getMessage().getBody(RetrieveDocumentSet.class);
                RetrievedDocumentSet retrievedDocumentSet;
                try {
                    retrievedDocumentSet = iti43TransactionProcessor.process(request);
                } catch (final Exception exception) {
                    log.error("Exception while processing ITI-43 transaction", exception);
                    retrievedDocumentSet = new RetrievedDocumentSet(Status.FAILURE);
                    retrievedDocumentSet.getErrors().add(new ErrorInfo(
                        ErrorCode.REGISTRY_ERROR,
                        "Uncaught exception while processing the transaction",
                        Severity.ERROR,
                        "Iti43RouteBuilder",
                        null
                    ));
                    exchange.getMessage().setBody(retrievedDocumentSet);
                    return;
                }

                // Set the response in the Camel message
                exchange.getMessage().setBody(retrievedDocumentSet);
            }
        );
    }
}
