package com.epam.lab7.v1.documentreader;

import java.io.File;

public interface DocumentService {
    void addDocument(File file);

    String queryDocument(String query);
}
