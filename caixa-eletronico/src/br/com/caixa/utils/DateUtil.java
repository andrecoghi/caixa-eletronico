package br.com.caixa.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String format(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return format.format(date);
	}
}
