package com.gydz.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gydz.user.model.chassisStatus;
import com.gydz.user.model.fruInfo;
import com.gydz.user.model.sensorrecord;
import com.gydz.user.model.serversip;
import com.veraxsystems.vxipmi.api.async.ConnectionHandle;
import com.veraxsystems.vxipmi.api.sync.IpmiConnector;
import com.veraxsystems.vxipmi.coding.commands.IpmiVersion;
import com.veraxsystems.vxipmi.coding.commands.PrivilegeLevel;
import com.veraxsystems.vxipmi.coding.commands.chassis.ChassisControl;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatus;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatusResponseData;
import com.veraxsystems.vxipmi.coding.commands.chassis.PowerCommand;
import com.veraxsystems.vxipmi.coding.commands.fru.BaseUnit;
import com.veraxsystems.vxipmi.coding.commands.fru.GetFruInventoryAreaInfo;
import com.veraxsystems.vxipmi.coding.commands.fru.GetFruInventoryAreaInfoResponseData;
import com.veraxsystems.vxipmi.coding.commands.fru.ReadFruData;
import com.veraxsystems.vxipmi.coding.commands.fru.ReadFruDataResponseData;
import com.veraxsystems.vxipmi.coding.commands.fru.record.BoardInfo;
import com.veraxsystems.vxipmi.coding.commands.fru.record.ChassisInfo;
import com.veraxsystems.vxipmi.coding.commands.fru.record.FruRecord;
import com.veraxsystems.vxipmi.coding.commands.fru.record.ProductInfo;
import com.veraxsystems.vxipmi.coding.commands.sdr.GetSdr;
import com.veraxsystems.vxipmi.coding.commands.sdr.GetSdrResponseData;
import com.veraxsystems.vxipmi.coding.commands.sdr.GetSensorReading;
import com.veraxsystems.vxipmi.coding.commands.sdr.GetSensorReadingResponseData;
import com.veraxsystems.vxipmi.coding.commands.sdr.ReserveSdrRepository;
import com.veraxsystems.vxipmi.coding.commands.sdr.ReserveSdrRepositoryResponseData;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.CompactSensorRecord;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.EventOnlyRecord;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.FruDeviceLocatorRecord;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.FullSensorRecord;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.RateUnit;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.ReadingType;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.SensorRecord;
import com.veraxsystems.vxipmi.coding.commands.sel.GetSelEntry;
import com.veraxsystems.vxipmi.coding.commands.sel.GetSelEntryResponseData;
import com.veraxsystems.vxipmi.coding.commands.sel.SelRecord;
import com.veraxsystems.vxipmi.coding.commands.session.SetSessionPrivilegeLevel;
import com.veraxsystems.vxipmi.coding.payload.CompletionCode;
import com.veraxsystems.vxipmi.coding.payload.lan.IPMIException;
import com.veraxsystems.vxipmi.coding.protocol.AuthenticationType;
import com.veraxsystems.vxipmi.coding.security.CipherSuite;
import com.veraxsystems.vxipmi.common.TypeConverter;
import com.veraxsystems.vxipmi.connection.Connection;

/**
 * Created by Administrator on 2018/5/22 0022.
 */
public class ipmiutil {

    /**
     * This is the value of Last Record ID (FFFFh). In order to retrieve the full set of SDR records, client must repeat
     * reading SDR records until MAX_REPO_RECORD_ID is returned as next record ID. For further information see section
     * 33.12 of the IPMI specification ver. 2.0
     */
    private static final int MAX_REPO_RECORD_ID = 65535;

    private static final String hostname = "192.168.1.106";

    private static final String username = "lenovo";

    private static final String password = "len0vO";
    
    private static final int FRU_READ_PACKET_SIZE = 16;
    
    private static final int DEFAULT_FRU_ID = 0;
    /**
     * Size of the initial GetSdr message to get record header and size
     */
    private static final int INITIAL_CHUNK_SIZE = 8;

    /**
     * Chunk size depending on buffer size of the IPMI server. Bigger values will improve performance. If server is
     * returning "Cannot return number of requested data bytes." error during GetSdr command, CHUNK_SIZE should be
     * decreased.
     */
    private static final int CHUNK_SIZE = 16;

    /**
     * Size of SDR record header
     */
    private static final int HEADER_SIZE = 5;

    //private static int nextRecId;

