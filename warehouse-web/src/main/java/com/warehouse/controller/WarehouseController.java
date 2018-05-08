package com.warehouse.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 仓库管理的controller
 * 2018.5.2
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.warehouse.bean.Log;
import com.warehouse.bean.Warehouse;
import com.warehouse.common.WMessage;
import com.warehouse.common.WResponse;
import com.warehouse.service.LogService;
import com.warehouse.service.WarehouseService;
import com.warehouse.utils.TimeUtil;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

	@Autowired
	private WarehouseService warehouseService;
	
	@Autowired
	private LogService logService;
	
	@ResponseBody
	@RequestMapping(value = "/addWarehouse", method = RequestMethod.POST)
	public WResponse addWarehouse(Warehouse warehouse,HttpSession session) throws Exception {
		WResponse response=new WResponse();
		response.setMessage(WMessage.MSG_FAIL);
		warehouse.setOperator((String)session.getAttribute("username"));
		if(warehouseService.checkWarehouseExists(warehouse.getNum())) {
			response.setMessage(WMessage.MSG_WAREHOUSE_EXISTS);
			return response;
		}
		
		Integer num=warehouseService.insertWarehouse(warehouse);
		if(num>0) {
			response.setMessage(WMessage.MSG_SUCCESS);
		}
		
		Log log=new Log();
		log.setOperatorName((String)session.getAttribute("username"));
		log.setOperationType(WMessage.MSG_OPREATION_CHANGE);
		log.setOperationDetail("用户： "+session.getAttribute("username")
								+" 等级： "+session.getAttribute("level")
								+" 于 "+TimeUtil.getNowerTime()
								+" 添加仓库： "+warehouse.toString()
								+" 结果： "+ response.getMessage());
		logService.insertLog(log);
		
		return response;
	}
}
