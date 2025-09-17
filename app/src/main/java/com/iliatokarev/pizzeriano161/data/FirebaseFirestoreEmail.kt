package com.iliatokarev.pizzeriano161.data

import com.iliatokarev.pizzeriano161.domain.order.OrderData
import java.util.UUID

class FirebaseFirestoreEmail() : FirebaseFirestoreEmailInterface, FirebaseFirestore() {

    private fun createFirebaseEmailData(
        orderData: OrderData
    ): FirebaseMessageData {
        return FirebaseMessageData(
            to = listOf(orderData.consumerEmail),
            message = MessageData(
                subject = MESSAGE_SUBJECT,
                text = createMessageEmailText(orderData),
                html = createMessageEmailHTML(orderData),
            )
        )
    }

    private fun createFirebaseConfirmationEmailData(
        orderData: OrderData
    ): FirebaseMessageData {
        return FirebaseMessageData(
            to = listOf(orderData.consumerEmail),
            message = MessageData(
                subject = CONFIRMATION_MESSAGE_SUBJECT,
                text = createConfirmationMessageEmailText(orderData),
                html = createConfirmationEmailHTML(orderData),
            )
        )
    }

    private fun createFirebaseRejectionEmailData(
        orderData: OrderData,
        rejectionReason: String
    ): FirebaseMessageData {
        return FirebaseMessageData(
            to = listOf(orderData.consumerEmail),
            message = MessageData(
                subject = REJECTION_MESSAGE_SUBJECT,
                text = createRejectionMessageEmailText(orderData, rejectionReason),
                html = createRejectedEmailHtml(orderData, rejectionReason),
            )
        )
    }

    private fun createRejectedEmailHtml(orderData: OrderData, rejectionReason: String): String {
        return "<!doctype html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
                "  </head>\n" +
                "  <body style=\"margin:0;padding:0;background:#f4f4f6;font-family:Arial,Helvetica,sans-serif;color:#222;\">\n" +
                "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "      <tr>\n" +
                "        <td align=\"center\" style=\"padding:24px 12px;\">\n" +
                "          <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.06);\">\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:20px 24px;background:#d32f2f;color:#ffffff;\">\n" +
                "                <h1 style=\"margin:0;font-size:20px;\">Pizzeria No 161</h1>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:20px 24px;\">\n" +
                "                <p style=\"margin:0 0 12px;font-size:16px;\">Здравствуйте, <strong>${orderData.consumerName}</strong>!</p>\n" +
                "\n" +
                "                <p style=\"margin:0 0 18px;font-size:14px;color:#444;\">\n" +
                "                  К сожалению, мы не сможем выполнить ваш заказ, по причине: ${rejectionReason}.\n" +
                "                </p>\n" +
                "\n" +
                "                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" role=\"presentation\" style=\"margin-top:12px;\">\n" +
                "                  <tr>\n" +
                "                    <td style=\"padding-right:12px;\" valign=\"top\">\n" +
                "                      <img src=\"${MESSAGE_LOGO}\" alt=\"Paolo Pizzaiolo\" width=\"72\" style=\"border-radius:8px;display:block;\" />\n" +
                "                    </td>\n" +
                "                    <td valign=\"middle\" style=\"font-family:Arial,Helvetica,sans-serif;font-size:14px;color:#333;\">\n" +
                "                      <div style=\"font-weight:700;\">Paolo Pizzaiolo</div>\n" +
                "                      <div style=\"font-size:13px;color:#777;\">Pizzeria Numero 161 - Vera Pizza Italiana</div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:12px 24px;background:#fafafa;border-top:1px solid #eee;font-size:12px;color:#888;\">\n" +
                "                <div style=\"margin-top:6px;\">Если у вас возникли вопросы — ответьте на это письмо или свяжитесь с нами по телефону +7 (903) 739-77-00.</div>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n"
    }