    /**
     * @param args
     * @throws UnknownHostException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    
    public static void main(String[] args) throws Exception{
    	Date date = new Date();
    	serversip serversip = new serversip();
    	/*serversip.setHostname("192.168.1.107");
    	serversip.setUsername("lenovo");
    	serversip.setPassword("len0vO");*/
    	serversip.setHostname("192.168.1.112");
    	serversip.setUsername("Administrator");
    	serversip.setPassword("GN9YTH76");
    	Map<String,List<sensorrecord>> listMap = getfullrecord(serversip,0);
    	List<sensorrecord> fulllist = listMap.get("fullList");
		List<sensorrecord> conpactlist = listMap.get("conpactList");
    	System.out.println(fulllist.size());
    	for (int i = 0; i < fulllist.size(); i++) {
    		System.out.println(fulllist.get(i).getName()+"  =  "+fulllist.get(i).getSensor_type()+"  =  "+fulllist.get(i).getCurrent_num());
		}
    	System.out.println(conpactlist.size());
    	for (int i = 0; i < conpactlist.size(); i++) {
    		System.out.println(conpactlist.get(i).getName()+"  =  "+conpactlist.get(i).getSensor_type()+"  =  "+conpactlist.get(i).getCurrent_num());
		}
    	Date endDate = new Date();
    	System.out.println(endDate.getTime() - date.getTime());
    	}

        public static void getsensortest() throws Exception {
        	
        	List<SensorRecord> list = new ArrayList<SensorRecord>();
          	 
          	 int nextRecId = 0;

               int reservationId = 0;
               int lastReservationId = -1;

               IpmiConnector connector = new IpmiConnector(0);

               System.out.println("1");
               ConnectionHandle handle = startSession(connector, InetAddress.getByName("192.168.5.6"), "Administrator", "6CVBPNPW", "",
                       PrivilegeLevel.Administrator);

               System.out.println("2");
               connector.setTimeout(handle, 32750);

               System.out.println("2");
               Date date = new Date();
               while (nextRecId < MAX_REPO_RECORD_ID) {
            	 sensorrecord sensorrecord = new sensorrecord();
              	 sensorrecord.setIP("192.168.5.6");
              	 sensorrecord.setStart_time(date);
                   SensorRecord record = null;
                   record = getSensorDat(connector, handle, reservationId,nextRecId);
                   GetSdrResponseData data = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(IpmiVersion.V20,
                           handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId, nextRecId));
                   nextRecId = data.getNextRecordId();
                       int recordReadingId = -1;
                       if (record instanceof FullSensorRecord) {
                           FullSensorRecord fsr = (FullSensorRecord) record;
                           
                           recordReadingId = TypeConverter.byteToInt(fsr.getSensorNumber());
                           sensorrecord.setName(fsr.getName());
                           sensorrecord.setEntity_id(fsr.getEntityId().toString());
                           sensorrecord.setSensor_type(fsr.getSensorType().toString());
                           sensorrecord.setNormal_maximum(fsr.getNormalMaximum());
                           sensorrecord.setNormal_minimum(fsr.getNormalMinimum());
                           sensorrecord.setSensor_owner_lun(fsr.getSensorOwnerLun());
                           sensorrecord.setSensor_base_unit(fsr.getSensorBaseUnit().toString());
                           sensorrecord.setAddress_type(fsr.getAddressType().toString());
                           sensorrecord.setEntity_instance_number(fsr.getEntityInstanceNumber());
                           sensorrecord.setEntity_physical(fsr.isEntityPhysical()+"");
                           sensorrecord.setLower_critical_threshold(fsr.getLowerCriticalThreshold());
                           sensorrecord.setLower_non_critical_threshold(fsr.getLowerNonCriticalThreshold());
                           sensorrecord.setLower_non_recoverable_threshold(fsr.getLowerNonRecoverableThreshold());
                           sensorrecord.setModifier_unit_usage(fsr.getModifierUnitUsage().toString());
                           sensorrecord.setNominal_reading(fsr.getNominalReading());
                           sensorrecord.setRate_unit(fsr.getRateUnit().toString());
                           sensorrecord.setSensor_direction(fsr.getSensorDirection().toString());
                           sensorrecord.setSensor_maximum_reading(fsr.getSensorMaximumReading());
                           sensorrecord.setSensor_minmum_reading(fsr.getSensorMinmumReading());
                           sensorrecord.setSensor_modifier_unit(fsr.getSensorModifierUnit().toString());
                           sensorrecord.setSensor_number(fsr.getSensorNumber());
                           sensorrecord.setUpper_critical_threshold(fsr.getUpperCriticalThreshold());
                           sensorrecord.setUpper_non_critical_threshold(fsr.getUpperNonCriticalThreshold());
                           sensorrecord.setUpper_non_recoverable_threshold(fsr.getUpperNonRecoverableThreshold());
                       }
                    } 
       
        }
        
    public static CipherSuite getcs(IpmiConnector connector,ConnectionHandle handle) {
    	List<CipherSuite> list = new ArrayList<CipherSuite>();
		try {
			list = connector.getAvailableCipherSuites(handle);
		} catch (Exception e) {
		}
    	CipherSuite cs = null;
    	
    	for (int i = 0; i < list.size(); i++) {
			if(list.get(i).authenticationAlgorithm==1&&list.get(i).confidentialityAlgorithm==1&&list.get(i).integrityAlgorithm==1) {
				cs = list.get(i);
			}
		}
    	
		return cs;
    }
    
    
    public static boolean chassisState(String hostnames, String usernames, String passwords) throws Exception {
    	IpmiConnector connector = new IpmiConnector(0);
    	ConnectionHandle handle = connector.createConnection(InetAddress.getByName(hostnames));
    	CipherSuite cs = getcs(connector,handle);
    	connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);
    	connector.openSession(handle, usernames, passwords, null);
    	GetChassisStatusResponseData rd = (GetChassisStatusResponseData) connector.sendMessage(handle,
                new GetChassisStatus(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus));
    	connector.sendMessage(handle, new SetSessionPrivilegeLevel(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
                 PrivilegeLevel.Administrator));
        connector.closeSession(handle);
        connector.closeConnection(handle);
        connector.tearDown();
		return rd.isPowerOn();
    }
    
    
    
    
    public static void chassisControl(String hostnames, String usernames, String passwords) throws Exception {
    	IpmiConnector connector = new IpmiConnector(0);
    	ConnectionHandle handle = connector.createConnection(InetAddress.getByName(hostnames));
    	CipherSuite cs = getcs(connector,handle);
    	connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);
    	connector.openSession(handle, usernames, passwords, null);
    	GetChassisStatusResponseData rd = (GetChassisStatusResponseData) connector.sendMessage(handle,
                new GetChassisStatus(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus));
    	connector.sendMessage(handle, new SetSessionPrivilegeLevel(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
                 PrivilegeLevel.Administrator));
    	ChassisControl chassisControl = null;
    	if (!rd.isPowerOn()) {
            chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus, PowerCommand.PowerUp);
        } else {
            chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
                    PowerCommand.PowerDown);
        }

        connector.sendMessage(handle, chassisControl);
        connector.closeSession(handle);
        connector.closeConnection(handle);
        connector.tearDown();
    }
    
    public static void PowerUp(String hostnames, String usernames, String passwords) throws Exception {
    	IpmiConnector connector = new IpmiConnector(0);
    	ConnectionHandle handle = connector.createConnection(InetAddress.getByName(hostnames));
    	CipherSuite cs = getcs(connector,handle);
    	connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);
    	connector.openSession(handle, usernames, passwords, null);
    	connector.sendMessage(handle, new SetSessionPrivilegeLevel(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
                 PrivilegeLevel.Administrator));
    	ChassisControl chassisControl = null;
        chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus, PowerCommand.PowerUp);

        connector.sendMessage(handle, chassisControl);
        connector.closeSession(handle);
        connector.closeConnection(handle);
        connector.tearDown();
    }
    
    public static void HardReset(String hostnames, String usernames, String passwords) throws Exception {
    	IpmiConnector connector = new IpmiConnector(0);
    	ConnectionHandle handle = connector.createConnection(InetAddress.getByName(hostnames));
    	CipherSuite cs = getcs(connector,handle);
    	connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);
    	connector.openSession(handle, usernames, passwords, null);
    	connector.sendMessage(handle, new SetSessionPrivilegeLevel(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
                 PrivilegeLevel.Administrator));
    	ChassisControl chassisControl = null;
        chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus, PowerCommand.HardReset);

        connector.sendMessage(handle, chassisControl);
        connector.closeSession(handle);
        connector.closeConnection(handle);
        connector.tearDown();
    }
    
    public static void PowerDown(String hostnames, String usernames, String passwords) throws Exception {
    	IpmiConnector connector = new IpmiConnector(0);
    	ConnectionHandle handle = connector.createConnection(InetAddress.getByName(hostnames));
    	CipherSuite cs = getcs(connector,handle);
    	connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);
    	connector.openSession(handle, usernames, passwords, null);
    	connector.sendMessage(handle, new SetSessionPrivilegeLevel(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
                 PrivilegeLevel.Administrator));
    	ChassisControl chassisControl = null;
        chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus, PowerCommand.PowerDown);

        connector.sendMessage(handle, chassisControl);
        connector.closeSession(handle);
        connector.closeConnection(handle);
        connector.tearDown();
    }
    
    public static List<SelRecord> getselrecord(serversip serversip,int selNextRecId,java.sql.Connection conn,int port) throws Exception{
    	int sel_num_max = 0;
    	List<SelRecord> list = new ArrayList<SelRecord>();
    	IpmiConnector connector = new IpmiConnector(port);
    	ConnectionHandle handle = connector.createConnection(InetAddress.getByName(serversip.getHostname()));
    	CipherSuite cs = getcs(connector,handle);
    	connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);
    	connector.openSession(handle, serversip.getUsername(), serversip.getPassword(), null);
    	while (selNextRecId < MAX_REPO_RECORD_ID) {
    		GetSelEntryResponseData data = (GetSelEntryResponseData) connector.sendMessage(handle, new GetSelEntry(IpmiVersion.V20,
                    handle.getCipherSuite(), AuthenticationType.RMCPPlus, 0, selNextRecId));
    		sel_num_max = selNextRecId;
    		selNextRecId = data.getNextRecordId();
    		list.add(data.getSelRecord());
    	}
    	if(selNextRecId!=sel_num_max) {
    		List<String> numList = new ArrayList<String>();
        	numList.add(sel_num_max+"");
        	numList.add(serversip.getHostname());
        	insertcurrentUtil.a_num_update(conn, numList);
    	}
    	connector.closeSession(handle);
    	connector.closeConnection(handle);
        connector.tearDown();
		return list;
    	}
    
    
    public static Map<String,List<sensorrecord>> getfullrecord(serversip serversip,int port) throws Exception{
    	
    	Map<String,List<sensorrecord>> listMap = new HashMap<String, List<sensorrecord>>();
    	 List<sensorrecord> fullList = new ArrayList<sensorrecord>();
    	 List<sensorrecord> conpactList = new ArrayList<sensorrecord>();
    	 
    	 int nextRecId = 0;

         int reservationId = 0;
         int lastReservationId = -1;

         IpmiConnector connector = new IpmiConnector(port);

         ConnectionHandle handle = startSession(connector, InetAddress.getByName(serversip.getHostname()), serversip.getUsername(), serversip.getPassword(), "",
                 PrivilegeLevel.User);

         connector.setTimeout(handle, 32750);

         Date date = new Date();
         while (nextRecId < MAX_REPO_RECORD_ID) {
        	 sensorrecord sensorrecord = new sensorrecord();
        	 sensorrecord.setIP(serversip.getHostname());
        	 sensorrecord.setStart_time(date);
             SensorRecord record = null;
              try {
            	  Map<String, Object> map = new HashMap<String, Object>();
                  map = getSensorData(connector, handle, reservationId,nextRecId);
                  	record = (SensorRecord) map.get("SensorRecord");
                      nextRecId = Integer.parseInt(map.get("nextRec").toString());
                 int recordReadingId = -1;
                 if (record instanceof FullSensorRecord) {
                     FullSensorRecord fsr = (FullSensorRecord) record;
                     sensorrecord.setRecord_type("FullSensorRecord");
                     recordReadingId = TypeConverter.byteToInt(fsr.getSensorNumber());
                     sensorrecord.setName(fsr.getName());
                     sensorrecord.setEntity_id(fsr.getEntityId().toString());
                     sensorrecord.setSensor_type(fsr.getSensorType().toString());
                     sensorrecord.setNormal_maximum(fsr.getNormalMaximum());
                     sensorrecord.setNormal_minimum(fsr.getNormalMinimum());
                     sensorrecord.setSensor_owner_lun(fsr.getSensorOwnerLun());
                     sensorrecord.setSensor_base_unit(fsr.getSensorBaseUnit().toString());
                     sensorrecord.setAddress_type(fsr.getAddressType().toString());
                     sensorrecord.setEntity_instance_number(fsr.getEntityInstanceNumber());
                     sensorrecord.setEntity_physical(fsr.isEntityPhysical()+"");
                     sensorrecord.setLower_critical_threshold(fsr.getLowerCriticalThreshold());
                     sensorrecord.setLower_non_critical_threshold(fsr.getLowerNonCriticalThreshold());
                     sensorrecord.setLower_non_recoverable_threshold(fsr.getLowerNonRecoverableThreshold());
                     sensorrecord.setModifier_unit_usage(fsr.getModifierUnitUsage().toString());
                     sensorrecord.setNominal_reading(fsr.getNominalReading());
                     sensorrecord.setRate_unit(fsr.getRateUnit().toString());
                     sensorrecord.setSensor_direction(fsr.getSensorDirection().toString());
                     sensorrecord.setSensor_maximum_reading(fsr.getSensorMaximumReading());
                     sensorrecord.setSensor_minmum_reading(fsr.getSensorMinmumReading());
                     sensorrecord.setSensor_modifier_unit(fsr.getSensorModifierUnit().toString());
                     sensorrecord.setSensor_number(fsr.getSensorNumber());
                     sensorrecord.setUpper_critical_threshold(fsr.getUpperCriticalThreshold());
                     sensorrecord.setUpper_non_critical_threshold(fsr.getUpperNonCriticalThreshold());
                     sensorrecord.setUpper_non_recoverable_threshold(fsr.getUpperNonRecoverableThreshold());
                 }
                 if (record instanceof CompactSensorRecord) {
                	 sensorrecord.setRecord_type("CompactSensorRecord");
                 	CompactSensorRecord fsr = (CompactSensorRecord) record;
                     recordReadingId = TypeConverter.byteToInt(fsr.getSensorNumber());
                     sensorrecord.setName(fsr.getName());
                     sensorrecord.setEntity_id(fsr.getEntityId().toString());
                     sensorrecord.setSensor_type(fsr.getSensorType().toString());
                     sensorrecord.setSensor_owner_lun(fsr.getSensorOwnerLun());
                     sensorrecord.setSensor_base_unit(fsr.getSensorBaseUnit().toString());
                     
                     sensorrecord.setAddress_type(fsr.getAddressType().toString());
                     sensorrecord.setEntity_instance_number(fsr.getEntityInstanceNumber());
                     sensorrecord.setEntity_physical(fsr.isEntityPhysical()+"");
                     sensorrecord.setModifier_unit_usage(fsr.getModifierUnitUsage().toString());
                     sensorrecord.setRate_unit(fsr.getRateUnit().toString());
                     sensorrecord.setSensor_direction(fsr.getSensorDirection().toString());
                     sensorrecord.setSensor_modifier_unit(fsr.getSensorModifierUnit().toString());
                     sensorrecord.setSensor_number(fsr.getSensorNumber());
                 }
                 GetSensorReadingResponseData data2 = null;
                 try {
                     if (recordReadingId >= 0) {
                         try {
							data2 = (GetSensorReadingResponseData) connector
							         .sendMessage(handle, new GetSensorReading(IpmiVersion.V20, handle.getCipherSuite(),
							                 AuthenticationType.RMCPPlus, recordReadingId));
						} catch (Exception e) {
							System.out.println("这有点问题");
							sensorrecord.setCurrent_num(-1);
							//sensorrecord.setSensor_state_valid("false");
							sensorrecord.setState("状态异常");
							if (record instanceof FullSensorRecord) {
								fullList.add(sensorrecord);
							}else if(record instanceof CompactSensorRecord) {
								conpactList.add(sensorrecord);
							}
						}
                         if (record instanceof FullSensorRecord) {
                             FullSensorRecord rec = (FullSensorRecord) record;
                             //解析传感器读取的记录信息
                             sensorrecord.setCurrent_num(data2.getSensorReading(rec));
                         	List<ReadingType> events = data2.getStatesAsserted(rec.getSensorType(),
                                    rec.getEventReadingType());
                            StringBuilder s = new StringBuilder();
                            for (int i = 0; i < events.size(); ++i) {
                                s.append(events.get(i)).append(", ");
                            }
                            
                            sensorrecord.setState(data2.getSensorState().toString());
                            sensorrecord.setStatesAsserted(s.toString());
                            sensorrecord.setSensor_state_valid(data2.isSensorStateValid()+"");
                            fullList.add(sensorrecord);
                         }
                         if (record instanceof CompactSensorRecord) {
                         	CompactSensorRecord rec = (CompactSensorRecord) record;
                         	List<ReadingType> events = data2.getStatesAsserted(rec.getSensorType(),
                                     rec.getEventReadingType());
                             StringBuilder s = new StringBuilder();
                             for (int i = 0; i < events.size(); ++i) {
                                 s.append(events.get(i)).append(", ");
                             }
                             sensorrecord.setCurrent_num(data2.getPlainSensorReading());
                             sensorrecord.setState(data2.getSensorState().toString());
                             sensorrecord.setStatesAsserted(s.toString());
                             sensorrecord.setSensor_state_valid(data2.isSensorStateValid()+"");
                             conpactList.add(sensorrecord);
                         }
                     }
                 } catch (Exception e) {
                 }
             } catch (IPMIException e) {
                 System.out.println("Getting new reservation ID");
                 System.out.println("156: " + e.getMessage());
                 if (lastReservationId == reservationId || e.getCompletionCode() != CompletionCode.ReservationCanceled)
                     throw e;
                 lastReservationId = reservationId;

                 reservationId = ((ReserveSdrRepositoryResponseData) connector.sendMessage(handle, new ReserveSdrRepository(IpmiVersion.V20, handle.getCipherSuite(),
                         AuthenticationType.RMCPPlus))).getReservationId();
             }
         }

         // 关闭会话
         connector.closeSession(handle);
         // 关闭连接
         connector.closeConnection(handle);
         connector.tearDown();
        // System.out.println(list);
         listMap.put("fullList", fullList);
         listMap.put("conpactList", conpactList);
		return listMap;
    	
    }
    
  
    
    public static chassisStatus getChassisStatus(serversip serversip,int port) throws Exception{
    	
     	 chassisStatus chassisStatus = new chassisStatus();

	      IpmiConnector connector = new IpmiConnector(port);
	
	      ConnectionHandle handle = startSession(connector, InetAddress.getByName(serversip.getHostname()), serversip.getUsername(), serversip.getPassword(), "",
	              PrivilegeLevel.User);
	
	      connector.setTimeout(handle, 32750);
	      
	      GetChassisStatusResponseData data = (GetChassisStatusResponseData) connector.sendMessage(handle, new GetChassisStatus(IpmiVersion.V20,
	              handle.getCipherSuite(), AuthenticationType.RMCPPlus));
	
	      chassisStatus.setAc_failed(data.acFailed());
	      chassisStatus.setIs_chassis_identify_command_supported(data.isChassisIdentifyCommandSupported());
	      chassisStatus.setIs_chassis_intrusion_active(data.isChassisIntrusionActive());
	      chassisStatus.setCooling_fault_detected(data.coolingFaultDetected());
	      chassisStatus.setDrive_fault_detected(data.driveFaultDetected());
	      chassisStatus.setIs_front_panel_button_capabilities_set(data.isFrontPanelButtonCapabilitiesSet());
	      chassisStatus.setIs_front_panel_lockout_active(data.isFrontPanelLockoutActive());
	      chassisStatus.setIs_interlock(data.isInterlock());
	      chassisStatus.setIs_power_control_fault(data.isPowerControlFault());
	      chassisStatus.setIs_power_fault(data.isPowerFault());
	      chassisStatus.setIs_power_on(data.isPowerOn());
	      chassisStatus.setIs_power_overload(data.isPowerOverload());
	      chassisStatus.setWas_interlock(data.wasInterlock());
	      chassisStatus.setWas_ipmi_power_on(data.wasIpmiPowerOn());
	      chassisStatus.setWas_ipmi_power_on(data.wasPowerFault());
	      if(data.isFrontPanelButtonCapabilitiesSet()) {
	    	chassisStatus.setIs_diagnostic_interrupt_button_disable_allowed(data.isDiagnosticInterruptButtonDisableAllowed());
		    chassisStatus.setIs_diagnostic_interrupt_button_disabled(data.isDiagnosticInterruptButtonDisabled()); 
		    chassisStatus.setIs_power_off_button_disable_allowed(data.isPowerOffButtonDisableAllowed());
		    chassisStatus.setIs_power_off_button_disabled(data.isPowerOffButtonDisabled());
		    chassisStatus.setIs_reset_button_disable_allowed(data.isResetButtonDisableAllowed());
		    chassisStatus.setIs_reset_button_disabled(data.isResetButtonDisabled());
		    chassisStatus.setIs_standby_button_disable_allowed(data.isStandbyButtonDisableAllowed());
		    chassisStatus.setIs_standby_button_disabled(data.isStandbyButtonDisabled());
	      }
	      
	      // 关闭会话
	      connector.closeSession(handle);
	      // 关闭连接
	      connector.closeConnection(handle);
	      connector.tearDown();
	     // System.out.println(list);
  		return chassisStatus;
     	
     }
    
    //像这样的定位记录还有两张
    public static List<FruDeviceLocatorRecord> getFruDeviceLocatorRecord(serversip serversip,int port) throws Exception{
    	
     	 List<FruDeviceLocatorRecord> list = new ArrayList<FruDeviceLocatorRecord>();
     	 
     	 int nextRecId = 0;
     	 
     	int reservationId = 0;

          IpmiConnector connector = new IpmiConnector(port);

          ConnectionHandle handle = startSession(connector, InetAddress.getByName(serversip.getHostname()), serversip.getUsername(), serversip.getPassword(), "",
                  PrivilegeLevel.User);

          connector.setTimeout(handle, 32750);
          
          CipherSuite cs = getcs(connector,handle);
          
          while (nextRecId < MAX_REPO_RECORD_ID) {
              try {
                  /*SensorRecord record = getSensorData(connector, handle, reservationId,nextRecId);
                  GetSdrResponseData data = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(IpmiVersion.V20,
                          handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId, nextRecId));
                  nextRecId = data.getNextRecordId();*/
            	  Map<String, Object> map = new HashMap<String, Object>();
                  map = getSensorData(connector, handle, reservationId,nextRecId);
                  SensorRecord record = (SensorRecord)map.get("SensorRecord");
                      nextRecId = Integer.parseInt(map.get("nextRec").toString());
                  if (record instanceof FruDeviceLocatorRecord) {
                      FruDeviceLocatorRecord fruLocator = (FruDeviceLocatorRecord) record;
                      list.add(fruLocator);
                  }
              } catch (Exception e) {
                  System.out.println(e.getMessage());
                  reservationId = ((ReserveSdrRepositoryResponseData) connector.sendMessage(handle,
                          new ReserveSdrRepository(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus))).getReservationId();
              }
          }

          // 关闭会话
          connector.closeSession(handle);
          // 关闭连接
          connector.closeConnection(handle);
          connector.tearDown();
         // System.out.println(list);
  		return list;
     	
     }
    
    public static List<fruInfo> getFruInfo(serversip serversip,int port) throws Exception {
        List<ReadFruDataResponseData> fruData = new ArrayList<ReadFruDataResponseData>();
        List<fruInfo> list = new ArrayList<fruInfo>();
        IpmiConnector connector = new IpmiConnector(port);
        ConnectionHandle handle = startSession(connector, InetAddress.getByName(serversip.getHostname()), serversip.getUsername(), serversip.getPassword(), "",
                PrivilegeLevel.User);

        connector.setTimeout(handle, 32750);
        GetFruInventoryAreaInfoResponseData info = (GetFruInventoryAreaInfoResponseData) connector.sendMessage(handle,
                new GetFruInventoryAreaInfo(IpmiVersion.V20, handle.getCipherSuite(), AuthenticationType.RMCPPlus,
                		DEFAULT_FRU_ID));
        int size = info.getFruInventoryAreaSize();
        BaseUnit unit = info.getFruUnit();

        for (int i = 0; i < size; i += FRU_READ_PACKET_SIZE) {
            int cnt = FRU_READ_PACKET_SIZE;
            if (i + cnt > size) {
                cnt = size % FRU_READ_PACKET_SIZE;
            }
            try {

                ReadFruDataResponseData data = (ReadFruDataResponseData) connector.sendMessage(handle, new ReadFruData(
                        IpmiVersion.V20, handle.getCipherSuite(), AuthenticationType.RMCPPlus, DEFAULT_FRU_ID, unit, i, cnt));
                fruData.add(data);

            } catch (Exception e) {
                System.out.println("Error while sending ReadFruData command : " + e.getMessage());
            }
        }

        try {
        	List<FruRecord> records = ReadFruData.decodeFruData(fruData);
        	for (int i = 0; i < records.size(); i++) {
        		if(records.get(i) instanceof BoardInfo) {
        			BoardInfo BoardInfo = (BoardInfo)records.get(i);
        			fruInfo fruInfo = new fruInfo();
        			fruInfo.setName("BoardInfo");
        			String[] array= BoardInfo.getCustomBoardInfo();
        			StringBuilder StringBuilder = new StringBuilder();
        			for (int j = 0; j < array.length; j++) {
        				StringBuilder.append(array[i]);
					}
        			fruInfo.setCustomInfo(StringBuilder.toString());
        			fruInfo.setManufacturer(BoardInfo.getBoardManufacturer());
        			fruInfo.setMfgDate(BoardInfo.getMfgDate());
        			fruInfo.setPartNumber(BoardInfo.getBoardPartNumber());
        			fruInfo.setProductName(BoardInfo.getBoardProductName());
        			fruInfo.setSerialNumber(BoardInfo.getBoardSerialNumber());
        			list.add(fruInfo);
        		}
				if(records.get(i) instanceof ChassisInfo) {
					ChassisInfo ChassisInfo = (ChassisInfo)records.get(i);
					fruInfo fruInfo = new fruInfo();
					fruInfo.setName("ChassisInfo");
					String[] array= ChassisInfo.getCustomChassisInfo();
        			StringBuilder StringBuilder = new StringBuilder();
        			for (int j = 0; j < array.length; j++) {
        				StringBuilder.append(array[i]);
					}
        			fruInfo.setCustomInfo(StringBuilder.toString());
        			fruInfo.setPartNumber(ChassisInfo.getChassisPartNumber());
        			fruInfo.setSerialNumber(ChassisInfo.getChassisSerialNumber());
        			list.add(fruInfo);
				}
				if(records.get(i) instanceof ProductInfo) {
					ProductInfo ProductInfo = (ProductInfo)records.get(i);
					fruInfo fruInfo = new fruInfo();
        			fruInfo.setName("ProductInfo");
        			fruInfo.setCustomInfo(ProductInfo.getProductName());
        			fruInfo.setManufacturer(ProductInfo.getManufacturerName());
        			fruInfo.setModelNumber(ProductInfo.getProductModelNumber());
        			fruInfo.setProductName(ProductInfo.getProductName());
        			fruInfo.setSerialNumber(ProductInfo.getProductSerialNumber());
        			list.add(fruInfo);
				}
        	}
        } catch (Exception e) {
            System.out.println("Error while parsing FRU record: " + e.getMessage());
        }
        // 关闭会话
        connector.closeSession(handle);
        // 关闭连接
        connector.closeConnection(handle);
        connector.tearDown();
       // System.out.println(list);
		return list;
    }


    /**
     * 启动会话
     *
     * @param connector      连接�?
     * @param address        连接地址
     * @param username       用户�?
     * @param password       密码
     * @param bmcKey         二次校验密码
     * @param privilegeLevel 特权级别
     * @return
     * @throws Exception
     */
    public static ConnectionHandle startSession(IpmiConnector connector, InetAddress address, String username, String password, String bmcKey, PrivilegeLevel privilegeLevel) throws Exception {
    	
        ConnectionHandle handle = connector.createConnection(address);
        CipherSuite cs;
        try {
            // 获取远程主机支持的密码套�?
           /* List<CipherSuite> suites = connector.getAvailableCipherSuites(handle);

            if (suites.size() > 3) {
                cs = suites.get(3);
            } else if (suites.size() > 2) {
                cs = suites.get(2);
            } else if (suites.size() > 1) {
                cs = suites.get(1);
            } else {
                cs = suites.get(0);
            }*/
        	cs = getcs(connector,handle);
            // 选择密码套件并请求会话特权级�?
            connector.getChannelAuthenticationCapabilities(handle, cs, privilegeLevel);
            // 打开会话并验�?
           
            connector.openSession(handle, username, password, bmcKey.getBytes());
        } catch (Exception e) {
            connector.closeConnection(handle);
            throw e;
        }
        return handle;
    }

    /**
     * 获取传感器数�?
     *
     * @param connector     连接�?
     * @param handle        连接句柄
     * @param reservationId 预留id
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getSensorData(IpmiConnector connector, ConnectionHandle handle, int reservationId,int nextRecId)
            throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
            // BMC功能是有限的，这意味�?有时记录大小超过消息的最大大小�?�因为我们不知道这个记录的大小，�?以我们先把整个记录放在第�?位�??
            GetSdrResponseData data = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(IpmiVersion.V20,
                    handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId, nextRecId));
            // 如果获得完整的记录，我们从接收到的数据创建传感记�?
          // System.out.println(data);
            SensorRecord sensorDataToPopulate = SensorRecord.populateSensorRecord(data.getSensorRecordData());
            // 更新下一个记录的ID
            int nextRec = data.getNextRecordId();
            map.put("nextRec", nextRec);
            map.put("SensorRecord", sensorDataToPopulate);
            //nextRecId = MAX_REPO_RECORD_ID;
            //System.out.println("nextRecId----------------------------------------------"+nextRecId);
            return map;
        } catch (IPMIException e) {
            // 下面的错误代码意味着记录太大，无法在�?个块中发送�?�这意味�?我们�?要把数据分割成更小的部分�?
            if (e.getCompletionCode() == CompletionCode.CannotRespond
                    || e.getCompletionCode() == CompletionCode.UnspecifiedError) {
                System.out.println("Getting chunks");
                // 首先，我们得到记录的头来找出它的大小�?
                GetSdrResponseData data = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(
                        IpmiVersion.V20, handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId,
                        nextRecId, 0, INITIAL_CHUNK_SIZE));
                // 记录的大小是记录的第五字节�?�它没有考虑页眉的大小，�?以我们需要添加它�?
                int recSize = TypeConverter.byteToInt(data.getSensorRecordData()[4]) + HEADER_SIZE;
                int read = INITIAL_CHUNK_SIZE;

                byte[] result = new byte[recSize];

                System.arraycopy(data.getSensorRecordData(), 0, result, 0, data.getSensorRecordData().length);

                // 我们得到了剩余的记录块（注意超过记录大小），因为这将导致BMC的错误�??
                int num = 0;
                while (read < recSize) {
                	System.out.println(num+"==============");
                    int bytesToRead = CHUNK_SIZE;
                    if (recSize - read < bytesToRead) {
                        bytesToRead = recSize - read;
                    }
                    GetSdrResponseData part = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(
                            IpmiVersion.V20, handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId,
                            nextRecId, read, bytesToRead));

                    System.arraycopy(part.getSensorRecordData(), 0, result, read, bytesToRead);
                    System.out.println("Received part");
                    read += bytesToRead;
                }

                // �?后，用收集的数据填充传感器记录�??
                SensorRecord sensorDataToPopulate = SensorRecord.populateSensorRecord(result);
                // 更新下一个记录的ID
                nextRecId = data.getNextRecordId();
                //nextRecId = MAX_REPO_RECORD_ID;
                //System.out.println("nextRecId======================================="+nextRecId);
                map.put("SensorRecord", sensorDataToPopulate);
                return map;
            } else {
                throw e;
            	//return null;
            }
        } catch (Exception e) {
            throw e;
        	//return null;
        }
		
    }
    public static SensorRecord getSensorDat(IpmiConnector connector, ConnectionHandle handle, int reservationId,int nextRecId)
            throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
            // BMC功能是有限的，这意味�?有时记录大小超过消息的最大大小�?�因为我们不知道这个记录的大小，�?以我们先把整个记录放在第�?位�??
            GetSdrResponseData data = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(IpmiVersion.V20,
                    handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId, nextRecId));
            // 如果获得完整的记录，我们从接收到的数据创建传感记�?
          // System.out.println(data);
            SensorRecord sensorDataToPopulate = SensorRecord.populateSensorRecord(data.getSensorRecordData());
            // 更新下一个记录的ID
            int nextRec = data.getNextRecordId();
            //nextRecId = MAX_REPO_RECORD_ID;
            //System.out.println("nextRecId----------------------------------------------"+nextRecId);
            return sensorDataToPopulate;
        } catch (IPMIException e) {
            // 下面的错误代码意味着记录太大，无法在�?个块中发送�?�这意味�?我们�?要把数据分割成更小的部分�?
            if (e.getCompletionCode() == CompletionCode.CannotRespond
                    || e.getCompletionCode() == CompletionCode.UnspecifiedError) {
                System.out.println("Getting chunks");
                // 首先，我们得到记录的头来找出它的大小�?
                GetSdrResponseData data = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(
                        IpmiVersion.V20, handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId,
                        nextRecId, 0, INITIAL_CHUNK_SIZE));
                // 记录的大小是记录的第五字节�?�它没有考虑页眉的大小，�?以我们需要添加它�?
                int recSize = TypeConverter.byteToInt(data.getSensorRecordData()[4]) + HEADER_SIZE;
                int read = INITIAL_CHUNK_SIZE;

                byte[] result = new byte[recSize];

                System.arraycopy(data.getSensorRecordData(), 0, result, 0, data.getSensorRecordData().length);

                // 我们得到了剩余的记录块（注意超过记录大小），因为这将导致BMC的错误�??
                int num = 0;
                while (read < recSize) {
                	System.out.println(num+"==============");
                    int bytesToRead = CHUNK_SIZE;
                    if (recSize - read < bytesToRead) {
                        bytesToRead = recSize - read;
                    }
                    GetSdrResponseData part = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(
                            IpmiVersion.V20, handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId,
                            nextRecId, read, bytesToRead));

                    System.arraycopy(part.getSensorRecordData(), 0, result, read, bytesToRead);
                    System.out.println("Received part");
                    read += bytesToRead;
                }

                // �?后，用收集的数据填充传感器记录�??
                SensorRecord sensorDataToPopulate = SensorRecord.populateSensorRecord(result);
                // 更新下一个记录的ID
                nextRecId = data.getNextRecordId();
                //nextRecId = MAX_REPO_RECORD_ID;
                //System.out.println("nextRecId======================================="+nextRecId);
                return sensorDataToPopulate;
            } else {
                throw e;
            	//return null;
            }
        } catch (Exception e) {
            throw e;
        	//return null;
        }
		
    }
    public static void getSensorData1(IpmiConnector connector, ConnectionHandle handle) throws Exception
             {
    	
    	int nextRecId = 0;
    	
    	while (nextRecId < MAX_REPO_RECORD_ID) {
    		GetSelEntryResponseData data = (GetSelEntryResponseData) connector.sendMessage(handle, new GetSelEntry(IpmiVersion.V20,
                    handle.getCipherSuite(), AuthenticationType.RMCPPlus, 0, nextRecId));
    		nextRecId = data.getNextRecordId();
    		//System.out.println("nextRecId"+nextRecId);
        	//System.out.println("data.getSelRecord()"+data.getSelRecord());
    	}
    	//SelRecord SelRecord = SelRecord.populateSelRecord(data.getReservationId());
        	//System.out.println(data);
        	
          //  System.out.println(sensorDataToPopulate);
    }
}
