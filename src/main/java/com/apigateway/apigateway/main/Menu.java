package com.apigateway.apigateway.main;

import com.apigateway.apigateway.main.controller.Callback;
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

/**
 * Classe responsável por exibir e gerenciar o menu principal da aplicação de integração com API Gateway.
 * <p>
 * Permite ao usuário realizar operações de pagamento (débito, crédito, pix), cancelamento de transação
 * e consulta do status das transações realizadas. Utiliza injeção de dependências do Spring para acessar
 * os serviços de transação e o payload das operações.
 * <p>
 * Fluxo principal:
 * <ul>
 *   <li>Exibe o menu inicial para o usuário escolher entre pagar ou cancelar uma transação.</li>
 *   <li>Direciona para menus específicos de acordo com a escolha do usuário.</li>
 *   <li>Realiza chamadas de serviço para processar pagamentos ou cancelamentos.</li>
 *   <li>Permite consultar o resultado da ultima transação.</li>
 * </ul>
 */
@Component
public class Menu {
    @Autowired
    private Services services;

    @Autowired
    private Payload payload;

    @Autowired
    private Callback callback;

    Scanner sc = new Scanner(System.in);
    int selectedOption;

    public void mainMenu() throws IOException, InterruptedException {
        do {
            System.out.print("1) Pagar  2) Cancelar transação\nDigite a opção: ");
            selectedOption = sc.nextInt();

            switch (selectedOption) {
                case 1:
                    this.paymentTypes(Command.PAYMENT);
                    break;
                case 2:
                    this.cancellment(Command.CANCELLMENT);
                    this.getTransaction();
                    break;
                default:
                    System.out.print("\nOpção inválida.");
            }
        } while (selectedOption != 3);
    }

    private void paymentTypes(Command command) throws IOException, InterruptedException {
        do {
            System.out.print("1) Debito   2) Credito  3) Pix  4) Voltar\nDigite a opção: ");
            selectedOption = sc.nextInt();

            switch (selectedOption) {
                case 1:
                    this.debit(command);
                    break;
                case 2:
                    this.credit(command);
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
            this.getTransaction();
        } while (selectedOption != 4);
    }

    private void getTransaction() throws IOException, InterruptedException {
        callback.startPolling();
        int selectedOption = -1;
        do {
            if (payload.getPayload() == null || payload.getPayload().isEmpty()) {
                System.out.println("processando transacao...");
                Thread.sleep(2000);
                continue;
            }

            System.out.print("1) Consultar transação efetuada   0) Voltar\nDigite a opção: ");
            selectedOption = sc.nextInt();

            switch (selectedOption) {
                case 1:
                    System.out.println("########## PAYLOAD ##########\n" + payload.getPayload() + "\n");
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior...\n");
                    break;
                default:
                    System.out.println("Opção inválida\n");
            }
        } while (selectedOption != 0);
    }

    private float setValue() {
        sc.nextLine();
        System.out.print("Digite o valor desejado: ");
        float value = sc.nextFloat();
        return value;
    }

    private void debit(Command command) throws IOException, InterruptedException {
        float value = setValue();
        services.transactionRequest(command, value, PaymentMethod.CARD, PaymentType.DEBIT, PaymentMethodSubType.FULL_PAYMENT);
    }

    private void credit(Command command) throws IOException, InterruptedException {
        float value = setValue();
        System.out.print("1) A vista  2) Parcelado\nDigite a opção: ");
        int selectedOption = sc.nextInt();

        if (selectedOption == 1)
            services.transactionRequest(command, value, PaymentMethod.CARD, PaymentType.CREDIT, PaymentMethodSubType.FULL_PAYMENT);
        else if (selectedOption == 2)
            services.transactionRequest(command, value, PaymentMethod.CARD, PaymentType.CREDIT, PaymentMethodSubType.FINANCED_NO_FEES);
        else
            System.out.println("Opção inválida.");
    }

    private void pix(Command command) throws IOException, InterruptedException {
        float value = setValue();
        services.transactionRequest(command, value, PaymentMethod.PIX, PaymentType.DEBIT, PaymentMethodSubType.FULL_PAYMENT);
    }

    private void cancellment(Command command) throws IOException, InterruptedException {
        //Todos parametros exceto o command serão ignorados
        services.transactionRequest(command, 0, PaymentMethod.PIX, PaymentType.DEBIT, PaymentMethodSubType.FINANCED_NO_FEES);
    }
}
