package com.epam.lab7.v1.documentreader;

import com.epam.lab7.v1.chat.Chat;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.epam.lab7.v1.common.DocumentSearchSystemPrompt.SYSTEM_PROMPT;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final Chat chat;

    @Override
    public void addDocument(File file) {
        String content = this.readDocument(file).content();
        Document document = new Document(content);
        chat.vectorStore().add(List.of(document));
    }

    @Override
    public String queryDocument(String query) {
        String context = getDocuments(query);
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(SYSTEM_PROMPT);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("context", context, "query", query));
        Prompt prompt = new Prompt(List.of(systemMessage));
        return chat.aiClient().prompt(prompt).call().content();
    }

    private String getDocuments(String query) {
        return getContext(chat.vectorStore().similaritySearch(query));
    }

    private String getContext(List<Document> documents) {
        return documents.stream()
                .map(Document::getContent)
                .findFirst()
                .orElse("No documents found");
    }

    @SneakyThrows
    private PdfDocument readDocument(File pdfFile) {
        PDDocument pdDocument = Loader.loadPDF(pdfFile);
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String pdfText = pdfTextStripper.getText(pdDocument);
        return new PdfDocument(pdfText);
    }
}
