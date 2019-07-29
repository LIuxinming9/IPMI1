package com.gydz.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Gauge;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.TextStyle;

public class EChartsUtil {

	/**
	 * ���ɼ򵥵���״ͼ
	 * @param dataMap
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Option generateBarSimple(LinkedHashMap<String, Object> dataMap, EChartsOptionParam param) throws Exception {
		
		Option option = new Option();
		
        option.title(param.getTitle()).tooltip(Trigger.axis).
        	title().textStyle(new TextStyle().fontSize(18)).subtext(param.getSubtext()).x(X.center);
        option.calculable(true);
        option.toolbox().show(param.isShowToolbox()).feature(
        		new MagicType(Magic.line, Magic.bar), Tool.dataView, Tool.mark, Tool.restore, Tool.saveAsImage);
        
        // ����ֵ��
        if(param.isyValue()) {
        	AxisLabel axisLabel = new AxisLabel();
        	if(StringUtils.isNotBlank(param.getyUnit())){
        		axisLabel.setFormatter("{value} "+param.getyUnit());
            }
        	option.yAxis(new ValueAxis().boundaryGap(0d, 0.01).axisLabel(axisLabel)); 
        }else{
        	AxisLabel axisLabel = new AxisLabel();
        	if(StringUtils.isNotBlank(param.getxUnit())){
        		axisLabel.setFormatter("{value} "+param.getxUnit());
            }
        	option.xAxis(new ValueAxis().boundaryGap(0d, 0.01).axisLabel(axisLabel)); 
        }
        // ������Ŀ��  
        CategoryAxis category = new CategoryAxis(); 
        // ��״ͼ
        Bar bar = new Bar(param.getName()); 
        
        Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();      
        while(iterator.hasNext())  
        {  
            Entry<String, Object> entry = iterator.next();
            //������Ŀ  
            category.data(entry.getKey());  
            //��Ŀ��Ӧ����״ͼ  
            bar.data(entry.getValue()); 
        }
        
        // ������Ŀ��
        if(param.isyValue()) {
        	option.xAxis(category);
        }else{
        	option.yAxis(category);
        }
        
        option.series(bar);
		
		return option;
	}
	
	/**
	 * ���ɶ��������״ͼ(֧�ֶѵ�)
	 * @param dataMap
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Option generateBarMore(LinkedHashMap<String,LinkedHashMap<String, Object>> dataMap, EChartsOptionParam param) throws Exception {
		
		Option option = new Option();
		
        option.title(param.getTitle()).tooltip(Trigger.axis).
        	title().textStyle(new TextStyle().fontSize(18)).subtext(param.getSubtext()).x(X.left);
        option.calculable(true);
        option.toolbox().show(param.isShowToolbox()).feature(
        		new MagicType(Magic.line, Magic.bar), Tool.dataView, Tool.mark, Tool.restore, Tool.saveAsImage);
        
	    //option.tooltip().trigger(Trigger.item).formatter("{b} <br/>{a} : {c} "+" GB");
        
        // ����ֵ��
        if(param.isyValue()) {
        	AxisLabel axisLabel = new AxisLabel();
        	if(StringUtils.isNotBlank(param.getyUnit())){
        		axisLabel.setFormatter("{value} "+param.getyUnit());
            }
        	option.yAxis(new ValueAxis().boundaryGap(0d, 0.01).axisLabel(axisLabel)); 
        }else{
        	AxisLabel axisLabel = new AxisLabel();
        	if(StringUtils.isNotBlank(param.getxUnit())){
        		axisLabel.setFormatter("{value} "+param.getxUnit());
            }
        	option.xAxis(new ValueAxis().boundaryGap(0d, 0.01).axisLabel(axisLabel)); 
        }
        // ������Ŀ��  
        CategoryAxis category = new CategoryAxis(); 
        List<String> legendData = new ArrayList();    
        Map<String,String> legendMap = new HashMap<String,String>();
        List<Series> seriesList = new ArrayList<Series>();
        List<Bar> bars = new ArrayList<Bar>();
        LinkedHashMap<String,List<Object>> valueMap = new LinkedHashMap<String,List<Object>>();
        
        Iterator<Map.Entry<String, LinkedHashMap<String, Object>>> iterator = dataMap.entrySet().iterator();
        while(iterator.hasNext())  
        {
        	Entry<String, LinkedHashMap<String, Object>> entry = iterator.next();
        	// ������Ŀ  
            category.data(entry.getKey()); 
            LinkedHashMap<String, Object> subEntry = entry.getValue();
            
            Iterator<Map.Entry<String, Object>> subIterator = subEntry.entrySet().iterator();
            while(subIterator.hasNext())  
            {  
            	Entry<String, Object> ssentry = subIterator.next();
            	String key = ssentry.getKey();
            	Object value = ssentry.getValue();
            	
            	if(legendMap.get(key) == null){
            		legendData.add(key);
            		legendMap.put(key, key);
            		List<Object> values = new ArrayList<Object>();
            		values.add(value);
            		valueMap.put(key, values);
            	}else{
            		valueMap.get(key).add(value);
            	}
            }
            
        }
        
        // �ѵ�
        Map<String,List<String>> stackMap = param.getStackMap();
        Map<String,String> stacksMap = new HashMap<String,String>();
        if(stackMap.size() > 0){
        	Iterator<Map.Entry<String, List<String>>> stacks = stackMap.entrySet().iterator();      
            while(stacks.hasNext())  
            {  
                Entry<String, List<String>> entry = stacks.next();
                String key = entry.getKey();
                for(String value : entry.getValue()){
                	stacksMap.put(value, key);
                }
            }
        }
        // ����ֵ
        Iterator<Map.Entry<String, List<Object>>> valueIterator = valueMap.entrySet().iterator();      
        while(valueIterator.hasNext())  
        {  
        	Bar bar = new Bar();
            Entry<String, List<Object>> entry = valueIterator.next();
            String key = entry.getKey();
            bar.setName(key);
            if(stacksMap.get(key) != null){
            	bar.setStack(stacksMap.get(key));
            }
            bar.data(entry.getValue().toArray());
            bars.add(bar);
        }
        seriesList.addAll(bars);
        
        // ������Ŀ��
        if(param.isyValue()) {
        	option.xAxis(category);
        }else{
        	option.yAxis(category);
        }
        
        option.legend().orient(Orient.horizontal).x(X.center).data(legendData.toArray());
        option.setSeries(seriesList);
        
		return option;
	}
	
	/**
	 * ���ɼ򵥵Ĳ���ͼ
	 * @param dataMap
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Option generateLineSimple(LinkedHashMap<String, Object> dataMap, EChartsOptionParam param) throws Exception {
		
		Option option = new Option();
		
        option.title(param.getTitle()).tooltip(Trigger.axis).
        	title().textStyle(new TextStyle().fontSize(18)).subtext(param.getSubtext()).x(X.center);
        option.calculable(true);
        option.toolbox().show(param.isShowToolbox()).feature(
        		new MagicType(Magic.line, Magic.bar), Tool.dataView, Tool.mark, Tool.restore, Tool.saveAsImage);
        // ����ֵ��
        if(param.isyValue()) {
        	AxisLabel axisLabel = new AxisLabel();
        	if(StringUtils.isNotBlank(param.getyUnit())){
        		axisLabel.setFormatter("{value} "+param.getyUnit());
            }
        	option.yAxis(new ValueAxis().boundaryGap(0d, 0.01).axisLabel(axisLabel)); 
        }else{
        	AxisLabel axisLabel = new AxisLabel();
        	if(StringUtils.isNotBlank(param.getxUnit())){
        		axisLabel.setFormatter("{value} "+param.getxUnit());
            }
        	option.xAxis(new ValueAxis().boundaryGap(0d, 0.01).axisLabel(axisLabel)); 
        }
        // ������Ŀ��  
        CategoryAxis category = new CategoryAxis(); 
        // ����ͼ
        Line line = new Line(param.getName()); 
        
        Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();      
        while(iterator.hasNext())  
        {  
            Entry<String, Object> entry = iterator.next();
            //������Ŀ  
            category.data(entry.getKey());  
            //��Ŀ��Ӧ����״ͼ  
            line.data(entry.getValue()); 
            line.smooth(true);
        }
        
        // ������Ŀ��
        if(param.isyValue()) {
        	option.xAxis(category);
        }else{
        	option.yAxis(category);
        }
        
        option.series(line);
		
		return option;
	}

	/**
	 * ���ɶ�����Ĳ���ͼ
	 * @param dataMap
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Option generateLineMore(LinkedHashMap<String,LinkedHashMap<String, Object>> dataMap, EChartsOptionParam param) throws Exception {
		
		Option option = new Option();
		
        option.title(param.getTitle()).tooltip(Trigger.axis).
        	title().textStyle(new TextStyle().fontSize(18)).subtext(param.getSubtext()).x(X.center);
        option.calculable(true);
        option.toolbox().show(param.isShowToolbox()).feature(
        		new MagicType(Magic.line, Magic.bar), Tool.dataView, Tool.mark, Tool.restore, Tool.saveAsImage);
        
        // ����ֵ��
        if(param.isyValue()) {
        	AxisLabel axisLabel = new AxisLabel();
        	if(StringUtils.isNotBlank(param.getyUnit())){
        		axisLabel.setFormatter("{value} "+param.getyUnit());
            }
        	option.yAxis(new ValueAxis().boundaryGap(0d, 0.01).axisLabel(axisLabel)); 
        }else{
        	AxisLabel axisLabel = new AxisLabel();
        	if(StringUtils.isNotBlank(param.getxUnit())){
        		axisLabel.setFormatter("{value} "+param.getxUnit());
            }
        	option.xAxis(new ValueAxis().boundaryGap(0d, 0.01).axisLabel(axisLabel)); 
        }
    	
        // ������Ŀ��  
        CategoryAxis category = new CategoryAxis(); 
        List<String> legendData = new ArrayList();    
        Map<String,String> legendMap = new HashMap<String,String>();
        List<Series> seriesList = new ArrayList<Series>();
        List<Line> lines = new ArrayList<Line>();
        LinkedHashMap<String,List<Object>> valueMap = new LinkedHashMap<String,List<Object>>();
        
        Iterator<Map.Entry<String, LinkedHashMap<String, Object>>> iterator = dataMap.entrySet().iterator();
        while(iterator.hasNext())  
        {
        	Entry<String, LinkedHashMap<String, Object>> entry = iterator.next();
        	// ������Ŀ  
            category.data(entry.getKey()); 
            LinkedHashMap<String, Object> subEntry = entry.getValue();
            
            Iterator<Map.Entry<String, Object>> subIterator = subEntry.entrySet().iterator();
            while(subIterator.hasNext())  
            {  
            	Entry<String, Object> ssentry = subIterator.next();
            	String key = ssentry.getKey();
            	Object value = ssentry.getValue();
            	
            	if(legendMap.get(key) == null){
            		legendData.add(key);
            		legendMap.put(key, key);
            		List<Object> values = new ArrayList<Object>();
            		values.add(value);
            		valueMap.put(key, values);
            	}else{
            		valueMap.get(key).add(value);
            	}
            }
            
        }
        String[] colors = new String[]{"#ADFF2F","#B0E2FF","#A52A2A","#F08080","#EEEE00","#D15FEE"};
        // ����ֵ
        Iterator<Map.Entry<String, List<Object>>> valueIterator = valueMap.entrySet().iterator();  
        int i = 0;
        while(valueIterator.hasNext())  
        {  
        	Line line = new Line();
            Entry<String, List<Object>> entry = valueIterator.next();
            line.setName(entry.getKey());
            line.data(entry.getValue().toArray()); 
            line.smooth(true);
            line.itemStyle().normal().color(colors[i]);
            line.itemStyle().normal().lineStyle().color(Color.getColor(colors[i]));
            lines.add(line);
            i++;
        }
        seriesList.addAll(lines);
        
        category.axisLabel().setRotate(40);
        
        // ������Ŀ��
        if(param.isyValue()) {
        	option.xAxis(category);
        }else{
        	option.yAxis(category);
        }
        
        option.legend().orient(Orient.horizontal).x(X.left).data(legendData.toArray());
        option.setSeries(seriesList);
        
		return option;
	}
	
	/**
	 * ���ɱ�ͼ
	 * @param dataMap
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Option generatePieSimple(LinkedHashMap<String, Object> dataMap, EChartsOptionParam param) throws Exception {
		
		Option option = new Option();
		
        option.title(param.getTitle()).
        	title().textStyle(new TextStyle().fontSize(18)).subtext(param.getSubtext()).x(X.center);
        option.calculable(true);
	    option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.toolbox().show(param.isShowToolbox()).feature(
        		new MagicType(Magic.line, Magic.bar), Tool.dataView, Tool.mark, Tool.restore, Tool.saveAsImage);
        
        List<String> legendData = new ArrayList<String>();
        List<PieData> pieData = new ArrayList<PieData>();
		Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();      
        while(iterator.hasNext())  
        {  
            Entry<String, Object> entry = iterator.next();
            //������Ŀ  
            legendData.add(entry.getKey());  
            //��Ŀ��Ӧ�ı�ͼ 
            pieData.add(new PieData(entry.getKey(), entry.getValue())); 
        }

	    option.legend().orient(Orient.vertical).x(X.left).data(legendData.toArray());
		
	    Pie pie = new Pie();
	    pie.itemStyle().normal().label().show(false);
	    pie.itemStyle().normal().labelLine().show(false);
		pie.name(param.getName())
				.radius("50%")
				.center(new String[] { "50%", "50%"})
				.data(pieData.toArray());
        
        option.series(pie);
		
		return option;
	}
	
	/**
	 * ���ɻ���ͼ
	 * @param dataMap
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Option generateRingSimple(LinkedHashMap<String, Object> dataMap, EChartsOptionParam param) throws Exception {
		
		Option option = new Option();
		
        option.title(param.getTitle()).
        	title().textStyle(new TextStyle().fontSize(18)).subtext(param.getSubtext()).x(X.center);
        option.calculable(true);
	    option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.toolbox().show(param.isShowToolbox()).feature(
        		new MagicType(Magic.line, Magic.bar), Tool.dataView, Tool.mark, Tool.restore, Tool.saveAsImage);
        
        List<String> legendData = new ArrayList<String>();
        List<PieData> pieData = new ArrayList<PieData>();
		Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();      
        while(iterator.hasNext())  
        {  
            Entry<String, Object> entry = iterator.next();
            //������Ŀ  
            legendData.add(entry.getKey());  
            //��Ŀ��Ӧ�ı�ͼ 
            pieData.add(new PieData(entry.getKey(), entry.getValue())); 
        }

	    option.legend().orient(Orient.vertical).x(X.left).data(legendData.toArray());
		
	    Pie pie = new Pie();
	    pie.itemStyle().normal().label().show(false);
	    pie.itemStyle().normal().labelLine().show(false);
		pie.name(param.getName())
				.radius("50%", "70%")
				.center(new String[] { "50%"})
				.data(pieData.toArray());
        
        option.series(pie);
		
		return option;
	}
	
	/**
	 * �����Ǳ���
	 * @param dataMap
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Option generateGauge(String name ,Double value, EChartsOptionParam param) throws Exception {
		
		Option option = new Option();
		
        // �Ǳ���
		Gauge gauge = new Gauge(param.getName()); 
		
		gauge.tooltip().formatter("{a} <br/>{b} : {c}%");
		
		gauge.detail().formatter("{value}%");
		
		DataObj obj = new DataObj();
		obj.setName(name);
		obj.setValue(value);		
		gauge.data(obj);
        
        option.series(gauge);
		
		return option;
	}

}
