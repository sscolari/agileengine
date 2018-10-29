package com.agileengine;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by santiagoscolari on 29/10/18.
 */
public class ButtonFinder {

    private static Logger LOGGER = LoggerFactory.getLogger(ButtonFinder.class);

    private static final String defatultOriginalElementId = "make-everything-ok-button";
    private static String CHARSET_NAME = "utf8";

    private static Integer CSS_SCORE = 12;
    private static Integer TITLE_EXACT_SCORE = 5;
    private static Integer TITLE_IGNORE_CASE_SCORE = 4;
    private static Integer HREF_EXACT_SCORE = 5;
    private static Integer HREF_IGNORE_CASE_SCORE = 4;

    public static void main(String[] args) {

        if (args.length != 2 && args.length != 3) {
            LOGGER.error("Invalid parameters. Use: <input_origin_file_path> <input_other_sample_file_path");
            throw new IllegalArgumentException("Expected: 2. Actual: " + args.length);
        }

        String originalFilePath = args[0];
        String otherFilePath = args[1];
        String originalElementId = defatultOriginalElementId;
        if (args.length == 3) {
            originalElementId = args[3];
        }

        LOGGER.info("Original file path: " + originalFilePath);
        LOGGER.info("Other file path: " + otherFilePath);
        LOGGER.info("ElementId: " + originalElementId);

        Document originalJsoupDocument = getDocument(originalFilePath, "original");
        Map<String, String> attributes = JsoupHelper.getButtonAttributes(originalJsoupDocument, originalElementId);

        Document otherJsoupDocument = getDocument(otherFilePath, "'the other'");
        List<Element> links = getLinks(otherJsoupDocument);

        List<Pair<Element, Integer>> scoresByClass = getCandidatesByClasses(links, attributes);
        List<Pair<Element, Integer>> scoresByTitle = getCandidatesByTitle(links, attributes);
        List<Pair<Element, Integer>> scoresByHref = getCandidatesByHref(links, attributes);

        Map<Element, Integer> scores = new HashMap<>();
        collectScores(scoresByClass, scores);
        collectScores(scoresByTitle, scores);
        collectScores(scoresByHref, scores);

        Element preferedLink = scores.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        LOGGER.info("Best match has the following attributes:");
        preferedLink.attributes().forEach(attrs -> LOGGER.info("[{}]", attrs));

        LOGGER.info("#####  Best match path: {}", preferedLink.cssSelector());

    }

    private static void collectScores(List<Pair<Element, Integer>> currentScores, Map<Element, Integer> scores) {
        currentScores.forEach(score -> {
            Integer prevScore = scores.get(score.getKey());
            if (prevScore == null) {
                scores.put(score.getKey(), score.getValue());
            } else {
                scores.put(score.getKey(), prevScore + score.getValue());
            }
        });
    }

    private static List<Element> getLinks(Document document) {
        return JsoupHelper.findLinks(document);
    }

    private static List<Pair<Element, Integer>> getCandidatesByClasses(List<Element> links, Map<String, String> attributes) {
        String cssClasses = attributes.get("class");
        List<Pair<Element, Integer>> elements = new ArrayList<>();
        if (cssClasses != null) {
            String[] classes = cssClasses.split(" ");
            links.forEach(link -> {
                if (Arrays.stream(classes).allMatch(aClass -> link.hasClass(aClass))) {
                    elements.add(new Pair(link, CSS_SCORE));
                }
            });
        }
        return elements;
    }

    private static List<Pair<Element, Integer>> getCandidatesByTitle(List<Element> links, Map<String, String> attributes) {
        String title = attributes.get("title");
        List<Pair<Element, Integer>> elements = new ArrayList<>();
        if (title != null) {
            links.forEach(link -> {
                if (title.equals(link.attr("title"))) {
                    elements.add(new Pair(link, TITLE_EXACT_SCORE));
                } else {
                    if (title.equals(link.attr("title"))) {
                        elements.add(new Pair(link, TITLE_IGNORE_CASE_SCORE));
                    }
                }
            });
        }
        return elements;
    }

    private static List<Pair<Element, Integer>> getCandidatesByHref(List<Element> links, Map<String, String> attributes) {
        String href = attributes.get("href");
        List<Pair<Element, Integer>> elements = new ArrayList<>();
        if (href != null) {
            links.forEach(link -> {
                if (href.equals(link.attr("href"))) {
                    elements.add(new Pair(link, HREF_EXACT_SCORE));
                } else {
                    if (href.equals(link.attr("title"))) {
                        elements.add(new Pair(link, HREF_IGNORE_CASE_SCORE));
                    }
                }
            });
        }
        return elements;
    }

    private static Document getDocument(String originalFilePath, String description) {
        Document originalJsoupDocument = null;
        File originalFile = new File(originalFilePath);
        try {
            originalJsoupDocument = Jsoup.parse(originalFile, CHARSET_NAME, originalFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", originalFilePath, e);
            throw new RuntimeException("Error reading " + description + " file in path: " + originalFilePath);
        }
        return originalJsoupDocument;
    }

}