    private fun createConfirmationEmailHTML(orderData: OrderData): String {
        return "<!doctype html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
                "  </head>\n" +
                "  <body style=\"margin:0;padding:0;background:#f4f4f6;font-family:Arial,Helvetica,sans-serif;color:#222;\">\n" +
                "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "      <tr>\n" +
                "        <td align=\"center\" style=\"padding:24px 12px;\">\n" +
                "          <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.06);\">\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:20px 24px;background:#d32f2f;color:#ffffff;\">\n" +
                "                <h1 style=\"margin:0;font-size:20px;\">Pizzeria No 161</h1>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:20px 24px;\">\n" +
                "                <p style=\"margin:0 0 12px;font-size:16px;\">Здравствуйте, <strong>${orderData.consumerName}</strong>!</p>\n" +
                "\n" +
                "                <p style=\"margin:0 0 18px;font-size:14px;color:#444;\">\n" +
                "                  Мы подтверждаем, что выполним ваш заказ:\n" +
                "                </p>\n" +
                "\n" +
                "                <p style=\"margin:0 0 10px;font-size:15px;color:#222;\">\n" +
                "                  <strong>Заказ:</strong> ${orderData.pizzaList.joinToString(", ")}\n" +
                "                </p>\n" +
                "                <p style=\"margin:0 0 10px;font-size:15px;color:#222;\">\n" +
                "                  <strong>Сумма заказа:</strong> ${orderData.sum} ₽\n" +
                "                </p>\n" +
                "                <p style=\"margin:0 0 18px;font-size:15px;color:#222;\">\n" +
                "                  <strong>Время приготовления:</strong> ${orderData.time}\n" +
                "                </p>\n" +
                "                <p style=\"margin:0 0 18px;font-size:15px;color:#222;\">\n" +
                "                  <strong>Комментарий:</strong> ${orderData.additionalInfo.ifBlank { "Не указан" }}\n" +
                "                </p>\n" +
                "\n" +
                "                <p style=\"margin:18px 0 6px;font-size:13px;color:#666;\">Спасибо за ваш заказ!</p>\n" +
                "\n" +
                "                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" role=\"presentation\" style=\"margin-top:12px;\">\n" +
                "                  <tr>\n" +
                "                    <td style=\"padding-right:12px;\" valign=\"top\">\n" +
                "                      <img src=\"${MESSAGE_LOGO}\" alt=\"Paolo Pizzaiolo\" width=\"72\" style=\"border-radius:8px;display:block;\" />\n" +
                "                    </td>\n" +
                "                    <td valign=\"middle\" style=\"font-family:Arial,Helvetica,sans-serif;font-size:14px;color:#333;\">\n" +
                "                      <div style=\"font-weight:700;\">Paolo Pizzaiolo</div>\n" +
                "                      <div style=\"font-size:13px;color:#777;\">Pizzeria Numero 161 - Vera Pizza Italiana</div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:12px 24px;background:#fafafa;border-top:1px solid #eee;font-size:12px;color:#888;\">\n" +
                "                <div style=\"margin-top:6px;\">Если у вас возникли вопросы — ответьте на это письмо или свяжитесь с нами по телефону +7 (903) 739-77-00.</div>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n"
    }

    private fun createMessageEmailHTML(orderData: OrderData): String {
        return "<!doctype html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
                "  </head>\n" +
                "  <body style=\"margin:0;padding:0;background:#f4f4f6;font-family:Arial,Helvetica,sans-serif;color:#222;\">\n" +
                "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "      <tr>\n" +
                "        <td align=\"center\" style=\"padding:24px 12px;\">\n" +
                "          <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.06);\">\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:20px 24px;background:#d32f2f;color:#ffffff;\">\n" +
                "                <h1 style=\"margin:0;font-size:20px;\">Pizzeria No 161</h1>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:20px 24px;\">\n" +
                "                <p style=\"margin:0 0 12px;font-size:16px;\">Здравствуйте, <strong>${orderData.consumerName}</strong>!</p>\n" +
                "\n" +
                "                <p style=\"margin:0 0 18px;font-size:14px;color:#444;\">\n" +
                "                  Мы получили ваш заказ:\n" +
                "                </p>\n" +
                "\n" +
                "                <p style=\"margin:0 0 10px;font-size:15px;color:#222;\">\n" +
                "                  <strong>Заказ:</strong> ${orderData.pizzaList.joinToString(", ")}\n" +
                "                </p>\n" +
                "                <p style=\"margin:0 0 10px;font-size:15px;color:#222;\">\n" +
                "                  <strong>Сумма заказа:</strong> ${orderData.sum} ₽\n" +
                "                </p>\n" +
                "                <p style=\"margin:0 0 18px;font-size:15px;color:#222;\">\n" +
                "                  <strong>Время приготовления:</strong> ${orderData.time}\n" +
                "                </p>\n" +
                "                <p style=\"margin:0 0 18px;font-size:15px;color:#222;\">\n" +
                "                  <strong>Комментарий:</strong> ${orderData.additionalInfo.ifBlank { "не указан" }}\n" +
                "                </p>\n" +
                "\n" +
                "                <p style=\"margin:18px 0 6px;font-size:13px;color:#666;\">Спасибо за ваш заказ!</p>\n" +
                "\n" +
                "                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" role=\"presentation\" style=\"margin-top:12px;\">\n" +
                "                  <tr>\n" +
                "                    <td style=\"padding-right:12px;\" valign=\"top\">\n" +
                "                      <img src=\"${MESSAGE_LOGO}\" alt=\"Paolo Pizzaiolo\" width=\"72\" style=\"border-radius:8px;display:block;\" />\n" +
                "                    </td>\n" +
                "                    <td valign=\"middle\" style=\"font-family:Arial,Helvetica,sans-serif;font-size:14px;color:#333;\">\n" +
                "                      <div style=\"font-weight:700;\">Paolo Pizzaiolo</div>\n" +
                "                      <div style=\"font-size:13px;color:#777;\">Pizzeria Numero 161 - Vera Pizza Italiana</div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:12px 24px;background:#fafafa;border-top:1px solid #eee;font-size:12px;color:#888;\">\n" +
                "                <div style=\"margin-top:6px;\">Если у вас возникли вопросы — ответьте на это письмо или свяжитесь с нами по телефону +7 (903) 739-77-00.</div>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n"
    }

