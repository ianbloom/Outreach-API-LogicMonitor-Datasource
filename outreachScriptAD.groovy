/*
 * Script to actively discover Outreach users to track daily calls made
 *
 * LogicMonitor - Sales Development Representative - IB 7/5/17
 *
 * NOTE: This LogicModule is custom-built and not supported by LogicMonitor Technical Support.
 *
 */

import groovy.json.JsonSlurper
import org.apache.http.HttpEntity
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

def accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpYW4uYmxvb21AbG9naWNtb25pdG9yLmNvbSIsImlhdCI6MTUwMDA2Mzk5MSwiZXhwIjoxNTAwMDcxMTkxLCJiZW50byI6ImFwcDJhIiwib3JnX3VzZXJfaWQiOjE1MiwiYXVkIjoiTG9naWNNb25pdG9yIiwic2NvcGVzIjoicHJvZmlsZSBlbWFpbCBjcmVhdGVfcHJvc3BlY3RzIHJlYWRfcHJvc3BlY3RzIHVwZGF0ZV9wcm9zcGVjdHMgcmVhZF9zZXF1ZW5jZXMgdXBkYXRlX3NlcXVlbmNlcyByZWFkX3RhZ3MgcmVhZF9hY2NvdW50cyBjcmVhdGVfYWNjb3VudHMgcmVhZF9hY3Rpdml0aWVzIHJlYWRfbWFpbGluZ3MgcmVhZF9tYXBwaW5ncyByZWFkX3BsdWdpbnMgcmVhZF91c2VycyBjcmVhdGVfY2FsbHMgcmVhZF9jYWxscyByZWFkX2NhbGxfcHVycG9zZXMgcmVhZF9jYWxsX2Rpc3Bvc2l0aW9ucyBBSkVBa2dEQkFNSUJBUUFSQUJJQVVRQmhBUEVBUVFCQ0FDRUFNUT09Iiwibm9uY2UiOiJiMWY5ODFhMyJ9.DRZ0MKkxi1Z_vWilG-oFbegFtCK8xtT5f9V8hIy_DUU"

def refresh_token = "03c23dc97d850ae5c8d09de41f492d32038902f941021fd6742c11dfbfecb60b";
def client_id = "669e3092433dbc7197337058eb3685dc79758e70137dfb3ceab582f5c8af8da9";
def client_secret = "ba3f85cde89544f7ad0c6e28e22a8908eea446c02bd5e137f90306e0029cfbd9";
def redirect_uri = "https://www.logicmonitor.com/oauth/outreach";
def posturl = "https://api.outreach.io/1.0";

// POST to refresh access token, and use new access token for the following GET request
CloseableHttpClient httpclient2 = HttpClients.createDefault();
HttpPost httpPost = new HttpPost("https://api.outreach.io/oauth/token");
List <NameValuePair> nvps = new ArrayList <NameValuePair>();
nvps.add(new BasicNameValuePair("grant_type", "refresh_token"));
nvps.add(new BasicNameValuePair("client_id", client_id));
nvps.add(new BasicNameValuePair("client_secret", client_secret));
nvps.add(new BasicNameValuePair("refresh_token", refresh_token));
nvps.add(new BasicNameValuePair("redirect_uri", redirect_uri));
httpPost.setEntity(new UrlEncodedFormEntity(nvps));
CloseableHttpResponse response2 = httpclient2.execute(httpPost);
try {
    HttpEntity entity2 = response2.getEntity();
    String response2Body = EntityUtils.toString(entity2);
    parseResponse2 = new JsonSlurper().parseText(response2Body);
    accessToken = parseResponse2.access_token;
    refresh_token = parseResponse2.refresh_token;
} finally {
    response2.close();
}

def url = "https://api.outreach.io/1.0/users";

for (i = 1; i <4; i++) {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	httpGet = new HttpGet(url + "?page[number]=" + i);
	httpGet.addHeader("Authorization", "Bearer " + accessToken);
	httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
	response = httpclient.execute(httpGet);
	responseBody = EntityUtils.toString(response.getEntity());

	def json = new JsonSlurper().parseText(responseBody).data;

	for (user in json){
		println user.id + '##' + user.attributes.metadata.first_name + ' ' + user.attributes.metadata.last_name
	}
}
return 0;