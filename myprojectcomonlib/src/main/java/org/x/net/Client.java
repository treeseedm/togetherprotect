package org.x.net;

import android.annotation.SuppressLint;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.x.net.Msg.DataType;
import org.x.net.socket.SocketInitializer;
import org.x.net.socket.SocketMsg;
import org.x.net.socket.SocketUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;


import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.alibaba.sdk.android.oss.util.OSSToolKit;


public class Client {
	protected Bootstrap bootstrap = null;
	protected Channel channel = null;
	protected EventLoopGroup workerGroup = null;
	private InetSocketAddress addr;
	public boolean isBackground = false;
	private static final int BACKOFF_CAP = 12;
	public static final int IDLE_SECONDS = 60;
	private static final String UserAgent = "Mozilla/5.0 (Linux; U; Android 3.3; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	protected String ip = null;
	protected int port = 0;
	public String localIP = null;
	public String localMAC = null;
	public String localName = null;
	public byte[] localNameBytes = null;
	public byte[] localIPBytes = null;
	private AtomicInteger tryTimes = null;
	protected String email = null;
	protected String password = null;
	public long userId = 0;
	public String userName = null;
	public String publicKey = "team8@yiqihi.com";
	public String privateKey = "";
	public boolean auth = false;
	public String cookieId = null;
	public MsgEvent event = null;
	protected ClientConfiguration cc = null;
	protected OSSServiceProvider ossService = null;
	public String imageDomain = null;
	public static enum ContentType {
		privateimage,privatevoice,privatevideo,image, video, json, xml, html, text, js
	}

	public static enum ResponseContentType {
		json, string,bytes
	}
	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void bind(MsgEvent event) {
		this.event = event;
	}

	private void initConnect() {
		try {
			if (bootstrap == null) {
				BasicDBObject localDevice = SocketUtil.getLocal();
				localIP = localDevice.getString("ip");
				localMAC = localDevice.getString("mac");
				localName = localDevice.getString("name");
				localNameBytes = SocketUtil.toBytes(localName);
				localIPBytes = SocketUtil.toBytes(localIP);

				workerGroup = new NioEventLoopGroup();
				addr = new InetSocketAddress(ip, port);
				bootstrap = new Bootstrap().channel(NioSocketChannel.class)
						.group(workerGroup);
				bootstrap.remoteAddress(addr);
				setDefaultTimeout(60, TimeUnit.SECONDS);
			}
		} catch (Throwable e) {
			this.event.onResponse(DataType.error, "connect", null,
					e.getLocalizedMessage());
		}
	}

	public void connect() {
		this.initConnect();
		tryTimes = new AtomicInteger(3);
		workerGroup.schedule(new Runnable() {
			@Override
			public void run() {
				handleConnect(1);
			}
		}, 2, TimeUnit.MILLISECONDS);
	}

