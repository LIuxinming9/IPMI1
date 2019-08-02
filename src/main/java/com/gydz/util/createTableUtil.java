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

public class createTableUtil{
	public static void main(String[] args) throws Exception {
		 
	}
	
	// 	传感器记录表，为fullrecord和conpactrecord创建,还有eventonlyrecord
	static void createTablesensor_record(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS sensor_record;";
		String creatsql = "CREATE TABLE IF NOT EXISTS "+"`"+"sensor_record_" + tableName + "_" + ip +"`" +
				"( `start_time` datetime DEFAULT NULL," + 
				"  `IP` varchar(255) DEFAULT NULL," + 
				"  `name` varchar(255) DEFAULT NULL," + 
				"  `entity_id` varchar(255) DEFAULT NULL," + 
				"  `sensor_type` varchar(255) DEFAULT NULL," + 
				"  `sensor_owner_lun` bit(1) DEFAULT NULL," + 
				"  `record_type` varchar(255) DEFAULT NULL," + 
				"  `current_num` double DEFAULT NULL," + 
				"  `sensor_base_unit` varchar(255) DEFAULT NULL," + 
				"  `entity_physical` varchar(255) DEFAULT NULL," +
				"  `sensor_number` int(11) DEFAULT NULL," +
				"  `entity_instance_number` int(11) DEFAULT NULL," +
				"  `address_type` varchar(255) DEFAULT NULL," +
				"  `rate_unit` varchar(255) DEFAULT NULL," +
				"  `modifier_unit_usage` varchar(255) DEFAULT NULL," +
				"  `sensor_modifier_unit` varchar(255) DEFAULT NULL," +
				"  `sensor_direction` varchar(255) DEFAULT NULL," +
				"  `sensor_state_valid` varchar(255) DEFAULT NULL," +
				"  `statesAsserted` varchar(255) DEFAULT NULL," +
				"  `state` varchar(255) DEFAULT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("sensor_record创建失败"+ tableName);
		}
		stmt.close();
	}
	
	//底盘状态表GetChassisStatusResponseData
	static void createTablechassis_status(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS sensor_record;";
		String creatsql = "CREATE TABLE IF NOT EXISTS "+"`"+"chassis_status_" + tableName + "_" + ip + "`" +
				"( `start_time` datetime DEFAULT NULL," + 
				"  `IP` varchar(255) DEFAULT NULL," + 
				"  `is_power_control_fault` tinyint(1) DEFAULT NULL," + 
				"  `is_power_fault` tinyint(1) DEFAULT NULL," + 
				"  `is_interlock` tinyint(1) DEFAULT NULL," + 
				"  `is_power_overload` tinyint(1) DEFAULT NULL," + 
				"  `is_power_on` tinyint(1) DEFAULT NULL," + 
				"  `was_ipmi_power_on` tinyint(1) DEFAULT NULL," + 
				"  `was_power_fault` tinyint(1) DEFAULT NULL," + 
				"  `was_interlock` tinyint(1) DEFAULT NULL," + 
				"  `ac_failed` tinyint(1) DEFAULT NULL," + 
				"  `is_chassis_identify_command_supported` tinyint(1) DEFAULT NULL," + 
				"  `cooling_fault_detected` tinyint(1) DEFAULT NULL," + 
				"  `drive_fault_detected` tinyint(1) DEFAULT NULL," + 
				"  `is_front_panel_lockout_active` tinyint(1) DEFAULT NULL," + 
				"  `is_chassis_intrusion_active` tinyint(1) DEFAULT NULL," + 
				"  `is_standby_button_disable_allowed` tinyint(1) DEFAULT NULL," + 
				"  `is_diagnostic_interrupt_button_disable_allowed` tinyint(1) DEFAULT NULL," + 
				"  `is_reset_button_disable_allowed` tinyint(1) DEFAULT NULL," + 
				"  `is_power_off_button_disable_allowed` tinyint(1) DEFAULT NULL," + 
				"  `is_standby_button_disabled` tinyint(1) DEFAULT NULL," + 
				"  `is_diagnostic_interrupt_button_disabled` tinyint(1) DEFAULT NULL," + 
				"  `is_reset_button_disabled` tinyint(1) DEFAULT NULL," + 
				"  `is_power_off_button_disabled` tinyint(1) DEFAULT NULL," + 
				"  `is_front_panel_button_capabilities_set` tinyint(1) DEFAULT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("chassis_status_创建失败"+ tableName);
		}
		stmt.close();
	}
	static void createTablefru_info(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS sensor_record;";
		String creatsql = "CREATE TABLE IF NOT EXISTS "+"`"+"fru_info_" + tableName + "_" + ip + "`" +
				"( `start_time` datetime DEFAULT NULL," + 
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
			System.out.println("fru_info创建失败"+ tableName);
		}
		stmt.close();
	}
	
	static void createTableget_time(List<Connection> connectionlist, String tableName,String ip) throws Exception {
		Connection conn = connectionlist.get(0);
		String newTableName = tableName.substring(0,tableName.length()-2);
		//String deletesql = "DROP TABLE IF EXISTS sensor_record;";
		String creatsql = "CREATE TABLE IF NOT EXISTS "+"`"+"get_time_" + newTableName + "_" + ip + "`" +
				"( `end_time` varchar(255) DEFAULT NULL," + 
				"  `sensor_time`  varchar(255)  DEFAULT NULL," + 
				"  `program_time`  varchar(255)  DEFAULT NULL," + 
				"  `thread`  varchar(255)  DEFAULT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("fru_info创建失败"+ tableName);
		}
		stmt.close();
	}
	
	static void createTablewarn_refer(List<Connection> connectionlist, String tableName) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS warn_refer;";
		String creatsql = "CREATE TABLE IF NOT EXISTS warn_refer" + 
				"( `start_time` datetime DEFAULT NULL," + 
				"  `IP` varchar(255) DEFAULT NULL," + 
				"  `name` varchar(255) DEFAULT NULL," + 
				"  `entity_id` varchar(255) DEFAULT NULL," + 
				"  `sensor_type` varchar(255) DEFAULT NULL," +
				"  `current_num` double DEFAULT NULL," + 
				"  `normal_maximum` double DEFAULT NULL," + 
				"  `normal_minimum` double DEFAULT NULL," + 
				"  `nominal_reading` double DEFAULT NULL," + 
				"  `sensor_maximum_reading` double DEFAULT NULL," + 
				"  `sensor_minmum_reading` double DEFAULT NULL," + 
				"  `lower_non_recoverable_threshold` double DEFAULT NULL," + 
				"  `upper_non_recoverable_threshold` double DEFAULT NULL," + 
				"  `upper_critical_threshold` double DEFAULT NULL," + 
				"  `lower_critical_threshold` double DEFAULT NULL," + 
				"  `upper_non_critical_threshold` double DEFAULT NULL," + 
				"  `lower_non_critical_threshold` double DEFAULT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("warn_refer创建失败current"+ tableName);
		}
		stmt.close();
	}
	
	static void createTablesnmp_value(List<Connection> connectionlist, String tableName) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS snmp_value;";
		String creatsql = "CREATE TABLE IF NOT EXISTS snmp_value_" + tableName + 
				"( `start_time` datetime DEFAULT NULL," + 
				"`hostname` varchar(255) DEFAULT NULL," + 
				 " `name` varchar(255) DEFAULT NULL," + 
				 " `oid` varchar(255) DEFAULT NULL," + 
				 " `value` varchar(255) DEFAULT NULL" + 
				 /*" KEY `ip` (`ip`)," + 
				 " KEY `type` (`type`)," + 
				 " KEY `name` (`name`)," + 
				 " CONSTRAINT `ip` FOREIGN KEY (`ip`) REFERENCES `serversip` (`id`)," + 
				"  CONSTRAINT `name` FOREIGN KEY (`name`) REFERENCES `snmp_oid` (`id`)," + 
				 " CONSTRAINT `type` FOREIGN KEY (`type`) REFERENCES `snmp_type` (`id`)" + */
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("snmp_value创建失败"+ tableName);
		}
		stmt.close();
	}
	static void createTablesnmpTrap(List<Connection> connectionlist, String tableName) throws Exception {
		Connection conn = connectionlist.get(0);
		String newTableName = tableName.substring(0,tableName.length()-4);
		//String deletesql = "DROP TABLE IF EXISTS snmp_value;";
		String creatsql = "CREATE TABLE IF NOT EXISTS snmp_trap_" + newTableName + 
				"( `start_time` datetime DEFAULT NULL," + 
				 " `ip` varchar(255) DEFAULT NULL," + 
				 " `oid` varchar(255) DEFAULT NULL," + 
				 " `value` varchar(255) DEFAULT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				"";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("snmp_trap_创建失败"+ tableName);
		}
		stmt.close();
	}
	static void createTablesnmp_oid(List<Connection> connectionlist, String tableName) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS snmp_value;";
		String creatsql = "CREATE TABLE IF NOT EXISTS `snmp_oid` " + 
				 "(`id` int(20) NOT NULL AUTO_INCREMENT," + 
				  "`oid` varchar(255) DEFAULT NULL ," + 
				  "`way` varchar(255) DEFAULT NULL," + 
				  "`english_des` varchar(255) DEFAULT NULL," + 
				 " `chinese_des` varchar(255) DEFAULT NULL," + 
				 " PRIMARY KEY (`id`)," + 
				 " KEY `id` (`id`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("snmp_oid创建失败"+ tableName);
		}
		stmt.close();
	}
	
	static void createTablesnmp_type(List<Connection> connectionlist, String tableName) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS snmp_value;";
		String creatsql = "CREATE TABLE IF NOT EXISTS `snmp_type` " + 
				"(`id` int(20) NOT NULL AUTO_INCREMENT, " + 
				"  `type` varchar(255) DEFAULT NULL COMMENT '用户名'," + 
				"  PRIMARY KEY (`id`)," + 
				"  KEY `type` (`type`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("snmp_oid创建失败"+ tableName);
		}
		stmt.close();
	}
	
	static void createTablesnmp_id_oid(List<Connection> connectionlist, String tableName) throws Exception {
		Connection conn = connectionlist.get(0);
		//String deletesql = "DROP TABLE IF EXISTS snmp_value;";
		String creatsql = "CREATE TABLE IF NOT EXISTS `snmp_id_oid` " + 
				"(`iid` int(11) DEFAULT NULL COMMENT '角色ID'," +
				"  `ooid` int(11) DEFAULT NULL COMMENT '用户ID'," +
				"  KEY `iid_id` (`iid`)," +
				"  KEY `ooid_id` (`ooid`)," +
				"  CONSTRAINT `iid_id` FOREIGN KEY (`iid`) REFERENCES `serversip` (`id`)," +
				"  CONSTRAINT `ooid_id` FOREIGN KEY (`ooid`) REFERENCES `snmp_oid` (`id`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate(deletesql);
		if (0 == stmt.executeUpdate(creatsql)) {
		} else {
			System.out.println("snmp_id_oid创建失败"+ tableName);
		}
		stmt.close();
	}
}
