package com.gydz.user.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gydz.user.mapper.OperationLogMapper;
import com.gydz.user.model.OperationLog;
import com.gydz.user.model.QueryParam;

@Service("operLogService")
public class OperLogServiceImpl implements OperLogService {
	
    @Resource
    private OperationLogMapper operLogMapper;

    /**
     * ��ѯ����ϵͳ��־
     */
	@Override
	public List<OperationLog> getAllOperLogs() {
		return operLogMapper.getAllOperLogs();
	}

	/**
	 * ���ݲ�ѯ������ȡϵͳ��־
	 */
	@Override
	public List<OperationLog> getOperLogByKeyWord(QueryParam param) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("keyword", param.getKeyword());
		map.put("datemin", param.getDatemin());
		map.put("datemax", param.getDatemax());
		return operLogMapper.getOperLogByKeyword(map);
	}

	/**
	 * ����ϵͳ��־
	 */
	@Override
	public void addOperLog(OperationLog user) {
		operLogMapper.addOperLog(user);
	}
}

