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
                    this.menuDois("payment");
                    break;
                case 2:
                    this.cancelamento("cancellment");
                    break;
                default:
                    System.out.print("\nOpção inválida.");
            }
        } while (opcao != 3);
    }

    private void menuDois(String command) throws IOException, InterruptedException {
        do {
            System.out.println("1) Debito   2) Credito  3) Pix  4) Voltar");
            System.out.print("Digite a opção: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    this.debito(command);
                    this.consultaTransacao();
                    break;
                case 2:
                    this.credito(command);
                    this.consultaTransacao();
                    break;
                case 3:
                    this.pix(command);
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
        int opcao = -1;
        do {
            if (manipulacaoPayload.getPayload() == null || manipulacaoPayload.getPayload().isEmpty()) {
                System.out.println("Aguardando transação... Por favor, aguarde.");
                // Aguarda um tempo antes de mostrar novamente (ex: 2 segundos)
                Thread.sleep(2000);
                continue; // volta para o início do loop
            }

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

    private void debito(String command) throws IOException, InterruptedException {
        float value = capturaValor();
        services.transactionRequest(command, value, "CARD", "DEBIT", "FULL_PAYMENT");
    }

    private void credito(String command) throws IOException, InterruptedException {
        float value = capturaValor();
        System.out.print("1) A vista  2) Parcelado");
        int opcao = sc.nextInt();

        if (opcao == 1)
            services.transactionRequest(command, value, "CARD", "CREDIT", "FULL_PAYMENT");
        else if (opcao == 2)
            services.transactionRequest(command, value, "CARD", "CREDIT", "FINANCED_NO_FEES");
        else
            System.out.println("Opção inválida.");
    }

    private void pix(String command) throws IOException, InterruptedException {
        float value = capturaValor();
        services.transactionRequest(command, value, "PIX", "DEBIT", "FULL_PAYMENT");
    }

    private void cancelamento(String command) throws IOException, InterruptedException {
        services.transactionRequest(command, 0, "c", "e", "d");
        this.consultaTransacao();
    }
}
