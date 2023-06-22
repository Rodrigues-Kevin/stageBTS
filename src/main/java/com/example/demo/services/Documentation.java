package com.example.demo.services;

import com.example.demo.dao.BookRepository;
import com.example.demo.dao.MovieRepository;
import com.example.demo.data.Book;
import com.example.demo.data.Movie;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class Documentation {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private BookRepository bookRepository;

    public void createDocument() throws Exception
    {
        FileInputStream templatePath = new FileInputStream("src\\main\\java\\com\\example\\demo\\templates\\tablesTemplate.docx");
        XWPFDocument document = new XWPFDocument(templatePath);
        List<Movie> movies = movieRepository.findAll();
        List<Book> books = bookRepository.findAll();

        //Read the template :
        int titleSize = 0;
        int tableTitleSize = 0;
        int propertiesSize = 0;
        int tablesSpacesNumber = 0;
        boolean movieLoop = false;
        boolean bookLoop = false;
        List<String> movieLoopData = new ArrayList<>();
        List<String> bookLoopData = new ArrayList<>();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if (text != null && text.contains("{foreach Movie m}")) {
                    movieLoop = true;
                    text = text.replace("{foreach Movie m}", "XXX");
                    run.setText(text, 0);
                }
                else if (text != null && text.contains("{foreach Book b}")) {
                    bookLoop = true;
                    text = text.replace("{foreach Book b}", "XXX");
                    run.setText(text, 0);
                }
                else if (text != null && text.contains("{foreachend}")) {
                    movieLoop = false;
                    bookLoop = false;
                    text = text.replace("{foreachend}", "XXX");
                    run.setText(text, 0);
                }
                else if (text != null && text.contains("Movie") && text.contains(":")) {
                    tableTitleSize = run.getFontSize();
                }
                else if (text != null && movieLoop) {
                    if (text.contains("{m.name}")) {
                        titleSize = run.getFontSize();
                    }
                    else {
                        propertiesSize = run.getFontSize();
                    }
                    movieLoopData.add(text);
                    text = text.replace(text, "XXX");
                    run.setText(text, 0);
                }
                else if (text != null && bookLoop) {
                    bookLoopData.add(text);
                    text = text.replace(text, "XXX");
                    run.setText(text, 0);
                }
                else if (text == null && !movieLoopData.isEmpty()) {
                    tablesSpacesNumber++;
                }
            }
        }

        //Delete variables and spaces in the template :
        for (int i = 0; i < document.getParagraphs().size(); i++) {
            XWPFParagraph paragraph = document.getParagraphs().get(i);
            String text = paragraph.getText();
            if (text.contains("XXX") || text.contains("Movie") && text.contains(":") || text.contains("Book") && text.contains(":")) {
                document.removeBodyElement(i);
                i = 0;
            }
        }
        for (int i = 0; i < tablesSpacesNumber; i++) {
            document.removeBodyElement(document.getParagraphs().size() - 1);
        }

        //Write the documentation for the Movie table :
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setFontSize(tableTitleSize);
        run.setBold(true);
        run.setUnderline(UnderlinePatterns.SINGLE);
        run.setText("Movie :");
        run.addBreak();
        for (Movie movie : movies) {
            paragraph = document.createParagraph();
            for (String element : movieLoopData) {
                if (element.contains("{m.name}")) {
                    run = paragraph.createRun();
                    run.setFontSize(titleSize);
                    run.setBold(true);
                    run.setUnderline(UnderlinePatterns.SINGLE);
                    run.setText(element.replace("{m.name}", movie.getName()));
                    run.addBreak();
                }
                else if (element.contains("{m.description}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setText(element.replace("{m.description}", movie.getDescription()));
                    run.addBreak();
                }
                else if (element.contains("{m.duration}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setText(element.replace("{m.duration}", movie.getDuration().toString()));
                    run.addBreak();
                }
                else if (element.contains("{m.releaseDate}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setText(element.replace("{m.releaseDate}", movie.getReleaseDate().toString()));
                    run.addBreak();
                }
                else if (element.contains("{m.director.firstName}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    element = element.replace("{m.director.firstName}", movie.getDirector().getFirstName());
                    String text = element.replace("{m.director.lastName}", movie.getDirector().getLastName());
                    run.setText(text);
                    run.addBreak();
                }
                else if (element.contains("{m.genre.name}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setText(element.replace("{m.genre.name}", movie.getGenre().getName()));
                    run.addBreak();
                }
                else {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setUnderline(UnderlinePatterns.SINGLE);
                    run.setText(element);
                }
            }
        }

        //Create spaces between the two documentations :
        if (tablesSpacesNumber != 0) {
            run.setFontSize(propertiesSize);
            for (int i = 0; i < tablesSpacesNumber - 1; i++) {
                run.addBreak();
            }
        }

        //Write the documentation for the Book table :
        paragraph = document.createParagraph();
        run = paragraph.createRun();
        run.setFontSize(tableTitleSize);
        run.setBold(true);
        run.setUnderline(UnderlinePatterns.SINGLE);
        run.setText("Book :");
        run.addBreak();
        for (Book book : books) {
            paragraph = document.createParagraph();
            for (String element : bookLoopData) {
                if (element.contains("{b.name}")) {
                    run = paragraph.createRun();
                    run.setFontSize(titleSize);
                    run.setBold(true);
                    run.setUnderline(UnderlinePatterns.SINGLE);
                    run.setText(element.replace("{b.name}", book.getName()));
                    run.addBreak();
                }
                else if (element.contains("{b.description}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setText(element.replace("{b.description}", book.getDescription()));
                    run.addBreak();
                }
                else if (element.contains("{b.releaseDate}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setText(element.replace("{b.releaseDate}", book.getReleaseDate().toString()));
                    run.addBreak();
                }
                else if (element.contains("{b.author.firstName}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    element = element.replace("{b.author.firstName}", book.getAuthor().getFirstName());
                    String text = element.replace("{b.author.lastName}", book.getAuthor().getLastName());
                    run.setText(text);
                    run.addBreak();
                }
                else if (element.contains("{b.genre.name}")) {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setText(element.replace("{b.genre.name}", book.getGenre().getName()));
                    run.addBreak();
                }
                else {
                    run = paragraph.createRun();
                    run.setFontSize(propertiesSize);
                    run.setUnderline(UnderlinePatterns.SINGLE);
                    run.setText(element);
                }
            }
        }

        FileOutputStream documentPath = new FileOutputStream("src\\main\\java\\com\\example\\demo\\doc\\tablesDoc.docx");
        document.write(documentPath);
        documentPath.close();
        System.out.println("Word document has been created.");
    }
}
