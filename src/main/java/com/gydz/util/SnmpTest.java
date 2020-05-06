package com.gydz.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
 
public class SnmpTest {
 
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		SnmpTest test = new SnmpTest();
		//1.3.6.1.4.1.232.11.2.14.1.1 System ROM
		//1.3.6.1.4.1.232.3.2.5.1.1.51.0.0 storage
		File file =new File("D:\\192.168.1.112-1.3.6.1.4.1.txt");
		Writer out =new FileWriter(file);
		String ip = "192.168.1.112";
		String community = "public";
		List<String> list = SnmpData.snmpWalk(ip, community, "1.3.6.1.4.1");
		for (int i = 0; i < list.size(); i++) {
			out.write(list.get(i)+System.getProperty("line.separator"));
		}
		out.close();

		//test.testWalk("1.3.6.1.2.1.1.5.0");
		/*String ip = "127.0.0.1";
		String community = "public";
		String str = SnmpData.snmpGet(ip, community, "1.3.6.1.2.1.1.6.0");
		System.out.println(str+"======");*/
	}
	
	public void testGet(String oid)
	{
		String ip = "192.168.1.106";
		String community = "public";
		SnmpData.snmpGet(ip, community, oid);
	}
 
 
	public void testGetList(){
		String ip = "192.168.1.179";
		String community = "public";
		List<String> oidList=new ArrayList<String>();
		oidList.add("1.3.6.1.2.1.1.1.0");
		oidList.add("1.3.6.1.2.1.1.5.0");
		SnmpData.snmpGetList(ip, community, oidList);
	}
	
	
	public void testGetAsyList()
	{
		String ip = "192.168.1.179";
		String community = "public";
		List<String> oidList=new ArrayList<String>();
		oidList.add("1.3.6.1.2.1.1.1.0");
		oidList.add("1.3.6.1.2.1.1.5.0");
		SnmpData.snmpAsynGetList(ip, community, oidList);
		System.out.println("i am first!");
	}
 
	
	public void testWalk(String oid)
	{
		String ip = "192.168.1.179";
		String community = "public";
		List<String> list = SnmpData.snmpWalk(ip, community, oid);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
	}
	
 
	public void testAsyWalk(String oid)
	{
		String ip = "192.168.1.179";
		String community = "public";
		// 异步采集数据
		SnmpData.snmpAsynWalk(ip, community, oid);
	}
	
	
	public void testSetPDU() throws Exception
	{
		String ip = "127.0.0.1";
		String community = "public";
		SnmpData.setPDU(ip, community, "1.3.6.1.2.1.1.6.0","jianghuiwen");
	}
	
 
	public void testVersion()
	{
		System.out.println(org.snmp4j.version.VersionInfo.getVersion());
	}
}

