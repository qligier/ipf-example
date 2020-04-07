package ch.qligier.ipf.routes;

import com.sun.istack.ByteArrayDataSource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;


/**
 * The example processor of ITI-43 transactions: Retrieve Document Set.
 *
 * @author Quentin Ligier
 */
@Component
@Slf4j
public class Iti43TransactionProcessor {

    /**
     * Processes the ITI-43 transaction.
     *
     * @param request The ITI-43 request.
     * @return The processing result as a set of retrieved results.
     */
    RetrievedDocumentSet process(@NonNull final RetrieveDocumentSet request) {
        for (final DocumentReference documentReference : request.getDocuments()) {
            log.info("Requested document reference: {}", documentReference);
        }

        // Create a fake document
        final RetrievedDocument retrievedDocument = new RetrievedDocument();
        retrievedDocument.setMimeType("text/xml");
        retrievedDocument.setRequestData(request.getDocuments().get(0));
        final DataSource dataSource = new ByteArrayDataSource(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Document></Document>".getBytes(),
            "text/xml; charset=UTF-8"
        );
        retrievedDocument.setDataHandler(new DataHandler(dataSource));

        // Create a successful response
        final RetrievedDocumentSet response = new RetrievedDocumentSet();
        response.setStatus(Status.SUCCESS);
        response.getDocuments().add(retrievedDocument);

        return response;
    }
}
