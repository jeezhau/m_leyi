package me.jeekhan.leyi.wxapi;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.jeekhan.leyi.common.HttpUtils;

/**
 * 调用微信通用、基础API
 * @author Jee Khan
 *
 */
public class WXSysParam {
	private static String prop_file = "props/WXParam";	//微信常量配置文件
	private static ResourceBundle BUNDLE = ResourceBundle.getBundle(prop_file);
	
	/**
	 * 根据指定key获取对应的值
	 * @param key
	 * @param params
	 * @return
	 */
	public static String getForamtValue(String key,Object[] params) {
		String keyValue = null;
		String msg = null;
		try{
			keyValue = BUNDLE.getString(key);
			MessageFormat mf = new MessageFormat(keyValue); 
			msg = mf.format(params);  
		}catch(Exception e){
			return null;
		}
		return msg;
	}
	//获取参数
	public static String getParam(String key) {
		return BUNDLE.getString(key);
	}
}
