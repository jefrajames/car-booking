package io.jefrajames.booking;

import java.util.List;
import java.util.function.Supplier;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DocRagAugmentor implements Supplier<RetrievalAugmentor> {

    @Inject
    EmbeddingStore<TextSegment> store;

    @Inject
    EmbeddingModel model;

    @Override
    public RetrievalAugmentor get() {
        return DefaultRetrievalAugmentor
                .builder()
                .contentRetriever(new MyDocRetriever(store, model))
                .build();
    }

    static class MyDocRetriever implements ContentRetriever {

        EmbeddingStoreContentRetriever retriever;

        private static final int MAX_RESULTS = 20;
        private static final double MIN_SCORE = 0.6; // From 0 (low selectivity) to 1 (high selectivity)

        MyDocRetriever(EmbeddingStore<TextSegment> store, EmbeddingModel model) {
            this.retriever = EmbeddingStoreContentRetriever
                    .builder()
                    .embeddingModel(model)
                    .embeddingStore(store)
                    .maxResults(MAX_RESULTS)
                    .minScore(MIN_SCORE)
                    .build();
        }

        @Override
        public List<Content> retrieve(Query query) {
            List<Content> contents = retriever.retrieve(query);
            // Uncomment to see the retrieved contents
            // Log.debug(String.format("DEMO: Found %d relevant contents for query <%s>", contents.size(), query.text()));
            // contents.forEach(c -> {
            //     Log.debug(String.format("DEMO: content from file_name %s is <<%s>>",
            //             c.textSegment().metadata("file_name"), c.textSegment().text()));
            // });
            return contents;
        }

    }

}
