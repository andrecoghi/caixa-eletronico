package br.com.caixa.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.caixa.domain.Deposito;
import br.com.caixa.domain.Lancamento;
import br.com.caixa.domain.NomeCedula;
import br.com.caixa.domain.SaldoCedula;
import br.com.caixa.domain.Saque;
import br.com.caixa.utils.DateUtil;

public class CaixaEletronicoService {

	private static CaixaEletronicoService INSTANCE;

	private CaixaEletronicoService() {
	}

	public synchronized static CaixaEletronicoService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CaixaEletronicoService();
		}
		return INSTANCE;
	}

	private final Map<NomeCedula, Integer> caixa = new HashMap<>();

	private final LinkedList<Lancamento> lancamentos = new LinkedList<>();

	private SelecionaCedulasService selecionaCedulasService = SelecionaCedulasService.getInstance();

	public synchronized void realizaDeposito(Integer qdte, Integer valorCedula) {

		NomeCedula cedula = selecionaCedulasService.nomeCedula(valorCedula);

		if (!caixa.containsKey(cedula)) {
			caixa.put(cedula, qdte);
		} else {
			caixa.put(cedula, caixa.get(cedula) + qdte);
		}

		Integer valorDeposito = qdte * valorCedula;

		registraLancamento(valorDeposito, new Deposito());

		System.out.print("Deposito realizado com sucesso!");
	}

	public synchronized List<SaldoCedula> realizaSaque(Integer valorSaque) {
		
		Map<NomeCedula, Integer> mapCelulasSaque = selecionaCelulas(valorSaque);

		final List<SaldoCedula> saldoCedulas = new ArrayList<>();

		for (NomeCedula cedula : mapCelulasSaque.keySet()) {
			caixa.computeIfPresent(cedula, (key, value) -> {
				if (mapCelulasSaque.get(cedula) <= value)
					return value = value - mapCelulasSaque.get(cedula);
				return value;
			});
		}
		// monta a lista com as cédulas que o usuário sacou
		mapCelulasSaque.forEach((k, v) -> {
			SaldoCedula cedula = new SaldoCedula();
			cedula.setNome(k);
			cedula.setValor(Double.valueOf(k.valor()));
			cedula.setQtdeCedula(v);
			saldoCedulas.add(cedula);
		});
		System.out.print("Saque realizado com sucesso!");
		return saldoCedulas;
	}

	/**
	 * Recupera as cedulas que serão usadas no saque de acordo com o valor de saque
	 * e disponibilidade no caixa
	 * 
	 * @param valorSaque
	 * @param caixa
	 * @return Map<NomeCedula, Integer>
	 */
	private Map<NomeCedula, Integer> selecionaCelulas(Integer valorSaque) {

		Map<NomeCedula, Integer> cedulas = selecionaCedulasService.cedulas(valorSaque, caixa);

		registraLancamento(valorSaque, new Saque());

		return cedulas;
	}

	private void registraLancamento(Integer valor, Lancamento lancamento) {
		lancamento.setData(Calendar.getInstance());
		lancamento.setValor(valor);
		if (lancamento instanceof Saque) {
			((Saque) lancamento).setOperacao("SAQUE");
		} else if (lancamento instanceof Deposito) {
			((Deposito) lancamento).setOperacao("DEPOSITO");
		}
		lancamentos.add(lancamento);
	}

	protected int getSaldoCaixa() {
		Set<NomeCedula> cedulas = caixa.keySet();
		int valorTotal = 0;

		if (!caixa.isEmpty())
			for (NomeCedula nomeCedula : cedulas) {
				if(nomeCedula != null)
					valorTotal = valorTotal + (Integer.valueOf(nomeCedula.valor()) * caixa.get(nomeCedula));
			}
		return valorTotal;
	}

	public boolean validaSaldo(int valorSaque) {

		int saldo = getSaldoCaixa();

		if (valorSaque > saldo) {
			System.out.println("===========================================");
			System.out.println("O caixa não possui saldo suficiente!");
			System.out.println("O limite de saque disponível é de: " + saldo);
			return false;
		}
		return true;
	}

	public void exibirExtrato() {
		System.out.println("==========================================");
		System.out.printf("%10s %10s %10s", "OPERACÃO", "VALOR", "DATA");
		System.out.println();
		System.out.println("==========================================");
		lancamentos.forEach((lancamento) -> {
			if (lancamento instanceof Saque) {
				System.out.format("%10s %9d %22s", ((Saque) lancamento).getOperacao(), lancamento.getValor(),
						DateUtil.format(lancamento.getData().getTime()));
				System.out.println();
			} else if (lancamento instanceof Deposito) {
				System.out.format("%10s %9d %22s", ((Deposito) lancamento).getOperacao(), lancamento.getValor(),
						DateUtil.format(lancamento.getData().getTime()));
				System.out.println();
			}
		});

		System.out.println("==========================================");
		System.out.println("		Saldo atual: " + getSaldoCaixa());
		System.out.println("==========================================");
	}

	public void exibirSaldo() {
		int saldoCaixa = getSaldoCaixa();
		if (saldoCaixa > 0) {
			System.out.println("====================================");
			System.out.printf("%20s %10s", "CEDULAS DISPONÍVEIS | ", "QUANTIDADE");
			System.out.println();
			System.out.println("====================================");
			caixa.forEach((k, v) -> {
				System.out.format("%12s %14d", k.valor(), v);
				System.out.println();
			});
		}
		System.out.println("====================================");
		System.out.println("	Saldo atual: " + getSaldoCaixa());
		System.out.println("====================================");
	}

	public boolean validaCelula(Integer valorCedula) {
		NomeCedula cedula = selecionaCedulasService.nomeCedula(valorCedula);
		if (cedula == null) {
			selecionaCedulasService.exibirCedulasValidas();
			return false;
		}
		return true;
	}
}
