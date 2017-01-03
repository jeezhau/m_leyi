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
public class AccessToken {
	private static Logger log = LoggerFactory.getLogger(AccessToken.class);
	private static String ACCESS_TOKEN = "";	//访问凭证
	private static long LASTUPDTIME = 0L;		//上次获取时间
	private static long EXPIRESIN = 0L;			//有效时间
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
	/**
	 * 获取微信接口访问凭证：ACCESS_TOKEN
	 */
	public static String getAccessToken(){
		long cutTime = System.currentTimeMillis();
		long needTime = LASTUPDTIME + EXPIRESIN*1000 + 10000;//提前10更新
		if(ACCESS_TOKEN == null || ACCESS_TOKEN.length()<1 || cutTime >= needTime){
			String appId = getParam("APPID");
			String appSecret = getParam("APPSECRET");
			String url = getParam("ACCESS_TOKEN_URL");
			url.replaceAll("APPID", appId).replaceAll("APPSECRET", appSecret);
			String result = null;
			JSONObject json = null;
			try {
				//返回格式：{"access_token":"ACCESS_TOKEN","expires_in":7200}
				//		   {"errcode":40013,"errmsg":"invalid appid"}
				result = HttpUtils.doGet(url);
				json = (JSONObject) JSONObject.stringToValue(result);
			} catch (IOException e) {
				e.printStackTrace();
				log.info("ACCESS_TOKEN获取失败，出现异常：" + e.getMessage());
			}
			if(json != null){
				if(json.has("access_token")){
					ACCESS_TOKEN = (String) json.get("access_token");
					EXPIRESIN = (long) json.get("expires_in");
					LASTUPDTIME = System.currentTimeMillis();
					log.info("ACCESS_TOKEN获取返回成功：" + result);
					return ACCESS_TOKEN;
				}else if(json.has("errcode")){
					log.info("ACCESS_TOKEN获取返回失败，失败信息：" + result);
				}
			}
		}
		return null;
	}
	
	
	
	

}