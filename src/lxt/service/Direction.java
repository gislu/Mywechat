package lxt.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;


public class Direction {
	/**
	 * ����http�����ȡ���ؽ��
	 * 
	 * @param requestUrl �����ַ
	 * @return
	 */
	public static String httpRequest(String requestUrl) {
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			// �����ص�������ת�����ַ���
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// �ͷ���Դ
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

		} catch (Exception e) {
		}
		return buffer.toString();
	}

	/**
	 * utf����
	 * 
	 * @param source
	 * @return
	 */
	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���루��->Ӣ Ӣ->�� ��->�� ��
	 * 
	 * @param source
	 * @return
	 */
	public static String translate(String source) {
		String dst = null;

		// ��װ��ѯ��ַ
		String requestUrl = "http://api.map.baidu.com/direction/v1?mode=transit&origin=����´�&destination=��վ&region=�Ϸ�&output=json&ak=344b87d71c4f56a10eb9e55d331d1973";
		// �Բ���q��ֵ����urlEncode utf-8����
//		requestUrl = requestUrl.replace("{keyWord}", urlEncodeUTF8(source));

		// ��ѯ���������
		try {
			// ��ѯ����ȡ���ؽ��
			String json = httpRequest(requestUrl);
			// ͨ��Gson���߽�jsonת����TranslateResult����
			TranslateResult translateResult = new Gson().fromJson(json, TranslateResult.class);
			// ȡ��translateResult�е�����
			dst = translateResult.getTrans_result().get(0).getDst();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null == dst)
			dst = "����ϵͳ�쳣�����Ժ��ԣ�";
		return dst;
	}

	public static void main(String[] args) {
		// ��������The network really powerful
		System.out.println(translate("������ǿ��"));
	}
}