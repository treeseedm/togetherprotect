package org.x.mobile;

public class Const {

	public String currencyNameBy(int type) {
		switch (type) {
		case 1:
			return "€";
		case 2:
			return "$";
		case 3:
			return "£";
		case 4:
			return "円";
		case 5:
			return "₩";
		case 6:
			return "฿";
		case 7:
			return "C";
		case 8:
			return "＄A";
		case 9:
			return "S$";
		case 10:
			return "RM";
		case 11:
			return "C$";
		default:
			return "￥";
		}
	}

	public String currencyName(int type) {
		switch (type) {
		case 1:
			return "欧元";
		case 2:
			return "美元";
		case 3:
			return "英镑";
		case 4:
			return "日元";
		case 5:
			return "韩币";
		case 6:
			return "泰铢";
		case 7:
			return "泰铢";
		case 8:
			return "澳币";
		case 9:
			return "新币";
		case 10:
			return "令吉";
		case 11:
			return "加元";
		default:
			return "人民币";
		}
	}

}
