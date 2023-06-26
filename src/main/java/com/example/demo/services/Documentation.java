package com.example.demo.services;

import com.example.demo.dao.BookRepository;
import com.example.demo.dao.MovieRepository;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        templatePath.close();

        //Read the template and write the new document :
        boolean loop = false;
        StringBuilder tableName = new StringBuilder();
        StringBuilder iteration = new StringBuilder();
        List<String> loopContent = new ArrayList<>();
        List<Integer> loopContentFontSize = new ArrayList<>();
        List<String> loopContentFontFamily = new ArrayList<>();
        List<Boolean> loopContentBold = new ArrayList<>();
        List<UnderlinePatterns> loopContentUnderline = new ArrayList<>();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null && text.contains("foreach ")) {
                    loop = true;
                    tableName = new StringBuilder();
                    iteration = new StringBuilder();
                    boolean iterationStart = false;
                    for (int i = 9; i < text.length() - 1; i++) {
                        if (text.charAt(i) != ' ' && !iterationStart) {
                            tableName.append(text.charAt(i));
                        }
                        else if (text.charAt(i) == ' ') {
                            iterationStart = true;
                        }
                        else if (iterationStart) {
                            iteration.append(text.charAt(i));
                        }
                    }
                    run.setText("@", 0);
                }
                else if (text != null && text.contains("foreachend")) {
                    loop = false;
                    run.setText("", 0);
                    List<Object> tableContent = new ArrayList<>();
                    if (tableName.toString().equals("Movie")) {
                        tableContent.addAll(movieRepository.findAll());
                    }
                    else if (tableName.toString().equals("Book")) {
                        tableContent.addAll(bookRepository.findAll());
                    }
                    for (Object object : tableContent) {
                        for (int i = 0; i < loopContent.size(); i++) {
                            boolean goBack = false;
                            if (loopContent.get(i) != null) {
                                run.setFontSize(loopContentFontSize.get(i));
                                run.setFontFamily(loopContentFontFamily.get(i));
                                run.setBold(loopContentBold.get(i));
                                run.setUnderline(loopContentUnderline.get(i));
                                text = loopContent.get(i);
                                if (text.contains("{") && text.contains("}")) {
                                    Field[] fields = object.getClass().getDeclaredFields();
                                    for (Field field : fields) {
                                        if (text.contains(field.getName())) {
                                            Method[] methods = object.getClass().getDeclaredMethods();
                                            for (Method method : methods) {
                                                if (method.getName().toLowerCase().contains("get" + field.getName().toLowerCase())) {
                                                    int count = 0;
                                                    for (int j = 0; j < text.length(); j++) {
                                                        if (text.charAt(j) == '.') {
                                                            count++;
                                                        }
                                                    }
                                                    if (count == 1)
                                                    {
                                                        text = text.replace("{" + iteration + "." + field.getName() + "}", method.invoke(object).toString());
                                                    }
                                                    else if (count > 1) {
                                                        Object auxiliaryObject = method.invoke(object);
                                                        Field[] auxiliaryObjectFields = auxiliaryObject.getClass().getDeclaredFields();
                                                        for (Field auxiliaryObjectField : auxiliaryObjectFields) {
                                                            if (text.contains(auxiliaryObjectField.getName())) {
                                                                Method[] auxiliaryObjectMethods = auxiliaryObject.getClass().getDeclaredMethods();
                                                                for (Method auxiliaryObjectMethod : auxiliaryObjectMethods) {
                                                                    if (auxiliaryObjectMethod.getName().toLowerCase().contains("get" + auxiliaryObjectField.getName().toLowerCase())) {
                                                                        text = text.replace("{" + iteration + "." + field.getName() + "." + auxiliaryObjectField.getName() + "}", auxiliaryObjectMethod.invoke(auxiliaryObject).toString());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (text.contains(";")) {
                                    text = text.replace(";", "");
                                    goBack = true;
                                }
                                run.setText(text);
                            }
                            if (goBack || loopContent.get(i) == null && object != tableContent.get(tableContent.size() - 1)) {
                                run.addBreak();
                            }
                        }
                    }
                    loopContent.clear();
                    loopContentFontSize.clear();
                    loopContentFontFamily.clear();
                    loopContentBold.clear();
                    loopContentUnderline.clear();
                }
                else if (loop) {
                    loopContent.add(text);
                    loopContentFontSize.add(run.getFontSize());
                    loopContentFontFamily.add(run.getFontFamily());
                    loopContentBold.add(run.isBold());
                    loopContentUnderline.add(run.getUnderline());
                    run.setText("@", 0);
                }
            }
        }

        //Delete codes in the template :
        for (int i = 0; i < document.getParagraphs().size(); i++) {
            XWPFParagraph paragraph = document.getParagraphs().get(i);
            String text = paragraph.getText();
            if (text.contains("@")) {
                document.removeBodyElement(i);
                i = 0;
            }
        }

        FileOutputStream documentPath = new FileOutputStream("src\\main\\java\\com\\example\\demo\\doc\\tablesDoc.docx");
        document.write(documentPath);
        documentPath.close();
        System.out.println("Word document has been created.");
    }

    public void createDocument2() throws ClassNotFoundException {
        Class c = Class.forName("com.example.demo.data.Movie");
        for (Method m : c.getDeclaredMethods()) {
            System.out.println(m.getName());
        }
    }
}
