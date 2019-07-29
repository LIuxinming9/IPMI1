/*
 * ReserveSdrRepositoryResponseData.java 
 * Created on 2011-08-03
 *
 * Copyright (c) Verax Systems 2011.
 * All rights reserved.
 *
 * This software is furnished under a license. Use, duplication,
 * disclosure and all other uses are restricted to the rights
 * specified in the written license agreement.
 */
package com.veraxsystems.vxipmi.coding.commands.sdr;

import com.veraxsystems.vxipmi.coding.commands.ResponseData;

/**
 * Wrapper for Reserve SDR Repository command response.
 * ����Reserve SDR�洢��������Ӧ�İ�װ����
 */
public class ReserveSdrRepositoryResponseData implements ResponseData {

	@Override
	public String toString() {
		return "ReserveSdrRepositoryResponseData [reservationId=" + reservationId + "]";
	}

	/**
	 * This value is required in other requests, such as the 'Delete SDR'
	 * command. These commands will not execute unless the correct Reservation
	 * ID value is provided.
	 * ���ֵ�������������Ǳ���ģ�����'Delete SDR'���
	 * �����ṩ��ȷ��reservation ationidֵ��������Щ�����ִ�С�
	 */
	private int reservationId;

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public int getReservationId() {
		return reservationId;
	}
}
