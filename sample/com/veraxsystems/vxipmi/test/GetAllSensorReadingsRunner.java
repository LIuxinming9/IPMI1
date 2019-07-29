package com.veraxsystems.vxipmi.test;

import java.net.InetAddress;
import java.util.List;

import com.veraxsystems.vxipmi.api.async.ConnectionHandle;
import com.veraxsystems.vxipmi.api.sync.IpmiConnector;
import com.veraxsystems.vxipmi.coding.commands.IpmiVersion;
import com.veraxsystems.vxipmi.coding.commands.PrivilegeLevel;
import com.veraxsystems.vxipmi.coding.commands.sdr.GetSdr;
import com.veraxsystems.vxipmi.coding.commands.sdr.GetSdrResponseData;
import com.veraxsystems.vxipmi.coding.commands.sdr.GetSensorReading;
import com.veraxsystems.vxipmi.coding.commands.sdr.GetSensorReadingResponseData;
import com.veraxsystems.vxipmi.coding.commands.sdr.ReserveSdrRepository;
import com.veraxsystems.vxipmi.coding.commands.sdr.ReserveSdrRepositoryResponseData;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.CompactSensorRecord;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.FullSensorRecord;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.RateUnit;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.ReadingType;
import com.veraxsystems.vxipmi.coding.commands.sdr.record.SensorRecord;
import com.veraxsystems.vxipmi.coding.payload.CompletionCode;
import com.veraxsystems.vxipmi.coding.payload.lan.IPMIException;
import com.veraxsystems.vxipmi.coding.protocol.AuthenticationType;
import com.veraxsystems.vxipmi.coding.security.CipherSuite;
import com.veraxsystems.vxipmi.common.PropertiesManager;
import com.veraxsystems.vxipmi.common.TypeConverter;

public class GetAllSensorReadingsRunner {

	/**
	 * GetAllSensorReadingsRunner ��ȡ���д���������
	*�������һ����¼ID (FFFFh)��ֵ��Ϊ�˼���ȫ��SDR��¼���ͻ��˱����ظ�
	*��ȡSDR��¼��ֱ��MAX_REPO_RECORD_ID��Ϊ��һ����¼ID����Ϊֹ
	* IPMI�淶ver��33.12��2.0
	*/

    private static final int MAX_REPO_RECORD_ID = 65535;

    private static final String hostname = "192.168.1.1";

    private static final String username = "user";

    private static final String password = "pass";

    /**
     * ��ȡ��¼ͷ�ʹ�С�ĳ�ʼGetSdr��Ϣ�Ĵ�С
     */
    private static final int INITIAL_CHUNK_SIZE = 8;

    /**
    *���Сȡ����IPMI�������Ļ�������С�������ֵ��������ܡ������������
	��GetSdr�����ڼ䣬CHUNK_SIZEӦ���Ǵ���ļ��١�
     */
    private static final int CHUNK_SIZE = 16;

    /**
     * SDR��¼ͷ�Ĵ�С
     */
    private static final int HEADER_SIZE = 5;

    private int nextRecId;

