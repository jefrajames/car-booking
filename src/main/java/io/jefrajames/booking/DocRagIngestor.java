package io.jefrajames.booking;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

import java.io.File;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class DocRagIngestor {

    @ConfigProperty(name = "app.docs-for-rag.dir")
    File docs;

    // To be injected by DocRetriever
    @Produces
    EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    @Inject
    EmbeddingModel embeddingModel;

    private List<Document> loadDocs() {
        return loadDocuments(docs.getPath(), new TextDocumentParser());
    }

    public void ingest(@Observes StartupEvent evt) throws Exception {

        long start = System.currentTimeMillis();

        // Warning: maxSegmenSizeInChars should be <= embedding model dimensions (384)
        DocumentSplitter splitter = DocumentSplitters.recursive(384, 0);

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .documentSplitter(splitter)
                .build();

        List<Document> docs = loadDocs();
        ingestor.ingest(docs);
        Log.debug(String.format("DEMO %d documents ingested in %d msec", docs.size(),
                System.currentTimeMillis() - start));
        return;
    }

}
