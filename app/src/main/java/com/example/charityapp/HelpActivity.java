package com.example.charityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

/**
 * @description This is the class where it takes users who click on the help button. This class
 * will open a pdf file for them to view which is the User Manual.
 *
 * @authors Felix Estrella and Jack Bates
 * @date_created 04/28/20
 * @date_modified 04/29/20
 */

public class HelpActivity extends AppCompatActivity {

    PDFView mPDFView;  // Declares a PDF View so we can view the PDF
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the view to that of activity_help
        setContentView(R.layout.activity_help);

        // Instatiates the PDFView so it can show
        mPDFView = (PDFView) findViewById(R.id.pdfView);
        // Load the pdf file so it can be viewed from the assets folder
        mPDFView.fromAsset("userManual.pdf").load();
    }
}
