package br.com.caixa;

import java.util.Scanner;

import br.com.caixa.service.CaixaEletronicoService;

public class MenuPrincipal {

	public static void main(String[] args) {

		Scanner console = new Scanner(System.in);

		CaixaEletronicoService caixa = CaixaEletronicoService.getInstance();

		boolean sair = false;
		do {
			System.out.println("============================");
			System.out.println("|   O que você quer fazer? |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. Saldo          |");
			System.out.println("|        2. Depositar      |");
			System.out.println("|        3. Sacar     	   |");
			System.out.println("|        4. Extrato        |");
			System.out.println("|        5. Sair           |");
			System.out.println("============================");
			System.out.print("Sua opção: ");
			String opcao = console.nextLine();
			switch (opcao) {
			
			case "1": {
				try {
					caixa.exibirSaldo();
				} catch (IllegalArgumentException ex) {
					System.out.println("Valor inválido!");
				}
				break;
			}
			
			case "2": {
				try {
					System.out.print("Informe o número de cedulas? ");
					Integer qtde = Integer.parseInt(console.nextLine());
					
					System.out.print("Qual valor da cedula? ");
					Integer valor = Integer.parseInt(console.nextLine());

					if(caixa.validaCelula(valor))
						caixa.realizaDeposito(qtde, valor);
					
				} catch (IllegalArgumentException ex) {
					System.out.println("Valor inválido!");
				}
				break;
			}
			
			case "3": {
				try {
					System.out.print("Informe o valor? ");
					Integer valor = Integer.parseInt(console.nextLine());
					
					if(caixa.validaSaldo(valor))
						caixa.realizaSaque(valor);
				} catch (IllegalArgumentException ex) {
					System.out.println("Valor inválido!");
				}
				break;
			}
			
			case "4": {
				try {
					caixa.exibirExtrato();
				} catch (IllegalArgumentException ex) {
					System.out.println("Valor inválido!");
				}
				break;
			}
			
			case "5":
				sair = true;
				console.close();
				System.exit(0);
				break;
			default:
			}
			System.out.println();
		} while (!sair);
	}
}
