package com.agileengine;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by santiagoscolari on 29/10/18.
 */
public class JsoupHelper {

    private static Logger LOGGER = LoggerFactory.getLogger(JsoupHelper.class);

    public static Optional<Element> getElementById(Document doc, String id) {
        return Optional.of(doc.getElementById(id));
    }

    public static Optional<Elements> findElementsByCssQuery(Document doc, String cssQuery) {
        return Optional.of(doc.select(cssQuery));
    }

    public static Elements findLinks(Document doc) {
        return doc.select("a");
    }

    public static Map<String, String> getButtonAttributes(Document doc, String originalElementId) {
        Optional<Element> originalButtonOpt = JsoupHelper.getElementById(doc, originalElementId);
        Map<String, String> attributes =
        originalButtonOpt.map(button ->
                button.attributes().asList().stream()
                        .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue))).get();

        return attributes;
//        Optional<String> stringifiedAttributesOpt = originalButtonOpt.map(button ->
//                button.attributes().asList().stream()
//                        .map(attr -> attr.getKey() + " = " + attr.getValue())
//                        .collect(Collectors.joining(", "))
//        );

    }

}
