package br.com.caixa.domain;

import java.util.Calendar;

public class Lancamento {

	private Calendar data;

	private Integer valor;

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

}