	public void setDefaultTimeout(long timeout, TimeUnit unit) {
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
				(int) unit.toMillis(timeout));
	}

	private void handleConnect(final int attempts) {
		if (tryTimes.decrementAndGet() < 0) {
			return;
		}
		ChannelFuture future;
		synchronized (this.bootstrap) {
			future = this.bootstrap.handler(new SocketInitializer(this))
					.connect();
		}
		final Client self = this;
		future.addListener(new GenericFutureListener<ChannelFuture>() {
			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				if (!future.isSuccess()) {
					int timeout = 2 << attempts;
					workerGroup.schedule(new Runnable() {
						@Override
						public void run() {
							handleConnect(Math.min(BACKOFF_CAP, attempts + 1));
						}
					}, timeout, TimeUnit.SECONDS);
				} else {
					channel = future.channel();
					self.event.onResponse(
							DataType.json,
							"connect",
							"ok",
							new BasicDBObject().append("xeach", true).append(
									"action", "connect"));
				}
			}
		});
	}

	public <T> T await(Future<T> cmd, long timeout, TimeUnit unit) {
		if (!cmd.awaitUninterruptibly(timeout, unit)) {
			cmd.cancel(true);
			this.event.onResponse(DataType.error, "await", null,
					"await,Command timed out");
			return null;
		}
		if (!cmd.isSuccess()) {
			this.event.onResponse(DataType.error, "await", null, cmd.cause()
					.getLocalizedMessage());
			return null;
		}
		return cmd.getNow();
	}

	public void close() {
		this.isBackground = true;
		if (channel != null) {
			channel.close();
		}
		workerGroup.shutdownGracefully();
	}

	public boolean isActive() {
		return this.channel != null && this.channel.isActive();
	}

	public ChannelFuture send(BasicDBObject oMsg) {
		if (isActive() == false)
			return null;
		SocketMsg msg = new SocketMsg();
		msg.write(auth ? this.privateKey : this.publicKey, oMsg);
		return channel.writeAndFlush(msg);
	}

	public ChannelFuture send(byte[] bytes) {
		if (isActive() == false)
			return null;
		SocketMsg msg = new SocketMsg();
		msg.write(bytes);
		return channel.writeAndFlush(msg);
	}

	public ChannelFuture heartbeat() {
		if (isActive() == false)
			return null;
		SocketMsg msg = new SocketMsg();
		BasicDBObject oMsg = new BasicDBObject().append("action", "live");
		msg.write(auth ? this.privateKey : this.publicKey, oMsg);
		return channel.writeAndFlush(msg);
	}

	public InetSocketAddress getAddr() {
		return addr;
	}

	private String parseUrl(String url) {
		StringBuilder buf = new StringBuilder();
		if (url.startsWith("http") == false) {
			buf.append("http://");
		}
		if (url.startsWith("www") == false) {
			buf.append("service.yiqihi.com");
		}
		if (url.startsWith("/") == false) {
			buf.append("/");
		}
		buf.append(url);
		return buf.toString();
	}

	public ChannelFuture register(String userName, String email,
			String password, int sex, byte[] face) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "register");
		oMsg.append("userName", userName);
		oMsg.append("email", email);
		oMsg.append("password", password);
		oMsg.append("sex", sex);
		oMsg.append("face", SocketUtil.encodeBASE64(face));
		return send(oMsg);
	}

	public ChannelFuture login(String email, String password) {
		this.cookieId = SocketUtil.newId();
		this.auth = false;
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "login");
		oMsg.append("user", email);
		oMsg.append("password", password);
		oMsg.append("cookieId", cookieId);
		return send(oMsg);
	}

	/*
	 * ==================>topicId,topicParam：为用户的讨论主题和讨论的参数
	 * ********************************type="trip",catalog=0，id为导游详细页的id
	 * ********************************type="trip",catalog=1，id为线路详细页的id
	 * ********************************type="car",catalog=0，id为租车详细页的id
	 */
	public ChannelFuture enter(String type, int catalog, String id,
			String topicId, String topicParam, boolean readMessages,
			boolean readContact) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "enter");
		oMsg.append("code", "connect");
		oMsg.append("type", type);
		oMsg.append("catalog", catalog);
		oMsg.append("id", id);
		oMsg.append("topicId", topicId);
		oMsg.append("topicParam", topicParam);
		oMsg.append("cookieId", this.cookieId);
		oMsg.append("readMessages", readMessages);
		oMsg.append("readContact", readContact);
		oMsg.append("timestamp", System.currentTimeMillis());
		return send(oMsg);
	}

	/*
	 * msgId聊天室id
	 */
	public ChannelFuture say(String msgId, String content, long sendTime) {
		return say(msgId, 0, content, sendTime);
	}

	/*
	 * msgId聊天室id，userId为私聊的目标用户
	 */
	public ChannelFuture say(String msgId, long userId, String content,
			long sendTime) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "say");
		oMsg.append("scope", userId);
		oMsg.append("msgId", msgId);
		oMsg.append("content", content);
		oMsg.append("sendtime", sendTime);
		return send(oMsg);
	}

	public ChannelFuture sendPicture(String msgId, long userId,
			String[] fileNames, Object[] fileBytes, long sendTime) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "picture");
		oMsg.append("scope", userId);
		oMsg.append("msgId", msgId);
		BasicDBList files = new BasicDBList();
		oMsg.append("content", files);
		for (int i = 0; i < fileNames.length; i++) {
			BasicDBObject oFile = new BasicDBObject();
			oFile.append("name", fileNames[i]);
			oFile.append("file", SocketUtil.encodeBASE64((byte[]) fileBytes[i]));
			files.add(oFile);
		}
		oMsg.append("sendtime", sendTime);
		return send(oMsg);
	}

	public ChannelFuture sendVoice(String msgId, long userId, long duration,
			byte[] bytes, long sendTime) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "voice");
		oMsg.append("scope", userId);
		oMsg.append("msgId", msgId);
		oMsg.append("duration", duration);
		oMsg.append("content", SocketUtil.encodeBASE64(bytes));
		oMsg.append("sendtime", sendTime);
		return send(oMsg);
	}

	public ChannelFuture sendAuthCode(String deviceId, String countryName,
			String countryCode, String mobile) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "sendAuthCode");
		oMsg.append("deviceId", deviceId);
		oMsg.append("countryName", countryName);
		oMsg.append("countryCode", countryCode);
		oMsg.append("mobile", mobile);
		return send(oMsg);
	}

	public ChannelFuture checkAuthCode(String deviceId, String countryCode,
			String mobile, String code) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "checkAuthCode");
		oMsg.append("deviceId", deviceId);
		oMsg.append("countryCode", countryCode);
		oMsg.append("mobile", mobile);
		oMsg.append("code", code);
		return send(oMsg);
	}

	public void forgetPassword(String email) {
		BasicDBObject oReq = newQuery("mobile", 4);
		oReq.append("email", email);
		postUrl("forgetPassword", "/module", oReq);
	}

	public ChannelFuture readDialogues(long timestamp) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "readDialogues");
		oMsg.append("timestamp", timestamp);
		return send(oMsg);
	}

	public ChannelFuture readInforms(long timestamp) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "readInforms");
		oMsg.append("timestamp", timestamp);
		return send(oMsg);
	}

	public ChannelFuture readCalls(long timestamp) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "readCalls");
		oMsg.append("timestamp", timestamp);
		return send(oMsg);
	}

	public ChannelFuture iRead(String type, String id) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "iRead");
		oMsg.append("type", type);
		oMsg.append("id", id);
		return send(oMsg);
	}

	public ChannelFuture call(long targetUserId, String targetUserName,
			String type, int catalog, String shortId, String topicId,
			String topicParam) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "call");
		oMsg.append("userId", targetUserId);
		oMsg.append("userName", targetUserName);
		oMsg.append("type", type);
		oMsg.append("catalog", catalog);
		oMsg.append("id", shortId);
		oMsg.append("topicId", topicId);
		oMsg.append("topicParam", topicParam);
		return send(oMsg);
	}

	public ChannelFuture addChatUserTag(String msgId, long userId,
			String userTag) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "addChatUserTag");
		oMsg.append("msgId", msgId);
		oMsg.append("userId", userId);
		oMsg.append("userTag", userTag);
		return send(oMsg);
	}

	public ChannelFuture addContact(long userId, String userName, String group,
			String tag) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "addContact");
		oMsg.append("userId", userId);
		oMsg.append("userName", userName);
		oMsg.append("tag", tag);
		oMsg.append("group", group);
		return send(oMsg);
	}

	public ChannelFuture join(String msgId, long[] userIds) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "join");
		oMsg.append("msgId", msgId);
		BasicDBList users = new BasicDBList();
		oMsg.append("users", users);
		for (int i = 0; i < userIds.length; i++) {
			BasicDBObject oUser = new BasicDBObject();
			oUser.append("findMode", "userId");
			oUser.append("findValue", userIds[i]);
			users.add(oUser);
		}
		return send(oMsg);
	}

	public BasicDBObject newQuery(String moduleName, int funcIndex) {
		BasicDBObject oReq = new BasicDBObject();
		oReq.append("moduleName", moduleName);
		oReq.append("funcIndex", funcIndex);
		return oReq;
	}

	public void readComments(long id, int pageNumber, int pageSize) {
		BasicDBObject oReq = new BasicDBObject();
		oReq.append("funcIndex", 16);
		oReq.append("id", id);
		oReq.append("pageNumber", pageNumber);
		oReq.append("pageSize", pageSize);
		postUrl("readComments", "/html5", oReq);
	}

	public void readCalendar(long id, String priceName) {
		BasicDBObject oReq = new BasicDBObject();
		oReq.append("funcIndex", 17);
		oReq.append("id", id);
		oReq.append("priceName", priceName);
		postUrl("readCalendar", "/html5", oReq);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void loadWebView(WebView view, WebViewClient container, String url) {
		CookieSyncManager cookieSyncManager = CookieSyncManager
				.createInstance(view.getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();
		String domain = "service.yiqihi.com";
		cookieManager.setCookie("http://" + domain, "cookieId=" + cookieId
				+ " ; Domain=" + domain);
		cookieSyncManager.sync();
		String cookie = cookieManager.getCookie("http://" + domain);
		System.out.println("test cookie:" + cookie);
		view.getSettings().setJavaScriptEnabled(true);
		view.setWebViewClient(container);
		view.loadUrl(url);
	}

	protected Object parseResponse(HttpResponse response,
								   ResponseContentType contentType) throws IllegalStateException,
			IOException {
		HttpEntity respEntity = response.getEntity();
		byte[] bytes = EntityUtils.toByteArray(respEntity);
		respEntity.consumeContent();
		switch (contentType) {
			case bytes:
				return bytes;
			case json:
				String content = SocketUtil.toString(bytes);
				BasicDBObject oResult = (BasicDBObject) JSON.parse(content);
				if (oResult.containsField("data")) {
					content = oResult.getString("data");
					content = SocketUtil.easyDecrypt(auth ? privateKey : publicKey,
							content);
					return (BasicDBObject) JSON.parse(content);
				}
				return oResult;
			default:
				return SocketUtil.toString(bytes);
		}
	}


	public BasicDBObject postUrl(final String action, String url,
								 BasicDBObject oReq) {
		return this.postUrl(action, url, oReq.toString());
	}

	public BasicDBObject postUrl(final String action, final String url,
								 String content) {
		final HttpClient httpClient = new DefaultHttpClient();
		final Client self = this;
		httpClient.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 1000 * 60 * 10);
		try {
			String Url = parseUrl(url);
			HttpPost httpPost = new HttpPost(Url);
			if (StringUtils.isEmpty(this.cookieId) == false) {
				httpPost.addHeader("Cookie", "cookieId=" + this.cookieId);
			}
			httpPost.addHeader("User-Agent", UserAgent);
			StringEntity entity = new StringEntity(content, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			BasicDBObject oResult = (BasicDBObject) parseResponse(response,
					ResponseContentType.json);
			self.event.onResponse(DataType.string, action, url, oResult);
			return oResult;
		} catch (Exception e) {
			this.event.onResponse(DataType.error, "post", url,
					e.getLocalizedMessage());
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	public void getUrl(final String action, final String url) {
		final HttpClient httpClient = new DefaultHttpClient();
		final Client self = this;
		httpClient.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 1000 * 60 * 10);
		try {
			HttpGet httpget = new HttpGet(parseUrl(url));
			httpget.addHeader("Cookie", "cookieId=" + this.cookieId);
			httpget.addHeader("User-Agent", UserAgent);
			ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {

				@Override
				public byte[] handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					InputStream stream = entity.getContent();
					byte[] bytes = new byte[1024];
					int size = 0;
					ByteBuf buf = Unpooled.buffer();
					while ((size = stream.read(bytes)) > 0) {
						buf.writeBytes(bytes, 0, size);
					}
					stream.close();
					self.event.onResponse(DataType.bytes, action, url,
							buf.array());
					httpClient.getConnectionManager().shutdown();
					return bytes;
				}
			};
			httpClient.execute(httpget, responseHandler);
		} catch (Exception e) {
			this.event.onResponse(DataType.error, action, url,
					e.getLocalizedMessage());
		}
	}

	public void getContent(final String action, final String url) {
		final HttpClient httpClient = new DefaultHttpClient();
		final Client self = this;
		httpClient.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 1000 * 60 * 10);
		try {
			HttpGet httpget = new HttpGet(parseUrl(url));
			httpget.addHeader("Content-Type", "text/html;charset=UTF-8");
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					String content = entity != null ? EntityUtils.toString(
							entity, "UTF-8") : null;
					self.event
							.onResponse(DataType.string, action, url, content);
					httpClient.getConnectionManager().shutdown();
					return content;
				}
			};
			httpClient.execute(httpget, responseHandler);
		} catch (Exception e) {
			this.event.onResponse(DataType.string, action, url,
					e.getLocalizedMessage());
		}
	}


	public void initAliyun(String hostId, final String accessKey,
						   final String screctKey, String imageDomain) {
		this.imageDomain = imageDomain;
		OSSClient.setGlobalDefaultHostId(hostId);
		OSSClient.setGlobalDefaultTokenGenerator(new TokenGenerator() {
			@Override
			public String generateToken(String httpMethod, String md5,
										String type, String date, String ossHeaders, String resource) {
				String content = httpMethod + "\n" + md5 + "\n" + type + "\n"
						+ date + "\n" + ossHeaders + resource;
				return OSSToolKit.generateToken(accessKey, screctKey, content);
			}
		});
		cc = new ClientConfiguration();
		ossService = OSSServiceProvider.getService();
		ossService.setClientConfiguration(cc);
	}

	public BasicDBList uploadPictures(String[] fileNames, Object[] fileBytes) {
		BasicDBList files = new BasicDBList();
		for (int i = 0; i < fileNames.length; i++) {
			String fileName = SocketUtil.shortFileName(fileNames[i]);
			BasicDBObject oItem = upload(ContentType.image, "", fileName, "",
					(byte[]) fileBytes[i]);
			if (oItem == null)
				continue;
			files.add(oItem);
		}
		return files;
	}

	public BasicDBObject uploadPicture(String fileTag, String fileName,
									   String title, byte[] content) {
		fileName = SocketUtil.shortFileName(fileName);
		return upload(ContentType.image, fileTag, fileName, title, content);
	}


	public BasicDBObject upload(final ContentType contentType, String fileTag,
								String fileName, String title, byte[] content) {
		String fileType = null;
		String fileExt = null;
		int index = fileName.lastIndexOf(".");
		if (index > 0) {
			fileExt = fileName.substring(index + 1, fileName.length());
			fileName = fileName.substring(0, index);
		}
		OSSBucket bucket = ossService.getOssBucket("yiqihi-"
				+ contentType.name());
		switch (contentType) {
			case image:
			case privateimage:
				fileExt = StringUtils.isEmpty(fileExt) ? "jpg" : fileExt;
				fileType = "image/" + fileExt;
				break;
			case privatevoice:
				fileExt = StringUtils.isEmpty(fileExt) ? "amr" : fileExt;
				fileType = "voice/" + fileExt;
				break;
			case video:
			case privatevideo:
				fileExt = StringUtils.isEmpty(fileExt) ? "mp4" : fileExt;
				fileType = "video/" + fileExt;
				break;
			case json:
				fileExt = StringUtils.isEmpty(fileExt) ? "json" : fileExt;
				fileType = "application/json";
				break;
			case xml:
				fileExt = StringUtils.isEmpty(fileExt) ? "xml" : fileExt;
				fileType = "text/xml";
				break;
			case html:
				fileExt = StringUtils.isEmpty(fileExt) ? "htm" : fileExt;
				fileType = "text/html";
				break;
			case text:
				fileExt = StringUtils.isEmpty(fileExt) ? "text" : fileExt;
				fileType = "text/plain";
				break;
			case js:
				fileExt = StringUtils.isEmpty(fileExt) ? "js" : fileExt;
				fileType = "text/javascript";
				break;
		}
		fileName = fileTag + "@" + this.userId + "@" + fileName + "." + fileExt;
		OSSData data = ossService.getOssData(bucket, fileName);
		data.setData(content, fileType);

		final BasicDBObject oReq = new BasicDBObject();
		final MsgEvent msgEvent = this.event;
		try {
			data.upload();
			oReq.append("moduleName", "mobile");
			oReq.append("funcIndex", 318);
			oReq.append("fileName", fileName);
			oReq.append("datacenter", "aliyun");
			oReq.append("contentType", contentType.ordinal());
			return postUrl("upload", "/module", oReq);
		} catch (Exception e) {
			oReq.append("xeach", false).append("name", fileName)
					.append("message", e.getMessage());
			msgEvent.onResponse(DataType.string, "upload", fileName, oReq);
		}
		return null;
	}

	public byte[] download(final ContentType contentType, String fileName) {
		OSSBucket bucket = ossService.getOssBucket("yiqihi-"
				+ contentType.name());
		switch (contentType) {
			case privateimage:
			case privatevoice:
			case privatevideo:
				bucket.setBucketACL(AccessControlList.PRIVATE);
				break;
			default:
				bucket.setBucketACL(AccessControlList.PUBLIC_READ);
				break;
		}
		int index = fileName.lastIndexOf("/");
		if (fileName.length() > 0) {
			fileName = fileName.substring(index + 1, fileName.length());
		}
		try {
			OSSData data = ossService.getOssData(bucket, fileName);
			return data.get();
		} catch (Exception e) {
			return null;
		}
	}

}
