package com.gydz.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * xml������
 */
public class XmlTool {
	
	public static void main(String[] args) throws Exception {
        String xmlStr= readFile("F:/text.xml");
        JSONObject json = documentToJSONObject(xmlStr.replaceAll("name=\"\" show=\"", "showname=\"")
        		.replaceAll("name=\"fake-field-wrapper\"", "showname=\"fake-field-wrapper\"")
        		.replaceAll("field name=\"data\"", "field showname=\"data\""));
        System.out.println("xmlStr:"+xmlStr.replaceAll("name=\"\" show=\"", "showname=\""));
        String jsonStr = json.toJSONString();//.replace("\"", "'");
        System.out.println("xml2Json:"+jsonStr.substring(jsonStr.indexOf("{\"proto\":[")+9,jsonStr.indexOf("}]}]")+2));
    }

    public static String readFile(String path) throws Exception {
        File file=new File(path);
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = ByteBuffer.allocate(new Long(file.length()).intValue());
        //fc��buffer�ж�������
        fc.read(bb);
        bb.flip();
        String str=new String(bb.array(),"UTF8");
        fc.close();
        fis.close();
        return str;

    }

    /**
     * String ת org.dom4j.Document
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document strToDocument(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }

    /**
     * org.dom4j.Document ת  com.alibaba.fastjson.JSONObject
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static JSONObject documentToJSONObject(String xml) throws DocumentException {
        return elementToJSONObject(strToDocument(xml).getRootElement());
    }

    /**
     * org.dom4j.Element ת  com.alibaba.fastjson.JSONObject
     * @param node
     * @return
     */
    public static JSONObject elementToJSONObject(Element node) {
        JSONObject result = new JSONObject();
        // ��ǰ�ڵ�����ơ��ı����ݺ�����
        List<Attribute> listAttr = node.attributes();// ��ǰ�ڵ���������Ե�list
        for (Attribute attr : listAttr) {// ������ǰ�ڵ����������
        	result.put(attr.getName(), attr.getValue());
        }
        // �ݹ������ǰ�ڵ����е��ӽڵ�
        List<Element> listElement = node.elements();// ����һ���ӽڵ��list
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {// ��������һ���ӽڵ�
                if (e.attributes().isEmpty() && e.elements().isEmpty()){ // �ж�һ���ڵ��Ƿ������Ժ��ӽڵ�
                    result.put(e.getName(), e.getTextTrim());// �]���򽫵�ǰ�ڵ���Ϊ�ϼ��ڵ�����ԶԴ�
                }
                else {
                    if (!result.containsKey(e.getName())){ // �жϸ��ڵ��Ƿ���ڸ�һ���ڵ����Ƶ�����
                        result.put(e.getName(), new JSONArray());// û���򴴽�
                    }
                	if(!StringUtils.contains(elementToJSONObject(e).toJSONString(), "General information")){
                		((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));// ����һ���ڵ����ýڵ����Ƶ����Զ�Ӧ��ֵ��
                	}
                }
            }
        }
        return result;
    }

}
