/*
 * BaseCompatibilityInfo.java 
 * Created on 2011-08-16
 *
 * Copyright (c) Verax Systems 2011.
 * All rights reserved.
 *
 * This software is furnished under a license. Use, duplication,
 * disclosure and all other uses are restricted to the rights
 * specified in the written license agreement.
 */
package com.veraxsystems.vxipmi.coding.commands.fru.record;

import com.veraxsystems.vxipmi.coding.commands.sdr.record.EntityId;
import com.veraxsystems.vxipmi.common.TypeConverter;

/**
 * Base Compatibility Information record from FRU Multi Record Area
 * ����FRU���¼����Ļ�����������Ϣ��¼
 */
public class BaseCompatibilityInfo extends MultiRecordInfo {

	private int manufacturerId;//������

	private EntityId entityId;

	private int compatibilityBase;//�����Եײ�

	private int codeStart;//��������

	private byte[] codeRangeMasks;//���뷶Χ�����

	/**
	 * Creates and populates record
	 * ����������¼
	 * 
	 * @param fruData
	 *            - raw data containing record
	 *            ������¼��ԭʼ����
	 * @param offset
	 *            - offset to the record in the data
	 *            ��¼���ݵ�ƫ����
	 * @param length
	 *            - length of the record
	 */
	public BaseCompatibilityInfo(byte[] fruData, int offset, int length) {
		super();
		// TODO: Test when server containing such records will be available
		//���԰��������¼�ķ�������ʱ����

		byte[] buffer = new byte[4];

		System.arraycopy(fruData, offset, buffer, 0, 3);
		buffer[3] = 0;

		manufacturerId = TypeConverter.littleEndianByteArrayToInt(buffer);
		entityId = EntityId.parseInt(TypeConverter
				.byteToInt(fruData[offset + 3]));
		compatibilityBase = TypeConverter.byteToInt(fruData[offset + 4]);
		codeStart = TypeConverter.byteToInt(fruData[offset + 5]) & 0x7f;
		codeRangeMasks = new byte[length - 6];
		System.arraycopy(fruData, 6, codeRangeMasks, 0, length - 6);
	}

	public int getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public EntityId getEntityId() {
		return entityId;
	}

	public void setEntityId(EntityId entityId) {
		this.entityId = entityId;
	}

	public int getCompatibilityBase() {
		return compatibilityBase;
	}

	public void setCompatibilityBase(int compatibilityBase) {
		this.compatibilityBase = compatibilityBase;
	}

	public int getCodeStart() {
		return codeStart;
	}

	public void setCodeStart(int codeStart) {
		this.codeStart = codeStart;
	}

	public byte[] getCodeRangeMasks() {
		return codeRangeMasks;
	}

	public void setCodeRangeMasks(byte[] codeRangeMasks) {
		this.codeRangeMasks = codeRangeMasks;
	}

}
