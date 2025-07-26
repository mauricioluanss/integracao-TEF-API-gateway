package com.apigateway.apigateway.main;

import com.apigateway.apigateway.main.entity.Payload;
import com.apigateway.apigateway.main.enums.parametrosPagamento.Command;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentMethod;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentMethodSubType;
import com.apigateway.apigateway.main.enums.parametrosPagamento.PaymentType;
import com.apigateway.apigateway.main.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;


@Component
public class Menu {
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
                    this.metodosPagamento(Command.PAYMENT);
                    break;
                case 2:
                    this.cancelamento(Command.CANCELLMENT);
                    this.consultaTransacao();
                    break;
                default:
                    System.out.print("\nOpção inválida.");
            }
        } while (opcao != 3);
    }

    private void metodosPagamento(Command command) throws IOException, InterruptedException {
        do {
            System.out.println("1) Debito   2) Credito  3) Pix  4) Voltar");
            System.out.print("Digite a opção: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    this.debito(command);
                    break;
                case 2:
                    this.credito(command);
                    break;
                case 3:
                    this.pix(command);
                    break;
                case 4:
                    System.out.println("Voltando ao menu anterior...");
                    return;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }
            this.consultaTransacao();
        } while (opcao != 4);
    }

    private void consultaTransacao() throws IOException, InterruptedException {
        int opcao = -1;
        do {
            if (manipulacaoPayload.getPayload() == null || manipulacaoPayload.getPayload().isEmpty()) {
                System.out.println("processando transacao.");
                Thread.sleep(2000);
                continue;
            }

            System.out.println("1) Consultar transação efetuada   0) Voltar:");
            System.out.print("Digite a opção: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("<<<---  PAYLOAD --->>>");
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

    private void debito(Command command) throws IOException, InterruptedException {
        float value = capturaValor();
        services.transactionRequest(command, value, PaymentMethod.CARD, PaymentType.DEBIT, PaymentMethodSubType.FULL_PAYMENT);
    }

    private void credito(Command command) throws IOException, InterruptedException {
        float value = capturaValor();
        System.out.println("1) A vista  2) Parcelado");
        int opcao = sc.nextInt();

        if (opcao == 1)
            services.transactionRequest(command, value, PaymentMethod.CARD, PaymentType.CREDIT, PaymentMethodSubType.FULL_PAYMENT);
        else if (opcao == 2)
            services.transactionRequest(command, value, PaymentMethod.CARD, PaymentType.CREDIT, PaymentMethodSubType.FINANCED_NO_FEES);
        else
            System.out.println("Opção inválida.");
    }

    private void pix(Command command) throws IOException, InterruptedException {
        float value = capturaValor();
        services.transactionRequest(command, value, PaymentMethod.PIX, PaymentType.DEBIT, PaymentMethodSubType.FULL_PAYMENT);
    }

    private void cancelamento(Command command) throws IOException, InterruptedException {
        //Todos parametros exceto o command serão ignorados
        services.transactionRequest(command, 0, PaymentMethod.PIX, PaymentType.DEBIT, PaymentMethodSubType.FINANCED_NO_FEES);
    }
}
