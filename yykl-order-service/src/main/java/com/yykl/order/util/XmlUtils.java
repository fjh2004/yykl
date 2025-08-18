package com.yykl.order.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class XmlUtils {
    public static Map<String, String> xmlToMap(String xml) {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(xml)));
            
            Element root = doc.getDocumentElement();
            Map<String, String> map = new HashMap<>();
            NodeList nodes = root.getChildNodes();
            
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    map.put(node.getNodeName(), node.getTextContent());
                }
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException("XML解析异常", e);
        }
    }

    public static String mapToXml(Map<String, String> map) {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .newDocument();
            
            Element root = doc.createElement("xml");
            doc.appendChild(root);
            
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Element element = doc.createElement(entry.getKey());
                element.appendChild(doc.createTextNode(entry.getValue()));
                root.appendChild(element);
            }
            
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("XML生成异常", e);
        }
    }
}