                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Src = fileName.substring(fileName.indexOf("."));
			InputStream is = part.getInputStream();// 得到文件流
			ExcelUtil eu = new ExcelUtil();

			if (Constants.EXCEL_VERSION_2003 == eu.checkExcelVersion(pathSrc)) // 2003及以前版本
			{
				HSSFWorkbook wb = new HSSFWorkbook(is);
				HSSFSheet childSheet = wb.getSheetAt(0); // 只读取第一个工作薄
				int rowNum = childSheet.getLastRowNum() + 1;
				HSSFCell cell1; // 资源串码
				for (int x = 1; x < rowNum; x++) {

					HSSFRow row = childSheet.getRow(x);
					if (null == row) {
						continue;
					}
					cell1 = row.getCell(0);

					// 如果资源串码为空，则不处理本行数据
					if (null == cell1) {
						continue;
					}
					// judgeRemovedPointZero 去掉数值后面的".0",如：111111.0 --> 111111
					String cellValue1 = StringUtils.removedPointZero(cell1).trim();

					if (!StringUtils.isEmpty(cellValue1)) {
						Map cell = new HashMap();
						cell.put("MKT_RES_INST_CODE", cellValue1);
						outList.add(cell);
					}
				}
			} else if (Constants.EXCEL_VERSION_2007 == eu
					.checkExcelVersion(pathSrc)) {
				// 处理excel 2007 格式的文件导入
				XSSFWorkbook xwb = new XSSFWorkbook(is);
				XSSFSheet sheet = xwb.getSheetAt(0);
				XSSFRow row;

				Cell cell1;
				// 循环输出表格中的内容
				int end = sheet.getPhysicalNumberOfRows();
				for (int start = sheet.getFirstRowNum() + 1; start < end; start++) {

					row = sheet.getRow(start);
					if (null == row) {
						continue;
					}
					cell1 = row.getCell(0);
					// 如果资源串码为空，则不处理本行数据
					if (null == cell1) {
						continue;
					}

					// judgeRemovedPointZero 如果密码为数值时，去掉数值后面的".0",如：111111.0 --> 111111
					String cellValue1 = StringUtils.removedPointZero(cell1).trim();
					if (!StringUtils.isEmpty(cellValue1)) {
						Map cell = new HashMap();
						cell.put("MKT_RES_INST_CODE", cellValue1);
						outList.add(cell);
					}
				}
			}
			// 文件不支持
			else {
				retMap.put("code", Constants.FAIL_CODE);
				retMap.put("desc", "非常抱歉，不支持当前文件格式！");
				return retMap;
			}
		}	
		int size = outList.size();
		if (size == 0) {
			retMap.put("code", Constants.FAIL_CODE);
			retMap.put("desc", "文件格式不正确,获取有效入库资源为空！");
		}else if(size>500)
		{
			retMap.put("code", Constants.FAIL_CODE);
			retMap.put("desc", "非常抱歉，目前仅支持直接入库记录数在500内!");
		}
		else {
			retMap.put("code", Constants.SUCCESS_CODE);
			retMap.put("content", outList);
		}
		return retMap;
	}
	
	/**
	 * 无序订单生成
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void notOrderedResAllot(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		Map retMap =null;
		try {
			logger.info("ResAllotAction.notOrderedResAllot: 无序订单生成");
			Map map=(Map) this.dataToMap(request);
			map.put("SYSTEM_USER_ID", 1);
			retMap = resAllotService.notOrderedResAllot(map);
			if(retMap.get("error")==null){
				map.put("BATCH_CD", retMap.get("BATCH_CD"));
				map.put("batchID", retMap.get("batchID"));
				map.put("COUNT", retMap.get("COUNT"));
				retMap = resAllotService.orederGenerate(map);
				rtnMap.put("msg",retMap.get("msg"));
			}else{
				rtnMap.put("error",retMap.get("error"));
			}
		} catch (Exception e) {
			logger.error("ResAllotAction.notOrderedResAllot: 无序订单生成失败"+e.getMessage());
			rtnMap.put("error", "生成订单失败!");
		}
	}
	
	/**
	 * 有序订单生成
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void orderedResAllot(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		Map retMap =null;
		try {
			logger.info("ResAllotAction.orderedResAllot: 有序订单生成");
			Map map=(Map) this.dataToMap(request);
			Map excelMap = new HashMap() ; 
			
			excelMap= fileImport(request,response);   //excel导入调拨
			
			excelMap.put("SOURCE_STORE_ID",request.getParameter("SOURCE_STORE_ID"));
			excelMap.put("TARGET_STORE_ID",request.getParameter("TARGET_STORE_ID"));
			excelMap.put("STANDARD_CD",request.getParameter("STANDARD_CD"));
			
			if(excelMap.get("code")==Constants.SUCCESS_CODE){
				excelMap.put("SYSTEM_USER_ID", 1);
				retMap = resAllotService.orderedResAllot(excelMap);
				if(retMap.get("error")==null){
					excelMap.put("BATCH_CD", retMap.get("BATCH_CD"));
					excelMap.put("batchID", retMap.get("batchID"));
					excelMap.put("COUNT", retMap.get("COUNT"));
					excelMap.put("checkOrder", true); //有序资源调拨
					excelMap = resAllotService.orederGenerate(excelMap);
					rtnMap.put("msg",excelMap.get("msg"));
				}else{
					rtnMap.put("error",excelMap.get("error"));
				}
			}else{
				rtnMap.put("error", "生成订单失败!"+excelMap.get("code"));
				logger.info("ResAllotAction.orderedResAllot: 有序订单生成失败!"+excelMap.get("code"));
			}
		} catch (Exception e) {
			logger.error("ResAllotAction.orderedResAllot: 有序订单生成失败!"+e.getMessage());
			rtnMap.put("error", "生成订单失败!");
		}
	}
	
	/**
	 * 导出excel
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		try {
			logger.info("ResAllotAction.exportExcel:Excel导出!");
			Map map=(Map) this.dataToMap(request);
			map.put("SYSTEM_USER_ID", 1);
			//获取导出的数据
			List<Map<String,String>> dataMap = resAllotService.exportExcel(map);
			if("1".equals(map.get("queryCount"))){
				if(dataMap != null && dataMap.size() > 0){
					rtnMap.put("code", "1100");
				}else{
					rtnMap.put("code", "1000");
				}
				return;
			}
			ExcelUtil excel = new ExcelUtil();
			
			//初始化数据
			String title ="资源调拨明细";
			String [] headers={"调拨单号","批次号","资源编码","资源名称","资源串码","数量","源资源库","目标资源库","创建人","创建时间"};
			String [] keys ={"ADJUST_ORDER_ID","OPT_BATCH_CD","STANDARD_CD","MKT_RES_NAME","MKT_RES_INST_CODE","QUANTITY","SOURCE_STORE_ID","TARGET_STORE_ID","STAFF_NAME","CRT_DATE"};
			
			String fname="资源调拨明细";
			OutputStream os = response.getOutputStream();//取得输出流
		    response.reset();//清空输出流
		    
		    //下面是对中文文件名的处理
		    response.setCharacterEncoding("UTF-8");//设置相应内容的编码格式
		    //fname = java.net.URLEncoder.encode(fname,"UTF-8");
		    String newExportFileName = new String(fname.getBytes("GBK"), "ISO-8859-1");
	        response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Content-Disposition","attachment; filename=\"" + newExportFileName+ ".xls\"");
//		    response.setHeader("Content-Disposition","attachment;filename="+fname+".xls");
//		    response.setContentType("application/msexcel");//定义输出类型
			excel.exportExcel(title, headers,keys, dataMap,os);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error("ResAllotAction.exportExcel:Excel导出失败!"+e.getMessage());
		}
	}
}
