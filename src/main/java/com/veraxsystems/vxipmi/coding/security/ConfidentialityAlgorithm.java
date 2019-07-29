/*
 * ConfidentialityAlgorithm.java 
 * Created on 2011-07-25
 *
 * Copyright (c) Verax Systems 2011.
 * All rights reserved.
 *
 * This software is furnished under a license. Use, duplication,
 * disclosure and all other uses are restricted to the rights
 * specified in the written license agreement.
 */
package com.veraxsystems.vxipmi.coding.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

/**
 * Interface for Confidentiality Algorithms. All classes extending this one must
 * implement constructor(byte[]).
 * �ӿڵĻ������㷨����չ�����������඼����ʵ�ֹ��캯��(byte[])��
 */
public abstract class ConfidentialityAlgorithm {
	protected byte[] sik;

	/**
	 * Initializes Confidentiality Algorithm
	 * 
	 * @param sik
	 *            - Session Integrity Key calculated during the opening of the
	 *            session or user password if 'one-key' logins are enabled.
	 *            ������á�һ������¼�����ڻỰ���û�������ڼ����Ự��������Կ��
	 * @throws InvalidKeyException
	 *             - when initiation of the algorithm fails
	 * @throws NoSuchAlgorithmException
	 *             - when initiation of the algorithm fails
	 * @throws NoSuchPaddingException
	 *             - when initiation of the algorithm fails
	 */
	public void initialize(byte[] sik) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		this.sik = sik;
	}

	/**
	 * Returns the algorithm's ID.
	 */
	public abstract byte getCode();

	/**
	 * Encrypts the data.
	 * �������ݡ�
	 * @param data
	 *            - payload to be encrypted
	 *            Ҫ���ܵĸ���
	 * @return encrypted data encapsulated in COnfidentiality Header and
	 *         Trailer.
	 *         ��װ�ڱ���ͷ��β�ļ��еļ������ݡ�
	 * @throws InvalidKeyException
	 *             - when initiation of the algorithm fails
	 */
	public abstract byte[] encrypt(byte[] data) throws InvalidKeyException;

	/**
	 * Decrypts the data.
	 * �������ݡ�
	 * @param data
	 *            - encrypted data encapsulated in COnfidentiality Header and
	 *            Trailer.
	 *            ��װ�ڱ���ͷ��β�ļ��еļ������ݡ�
	 * @return decrypted data.
	 * @throws IllegalArgumentException 
	 *             - when initiation of the algorithm fails
	 */
	public abstract byte[] decrypt(byte[] data) throws IllegalArgumentException;

	/**
	 * Calculates size of the confidentiality header and trailer specific for
	 * the algorithm.
	 * �����ض��ڸ��㷨�Ļ��ܱ����β���Ĵ�С��
	 * 
	 * @param payloadSize
	 *            - size of the data that will be encrypted
	 */
	public abstract int getConfidentialityOverheadSize(int payloadSize);
}