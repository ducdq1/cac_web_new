package com.viettel.ws;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.ws.bo.AccessToken;
import com.viettel.ws.bo.HangHoaBO;
import com.viettel.ws.bo.Inventory;
import com.viettel.ws.bo.KiotVietProductResponse;

public class KiotVietService {
	private int countRequest = 0;

	public HangHoaBO layThongTinTonKho_KiotViet(String maVT) {

		if (maVT == null || maVT.isEmpty()) {
			return null;
		}

		if (ProductService.KiotViet_accesstoken == null) {
			ProductService.KiotViet_accesstoken = layToken();
		}

		CloseableHttpClient httpClient = null;// = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		try {
			SSLContext ctx = SSLContexts.custom().useProtocol("TLSv1.2").build();
			httpClient = HttpClientBuilder.create().setSslcontext(ctx).build();

			String url = ResourceBundleUtil.getString("kiot_viet_product_url")
					+ URLEncoder.encode(maVT, StandardCharsets.UTF_8.toString());
			HttpGet request = new HttpGet(url);

			RequestConfig.Builder requestConfig = RequestConfig.custom();
			requestConfig.setConnectTimeout(20 * 1000);
			requestConfig.setConnectionRequestTimeout(20 * 1000);
			requestConfig.setSocketTimeout(20 * 1000);

			request.setConfig(requestConfig.build());
			request.addHeader("Authorization", "Bearer " + ProductService.KiotViet_accesstoken);
			request.addHeader("Retailer", "noithatchauaudn");

			response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 401) {
				ProductService.KiotViet_accesstoken = layToken();
				System.out.println("countRequest: " + countRequest);
				if (countRequest < 3) {
					countRequest++;
					return layThongTinTonKho_KiotViet(maVT);
				} else {
					countRequest = 0;
					return null;
				}
			}

			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {

				String result = EntityUtils.toString(entity);
				// System.out.println(result);
				HangHoaBO tonKho = new HangHoaBO();//
				KiotVietProductResponse kiotVietProductResponse = new Gson().fromJson(result,
						KiotVietProductResponse.class);
				if (kiotVietProductResponse != null) {
					tonKho.setDvt(kiotVietProductResponse.getUnit());
					tonKho.setTen_vt(kiotVietProductResponse.getFullName());
					List<Inventory> inventories = kiotVietProductResponse.getInventories();
					if (inventories != null && !inventories.isEmpty()) {
						Inventory inventory = inventories.get(0);
						tonKho.setSo_luong(inventory.getOnHand());

					}
				}
				return tonKho;
			} else {
				if (entity != null) {

					String result = EntityUtils.toString(entity);
					System.out.println(result);
					return null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// KiotViet: Lay access token
	public String layToken() {
		System.out.println("KiotViet_curren_accesstoken: " + ProductService.KiotViet_accesstoken);
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			SSLContext ctx = SSLContexts.custom().useProtocol("TLSv1.2").build();
			httpClient = HttpClientBuilder.create().setSslcontext(ctx).build();

			String url = ResourceBundleUtil.getString("kiot_viet_authen_url");
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
			urlParameters.add(new BasicNameValuePair("client_id", ResourceBundleUtil.getString("kiot_viet_client_id")));
			urlParameters
					.add(new BasicNameValuePair("client_secret", ResourceBundleUtil.getString("kiot_viet_secret")));
			urlParameters.add(new BasicNameValuePair("scopes", "PublicApi.Access"));

			HttpEntity postParams = new UrlEncodedFormEntity(urlParameters, "UTF-8");
			httpPost.setEntity(postParams);

			RequestConfig.Builder requestConfig = RequestConfig.custom();
			requestConfig.setConnectTimeout(20 * 1000);
			requestConfig.setConnectionRequestTimeout(20 * 1000);
			requestConfig.setSocketTimeout(20 * 1000);

			httpPost.setConfig(requestConfig.build());
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {

				String result = EntityUtils.toString(entity);
				AccessToken accessToken = new Gson().fromJson(result, AccessToken.class);
				String token = accessToken.getAccess_token();

				System.out.println("Kiot Viet new Token: ");
				System.out.println(token);
				return token;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
