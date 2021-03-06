                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                .printStackTrace();
		}
	}
	
	/**
	 * 分页查询图标实例信息
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	@SuppressWarnings("unchecked")
	public void queryIconEntryInstList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		Map<String,Object> params = this.dataToMap(request);
		try {
			Map<String,Object> rtnList=iconService.queryIconEntryInstList(params);
			rtnMap.put("data", rtnList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询配置项菜单树
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void getIconOrgItemList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		Map<String,Object> params = this.dataToMap(request);
		try {
			List<Map<String,Object>> rtnList=iconService.getIconOrgItemList(params);
			rtnMap.put("list", this.getMenuInfo(rtnList, "0"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 递归集合
	 * @param list
	 * @return
	 */
	private static List<Map<String,Object>> getMenuInfo(List<Map<String,Object>> list,String menuId){
		//菜单集合
		List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
		//结果集合
		if(list!=null && list.size() > 0){
			for(Map<String,Object> menu : list){
				if(menuId.equals(menu.get("pid").toString())){ //判断当前菜单的父级关联编码与menuId是否相等，是则表示隶属于该节点下的子节点
					//组装节点信息
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", menu.get("id"));
					map.put("pid", menu.get("pid"));
					StringBuilder sb = new StringBuilder("<i class=\"iconfont\">"+menu.get("code")+"</i>&nbsp;");
					sb.append(menu.get("name"));
					map.put("name", sb);
					map.put("icon", "/template/resource/plugins/JQueryzTreev3.5.14/css/zTreeStyle/img/sitemap.gif");
					Map<String,Object> fontMap=new HashMap<String,Object>();
					fontMap.put("font-size", menu.get("size"));
					fontMap.put("color", menu.get("color"));
					map.put("font", fontMap);
					map.put("open", true);
					//子节点
					List<Map<String,Object>> subMenu = getMenuInfo(list,menu.get("id").toString());
					if(subMenu != null && subMenu.size() > 0){//如果节点下有子节点，则将子节点存入孩子节点集合
						map.put("children",subMenu);
					}
					
					result.add(map);
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	public void getMenuList(HttpServletRequest request, HttpServletResponse response,Map rstMap){
		try {
			Map<String,Object> params = this.dataToMap(request);
			List<Map<String,Object>> rtList=menuService.queryMenuList(params);
			rstMap.put("list", this.getSysMenuInfo(rtList, "0"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 递归菜单
	 * @param list
	 * @return
	 */
	private static List<Map<String,Object>> getSysMenuInfo(List<Map<String,Object>> list,String menuId){
		//菜单集合
		List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
		//结果集合
		if(list!=null && list.size() > 0){
			for(Map<String,Object> menu : list){
				if(menuId.equals(menu.get("pid").toString())){ //判断当前菜单的父级关联编码与menuId是否相等，是则表示隶属于该节点下的子节点
					//组装节点信息
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", menu.get("id"));
					map.put("pid", menu.get("pid"));
					StringBuilder sb = new StringBuilder("<i class=\"iconfont\">&#xe701;</i>&nbsp;");
					sb.append(menu.get("text"));
					map.put("name", sb);
					map.put("icon", "/template/images/icon-bm4.png");
					if("0".equals(menu.get("pid").toString())){
						map.put("open", true);
					}
					Map<String,Object> fontMap=new HashMap<String,Object>();
					fontMap.put("font-size", "32");
					fontMap.put("color", "#41aff7");
					map.put("font", fontMap);
					//子节点
					List<Map<String,Object>> subMenu = getSysMenuInfo(list,menu.get("id").toString());
					if(subMenu != null && subMenu.size() > 0){//如果节点下有子节点，则将子节点存入孩子节点集合
						map.put("children",subMenu);
					}
					
					result.add(map);
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	public void getUserList(HttpServletRequest request, HttpServletResponse response,Map rstMap){
		try {
			Map<String,Object> params = this.dataToMap(request);
			List<Map<String,Object>> rtList=menuService.getUserList(params);
			rstMap.put("list", this.getSysUserInfo(rtList, "0"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 递归菜单
	 * @param list
	 * @return
	 */
	private static List<Map<String,Object>> getSysUserInfo(List<Map<String,Object>> list,String menuId){
		//菜单集合
		List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
		//结果集合
		if(list!=null && list.size() > 0){
			for(Map<String,Object> menu : list){
				if(menuId.equals(menu.get("pid").toString())){ //判断当前菜单的父级关联编码与menuId是否相等，是则表示隶属于该节点下的子节点
					//组装节点信息
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", menu.get("id"));
					map.put("pid", menu.get("pid"));
					map.put("name", menu.get("name"));
					String sex = menu.get("sex").toString();
					if("0".equals(sex)){
						map.put("icon", "/template/images/icon-bm4.png");
					}else if("1".equals(sex)){
						map.put("icon", "/template/images/icon-man.png");
					}else if("2".equals(sex)){
						map.put("icon", "/template/images/icon-women.png");
					}
					if("0".equals(menu.get("pid").toString())){
						map.put("open", true);
					}
					Map<String,Object> fontMap=new HashMap<String,Object>();
					fontMap.put("font-size", "32");
					fontMap.put("color", "#41aff7");
					map.put("font", fontMap);
					//子节点
					List<Map<String,Object>> subMenu = getSysUserInfo(list,menu.get("id").toString());
					if(subMenu != null && subMenu.size() > 0){//如果节点下有子节点，则将子节点存入孩子节点集合
						map.put("children",subMenu);
					}
					
					result.add(map);
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	public void getOrgList(HttpServletRequest request, HttpServletResponse response,Map rstMap){
		try {
			Map<String,Object> params = this.dataToMap(request);
			List<Map<String,Object>> rtList=menuService.getOrgList(params);
			if("1".equals(params.get("queryFlag"))){
				rstMap.put("list", rtList);
				return;
			}
			rstMap.put("list", this.getSysUOrgInfo(rtList, "0"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 递归菜单
	 * @param list
	 * @return
	 */
	private static List<Map<String,Object>> getSysUOrgInfo(List<Map<String,Object>> list,String menuId){
		//菜单集合
		List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
		//结果集合
		if(list!=null && list.size() > 0){
			for(Map<String,Object> menu : list){
				if(menuId.equals(menu.get("pid").toString())){ //判断当前菜单的父级关联编码与menuId是否相等，是则表示隶属于该节点下的子节点
					//组装节点信息
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", menu.get("id"));
					map.put("pid", menu.get("pid"));
					map.put("name", menu.get("name"));
					if("0".equals(menu.get("pid").toString())){
						map.put("open", true);
						map.put("icon", "/template/images/icon-org.png");
					}else{
						map.put("icon", "/template/images/icon-bm.png");
					}
					Map<String,Object> fontMap=new HashMap<String,Object>();
					fontMap.put("font-size", "32");
					fontMap.put("color", "#41aff7");
					map.put("font", fontMap);
					//子节点
					List<Map<String,Object>> subMenu = getSysUOrgInfo(list,menu.get("id").toString());
					if(subMenu != null && subMenu.size() > 0){//如果节点下有子节点，则将子节点存入孩子节点集合
						map.put("children",subMenu);
					}
					
					result.add(map);
				}
			}
		}
		return result;
	}
}
