package ch.qligier.ipf.routes;

import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;


/**
 * The example processor of ITI-43 transactions: Retrieve Document Set.
 *
 * See <a href="https://oehf.github.io/ipf-docs/docs/ihe/iti43/">xds-iti43 component</a>
 *
 * @author Quentin Ligier
 */
@Component
public class Iti43TransactionProcessor {
    private static final Logger log = LoggerFactory.getLogger(Iti43TransactionProcessor.class);

    /**
     * Processes the ITI-43 transaction.
     *
     * @param request The ITI-43 request.
     * @return The processing result as a set of retrieved results.
     */
    RetrievedDocumentSet process(final RetrieveDocumentSet request) {
        for (final DocumentReference documentReference : request.getDocuments()) {
            log.info("Requested document reference: {}", documentReference);
        }

        // Create a fake document
        final RetrievedDocument retrievedDocument = new RetrievedDocument();
        retrievedDocument.setMimeType("text/xml");
        retrievedDocument.setRequestData(request.getDocuments().get(0));
        retrievedDocument.setDataHandler(new DataHandler(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Document></Document>",
            "text/xml; charset=UTF-8"
        ));

        // Create a successful response
        final RetrievedDocumentSet response = new RetrievedDocumentSet();
        response.setStatus(Status.SUCCESS);
        response.getDocuments().add(retrievedDocument);

        return response;
    }
}