    private fun createMessageEmailText(orderData: OrderData): String {
        return "Здравствуйте, ${orderData.consumerName}!\n" +
                "\n" +
                "Мы получили ваш заказ:\n" +
                "\n" +
                "Заказ: ${orderData.pizzaList.joinToString(", ")}\n" +
                "Сумма: ${orderData.sum} ₽\n" +
                "Время: ${orderData.time}\n" +
                "Комментарий: ${orderData.additionalInfo.ifBlank { "не указан" }}\n" +
                "\n" +
                "Через некоторое время мы подтвердим сможем ли мы выполнить ваш заказ, и отправим уведомление на вашу почту.\n" +
                "\n" +
                "Спасибо за ваш заказ!\n" +
                "\n" +
                "Paolo Pizzaiolo\n" +
                "Pizzeria Numerо 161 - Vera Pizza Italiana"
        "\n" +
                "Если у вас возникли вопросы — ответьте на это письмо или свяжитесь с нами по телефону +7 (903) 739-77-00."
    }

    private fun createConfirmationMessageEmailText(orderData: OrderData): String {
        return "Здравствуйте, ${orderData.consumerName}!\n" +
                "\n" +
                "Мы подтверждаем, что выполним ваш заказ:\n" +
                "\n" +
                "Заказ: ${orderData.pizzaList.joinToString(", ")}\n" +
                "Сумма: ${orderData.sum} ₽\n" +
                "Время: ${orderData.time}\n" +
                "Комментарий: ${orderData.additionalInfo.ifBlank { "не указан" }}\n" +
                "\n" +
                "Спасибо за ваш заказ!\n" +
                "\n" +
                "Paolo Pizzaiolo\n" +
                "Pizzeria Numerо 161 - Vera Pizza Italiana"
        "\n" +
                "Если у вас возникли вопросы — ответьте на это письмо или свяжитесь с нами по телефону +7 (903) 739-77-00."
    }

    private fun createRejectionMessageEmailText(
        orderData: OrderData,
        rejectionReason: String
    ): String {
        return "Здравствуйте, ${orderData.consumerName}!\n" +
                "\n" +
                "К сожалению, мы не сможем выполнить ваш заказ, по причине: ${rejectionReason}.\n" +
                "\n" +
                "Paolo Pizzaiolo\n" +
                "Pizzeria Numerо 161 - Vera Pizza Italiana"
        "\n" +
                "Если у вас возникли вопросы — ответьте на это письмо или свяжитесь с нами по телефону +7 (903) 739-77-00."
    }

    override suspend fun uploadMessageData(
        collectionPath: String,
        orderData: OrderData
    ) {
        return setDocumentData<FirebaseMessageData>(
            data = createFirebaseEmailData(orderData),
            collectionPath = collectionPath,
            documentName = UUID.randomUUID().toString(),
        )
    }

    override suspend fun uploadConfirmationMessageData(
        collectionPath: String,
        orderData: OrderData
    ) {
        return setDocumentData<FirebaseMessageData>(
            data = createFirebaseConfirmationEmailData(orderData),
            collectionPath = collectionPath,
            documentName = UUID.randomUUID().toString(),
        )
    }

    override suspend fun uploadRejectionMessageData(
        collectionPath: String,
        orderData: OrderData,
        rejectionReason: String
    ) {
        return setDocumentData<FirebaseMessageData>(
            data = createFirebaseRejectionEmailData(orderData, rejectionReason),
            collectionPath = collectionPath,
            documentName = UUID.randomUUID().toString(),
        )
    }
}

private const val MESSAGE_COLLECTION_PATH = "mail"
private const val MESSAGE_SUBJECT = "Заказ получен"
private const val CONFIRMATION_MESSAGE_SUBJECT = "Заказ подтвержден"
private const val REJECTION_MESSAGE_SUBJECT = "Заказ отклонен"
private const val MESSAGE_LOGO =
    "https://firebasestorage.googleapis.com/v0/b/pizzeria-161.firebasestorage.app/o/pizzeria_161-playstore%20-%20round%20-%20280-280.png?alt=media&token=01e43e65-9d0a-423a-bd97-30fc64bd99cc"

interface FirebaseFirestoreEmailInterface {
    suspend fun uploadMessageData(
        collectionPath: String = MESSAGE_COLLECTION_PATH,
        orderData: OrderData,
    )

    suspend fun uploadConfirmationMessageData(
        collectionPath: String = MESSAGE_COLLECTION_PATH,
        orderData: OrderData,
    )

    suspend fun uploadRejectionMessageData(
        collectionPath: String = MESSAGE_COLLECTION_PATH,
        orderData: OrderData,
        rejectionReason: String,
    )
}