package com.gydz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

public class createcurrentTableUtil{
	public static void main(String[] args) throws Exception {
		System.out.println(6%10);  
	}
	
	// 	传感器记录表，为fullrecord和conpactrecord创建,还有eventonlyrecord
	static void createTablesensor_record(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		////String deletesql = "DROP TABLE IF EXISTS sensor_record_current;";
		String creatsql = "CREATE TABLE IF NOT EXISTS "+"`"+"sensor_record_current" + "_" + ip  +"`" +
				" ( `start_time` datetime DEFAULT NULL," + 
				"  `IP` varchar(255) DEFAULT NULL," + 
				"  `name` varchar(255) DEFAULT NULL," + 
				"  `entity_id` varchar(255) DEFAULT NULL," + 
				"  `sensor_type` varchar(255) DEFAULT NULL," + 
				"  `record_type` varchar(255) DEFAULT NULL," + 
				"  `current_num` double DEFAULT NULL," + 
				"  `sensor_base_unit` varchar(255) DEFAULT NULL," + 
				"  `entity_physical` varchar(255) DEFAULT NULL," +
				"  `sensor_state_valid` varchar(255) DEFAULT NULL," +
				"  `statesAsserted` varchar(255) DEFAULT NULL," +
				"  `state` varchar(255) DEFAULT NULL," + 
				"KEY `name_id` (`name`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		////stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("sensor_record创建失败current"+ tableName);
		}
		stmt.close();
	}
	
	//底盘状态表GetChassisStatusResponseData
	static void createTablechassis_status(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS chassis_status_current;";
		String creatsql = "CREATE TABLE IF NOT EXISTS "+"`"+"chassis_status_current" + "_" +  ip + "`" +
				" ( `start_time` datetime DEFAULT NULL," + 
				"  `IP` varchar(255) DEFAULT NULL," + 
				"  `is_power_fault` tinyint(1) DEFAULT NULL," + 
				"  `is_interlock` tinyint(1) DEFAULT NULL," + 
				"  `is_power_overload` tinyint(1) DEFAULT NULL," + 
				"  `is_power_on` tinyint(1) DEFAULT NULL," + 
				"  `was_power_fault` tinyint(1) DEFAULT NULL," + 
				"  `was_interlock` tinyint(1) DEFAULT NULL," + 
				"  `ac_failed` tinyint(1) DEFAULT NULL," + 
				"  `cooling_fault_detected` tinyint(1) DEFAULT NULL," + 
				"  `drive_fault_detected` tinyint(1) DEFAULT NULL," + 
				"  `is_chassis_intrusion_active` tinyint(1) DEFAULT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("chassis_status_创建失败current"+ tableName);
		}
		stmt.close();
	}
	static void createTablefru_info(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS fru_info_current;";
		String creatsql = "CREATE TABLE IF NOT EXISTS "+"`"+"fru_info_current" + "_" +  ip + "`" +
				" ( `start_time` datetime DEFAULT NULL," + 
				"  `IP` varchar(255) DEFAULT NULL," + 
				"  `name` varchar(255) DEFAULT NULL," + 
				"  `product_name` varchar(255) DEFAULT NULL," + 
				"  `mfg_date` datetime DEFAULT NULL," + 
				"  `manufacturer` varchar(255) DEFAULT NULL," + 
				"  `serial_number` varchar(255) DEFAULT NULL," + 
				"  `part_number` varchar(255) DEFAULT NULL," + 
				"  `custom_info` varchar(255) DEFAULT NULL," + 
				"  `model_number` varchar(255) DEFAULT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("fru_info创建失败current"+ tableName);
		}
		stmt.close();
	}
	
	static void createTablesel_record(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS sel_record;";
		String creatsql = "CREATE TABLE IF NOT EXISTS "+"`"+"sel_record" + "_" +  ip +"`" +
				" ( `start_time` datetime DEFAULT NULL," + 
				"  `IP` varchar(255) DEFAULT NULL," + 
				"  `recordId` int(11) DEFAULT NULL," + 
				"  `recordType` varchar(255) DEFAULT NULL," + 
				"  `timestamp` datetime DEFAULT NULL," + 
				"  `sensorType` varchar(255) DEFAULT NULL," + 
				"  `sensorNumber` int(255) DEFAULT NULL," + 
				"  `eventDirection` varchar(255) DEFAULT NULL," + 
				"  `event` varchar(255) DEFAULT NULL," + 
				"  `reading` int(11) DEFAULT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("sel_record创建失败current"+ tableName);
		}
		stmt.close();
	}
	
	static void createTablesel_type(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS sel_type;";
		String creatsql = "CREATE TABLE IF not EXISTS "+"`"+"sel_type" + "_" +  ip + "`" +
				" ( `id` int(11) NOT NULL AUTO_INCREMENT," + 
				"`sel_type` varchar(255) DEFAULT NULL,"+
				"PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("sel_type创建失败current"+ tableName);
		}
		stmt.close();
	}
	
	static void createTablesel_name(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS sel_name;";
		String creatsql = "CREATE TABLE IF not EXISTS "+"`"+"sel_name" + "_" +  ip + "`" +
				"( `id` int(11) NOT NULL AUTO_INCREMENT," + 
				"`sel_name` varchar(255) DEFAULT NULL,"+
				"PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("sel_name创建失败"+ tableName);
		}
		stmt.close();
	}
	
	static void createTablesnmp_value(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS snmp_value;";
		String creatsql = "CREATE TABLE IF NOT EXISTS"+"`"+"snmp_value" + "_" +  ip + "`" +
				"( `start_time` datetime DEFAULT NULL," + 
				"  `hostname` varchar(255) DEFAULT NULL," + 
				 " `name` varchar(255) DEFAULT NULL," + 
				 " `oid` varchar(255) DEFAULT NULL," + 
				 " `value` varchar(255) DEFAULT NULL" + 
				 /*" KEY `ip` (`ip`)," + 
				 " KEY `type` (`type`)," + 
				 " KEY `name` (`name`)," + 
				 " CONSTRAINT `c_ip` FOREIGN KEY (`ip`) REFERENCES `serversip` (`id`)," + 
				"  CONSTRAINT `c_name` FOREIGN KEY (`name`) REFERENCES `snmp_oid` (`id`)," + 
				 " CONSTRAINT `c_type` FOREIGN KEY (`type`) REFERENCES `snmp_type` (`id`)" + */
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("snmp_value创建失败current"+ tableName);
		}
		stmt.close();
	}
}
