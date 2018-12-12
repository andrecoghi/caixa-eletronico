package br.com.caixa.domain;

import java.util.HashMap;
import java.util.Map;

public enum NomeCedula {
	_100("100"), _50("50"), _20("20"), _10("10"), _5("5"), _2("2");

	private static final Map<String, NomeCedula> CEDULAS = new HashMap<String, NomeCedula>();

	static {
		for (NomeCedula cedulas : values()) {
			CEDULAS.put(cedulas.valor(), cedulas);
		}
	}

	private final String valor;

	private NomeCedula(String valor) {
		this.valor = valor;
	}

	public static NomeCedula getByValue(String value) {
		return CEDULAS.get(value);
	}

	public String valor() {
		return this.valor;
	}
}
