package com.apigateway.apigateway.main;

import com.apigateway.apigateway.main.entity.Payload;
import com.apigateway.apigateway.main.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

/**
 * Classe que contém os menus de interação com o usuário.
 * Os metodos são bem simples e auto-explicativos.
 */
@Component
public class MenusDeOpcoes {
    @Autowired
    private Services services;

    @Autowired
    private Payload manipulacaoPayload;

    Scanner sc = new Scanner(System.in);
    int opcao;

    public void menuPrincipal() throws IOException, InterruptedException {
        do {
            System.out.println("1) Pagar  2) Cancelar transação");
            System.out.print("Digite a opção: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    this.menuDois();
                    break;
                case 2:
                    this.cancelamento();
                    this.consultaTransacao();
                    break;
                default:
                    System.out.print("\nOpção inválida.");
            }
        } while (opcao != 3);
    }

    private void menuDois() throws IOException, InterruptedException {
        do {
            System.out.println("1) Debito   2) Credito  3) Pix  4) Voltar");
            System.out.print("Digite a opção: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    this.debito();
                    this.consultaTransacao();
                    break;
                case 2:
                    this.credito();
                    this.consultaTransacao();
                    break;
                case 3:
                    this.pix();
                    this.consultaTransacao();
                    break;
                case 4:
                    System.out.println("Voltando ao menu anterior...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 4);
    }

    private void consultaTransacao() throws IOException, InterruptedException {
        do {
            System.out.println("1) Consultar transação efetuada   0) Voltar:");
            System.out.print("Digite a opção: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("<<<---  RETORNO DA TRANSAÇÃO --->>>");
                    System.out.println(manipulacaoPayload.getPayload() + "\n");
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior...\n");
                    break;
                default:
                    System.out.println("Opção inválida\n");
            }
        } while (opcao != 0);
    }

    private float capturaValor() {
        sc.nextLine();
        System.out.print("Digite o valor desejado: ");
        float value = sc.nextFloat();
        return value;
    }

    private void debito() throws IOException, InterruptedException {
        float value = capturaValor();
        services.chamaPagamento(value, "CARD", "DEBIT", "FULL_PAYMENT");
    }

    private void credito() throws IOException, InterruptedException {
        float value = capturaValor();
        System.out.print("1) A vista  2) Parcelado");
        int opcao = sc.nextInt();

        if (opcao == 1)
            services.chamaPagamento(value, "CARD", "CREDIT", "FULL_PAYMENT");
        else if (opcao == 2)
            services.chamaPagamento(value, "CARD", "CREDIT", "FINANCED_NO_FEES");
        else
            System.out.println("Opção inválida.");
    }

    private void pix() throws IOException, InterruptedException {
        float value = capturaValor();
        services.chamaPagamento(value, "PIX", "DEBIT", "FULL_PAYMENT");
    }

    private void cancelamento() throws IOException, InterruptedException {
        services.chamaCancelamento();
    }
}
