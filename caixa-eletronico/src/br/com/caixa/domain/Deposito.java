package br.com.caixa.domain;

public class Deposito extends Lancamento {
	private String operacao;

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
}
