package org.x.net.socket.test;

import io.netty.channel.ChannelFuture;

import org.x.conf.Const;
import org.x.net.Client;
import org.x.net.Msg.DataType;
import org.x.net.MsgEvent;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class SocketDemo {

	public static void handleDashboard(BasicDBObject oResult) {
		System.out.println(oResult);
	}

	public static void main(String[] args) {
		String ip = "116.90.87.218";
		if (Const.DebugMode == true) {
			ip = "127.0.0.1";
		}
		final Client client = new Client(ip, 8080);
		MsgEvent event = new MsgEvent() {

			@Override
			public void onResponse(DataType type, String action, String key,
								   Object data) {
				switch (type) {
					case json:
					case string:
						BasicDBObject result = (BasicDBObject) data;
						System.out.println(data.toString());

						if (action.equalsIgnoreCase("connect")
								&& result.getBoolean("xeach", false) == true) {
							client.login("3@yiqihi.com", "abc123");
						} else if (action.equalsIgnoreCase("login")
								&& result.getBoolean("xeach", false) == true) {
							client.auth = true;
							client.privateKey = result.getString("key");
							client.userId = result.getLong("id");
							client.userName = result.getString("name");
							client.initAliyun(result.getString("aliyun-hostId"),
									result.getString("aliyun-key"),
									result.getString("aliyun-secret"),
									result.getString("imageDomain"));
						}


						if (action.equalsIgnoreCase("searchTrip")) {
							BasicDBObject oResponse = (BasicDBObject) JSON
									.parse(data.toString());

							BasicDBList oItems = (BasicDBList) oResponse
									.get("items");
							for (int i = 0; i < oItems.size(); i++) {
								BasicDBObject oItem = (BasicDBObject) oItems.get(i);
								System.out.println(oItem.getString("title"));
								BasicDBList photos = (BasicDBList) JSON.parse(oItem
										.getString("photos"));
								for (int t = 0; t < photos.size(); t++) {
									BasicDBObject photo = (BasicDBObject) photos
											.get(i);
									System.out.println(photo.getString("shot"));
								}
							}
						}
						break;
					case bytes:
						byte[] bytes = (byte[]) data;
						break;
					case error:
						System.out.println(data);
						break;
				}
			}

		};
		client.bind(event);
		client.connect();
		readSearchHot(client);
		findLocation(client, "fa");
		searchTrip(client, 4);
	}

	public static ChannelFuture syncDict(Client client, String name,
										 long timestamp) {
		BasicDBObject oMsg = new BasicDBObject();
		oMsg.append("action", "syncDict");
		oMsg.append("name", name);
		oMsg.append("timestamp", timestamp);
		return client.send(oMsg);
	}

	public static void syncView(Client client, String type, String catalog,
								long timestamp) {
		BasicDBObject oReq = client.newQuery("mobile", 1);
		BasicDBList items = new BasicDBList();
		oReq.append("items", items);
		items.add(new BasicDBObject().append("type", type)
				.append("catalog", catalog).append("timestamp", timestamp));
		client.postUrl("readView", "/module", oReq);
	}

	public static void findLocation(Client client, String q) {
		BasicDBObject oReq = client.newQuery("mobile", 5);
		oReq.append("q", q);
		client.postUrl("findLocation", "/module", oReq);
	}

	public static void readCount(Client client) {
		BasicDBObject oReq = client.newQuery("mobile", 300);
		client.postUrl("readCount", "/module", oReq);
	}

	public static void readFavorite(Client client) {
		BasicDBObject oReq = client.newQuery("mobile", 301);
		client.postUrl("readFavorite", "/module", oReq);
	}

	public static void addFavorite(Client client) {
		BasicDBObject oReq = client.newQuery("mobile", 302);
		oReq.append("title", "2015去美国");
		client.postUrl("addFavorite", "/module", oReq);
	}

	public static void updateFavorite(Client client) {
		BasicDBObject oReq = client.newQuery("mobile", 303);
		BasicDBList items = new BasicDBList();
		oReq.append("items", items);

		BasicDBObject item = new BasicDBObject();
		item.append("id", 2);
		item.append("index", 9);
		item.append("title", "2015去法国");
		items.add(item);
		client.postUrl("updateFavorite", "/module", oReq);
	}

	public static void removeFavorite(Client client) {
		BasicDBObject oReq = client.newQuery("mobile", 305);
		BasicDBList items = new BasicDBList();
		oReq.append("items", items);

		BasicDBObject item = new BasicDBObject();
		item.append("id", 2);
		items.add(item);
		client.postUrl("removeFavorite", "/module", oReq);
	}

	public static void updateDashboard(Client client) {
		BasicDBObject oReq = client.newQuery("mobile", 304);
		BasicDBList items = new BasicDBList();
		oReq.append("items", items);

		BasicDBObject oItem = new BasicDBObject();
		items.add(oItem);
		oItem.append("id", 10);
		oItem.append("index", 9);
		oItem.append("selected", 0);
		oItem.append("timestamp", 0);

		oItem = new BasicDBObject();
		items.add(oItem);
		oItem.append("id", 11);
		oItem.append("index", 12);
		oItem.append("selected", 0);
		oItem.append("timestamp", 0);

		oItem = new BasicDBObject();
		items.add(oItem);
		oItem.append("id", 1);
		oItem.append("timestamp", 0);

		client.postUrl("updateDashboard", "/module", oReq);
	}

	public static void readDashboard(Client client) {
		BasicDBObject oReq = client.newQuery("mobile", 0);
		client.postUrl("readDashboard", "/module", oReq);
	}

	public static void readSearchHot(Client client) {
		BasicDBObject oReq = client.newQuery("mobile", 3);
		BasicDBList items = new BasicDBList();
		oReq.append("items", items);

		BasicDBObject oItem = new BasicDBObject();
		items.add(oItem);
		oItem.append("name", "guide");
		oItem.append("value", "3vai");

//		oItem = new BasicDBObject();
//		items.add(oItem);
//		oItem.append("name", "car");
//		oItem.append("value", "36mN");
//
//		oItem = new BasicDBObject();
//		items.add(oItem);
//		oItem.append("name", "product");
//		oItem.append("value", "index");
//
//		oItem = new BasicDBObject();
//		items.add(oItem);
//		oItem.append("name", "slider");
//		oItem.append("value", "mobile-search");
//
//		oItem = new BasicDBObject();
//		items.add(oItem);
//		oItem.append("name", "group");
//		oItem.append("value", "all");

		client.postUrl("readSearchHot", "/module", oReq);
	}

	public static void searchTrip(Client client, int mode) {
		BasicDBObject oReq = client.newQuery("mobile", 8);
		switch (mode) {
			case 0:
				oReq.append("country", "法国");
				// oReq.append("location", "巴黎");
				break;
			// 按category导游
			case 1:
			/* 3vai 导游 36mN 租车 3jzN 巴黎 */
				oReq.append("country", "法国");
				oReq.append("location", "巴黎");
				oReq.append("category", "3vai");
				break;
			// 语言过滤,72zF为普通话
			case 2:
				oReq.append("country", "法国");
				oReq.append("location", "巴黎");
				oReq.append("category", "3vai");
				oReq.append("language", "72zF-7fy7");
				break;
			// 服务标签过滤,4VY包含车的停车费
			case 3:
				oReq.append("country", "法国");
				oReq.append("location", "巴黎");
				oReq.append("category", "3jzN");
				oReq.append("serviceTag", "4VY");
				break;
			// 排序
			case 4:
				BasicDBObject sort = new BasicDBObject();
				sort.append("field", 2);// 1:默认排序，2:价格 3:更新日期
				sort.append("mode", 1);// 0:升序1:降序
				oReq.append("country", "韩国");
				oReq.append("location", "");
				oReq.append("sort", sort);
				break;
			// 排序
			case 6:
				sort = new BasicDBObject();
				sort.append("field", 2);// 1:默认排序，2:价格 3:更新日期
				sort.append("mode", 0);// 0:升序1:降序
				oReq.append("country", "奥地利");
				// oReq.append("location", "巴黎");
				oReq.append("sort", sort);
				break;
			// 价格标签 ，停车费
			case 5:
				oReq.append("country", "法国");
				oReq.append("location", "巴黎");
				oReq.append("priceTag", "56E");
				break;
		}
		client.postUrl("searchTrip", "/module", oReq);
	}

}
