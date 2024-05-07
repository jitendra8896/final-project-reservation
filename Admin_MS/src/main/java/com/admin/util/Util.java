package com.admin.util;

import java.util.Optional;

public class Util {

	public static boolean isNullEmpty(String str) {
		String s = Optional.ofNullable(str).orElse("");
		boolean isEmpty=true;
		if (!s.trim().isEmpty()) {
			isEmpty=false;
		}
		return isEmpty;
	}
}
