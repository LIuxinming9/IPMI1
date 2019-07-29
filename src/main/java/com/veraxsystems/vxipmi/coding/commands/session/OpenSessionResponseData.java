/*
 * OpenSessionResponseData.java 
 * Created on 2011-08-25
 *
 * Copyright (c) Verax Systems 2011.
 * All rights reserved.
 *
 * This software is furnished under a license. Use, duplication,
 * disclosure and all other uses are restricted to the rights
 * specified in the written license agreement.
 */
package com.veraxsystems.vxipmi.coding.commands.session;

import com.veraxsystems.vxipmi.coding.commands.CommandsConstants;
import com.veraxsystems.vxipmi.coding.commands.PrivilegeLevel;
import com.veraxsystems.vxipmi.coding.commands.ResponseData;

/**
 * A wrapper for Open Session command response
 * ���ڴ򿪻Ự������Ӧ�İ�װ��

 * 
 * @see OpenSession
 */
public class OpenSessionResponseData implements ResponseData {
	@Override
	public String toString() {
		return "OpenSessionResponseData [messageTag=" + messageTag + ", statusCode=" + statusCode + ", privilegeLevel="
				+ privilegeLevel + ", remoteConsoleSessionId=" + remoteConsoleSessionId + ", managedSystemSessionId="
				+ managedSystemSessionId + ", authenticationAlgorithm=" + authenticationAlgorithm
				+ ", integrityAlgorithm=" + integrityAlgorithm + ", confidentialityAlgorithm="
				+ confidentialityAlgorithm + "]";
	}

	/**
	 * The BMC returns the Message Tag value that was passed by the remote
	 * console in the Open Session Request message
	 * BMC����Զ�̿���̨�ڿ��ŻỰ������Ϣ�д��ݵ���Ϣ���ֵ
	 */
	private byte messageTag;

	/**
	 * Identifies the status of the previous message. If the previous message
	 * generated an error, then only the Status Code, Reserved, and Remote
	 * Console Session ID fields are returned.
	 * ��ʶǰһ����Ϣ��״̬�����ǰһ����Ϣ���ɴ�����ֻ����״̬���롢�����ĺ�Զ�̿���̨�ỰID�ֶΡ�
	 */
	private byte statusCode;

	/**
	 * Indicates the Maximum Privilege Level allowed for the session.
	 * ָʾ�Ự����������Ȩ����
	 */
	private byte privilegeLevel;

	/**
	 * The Remote Console Session ID specified by RMCP+ Open Session Request
	 * message associated with this response.
	 * RMCP+ Open Session Request messageָ����Զ�̿���̨�ỰID�����Ӧ������
	 */
	private int remoteConsoleSessionId;

	/**
	 * The Session ID selected by the Managed System for this new session.
	 * �й�ϵͳΪ����»Ựѡ��ĻỰID��
	 */
	private int managedSystemSessionId;

	/**
	 * This payload defines the authentication algorithm proposal selected by
	 * the Managed System to be used for this session
	 * ����Ч���ض������й�ϵͳѡ��������֤�㷨���飬���ڴ˻Ự
	 */
	private byte authenticationAlgorithm;

	/**
	 * This payload defines the integrity algorithm proposal selected by the
	 * Managed System to be used for this session
	 * ����Ч���ض����˱�����ϵͳѡ����������㷨���飬�����ڴ˻Ự
	 */
	private byte integrityAlgorithm;

	/**
	 * This payload defines the confidentiality algorithm proposal selected by
	 * the Managed System to be used for this session
	 * ������ض����˱������ϵͳΪ����Ựѡ��Ļ������㷨����
	 */
	private byte confidentialityAlgorithm;

	public void setMessageTag(byte messageTag) {
		this.messageTag = messageTag;
	}

	public byte getMessageTag() {
		return messageTag;
	}

	public void setStatusCode(byte statusCode) {
		this.statusCode = statusCode;
	}

	public byte getStatusCode() {
		return statusCode;
	}

	public void setPrivilegeLevel(byte privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}

	public PrivilegeLevel getPrivilegeLevel() {
		switch (privilegeLevel) {
		case CommandsConstants.AL_CALLBACK:
			return PrivilegeLevel.Callback;
		case CommandsConstants.AL_USER:
			return PrivilegeLevel.User;
		case CommandsConstants.AL_OPERATOR:
			return PrivilegeLevel.Operator;
		case CommandsConstants.AL_ADMINISTRATOR:
			return PrivilegeLevel.Administrator;
		default:
			throw new IllegalArgumentException("Invalid privilege level");
		}
	}

	public void setRemoteConsoleSessionId(int remoteConsoleSessionId) {
		this.remoteConsoleSessionId = remoteConsoleSessionId;
	}

	public int getRemoteConsoleSessionId() {
		return remoteConsoleSessionId;
	}

	public void setManagedSystemSessionId(int managedSystemSessionId) {
		this.managedSystemSessionId = managedSystemSessionId;
	}

	public int getManagedSystemSessionId() {
		return managedSystemSessionId;
	}

	public void setAuthenticationAlgorithm(byte authenticationAlgorithm) {
		this.authenticationAlgorithm = authenticationAlgorithm;
	}

	public byte getAuthenticationAlgorithm() {
		return authenticationAlgorithm;
	}

	public void setIntegrityAlgorithm(byte integrityAlgorithm) {
		this.integrityAlgorithm = integrityAlgorithm;
	}

	public byte getIntegrityAlgorithm() {
		return integrityAlgorithm;
	}

	public void setConfidentialityAlgorithm(byte confidentialityAlgorithm) {
		this.confidentialityAlgorithm = confidentialityAlgorithm;
	}

	public byte getConfidentialityAlgorithm() {
		return confidentialityAlgorithm;
	}
}
