package org.x.net.socket;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.x.conf.Const;
import org.x.net.base64.BASE64Decoder;
import org.x.net.base64.BASE64Encoder;
import org.xerial.snappy.Snappy;

import com.mongodb.BasicDBObject;

public class SocketUtil {

	public static byte[] IVKey = "MIXIU.YIQIHI.COM".getBytes();

	public static String toString(byte[] bytes, String encode) {
		if (bytes == null)
			return null;
		try {
			return new String(bytes, encode);
		} catch (Exception e) {
			return new String(bytes);
		}
	}

	public static String newId() {
		return new ObjectId().toString();
	}

	public static String toString(byte[] bytes) {
		return toString(bytes, "utf-8");
	}

	public static byte[] toBytes(String content) {
		try {
			return content.getBytes("utf-8");
		} catch (Exception e) {
			return content.getBytes();
		}
	}

	public static byte[] compress(byte[] orig) {
		try {
			return Snappy.compress(orig);
		} catch (Exception e) {
			return orig;
		}
	}

	public static byte[] uncompress(byte[] compressed) {
		try {
			return Snappy.uncompress(compressed);
		} catch (Exception e) {
			return compressed;
		}
	}

	public static String readString(ByteBuf buffer) {
		return toString(readBytes(buffer));
	}

	public static void writeString(ByteBuf buffer, String content) {
		byte[] bytes = toBytes(content);
		writeBytes(buffer, bytes);
	}

	public static void writeBytes(ByteBuf buffer, byte[] bytes) {
		buffer.writeInt(bytes.length);
		buffer.writeBytes(bytes);
	}

	public static byte[] readBytes(ByteBuf buffer) {
		int size = buffer.readInt();
		return buffer.readBytes(size).array();
	}

	public static byte[] readBytes(ByteBuf source, int length) {
		return source.readBytes(length).array();
	}

	public static String getLocalName() {
		try {
			Runtime run = Runtime.getRuntime();
			Process proc = run.exec("hostname");
			StringWriter writer = new StringWriter();
			IOUtils.copy(proc.getInputStream(), writer, "utf-8");
			String name = StringUtils.trim(writer.toString());
			return name;
		} catch (Exception e) {
			return "unknow";
		}
	}

	public static BasicDBObject getLocal() {
		BasicDBObject oResult = new BasicDBObject().append("name",
				getLocalName());
		Enumeration<NetworkInterface> netInterfaces = null;
		List<BasicDBObject> items = new ArrayList<BasicDBObject>();
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				String name = ni.getName().toLowerCase();
				if (name.startsWith("lo") || name.startsWith("vir")
						|| name.startsWith("vmnet") || name.startsWith("wlan")) {
					continue;
				}
				BasicDBObject oItem = new BasicDBObject();
				oItem.append("name", name);
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress ia = ips.nextElement();
					if (ia instanceof Inet4Address) {
						if (ia.getHostAddress().toString().startsWith("127")) {
							continue;
						} else {
							oItem.append("ip", ia.getHostAddress());
							items.add(oItem);
							break;
						}
					}
				}
			}
			Collections.sort(items, new Comparator<BasicDBObject>() {

				@Override
				public int compare(BasicDBObject o1, BasicDBObject o2) {
					String n1 = o1.getString("name");
					String n2 = o2.getString("name");
					return n1.compareTo(n2);
				}

			});
			if (items == null || items.size() == 0) {
				oResult.append("ip", "unknow").append("mac", "none");
			} else {
				oResult.append("ip", items.get(0).getString("ip")).append(
						"mac", items.get(0).getString("mac"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oResult;
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String encodeBASE64(byte[] bytes) {
		if (bytes == null)
			return null;
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			return encoder.encode(bytes);
		} catch (Exception e) {
			return null;
		}
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String decodeBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

	// 加密
	public static String easyEncrypt(String password, String content) {
		try {
			if (password.length() != 16) {
				return null;
			}
			byte[] raw = password.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] original = cipher.doFinal(content.getBytes("UTF-8"));
			return new BASE64Encoder().encodeBuffer(original).toString();
		} catch (Exception e) {
			return content;
		}
	}

	// 解密
	public static String easyDecrypt(String password, String content) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes("UTF-8"), 0, password.length());
			byte[] rawKey = md.digest();
			SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(rawKey);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
			byte[] bytes = new BASE64Decoder().decodeBuffer(content);
			byte[] encrypted = cipher.doFinal(bytes);
			return new String(encrypted, "utf-8");
		} catch (Exception e) {
			return content;
		}
	}

	public static String hexString(byte[] bytes) {
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int val = ((int) bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static byte[] readBytes(String fileName) {
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			InputStream inStream = new FileInputStream(fileName);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			byte[] data = outStream.toByteArray();
			outStream.close();
			inStream.close();
			return data;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {

		String content = "Hello, world.";
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(content.getBytes("UTF-8"), 0, content.length());
		byte[] rawKey = md.digest();
		System.out.println(hexString(rawKey));

		if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
			System.out.println("BIG_ENDIAN");
		} else {
			System.out.println("LITTLE_ENDIAN");
		}
		String key = "vtMMEi4JinJKLorg";
		System.out.println("====数据提交到服务器加密===");
		for (int i = 0; i < 10; i++) {
			content = "xiaoming" + i;
			System.out.print("明文:" + content + "&&");
			content = SocketUtil.easyEncrypt(key, content);
			System.out.println("密文:" + content);
		}
		System.out.println("====收到服务器数据解密===");
		String[] values = new String[] { "6Zh9Xp0oJ+DDvSfAPcBTlA==",
				"M0NCX3hZWCK588e0SDk2+w==", "5/3nh+CknAlOsYk+b8xokQ==",
				"8w5phYiQDr/+EMpCO2TbXg==", "9BfNUK4vAAdw1QbdH//U6g==",
				"DiQE53CJn7jd9j3ojRhhcw==", "WSV/11UUfBi9bcOqo8Qg8w==",
				"hqAsMufATuKY7CnJIy6vyA==", "ILNG8orEWg6vlcuZrSEmQg==" };
		for (int i = 0; i < values.length; i++) {
			System.out.print("密文:" + values[i]);
			content = SocketUtil.easyDecrypt(key, values[i]);
			System.out.println("&&明文:" + content);
		}

	}
	public static String shortFileName(String fileName) {
		int index = fileName.lastIndexOf("/");
		if (index > 0) {
			fileName = fileName.substring(index + 1, fileName.length());
		}
		return fileName;
	}

}
