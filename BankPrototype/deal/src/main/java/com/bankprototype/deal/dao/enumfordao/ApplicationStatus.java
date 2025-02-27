package com.bankprototype.deal.dao.enumfordao;

/**
 * PREAPPROVAL - Предварительное одобрение при создании
 * CLIENT_DENIED - Отказ клиента
 * APPROVED - Выбран один из 4 предложенных офера
 * CC_DENIED - Ошибка скоринга
 * CC_APPROVED - Переход к оформлению документов
 * PREPARE_DOCUMENTS - формирование документов
 * DOCUMENT_CREATED - документы созданы и отправлены на почту пользователю
 * DOCUMENT_SIGNED - Документы подписаны
 * CREDIT_ISSUED - кредит выдан
 */
public enum ApplicationStatus {
    PREAPPROVAL,
    APPROVED,
    CC_DENIED,
    CC_APPROVED,
    PREPARE_DOCUMENTS,
    DOCUMENT_CREATED,
    CLIENT_DENIED,
    DOCUMENT_SIGNED,
    CREDIT_ISSUED
}