    /**
     * @param args
     */
    public static void main(String[] args) {
        GetAllSensorReadingsRunner runner = new GetAllSensorReadingsRunner();
        try {
            // ����Ĭ�ϳ�ʱֵ
            PropertiesManager.getInstance().setProperty("timeout", "2500");
            runner.doRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doRun() throws Exception {
    	// Id 0��ʾSDR�еĵ�һ����¼�����������Դ����м���id
    	//��¼�������Ǳ���֯��һ���б��У�û��BMC����
    	//�����Ƕ�Ū�á�
        nextRecId = 0;

       //��Щ�������������ޱ����ػ�ȡ��������¼���������ǳ���������
       //��������
        int reservationId = 0;
        int lastReservationId = -1;

        // Create the connector
        IpmiConnector connector = new IpmiConnector(0);

       //������Զ�������ĻỰ�����Ǽ�����������
       //�����֤δ���ã����BMC��ԿΪ��(�����
       // #startSession��ȡ��ϸ��Ϣ)��

        ConnectionHandle handle = startSession(connector, InetAddress.getByName(hostname), username, password, "",
                PrivilegeLevel.User);

        // ���Ĵ��ض����ӵĳ�ʱ(�������ӵ�Ĭ��ֵ����)
        connector.setTimeout(handle, 2750);
        
       //���ǵõ����������ݣ�ֱ����������ID = 65535������ζ��
       //�������һ�ų�Ƭ��

        while (nextRecId < MAX_REPO_RECORD_ID) {

            SensorRecord record = null;

            try {
            	//��䴫������¼����ȡ��һ����¼��ID
            	//�洢��(��ϸ��Ϣ��μ�#getSensorData)��
                record = getSensorData(connector, handle, reservationId);

                int recordReadingId = -1;

               //���Ǽ���յ��ļ�¼��FullSensorRecord����FullSensorRecord
              // CompactSensorRecord����Ϊ��Щ�����ж���
              //��֮���(��ϸ��Ϣ��μ�IPMI�淶)��

                if (record instanceof FullSensorRecord) {
                    FullSensorRecord fsr = (FullSensorRecord) record;
                    recordReadingId = TypeConverter.byteToInt(fsr.getSensorNumber());
                    System.out.println(fsr.getName());

                } else if (record instanceof CompactSensorRecord) {
                    CompactSensorRecord csr = (CompactSensorRecord) record;
                    recordReadingId = TypeConverter.byteToInt(csr.getSensorNumber());
                    System.out.println(csr.getName());
                }

              //������ǵļ�¼�й����Ķ��������Ǿͻ��յ�Ϊ��������

                GetSensorReadingResponseData data2 = null;
                try {
                    if (recordReadingId >= 0) {
                        data2 = (GetSensorReadingResponseData) connector
                                .sendMessage(handle, new GetSensorReading(IpmiVersion.V20, handle.getCipherSuite(),
                                        AuthenticationType.RMCPPlus, recordReadingId));
                        if (record instanceof FullSensorRecord) {
                            FullSensorRecord rec = (FullSensorRecord) record;
                          //ʹ�ü���������Ϣ������������ȡ
                          //���Դ�������¼������
                          // FullSensorRecord#calcFormula��ȡ��ϸ��Ϣ��

                            System.out.println(data2.getSensorReading(rec) + " " + rec.getSensorBaseUnit().toString()
                                    + (rec.getRateUnit() != RateUnit.None ? " per " + rec.getRateUnit() : ""));
                        }
                        if (record instanceof CompactSensorRecord) {
                            CompactSensorRecord rec = (CompactSensorRecord) record;
                            // ��ȡ���������Ե�״̬
                            List<ReadingType> events = data2.getStatesAsserted(rec.getSensorType(),
                                    rec.getEventReadingType());
                            String s = "";
                            for (int i = 0; i < events.size(); ++i) {
                                s += events.get(i) + ", ";
                            }
                            System.out.println(s);

                        }

                    }
                } catch (IPMIException e) {
                    if (e.getCompletionCode() == CompletionCode.DataNotPresent) {
                        e.printStackTrace();
                    } else {
                        throw e;
                    }
                }
            } catch (IPMIException e) {

                System.out.println("Getting new reservation ID");

                System.out.println("156: " + e.getMessage());

               //�����ȡ����������ʧ�ܣ����Ǽ�����Ƿ��Ѿ�ʧ��
               //�����Ԥ���š�

                if (lastReservationId == reservationId)
                    throw e;
                lastReservationId = reservationId;

              //���ʧ�ܵ�ԭ����ȡ����
              //Ԥ�������ǵõ��µ�Ԥ��id�����ԡ������
              //�ڻ�ȡ���д������Ĺ����лᷢ���ܶ�Σ���ΪBMC����
              //�����лỰ���ɻỰ��Ч��������»Ự
              //���֡�

                reservationId = ((ReserveSdrRepositoryResponseData) connector
                        .sendMessage(handle, new ReserveSdrRepository(IpmiVersion.V20, handle.getCipherSuite(),
                                AuthenticationType.RMCPPlus))).getReservationId();
            }
        }

        // Close the session
        connector.closeSession(handle);
        System.out.println("Session closed");
        // Close the connection
        connector.closeConnection(handle);
        connector.tearDown();
        System.out.println("Connection manager closed");
    }

    public ConnectionHandle startSession(IpmiConnector connector, InetAddress address, String username,
            String password, String bmcKey, PrivilegeLevel privilegeLevel) throws Exception {

        // �������ӵľ�������������ӵı�ʶ��
        ConnectionHandle handle = connector.createConnection(address);

        CipherSuite cs;

        try {
            // ��ȡԶ������֧�ֵ������׼�
            List<CipherSuite> suites = connector.getAvailableCipherSuites(handle);

            if (suites.size() > 3) {
                cs = suites.get(3);
            } else if (suites.size() > 2) {
                cs = suites.get(2);
            } else if (suites.size() > 1) {
                cs = suites.get(1);
            } else {
                cs = suites.get(0);
            }
          //ѡ�������׼��������Ȩ�޼���
          //�Ự

            connector.getChannelAuthenticationCapabilities(handle, cs, privilegeLevel);

            // �򿪻Ự�����������֤
            connector.openSession(handle, username, password, bmcKey.getBytes());
        } catch (Exception e) {
            connector.closeConnection(handle);
            throw e;
        }

        return handle;
    }

    public SensorRecord getSensorData(IpmiConnector connector, ConnectionHandle handle, int reservationId)
            throws Exception {
        try {
        	// BMC�����������޵�-����ζ����ʱ
        	//��¼��С������Ϣ������С����Ϊ���ǲ�
        	//֪�����ų�Ƭ�ĳߴ磬���Ǿ���Ū��
        	//�ȳ�һ����

            GetSdrResponseData data = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(IpmiVersion.V20,
                    handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId, nextRecId));
           //�����ȡ������¼�ɹ������ǽ�����SensorRecord
           //���յ����ݡ�

            SensorRecord sensorDataToPopulate = SensorRecord.populateSensorRecord(data.getSensorRecordData());
            // ����������һ����¼��ID
            nextRecId = data.getNextRecordId();
            return sensorDataToPopulate;
        } catch (IPMIException e) {
            // System.out.println(e.getCompletionCode() + ": " + e.getMessage());
        	//����Ĵ��������ζ�ż�¼̫����
        	//��һ�����з��͡�����ζ��������Ҫ�ָ�����
        	//С������

            if (e.getCompletionCode() == CompletionCode.CannotRespond
                    || e.getCompletionCode() == CompletionCode.UnspecifiedError) {
                System.out.println("Getting chunks");
                // ���ȣ����ǻ�ȡ��¼�ı������ҳ����Ĵ�С��
                GetSdrResponseData data = (GetSdrResponseData) connector.sendMessage(handle, new GetSdr(
                        IpmiVersion.V20, handle.getCipherSuite(), AuthenticationType.RMCPPlus, reservationId,
                        nextRecId, 0, INITIAL_CHUNK_SIZE));
              //��¼��СΪ��¼��5�ֽڡ�������Ҫ
              //���ǵ�����Ĵ�С��������Ҫ�������

                int recSize = TypeConverter.byteToInt(data.getSensorRecordData()[4]) + HEADER_SIZE;
                int read = INITIAL_CHUNK_SIZE;

                byte[] result = new byte[recSize];

                System.arraycopy(data.getSensorRecordData(), 0, result, 0, data.getSensorRecordData().length);

              //���ǰ�����ļ�¼�ֳɼ���(С��
              //������¼��С����Ϊ��ᵼ��BMC
              //����

                while (read < recSize) {
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

              //����������ռ�����������䴫������¼
              //���ݡ���

                SensorRecord sensorDataToPopulate = SensorRecord.populateSensorRecord(result);
                // ����������һ����¼��ID
                nextRecId = data.getNextRecordId();
                return sensorDataToPopulate;
            } else {
                throw e;
            }
        } catch (Exception e) {
            throw e;
        }
    }

}
