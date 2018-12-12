package br.com.caixa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.caixa.domain.NomeCedula;
/**
 * Classe reponsável pelos serviços de selecionar as cédulas
 * corretas durante o saque de acordo com o valor solicitado
 * e com a disponibilidade de cédulas no caixa
 * 
 * @author andre
 *
 */
public class SelecionaCedulasService {

	private static SelecionaCedulasService INSTANCE;

	private SelecionaCedulasService() {
	}

	public synchronized static SelecionaCedulasService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SelecionaCedulasService();
		}
		return INSTANCE;
	}

	protected Map<NomeCedula, Integer> cedulas(int valor, Map<NomeCedula, Integer> caixa) {

		final Map<NomeCedula, Integer> cedulas = new HashMap<>();

		selecionarCedulas(valor, caixa, cedulas);

		return cedulas;
	}

	private void selecionarCedulas(int valor, Map<NomeCedula, Integer> caixa, final Map<NomeCedula, Integer> cedulas) {

		int contador = 0;

		for (Integer cedula : obterCedulasCaixa(caixa)) {
			if (valor >= cedula) {
				contador = valor / cedula;
				valor = valor - contador * cedula;
				cedulas.put(nomeCedula(cedula), contador);

			}
		}

		if (valor != 0) {
			exibirCedulasDisponiveis(caixa);
			return;
		}
	}

	private List<Integer> obterCedulasCaixa(Map<NomeCedula, Integer> caixa) {
		final List<Integer> cedulasCaixa = new ArrayList<>();

		final List<NomeCedula> cedulas = Arrays.stream(NomeCedula.values()).collect(Collectors.toList());

		cedulas.forEach(cedula -> {
			if (caixa.get(cedula) != null)
				cedulasCaixa.add(Integer.valueOf(cedula.valor()));
		});

		return cedulasCaixa;
	}

	protected NomeCedula nomeCedula(Integer valorCedula) {
		return NomeCedula.getByValue(String.valueOf(valorCedula));

	}

	protected void exibirCedulasDisponiveis(Map<NomeCedula, Integer> caixa) {
		System.out.println("==========================================");
		System.out.printf("%20s", "CEDULAS DISPONÍVEIS");
		System.out.println();
		System.out.println("==========================================");
		caixa.forEach((k, v) -> {
			System.out.format("%12s", k.valor());
			System.out.println();
		});
		System.out.println("==========================================");
	}

	public void exibirCedulasValidas() {
		StringBuilder builder = new StringBuilder();
		System.out.println("===========================");
		System.out.println("CEDULAS VÁLIDAS PARA DEPÓSITO");
		System.out.println();
		System.out.println("===========================");
		final List<NomeCedula> cedulas = Arrays.stream(NomeCedula.values()).collect(Collectors.toList());
		int i = 0;
		for (NomeCedula cedula : cedulas) {
			builder.append(cedula.valor());
			if (i++ != cedulas.size() - 1) {
				builder.append(" | ");
			}
		}
		System.out.println(builder.toString());
		System.out.println("===========================");
	}
}